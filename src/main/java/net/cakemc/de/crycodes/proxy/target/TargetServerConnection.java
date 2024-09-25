package net.cakemc.de.crycodes.proxy.target;


import net.cakemc.de.crycodes.proxy.channel.PlayerChannel;
import net.cakemc.de.crycodes.proxy.network.packet.AbstractPacket;
import net.cakemc.de.crycodes.proxy.protocol.Protocol;
import net.cakemc.mc.lib.game.text.test.api.chat.BaseComponent;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * The type TargetServer connection.
 */
public class TargetServerConnection implements TargetServer {

    private final PlayerChannel ch;
    private final ProxyTargetImpl info;
    private final boolean forgeServer = false;
    private final Queue<KeepAliveData> keepAlives = new ArrayDeque<>();
    private final Queue<AbstractPacket> packetQueue = new ConcurrentLinkedQueue<>();
    private boolean isObsolete;

    /**
     * Instantiates a new TargetServer connection.
     *
     * @param ch   the ch
     * @param info the info
     */
    public TargetServerConnection(final PlayerChannel ch, final ProxyTargetImpl info) {
        this.ch = ch;
        this.info = info;
    }

    public void sendPacket(AbstractPacket packet) {
        this.ch.write(packet);
    }

    /**
     * Send packet queued.
     *
     * @param packet the packet
     */
    public void sendPacketQueued(AbstractPacket packet) {
        Protocol encodeProtocol = ch.getEncodeProtocol();
        if (!encodeProtocol.TO_SERVER.hasPacket(packet.getClass(), ch.getEncodeVersion())) {
            packetQueue.add(packet);
        } else {
            sendPacket(packet);
        }
    }

    /**
     * Send queued packets.
     */
    public void sendQueuedPackets() {
        AbstractPacket packet;
        while ((packet = packetQueue.poll()) != null) {
            sendPacket(packet);
        }
    }

    @Override
    public void disconnect(String reason) {
        disconnect();
    }

    @Override
    public void disconnect(BaseComponent... reason) {

        isObsolete = true;
        ch.close();
    }

    @Override
    public void disconnect(BaseComponent reason) {
        disconnect();
    }

    @Override
    public InetSocketAddress getAddress() {
        return (InetSocketAddress) getSocketAddress();
    }

    @Override
    public SocketAddress getSocketAddress() {
        return getInfo().getAddress();
    }

    @Override
    public boolean isConnected() {
        return !ch.isClosed();
    }

    /**
     * Gets ch.
     *
     * @return the ch
     */
    public PlayerChannel getCh() {
        return this.ch;
    }

    public ProxyTargetImpl getInfo() {
        return this.info;
    }

    /**
     * Is forge server boolean.
     *
     * @return the boolean
     */
    public boolean isForgeServer() {
        return this.forgeServer;
    }

    /**
     * Gets keep alives.
     *
     * @return the keep alives
     */
    public Queue<KeepAliveData> getKeepAlives() {
        return this.keepAlives;
    }

    /**
     * Is obsolete boolean.
     *
     * @return the boolean
     */
    public boolean isObsolete() {
        return this.isObsolete;
    }

    /**
     * Sets obsolete.
     *
     * @param isObsolete the is obsolete
     */
    public void setObsolete(final boolean isObsolete) {
        this.isObsolete = isObsolete;
    }

    /**
     * The type Keep alive data.
     */
    public static class KeepAliveData {
        private final long id;
        private final long time;

        /**
         * Instantiates a new Keep alive data.
         *
         * @param id   the id
         * @param time the time
         */
        public KeepAliveData(final long id, final long time) {
            this.id = id;
            this.time = time;
        }

        /**
         * Gets id.
         *
         * @return the id
         */
        public long getId() {
            return this.id;
        }

        /**
         * Gets time.
         *
         * @return the time
         */
        public long getTime() {
            return this.time;
        }

        @Override
        public boolean equals(final Object o) {
            if (o == this) return true;
            if (!(o instanceof KeepAliveData other)) return false;
            if (!other.canEqual(this)) return false;
            if (this.getId() != other.getId()) return false;
            return this.getTime() == other.getTime();
        }

        /**
         * Can equal boolean.
         *
         * @param other the other
         * @return the boolean
         */
        protected boolean canEqual(final Object other) {
            return other instanceof TargetServerConnection.KeepAliveData;
        }

        @Override
        public int hashCode() {
            final int PRIME = 59;
            int result = 1;
            final long $id = this.getId();
            result = result * PRIME + (int) ($id >>> 32 ^ $id);
            final long $time = this.getTime();
            result = result * PRIME + (int) ($time >>> 32 ^ $time);
            return result;
        }

        @Override
        public String toString() {
            return "TargetServerConnection.KeepAliveData(id=" + this.getId() + ", time=" + this.getTime() + ")";
        }
    }
}
