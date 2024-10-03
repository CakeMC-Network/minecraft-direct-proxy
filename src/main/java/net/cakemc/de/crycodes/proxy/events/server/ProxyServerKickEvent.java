package net.cakemc.de.crycodes.proxy.events.server;

import net.cakemc.de.crycodes.proxy.player.ProxyPlayer;
import net.cakemc.de.crycodes.proxy.target.AbstractTarget;
import net.cakemc.mc.lib.game.event.AbstractEvent;
import net.cakemc.mc.lib.game.text.test.api.chat.BaseComponent;
import net.cakemc.mc.lib.game.text.test.api.chat.TextComponent;

import java.util.Objects;

/**
 * The type TargetServer kick event.
 */
public class ProxyServerKickEvent extends AbstractEvent implements AbstractEvent.Cancellable {
    private final ProxyPlayer player;
    private final AbstractTarget kickedFrom;
    private boolean cancelled;
    private BaseComponent reason;
    private AbstractTarget cancelServer;
    private State state;

    /**
     * Instantiates a new TargetServer kick event.
     *
     * @param player              the player
     * @param kickReasonComponent the kick reason component
     * @param cancelServer        the cancel server
     */
    public ProxyServerKickEvent(ProxyPlayer player, BaseComponent[] kickReasonComponent, AbstractTarget cancelServer) {
        this(player, kickReasonComponent, cancelServer, State.UNKNOWN);
    }

    /**
     * Instantiates a new TargetServer kick event.
     *
     * @param player              the player
     * @param kickReasonComponent the kick reason component
     * @param cancelServer        the cancel server
     * @param state               the state
     */
    public ProxyServerKickEvent(ProxyPlayer player, BaseComponent[] kickReasonComponent, AbstractTarget cancelServer, State state) {
        this(player, player.getServer().getInfo(), kickReasonComponent, cancelServer, state);
    }

    /**
     * Instantiates a new TargetServer kick event.
     *
     * @param player              the player
     * @param kickedFrom          the kicked from
     * @param kickReasonComponent the kick reason component
     * @param cancelServer        the cancel server
     * @param state               the state
     */
    public ProxyServerKickEvent(ProxyPlayer player, AbstractTarget kickedFrom, BaseComponent[] kickReasonComponent, AbstractTarget cancelServer, State state) {
        this(player, kickedFrom, TextComponent.fromArray(kickReasonComponent), cancelServer, state);
    }

    /**
     * Instantiates a new TargetServer kick event.
     *
     * @param player       the player
     * @param kickedFrom   the kicked from
     * @param reason       the reason
     * @param cancelServer the cancel server
     * @param state        the state
     */
    public ProxyServerKickEvent(ProxyPlayer player, AbstractTarget kickedFrom, BaseComponent reason, AbstractTarget cancelServer, State state) {
        this.player = player;
        this.kickedFrom = kickedFrom;
        this.reason = reason;
        this.cancelServer = cancelServer;
        this.state = state;
    }

    /**
     * Gets kick reason.
     *
     * @return the kick reason
     */
    public String getKickReason() {
        return BaseComponent.toLegacyText(getReason());
    }

    /**
     * Sets kick reason.
     *
     * @param reason the reason
     */
    public void setKickReason(String reason) {
        this.setReason(TextComponent.fromLegacy(reason));
    }

    /**
     * Get kick reason component base component [ ].
     *
     * @return the base component [ ]
     */
    public BaseComponent[] getKickReasonComponent() {
        return new BaseComponent[]{getReason()};
    }

    /**
     * Sets kick reason component.
     *
     * @param kickReasonComponent the kick reason component
     */
    public void setKickReasonComponent(BaseComponent[] kickReasonComponent) {
        this.setReason(TextComponent.fromArray(kickReasonComponent));
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
     * Gets kicked from.
     *
     * @return the kicked from
     */
    public AbstractTarget getKickedFrom() {
        return this.kickedFrom;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    /**
     * Sets cancelled.
     *
     * @param cancelled the cancelled
     */
    public void setCancelled(final boolean cancelled) {
        this.cancelled = cancelled;
    }

    /**
     * Gets reason.
     *
     * @return the reason
     */
    public BaseComponent getReason() {
        return this.reason;
    }

    /**
     * Sets reason.
     *
     * @param reason the reason
     */
    public void setReason(final BaseComponent reason) {
        this.reason = reason;
    }

    /**
     * Gets cancel server.
     *
     * @return the cancel server
     */
    public AbstractTarget getCancelServer() {
        return this.cancelServer;
    }

    /**
     * Sets cancel server.
     *
     * @param cancelServer the cancel server
     */
    public void setCancelServer(final AbstractTarget cancelServer) {
        this.cancelServer = cancelServer;
    }

    /**
     * Gets state.
     *
     * @return the state
     */
    public State getState() {
        return this.state;
    }

    /**
     * Sets state.
     *
     * @param state the state
     */
    public void setState(final State state) {
        this.state = state;
    }

    @Override
    public boolean cancel(boolean b) {
        return this.cancelled = b;
    }

    @Override
    public boolean cancelled() {
        return cancelled;
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public String toString() {
        return "ProxyServerKickEvent(player=" + this.getPlayer() + ", kickedFrom=" + this.getKickedFrom() + ", cancelled=" + this.isCancelled()
                + ", reason=" + this.getReason() + ", cancelServer=" + this.getCancelServer() + ", state=" + this.getState() + ")";
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof ProxyServerKickEvent other)) return false;
        if (!other.canEqual(this)) return false;
        if (this.isCancelled() != other.isCancelled()) return false;
        final Object this$player = this.getPlayer();
        final Object other$player = other.getPlayer();
        if (!Objects.equals(this$player, other$player)) return false;
        final Object this$kickedFrom = this.getKickedFrom();
        final Object other$kickedFrom = other.getKickedFrom();
        if (!Objects.equals(this$kickedFrom, other$kickedFrom))
            return false;
        final Object this$reason = this.getReason();
        final Object other$reason = other.getReason();
        if (!Objects.equals(this$reason, other$reason)) return false;
        final Object this$cancelServer = this.getCancelServer();
        final Object other$cancelServer = other.getCancelServer();
        if (!Objects.equals(this$cancelServer, other$cancelServer))
            return false;
        final Object this$state = this.getState();
        final Object other$state = other.getState();
        return Objects.equals(this$state, other$state);
    }

    /**
     * Can equal boolean.
     *
     * @param other the other
     * @return the boolean
     */
    protected boolean canEqual(final Object other) {
        return other instanceof ProxyServerKickEvent;
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + (this.isCancelled() ? 79 : 97);
        final Object $player = this.getPlayer();
        result = result * PRIME + ($player == null ? 43 : $player.hashCode());
        final Object $kickedFrom = this.getKickedFrom();
        result = result * PRIME + ($kickedFrom == null ? 43 : $kickedFrom.hashCode());
        final Object $reason = this.getReason();
        result = result * PRIME + ($reason == null ? 43 : $reason.hashCode());
        final Object $cancelServer = this.getCancelServer();
        result = result * PRIME + ($cancelServer == null ? 43 : $cancelServer.hashCode());
        final Object $state = this.getState();
        result = result * PRIME + ($state == null ? 43 : $state.hashCode());
        return result;
    }

    /**
     * The enum State.
     */
    public enum State {
        /**
         * Connecting state.
         */
        CONNECTING,
        /**
         * Connected state.
         */
        CONNECTED,
        /**
         * Unknown state.
         */
        UNKNOWN
    }
}
