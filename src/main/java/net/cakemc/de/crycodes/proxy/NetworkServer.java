package net.cakemc.de.crycodes.proxy;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import net.cakemc.de.crycodes.proxy.network.PipelineUtils;
import net.cakemc.de.crycodes.proxy.network.codec.ServerChannelInitializer;
import net.cakemc.de.crycodes.proxy.units.ProxyServiceAddress;

import java.net.InetSocketAddress;

/**
 * The type Network server.
 */
public class NetworkServer {

    private final ProxyServiceImpl proxyServiceImpl;
    private final String host;
    private final int port;
    private final ChannelInitializer<Channel> channelInitializer;
    private Channel listener;
    private EventLoopGroup eventLoopGroup;

    /**
     * Instantiates a new Network server.
     *
     * @param proxyServiceImpl the proxy service
     * @param host             the host
     * @param port             the port
     */
    public NetworkServer(ProxyServiceImpl proxyServiceImpl, String host, int port) {
        this.proxyServiceImpl = proxyServiceImpl;
        this.host = host;
        this.port = port;

        channelInitializer = new ServerChannelInitializer(proxyServiceImpl);
    }

    /**
     * Start.
     */
    public void start() {
        eventLoopGroup = PipelineUtils.newEventLoopGroup(0, new ThreadFactoryBuilder()
                .setNameFormat("Netty IO Thread #%1$d").build());

        proxyServiceImpl.setRunning(true);
        ChannelFutureListener listener = future -> {
            if (future.isSuccess()) {
                this.listener = future.channel();
            }
        };

        new ServerBootstrap()
                .channel(PipelineUtils.getServerChannel(new InetSocketAddress(host, port)))
                .option(ChannelOption.SO_REUSEADDR, true)
                .childAttr(PipelineUtils.LISTENER, new ProxyServiceAddress(proxyServiceImpl.getIdentifier(),
                        new InetSocketAddress(host, port), "proxy-instance"))

                .childHandler(channelInitializer)
                .group(eventLoopGroup)
                .localAddress(host, port)
                .bind()
                .addListener(listener);
    }

    /**
     * Stop.
     */
    public void stop() {
        try {
            listener.close().syncUninterruptibly();
        } catch (ChannelException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Gets port.
     *
     * @return the port
     */
    public int getPort() {
        return port;
    }

    /**
     * Gets host.
     *
     * @return the host
     */
    public String getHost() {
        return host;
    }

    /**
     * Gets event loop group.
     *
     * @return the event loop group
     */
    public EventLoopGroup getEventLoopGroup() {
        return eventLoopGroup;
    }
}
