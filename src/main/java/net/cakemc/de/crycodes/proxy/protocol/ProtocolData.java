package net.cakemc.de.crycodes.proxy.protocol;

import net.cakemc.de.crycodes.proxy.network.packet.AbstractPacket;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * The type Protocol data.
 */
public class ProtocolData {
    /**
     * The Protocol version.
     */
    public final int protocolVersion;
    /**
     * The Packet map.
     */
    public final Map<Class<? extends AbstractPacket>, Integer> packetMap = new HashMap<>(Protocol.MAX_PACKET_ID);
    /**
     * The Packet constructors.
     */
    @SuppressWarnings("unchecked")
    public final Supplier<? extends AbstractPacket>[] packetConstructors = new Supplier[Protocol.MAX_PACKET_ID];

    /**
     * Instantiates a new Protocol data.
     *
     * @param protocolVersion the protocol version
     */
    public ProtocolData(final int protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    /**
     * Gets protocol version.
     *
     * @return the protocol version
     */
    public int getProtocolVersion() {
        return this.protocolVersion;
    }

    /**
     * Gets packet map.
     *
     * @return the packet map
     */
    public Map<Class<? extends AbstractPacket>, Integer> getPacketMap() {
        return this.packetMap;
    }

    /**
     * Get packet constructors supplier [ ].
     *
     * @return the supplier [ ]
     */
    public Supplier<? extends AbstractPacket>[] getPacketConstructors() {
        return this.packetConstructors;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof ProtocolData other)) return false;
        if (!other.canEqual(this)) return false;
        if (this.getProtocolVersion() != other.getProtocolVersion()) return false;
        final Object this$packetMap = this.getPacketMap();
        final Object other$packetMap = other.getPacketMap();
        if (!Objects.equals(this$packetMap, other$packetMap))
            return false;
        return java.util.Arrays.deepEquals(this.getPacketConstructors(), other.getPacketConstructors());
    }

    /**
     * Can equal boolean.
     *
     * @param other the other
     * @return the boolean
     */
    protected boolean canEqual(final Object other) {
        return other instanceof ProtocolData;
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + this.getProtocolVersion();
        final Object $packetMap = this.getPacketMap();
        result = result * PRIME + ($packetMap == null ? 43 : $packetMap.hashCode());
        result = result * PRIME + java.util.Arrays.deepHashCode(this.getPacketConstructors());
        return result;
    }

    @Override
    public String toString() {
        return "Protocol.ProtocolData(protocolVersion=" + this.getProtocolVersion() + ", packetMap=" + this.getPacketMap() + ", packetConstructors=" + java.util.Arrays.deepToString(this.getPacketConstructors()) + ")";
    }
}
