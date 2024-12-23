package net.cakemc.de.crycodes.proxy.connection.handler;


import net.cakemc.de.crycodes.proxy.ProxyServiceImpl;
import net.cakemc.de.crycodes.proxy.channel.PlayerChannel;
import net.cakemc.de.crycodes.proxy.connection.UpstreamBridge;
import net.cakemc.de.crycodes.proxy.events.connect.ProxyLoginEvent;
import net.cakemc.de.crycodes.proxy.network.PacketHandler;
import net.cakemc.de.crycodes.proxy.network.PacketReader;
import net.cakemc.de.crycodes.proxy.network.PipelineUtils;
import net.cakemc.de.crycodes.proxy.network.codec.cipher.*;
import net.cakemc.de.crycodes.proxy.network.http.ClientAuthHttpCallBack;
import net.cakemc.de.crycodes.proxy.network.http.HttpClient;
import net.cakemc.de.crycodes.proxy.network.packet.AbstractPacket;
import net.cakemc.de.crycodes.proxy.network.packet.ProtocolPacket;
import net.cakemc.de.crycodes.proxy.network.packet.impl.*;
import net.cakemc.de.crycodes.proxy.network.packet.impl.intent.PlayerIntentPacket;
import net.cakemc.de.crycodes.proxy.network.packet.impl.login.*;
import net.cakemc.de.crycodes.proxy.network.packet.impl.status.StatusRequestPacket;
import net.cakemc.de.crycodes.proxy.player.ConnectedPlayer;
import net.cakemc.de.crycodes.proxy.player.PendingConnection;
import net.cakemc.de.crycodes.proxy.player.ProxyPlayer;
import net.cakemc.de.crycodes.proxy.protocol.Protocol;
import net.cakemc.de.crycodes.proxy.protocol.ProtocolVersion;
import net.cakemc.de.crycodes.proxy.target.AbstractTarget;
import net.cakemc.de.crycodes.proxy.target.ConnectionReason;
import net.cakemc.de.crycodes.proxy.units.ProxyServiceAddress;
import net.cakemc.mc.lib.game.PlayerProfile;
import net.cakemc.mc.lib.game.text.test.api.chat.BaseComponent;
import net.cakemc.mc.lib.game.text.test.api.chat.TextComponent;

import javax.crypto.SecretKey;
import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.security.KeyPair;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Pattern;

/**
 * The type Proxy login handler.
 */
public class ProxyLoginHandler extends PacketHandler implements PendingConnection {

    private static final String BASE_URL =
            "https://sessionserver.mojang.com/session/minecraft/hasJoined";

    private static final Pattern USER_NAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_]+$");

    private final String sessionId = Long.toString(ThreadLocalRandom.current().nextLong(), 16).trim();

    private final ProxyServiceImpl service;
    private final ProxyServiceAddress listener;
    private final Set<String> registeredChannels = new HashSet<>();
    private final boolean onlineMode;
    private final byte[] pass;
    private final KeyPair keyPair;
    private final StatusHandler statusHandler;
    private PlayerChannel channel;
    private PlayerIntentPacket playerIntentPacket;
    private ServerHelloPacket serverHelloPacket;
    private EncryptionRequestPacket request;
    private State thisState = State.HANDSHAKE;
    private InetSocketAddress virtualHost;
    // user data
    private PlayerProfile profile;
    // user data
    private String name;
    private UUID uniqueId;
    private boolean legacy;
    private String extraDataInHandshake = "";
    private boolean transferred;
    private ConnectedPlayer userCon;

    /**
     * Instantiates a new Proxy login handler.
     *
     * @param service    the service
     * @param listener   the listener
     * @param onlineMode the online mode
     */
    public ProxyLoginHandler(final ProxyServiceImpl service, final ProxyServiceAddress listener, boolean onlineMode) {
        this.service = service;
        this.listener = listener;

        this.onlineMode = onlineMode;

        this.statusHandler = new StatusHandler(this);

        {
            this.pass = CryptUnits.generatePass();
            this.keyPair = CryptUnits.generateKeyPair();
        }
    }

    private static boolean isNameAllowedCharacter(char c) {
        return (c >= 'a' && c <= 'z') || (c >= '0' && c <= '9') || (c >= 'A' && c <= 'Z') || c == '_';
    }

    // ####################################################################################################################

    /**
     * Is valid name boolean.
     *
     * @param name the name
     * @return the boolean
     */
    public static boolean isValidName(String name) {
        if (name.isEmpty() || name.length() > 16) {
            return false;
        }
        for (int index = 0, len = name.length(); index < len; index++) {
            if (!isNameAllowedCharacter(name.charAt(index))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void handle(PlayerIntentPacket playerIntentPacket) throws Exception {

        this.playerIntentPacket = playerIntentPacket;
        channel.setVersion(playerIntentPacket.getProtocolVersion());
        channel.getHandle().pipeline().remove(PipelineUtils.LEGACY_KICKER);

        this.virtualHost = InetSocketAddress.createUnresolved(playerIntentPacket.getHost(), playerIntentPacket.getPort());

        if (channel.isClosing()) {
            return;
        }
        switch (playerIntentPacket.getRequestedProtocol()) {
            case 1:
                thisState = State.STATUS;
                channel.setProtocol(Protocol.STATUS);
                break;
            case 2:
            case 3:
                transferred = playerIntentPacket.getRequestedProtocol() == 3;
                // Login
                thisState = State.USERNAME;
                channel.setProtocol(Protocol.LOGIN);
                if (!ProtocolVersion.getSupportedVersionIds().contains(playerIntentPacket.getProtocolVersion())) {
                    if (playerIntentPacket.getProtocolVersion() > service.getProtocolVersion()) {
                        disconnect("This service is running a lower minecraft version!");
                    } else {
                        disconnect("Your client is to old to join this service!");
                    }
                    return;
                }
                break;
            default:
                throw new RuntimeException("Cannot request protocol " + playerIntentPacket.getRequestedProtocol());
        }
    }

    @Override
    public void handle(ServerHelloPacket packet) throws Exception {
        this.name = packet.getData();

        this.serverHelloPacket = packet;

        String userName = packet.getData();
        int length = userName.length();
        if (length > 16 || !USER_NAME_PATTERN.matcher(userName).find()) {
            disconnect(new TextComponent("invalid username!"));
            return;
        }

        if (this.onlineMode) {
            // Get necessary information to create our request message
            setName(userName);

            // Send created request message and wait for the response
            sendPacket(new EncryptionRequestPacket(sessionId,
                    keyPair.getPublic().getEncoded(), pass,
                    true));
            thisState = State.ENCRYPT;
        } else {
            profile = (new PlayerProfile(
                    UUID.nameUUIDFromBytes("OfflineUser:%s".formatted(userName).getBytes()).toString(),
                    userName)
            );

            this.setName(userName);
            this.setUniqueId(profile.getUUID());

            sendPacket(new ServerGameProfilePacket(
                    uniqueId, name, profile.getProperties().toArray(new PlayerProfile.Property[0])
            ));

            thisState = State.FINISHING;
            this.finish();
        }

        //if (onlineMode) {
        //    AuthService.get().byUUID(packet.getUuid().toString()).thenAcceptAsync(user -> {
        //        if (user == null) {
        //            this.disconnect("§cmojang auth offline?");
        //        }
        //        PlayerProfile profile = new PlayerProfile(packet.getUuid().toString(), user.name());
        //        Arrays.stream(user.properties())
        //                .map(properties -> new PlayerProfile
        //                        .Property(properties.name(), properties.value(), properties.signature()))
        //                .forEach(property -> profile.getProperties().add(property));
        //        this.profile = profile;
        //
        //        EncryptionRequestPacket encryptionRequestPacket = new EncryptionRequestPacket(
        //                "", keyPair.getPublic().getEncoded(), this.pass, true
        //        );
        //        sendPacket(encryptionRequestPacket);
        //        thisState = State.ENCRYPT;
        //    }).join();
        //
        //} else {
        //    // offline profile
        //    AuthService.get().byUUID(packet.getUuid().toString()).thenAcceptAsync(user -> {
        //        if (user != null) {
        //            PlayerProfile profile = new PlayerProfile(packet.getUuid().toString(), user.name());
        //            Arrays.stream(user.properties())
        //                    .map(properties -> new PlayerProfile
        //                            .Property(properties.name(), properties.value(), properties.signature()))
        //                    .forEach(property -> profile.getProperties().add(property));
        //            this.profile = profile;
        //            return;
        //        }
        //        this.profile = new PlayerProfile(packet.getUuid().toString(), packet.getData());
        //    }).join();
        //
        //    ServerGameProfilePacket profilePacket = new ServerGameProfilePacket(
        //            uniqueId, name, profile.getProperties().toArray(new PlayerProfile.Property[0]));
        //
        //    sendPacket(profilePacket);
        //    thisState = State.FINISHING;
        //}
    }

    @Override
    public void handle(final EncryptionResponsePacket encryptResponse) throws Exception {
        SecretKey secretKey = CryptUnits.getSecretKey(this.keyPair.getPrivate(), encryptResponse.getSharedSecret());

        boolean matching = Arrays.equals(this.pass, CryptUnits.getEncryptedPass(keyPair.getPrivate(), encryptResponse.getVerifyToken()));
        if (!matching)
            throw new IllegalStateException("keys miss matching!");

        Encryption decrypt = EncryptionUtil.getCipher(false, secretKey);
        channel.addBefore(PipelineUtils.FRAME_DECODER, PipelineUtils.DECRYPT_HANDLER, new CipherDecoder(decrypt));

        Encryption encrypt = EncryptionUtil.getCipher(true, secretKey);
        channel.addBefore(PipelineUtils.FRAME_PREPENDER, PipelineUtils.ENCRYPT_HANDLER, new CipherEncoder(encrypt));

        // create hash for auth
        String hash;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(sessionId.getBytes());
            digest.update(secretKey.getEncoded());
            digest.update(keyPair.getPublic().getEncoded());

            // BigInteger takes care of sign and leading zeroes
            hash = new BigInteger(digest.digest()).toString(16);
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
            return;
        }

        String url = BASE_URL + "?username=" + getName() // NON-NLS
                + "&serverId=" + hash; // NON-NLS

        HttpClient.connectWithResult(
                url, channel.getHandle().eventLoop(),
                new ClientAuthHttpCallBack(this)
        );
    }

    public void finish() {
        ProxyPlayer oldName = service.getPlayer(getName());
        if (oldName != null) {
            disconnect("already connected to this proxy instance");
            return;
        }
        ProxyPlayer oldID = service.getPlayer(getUniqueId());
        if (oldID != null) {
            disconnect("already connected to this proxy instance");
            return;
        }

        if (channel.isClosing()) {
            return;
        }
        channel.getHandle().eventLoop().execute(() -> {
            if (!channel.isClosing()) {
                userCon = new ConnectedPlayer(service, channel, getName(), ProxyLoginHandler.this);
                userCon.setCompressionThreshold(ProxyServiceImpl.COMPRESSION_THRESHOLD);

                if (getVersion() < ProtocolVersion.MINECRAFT_1_20_2.getProtocolId()) {
                    sendPacket(new ServerGameProfilePacket(getUniqueId(), getName(),
                            this.profile == null ? new PlayerProfile.Property[0] : profile.getProperties().toArray(new PlayerProfile.Property[0])));
                    channel.setProtocol(Protocol.GAME);
                }

                this.connectToServer();
            }
        });

    }

    private void connectToServer() {
        if (!userCon.init()) {
            disconnect("already connected to this proxy instance");
            return;
        }
        channel.getHandle().pipeline().get(PacketReader.class)
                .setHandler(new UpstreamBridge(service, userCon));

        AbstractTarget initialServer = service.getDefaultTarget();

        ProxyLoginEvent loginEvent = new ProxyLoginEvent(userCon, initialServer);

        // fire post-login event
        service.getEventManager().call(loginEvent);
        // #3612: Don't progress further if disconnected during event
        if (channel.isClosing()) {
            return;
        }
        userCon.connect(loginEvent.getTarget(), null, ConnectionReason.JOIN_PROXY);
    }

    public void sendPacket(AbstractPacket packet) {
        channel.write(packet);
    }

    @Override
    public void handle(LoginPayloadResponsePacket response) throws Exception {
        disconnect("Unexpected custom LoginPayloadResponsePacket");
    }

    @Override
    public void handle(ServerLoginAcknowledgedPacket serverLoginAcknowledgedPacket) throws Exception {
        disconnect("Unexpected LoginAcknowledged");
    }

    @Override
    public void disconnect(String reason) {
        disconnect(TextComponent.fromLegacy(reason));
    }

    @Override
    public void disconnect(BaseComponent reason) {
        if (canSendKickMessage()) {
            channel.delayedClose(new DisconnectPacket(reason));
        } else {
            channel.close();
        }
    }

    @Override
    public void disconnect(final BaseComponent... reason) {
        disconnect(TextComponent.fromArray(reason));
    }



    @Override
    public String getName() {
        return (name != null) ? name : (serverHelloPacket == null) ? null : serverHelloPacket.getData();
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int getVersion() {
        return (playerIntentPacket == null) ? -1 : playerIntentPacket.getProtocolVersion();
    }

    @Override
    public InetSocketAddress getAddress() {
        return (InetSocketAddress) getSocketAddress();
    }

    @Override
    public SocketAddress getSocketAddress() {
        return channel.getRemoteAddress();
    }

    @Override
    public String getUUID() {
        return uniqueId.toString().replace("-", "");
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        String currentName = getName();
        if (currentName != null) {
            sb.append(currentName);
            sb.append(',');
        }
        sb.append(getSocketAddress());
        sb.append("] <-> InitialHandler");
        return sb.toString();
    }

    @Override
    public boolean isConnected() {
        return !channel.isClosed();
    }

    @Override
    public boolean shouldHandle(ProtocolPacket packet) {
        return !channel.isClosing();
    }

    private boolean canSendKickMessage() {
        return thisState == State.USERNAME || thisState == State.ENCRYPT || thisState == State.FINISHING;
    }

    @Override
    public void connected(PlayerChannel channel) {
        this.channel = channel;
    }

    @Override
    public void exception(Throwable t) throws Exception {
        System.err.printf("EX exception in login: %s%n", t.getMessage());
    }

    @Override
    public void handle(ProtocolPacket packet) throws Exception {
        if (packet.packet == null) {
            throw new RuntimeException("Unexpected packet received during login process!");
        }
    }

    @Override
    public void handle(LegacyHandshakePacket legacyHandshakePacket) throws Exception {
        statusHandler.handle(legacyHandshakePacket);
    }

    @Override
    public void handle(LegacyPingPacket ping) throws Exception {
        statusHandler.handle(ping);
    }

    @Override
    public void handle(StatusRequestPacket statusRequestPacket) throws Exception {
        statusHandler.handle(statusRequestPacket);
    }

    public ProxyServiceAddress getListener() {
        return this.listener;
    }

    /**
     * Gets registered channels.
     *
     * @return the registered channels
     */
    public Set<String> getRegisteredChannels() {
        return this.registeredChannels;
    }

    /**
     * Gets handshake.
     *
     * @return the handshake
     */
    public PlayerIntentPacket getHandshake() {
        return this.playerIntentPacket;
    }

    /**
     * Gets login request.
     *
     * @return the login request
     */
    public ServerHelloPacket getLoginRequest() {
        return this.serverHelloPacket;
    }

    public InetSocketAddress getVirtualHost() {
        return this.virtualHost;
    }

    /**
     * Sets virtual host.
     *
     * @param virtualHost the virtual host
     */
    public void setVirtualHost(InetSocketAddress virtualHost) {
        this.virtualHost = virtualHost;
    }

    public UUID getUniqueId() {
        return this.uniqueId;
    }

    /**
     * Sets unique id.
     *
     * @param uniqueId the unique id
     */
    public void setUniqueId(UUID uniqueId) {
        this.uniqueId = uniqueId;
    }

    public boolean isLegacy() {
        return this.legacy;
    }

    /**
     * Sets legacy.
     *
     * @param legacy the legacy
     */
    public void setLegacy(boolean legacy) {
        this.legacy = legacy;
    }

    /**
     * Gets extra data in handshake.
     *
     * @return the extra data in handshake
     */
    public String getExtraDataInHandshake() {
        return this.extraDataInHandshake;
    }

    /**
     * Sets extra data in handshake.
     *
     * @param extraDataInHandshake the extra data in handshake
     */
    public void setExtraDataInHandshake(String extraDataInHandshake) {
        this.extraDataInHandshake = extraDataInHandshake;
    }

    public boolean isTransferred() {
        return this.transferred;
    }

    /**
     * Sets transferred.
     *
     * @param transferred the transferred
     */
    public void setTransferred(boolean transferred) {
        this.transferred = transferred;
    }

    /**
     * Gets channel.
     *
     * @return the channel
     */
    public PlayerChannel getChannel() {
        return channel;
    }

    /**
     * Sets channel.
     *
     * @param channel the channel
     */
    public void setChannel(PlayerChannel channel) {
        this.channel = channel;
    }

    /**
     * Gets service.
     *
     * @return the service
     */
    public ProxyServiceImpl getService() {
        return service;
    }

    /**
     * Gets player intent packet.
     *
     * @return the player intent packet
     */
    public PlayerIntentPacket getPlayerIntentPacket() {
        return playerIntentPacket;
    }

    /**
     * Sets player intent packet.
     *
     * @param playerIntentPacket the player intent packet
     */
    public void setPlayerIntentPacket(PlayerIntentPacket playerIntentPacket) {
        this.playerIntentPacket = playerIntentPacket;
    }

    /**
     * Gets server hello packet.
     *
     * @return the server hello packet
     */
    public ServerHelloPacket getServerHelloPacket() {
        return serverHelloPacket;
    }

    /**
     * Sets server hello packet.
     *
     * @param serverHelloPacket the server hello packet
     */
    public void setServerHelloPacket(ServerHelloPacket serverHelloPacket) {
        this.serverHelloPacket = serverHelloPacket;
    }

    /**
     * Gets request.
     *
     * @return the request
     */
    public EncryptionRequestPacket getRequest() {
        return request;
    }

    /**
     * Sets request.
     *
     * @param request the request
     */
    public void setRequest(EncryptionRequestPacket request) {
        this.request = request;
    }

    /**
     * Gets this state.
     *
     * @return the this state
     */
    public State getThisState() {
        return thisState;
    }

    /**
     * Sets this state.
     *
     * @param thisState the this state
     */
    public void setThisState(State thisState) {
        this.thisState = thisState;
    }

    /**
     * Gets user con.
     *
     * @return the user con
     */
    public ConnectedPlayer getUserCon() {
        return userCon;
    }

    /**
     * Sets user con.
     *
     * @param userCon the user con
     */
    public void setUserCon(ConnectedPlayer userCon) {
        this.userCon = userCon;
    }

    /**
     * Gets profile.
     *
     * @return the profile
     */
    public PlayerProfile getProfile() {
        return profile;
    }

    /**
     * Gets key pair.
     *
     * @return the key pair
     */
    public KeyPair getKeyPair() {
        return keyPair;
    }

    /**
     * Get pass byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] getPass() {
        return pass;
    }

    /**
     * The enum State.
     */
    public enum State {
        /**
         * Handshake state.
         */
        HANDSHAKE,
        /**
         * Status state.
         */
        STATUS,
        /**
         * Ping state.
         */
        PING,
        /**
         * Username state.
         */
        USERNAME,
        /**
         * Encrypt state.
         */
        ENCRYPT,
        /**
         * Finishing state.
         */
        FINISHING
    }

    public void setProfile(PlayerProfile profile) {
        this.profile = profile;
    }

}
