package utils;

import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.text.FieldPosition;

public class TimeZoneDateFormat extends DateFormat {
    private final SimpleDateFormat formatter;

    public TimeZoneDateFormat(String pattern, TimeZone timeZone) {
        this.formatter = new SimpleDateFormat(pattern);
        this.formatter.setTimeZone(timeZone);
    }

    @Override
    public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition pos) {
        if (date == null) {
            return toAppendTo.append("");
        }
        return formatter.format(date, toAppendTo, pos);
    }

    @Override
    public Date parse(String source, ParsePosition pos) {
        if (source == null) {
            return null;
        }
        return formatter.parse(source, pos);
    }

    @Override
    public Object clone() {
        return super.clone();
    }
}