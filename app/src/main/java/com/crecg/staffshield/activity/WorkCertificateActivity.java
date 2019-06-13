package com.crecg.staffshield.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crecg.staffshield.R;
import com.crecg.staffshield.common.BaseActivity;
import com.crecg.staffshield.dialog.SelectPhotoDialog;
import com.crecg.staffshield.utils.PhotoUtils;
import com.hyphenate.util.ImageUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 上传工作证明
 */

public class WorkCertificateActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_name;  // 姓名
    private TextView tv_id_card;  // 身份证号
    private TextView tv_phone_number;  // 电话
    private LinearLayout ll_upload_left;  // 上传工作证明 布局
    private LinearLayout ll_upload_right;  // 上传工会盖章证明材料 布局
    private ImageView iv_work_certificate;   // 工作证明
    private ImageView iv_trade_union_certificate;// 工会盖章证明材料
    private Button btn_join_immediately;  //  立即加入

    //表示选择的是相册--2
    private static int GALLERY_REQUEST_CODE = 2;
    //表示选择的是裁剪--3
    private static int CROP_REQUEST_CODE = 3;
    //图片保存SD卡位置
    private final static String IMG_PATH = Environment.getExternalStorageDirectory() + "/jiaokan/imgs/";
    //获取到的图片路径
    private String picPath;
    private Uri photoUri;
    //使用照相机拍照获取图片
    public static final int SELECT_PIC_BY_TACK_PHOTO = 1;
    //使用相册中的图片
    public static final int SELECT_PIC_BY_PICK_PHOTO = 2;
    private String workCertificate;
    private Bitmap newZoomImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseSetContentView(R.layout.activity_work_certificate);

        initView();
    }

    private void initView() {
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_id_card = (TextView) findViewById(R.id.tv_id_card);
        tv_phone_number = (TextView) findViewById(R.id.tv_phone_number);
        ll_upload_left = (LinearLayout) findViewById(R.id.ll_upload_left);
        ll_upload_right = (LinearLayout) findViewById(R.id.ll_upload_right);
        iv_work_certificate = findViewById(R.id.iv_work_certificate);
        iv_trade_union_certificate = findViewById(R.id.iv_trade_union_certificate);
        btn_join_immediately = (Button) findViewById(R.id.btn_join_immediately);

        if (!TextUtils.isEmpty(workCertificate)) {
            File file = new File(IMG_PATH);

            if (file.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(IMG_PATH + "Test.png");
                iv_work_certificate.setImageBitmap(bitmap);
            } else {
                new ImageViewService().execute(workCertificate);
            }
        }
//        else {
//            iv_work_certificate.setImageDrawable(getResources().getDrawable(R.mipmap.ic_launcher));
//        }

        ll_upload_left.setOnClickListener(this);
        ll_upload_right.setOnClickListener(this);
        btn_join_immediately.setOnClickListener(this);
    }

    /**
     * 获取网落图片资源
     * @return
     */
    class ImageViewService extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap data = getImageBitmap(params[0]);
            return data;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            if (result != null) {
                iv_work_certificate.setImageBitmap(result);
                saveBitmap2(result);
            }
//            else {
//                iv_work_certificate.setImageDrawable(getResources().getDrawable(R.mipmap.ic_launcher));
//            }
        }

    }

    private Uri saveBitmap2(Bitmap bm) {
        File tmpDir = new File(IMG_PATH);
        if (!tmpDir.exists()) {
            tmpDir.mkdirs();
        }
        File img = new File(IMG_PATH + "Test.png");
        try {
            FileOutputStream fos = new FileOutputStream(img);
            bm.compress(Bitmap.CompressFormat.PNG, 70, fos);
            fos.flush();
            fos.close();
            return Uri.fromFile(img);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    private Bitmap getImageBitmap(String url) {
        URL imgUrl = null;
        Bitmap bitmap = null;
        try {
            imgUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) imgUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
//            bitmap = ImageUtils.toRoundBitmap(bitmap); // 把图片处理成圆形
            is.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_upload_left: // 上传工作证明
                selectPhoto();
                break;
            case R.id.ll_upload_right: // 上传工会盖章证明材料
                break;
            case R.id.btn_join_immediately: // 立即加入

                break;
        }
    }

    private void selectPhoto() {
        SelectPhotoDialog mDialog = new SelectPhotoDialog(this, new SelectPhotoDialog.OnSelectPhotoChanged() {
            @Override
            public void onAlbum() { // 相册
                pickPhoto();
            }

            @Override
            public void onCamera() { // 相机
                takePhoto();
            }

        });
        mDialog.show();
    }

    /***
     *  从相册选取图片
     */
    private void pickPhoto() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }

    /**
     * 拍照获取图片
     */
    private void takePhoto() {
        //执行拍照前，应该先判断SD卡是否存在
        String sdState = Environment.getExternalStorageState();
        if (sdState.equals(Environment.MEDIA_MOUNTED)) {

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//"android.media.action.IMAGE_CAPTURE"
            /***
             * 需要说明一下，以下操作使用照相机拍照，拍照后的图片会存放在相册中的
             * 这里使用的这种方式有一个好处就是获取的图片是拍照后的原图
             * 如果不使用ContentValues存放照片路径的话，拍照后获取的图片为缩略图不清晰
             */
            /*//设置图片的保存路径,作为全局变量
            String imageFilePath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/filename.jpg";
			File temp = new File(imageFilePath);
			photoUri = Uri.fromFile(temp);//获取文件的Uri*/
            ContentValues values = new ContentValues();
            photoUri = this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            Log.i("aa", "photoUri: -- " + photoUri);
            // 例：（三星手机）photoUri = content://media/external/images/media/27388
            // 例：（华为手机）photoUri = content://media/external/images/media/539797

            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            startActivityForResult(intent, SELECT_PIC_BY_TACK_PHOTO);
        } else {
            Toast.makeText(this, "内存卡不存在", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 根据用户选择，返回图片资源
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//			doPhoto(requestCode, data);
        if (requestCode == SELECT_PIC_BY_TACK_PHOTO) {  // 表示用的相机
            Bitmap photoBmp = null;
            File file = null;
            file = getFileFromMediaUri(this, photoUri);

            if (photoUri != null) {
                try {
                    photoBmp = getBitmapFormUri(WorkCertificateActivity.this, Uri.fromFile(file));
                    int degree = PhotoUtils.readPictureDegree(file.getAbsolutePath());
//                    Log.i("aaa", "degree:  " + degree);
                    // 把图片旋转为正的方向
                    Bitmap newbitmap = PhotoUtils.rotateBitmap(photoBmp, degree);
                    if (newbitmap != null) {
                        dialog.setmLoadingTip("正在上传照片，请稍后……");
                        startLoading();
                    }

                    newZoomImage = newbitmap;
                    sendImage(newbitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        } else if (requestCode == GALLERY_REQUEST_CODE) {  // 表示用的相册
            if (data == null) {
                return;
            }
            dialog.setmLoadingTip("正在上传照片，请稍后……");
            startLoading();
            Uri mImageCaptureUri = data.getData();

            Bitmap photoBmp = null;

            if (mImageCaptureUri != null) {
                try {
                    photoBmp = getBitmapFormUri(WorkCertificateActivity.this, mImageCaptureUri);
                    newZoomImage = photoBmp;
                    sendImage(photoBmp);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (requestCode == CROP_REQUEST_CODE) {
            if (data == null) {
                return;
            }
            Bundle extras = data.getExtras();
            if (extras == null) {
                return;
            }
            Bitmap bm = extras.getParcelable("data");
            newZoomImage = zoomImage(bm, 600, 300);
//			sendImage(newZoomImage);
        }
//        else if (requestCode == SALES_RETURN) {
//            //销售认证界面返回，刷新数据
//            requestUserInfo();
//        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 通过Uri获取文件
     * @param ac
     * @param uri
     * @return
     */
    public static File getFileFromMediaUri(Context ac, Uri uri) {
        if(uri.getScheme().toString().compareTo("content") == 0){
            ContentResolver cr = ac.getContentResolver();
            Cursor cursor = cr.query(uri, null, null, null, null);// 根据Uri从数据库中找
            if (cursor != null) {
                cursor.moveToFirst();
                String filePath = cursor.getString(cursor.getColumnIndex("_data"));// 获取图片路径
                cursor.close();
                if (filePath != null) {
                    return new File(filePath);
                }
            }
        }else if(uri.getScheme().toString().compareTo("file") == 0){
            return new File(uri.toString().replace("file://",""));
        }
        return null;
    }

    /**
     * 通过uri获取图片并进行压缩
     * @param uri
     */
    public static Bitmap getBitmapFormUri(Activity ac, Uri uri) throws FileNotFoundException, IOException {
        InputStream input = ac.getContentResolver().openInputStream(uri);
        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        onlyBoundsOptions.inDither = true;//optional
        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        input.close();
        int originalWidth = onlyBoundsOptions.outWidth;
        int originalHeight = onlyBoundsOptions.outHeight;
        if ((originalWidth == -1) || (originalHeight == -1)) return null;
        //图片分辨率以480x800为标准
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (originalWidth > originalHeight && originalWidth > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (originalWidth / ww);
        } else if (originalWidth < originalHeight && originalHeight > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (originalHeight / hh);
        }
        if (be <= 0) be = 1;
        //比例压缩
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = be;//设置缩放比例
        bitmapOptions.inDither = true;//optional
        bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        input = ac.getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        input.close();

        return compressImage(bitmap);//再进行质量压缩
    }

    /**
     * 质量压缩方法
     * @param image
     * @return
     */
    public static Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            //第一个参数 ：图片格式 ，第二个参数： 图片质量，100为最高，0为最差  ，第三个参数：保存压缩后的数据的流
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据保存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    public static Bitmap zoomImage(Bitmap bgimage, double newWidth, double newHeight) {
        // 获取这个图片的宽和高
        float width = bgimage.getWidth();
        float height = bgimage.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width, (int) height, matrix, true);
        return bitmap;
    }

    /**
     * 调接口，上传图片到服务器
     * @param bm
     */
    private void sendImage(Bitmap bm) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] bytes = stream.toByteArray();

        String img = new String(Base64.encodeToString(bytes, Base64.DEFAULT));

//        try {
//            AsyncHttpClient client = new AsyncHttpClient();
//            RequestParams params = new RequestParams();
//            params.add("photo", img);
//            params.add("name", "headPhoto.jpg");
//            params.add("id", userId);
//            params.add("photoType", "headPhoto");
//            String url = Urls.URL_SUBMIT_PHOTO;
//            client.post(url, params, new AsyncHttpResponseHandler() {
//                @Override
//                public void onSuccess(int statusCode, Header[] headers, String content) {
//                    super.onSuccess(statusCode, headers, content);
//                    stopLoading();
//                    newZoomImage = ImageUtils.toRoundBitmap(newZoomImage); // 把图片处理成圆形
//                    img_photo.setImageBitmap(newZoomImage);
//                }

//                @Override
//                public void onFailure(Throwable error, String content) {
//                    super.onFailure(error, content);
//                }
//            });
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}
