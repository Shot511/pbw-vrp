package helpers;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;

public class SimplerFormatter extends SimpleFormatter {

	public String format(LogRecord record){
		if(record.getLevel() == Level.INFO){
			return record.getMessage() + "\r\n";
		} else {
			return super.format(record);
		}
	}
}
