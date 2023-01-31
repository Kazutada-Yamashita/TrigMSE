package tmse.gui;

import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.*;
import javax.swing.*;

import tmse.core.Solver;

@SuppressWarnings("serial")
public final class Master extends JFrame
{
	private final JLabel labelInfo = new JLabel("");
	private final JLabel labelFrequency = new JLabel("Frequency: ");
	private final JLabel labelAmplitude = new JLabel("Amplitude: ");
	private final JLabel labelPhase = new JLabel("Phase: ");
	private final JLabel labelOffset = new JLabel("Offset: ");
	private final JLabel labelData = new JLabel("Data");
	private final JLabel labelSeparator = new JLabel("Separator: ");
	private final JLabel labelLine = new JLabel("Line: ");
	private final JTextField fieldFrequency = new JTextField();
	private final JTextField fieldAmplitude = new JTextField();
	private final JTextField fieldPhase = new JTextField();
	private final JTextField fieldOffset = new JTextField();
	private final JTextArea areaValue = new JTextArea();
	private final JRadioButton radioComma = new JRadioButton("Comma");
	private final JRadioButton radioTab = new JRadioButton("Tab");
	private final JRadioButton radioLF = new JRadioButton("LF");
	private final JRadioButton radioCRLF = new JRadioButton("CRLF");
	private final JButton buttonFire = new JButton("Calculate");
	private final JButton buttonFrequency = new JButton("Copy");
	private final JButton buttonPhase = new JButton("Copy");
	private final JButton buttonOffset = new JButton("Copy");

	public Master()
	{
		super("Trigonometric MSE");

		this.initField();
		this.initListener();
		this.initPane();

		super.setSize(400,400);
		super.setResizable(true);
	}

	private void initField()
	{
		this.buttonFrequency.setActionCommand("amplitude");
		this.buttonPhase.setActionCommand("phase");
		this.buttonOffset.setActionCommand("offset");

		ButtonGroup groupSeparator = new ButtonGroup();
		groupSeparator.add(this.radioComma);
		groupSeparator.add(this.radioTab);

		ButtonGroup groupLine = new ButtonGroup();
		groupLine.add(this.radioLF);
		groupLine.add(this.radioCRLF);

		this.radioTab.setSelected(true);
		this.radioLF.setSelected(true);
	}

	private void initListener()
	{
		CopyListener listenerCopy = new CopyListener();
		this.buttonFrequency.addActionListener(listenerCopy);
		this.buttonPhase.addActionListener(listenerCopy);
		this.buttonOffset.addActionListener(listenerCopy);

		FuncListener listenerFunc = new FuncListener();
		this.buttonFire.addActionListener(listenerFunc);
		super.addWindowListener(listenerFunc);
	}

	private void initPane()
	{
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = gbc.gridy = 0;
		gbc.gridwidth = gbc.gridheight = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(3,3,3,3);

		GridBagLayout layoutNorth = new GridBagLayout();
		JPanel paneNorth = new JPanel(layoutNorth);

		gbc.gridx = 0;
		gbc.weightx = 0.1d;
		layoutNorth.setConstraints(this.labelFrequency,gbc);
		paneNorth.add(this.labelFrequency);

		gbc.gridwidth = 2;
		gbc.gridx = 1;
		gbc.weightx = 0.9d;
		layoutNorth.setConstraints(this.fieldFrequency, gbc);
		paneNorth.add(this.fieldFrequency);

		JComponent[] arrayComp = new JComponent[] {
				this.labelSeparator,this.radioComma,this.radioTab,
				this.labelLine,this.radioLF,this.radioCRLF,
		};

		gbc.gridwidth = 1;
		for(int i=0; i<6; i++) {
			gbc.gridx = i % 3;
			gbc.gridy = 1 + i / 3;
			gbc.weightx = gbc.gridx == 0 ? 0.0d : 0.5d;
			layoutNorth.setConstraints(arrayComp[i],gbc);
			paneNorth.add(arrayComp[i]);
		}

		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.gridwidth = 3;
		gbc.weightx = 0.0d;
		layoutNorth.setConstraints(this.buttonFire,gbc);
		paneNorth.add(this.buttonFire);

		gbc.gridy = 4;
		layoutNorth.setConstraints(this.labelInfo,gbc);
		paneNorth.add(this.labelInfo);

		GridBagLayout layoutCenter = new GridBagLayout();
		JPanel paneCenter = new JPanel(layoutCenter);

		gbc.weightx = 1.0d;
		gbc.gridwidth = 1;
		gbc.gridx = 0;

		gbc.gridy = 0;
		layoutCenter.setConstraints(this.labelData,gbc);
		paneCenter.add(this.labelData);

		JScrollPane scroll = new JScrollPane(this.areaValue);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		gbc.gridy = 1;
		gbc.weighty = 1.0d;
		layoutCenter.setConstraints(scroll,gbc);
		paneCenter.add(scroll);

		GridBagLayout layoutSouth = new GridBagLayout();
		JPanel paneSouth = new JPanel(layoutSouth);

		JComponent[] arrayComp3 = new JComponent[] {
				this.labelAmplitude,this.fieldAmplitude,this.buttonFrequency,
				this.labelPhase,this.fieldPhase,this.buttonPhase,
				this.labelOffset,this.fieldOffset,this.buttonOffset,
		};
		for(int i=0; i<arrayComp3.length; i++) {
			gbc.gridx = i % 3;
			gbc.gridy = i / 3;
			gbc.weightx = gbc.gridx == 1 ? 0.9d : 0.1d;
			layoutSouth.setConstraints(arrayComp3[i],gbc);
			paneSouth.add(arrayComp3[i]);
		}

		super.add(paneNorth,BorderLayout.NORTH);
		super.add(paneCenter,BorderLayout.CENTER);
		super.add(paneSouth,BorderLayout.SOUTH);
	}

	class FuncListener extends WindowAdapter implements ActionListener
	{
		@Override
		public void windowClosing(WindowEvent e)
		{
			System.exit(0);
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{
			try {
				double freq = Double.parseDouble(fieldFrequency.getText());

				String linediv = null;
				if(radioLF.isSelected()) linediv = "\n";
				else if(radioCRLF.isSelected()) linediv = "\r\n";
				else linediv = "";

				String sep = null;
				if(radioComma.isSelected()) sep = ",";
				else if(radioTab.isSelected()) sep = "\t";
				else sep = "";

				String[] lines = areaValue.getText().split(linediv);
				int size = lines.length;

				String[] split = null;

				double[] t = new double[size];
				double[] x = new double[size];
				for(int i=0; i<size; i++) {
					split = lines[i].split(sep);
					t[i] = Double.parseDouble(split[0]);
					x[i] = Double.parseDouble(split[1]);
				}
				double[] sol = Solver.calc(size,freq,t,x);
				fieldAmplitude.setText(String.valueOf(sol[0]));
				fieldPhase.setText(String.valueOf(sol[1]));
				fieldOffset.setText(String.valueOf(sol[2]));
				labelInfo.setText("");
			}
			catch(RuntimeException re) {
				labelInfo.setText("Something went wrong.");
			}
		}
	}

	class CopyListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			Toolkit kit = Toolkit.getDefaultToolkit();
			Clipboard cb = kit.getSystemClipboard();
			StringSelection ss = null;

			String command = e.getActionCommand();
			switch(command) {
			case "amplitude":
				ss = new StringSelection(fieldAmplitude.getText());
				break;
			case "phase":
				ss = new StringSelection(fieldPhase.getText());
				break;
			case "offset":
				ss = new StringSelection(fieldOffset.getText());
				break;
			default:
				ss = new StringSelection("");
				break;
			}

			cb.setContents(ss,ss);
		}
	}
}
