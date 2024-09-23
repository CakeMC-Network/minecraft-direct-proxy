package net.cakemc.de.crycodes.proxy.events;

import net.cakemc.de.crycodes.proxy.player.PendingConnection;
import net.cakemc.mc.lib.game.Status;
import net.cakemc.mc.lib.game.event.AbstractEvent;

import java.util.Objects;

/**
 * The type Proxy ping event.
 */
public class ProxyPingEvent extends AbstractEvent {
    private final PendingConnection connection;
    private Status.Info response;

    /**
     * Instantiates a new Proxy ping event.
     *
     * @param connection the connection
     * @param response   the response
     */
    public ProxyPingEvent(PendingConnection connection, Status.Info response) {
        this.connection = connection;
        this.response = response;
    }

    /**
     * Gets connection.
     *
     * @return the connection
     */
    public PendingConnection getConnection() {
        return this.connection;
    }

    /**
     * Gets response.
     *
     * @return the response
     */
    public Status.Info getResponse() {
        return this.response;
    }

    /**
     * Sets response.
     *
     * @param response the response
     */
    public void setResponse(final Status.Info response) {
        this.response = response;
    }

    @Override
    public String toString() {
        return "ProxyPingEvent(connection=" + this.getConnection() + ", response=" + this.getResponse() + ")";
    }

    @Override
    public String getName() {
        return this.toString();
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof ProxyPingEvent other)) return false;
        if (!other.canEqual(this)) return false;
        final Object this$connection = this.getConnection();
        final Object other$connection = other.getConnection();
        if (!Objects.equals(this$connection, other$connection))
            return false;
        final Object this$response = this.getResponse();
        final Object other$response = other.getResponse();
        return Objects.equals(this$response, other$response);
    }

    /**
     * Can equal boolean.
     *
     * @param other the other
     * @return the boolean
     */
    protected boolean canEqual(final Object other) {
        return other instanceof ProxyPingEvent;
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $connection = this.getConnection();
        result = result * PRIME + ($connection == null ? 43 : $connection.hashCode());
        final Object $response = this.getResponse();
        result = result * PRIME + ($response == null ? 43 : $response.hashCode());
        return result;
    }
}
