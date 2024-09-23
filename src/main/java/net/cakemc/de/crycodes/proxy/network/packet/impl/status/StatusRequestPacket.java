package net.cakemc.de.crycodes.proxy.network.packet.impl.status;

import io.netty.buffer.ByteBuf;
import net.cakemc.de.crycodes.proxy.network.packet.AbstractPacket;
import net.cakemc.de.crycodes.proxy.network.packet.AbstractPacketHandler;

/**
 * The type Status request packet.
 */
public class StatusRequestPacket extends AbstractPacket {
    /**
     * Instantiates a new Status request packet.
     */
    public StatusRequestPacket() {
    }

    @Override
    public void read(ByteBuf buf) {
    }

    @Override
    public void write(ByteBuf buf) {
    }

    @Override
    public void handle(AbstractPacketHandler handler) throws Exception {
        handler.handle(this);
    }

    @Override
    public String toString() {
        return "StatusRequestPacket()";
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof StatusRequestPacket other)) return false;
        return other.canEqual(this);
    }

    /**
     * Can equal boolean.
     *
     * @param other the other
     * @return the boolean
     */
    protected boolean canEqual(final Object other) {
        return other instanceof StatusRequestPacket;
    }

    @Override
    public int hashCode() {
        final int result = 1;
        return result;
    }
}
