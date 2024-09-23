package net.cakemc.de.crycodes.proxy.protocol;

import java.util.Objects;

/**
 * The type Shifted world position.
 */
public class ShiftedWorldPosition {
    private final String dimension;
    private final long pos;

    /**
     * Instantiates a new Shifted world position.
     *
     * @param dimension the dimension
     * @param pos       the pos
     */
    public ShiftedWorldPosition(final String dimension, final long pos) {
        this.dimension = dimension;
        this.pos = pos;
    }

    /**
     * Gets dimension.
     *
     * @return the dimension
     */
    public String getDimension() {
        return this.dimension;
    }

    /**
     * Gets pos.
     *
     * @return the pos
     */
    public long getPos() {
        return this.pos;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof ShiftedWorldPosition other)) return false;
        if (!other.canEqual(this)) return false;
        if (this.getPos() != other.getPos()) return false;
        final Object this$dimension = this.getDimension();
        final Object other$dimension = other.getDimension();
        return Objects.equals(this$dimension, other$dimension);
    }

    /**
     * Can equal boolean.
     *
     * @param other the other
     * @return the boolean
     */
    protected boolean canEqual(final Object other) {
        return other instanceof ShiftedWorldPosition;
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final long $pos = this.getPos();
        result = result * PRIME + (int) ($pos >>> 32 ^ $pos);
        final Object $dimension = this.getDimension();
        result = result * PRIME + ($dimension == null ? 43 : $dimension.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "Location(dimension=" + this.getDimension() + ", pos=" + this.getPos() + ")";
    }
}
