package com.example.hansung.ifindthanq.addBLE;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.hansung.ifindthanq.R;

public class ADDBLEImageSearchPOP extends Activity {
    private static final int PICK_FROM_ALBUM = 1;

    private Uri imageUri;
    private ImageView searchImage;
    private String macs, name;

    //권한 추가
    private static final int REQUEST_PERMISSIONS = 1;
    private static final String[] MY_PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    public boolean hasAllPermissionsGranted() {
        for (String permission : MY_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, MY_PERMISSIONS, REQUEST_PERMISSIONS);
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_PERMISSIONS) {
            int index = 0;
            for (String permission : permissions) {
                if (permission.equalsIgnoreCase(Manifest.permission.READ_EXTERNAL_STORAGE) && grantResults[index] == PackageManager.PERMISSION_GRANTED) {
                    getGalley();
                    break;
                }
                index++;
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //타이틀바 제거
        setContentView(R.layout.activity_addbleimage_search_pop);

//        Button searchImage = (Button) findViewById(R.id.searchImage);
//        Button searchIcon = (Button) findViewById(R.id.searchIcon);

        Intent intent = getIntent();
        if (intent != null) {
            macs = intent.getStringExtra("macs");
            name = intent.getStringExtra("name");
        }
    }

    //사진 등록 클릭
    public void onSearchImage(View view) {
        if (ActivityCompat.checkSelfPermission(ADDBLEImageSearchPOP.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            boolean permission = hasAllPermissionsGranted();
            //Log.e("test","permission : "+permission);
            if (!permission)
                return;
        }
        getGalley();
    }

    private void getGalley() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FROM_ALBUM) {
            if (resultCode == Activity.RESULT_OK) {
                //Log.d("test","data : "+data);
                if (data != null) {
                    Uri returnImg = data.getData();
                    if ("com.google.android.apps.photos.contentprovider".equals(returnImg.getAuthority())) {
                        for (int i = 0; i < returnImg.getPathSegments().size(); i++) {
                            String temp = returnImg.getPathSegments().get(i);
                            if (temp.startsWith("content://")) {
                                returnImg = Uri.parse(temp);
                                break;
                            }
                        }
                    }
                    // 썸네일 가져오기
                    Bitmap bm = getThumbNail(returnImg);
//                    Toast.makeText(this, "이미지", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(this, AddBLEActivity.class);
                    intent.putExtra("img", bm);
                    intent.putExtra("macs", "" + macs);
                    intent.putExtra("name", "" + name);
                    startActivity(intent);
                    //imageView.setImageBitmap(getRoundedCornerBitmap(bm, 50));
                }
            }
        }
    }

    //아이콘 등록 클릭
    public void onSearchIcon(View view) {
        Intent intent = new Intent(this, ADDBLEImageIconSearchPOP.class);
        intent.putExtra("macs", "" + macs);
        intent.putExtra("name", "" + name);
        startActivity(intent);
    }

    //버튼 클릭
    public void onRegisterBLE(View v) {
        //데이터 전달하기
//        Intent intent = new Intent();
//        intent.putExtra("result", "Close Popup");
//        setResult(RESULT_OK, intent);
//
//        //액티비티(팝업) 닫기
//        finish();
//        Toast.makeText(this, "BLE 등록 누름", Toast.LENGTH_SHORT).show();
    }

    //닫기 버튼
    public void onCancel(View v) {
        //액티비티(팝업) 닫기
        finish();
//        Toast.makeText(this, "닫기 누름", Toast.LENGTH_SHORT).show();
    }

    //바깥레이어 클릭시 안닫히게
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
            return false;
        }
        return true;
    }

//    //안드로이드 백버튼 막기
//    @Override
//    public void onBackPressed() {
//        return;
//    }

    //사진
    private Bitmap getThumbNail(Uri uri) {
        //  Log.d("test","from uri : "+uri);
        String[] filePathColumn = {MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA, MediaStore.Images.Media.TITLE/*, MediaStore.Images.Media.ORIENTATION*/};

        ContentResolver cor = getContentResolver();
        //content 프로토콜로 리턴되기 때문에 실제 파일의 위치로 변환한다.
        Cursor cursor = cor.query(uri, filePathColumn, null, null, null);

        Bitmap thumbnail = null;
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            long ImageId = cursor.getLong(columnIndex);
            if (ImageId != 0) {
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                thumbnail = MediaStore.Images.Thumbnails.getThumbnail(
                        getContentResolver(), ImageId,
                        MediaStore.Images.Thumbnails.MINI_KIND,
                        bmOptions);
            } else {
                Toast.makeText(this, "불러올수 없는 이미지 입니다.", Toast.LENGTH_LONG).show();
            }
            cursor.close();
        }
        return thumbnail;
    }

    // 비트맵 모서리 둥글게
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int px) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = px;
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }


}
