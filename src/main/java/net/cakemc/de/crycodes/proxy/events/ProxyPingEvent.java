package net.cakemc.de.crycodes.proxy.events;

import net.cakemc.de.crycodes.proxy.player.PendingConnection;
import net.cakemc.mc.lib.game.PlayerProfile;
import net.cakemc.mc.lib.game.Status;
import net.cakemc.mc.lib.game.event.AbstractEvent;
import net.cakemc.mc.lib.game.text.rewrite.LegacyText;
import net.cakemc.mc.lib.game.text.rewrite.Text;

import java.util.Arrays;
import java.util.Objects;

/**
 * The type Proxy ping event.
 */
public class ProxyPingEvent extends AbstractEvent {
    private final PendingConnection connection;
    private Status.Info response;

    /**
     * Instantiates a new Proxy ping event.
     *
     * @param connection the connection
     * @param response   the response
     */
    public ProxyPingEvent(PendingConnection connection, Status.Info response) {
        this.connection = connection;
        this.response = response;
    }

    /**
     * Gets connection.
     *
     * @return the connection
     */
    public PendingConnection getConnection() {
        return this.connection;
    }

    /**
     * Gets response.
     *
     * @return the response
     */
    public Status.Info getResponse() {
        return this.response;
    }

    public int getPingedProtocolId() {
        return response.getVersionInfo().getProtocolVersion();
    }

    public ProxyPingEvent setVersion(String version, int protocolId) {
        this.response.setVersionInfo(new Status.Version(version, protocolId));
        return this;
    }

    public ProxyPingEvent setPlayer(int players, int slots, PlayerProfile... profiles) {
        this.response.setPlayerList(
                new Status.PlayerList(slots, players, Arrays.stream(profiles).toList())
        );
        return this;
    }

    public ProxyPingEvent setMotdRaw(String fullMots) {
        this.response.setDescription(LegacyText.fromLegacy(fullMots));
        return this;
    }

    public ProxyPingEvent setMotdLines(String top, String bottom) {
        this.response.setDescription(LegacyText.fromLegacy(
                mergeMotdLinesCenter(top, bottom, 69)));
        return this;
    }

    /**
     * Merges two strings into one Minecraft MOTD-compatible string.
     * The first string represents the top line, and the second string represents the bottom line.
     * The top line is automatically centered with spaces if its length is less than the maximum width.
     *
     * @param topLine    the first line of the MOTD
     * @param bottomLine the second line of the MOTD
     * @param maxWidth   the maximum width for each line
     * @return a single string formatted for the Minecraft MOTD
     */
    private String mergeMotdLinesCenter(String topLine, String bottomLine, int maxWidth) {
        if (topLine == null) topLine = "";
        if (bottomLine == null) bottomLine = "";

        String centeredTopLine = centerText(topLine, maxWidth);
        String centeredBottomLine = centerText(bottomLine, maxWidth);
        return centeredTopLine + "\n" + centeredBottomLine;
    }

    int countCharsSkipColorChar(String text) {
        int counter = 0;
        for (char c : text.toCharArray()) {
            if (c == '§')
                continue;

            counter++;
        }
        return counter;
    }

    /**
     * Centers a string by adding spaces to the beginning and end until it reaches the specified width.
     * If the string length exceeds the width, it is truncated.
     *
     * @param text the text to center
     * @param width the maximum width of the centered text
     * @return the centered text
     */
    private String centerText(String text, int width) {
        int length = countCharsSkipColorChar(text);
        if (length >= width) {
            return text.substring(0, width); // Truncate if too long
        }

        int totalPadding = width - length;
        int paddingStart = totalPadding / 2;

        return " ".repeat(paddingStart) + text;
    }

    /**
     * Sets response.
     *
     * @param response the response
     */
    public void setResponse(final Status.Info response) {
        this.response = response;
    }

    @Override
    public String toString() {
        return "ProxyPingEvent(connection=" + this.getConnection() + ", response=" + this.getResponse() + ")";
    }

    @Override
    public String getName() {
        return this.toString();
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof ProxyPingEvent other)) return false;
        if (!other.canEqual(this)) return false;
        final Object this$connection = this.getConnection();
        final Object other$connection = other.getConnection();
        if (!Objects.equals(this$connection, other$connection))
            return false;
        final Object this$response = this.getResponse();
        final Object other$response = other.getResponse();
        return Objects.equals(this$response, other$response);
    }

    /**
     * Can equal boolean.
     *
     * @param other the other
     * @return the boolean
     */
    protected boolean canEqual(final Object other) {
        return other instanceof ProxyPingEvent;
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $connection = this.getConnection();
        result = result * PRIME + ($connection == null ? 43 : $connection.hashCode());
        final Object $response = this.getResponse();
        result = result * PRIME + ($response == null ? 43 : $response.hashCode());
        return result;
    }
}
