package net.cakemc.de.crycodes.proxy.connection;


import net.cakemc.de.crycodes.proxy.AbstractProxyService;
import net.cakemc.de.crycodes.proxy.channel.PlayerChannel;
import net.cakemc.de.crycodes.proxy.connection.data.CancelSendSignal;
import net.cakemc.de.crycodes.proxy.events.server.ProxyServerConnectEvent;
import net.cakemc.de.crycodes.proxy.events.server.ProxyServerDisconnectEvent;
import net.cakemc.de.crycodes.proxy.events.server.ProxyServerKickEvent;
import net.cakemc.de.crycodes.proxy.network.PacketHandler;
import net.cakemc.de.crycodes.proxy.network.packet.ProtocolPacket;
import net.cakemc.de.crycodes.proxy.network.packet.impl.*;
import net.cakemc.de.crycodes.proxy.network.packet.impl.login.ServerSetCompressionPacket;
import net.cakemc.de.crycodes.proxy.player.ConnectedPlayer;
import net.cakemc.de.crycodes.proxy.target.AbstractTarget;
import net.cakemc.de.crycodes.proxy.target.ConnectionReason;
import net.cakemc.de.crycodes.proxy.target.TargetServerConnection;
import net.cakemc.de.crycodes.proxy.target.TargetServerConnection.KeepAliveData;
import net.cakemc.de.crycodes.proxy.target.ServerConnector;
import net.cakemc.mc.lib.game.text.test.api.chat.BaseComponent;

/**
 * The type Downstream bridge.
 */
public class DownstreamBridge extends PacketHandler {
    private final AbstractProxyService bungee;
    private final ConnectedPlayer con;
    private final TargetServerConnection server;
    private boolean receivedLogin;

    /**
     * Instantiates a new Downstream bridge.
     *
     * @param bungee the bungee
     * @param con    the con
     * @param server the server
     */
    public DownstreamBridge(final AbstractProxyService bungee, final ConnectedPlayer con, final TargetServerConnection server) {
        this.bungee = bungee;
        this.con = con;
        this.server = server;
    }

    @Override
    public void exception(Throwable t) throws Exception {
        if (server.isObsolete()) {
            // do not perform any actions if the user has already moved
            return;
        }
        AbstractTarget def = con.updateAndGetNextServer(server.getInfo());
        if (def != null) {
            server.setObsolete(true);
            con.connectNow(def, ConnectionReason.SERVER_DOWN_REDIRECT);
            con.sendMessage("service '%s' closed.".formatted(def.getName()));
        } else {
            con.disconnect(t.getMessage());
        }
    }

    @Override
    public void disconnected(PlayerChannel channel) throws Exception {
        // We lost connection to the server
        server.getInfo().removePlayer(con);

        ProxyServerDisconnectEvent proxyServerDisconnectEvent = new ProxyServerDisconnectEvent(con, server.getInfo());
        bungee.getEventManager().call(proxyServerDisconnectEvent);
        if (server.isObsolete()) {
            // do not perform any actions if the user has already moved
            return;
        }
        AbstractTarget def = con.updateAndGetNextServer(server.getInfo());
        if (def != null) {
            server.setObsolete(true);
            con.connectNow(def, ConnectionReason.SERVER_DOWN_REDIRECT);
            con.sendMessage("service '%s' closed.".formatted(def.getName()));
        } else {
            con.disconnect("lost connection to service.");
        }
    }

    @Override
    public boolean shouldHandle(ProtocolPacket packet) throws Exception {
        return !server.isObsolete();
    }

    @Override
    public void handle(ProtocolPacket packet) throws Exception {
        con.sendPacket(packet);
    }

    @Override
    public void handle(DisconnectPacket disconnectPacket) throws Exception {
        AbstractTarget def = con.updateAndGetNextServer(server.getInfo());
        ProxyServerKickEvent event = (new ProxyServerKickEvent(con, server.getInfo(), new BaseComponent[]
                {disconnectPacket.getMessage()}, def, ProxyServerKickEvent.State.CONNECTED));

        bungee.getEventManager().call(event);

        if (event.isCancelled() && event.getCancelServer() != null) {
            con.connectNow(event.getCancelServer(), ConnectionReason.KICK_REDIRECT);
        } else {
            con.disconnect(event.getKickReasonComponent()); // TODO: Prefix our own stuff.
        }
        server.setObsolete(true);
        throw CancelSendSignal.INSTANCE;
    }

    @Override
    public void handle(ServerSetCompressionPacket serverSetCompressionPacket) throws Exception {
        server.getCh().setCompressionThreshold(serverSetCompressionPacket.getThreshold());
    }

    @Override
    public void handle(RespawnPacket respawnPacket) {
        con.setDimension(respawnPacket.getDimension());
    }

    @Override
    public void handle(ServerDataPacket serverDataPacket) throws Exception {
        // 1.19.4 doesn't allow empty MOTD and we probably don't want to simulate a ping event to get the "correct" one
        // serverDataPacket.setMotd( null );
        // serverDataPacket.setIcon( null );
        // con.unsafe().sendPacket( serverDataPacket );
        throw CancelSendSignal.INSTANCE;
    }

    @Override
    public void handle(PlayerCreationPacket playerCreationPacket) throws Exception {

        receivedLogin = true;
        ServerConnector.handleLogin(bungee, server.getCh(), con, server.getInfo(), server, playerCreationPacket);
        throw CancelSendSignal.INSTANCE;
    }

    @Override
    public String toString() {
        return "[" + con.getName() + "] <-> DownstreamBridge <-> [" + server.getInfo().getName() + "]";
    }
}
