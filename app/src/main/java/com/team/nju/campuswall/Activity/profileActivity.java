package com.team.nju.campuswall.Activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.team.nju.campuswall.Adapter.messageListAdapter;
import com.team.nju.campuswall.App;
import com.team.nju.campuswall.Model.UserModel;
import com.team.nju.campuswall.Network.NetworkCallbackInterface;
import com.team.nju.campuswall.Network.StatusCode;
import com.team.nju.campuswall.Network.netRequest;
import com.team.nju.campuswall.R;
import com.team.nju.campuswall.Util.CommonUrl;
import com.team.nju.campuswall.Util.CommonUtils;
import com.team.nju.campuswall.Util.UploadPhotoUtil;
import com.team.nju.campuswall.View.CircleImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;

public class profileActivity extends Activity implements NetworkCallbackInterface.NetRequestIterface {

    private netRequest requestFragment;
    ImageView photo;
    TextView username;
    TextView school;
    TextView product;
    TextView join;
    TextView star;
    TextView signature;

    Button back;
    Button changeprofile;

    String phone;
    String nickname;
    String password;
    String sex;
    String sign;
    String userurl;
    TextView take_picture;
    TextView select_local_picture;
    FrameLayout edit_photo_fullscreen_layout;
    RelativeLayout edit_photo_outer_layout;
    UserModel userModel;

    Intent intent;
    private final int NONE=0,TAKE_PICTURE=1,LOCAL_PICTURE=2,UPLOAD_TAKE_PICTURE=4,SAVE_THEME_IMAGE=5;
    private int addTakePicCount=1;
    private String takePictureUrl,upToken, imageFileName;
    private ProgressDialog progressDlg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Bundle data = getIntent().getExtras();
        phone = data.getString("phone");

        requestFragment = new netRequest(this, this);

        changeprofile = (Button) findViewById(R.id.bt_edit);
        username = (TextView) findViewById(R.id.profile_username);
        school = (TextView) findViewById(R.id.profile_school);
        product = (TextView) findViewById(R.id.profile_product);
        join = (TextView) findViewById(R.id.profile_join);
        star = (TextView) findViewById(R.id.profile_star);
        signature = (TextView) findViewById(R.id.profile_signature);
        back=(Button)findViewById(R.id.bt_exit);
        photo = (CircleImageView) findViewById(R.id.photo);
        take_picture = (TextView) findViewById(R.id.take_picture);
        select_local_picture = (TextView) findViewById(R.id.select_local_picture);

        //下部弹出层
        edit_photo_fullscreen_layout = (FrameLayout) findViewById(R.id.edit_photo_fullscreen_layout);
        edit_photo_fullscreen_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_photo_fullscreen_layout.setVisibility(View.GONE);
            }
        });
        edit_photo_outer_layout = (RelativeLayout) findViewById(R.id.edit_photo_outer_layout);
        //取消更改头像
        TextView cancelEditPhoto = (TextView) edit_photo_outer_layout.findViewById(R.id.cancel_upload);
        cancelEditPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_photo_fullscreen_layout.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        initInfo();
    }

    private void initInfo() {
        Map map = new HashMap();
        map.put("phone", phone);
        map.put("type", StatusCode.REQUEST_PROFILE);
        requestFragment.httpRequest(map, CommonUrl.getProfile);

        //监听
        changeprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(profileActivity.this, EditProfile.class);
                Bundle data = new Bundle();
                data.putString("phone", phone);
                data.putString("nickname", nickname);
                data.putString("sex", sex);
                data.putString("password", password);
                data.putString("signature", sign);

                intent.putExtras(data);
                startActivity(intent);
                finish();
            }
        });
       back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           finish();
                return;
            }
        });
        //点击头像
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_photo_fullscreen_layout.setVisibility(View.VISIBLE);
                Animation get_photo_layout_in_from_down = AnimationUtils.loadAnimation(profileActivity.this, R.anim.search_layout_in_from_down);
                edit_photo_outer_layout.startAnimation(get_photo_layout_in_from_down);
            }
        });
        //点击拍照
        take_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_photo_fullscreen_layout.setVisibility(View.GONE);
                takePictureUrl= App.photo_path+"picture_take_0"
                        +addTakePicCount+".jpg";
                File file=new File(takePictureUrl);
                intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                startActivityForResult(intent,TAKE_PICTURE);
                addTakePicCount++;
            }
        });
        //点击选择本地图片
        select_local_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_photo_fullscreen_layout.setVisibility(View.GONE);
                Intent intent=new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        "image/*");
                startActivityForResult(intent,LOCAL_PICTURE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==NONE)
            return;
        //耗时操作传到handler里去处理
        if(requestCode==TAKE_PICTURE){
            handler.sendEmptyMessage(TAKE_PICTURE);
            return;
        }
        if(resultCode== Activity.RESULT_OK){
            this.intent=data;
            handler.sendEmptyMessage(LOCAL_PICTURE);
        }
    }

    @Override
    public void requestFinish(String result, String requestUrl) throws JSONException {
        Message message = new Message();
        //获取基本资料
        if (requestUrl.equals(CommonUrl.getProfile)) {
            JSONObject object = new JSONObject(result);
            int code = Integer.valueOf(object.getString("code"));
            if (code == StatusCode.REQUEST_PROFILE_SUCCESS) {
                Gson gson = new Gson();
                userModel = gson.fromJson(object.getJSONArray("contents").getJSONObject(0).toString(), UserModel.class);
                message.what = StatusCode.REQUEST_PROFILE_SUCCESS;
                handler.sendMessage(message);
                return;
            } else {
              //  Toast.makeText(profileActivity.this, "未知错误", Toast.LENGTH_LONG).show();
                return;
            }
        }
        //获取token
         if(requestUrl.equals((CommonUrl.editProfile))){
            JSONObject jsonObject=new JSONObject(result);
        int code = Integer.valueOf(jsonObject.getString("code"));
        if (code == StatusCode.REQUEST_TOKEN_SUCCESS) {
            JSONArray token = jsonObject.getJSONArray("contents");
            upToken = token.getString(0);//成功获取口令
            handler.sendEmptyMessage(UPLOAD_TAKE_PICTURE);
            return;
         }
        else if (code == StatusCode.REQUEST_CHANGE_PHOTO_SUCCESS) {
            progressDlg.dismiss();
//            Toast.makeText(profileActivity.this, "修改成功", Toast.LENGTH_LONG).show();
            return;
        }
       }
    }

    @Override
    public void exception(IOException e, String requestUrl) {
//        Message msg=new Message();
//        msg.what=StatusCode.REQUEST_FAILURE;
//        msg.obj="网络请求失败";
//        // mHandler.sendMessage(msg);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case StatusCode.REQUEST_PROFILE_SUCCESS://成功返回所有动态
                {
                    nickname = userModel.getUalais();
                    password = userModel.getUpassword();
                    sign = userModel.getUsign();
                    sex = userModel.getUsex();
                    userurl=userModel.getUserurl();

                    if(userurl!=null) {
                        Log.d("url",userurl);
                        Glide.with(getApplicationContext())
                                .load(userurl).centerCrop()
                                .into(photo);
                    }
                    username.setText(nickname);

                    if (!signature.equals(""))
                        signature.setText(sign);
                    else
                        signature.setText("未填写");
                    break;
                }
                case TAKE_PICTURE:
                    //在这里处理，获取拍到的图
                    Log.d("PicUrl:",takePictureUrl);
                    Bitmap bitmap= UploadPhotoUtil.getInstance()
                            .trasformToZoomPhotoAndLessMemory(takePictureUrl);
                    BitmapDrawable bd=new BitmapDrawable(getResources(),bitmap);
                    Drawable addPicture = bd;
                    photo.setImageDrawable(addPicture);
                {
                    //获取token
                    HashMap map = new HashMap();
                    map.put("type", StatusCode.REQUEST_ASK_TOKEN);
                    String[] ext = takePictureUrl.split("\\.");
                    imageFileName = phone + "/" + takePictureUrl.hashCode() + new Random(System.nanoTime()).toString() + ext[ext.length - 1];
                    ArrayList<String>temp=new ArrayList<>();
                    temp.add("\""+imageFileName+"\"");
                    map.put("image", temp);
                    Log.d("选择了拍照后", "正在请求服务器端");
                    requestFragment.httpRequest(map,CommonUrl.editProfile);
                }
                    break;
                case LOCAL_PICTURE:
                    //处理本地选择的图片
                    Uri uri=intent.getData();
                    String photo_local_file_path=getPath_above19(profileActivity.this, uri);
                    if (!(photo_local_file_path.toString().toLowerCase().endsWith("jpg")||photo_local_file_path.toString().toLowerCase().endsWith("png")
                            ||photo_local_file_path.toString().toLowerCase().endsWith("jpeg")||photo_local_file_path.toString().toLowerCase().endsWith("gif"))){
                        Toast.makeText(profileActivity.this,"不支持此格式的上传", Toast.LENGTH_LONG).show();
                        break;
                    }
                    Bitmap bitmap2=UploadPhotoUtil.getInstance().trasformToZoomPhotoAndLessMemory(photo_local_file_path);
                    addPicture =new BitmapDrawable(getResources(), bitmap2);
                    takePictureUrl=photo_local_file_path;
                    photo.setImageDrawable(addPicture);
                {
                    HashMap map = new HashMap();
                    map.put("type", StatusCode.REQUEST_ASK_TOKEN);
                    String[] ext = takePictureUrl.split("\\.");
                    imageFileName = phone + "/" + takePictureUrl.hashCode() + new Random(System.nanoTime()).toString() + ext[ext.length - 1];
                    ArrayList<String>temp=new ArrayList<>();
                    temp.add("\""+imageFileName+"\"");
                    map.put("image", temp);
                    Log.d("选择了本地照片", "正在请求服务器端获取token");
                    requestFragment.httpRequest(map,CommonUrl.editProfile);
                }
                    break;
                case UPLOAD_TAKE_PICTURE:  //获取token之后
                {
                    UploadManager uploadmgr = new UploadManager();
                    File data = new File(takePictureUrl);
                    String key = imageFileName;
                    String token = upToken;
                    progressDlg = ProgressDialog.show(profileActivity.this, "上传头像", "正在上传图片", true, true, new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialogInterface) {
                            //取消了上传
                        }
                    });
                    progressDlg.setMax(101);
                    uploadmgr.put(data, key, token, new UpCompletionHandler() {
                        @Override
                        public void complete(String key, ResponseInfo info, JSONObject response) {
                            //完成，发信息给业务服务器
                            Log.d("to第三方结果",response.toString());
                            new Thread() {
                                public void run() {
                                    Map<String, Object> map = new HashMap<>();
                                    Message msg = handler.obtainMessage();
                                    msg.obj = map;
                                    msg.what = SAVE_THEME_IMAGE;
                                    handler.sendMessage(msg);//要上传的图片包装在msg后变成了消息发到handler
                                }
                            }.start();
                        }
                    }, new UploadOptions(null, null, false,
                            new UpProgressHandler() {
                                public void progress(String key, double percent) {
                                    //mProgress.setProgress((int)percent*100);
                                    progressDlg.setProgress((int) percent * 100);
                                }
                            }, null));
                }
                    break;
                case SAVE_THEME_IMAGE: //传到第三方之后告知服务器
                    progressDlg.dismiss();
                {
                    HashMap map = new HashMap();
                    map.put("type", StatusCode.REQUEST_CHANGE_PHOTO);
                    map.put("utel", phone);
                    ArrayList<String> temp = new ArrayList<>();
                    temp.add("\"" + imageFileName + "\"");
                    map.put("image", temp);
                    requestFragment.httpRequest(map, CommonUrl.editProfile);
                }
                    progressDlg=ProgressDialog.show(profileActivity.this, "上传头像", "正在保存头像", true, true, new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialogInterface) {
                            //上传完图片后取消了保存
                        }
                    });
                    break;
                default: //用户身份认证失败
                {

                    break;
                }
            }
        }
    };


    /*************
     */
    //是否为外置存储器
    public static boolean isExternalStorageDocument(Uri uri){
        return"com.android.externalstorage.documents".equals(uri.getAuthority());
    }
    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri){
        return"com.android.providers.downloads.documents".equals(uri.getAuthority());
    }
    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri){
        return"com.android.providers.media.documents".equals(uri.getAuthority());
    }
    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.*/

    public static boolean isGooglePhotosUri(Uri uri){
        return"com.google.android.apps.photos.content".equals(uri.getAuthority());
    }
    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[]selectionArgs){
        Cursor cursor=null;
        final String column="_data";
        final String[]projection={column};
        try{
            cursor=context.getContentResolver().query(uri,projection,selection,selectionArgs, null);
            if(cursor!=null&&cursor.moveToFirst()){
                final int index=cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        }finally{
            if(cursor!=null)
                cursor.close();
        }
        return null;
    }
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getPath_above19(final Context context,final Uri uri){
        final boolean isKitKat=Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if(isKitKat&& DocumentsContract.isDocumentUri(context, uri)){
            // ExternalStorageProvider
            if(isExternalStorageDocument(uri)){
                final String docId=DocumentsContract.getDocumentId(uri);
                final String[]split=docId.split(":");
                final String type=split[0];
                if("primary".equalsIgnoreCase(type)){
                    return Environment.getExternalStorageDirectory()+"/"+split[1];
                }

            }
            // DownloadsProvider
            else if(isDownloadsDocument(uri)){
                final String id=DocumentsContract.getDocumentId(uri);
                final Uri contentUri= ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context,contentUri,null,null);
            }
            // MediaProvider
            else if(isMediaDocument(uri)){
                final String docId=DocumentsContract.getDocumentId(uri);
                final String[]split=docId.split(":");
                final String type=split[0];
                Uri contentUri=null;
                if("image".equals(type)){
                    contentUri= MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                }else if("video".equals(type)){
                    contentUri=MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                }else if("audio".equals(type)){
                    contentUri=MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection="_id=?";
                final String[]selectionArgs=new String[]{
                        split[1]
                };
                return getDataColumn(context,contentUri,selection,selectionArgs);
            }
        }
        // MediaStore (and general)
        else if("content".equalsIgnoreCase(uri.getScheme())){
            // Return the remote address
            if(isGooglePhotosUri(uri))
                return uri.getLastPathSegment();
            return getDataColumn(context,uri,null,null);
        }
        // File
        else if("file".equalsIgnoreCase(uri.getScheme())){
            return uri.getPath();
        }
        return null;
    }
}
