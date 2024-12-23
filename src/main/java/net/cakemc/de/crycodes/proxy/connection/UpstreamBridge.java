package net.cakemc.de.crycodes.proxy.connection;


import io.netty.channel.Channel;
import net.cakemc.de.crycodes.proxy.AbstractProxyService;
import net.cakemc.de.crycodes.proxy.channel.PlayerChannel;
import net.cakemc.de.crycodes.proxy.connection.data.CancelSendSignal;
import net.cakemc.de.crycodes.proxy.events.ProxySettingsChangedEvent;
import net.cakemc.de.crycodes.proxy.events.connect.ProxyPlayerDisconnectEvent;
import net.cakemc.de.crycodes.proxy.network.PacketHandler;
import net.cakemc.de.crycodes.proxy.network.packet.ProtocolPacket;
import net.cakemc.de.crycodes.proxy.network.packet.impl.ClientSettingsPacket;
import net.cakemc.de.crycodes.proxy.network.packet.impl.FinishConfigurationPacket;
import net.cakemc.de.crycodes.proxy.network.packet.impl.StartConfigurationPacket;
import net.cakemc.de.crycodes.proxy.network.packet.impl.login.ServerLoginAcknowledgedPacket;
import net.cakemc.de.crycodes.proxy.player.ConnectedPlayer;
import net.cakemc.de.crycodes.proxy.protocol.Protocol;
import net.cakemc.de.crycodes.proxy.target.TargetServerConnection;
import net.cakemc.de.crycodes.proxy.target.TargetServerConnection.KeepAliveData;

/**
 * The type Upstream bridge.
 */
public class UpstreamBridge extends PacketHandler {

    private final AbstractProxyService abstractProxyService;
    private final ConnectedPlayer con;

    /**
     * Instantiates a new Upstream bridge.
     *
     * @param abstractProxyService the abstract proxy service
     * @param con                  the con
     */
    public UpstreamBridge(AbstractProxyService abstractProxyService, ConnectedPlayer con) {
        this.abstractProxyService = abstractProxyService;
        this.con = con;
    }

    @Override
    public void exception(Throwable t) throws Exception {
        System.err.printf("EX exception in connection: %s%n", t.getMessage());
    }

    @Override
    public void disconnected(PlayerChannel channel) throws Exception {
        // We lost connection to the client
        ProxyPlayerDisconnectEvent event = new ProxyPlayerDisconnectEvent(con);
        abstractProxyService.getEventManager().call(event);
        abstractProxyService.removeConnection(con);

        if (con.getServer() != null) {
            con.getServer().disconnect("Quitting");
        }
    }

    @Override
    public void writabilityChanged(PlayerChannel channel) throws Exception {
        if (con.getServer() != null) {
            Channel server = con.getServer().getCh().getHandle();
            server.config().setAutoRead(channel.getHandle().isWritable());
        }
    }

    @Override
    public boolean shouldHandle(ProtocolPacket packet) throws Exception {
        return con.getServer() != null;
    }

    @Override
    public void handle(ProtocolPacket packet) throws Exception {
        TargetServerConnection server = con.getServer();
        if (server != null && server.isConnected()) {
            Protocol serverEncode = server.getCh().getEncodeProtocol();
            // #3527: May still have old packets from client in game state when switching server to configuration state - discard those
            if (packet.protocol != serverEncode) {
                return;
            }

            server.getCh().write(packet);
        }
    }

    @Override
    public void handle(ClientSettingsPacket settings) throws Exception {
        con.setSettings(settings);

        ProxySettingsChangedEvent settingsEvent = new ProxySettingsChangedEvent(con);
        abstractProxyService.getEventManager().call(settingsEvent);
    }

    @Override
    public void handle(ServerLoginAcknowledgedPacket serverLoginAcknowledgedPacket) throws Exception {
        configureServer();
    }

    @Override
    public void handle(StartConfigurationPacket startConfigurationPacket) throws Exception {
        configureServer();
    }

    private void configureServer() {
        PlayerChannel ch = con.getServer().getCh();
        if (ch.getDecodeProtocol() == Protocol.LOGIN) {
            ch.setDecodeProtocol(Protocol.CONFIGURATION);
            ch.write(new ServerLoginAcknowledgedPacket());
            ch.setEncodeProtocol(Protocol.CONFIGURATION);

            con.getServer().sendQueuedPackets();

            throw CancelSendSignal.INSTANCE;
        }
    }

    @Override
    public void handle(FinishConfigurationPacket finishConfigurationPacket) throws Exception {
        con.sendQueuedPackets();
    }


    @Override
    public String toString() {
        return "[" + con.getName() + "] -> UpstreamBridge";
    }
}
