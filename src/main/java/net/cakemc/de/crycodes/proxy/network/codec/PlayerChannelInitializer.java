package net.cakemc.de.crycodes.proxy.network.codec;

import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import net.cakemc.de.crycodes.proxy.network.PacketReader;
import net.cakemc.de.crycodes.proxy.network.codec.frame.Varint21FrameDecoder;

import java.util.concurrent.TimeUnit;

import static net.cakemc.de.crycodes.proxy.network.PipelineUtils.*;

/**
 * The type Player channel initializer.
 */
public class PlayerChannelInitializer extends ChannelInitializer<Channel> {
    private boolean toServer = false;

    /**
     * Instantiates a new Player channel initializer.
     */
    public PlayerChannelInitializer() {

    }

    /**
     * Instantiates a new Player channel initializer.
     *
     * @param toServer the to server
     */
    public PlayerChannelInitializer(final boolean toServer) {
        this.toServer = toServer;
    }

    @Override
    public void initChannel(Channel ch) throws Exception {
        ch.config().setOption(ChannelOption.IP_TOS, 24);

        ch.config().setAllocator(PooledByteBufAllocator.DEFAULT);
        ch.pipeline().addLast(FRAME_DECODER, new Varint21FrameDecoder());
        //ch.pipeline().addLast(TIMEOUT_HANDLER, new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS));

        ch.pipeline().addLast(FRAME_PREPENDER, (toServer) ? SERVER_LENGTH_FIELD_PREPENDER : LENGTH_FIELD_PREPENDER);
        ch.pipeline().addLast(PACKET_READER, new PacketReader());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}