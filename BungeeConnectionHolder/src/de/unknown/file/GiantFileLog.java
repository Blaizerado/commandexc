package de.unknown.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.zip.GZIPOutputStream;


public class GiantFileLog {

	private String dir;
	private String filename;
	private File file;
	private boolean ended = false;

	public enum LevelLog {
		INFO, WARNING, ERROR, DEBUG;
	}

	public GiantFileLog(String dir, String name) {
		this.dir = dir;
		filename = this.dir + "/" + name + ".lgf";
		file = new File(filename);
		if (!file.exists()) {
			File path = new File(this.dir);
			path.mkdirs();
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void log(String log, LevelLog l) {
		if (ended) {
			return;
		}
		PrintWriter writer = null;
		try {
			String prefix = "";
			String timeStamp = new SimpleDateFormat("HH:mm:ss").format(new Timestamp(System.currentTimeMillis()));
			prefix += "[" + timeStamp + "]";
			switch (l) {
			case INFO:
				prefix += "[INFO]";
				break;
			case WARNING:
				prefix += "[WARNING]";
			case ERROR:
				prefix += "[ERROR]";
				break;
			case DEBUG:
				prefix += "[DEBUG]";
				break;
			}
			String previousLog = readFile();
			writer = new PrintWriter(filename, "UTF-8");
			writer.print(previousLog);
			writer.println(prefix + log);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			writer.close();
		}
	}

	public String readFile() {
		if (ended) {
			return null;
		}
		StringBuilder previousLog = new StringBuilder();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "UTF-8"));
			String sCurrentLine;

			while ((sCurrentLine = br.readLine()) != null) {
				previousLog.append(sCurrentLine + "\n");
			}

		} catch (IOException e) {

			e.printStackTrace();

		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException ex) {

				ex.printStackTrace();

			}
		}
		return previousLog.toString();
	}

	public void endFile() {
		FileInputStream fis = null;
		FileOutputStream fos = null;
		GZIPOutputStream gzipOS = null;
		try {

			String timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(new Timestamp(System.currentTimeMillis()));
			String gzipFile = dir + "/" + timeStamp + "-0";
			int beginIndex = dir.length() + 12;
			while (new File(gzipFile + ".lgf.gz").exists()) {	
				int i = Integer.parseInt(gzipFile.substring(beginIndex));				
				i++;
				gzipFile = dir + "/" + timeStamp + "-" + i;
			}
			gzipFile += ".lgf.gz";
			fis = new FileInputStream(filename);
			fos = new FileOutputStream(gzipFile);
			gzipOS = new GZIPOutputStream(fos);
			byte[] buffer = new byte[1024];
			int len;
			while ((len = fis.read(buffer)) != -1) {
				gzipOS.write(buffer, 0, len);
			}
			gzipOS.flush();
			gzipOS.finish();
			gzipOS.close();
			fis.close();
			fos.close();
			file.delete();
			ended = true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				gzipOS.finish();
				gzipOS.close();
				fis.close();
				fos.close();				
			} catch (IOException e) {
				System.err.println("A very big Error ocurred");
				e.printStackTrace();
			}

		}
	}

}
