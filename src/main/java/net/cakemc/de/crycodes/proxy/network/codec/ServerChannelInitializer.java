package net.cakemc.de.crycodes.proxy.network.codec;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import net.cakemc.de.crycodes.proxy.AbstractProxyService;
import net.cakemc.de.crycodes.proxy.ProxyServiceImpl;
import net.cakemc.de.crycodes.proxy.connection.handler.ProxyLoginHandler;
import net.cakemc.de.crycodes.proxy.events.connect.ProxyPlayerConnectToServerEvent;
import net.cakemc.de.crycodes.proxy.network.PacketReader;
import net.cakemc.de.crycodes.proxy.network.codec.minecraft.MinecraftDecoder;
import net.cakemc.de.crycodes.proxy.network.codec.minecraft.MinecraftEncoder;
import net.cakemc.de.crycodes.proxy.protocol.Protocol;
import net.cakemc.de.crycodes.proxy.units.ProxyServiceAddress;

import java.net.SocketAddress;

import static net.cakemc.de.crycodes.proxy.network.PipelineUtils.*;

/**
 * The type TargetServer channel initializer.
 */
public class ServerChannelInitializer extends ChannelInitializer<Channel> {

    private final AbstractProxyService proxyService;

    /**
     * Instantiates a new TargetServer channel initializer.
     *
     * @param proxyService the proxy service
     */
    public ServerChannelInitializer(AbstractProxyService proxyService) {
        this.proxyService = proxyService;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        SocketAddress remoteAddress = (ch.remoteAddress() == null) ? ch.parent().localAddress() : ch.remoteAddress();

        ProxyServiceAddress listener = ch.attr(LISTENER).get();

        ProxyPlayerConnectToServerEvent PLayerConnectEventProxy = new ProxyPlayerConnectToServerEvent(remoteAddress, listener);
        proxyService.getEventManager().call(PLayerConnectEventProxy);
        if (PLayerConnectEventProxy.cancelled()) {
            ch.close();
            return;
        }
        PLAYER_CHANNEL_INITIALIZER.initChannel(ch);
        ch.pipeline().addBefore(FRAME_DECODER, LEGACY_DECODER, new LegacyDecoder());
        ch.pipeline().addAfter(FRAME_DECODER, PACKET_DECODER, new MinecraftDecoder(true, Protocol.HANDSHAKE,
                proxyService.getProtocolVersion()));
        ch.pipeline().addAfter(FRAME_PREPENDER, PACKET_ENCODER, new MinecraftEncoder(Protocol.HANDSHAKE, true,
                proxyService.getProtocolVersion()));
        ch.pipeline().addBefore(FRAME_PREPENDER, LEGACY_KICKER, LEGACY_KICK_STRING_WRITER);
        ch.pipeline().get(PacketReader.class).setHandler(new ProxyLoginHandler((ProxyServiceImpl) proxyService,
                listener, true)); // todo change by module!
    }

}
