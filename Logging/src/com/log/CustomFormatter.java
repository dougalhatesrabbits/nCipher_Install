package com.log;

import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class CustomFormatter extends Formatter {

    private static final String format = "%1$tF %1$tT [%3$s] [%2$-46s] [%4$-42s] [%5$s] %6s%n";
    //private static final String format = "%1$tc %2$s [%4$s] %5$s %n";

    public String format(LogRecord record){
            return String.format(format,
                    record.getMillis(),
                    record.getLoggerName() + " " + record.getSourceClassName() + " " + record.getSourceMethodName(),
                    record.getLevel(),
                    record.getMessage(),
                    record.getThrown(),
                    record.getParameters() + " " + record.getResourceBundleName() + " " + record.getSequenceNumber() + " " + record.getThreadID()
            );
    }
}
