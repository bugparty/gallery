package com.os1.common.net;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by hanbowen on 2014/10/17.
 */
abstract public class INet  {
    abstract public  void  uploadImageLog(String localPath, String idExternal );
    abstract public  void uploadImage(byte[] data,String url);
    abstract public  void uploadImage(byte[] data);
    abstract public JSONObject getInterest();
    abstract public JSONArray getEvent(int lastEventId);
    abstract public String getUploadImageFilename();
    abstract public String getUploadImageUrl();
}
