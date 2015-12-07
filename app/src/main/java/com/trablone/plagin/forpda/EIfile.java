package com.trablone.plagin.forpda;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Environment;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.*;

public class EIfile {


    public static  boolean exportStyle(File in, File out, String name){

	//	if(!SdIsPresent()) return false;
	//	Log.e("data", "export= " + SdIsPresent());
        File dbFile = in;
        File exportDir = out;
        File file = new File(exportDir, name);

        if (!exportDir.exists()) {
            exportDir.mkdirs();
        }

        try {
            file.createNewFile();
            copyFile(dbFile, file);
			
            return true;
        } catch (IOException e) {
			
            e.printStackTrace();
            return false;
        }
    }

    private static void copyFile(File src, File dst) throws IOException {
        FileChannel inChannel = new FileInputStream(src).getChannel();
        FileChannel outChannel = new FileOutputStream(dst).getChannel();
        try {
            inChannel.transferTo(0, inChannel.size(), outChannel);
        } finally {
            if (inChannel != null)
                inChannel.close();
            if (outChannel != null)
                outChannel.close();
        }
    }

    /** Returns whether an SD card is present and writable **/
    public static boolean SdIsPresent() {
        return Environment.getExternalStorageState().equals(
			Environment.MEDIA_MOUNTED);
    }
}
