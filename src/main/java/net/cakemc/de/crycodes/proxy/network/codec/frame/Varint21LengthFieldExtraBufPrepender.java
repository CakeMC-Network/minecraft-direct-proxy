package net.cakemc.de.crycodes.proxy.network.codec.frame;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import net.cakemc.de.crycodes.proxy.network.packet.AbstractPacket;

import java.util.List;

/**
 * The type Varint 21 length field extra buf prepender.
 */
@ChannelHandler.Sharable
public class Varint21LengthFieldExtraBufPrepender extends MessageToMessageEncoder<ByteBuf> {

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        int bodyLen = msg.readableBytes();
        ByteBuf lenBuf = ctx.alloc().ioBuffer(Varint21LengthFieldPrepender.varintSize(bodyLen));
        AbstractPacket.writeVarInt(bodyLen, lenBuf);
        out.add(lenBuf);
        out.add(msg.retain());
    }
}
