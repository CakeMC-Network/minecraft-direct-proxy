package net.cakemc.de.crycodes.proxy.events.server;

import net.cakemc.de.crycodes.proxy.player.ProxyPlayer;
import net.cakemc.de.crycodes.proxy.target.TargetServer;
import net.cakemc.mc.lib.game.event.AbstractEvent;

import java.util.Objects;

/**
 * The type TargetServer connected event.
 */
public class ProxyServerConnectedEvent extends AbstractEvent {
    private final ProxyPlayer player;
    private final TargetServer targetServer;

    /**
     * Instantiates a new TargetServer connected event.
     *
     * @param player the player
     * @param targetServer the targetServer
     */
    public ProxyServerConnectedEvent(final ProxyPlayer player, final TargetServer targetServer) {
        this.player = player;
        this.targetServer = targetServer;
    }

    /**
     * Gets player.
     *
     * @return the player
     */
    public ProxyPlayer getPlayer() {
        return this.player;
    }

    /**
     * Gets targetServer.
     *
     * @return the targetServer
     */
    public TargetServer getServer() {
        return this.targetServer;
    }

    @Override
    public String toString() {
        return "ProxyServerConnectedEvent(player=" + this.getPlayer() + ", targetServer=" + this.getServer() + ")";
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof ProxyServerConnectedEvent other)) return false;
        if (!other.canEqual(this)) return false;
        final Object this$player = this.getPlayer();
        final Object other$player = other.getPlayer();
        if (!Objects.equals(this$player, other$player)) return false;
        final Object this$server = this.getServer();
        final Object other$server = other.getServer();
        return Objects.equals(this$server, other$server);
    }

    /**
     * Can equal boolean.
     *
     * @param other the other
     * @return the boolean
     */
    protected boolean canEqual(final Object other) {
        return other instanceof ProxyServerConnectedEvent;
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $player = this.getPlayer();
        result = result * PRIME + ($player == null ? 43 : $player.hashCode());
        final Object $server = this.getServer();
        result = result * PRIME + ($server == null ? 43 : $server.hashCode());
        return result;
    }

    @Override
    public String getName() {
        return "";
    }
}
