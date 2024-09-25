package net.cakemc.de.crycodes.proxy.network.packet.impl;

import io.netty.buffer.ByteBuf;
import net.cakemc.de.crycodes.proxy.network.packet.AbstractPacket;
import net.cakemc.de.crycodes.proxy.network.packet.AbstractPacketHandler;
import net.cakemc.de.crycodes.proxy.protocol.ProtocolVersion;
import net.cakemc.mc.lib.game.text.test.api.chat.BaseComponent;

import java.util.Objects;

/**
 * The type TargetServer data packet.
 */
public class ServerDataPacket extends AbstractPacket {
    private BaseComponent motd;
    private Object icon;
    private boolean preview;
    private boolean enforceSecure;

    /**
     * Instantiates a new TargetServer data packet.
     */
    public ServerDataPacket() {
    }

    /**
     * Instantiates a new TargetServer data packet.
     *
     * @param motd          the motd
     * @param icon          the icon
     * @param preview       the preview
     * @param enforceSecure the enforce secure
     */
    public ServerDataPacket(final BaseComponent motd, final Object icon, final boolean preview, final boolean enforceSecure) {
        this.motd = motd;
        this.icon = icon;
        this.preview = preview;
        this.enforceSecure = enforceSecure;
    }

    @Override
    public void read(ByteBuf buf, ProtocolVersion.Direction direction, int protocolVersion) {
        if (protocolVersion >= ProtocolVersion.MINECRAFT_1_19_4.getProtocolId() || buf.readBoolean()) {
            motd = net.cakemc.mc.lib.network.AbstractPacket.readComponent(buf);
        }
        if (buf.readBoolean()) {
            if (protocolVersion >= ProtocolVersion.MINECRAFT_1_19_4.getProtocolId()) {
                icon = readArray(buf);
            } else {
                icon = readString(buf);
            }
        }
        if (protocolVersion < ProtocolVersion.MINECRAFT_1_19_3.getProtocolId()) {
            preview = buf.readBoolean();
        }
        if (protocolVersion >= ProtocolVersion.MINECRAFT_1_19_1.getProtocolId() &&
                protocolVersion < ProtocolVersion.MINECRAFT_1_20_5.getProtocolId()) {
            enforceSecure = buf.readBoolean();
        }
    }

    @Override
    public void write(ByteBuf buf, ProtocolVersion.Direction direction, int protocolVersion) {
        if (motd != null) {
            if (protocolVersion >= ProtocolVersion.MINECRAFT_1_19_4.getProtocolId()) {
                buf.writeBoolean(true);
            }
            net.cakemc.mc.lib.network.AbstractPacket.writeComponent(buf, motd);
        } else {
            if (protocolVersion >= ProtocolVersion.MINECRAFT_1_19_4.getProtocolId()) {
                throw new IllegalArgumentException("MOTD required for this version");
            }
            buf.writeBoolean(false);
        }
        if (icon != null) {
            buf.writeBoolean(true);
            if (protocolVersion >= ProtocolVersion.MINECRAFT_1_19_4.getProtocolId()) {
                writeArray((byte[]) icon, buf);
            } else {
                writeString((String) icon, buf);
            }
        } else {
            buf.writeBoolean(false);
        }
        if (protocolVersion < ProtocolVersion.MINECRAFT_1_19_3.getProtocolId()) {
            buf.writeBoolean(preview);
        }
        if (protocolVersion >= ProtocolVersion.MINECRAFT_1_19_1.getProtocolId() &&
                protocolVersion < ProtocolVersion.MINECRAFT_1_20_5.getProtocolId()) {
            buf.writeBoolean(enforceSecure);
        }
    }

    @Override
    public void handle(AbstractPacketHandler handler) throws Exception {
        handler.handle(this);
    }

    /**
     * Gets motd.
     *
     * @return the motd
     */
    public BaseComponent getMotd() {
        return this.motd;
    }

    /**
     * Sets motd.
     *
     * @param motd the motd
     */
    public void setMotd(final BaseComponent motd) {
        this.motd = motd;
    }

    /**
     * Gets icon.
     *
     * @return the icon
     */
    public Object getIcon() {
        return this.icon;
    }

    /**
     * Sets icon.
     *
     * @param icon the icon
     */
    public void setIcon(final Object icon) {
        this.icon = icon;
    }

    /**
     * Is preview boolean.
     *
     * @return the boolean
     */
    public boolean isPreview() {
        return this.preview;
    }

    /**
     * Sets preview.
     *
     * @param preview the preview
     */
    public void setPreview(final boolean preview) {
        this.preview = preview;
    }

    /**
     * Is enforce secure boolean.
     *
     * @return the boolean
     */
    public boolean isEnforceSecure() {
        return this.enforceSecure;
    }

    /**
     * Sets enforce secure.
     *
     * @param enforceSecure the enforce secure
     */
    public void setEnforceSecure(final boolean enforceSecure) {
        this.enforceSecure = enforceSecure;
    }

    @Override
    public String toString() {
        return "ServerDataPacket(motd=" + this.getMotd() + ", icon=" + this.getIcon() + ", preview=" + this.isPreview() + ", enforceSecure=" + this.isEnforceSecure() + ")";
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof ServerDataPacket other)) return false;
        if (!other.canEqual(this)) return false;
        if (this.isPreview() != other.isPreview()) return false;
        if (this.isEnforceSecure() != other.isEnforceSecure()) return false;
        final Object this$motd = this.getMotd();
        final Object other$motd = other.getMotd();
        if (!Objects.equals(this$motd, other$motd)) return false;
        final Object this$icon = this.getIcon();
        final Object other$icon = other.getIcon();
        return Objects.equals(this$icon, other$icon);
    }

    /**
     * Can equal boolean.
     *
     * @param other the other
     * @return the boolean
     */
    protected boolean canEqual(final Object other) {
        return other instanceof ServerDataPacket;
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + (this.isPreview() ? 79 : 97);
        result = result * PRIME + (this.isEnforceSecure() ? 79 : 97);
        final Object $motd = this.getMotd();
        result = result * PRIME + ($motd == null ? 43 : $motd.hashCode());
        final Object $icon = this.getIcon();
        result = result * PRIME + ($icon == null ? 43 : $icon.hashCode());
        return result;
    }
}
