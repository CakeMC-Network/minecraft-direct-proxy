package net.cakemc.de.crycodes.proxy.network.codec.cipher;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

/**
 * The type Cipher decoder.
 */
public class CipherDecoder extends MessageToMessageDecoder<ByteBuf> {
    private final Encryption cipher;

    /**
     * Instantiates a new Cipher decoder.
     *
     * @param cipher the cipher
     */
    public CipherDecoder(final Encryption cipher) {
        this.cipher = cipher;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        out.add(cipher.cipher(ctx, msg));
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        cipher.free();
    }

}
