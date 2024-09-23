package net.cakemc.de.crycodes.proxy.network.packet.impl;

import io.netty.buffer.ByteBuf;
import net.cakemc.de.crycodes.proxy.network.packet.AbstractPacket;
import net.cakemc.de.crycodes.proxy.network.packet.AbstractPacketHandler;

/**
 * The type Set simulation distance packet.
 */
public class SetSimulationDistancePacket extends AbstractPacket {
    private int distance;

    /**
     * Instantiates a new Set simulation distance packet.
     */
    public SetSimulationDistancePacket() {
    }

    /**
     * Instantiates a new Set simulation distance packet.
     *
     * @param distance the distance
     */
    public SetSimulationDistancePacket(final int distance) {
        this.distance = distance;
    }

    @Override
    public void read(ByteBuf buf) {
        distance = AbstractPacket.readVarInt(buf);
    }

    @Override
    public void write(ByteBuf buf) {
        AbstractPacket.writeVarInt(distance, buf);
    }

    @Override
    public void handle(AbstractPacketHandler handler) throws Exception {
        handler.handle(this);
    }

    /**
     * Gets distance.
     *
     * @return the distance
     */
    public int getDistance() {
        return this.distance;
    }

    /**
     * Sets distance.
     *
     * @param distance the distance
     */
    public void setDistance(final int distance) {
        this.distance = distance;
    }

    @Override
    public String toString() {
        return "SetSimulationDistancePacket(distance=" + this.getDistance() + ")";
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof SetSimulationDistancePacket other)) return false;
        if (!other.canEqual(this)) return false;
        return this.getDistance() == other.getDistance();
    }

    /**
     * Can equal boolean.
     *
     * @param other the other
     * @return the boolean
     */
    protected boolean canEqual(final Object other) {
        return other instanceof SetSimulationDistancePacket;
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + this.getDistance();
        return result;
    }
}
