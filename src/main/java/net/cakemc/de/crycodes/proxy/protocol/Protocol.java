package net.cakemc.de.crycodes.proxy.protocol;


import net.cakemc.de.crycodes.proxy.network.packet.AbstractPacket;
import net.cakemc.de.crycodes.proxy.network.packet.impl.*;
import net.cakemc.de.crycodes.proxy.network.packet.impl.intent.PlayerIntentPacket;
import net.cakemc.de.crycodes.proxy.network.packet.impl.login.*;
import net.cakemc.de.crycodes.proxy.network.packet.impl.status.StatusRequestPacket;
import net.cakemc.de.crycodes.proxy.network.packet.impl.status.StatusResponsePacket;

/**
 * The enum Protocol.
 */
public enum Protocol {
    /**
     * Handshake protocol.
     */
// Undef
    HANDSHAKE {
        {
            TO_SERVER.registerPacket(PlayerIntentPacket.class, PlayerIntentPacket::new, map(ProtocolVersion.MINECRAFT_1_8, 0));
        }
    },

    /**
     * Game protocol.
     */
// 0
    GAME {
        {
            TO_CLIENT.registerPacket(PlayerCreationPacket.class, PlayerCreationPacket::new, map(ProtocolVersion.MINECRAFT_1_8, 1), map(ProtocolVersion.MINECRAFT_1_9, 35), map(ProtocolVersion.MINECRAFT_1_13, 37), map(ProtocolVersion.MINECRAFT_1_15, 38), map(ProtocolVersion.MINECRAFT_1_16, 37), map(ProtocolVersion.MINECRAFT_1_16_2, 36), map(ProtocolVersion.MINECRAFT_1_17, 38), map(ProtocolVersion.MINECRAFT_1_19, 35), map(ProtocolVersion.MINECRAFT_1_19_1, 37), map(ProtocolVersion.MINECRAFT_1_19_3, 36), map(ProtocolVersion.MINECRAFT_1_19_4, 40), map(ProtocolVersion.MINECRAFT_1_20_2, 41), map(ProtocolVersion.MINECRAFT_1_20_5, 43));
            TO_CLIENT.registerPacket(RespawnPacket.class, RespawnPacket::new, map(ProtocolVersion.MINECRAFT_1_8, 7), map(ProtocolVersion.MINECRAFT_1_9, 51), map(ProtocolVersion.MINECRAFT_1_12, 52), map(ProtocolVersion.MINECRAFT_1_12_1, 53), map(ProtocolVersion.MINECRAFT_1_13, 56), map(ProtocolVersion.MINECRAFT_1_14, 58), map(ProtocolVersion.MINECRAFT_1_15, 59), map(ProtocolVersion.MINECRAFT_1_16, 58), map(ProtocolVersion.MINECRAFT_1_16_2, 57), map(ProtocolVersion.MINECRAFT_1_17, 61), map(ProtocolVersion.MINECRAFT_1_19, 59), map(ProtocolVersion.MINECRAFT_1_19_1, 62), map(ProtocolVersion.MINECRAFT_1_19_3, 61), map(ProtocolVersion.MINECRAFT_1_19_4, 65), map(ProtocolVersion.MINECRAFT_1_20_2, 67), map(ProtocolVersion.MINECRAFT_1_20_3, 69), map(ProtocolVersion.MINECRAFT_1_20_5, 71), map(ProtocolVersion.MINECRAFT_1_21_2, 72));
            TO_CLIENT.registerPacket(DisconnectPacket.class, DisconnectPacket::new, map(ProtocolVersion.MINECRAFT_1_8, 64), map(ProtocolVersion.MINECRAFT_1_9, 26), map(ProtocolVersion.MINECRAFT_1_13, 27), map(ProtocolVersion.MINECRAFT_1_14, 26), map(ProtocolVersion.MINECRAFT_1_15, 27), map(ProtocolVersion.MINECRAFT_1_16, 26), map(ProtocolVersion.MINECRAFT_1_16_2, 25), map(ProtocolVersion.MINECRAFT_1_17, 26), map(ProtocolVersion.MINECRAFT_1_19, 23), map(ProtocolVersion.MINECRAFT_1_19_1, 25), map(ProtocolVersion.MINECRAFT_1_19_3, 23), map(ProtocolVersion.MINECRAFT_1_19_4, 26), map(ProtocolVersion.MINECRAFT_1_20_2, 27), map(ProtocolVersion.MINECRAFT_1_20_5, 29));
            TO_CLIENT.registerPacket(SystemChatPacket.class, SystemChatPacket::new, map(ProtocolVersion.MINECRAFT_1_19, 95), map(ProtocolVersion.MINECRAFT_1_19_1, 98), map(ProtocolVersion.MINECRAFT_1_19_3, 96), map(ProtocolVersion.MINECRAFT_1_19_4, 100), map(ProtocolVersion.MINECRAFT_1_20_2, 103), map(ProtocolVersion.MINECRAFT_1_20_3, 105), map(ProtocolVersion.MINECRAFT_1_20_5, 108), map(ProtocolVersion.MINECRAFT_1_21_2, 111));
            TO_CLIENT.registerPacket(EntityStatusPacket.class, EntityStatusPacket::new, map(ProtocolVersion.MINECRAFT_1_8, 26), map(ProtocolVersion.MINECRAFT_1_9, 27), map(ProtocolVersion.MINECRAFT_1_13, 28), map(ProtocolVersion.MINECRAFT_1_14, 27), map(ProtocolVersion.MINECRAFT_1_15, 28), map(ProtocolVersion.MINECRAFT_1_16, 27), map(ProtocolVersion.MINECRAFT_1_16_2, 26), map(ProtocolVersion.MINECRAFT_1_17, 27), map(ProtocolVersion.MINECRAFT_1_19, 24), map(ProtocolVersion.MINECRAFT_1_19_1, 26), map(ProtocolVersion.MINECRAFT_1_19_3, 25), map(ProtocolVersion.MINECRAFT_1_19_4, 28), map(ProtocolVersion.MINECRAFT_1_20_2, 29), map(ProtocolVersion.MINECRAFT_1_20_5, 31));
            TO_CLIENT.registerPacket(GameStatePacket.class, GameStatePacket::new, map(ProtocolVersion.MINECRAFT_1_15, 31), map(ProtocolVersion.MINECRAFT_1_16, 30), map(ProtocolVersion.MINECRAFT_1_16_2, 29), map(ProtocolVersion.MINECRAFT_1_17, 30), map(ProtocolVersion.MINECRAFT_1_19, 27), map(ProtocolVersion.MINECRAFT_1_19_1, 29), map(ProtocolVersion.MINECRAFT_1_19_3, 28), map(ProtocolVersion.MINECRAFT_1_19_4, 31), map(ProtocolVersion.MINECRAFT_1_20_2, 32), map(ProtocolVersion.MINECRAFT_1_20_5, 34));
            TO_CLIENT.registerPacket(SetSimulationDistancePacket.class, SetSimulationDistancePacket::new, map(ProtocolVersion.MINECRAFT_1_14, 65), map(ProtocolVersion.MINECRAFT_1_15, 66), map(ProtocolVersion.MINECRAFT_1_16, 65), map(ProtocolVersion.MINECRAFT_1_17, 74), map(ProtocolVersion.MINECRAFT_1_19, 73), map(ProtocolVersion.MINECRAFT_1_19_1, 76), map(ProtocolVersion.MINECRAFT_1_19_3, 75), map(ProtocolVersion.MINECRAFT_1_19_4, 79), map(ProtocolVersion.MINECRAFT_1_20_2, 81), map(ProtocolVersion.MINECRAFT_1_20_3, 83), map(ProtocolVersion.MINECRAFT_1_20_5, 85));
            TO_CLIENT.registerPacket(ServerDataPacket.class, ServerDataPacket::new, map(ProtocolVersion.MINECRAFT_1_19, 63), map(ProtocolVersion.MINECRAFT_1_19_1, 66), map(ProtocolVersion.MINECRAFT_1_19_3, 65), map(ProtocolVersion.MINECRAFT_1_19_4, 69), map(ProtocolVersion.MINECRAFT_1_20_2, 71), map(ProtocolVersion.MINECRAFT_1_20_3, 73), map(ProtocolVersion.MINECRAFT_1_20_5, 75), map(ProtocolVersion.MINECRAFT_1_21_2, 76));
            TO_CLIENT.registerPacket(StartConfigurationPacket.class, StartConfigurationPacket::new, map(ProtocolVersion.MINECRAFT_1_20_2, 101), map(ProtocolVersion.MINECRAFT_1_20_3, 103), map(ProtocolVersion.MINECRAFT_1_20_5, 105), map(ProtocolVersion.MINECRAFT_1_21_2, 108));
            TO_CLIENT.registerPacket(ServerTransferPacket.class, ServerTransferPacket::new, map(ProtocolVersion.MINECRAFT_1_20_5, 115), map(ProtocolVersion.MINECRAFT_1_21_2, 118));
            TO_CLIENT.registerPacket(DisconnectReportDetailsPacket.class, DisconnectReportDetailsPacket::new, map(ProtocolVersion.MINECRAFT_1_21, 122), map(ProtocolVersion.MINECRAFT_1_21_2, 125));
            TO_SERVER.registerPacket(ClientSettingsPacket.class, ClientSettingsPacket::new, map(ProtocolVersion.MINECRAFT_1_8, 21), map(ProtocolVersion.MINECRAFT_1_9, 4), map(ProtocolVersion.MINECRAFT_1_12, 5), map(ProtocolVersion.MINECRAFT_1_12_1, 4), map(ProtocolVersion.MINECRAFT_1_14, 5), map(ProtocolVersion.MINECRAFT_1_19, 7), map(ProtocolVersion.MINECRAFT_1_19_1, 8), map(ProtocolVersion.MINECRAFT_1_19_3, 7), map(ProtocolVersion.MINECRAFT_1_19_4, 8), map(ProtocolVersion.MINECRAFT_1_20_2, 9), map(ProtocolVersion.MINECRAFT_1_20_5, 10), map(ProtocolVersion.MINECRAFT_1_21_2, 12));
            TO_SERVER.registerPacket(StartConfigurationPacket.class, StartConfigurationPacket::new, map(ProtocolVersion.MINECRAFT_1_20_2, 11), map(ProtocolVersion.MINECRAFT_1_20_5, 12), map(ProtocolVersion.MINECRAFT_1_21_2, 14));
        }
    },

    /**
     * Status protocol.
     */
// 1
    STATUS {
        {
            TO_CLIENT.registerPacket(StatusResponsePacket.class, StatusResponsePacket::new, map(ProtocolVersion.MINECRAFT_1_8, 0));
            TO_CLIENT.registerPacket(KeepAliveResponsePacket.class, KeepAliveResponsePacket::new, map(ProtocolVersion.MINECRAFT_1_8, 1));
            TO_SERVER.registerPacket(StatusRequestPacket.class, StatusRequestPacket::new, map(ProtocolVersion.MINECRAFT_1_8, 0));
            TO_SERVER.registerPacket(KeepAliveResponsePacket.class, KeepAliveResponsePacket::new, map(ProtocolVersion.MINECRAFT_1_8, 1));
        }
    },

    /**
     * Login protocol.
     */
//2
    LOGIN {
        {
            TO_CLIENT.registerPacket(DisconnectPacket.class, DisconnectPacket::new, map(ProtocolVersion.MINECRAFT_1_8, 0));
            TO_CLIENT.registerPacket(EncryptionRequestPacket.class, EncryptionRequestPacket::new, map(ProtocolVersion.MINECRAFT_1_8, 1));
            TO_CLIENT.registerPacket(ServerGameProfilePacket.class, ServerGameProfilePacket::new, map(ProtocolVersion.MINECRAFT_1_8, 2));
            TO_CLIENT.registerPacket(ServerSetCompressionPacket.class, ServerSetCompressionPacket::new, map(ProtocolVersion.MINECRAFT_1_8, 3));
            TO_CLIENT.registerPacket(LoginPayloadRequestPacket.class, LoginPayloadRequestPacket::new, map(ProtocolVersion.MINECRAFT_1_13, 4));
            TO_SERVER.registerPacket(ServerHelloPacket.class, ServerHelloPacket::new, map(ProtocolVersion.MINECRAFT_1_8, 0));
            TO_SERVER.registerPacket(EncryptionResponsePacket.class, EncryptionResponsePacket::new, map(ProtocolVersion.MINECRAFT_1_8, 1));
            TO_SERVER.registerPacket(LoginPayloadResponsePacket.class, LoginPayloadResponsePacket::new, map(ProtocolVersion.MINECRAFT_1_13, 2));
            TO_SERVER.registerPacket(ServerLoginAcknowledgedPacket.class, ServerLoginAcknowledgedPacket::new, map(ProtocolVersion.MINECRAFT_1_20_2, 3));
        }
    },

    /**
     * Configuration protocol.
     */
// 3
    CONFIGURATION {
        {
            TO_CLIENT.registerPacket(DisconnectPacket.class, DisconnectPacket::new, map(ProtocolVersion.MINECRAFT_1_20_2, 1), map(ProtocolVersion.MINECRAFT_1_20_5, 2));
            TO_CLIENT.registerPacket(FinishConfigurationPacket.class, FinishConfigurationPacket::new, map(ProtocolVersion.MINECRAFT_1_20_2, 2), map(ProtocolVersion.MINECRAFT_1_20_5, 3));
            TO_CLIENT.registerPacket(ServerTransferPacket.class, ServerTransferPacket::new, map(ProtocolVersion.MINECRAFT_1_20_5, 11));
            TO_CLIENT.registerPacket(DisconnectReportDetailsPacket.class, DisconnectReportDetailsPacket::new, map(ProtocolVersion.MINECRAFT_1_21, 15));
            TO_SERVER.registerPacket(ClientSettingsPacket.class, ClientSettingsPacket::new, map(ProtocolVersion.MINECRAFT_1_20_2, 0));
            TO_SERVER.registerPacket(FinishConfigurationPacket.class, FinishConfigurationPacket::new, map(ProtocolVersion.MINECRAFT_1_20_2, 2), map(ProtocolVersion.MINECRAFT_1_20_5, 3));
        }
    };

    /**
     * The constant MAX_PACKET_ID.
     */
    public static final int MAX_PACKET_ID = 255;

    /**
     * The To server.
     */
    public final DirectionData TO_SERVER = new DirectionData(this, ProtocolVersion.Direction.TO_SERVER);
    /**
     * The To client.
     */
    public final DirectionData TO_CLIENT = new DirectionData(this, ProtocolVersion.Direction.TO_CLIENT);

    private static ProtocolMapping map(ProtocolVersion protocol, int id) {
        return new ProtocolMapping(protocol.getProtocolId(), id);
    }

}
