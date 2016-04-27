import java.io.*;
import java.net.URL;
import javax.swing.JFileChooser;
import javax.swing.JTextField;

/**
 * Hilfsklasse zum Speichern und Laden von Dateien
 *
 */
public class FileBrowser {

	private JTextField jtfStatus;
	private JFileChooser fileChooser;
	private File file;

	public File getFile() {
		return file;
	};

	/**
	 * Konstruktor
	 * 
	 * @param jtfStatus
	 *            Textfield in das diese Klasse Statusmeldungen schreibt
	 */

	public FileBrowser(JTextField jtfStatus) {
		this.jtfStatus = jtfStatus;
	}

	/**
	 * Auswahl einer URL zum Öffnen einer Html-Seite
	 * 
	 * @param title
	 *            Titel des Fensters
	 * @param startDir
	 *            Pfad der beim Öffnen dieses Dialoges angezeigt werden soll
	 * @return ein URL-Objekt mit dem gewählten URL
	 */
	public URL chooseURL(String title, String startDir) {

		URL url = null;
		fileChooser = new JFileChooser(new File(startDir));
		int returnVal = fileChooser.showDialog(null, title);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			try {
				file = fileChooser.getSelectedFile();
				url = file.toURI().toURL();
			} catch (java.net.MalformedURLException murlex) {
				jtfStatus.setText("MalformedURLException: " + murlex.getMessage());
			}
		} else if (returnVal == JFileChooser.CANCEL_OPTION) {
			jtfStatus.setText("Keine Datei ausgewählt");
		}
		return url;
	}

	/**
	 * Auswahl einer Textdatei
	 * 
	 * @param title
	 *            Titel des Fensters
	 * @param startDir
	 *            Pfad der beim Öffnen dieses Dialoges angezeigt werden soll
	 * @return ein file-Objekt mit Referenz zur ausgewählten Datei
	 */
	public String chooseText(String title, String startDir) {
		file = null;
		fileChooser = new JFileChooser(new File(startDir));
		int returnVal = fileChooser.showDialog(null, title);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			file = fileChooser.getSelectedFile();
		} else if (returnVal == JFileChooser.CANCEL_OPTION) {
			return null;
		}
		FileReader reader = null;
		try {
			reader = new FileReader(file);
		} catch (FileNotFoundException fnfe) {
			System.out.println("FileNotFoundException: " + fnfe.getMessage());
		}
		BufferedReader bufReader = new BufferedReader(reader);
		String str;
		StringBuffer sb = new StringBuffer();
		try {
			while ((str = bufReader.readLine()) != null) {
				sb.append(str);
				sb.append(System.getProperty("line.separator"));
			}
			bufReader.close();
		} catch (IOException ioe) {
			System.out.println("IOException: " + ioe.getMessage());
		}
		return sb.toString();
	}

	/**
	 * Speichern eines Textes in eine Datei
	 * 
	 * @param file
	 *            Referenz auf die Datei in welche geschrieben werden soll
	 * @param text
	 *            Der Text der in die Datei geschrieben werden soll
	 */

	public void saveFile(File file, String text) {
		try {
			BufferedWriter bufWriter = new BufferedWriter(new FileWriter(file));
			bufWriter.write(text);
			bufWriter.flush();
			bufWriter.close();
		} catch (IOException ioe) {
			jtfStatus.setText("IOException: " + ioe.getMessage());
		}
	}

	/**
	 * Dialog zum Speichern des Textes in eine Datei
	 * 
	 * @param text
	 *            Der Text der gespeichert werden soll
	 * @param title
	 *            Der Titel den das Fenster haben soll
	 * @param startDir
	 *            Pfad der beim Öffnen dieses Dialoges angezeigt werden soll
	 * @return nach erfolgreichem Speichern wird ein file-Objekt mit Referenz
	 *         zur gespeicherten Datei zurückgegeben. Bei Fehlschlag wird null
	 *         zurückgegeben.
	 */
	public File saveFileAs(String text, String title, String startDir) {
		File file;
		fileChooser = new JFileChooser(new File(startDir));
		fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
		fileChooser.setApproveButtonText(title);
		int returnVal = fileChooser.showDialog(jtfStatus, null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			file = fileChooser.getSelectedFile();
			saveFile(file, text);
			jtfStatus.setText("Text in Datei \"" + file + "\" gespeichert");
			return file;
		} else if (returnVal == JFileChooser.CANCEL_OPTION) {
			jtfStatus.setText("Speicherdialog abgebrochen");
		}
		return null;
	}

}
