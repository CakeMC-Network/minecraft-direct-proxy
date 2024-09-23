package net.cakemc.de.crycodes.proxy.network.packet.impl;

import io.netty.buffer.ByteBuf;
import net.cakemc.de.crycodes.proxy.network.packet.AbstractPacket;
import net.cakemc.de.crycodes.proxy.network.packet.AbstractPacketHandler;
import net.cakemc.de.crycodes.proxy.protocol.Protocol;
import net.cakemc.de.crycodes.proxy.protocol.ProtocolVersion;

/**
 * The type Finish configuration packet.
 */
public class FinishConfigurationPacket extends AbstractPacket {
    /**
     * Instantiates a new Finish configuration packet.
     */
    public FinishConfigurationPacket() {
    }

    @Override
    public void read(ByteBuf buf, ProtocolVersion.Direction direction, int protocolVersion) {
    }

    @Override
    public void write(ByteBuf buf, ProtocolVersion.Direction direction, int protocolVersion) {
    }

    @Override
    public Protocol nextProtocol() {
        return Protocol.GAME;
    }

    @Override
    public void handle(AbstractPacketHandler handler) throws Exception {
        handler.handle(this);
    }

    @Override
    public String toString() {
        return "FinishConfiguration()";
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof FinishConfigurationPacket other)) return false;
        return other.canEqual(this);
    }

    /**
     * Can equal boolean.
     *
     * @param other the other
     * @return the boolean
     */
    protected boolean canEqual(final Object other) {
        return other instanceof FinishConfigurationPacket;
    }

    @Override
    public int hashCode() {
        final int result = 1;
        return result;
    }
}
