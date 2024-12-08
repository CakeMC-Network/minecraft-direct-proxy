package net.cakemc.de.crycodes.proxy.network.http;

import net.cakemc.de.crycodes.proxy.connection.handler.ProxyLoginHandler;
import net.cakemc.mc.lib.common.type.json.JsonArray;
import net.cakemc.mc.lib.common.type.json.JsonObject;
import net.cakemc.mc.lib.common.type.json.JsonParser;
import net.cakemc.mc.lib.common.type.json.JsonValue;
import net.cakemc.mc.lib.game.PlayerProfile;
import net.cakemc.mc.lib.game.PlayerProfile.Property;
import net.cakemc.protocol.player.NetworkPlayerConnection;
import net.cakemc.protocol.protocols.intern.packets.client.ClientGameProfilePacket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ClientAuthHttpCallBack implements HttpCallback {

	private final ProxyLoginHandler networkPlayer;

	public ClientAuthHttpCallBack(ProxyLoginHandler networkPlayer) {
		this.networkPlayer = networkPlayer;
	}

	@Override public void finish(String response) {
		JsonParser parser = new JsonParser(response);
		try {
			JsonObject value = parser.parse().asObject();

			String name = value.get("name").asString();
			String id = value.get("id").asString();

			String uuid = fromFlatString(id);

			JsonArray jsonValues = value.get("properties").asArray();
			List<Property> properties = new ArrayList<>(jsonValues.size());
			for (JsonValue jsonValue : jsonValues) {
				JsonObject propertyEntry = jsonValue.asObject();

				String propertyName = propertyEntry.get("name").asString();
				String propertyValue = propertyEntry.get("value").asString();
				String propertySignature = propertyEntry.get("signature").asString();
				properties.add(new Property(propertyName, propertyValue, propertySignature));
			}

			PlayerProfile profile = new PlayerProfile(uuid, name);
			profile.getProperties().addAll(properties);

			networkPlayer.setProfile(profile);
			networkPlayer.setUniqueId(UUID.fromString(uuid));

			networkPlayer.finish();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override public void exception(Throwable exception) {
		exception.printStackTrace();
	}

	public static String fromFlatString(String str) {
		return (str.substring(0, 8)
		                  + "-" + str.substring(8, 12)
		                  + "-" + str.substring(12, 16)
		                  + "-" + str.substring(16, 20)
		                  + "-" + str.substring(20, 32));
	}
}
