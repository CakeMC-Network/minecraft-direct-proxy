package net.cakemc.de.crycodes.proxy.network.codec.compress;


import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import net.cakemc.de.crycodes.proxy.network.packet.AbstractPacket;

import java.util.List;

/**
 * The type Packet decompressor.
 */
public class PacketDecompressor extends MessageToMessageDecoder<ByteBuf> {

    /**
     * The Compressor.
     */
    public final Compressor compressor = new Compressor();

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        compressor.init(false, 0);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        compressor.free();
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int size = AbstractPacket.readVarInt(in);
        if (size == 0) {
            out.add(in.retain());
        } else {
            ByteBuf decompressed = ctx.alloc().directBuffer();

            try {
                compressor.process(in, decompressed);


                out.add(decompressed);
                decompressed = null;
            } finally {
                if (decompressed != null) {
                    decompressed.release();
                }
            }
        }
    }
}
