package net.cakemc.de.crycodes.proxy.network.codec.minecraft;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import net.cakemc.de.crycodes.proxy.network.packet.AbstractPacket;
import net.cakemc.de.crycodes.proxy.protocol.DirectionData;
import net.cakemc.de.crycodes.proxy.protocol.Protocol;

/**
 * The type Minecraft encoder.
 */
public class MinecraftEncoder extends MessageToByteEncoder<AbstractPacket> {
    private final boolean server;
    private Protocol protocol;
    private int protocolVersion;

    /**
     * Instantiates a new Minecraft encoder.
     *
     * @param protocol        the protocol
     * @param server          the server
     * @param protocolVersion the protocol version
     */
    public MinecraftEncoder(final Protocol protocol, final boolean server, final int protocolVersion) {
        this.protocol = protocol;
        this.server = server;
        this.protocolVersion = protocolVersion;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, AbstractPacket msg, ByteBuf out) throws Exception {
        DirectionData prot = (server) ? protocol.TO_CLIENT : protocol.TO_SERVER;
        AbstractPacket.writeVarInt(prot.getId(msg.getClass(), protocolVersion), out);
        msg.write(out, protocol, prot.getDirection(), protocolVersion);
    }

    /**
     * Gets protocol.
     *
     * @return the protocol
     */
    public Protocol getProtocol() {
        return this.protocol;
    }

    /**
     * Sets protocol.
     *
     * @param protocol the protocol
     */
    public void setProtocol(final Protocol protocol) {
        this.protocol = protocol;
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
}
