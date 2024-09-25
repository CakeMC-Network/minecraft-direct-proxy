package net.cakemc.de.crycodes.proxy.network.packet.impl;

import io.netty.buffer.ByteBuf;
import net.cakemc.de.crycodes.proxy.network.packet.AbstractPacket;
import net.cakemc.de.crycodes.proxy.network.packet.AbstractPacketHandler;
import net.cakemc.de.crycodes.proxy.protocol.ProtocolVersion;

import java.util.Objects;

/**
 * The type TargetServer transfer packet.
 */
public class ServerTransferPacket extends AbstractPacket {
    private String host;
    private int port;

    /**
     * Instantiates a new TargetServer transfer packet.
     */
    public ServerTransferPacket() {
    }

    /**
     * Instantiates a new TargetServer transfer packet.
     *
     * @param host the host
     * @param port the port
     */
    public ServerTransferPacket(final String host, final int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public void read(ByteBuf buf, ProtocolVersion.Direction direction, int protocolVersion) {
        host = readString(buf);
        port = readVarInt(buf);
    }

    @Override
    public void write(ByteBuf buf, ProtocolVersion.Direction direction, int protocolVersion) {
        writeString(host, buf);
        writeVarInt(port, buf);
    }

    @Override
    public void handle(AbstractPacketHandler handler) throws Exception {
        handler.handle(this);
    }

    /**
     * Gets host.
     *
     * @return the host
     */
    public String getHost() {
        return this.host;
    }

    /**
     * Sets host.
     *
     * @param host the host
     */
    public void setHost(final String host) {
        this.host = host;
    }

    /**
     * Gets port.
     *
     * @return the port
     */
    public int getPort() {
        return this.port;
    }

    /**
     * Sets port.
     *
     * @param port the port
     */
    public void setPort(final int port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return "ServerTransferPacket(host=" + this.getHost() + ", port=" + this.getPort() + ")";
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof ServerTransferPacket other)) return false;
        if (!other.canEqual(this)) return false;
        if (this.getPort() != other.getPort()) return false;
        final Object this$host = this.getHost();
        final Object other$host = other.getHost();
        return Objects.equals(this$host, other$host);
    }

    /**
     * Can equal boolean.
     *
     * @param other the other
     * @return the boolean
     */
    protected boolean canEqual(final Object other) {
        return other instanceof ServerTransferPacket;
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + this.getPort();
        final Object $host = this.getHost();
        result = result * PRIME + ($host == null ? 43 : $host.hashCode());
        return result;
    }
}
