package net.cakemc.de.crycodes.proxy.events.connect;

import net.cakemc.de.crycodes.proxy.player.ProxyPlayer;
import net.cakemc.mc.lib.game.event.AbstractEvent;

import java.util.Objects;

/**
 * The type Player disconnect event.
 */
public class ProxyPlayerDisconnectEvent extends AbstractEvent {
    private final ProxyPlayer player;

    /**
     * Instantiates a new Player disconnect event.
     *
     * @param player the player
     */
    public ProxyPlayerDisconnectEvent(final ProxyPlayer player) {
        this.player = player;
    }

    /**
     * Gets player.
     *
     * @return the player
     */
    public ProxyPlayer getPlayer() {
        return this.player;
    }

    @Override
    public String toString() {
        return "ProxyPlayerDisconnectEvent(player=" + this.getPlayer() + ")";
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof ProxyPlayerDisconnectEvent other)) return false;
        if (!other.canEqual(this)) return false;
        final Object this$player = this.getPlayer();
        final Object other$player = other.getPlayer();
        return Objects.equals(this$player, other$player);
    }

    /**
     * Can equal boolean.
     *
     * @param other the other
     * @return the boolean
     */
    protected boolean canEqual(final Object other) {
        return other instanceof ProxyPlayerDisconnectEvent;
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $player = this.getPlayer();
        result = result * PRIME + ($player == null ? 43 : $player.hashCode());
        return result;
    }

    @Override
    public String getName() {
        return "";
    }
}
