package net.cakemc.de.crycodes.proxy.player;

import net.cakemc.de.crycodes.proxy.connection.ConnectionListener;
import net.cakemc.de.crycodes.proxy.events.server.ProxyServerConnectEvent;
import net.cakemc.de.crycodes.proxy.target.AbstractTarget;
import net.cakemc.de.crycodes.proxy.target.ConnectionReason;
import net.cakemc.de.crycodes.proxy.target.TargetServer;
import net.cakemc.de.crycodes.proxy.target.TargetRequest;
import net.cakemc.mc.lib.game.text.test.api.ChatMessageType;
import net.cakemc.mc.lib.game.text.test.api.chat.BaseComponent;

import java.util.Locale;
import java.util.UUID;


/**
 * The interface Proxied player.
 */
public interface ProxyPlayer extends Connection {

    /**
     * Gets display name.
     *
     * @return the display name
     */
    String getDisplayName();

    /**
     * Sets display name.
     *
     * @param name the name
     */
    void setDisplayName(String name);


    /**
     * Connect.
     *
     * @param target the target
     */
    void connect(AbstractTarget target);

    /**
     * Connect.
     *
     * @param target the target
     * @param reason the reason
     */
    void connect(AbstractTarget target, ConnectionReason reason);

    /**
     * Connect.
     *
     * @param target             the target
     * @param connectionListener the connection listener
     */
    void connect(AbstractTarget target, ConnectionListener<Boolean> connectionListener);

    /**
     * Connect.
     *
     * @param target             the target
     * @param connectionListener the connection listener
     * @param reason             the reason
     */
    void connect(AbstractTarget target, ConnectionListener<Boolean> connectionListener, ConnectionReason reason);

    /**
     * Connect.
     *
     * @param request the request
     */
    void connect(TargetRequest request);

    /**
     * Gets server.
     *
     * @return the server
     */
    TargetServer getServer();

    /**
     * Gets ping.
     *
     * @return the ping
     */
    int getPing();

    /**
     * Gets pending connection.
     *
     * @return the pending connection
     */
    PendingConnection getPendingConnection();

    /**
     * Gets reconnect server.
     *
     * @return the reconnect server
     */
    AbstractTarget getReconnectServer();

    /**
     * Sets reconnect server.
     *
     * @param server the server
     */
    void setReconnectServer(AbstractTarget server);

    /**
     * Gets uuid.
     *
     * @return the uuid
     */
    String getUUID();

    /**
     * Gets unique id.
     *
     * @return the unique id
     */
    UUID getUniqueId();

    /**
     * Gets locale.
     *
     * @return the locale
     */
    Locale getLocale();

    /**
     * Gets view distance.
     *
     * @return the view distance
     */
    byte getViewDistance();

    /**
     * Gets chat mode.
     *
     * @return the chat mode
     */
    ChatMode getChatMode();

    /**
     * Has chat colors boolean.
     *
     * @return the boolean
     */
    boolean hasChatColors();

    /**
     * Transfer.
     *
     * @param host the host
     * @param port the port
     */
    void transfer(String host, int port);

    /**
     * Gets name.
     *
     * @return the name
     */
    String getName();

    /**
     * The enum Chat mode.
     */
    enum ChatMode {

        /**
         * Shown chat mode.
         */
        SHOWN,
        /**
         * Commands only chat mode.
         */
        COMMANDS_ONLY,
        /**
         * Hidden chat mode.
         */
        HIDDEN

    }


}
