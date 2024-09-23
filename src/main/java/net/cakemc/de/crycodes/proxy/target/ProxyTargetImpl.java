package net.cakemc.de.crycodes.proxy.target;


import net.cakemc.de.crycodes.proxy.network.packet.AbstractPacket;
import net.cakemc.de.crycodes.proxy.player.ProxiedPlayer;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.*;

/**
 * The type Proxy target.
 */
public class ProxyTargetImpl implements AbstractTarget {
    private final String name;
    private final SocketAddress socketAddress;
    private final Collection<ProxiedPlayer> players = new ArrayList<>();
    private final Queue<AbstractPacket> packetQueue = new LinkedList<>();

    /**
     * Instantiates a new Proxy target.
     *
     * @param name          the name
     * @param socketAddress the socket address
     */
    public ProxyTargetImpl(final String name, final SocketAddress socketAddress) {
        this.name = name;
        this.socketAddress = socketAddress;
    }

    /**
     * Add player.
     *
     * @param player the player
     */
    public void addPlayer(ProxiedPlayer player) {
        synchronized (this.players) {
            players.add(player);
        }
    }

    /**
     * Remove player.
     *
     * @param player the player
     */
    public void removePlayer(ProxiedPlayer player) {
        synchronized (this.players) {
            players.remove(player);
        }
    }

    @Override
    public Collection<ProxiedPlayer> getPlayers() {
        synchronized (this.players) {
            return Collections.unmodifiableCollection(new HashSet<>(players));
        }
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof AbstractTarget) && Objects.equals(getAddress(), ((AbstractTarget) obj).getAddress());
    }

    @Override
    public int hashCode() {
        return socketAddress.hashCode();
    }

    @Override
    public InetSocketAddress getAddress() {
        return (InetSocketAddress) socketAddress;
    }

    @Override
    public String toString() {
        return "BungeeServerInfo{" +
                "name='" + name + '\'' +
                ", socketAddress=" + socketAddress +
                ", players=" + players +
                ", packetQueue=" + packetQueue +
                '}';
    }

    public String getName() {
        return this.name;
    }

    public SocketAddress getSocketAddress() {
        return this.socketAddress;
    }

    /**
     * Gets packet queue.
     *
     * @return the packet queue
     */
    public Queue<AbstractPacket> getPacketQueue() {
        return this.packetQueue;
    }
}
