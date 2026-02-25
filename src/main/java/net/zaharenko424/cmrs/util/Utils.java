package net.zaharenko424.cmrs.util;

import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.text.DecimalFormat;

public class Utils {

    public static long GB = 1024 * 1024 * 1024;
    public static long MB = 1024 * 1024;
    public static long KB = 1024;
    public static final DecimalFormat FORMAT = new DecimalFormat("#.##");

    public static String memFormat(long bytes){
        if(bytes >= GB) return FORMAT.format(bytes / (float) GB) + "GB";
        if(bytes >= MB) return FORMAT.format(bytes / (float) MB) + "MB";
        if(bytes >= KB) return FORMAT.format(bytes / (float) KB) + "KB";
        return bytes + "B";
    }

    public static boolean isNonZero(Vector3f vec){
        return vec.x != 0 || vec.y != 0 || vec.z != 0;
    }

    public static boolean isNonZero(Quaternionf quat){
        return quat.x != 0 || quat.y != 0 || quat.z != 0;
    }

    public static boolean isNonOne(Vector3f vec){
        return vec.x != 1 || vec.y != 1 || vec.z != 1;
    }

    public static int color(int alpha, int color) {
        return alpha << 24 | color & 16777215;
    }
}
