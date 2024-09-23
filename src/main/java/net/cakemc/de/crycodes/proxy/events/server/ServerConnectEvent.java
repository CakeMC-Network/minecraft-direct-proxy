package net.cakemc.de.crycodes.proxy.events.server;


import net.cakemc.de.crycodes.proxy.player.ProxiedPlayer;
import net.cakemc.de.crycodes.proxy.target.AbstractTarget;
import net.cakemc.de.crycodes.proxy.target.TargetRequest;
import net.cakemc.mc.lib.game.event.AbstractEvent;
import net.cakemc.mc.lib.game.event.Cancelable;

import java.util.Objects;

/**
 * The type Server connect event.
 */
public class ServerConnectEvent extends AbstractEvent implements Cancelable {
    private final ProxiedPlayer player;
    private final Reason reason;
    private final TargetRequest request;

    private AbstractTarget target;
    private boolean cancelled;

    /**
     * Instantiates a new Server connect event.
     *
     * @param player the player
     * @param target the target
     */
    @Deprecated
    public ServerConnectEvent(ProxiedPlayer player, AbstractTarget target) {
        this(player, target, Reason.UNKNOWN);
    }

    /**
     * Instantiates a new Server connect event.
     *
     * @param player the player
     * @param target the target
     * @param reason the reason
     */
    @Deprecated
    public ServerConnectEvent(ProxiedPlayer player, AbstractTarget target, Reason reason) {
        this(player, target, reason, null);
    }

    /**
     * Instantiates a new Server connect event.
     *
     * @param player  the player
     * @param target  the target
     * @param reason  the reason
     * @param request the request
     */
    public ServerConnectEvent(ProxiedPlayer player, AbstractTarget target, Reason reason, TargetRequest request) {
        this.player = player;
        this.target = target;
        this.reason = reason;
        this.request = request;
    }

    @Override
    public String getName() {
        return "";
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
     * Gets reason.
     *
     * @return the reason
     */
    public Reason getReason() {
        return this.reason;
    }

    /**
     * Gets request.
     *
     * @return the request
     */
    public TargetRequest getRequest() {
        return this.request;
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
        if (target == null) {
            throw new NullPointerException("target is marked non-null but is null");
        }
        this.target = target;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public boolean setCancelState(boolean b) {
        return cancelled = b;
    }

    @Override
    public String toString() {
        return "ServerConnectEvent(player=" + this.getPlayer() + ", reason=" + this.getReason() + ", request=" + this.getRequest() + ", target=" + this.getTarget() + ", cancelled=" + this.isCancelled() + ")";
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof ServerConnectEvent other)) return false;
        if (!other.canEqual(this)) return false;
        if (this.isCancelled() != other.isCancelled()) return false;
        final Object this$player = this.getPlayer();
        final Object other$player = other.getPlayer();
        if (!Objects.equals(this$player, other$player)) return false;
        final Object this$reason = this.getReason();
        final Object other$reason = other.getReason();
        if (!Objects.equals(this$reason, other$reason)) return false;
        final Object this$request = this.getRequest();
        final Object other$request = other.getRequest();
        if (!Objects.equals(this$request, other$request)) return false;
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
        return other instanceof ServerConnectEvent;
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + (this.isCancelled() ? 79 : 97);
        final Object $player = this.getPlayer();
        result = result * PRIME + ($player == null ? 43 : $player.hashCode());
        final Object $reason = this.getReason();
        result = result * PRIME + ($reason == null ? 43 : $reason.hashCode());
        final Object $request = this.getRequest();
        result = result * PRIME + ($request == null ? 43 : $request.hashCode());
        final Object $target = this.getTarget();
        result = result * PRIME + ($target == null ? 43 : $target.hashCode());
        return result;
    }

    /**
     * The enum Reason.
     */
    public enum Reason {
        /**
         * Lobby fallback reason.
         */
        LOBBY_FALLBACK,
        /**
         * Command reason.
         */
        COMMAND,
        /**
         * Server down redirect reason.
         */
        SERVER_DOWN_REDIRECT,
        /**
         * Kick redirect reason.
         */
        KICK_REDIRECT,
        /**
         * Plugin message reason.
         */
        PLUGIN_MESSAGE,
        /**
         * Join proxy reason.
         */
        JOIN_PROXY,
        /**
         * Plugin reason.
         */
        PLUGIN,
        /**
         * Unknown reason.
         */
        UNKNOWN
    }
}
