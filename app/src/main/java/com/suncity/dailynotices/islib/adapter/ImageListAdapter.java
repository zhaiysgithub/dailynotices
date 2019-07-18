package com.suncity.dailynotices.islib.adapter;

import android.content.Context;
import android.widget.ImageView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.suncity.dailynotices.R;
import com.suncity.dailynotices.islib.ISNav;
import com.suncity.dailynotices.islib.bean.Image;
import com.suncity.dailynotices.islib.common.Constant;
import com.suncity.dailynotices.islib.common.OnItemClickListener;
import com.suncity.dailynotices.islib.config.ISListConfig;
import com.yuyh.easyadapter.recyclerview.EasyRVAdapter;
import com.yuyh.easyadapter.recyclerview.EasyRVHolder;

import java.util.List;

/**
 * @author yuyh.
 * @date 2016/8/5.
 */
public class ImageListAdapter extends EasyRVAdapter<Image> {

    private boolean showCamera;
    private boolean mutiSelect;

    private ISListConfig config;
    private Context context;
    private OnItemClickListener listener;

    public ImageListAdapter(Context context, List<Image> list, ISListConfig config) {
        super(context, list, R.layout.is_item_img_sel, R.layout.is_item_img_sel_take_photo);
        this.context = context;
        this.config = config;
    }

    @Override
    protected void onBindData(final EasyRVHolder viewHolder, final int position, final Image item) {

        if (position == 0 && showCamera) {
            ImageView iv = viewHolder.getView(R.id.ivTakePhoto);
            iv.setImageResource(R.drawable.ic_take_photo);
            iv.setOnClickListener(v -> {
                if (listener != null)
                    listener.onImageClick(position, item);
            });
            return;
        }

        if (mutiSelect) {
            viewHolder.getView(R.id.ivPhotoCheaked).setOnClickListener(v -> {
                if (listener != null) {
                    int ret = listener.onCheckedClick(position, item);
                    if (ret == 1) { // 局部刷新
                        if (Constant.imageList.contains(item.path)) {
                            viewHolder.setImageResource(R.id.ivPhotoCheaked, R.drawable.ic_checked);
                        } else {
                            viewHolder.setImageResource(R.id.ivPhotoCheaked, R.drawable.ic_uncheck);
                        }
                    }
                }
            });
        }

        viewHolder.setOnItemViewClickListener(v -> {
            if (listener != null)
                listener.onImageClick(position, item);
        });

        final SimpleDraweeView iv = viewHolder.getView(R.id.ivImage);
        ISNav.getInstance().displayImage(context, item.path, iv);

        if (mutiSelect) {
            viewHolder.setVisible(R.id.ivPhotoCheaked, true);
            if (Constant.imageList.contains(item.path)) {
                viewHolder.setImageResource(R.id.ivPhotoCheaked, R.drawable.ic_checked);
            } else {
                viewHolder.setImageResource(R.id.ivPhotoCheaked, R.drawable.ic_uncheck);
            }
        } else {
            viewHolder.setVisible(R.id.ivPhotoCheaked, false);
        }
    }

    public void setShowCamera(boolean showCamera) {
        this.showCamera = showCamera;
    }

    public void setMutiSelect(boolean mutiSelect) {
        this.mutiSelect = mutiSelect;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && showCamera) {
            return 1;
        }
        return 0;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
