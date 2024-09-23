package net.cakemc.de.crycodes.proxy.network.codec.minecraft;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import net.cakemc.de.crycodes.proxy.network.packet.AbstractPacket;
import net.cakemc.de.crycodes.proxy.network.packet.BadPacketException;
import net.cakemc.de.crycodes.proxy.network.packet.ProtocolPacket;
import net.cakemc.de.crycodes.proxy.protocol.DirectionData;
import net.cakemc.de.crycodes.proxy.protocol.Protocol;

import java.util.List;

/**
 * The type Minecraft decoder.
 */
public class MinecraftDecoder extends MessageToMessageDecoder<ByteBuf> {
    private final boolean server;
    private Protocol protocol;
    private int protocolVersion;

    /**
     * Instantiates a new Minecraft decoder.
     *
     * @param server          the server
     * @param protocol        the protocol
     * @param protocolVersion the protocol version
     */
    public MinecraftDecoder(boolean server, Protocol protocol, int protocolVersion) {
        this.server = server;
        this.protocol = protocol;
        this.protocolVersion = protocolVersion;
    }

    /**
     * Instantiates a new Minecraft decoder.
     *
     * @param inboundMessageType the inbound message type
     * @param server             the server
     * @param protocol           the protocol
     * @param protocolVersion    the protocol version
     */
    public MinecraftDecoder(Class<? extends ByteBuf> inboundMessageType, boolean server, Protocol protocol, int protocolVersion) {
        super(inboundMessageType);
        this.server = server;
        this.protocol = protocol;
        this.protocolVersion = protocolVersion;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        // See Varint21FrameDecoder for the general reasoning. We add this here as ByteToMessageDecoder#handlerRemoved()
        // will fire any cumulated data through the pipeline, so we want to try and stop it here.
        if (!ctx.channel().isActive()) {
            return;
        }
        DirectionData prot = (server) ? protocol.TO_SERVER : protocol.TO_CLIENT;
        ByteBuf slice = in.copy(); // Can't slice this one due to EntityMap :(
        try {
            int packetId = AbstractPacket.readVarInt(in);
            AbstractPacket packet = prot.createPacket(packetId, protocolVersion);
            if (packet != null) {
                packet.read(in, protocol, prot.getDirection(), protocolVersion);
                if (in.isReadable()) {
                    throw new BadPacketException("Packet " + protocol + ":" + prot.getDirection() + "/" + packetId + " (" + packet.getClass().getSimpleName() + ") larger than expected, extra bytes: " + in.readableBytes());
                }
            } else {
                in.skipBytes(in.readableBytes());
            }
            out.add(new ProtocolPacket(packet, slice, protocol));
            slice = null;
        } finally {
            if (slice != null) {
                slice.release();
            }
        }
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
     * Sets protocol version.
     *
     * @param protocolVersion the protocol version
     */
    public void setProtocolVersion(final int protocolVersion) {
        this.protocolVersion = protocolVersion;
    }
}
