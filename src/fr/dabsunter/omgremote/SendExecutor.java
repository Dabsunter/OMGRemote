package fr.dabsunter.omgremote;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SendExecutor implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		switch(args.length) {
		case 0:
			return false;
		default:
			try {
				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(Main.serverProcess.getOutputStream()));
				try {
					writer.write(join(args));
					writer.newLine();
				} finally {
					writer.flush();
				}
			} catch(IOException ioe) {
				ioe.printStackTrace();
			}
			return true;
		}
	}
	
	private String join(String[] args) {
		String result = "";
		for(String arg : args)
			result += " " + arg;
		return result.substring(1);
	}

}
