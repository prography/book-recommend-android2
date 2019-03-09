package org.techtown.just;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;

import org.techtown.just.base.BaseActivity;
import org.techtown.just.model.LocalStore;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;


public class Mod_ProfileActivity extends BaseActivity implements View.OnClickListener{

    @BindView(R.id.btn_back)
    ImageView bt_back;


    //requestCode 선택한 사진에 대한 요청 값 구분 용도
    private final int CAMERA_CODE=1111;
    private final int GALLERY_CODE=1112;

    private CallbackManager callbackManager;
    private Login_FacebookActivity mLoginCallback;
    private KakaoSessionCallback kakaoSessionCallback;
    private Uri photoUri , albumUri , imageUri;
    private String currentPhotoPath;//파일경로
    String mlmageCaptureName;//이미지 이름

    ImageButton imageButton;
    Button rename, intro, facebook, cacao, logout, cancle;
    TextView tv_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mod__profile);

        ButterKnife.bind(this);


        String userId = getLocalStore().getStringValue(LocalStore.UserId);
        imageButton = (ImageButton) findViewById(R.id.img_profile);
        tv_name = (TextView) findViewById(R.id.profile_ID);
        tv_name.setText(userId);
        rename = (Button)findViewById(R.id.edit_name);
        facebook = (Button)findViewById(R.id.facebook_logout) ;

        imageButton.setBackground(new ShapeDrawable(new OvalShape()));
        if(Build.VERSION.SDK_INT>=21){
            imageButton.setClipToOutline(true);
        }

        imageButton.setOnClickListener(this);
        facebook.setOnClickListener(this);
        bt_back.setOnClickListener(this);

    }

    void show(){

        checkPermission();

        final CharSequence[] items = new CharSequence[]{"사진 촬영","앨범에서 선택"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("프로필 사진 설정방법을 선택하세요");

        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {

                switch(i)
                {
                    case 0 : //사진촬영

                        int permissionCheck = ContextCompat.checkSelfPermission(Mod_ProfileActivity.this,Manifest.permission.CAMERA);
                        int writePermission = ContextCompat.checkSelfPermission(Mod_ProfileActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE);
//                            Toast.makeText(getApplicationContext(),"카메라 권한 없음", Toast.LENGTH_SHORT).show();

                        //ActivityCompat.requestPermissions(My_Profile.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);

                        //하나라도 권한이 없으면
                        if(permissionCheck==PackageManager.PERMISSION_DENIED || writePermission==PackageManager.PERMISSION_DENIED) {
                            checkPermission();
                            //Toast.makeText(getApplicationContext(), "저장소 저장권한 없음", Toast.LENGTH_SHORT).show();
                        }
                        else{   //권한 o
                            captureCamera();
                        }
                        break;

                    case 1 : //앨범에서 선택
                        selectGallery();
                        break;
                }
                dialog.dismiss();

            }
        });
        builder.show();

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        //super.onRequestPermissionsResult(requestCode,permissions, grantResults);
        switch (requestCode){
            case 0 :
                for(int i=0; i<grantResults.length; i++){
                    if(grantResults[i]<0){
                        Toast.makeText(Mod_ProfileActivity.this,"해당 권한을 활성화 해야 합니다.",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    break;
                }
        }
//        if(requestCode==0){ //camera
//            if(grantResults[0]==0){
//                Toast.makeText(this,"카메라 권한 승인됨",Toast.LENGTH_SHORT).show();
//            }else{
//                Toast.makeText(this,"카메라 권한이 거절됨",Toast.LENGTH_SHORT).show();
//            }
//        }else //requestcode==1 // External
//        {
//            if(grantResults[0]==0){
//                Toast.makeText(this,"저장소 권한 승인됨",Toast.LENGTH_SHORT).show();
//            }else{
//                Toast.makeText(this,"저장소 권한이 거절됨",Toast.LENGTH_SHORT).show();
//            }
//        }

    }

    //갤러리에서 사진을 가져오는 경우
    private void selectGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent,GALLERY_CODE);
    }

    private void captureCamera(){
        String state = Environment.getExternalStorageState();
        //외장메모리 검사
        if(Environment.MEDIA_MOUNTED.equals(state)){
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if(intent.resolveActivity(getPackageManager())!=null){
                File photoFile = null;
                try{
                    photoFile = createImageFile();
                }catch (IOException ex){
                    ex.printStackTrace();
                }if(photoFile!=null){
                    Uri providerUri=FileProvider.getUriForFile(this,getPackageName(),photoFile);
                    imageUri = providerUri; //인자를 content::로 넘겨주기 위함.
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, providerUri);
                    startActivityForResult(intent,CAMERA_CODE);
                } else{
                    Toast.makeText(this,"저장공간이 접근 불가능한 기기입니다.",Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }

    }

    //카메라로 찍은 사진을 실제 파일로 저장하는 코드  * path이름 일치
    private File createImageFile() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        mlmageCaptureName = timeStamp+".png";

        File imageFile = null;
//        File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File dir = new File(Environment.getExternalStorageDirectory()+"/Pictures","BookRecommend");
        if(!dir.exists()){
            dir.mkdirs();
        }
        // File imageFile = File.createTempFile(mlmageCaptureName,".png",dir);
        // File storageDir = new File(Environment.getExternalStorageDirectory().getAbsoluteFile()+"/Pictures/BookRecommend"+mlmageCaptureName);
        imageFile = new File(dir,mlmageCaptureName);
        currentPhotoPath = imageFile.getAbsolutePath();


        return imageFile;
    }


    private void galleryAddPic(){
        Log.i("galleryAddpic", "call");
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);
        Toast.makeText(this,"사진이 앨범에 저장되었습니다.",Toast.LENGTH_SHORT).show();

    }


    private void sendPicture(Uri imgUri){
        String imagePath = getRealPathFromURI(imgUri); //path경로
        ExifInterface exif = null;
        try{
            exif = new ExifInterface(imagePath);
        }catch(IOException e){
            e.printStackTrace();
        }
        int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,ExifInterface.ORIENTATION_NORMAL);
        int exifDegree = exifOrientationToDegrees(exifOrientation);


        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        imageButton.setImageBitmap(rotate(bitmap,exifDegree));
    }

    //카메라로 찍은 사진 적용
    private void getPictureForPhoto(){
        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath);
        ExifInterface exif = null;
        try{
            exif = new ExifInterface(currentPhotoPath);
        }catch (IOException e){
            e.printStackTrace();
        }
        int exifOrientation;
        int exifDegree;

        if(exif != null){
            exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            exifDegree = exifOrientationToDegrees(exifOrientation);
        }else{
            exifDegree=0;
        }
        //이미지 뷰에 비트맵 넣기
        imageButton.setImageBitmap(rotate(bitmap,exifDegree));
    }

    //선택한 사진 데이터 처리
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode, data);

        if(resultCode==RESULT_OK){

            switch(requestCode){
                case  GALLERY_CODE :
                    if(data.getData()!=null){
                        //sendPicture(data.getData());
                        try{
                            File albumFile = null;
                            albumFile = createImageFile();
                            photoUri = data.getData();

                            albumUri = Uri.fromFile(albumFile);

                            //galleryAddPic();
                            imageButton.setImageURI(photoUri);
                            sendPicture(photoUri);

                            Bundle extras = data.getExtras();
                            Bitmap imageBitmap = (Bitmap) extras.get("data");
                            imageButton.setImageBitmap(imageBitmap);


                        }catch (Exception e){}
                    }
                    break;
                case CAMERA_CODE:

                    try {
//                        File albumFile = null;
//                        albumFile = createImageFile();
//                        photoUri = data.getData();
//
//                        albumUri = Uri.fromFile(albumFile);

//                    imageButton.setImageURI(photoUri);

                        getPictureForPhoto();

                        galleryAddPic();

                        Bundle extras = data.getExtras();
                        Bitmap imageBitmap = (Bitmap) extras.get("data");
                        imageButton.setImageBitmap(imageBitmap);

                    }catch (Exception e){

                    }
                    break;
            }
        }
    }


    //사진 회전 값 가져오기 <- 미처리시 사진을 찍은 방향대로 이미지뷰에 처리되지 않음.
    private int exifOrientationToDegrees(int exifOrientation){
        if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_90){
            return 90;
        }else if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_180){
            return 180;
        }else if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_270){
            return 270;
        }
        return 0;
    }

    private Bitmap rotate(Bitmap src, float degree){
        //Matrix 객체 생성
        Matrix matrix = new Matrix();
        //회전 각도
        matrix.postRotate(degree);

        return Bitmap.createBitmap(src,0,0,src.getWidth(),src.getHeight(),matrix,true);
    }

    //사진의 절대 경로 구하기
    private String getRealPathFromURI(Uri contentUri){
        int column_index=0;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor =  getContentResolver().query(contentUri,proj, null, null, null);
        if(cursor.moveToFirst()){
            column_index=cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        }
        return cursor.getString(column_index);
    }

    private void checkPermission(){ // + 읽기 권한
        //둘 중 하나라도 권한이 없을 때
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE ) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA ) != PackageManager.PERMISSION_GRANTED ) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);

//            //최초 권한 요청이면 else로 , 재요청이면 if문
//            if ((ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) || (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA))) {
//                new AlertDialog.Builder(this).setTitle("알림").setMessage("권한이 거부되었습니다. 설정에서 권한을 주세요!").setNegativeButton("설정", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                        intent.setData(Uri.parse("package:" + getPackageName()));
//                        startActivity(intent);
//                    }
//                }).setPositiveButton("확인", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        finish();
//                    }
//                }).setCancelable(false).create().show();
//            } else {
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
//            }
        }
        //재요청이면 if문
        else if ((ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) || (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA))) {
            new AlertDialog.Builder(this).setTitle("알림").setMessage("권한이 거부되었습니다. 설정에서 권한을 주세요!").setNegativeButton("설정", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.setData(Uri.parse("package:" + getPackageName()));
                    startActivity(intent);
                }
            }).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            }).setCancelable(false).create().show();
        }
//        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA ) != PackageManager.PERMISSION_GRANTED){
//            //최초 권한 요청이면 else로 , 재요청이면 if문
//            if((ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.CAMERA)))
//            {
//                new AlertDialog.Builder(this).setTitle("알림").setMessage("카메라 권한이 거부되었습니다. 설정에서 권한을 주세요!").setNegativeButton("설정", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                        intent.setData(Uri.parse("package:"+getPackageName()));
//                        startActivity(intent);
//                    }
//                }).setPositiveButton("확인", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        finish();
//                    }
//                }).setCancelable(false).create().show();
//            }
//        }else{
//                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},0);
//
//        }

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_back :
                finish();

                break;
            case R.id.img_profile:
                show();
                break;

            case R.id.facebook_logout:
                //callbackManager = CallbackManager.Factory.create();
                mLoginCallback = new Login_FacebookActivity();
                mLoginCallback.disconnectFromFacebook();

                Intent intent = new Intent(this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                startActivity(intent);

                break;

            case R.id.edit_name:
                //Intent intent = new Intent(getApplicationContext(), RenameActivity.class);
                //intent.putExtra("nickname",tv_name.getText());
                //startActivity(intent);
                break;

//            case R.id.kakao_logout :
//                if(Session.getCurrentSession().isOpened()){
//                    kakaoSessionCallback.requestLogout();
//                }
//                break;
        }
    }
}
