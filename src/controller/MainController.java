package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import view.AboutFrame;
import view.MainFrame;

/* Uses singleton pattern */
public class MainController 
{
	public final static MainController INSTANCE = new MainController();
	
	private MainFrame mainFrame;
	
	private MainController() 
	{
		mainFrame = MainFrame.INSTANCE;
		
		int nodeSize = mainFrame.getMapPanel().getNodeSize();
		mainFrame.addGeneratePointsListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				if(mainFrame.getNumPoints() > 0 && mainFrame.getNumPoints() < 201)
					mainFrame.getMapPanel().generateNodes(mainFrame.getNumPoints(), 2*nodeSize);
				else
					JOptionPane.showMessageDialog(mainFrame.getPanel(), "Number of points must be greater than 0 and lower than 200!");
			}
		});
		
		mainFrame.addStartCalculationsListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				mainFrame.getMapPanel().generateSomeLines();
				//JOptionPane.showMessageDialog(mainFrame.getPanel(), "Starting calculations!");
			}
		});
		
		mainFrame.addShowAboutFrameListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				new AboutFrame();
			}
		});
	}
}
