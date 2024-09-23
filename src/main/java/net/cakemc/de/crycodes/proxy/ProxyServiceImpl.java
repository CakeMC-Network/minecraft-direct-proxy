package net.cakemc.de.crycodes.proxy;

import net.cakemc.de.crycodes.proxy.player.ConnectedPlayer;
import net.cakemc.de.crycodes.proxy.player.ProxiedPlayer;
import net.cakemc.de.crycodes.proxy.protocol.ProtocolVersion;
import net.cakemc.de.crycodes.proxy.target.AbstractTarget;
import net.cakemc.de.crycodes.proxy.target.ProxyTargetImpl;
import net.cakemc.mc.lib.game.event.EventManager;
import net.cakemc.screensystem.ScreenAbleObject;
import net.cakemc.screensystem.logging.Logger;
import net.cakemc.screensystem.logging.ServiceLogger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * The type Proxy service.
 */
public class ProxyServiceImpl extends AbstractProxyService implements ScreenAbleObject {
    /**
     * The constant COMPRESSION_THRESHOLD.
     */
    public static final int COMPRESSION_THRESHOLD = 256;
    /**
     * The constant SERVER_ICON.
     */
    public static final byte[] SERVER_ICON;

    static {
        try {
            SERVER_ICON = ProxyServiceImpl.class.getResourceAsStream("/server-icon.png").readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * The Event bus.
     */
    public final EventManager eventBus;
    private final NetworkServer networkServer;

    private final Logger logger;

    private final List<AbstractTarget> targets = new ArrayList<>();
    private final List<ConnectedPlayer> connections = new ArrayList<>();
    private final String identifier;
    private final String host;
    private final int port;
    /**
     * The Is running.
     */
    public volatile boolean isRunning;

    /**
     * Instantiates a new Proxy service.
     *
     * @param identifier the identifier
     * @param host       the host
     * @param port       the port
     * @throws IOException the io exception
     */
    public ProxyServiceImpl(String identifier, String host, int port) throws IOException {
        this.identifier = identifier;
        this.host = host;
        this.port = port;

        logger = new ServiceLogger(ProxyServiceImpl.class);

        this.eventBus = new EventManager();
        this.networkServer = new NetworkServer(this, host, port);
    }

    /**
     * Start.
     */
    public void start() {
        this.networkServer.start();
    }

    @Override
    public void stop() {
        // todo make save to interrupt
        this.networkServer.stop();
    }

    @Override
    public AbstractTarget getDefaultTarget() {
        return this.targets.stream().findFirst().orElse(null);
    }

    @Override
    public List<? extends ProxiedPlayer> getPlayers() {
        return connections;
    }

    @Override
    public int getOnlineCount() {
        return connections.size();
    }

    @Override
    public ProxiedPlayer getPlayer(String name) {
        return connections.stream().filter(userConnection -> userConnection.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    @Override
    public ProxiedPlayer getPlayer(UUID uuid) {
        return connections.stream().filter(userConnection -> userConnection.getUniqueId().equals(uuid)).findFirst().orElse(null);
    }

    @Override
    public AbstractTarget getTargetByName(String name) {
        return getTargets().stream().filter(abstractTarget -> abstractTarget.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    @Override
    public int getProtocolVersion() {
        return ProtocolVersion.getSupportedVersionIds().get(ProtocolVersion.getSupportedVersionIds().size() - 1);
    }

    @Override
    public String getGameVersion() {
        return ProtocolVersion.getSupportedVersionIds().get(0) + "-" +
                ProtocolVersion.getSupportedVersionIds().get(ProtocolVersion.getSupportedVersionIds().size() - 1);
    }

    @Override
    public AbstractTarget constructServerInfo(String name, InetSocketAddress address) {
        return constructServerInfo(name, (SocketAddress) address);
    }

    @Override
    public AbstractTarget constructServerInfo(String name, SocketAddress address) {
        return new ProxyTargetImpl(name, address);
    }

    @Override
    public boolean addConnection(ConnectedPlayer con) {
        if (connections.stream().anyMatch(userConnection -> userConnection.getName().equals(con.getName()))) {
            return false;
        }

        connections.add(con);
        return true;
    }

    @Override
    public void removeConnection(ConnectedPlayer con) {
        connections.remove(con);
    }

    @Override
    public EventManager getEventManager() {
        return eventBus;
    }

    @Override
    public List<AbstractTarget> getTargets() {
        return targets;
    }

    /**
     * Sets running.
     *
     * @param running the running
     */
    public void setRunning(boolean running) {
        isRunning = running;
    }

    /**
     * Gets port.
     *
     * @return the port
     */
    public int getPort() {
        return port;
    }

    /**
     * Gets host.
     *
     * @return the host
     */
    public String getHost() {
        return host;
    }

    /**
     * Gets identifier.
     *
     * @return the identifier
     */
    public String getIdentifier() {
        return identifier;
    }

    @Override
    public Logger getLogger() {
        return logger;
    }
}
