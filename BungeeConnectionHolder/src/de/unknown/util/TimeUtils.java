package de.unknown.util;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {

    public static long toTimestamp(String in) throws IllegalStateException, NumberFormatException {
        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy - HH:mm");
        try {
            Date d = df.parse(in);
            return d.getTime();
        } catch(ParseException e) {
            //Not in dateformat
            long ds = 0, mins = 0, hs = 0, ys = 0;
            
            StringBuilder sb = new StringBuilder();
            for(int i = 0; i < in.length(); i++)
            {
                Character c = in.charAt(i);
                DecimalFormat dfn = new DecimalFormat("########");
                try{
                    dfn.parse(c.toString());
                    sb.append(c);
                }catch(ParseException en) {
                    if(!sb.toString().isEmpty()) {
                        if(c.equals('d')) {
                            Long inte = Long.parseLong(sb.toString());
                            ds += inte;
                        } else if(c.equals('m')) {
                            Long inte = Long.parseLong(sb.toString());
                            mins += inte;
                        } else if(c.equals('h')) {
                            Long inte = Long.parseLong(sb.toString());
                            hs += inte;
                        } else if(c.equals('y')) {
                            Long inte = Long.parseLong(sb.toString());
                            ys += inte;
                        }
                        sb = new StringBuilder();
                    }
                }
            }
            long curr = System.currentTimeMillis();
            long out = curr;
            out = (long) out + ((long) mins * 60000L);
            out = (long) out + ((long) hs * 3600000L);
            out = (long) out + ((long) ds * 86400000L);
            out = (long) out + ((long) ys * 31536000000L);
            if(curr == out) {
                throw new IllegalStateException("No valid format");
            }
            return out;
        }
    }

}
