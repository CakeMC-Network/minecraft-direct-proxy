package net.cakemc.de.crycodes.proxy.network.packet;

import net.cakemc.de.crycodes.proxy.network.packet.impl.*;
import net.cakemc.de.crycodes.proxy.network.packet.impl.intent.PlayerIntentPacket;
import net.cakemc.de.crycodes.proxy.network.packet.impl.login.*;
import net.cakemc.de.crycodes.proxy.network.packet.impl.status.StatusRequestPacket;
import net.cakemc.de.crycodes.proxy.network.packet.impl.status.StatusResponsePacket;

/**
 * The type Abstract packet handler.
 */
public abstract class AbstractPacketHandler {

    /**
     * Handle.
     *
     * @param ping the ping
     * @throws Exception the exception
     */
    public void handle(LegacyPingPacket ping) throws Exception {
    }

    /**
     * Handle.
     *
     * @param ping the ping
     * @throws Exception the exception
     */
    public void handle(KeepAliveResponsePacket ping) throws Exception {
    }

    /**
     * Handle.
     *
     * @param statusRequestPacket the status request packet
     * @throws Exception the exception
     */
    public void handle(StatusRequestPacket statusRequestPacket) throws Exception {
    }

    /**
     * Handle.
     *
     * @param statusResponsePacket the status response packet
     * @throws Exception the exception
     */
    public void handle(StatusResponsePacket statusResponsePacket) throws Exception {
    }

    /**
     * Handle.
     *
     * @param playerIntentPacket the player intent packet
     * @throws Exception the exception
     */
    public void handle(PlayerIntentPacket playerIntentPacket) throws Exception {
    }

    /**
     * Handle.
     *
     * @param keepAliveRequestPacket the keep alive request packet
     * @throws Exception the exception
     */
    public void handle(KeepAliveRequestPacket keepAliveRequestPacket) throws Exception {
    }

    /**
     * Handle.
     *
     * @param playerCreationPacket the player creation packet
     * @throws Exception the exception
     */
    public void handle(PlayerCreationPacket playerCreationPacket) throws Exception {
    }

    /**
     * Handle.
     *
     * @param chat the chat
     * @throws Exception the exception
     */
    public void handle(SystemChatPacket chat) throws Exception {
    }

    /**
     * Handle.
     *
     * @param respawnPacket the respawn packet
     * @throws Exception the exception
     */
    public void handle(RespawnPacket respawnPacket) throws Exception {
    }

    /**
     * Handle.
     *
     * @param serverHelloPacket the server hello packet
     * @throws Exception the exception
     */
    public void handle(ServerHelloPacket serverHelloPacket) throws Exception {
    }

    /**
     * Handle.
     *
     * @param settings the settings
     * @throws Exception the exception
     */
    public void handle(ClientSettingsPacket settings) throws Exception {
    }

    /**
     * Handle.
     *
     * @param encryptionRequestPacket the encryption request packet
     * @throws Exception the exception
     */
    public void handle(EncryptionRequestPacket encryptionRequestPacket) throws Exception {
    }

    /**
     * Handle.
     *
     * @param disconnectPacket the disconnect packet
     * @throws Exception the exception
     */
    public void handle(DisconnectPacket disconnectPacket) throws Exception {
    }

    /**
     * Handle.
     *
     * @param encryptionResponsePacket the encryption response packet
     * @throws Exception the exception
     */
    public void handle(EncryptionResponsePacket encryptionResponsePacket) throws Exception {
    }

    /**
     * Handle.
     *
     * @param serverGameProfilePacket the server game profile packet
     * @throws Exception the exception
     */
    public void handle(ServerGameProfilePacket serverGameProfilePacket) throws Exception {
    }

    /**
     * Handle.
     *
     * @param legacyHandshakePacket the legacy handshake packet
     * @throws Exception the exception
     */
    public void handle(LegacyHandshakePacket legacyHandshakePacket) throws Exception {
    }

    /**
     * Handle.
     *
     * @param serverSetCompressionPacket the server set compression packet
     * @throws Exception the exception
     */
    public void handle(ServerSetCompressionPacket serverSetCompressionPacket) throws Exception {
    }

    /**
     * Handle.
     *
     * @param request the request
     * @throws Exception the exception
     */
    public void handle(LoginPayloadRequestPacket request) throws Exception {
    }

    /**
     * Handle.
     *
     * @param response the response
     * @throws Exception the exception
     */
    public void handle(LoginPayloadResponsePacket response) throws Exception {
    }

    /**
     * Handle.
     *
     * @param status the status
     * @throws Exception the exception
     */
    public void handle(EntityStatusPacket status) throws Exception {
    }

    /**
     * Handle.
     *
     * @param setSimulationDistancePacket the set simulation distance packet
     * @throws Exception the exception
     */
    public void handle(SetSimulationDistancePacket setSimulationDistancePacket) throws Exception {
    }

    /**
     * Handle.
     *
     * @param gameStatePacket the game state packet
     * @throws Exception the exception
     */
    public void handle(GameStatePacket gameStatePacket) throws Exception {
    }

    /**
     * Handle.
     *
     * @param serverDataPacket the server data packet
     * @throws Exception the exception
     */
    public void handle(ServerDataPacket serverDataPacket) throws Exception {
    }

    /**
     * Handle.
     *
     * @param serverLoginAcknowledgedPacket the server login acknowledged packet
     * @throws Exception the exception
     */
    public void handle(ServerLoginAcknowledgedPacket serverLoginAcknowledgedPacket) throws Exception {
    }

    /**
     * Handle.
     *
     * @param startConfigurationPacket the start configuration packet
     * @throws Exception the exception
     */
    public void handle(StartConfigurationPacket startConfigurationPacket) throws Exception {
    }

    /**
     * Handle.
     *
     * @param finishConfigurationPacket the finish configuration packet
     * @throws Exception the exception
     */
    public void handle(FinishConfigurationPacket finishConfigurationPacket) throws Exception {
    }

    /**
     * Handle.
     *
     * @param serverTransferPacket the server transfer packet
     * @throws Exception the exception
     */
    public void handle(ServerTransferPacket serverTransferPacket) throws Exception {
    }

    /**
     * Handle.
     *
     * @param disconnectReportDetailsPacket the disconnect report details packet
     * @throws Exception the exception
     */
    public void handle(DisconnectReportDetailsPacket disconnectReportDetailsPacket) throws Exception {
    }
}
