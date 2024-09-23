package net.cakemc.de.crycodes.proxy.network;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import net.cakemc.de.crycodes.proxy.channel.PlayerChannel;
import net.cakemc.de.crycodes.proxy.connection.data.CancelSendSignal;
import net.cakemc.de.crycodes.proxy.network.packet.ProtocolPacket;
import net.cakemc.de.crycodes.proxy.protocol.Protocol;

/**
 * The type Packet reader.
 */
public class PacketReader extends ChannelInboundHandlerAdapter {

    private PlayerChannel channel;
    private PacketHandler handler;

    /**
     * Sets handler.
     *
     * @param handler the handler
     */
    public void setHandler(PacketHandler handler) {

        this.handler = handler;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        if (handler != null) {
            channel = new PlayerChannel(ctx);
            handler.connected(channel);

        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        if (handler != null) {
            channel.markClosed();
            handler.disconnected(channel);
        }
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        if (handler != null) {
            handler.writabilityChanged(channel);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ProtocolPacket packet = (ProtocolPacket) msg;
        if (packet.packet != null) {
            Protocol nextProtocol = packet.packet.nextProtocol();
            if (nextProtocol != null) {
                channel.setDecodeProtocol(nextProtocol);
            }
        }

        if (handler != null) {
            boolean sendPacket = handler.shouldHandle(packet);
            try {
                if (sendPacket && packet.packet != null) {
                    try {
                        packet.packet.handle(handler);
                    } catch (CancelSendSignal ex) {
                        sendPacket = false;
                    }
                }
                if (sendPacket) {
                    handler.handle(packet);
                }
            } finally {
                packet.trySingleRelease();
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (ctx.channel().isActive()) {
            ctx.close();
        }
    }
}
