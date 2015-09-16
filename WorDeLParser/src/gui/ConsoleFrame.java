package gui;

import java.awt.HeadlessException;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

public class ConsoleFrame extends JFrame {

	private JScrollPane contentPane;
	private JTextArea textArea;

	public ConsoleFrame() throws HeadlessException {
		super();
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(10, 10, 700, 600);

		textArea = new JTextArea();
		contentPane = new JScrollPane(textArea);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		// PrintStream printStream = new PrintStream(new
		// ConsoleStream(textArea));
		// System.setOut(printStream);
		// System.setErr(printStream);
		// setContentPane(contentPane);

		setVisible(true);
	}

	public void addText(String string) {
		this.textArea.append(string);
		this.repaint();
	}

	public void reset() {
		this.textArea.setText("");
		this.repaint();
	}
}