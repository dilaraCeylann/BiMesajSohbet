package com.elias.bimesajsohbet;

import android.content.Context;
import android.os.StrictMode;

public class LastSeenTime {
    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    public static String kalanZaman;

    public static String getTimeAgo(long time,Context ctx){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (time < 1000000000000L) {
                time *= 1000;
            }

            long now = System.currentTimeMillis();

            if (time > now || time <= 0) {
                return null;
            }

            final long diff = now - time;
            if (diff < MINUTE_MILLIS) {
                kalanZaman = "Az önce";
                return "Az önce";
            } else if (diff < 2 * MINUTE_MILLIS) {
                kalanZaman = "Bir dakika önce";
                return "Bir dakika önce";
            } else if (diff < 50 * MINUTE_MILLIS) {
                kalanZaman = diff / MINUTE_MILLIS + " dakika önce";
                return diff / MINUTE_MILLIS + " dakika önce";
            } else if (diff < 90 * MINUTE_MILLIS) {
                kalanZaman = "1 saat önce";
                return "1 saat önce";
            } else if (diff < 24 * HOUR_MILLIS) {
                kalanZaman = diff / HOUR_MILLIS + " saat önce";
                return diff / HOUR_MILLIS + " saat önce";
            } else {
                kalanZaman = diff / DAY_MILLIS + " gün önce";
                return diff / DAY_MILLIS + " gün önce";
            }


    }

    public static String getKalanZaman() {
        return kalanZaman;
    }

}
