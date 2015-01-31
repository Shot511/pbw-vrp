import helpers.Log;

import java.awt.EventQueue;

import view.MainFrame;
import controller.MainController;


public class Main 
{

	public static void main(String[] args) 
	{
		EventQueue.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				Log.init();
				MainFrame main = MainFrame.INSTANCE;
				MainController mainController = MainController.INSTANCE;
			}
		});
		
	}	
}
