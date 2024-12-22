package net.cakemc.de.crycodes.proxy.player;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.util.internal.PlatformDependent;
import net.cakemc.de.crycodes.proxy.AbstractProxyService;
import net.cakemc.de.crycodes.proxy.channel.PlayerChannel;
import net.cakemc.de.crycodes.proxy.connection.ConnectionListener;
import net.cakemc.de.crycodes.proxy.connection.handler.ProxyLoginHandler;
import net.cakemc.de.crycodes.proxy.events.server.ProxyServerConnectEvent;
import net.cakemc.de.crycodes.proxy.network.PacketReader;
import net.cakemc.de.crycodes.proxy.network.PipelineUtils;
import net.cakemc.de.crycodes.proxy.network.codec.minecraft.MinecraftDecoder;
import net.cakemc.de.crycodes.proxy.network.codec.minecraft.MinecraftEncoder;
import net.cakemc.de.crycodes.proxy.network.packet.AbstractPacket;
import net.cakemc.de.crycodes.proxy.network.packet.ProtocolPacket;
import net.cakemc.de.crycodes.proxy.network.packet.impl.ClientSettingsPacket;
import net.cakemc.de.crycodes.proxy.network.packet.impl.DisconnectPacket;
import net.cakemc.de.crycodes.proxy.network.packet.impl.ServerTransferPacket;
import net.cakemc.de.crycodes.proxy.network.packet.impl.login.ServerSetCompressionPacket;
import net.cakemc.de.crycodes.proxy.protocol.Protocol;
import net.cakemc.de.crycodes.proxy.target.*;
import net.cakemc.mc.lib.game.text.test.api.chat.BaseComponent;
import net.cakemc.mc.lib.game.text.test.api.chat.TextComponent;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * The type Connected player.
 */
public final class ConnectedPlayer implements ProxyPlayer {

    private final AbstractProxyService abstractProxyService;

    private final PlayerChannel ch;

    private final String name;
    private final ProxyLoginHandler pendingConnection;
    private final Collection<AbstractTarget> pendingConnects = new HashSet<>();
    private final Queue<AbstractPacket> packetQueue = new ConcurrentLinkedQueue<>();

    private TargetServerConnection server;
    private Object dimension;
    private boolean dimensionChange = true;
    private int ping = 100;
    private AbstractTarget reconnectServer;
    private int gamemode;
    private int compressionThreshold = -1;

    private Queue<String> serverJoinQueue;
    private int clientEntityId;
    private int serverEntityId;

    private ClientSettingsPacket settings;
    private String lastCommandTabbed;
    private String displayName;
    private Locale locale;

    /**
     * Instantiates a new Connected player.
     *
     * @param abstractProxyService the abstract proxy service
     * @param ch                   the ch
     * @param name                 the name
     * @param pendingConnection    the pending connection
     */
    public ConnectedPlayer(final AbstractProxyService abstractProxyService, final PlayerChannel ch, final String name,
                           final ProxyLoginHandler pendingConnection) {
        if (abstractProxyService == null) {
            throw new NullPointerException("bungee is marked non-null but is null");
        }
        if (ch == null) {
            throw new NullPointerException("ch is marked non-null but is null");
        }
        if (name == null) {
            throw new NullPointerException("name is marked non-null but is null");
        }
        this.abstractProxyService = abstractProxyService;
        this.ch = ch;
        this.name = name;
        this.pendingConnection = pendingConnection;
    }

    /**
     * Init boolean.
     *
     * @return the boolean
     */
    public boolean init() {
        this.displayName = name;
        return abstractProxyService.addConnection(this);
    }

    /**
     * Send packet.
     *
     * @param packet the packet
     */
    public void sendPacket(ProtocolPacket packet) {
        ch.write(packet);
    }

    /**
     * Send packet queued.
     *
     * @param packet the packet
     */
    public void sendPacketQueued(AbstractPacket packet) {
        Protocol encodeProtocol = ch.getEncodeProtocol();
        if (!encodeProtocol.TO_CLIENT.hasPacket(packet.getClass(), getPendingConnection().getVersion())) {
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

    /**
     * Is active boolean.
     *
     * @return the boolean
     */
    public boolean isActive() {
        return !ch.isClosed();
    }

    @Override
    public void connect(AbstractTarget target) {
        connect(target, null, ConnectionReason.UNKNOWN);
    }

    @Override
    public void connect(AbstractTarget target, ConnectionReason reason) {
        connect(target, null, reason);
    }

    @Override
    public void connect(AbstractTarget target, ConnectionListener<Boolean> connectionListener) {
        connect(target, connectionListener, ConnectionReason.UNKNOWN);
    }

    /**
     * Connect now.
     *
     * @param target the target
     */
    public void connectNow(AbstractTarget target) {
        connectNow(target, ConnectionReason.UNKNOWN);
    }

    /**
     * Connect now.
     *
     * @param target the target
     * @param reason the reason
     */
    public void connectNow(AbstractTarget target, ConnectionReason reason) {
        dimensionChange = true;
        connect(target, reason);
    }

    @Override
    public void sendPacket(AbstractPacket packet) {
        this.ch.write(packet);
    }

    /**
     * Update and get next server abstract target.
     *
     * @param currentTarget the current target
     * @return the abstract target
     */
    public AbstractTarget updateAndGetNextServer(AbstractTarget currentTarget) {
        if (serverJoinQueue == null) {
            serverJoinQueue = new LinkedList<>(List.of(abstractProxyService.getDefaultTarget().getName())); // todo wtf??
        }
        AbstractTarget next = null;
        while (!serverJoinQueue.isEmpty()) {
            AbstractTarget candidate = abstractProxyService.getTargetByName(serverJoinQueue.remove());
            if (!Objects.equals(currentTarget, candidate)) {
                next = candidate;
                break;
            }
        }
        return next;
    }

    /**
     * Connect.
     *
     * @param info               the info
     * @param connectionListener the connection listener
     * @param reason             the reason
     */
    public void connect(AbstractTarget info, final ConnectionListener<Boolean> connectionListener,  ConnectionReason reason) {
        if (info == null) {
            this.disconnect("§cconnection to null server.");
        }
        TargetRequest request = new TargetRequest(info, reason, connectionListener == null ? (result, error) -> {
        } : (result, error) -> connectionListener.done((result == TargetRequest.Result.SUCCESS) ? Boolean.TRUE : Boolean.FALSE, error));
        connect(request);
    }

    @Override
    public void connect(final TargetRequest request) {
        final ConnectionListener<TargetRequest.Result> connectionListener = request.getCallback();
        ProxyServerConnectEvent event = new ProxyServerConnectEvent(this, request.getTarget(), request.getReason(), request);
        abstractProxyService.getEventManager().call(event);
        if (event.cancelled()) {
            if (connectionListener != null) {
                connectionListener.done(TargetRequest.Result.EVENT_CANCEL, null);
            }
            if (getServer() == null && !ch.isClosing()) {
                throw new IllegalStateException("Cancelled ProxyServerConnectEvent with no server or disconnect.");
            }
            return;
        }
        final ProxyTargetImpl target = (ProxyTargetImpl) event.getTarget(); // Update in case the event changed target
        if (getServer() != null && Objects.equals(getServer().getInfo(), target)) {
            if (connectionListener != null) {
                connectionListener.done(TargetRequest.Result.ALREADY_CONNECTED, null);
            }
            return;
        }
        if (pendingConnects.contains(target)) {
            if (connectionListener != null) {
                connectionListener.done(TargetRequest.Result.ALREADY_CONNECTING, null);
            }
            return;
        }
        pendingConnects.add(target);
        ChannelInitializer<Channel> initializer = new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel ch) throws Exception {
                PipelineUtils.SERVER_PLAYER_CHANNEL_INITIALIZER.initChannel(ch);
                ch.pipeline().addAfter(PipelineUtils.FRAME_DECODER, PipelineUtils.PACKET_DECODER,
                        new MinecraftDecoder(false, Protocol.HANDSHAKE, getPendingConnection().getVersion()));

                ch.pipeline().addAfter(PipelineUtils.FRAME_PREPENDER, PipelineUtils.PACKET_ENCODER,
                        new MinecraftEncoder(Protocol.HANDSHAKE, false, getPendingConnection().getVersion()));

                ch.pipeline().get(PacketReader.class).setHandler(new ServerConnector(abstractProxyService, ConnectedPlayer.this, target));
            }
        };
        ChannelFutureListener listener = future -> {
            if (connectionListener != null) {
                connectionListener.done((future.isSuccess()) ? TargetRequest.Result.SUCCESS : TargetRequest.Result.FAIL, future.cause());
            }
            if (!future.isSuccess()) {
                future.channel().close();
                pendingConnects.remove(target);
                AbstractTarget def = updateAndGetNextServer(target);
                if (request.isRetry() && def != null && (getServer() == null || def != getServer().getInfo())) {
                    connect(def, null, ConnectionReason.LOBBY_FALLBACK);
                } else if (dimensionChange) {
                    disconnect(connectionFailMessage(future.cause()));
                }
            }
        };
        Bootstrap b = new Bootstrap().channel(PipelineUtils.getChannel(target.getAddress())).group(ch.getHandle().eventLoop()).handler(initializer).option(ChannelOption.CONNECT_TIMEOUT_MILLIS, request.getConnectTimeout()).remoteAddress(target.getAddress());
        // Windows is bugged, multi homed users will just have to live with random connecting IPs

        if (!PlatformDependent.isWindows() && getPendingConnection().getListener().getSocketAddress() instanceof InetSocketAddress) {
            b.localAddress(getPendingConnection().getListener().toSocketAddress().getHostString(), 0);
        }
        b.connect().addListener(listener);
    }

    private String connectionFailMessage(Throwable cause) {
        return cause.getMessage();
    }

    @Override
    public void disconnect(String reason) {
        disconnect(TextComponent.fromLegacy(reason));
    }

    @Override
    public void disconnect(BaseComponent... reason) {
        disconnect(TextComponent.fromArray(reason));
    }

    @Override
    public void disconnect(BaseComponent reason) {
        disconnect0(reason);
    }

    /**
     * Disconnect 0.
     *
     * @param reason the reason
     */
    public void disconnect0(final BaseComponent reason) {
        if (!ch.isClosing()) {

            ch.close(new DisconnectPacket(reason));

            if (server != null) {
                server.disconnect("Quitting");
            }
        }
    }

    @Override
    public InetSocketAddress getAddress() {
        return (InetSocketAddress) getSocketAddress();
    }

    @Override
    public SocketAddress getSocketAddress() {
        return ch.getRemoteAddress();
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public String getUUID() {
        return getPendingConnection().getUUID();
    }

    @Override
    public UUID getUniqueId() {
        return getPendingConnection().getUniqueId();
    }

    @Override
    public Locale getLocale() {
        return (locale == null && settings != null) ? locale = Locale.forLanguageTag(settings.getLocale().replace('_', '-')) : locale;
    }

    @Override
    public byte getViewDistance() {
        return (settings != null) ? settings.getViewDistance() : 10;
    }

    @Override
    public ChatMode getChatMode() {
        if (settings == null) {
            return ChatMode.SHOWN;
        }
        return switch (settings.getChatFlags()) {
            case 1 -> ChatMode.COMMANDS_ONLY;
            case 2 -> ChatMode.HIDDEN;
            default -> ChatMode.SHOWN;
        };
    }

    @Override
    public boolean hasChatColors() {
        return settings == null || settings.isChatColours();
    }

    /**
     * Gets extra data in handshake.
     *
     * @return the extra data in handshake
     */
    public String getExtraDataInHandshake() {
        return this.getPendingConnection().getExtraDataInHandshake();
    }

    @Override
    public boolean isConnected() {
        return !ch.isClosed();
    }

    @Override
    public void transfer(String host, int port) {
        sendPacket(new ServerTransferPacket(host, port));
    }

    /**
     * Gets ch.
     *
     * @return the ch
     */
    public PlayerChannel getCh() {
        return this.ch;
    }

    public String getName() {
        return this.name;
    }

    public ProxyLoginHandler getPendingConnection() {
        return this.pendingConnection;
    }

    /**
     * Gets pending connects.
     *
     * @return the pending connects
     */
    public Collection<AbstractTarget> getPendingConnects() {
        return this.pendingConnects;
    }

    public TargetServerConnection getServer() {
        return this.server;
    }

    /**
     * Sets server.
     *
     * @param server the server
     */
    public void setServer(final TargetServerConnection server) {
        this.server = server;
    }

    /**
     * Gets dimension.
     *
     * @return the dimension
     */
    public Object getDimension() {
        return this.dimension;
    }

    /**
     * Sets dimension.
     *
     * @param dimension the dimension
     */
    public void setDimension(final Object dimension) {
        this.dimension = dimension;
    }

    /**
     * Is dimension change boolean.
     *
     * @return the boolean
     */
    public boolean isDimensionChange() {
        return this.dimensionChange;
    }

    /**
     * Sets dimension change.
     *
     * @param dimensionChange the dimension change
     */
    public void setDimensionChange(final boolean dimensionChange) {
        this.dimensionChange = dimensionChange;
    }

    public int getPing() {
        return this.ping;
    }

    /**
     * Sets ping.
     *
     * @param ping the ping
     */
    public void setPing(final int ping) {
        this.ping = ping;
    }

    public AbstractTarget getReconnectServer() {
        return this.reconnectServer;
    }

    public void setReconnectServer(final AbstractTarget reconnectServer) {
        this.reconnectServer = reconnectServer;
    }

    /**
     * Gets gamemode.
     *
     * @return the gamemode
     */
    public int getGamemode() {
        return this.gamemode;
    }

    /**
     * Sets gamemode.
     *
     * @param gamemode the gamemode
     */
    public void setGamemode(final int gamemode) {
        this.gamemode = gamemode;
    }

    /**
     * Gets compression threshold.
     *
     * @return the compression threshold
     */
    public int getCompressionThreshold() {
        return this.compressionThreshold;
    }

    /**
     * Sets compression threshold.
     *
     * @param compressionThreshold the compression threshold
     */
    public void setCompressionThreshold(int compressionThreshold) {
        if (!ch.isClosing() && this.compressionThreshold == -1 && compressionThreshold >= 0) {
            this.compressionThreshold = compressionThreshold;
            sendPacket(new ServerSetCompressionPacket(compressionThreshold));
            ch.setCompressionThreshold(compressionThreshold);
        }
    }

    /**
     * Sets server join queue.
     *
     * @param serverJoinQueue the server join queue
     */
    public void setServerJoinQueue(final Queue<String> serverJoinQueue) {
        this.serverJoinQueue = serverJoinQueue;
    }

    /**
     * Gets client entity id.
     *
     * @return the client entity id
     */
    public int getClientEntityId() {
        return this.clientEntityId;
    }

    /**
     * Sets client entity id.
     *
     * @param clientEntityId the client entity id
     */
    public void setClientEntityId(final int clientEntityId) {
        this.clientEntityId = clientEntityId;
    }

    /**
     * Gets server entity id.
     *
     * @return the server entity id
     */
    public int getServerEntityId() {
        return this.serverEntityId;
    }

    /**
     * Sets server entity id.
     *
     * @param serverEntityId the server entity id
     */
    public void setServerEntityId(final int serverEntityId) {
        this.serverEntityId = serverEntityId;
    }

    /**
     * Gets settings.
     *
     * @return the settings
     */
    public ClientSettingsPacket getSettings() {
        return this.settings;
    }

    /**
     * Sets settings.
     *
     * @param settings the settings
     */
    public void setSettings(ClientSettingsPacket settings) {
        this.settings = settings;
        this.locale = null;
    }

    /**
     * Gets last command tabbed.
     *
     * @return the last command tabbed
     */
    public String getLastCommandTabbed() {
        return this.lastCommandTabbed;
    }

    /**
     * Sets last command tabbed.
     *
     * @param lastCommandTabbed the last command tabbed
     */
    public void setLastCommandTabbed(final String lastCommandTabbed) {
        this.lastCommandTabbed = lastCommandTabbed;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    @Override
    public void setDisplayName(String name) {
        displayName = name;
    }

}
