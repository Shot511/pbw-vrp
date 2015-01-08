package view;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class AboutFrame extends JFrame 
{
	public AboutFrame() 
	{
		super("About...");
		setSize(300, 200);
		setLocation(300, 200);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		JLabel about = new JLabel("<html><center>"
				+ "				  <h2>About authors</h2>"
				+ "				  Magdalena Michalik - <i>Programming</i><br>"
				+ "               Tomasz Ga≥aj - <i>Programming</i><br>"
				+ "               Micha≥ Michalski - <i>Research & Report</i><br>"
				//+ "               Jakub Matusiak - <br>"
				+ "				  Szymon Niedüwiedzki - <i>Research & Report</i>"
				+ "				  </center></html>");	
		about.setHorizontalAlignment(JLabel.CENTER);
		
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		
		panel.add(about, BorderLayout.CENTER);
		
		setContentPane(panel);
		
		setVisible(true);
	}
}
