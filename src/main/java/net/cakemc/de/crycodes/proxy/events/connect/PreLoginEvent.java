package net.cakemc.de.crycodes.proxy.events.connect;

import net.cakemc.de.crycodes.proxy.player.PendingConnection;
import net.cakemc.mc.lib.game.event.AbstractEvent;
import net.cakemc.mc.lib.game.event.Cancelable;
import net.cakemc.mc.lib.game.text.test.api.chat.BaseComponent;
import net.cakemc.mc.lib.game.text.test.api.chat.TextComponent;

import java.util.Objects;

/**
 * The type Pre login event.
 */
public class PreLoginEvent extends AbstractEvent implements Cancelable {
    private final PendingConnection connection;
    private boolean cancelled;
    private BaseComponent reason;

    /**
     * Instantiates a new Pre login event.
     *
     * @param connection the connection
     */
    public PreLoginEvent(PendingConnection connection) {
        this.connection = connection;
    }

    /**
     * Gets cancel reason.
     *
     * @return the cancel reason
     */
    @Deprecated
    public String getCancelReason() {
        return BaseComponent.toLegacyText(getReason());
    }

    /**
     * Sets cancel reason.
     *
     * @param cancelReason the cancel reason
     */
    @Deprecated
    public void setCancelReason(String cancelReason) {
        setReason(TextComponent.fromLegacy(cancelReason));
    }

    /**
     * Sets cancel reason.
     *
     * @param cancelReason the cancel reason
     */
    @Deprecated
    public void setCancelReason(BaseComponent... cancelReason) {
        setReason(TextComponent.fromArray(cancelReason));
    }

    /**
     * Get cancel reason components base component [ ].
     *
     * @return the base component [ ]
     */
    @Deprecated
    public BaseComponent[] getCancelReasonComponents() {
        return new BaseComponent[]{getReason()};
    }

    /**
     * Gets connection.
     *
     * @return the connection
     */
    public PendingConnection getConnection() {
        return this.connection;
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

    @Override
    public boolean setCancelState(boolean b) {
        return false;
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

    @Override
    public String toString() {
        return "PreLoginEvent(connection=" + this.getConnection() + ", cancelled=" + this.isCancelled() + ", reason=" + this.getReason() + ")";
    }

    @Override
    public String getName() {
        return this.toString();
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof PreLoginEvent other)) return false;
        if (!other.canEqual(this)) return false;
        if (this.isCancelled() != other.isCancelled()) return false;
        final Object this$connection = this.getConnection();
        final Object other$connection = other.getConnection();
        if (!Objects.equals(this$connection, other$connection))
            return false;
        final Object this$reason = this.getReason();
        final Object other$reason = other.getReason();
        return Objects.equals(this$reason, other$reason);
    }

    /**
     * Can equal boolean.
     *
     * @param other the other
     * @return the boolean
     */
    protected boolean canEqual(final Object other) {
        return other instanceof PreLoginEvent;
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + (this.isCancelled() ? 79 : 97);
        final Object $connection = this.getConnection();
        result = result * PRIME + ($connection == null ? 43 : $connection.hashCode());
        final Object $reason = this.getReason();
        result = result * PRIME + ($reason == null ? 43 : $reason.hashCode());
        return result;
    }
}
