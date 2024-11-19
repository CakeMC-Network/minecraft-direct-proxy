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
        final int protocol = (ProtocolVersion.getSupportedVersionIds()
                .contains(loginHandler.getPlayerIntentPacket().getProtocolVersion())) ?
                loginHandler.getPlayerIntentPacket().getProtocolVersion() : loginHandler.getService().getProtocolVersion();

        Status.Info pingInfo = getPingInfo(motd, protocol);

        ProxyPingEvent proxyPingEvent = new ProxyPingEvent(loginHandler, pingInfo);
        loginHandler.getService().getEventManager().call(proxyPingEvent);

        loginHandler.sendPacket(new StatusResponsePacket(proxyPingEvent.getResponse()));

        loginHandler.setThisState(ProxyLoginHandler.State.PING);
        loginHandler.getChannel().close();
    }

    private String getFirstLine(String str) {
        int pos = str.indexOf('\n');
        return pos == -1 ? str : str.substring(0, pos);
    }


}
