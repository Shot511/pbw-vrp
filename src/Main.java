import java.awt.EventQueue;

import controller.MainController;
import view.MainFrame;


public class Main 
{

	public static void main(String[] args) 
	{
		EventQueue.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				MainFrame main = MainFrame.INSTANCE;
				MainController mainController = MainController.INSTANCE;
			}
		});

	}

}
