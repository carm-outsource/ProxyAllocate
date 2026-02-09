package cc.carm.outsource.plugin.proxyallocate;

import com.google.inject.Inject;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.ServerPreConnectEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.config.ProxyConfig;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

@Plugin(id = "proxyallocate", name = "ProxyAllocate", version = "1.0.1",
        description = "A simple plugin that supports evenly teleporting players to different default or forced-host servers when login.",
        url = "https://github.com/carm-outsource/ProxyAllocate",
        authors = {"CarmJos"}
)
public class ProxyAllocate {

    protected static ProxyAllocate instance;

    private final ProxyServer server;
    private final Logger logger;

    public static ProxyAllocate getInstance() {
        return instance;
    }

    @Inject
    public ProxyAllocate(ProxyServer server, Logger logger) {
        this.server = server;
        this.logger = logger;
        ProxyAllocate.instance = this;
    }

    public static ProxyServer getServer() {
        return getInstance().server;
    }

    public @NotNull Logger getLogger() {
        return logger;
    }

    public static void info(String message, Object... params) {
        getInstance().getLogger().info(String.format(message, params));
    }

    public static void severe(String message, Object... params) {
        getInstance().getLogger().severe(String.format(message, params));
    }

    @Subscribe
    public void onServerConnected(ServerPreConnectEvent event) {
        Player player = event.getPlayer();
        if (player.getCurrentServer().isPresent()) return;

        String domain = player.getVirtualHost().map(InetSocketAddress::getHostString).orElse("").toLowerCase();
        ProxyConfig conf = ProxyAllocate.getServer().getConfiguration();
        RegisteredServer target = pickServer(conf.getForcedHosts().get(domain));
        if (target == null) {
            target = pickServer(conf.getAttemptConnectionOrder());
        }

        if (target == null) {
            // NO server available
            event.setResult(ServerPreConnectEvent.ServerResult.denied());
            return;
        }
        event.setResult(ServerPreConnectEvent.ServerResult.allowed(target));
    }

    public static @Nullable RegisteredServer pickServer(@Nullable Collection<String> servers) {
        if (servers == null || servers.isEmpty()) return null;
        if (servers.size() == 1) return ProxyAllocate.getServer().getServer(servers.iterator().next()).orElse(null);
        Set<RegisteredServer> available = new HashSet<>();
        servers.forEach(id -> ProxyAllocate.getServer().getServer(id).ifPresent(available::add));
        return pickRegisteredServer(available);
    }

    public static @Nullable RegisteredServer pickRegisteredServer(@Nullable Collection<RegisteredServer> servers) {
        if (servers == null || servers.isEmpty()) return null;
        if (servers.size() == 1) return servers.iterator().next();
        return servers.stream().min(
                Comparator.comparingInt((RegisteredServer o) -> o.getPlayersConnected().size())
                        .thenComparing(o -> o.getServerInfo().getName())
        ).orElse(null);
    }


}
