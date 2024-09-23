package net.cakemc.de.crycodes.proxy.network.packet.impl;

import io.netty.buffer.ByteBuf;
import net.cakemc.de.crycodes.proxy.network.packet.AbstractPacket;
import net.cakemc.de.crycodes.proxy.network.packet.AbstractPacketHandler;
import net.cakemc.de.crycodes.proxy.protocol.PacketOverflowException;

import java.util.Objects;

/**
 * The type Login payload request packet.
 */
public class LoginPayloadRequestPacket extends AbstractPacket {
    private int id;
    private String channel;
    private byte[] data;

    /**
     * Instantiates a new Login payload request packet.
     */
    public LoginPayloadRequestPacket() {
    }

    /**
     * Instantiates a new Login payload request packet.
     *
     * @param id      the id
     * @param channel the channel
     * @param data    the data
     */
    public LoginPayloadRequestPacket(final int id, final String channel, final byte[] data) {
        this.id = id;
        this.channel = channel;
        this.data = data;
    }

    @Override
    public void read(ByteBuf buf) {
        id = readVarInt(buf);
        channel = readString(buf);
        int len = buf.readableBytes();
        if (len > 1048576) {
            throw new PacketOverflowException("Payload may not be larger than 1048576 bytes");
        }
        data = new byte[len];
        buf.readBytes(data);
    }

    @Override
    public void write(ByteBuf buf) {
        writeVarInt(id, buf);
        writeString(channel, buf);
        buf.writeBytes(data);
    }

    @Override
    public void handle(AbstractPacketHandler handler) throws Exception {
        handler.handle(this);
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public int getId() {
        return this.id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(final int id) {
        this.id = id;
    }

    /**
     * Gets channel.
     *
     * @return the channel
     */
    public String getChannel() {
        return this.channel;
    }

    /**
     * Sets channel.
     *
     * @param channel the channel
     */
    public void setChannel(final String channel) {
        this.channel = channel;
    }

    /**
     * Get data byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] getData() {
        return this.data;
    }

    /**
     * Sets data.
     *
     * @param data the data
     */
    public void setData(final byte[] data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "LoginPayloadRequestPacket(id=" + this.getId() + ", channel=" + this.getChannel() + ", data=" + java.util.Arrays.toString(this.getData()) + ")";
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof LoginPayloadRequestPacket other)) return false;
        if (!other.canEqual(this)) return false;
        if (this.getId() != other.getId()) return false;
        final Object this$channel = this.getChannel();
        final Object other$channel = other.getChannel();
        if (!Objects.equals(this$channel, other$channel)) return false;
        return java.util.Arrays.equals(this.getData(), other.getData());
    }

    /**
     * Can equal boolean.
     *
     * @param other the other
     * @return the boolean
     */
    protected boolean canEqual(final Object other) {
        return other instanceof LoginPayloadRequestPacket;
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + this.getId();
        final Object $channel = this.getChannel();
        result = result * PRIME + ($channel == null ? 43 : $channel.hashCode());
        result = result * PRIME + java.util.Arrays.hashCode(this.getData());
        return result;
    }
}
