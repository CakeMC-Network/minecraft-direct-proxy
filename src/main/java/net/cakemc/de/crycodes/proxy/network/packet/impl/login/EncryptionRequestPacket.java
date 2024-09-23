package net.cakemc.de.crycodes.proxy.network.packet.impl.login;

import io.netty.buffer.ByteBuf;
import net.cakemc.de.crycodes.proxy.network.packet.AbstractPacket;
import net.cakemc.de.crycodes.proxy.network.packet.AbstractPacketHandler;
import net.cakemc.de.crycodes.proxy.protocol.ProtocolVersion;

import java.util.Objects;

/**
 * The type Encryption request packet.
 */
public class EncryptionRequestPacket extends AbstractPacket {
    private String serverId;
    private byte[] publicKey;
    private byte[] verifyToken;
    private boolean shouldAuthenticate;

    /**
     * Instantiates a new Encryption request packet.
     */
    public EncryptionRequestPacket() {
    }

    /**
     * Instantiates a new Encryption request packet.
     *
     * @param serverId           the server id
     * @param publicKey          the public key
     * @param verifyToken        the verify token
     * @param shouldAuthenticate the should authenticate
     */
    public EncryptionRequestPacket(final String serverId, final byte[] publicKey, final byte[] verifyToken, final boolean shouldAuthenticate) {
        this.serverId = serverId;
        this.publicKey = publicKey;
        this.verifyToken = verifyToken;
        this.shouldAuthenticate = shouldAuthenticate;
    }

    @Override
    public void read(ByteBuf buf, ProtocolVersion.Direction direction, int protocolVersion) {
        serverId = readString(buf);
        publicKey = readArray(buf);
        verifyToken = readArray(buf);
        if (protocolVersion >= ProtocolVersion.MINECRAFT_1_20_5.getProtocolId()) {
            shouldAuthenticate = buf.readBoolean();
        }
    }

    @Override
    public void write(ByteBuf buf, ProtocolVersion.Direction direction, int protocolVersion) {
        writeString(serverId, buf);
        writeArray(publicKey, buf);
        writeArray(verifyToken, buf);
        if (protocolVersion >= ProtocolVersion.MINECRAFT_1_20_5.getProtocolId()) {
            buf.writeBoolean(shouldAuthenticate);
        }
    }

    @Override
    public void handle(AbstractPacketHandler handler) throws Exception {
        handler.handle(this);
    }

    /**
     * Gets server id.
     *
     * @return the server id
     */
    public String getServerId() {
        return this.serverId;
    }

    /**
     * Sets server id.
     *
     * @param serverId the server id
     */
    public void setServerId(final String serverId) {
        this.serverId = serverId;
    }

    /**
     * Get public key byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] getPublicKey() {
        return this.publicKey;
    }

    /**
     * Sets public key.
     *
     * @param publicKey the public key
     */
    public void setPublicKey(final byte[] publicKey) {
        this.publicKey = publicKey;
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
     * Is should authenticate boolean.
     *
     * @return the boolean
     */
    public boolean isShouldAuthenticate() {
        return this.shouldAuthenticate;
    }

    /**
     * Sets should authenticate.
     *
     * @param shouldAuthenticate the should authenticate
     */
    public void setShouldAuthenticate(final boolean shouldAuthenticate) {
        this.shouldAuthenticate = shouldAuthenticate;
    }

    @Override
    public String toString() {
        return "EncryptionRequest(serverId=" + this.getServerId() + ", publicKey=" + java.util.Arrays.toString(this.getPublicKey()) + ", verifyToken=" + java.util.Arrays.toString(this.getVerifyToken()) + ", shouldAuthenticate=" + this.isShouldAuthenticate() + ")";
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof EncryptionRequestPacket other)) return false;
        if (!other.canEqual(this)) return false;
        if (this.isShouldAuthenticate() != other.isShouldAuthenticate()) return false;
        final Object this$serverId = this.getServerId();
        final Object other$serverId = other.getServerId();
        if (!Objects.equals(this$serverId, other$serverId)) return false;
        if (!java.util.Arrays.equals(this.getPublicKey(), other.getPublicKey())) return false;
        return java.util.Arrays.equals(this.getVerifyToken(), other.getVerifyToken());
    }

    /**
     * Can equal boolean.
     *
     * @param other the other
     * @return the boolean
     */
    protected boolean canEqual(final Object other) {
        return other instanceof EncryptionRequestPacket;
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + (this.isShouldAuthenticate() ? 79 : 97);
        final Object $serverId = this.getServerId();
        result = result * PRIME + ($serverId == null ? 43 : $serverId.hashCode());
        result = result * PRIME + java.util.Arrays.hashCode(this.getPublicKey());
        result = result * PRIME + java.util.Arrays.hashCode(this.getVerifyToken());
        return result;
    }
}
