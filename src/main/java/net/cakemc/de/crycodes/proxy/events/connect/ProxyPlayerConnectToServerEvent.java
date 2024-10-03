package net.cakemc.de.crycodes.proxy.events.connect;

import net.cakemc.de.crycodes.proxy.units.ProxyServiceAddress;
import net.cakemc.mc.lib.game.event.AbstractEvent;
import net.cakemc.mc.lib.game.event.AbstractEvent.Cancellable;

import java.net.SocketAddress;
import java.util.Objects;

/**
 * The type Player connect event.
 */
public class ProxyPlayerConnectToServerEvent extends AbstractEvent implements Cancellable {
    private final SocketAddress socketAddress;
    private final ProxyServiceAddress listener;
    private boolean cancelled;

    /**
     * Instantiates a new Player connect event.
     *
     * @param socketAddress the socket address
     * @param listener      the listener
     */
    public ProxyPlayerConnectToServerEvent(final SocketAddress socketAddress, final ProxyServiceAddress listener) {
        this.socketAddress = socketAddress;
        this.listener = listener;
    }

    /**
     * Gets socket address.
     *
     * @return the socket address
     */
    public SocketAddress getSocketAddress() {
        return this.socketAddress;
    }

    /**
     * Gets listener.
     *
     * @return the listener
     */
    public ProxyServiceAddress getListener() {
        return this.listener;
    }

    @Override
    public boolean cancel(boolean b) {
        return cancelled = b;
    }

    @Override
    public boolean cancelled() {
        return cancelled;
    }

    @Override
    public String toString() {
        return "ClientConnectEvent(socketAddress=" + this.getSocketAddress() + ", listener=" + this.getListener() + ", cancelled=" + this.cancelled + ")";
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof ProxyPlayerConnectToServerEvent other)) return false;
        if (!other.canEqual(this)) return false;
        if (this.cancelled != other.cancelled) return false;
        final Object this$socketAddress = this.getSocketAddress();
        final Object other$socketAddress = other.getSocketAddress();
        if (!Objects.equals(this$socketAddress, other$socketAddress))
            return false;
        final Object this$listener = this.getListener();
        final Object other$listener = other.getListener();
        return Objects.equals(this$listener, other$listener);
    }

    /**
     * Can equal boolean.
     *
     * @param other the other
     * @return the boolean
     */
    protected boolean canEqual(final Object other) {
        return other instanceof ProxyPlayerConnectToServerEvent;
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + (this.cancelled ? 79 : 97);
        final Object $socketAddress = this.getSocketAddress();
        result = result * PRIME + ($socketAddress == null ? 43 : $socketAddress.hashCode());
        final Object $listener = this.getListener();
        result = result * PRIME + ($listener == null ? 43 : $listener.hashCode());
        return result;
    }

    @Override
    public String getName() {
        return getClass().getSimpleName();
    }

}
