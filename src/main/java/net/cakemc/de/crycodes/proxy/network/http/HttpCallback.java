package net.cakemc.de.crycodes.proxy.network.http;

public interface HttpCallback {

	void finish(String response);

	void exception(Throwable exception);

}
