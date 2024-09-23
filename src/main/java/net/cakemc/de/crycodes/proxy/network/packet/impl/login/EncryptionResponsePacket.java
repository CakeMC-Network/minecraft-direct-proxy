package net.cakemc.de.crycodes.proxy.network.packet.impl.login;

import io.netty.buffer.ByteBuf;
import net.cakemc.de.crycodes.proxy.network.packet.AbstractPacket;
import net.cakemc.de.crycodes.proxy.network.packet.AbstractPacketHandler;
import net.cakemc.de.crycodes.proxy.protocol.ProtocolVersion;

import java.util.Objects;

/**
 * The type Encryption response packet.
 */
public class EncryptionResponsePacket extends AbstractPacket {
    private byte[] sharedSecret;
    private byte[] verifyToken;
    private EncryptionData encryptionData;

    /**
     * Instantiates a new Encryption response packet.
     */
    public EncryptionResponsePacket() {
    }

    /**
     * Instantiates a new Encryption response packet.
     *
     * @param sharedSecret   the shared secret
     * @param verifyToken    the verify token
     * @param encryptionData the encryption data
     */
    public EncryptionResponsePacket(final byte[] sharedSecret, final byte[] verifyToken, final EncryptionData encryptionData) {
        this.sharedSecret = sharedSecret;
        this.verifyToken = verifyToken;
        this.encryptionData = encryptionData;
    }

    public void read(ByteBuf buf, ProtocolVersion.Direction direction, int protocolVersion) {
        sharedSecret = readArray(buf, 128);
        if (protocolVersion < ProtocolVersion.MINECRAFT_1_19.getProtocolId() ||
                protocolVersion >= ProtocolVersion.MINECRAFT_1_19_3.getProtocolId() ||
                buf.readBoolean()) {
            verifyToken = readArray(buf, 128);
        } else {
            encryptionData = new EncryptionData(buf.readLong(), readArray(buf));
        }
    }

    @Override
    public void write(ByteBuf buf, ProtocolVersion.Direction direction, int protocolVersion) {
        writeArray(sharedSecret, buf);
        if (verifyToken != null) {
            if (protocolVersion >= ProtocolVersion.MINECRAFT_1_19.getProtocolId() &&
                    protocolVersion <= ProtocolVersion.MINECRAFT_1_19_3.getProtocolId()) {
                buf.writeBoolean(true);
            }
            writeArray(verifyToken, buf);
        } else {
            buf.writeLong(encryptionData.getSalt());
            writeArray(encryptionData.getSignature(), buf);
        }
    }

    @Override
    public void handle(AbstractPacketHandler handler) throws Exception {
        handler.handle(this);
    }

    /**
     * Get shared secret byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] getSharedSecret() {
        return this.sharedSecret;
    }

    /**
     * Sets shared secret.
     *
     * @param sharedSecret the shared secret
     */
    public void setSharedSecret(final byte[] sharedSecret) {
        this.sharedSecret = sharedSecret;
    }

    /**
     * Get verify token byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] getVerifyToken() {
        return this.verifyToken;
    }

    /**
     * Sets verify token.
     *
     * @param verifyToken the verify token
     */
    public void setVerifyToken(final byte[] verifyToken) {
        this.verifyToken = verifyToken;
    }

    /**
     * Gets encryption data.
     *
     * @return the encryption data
     */
    public EncryptionData getEncryptionData() {
        return this.encryptionData;
    }

    /**
     * Sets encryption data.
     *
     * @param encryptionData the encryption data
     */
    public void setEncryptionData(final EncryptionData encryptionData) {
        this.encryptionData = encryptionData;
    }

    @Override
    public String toString() {
        return "EncryptionResponse(sharedSecret=" + java.util.Arrays.toString(this.getSharedSecret()) + ", verifyToken=" + java.util.Arrays.toString(this.getVerifyToken()) + ", encryptionData=" + this.getEncryptionData() + ")";
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof EncryptionResponsePacket other)) return false;
        if (!other.canEqual(this)) return false;
        if (!java.util.Arrays.equals(this.getSharedSecret(), other.getSharedSecret())) return false;
        if (!java.util.Arrays.equals(this.getVerifyToken(), other.getVerifyToken())) return false;
        final Object this$encryptionData = this.getEncryptionData();
        final Object other$encryptionData = other.getEncryptionData();
        return Objects.equals(this$encryptionData, other$encryptionData);
    }

    /**
     * Can equal boolean.
     *
     * @param other the other
     * @return the boolean
     */
    protected boolean canEqual(final Object other) {
        return other instanceof EncryptionResponsePacket;
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + java.util.Arrays.hashCode(this.getSharedSecret());
        result = result * PRIME + java.util.Arrays.hashCode(this.getVerifyToken());
        final Object $encryptionData = this.getEncryptionData();
        result = result * PRIME + ($encryptionData == null ? 43 : $encryptionData.hashCode());
        return result;
    }

    /**
     * The type Encryption data.
     */
    public static class EncryptionData {
        private final long salt;
        private final byte[] signature;

        /**
         * Instantiates a new Encryption data.
         *
         * @param salt      the salt
         * @param signature the signature
         */
        public EncryptionData(final long salt, final byte[] signature) {
            this.salt = salt;
            this.signature = signature;
        }

        /**
         * Gets salt.
         *
         * @return the salt
         */
        public long getSalt() {
            return this.salt;
        }

        /**
         * Get signature byte [ ].
         *
         * @return the byte [ ]
         */
        public byte[] getSignature() {
            return this.signature;
        }

        @Override
        public boolean equals(final Object o) {
            if (o == this) return true;
            if (!(o instanceof EncryptionData other)) return false;
            if (!other.canEqual(this)) return false;
            if (this.getSalt() != other.getSalt()) return false;
            return java.util.Arrays.equals(this.getSignature(), other.getSignature());
        }

        /**
         * Can equal boolean.
         *
         * @param other the other
         * @return the boolean
         */
        protected boolean canEqual(final Object other) {
            return other instanceof EncryptionData;
        }

        @Override
        public int hashCode() {
            final int PRIME = 59;
            int result = 1;
            final long $salt = this.getSalt();
            result = result * PRIME + (int) ($salt >>> 32 ^ $salt);
            result = result * PRIME + java.util.Arrays.hashCode(this.getSignature());
            return result;
        }

        @Override
        public String toString() {
            return "EncryptionResponse.EncryptionData(salt=" + this.getSalt() + ", signature=" + java.util.Arrays.toString(this.getSignature()) + ")";
        }
    }
}
