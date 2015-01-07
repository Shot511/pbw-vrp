package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import view.MainFrame;

/* Uses singleton pattern */
public class MainController 
{
	public final static MainController INSTANCE = new MainController();
	
	private MainFrame mainFrame;
	
	private MainController() 
	{
		mainFrame = MainFrame.INSTANCE;
		
		mainFrame.addGeneratePointsListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				JOptionPane.showMessageDialog(mainFrame.getPanel(), "Generating points!");
			}
		});
		
		mainFrame.addStartCalculationsListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				JOptionPane.showMessageDialog(mainFrame.getPanel(), "Starting calculations!");
			}
		});
		
		mainFrame.addShowAboutFrameListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JFrame frame = new JFrame("About");
				frame.setSize(200, 200);
				frame.setVisible(true);
				
			}
		});
	}
}
