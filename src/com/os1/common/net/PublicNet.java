package com.os1.common.net;

import android.net.Uri;
import android.util.Log;
import com.os1.common.config.PublicConfig;
import com.os1.common.http.FromEntityBuilder;
import com.os1.common.model.IuserEntity;
import de.aflx.sardine.Sardine;
import de.aflx.sardine.SardineFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by hanbowen on 2014/10/12.
 */
public class PublicNet {
    public static final String TAG = "UploadImageUtil";
    private IuserEntity user;
    public PublicNet(IuserEntity user) {
        this.user = user;
    }

    public  void  uploadImageLog(String localPath, String idExternal ){
        HttpClient httpClient = new DefaultHttpClient();
        //创建一个HttpResponse用于存放相应的数据
        HttpResponse response;
        //创建一个HttpPost请求
        HttpPost httpPost;
        HttpEntity entity;
        int loginStatus = 0;
        try {
            Uri uri = new Uri.Builder().scheme("http")
                    .authority(PublicConfig.uploadImageLogHost)
                    .encodedPath(PublicConfig.uploadImageLogPath)
                    .build();
            //Log.d(TAG, "builded uri:"+uri.toString());
            //设置请求的路径
            httpPost = new HttpPost(uri.toString());
            //创建一个用户，用于向服务端发送数据时，存放的实体
            UrlEncodedFormEntity fromEntity = FromEntityBuilder.create()
                    .add("iuserId",   String.valueOf(user.getIuserId()))
                    .add("localPath", localPath)
                    .add("idExternal",idExternal)
                    .add("sourceType","eye")
                    .build();
            //设置请求体
            httpPost.setEntity(fromEntity);
            //执行请求获取响应
            response = httpClient.execute(httpPost);
            //如果响应的状态码为200时，表示请求响应成功
            Log.v(TAG, "post request");
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
                Log.i(TAG, "uplaod compelet,s: " + loginStatus);
                return;

            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void uploadImage(byte[] data){
        Date now = new Date();
        SimpleDateFormat time=new SimpleDateFormat("_yyyyMMdd_HHmmdd_FFF");
        String timeStr = time.format(now);
        String uploadName = user.getIuserId()+timeStr + ".jpg";
        uploadImage(data, uploadName);

    }
    public  void uploadImage(byte[] datas,String destPath){
        Sardine sardine = SardineFactory.begin(PublicConfig.WEBDAV_USER, PublicConfig.WEBDAV_PASS);

        sardine.disableCompression();
        sardine.enablePreemptiveAuthentication(PublicConfig.webdavHost);

        try {
            sardine.put(PublicConfig.webdavEyeUrl+destPath, datas);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.v(TAG, "sardine finished");
    }


    private static final int timeout = 3;
    public JSONObject getInterest() {

        // TODO: attempt authentication against a network service.
        //创建一个httpClient连接
        HttpClient httpClient;
        //创建一个HttpResponse用于存放相应的数据
        HttpResponse response;
        //创建一个HttpPost请求
        HttpGet httpGet;
        //创建一个httpEntity用于存放请求的实体数据
        HttpEntity entity;
        HttpParams params;
        httpClient = new DefaultHttpClient();
        HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), timeout * 1000);
        HttpConnectionParams.setSoTimeout(httpClient.getParams(), timeout * 1000);
        try {
            Uri uri = new Uri.Builder().scheme("http")
                    .encodedAuthority(PublicConfig.getInterestHost)
                    .encodedPath(PublicConfig.getInterestPath)
                    .appendQueryParameter("op", "GetInterest")
                    .appendQueryParameter("UserId", "" +  String.valueOf(user.getIuserId()))
                    .build();
            Log.d(TAG, "builded uri:" + uri.toString());
            //设置请求的路径
            httpGet = new HttpGet(uri.toString());
            //创建一个用户，用于向服务端发送数据时，存放的实体

            //设置请求体

            //执行请求获取响应
            response = httpClient.execute(httpGet);
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


                return datas;
            }
            Log.e(TAG, "status error" + response.getStatusLine().getStatusCode());
            return null;

        }catch(ConnectTimeoutException e){
            return null;
        }
        catch (Exception e) {
            e.printStackTrace();
            return  null;
        }

    }
    public JSONArray getEvent(int lastEventId) {

        // TODO: attempt authentication against a network service.
        //创建一个httpClient连接
        HttpClient httpClient;
        //创建一个HttpResponse用于存放相应的数据
        HttpResponse response;
        //创建一个HttpPost请求
        HttpGet httpGet;
        //创建一个httpEntity用于存放请求的实体数据
        HttpEntity entity;
        HttpParams params;
        httpClient = new DefaultHttpClient();
        int loginStatus = 0;
        try {
            Uri uri = new Uri.Builder().scheme("http")
                    .encodedAuthority(PublicConfig.getEventHost)
                    .encodedPath(PublicConfig.getEventPath)
                    .appendQueryParameter("op", "GetEvent")
                    .appendQueryParameter("UserId",  String.valueOf(user.getIuserId()))
                    .appendQueryParameter("LastEvEntId", String.valueOf(lastEventId))
                    .build();
            Log.v(TAG, "builded uri:" + uri.toString());
            //设置请求的路径
            httpGet = new HttpGet(uri.toString());
            //创建一个用户，用于向服务端发送数据时，存放的实体

            //设置请求体

            //执行请求获取响应
            response = httpClient.execute(httpGet);
            //如果响应的状态码为200时，表示请求响应成功
            Log.v(TAG, "post request");


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
                JSONArray datas = new JSONArray(sb.toString());
                //创建一个boolean变量用于存放服务端的处理结果状态


                return datas;
            }
            Log.e(TAG, "status error" + response.getStatusLine().getStatusCode());
            return null;

        } catch (Exception e) {
            e.printStackTrace();
            return  null;
        }

    }
}
