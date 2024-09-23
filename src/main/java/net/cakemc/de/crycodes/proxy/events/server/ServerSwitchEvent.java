package net.cakemc.de.crycodes.proxy.events.server;

import net.cakemc.de.crycodes.proxy.player.ProxiedPlayer;
import net.cakemc.de.crycodes.proxy.target.AbstractTarget;
import net.cakemc.mc.lib.game.event.AbstractEvent;

import java.util.Objects;

/**
 * The type Server switch event.
 */
public class ServerSwitchEvent extends AbstractEvent {
    private final ProxiedPlayer player;
    private final AbstractTarget from;

    /**
     * Instantiates a new Server switch event.
     *
     * @param player the player
     * @param from   the from
     */
    public ServerSwitchEvent(final ProxiedPlayer player, final AbstractTarget from) {
        this.player = player;
        this.from = from;
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
     * Gets from.
     *
     * @return the from
     */
    public AbstractTarget getFrom() {
        return this.from;
    }

    @Override
    public String toString() {
        return "ServerSwitchEvent(player=" + this.getPlayer() + ", from=" + this.getFrom() + ")";
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof ServerSwitchEvent other)) return false;
        if (!other.canEqual(this)) return false;
        final Object this$player = this.getPlayer();
        final Object other$player = other.getPlayer();
        if (!Objects.equals(this$player, other$player)) return false;
        final Object this$from = this.getFrom();
        final Object other$from = other.getFrom();
        return Objects.equals(this$from, other$from);
    }

    /**
     * Can equal boolean.
     *
     * @param other the other
     * @return the boolean
     */
    protected boolean canEqual(final Object other) {
        return other instanceof ServerSwitchEvent;
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $player = this.getPlayer();
        result = result * PRIME + ($player == null ? 43 : $player.hashCode());
        final Object $from = this.getFrom();
        result = result * PRIME + ($from == null ? 43 : $from.hashCode());
        return result;
    }

    @Override
    public String getName() {
        return "";
    }
}
