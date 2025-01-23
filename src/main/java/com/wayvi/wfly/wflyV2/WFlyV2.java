package com.wayvi.wfly.wflyV2;

import com.wayvi.wfly.wflyV2.commands.AddTimeCommand;
import com.wayvi.wfly.wflyV2.commands.FlyCommand;
import com.wayvi.wfly.wflyV2.commands.FlySpeedCommand;
import com.wayvi.wfly.wflyV2.commands.ReloadCommand;
import com.wayvi.wfly.wflyV2.listeners.PlayerJoinListener;
import com.wayvi.wfly.wflyV2.listeners.PlayerLeaveListener;
import com.wayvi.wfly.wflyV2.managers.FlyManager;
import com.wayvi.wfly.wflyV2.managers.TimeFlyManager;
import com.wayvi.wfly.wflyV2.services.DatabaseService;
import com.wayvi.wfly.wflyV2.util.MiniMessageSupportUtil;
import fr.maxlego08.sarah.DatabaseConfiguration;
import fr.maxlego08.sarah.DatabaseConnection;
import fr.maxlego08.sarah.SqliteConnection;
import fr.maxlego08.sarah.RequestHelper;
import fr.traqueur.commands.api.CommandManager;
import org.bukkit.plugin.java.JavaPlugin;
import com.wayvi.wfly.wflyV2.util.ConfigUtil;

public final class WFlyV2 extends JavaPlugin {

    private FlyManager flyManager;

    private TimeFlyManager timeFlyManager;

    @Override
    public void onEnable() {

        //CREATE INSTANCE TO ACCESS DATABASE
        DatabaseConfiguration configuration = DatabaseConfiguration.sqlite(true);
        DatabaseConnection connection = new SqliteConnection(configuration, getDataFolder());

        //INIT DATABASE
        DatabaseService databaseService = new DatabaseService(this);
        databaseService.initializeDatabase();

        //INIT RequestHelper
        RequestHelper requestHelper = new RequestHelper(connection, this.getLogger()::info);

        // CONFIGS
        ConfigUtil configUtil = new ConfigUtil(this);
        configUtil.createCustomConfig();

        //INIT miniMessageSupport
        MiniMessageSupportUtil miniMessageSupportUtil = new MiniMessageSupportUtil();

        //INIT FlyManager
        this.flyManager = new FlyManager(this, requestHelper, configUtil, miniMessageSupportUtil);

        this.timeFlyManager = new TimeFlyManager(this, requestHelper, miniMessageSupportUtil);

        // COMMANDS
        CommandManager commandManager = new CommandManager(this);
        commandManager.registerCommand(new ReloadCommand(this, configUtil, miniMessageSupportUtil));
        commandManager.registerCommand(new FlyCommand(this));
        commandManager.registerCommand(new FlySpeedCommand(this, this.flyManager));
        commandManager.registerCommand(new AddTimeCommand(this));

        //LISTENER
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this.flyManager), this);
        getServer().getPluginManager().registerEvents(new PlayerLeaveListener(this), this);


        getLogger().info("Plugin enabled");
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin disabled");
    }

    public TimeFlyManager getTimeFlyManager() {
        return timeFlyManager;
    }

    public FlyManager getFlyManager() {
        return flyManager;
    }
}
