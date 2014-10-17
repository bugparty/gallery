package com.os1.gallery.service;

/**
 * Created by hanbowen on 2014/10/9.
 */

import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import android.widget.Toast;
import com.os1.camera.ImageManager;
import com.os1.gallery.IImage;
import com.os1.gallery.IImageList;
import com.os1.gallery.ui.Item;
import com.os1.util.jpegThumb.JpegThumb;
import de.aflx.sardine.Sardine;
import de.aflx.sardine.SardineFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class UploadService extends Service {
    public static final String TAG = "UploadService";
    private UploadBinder mBinder = new UploadBinder();
    private Thread statThread = null;

    @Override
    public IBinder onBind(Intent arg0) {
        Log.d(TAG, "onBind");
        return mBinder;
    }

    private volatile boolean mAbort = false;
    ArrayList<Item> allItems = null;
    int length;

    public int size() {
        if (statThread == null) return 0;
        try {
            statThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return length;
    }
    int progress =0;
    int progressSize;
    private IImageList getCameraPhotos(){
        for(Item i:allItems){
            if(i == null)continue;
            if(i.mType == Item.TYPE_ALL_IMAGES && i.mImageList != null){
                Log.d(TAG ,"selecting all images");
                return i.mImageList;
            }

            if(i.mType == Item.TYPE_CAMERA_MEDIAS && i.mImageList != null){
                Log.d(TAG ,"selecting camera medias");
                return i.mImageList;
            }

            if(i.mType == Item.TYPE_CAMERA_IMAGES && i.mImageList != null){
                Log.d(TAG ,"selecting camera images");
                return i.mImageList;
            }

        }
        return null;
    }
    Thread uploadThread;
    public void upload(){
        Log.d(TAG, "upload");

        final IImageList list = getCameraPhotos();
        if(list == null){
            Toast.makeText(getApplicationContext(), "no photos",Toast.LENGTH_SHORT).show();
            Log.e(TAG, "no photos");
            return;
        }
        progressSize = list.getCount();
        Log.d(TAG ,progressSize + "to upload");
        final String thumbPath = checkThumbFolder();

        uploadThread = new Thread("UploadService upload main thread") {

            @Override
            public void run() {
                for(progress=0;progress < progressSize;progress++){
                    IImage image = list.getImageAt(progress);
                    Log.d(TAG, "progress:"+progress+" "+image.getDataPath()+" "+image.getTitle());
                    String destpath = thumbPath +"/"+image.getTitle()+ ".thumb.jpg";
                    if(isInterrupted()){
                        Log.d(TAG, "thread canceled");
                        return;
                    }
                    int status = JpegThumb.jpegThumbFile(image.getDataPath(), destpath, 800, 600, 90);
                    if(isInterrupted()){
                        Log.d(TAG, "thread canceled");
                        return;
                    }
                    uploadFile(destpath,image.getTitle()+".jpg");
                    UploadImageUtil.uploadImageLog("110",destpath,""+progress);
                }

            }
        };
        uploadThread.start();
    }
    private String  checkThumbFolder(){
        File sdcardDir = Environment.getExternalStorageDirectory();
        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
            //得到一个路径，内容是sdcard的文件夹路径和名字
            String path = sdcardDir.getPath();   //得到/sdcard/profitData的路径
            File thumbFolder = new File(path+"/os1Thumb");
            if(!thumbFolder.exists()) {
                Log.d(TAG, "make os1Thumb dir");
                thumbFolder.mkdir();
            }
            return thumbFolder.getAbsolutePath();
        }
        return null;
    }
    private void uploadFile(String imagePath,String destPath){
        Sardine sardine = SardineFactory.begin("bowman", "bowman");
        byte[] data = UploadImageUtil.readBinary(imagePath);
        try {
            sardine.put("http://os1dav.ifancc.com/"+destPath, data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "sardine finished");
    }
    public void cancelUpload(){
        if(uploadThread != null){
            uploadThread.interrupt();
            Log.d(TAG, "trying to cancel upload");
        }
    }

    ArrayList<IImageList> mAllLists = new ArrayList<IImageList>();

    @Override
    public void onDestroy() {
        Log.d(TAG, "The background service is destroyed!!!!");
    }

    public class UploadBinder extends Binder {
        public UploadService getService() {
            return UploadService.this;
        }

    }
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        // 在这里创建线程
        startStatThread();
        return START_STICKY;
    }

    private void startStatThread() {
        if (statThread == null) {
            statThread = new Thread("UploadService statThread") {
                @Override
                public void run() {
                    statRun();
                }
            };
            statThread.start();
        }
    }
    private IImageList createImageList(int mediaTypes, String bucketId,
                                       ContentResolver cr) {
        IImageList list = ImageManager.makeImageList(
                cr,
                ImageManager.DataLocation.ALL,
                mediaTypes,
                ImageManager.SORT_DESCENDING,
                bucketId);
        mAllLists.add(list);
        return list;
    }
    private void statRun() {
        allItems = new ArrayList<Item>();
        checkImageList(allItems);
        length = 0;

        for (Item i : allItems) {

            if (i != null) {
                int size = i.mImageList.getCount();
                for (int j = 0; j < size; j++) {
                    i.mImageList.getImageAt(j);
                    length++;
                }
            }
        }

    }
    private void checkImageList(ArrayList<Item> allItems) {
        int length = ImageListDataUtil.IMAGE_LIST_DATA.length;
        IImageList[] lists = new IImageList[length];
        for (int i = 0; i < length; i++) {
            ImageListDataUtil.ImageListData data = ImageListDataUtil.IMAGE_LIST_DATA[i];
            lists[i] = createImageList(data.mInclude, data.mBucketId,
                    getContentResolver());
            if (mAbort) return;
            Item item = null;

            if (lists[i].isEmpty()) continue;

            // i >= 3 means we are looking at All Images/All Videos.
            // lists[i-3] is the corresponding Camera Images/Camera Videos.
            // We want to add the "All" list only if it's different from
            // the "Camera" list.
            if (i >= 3 && lists[i].getCount() == lists[i - 3].getCount()) {
                continue;
            }

            item = new Item(data.mType,
                    data.mBucketId,
                    getResources().getString(data.mStringId),
                    lists[i]);

            allItems.add(item);
            int total = 0;
            for (IImageList l : lists) {
                if (l != null)
                    total += l.getCount();
            }
            final int fTotal = total;

        }
    }

}