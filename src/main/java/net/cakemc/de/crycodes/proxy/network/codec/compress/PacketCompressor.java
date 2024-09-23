package net.cakemc.de.crycodes.proxy.network.codec.compress;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import net.cakemc.de.crycodes.proxy.network.packet.AbstractPacket;

import java.util.zip.Deflater;

/**
 * The type Packet compressor.
 */
public class PacketCompressor extends MessageToByteEncoder<ByteBuf> {
    private final Compressor compressor = new Compressor();
    private int threshold = 256;

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        compressor.init(true, Deflater.DEFAULT_COMPRESSION);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        compressor.free();
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf msg, ByteBuf out) throws Exception {
        int origSize = msg.readableBytes();
        if (origSize < threshold) {
            AbstractPacket.writeVarInt(0, out);
            out.writeBytes(msg);
        } else {
            AbstractPacket.writeVarInt(origSize, out);
            compressor.process(msg, out);
        }
    }

    /**
     * Sets threshold.
     *
     * @param threshold the threshold
     */
    public void setThreshold(final int threshold) {
        this.threshold = threshold;
    }
}
