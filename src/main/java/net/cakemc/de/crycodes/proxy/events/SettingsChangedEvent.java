package net.cakemc.de.crycodes.proxy.events;

import net.cakemc.de.crycodes.proxy.player.ProxiedPlayer;
import net.cakemc.mc.lib.game.event.AbstractEvent;

import java.util.Objects;

/**
 * The type Settings changed event.
 */
public class SettingsChangedEvent extends AbstractEvent {
    private final ProxiedPlayer player;

    /**
     * Instantiates a new Settings changed event.
     *
     * @param player the player
     */
    public SettingsChangedEvent(final ProxiedPlayer player) {
        this.player = player;
    }

    /**
     * Gets player.
     *
     * @return the player
     */
    public ProxiedPlayer getPlayer() {
        return this.player;
    }

    @Override
    public String toString() {
        return "SettingsChangedEvent(player=" + this.getPlayer() + ")";
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof SettingsChangedEvent other)) return false;
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
        return other instanceof SettingsChangedEvent;
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $player = this.getPlayer();
        result = result * PRIME + ($player == null ? 43 : $player.hashCode());
        return result;
    }
}
