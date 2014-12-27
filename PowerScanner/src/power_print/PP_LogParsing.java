package power_print;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class PP_LogParsing {
	FileInputStream fileInput;
	DataInputStream in;
	BufferedReader br;

	String logString = "";

	HashMap<String, String> map_APPLIST;
	HashMap<String, String> map_CPU;
	HashMap<String, String> map_Merge;

	ArrayList<String> key;

	protected PP_LogParsing(String filePath) {
		try {
			fileInput = new FileInputStream(filePath);
			in = new DataInputStream(fileInput);
			br = new BufferedReader(new InputStreamReader(in));
			key = new ArrayList<String>();

			String strLine;
			while ((strLine = br.readLine()) != null) {
				logString += strLine + "\n";
			}
			in.close();
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void logParsing_AppList() {
		String app_logString = logString;

		map_APPLIST = new HashMap<String, String>();
		int bp = app_logString.toString().indexOf("<APP_LIST>")
				+ "<APP_LIST>".length() + 1;
		int ep = app_logString.toString().indexOf("</APP_LIST>");
		String buf = app_logString.toString().substring(bp, ep);

		String split_logString[] = buf.split("\n");
		String split_appinfo[];
		for (int i = 0; i < split_logString.length; i++) {
			split_appinfo = split_logString[i].split(",");
			key.add(split_appinfo[0]);
			map_APPLIST.put(split_appinfo[0], split_appinfo[1]);
		}
	}

	public void logParsing_CPU() {
		String cpu_logString = logString;
		map_CPU = new HashMap<String, String>();
		int bp = cpu_logString.toString().indexOf("<CPU>") + "<CPU>".length()
				+ 1;
		int ep = cpu_logString.toString().indexOf("</CPU>");
		String buf = cpu_logString.toString().substring(bp, ep);

		String split_logString[] = buf.split("\n");
		String split_cpuinfo[];
		for (int i = 0; i < split_logString.length; i++) {
			split_cpuinfo = split_logString[i].split(",");
			map_CPU.put(split_cpuinfo[0], split_cpuinfo[1]);
		}
	}

	public HashMap<String, String> parsingMap_Merge() {
		map_Merge = new HashMap<String, String>();

		int length = key.size();
		String app, cpu;
		for (int i = 0; i < length; i++) {
			app = map_APPLIST.get(key.get(i));
			cpu = map_CPU.get(key.get(i));
			map_Merge.put(app, cpu);
		}
		return map_Merge; 
	}
}
