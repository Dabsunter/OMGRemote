package fr.dabsunter.omgremote.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileManager {
	
	public static void copy(File source, File destination) {
		if(source.isDirectory()) {
			if(!destination.exists())
				destination.mkdirs();
			for(String children : source.list())
				copy(new File(source, children), new File(destination, children));
		} else {
			try {
				InputStream in = new FileInputStream(source);
				OutputStream out = new FileOutputStream(destination);
			
				byte[] buf = new byte[1024];
				int len;
				while((len = in.read(buf)) > 0)
					out.write(buf, 0, len);
				in.close();
				out.close();
			} catch(IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	public static void delete(File file){
		if(file.isDirectory()) {
			String files[] = file.list();
			if(files.length == 0){
				file.delete();
			} else {
				for(String child : files){
					delete(new File(file, child));
				}
				delete(file);
			}
		} else {
			file.delete();
		}
	}
	
	public static void move(File source, File destination) {
		copy(source, destination);
		delete(source);
	}

}
