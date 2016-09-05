package com.ligen.wellwatcher.util;

import android.content.Context;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;


/**
 * Created by ligen on 2016/5/24.
 */
public class FileUtil {


    public static String readFile(Context context, String filename) {
        FileInputStream in;
        StringBuilder sb = new StringBuilder();
        try {
            in = context.openFileInput(filename);
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static void writeFile(Context context, String filename, String data) {
        Writer writer = null;
        try {
            FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(fos);
            writer.write(data);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
