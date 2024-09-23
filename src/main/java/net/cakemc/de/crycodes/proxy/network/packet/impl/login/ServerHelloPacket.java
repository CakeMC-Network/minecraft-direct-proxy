package net.cakemc.de.crycodes.proxy.network.packet.impl.login;

import io.netty.buffer.ByteBuf;
import net.cakemc.de.crycodes.proxy.network.packet.AbstractPacket;
import net.cakemc.de.crycodes.proxy.network.packet.AbstractPacketHandler;
import net.cakemc.de.crycodes.proxy.protocol.PlayerPublicKey;
import net.cakemc.de.crycodes.proxy.protocol.ProtocolVersion;

import java.util.Objects;
import java.util.UUID;

/**
 * The type Server hello packet.
 */
public class ServerHelloPacket extends AbstractPacket {
    private String data;
    private PlayerPublicKey publicKey;
    private UUID uuid;

    /**
     * Instantiates a new Server hello packet.
     */
    public ServerHelloPacket() {
    }

    /**
     * Instantiates a new Server hello packet.
     *
     * @param data      the data
     * @param publicKey the public key
     * @param uuid      the uuid
     */
    public ServerHelloPacket(final String data, final PlayerPublicKey publicKey, final UUID uuid) {
        this.data = data;
        this.publicKey = publicKey;
        this.uuid = uuid;
    }

    @Override
    public void read(ByteBuf buf, ProtocolVersion.Direction direction, int protocolVersion) {
        data = readString(buf, 16);
        if (protocolVersion >= ProtocolVersion.MINECRAFT_1_19.getProtocolId() &&
                protocolVersion < ProtocolVersion.MINECRAFT_1_19_3.getProtocolId()) {
            publicKey = readPublicKey(buf);
        }
        if (protocolVersion >= ProtocolVersion.MINECRAFT_1_19_1.getProtocolId()) {
            if (protocolVersion >= ProtocolVersion.MINECRAFT_1_20_2.getProtocolId() || buf.readBoolean()) {
                uuid = readUUID(buf);
            }
        }
    }

    @Override
    public void write(ByteBuf buf, ProtocolVersion.Direction direction, int protocolVersion) {
        writeString(data, buf);
        if (protocolVersion >= ProtocolVersion.MINECRAFT_1_19.getProtocolId() &&
                protocolVersion < ProtocolVersion.MINECRAFT_1_19_3.getProtocolId()) {
            writePublicKey(publicKey, buf);
        }
        if (protocolVersion >= ProtocolVersion.MINECRAFT_1_19_1.getProtocolId()) {
            if (protocolVersion >= ProtocolVersion.MINECRAFT_1_20_2.getProtocolId()) {
                writeUUID(uuid, buf);
            } else {
                if (uuid != null) {
                    buf.writeBoolean(true);
                    writeUUID(uuid, buf);
                } else {
                    buf.writeBoolean(false);
                }
            }
        }
    }

    @Override
    public void handle(AbstractPacketHandler handler) throws Exception {
        handler.handle(this);
    }

    /**
     * Gets data.
     *
     * @return the data
     */
    public String getData() {
        return this.data;
    }

    /**
     * Sets data.
     *
     * @param data the data
     */
    public void setData(final String data) {
        this.data = data;
    }

    /**
     * Gets public key.
     *
     * @return the public key
     */
    public PlayerPublicKey getPublicKey() {
        return this.publicKey;
    }

    /**
     * Sets public key.
     *
     * @param publicKey the public key
     */
    public void setPublicKey(final PlayerPublicKey publicKey) {
        this.publicKey = publicKey;
    }

    /**
     * Gets uuid.
     *
     * @return the uuid
     */
    public UUID getUuid() {
        return this.uuid;
    }

    /**
     * Sets uuid.
     *
     * @param uuid the uuid
     */
    public void setUuid(final UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public String toString() {
        return "LoginRequest(data=" + this.getData() + ", publicKey=" + this.getPublicKey() + ", uuid=" + this.getUuid() + ")";
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof ServerHelloPacket other)) return false;
        if (!other.canEqual(this)) return false;
        final Object this$data = this.getData();
        final Object other$data = other.getData();
        if (!Objects.equals(this$data, other$data)) return false;
        final Object this$publicKey = this.getPublicKey();
        final Object other$publicKey = other.getPublicKey();
        if (!Objects.equals(this$publicKey, other$publicKey)) return false;
        final Object this$uuid = this.getUuid();
        final Object other$uuid = other.getUuid();
        return Objects.equals(this$uuid, other$uuid);
    }

    /**
     * Can equal boolean.
     *
     * @param other the other
     * @return the boolean
     */
    protected boolean canEqual(final Object other) {
        return other instanceof ServerHelloPacket;
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $data = this.getData();
        result = result * PRIME + ($data == null ? 43 : $data.hashCode());
        final Object $publicKey = this.getPublicKey();
        result = result * PRIME + ($publicKey == null ? 43 : $publicKey.hashCode());
        final Object $uuid = this.getUuid();
        result = result * PRIME + ($uuid == null ? 43 : $uuid.hashCode());
        return result;
    }
}
