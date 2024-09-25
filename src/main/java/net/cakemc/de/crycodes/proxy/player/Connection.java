package net.cakemc.de.crycodes.proxy.player;

import net.cakemc.de.crycodes.proxy.network.packet.AbstractPacket;
import net.cakemc.mc.lib.game.text.test.api.chat.BaseComponent;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

/**
 * The interface Connection.
 */
public interface Connection {

    /**
     * Gets address.
     *
     * @return the address
     */
    InetSocketAddress getAddress();

    /**
     * Gets socket address.
     *
     * @return the socket address
     */
    SocketAddress getSocketAddress();

    /**
     * Disconnect.
     *
     * @param reason the reason
     */
    void disconnect(String reason);

    /**
     * Disconnect.
     *
     * @param reason the reason
     */
    void disconnect(BaseComponent... reason);

    /**
     * Disconnect.
     *
     * @param reason the reason
     */
    void disconnect(BaseComponent reason);

    /**
     * Is connected boolean.
     *
     * @return the boolean
     */
    boolean isConnected();

    /**
     * Send packet.
     *
     * @param packet the packet
     */
    void sendPacket(AbstractPacket packet);

}
