package net.cakemc.de.crycodes.proxy.network.packet.impl;

import io.netty.buffer.ByteBuf;
import net.cakemc.de.crycodes.proxy.network.packet.AbstractPacket;
import net.cakemc.de.crycodes.proxy.network.packet.AbstractPacketHandler;

/**
 * The type Keep alive response packet.
 */
public class KeepAliveResponsePacket extends AbstractPacket {
    private long time;

    /**
     * Instantiates a new Keep alive response packet.
     */
    public KeepAliveResponsePacket() {
    }

    /**
     * Instantiates a new Keep alive response packet.
     *
     * @param time the time
     */
    public KeepAliveResponsePacket(final long time) {
        this.time = time;
    }

    @Override
    public void read(ByteBuf buf) {
        time = buf.readLong();
    }

    @Override
    public void write(ByteBuf buf) {
        buf.writeLong(time);
    }

    @Override
    public void handle(AbstractPacketHandler handler) throws Exception {
    }

    /**
     * Gets time.
     *
     * @return the time
     */
    public long getTime() {
        return this.time;
    }

    /**
     * Sets time.
     *
     * @param time the time
     */
    public void setTime(final long time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "PingPacket(time=" + this.getTime() + ")";
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof KeepAliveResponsePacket other)) return false;
        if (!other.canEqual(this)) return false;
        return this.getTime() == other.getTime();
    }

    /**
     * Can equal boolean.
     *
     * @param other the other
     * @return the boolean
     */
    protected boolean canEqual(final Object other) {
        return other instanceof KeepAliveResponsePacket;
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final long $time = this.getTime();
        result = result * PRIME + (int) ($time >>> 32 ^ $time);
        return result;
    }
}