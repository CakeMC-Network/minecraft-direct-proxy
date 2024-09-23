package net.cakemc.de.crycodes.proxy.events.server;

import net.cakemc.de.crycodes.proxy.player.ProxiedPlayer;
import net.cakemc.de.crycodes.proxy.target.Server;
import net.cakemc.mc.lib.game.event.AbstractEvent;

import java.util.Objects;

/**
 * The type Server connected event.
 */
public class ServerConnectedEvent extends AbstractEvent {
    private final ProxiedPlayer player;
    private final Server server;

    /**
     * Instantiates a new Server connected event.
     *
     * @param player the player
     * @param server the server
     */
    public ServerConnectedEvent(final ProxiedPlayer player, final Server server) {
        this.player = player;
        this.server = server;
    }

    /**
     * Gets player.
     *
     * @return the player
     */
    public ProxiedPlayer getPlayer() {
        return this.player;
    }

    /**
     * Gets server.
     *
     * @return the server
     */
    public Server getServer() {
        return this.server;
    }

    @Override
    public String toString() {
        return "ServerConnectedEvent(player=" + this.getPlayer() + ", server=" + this.getServer() + ")";
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof ServerConnectedEvent other)) return false;
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
        return other instanceof ServerConnectedEvent;
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
