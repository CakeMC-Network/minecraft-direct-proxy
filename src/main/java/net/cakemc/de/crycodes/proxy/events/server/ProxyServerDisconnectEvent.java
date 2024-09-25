package net.cakemc.de.crycodes.proxy.events.server;


import net.cakemc.de.crycodes.proxy.player.ProxyPlayer;
import net.cakemc.de.crycodes.proxy.target.AbstractTarget;
import net.cakemc.mc.lib.game.event.AbstractEvent;

import java.util.Objects;

/**
 * The type TargetServer disconnect event.
 */
public class ProxyServerDisconnectEvent extends AbstractEvent {

    private final ProxyPlayer player;

    private final AbstractTarget target;


    /**
     * Instantiates a new TargetServer disconnect event.
     *
     * @param player the player
     * @param target the target
     */
    public ProxyServerDisconnectEvent(final ProxyPlayer player, final AbstractTarget target) {
        if (player == null) {
            throw new NullPointerException("player is marked non-null but is null");
        }
        if (target == null) {
            throw new NullPointerException("target is marked non-null but is null");
        }
        this.player = player;
        this.target = target;
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
     * Gets target.
     *
     * @return the target
     */
    public AbstractTarget getTarget() {
        return this.target;
    }

    @Override
    public String toString() {
        return "ProxyServerDisconnectEvent(player=" + this.getPlayer() + ", target=" + this.getTarget() + ")";
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof ProxyServerDisconnectEvent other)) return false;
        if (!other.canEqual(this)) return false;
        final Object this$player = this.getPlayer();
        final Object other$player = other.getPlayer();
        if (!Objects.equals(this$player, other$player)) return false;
        final Object this$target = this.getTarget();
        final Object other$target = other.getTarget();
        return Objects.equals(this$target, other$target);
    }

    /**
     * Can equal boolean.
     *
     * @param other the other
     * @return the boolean
     */
    protected boolean canEqual(final Object other) {
        return other instanceof ProxyServerDisconnectEvent;
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $player = this.getPlayer();
        result = result * PRIME + ($player == null ? 43 : $player.hashCode());
        final Object $target = this.getTarget();
        result = result * PRIME + ($target == null ? 43 : $target.hashCode());
        return result;
    }

    @Override
    public String getName() {
        return "";
    }
}
