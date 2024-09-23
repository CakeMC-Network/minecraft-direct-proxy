package net.cakemc.de.crycodes.proxy.network.packet;

import io.netty.buffer.ByteBuf;
import net.cakemc.de.crycodes.proxy.protocol.Protocol;

/**
 * The type Protocol packet.
 */
public class ProtocolPacket {
    /**
     * The Packet.
     */
    public final AbstractPacket packet;
    /**
     * The Buf.
     */
    public final ByteBuf buf;
    /**
     * The Protocol.
     */
    public final Protocol protocol;
    private boolean released;

    /**
     * Instantiates a new Protocol packet.
     *
     * @param packet   the packet
     * @param buf      the buf
     * @param protocol the protocol
     */
    public ProtocolPacket(final AbstractPacket packet, final ByteBuf buf, final Protocol protocol) {
        this.packet = packet;
        this.buf = buf;
        this.protocol = protocol;
    }

    /**
     * Try single release.
     */
    public void trySingleRelease() {
        if (!released) {
            buf.release();
            released = true;
        }
    }

    /**
     * Gets packet.
     *
     * @return the packet
     */
    public AbstractPacket getPacket() {
        return packet;
    }

    /**
     * Gets buf.
     *
     * @return the buf
     */
    public ByteBuf getBuf() {
        return buf;
    }

    /**
     * Gets protocol.
     *
     * @return the protocol
     */
    public Protocol getProtocol() {
        return protocol;
    }

    /**
     * Is released boolean.
     *
     * @return the boolean
     */
    public boolean isReleased() {
        return released;
    }

    /**
     * Sets released.
     *
     * @param released the released
     */
    public void setReleased(final boolean released) {
        this.released = released;
    }
}
