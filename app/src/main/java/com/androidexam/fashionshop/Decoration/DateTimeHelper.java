package com.androidexam.fashionshop.Decoration;
import java.text.ParseException;
        import java.text.SimpleDateFormat;
        import java.util.Date;
        import java.util.Locale;
import java.util.TimeZone;

public class DateTimeHelper {

    public static String formatDateTime(String inputDateTime) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault());

        try {
            Date date = inputFormat.parse(inputDateTime);
            if (date != null) {
                outputFormat.setTimeZone(TimeZone.getTimeZone("GMT+07:00"));
                return outputFormat.format(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "";
    }
}
