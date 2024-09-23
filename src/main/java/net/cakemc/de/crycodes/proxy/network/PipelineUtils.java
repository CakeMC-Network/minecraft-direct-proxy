package net.cakemc.de.crycodes.proxy.network;


import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.ServerChannel;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import net.cakemc.de.crycodes.proxy.network.codec.KickStringWriter;
import net.cakemc.de.crycodes.proxy.network.codec.PlayerChannelInitializer;
import net.cakemc.de.crycodes.proxy.network.codec.frame.Varint21LengthFieldExtraBufPrepender;
import net.cakemc.de.crycodes.proxy.network.codec.frame.Varint21LengthFieldPrepender;
import net.cakemc.de.crycodes.proxy.units.ProxyServiceAddress;

import java.net.SocketAddress;
import java.util.concurrent.ThreadFactory;

/**
 * The type Pipeline utils.
 */
public class PipelineUtils {
    /**
     * The constant LISTENER.
     */
    public static final AttributeKey<ProxyServiceAddress> LISTENER = AttributeKey.valueOf("ListerInfo");

    /**
     * The constant PLAYER_CHANNEL_INITIALIZER.
     */
    public static final PlayerChannelInitializer PLAYER_CHANNEL_INITIALIZER = new PlayerChannelInitializer(false);
    /**
     * The constant SERVER_PLAYER_CHANNEL_INITIALIZER.
     */
    public static final PlayerChannelInitializer SERVER_PLAYER_CHANNEL_INITIALIZER = new PlayerChannelInitializer(true);

    /**
     * The constant TIMEOUT_HANDLER.
     */
    public static final String TIMEOUT_HANDLER = "timeout";
    /**
     * The constant PACKET_DECODER.
     */
    public static final String PACKET_DECODER = "packet_decoder";
    /**
     * The constant PACKET_ENCODER.
     */
    public static final String PACKET_ENCODER = "packet_encoder";
    /**
     * The constant PACKET_READER.
     */
    public static final String PACKET_READER = "inbound_boss";
    /**
     * The constant ENCRYPT_HANDLER.
     */
    public static final String ENCRYPT_HANDLER = "encrypt";
    /**
     * The constant DECRYPT_HANDLER.
     */
    public static final String DECRYPT_HANDLER = "decrypt";
    /**
     * The constant FRAME_DECODER.
     */
    public static final String FRAME_DECODER = "frame_decoder";
    /**
     * The constant FRAME_PREPENDER.
     */
    public static final String FRAME_PREPENDER = "frame_prepender";
    /**
     * The constant LEGACY_DECODER.
     */
    public static final String LEGACY_DECODER = "legacy_decoder";
    /**
     * The constant LEGACY_KICKER.
     */
    public static final String LEGACY_KICKER = "legacy_kick";

    /**
     * The constant LEGACY_KICK_STRING_WRITER.
     */
    public static final KickStringWriter LEGACY_KICK_STRING_WRITER = new KickStringWriter();
    /**
     * The constant LENGTH_FIELD_PREPENDER.
     */
    public static final Varint21LengthFieldPrepender LENGTH_FIELD_PREPENDER = new Varint21LengthFieldPrepender();
    /**
     * The constant SERVER_LENGTH_FIELD_PREPENDER.
     */
    public static final Varint21LengthFieldExtraBufPrepender SERVER_LENGTH_FIELD_PREPENDER = new Varint21LengthFieldExtraBufPrepender();

    private static final boolean EPOLL = Epoll.isAvailable();

    /**
     * New event loop group event loop group.
     *
     * @param threads the threads
     * @param factory the factory
     * @return the event loop group
     */
    public static EventLoopGroup newEventLoopGroup(int threads, ThreadFactory factory) {
        return EPOLL ? new EpollEventLoopGroup(threads, factory) : new NioEventLoopGroup(threads, factory);
    }

    /**
     * Gets server channel.
     *
     * @param address the address
     * @return the server channel
     */
    public static Class<? extends ServerChannel> getServerChannel(SocketAddress address) {
        return EPOLL ? EpollServerSocketChannel.class : NioServerSocketChannel.class;
    }

    /**
     * Gets channel.
     *
     * @param address the address
     * @return the channel
     */
    public static Class<? extends Channel> getChannel(SocketAddress address) {
        return EPOLL ? EpollSocketChannel.class : NioSocketChannel.class;
    }

}
