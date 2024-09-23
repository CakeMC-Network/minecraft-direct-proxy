package net.cakemc.de.crycodes.proxy.network.packet.impl;

import io.netty.buffer.ByteBuf;
import net.cakemc.de.crycodes.proxy.network.packet.AbstractPacket;
import net.cakemc.de.crycodes.proxy.network.packet.AbstractPacketHandler;
import net.cakemc.de.crycodes.proxy.protocol.PacketOverflowException;

/**
 * The type Login payload response packet.
 */
public class LoginPayloadResponsePacket extends AbstractPacket {
    private int id;
    private byte[] data;

    /**
     * Instantiates a new Login payload response packet.
     */
    public LoginPayloadResponsePacket() {
    }

    /**
     * Instantiates a new Login payload response packet.
     *
     * @param id   the id
     * @param data the data
     */
    public LoginPayloadResponsePacket(final int id, final byte[] data) {
        this.id = id;
        this.data = data;
    }

    @Override
    public void read(ByteBuf buf) {
        id = readVarInt(buf);
        if (buf.readBoolean()) {
            int len = buf.readableBytes();
            if (len > 1048576) {
                throw new PacketOverflowException("Payload may not be larger than 1048576 bytes");
            }
            data = new byte[len];
            buf.readBytes(data);
        }
    }

    @Override
    public void write(ByteBuf buf) {
        writeVarInt(id, buf);
        if (data != null) {
            buf.writeBoolean(true);
            buf.writeBytes(data);
        } else {
            buf.writeBoolean(false);
        }
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
        return "LoginPayloadResponsePacket(id=" + this.getId() + ", data=" + java.util.Arrays.toString(this.getData()) + ")";
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof LoginPayloadResponsePacket other)) return false;
        if (!other.canEqual(this)) return false;
        if (this.getId() != other.getId()) return false;
        return java.util.Arrays.equals(this.getData(), other.getData());
    }

    /**
     * Can equal boolean.
     *
     * @param other the other
     * @return the boolean
     */
    protected boolean canEqual(final Object other) {
        return other instanceof LoginPayloadResponsePacket;
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + this.getId();
        result = result * PRIME + java.util.Arrays.hashCode(this.getData());
        return result;
    }
}
