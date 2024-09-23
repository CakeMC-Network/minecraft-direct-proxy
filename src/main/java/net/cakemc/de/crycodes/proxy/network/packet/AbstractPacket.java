package net.cakemc.de.crycodes.proxy.network.packet;

import io.netty.buffer.ByteBuf;
import net.cakemc.de.crycodes.proxy.protocol.PacketOverflowException;
import net.cakemc.de.crycodes.proxy.protocol.PlayerPublicKey;
import net.cakemc.de.crycodes.proxy.protocol.Protocol;
import net.cakemc.de.crycodes.proxy.protocol.ProtocolVersion;
import net.cakemc.mc.lib.game.PlayerProfile;
import net.cakemc.mc.lib.game.nbt.NBTComponent;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * The type Abstract packet.
 */
public abstract class AbstractPacket {
    /**
     * Instantiates a new Abstract packet.
     */
    public AbstractPacket() {
    }

    /**
     * Write string.
     *
     * @param s   the s
     * @param buf the buf
     */
    public static void writeString(String s, ByteBuf buf) {
        writeString(s, buf, Short.MAX_VALUE);
    }

    /**
     * Write string.
     *
     * @param s         the s
     * @param buf       the buf
     * @param maxLength the max length
     */
    public static void writeString(String s, ByteBuf buf, int maxLength) {
        if (s.length() > maxLength) {
            throw new PacketOverflowException("Cannot send string longer than " + maxLength + " (got " + s.length() + " characters)");
        }
        byte[] b = s.getBytes(StandardCharsets.UTF_8);
        if (b.length > maxLength * 3) {
            throw new PacketOverflowException("Cannot send string longer than " + (maxLength * 3) + " (got " + b.length + " bytes)");
        }
        writeVarInt(b.length, buf);
        buf.writeBytes(b);
    }

    /**
     * Read string string.
     *
     * @param buf the buf
     * @return the string
     */
    public static String readString(ByteBuf buf) {
        return readString(buf, Short.MAX_VALUE);
    }

    /**
     * Read string string.
     *
     * @param buf    the buf
     * @param maxLen the max len
     * @return the string
     */
    public static String readString(ByteBuf buf, int maxLen) {
        int len = readVarInt(buf);
        if (len > maxLen * 3) {
            throw new PacketOverflowException("Cannot receive string longer than " + maxLen * 3 + " (got " + len + " bytes)");
        }
        String s = buf.toString(buf.readerIndex(), len, StandardCharsets.UTF_8);
        buf.readerIndex(buf.readerIndex() + len);
        if (s.length() > maxLen) {
            throw new PacketOverflowException("Cannot receive string longer than " + maxLen + " (got " + s.length() + " characters)");
        }
        return s;
    }

    /**
     * Write array.
     *
     * @param b   the b
     * @param buf the buf
     */
    public static void writeArray(byte[] b, ByteBuf buf) {
        if (b.length > Short.MAX_VALUE) {
            throw new PacketOverflowException("Cannot send byte array longer than Short.MAX_VALUE (got " + b.length + " bytes)");
        }
        writeVarInt(b.length, buf);
        buf.writeBytes(b);
    }

    /**
     * To array byte [ ].
     *
     * @param buf the buf
     * @return the byte [ ]
     */
    public static byte[] toArray(ByteBuf buf) {
        byte[] ret = new byte[buf.readableBytes()];
        buf.readBytes(ret);
        return ret;
    }

    /**
     * Read array byte [ ].
     *
     * @param buf the buf
     * @return the byte [ ]
     */
    public static byte[] readArray(ByteBuf buf) {
        return readArray(buf, buf.readableBytes());
    }

    /**
     * Read array byte [ ].
     *
     * @param buf   the buf
     * @param limit the limit
     * @return the byte [ ]
     */
    public static byte[] readArray(ByteBuf buf, int limit) {
        int len = readVarInt(buf);
        if (len > limit) {
            throw new PacketOverflowException("Cannot receive byte array longer than " + limit + " (got " + len + " bytes)");
        }
        byte[] ret = new byte[len];
        buf.readBytes(ret);
        return ret;
    }

    /**
     * Read var int array int [ ].
     *
     * @param buf the buf
     * @return the int [ ]
     */
    public static int[] readVarIntArray(ByteBuf buf) {
        int len = readVarInt(buf);
        int[] ret = new int[len];
        for (int i = 0; i < len; i++) {
            ret[i] = readVarInt(buf);
        }
        return ret;
    }

    /**
     * Write string array.
     *
     * @param s   the s
     * @param buf the buf
     */
    public static void writeStringArray(List<String> s, ByteBuf buf) {
        writeVarInt(s.size(), buf);
        for (String str : s) {
            writeString(str, buf);
        }
    }

    /**
     * Read string array list.
     *
     * @param buf the buf
     * @return the list
     */
    public static List<String> readStringArray(ByteBuf buf) {
        int len = readVarInt(buf);
        List<String> ret = new ArrayList<>(len);
        for (int i = 0; i < len; i++) {
            ret.add(readString(buf));
        }
        return ret;
    }

    /**
     * Read var int int.
     *
     * @param input the input
     * @return the int
     */
    public static int readVarInt(ByteBuf input) {
        return readVarInt(input, 5);
    }

    /**
     * Read var int int.
     *
     * @param input    the input
     * @param maxBytes the max bytes
     * @return the int
     */
    public static int readVarInt(ByteBuf input, int maxBytes) {
        int out = 0;
        int bytes = 0;
        byte in;
        while (true) {
            in = input.readByte();
            out |= (in & 127) << (bytes++ * 7);
            if (bytes > maxBytes) {
                throw new PacketOverflowException("VarInt too big (max " + maxBytes + ")");
            }
            if ((in & 128) != 128) {
                break;
            }
        }
        return out;
    }

    /**
     * Write var int.
     *
     * @param value  the value
     * @param output the output
     */
    public static void writeVarInt(int value, ByteBuf output) {
        int part;
        while (true) {
            part = value & 127;
            value >>>= 7;
            if (value != 0) {
                part |= 128;
            }
            output.writeByte(part);
            if (value == 0) {
                break;
            }
        }
    }

    /**
     * Read var short int.
     *
     * @param buf the buf
     * @return the int
     */
    public static int readVarShort(ByteBuf buf) {
        int low = buf.readUnsignedShort();
        int high = 0;
        if ((low & 32768) != 0) {
            low = low & 32767;
            high = buf.readUnsignedByte();
        }
        return ((high & 255) << 15) | low;
    }

    /**
     * Write var short.
     *
     * @param buf     the buf
     * @param toWrite the to write
     */
    public static void writeVarShort(ByteBuf buf, int toWrite) {
        int low = toWrite & 32767;
        int high = (toWrite & 8355840) >> 15;
        if (high != 0) {
            low = low | 32768;
        }
        buf.writeShort(low);
        if (high != 0) {
            buf.writeByte(high);
        }
    }

    /**
     * Write uuid.
     *
     * @param value  the value
     * @param output the output
     */
    public static void writeUUID(UUID value, ByteBuf output) {
        output.writeLong(value.getMostSignificantBits());
        output.writeLong(value.getLeastSignificantBits());
    }

    /**
     * Read uuid uuid.
     *
     * @param input the input
     * @return the uuid
     */
    public static UUID readUUID(ByteBuf input) {
        return new UUID(input.readLong(), input.readLong());
    }

    /**
     * Write properties.
     *
     * @param properties the properties
     * @param buf        the buf
     */
    public static void writeProperties(PlayerProfile.Property[] properties, ByteBuf buf) {
        if (properties == null) {
            writeVarInt(0, buf);
            return;
        }
        writeVarInt(properties.length, buf);
        for (PlayerProfile.Property prop : properties) {
            writeString(prop.getName(), buf);
            writeString(prop.getValue(), buf);
            if (prop.getSignature() != null) {
                buf.writeBoolean(true);
                writeString(prop.getSignature(), buf);
            } else {
                buf.writeBoolean(false);
            }
        }
    }

    /**
     * Read properties player profile . property [ ].
     *
     * @param buf the buf
     * @return the player profile . property [ ]
     */
    public static PlayerProfile.Property[] readProperties(ByteBuf buf) {
        PlayerProfile.Property[] properties = new PlayerProfile.Property[AbstractPacket.readVarInt(buf)];
        for (int j = 0; j < properties.length; j++) {
            String name = readString(buf);
            String value = readString(buf);
            if (buf.readBoolean()) {
                properties[j] = new PlayerProfile.Property(name, value, AbstractPacket.readString(buf));
            } else {
                properties[j] = new PlayerProfile.Property(name, value);
            }
        }
        return properties;
    }

    /**
     * Write public key.
     *
     * @param publicKey the public key
     * @param buf       the buf
     */
    public static void writePublicKey(PlayerPublicKey publicKey, ByteBuf buf) {
        if (publicKey != null) {
            buf.writeBoolean(true);
            buf.writeLong(publicKey.getExpiry());
            writeArray(publicKey.getKey(), buf);
            writeArray(publicKey.getSignature(), buf);
        } else {
            buf.writeBoolean(false);
        }
    }

    /**
     * Read public key player public key.
     *
     * @param buf the buf
     * @return the player public key
     */
    public static PlayerPublicKey readPublicKey(ByteBuf buf) {
        if (buf.readBoolean()) {
            return new PlayerPublicKey(buf.readLong(), readArray(buf, 512), readArray(buf, 4096));
        }
        return null;
    }

    /**
     * Read tag nbt component.
     *
     * @param input the input
     * @return the nbt component
     */
    public static NBTComponent readTag(ByteBuf input) {
        return net.cakemc.mc.lib.network.AbstractPacket.readNBTComponent(input);
    }

    /**
     * Write tag.
     *
     * @param tag    the tag
     * @param output the output
     */
    public static void writeTag(NBTComponent tag, ByteBuf output) {
        net.cakemc.mc.lib.network.AbstractPacket.writeNBT(output, tag);
    }

    /**
     * Write enum set.
     *
     * @param <E>     the type parameter
     * @param enumset the enumset
     * @param oclass  the oclass
     * @param buf     the buf
     */
    public static <E extends Enum<E>> void writeEnumSet(EnumSet<E> enumset, Class<E> oclass, ByteBuf buf) {
        E[] enums = oclass.getEnumConstants();
        BitSet bits = new BitSet(enums.length);
        for (int i = 0; i < enums.length; ++i) {
            bits.set(i, enumset.contains(enums[i]));
        }
        writeFixedBitSet(bits, enums.length, buf);
    }

    /**
     * Read enum set enum set.
     *
     * @param <E>    the type parameter
     * @param oclass the oclass
     * @param buf    the buf
     * @return the enum set
     */
    public static <E extends Enum<E>> EnumSet<E> readEnumSet(Class<E> oclass, ByteBuf buf) {
        E[] enums = oclass.getEnumConstants();
        BitSet bits = readFixedBitSet(enums.length, buf);
        EnumSet<E> set = EnumSet.noneOf(oclass);
        for (int i = 0; i < enums.length; ++i) {
            if (bits.get(i)) {
                set.add(enums[i]);
            }
        }
        return set;
    }

    /**
     * Read fixed bit set bit set.
     *
     * @param i   the
     * @param buf the buf
     * @return the bit set
     */
    public static BitSet readFixedBitSet(int i, ByteBuf buf) {
        byte[] bits = new byte[(i + 8) >> 3];
        buf.readBytes(bits);
        return BitSet.valueOf(bits);
    }

    /**
     * Write fixed bit set.
     *
     * @param bits the bits
     * @param size the size
     * @param buf  the buf
     */
    public static void writeFixedBitSet(BitSet bits, int size, ByteBuf buf) {
        if (bits.length() > size) {
            throw new PacketOverflowException("BitSet too large (expected " + size + " got " + bits.size() + ")");
        }
        buf.writeBytes(Arrays.copyOf(bits.toByteArray(), (size + 8) >> 3));
    }

    /**
     * Read nullable t.
     *
     * @param <T>    the type parameter
     * @param reader the reader
     * @param buf    the buf
     * @return the t
     */
    public <T> T readNullable(Function<ByteBuf, T> reader, ByteBuf buf) {
        return buf.readBoolean() ? reader.apply(buf) : null;
    }

    /**
     * Write nullable.
     *
     * @param <T>    the type parameter
     * @param t0     the t 0
     * @param writer the writer
     * @param buf    the buf
     */
    public <T> void writeNullable(T t0, BiConsumer<T, ByteBuf> writer, ByteBuf buf) {
        if (t0 != null) {
            buf.writeBoolean(true);
            writer.accept(t0, buf);
        } else {
            buf.writeBoolean(false);
        }
    }

    /**
     * Read.
     *
     * @param buf the buf
     */
    public void read(ByteBuf buf) {
        throw new UnsupportedOperationException("Packet must implement read method");
    }

    /**
     * Read.
     *
     * @param buf             the buf
     * @param protocol        the protocol
     * @param direction       the direction
     * @param protocolVersion the protocol version
     */
    public void read(ByteBuf buf, Protocol protocol, ProtocolVersion.Direction direction, int protocolVersion) {
        read(buf, direction, protocolVersion);
    }

    /**
     * Read.
     *
     * @param buf             the buf
     * @param direction       the direction
     * @param protocolVersion the protocol version
     */
    public void read(ByteBuf buf, ProtocolVersion.Direction direction, int protocolVersion) {
        read(buf);
    }

    /**
     * Write.
     *
     * @param buf the buf
     */
    public void write(ByteBuf buf) {
        throw new UnsupportedOperationException("Packet must implement write method");
    }

    /**
     * Write.
     *
     * @param buf             the buf
     * @param protocol        the protocol
     * @param direction       the direction
     * @param protocolVersion the protocol version
     */
    public void write(ByteBuf buf, Protocol protocol, ProtocolVersion.Direction direction, int protocolVersion) {
        write(buf, direction, protocolVersion);
    }

    /**
     * Write.
     *
     * @param buf             the buf
     * @param direction       the direction
     * @param protocolVersion the protocol version
     */
    public void write(ByteBuf buf, ProtocolVersion.Direction direction, int protocolVersion) {
        write(buf);
    }

    /**
     * Next protocol protocol.
     *
     * @return the protocol
     */
    public Protocol nextProtocol() {
        return null;
    }

    /**
     * Handle.
     *
     * @param handler the handler
     * @throws Exception the exception
     */
    public abstract void handle(AbstractPacketHandler handler) throws Exception;

    @Override
    public abstract boolean equals(Object obj);

    @Override
    public abstract int hashCode();

    @Override
    public abstract String toString();
}
