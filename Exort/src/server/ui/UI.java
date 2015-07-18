package server.ui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;

import server.*;

public class UI {
	// Swing components.
	private JFrame frame;
	private JPanel panel;
	private JTextField textField;
	private JScrollPane scrollPane;
	private JTextArea textLog;

	public UI(String name, int width, int height, final CommandHandler commands) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		this.frame = new JFrame(name);
		this.frame.setSize(width, height);
		this.frame.setResizable(false);
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.setLocationRelativeTo(null);
		this.frame.setVisible(true);

		this.panel = new JPanel();
		this.panel.setLayout(null);

		this.frame.add(this.panel);

		this.textField = new JTextField();
		this.textField.setBounds(2, height - 43, width - 10, 13);
		this.textField.setFont(new Font("consolas", 0, 12));
		this.textField.setBorder(new LineBorder(Color.BLACK));
		this.textField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				UI.this.textLog.append('\n' + UI.this.textField.getText());
				// Only need a reference in this ActionListener, so there's no need for a
				// CommandHandler field.
				UI.this.textLog.append('\n' + commands.handleInput(UI.this.textField.getText()));

				UI.this.textField.setText("");
			}
		});
		this.panel.add(this.textField);

		this.textLog = new JTextArea();
		this.textLog.setBounds(0, 0, width, height - 43);
		this.textLog.setFont(new Font("consolas", 0, 12));
		this.textLog.setEditable(false);
		this.textLog.setBackground(new Color(235, 235, 235));

		this.scrollPane = new JScrollPane(this.textLog, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		this.scrollPane.setBounds(0, 0, width - 5, height - 43);
		this.scrollPane.setAutoscrolls(true);

		this.panel.add(this.scrollPane);

		this.frame.setVisible(true);
		this.frame.toFront();
		this.textField.requestFocusInWindow();
	}

	public void addMessage(String message) {
		this.textLog.append('\n' + message);
		this.textLog.setCaretPosition(this.textLog.getDocument().getLength());
		// scrollPane.getViewport().scrollRectToVisible(textLog.getBounds());
	}
}