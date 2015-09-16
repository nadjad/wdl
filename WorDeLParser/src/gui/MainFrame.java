package gui;

import generated.WorDeLLexer;
import generated.WorDeLParser;
import generated.WorDeLParser.ContentContext;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

import representation.nodes.ContentNode;
import representation.nodes.FlowNode;
import scheduler.Scheduler;
import support.CustomErrorListener;
import support.ErrorRepository;
import support.xml.ConfigParser;
import support.xml.Properties;

public class MainFrame extends JFrame implements ActionListener,
		MouseWheelListener, MouseListener, ComponentListener {

	private JPanel contentPane;
	private JScrollPane scroll;
	private JButton btnProcess;
	private JTextArea textArea;
	private DrawPanel canvas;
	private int sizeX = 1250;
	private int sizeY = 730;
	private JPanel scrollHolder;
	private JTextArea errorArea;
	private JButton btnSchedule;
	private ContentNode content;
	private JMenuItem mntmNew;
	private JMenuItem mntmOpen;
	private JMenuItem mntmSave;
	private JFileChooser fileChooser = new JFileChooser();
	private Properties props;
	private String base_path;
	private JMenuItem mntmImport;
	private JMenuItem mntmExport;

	/**
	 * Create the frame.
	 */
	public MainFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(10, 10, this.sizeX, this.sizeY);
		new ConsoleFrame();
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		/*************** text area ******************/
		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel.setBounds(10, 29, 375, 375);
		contentPane.add(panel);
		panel.setLayout(null);

		textArea = new JTextArea();
		JScrollPane textHolder = new JScrollPane(textArea);
		textHolder.setBounds(0, 0, 375, 373);
		panel.add(textHolder);
		/********************************************/
		/*************** error area ******************/
		JPanel panel2 = new JPanel();
		panel2.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel2.setBounds(10, 410, 375, 240);
		contentPane.add(panel2);
		panel2.setLayout(null);

		errorArea = new JTextArea();
		JScrollPane errorAreaHolder = new JScrollPane(errorArea);
		errorAreaHolder.setBounds(0, 0, 375, 240);
		panel2.add(errorAreaHolder);
		/********************************************/

		scrollHolder = new JPanel();
		scrollHolder.setBorder(new LineBorder(new Color(0, 0, 0)));
		scrollHolder.setBounds(395, 11, 830, 670);

		canvas = new DrawPanel(10000, 10000);
		scroll = new JScrollPane(canvas,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll.getHorizontalScrollBar().setUnitIncrement(16);
		scroll.getVerticalScrollBar().setUnitIncrement(16);
		scroll.setPreferredSize(new Dimension(825, 660));

		scrollHolder.add(scroll, BorderLayout.CENTER);

		contentPane.add(scrollHolder);

		btnProcess = new JButton("Process");
		btnProcess.setBounds(296, 655, 89, 23);
		btnProcess.addActionListener(this);
		contentPane.add(btnProcess);

		btnSchedule = new JButton("Schedule");
		btnSchedule.setBounds(5, 655, 100, 23);
		btnSchedule.addActionListener(this);
		contentPane.add(btnSchedule);

		props = ConfigParser.parse("config.xml");
		base_path = props.getBasePath();
		String ss = null;
		try {
			ss = readFile(base_path + "Input.txt");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Cannot read file " + base_path
					+ "Input.txt" + "!!!!!!");
		}
		if (ss != null)
			this.textArea.append(ss);
		canvas.addMouseListener(this);
		canvas.addMouseWheelListener(this);

		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 385, 21);
		contentPane.add(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		mntmNew = new JMenuItem("New");
		mntmNew.addActionListener(this);
		mnFile.add(mntmNew);

		// import-export menu buttons
		mntmImport = new JMenuItem("Import WDL file");
		mntmImport.addActionListener(this);
		mnFile.add(mntmImport);

		mntmExport = new JMenuItem("Export WDL file");
		mntmExport.addActionListener(this);
		mnFile.add(mntmExport);

		mntmOpen = new JMenuItem("Open Project");
		mntmOpen.addActionListener(this);
		mnFile.add(mntmOpen);

		mntmSave = new JMenuItem("Save Project");
		mntmSave.addActionListener(this);
		mnFile.add(mntmSave);

		addComponentListener(this);

		setVisible(true);

	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		Object obj = arg0.getSource();
		if (obj == this.btnProcess) {
			// this.canvas.reset();
			WorDeLLexer lexer = new WorDeLLexer(new ANTLRInputStream(
					this.textArea.getText()));
			// String ss = readFile("src\\Input.txt");
			// FirstLexer lexer = new FirstLexer(new ANTLRInputStream(ss));
			// lexer.reset();
			CommonTokenStream tokens = new CommonTokenStream(lexer);

			WorDeLParser parser = new WorDeLParser(tokens);
			parser.addErrorListener(new CustomErrorListener());
			ContentContext result = parser.content();
			content = result.c;
			Map<String, FlowNode> aa = content.getFlows();
			Collection<FlowNode> rr = aa.values();
			Iterator<FlowNode> i = rr.iterator();
			canvas.setFlows(rr);

			// add error report
			errorArea.setText("");
			List<support.Error> errors = ErrorRepository.getErrorList();
			for (support.Error e : errors) {
				errorArea.append(e.toString() + "\n");
				System.out.println(e.toString());
			}
		} else if (obj == this.btnSchedule) {
			if (this.content != null && content.getSimulation() != null) {
				Scheduler sch = new Scheduler();
				sch.scheduleSimulation(content.getSimulation());
				// Executor e = new Executor();
				// e.start();
			}
		} else if (obj == mntmNew) {
			if (this.canvas != null)
				this.canvas.restart();
			this.textArea.setText("");
		} else if (obj == mntmImport) {
			this.textArea.setText("");
			fileChooser.showOpenDialog(this);
			try {
				String contents = readFile(fileChooser.getSelectedFile()
						.getAbsolutePath());
				textArea.append(contents);
				// dis.close();
			} catch (NullPointerException e) {
				JOptionPane.showMessageDialog(null, "No file selected!!!!!!!");
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Write error!!!!!!!");
				e.printStackTrace();
			}

		} else if (obj == mntmExport) {
			fileChooser.showSaveDialog(this);
			try {
				PrintWriter out = new PrintWriter(fileChooser.getSelectedFile());
				out.println(textArea.getText());
				out.close();
			} catch (NullPointerException e) {
				JOptionPane.showMessageDialog(null, "No file selected!!!!!!!");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(null, "File not found!!!!");
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(null,
						"Error reading thr file!!!!");
				e.printStackTrace();
			}
		}
		repaint();
	}

	static String readFile(String path) {
		byte[] encoded = null;
		try {
			encoded = Files.readAllBytes(Paths.get(path));
		} catch (IOException e) {
			// e.printStackTrace();

		}
		return new String(encoded);
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		int notches = e.getWheelRotation();
		if (notches < 0) {
			canvas.zoomIn();
		} else if (notches > 0) {
			canvas.zoomOut();
		}
		repaint();

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		/*
		 * canvas.zoomIn();
		 * System.out.println("zzzzzzzzzzzzzzzzzzzzzooooooooooom"); repaint();
		 */
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void componentHidden(ComponentEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void componentMoved(ComponentEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void componentResized(ComponentEvent e) {

	}

	@Override
	public void componentShown(ComponentEvent e) {
		// TODO Auto-generated method stub

	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					// UIManager.setLookAndFeel(UIManager
					// .getSystemLookAndFeelClassName());
					MainFrame frame = new MainFrame();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
