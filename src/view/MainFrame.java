package view;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
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
	
	private JLabel algOptionsLabel;
	private JLabel numPointsLabel;
	private JLabel numBoxesPerStoreLabel;
	private JLabel hourStartLabel;
	private JLabel hourEndLabel;
	private JLabel serviceTimeLabel;
	private JLabel truckCapacityLabel;
	private JLabel statisticsLabel;
	private JLabel stat1Label;
	private JLabel stat2Label;
	private JLabel stat3Label;
	private JLabel stat4Label;
	
	private JFormattedTextField numPointsTF;
	private JFormattedTextField numBoxesPerStoreTF;
	private JFormattedTextField hourStartTF;
	private JFormattedTextField hourEndTF;
	private JFormattedTextField serviceTimeTF;
	private JFormattedTextField truckCapacityTF;
	
	
	private MainFrame()
	{
		super("Vehicle Routing Problem - Split Delievery - PBW 2014/2015");
		setSize(804, 700);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		prepareUI();
		setVisible(true);
		setResizable(false);
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
		/* Range = [0, MAX_INT] */
	    NumberFormatter formatter0_MX_INT = new NumberFormatter(format);
	    formatter0_MX_INT.setValueClass(Integer.class);
	    formatter0_MX_INT.setMinimum(0);
	    formatter0_MX_INT.setMaximum(Integer.MAX_VALUE);
	    formatter0_MX_INT.setCommitsOnValidEdit(true);
	    
	    /* Range = [MIN_INT, MAX_INT] */
	    NumberFormatter formatterMIN_MX_INT = new NumberFormatter(format);
	    formatterMIN_MX_INT.setValueClass(Integer.class);
	    formatterMIN_MX_INT.setMinimum(Integer.MIN_VALUE);
	    formatterMIN_MX_INT.setMaximum(Integer.MAX_VALUE);
	    formatterMIN_MX_INT.setCommitsOnValidEdit(true);
	    
	    algOptionsLabel = new JLabel("<html><center><h3><u>Algorithm options</u></h3></center><html>");
		numPointsLabel = new JLabel("Points number: ");
		numBoxesPerStoreLabel = new JLabel("Boxes per store: ");
		hourStartLabel = new JLabel("Start hour: ");
		hourEndLabel = new JLabel("Start end: ");
		serviceTimeLabel = new JLabel("Service time (min): ");
		truckCapacityLabel = new JLabel("Truck capacity: ");
		statisticsLabel = new JLabel("<html><center><h3><u>Statistics</u></h3></center><html>");
		stat1Label = new JLabel("Stat1: ");;
		stat2Label = new JLabel("Stat2: ");;
		stat3Label = new JLabel("Stat3: ");;
		stat4Label = new JLabel("Stat4: ");;
		
		numPointsTF = new JFormattedTextField(formatter0_MX_INT);
		numPointsTF.setColumns(5);
		numPointsTF.setValue(30);
		
		numBoxesPerStoreTF = new JFormattedTextField(formatter0_MX_INT);
		numBoxesPerStoreTF.setColumns(5);
		numBoxesPerStoreTF.setValue(15);
		
		hourStartTF = new JFormattedTextField(formatterMIN_MX_INT);
		hourStartTF.setColumns(5);
		hourStartTF.setValue(-60);
		
		hourEndTF = new JFormattedTextField(formatterMIN_MX_INT);
		hourEndTF.setColumns(5);
		hourEndTF.setValue(6*60);
		
		serviceTimeTF = new JFormattedTextField(formatterMIN_MX_INT);
		serviceTimeTF.setColumns(5);
		serviceTimeTF.setValue(4);
		
		truckCapacityTF = new JFormattedTextField(formatter0_MX_INT);
		truckCapacityTF.setColumns(5);
		truckCapacityTF.setValue(54);
		
		generatePointsBtn = new JButton("Generate!");
		startCalculationsBtn = new JButton("Start calculations!");
		
		/* Layout setup */ 
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.NORTH;
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		menuPanel.add(algOptionsLabel, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		menuPanel.add(numPointsLabel, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.insets = new Insets(2, 2, 2, 10);
		menuPanel.add(numPointsTF, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 2;
		gbc.insets = new Insets(2, 2, 20, 10);
		menuPanel.add(generatePointsBtn, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.gridwidth = 1;
		gbc.insets = new Insets(2, 2, 2, 10);
		menuPanel.add(numBoxesPerStoreLabel, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 3;
		menuPanel.add(numBoxesPerStoreTF, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 4;
		menuPanel.add(hourStartLabel, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 4;
		menuPanel.add(hourStartTF, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 5;
		menuPanel.add(hourEndLabel, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 5;
		menuPanel.add(hourEndTF, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 6;
		menuPanel.add(serviceTimeLabel, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 6;
		menuPanel.add(serviceTimeTF, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 7;
		menuPanel.add(truckCapacityLabel, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 7;
		menuPanel.add(truckCapacityTF, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 8;
		gbc.gridwidth = 2;
		menuPanel.add(startCalculationsBtn, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 9;
		gbc.insets = new Insets(10, 2, 2, 10);
		menuPanel.add(statisticsLabel, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 10;
		gbc.insets = new Insets(2, 2, 2, 10);
		menuPanel.add(stat1Label, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 11;
		menuPanel.add(stat2Label, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 12;
		menuPanel.add(stat3Label, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 13;
		gbc.weighty = 1;
		menuPanel.add(stat4Label, gbc);
		/* End Menu Panel */
		
		panel.add(mapPanel, BorderLayout.CENTER);
		panel.add(menuPanel, BorderLayout.LINE_END);
		setJMenuBar(menuBar);
	}
	
	public void addGeneratePointsListener(ActionListener listener)
	{
		generatePointsBtn.addActionListener(listener);
	}
	
	public void addGeneratePointsKeyListener(KeyListener listener)
	{
		numPointsTF.addKeyListener(listener);
		generatePointsBtn.addKeyListener(listener);
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
	
	public MapPanel getMapPanel()
	{
		return mapPanel;
	}
	
	public int getNumPoints() 
	{
		int numPoints = 0;
		
		if(numPointsTF.isEditValid())
		{
			numPoints = (int)numPointsTF.getValue();
		}
		
		return numPoints;
	}
	
	public int getHourStart()
	{
		int retVal = 0;
		
		if(hourStartTF.isEditValid())
		{
			retVal = (int)hourStartTF.getValue();
		}
		
		return retVal;
	}
	
	public int getHourEnd()
	{
		int retVal = 0;
		
		if(hourEndTF.isEditValid())
		{
			retVal = (int)hourEndTF.getValue();
		}
		
		return retVal;
	}
	
	public int getBoxesPerStore()
	{
		int retVal = 0;
		
		if(numBoxesPerStoreTF.isEditValid())
		{
			retVal = (int)numBoxesPerStoreTF.getValue();
		}
		
		return retVal;
	}
	
	public int getServiceTime()
	{
		int retVal = 0;
		
		if(serviceTimeTF.isEditValid())
		{
			retVal = (int)serviceTimeTF.getValue();
		}
		
		return retVal;
	}
	
	public int getTruckCapacity()
	{
		int retVal = 0;
		
		if(truckCapacityTF.isEditValid())
		{
			retVal = (int)truckCapacityTF.getValue();
		}
		
		return retVal;
	}
	
	public void setStatsText(String stat1, String stat2, String stat3, String stat4)
	{
		stat1Label.setText("Stat1: " + stat1);
		stat2Label.setText("Stat2: " + stat2);
		stat3Label.setText("Stat3: " + stat3);
		stat4Label.setText("Stat4: " + stat4);
	}
	
}
