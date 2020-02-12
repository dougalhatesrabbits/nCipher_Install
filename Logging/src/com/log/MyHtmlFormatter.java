package com.log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

//This custom formatter formats parts of a log record to a single line
class MyHtmlFormatter extends Formatter {

    // This method is called for every log records
    public String format(LogRecord rec) {
        StringBuffer buf = new StringBuffer(1000);
        // Bold any levels >= WARNING
        buf.append("<tr>");

        buf.append("<td>");
        buf.append(calcDate(rec.getMillis()));
        //buf.append(' ');
        buf.append("</td>");

        buf.append("<td>");
        buf.append(rec.getLoggerName());
        buf.append(' ');
        buf.append(rec.getSourceClassName());
        buf.append(' ');
        buf.append(rec.getSourceMethodName());
        buf.append("</td>");

        buf.append("<td>");
        if (rec.getLevel().intValue() == Level.WARNING.intValue()) {
            buf.append("<font color=\"orange\">");
            buf.append("<b>");
            buf.append(rec.getLevel());
            buf.append("</b>");
            buf.append("</font>");
        } else if (rec.getLevel().intValue() == Level.SEVERE.intValue()){
            buf.append("<font color=\"red\">");
            buf.append("<b>");
            buf.append(rec.getLevel());
            buf.append("</b>");
            buf.append("</font>");
        } else if (rec.getLevel().intValue() == Level.INFO.intValue()) {
            buf.append("<font color=\"blue\">");
            buf.append("<b>");
            buf.append(rec.getLevel());
            buf.append("</b>");
            buf.append("</font>");
        } else {
            buf.append(rec.getLevel());
        }
        buf.append("</td>");

        buf.append("<td>");
        buf.append(formatMessage(rec));
        buf.append("</td>");

        buf.append("<td>");

            buf.append(rec.getThrown());
        buf.append('\n');

        buf.append("</td>");

        buf.append("</tr>\n");

        return buf.toString();
    }

    private String calcDate(long millisecs) {
        SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MMM-dd HH:mm");
        Date resultdate = new Date(millisecs);
        return date_format.format(resultdate);
    }

    // This method is called just after the handler using this
    // formatter is created
    public String getHead(Handler h) {
        return "<HTML>\n<HEAD>\n" + (new Date())
                + "\n</HEAD>\n<BODY>\n<PRE>\n"
                + "<table width=\"100%\" border>\n  "
                + "<tr><th>Time</th>" +
                "<th>Logger</th>" +
                "<th>Level</th>" +
                "<th>Log Message</th>" +
                "<th>Thrown</th>" +
                "</tr>\n";
    }

    // This method is called just after the handler using this
    // formatter is closed
    public String getTail(Handler h) {
        return "</table>\n  </PRE></BODY>\n</HTML>\n";
    }
}
