package fr.dabsunter.omgremote.commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import fr.dabsunter.omgremote.ProcessManager;
import fr.dabsunter.omgremote.util.Tools;

public class ProcessCommands implements CommandExecutor {
	
	private String selected = null;

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		switch(label) {
		case "start":
			start(sender, args);
			break;
		case "select":
			select(sender, args);
			break;
		case "send":
			send(sender, args);
			break;
		case "kill":
			kill(sender, args);
			return true;
		}
		return false;
	}
	
	private void start(CommandSender sender, String[] args) {
		List<String> command = Tools.parseCommand(Tools.join(args));
		String name = command.remove(0);
		if(ProcessManager.get(name) != null) {
			sender.sendMessage("Process " + name + " is already running !");
		} else {
			new ProcessManager(name, command);
			selected = name;
			sender.sendMessage("Successfully launched and selected " + name + " process");
		}
	}
	
	private void select(CommandSender sender, String[] args) {
		String name = Tools.join(args);
		if(ProcessManager.get(name) != null) {
			selected = name;
			sender.sendMessage("Successfully selected " + name + "process");
		} else {
			sender.sendMessage(name + " process does not exist !");
		}
	}
	
	private void send(CommandSender sender, String[] args) {
		ProcessManager pm = ProcessManager.get(selected);
		if(pm != null) {
			pm.write(Tools.join(args));
			sender.sendMessage("Command successfully sent to " + selected + " process");
		} else {
			sender.sendMessage(selected + " process does not exist !");
		}
	}
	
	private void kill(CommandSender sender, String[] args) {
		String name = Tools.join(args);
		ProcessManager pm = ProcessManager.get(name);
		if(pm != null) {
			pm.stop();
			sender.sendMessage(name + " process successfully stopped");
		} else {
			sender.sendMessage(name + " process does not exist !");
		}
	}

}
