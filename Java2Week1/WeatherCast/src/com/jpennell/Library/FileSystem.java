/*
 * project		WeatherCast
 * 
 * package		com.jpennell.Library
 * 
 * author		Jerry Pennell
 * 
 * date			Jul 22, 2013
 * update       Aug 06, 2013
 */
package com.jpennell.Library;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import android.content.Context;
import android.util.Log;

// TODO: Auto-generated Javadoc
/**
 * The Class FileSystem.
 */
public class FileSystem {

    // Declare variables
    //Context _context;

    /**
     * Store string file.
     *
     * @param context the context
     * @param filename the filename
     * @param content the content
     * @param external the external
     * @return the boolean
     */
    @SuppressWarnings("resource")
	public static Boolean storeStringFile(Context context, String filename, String content, Boolean external) {
        try {
            File file;
            FileOutputStream fos;
            if (external) {
                file = new File(context.getExternalFilesDir(null), filename);
                fos = new FileOutputStream(file);
            } else {
                fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
            }
            fos.write(content.getBytes());
            fos.close();
        } catch (IOException e) {
            Log.e("WRITE ERROR", filename);
        }

        return true;
    }

    
    /**
     * Read string file.
     *
     * @param context the context
     * @param filename the filename
     * @param external the external
     * @return the string
     */
    @SuppressWarnings("resource")
	public static String readStringFile(Context context, String filename, Boolean external) {
        String content = "";

        try {
            File file;
            FileInputStream fin;

            if (external) {
                file = new File(context.getFileStreamPath(null), filename);
                fin = new FileInputStream(file);
            } else {
                file = new File(filename);
                fin = context.openFileInput(filename);
            }
            BufferedInputStream bin = new BufferedInputStream(fin);
            byte[] contentBytes = new byte[1024];
            int bytesRead = 0;
            // StringBulider is a safer call than StringBuffer
            StringBuilder contentBuffer = new StringBuilder();

            while ((bytesRead = bin.read(contentBytes)) != -1) {
                content = new String(contentBytes, 0, bytesRead);
                contentBuffer.append(content);
            }
            content = contentBuffer.toString();
            fin.close();
        } catch (FileNotFoundException e) {
            Log.e("READ ERROR", "FILE NOT FOUND" + filename);
        } catch (IOException e) {
            Log.e("READ ERROR", "I/O ERROR");
        }
        return content;
    }
}