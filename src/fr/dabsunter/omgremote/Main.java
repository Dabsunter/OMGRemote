package fr.dabsunter.omgremote;

import java.io.File;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import fr.dabsunter.omgremote.commands.FileCommands;
import fr.dabsunter.omgremote.commands.ProcessCommands;
import fr.dabsunter.omgremote.util.Tools;

public class Main extends JavaPlugin {
	
	@Override
	public void onEnable() {
		saveDefaultConfig();
		
		getServer().getPluginManager().registerEvents(new CommandListener(), this);
		
		FileCommands fileCmd = new FileCommands();
		CommandListener.register("cd", fileCmd);
		CommandListener.register("ls", fileCmd);
		CommandListener.register("cp", fileCmd);
		CommandListener.register("mv", fileCmd);
		CommandListener.register("rm", fileCmd);
		ProcessCommands processCmd = new ProcessCommands();
		CommandListener.register("start", processCmd);
		CommandListener.register("select", processCmd);
		CommandListener.register("send", processCmd);
		CommandListener.register("kill", processCmd);
		
		ConfigurationSection servers = getConfig().getConfigurationSection("servers");
		for(String key : servers.getKeys(false)) {
			ConfigurationSection section = servers.getConfigurationSection(key);
			List<String> command;
			if(section.isList("command")) {
				command = section.getStringList("command");
			} else {
				command = Tools.parseCommand(section.getString("command"));
			}
			File directory = null;
			if(section.contains("folder")) {
				directory = new File(section.getString("folder"));
			}
			new ProcessManager(key, command, directory);
		}
	}
	
	@Override
	public void onDisable() {
		ProcessManager.stopAll();
	}

}
