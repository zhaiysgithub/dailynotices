package com.suncity.dailynotices.islib.common;


import com.suncity.dailynotices.islib.bean.Folder;

/**
 * @author yuyh.
 * @date 2016/8/5.
 */
public interface OnFolderChangeListener {

    void onChange(int position, Folder folder);
}
