package fr.dabsunter.omgremote;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.command.CommandExecutor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerCommandEvent;

public class CommandListener implements Listener {
	
	private static final Map<String, CommandExecutor> executors = new HashMap<>();
	
	public static void register(String command, CommandExecutor executor) {
		executors.put(command.toLowerCase(), executor);
	}
	
	@EventHandler
	public void onServerCommand(ServerCommandEvent e) {
		String[] command = e.getCommand().split(" ");
		String label = command[0].toLowerCase();
		if(executors.containsKey(label)) {
			String[] args = trim(command);
			boolean cancel = executors.get(label).onCommand(
				e.getSender(),
				null,
				label,
				args
				);
			if(cancel)
				e.setCommand(null);
		}
		
		
	}
	
	private String[] trim(String[] command) {
		String[] args = new String[command.length-1];
		for(int i = 1; i < command.length; i++)
			args[i-1] = command[i];
		return args;
	}

}
