package net.cakemc.de.crycodes.proxy.network.codec.cipher;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * The type Cipher encoder.
 */
public class CipherEncoder extends MessageToByteEncoder<ByteBuf> {
    private final Encryption cipher;

    /**
     * Instantiates a new Cipher encoder.
     *
     * @param cipher the cipher
     */
    public CipherEncoder(final Encryption cipher) {
        this.cipher = cipher;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf in, ByteBuf out) throws Exception {
        cipher.cipher(in, out);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        cipher.free();
    }


}
