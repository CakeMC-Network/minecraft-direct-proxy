package net.cakemc.de.crycodes.proxy.connection.handler;

import net.cakemc.de.crycodes.proxy.ProxyServiceImpl;
import net.cakemc.de.crycodes.proxy.events.ProxyPingEvent;
import net.cakemc.de.crycodes.proxy.network.packet.ProtocolPacket;
import net.cakemc.de.crycodes.proxy.network.packet.impl.LegacyHandshakePacket;
import net.cakemc.de.crycodes.proxy.network.packet.impl.LegacyPingPacket;
import net.cakemc.de.crycodes.proxy.network.packet.impl.status.StatusRequestPacket;
import net.cakemc.de.crycodes.proxy.network.packet.impl.status.StatusResponsePacket;
import net.cakemc.de.crycodes.proxy.protocol.ProtocolVersion;
import net.cakemc.mc.lib.game.Status;
import net.cakemc.mc.lib.game.text.rewrite.Text;

import java.util.ArrayList;

/**
 * The type Status handler.
 */
public class StatusHandler {

    private final ProxyLoginHandler loginHandler;

    /**
     * Instantiates a new Status handler.
     *
     * @param loginHandler the login handler
     */
    public StatusHandler(ProxyLoginHandler loginHandler) {
        this.loginHandler = loginHandler;
    }

    /**
     * Handle.
     *
     * @param packet the packet
     * @throws Exception the exception
     */
    public void handle(ProtocolPacket packet) throws Exception {
        if (packet.packet == null) {
            throw new RuntimeException("Unexpected packet received during login process!");
        }
    }

    /**
     * Handle.
     *
     * @param legacyHandshakePacket the legacy handshake packet
     * @throws Exception the exception
     */
    public void handle(LegacyHandshakePacket legacyHandshakePacket) throws Exception {
        loginHandler.setLegacy(true);
        loginHandler.getChannel().close("outdated minecraft version!");
    }

    /**
     * Handle.
     *
     * @param ping the ping
     * @throws Exception the exception
     */
    public void handle(LegacyPingPacket ping) throws Exception {
        loginHandler.setLegacy(true);
        final boolean v1_5 = ping.isV1_5();

        final String motd = loginHandler.getListener().getMotd();
        final int protocol = loginHandler.getService().getProtocolVersion();
        //Callback<ServerPing> pingBack = (result, error) -> {
        //    if (error != null) {
        //        result = getPingInfo("can't ping service...", protocol);
        //    }
        //
        //    ProxyPingEvent proxyPingEvent = new ProxyPingEvent(loginHandler, result);
        //    loginHandler.getService().getEventManager().call(proxyPingEvent);
        //    if (loginHandler.getChannel().isClosing()) {
        //        return;
        //    }
        //
        //    ServerPing legacy = proxyPingEvent.getResponse();
        //    String kickMessage;
        //    if (v1_5) {
        //        kickMessage = ChatColor.DARK_BLUE + "\000" + 127 + '\000' + legacy.getVersion().getName() + '\000' + getFirstLine(legacy.getDescription())
        //                + '\000' + ((legacy.getPlayers() != null) ? legacy.getPlayers().getOnline() : "-1") + '\000' + ((legacy.getPlayers() != null) ? legacy.getPlayers().getMax() : "-1");
        //    } else {
        //        // Clients <= 1.3 don't support colored motds because the color char is used as delimiter
        //        kickMessage = ChatColor.stripColor(getFirstLine(legacy.getDescription())) + '§' + ((legacy.getPlayers() != null) ?
        //                legacy.getPlayers().getOnline() : "-1") + '§' + ((legacy.getPlayers() != null) ? legacy.getPlayers().getMax() : "-1");
        //    }
        //    loginHandler.getChannel().close(kickMessage);
        //
        //};
        //pingBack.done(getPingInfo(motd, protocol), null);
    }

    private Status.Info getPingInfo(String motd, int protocol) {
        return new Status.Info(
                new Text(motd), new Status.PlayerList(100, 1, new ArrayList<>()),
                new Status.Version("custom proxy", protocol), ProxyServiceImpl.SERVER_ICON, false
        );
    }

    /**
     * Handle.
     *
     * @param statusRequestPacket the status request packet
     * @throws Exception the exception
     */
    public void handle(StatusRequestPacket statusRequestPacket) throws Exception {
        final String motd = loginHandler.getListener().getMotd();
        final int protocol = (ProtocolVersion.getSupportedVersionIds().contains(loginHandler.getPlayerIntentPacket().getProtocolVersion())) ?
                loginHandler.getPlayerIntentPacket().getProtocolVersion() : loginHandler.getService().getProtocolVersion();

        Status.Info pingInfo = getPingInfo(motd, protocol);

        ProxyPingEvent proxyPingEvent = new ProxyPingEvent(loginHandler, pingInfo);
        loginHandler.getService().getEventManager().call(proxyPingEvent);

        loginHandler.sendPacket(new StatusResponsePacket(proxyPingEvent.getResponse()));

        loginHandler.setThisState(ProxyLoginHandler.State.PING);
    }

    private String getFirstLine(String str) {
        int pos = str.indexOf('\n');
        return pos == -1 ? str : str.substring(0, pos);
    }


}
