package net.cakemc.de.crycodes.proxy.channel;


import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import net.cakemc.de.crycodes.proxy.network.PipelineUtils;
import net.cakemc.de.crycodes.proxy.network.codec.compress.PacketCompressor;
import net.cakemc.de.crycodes.proxy.network.codec.compress.PacketDecompressor;
import net.cakemc.de.crycodes.proxy.network.codec.minecraft.MinecraftDecoder;
import net.cakemc.de.crycodes.proxy.network.codec.minecraft.MinecraftEncoder;
import net.cakemc.de.crycodes.proxy.network.packet.AbstractPacket;
import net.cakemc.de.crycodes.proxy.network.packet.ProtocolPacket;
import net.cakemc.de.crycodes.proxy.network.packet.impl.DisconnectPacket;
import net.cakemc.de.crycodes.proxy.protocol.Protocol;

import java.net.SocketAddress;
import java.util.concurrent.TimeUnit;

/**
 * The type Player channel.
 */
public class PlayerChannel {
    private final Channel channel;
    private SocketAddress remoteAddress;
    private volatile boolean closed;
    private volatile boolean closing;

    /**
     * Instantiates a new Player channel.
     *
     * @param ctx the ctx
     */
    public PlayerChannel(ChannelHandlerContext ctx) {
        this.channel = ctx.channel();
        this.remoteAddress = (this.channel.remoteAddress() == null) ? this.channel.parent().localAddress() : this.channel.remoteAddress();
    }

    /**
     * Gets decode protocol.
     *
     * @return the decode protocol
     */
    public Protocol getDecodeProtocol() {
        return channel.pipeline().get(MinecraftDecoder.class).getProtocol();
    }

    /**
     * Sets decode protocol.
     *
     * @param protocol the protocol
     */
    public void setDecodeProtocol(Protocol protocol) {
        channel.pipeline().get(MinecraftDecoder.class).setProtocol(protocol);
    }

    /**
     * Gets encode protocol.
     *
     * @return the encode protocol
     */
    public Protocol getEncodeProtocol() {
        return channel.pipeline().get(MinecraftEncoder.class).getProtocol();
    }

    /**
     * Sets encode protocol.
     *
     * @param protocol the protocol
     */
    public void setEncodeProtocol(Protocol protocol) {
        channel.pipeline().get(MinecraftEncoder.class).setProtocol(protocol);
    }

    /**
     * Sets protocol.
     *
     * @param protocol the protocol
     */
    public void setProtocol(Protocol protocol) {
        setDecodeProtocol(protocol);
        setEncodeProtocol(protocol);
    }

    /**
     * Sets version.
     *
     * @param protocol the protocol
     */
    public void setVersion(int protocol) {
        channel.pipeline().get(MinecraftDecoder.class).setProtocolVersion(protocol);
        channel.pipeline().get(MinecraftEncoder.class).setProtocolVersion(protocol);
    }

    /**
     * Gets encode version.
     *
     * @return the encode version
     */
    public int getEncodeVersion() {
        return channel.pipeline().get(MinecraftEncoder.class).getProtocolVersion();
    }

    /**
     * Write.
     *
     * @param packet the packet
     */
    public void write(Object packet) {
        if (!closed) {
            AbstractPacket defined = null;
            if (packet instanceof ProtocolPacket wrapper) {
                wrapper.setReleased(true);
                channel.writeAndFlush(wrapper.buf);
                defined = wrapper.packet;
            } else {
                channel.writeAndFlush(packet);
                if (packet instanceof AbstractPacket) {
                    defined = (AbstractPacket) packet;
                }
            }
            if (defined != null) {
                Protocol nextProtocol = defined.nextProtocol();
                if (nextProtocol != null) {
                    setEncodeProtocol(nextProtocol);
                }
            }
        }
    }

    /**
     * Mark closed.
     */
    public void markClosed() {
        closed = closing = true;
    }

    /**
     * Close.
     */
    public void close() {
        close(null);
    }

    /**
     * Close.
     *
     * @param packet the packet
     */
    public void close(Object packet) {
        if (!closed) {
            closed = closing = true;
            if (packet != null && channel.isActive()) {
                channel.writeAndFlush(packet).addListeners(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE,
                        ChannelFutureListener.CLOSE);
            } else {
                channel.flush();
                channel.close();
            }
        }
    }

    /**
     * Delayed close.
     *
     * @param disconnectPacket the disconnect packet
     */
    public void delayedClose(final DisconnectPacket disconnectPacket) {
        if (!closing) {
            closing = true;
            // Minecraft client can take some time to switch protocols.
            // Sending the wrong disconnect packet whilst a protocol switch is in progress will crash it.
            // Delay 250ms to ensure that the protocol switch (if any) has definitely taken place.
            channel.eventLoop().schedule(() -> close(disconnectPacket), 250, TimeUnit.MILLISECONDS);
        }
    }

    /**
     * Add before.
     *
     * @param baseName the base name
     * @param name     the name
     * @param handler  the handler
     */
    public void addBefore(String baseName, String name, ChannelHandler handler) {

        channel.pipeline().flush();
        channel.pipeline().addBefore(baseName, name, handler);
    }

    /**
     * Gets handle.
     *
     * @return the handle
     */
    public Channel getHandle() {
        return channel;
    }

    /**
     * Sets compression threshold.
     *
     * @param compressionThreshold the compression threshold
     */
    public void setCompressionThreshold(int compressionThreshold) {
        if (channel.pipeline().get(PacketCompressor.class) == null && compressionThreshold >= 0) {
            addBefore(PipelineUtils.PACKET_ENCODER, "compress", new PacketCompressor());
        }
        if (compressionThreshold >= 0) {
            channel.pipeline().get(PacketCompressor.class).setThreshold(compressionThreshold);
        } else {
            channel.pipeline().remove("compress");
        }
        if (channel.pipeline().get(PacketDecompressor.class) == null && compressionThreshold >= 0) {
            addBefore(PipelineUtils.PACKET_DECODER, "decompress", new PacketDecompressor());
        }
        if (compressionThreshold < 0) {
            channel.pipeline().remove("decompress");
        }
    }

    /**
     * Gets remote address.
     *
     * @return the remote address
     */
    public SocketAddress getRemoteAddress() {
        return this.remoteAddress;
    }

    /**
     * Sets remote address.
     *
     * @param remoteAddress the remote address
     */
    public void setRemoteAddress(final SocketAddress remoteAddress) {
        this.remoteAddress = remoteAddress;
    }

    /**
     * Is closed boolean.
     *
     * @return the boolean
     */
    public boolean isClosed() {
        return this.closed;
    }

    /**
     * Is closing boolean.
     *
     * @return the boolean
     */
    public boolean isClosing() {
        return this.closing;
    }
}
