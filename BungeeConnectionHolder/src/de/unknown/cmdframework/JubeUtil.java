package de.unknown.cmdframework;

public class JubeUtil {

	private JubeUtil() {
	}

	public static boolean hasArrayString(String s, String[] args) {
		if (s == null || args == null)
			return false;
		s = s.toLowerCase();
		for (String arg : args) {
			if (s.equals(arg.toLowerCase()))
				return true;
		}
		return false;
	}

	public static boolean startsArrayWith(String s, String[] args) {
		if (s == null || args == null)
			return false;
		s = s.toLowerCase();
		for (String arg : args) {
			if (s.startsWith(arg.toLowerCase()))
				return true;
		}
		return false;
	}
}
