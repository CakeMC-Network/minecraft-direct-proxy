package net.cakemc.de.crycodes.proxy.network;

import net.cakemc.de.crycodes.proxy.channel.PlayerChannel;
import net.cakemc.de.crycodes.proxy.network.packet.AbstractPacketHandler;
import net.cakemc.de.crycodes.proxy.network.packet.ProtocolPacket;

/**
 * The type Packet handler.
 */
public abstract class PacketHandler extends AbstractPacketHandler {

    @Override
    public abstract String toString();

    /**
     * Should handle boolean.
     *
     * @param packet the packet
     * @return the boolean
     * @throws Exception the exception
     */
    public boolean shouldHandle(ProtocolPacket packet) throws Exception {
        return true;
    }

    /**
     * Exception.
     *
     * @param t the t
     * @throws Exception the exception
     */
    public void exception(Throwable t) throws Exception {
    }

    /**
     * Handle.
     *
     * @param packet the packet
     * @throws Exception the exception
     */
    public void handle(ProtocolPacket packet) throws Exception {
    }

    /**
     * Connected.
     *
     * @param channel the channel
     * @throws Exception the exception
     */
    public void connected(PlayerChannel channel) throws Exception {
    }

    /**
     * Disconnected.
     *
     * @param channel the channel
     * @throws Exception the exception
     */
    public void disconnected(PlayerChannel channel) throws Exception {
    }

    /**
     * Writability changed.
     *
     * @param channel the channel
     * @throws Exception the exception
     */
    public void writabilityChanged(PlayerChannel channel) throws Exception {
    }
}
