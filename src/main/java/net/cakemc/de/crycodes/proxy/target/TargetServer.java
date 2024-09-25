package net.cakemc.de.crycodes.proxy.target;

import net.cakemc.de.crycodes.proxy.player.Connection;

/**
 * The interface TargetServer.
 */
public interface TargetServer extends Connection {

    /**
     * Gets info.
     *
     * @return the info
     */
    AbstractTarget getInfo();

}
