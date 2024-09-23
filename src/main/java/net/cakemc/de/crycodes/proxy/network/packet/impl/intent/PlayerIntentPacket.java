package net.cakemc.de.crycodes.proxy.network.packet.impl.intent;

import io.netty.buffer.ByteBuf;
import net.cakemc.de.crycodes.proxy.network.packet.AbstractPacket;
import net.cakemc.de.crycodes.proxy.network.packet.AbstractPacketHandler;

import java.util.Objects;

/**
 * The type Player intent packet.
 */
public class PlayerIntentPacket extends AbstractPacket {
    private int protocolVersion;
    private String host;
    private int port;
    private int requestedProtocol;

    /**
     * Instantiates a new Player intent packet.
     */
    public PlayerIntentPacket() {
    }

    /**
     * Instantiates a new Player intent packet.
     *
     * @param protocolVersion   the protocol version
     * @param host              the host
     * @param port              the port
     * @param requestedProtocol the requested protocol
     */
    public PlayerIntentPacket(final int protocolVersion, final String host, final int port, final int requestedProtocol) {
        this.protocolVersion = protocolVersion;
        this.host = host;
        this.port = port;
        this.requestedProtocol = requestedProtocol;
    }

    @Override
    public void read(ByteBuf buf) {
        protocolVersion = readVarInt(buf);
        host = readString(buf, 255);
        port = buf.readUnsignedShort();
        requestedProtocol = readVarInt(buf);
    }

    @Override
    public void write(ByteBuf buf) {
        writeVarInt(protocolVersion, buf);
        writeString(host, buf);
        buf.writeShort(port);
        writeVarInt(requestedProtocol, buf);
    }

    @Override
    public void handle(AbstractPacketHandler handler) throws Exception {
        handler.handle(this);
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
     * Sets protocol version.
     *
     * @param protocolVersion the protocol version
     */
    public void setProtocolVersion(final int protocolVersion) {
        this.protocolVersion = protocolVersion;
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

    /**
     * Gets requested protocol.
     *
     * @return the requested protocol
     */
    public int getRequestedProtocol() {
        return this.requestedProtocol;
    }

    /**
     * Sets requested protocol.
     *
     * @param requestedProtocol the requested protocol
     */
    public void setRequestedProtocol(final int requestedProtocol) {
        this.requestedProtocol = requestedProtocol;
    }

    @Override
    public String toString() {
        return "Handshake(protocolVersion=" + this.getProtocolVersion() + ", host=" + this.getHost() + ", port=" + this.getPort() + ", requestedProtocol=" + this.getRequestedProtocol() + ")";
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof PlayerIntentPacket other)) return false;
        if (!other.canEqual(this)) return false;
        if (this.getProtocolVersion() != other.getProtocolVersion()) return false;
        if (this.getPort() != other.getPort()) return false;
        if (this.getRequestedProtocol() != other.getRequestedProtocol()) return false;
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
        return other instanceof PlayerIntentPacket;
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + this.getProtocolVersion();
        result = result * PRIME + this.getPort();
        result = result * PRIME + this.getRequestedProtocol();
        final Object $host = this.getHost();
        result = result * PRIME + ($host == null ? 43 : $host.hashCode());
        return result;
    }
}
