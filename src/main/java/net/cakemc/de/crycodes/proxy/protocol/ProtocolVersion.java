package net.cakemc.de.crycodes.proxy.protocol;


import java.util.ArrayList;
import java.util.List;

/**
 * The enum Protocol version.
 */
public enum ProtocolVersion {

    /**
     * Minecraft 1 8 protocol version.
     */
    MINECRAFT_1_8(47, "1.8.x"),
    /**
     * Minecraft 1 9 protocol version.
     */
    MINECRAFT_1_9(107, "1.9.x"),
    /**
     * Minecraft 1 9 1 protocol version.
     */
    MINECRAFT_1_9_1(108, "1.9.x"),
    /**
     * Minecraft 1 9 2 protocol version.
     */
    MINECRAFT_1_9_2(109, "1.9.x"),
    /**
     * Minecraft 1 9 4 protocol version.
     */
    MINECRAFT_1_9_4(110, "1.9.x"),
    /**
     * Minecraft 1 10 protocol version.
     */
    MINECRAFT_1_10(210, "1.10.x"),
    /**
     * Minecraft 1 11 protocol version.
     */
    MINECRAFT_1_11(315, "1.11.x"),
    /**
     * Minecraft 1 11 1 protocol version.
     */
    MINECRAFT_1_11_1(316, "1.11.x"),
    /**
     * Minecraft 1 12 protocol version.
     */
    MINECRAFT_1_12(335, "1.12.x"),
    /**
     * Minecraft 1 12 1 protocol version.
     */
    MINECRAFT_1_12_1(338, "1.12.x"),
    /**
     * Minecraft 1 12 2 protocol version.
     */
    MINECRAFT_1_12_2(340, "1.12.x"),
    /**
     * Minecraft 1 13 protocol version.
     */
    MINECRAFT_1_13(393, "1.13.x"),
    /**
     * Minecraft 1 13 1 protocol version.
     */
    MINECRAFT_1_13_1(401, "1.13.x"),
    /**
     * Minecraft 1 13 2 protocol version.
     */
    MINECRAFT_1_13_2(404, "1.13.x"),
    /**
     * Minecraft 1 14 protocol version.
     */
    MINECRAFT_1_14(477, "1.14.x"),
    /**
     * Minecraft 1 14 1 protocol version.
     */
    MINECRAFT_1_14_1(480, "1.14.x"),
    /**
     * Minecraft 1 14 2 protocol version.
     */
    MINECRAFT_1_14_2(485, "1.14.x"),
    /**
     * Minecraft 1 14 3 protocol version.
     */
    MINECRAFT_1_14_3(490, "1.14.x"),
    /**
     * Minecraft 1 14 4 protocol version.
     */
    MINECRAFT_1_14_4(498, "1.14.x"),
    /**
     * Minecraft 1 15 protocol version.
     */
    MINECRAFT_1_15(573, "1.15.x"),
    /**
     * Minecraft 1 15 1 protocol version.
     */
    MINECRAFT_1_15_1(575, "1.15.x"),
    /**
     * Minecraft 1 15 2 protocol version.
     */
    MINECRAFT_1_15_2(578, "1.15.x"),
    /**
     * Minecraft 1 16 protocol version.
     */
    MINECRAFT_1_16(735, "1.16.x"),
    /**
     * Minecraft 1 16 1 protocol version.
     */
    MINECRAFT_1_16_1(736, "1.16.x"),
    /**
     * Minecraft 1 16 2 protocol version.
     */
    MINECRAFT_1_16_2(751, "1.16.x"),
    /**
     * Minecraft 1 16 3 protocol version.
     */
    MINECRAFT_1_16_3(753, "1.16.x"),
    /**
     * Minecraft 1 16 4 protocol version.
     */
    MINECRAFT_1_16_4(754, "1.16.x"),
    /**
     * Minecraft 1 17 protocol version.
     */
    MINECRAFT_1_17(755, "1.17.x"),
    /**
     * Minecraft 1 17 1 protocol version.
     */
    MINECRAFT_1_17_1(756, "1.17.x"),
    /**
     * Minecraft 1 18 protocol version.
     */
    MINECRAFT_1_18(757, "1.18.x"),
    /**
     * Minecraft 1 18 2 protocol version.
     */
    MINECRAFT_1_18_2(758, "1.18.x"),
    /**
     * Minecraft 1 19 protocol version.
     */
    MINECRAFT_1_19(759, "1.19.x"),
    /**
     * Minecraft 1 19 1 protocol version.
     */
    MINECRAFT_1_19_1(760, "1.19.x"),
    /**
     * Minecraft 1 19 3 protocol version.
     */
    MINECRAFT_1_19_3(761, "1.19.x"),
    /**
     * Minecraft 1 19 4 protocol version.
     */
    MINECRAFT_1_19_4(762, "1.19.x"),
    /**
     * Minecraft 1 20 protocol version.
     */
    MINECRAFT_1_20(763, "1.20.x"),
    /**
     * Minecraft 1 20 2 protocol version.
     */
    MINECRAFT_1_20_2(764, "1.20.x"),
    /**
     * Minecraft 1 20 3 protocol version.
     */
    MINECRAFT_1_20_3(765, "1.20.x"),
    /**
     * Minecraft 1 20 5 protocol version.
     */
    MINECRAFT_1_20_5(766, "1.20.x"),
    /**
     * Minecraft 1 21 protocol version.
     */
    MINECRAFT_1_21(767, "1.21.x"),
    /**
     * Minecraft 1 21 2 protocol version.
     */
    MINECRAFT_1_21_2(768, "1.21.x"),

    MINECRAFT_1_21_4(769, "1.21.4"),

    ;

    /**
     * The constant SNAPSHOT_SUPPORT.
     */
    public static final boolean SNAPSHOT_SUPPORT = Boolean.getBoolean("net.cakemc.de.crycodes.proxy.protocol.snapshot");

    static {
        if (SNAPSHOT_SUPPORT) {
            // Add snapshot-specific logic if necessary
        }
    }

    private final int protocolId;
    private final String versionName;

    ProtocolVersion(int protocolId, String versionName) {
        this.protocolId = protocolId;
        this.versionName = versionName;
    }

    /**
     * Gets supported versions.
     *
     * @return the supported versions
     */
    public static List<String> getSupportedVersions() {
        List<String> supportedVersions = new ArrayList<>();
        for (ProtocolVersion version : ProtocolVersion.values()) {
            supportedVersions.add(version.getVersionName());
        }
        return supportedVersions;
    }

    /**
     * Gets supported version ids.
     *
     * @return the supported version ids
     */
    public static List<Integer> getSupportedVersionIds() {
        List<Integer> supportedVersionIds = new ArrayList<>();
        for (ProtocolVersion version : ProtocolVersion.values()) {
            supportedVersionIds.add(version.getProtocolId());
        }
        return supportedVersionIds;
    }

    /**
     * Gets by protocol id.
     *
     * @param id the id
     * @return the by protocol id
     */
    public static ProtocolVersion getByProtocolId(int id) {
        for (ProtocolVersion version : ProtocolVersion.values()) {
            if (version.getProtocolId() == id) {
                return version;
            }
        }
        throw new IllegalArgumentException("Unknown protocol ID: " + id);
    }

    /**
     * Gets protocol id.
     *
     * @return the protocol id
     */
    public int getProtocolId() {
        return protocolId;
    }

    /**
     * Gets version name.
     *
     * @return the version name
     */
    public String getVersionName() {
        return versionName;
    }

    /**
     * The enum Direction.
     */
    public enum Direction {
        /**
         * To client direction.
         */
        TO_CLIENT,
        /**
         * To server direction.
         */
        TO_SERVER
    }
}
