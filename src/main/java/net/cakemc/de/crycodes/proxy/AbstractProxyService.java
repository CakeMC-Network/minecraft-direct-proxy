package net.cakemc.de.crycodes.proxy;


import net.cakemc.de.crycodes.proxy.player.ConnectedPlayer;
import net.cakemc.de.crycodes.proxy.player.ProxiedPlayer;
import net.cakemc.de.crycodes.proxy.target.AbstractTarget;
import net.cakemc.mc.lib.game.event.EventManager;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.List;
import java.util.UUID;

/**
 * The type Abstract proxy service.
 */
public abstract class AbstractProxyService {

    /**
     * Gets default target.
     *
     * @return the default target
     */
    public abstract AbstractTarget getDefaultTarget();

    /**
     * Gets players.
     *
     * @return the players
     */
    public abstract List<? extends ProxiedPlayer> getPlayers();

    /**
     * Gets player.
     *
     * @param name the name
     * @return the player
     */
    public abstract ProxiedPlayer getPlayer(String name);

    /**
     * Gets player.
     *
     * @param uuid the uuid
     * @return the player
     */
    public abstract ProxiedPlayer getPlayer(UUID uuid);

    /**
     * Gets targets.
     *
     * @return the targets
     */
    public abstract List<AbstractTarget> getTargets();

    /**
     * Gets target by name.
     *
     * @param name the name
     * @return the target by name
     */
    public abstract AbstractTarget getTargetByName(String name);

    /**
     * Add connection boolean.
     *
     * @param con the con
     * @return the boolean
     */
    public abstract boolean addConnection(ConnectedPlayer con);

    /**
     * Remove connection.
     *
     * @param con the con
     */
    public abstract void removeConnection(ConnectedPlayer con);

    /**
     * Gets event manager.
     *
     * @return the event manager
     */
    public abstract EventManager getEventManager();

    /**
     * Stop.
     */
    public abstract void stop();

    /**
     * Gets game version.
     *
     * @return the game version
     */
    public abstract String getGameVersion();

    /**
     * Gets protocol version.
     *
     * @return the protocol version
     */
    public abstract int getProtocolVersion();

    /**
     * Construct server info abstract target.
     *
     * @param name    the name
     * @param address the address
     * @return the abstract target
     */
    public abstract AbstractTarget constructServerInfo(String name, InetSocketAddress address);

    /**
     * Gets online count.
     *
     * @return the online count
     */
    public abstract int getOnlineCount();

    /**
     * Construct server info abstract target.
     *
     * @param name    the name
     * @param address the address
     * @return the abstract target
     */
    public abstract AbstractTarget constructServerInfo(String name, SocketAddress address);

}
