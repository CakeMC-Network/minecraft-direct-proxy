package net.cakemc.de.crycodes.proxy.network.codec.frame;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import net.cakemc.de.crycodes.proxy.network.packet.AbstractPacket;

/**
 * The type Varint 21 length field prepender.
 */
@ChannelHandler.Sharable
public class Varint21LengthFieldPrepender extends MessageToByteEncoder<ByteBuf> {

    /**
     * Varint size int.
     *
     * @param paramInt the param int
     * @return the int
     */
    static int varintSize(int paramInt) {
        if ((paramInt & 0xFFFFFF80) == 0) {
            return 1;
        }
        if ((paramInt & 0xFFFFC000) == 0) {
            return 2;
        }
        if ((paramInt & 0xFFE00000) == 0) {
            return 3;
        }
        if ((paramInt & 0xF0000000) == 0) {
            return 4;
        }
        return 5;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf msg, ByteBuf out) throws Exception {
        int bodyLen = msg.readableBytes();
        int headerLen = varintSize(bodyLen);
        out.ensureWritable(headerLen + bodyLen);

        AbstractPacket.writeVarInt(bodyLen, out);
        out.writeBytes(msg);
    }
}
