package com.liansu.boduowms.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.liansu.boduowms.R;

import java.util.List;
import java.util.Map;

import androidx.annotation.DrawableRes;


public class GridViewItemAdapter extends BaseAdapter {

    private Context                   context;
    private List<Map<String, Object>> listitem;
    private  int mBackgroundColor=-1;
    public GridViewItemAdapter(Context context, List<Map<String, Object>> listitem) {
        this.context = context;
        this.listitem = listitem;
    }

    public void setBackgroundColor(@DrawableRes int id ){
        mBackgroundColor=id;
    }
    @Override
    public int getCount() {
        return listitem == null ? 0 : listitem.size();
    }

    @Override
    public Object getItem(int position) {
        return listitem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_main_gridview, null);
        }

        final ImageView imageView = (ImageView) convertView.findViewById(R.id.ItemImage);
        TextView textView = (TextView) convertView.findViewById(R.id.ItemText);
        ImageView backImage=(ImageView)convertView.findViewById(R.id.item_background);
        Map<String, Object> map = listitem.get(position);
        int iconId = (Integer) map.get("image");
//        imageView.setImageResource((Integer) map.get("image"));
//        imageView.setImageResource(iconId);
        // 初始化背景
//        Glide.with(context)
//                .asBitmap()
//                .fitCenter() //居中剪切
//                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
//                .load(drawableToBitmap(context.getResources().getDrawable(iconId)))
//                .into(new UniformScaleTransformation(imageView));
//        Glide.with(context)
//                .load(context.getResources().getDrawable(iconId))
//                .centerCrop() //居中剪切
//                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
//                .into(new DrawableImageViewTarget(imageView) {
//                    @Override
//                    protected void setResource(@Nullable Drawable resource) {
//                        if (resource == null) {
//                            super.setResource(resource);
//                            return;
//                        }
//                        // 使用适配类进行包装
//                        Drawable drawable = DrawableCompat.wrap(resource);
//                        // 设置给ImageView
//                        super.setResource(drawable);
//                    }
//                });
//        Glide.with(context)
//                .asBitmap()
//                .load(drawableToBitmap(context.getResources().getDrawable(iconId)))
//                .fitCenter()
//                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
//                .placeholder(R.color.white)
//                .thumbnail(0.5f)//缩略图
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .into(new GlideRatioScaleTransForm(imageView));


        Glide.with(context)
                .asDrawable()
                .load(context.getResources().getDrawable(iconId))
                .fitCenter()
//                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(imageView);

        textView.setText(map.get("text") + "");
//        if (mBackgroundColor!=-1){
//            backImage.setImageDrawable(context.getResources().getDrawable(mBackgroundColor));
//        }else {
//            backImage.setImageDrawable(context.getResources().getDrawable(R.drawable.blue_background));
//
//        }
        return convertView;
    }

    /**
     * 根据图片，强制调整View的大小
     */
    public class UniformScaleTransformation extends ImageViewTarget<Bitmap> {

        private ImageView target;

        public UniformScaleTransformation(ImageView target) {
            super(target);
            this.target = target;
        }

        @Override
        protected void setResource(Bitmap resource) {

            if (resource == null) {
                return;
            }

            view.setImageBitmap(resource);

            //获取原图的宽高
            int width = resource.getWidth();
            int height = resource.getHeight();

            //获取imageView的宽
            int imageViewWidth = target.getWidth();

            //计算缩放比例
            float sy = (float) (imageViewWidth * 0.1) / (float) (width * 0.1);

            //计算图片等比例放大后的高
            int imageViewHeight = (int) (height * sy);
            ViewGroup.LayoutParams params = target.getLayoutParams();
            params.height = imageViewHeight;
            target.setLayoutParams(params);
        }
    }

    /**
     * Drawable转换成一个Bitmap
     *
     * @param drawable drawable对象
     * @return
     */
    public static final Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }
}