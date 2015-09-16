package support;

import java.io.IOException;
import java.io.OutputStream;

import javax.swing.JTextArea;

public class ConsoleStream extends OutputStream {
	private JTextArea ta;

	public ConsoleStream(JTextArea ta) {
		this.ta = ta;
	}

	@Override
	public void write(int b) throws IOException {
		ta.append(String.valueOf((char) b));
	}

}
