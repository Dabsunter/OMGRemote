package fr.dabsunter.omgremote;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import fr.dabsunter.omgremote.commands.FileCommands;

public class ProcessManager {
	
	private static final Set<ProcessManager> managers = new HashSet<>();
	private static final Map<String, ProcessBuilder> builders = new HashMap<>();
	
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
	
	public ProcessManager(String name) throws NullPointerException {
		if(builders.containsKey(name))
			init(name);
		else
			throw new NullPointerException();
	}
	
	public ProcessManager(String name, List<String> command) {
		this(name, command, FileCommands.current);
	}
	
	public ProcessManager(String name, List<String> command, File directory) {
		builders.put(name, new ProcessBuilder()
		 .command(command)
		 .directory(directory)
		 .redirectErrorStream(true));
		init(name);
	}
	
	private void init(String name) {
		this.name = name;
		
		try {
			File logs = new File("logs");
			if(!logs.exists())
				logs.mkdir();
			logs = new File(logs, "remote-" + name + ".log");
			if(!logs.exists())
				logs.createNewFile();
			this.process = builders.get(name).start();
		} catch (IOException ex) {
			System.out.println("Error while starting " + name + " process.");
			ex.printStackTrace();
			return;
		}
		
		new Thread() {
			public void run() {
				try {
					BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
					BufferedWriter writer = new BufferedWriter(new FileWriter("logs/remote-" + name + ".log"));
					String line = "";
					try {
						while((line = reader.readLine()) != null) {
							writer.write(line);
							writer.newLine();
							writer.flush();
						}
					} finally {
						reader.close();
						writer.close();
					}
				} catch(IOException ex) {
					ex.printStackTrace();
				}
				managers.remove(ProcessManager.this);
			}
		}.start();;
		
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
		} catch(IOException ex) {
			System.out.println("Error while writing \"" + command + "\" in OutputStream of " + name + " process.");
			ex.printStackTrace();
		}
	}
	
	public void stop() {
		process.destroy();
		try {
			process.waitFor(10, TimeUnit.SECONDS);
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
		if(process.isAlive())
			process.destroyForcibly();
	}

}
