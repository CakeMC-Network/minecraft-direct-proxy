package net.cakemc.de.crycodes.proxy.network.http;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollDatagramChannel;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.kqueue.KQueue;
import io.netty.channel.kqueue.KQueueDatagramChannel;
import io.netty.channel.kqueue.KQueueSocketChannel;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.resolver.dns.DnsAddressResolverGroup;
import io.netty.resolver.dns.DnsServerAddressStreamProvider;
import io.netty.resolver.dns.DnsServerAddressStreamProviders;

import javax.net.ssl.SSLException;
import java.net.InetSocketAddress;
import java.net.URI;

public class HttpClient {

	private static final DnsAddressResolverGroup resolver;

	static {
		final DnsServerAddressStreamProvider dnsProvider;
		dnsProvider = DnsServerAddressStreamProviders.platformDefault();

		resolver = new DnsAddressResolverGroup(
			 Epoll.isAvailable() ? EpollDatagramChannel.class :
			 KQueue.isAvailable() ? KQueueDatagramChannel.class : NioDatagramChannel.class,
			 dnsProvider);
	}

	public static void connectWithResult(String url, EventLoopGroup eventExecutors, HttpCallback callback) {
		URI uri = URI.create(url);

		String scheme = uri.getScheme() == null ? "http" : uri.getScheme();
		String host = uri.getHost() == null ? "127.0.0.1" : uri.getHost();
		int port = uri.getPort();

		SslContext context = null;

		switch (scheme) {
			case "https": {
				if (port == -1) {
					port = 443;
				}
				try {
					context = SslContextBuilder.forClient()
					                          .trustManager(InsecureTrustManagerFactory.INSTANCE).build();
				} catch (SSLException e) {
					callback.exception(e);
					return;
				}
				break;
			}
			case "http": {
				if (port == -1)
					port = 80;
				break;
			}

			default:
				throw new IllegalStateException("invalid scheme (%s) for http client!".formatted(scheme));
		}


		new Bootstrap()
			 .group(eventExecutors)
			 //.resolver(resolver)
			 .channel(
				  Epoll.isAvailable() ? EpollSocketChannel.class :
				  KQueue.isAvailable() ? KQueueSocketChannel.class : NioSocketChannel.class
			 )
			 .handler(new HttpChannelInitializer(context, callback))
			 .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
			 .connect(InetSocketAddress.createUnresolved(host, port))
			 .addListener((ChannelFutureListener) future -> {
				 if (future.isSuccess()) {
					 String path = uri.getRawPath() + (uri.getRawQuery() == null ? ""
					                                                             : "?" + uri.getRawQuery());
					 HttpRequest request = new DefaultHttpRequest(HttpVersion.HTTP_1_1,
					                                              HttpMethod.GET, path);
					 request.headers().set(HttpHeaderNames.HOST, host);
					 future.channel().writeAndFlush(request);
				 } else {
					 callback.exception(future.cause());
				 }
			 });

	}

}
