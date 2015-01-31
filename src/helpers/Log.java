package helpers;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/***
 * Generates statistics after calculations
 * @author tomas_000
 *
 */
public class Log 
{
	private final static Logger LOGGER_A = Logger.getLogger("algorithm");
	private final static Logger LOGGER_R = Logger.getLogger("routes");
	private static FileHandler fhA = null;
	private static FileHandler fhR = null;
	
	public static void init() {
		try {
			String filePathA = "log/algorithmLog.log";
			String filePathR = "log/routesDescriptionLog.log";
			(new File(filePathA)).createNewFile();
			fhA=new FileHandler(filePathA, false);
			(new File(filePathR)).createNewFile();
			fhR=new FileHandler(filePathR, false);
		} catch (SecurityException | IOException e) {
			e.printStackTrace();
		}
		//LOGGER = Logger.getLogger("");
		fhA.setFormatter(new SimplerFormatter());
		fhR.setFormatter(new SimplerFormatter());
		LOGGER_A.addHandler(fhA);
		LOGGER_A.setLevel(Level.CONFIG);
		LOGGER_R.addHandler(fhR);
		LOGGER_R.setLevel(Level.CONFIG);
	}

}
