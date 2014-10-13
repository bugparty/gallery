package com.os1.camera.gallery.service;

import java.io.*;

/**
 * Created by hanbowen on 2014/10/12.
 */
public class UploadImageUtil {
    public static byte[] readBinary(String file){
        InputStream is = null;
        try {
            is = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int b = 0;
        try {
            while((b = is.read())!=-1){
                baos.write(b);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return baos.toByteArray();
    }
}
