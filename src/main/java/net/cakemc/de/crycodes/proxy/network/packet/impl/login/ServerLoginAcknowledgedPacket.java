package net.cakemc.de.crycodes.proxy.network.packet.impl.login;

import io.netty.buffer.ByteBuf;
import net.cakemc.de.crycodes.proxy.network.packet.AbstractPacket;
import net.cakemc.de.crycodes.proxy.network.packet.AbstractPacketHandler;
import net.cakemc.de.crycodes.proxy.protocol.Protocol;
import net.cakemc.de.crycodes.proxy.protocol.ProtocolVersion;

/**
 * The type TargetServer login acknowledged packet.
 */
public class ServerLoginAcknowledgedPacket extends AbstractPacket {
    /**
     * Instantiates a new TargetServer login acknowledged packet.
     */
    public ServerLoginAcknowledgedPacket() {
    }

    @Override
    public void read(ByteBuf buf, ProtocolVersion.Direction direction, int protocolVersion) {
    }

    @Override
    public void write(ByteBuf buf, ProtocolVersion.Direction direction, int protocolVersion) {
    }

    @Override
    public Protocol nextProtocol() {
        return Protocol.CONFIGURATION;
    }

    @Override
    public void handle(AbstractPacketHandler handler) throws Exception {
        handler.handle(this);
    }

    @Override
    public String toString() {
        return "LoginAcknowledged()";
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof ServerLoginAcknowledgedPacket other)) return false;
        return other.canEqual(this);
    }

    /**
     * Can equal boolean.
     *
     * @param other the other
     * @return the boolean
     */
    protected boolean canEqual(final Object other) {
        return other instanceof ServerLoginAcknowledgedPacket;
    }

    @Override
    public int hashCode() {
        final int result = 1;
        return result;
    }
}
