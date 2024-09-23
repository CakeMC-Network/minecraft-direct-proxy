package net.cakemc.de.crycodes.proxy.network.codec.cipher;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.IvParameterSpec;
import java.security.GeneralSecurityException;

/**
 * The type Encryption.
 */
public class Encryption {

    private static final ThreadLocal<byte[]> heapInLocal = new EmptyByteThreadLocal();
    private static final ThreadLocal<byte[]> heapOutLocal = new EmptyByteThreadLocal();
    private final Cipher cipher;

    /**
     * Instantiates a new Encryption.
     */
    public Encryption() {
        try {
            this.cipher = Cipher.getInstance("AES/CFB8/NoPadding");
        } catch (GeneralSecurityException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Init.
     *
     * @param forEncryption the for encryption
     * @param key           the key
     * @throws GeneralSecurityException the general security exception
     */
    public void init(boolean forEncryption, SecretKey key) throws GeneralSecurityException {
        int mode = forEncryption ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE;
        cipher.init(mode, key, new IvParameterSpec(key.getEncoded()));
    }

    /**
     * Cipher.
     *
     * @param in  the in
     * @param out the out
     * @throws ShortBufferException the short buffer exception
     */
    public void cipher(ByteBuf in, ByteBuf out) throws ShortBufferException {
        int readableBytes = in.readableBytes();
        byte[] heapIn = bufToByte(in);

        byte[] heapOut = heapOutLocal.get();
        int outputSize = cipher.getOutputSize(readableBytes);
        if (heapOut.length < outputSize) {
            heapOut = new byte[outputSize];
            heapOutLocal.set(heapOut);
        }
        out.writeBytes(heapOut, 0, cipher.update(heapIn, 0, readableBytes, heapOut));
    }

    /**
     * Cipher byte buf.
     *
     * @param ctx the ctx
     * @param in  the in
     * @return the byte buf
     * @throws ShortBufferException the short buffer exception
     */
    public ByteBuf cipher(ChannelHandlerContext ctx, ByteBuf in) throws ShortBufferException {
        int readableBytes = in.readableBytes();
        byte[] heapIn = bufToByte(in);

        ByteBuf heapOut = ctx.alloc().heapBuffer(cipher.getOutputSize(readableBytes));
        heapOut.writerIndex(cipher.update(heapIn, 0, readableBytes, heapOut.array(), heapOut.arrayOffset()));

        return heapOut;
    }

    /**
     * Free.
     */
    public void free() {
    }

    private byte[] bufToByte(ByteBuf in) {
        byte[] heapIn = heapInLocal.get();
        int readableBytes = in.readableBytes();
        if (heapIn.length < readableBytes) {
            heapIn = new byte[readableBytes];
            heapInLocal.set(heapIn);
        }
        in.readBytes(heapIn, 0, readableBytes);
        return heapIn;
    }

    private static class EmptyByteThreadLocal extends ThreadLocal<byte[]> {

        @Override
        protected byte[] initialValue() {
            return new byte[0];
        }
    }
}
