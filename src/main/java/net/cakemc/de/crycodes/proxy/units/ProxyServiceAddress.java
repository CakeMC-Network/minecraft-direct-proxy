package net.cakemc.de.crycodes.proxy.units;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

/**
 * The type Proxy service address.
 */
public class ProxyServiceAddress {
    private final String identifier;
    private final SocketAddress socketAddress;
    private final String motd;

    /**
     * Instantiates a new Proxy service address.
     *
     * @param identifier    the identifier
     * @param socketAddress the socket address
     * @param motd          the motd
     */
    public ProxyServiceAddress(String identifier, final SocketAddress socketAddress, String motd) {
        this.identifier = identifier;
        this.socketAddress = socketAddress;
        this.motd = motd;
    }

    /**
     * To socket address inet socket address.
     *
     * @return the inet socket address
     */
    public InetSocketAddress toSocketAddress() {
        return (InetSocketAddress) socketAddress;
    }

    /**
     * Gets socket address.
     *
     * @return the socket address
     */
    public SocketAddress getSocketAddress() {
        return socketAddress;
    }

    /**
     * Gets motd.
     *
     * @return the motd
     */
    public String getMotd() {
        return motd;
    }

    /**
     * Gets identifier.
     *
     * @return the identifier
     */
    public String getIdentifier() {
        return identifier;
    }
}
