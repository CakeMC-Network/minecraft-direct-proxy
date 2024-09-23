package net.cakemc.de.crycodes.proxy.network.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import net.cakemc.de.crycodes.proxy.network.packet.ProtocolPacket;
import net.cakemc.de.crycodes.proxy.network.packet.impl.LegacyHandshakePacket;
import net.cakemc.de.crycodes.proxy.network.packet.impl.LegacyPingPacket;
import net.cakemc.de.crycodes.proxy.protocol.Protocol;

import java.util.List;

/**
 * The type Legacy decoder.
 */
public class LegacyDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        // See check in Varint21FrameDecoder for more details
        if (!ctx.channel().isActive()) {
            in.skipBytes(in.readableBytes());
            return;
        }

        if (!in.isReadable()) {
            return;
        }

        in.markReaderIndex();
        short packetID = in.readUnsignedByte();

        if (packetID == 0xFE) {
            out.add(new ProtocolPacket(new LegacyPingPacket(in.isReadable() && in.readUnsignedByte() == 0x01), Unpooled.EMPTY_BUFFER, Protocol.STATUS));
            return;
        } else if (packetID == 0x02 && in.isReadable()) {
            in.skipBytes(in.readableBytes());
            out.add(new ProtocolPacket(new LegacyHandshakePacket(), Unpooled.EMPTY_BUFFER, Protocol.STATUS));
            return;
        }

        in.resetReaderIndex();
        ctx.pipeline().remove(this);
    }
}
