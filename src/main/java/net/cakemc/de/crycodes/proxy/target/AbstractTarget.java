package net.cakemc.de.crycodes.proxy.target;

import net.cakemc.de.crycodes.proxy.player.ProxiedPlayer;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Collection;

/**
 * The interface Abstract target.
 */
public interface AbstractTarget {

    /**
     * Gets name.
     *
     * @return the name
     */
    String getName();

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
     * Gets players.
     *
     * @return the players
     */
    Collection<ProxiedPlayer> getPlayers();

}
