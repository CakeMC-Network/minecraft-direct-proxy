package net.cakemc.de.crycodes.proxy.player;

import net.cakemc.de.crycodes.proxy.units.ProxyServiceAddress;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.UUID;


/**
 * The interface Pending connection.
 */
public interface PendingConnection extends Connection {

    /**
     * Gets name.
     *
     * @return the name
     */
    String getName();

    /**
     * Gets version.
     *
     * @return the version
     */
    int getVersion();

    /**
     * Gets virtual host.
     *
     * @return the virtual host
     */
    InetSocketAddress getVirtualHost();

    /**
     * Gets listener.
     *
     * @return the listener
     */
    ProxyServiceAddress getListener();

    /**
     * Gets uuid.
     *
     * @return the uuid
     */
    @Deprecated
    String getUUID();

    /**
     * Gets unique id.
     *
     * @return the unique id
     */
    UUID getUniqueId();

    /**
     * Is legacy boolean.
     *
     * @return the boolean
     */
    boolean isLegacy();

    /**
     * Is transferred boolean.
     *
     * @return the boolean
     */
    boolean isTransferred();

    @Override
    SocketAddress getSocketAddress();

    @Override
    boolean isConnected();
}
