package com.suncity.dailynotices.islib.common;


import com.suncity.dailynotices.islib.bean.Image;

/**
 * @author yuyh.
 * @date 2016/8/5.
 */
public interface OnItemClickListener {

    int onCheckedClick(int position, Image image);

    void onImageClick(int position, Image image);
}
