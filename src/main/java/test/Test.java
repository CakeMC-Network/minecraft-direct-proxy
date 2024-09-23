package test;

import net.cakemc.de.crycodes.proxy.events.connect.PostLoginEvent;
import net.cakemc.de.crycodes.proxy.target.ProxyTargetImpl;
import net.cakemc.de.crycodes.proxy.ProxyServiceImpl;

import java.net.InetSocketAddress;

public class Test {

    public static void main(String[] args) throws Exception {
        ProxyServiceImpl proxyService = new ProxyServiceImpl("proxy-1", "localhost", 25577);
        proxyService.getEventManager().register(PostLoginEvent.class, postLoginEvent -> {
            postLoginEvent.getPlayer().setDisplayName("test");
        });

        proxyService.getTargets().add(new ProxyTargetImpl("test", new InetSocketAddress(20000)));
        proxyService.start();
    }
}
