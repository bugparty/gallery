package com.os1.gallery.service;

import com.os1.camera.ImageManager;
import com.os1.gallery.ui.Item;
import com.os1.gallery.R;

/**
 * Created by hanbowen on 2014/10/9.
 */
public class ImageListDataUtil {
    public static final ImageListData[] IMAGE_LIST_DATA = {
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
    // IMAGE_LIST_DATA stores the parameters for the four image lists
    // we are interested in. The order of the IMAGE_LIST_DATA array is
    // significant (See the implementation of GalleryPickerAdapter.init).
    public static final class ImageListData {
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


}
