package net.cakemc.de.crycodes.proxy.network.packet.impl.login;

import io.netty.buffer.ByteBuf;
import net.cakemc.de.crycodes.proxy.network.packet.AbstractPacket;
import net.cakemc.de.crycodes.proxy.network.packet.AbstractPacketHandler;
import net.cakemc.de.crycodes.proxy.protocol.ProtocolVersion;
import net.cakemc.mc.lib.game.PlayerProfile;

import java.util.Objects;
import java.util.UUID;

/**
 * The type TargetServer game profile packet.
 */
public class ServerGameProfilePacket extends AbstractPacket {
    private UUID uuid;
    private String username;
    private PlayerProfile.Property[] properties;

    /**
     * Instantiates a new TargetServer game profile packet.
     */
    public ServerGameProfilePacket() {
    }

    /**
     * Instantiates a new TargetServer game profile packet.
     *
     * @param uuid       the uuid
     * @param username   the username
     * @param properties the properties
     */
    public ServerGameProfilePacket(final UUID uuid, final String username, final PlayerProfile.Property[] properties) {
        this.uuid = uuid;
        this.username = username;
        this.properties = properties;
    }

    @Override
    public void read(ByteBuf buf, ProtocolVersion.Direction direction, int protocolVersion) {
        if (protocolVersion >= ProtocolVersion.MINECRAFT_1_16.getProtocolId()) {
            uuid = readUUID(buf);
        } else {
            uuid = UUID.fromString(readString(buf));
        }
        username = readString(buf);
        if (protocolVersion >= ProtocolVersion.MINECRAFT_1_19.getProtocolId()) {
            properties = readProperties(buf);
        }
        if (protocolVersion >= ProtocolVersion.MINECRAFT_1_20.getProtocolId() &&
                protocolVersion < ProtocolVersion.MINECRAFT_1_21_2.getProtocolId()) {
            // Whether the client should disconnect on its own if it receives invalid data from the server
            buf.readBoolean();
        }
    }

    @Override
    public void write(ByteBuf buf, ProtocolVersion.Direction direction, int protocolVersion) {
        if (protocolVersion >= ProtocolVersion.MINECRAFT_1_16.getProtocolId()) {
            writeUUID(uuid, buf);
        } else {
            writeString(uuid.toString(), buf);
        }
        writeString(username, buf);
        if (protocolVersion >= ProtocolVersion.MINECRAFT_1_19.getProtocolId()) {
            writeProperties(properties, buf);
        }
        if (protocolVersion >= ProtocolVersion.MINECRAFT_1_20_5.getProtocolId() &&
                protocolVersion < ProtocolVersion.MINECRAFT_1_21_2.getProtocolId()) {
            // Whether the client should disconnect on its own if it receives invalid data from the server
            // Vanilla sends true so we also send true
            buf.writeBoolean(true);
        }
    }

    @Override
    public void handle(AbstractPacketHandler handler) throws Exception {
        handler.handle(this);
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

    /**
     * Gets username.
     *
     * @return the username
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Sets username.
     *
     * @param username the username
     */
    public void setUsername(final String username) {
        this.username = username;
    }

    /**
     * Get properties player profile . property [ ].
     *
     * @return the player profile . property [ ]
     */
    public PlayerProfile.Property[] getProperties() {
        return this.properties;
    }

    /**
     * Sets properties.
     *
     * @param properties the properties
     */
    public void setProperties(final PlayerProfile.Property[] properties) {
        this.properties = properties;
    }

    @Override
    public String toString() {
        return "LoginSuccess(uuid=" + this.getUuid() + ", username=" + this.getUsername() + ", properties=" + java.util.Arrays.deepToString(this.getProperties()) + ")";
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof ServerGameProfilePacket other)) return false;
        if (!other.canEqual(this)) return false;
        final Object this$uuid = this.getUuid();
        final Object other$uuid = other.getUuid();
        if (!Objects.equals(this$uuid, other$uuid)) return false;
        final Object this$username = this.getUsername();
        final Object other$username = other.getUsername();
        if (!Objects.equals(this$username, other$username)) return false;
        return java.util.Arrays.deepEquals(this.getProperties(), other.getProperties());
    }

    /**
     * Can equal boolean.
     *
     * @param other the other
     * @return the boolean
     */
    protected boolean canEqual(final Object other) {
        return other instanceof ServerGameProfilePacket;
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $uuid = this.getUuid();
        result = result * PRIME + ($uuid == null ? 43 : $uuid.hashCode());
        final Object $username = this.getUsername();
        result = result * PRIME + ($username == null ? 43 : $username.hashCode());
        result = result * PRIME + java.util.Arrays.deepHashCode(this.getProperties());
        return result;
    }
}
