package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import com.sun.javafx.geom.Point2D;

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
		
		final int nodeSize = mainFrame.getMapPanel().getNodeSize();
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
				ArrayList<Point2D> nodes = mainFrame.getMapPanel().getNodes();
				if(nodes!=null) {
					AlgorithmController ac = new AlgorithmController(nodes);
					//mainFrame.getMapPanel().generateSomeLines();
					mainFrame.getMapPanel().generateSolutionLines(ac.getBestSolution());
				}
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
