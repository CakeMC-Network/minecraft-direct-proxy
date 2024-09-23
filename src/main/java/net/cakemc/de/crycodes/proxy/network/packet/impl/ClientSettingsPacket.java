package net.cakemc.de.crycodes.proxy.network.packet.impl;

import io.netty.buffer.ByteBuf;
import net.cakemc.de.crycodes.proxy.network.packet.AbstractPacket;
import net.cakemc.de.crycodes.proxy.network.packet.AbstractPacketHandler;
import net.cakemc.de.crycodes.proxy.protocol.ProtocolVersion;

import java.util.Objects;

/**
 * The type Client settings packet.
 */
public class ClientSettingsPacket extends AbstractPacket {
    private String locale;
    private byte viewDistance;
    private int chatFlags;
    private boolean chatColours;
    private byte difficulty;
    private byte skinParts;
    private int mainHand;
    private boolean disableTextFiltering;
    private boolean allowServerListing;
    private ParticleStatus particleStatus;

    /**
     * Instantiates a new Client settings packet.
     */
    public ClientSettingsPacket() {
    }

    /**
     * Instantiates a new Client settings packet.
     *
     * @param locale               the locale
     * @param viewDistance         the view distance
     * @param chatFlags            the chat flags
     * @param chatColours          the chat colours
     * @param difficulty           the difficulty
     * @param skinParts            the skin parts
     * @param mainHand             the main hand
     * @param disableTextFiltering the disable text filtering
     * @param allowServerListing   the allow server listing
     * @param particleStatus       the particle status
     */
    public ClientSettingsPacket(final String locale, final byte viewDistance, final int chatFlags, final boolean chatColours, final byte difficulty, final byte skinParts, final int mainHand, final boolean disableTextFiltering, final boolean allowServerListing, final ParticleStatus particleStatus) {
        this.locale = locale;
        this.viewDistance = viewDistance;
        this.chatFlags = chatFlags;
        this.chatColours = chatColours;
        this.difficulty = difficulty;
        this.skinParts = skinParts;
        this.mainHand = mainHand;
        this.disableTextFiltering = disableTextFiltering;
        this.allowServerListing = allowServerListing;
        this.particleStatus = particleStatus;
    }

    @Override
    public void read(ByteBuf buf, ProtocolVersion.Direction direction, int protocolVersion) {
        locale = readString(buf, 16);
        viewDistance = buf.readByte();
        chatFlags = protocolVersion >= ProtocolVersion.MINECRAFT_1_9.getProtocolId() ? AbstractPacket.readVarInt(buf) : buf.readUnsignedByte();
        chatColours = buf.readBoolean();
        skinParts = buf.readByte();
        if (protocolVersion >= ProtocolVersion.MINECRAFT_1_9.getProtocolId()) {
            mainHand = AbstractPacket.readVarInt(buf);
        }
        if (protocolVersion >= ProtocolVersion.MINECRAFT_1_17.getProtocolId()) {
            disableTextFiltering = buf.readBoolean();
        }
        if (protocolVersion >= ProtocolVersion.MINECRAFT_1_18.getProtocolId()) {
            allowServerListing = buf.readBoolean();
        }
        if (protocolVersion >= ProtocolVersion.MINECRAFT_1_21_2.getProtocolId()) {
            particleStatus = ParticleStatus.values()[readVarInt(buf)];
        }
    }

    @Override
    public void write(ByteBuf buf, ProtocolVersion.Direction direction, int protocolVersion) {
        writeString(locale, buf);
        buf.writeByte(viewDistance);
        if (protocolVersion >= ProtocolVersion.MINECRAFT_1_9.getProtocolId()) {
            AbstractPacket.writeVarInt(chatFlags, buf);
        } else {
            buf.writeByte(chatFlags);
        }
        buf.writeBoolean(chatColours);
        buf.writeByte(skinParts);
        if (protocolVersion >= ProtocolVersion.MINECRAFT_1_9.getProtocolId()) {
            AbstractPacket.writeVarInt(mainHand, buf);
        }
        if (protocolVersion >= ProtocolVersion.MINECRAFT_1_17.getProtocolId()) {
            buf.writeBoolean(disableTextFiltering);
        }
        if (protocolVersion >= ProtocolVersion.MINECRAFT_1_18.getProtocolId()) {
            buf.writeBoolean(allowServerListing);
        }
        if (protocolVersion >= ProtocolVersion.MINECRAFT_1_21_2.getProtocolId()) {
            writeVarInt(particleStatus.ordinal(), buf);
        }
    }

    @Override
    public void handle(AbstractPacketHandler handler) throws Exception {
        handler.handle(this);
    }

    /**
     * Gets locale.
     *
     * @return the locale
     */
    public String getLocale() {
        return this.locale;
    }

    /**
     * Sets locale.
     *
     * @param locale the locale
     */
    public void setLocale(final String locale) {
        this.locale = locale;
    }

    /**
     * Gets view distance.
     *
     * @return the view distance
     */
    public byte getViewDistance() {
        return this.viewDistance;
    }

    /**
     * Sets view distance.
     *
     * @param viewDistance the view distance
     */
    public void setViewDistance(final byte viewDistance) {
        this.viewDistance = viewDistance;
    }

    /**
     * Gets chat flags.
     *
     * @return the chat flags
     */
    public int getChatFlags() {
        return this.chatFlags;
    }

    /**
     * Sets chat flags.
     *
     * @param chatFlags the chat flags
     */
    public void setChatFlags(final int chatFlags) {
        this.chatFlags = chatFlags;
    }

    /**
     * Is chat colours boolean.
     *
     * @return the boolean
     */
    public boolean isChatColours() {
        return this.chatColours;
    }

    /**
     * Sets chat colours.
     *
     * @param chatColours the chat colours
     */
    public void setChatColours(final boolean chatColours) {
        this.chatColours = chatColours;
    }

    /**
     * Gets difficulty.
     *
     * @return the difficulty
     */
    public byte getDifficulty() {
        return this.difficulty;
    }

    /**
     * Sets difficulty.
     *
     * @param difficulty the difficulty
     */
    public void setDifficulty(final byte difficulty) {
        this.difficulty = difficulty;
    }

    /**
     * Gets skin parts.
     *
     * @return the skin parts
     */
    public byte getSkinParts() {
        return this.skinParts;
    }

    /**
     * Sets skin parts.
     *
     * @param skinParts the skin parts
     */
    public void setSkinParts(final byte skinParts) {
        this.skinParts = skinParts;
    }

    /**
     * Gets main hand.
     *
     * @return the main hand
     */
    public int getMainHand() {
        return this.mainHand;
    }

    /**
     * Sets main hand.
     *
     * @param mainHand the main hand
     */
    public void setMainHand(final int mainHand) {
        this.mainHand = mainHand;
    }

    /**
     * Is disable text filtering boolean.
     *
     * @return the boolean
     */
    public boolean isDisableTextFiltering() {
        return this.disableTextFiltering;
    }

    /**
     * Sets disable text filtering.
     *
     * @param disableTextFiltering the disable text filtering
     */
    public void setDisableTextFiltering(final boolean disableTextFiltering) {
        this.disableTextFiltering = disableTextFiltering;
    }

    /**
     * Is allow server listing boolean.
     *
     * @return the boolean
     */
    public boolean isAllowServerListing() {
        return this.allowServerListing;
    }

    /**
     * Sets allow server listing.
     *
     * @param allowServerListing the allow server listing
     */
    public void setAllowServerListing(final boolean allowServerListing) {
        this.allowServerListing = allowServerListing;
    }

    /**
     * Gets particle status.
     *
     * @return the particle status
     */
    public ParticleStatus getParticleStatus() {
        return this.particleStatus;
    }

    /**
     * Sets particle status.
     *
     * @param particleStatus the particle status
     */
    public void setParticleStatus(final ParticleStatus particleStatus) {
        this.particleStatus = particleStatus;
    }

    @Override
    public String toString() {
        return "ClientSettings(locale=" + this.getLocale() + ", viewDistance=" + this.getViewDistance() + ", chatFlags=" + this.getChatFlags() + ", chatColours=" + this.isChatColours() + ", difficulty=" + this.getDifficulty() + ", skinParts=" + this.getSkinParts() + ", mainHand=" + this.getMainHand() + ", disableTextFiltering=" + this.isDisableTextFiltering() + ", allowServerListing=" + this.isAllowServerListing() + ", particleStatus=" + this.getParticleStatus() + ")";
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof ClientSettingsPacket other)) return false;
        if (!other.canEqual(this)) return false;
        if (this.getViewDistance() != other.getViewDistance()) return false;
        if (this.getChatFlags() != other.getChatFlags()) return false;
        if (this.isChatColours() != other.isChatColours()) return false;
        if (this.getDifficulty() != other.getDifficulty()) return false;
        if (this.getSkinParts() != other.getSkinParts()) return false;
        if (this.getMainHand() != other.getMainHand()) return false;
        if (this.isDisableTextFiltering() != other.isDisableTextFiltering()) return false;
        if (this.isAllowServerListing() != other.isAllowServerListing()) return false;
        final Object this$locale = this.getLocale();
        final Object other$locale = other.getLocale();
        if (!Objects.equals(this$locale, other$locale)) return false;
        final Object this$particleStatus = this.getParticleStatus();
        final Object other$particleStatus = other.getParticleStatus();
        return Objects.equals(this$particleStatus, other$particleStatus);
    }

    /**
     * Can equal boolean.
     *
     * @param other the other
     * @return the boolean
     */
    protected boolean canEqual(final Object other) {
        return other instanceof ClientSettingsPacket;
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + this.getViewDistance();
        result = result * PRIME + this.getChatFlags();
        result = result * PRIME + (this.isChatColours() ? 79 : 97);
        result = result * PRIME + this.getDifficulty();
        result = result * PRIME + this.getSkinParts();
        result = result * PRIME + this.getMainHand();
        result = result * PRIME + (this.isDisableTextFiltering() ? 79 : 97);
        result = result * PRIME + (this.isAllowServerListing() ? 79 : 97);
        final Object $locale = this.getLocale();
        result = result * PRIME + ($locale == null ? 43 : $locale.hashCode());
        final Object $particleStatus = this.getParticleStatus();
        result = result * PRIME + ($particleStatus == null ? 43 : $particleStatus.hashCode());
        return result;
    }

    /**
     * The enum Particle status.
     */
    public enum ParticleStatus {
        /**
         * All particle status.
         */
        ALL,
        /**
         * Decreased particle status.
         */
        DECREASED,
        /**
         * Minimal particle status.
         */
        MINIMAL
    }
}
