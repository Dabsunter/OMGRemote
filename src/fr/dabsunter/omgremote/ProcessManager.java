package fr.dabsunter.omgremote;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import fr.dabsunter.omgremote.commands.FileCommands;

public class ProcessManager {
	
	private static final Set<ProcessManager> managers = new HashSet<>();
	
	public static ProcessManager get(String name) {
		for(ProcessManager manager : managers)
			if(manager.name.equals(name))
				return manager;
		return null;
	}
	
	public static void stopAll() {
		for(ProcessManager manager : managers)
			manager.stop();
	}
	
	
	private String name;
	private Process process;
	
	public ProcessManager(String name, List<String> command) {
		this(name, command, FileCommands.current);
	}
	
	public ProcessManager(String name, List<String> command, File directory) {
		this.name = name;
		
		File logs = new File("logs/remote-" + name + ".log");
		try {
			process = new ProcessBuilder()
			 .command(command)
			 .directory(directory)
			 .redirectInput(logs)
			 .redirectError(logs)
			 .start();
		} catch (IOException e) {
			System.out.println("Error while starting " + name + " process.");
			e.printStackTrace();
			return;
		}
		
		managers.add(this);
	}
	
	public void write(String command) {
		try {
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
			try {
				writer.write(command);
				writer.newLine();
			} finally {
				writer.flush();
			}
		} catch(IOException ioe) {
			System.out.println("Error while writing \"" + command + "\" in OutputStream of " + name + " process.");
			ioe.printStackTrace();
		}
	}
	
	public void stop() {
		process.destroy();
		try {
			process.waitFor(10, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if(process.isAlive())
			process.destroyForcibly();
		managers.remove(this);
	}

}
