package view;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.text.NumberFormat;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.text.NumberFormatter;

/* Uses singleton pattern */
public class MainFrame extends JFrame 
{
	public final static MainFrame INSTANCE = new MainFrame();
	
	private JPanel panel;
	private JPanel menuPanel;
	private MapPanel mapPanel;
	
	private JMenuItem aboutMenuItem;
	
	private JButton generatePointsBtn;
	private JButton startCalculationsBtn;
	private JLabel numPointsLabel;
	private JLabel numBoxesPerStoreLabel;
	private JLabel algOptionsLabel;
	private JFormattedTextField numPointsTF;
	private JFormattedTextField numBoxesPerStoreTF;
	
	
	private MainFrame()
	{
		super("Vehicle Routing Problem - Split Delievery - PBW 2014/2015");
		setSize(1024, 700);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		prepareUI();
		setVisible(true);
	}
	
	private void prepareUI()
	{
		/* Main Panel */
		panel = new JPanel();
		panel.setLayout(new BorderLayout());
		this.setContentPane(panel);
		
		mapPanel = new MapPanel();
		
		/* Create menu */
		JMenuBar menuBar = new JMenuBar();
		JMenu helpMenu = new JMenu("Help");
		aboutMenuItem = new JMenuItem("About");
		
		helpMenu.add(aboutMenuItem);
		menuBar.add(helpMenu);
		
		/* Menu Panel */
		menuPanel = new JPanel();
		menuPanel.setLayout(new GridBagLayout());
		
		NumberFormat format = NumberFormat.getInstance();
	    NumberFormatter formatter = new NumberFormatter(format);
	    formatter.setValueClass(Integer.class);
	    formatter.setMinimum(0);
	    formatter.setMaximum(Integer.MAX_VALUE);
	    formatter.setCommitsOnValidEdit(true);
	    
		numPointsLabel = new JLabel("Points number: ");
		numBoxesPerStoreLabel = new JLabel("Boxes per store: ");
		
		numPointsTF = new JFormattedTextField(formatter);
		numPointsTF.setColumns(10);
		
		numBoxesPerStoreTF = new JFormattedTextField(formatter);
		numBoxesPerStoreTF.setColumns(10);
		
		generatePointsBtn = new JButton("Generate!");
		algOptionsLabel = new JLabel("<html>Algorithm options:<br><br> other options...<br><br><html>");
		startCalculationsBtn = new JButton("Start calculations!");
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(20, 2, 2, 2);
		gbc.anchor = GridBagConstraints.NORTH;
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		menuPanel.add(numPointsLabel, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 0;
		menuPanel.add(numPointsTF, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 2;
		gbc.insets = new Insets(2, 2, 2, 2);
		menuPanel.add(generatePointsBtn, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.insets = new Insets(20, 2, 2, 2);
		menuPanel.add(algOptionsLabel, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.gridwidth = 1;
		gbc.insets = new Insets(2, 2, 2, 2);
		menuPanel.add(numBoxesPerStoreLabel, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 3;
		menuPanel.add(numBoxesPerStoreTF, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.weighty = 1;
		gbc.gridwidth = 2;
		menuPanel.add(startCalculationsBtn, gbc);
		/* End Menu Panel */
		
		panel.add(mapPanel, BorderLayout.CENTER);
		panel.add(menuPanel, BorderLayout.LINE_END);
		setJMenuBar(menuBar);
	}
	
	public void addGeneratePointsListener(ActionListener listener)
	{
		generatePointsBtn.addActionListener(listener);
	}
	
	public void addStartCalculationsListener(ActionListener listener)
	{
		startCalculationsBtn.addActionListener(listener);
	}
	
	public void addShowAboutFrameListener(ActionListener listener)
	{
		aboutMenuItem.addActionListener(listener);
	}
	
	public JPanel getPanel()
	{
		return panel;
	}
	
}
