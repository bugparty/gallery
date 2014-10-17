package com.os1.gallery.ui;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.os1.camera.ImageManager;
import com.os1.gallery.IImageList;
import com.os1.gallery.service.UploadService;
import com.os1.gallery.R;

import java.util.ArrayList;

/**
 * Created by hanbowen on 2014/10/9.
 */
public class UploadActivity extends Activity {
    public static final String TAG = "UploadActivity";
    private TextView tv_summary;
    private Button btn_upload;
    private ProgressBar progressBar_upload;
    private Handler mHandler = new Handler();
    private Thread mUploadThread;
    private boolean isUploading = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        bindUploadService();
        tv_summary = (TextView) findViewById(R.id.tv_summary);
        btn_upload = (Button) findViewById(R.id.btnUpload);
        progressBar_upload = (ProgressBar ) findViewById(R.id.progressBar_upload);

        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isUploading){
                    Toast.makeText(getApplicationContext(), "uploading in the background", Toast.LENGTH_SHORT).show();
                    btn_upload.setText("uploading, click to cancel");
                    startUploadThread();
                    myBinder.getService().upload();
                    isUploading = true;
                }else{
                    myBinder.getService().cancelUpload();
                }

            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);

    }

    private void bindUploadService(){
        Intent serviceIntent;
        serviceIntent = new Intent(this, UploadService.class);
        startService(serviceIntent);
        bindService(serviceIntent, connection, BIND_AUTO_CREATE);
    }
    private UploadService.UploadBinder myBinder;

    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "onServiceDisconnected");
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected");
            myBinder = (UploadService.UploadBinder) service;
            updateSummary(myBinder.getService().size());
        }
    };
    private void startUploadThread(){
        mUploadThread = new Thread("UploadActivity UploadThread"){
            @Override
            public void run() {
                uploadRun();
            }
        };
        mUploadThread.start();

    }

    private void uploadRun(){
        ArrayList<Item> allItems = new ArrayList<Item>();
        checkImageList(allItems);
        int total = 0;

        Log.d(TAG, "totol "+total);
        for(Item i:allItems){

            if(i!=null){
               int size = i.mImageList.getCount();
               for(int j=0;j<size;j++){
                   i.mImageList.getImageAt(j);
                   total++;
                   try {
                       Thread.sleep(1000*5);
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }
                   final int progress = total;
                   mHandler.post(new Runnable() {
                       @Override
                       public void run() {
                           updateProgress(progress);
                       }
                   });
               }
            }
        }

    }
    private void updateProgress(int i){
        //Log.d(TAG, "updateProgress "+i);
        progressBar_upload.setProgress(i);
    }

    // This is used to stop the worker thread.
    volatile boolean mAbort = false;

    private void checkImageList(ArrayList<Item> allItems) {
        int length = IMAGE_LIST_DATA.length;
        IImageList[] lists = new IImageList[length];
        for (int i = 0; i < length; i++) {
            ImageListData data = IMAGE_LIST_DATA[i];
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
            for(IImageList l : lists){
                if(l!= null)
                    total += l.getCount();
            }
            final int  fTotal = total;
            mHandler.post(new Runnable() {
                public void run() {
                    updateSummary(fTotal);
                }
            });
        }
    }
    //run in the main thread
    void updateSummary(int total) {
        tv_summary.setText("total picture count "+total);
        progressBar_upload.setMax(total);
        progressBar_upload.setProgress(0);
    }
    // IMAGE_LIST_DATA stores the parameters for the four image lists
    // we are interested in. The order of the IMAGE_LIST_DATA array is
    // significant (See the implementation of GalleryPickerAdapter.init).
    private static final class ImageListData {
        ImageListData(int type, int include, String bucketId, int stringId) {
            mType = type;
            mInclude = include;
            mBucketId = bucketId;
            mStringId = stringId;
        }
        int mType;
        int mInclude;
        String mBucketId;
        int mStringId;
    }

    private static final ImageListData[] IMAGE_LIST_DATA = {
            // Camera Images
            new ImageListData(Item.TYPE_CAMERA_IMAGES,
                    ImageManager.INCLUDE_IMAGES,
                    ImageManager.CAMERA_IMAGE_BUCKET_ID,
                    R.string.gallery_camera_bucket_name),
            // Camera Videos
            new ImageListData(Item.TYPE_CAMERA_VIDEOS,
                    ImageManager.INCLUDE_VIDEOS,
                    ImageManager.CAMERA_IMAGE_BUCKET_ID,
                    R.string.gallery_camera_videos_bucket_name),

            // Camera Medias
            new ImageListData(Item.TYPE_CAMERA_MEDIAS,
                    ImageManager.INCLUDE_VIDEOS | ImageManager.INCLUDE_IMAGES,
                    ImageManager.CAMERA_IMAGE_BUCKET_ID,
                    R.string.gallery_camera_media_bucket_name),

            // All Images
            new ImageListData(Item.TYPE_ALL_IMAGES,
                    ImageManager.INCLUDE_IMAGES,
                    null,
                    R.string.all_images),

            // All Videos
            new ImageListData(Item.TYPE_ALL_VIDEOS,
                    ImageManager.INCLUDE_VIDEOS,
                    null,
                    R.string.all_videos),
    };
    ArrayList<IImageList> mAllLists = new ArrayList<IImageList>();

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

    private void clearImageLists() {
        for (IImageList list : mAllLists) {
            list.close();
        }
        mAllLists.clear();
    }
}


