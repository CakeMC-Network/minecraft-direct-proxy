package net.cakemc.de.crycodes.proxy.network.packet.impl;

import io.netty.buffer.ByteBuf;
import net.cakemc.de.crycodes.proxy.network.packet.AbstractPacket;
import net.cakemc.de.crycodes.proxy.network.packet.AbstractPacketHandler;

/**
 * The type Legacy ping packet.
 */
public class LegacyPingPacket extends AbstractPacket {
    private final boolean v1_5;

    /**
     * Instantiates a new Legacy ping packet.
     *
     * @param v1_5 the v 1 5
     */
    public LegacyPingPacket(final boolean v1_5) {
        this.v1_5 = v1_5;
    }

    @Override
    public void read(ByteBuf buf) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void write(ByteBuf buf) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void handle(AbstractPacketHandler handler) throws Exception {
        handler.handle(this);
    }

    /**
     * Is v 1 5 boolean.
     *
     * @return the boolean
     */
    public boolean isV1_5() {
        return this.v1_5;
    }

    @Override
    public String toString() {
        return "LegacyPingPacket(v1_5=" + this.isV1_5() + ")";
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof LegacyPingPacket other)) return false;
        if (!other.canEqual(this)) return false;
        return this.isV1_5() == other.isV1_5();
    }

    /**
     * Can equal boolean.
     *
     * @param other the other
     * @return the boolean
     */
    protected boolean canEqual(final Object other) {
        return other instanceof LegacyPingPacket;
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + (this.isV1_5() ? 79 : 97);
        return result;
    }
}
