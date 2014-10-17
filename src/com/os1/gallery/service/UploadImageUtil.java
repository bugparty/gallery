package com.os1.gallery.service;

import android.net.Uri;
import android.util.Log;
import com.os1.gallery.util.FromEntityBuilder;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.*;

/**
 * Created by hanbowen on 2014/10/12.
 */
public class UploadImageUtil {
    public static final String TAG = "UploadImageUtil";
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
    private static final String host = "os1.ifancc.com";
    private static final String path = "api/image";
    public static void  uploadImageLog(String userId,String localPath, String idExternal ){
        HttpClient httpClient = new DefaultHttpClient();
        //创建一个HttpResponse用于存放相应的数据
        HttpResponse response;
        //创建一个HttpPost请求
        HttpPost httpPost;
        HttpEntity entity;
        int loginStatus = 0;
        try {
            Uri uri = new Uri.Builder().scheme("http")
                    .authority(host)
                    .encodedPath(path)
                    .build();
            //Log.d(TAG, "builded uri:"+uri.toString());
            //设置请求的路径
            httpPost = new HttpPost(uri.toString());
            //创建一个用户，用于向服务端发送数据时，存放的实体
            UrlEncodedFormEntity fromEntity = FromEntityBuilder.create()
                    .add("iuserId", userId)
                    .add("localPath", localPath)
                    .add("idExternal",idExternal)
                    .add("sourceType","gallery")
                    .build();
            //设置请求体
            httpPost.setEntity(fromEntity);
            //执行请求获取响应
            response = httpClient.execute(httpPost);
            //如果响应的状态码为200时，表示请求响应成功
            Log.d(TAG, "post request");
            while (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                //获取响应的实体数据
                entity = response.getEntity();
                StringBuilder sb = new StringBuilder();
                //通过reader读取实体对象包含的数据
                BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));
                //循环读取实体里面的数据
                String s;
                while ((s = reader.readLine()) != null) {
                    sb.append(s);
                }
                //创建一个JSONObject对象存放从服务端获取到的JSONObject数据
                JSONObject datas = new JSONObject(sb.toString());
                //创建一个boolean变量用于存放服务端的处理结果状态
                loginStatus = datas.getInt("status");
                Log.d(TAG, "the status is " + loginStatus);


            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
