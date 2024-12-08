package net.cakemc.de.crycodes.proxy.network.http;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.timeout.ReadTimeoutHandler;

import java.util.concurrent.TimeUnit;

public class HttpChannelInitializer extends ChannelInitializer<Channel> {

	private final SslContext sslContext;
	private final HttpCallback callback;

	public HttpChannelInitializer(SslContext sslContext, HttpCallback callback) {
		this.sslContext = sslContext;
		this.callback = callback;
	}

	@Override protected void initChannel(Channel channel) throws Exception {
		ChannelPipeline pipeline = channel.pipeline();
		pipeline.addLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS));

		if (sslContext != null) {
			pipeline.addLast(sslContext.newHandler(channel.alloc()));
		}

		pipeline.addLast(new HttpClientCodec());
		pipeline.addLast(new HttpHandler(this.callback));
	}

}
