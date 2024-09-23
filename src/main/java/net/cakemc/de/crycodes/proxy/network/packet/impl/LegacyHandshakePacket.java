package net.cakemc.de.crycodes.proxy.network.packet.impl;

import io.netty.buffer.ByteBuf;
import net.cakemc.de.crycodes.proxy.network.packet.AbstractPacket;
import net.cakemc.de.crycodes.proxy.network.packet.AbstractPacketHandler;

/**
 * The type Legacy handshake packet.
 */
public class LegacyHandshakePacket extends AbstractPacket {
    /**
     * Instantiates a new Legacy handshake packet.
     */
    public LegacyHandshakePacket() {
    }

    @Override
    public void read(ByteBuf buf) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void write(ByteBuf buf) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void handle(AbstractPacketHandler handler) throws Exception {
        handler.handle(this);
    }

    @Override
    public String toString() {
        return "LegacyHandshakePacket()";
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof LegacyHandshakePacket other)) return false;
        return other.canEqual(this);
    }

    /**
     * Can equal boolean.
     *
     * @param other the other
     * @return the boolean
     */
    protected boolean canEqual(final Object other) {
        return other instanceof LegacyHandshakePacket;
    }

    @Override
    public int hashCode() {
        final int result = 1;
        return result;
    }
}
