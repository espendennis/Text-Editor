import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.JEditorPane;
import java.net.URL;
import java.io.*;

public class SwingFrame extends JFrame implements ActionListener {

	private JTextField jtfStatus;
	private JMenuBar menuBar;
	private JMenu menuFile;
	private JMenu menuOpen;
	private JMenu menuHelp;
	private JMenuItem jmiNew;
	private JMenuItem jmiClose;
	private JMenuItem jmiSave;
	private JMenuItem jmiSaveAs;
	private JMenuItem jmiOpenFile;
	private JMenuItem jmiOpenWeb;
	private JMenuItem jmiHelp;
	private JMenuItem jmiAbout;
	private Action actNewFile, actOpenFile, actOpenWeb, actSave, actHelp, actAbout;
	private final String TTT_NEW = "Erstelle eine neue Datei";
	private final String TTT_OPEN = "Datei Öffnen";
	private final String TTT_WEB = "Webseite öffnen";
	private final String TTT_SAVE = "Speichern";
	private final String TTT_HELP = "Die Hilfe aufrufen";
	private final String TTT_ABOUT = "Die Info aufrufen";
	private KeyStroke keyStroke;
	private JEditorPane editorPane;
	private FileBrowser fileBrowser;
	private File file = null;

	public SwingFrame() {
		super("Swing-Frame");
		this.setSize(600, 450);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container contentPane = this.getContentPane();

		createMenu();

		jtfStatus = new JTextField();
		jtfStatus.setBackground(Color.BLACK);
		jtfStatus.setForeground(Color.WHITE);
		jtfStatus.setEditable(false);

		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(BorderLayout.SOUTH, jtfStatus);
		editorPane = new JEditorPane();
		JScrollPane editorSP = new JScrollPane(editorPane);
		panel.add(BorderLayout.CENTER, editorSP);
		contentPane.add(BorderLayout.CENTER, panel);
		contentPane.add(BorderLayout.NORTH, this.getToolBar());
		this.setVisible(true);
		fileBrowser = new FileBrowser(jtfStatus);
	}

	private void createMenu() {

		// MenuBar
		menuBar = new JMenuBar();
		this.setJMenuBar(menuBar);

		// Datei Menü
		menuFile = new JMenu("Datei");
		menuFile.setMnemonic('d');
		menuBar.add(menuFile);

		// Neu...
		actNewFile = new AbstractAction("Neu...", new ImageIcon("icons/toolbarButtonGraphics/general/New24.gif")) {
			public void actionPerformed(ActionEvent ae) {
				file = null;
				editorPane.setContentType("text/plain");
				jtfStatus.setText("");
			}
		};
		JMenuItem jmiNew = menuFile.add(actNewFile);
		actNewFile.putValue(Action.SHORT_DESCRIPTION, TTT_NEW);
		keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK);
		actNewFile.putValue(Action.ACCELERATOR_KEY, keyStroke);
		jmiNew.setMnemonic('n');

		// Untermenü Öffnen
		menuOpen = new JMenu("Öffnen");
		menuFile.setMnemonic('f');
		menuFile.add(menuOpen);

		// Schließen
		jmiClose = new JMenuItem("Schließen");
		menuFile.add(jmiClose);
		jmiClose.setMnemonic('s');
		jmiClose.addActionListener(this);
		jmiClose.setToolTipText("Das Programm Schließen");

		// Separator
		menuFile.addSeparator();

		// Speichern...
		actSave = new AbstractAction("Speichern", new ImageIcon("icons/toolbarButtonGraphics/general/Save24.gif")) {
			public void actionPerformed(ActionEvent ae) {
				if (file != null) {
					jtfStatus.setText("\"Speichern\" wurde geklickt");
					save();
				} else {
					jtfStatus.setText("Umgeleitet auf: \"Datei - Speichern als...\"");
					saveAs();
				}
			}
		};

		jmiSave = menuFile.add(actSave);
		actSave.putValue(Action.SHORT_DESCRIPTION, TTT_SAVE);
		keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK);
		actSave.putValue(Action.ACCELERATOR_KEY, keyStroke);
		jmiSave.setMnemonic('p');

		// Speichern als...
		jmiSaveAs = new JMenuItem("Speichern als...");
		jmiSaveAs.setMnemonic('a');
		jmiSaveAs.addActionListener(this);
		menuFile.add(jmiSaveAs);
		jmiSaveAs.setToolTipText("Speichern als");

		// Dateisystem
		actOpenFile = new AbstractAction("Dateisystem...",
				new ImageIcon("icons/toolbarButtonGraphics/general/Open24.gif")) {
			public void actionPerformed(ActionEvent ae) {
				String str = fileBrowser.chooseText("Datei wählen", "./");
				file = fileBrowser.getFile();
				editorPane.setContentType("text/plain");
				if (str != null) {
					editorPane.setText(str);
					jtfStatus.setText("\"Datei - Öffnen - Dateisystem...\" gewählt.");
				} else {
					jtfStatus.setText("Keine Datei ausgewählt!");
				}
			}
		};
		jmiOpenFile = menuOpen.add(actOpenFile);
		actOpenFile.putValue(Action.SHORT_DESCRIPTION, TTT_OPEN);
		keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK);
		actOpenFile.putValue(Action.ACCELERATOR_KEY, keyStroke);
		jmiOpenFile.setMnemonic('e');

		// Web
		actOpenWeb = new AbstractAction("Web...",
				new ImageIcon("icons/toolbarButtonGraphics/development/Webcomponent24.gif")) {
			public void actionPerformed(ActionEvent ae) {
				URL url = fileBrowser.chooseURL("HTML-Seite wählen", "./");
				if (url != null) {
					try {
						editorPane.setPage(url);
						file = fileBrowser.getFile();
						jtfStatus.setText("Datei \"" + url.getPath() + "\"geladen");
					} catch (IOException ioe) {
						jtfStatus.setText(ioe.toString());
					}

				}
			}
		};
		jmiOpenWeb = menuOpen.add(actOpenWeb);
		actOpenWeb.putValue(Action.SHORT_DESCRIPTION, TTT_WEB);
		keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_MASK);
		actOpenWeb.putValue(Action.ACCELERATOR_KEY, keyStroke);
		jmiOpenWeb.setMnemonic('w');

		// Hilfe Menü
		menuHelp = new JMenu("?");
		menuBar.add(menuHelp);

		// Help
		actHelp = new AbstractAction("Hilfe", new ImageIcon("icons/toolbarButtonGraphics/general/Help24.gif")) {
			public void actionPerformed(ActionEvent ae) {
				jtfStatus.setText("\"Hilfe\" wurde geklickt");
			}
		};
		jmiHelp = menuHelp.add(actHelp);
		actHelp.putValue(Action.SHORT_DESCRIPTION, TTT_HELP);
		keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0);
		actHelp.putValue(Action.ACCELERATOR_KEY, keyStroke);
		jmiHelp.setMnemonic('h');

		// Separator
		menuHelp.addSeparator();

		// Info
		actAbout = new AbstractAction("Info", new ImageIcon("icons/toolbarButtonGraphics/general/About24.gif")) {
			public void actionPerformed(ActionEvent ae) {
				jtfStatus.setText("\"Info\" wurde geklickt");
			}
		};
		jmiAbout = menuHelp.add(actAbout);
		actAbout.putValue(Action.SHORT_DESCRIPTION, TTT_ABOUT);
		keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_I, InputEvent.CTRL_MASK);
		actAbout.putValue(Action.ACCELERATOR_KEY, keyStroke);
		jmiAbout.setMnemonic('o');

	}

	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource().equals(jmiClose))
			System.exit(0);
		else if (ae.getSource().equals(jmiSaveAs)) {
			// jtfStatus.setText("\"Datei - Speichern als ...\" gewählt");
			this.saveAs();
		}
	}

	private JToolBar getToolBar() {
		JToolBar toolBar = new JToolBar();
		toolBar.add(actNewFile);
		toolBar.add(actOpenFile);
		toolBar.add(actOpenWeb);
		toolBar.add(actSave);
		toolBar.addSeparator();
		toolBar.add(actHelp);
		toolBar.add(actAbout);

		return toolBar;
	}

	public static void main(String[] args) {
		new SwingFrame();

	}

	private void save() {
		fileBrowser.saveFile(file, editorPane.getText());
		jtfStatus.setText("Datei \"" + file.getAbsolutePath() + "\" gespeichert");
	}

	private void saveAs() {
		file = fileBrowser.saveFileAs(editorPane.getText(), "Speichern", ".");
		if (file != null) {
			this.save();
		} else {
			jtfStatus.setText("Speichern abgebrochen");
		}

	}
}
