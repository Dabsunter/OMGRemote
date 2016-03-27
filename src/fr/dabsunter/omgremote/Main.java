package fr.dabsunter.omgremote;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	
	public static Process serverProcess;
	
	@Override
	public void onEnable() {
		saveDefaultConfig();
		
		getCommand("send").setExecutor(new SendExecutor());
		
		Runtime runtime = Runtime.getRuntime();
		try {
			serverProcess = runtime.exec(
					getConfig().getString("command"),
					null,
					new File(getConfig().getString("server-folder"))
					);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		new Thread() {
			public void run() {
				try {
					BufferedReader reader = new BufferedReader(new InputStreamReader(serverProcess.getInputStream()));
					String line = "";
					try {
						while((line = reader.readLine()) != null) {
							System.out.println("[Remote Server]" + line);
						}
					} finally {
						reader.close();
					}
				} catch(IOException ioe) {
					ioe.printStackTrace();
				}
			}
		}.start();

		new Thread() {
			public void run() {
				try {
					BufferedReader reader = new BufferedReader(new InputStreamReader(serverProcess.getErrorStream()));
					String line = "";
					try {
						while((line = reader.readLine()) != null) {
							System.out.println("[Remote Server]" + line);
						}
					} finally {
						reader.close();
					}
				} catch(IOException ioe) {
					ioe.printStackTrace();
				}
			}
		}.start();
	}
	
	@Override
	public void onDisable() {
		try {
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(serverProcess.getOutputStream()));
			try {
				writer.write("stop");
			} finally {
				writer.close();
			}
		} catch(IOException ioe) {
			ioe.printStackTrace();
		}
		
		try {
			serverProcess.waitFor();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
