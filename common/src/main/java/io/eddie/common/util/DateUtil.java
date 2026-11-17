package io.eddie.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    private static final String FMT = "yyyyMMdd";

    public static String getToday() {
        return new SimpleDateFormat(FMT).format(new Date());
    }

    public static int diffDays(String fromDt, String toDt) {
        Calendar from = toCal(fromDt);
        Calendar to = toCal(toDt);
        long diff = to.getTimeInMillis() + to.get(Calendar.DST_OFFSET)
                  - from.getTimeInMillis() - from.get(Calendar.DST_OFFSET);
        return (int) (diff / (24L * 60 * 60 * 1000));
    }

    public static int diffMonths(String fromDt, String toDt) {
        Calendar from = toCal(fromDt);
        Calendar to = toCal(toDt);
        int months = (to.get(Calendar.YEAR) - from.get(Calendar.YEAR)) * 12
                   + (to.get(Calendar.MONTH) - from.get(Calendar.MONTH));
        if (to.get(Calendar.DAY_OF_MONTH) < from.get(Calendar.DAY_OF_MONTH)) {
            months--;
        }
        return months;
    }

    private static Calendar toCal(String yyyymmdd) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(FMT);
            sdf.setLenient(false);
            Calendar cal = Calendar.getInstance();
            cal.setTime(sdf.parse(yyyymmdd));
            return cal;
        } catch (ParseException e) {
            throw new IllegalArgumentException("날짜 형식 오류: " + yyyymmdd);
        }
    }
}
