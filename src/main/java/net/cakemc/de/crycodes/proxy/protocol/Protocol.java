package net.cakemc.de.crycodes.proxy.protocol;


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
            TO_SERVER.registerPacket(
                    PlayerIntentPacket.class, PlayerIntentPacket::new,
                    map(ProtocolVersion.MINECRAFT_1_8, 0)
            );
        }
    },

    /**
     * Game protocol.
     */
// 0
    GAME {
        {
            TO_CLIENT.registerPacket(
                    PlayerCreationPacket.class, PlayerCreationPacket::new,
                    map(ProtocolVersion.MINECRAFT_1_8, 0x01),
                    map(ProtocolVersion.MINECRAFT_1_9, 0x23),
                    map(ProtocolVersion.MINECRAFT_1_13, 0x25),
                    map(ProtocolVersion.MINECRAFT_1_15, 0x26),
                    map(ProtocolVersion.MINECRAFT_1_16, 0x25),
                    map(ProtocolVersion.MINECRAFT_1_16_2, 0x24),
                    map(ProtocolVersion.MINECRAFT_1_17, 0x26),
                    map(ProtocolVersion.MINECRAFT_1_19, 0x23),
                    map(ProtocolVersion.MINECRAFT_1_19_1, 0x25),
                    map(ProtocolVersion.MINECRAFT_1_19_3, 0x24),
                    map(ProtocolVersion.MINECRAFT_1_19_4, 0x28),
                    map(ProtocolVersion.MINECRAFT_1_20_2, 0x29),
                    map(ProtocolVersion.MINECRAFT_1_20_5, 0x2B),
                    map(ProtocolVersion.MINECRAFT_1_21_2, 0x2C)
            );

            TO_CLIENT.registerPacket(
                    RespawnPacket.class, RespawnPacket::new,
                    map(ProtocolVersion.MINECRAFT_1_8, 0x07),
                    map(ProtocolVersion.MINECRAFT_1_9, 0x33),
                    map(ProtocolVersion.MINECRAFT_1_12, 0x34),
                    map(ProtocolVersion.MINECRAFT_1_12_1, 0x35),
                    map(ProtocolVersion.MINECRAFT_1_13, 0x38),
                    map(ProtocolVersion.MINECRAFT_1_14, 0x3A),
                    map(ProtocolVersion.MINECRAFT_1_15, 0x3B),
                    map(ProtocolVersion.MINECRAFT_1_16, 0x3A),
                    map(ProtocolVersion.MINECRAFT_1_16_2, 0x39),
                    map(ProtocolVersion.MINECRAFT_1_17, 0x3D),
                    map(ProtocolVersion.MINECRAFT_1_19, 0x3B),
                    map(ProtocolVersion.MINECRAFT_1_19_1, 0x3E),
                    map(ProtocolVersion.MINECRAFT_1_19_3, 0x3D),
                    map(ProtocolVersion.MINECRAFT_1_19_4, 0x41),
                    map(ProtocolVersion.MINECRAFT_1_20_2, 0x43),
                    map(ProtocolVersion.MINECRAFT_1_20_3, 0x45),
                    map(ProtocolVersion.MINECRAFT_1_20_5, 0x47),
                    map(ProtocolVersion.MINECRAFT_1_21_2, 0x4C)
            );
            TO_CLIENT.registerPacket(
                    DisconnectPacket.class, DisconnectPacket::new,
                    map(ProtocolVersion.MINECRAFT_1_8, 0x40),
                    map(ProtocolVersion.MINECRAFT_1_9, 0x1A),
                    map(ProtocolVersion.MINECRAFT_1_13, 0x1B),
                    map(ProtocolVersion.MINECRAFT_1_14, 0x1A),
                    map(ProtocolVersion.MINECRAFT_1_15, 0x1B),
                    map(ProtocolVersion.MINECRAFT_1_16, 0x1A),
                    map(ProtocolVersion.MINECRAFT_1_16_2, 0x19),
                    map(ProtocolVersion.MINECRAFT_1_17, 0x1A),
                    map(ProtocolVersion.MINECRAFT_1_19, 0x17),
                    map(ProtocolVersion.MINECRAFT_1_19_1, 0x19),
                    map(ProtocolVersion.MINECRAFT_1_19_3, 0x17),
                    map(ProtocolVersion.MINECRAFT_1_19_4, 0x1A),
                    map(ProtocolVersion.MINECRAFT_1_20_2, 0x1B),
                    map(ProtocolVersion.MINECRAFT_1_20_5, 0x1D)
            );
            TO_CLIENT.registerPacket(
                    EntityStatusPacket.class, EntityStatusPacket::new,
                    map(ProtocolVersion.MINECRAFT_1_8, 0x1A),
                    map(ProtocolVersion.MINECRAFT_1_9, 0x1B),
                    map(ProtocolVersion.MINECRAFT_1_13, 0x1C),
                    map(ProtocolVersion.MINECRAFT_1_14, 0x1B),
                    map(ProtocolVersion.MINECRAFT_1_15, 0x1C),
                    map(ProtocolVersion.MINECRAFT_1_16, 0x1B),
                    map(ProtocolVersion.MINECRAFT_1_16_2, 0x1A),
                    map(ProtocolVersion.MINECRAFT_1_17, 0x1B),
                    map(ProtocolVersion.MINECRAFT_1_19, 0x18),
                    map(ProtocolVersion.MINECRAFT_1_19_1, 0x1A),
                    map(ProtocolVersion.MINECRAFT_1_19_3, 0x19),
                    map(ProtocolVersion.MINECRAFT_1_19_4, 0x1C),
                    map(ProtocolVersion.MINECRAFT_1_20_2, 0x1D),
                    map(ProtocolVersion.MINECRAFT_1_20_5, 0x1F)
            );
            TO_CLIENT.registerPacket(
                    GameStatePacket.class, GameStatePacket::new,
                    map(ProtocolVersion.MINECRAFT_1_15, 0x1F),
                    map(ProtocolVersion.MINECRAFT_1_16, 0x1E),
                    map(ProtocolVersion.MINECRAFT_1_16_2, 0x1D),
                    map(ProtocolVersion.MINECRAFT_1_17, 0x1E),
                    map(ProtocolVersion.MINECRAFT_1_19, 0x1B),
                    map(ProtocolVersion.MINECRAFT_1_19_1, 0x1D),
                    map(ProtocolVersion.MINECRAFT_1_19_3, 0x1C),
                    map(ProtocolVersion.MINECRAFT_1_19_4, 0x1F),
                    map(ProtocolVersion.MINECRAFT_1_20_2, 0x20),
                    map(ProtocolVersion.MINECRAFT_1_20_5, 0x22),
                    map(ProtocolVersion.MINECRAFT_1_21_2, 0x23)
            );
            TO_CLIENT.registerPacket(
                    SetSimulationDistancePacket.class, SetSimulationDistancePacket::new,
                    map(ProtocolVersion.MINECRAFT_1_14, 0x41),
                    map(ProtocolVersion.MINECRAFT_1_15, 0x42),
                    map(ProtocolVersion.MINECRAFT_1_16, 0x41),
                    map(ProtocolVersion.MINECRAFT_1_17, 0x4A),
                    map(ProtocolVersion.MINECRAFT_1_19, 0x49),
                    map(ProtocolVersion.MINECRAFT_1_19_1, 0x4C),
                    map(ProtocolVersion.MINECRAFT_1_19_3, 0x4B),
                    map(ProtocolVersion.MINECRAFT_1_19_4, 0x4F),
                    map(ProtocolVersion.MINECRAFT_1_20_2, 0x51),
                    map(ProtocolVersion.MINECRAFT_1_20_3, 0x53),
                    map(ProtocolVersion.MINECRAFT_1_20_5, 0x55),
                    map(ProtocolVersion.MINECRAFT_1_21_2, 0x59)
            );
            TO_CLIENT.registerPacket(
                    ServerDataPacket.class, ServerDataPacket::new,
                    map(ProtocolVersion.MINECRAFT_1_19, 0x3F),
                    map(ProtocolVersion.MINECRAFT_1_19_1, 0x42),
                    map(ProtocolVersion.MINECRAFT_1_19_3, 0x41),
                    map(ProtocolVersion.MINECRAFT_1_19_4, 0x45),
                    map(ProtocolVersion.MINECRAFT_1_20_2, 0x47),
                    map(ProtocolVersion.MINECRAFT_1_20_3, 0x49),
                    map(ProtocolVersion.MINECRAFT_1_20_5, 0x4B),
                    map(ProtocolVersion.MINECRAFT_1_21_2, 0x50)
            );
            TO_CLIENT.registerPacket(
                    StartConfigurationPacket.class, StartConfigurationPacket::new,
                    map(ProtocolVersion.MINECRAFT_1_20_2, 0x65),
                    map(ProtocolVersion.MINECRAFT_1_20_3, 0x67),
                    map(ProtocolVersion.MINECRAFT_1_20_5, 0x69),
                    map(ProtocolVersion.MINECRAFT_1_21_2, 0x70)
            );
            TO_CLIENT.registerPacket(
                    ServerTransferPacket.class, ServerTransferPacket::new,
                    map(ProtocolVersion.MINECRAFT_1_20_5, 0x73),
                    map(ProtocolVersion.MINECRAFT_1_21_2, 0x7A)
            );
            TO_CLIENT.registerPacket(
                    DisconnectReportDetailsPacket.class, DisconnectReportDetailsPacket::new,
                    map(ProtocolVersion.MINECRAFT_1_21, 0x7A),
                    map(ProtocolVersion.MINECRAFT_1_21_2, 0x81)
            );
            TO_SERVER.registerPacket(
                    ClientSettingsPacket.class, ClientSettingsPacket::new,
                    map(ProtocolVersion.MINECRAFT_1_8, 0x15),
                    map(ProtocolVersion.MINECRAFT_1_9, 0x04),
                    map(ProtocolVersion.MINECRAFT_1_12, 0x05),
                    map(ProtocolVersion.MINECRAFT_1_12_1, 0x04),
                    map(ProtocolVersion.MINECRAFT_1_14, 0x05),
                    map(ProtocolVersion.MINECRAFT_1_19, 0x07),
                    map(ProtocolVersion.MINECRAFT_1_19_1, 0x08),
                    map(ProtocolVersion.MINECRAFT_1_19_3, 0x07),
                    map(ProtocolVersion.MINECRAFT_1_19_4, 0x08),
                    map(ProtocolVersion.MINECRAFT_1_20_2, 0x09),
                    map(ProtocolVersion.MINECRAFT_1_20_5, 0x0A),
                    map(ProtocolVersion.MINECRAFT_1_21_2, 0x0C)
            );
            TO_SERVER.registerPacket(
                    StartConfigurationPacket.class, StartConfigurationPacket::new,
                    map(ProtocolVersion.MINECRAFT_1_20_2, 0x0B),
                    map(ProtocolVersion.MINECRAFT_1_20_5, 0x0C),
                    map(ProtocolVersion.MINECRAFT_1_21_2, 0x0E)
            );
        }
    },

    /**
     * Status protocol.
     */
// 1
    STATUS {
        {
            TO_CLIENT.registerPacket(
                    StatusResponsePacket.class, StatusResponsePacket::new,
                    map(ProtocolVersion.MINECRAFT_1_8, 0)
            );
            TO_CLIENT.registerPacket(
                    KeepAliveResponsePacket.class, KeepAliveResponsePacket::new,
                    map(ProtocolVersion.MINECRAFT_1_8, 1)
            );
            TO_SERVER.registerPacket(
                    StatusRequestPacket.class, StatusRequestPacket::new,
                    map(ProtocolVersion.MINECRAFT_1_8, 0)
            );
            TO_SERVER.registerPacket(
                    KeepAliveResponsePacket.class, KeepAliveResponsePacket::new,
                    map(ProtocolVersion.MINECRAFT_1_8, 1)
            );
        }
    },

    /**
     * Login protocol.
     */
//2
    LOGIN {
        {
            TO_CLIENT.registerPacket(
                    DisconnectPacket.class, DisconnectPacket::new,
                    map(ProtocolVersion.MINECRAFT_1_8, 0)
            );
            TO_CLIENT.registerPacket(
                    EncryptionRequestPacket.class, EncryptionRequestPacket::new,
                    map(ProtocolVersion.MINECRAFT_1_8, 1)
            );
            TO_CLIENT.registerPacket(
                    ServerGameProfilePacket.class, ServerGameProfilePacket::new,
                    map(ProtocolVersion.MINECRAFT_1_8, 2)
            );
            TO_CLIENT.registerPacket(
                    ServerSetCompressionPacket.class, ServerSetCompressionPacket::new,
                    map(ProtocolVersion.MINECRAFT_1_8, 3)
            );
            TO_CLIENT.registerPacket(
                    LoginPayloadRequestPacket.class, LoginPayloadRequestPacket::new,
                    map(ProtocolVersion.MINECRAFT_1_13, 4)
            );
            TO_SERVER.registerPacket(
                    ServerHelloPacket.class, ServerHelloPacket::new,
                    map(ProtocolVersion.MINECRAFT_1_8, 0)
            );
            TO_SERVER.registerPacket(
                    EncryptionResponsePacket.class, EncryptionResponsePacket::new,
                    map(ProtocolVersion.MINECRAFT_1_8, 1)
            );
            TO_SERVER.registerPacket(
                    LoginPayloadResponsePacket.class, LoginPayloadResponsePacket::new,
                    map(ProtocolVersion.MINECRAFT_1_13, 2)
            );
            TO_SERVER.registerPacket(
                    ServerLoginAcknowledgedPacket.class, ServerLoginAcknowledgedPacket::new,
                    map(ProtocolVersion.MINECRAFT_1_20_2, 3)
            );
        }
    },

    /**
     * Configuration protocol.
     */
// 3
    CONFIGURATION {
        {
            TO_CLIENT.registerPacket(
                    DisconnectPacket.class, DisconnectPacket::new,
                    map(ProtocolVersion.MINECRAFT_1_20_2, 1),
                    map(ProtocolVersion.MINECRAFT_1_20_5, 2)
            );
            TO_CLIENT.registerPacket(
                    FinishConfigurationPacket.class, FinishConfigurationPacket::new,
                    map(ProtocolVersion.MINECRAFT_1_20_2, 2),
                    map(ProtocolVersion.MINECRAFT_1_20_5, 3)
            );
            TO_CLIENT.registerPacket(
                    ServerTransferPacket.class, ServerTransferPacket::new,
                    map(ProtocolVersion.MINECRAFT_1_20_5, 11)
            );
            TO_CLIENT.registerPacket(
                    DisconnectReportDetailsPacket.class, DisconnectReportDetailsPacket::new,
                    map(ProtocolVersion.MINECRAFT_1_21, 15)
            );
            TO_SERVER.registerPacket(
                    ClientSettingsPacket.class, ClientSettingsPacket::new,
                    map(ProtocolVersion.MINECRAFT_1_20_2, 0)
            );
            TO_SERVER.registerPacket(
                    FinishConfigurationPacket.class, FinishConfigurationPacket::new,
                    map(ProtocolVersion.MINECRAFT_1_20_2, 2),
                    map(ProtocolVersion.MINECRAFT_1_20_5, 3)
            );
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
