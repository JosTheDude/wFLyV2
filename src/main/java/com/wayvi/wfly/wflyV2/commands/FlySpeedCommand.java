package com.wayvi.wfly.wflyV2.commands;

import com.wayvi.wfly.wflyV2.managers.fly.FlyManager;
import fr.traqueur.commands.api.Arguments;
import fr.traqueur.commands.api.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class FlySpeedCommand extends Command<JavaPlugin> {

    private final FlyManager flyManager;

    public FlySpeedCommand(JavaPlugin plugin, FlyManager flyManager) {
        super(plugin, "flyspeed");
        setDescription("Manage the fly speed");
        setUsage("/flyspeed <number>");
        addArgs("speed:double");
        this.flyManager = flyManager;
    }

    @Override
    public void execute(CommandSender commandSender, Arguments arguments) {

        double speed = arguments.get("speed");
        flyManager.manageFlySpeed((Player) commandSender, speed);


    }
}
