package net.cakemc.de.crycodes.proxy.network.packet.impl.login;

import io.netty.buffer.ByteBuf;
import net.cakemc.de.crycodes.proxy.network.packet.AbstractPacket;
import net.cakemc.de.crycodes.proxy.network.packet.AbstractPacketHandler;
import net.cakemc.de.crycodes.proxy.protocol.ProtocolVersion;

/**
 * The type Server set compression packet.
 */
public class ServerSetCompressionPacket extends AbstractPacket {
    private int threshold;

    /**
     * Instantiates a new Server set compression packet.
     */
    public ServerSetCompressionPacket() {
    }

    /**
     * Instantiates a new Server set compression packet.
     *
     * @param threshold the threshold
     */
    public ServerSetCompressionPacket(final int threshold) {
        this.threshold = threshold;
    }

    @Override
    public void read(ByteBuf buf, ProtocolVersion.Direction direction, int protocolVersion) {
        threshold = AbstractPacket.readVarInt(buf);
    }

    @Override
    public void write(ByteBuf buf, ProtocolVersion.Direction direction, int protocolVersion) {
        AbstractPacket.writeVarInt(threshold, buf);
    }

    @Override
    public void handle(AbstractPacketHandler handler) throws Exception {
        handler.handle(this);
    }

    /**
     * Gets threshold.
     *
     * @return the threshold
     */
    public int getThreshold() {
        return this.threshold;
    }

    /**
     * Sets threshold.
     *
     * @param threshold the threshold
     */
    public void setThreshold(final int threshold) {
        this.threshold = threshold;
    }

    @Override
    public String toString() {
        return "SetCompression(threshold=" + this.getThreshold() + ")";
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof ServerSetCompressionPacket other)) return false;
        if (!other.canEqual(this)) return false;
        return this.getThreshold() == other.getThreshold();
    }

    /**
     * Can equal boolean.
     *
     * @param other the other
     * @return the boolean
     */
    protected boolean canEqual(final Object other) {
        return other instanceof ServerSetCompressionPacket;
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + this.getThreshold();
        return result;
    }
}
