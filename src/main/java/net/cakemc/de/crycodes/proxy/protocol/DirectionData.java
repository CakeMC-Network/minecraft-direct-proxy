package net.cakemc.de.crycodes.proxy.protocol;

import com.google.common.collect.Iterables;
import net.cakemc.de.crycodes.proxy.network.packet.AbstractPacket;
import net.cakemc.de.crycodes.proxy.network.packet.BadPacketException;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * The type Direction data.
 */
public final class DirectionData {
    /**
     * The Protocols.
     */
    public final Map<Integer, ProtocolData> protocols = new HashMap<>();

    /**
     * The Protocol phase.
     */
    public final Protocol protocolPhase;
    /**
     * The Direction.
     */
    public final ProtocolVersion.Direction direction;

    /**
     * Instantiates a new Direction data.
     *
     * @param protocolPhase the protocol phase
     * @param direction     the direction
     */
    public DirectionData(Protocol protocolPhase, ProtocolVersion.Direction direction) {
        this.protocolPhase = protocolPhase;
        this.direction = direction;
        for (int protocol : ProtocolVersion.getSupportedVersionIds()) {
            protocols.put(protocol, new ProtocolData(protocol));
        }
    }

    private ProtocolData getProtocolData(int version) {
        ProtocolData protocol = protocols.get(version);
        if (protocol == null && (protocolPhase != Protocol.GAME)) {
            protocol = Iterables.getFirst(protocols.values(), null);
        }
        return protocol;
    }

    /**
     * Create packet abstract packet.
     *
     * @param id      the id
     * @param version the version
     * @return the abstract packet
     */
    public AbstractPacket createPacket(int id, int version) {
        ProtocolData protocolData = getProtocolData(version);
        if (protocolData == null) {
            throw new BadPacketException("Unsupported protocol version " + version);
        }
        if (id > Protocol.MAX_PACKET_ID || id < 0) {
            throw new BadPacketException("Packet with id " + id + " outside of range");
        }
        Supplier<? extends AbstractPacket> constructor = protocolData.packetConstructors[id];
        return (constructor == null) ? null : constructor.get();
    }

    /**
     * Register packet.
     *
     * @param packetClass the packet class
     * @param constructor the constructor
     * @param mappings    the mappings
     */
    public void registerPacket(Class<? extends AbstractPacket> packetClass, Supplier<? extends AbstractPacket> constructor, ProtocolMapping... mappings) {
        int mappingIndex = 0;
        ProtocolMapping mapping = mappings[mappingIndex];
        for (int protocol : ProtocolVersion.getSupportedVersionIds()) {
            if (protocol < mapping.protocolVersion) {
                // This is a new packet, skip it till we reach the next protocol
                continue;
            }
            if (mapping.protocolVersion < protocol && mappingIndex + 1 < mappings.length) {
                // Mapping is non current, but the next one may be ok
                ProtocolMapping nextMapping = mappings[mappingIndex + 1];
                if (nextMapping.protocolVersion == protocol) {

                    mapping = nextMapping;
                    mappingIndex++;
                }
            }
            if (mapping.packetID < 0) {
                break;
            }
            ProtocolData data = protocols.get(protocol);
            data.packetMap.put(packetClass, mapping.packetID);
            data.packetConstructors[mapping.packetID] = constructor;
        }
    }

    /**
     * Has packet boolean.
     *
     * @param packet  the packet
     * @param version the version
     * @return the boolean
     */
    public boolean hasPacket(Class<? extends AbstractPacket> packet, int version) {
        ProtocolData protocolData = getProtocolData(version);
        if (protocolData == null) {
            throw new BadPacketException("Unsupported protocol version");
        }
        return protocolData.packetMap.containsKey(packet);
    }

    /**
     * Gets id.
     *
     * @param packet  the packet
     * @param version the version
     * @return the id
     */
    public int getId(Class<? extends AbstractPacket> packet, int version) {
        ProtocolData protocolData = getProtocolData(version);
        if (protocolData == null) {
            throw new BadPacketException("Unsupported protocol version");
        }

        return protocolData.packetMap.get(packet);
    }

    /**
     * Gets direction.
     *
     * @return the direction
     */
    public ProtocolVersion.Direction getDirection() {
        return this.direction;
    }

}
