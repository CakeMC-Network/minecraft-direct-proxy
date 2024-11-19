package test;

import net.cakemc.de.crycodes.proxy.events.ProxyPingEvent;
import net.cakemc.de.crycodes.proxy.events.connect.ProxyLoginEvent;
import net.cakemc.de.crycodes.proxy.target.ProxyTargetImpl;
import net.cakemc.de.crycodes.proxy.ProxyServiceImpl;

import java.net.InetSocketAddress;

public class Test {

    public static void main(String[] args) throws Exception {
        ProxyServiceImpl proxyService = new ProxyServiceImpl("proxy-1", "localhost", 25577);
        proxyService.getEventManager().register(ProxyLoginEvent.class, proxyLoginEvent -> {
            proxyLoginEvent.getPlayer().setDisplayName("test");
        });

        proxyService.getEventManager().register(ProxyPingEvent.class, pingEvent -> {
            pingEvent.setMotdRaw("A".repeat(45) + "B".repeat(45));
            //pingEvent.setMotdLines("Hello this is a wonderfull motd line",
            //        "This is my second §amotd§7 line")
            //        .setVersion("TEST", pingEvent.getPingedProtocolId())
            //        .setPlayer(ThreadLocalRandom.current().nextInt(400), 500);
        });

        proxyService.getTargets().add(new ProxyTargetImpl("test", new InetSocketAddress(20000)));
        proxyService.start();

    }
}
