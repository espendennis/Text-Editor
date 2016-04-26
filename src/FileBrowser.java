import java.io.*;
import java.net.URL;
import javax.swing.JFileChooser;
import javax.swing.JTextField;

public class FileBrowser {
	
	private JTextField jtfStatus;
	private JFileChooser fileChooser;
	private File file;
	public File getFile(){return file;};
	
	public FileBrowser(JTextField jtfStatus){
		this.jtfStatus = jtfStatus;
	}
	
	public URL chooseURL(String title, String startDir){
		
		URL url = null;
		fileChooser = new JFileChooser(new File(startDir));
		int returnVal = fileChooser.showDialog(null,  title);
		if(returnVal == JFileChooser.APPROVE_OPTION){
			try {
				file = fileChooser.getSelectedFile();
				url = file.toURI().toURL();
			} catch (java.net.MalformedURLException murlex){
				jtfStatus.setText("MalformedURLException: "+murlex.getMessage());
			}
		}else if (returnVal == JFileChooser.CANCEL_OPTION){
			jtfStatus.setText("Keine Datei ausgewählt");
		}
	return url;
	}
	public String chooseText (String title, String startDir){
		file = null;
		fileChooser = new JFileChooser(new File(startDir));
		int returnVal = fileChooser.showDialog(null, title);
		if (returnVal == JFileChooser.APPROVE_OPTION){
			file = fileChooser.getSelectedFile();
		} else if (returnVal == JFileChooser.CANCEL_OPTION){
			return null;
		}
		FileReader reader = null;
		try {
			reader = new FileReader(file);
		} catch (FileNotFoundException fnfe) {
			System.out.println("FileNotFoundException: "+ fnfe.getMessage());
		}
		BufferedReader bufReader = new BufferedReader(reader);
		String str;
		StringBuffer sb = new StringBuffer();
		try {
			while((str = bufReader.readLine()) != null){
				sb.append(str);
				sb.append(System.getProperty("line.separator"));
			}
			bufReader.close();
		} catch(IOException ioe) {
			System.out.println("IOException: "+ ioe.getMessage());
		}
		return sb.toString();
	}
	public void saveFile(File file, String text){
		try {
			BufferedWriter bufWriter = new BufferedWriter( new FileWriter(file));
			bufWriter.write(text);
			bufWriter.flush();
			bufWriter.close();
		} catch (IOException ioe) {
			jtfStatus.setText("IOException: " + ioe.getMessage());
		}
	}
	
	public File saveFileAs (String text, String title, String startDir){
		File file;
		fileChooser = new JFileChooser(new File(startDir));
		fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
		fileChooser.setApproveButtonText(title);
		int returnVal = fileChooser.showDialog(jtfStatus, null);
		if ( returnVal == JFileChooser.APPROVE_OPTION){
			file = fileChooser.getSelectedFile();
			saveFile(file, text);
			jtfStatus.setText("Text in Datei \"" + file + "\" gespeichert");
			return file;
		} else if (returnVal == JFileChooser.CANCEL_OPTION){
			jtfStatus.setText("Speicherdialog abgebrochen");
		}
		return null;
	}

}
