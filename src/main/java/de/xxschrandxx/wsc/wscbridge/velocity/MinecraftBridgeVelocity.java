package de.xxschrandxx.wsc.wscbridge.velocity;

import java.io.File;
import java.io.IOException;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

import org.bstats.charts.SimpleBarChart;
import org.bstats.velocity.Metrics;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;
import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandMeta.Builder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;

import de.xxschrandxx.wsc.wscbridge.core.BridgeVars;
import de.xxschrandxx.wsc.wscbridge.core.IBridgePlugin;
import de.xxschrandxx.wsc.wscbridge.core.api.MinecraftBridgeLogger;
import de.xxschrandxx.wsc.wscbridge.core.api.command.ISender;
import de.xxschrandxx.wsc.wscbridge.velocity.api.ConfigurationVelocity;
import de.xxschrandxx.wsc.wscbridge.velocity.api.MinecraftBridgeVelocityAPI;
import de.xxschrandxx.wsc.wscbridge.velocity.api.command.SenderVelocity;
import de.xxschrandxx.wsc.wscbridge.velocity.api.event.WSCBridgeConfigReloadEventVelocity;
import de.xxschrandxx.wsc.wscbridge.velocity.api.event.WSCBridgeModuleEventVelocity;
import de.xxschrandxx.wsc.wscbridge.velocity.api.event.WSCBridgePluginReloadEventVelocity;
import de.xxschrandxx.wsc.wscbridge.velocity.commands.WSCBridgeVelocity;

@Plugin(id = "wscbridge-velocity", name = "wscbridge",
        version = "3.0.4", authors = {"xXSchrandXx"})
public class MinecraftBridgeVelocity implements IBridgePlugin<MinecraftBridgeVelocityAPI> {
    // start of api part
    public String getInfo() {
        String rawMessage = getConfiguration().getString(BridgeVars.Configuration.LangCmdInfoInfo);
        String message = rawMessage
            .replaceAll("%server%", this.getProxy().getVersion().getName())
            .replaceAll("%serverversion%", this.getProxy().getVersion().getVersion())
            .replaceAll("%pluginversion%", "3.0.4");
        return message;
    }

    private static MinecraftBridgeVelocity instance;

    public static MinecraftBridgeVelocity getInstance() {
        return instance;
    }

    private MinecraftBridgeVelocityAPI api;

    private MinecraftBridgeLogger bridgeLogger;

    @Override
    public MinecraftBridgeLogger getBridgeLogger() {
        return bridgeLogger;
    }

    public void loadAPI(ISender<?> sender) {
        api = new MinecraftBridgeVelocityAPI(
            getConfiguration().getString(BridgeVars.Configuration.User),
            getConfiguration().getString(BridgeVars.Configuration.Password),
            getBridgeLogger(),
            getConfiguration().getBoolean(BridgeVars.Configuration.Debug),
            this.getProxy()
        );
        this.getProxy().getEventManager().fireAndForget(new WSCBridgePluginReloadEventVelocity(sender));
    }

    public MinecraftBridgeVelocityAPI getAPI() {
        return this.api;
    }
    // end of api part

    // start of plugin part
    private final ProxyServer server;
    public ProxyServer getProxy() {
        return this.server;
    }

    private final Logger logger;
    private final Path dataDirectory;
    private final Metrics.Factory metricsFactory;

    @Inject
    public MinecraftBridgeVelocity(
        ProxyServer server,
        Logger logger,
        @DataDirectory Path dataDirectory,
        Metrics.Factory metricsFactory
    ) {
        this.server = server;
        this.logger = logger;
        this.dataDirectory = dataDirectory;
        this.metricsFactory = metricsFactory;

        this.configFile = new File(this.dataDirectory.toFile(), "config.json");
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        instance = this;
        bridgeLogger = new MinecraftBridgeLogger(this.logger);

        // Load configuration
        getBridgeLogger().info("Loading Configuration.");
        SenderVelocity sender = new SenderVelocity(this.getProxy().getConsoleCommandSource(), getInstance());
        if (!reloadConfiguration(sender)) {
            getBridgeLogger().warn("Could not load config.yml, disabeling plugin!");
            return;
        }

        // Load API
        getBridgeLogger().info("Loading API.");
        loadAPI(sender);

        // Load commands
        getBridgeLogger().info("Loading Commands.");
        Builder commandMeta = this.getProxy().getCommandManager().metaBuilder("wscbridge")
            .plugin(getInstance());
        if (this.getConfiguration().getBoolean(BridgeVars.Configuration.cmdAliasEnabled)) {
            for (String alias : getConfiguration().getStringList(BridgeVars.Configuration.cmdAliases)) {
                commandMeta.aliases(alias);
            }
        }
        this.getProxy().getCommandManager().register(
            commandMeta.build(),
            new WSCBridgeVelocity()
        );

        // Load bStats
        this.getProxy().getScheduler().
            buildTask(getInstance(), () -> {
                Metrics metrics = metricsFactory.make(this, 30392);
                MinecraftBridgeVelocity.getInstance().getProxy().getEventManager().fire(new WSCBridgeModuleEventVelocity()).thenAccept((moduleEvent) -> {
                    metrics.addCustomChart(new SimpleBarChart("Module", new Callable<Map<String, Integer>>() {
                        @Override
                        public Map<String, Integer> call() throws Exception {
                            Map<String, Integer> map = new HashMap<String, Integer>();
                            for (String module : moduleEvent.getModules()) {
                                map.put(module, 1);
                            }
                            return map;
                        }
                    }));
                });
            })
            .schedule();
    }
    // end of plugin part

    // start config part
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final File configFile;
    private ConfigurationVelocity config;

    public ConfigurationVelocity getConfiguration() {
        return config;
    }

    public boolean reloadConfiguration(ISender sender) {
        if (!dataDirectory.toFile().exists()) {
            dataDirectory.toFile().mkdir();
        }
        if (configFile.exists()) {
            try {
                String json = Files.readString(configFile.toPath());
                this.config = new ConfigurationVelocity(gson.fromJson(json, LinkedTreeMap.class));
            }
            catch (IOException e) {
                getBridgeLogger().warn("Could not load config.json.", e);
                return false;
            }
        }
        else {
            try {
                configFile.createNewFile();
            }
            catch (IOException e) {
                getBridgeLogger().warn("Could not create config.json.", e);
                return false;
            }
            config = new ConfigurationVelocity();
        }

        if (BridgeVars.startConfig(getConfiguration(), getBridgeLogger())) {
            if (!saveConfiguration()) {
                return false;
            }
            return reloadConfiguration(sender);
        }
        this.getProxy().getEventManager().fireAndForget(new WSCBridgeConfigReloadEventVelocity(sender));
        return true;
    }

    public boolean saveConfiguration() {
        if (!this.dataDirectory.toFile().exists()) {
            this.dataDirectory.toFile().mkdir();
        }
        try {
            Path tmp = this.dataDirectory.resolve("config.json.tmp");
            String json = gson.toJson(this.config.getConfiguration());
            Files.writeString(tmp, json, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            try {
                Files.move(tmp, configFile.toPath(), StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
            } catch (AtomicMoveNotSupportedException ex) {
                // Falls ATOMIC_MOVE nicht unterstützt wird
                Files.move(tmp, configFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
        }
        catch (IOException e) {
            getBridgeLogger().warn("Could not save config.json.", e);
            return false;
        }
        return true;
    }
    // end config part
}
