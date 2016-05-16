package fr.dabsunter.omgremote.commands;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import fr.dabsunter.omgremote.util.FileManager;
import fr.dabsunter.omgremote.util.Tools;

public class FileCommands implements CommandExecutor {
	
	public static File current = new File(".");

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		try {
			switch(label) {
			case "cd":
				current = new File(current, Tools.join(args));
				sender.sendMessage("Moved to " + current.getCanonicalPath());
				break;
			case "ls":
				String msg = "Files in " + current.getCanonicalPath();
				for(File file : current.listFiles()) {
					msg += "\n" + file.getName();
					if(file.isDirectory())
						msg += "/";
				}
				sender.sendMessage(msg);
				break;
			case "cp":
				List<String> copy = Tools.parseCommand(Tools.join(args));
				if(copy.size() != 2) {
					sender.sendMessage("Specify a source and a destination");
				} else {
					File from = new File(current, copy.get(0));
					File to = new File(current, copy.get(1));
					FileManager.copy(from, to);
					sender.sendMessage("Copied " + from.getCanonicalPath() + " to " + to.getCanonicalPath());
				}
				break;
			case "mv":
				List<String> move = Tools.parseCommand(Tools.join(args));
				if(move.size() != 2) {
					sender.sendMessage("Specify a source and a destination");
				} else {
					File from = new File(current, move.get(0));
					File to = new File(current, move.get(1));
					FileManager.move(from, to);
					sender.sendMessage("Moved " + from.getCanonicalPath() + " to " + to.getCanonicalPath());
				}
				break;
			case "rm":
				File delete = new File(current, Tools.join(args));
				FileManager.delete(delete);
				sender.sendMessage("Deleted " + delete.getCanonicalPath() + " !");
				break;
			}
		} catch(IOException ex) {
			ex.printStackTrace();
		}
		return false;
	}

}
