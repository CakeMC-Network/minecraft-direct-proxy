package net.cakemc.de.crycodes.proxy.network.http;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.LastHttpContent;

import java.nio.charset.StandardCharsets;

public class HttpHandler extends SimpleChannelInboundHandler<Object> {

	private final HttpCallback callback;
	private StringBuilder context = new StringBuilder();

	public HttpHandler(HttpCallback callback) {
		this.callback = callback;
	}

	@Override protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object object) throws Exception {
		if (object instanceof HttpContent content) {
			context.append(content.content().toString(StandardCharsets.UTF_8));

			if (object instanceof LastHttpContent) {
				finishConversation(channelHandlerContext);
			}
		}
		if (object instanceof HttpResponse response) {
			int code = response.status().code();

			if (code == HttpResponseStatus.NO_CONTENT.code()) {
				finishConversation(channelHandlerContext);
				return;
			}

			if (code == HttpResponseStatus.OK.code())
				return;

			throw new IllegalArgumentException("expected code %s but received".formatted(code));
		}
	}

	public void finishConversation(ChannelHandlerContext channelHandlerContext) {
		if (callback == null)
			return;

		this.callback.finish(this.context.toString());
		this.context = new StringBuilder();
		channelHandlerContext.close();
	}
}
