package net.cakemc.de.crycodes.proxy.protocol;

/**
 * The type Protocol mapping.
 */
public class ProtocolMapping {
    /**
     * The Protocol version.
     */
    public final int protocolVersion;
    /**
     * The Packet id.
     */
    public final int packetID;

    /**
     * Instantiates a new Protocol mapping.
     *
     * @param protocolVersion the protocol version
     * @param packetID        the packet id
     */
    public ProtocolMapping(final int protocolVersion, final int packetID) {
        this.protocolVersion = protocolVersion;
        this.packetID = packetID;
    }

    /**
     * Gets protocol version.
     *
     * @return the protocol version
     */
    public int getProtocolVersion() {
        return this.protocolVersion;
    }

    /**
     * Gets packet id.
     *
     * @return the packet id
     */
    public int getPacketID() {
        return this.packetID;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof ProtocolMapping other)) return false;
        if (!other.canEqual(this)) return false;
        if (this.getProtocolVersion() != other.getProtocolVersion()) return false;
        return this.getPacketID() == other.getPacketID();
    }

    /**
     * Can equal boolean.
     *
     * @param other the other
     * @return the boolean
     */
    protected boolean canEqual(final Object other) {
        return other instanceof ProtocolMapping;
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + this.getProtocolVersion();
        result = result * PRIME + this.getPacketID();
        return result;
    }

    @Override
    public String toString() {
        return "Protocol.ProtocolMapping(protocolVersion=" + this.getProtocolVersion() + ", packetID=" + this.getPacketID() + ")";
    }
}
