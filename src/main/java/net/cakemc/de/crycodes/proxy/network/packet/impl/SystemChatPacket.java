package net.cakemc.de.crycodes.proxy.network.packet.impl;

import io.netty.buffer.ByteBuf;
import net.cakemc.de.crycodes.proxy.network.packet.AbstractPacket;
import net.cakemc.de.crycodes.proxy.network.packet.AbstractPacketHandler;
import net.cakemc.de.crycodes.proxy.protocol.ProtocolVersion;
import net.cakemc.mc.lib.game.text.test.api.ChatMessageType;
import net.cakemc.mc.lib.game.text.test.api.chat.BaseComponent;

import java.util.Objects;

/**
 * The type System chat packet.
 */
public class SystemChatPacket extends AbstractPacket {
    private BaseComponent message;
    private int position;

    /**
     * Instantiates a new System chat packet.
     */
    public SystemChatPacket() {
    }

    /**
     * Instantiates a new System chat packet.
     *
     * @param message  the message
     * @param position the position
     */
    public SystemChatPacket(final BaseComponent message, final int position) {
        this.message = message;
        this.position = position;
    }

    @Override
    public void read(ByteBuf buf, ProtocolVersion.Direction direction, int protocolVersion) {
        message = net.cakemc.mc.lib.network.AbstractPacket.readComponent(buf);
        position = (protocolVersion >= ProtocolVersion.MINECRAFT_1_19_1.getProtocolId()) ?
                ((buf.readBoolean()) ? ChatMessageType.ACTION_BAR.ordinal() : 0) : readVarInt(buf);
    }

    @Override
    public void write(ByteBuf buf, ProtocolVersion.Direction direction, int protocolVersion) {
        net.cakemc.mc.lib.network.AbstractPacket.writeComponent(buf, message);
        if (protocolVersion >= ProtocolVersion.MINECRAFT_1_19_1.getProtocolId()) {
            buf.writeBoolean(position == ChatMessageType.ACTION_BAR.ordinal());
        } else {
            writeVarInt(position, buf);
        }
    }

    @Override
    public void handle(AbstractPacketHandler handler) throws Exception {
        handler.handle(this);
    }

    /**
     * Gets message.
     *
     * @return the message
     */
    public BaseComponent getMessage() {
        return this.message;
    }

    /**
     * Sets message.
     *
     * @param message the message
     */
    public void setMessage(final BaseComponent message) {
        this.message = message;
    }

    /**
     * Gets position.
     *
     * @return the position
     */
    public int getPosition() {
        return this.position;
    }

    /**
     * Sets position.
     *
     * @param position the position
     */
    public void setPosition(final int position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "SystemChatPacket(message=" + this.getMessage() + ", position=" + this.getPosition() + ")";
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof SystemChatPacket other)) return false;
        if (!other.canEqual(this)) return false;
        if (this.getPosition() != other.getPosition()) return false;
        final Object this$message = this.getMessage();
        final Object other$message = other.getMessage();
        return Objects.equals(this$message, other$message);
    }

    /**
     * Can equal boolean.
     *
     * @param other the other
     * @return the boolean
     */
    protected boolean canEqual(final Object other) {
        return other instanceof SystemChatPacket;
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + this.getPosition();
        final Object $message = this.getMessage();
        result = result * PRIME + ($message == null ? 43 : $message.hashCode());
        return result;
    }
}
