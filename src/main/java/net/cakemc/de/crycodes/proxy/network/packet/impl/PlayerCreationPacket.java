package net.cakemc.de.crycodes.proxy.network.packet.impl;

import io.netty.buffer.ByteBuf;
import net.cakemc.de.crycodes.proxy.network.packet.AbstractPacket;
import net.cakemc.de.crycodes.proxy.network.packet.AbstractPacketHandler;
import net.cakemc.de.crycodes.proxy.protocol.ProtocolVersion;
import net.cakemc.de.crycodes.proxy.protocol.ShiftedWorldPosition;
import net.cakemc.mc.lib.game.nbt.NBTComponent;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * The type Player creation packet.
 */
public class PlayerCreationPacket extends AbstractPacket {
    private int entityId;
    private boolean hardcore;
    private short gameMode;
    private short previousGameMode;
    private Set<String> worldNames;
    private NBTComponent dimensions;
    private Object dimension;
    private String worldName;
    private long seed;
    private short difficulty;
    private int maxPlayers;
    private String levelType;
    private int viewDistance;
    private int simulationDistance;
    private boolean reducedDebugInfo;
    private boolean normalRespawn;
    private boolean limitedCrafting;
    private boolean debug;
    private boolean flat;
    private ShiftedWorldPosition deathShiftedWorldPosition;
    private int portalCooldown;
    private int seaLevel;
    private boolean secureProfile;

    /**
     * Instantiates a new Player creation packet.
     */
    public PlayerCreationPacket() {
    }

    /**
     * Instantiates a new Player creation packet.
     *
     * @param entityId                  the entity id
     * @param hardcore                  the hardcore
     * @param gameMode                  the game mode
     * @param previousGameMode          the previous game mode
     * @param worldNames                the world names
     * @param dimensions                the dimensions
     * @param dimension                 the dimension
     * @param worldName                 the world name
     * @param seed                      the seed
     * @param difficulty                the difficulty
     * @param maxPlayers                the max players
     * @param levelType                 the level type
     * @param viewDistance              the view distance
     * @param simulationDistance        the simulation distance
     * @param reducedDebugInfo          the reduced debug info
     * @param normalRespawn             the normal respawn
     * @param limitedCrafting           the limited crafting
     * @param debug                     the debug
     * @param flat                      the flat
     * @param deathShiftedWorldPosition the death shifted world position
     * @param portalCooldown            the portal cooldown
     * @param seaLevel                  the sea level
     * @param secureProfile             the secure profile
     */
    public PlayerCreationPacket(final int entityId, final boolean hardcore, final short gameMode, final short previousGameMode, final Set<String> worldNames,
                                final NBTComponent dimensions, final Object dimension, final String worldName, final long seed, final short difficulty,
                                final int maxPlayers, final String levelType, final int viewDistance, final int simulationDistance,
                                final boolean reducedDebugInfo, final boolean normalRespawn, final boolean limitedCrafting, final boolean debug,
                                final boolean flat, final ShiftedWorldPosition deathShiftedWorldPosition, final int portalCooldown, final int seaLevel, final boolean secureProfile) {
        this.entityId = entityId;
        this.hardcore = hardcore;
        this.gameMode = gameMode;
        this.previousGameMode = previousGameMode;
        this.worldNames = worldNames;
        this.dimensions = dimensions;
        this.dimension = dimension;
        this.worldName = worldName;
        this.seed = seed;
        this.difficulty = difficulty;
        this.maxPlayers = maxPlayers;
        this.levelType = levelType;
        this.viewDistance = viewDistance;
        this.simulationDistance = simulationDistance;
        this.reducedDebugInfo = reducedDebugInfo;
        this.normalRespawn = normalRespawn;
        this.limitedCrafting = limitedCrafting;
        this.debug = debug;
        this.flat = flat;
        this.deathShiftedWorldPosition = deathShiftedWorldPosition;
        this.portalCooldown = portalCooldown;
        this.seaLevel = seaLevel;
        this.secureProfile = secureProfile;
    }

    @Override
    public void read(ByteBuf buf, ProtocolVersion.Direction direction, int protocolVersion) {
        entityId = buf.readInt();
        if (protocolVersion >= ProtocolVersion.MINECRAFT_1_16_2.getProtocolId()) {
            hardcore = buf.readBoolean();
        }
        if (protocolVersion < ProtocolVersion.MINECRAFT_1_20_2.getProtocolId()) {
            gameMode = buf.readUnsignedByte();
        }
        if (protocolVersion >= ProtocolVersion.MINECRAFT_1_16.getProtocolId()) {
            if (protocolVersion < ProtocolVersion.MINECRAFT_1_20_2.getProtocolId()) {
                previousGameMode = buf.readUnsignedByte();
            }
            worldNames = new HashSet<>();
            int worldCount = readVarInt(buf);
            for (int i = 0; i < worldCount; i++) {
                worldNames.add(readString(buf));
            }
            if (protocolVersion < ProtocolVersion.MINECRAFT_1_20_2.getProtocolId()) {
                dimensions = readTag(buf);
            }
        }
        if (protocolVersion >= ProtocolVersion.MINECRAFT_1_16.getProtocolId()) {
            if (protocolVersion >= ProtocolVersion.MINECRAFT_1_16_2.getProtocolId() &&
                    protocolVersion < ProtocolVersion.MINECRAFT_1_19.getProtocolId()) {
                dimension = readTag(buf);
            } else if (protocolVersion < ProtocolVersion.MINECRAFT_1_20_2.getProtocolId()) {
                dimension = readString(buf);
            }
            if (protocolVersion < ProtocolVersion.MINECRAFT_1_20_2.getProtocolId()) {
                worldName = readString(buf);
            }
        } else if (protocolVersion > ProtocolVersion.MINECRAFT_1_9.getProtocolId()) {
            dimension = buf.readInt();
        } else {
            dimension = (int) buf.readByte();
        }
        if (protocolVersion >= ProtocolVersion.MINECRAFT_1_15.getProtocolId() &&
                protocolVersion < ProtocolVersion.MINECRAFT_1_20_2.getProtocolId()) {
            seed = buf.readLong();
        }
        if (protocolVersion < ProtocolVersion.MINECRAFT_1_14.getProtocolId()) {
            difficulty = buf.readUnsignedByte();
        }
        if (protocolVersion >= ProtocolVersion.MINECRAFT_1_16_2.getProtocolId()) {
            maxPlayers = readVarInt(buf);
        } else {
            maxPlayers = buf.readUnsignedByte();
        }
        if (protocolVersion < ProtocolVersion.MINECRAFT_1_16.getProtocolId()) {
            levelType = readString(buf);
        }
        if (protocolVersion >= ProtocolVersion.MINECRAFT_1_14.getProtocolId()) {
            viewDistance = readVarInt(buf);
        }
        if (protocolVersion >= ProtocolVersion.MINECRAFT_1_18.getProtocolId()) {
            simulationDistance = readVarInt(buf);
        }
        if (protocolVersion >= 29) {
            reducedDebugInfo = buf.readBoolean();
        }
        if (protocolVersion >= ProtocolVersion.MINECRAFT_1_15.getProtocolId()) {
            normalRespawn = buf.readBoolean();
        }
        if (protocolVersion >= ProtocolVersion.MINECRAFT_1_20_2.getProtocolId()) {
            limitedCrafting = buf.readBoolean();
            if (protocolVersion >= ProtocolVersion.MINECRAFT_1_20_5.getProtocolId()) {
                dimension = readVarInt(buf);
            } else {
                dimension = readString(buf);
            }
            worldName = readString(buf);
            seed = buf.readLong();
            gameMode = buf.readUnsignedByte();
            previousGameMode = buf.readByte();
        }
        if (protocolVersion >= ProtocolVersion.MINECRAFT_1_16.getProtocolId()) {
            debug = buf.readBoolean();
            flat = buf.readBoolean();
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
        if (protocolVersion >= ProtocolVersion.MINECRAFT_1_20_5.getProtocolId()) {
            secureProfile = buf.readBoolean();
        }
    }

    @Override
    public void write(ByteBuf buf, ProtocolVersion.Direction direction, int protocolVersion) {
        buf.writeInt(entityId);
        if (protocolVersion >= ProtocolVersion.MINECRAFT_1_16_2.getProtocolId()) {
            buf.writeBoolean(hardcore);
        }
        if (protocolVersion < ProtocolVersion.MINECRAFT_1_20_2.getProtocolId()) {
            buf.writeByte(gameMode);
        }
        if (protocolVersion >= ProtocolVersion.MINECRAFT_1_16.getProtocolId()) {
            if (protocolVersion < ProtocolVersion.MINECRAFT_1_20_2.getProtocolId()) {
                buf.writeByte(previousGameMode);
            }
            writeVarInt(worldNames.size(), buf);
            for (String world : worldNames) {
                writeString(world, buf);
            }
            if (protocolVersion < ProtocolVersion.MINECRAFT_1_20_2.getProtocolId()) {
                writeTag(dimensions, buf);
            }
        }
        if (protocolVersion >= ProtocolVersion.MINECRAFT_1_16.getProtocolId()) {
            if (protocolVersion >= ProtocolVersion.MINECRAFT_1_16_2.getProtocolId() &&
                    protocolVersion < ProtocolVersion.MINECRAFT_1_19.getProtocolId()) {
                writeTag((NBTComponent) dimension, buf);
            } else if (protocolVersion < ProtocolVersion.MINECRAFT_1_20_2.getProtocolId()) {
                writeString((String) dimension, buf);
            }
            if (protocolVersion < ProtocolVersion.MINECRAFT_1_20_2.getProtocolId()) {
                writeString(worldName, buf);
            }
        } else if (protocolVersion > ProtocolVersion.MINECRAFT_1_9.getProtocolId()) {
            buf.writeInt((Integer) dimension);
        } else {
            buf.writeByte((Integer) dimension);
        }
        if (protocolVersion >= ProtocolVersion.MINECRAFT_1_15.getProtocolId()) {
            if (protocolVersion < ProtocolVersion.MINECRAFT_1_20_2.getProtocolId()) {
                buf.writeLong(seed);
            }
        }
        if (protocolVersion < ProtocolVersion.MINECRAFT_1_14.getProtocolId()) {
            buf.writeByte(difficulty);
        }
        if (protocolVersion >= ProtocolVersion.MINECRAFT_1_16_2.getProtocolId()) {
            writeVarInt(maxPlayers, buf);
        } else {
            buf.writeByte(maxPlayers);
        }
        if (protocolVersion < ProtocolVersion.MINECRAFT_1_16.getProtocolId()) {
            writeString(levelType, buf);
        }
        if (protocolVersion >= ProtocolVersion.MINECRAFT_1_14.getProtocolId()) {
            writeVarInt(viewDistance, buf);
        }
        if (protocolVersion >= ProtocolVersion.MINECRAFT_1_18.getProtocolId()) {
            writeVarInt(simulationDistance, buf);
        }
        if (protocolVersion >= 29) {
            buf.writeBoolean(reducedDebugInfo);
        }
        if (protocolVersion >= ProtocolVersion.MINECRAFT_1_15.getProtocolId()) {
            buf.writeBoolean(normalRespawn);
        }
        if (protocolVersion >= ProtocolVersion.MINECRAFT_1_20_2.getProtocolId()) {
            buf.writeBoolean(limitedCrafting);
            if (protocolVersion >= ProtocolVersion.MINECRAFT_1_20_5.getProtocolId()) {
                writeVarInt((Integer) dimension, buf);
            } else {
                writeString((String) dimension, buf);
            }
            writeString(worldName, buf);
            buf.writeLong(seed);
            buf.writeByte(gameMode);
            buf.writeByte(previousGameMode);
        }
        if (protocolVersion >= ProtocolVersion.MINECRAFT_1_16.getProtocolId()) {
            buf.writeBoolean(debug);
            buf.writeBoolean(flat);
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
        if (protocolVersion >= ProtocolVersion.MINECRAFT_1_20_5.getProtocolId()) {
            buf.writeBoolean(secureProfile);
        }
    }

    @Override
    public void handle(AbstractPacketHandler handler) throws Exception {
        handler.handle(this);
    }

    /**
     * Gets entity id.
     *
     * @return the entity id
     */
    public int getEntityId() {
        return this.entityId;
    }

    /**
     * Sets entity id.
     *
     * @param entityId the entity id
     */
    public void setEntityId(final int entityId) {
        this.entityId = entityId;
    }

    /**
     * Is hardcore boolean.
     *
     * @return the boolean
     */
    public boolean isHardcore() {
        return this.hardcore;
    }

    /**
     * Sets hardcore.
     *
     * @param hardcore the hardcore
     */
    public void setHardcore(final boolean hardcore) {
        this.hardcore = hardcore;
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
     * Gets world names.
     *
     * @return the world names
     */
    public Set<String> getWorldNames() {
        return this.worldNames;
    }

    /**
     * Sets world names.
     *
     * @param worldNames the world names
     */
    public void setWorldNames(final Set<String> worldNames) {
        this.worldNames = worldNames;
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
     * Gets max players.
     *
     * @return the max players
     */
    public int getMaxPlayers() {
        return this.maxPlayers;
    }

    /**
     * Sets max players.
     *
     * @param maxPlayers the max players
     */
    public void setMaxPlayers(final int maxPlayers) {
        this.maxPlayers = maxPlayers;
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
     * Gets view distance.
     *
     * @return the view distance
     */
    public int getViewDistance() {
        return this.viewDistance;
    }

    /**
     * Sets view distance.
     *
     * @param viewDistance the view distance
     */
    public void setViewDistance(final int viewDistance) {
        this.viewDistance = viewDistance;
    }

    /**
     * Gets simulation distance.
     *
     * @return the simulation distance
     */
    public int getSimulationDistance() {
        return this.simulationDistance;
    }

    /**
     * Sets simulation distance.
     *
     * @param simulationDistance the simulation distance
     */
    public void setSimulationDistance(final int simulationDistance) {
        this.simulationDistance = simulationDistance;
    }

    /**
     * Is reduced debug info boolean.
     *
     * @return the boolean
     */
    public boolean isReducedDebugInfo() {
        return this.reducedDebugInfo;
    }

    /**
     * Sets reduced debug info.
     *
     * @param reducedDebugInfo the reduced debug info
     */
    public void setReducedDebugInfo(final boolean reducedDebugInfo) {
        this.reducedDebugInfo = reducedDebugInfo;
    }

    /**
     * Is normal respawn boolean.
     *
     * @return the boolean
     */
    public boolean isNormalRespawn() {
        return this.normalRespawn;
    }

    /**
     * Sets normal respawn.
     *
     * @param normalRespawn the normal respawn
     */
    public void setNormalRespawn(final boolean normalRespawn) {
        this.normalRespawn = normalRespawn;
    }

    /**
     * Is limited crafting boolean.
     *
     * @return the boolean
     */
    public boolean isLimitedCrafting() {
        return this.limitedCrafting;
    }

    /**
     * Sets limited crafting.
     *
     * @param limitedCrafting the limited crafting
     */
    public void setLimitedCrafting(final boolean limitedCrafting) {
        this.limitedCrafting = limitedCrafting;
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

    /**
     * Is secure profile boolean.
     *
     * @return the boolean
     */
    public boolean isSecureProfile() {
        return this.secureProfile;
    }

    /**
     * Sets secure profile.
     *
     * @param secureProfile the secure profile
     */
    public void setSecureProfile(final boolean secureProfile) {
        this.secureProfile = secureProfile;
    }

    /**
     * Gets dimensions.
     *
     * @return the dimensions
     */
    public NBTComponent getDimensions() {
        return dimensions;
    }

    /**
     * Sets dimensions.
     *
     * @param dimensions the dimensions
     */
    public void setDimensions(final NBTComponent dimensions) {
        this.dimensions = dimensions;
    }

    @Override
    public String toString() {
        return "Login(entityId=" + this.getEntityId() + ", hardcore=" + this.isHardcore() + ", gameMode=" + this.getGameMode() + ", previousGameMode=" + this.getPreviousGameMode() + ", worldNames=" + this.getWorldNames() + ", dimensions=" + this.getDimensions() + ", dimension=" + this.getDimension() + ", worldName=" + this.getWorldName() + ", seed=" + this.getSeed() + ", difficulty=" + this.getDifficulty() + ", maxPlayers=" + this.getMaxPlayers() + ", levelType=" + this.getLevelType() + ", viewDistance=" + this.getViewDistance() + ", simulationDistance=" + this.getSimulationDistance() + ", reducedDebugInfo=" + this.isReducedDebugInfo() + ", normalRespawn=" + this.isNormalRespawn() + ", limitedCrafting=" + this.isLimitedCrafting() + ", debug=" + this.isDebug() + ", flat=" + this.isFlat() + ", deathLocation=" + this.getDeathLocation() + ", portalCooldown=" + this.getPortalCooldown() + ", seaLevel=" + this.getSeaLevel() + ", secureProfile=" + this.isSecureProfile() + ")";
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof PlayerCreationPacket other)) return false;
        if (!other.canEqual(this)) return false;
        if (this.getEntityId() != other.getEntityId()) return false;
        if (this.isHardcore() != other.isHardcore()) return false;
        if (this.getGameMode() != other.getGameMode()) return false;
        if (this.getPreviousGameMode() != other.getPreviousGameMode()) return false;
        if (this.getSeed() != other.getSeed()) return false;
        if (this.getDifficulty() != other.getDifficulty()) return false;
        if (this.getMaxPlayers() != other.getMaxPlayers()) return false;
        if (this.getViewDistance() != other.getViewDistance()) return false;
        if (this.getSimulationDistance() != other.getSimulationDistance()) return false;
        if (this.isReducedDebugInfo() != other.isReducedDebugInfo()) return false;
        if (this.isNormalRespawn() != other.isNormalRespawn()) return false;
        if (this.isLimitedCrafting() != other.isLimitedCrafting()) return false;
        if (this.isDebug() != other.isDebug()) return false;
        if (this.isFlat() != other.isFlat()) return false;
        if (this.getPortalCooldown() != other.getPortalCooldown()) return false;
        if (this.getSeaLevel() != other.getSeaLevel()) return false;
        if (this.isSecureProfile() != other.isSecureProfile()) return false;
        final Object this$worldNames = this.getWorldNames();
        final Object other$worldNames = other.getWorldNames();
        if (!Objects.equals(this$worldNames, other$worldNames))
            return false;
        final Object this$dimensions = this.getDimensions();
        final Object other$dimensions = other.getDimensions();
        if (!Objects.equals(this$dimensions, other$dimensions))
            return false;
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
        return other instanceof PlayerCreationPacket;
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + this.getEntityId();
        result = result * PRIME + (this.isHardcore() ? 79 : 97);
        result = result * PRIME + this.getGameMode();
        result = result * PRIME + this.getPreviousGameMode();
        final long $seed = this.getSeed();
        result = result * PRIME + (int) ($seed >>> 32 ^ $seed);
        result = result * PRIME + this.getDifficulty();
        result = result * PRIME + this.getMaxPlayers();
        result = result * PRIME + this.getViewDistance();
        result = result * PRIME + this.getSimulationDistance();
        result = result * PRIME + (this.isReducedDebugInfo() ? 79 : 97);
        result = result * PRIME + (this.isNormalRespawn() ? 79 : 97);
        result = result * PRIME + (this.isLimitedCrafting() ? 79 : 97);
        result = result * PRIME + (this.isDebug() ? 79 : 97);
        result = result * PRIME + (this.isFlat() ? 79 : 97);
        result = result * PRIME + this.getPortalCooldown();
        result = result * PRIME + this.getSeaLevel();
        result = result * PRIME + (this.isSecureProfile() ? 79 : 97);
        final Object $worldNames = this.getWorldNames();
        result = result * PRIME + ($worldNames == null ? 43 : $worldNames.hashCode());
        final Object $dimensions = this.getDimensions();
        result = result * PRIME + ($dimensions == null ? 43 : $dimensions.hashCode());
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
