package net.cakemc.de.crycodes.proxy.network.packet.impl;

import io.netty.buffer.ByteBuf;
import net.cakemc.de.crycodes.proxy.network.packet.AbstractPacket;
import net.cakemc.de.crycodes.proxy.network.packet.AbstractPacketHandler;
import net.cakemc.de.crycodes.proxy.protocol.ProtocolVersion;
import net.cakemc.de.crycodes.proxy.protocol.ShiftedWorldPosition;
import net.cakemc.mc.lib.game.nbt.NBTComponent;

import java.util.Objects;

/**
 * The type Respawn packet.
 */
public class RespawnPacket extends AbstractPacket {
    private Object dimension;
    private String worldName;
    private long seed;
    private short difficulty;
    private short gameMode;
    private short previousGameMode;
    private String levelType;
    private boolean debug;
    private boolean flat;
    private byte copyMeta;
    private ShiftedWorldPosition deathShiftedWorldPosition;
    private int portalCooldown;
    private int seaLevel;

    /**
     * Instantiates a new Respawn packet.
     */
    public RespawnPacket() {
    }

    /**
     * Instantiates a new Respawn packet.
     *
     * @param dimension                 the dimension
     * @param worldName                 the world name
     * @param seed                      the seed
     * @param difficulty                the difficulty
     * @param gameMode                  the game mode
     * @param previousGameMode          the previous game mode
     * @param levelType                 the level type
     * @param debug                     the debug
     * @param flat                      the flat
     * @param copyMeta                  the copy meta
     * @param deathShiftedWorldPosition the death shifted world position
     * @param portalCooldown            the portal cooldown
     * @param seaLevel                  the sea level
     */
    public RespawnPacket(final Object dimension, final String worldName, final long seed, final short difficulty, final short gameMode, final short previousGameMode, final String levelType, final boolean debug, final boolean flat, final byte copyMeta, final ShiftedWorldPosition deathShiftedWorldPosition, final int portalCooldown, final int seaLevel) {
        this.dimension = dimension;
        this.worldName = worldName;
        this.seed = seed;
        this.difficulty = difficulty;
        this.gameMode = gameMode;
        this.previousGameMode = previousGameMode;
        this.levelType = levelType;
        this.debug = debug;
        this.flat = flat;
        this.copyMeta = copyMeta;
        this.deathShiftedWorldPosition = deathShiftedWorldPosition;
        this.portalCooldown = portalCooldown;
        this.seaLevel = seaLevel;
    }

    @Override
    public void read(ByteBuf buf, ProtocolVersion.Direction direction, int protocolVersion) {
        if (protocolVersion >= ProtocolVersion.MINECRAFT_1_16.getProtocolId()) {
            if (protocolVersion >= ProtocolVersion.MINECRAFT_1_20_5.getProtocolId()) {
                dimension = readVarInt(buf);
            } else if (protocolVersion >= ProtocolVersion.MINECRAFT_1_16_2.getProtocolId() &&
                    protocolVersion < ProtocolVersion.MINECRAFT_1_19.getProtocolId()) {
                dimension = readTag(buf);
            } else {
                dimension = readString(buf);
            }
            worldName = readString(buf);
        } else {
            dimension = buf.readInt();
        }
        if (protocolVersion >= ProtocolVersion.MINECRAFT_1_15.getProtocolId()) {
            seed = buf.readLong();
        }
        if (protocolVersion < ProtocolVersion.MINECRAFT_1_14.getProtocolId()) {
            difficulty = buf.readUnsignedByte();
        }
        gameMode = buf.readUnsignedByte();
        if (protocolVersion >= ProtocolVersion.MINECRAFT_1_16.getProtocolId()) {
            previousGameMode = buf.readUnsignedByte();
            debug = buf.readBoolean();
            flat = buf.readBoolean();
            if (protocolVersion < ProtocolVersion.MINECRAFT_1_20_2.getProtocolId()) {
                copyMeta = buf.readByte();
            }
        } else {
            levelType = readString(buf);
        }
        if (protocolVersion >= ProtocolVersion.MINECRAFT_1_19.getProtocolId()) {
            if (buf.readBoolean()) {
                deathShiftedWorldPosition = new ShiftedWorldPosition(readString(buf), buf.readLong());
            }
        }
        if (protocolVersion >= ProtocolVersion.MINECRAFT_1_20.getProtocolId()) {
            portalCooldown = readVarInt(buf);
        }
        if (protocolVersion >= ProtocolVersion.MINECRAFT_1_21_2.getProtocolId()) {
            seaLevel = readVarInt(buf);
        }
        if (protocolVersion >= ProtocolVersion.MINECRAFT_1_20_2.getProtocolId()) {
            copyMeta = buf.readByte();
        }
    }

    @Override
    public void write(ByteBuf buf, ProtocolVersion.Direction direction, int protocolVersion) {
        if (protocolVersion >= ProtocolVersion.MINECRAFT_1_16.getProtocolId()) {
            if (protocolVersion >= ProtocolVersion.MINECRAFT_1_20_5.getProtocolId()) {
                writeVarInt((Integer) dimension, buf);
            } else if (protocolVersion >= ProtocolVersion.MINECRAFT_1_16_2.getProtocolId()
                    && protocolVersion < ProtocolVersion.MINECRAFT_1_19.getProtocolId()) {
                writeTag((NBTComponent) dimension, buf);
            } else {
                writeString((String) dimension, buf);
            }
            writeString(worldName, buf);
        } else {
            buf.writeInt(((Integer) dimension));
        }
        if (protocolVersion >= ProtocolVersion.MINECRAFT_1_15.getProtocolId()) {
            buf.writeLong(seed);
        }
        if (protocolVersion < ProtocolVersion.MINECRAFT_1_14.getProtocolId()) {
            buf.writeByte(difficulty);
        }
        buf.writeByte(gameMode);
        if (protocolVersion >= ProtocolVersion.MINECRAFT_1_16.getProtocolId()) {
            buf.writeByte(previousGameMode);
            buf.writeBoolean(debug);
            buf.writeBoolean(flat);
            if (protocolVersion < ProtocolVersion.MINECRAFT_1_20_2.getProtocolId()) {
                buf.writeByte(copyMeta);
            }
        } else {
            writeString(levelType, buf);
        }
        if (protocolVersion >= ProtocolVersion.MINECRAFT_1_19.getProtocolId()) {
            if (deathShiftedWorldPosition != null) {
                buf.writeBoolean(true);
                writeString(deathShiftedWorldPosition.getDimension(), buf);
                buf.writeLong(deathShiftedWorldPosition.getPos());
            } else {
                buf.writeBoolean(false);
            }
        }
        if (protocolVersion >= ProtocolVersion.MINECRAFT_1_20.getProtocolId()) {
            writeVarInt(portalCooldown, buf);
        }
        if (protocolVersion >= ProtocolVersion.MINECRAFT_1_21_2.getProtocolId()) {
            writeVarInt(seaLevel, buf);
        }
        if (protocolVersion >= ProtocolVersion.MINECRAFT_1_20_2.getProtocolId()) {
            buf.writeByte(copyMeta);
        }
    }

    @Override
    public void handle(AbstractPacketHandler handler) throws Exception {
        handler.handle(this);
    }

    /**
     * Gets dimension.
     *
     * @return the dimension
     */
    public Object getDimension() {
        return this.dimension;
    }

    /**
     * Sets dimension.
     *
     * @param dimension the dimension
     */
    public void setDimension(final Object dimension) {
        this.dimension = dimension;
    }

    /**
     * Gets world name.
     *
     * @return the world name
     */
    public String getWorldName() {
        return this.worldName;
    }

    /**
     * Sets world name.
     *
     * @param worldName the world name
     */
    public void setWorldName(final String worldName) {
        this.worldName = worldName;
    }

    /**
     * Gets seed.
     *
     * @return the seed
     */
    public long getSeed() {
        return this.seed;
    }

    /**
     * Sets seed.
     *
     * @param seed the seed
     */
    public void setSeed(final long seed) {
        this.seed = seed;
    }

    /**
     * Gets difficulty.
     *
     * @return the difficulty
     */
    public short getDifficulty() {
        return this.difficulty;
    }

    /**
     * Sets difficulty.
     *
     * @param difficulty the difficulty
     */
    public void setDifficulty(final short difficulty) {
        this.difficulty = difficulty;
    }

    /**
     * Gets game mode.
     *
     * @return the game mode
     */
    public short getGameMode() {
        return this.gameMode;
    }

    /**
     * Sets game mode.
     *
     * @param gameMode the game mode
     */
    public void setGameMode(final short gameMode) {
        this.gameMode = gameMode;
    }

    /**
     * Gets previous game mode.
     *
     * @return the previous game mode
     */
    public short getPreviousGameMode() {
        return this.previousGameMode;
    }

    /**
     * Sets previous game mode.
     *
     * @param previousGameMode the previous game mode
     */
    public void setPreviousGameMode(final short previousGameMode) {
        this.previousGameMode = previousGameMode;
    }

    /**
     * Gets level type.
     *
     * @return the level type
     */
    public String getLevelType() {
        return this.levelType;
    }

    /**
     * Sets level type.
     *
     * @param levelType the level type
     */
    public void setLevelType(final String levelType) {
        this.levelType = levelType;
    }

    /**
     * Is debug boolean.
     *
     * @return the boolean
     */
    public boolean isDebug() {
        return this.debug;
    }

    /**
     * Sets debug.
     *
     * @param debug the debug
     */
    public void setDebug(final boolean debug) {
        this.debug = debug;
    }

    /**
     * Is flat boolean.
     *
     * @return the boolean
     */
    public boolean isFlat() {
        return this.flat;
    }

    /**
     * Sets flat.
     *
     * @param flat the flat
     */
    public void setFlat(final boolean flat) {
        this.flat = flat;
    }

    /**
     * Gets copy meta.
     *
     * @return the copy meta
     */
    public byte getCopyMeta() {
        return this.copyMeta;
    }

    /**
     * Sets copy meta.
     *
     * @param copyMeta the copy meta
     */
    public void setCopyMeta(final byte copyMeta) {
        this.copyMeta = copyMeta;
    }

    /**
     * Gets death location.
     *
     * @return the death location
     */
    public ShiftedWorldPosition getDeathLocation() {
        return this.deathShiftedWorldPosition;
    }

    /**
     * Sets death location.
     *
     * @param deathShiftedWorldPosition the death shifted world position
     */
    public void setDeathLocation(final ShiftedWorldPosition deathShiftedWorldPosition) {
        this.deathShiftedWorldPosition = deathShiftedWorldPosition;
    }

    /**
     * Gets portal cooldown.
     *
     * @return the portal cooldown
     */
    public int getPortalCooldown() {
        return this.portalCooldown;
    }

    /**
     * Sets portal cooldown.
     *
     * @param portalCooldown the portal cooldown
     */
    public void setPortalCooldown(final int portalCooldown) {
        this.portalCooldown = portalCooldown;
    }

    /**
     * Gets sea level.
     *
     * @return the sea level
     */
    public int getSeaLevel() {
        return this.seaLevel;
    }

    /**
     * Sets sea level.
     *
     * @param seaLevel the sea level
     */
    public void setSeaLevel(final int seaLevel) {
        this.seaLevel = seaLevel;
    }

    @Override
    public String toString() {
        return "RespawnPacket(dimension=" + this.getDimension() + ", worldName=" + this.getWorldName() + ", seed=" + this.getSeed() + ", difficulty=" + this.getDifficulty() + ", gameMode=" + this.getGameMode() + ", previousGameMode=" + this.getPreviousGameMode() + ", levelType=" + this.getLevelType() + ", debug=" + this.isDebug() + ", flat=" + this.isFlat() + ", copyMeta=" + this.getCopyMeta() + ", deathLocation=" + this.getDeathLocation() + ", portalCooldown=" + this.getPortalCooldown() + ", seaLevel=" + this.getSeaLevel() + ")";
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof RespawnPacket other)) return false;
        if (!other.canEqual(this)) return false;
        if (this.getSeed() != other.getSeed()) return false;
        if (this.getDifficulty() != other.getDifficulty()) return false;
        if (this.getGameMode() != other.getGameMode()) return false;
        if (this.getPreviousGameMode() != other.getPreviousGameMode()) return false;
        if (this.isDebug() != other.isDebug()) return false;
        if (this.isFlat() != other.isFlat()) return false;
        if (this.getCopyMeta() != other.getCopyMeta()) return false;
        if (this.getPortalCooldown() != other.getPortalCooldown()) return false;
        if (this.getSeaLevel() != other.getSeaLevel()) return false;
        final Object this$dimension = this.getDimension();
        final Object other$dimension = other.getDimension();
        if (!Objects.equals(this$dimension, other$dimension)) return false;
        final Object this$worldName = this.getWorldName();
        final Object other$worldName = other.getWorldName();
        if (!Objects.equals(this$worldName, other$worldName)) return false;
        final Object this$levelType = this.getLevelType();
        final Object other$levelType = other.getLevelType();
        if (!Objects.equals(this$levelType, other$levelType)) return false;
        final Object this$deathLocation = this.getDeathLocation();
        final Object other$deathLocation = other.getDeathLocation();
        return Objects.equals(this$deathLocation, other$deathLocation);
    }

    /**
     * Can equal boolean.
     *
     * @param other the other
     * @return the boolean
     */
    protected boolean canEqual(final Object other) {
        return other instanceof RespawnPacket;
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final long $seed = this.getSeed();
        result = result * PRIME + (int) ($seed >>> 32 ^ $seed);
        result = result * PRIME + this.getDifficulty();
        result = result * PRIME + this.getGameMode();
        result = result * PRIME + this.getPreviousGameMode();
        result = result * PRIME + (this.isDebug() ? 79 : 97);
        result = result * PRIME + (this.isFlat() ? 79 : 97);
        result = result * PRIME + this.getCopyMeta();
        result = result * PRIME + this.getPortalCooldown();
        result = result * PRIME + this.getSeaLevel();
        final Object $dimension = this.getDimension();
        result = result * PRIME + ($dimension == null ? 43 : $dimension.hashCode());
        final Object $worldName = this.getWorldName();
        result = result * PRIME + ($worldName == null ? 43 : $worldName.hashCode());
        final Object $levelType = this.getLevelType();
        result = result * PRIME + ($levelType == null ? 43 : $levelType.hashCode());
        final Object $deathLocation = this.getDeathLocation();
        result = result * PRIME + ($deathLocation == null ? 43 : $deathLocation.hashCode());
        return result;
    }
}
