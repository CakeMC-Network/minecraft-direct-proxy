package net.cakemc.de.crycodes.proxy.network.packet.impl;

import io.netty.buffer.ByteBuf;
import net.cakemc.de.crycodes.proxy.network.packet.AbstractPacket;
import net.cakemc.de.crycodes.proxy.network.packet.AbstractPacketHandler;
import net.cakemc.de.crycodes.proxy.protocol.Protocol;
import net.cakemc.de.crycodes.proxy.protocol.ProtocolVersion;
import net.cakemc.mc.lib.game.text.rewrite.Text;
import net.cakemc.mc.lib.game.text.test.api.chat.BaseComponent;

import java.util.Objects;

/**
 * The type Disconnect packet.
 */
public class DisconnectPacket extends AbstractPacket {
    private BaseComponent message;

    /**
     * Instantiates a new Disconnect packet.
     */
    public DisconnectPacket() {
    }

    /**
     * Instantiates a new Disconnect packet.
     *
     * @param message the message
     */
    public DisconnectPacket(final BaseComponent message) {
        this.message = message;
    }

    @Override
    public void read(ByteBuf buf, Protocol protocol, ProtocolVersion.Direction direction, int protocolVersion) {
        message = net.cakemc.mc.lib.network.AbstractPacket.readComponent(buf);
    }

    @Override
    public void write(ByteBuf buf, Protocol protocol, ProtocolVersion.Direction direction, int protocolVersion) {
        if (protocol == Protocol.LOGIN) {
            writeString(new Text(message.toLegacyText()).toJson().toString(), buf);
            return;
        }
        net.cakemc.mc.lib.network.AbstractPacket.writeComponent(buf, message);
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

    @Override
    public String toString() {
        return "DisconnectPacket(message=" + this.getMessage() + ")";
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof DisconnectPacket other)) return false;
        if (!other.canEqual(this)) return false;
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
        return other instanceof DisconnectPacket;
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $message = this.getMessage();
        result = result * PRIME + ($message == null ? 43 : $message.hashCode());
        return result;
    }
}
