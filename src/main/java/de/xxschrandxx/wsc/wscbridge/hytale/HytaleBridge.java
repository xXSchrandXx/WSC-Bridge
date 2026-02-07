package de.xxschrandxx.wsc.wscbridge.hytale;

import java.io.File;
import java.io.IOException;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hypixel.hytale.server.core.HytaleServer;
import com.hypixel.hytale.server.core.console.ConsoleSender;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;

import de.xxschrandxx.wsc.wscbridge.core.BridgeVars;
import de.xxschrandxx.wsc.wscbridge.core.IBridgePlugin;
import de.xxschrandxx.wsc.wscbridge.core.api.command.ISender;
import de.xxschrandxx.wsc.wscbridge.hytale.api.ConfigurationHytale;
import de.xxschrandxx.wsc.wscbridge.hytale.api.HytaleBridgeAPI;
import de.xxschrandxx.wsc.wscbridge.hytale.api.HytaleBridgeLogger;
import de.xxschrandxx.wsc.wscbridge.hytale.api.command.SenderHytale;
import de.xxschrandxx.wsc.wscbridge.hytale.api.event.WSCBridgeConfigReloadEventHytale;
import de.xxschrandxx.wsc.wscbridge.hytale.api.event.WSCBridgePluginReloadEventHytale;
import de.xxschrandxx.wsc.wscbridge.hytale.commands.WSCBridgeHytale;

public class HytaleBridge extends JavaPlugin implements IBridgePlugin {

    // start of api part
    public String getInfo() {
        String rawMessage = getConfiguration().getString(BridgeVars.Configuration.LangCmdInfoInfo);
        String message = rawMessage
            .replaceAll("%server%", HytaleServer.get().getServerName())
            .replaceAll("%serverversion%", getManifest().getServerVersion().toString())
            .replaceAll("%pluginversion%", getManifest().getVersion().toString());
        return message;
    }

    private static HytaleBridge instance;

    public static HytaleBridge getInstance() {
        return instance;
    }

    private HytaleBridgeAPI api;

    private HytaleBridgeLogger bridgeLogger;

    @Override
    public void loadAPI(ISender sender) {
        api = new HytaleBridgeAPI(
            getConfiguration().getString(BridgeVars.Configuration.User),
            getConfiguration().getString(BridgeVars.Configuration.Password),
            getBridgeLogger(),
            getConfiguration().getBoolean(BridgeVars.Configuration.Debug)
        );
        HytaleServer.get().getEventBus().dispatchFor(WSCBridgePluginReloadEventHytale.class).dispatch(new WSCBridgePluginReloadEventHytale(sender));
    }

    public HytaleBridgeAPI getAPI() {
        return this.api;
    }

    @Override
    public HytaleBridgeLogger getBridgeLogger() {
        return bridgeLogger;
    }
    // end of api part

    // start of plugin part
    public HytaleBridge(JavaPluginInit init) {
        super(init);
    }

    @Override
    protected void setup() {
        instance = this;
        bridgeLogger = new HytaleBridgeLogger(getLogger());

        // Load configuration
        getLogger().atInfo().log("Loading Configuration.");
        SenderHytale sender = new SenderHytale(ConsoleSender.INSTANCE, getInstance());
        if (!reloadConfiguration(sender)) {
            getLogger().atWarning().log("Could not load config.json, disabeling plugin!");
            shutdown();
            return;
        }
    }

    @Override
    protected void start() {
        // Load API
        getLogger().atInfo().log("Loading API.");
        SenderHytale sender = new SenderHytale(ConsoleSender.INSTANCE, getInstance());
        loadAPI(sender);

        // Load commands
        getLogger().atInfo().log("Loading Commands.");
        List<String> aliases = null;
        if (getConfiguration().getBoolean(BridgeVars.Configuration.cmdAliasEnabled)) {
            aliases = getConfiguration().getStringList(BridgeVars.Configuration.cmdAliases);
        }
        getCommandRegistry().registerCommand(new WSCBridgeHytale(aliases));
    }

    @Override
    protected void shutdown() {
    }
    // end of plugin part

    // start config part
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private File configFile = new File(getDataDirectory().toFile(), "config.json");
    private ConfigurationHytale config;

    public ConfigurationHytale getConfiguration() {
        return config;
    }

    public boolean reloadConfiguration(ISender sender) {
        if (!getDataDirectory().toFile().exists()) {
            getDataDirectory().toFile().mkdir();
        }
        if (configFile.exists()) {
            try {
                String json = Files.readString(configFile.toPath());
                config = new ConfigurationHytale(gson.fromJson(json, HashMap.class));
            }
            catch (IOException e) {
                getLogger().atWarning().log("Could not load config.json.", e);
                return false;
            }
        }
        else {
            try {
                configFile.createNewFile();
            }
            catch (IOException e) {
                getLogger().atWarning().log("Could not create config.json.", e);
                return false;
            }
            config = new ConfigurationHytale();
        }

        if (BridgeVars.startConfig(getConfiguration(), getBridgeLogger())) {
            if (!saveConfiguration()) {
                return false;
            }
            return reloadConfiguration(sender);
        }
        HytaleServer.get().getEventBus().dispatchFor(WSCBridgeConfigReloadEventHytale.class).dispatch(new WSCBridgeConfigReloadEventHytale(sender));
        return true;
    }

    public boolean saveConfiguration() {
        if (!getDataDirectory().toFile().exists()) {
            getDataDirectory().toFile().mkdir();
        }
        try {
            Path tmp = getDataDirectory().resolve("config.json.tmp");
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
            getLogger().atWarning().log("Could not save config.json.", e);
            return false;
        }
        return true;
    }
    // end config part
}
