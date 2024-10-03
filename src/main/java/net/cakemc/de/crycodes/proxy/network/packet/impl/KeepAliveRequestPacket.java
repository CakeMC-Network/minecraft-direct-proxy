package net.cakemc.de.crycodes.proxy.network.packet.impl;

import io.netty.buffer.ByteBuf;
import net.cakemc.de.crycodes.proxy.network.packet.AbstractPacket;
import net.cakemc.de.crycodes.proxy.network.packet.AbstractPacketHandler;
import net.cakemc.de.crycodes.proxy.protocol.ProtocolVersion;

/**
 * The type Keep alive request packet.
 */
public class KeepAliveRequestPacket extends AbstractPacket {
    private long randomId;

    /**
     * Instantiates a new Keep alive request packet.
     */
    public KeepAliveRequestPacket() {
    }

    /**
     * Instantiates a new Keep alive request packet.
     *
     * @param randomId the random id
     */
    public KeepAliveRequestPacket(final long randomId) {
        this.randomId = randomId;
    }

    @Override
    public void read(ByteBuf buf, ProtocolVersion.Direction direction, int protocolVersion) {
        randomId = (protocolVersion >= ProtocolVersion.MINECRAFT_1_12_2.getProtocolId()) ? buf.readLong() : readVarInt(buf);
    }

    @Override
    public void write(ByteBuf buf, ProtocolVersion.Direction direction, int protocolVersion) {
        if (protocolVersion >= ProtocolVersion.MINECRAFT_1_12_2.getProtocolId()) {
            buf.writeLong(randomId);
        } else {
            writeVarInt((int) randomId, buf);
        }
    }

    @Override
    public void handle(AbstractPacketHandler handler) throws Exception {
    }

    /**
     * Gets random id.
     *
     * @return the random id
     */
    public long getRandomId() {
        return this.randomId;
    }

    /**
     * Sets random id.
     *
     * @param randomId the random id
     */
    public void setRandomId(final long randomId) {
        this.randomId = randomId;
    }

    @Override
    public String toString() {
        return "KeepAlive(randomId=" + this.getRandomId() + ")";
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof KeepAliveRequestPacket other)) return false;
        if (!other.canEqual(this)) return false;
        return this.getRandomId() == other.getRandomId();
    }

    /**
     * Can equal boolean.
     *
     * @param other the other
     * @return the boolean
     */
    protected boolean canEqual(final Object other) {
        return other instanceof KeepAliveRequestPacket;
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final long $randomId = this.getRandomId();
        result = result * PRIME + (int) ($randomId >>> 32 ^ $randomId);
        return result;
    }
}