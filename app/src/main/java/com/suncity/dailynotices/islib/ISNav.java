package com.suncity.dailynotices.islib;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.Fragment;
import com.facebook.drawee.view.SimpleDraweeView;
import com.suncity.dailynotices.islib.config.ISCameraConfig;
import com.suncity.dailynotices.islib.config.ISListConfig;
import com.suncity.dailynotices.islib.ui.ISCameraActivity;
import com.suncity.dailynotices.islib.ui.ISListActivity;
import com.suncity.dailynotices.utils.LogUtils;

import java.io.File;

/**
 * 总线
 * <p>
 * Created by yuyuhang on 2017/10/23.
 */
public class ISNav {

    private static ISNav instance;

    public static ISNav getInstance() {
        if (instance == null) {
            synchronized (ISNav.class) {
                if (instance == null) {
                    instance = new ISNav();
                }
            }
        }
        return instance;
    }


    public void displayImage(Context context, String path, SimpleDraweeView imageView) {
        LogUtils.INSTANCE.e("path =" + ("file://" + path));
        imageView.setImageURI(Uri.parse(("file://" + path)));
    }

    public void toListActivity(Object source, ISListConfig config, int reqCode) {
        if (source instanceof Activity) {
            ISListActivity.startForResult((Activity) source, config, reqCode);
        } else if (source instanceof Fragment) {
            ISListActivity.startForResult((Fragment) source, config, reqCode);
        } else if (source instanceof android.app.Fragment) {
            ISListActivity.startForResult((android.app.Fragment) source, config, reqCode);
        }
    }

    public void toCameraActivity(Object source, ISCameraConfig config, int reqCode) {
        if (source instanceof Activity) {
            ISCameraActivity.startForResult((Activity) source, config, reqCode);
        } else if (source instanceof Fragment) {
            ISCameraActivity.startForResult((Fragment) source, config, reqCode);
        } else if (source instanceof android.app.Fragment) {
            ISCameraActivity.startForResult((android.app.Fragment) source, config, reqCode);
        }
    }

}
