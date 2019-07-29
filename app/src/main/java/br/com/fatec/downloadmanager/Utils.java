package br.com.fatec.downloadmanager;

import android.content.Context;
import android.os.StatFs;

public class Utils {

    public static double getAvailableSpace(boolean inBytes, Context context){
        StatFs stat = new StatFs(context.getFilesDir().getPath());
        double sdAvailSize = (double)stat.getAvailableBlocks() * (double)stat.getBlockSize();
        return (inBytes) ? sdAvailSize :  sdAvailSize / 1073741824;
    }

}
