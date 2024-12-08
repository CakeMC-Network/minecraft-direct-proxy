package net.cakemc.de.crycodes.proxy.target;


import net.cakemc.de.crycodes.proxy.AbstractProxyService;
import net.cakemc.de.crycodes.proxy.channel.PlayerChannel;
import net.cakemc.de.crycodes.proxy.connection.DownstreamBridge;
import net.cakemc.de.crycodes.proxy.connection.data.CancelSendSignal;
import net.cakemc.de.crycodes.proxy.events.server.ProxyServerConnectedEvent;
import net.cakemc.de.crycodes.proxy.events.server.ProxyServerKickEvent;
import net.cakemc.de.crycodes.proxy.events.server.ProxyServerSwitchEvent;
import net.cakemc.de.crycodes.proxy.network.PacketHandler;
import net.cakemc.de.crycodes.proxy.network.PacketReader;
import net.cakemc.de.crycodes.proxy.network.packet.AbstractPacket;
import net.cakemc.de.crycodes.proxy.network.packet.ProtocolPacket;
import net.cakemc.de.crycodes.proxy.network.packet.impl.*;
import net.cakemc.de.crycodes.proxy.network.packet.impl.intent.PlayerIntentPacket;
import net.cakemc.de.crycodes.proxy.network.packet.impl.login.EncryptionRequestPacket;
import net.cakemc.de.crycodes.proxy.network.packet.impl.login.ServerGameProfilePacket;
import net.cakemc.de.crycodes.proxy.network.packet.impl.login.ServerHelloPacket;
import net.cakemc.de.crycodes.proxy.network.packet.impl.login.ServerSetCompressionPacket;
import net.cakemc.de.crycodes.proxy.player.ConnectedPlayer;
import net.cakemc.de.crycodes.proxy.protocol.Protocol;
import net.cakemc.de.crycodes.proxy.protocol.ProtocolVersion;
import net.cakemc.mc.lib.common.type.json.JsonArray;
import net.cakemc.mc.lib.common.type.json.JsonObject;
import net.cakemc.mc.lib.game.PlayerProfile;
import net.cakemc.mc.lib.game.text.test.api.chat.BaseComponent;

import java.net.Inet6Address;
import java.net.InetSocketAddress;
import java.util.Locale;
import java.util.Queue;
import java.util.UUID;

/**
 * The type TargetServer connector.
 */
public class ServerConnector extends PacketHandler {
    private final AbstractProxyService bungee;
    private final ConnectedPlayer user;
    private final ProxyTargetImpl target;
    private PlayerChannel ch;
    private State thisState = State.LOGIN_SUCCESS;
    private boolean obsolete;

    /**
     * Instantiates a new TargetServer connector.
     *
     * @param bungee the bungee
     * @param user   the user
     * @param target the target
     */
    public ServerConnector(final AbstractProxyService bungee, final ConnectedPlayer user, final ProxyTargetImpl target) {
        this.bungee = bungee;
        this.user = user;
        this.target = target;
    }

    /**
     * Handle login.
     *
     * @param bungee               the bungee
     * @param ch                   the ch
     * @param user                 the user
     * @param target               the target
     * @param server               the server
     * @param playerCreationPacket the player creation packet
     * @throws Exception the exception
     */
    public static void handleLogin(AbstractProxyService bungee, PlayerChannel ch, ConnectedPlayer user, ProxyTargetImpl target,
                                   TargetServerConnection server, PlayerCreationPacket playerCreationPacket) throws Exception {
        ProxyServerConnectedEvent event = new ProxyServerConnectedEvent(user, server);
        bungee.getEventManager().call(event);
        Queue<AbstractPacket> packetQueue = target.getPacketQueue();
        synchronized (packetQueue) {
            while (!packetQueue.isEmpty()) {
                ch.write(packetQueue.poll());
            }
        }
        if (user.getSettings() != null) {
            ch.write(user.getSettings());
        }
        if (user.getServer() == null || user.getPendingConnection().getVersion() >= ProtocolVersion.MINECRAFT_1_16.getProtocolId()) {
            user.setClientEntityId(playerCreationPacket.getEntityId());
            user.setServerEntityId(playerCreationPacket.getEntityId());

            PlayerCreationPacket modPlayerCreationPacket = new PlayerCreationPacket(playerCreationPacket.getEntityId(), playerCreationPacket.isHardcore(), playerCreationPacket.getGameMode(), playerCreationPacket.getPreviousGameMode(), playerCreationPacket.getWorldNames(), playerCreationPacket.getDimensions(), playerCreationPacket.getDimension(), playerCreationPacket.getWorldName(), playerCreationPacket.getSeed(), playerCreationPacket.getDifficulty(), (byte)
                    70, playerCreationPacket.getLevelType(), playerCreationPacket.getViewDistance(), playerCreationPacket.getSimulationDistance(), playerCreationPacket.isReducedDebugInfo(), playerCreationPacket.isNormalRespawn(), playerCreationPacket.isLimitedCrafting(), playerCreationPacket.isDebug(), playerCreationPacket.isFlat(), playerCreationPacket.getDeathLocation(), playerCreationPacket.getPortalCooldown(), playerCreationPacket.getSeaLevel(), playerCreationPacket.isSecureProfile());
            user.sendPacket(modPlayerCreationPacket);

            if (user.getDimension() != null) {
                user.sendPacket(new RespawnPacket(playerCreationPacket.getDimension(), playerCreationPacket.getWorldName(), playerCreationPacket.getSeed(),
                        playerCreationPacket.getDifficulty(), playerCreationPacket.getGameMode(), playerCreationPacket.getPreviousGameMode(), playerCreationPacket.getLevelType(), playerCreationPacket.isDebug(), playerCreationPacket.isFlat(), (byte) 0, playerCreationPacket.getDeathLocation(), playerCreationPacket.getPortalCooldown(), playerCreationPacket.getSeaLevel()));
            }

            user.setDimension(playerCreationPacket.getDimension());
        } else {
            user.getServer().setObsolete(true);
            // Update debug info from login packet
            user.sendPacket(new EntityStatusPacket(user.getClientEntityId(), playerCreationPacket.isReducedDebugInfo() ? EntityStatusPacket.DEBUG_INFO_REDUCED : EntityStatusPacket.DEBUG_INFO_NORMAL));
            // And immediate respawn
            if (user.getPendingConnection().getVersion() >= ProtocolVersion.MINECRAFT_1_15.getProtocolId()) {
                user.sendPacket(new GameStatePacket(GameStatePacket.IMMEDIATE_RESPAWN, playerCreationPacket.isNormalRespawn() ? 0 : 1));
            }
            user.setDimensionChange(true);
            if (playerCreationPacket.getDimension() == user.getDimension()) {
                user.sendPacket(new RespawnPacket((Integer) playerCreationPacket.getDimension() >= 0 ? -1 : 0, playerCreationPacket.getWorldName(), playerCreationPacket.getSeed(), playerCreationPacket.getDifficulty(), playerCreationPacket.getGameMode(), playerCreationPacket.getPreviousGameMode(), playerCreationPacket.getLevelType(), playerCreationPacket.isDebug(), playerCreationPacket.isFlat(), (byte) 0, playerCreationPacket.getDeathLocation(), playerCreationPacket.getPortalCooldown(), playerCreationPacket.getSeaLevel()));
            }
            user.setServerEntityId(playerCreationPacket.getEntityId());
            user.sendPacket(new RespawnPacket(playerCreationPacket.getDimension(), playerCreationPacket.getWorldName(), playerCreationPacket.getSeed(), playerCreationPacket.getDifficulty(), playerCreationPacket.getGameMode(), playerCreationPacket.getPreviousGameMode(), playerCreationPacket.getLevelType(), playerCreationPacket.isDebug(), playerCreationPacket.isFlat(), (byte) 0, playerCreationPacket.getDeathLocation(), playerCreationPacket.getPortalCooldown(), playerCreationPacket.getSeaLevel()));
            if (user.getPendingConnection().getVersion() >= ProtocolVersion.MINECRAFT_1_14.getProtocolId()) {
                user.sendPacket(new SetSimulationDistancePacket(playerCreationPacket.getViewDistance()));
            }
            user.setDimension(playerCreationPacket.getDimension());
        }
    }

    @Override
    public void exception(Throwable t) throws Exception {
        if (obsolete) {
            return;
        }
        String message = "Exception Connecting: " + t.getMessage();
        if (user.getServer() == null) {
            user.disconnect(message);
        } else {
            user.sendMessage(message);
        }
    }

    @Override
    public void connected(PlayerChannel channel) throws Exception {
        this.ch = channel;
        PlayerIntentPacket originalPlayerIntentPacket = user.getPendingConnection().getHandshake();
        PlayerIntentPacket copiedPlayerIntentPacket = new PlayerIntentPacket(originalPlayerIntentPacket.getProtocolVersion(),
                originalPlayerIntentPacket.getHost(), originalPlayerIntentPacket.getPort(), 2);

        // append proxy-data
        String newHost = copiedPlayerIntentPacket.getHost() + "\00" + sanitizeAddress(user.getAddress()) + "\00" + user.getUUID();
        copiedPlayerIntentPacket.setHost(newHost);

        PlayerProfile profile = this.user.getPendingConnection().getProfile();
        if (profile != null && profile.getProperties() != null && !profile.getProperties().isEmpty()) {
            JsonArray array = new JsonArray();

            for (PlayerProfile.Property property : profile.getProperties()) {
                JsonObject object = new JsonObject();
                object.add("name", property.getName());
                object.add("value", property.getValue());
                object.add("signature", property.getSignature());

                array.add(object);
            }
            newHost += "\00" + array.toString();
        }

        copiedPlayerIntentPacket.setHost(newHost);
        // append proxy-data

        if (!user.getExtraDataInHandshake().isEmpty()) {
            copiedPlayerIntentPacket.setHost(copiedPlayerIntentPacket.getHost() + user.getExtraDataInHandshake());
        }

        channel.write(copiedPlayerIntentPacket);
        channel.setProtocol(Protocol.LOGIN);
        channel.write(new ServerHelloPacket(user.getName(), null, user.getUniqueId()));
    }

    String sanitizeAddress(InetSocketAddress address) {
        String string = address.getAddress().getHostAddress();

        // Remove IPv6 scope if present
        if (address.getAddress() instanceof Inet6Address) {
            int strip = string.indexOf('%');
            return (strip == -1) ? string : string.substring(0, strip);
        } else {
            return string;
        }
    }

    @Override
    public void disconnected(PlayerChannel channel) throws Exception {
        user.getPendingConnects().remove(target);
    }

    @Override
    public void handle(ProtocolPacket packet) throws Exception {
        if (packet.packet == null) {
            throw new RuntimeException("Unexpected packet received during server login process!");
        }
    }

    @Override
    public void handle(ServerGameProfilePacket serverGameProfilePacket) throws Exception {

        if (user.getPendingConnection().getVersion() >= ProtocolVersion.MINECRAFT_1_20_2.getProtocolId()) {
            TargetServerConnection server = new TargetServerConnection(ch, target);
            cutThrough(server);
        } else {
            ch.setProtocol(Protocol.GAME);
            thisState = State.LOGIN;
        }

        throw CancelSendSignal.INSTANCE;
    }

    @Override
    public void handle(ServerSetCompressionPacket serverSetCompressionPacket) throws Exception {
        ch.setCompressionThreshold(serverSetCompressionPacket.getThreshold());
    }

    @Override
    public void handle(PlayerCreationPacket playerCreationPacket) throws Exception {

        TargetServerConnection server = new TargetServerConnection(ch, target);
        handleLogin(bungee, ch, user, target, server, playerCreationPacket);
        cutThrough(server);
    }

    private void cutThrough(TargetServerConnection server) {
        if (!user.isActive()) {
            server.disconnect("Quitting");
            return;
        }
        if (user.getPendingConnection().getVersion() >= ProtocolVersion.MINECRAFT_1_20_2.getProtocolId()) {
            if (user.getServer() != null) {
                // Begin config mode
                user.sendPacket(new StartConfigurationPacket());
            } else {
                PlayerProfile playerProfile = user.getPendingConnection().getProfile();

                // TODO THIS THROWS: "Invalid signature for profile public key."
                UUID uuid = user.getUniqueId(); //UUID.randomUUID(); // user.getUniqueId();

                user.sendPacket(new ServerGameProfilePacket(uuid, user.getName(),
                        (playerProfile == null) ? new PlayerProfile.Property[0] : playerProfile.getProperties().toArray(new PlayerProfile.Property[0])));

                user.getCh().setEncodeProtocol(Protocol.CONFIGURATION);
            }
        }
        if (user.getServer() != null) {
            user.getServer().disconnect("Quitting");
        }

        target.addPlayer(user);
        user.getPendingConnects().remove(target);
        user.setServerJoinQueue(null);
        user.setDimensionChange(false);
        AbstractTarget from = (user.getServer() == null) ? null : user.getServer().getInfo();
        user.setServer(server);
        ch.getHandle().pipeline().get(PacketReader.class).setHandler(new DownstreamBridge(bungee, user, server));
        bungee.getEventManager().call(new ProxyServerSwitchEvent(user, from));
        thisState = State.FINISHED;
        throw CancelSendSignal.INSTANCE;
    }

    @Override
    public void handle(EncryptionRequestPacket encryptionRequestPacket) throws Exception {
        throw new RuntimeException("TargetServer is online mode!");
    }

    @Override
    public void handle(DisconnectPacket disconnectPacket) throws Exception {
        AbstractTarget def = user.updateAndGetNextServer(target);
        ProxyServerKickEvent event = new ProxyServerKickEvent(user, target, new BaseComponent[]{disconnectPacket.getMessage()}, def, ProxyServerKickEvent.State.CONNECTING);
        if (event.getKickReason().toLowerCase(Locale.ROOT).contains("outdated") && def != null) {
            // Pre cancel the event if we are going to try another server
            event.setCancelled(true);
        }
        bungee.getEventManager().call(event);
        if (event.isCancelled() && event.getCancelServer() != null) {
            obsolete = true;
            user.connect(event.getCancelServer(), ConnectionReason.KICK_REDIRECT);
            throw CancelSendSignal.INSTANCE;
        }
        if (user.isDimensionChange()) {
            user.disconnect(event.getKickReason());
        } else {
            user.sendMessage(event.getKickReason());
        }
        throw CancelSendSignal.INSTANCE;
    }

    @Override
    public void handle(LoginPayloadRequestPacket loginPayloadRequestPacket) {
        ch.write(new LoginPayloadResponsePacket(loginPayloadRequestPacket.getId(), null));
    }

    @Override
    public String toString() {
        return "[" + user.getName() + "] <-> ServerConnector [" + target.getName() + "]";
    }

    private enum State {
        /**
         * Login success state.
         */
        LOGIN_SUCCESS,
        /**
         * Login state.
         */
        LOGIN,
        /**
         * Finished state.
         */
        FINISHED
    }


}
