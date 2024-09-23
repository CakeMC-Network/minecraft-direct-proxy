package net.cakemc.de.crycodes.proxy.target;

import net.cakemc.de.crycodes.proxy.player.Connection;

/**
 * The interface Server.
 */
public interface Server extends Connection {

    /**
     * Gets info.
     *
     * @return the info
     */
    AbstractTarget getInfo();

}
