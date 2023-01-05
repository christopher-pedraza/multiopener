package multiopener;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Multiopener {
	private final static String CONFIG_FILE_NAME = "config.txt";
	private final static String ERROR_EMPTY_FILE = "Config file is empty or contains errors.";
	private final static String ERROR_NEW_FILE = "Config file was created in program directory.";
	private final static String ERROR_TITLE = "Warning";

	private static JFrame frame = new JFrame();

	public static void main(String[] args) {
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		try {
			openFiles(readConfigFile());
		} catch (NullPointerException e) {
			JOptionPane.showMessageDialog(frame, ERROR_EMPTY_FILE, ERROR_TITLE, JOptionPane.WARNING_MESSAGE);
		}

		frame.dispose();
	}

	private static void openFiles(String[] files) {
		for (String line : files) {
			try {
				File file = new File(line);
				Desktop.getDesktop().open(file);
			} catch (IllegalArgumentException | IOException e) {
				JOptionPane.showMessageDialog(frame, "Couldn't open: '" + line + "'.", ERROR_TITLE,
						JOptionPane.WARNING_MESSAGE);
			}
		}

	}

	private static String[] readConfigFile() {
		int fileSize = 0;

		try (BufferedReader br = new BufferedReader(new FileReader(CONFIG_FILE_NAME))) {
			while (br.readLine() != null) {
				fileSize++;
			}
			br.close();

			if (fileSize == 0) {
				return null;
			}
		} catch (IOException readerException) {
			try (BufferedWriter bw = new BufferedWriter(new FileWriter(CONFIG_FILE_NAME))) {
				bw.close();
				JOptionPane.showMessageDialog(frame, ERROR_NEW_FILE);
			} catch (IOException writerException) {
				return null;
			}
		}

		try (BufferedReader br = new BufferedReader(new FileReader(CONFIG_FILE_NAME))) {
			String[] files = new String[fileSize];
			int index = 0;

			String line = br.readLine();
			while (line != null) {
				if (!line.equals("")) {
					files[index] = line;
					index++;
				}
				line = br.readLine();
			}
			br.close();

			return files;
		} catch (IOException readerException) {
			return null;
		}
	}
}