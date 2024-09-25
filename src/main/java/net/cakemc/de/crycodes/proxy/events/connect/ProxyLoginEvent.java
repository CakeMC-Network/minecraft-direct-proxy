package net.cakemc.de.crycodes.proxy.events.connect;

import net.cakemc.de.crycodes.proxy.player.ProxyPlayer;
import net.cakemc.de.crycodes.proxy.target.AbstractTarget;
import net.cakemc.mc.lib.game.event.AbstractEvent;

import java.util.Objects;

/**
 * The type Post login event.
 */
public class ProxyLoginEvent extends AbstractEvent {
    private final ProxyPlayer player;
    private AbstractTarget target;

    /**
     * Instantiates a new Post login event.
     *
     * @param player the player
     * @param target the target
     */
    public ProxyLoginEvent(ProxyPlayer player, AbstractTarget target) {
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

    /**
     * Sets target.
     *
     * @param target the target
     */
    public void setTarget(final AbstractTarget target) {
        this.target = target;
    }

    @Override
    public String toString() {
        return "ProxyLoginEvent(player=" + this.getPlayer() + ", target=" + this.getTarget() + ")";
    }

    @Override
    public String getName() {
        return this.toString();
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof ProxyLoginEvent other)) return false;
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
        return other instanceof ProxyLoginEvent;
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
}
