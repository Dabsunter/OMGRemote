package fr.dabsunter.omgremote.util;

import java.util.ArrayList;
import java.util.List;

public class Tools {
	
	public static String join(String[] args) {
		String result = "";
		for(String arg : args)
			result += " " + arg;
		return result.substring(1);
	}

	public static List<String> parseCommand(String command) {
		List<String> list = new ArrayList<>();
		StringBuilder sb = new StringBuilder();
		boolean betweenQuote = false;
		for(int i = 0; i < command.length(); i++) {
			if(command.charAt(i) == '"') {
				betweenQuote = !betweenQuote;
			} else if(command.charAt(i) == ' ' && !betweenQuote) {
				list.add(sb.toString());
				sb = new StringBuilder();
			} else {
				sb.append(command.charAt(i));
			}
		}
		list.add(sb.toString());
		return list;
	}

}
