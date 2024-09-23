package net.cakemc.de.crycodes.proxy.network.packet.impl;

import io.netty.buffer.ByteBuf;
import net.cakemc.de.crycodes.proxy.network.packet.AbstractPacket;
import net.cakemc.de.crycodes.proxy.network.packet.AbstractPacketHandler;

/**
 * The type Game state packet.
 */
public class GameStatePacket extends AbstractPacket {
    /**
     * The constant IMMEDIATE_RESPAWN.
     */
    public static final short IMMEDIATE_RESPAWN = 11;
    //
    private short state;
    private float value;

    /**
     * Instantiates a new Game state packet.
     */
    public GameStatePacket() {
    }

    /**
     * Instantiates a new Game state packet.
     *
     * @param state the state
     * @param value the value
     */
    public GameStatePacket(final short state, final float value) {
        this.state = state;
        this.value = value;
    }

    @Override
    public void read(ByteBuf buf) {
        state = buf.readUnsignedByte();
        value = buf.readFloat();
    }

    @Override
    public void write(ByteBuf buf) {
        buf.writeByte(state);
        buf.writeFloat(value);
    }

    @Override
    public void handle(AbstractPacketHandler handler) throws Exception {
        handler.handle(this);
    }

    /**
     * Gets state.
     *
     * @return the state
     */
    public short getState() {
        return this.state;
    }

    /**
     * Sets state.
     *
     * @param state the state
     */
    public void setState(final short state) {
        this.state = state;
    }

    /**
     * Gets value.
     *
     * @return the value
     */
    public float getValue() {
        return this.value;
    }

    /**
     * Sets value.
     *
     * @param value the value
     */
    public void setValue(final float value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "GameStatePacket(state=" + this.getState() + ", value=" + this.getValue() + ")";
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof GameStatePacket other)) return false;
        if (!other.canEqual(this)) return false;
        if (this.getState() != other.getState()) return false;
        return Float.compare(this.getValue(), other.getValue()) == 0;
    }

    /**
     * Can equal boolean.
     *
     * @param other the other
     * @return the boolean
     */
    protected boolean canEqual(final Object other) {
        return other instanceof GameStatePacket;
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + this.getState();
        result = result * PRIME + Float.floatToIntBits(this.getValue());
        return result;
    }
}
