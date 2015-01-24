package controller;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import view.AboutFrame;
import view.MainFrame;

import com.sun.javafx.geom.Point2D;

/* Uses singleton pattern */
public class MainController 
{
	public final static MainController INSTANCE = new MainController();
	public final static boolean IS_CALCULATING = false;
	
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
				final JDialog dialog = new JDialog(mainFrame, "Child", true); 
				dialog.setSize(110, 50);
			    dialog.setLocationRelativeTo(mainFrame);
			    
			    ImageIcon loading = new ImageIcon("img/ajax-loader.gif");	
			    JLabel l = new JLabel("<html><h2>Loading...</h2></html>", loading, JLabel.CENTER);
			    l.setForeground(Color.black);
			    dialog.add(l);
			    dialog.setUndecorated(true);
			    dialog.setEnabled(false);
			    dialog.getRootPane().setOpaque(false);
			    dialog.getContentPane ().setBackground (new Color (0, 0, 0, 0));
			    dialog.setBackground (new Color (0, 0, 0, 0));
			    
			    SwingWorker<String, Void> worker = new SwingWorker<String, Void>()
			    {
					@Override
					protected String doInBackground() throws Exception
					{
						ArrayList<Point2D> nodes = mainFrame.getMapPanel().getNodes();
						if(nodes!=null) 
						{
							AlgorithmController ac = new AlgorithmController(nodes, l);
							mainFrame.getMapPanel().generateSolutionLines(ac.getBestSolution());
							return "Done";
						}
						return null;
					}
					
					@Override
					protected void done()
					{
						dialog.dispose();
					}
			    	
			    };    
			    worker.execute();
			    dialog.setVisible(true);
			}
		});
		
		mainFrame.addShowAboutFrameListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				new AboutFrame();
			}
		});
		
		mainFrame.addGeneratePointsKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) 
			{
				if(e.getKeyCode() == KeyEvent.VK_ENTER)
				{
					if(mainFrame.getNumPoints() > 0 && mainFrame.getNumPoints() < 201)
						mainFrame.getMapPanel().generateNodes(mainFrame.getNumPoints(), 2*nodeSize);
					else
						JOptionPane.showMessageDialog(mainFrame.getPanel(), "Number of points must be greater than 0 and lower than 200!");
				}
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
	}
}
