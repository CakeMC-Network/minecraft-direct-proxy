package net.cakemc.de.crycodes.proxy.network.packet.impl;

import io.netty.buffer.ByteBuf;
import net.cakemc.de.crycodes.proxy.network.packet.AbstractPacket;
import net.cakemc.de.crycodes.proxy.network.packet.AbstractPacketHandler;

/**
 * The type Entity status packet.
 */
public class EntityStatusPacket extends AbstractPacket {
    /**
     * The constant DEBUG_INFO_REDUCED.
     */
    public static final byte DEBUG_INFO_REDUCED = 22;
    /**
     * The constant DEBUG_INFO_NORMAL.
     */
    public static final byte DEBUG_INFO_NORMAL = 23;
    //
    private int entityId;
    private byte status;

    /**
     * Instantiates a new Entity status packet.
     */
    public EntityStatusPacket() {
    }

    /**
     * Instantiates a new Entity status packet.
     *
     * @param entityId the entity id
     * @param status   the status
     */
    public EntityStatusPacket(final int entityId, final byte status) {
        this.entityId = entityId;
        this.status = status;
    }

    @Override
    public void read(ByteBuf buf) {
        entityId = buf.readInt();
        status = buf.readByte();
    }

    @Override
    public void write(ByteBuf buf) {
        buf.writeInt(entityId);
        buf.writeByte(status);
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
     * Gets status.
     *
     * @return the status
     */
    public byte getStatus() {
        return this.status;
    }

    /**
     * Sets status.
     *
     * @param status the status
     */
    public void setStatus(final byte status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "EntityStatusPacket(entityId=" + this.getEntityId() + ", status=" + this.getStatus() + ")";
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof EntityStatusPacket other)) return false;
        if (!other.canEqual(this)) return false;
        if (this.getEntityId() != other.getEntityId()) return false;
        return this.getStatus() == other.getStatus();
    }

    /**
     * Can equal boolean.
     *
     * @param other the other
     * @return the boolean
     */
    protected boolean canEqual(final Object other) {
        return other instanceof EntityStatusPacket;
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + this.getEntityId();
        result = result * PRIME + this.getStatus();
        return result;
    }
}
