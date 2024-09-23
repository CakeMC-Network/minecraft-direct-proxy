package net.cakemc.de.crycodes.proxy.network.packet.impl.status;

import io.netty.buffer.ByteBuf;
import net.cakemc.de.crycodes.proxy.network.packet.AbstractPacket;
import net.cakemc.de.crycodes.proxy.network.packet.AbstractPacketHandler;
import net.cakemc.mc.lib.common.type.json.Json;
import net.cakemc.mc.lib.common.type.json.JsonArray;
import net.cakemc.mc.lib.common.type.json.JsonObject;
import net.cakemc.mc.lib.common.type.json.JsonValue;
import net.cakemc.mc.lib.game.PlayerProfile;
import net.cakemc.mc.lib.game.Status;
import net.cakemc.mc.lib.game.text.rewrite.Text;

import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * The type Status response packet.
 */
public class StatusResponsePacket extends AbstractPacket {

    private JsonObject jsonObject;
    private Status.Info info;

    /**
     * Instantiates a new Status response packet.
     */
    public StatusResponsePacket() {
    }

    /**
     * Instantiates a new Status response packet.
     *
     * @param info the info
     */
    public StatusResponsePacket(Status.Info info) {
        this.info = info;
    }

    private static JsonObject toJson(Status.Info info) {
        JsonObject obj = new JsonObject();
        Text component = info.getDescription();
        obj.add("description", component.toJson());
        JsonObject plrs;
        if (info.getPlayerList() != null) {
            plrs = new JsonObject();
            plrs.add("max", info.getPlayerList().getMaxPlayers());
            plrs.add("online", info.getPlayerList().getOnlinePlayers());
            if (!info.getPlayerList().getPlayers().isEmpty()) {
                JsonArray array = new JsonArray();
                Iterator var5 = info.getPlayerList().getPlayers().iterator();

                while (var5.hasNext()) {
                    PlayerProfile profile = (PlayerProfile) var5.next();
                    JsonObject o = new JsonObject();
                    o.add("name", profile.getName());
                    o.add("id", profile.getUuidString());
                    array.add(o);
                }

                plrs.add("sample", array);
            }

            obj.add("players", plrs);
        }

        if (info.getVersionInfo() != null) {
            plrs = new JsonObject();
            plrs.add("name", info.getVersionInfo().getVersionName());
            plrs.add("protocol", info.getVersionInfo().getProtocolVersion());
            obj.add("version", plrs);
        }

        if (info.getIconPng() != null) {
            obj.add("favicon", iconToString(info.getIconPng()));
        }

        obj.add("enforcesSecureChat", info.isEnforcesSecureChat());
        return obj;
    }

    /**
     * String to icon byte [ ].
     *
     * @param str the str
     * @return the byte [ ]
     */
    public static byte[] stringToIcon(String str) {
        if (str.startsWith("data:image/png;base64,")) {
            str = str.substring("data:image/png;base64,".length());
        }

        return Base64.getDecoder().decode(str.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Icon to string string.
     *
     * @param icon the icon
     * @return the string
     */
    public static String iconToString(byte[] icon) {
        String var10000 = new String(Base64.getEncoder().encode(icon), StandardCharsets.UTF_8);
        return "data:image/png;base64," + var10000;
    }

    @Override
    public void handle(AbstractPacketHandler handler) throws Exception {
        handler.handle(this);
    }

    @Override
    public void read(ByteBuf buf) {
        String json = readString(buf);
        Json.object();
        this.jsonObject = JsonObject.readFrom(json);
        this.info = this.parseInfo();
    }

    @Override
    public void write(ByteBuf buf) {
        writeString(toJson(this.info).toString(), buf);
    }

    /**
     * Parse info status . info.
     *
     * @return the status . info
     */
    public Status.Info parseInfo() {
        JsonValue desc = this.jsonObject.get("description");
        Text description = Text.fromJson(desc.asObject());
        Status.PlayerList players = null;
        if (this.jsonObject.get("players") != null) {
            JsonObject plrs = this.jsonObject.get("players").asObject();
            List<PlayerProfile> profiles = new ArrayList();
            if (plrs.get("sample") != null) {
                JsonArray prof = plrs.get("sample").asArray();
                if (!prof.isEmpty()) {
                    for (int index = 0; index < prof.size(); ++index) {
                        JsonObject o = prof.get(index).asObject();
                        profiles.add(new PlayerProfile(o.get("id").asString(), o.get("name").asString()));
                    }
                }
            }

            players = new Status.PlayerList(plrs.get("max").asInt(), plrs.get("online").asInt(), profiles);
        }

        Status.Version version = null;
        if (this.jsonObject.get("version") != null) {
            JsonObject ver = this.jsonObject.get("version").asObject();
            version = new Status.Version(ver.get("name").asString(), ver.get("protocol").asInt());
        }

        byte[] icon = null;
        if (this.jsonObject.get("favicon") != null) {
            icon = stringToIcon(this.jsonObject.get("favicon").asString());
        }

        boolean enforcesSecureChat = false;
        if (this.jsonObject.get("enforcesSecureChat") != null) {
            enforcesSecureChat = this.jsonObject.get("enforcesSecureChat").asBoolean();
        }

        return new Status.Info(description, players, version, icon, enforcesSecureChat);
    }

    /**
     * Gets json object.
     *
     * @return the json object
     */
    public JsonObject getJsonObject() {
        return this.jsonObject;
    }

    /**
     * Gets info.
     *
     * @return the info
     */
    public Status.Info getInfo() {
        return this.info;
    }

    public String toString() {
        String var10000 = String.valueOf(this.jsonObject);
        return "ClientStatusResponsePacket{jsonObject=" + var10000 + ", info=" + this.info + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StatusResponsePacket that)) return false;
        return Objects.equals(jsonObject, that.jsonObject) && Objects.equals(info, that.info);
    }

    @Override
    public int hashCode() {
        return Objects.hash(jsonObject, info);
    }
}
