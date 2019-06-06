package com.wisn.medial.compressor;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.wisn.medial.R;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;

public class CompressorActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 100;
    private TextView result;
    private String resultPath;
    private ImageView image;
    private EditText et_width;
    private EditText et_height;
    private EditText et_quality;

    int width = 100;
    int height = 100;
    int quality = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compressor);
        result = findViewById(R.id.result);
        image = findViewById(R.id.image);
        et_width = findViewById(R.id.et_width);
        et_height = findViewById(R.id.et_height);
        et_quality = findViewById(R.id.et_quality);
        resultPath = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES).getAbsolutePath();
    }

    public void updateView(final String msg, final boolean isAppend) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isAppend) {
                    result.append(msg + "\n");
                } else {
                    result.setText(msg);
                }
            }
        });
    }


    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.bt_compressor:
                Intent intent = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // 打开手机相册,设置请求码
                startActivityForResult(intent, REQUEST_CODE);
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            try {
                width = Integer.parseInt(et_width.getText().toString());
                height = Integer.parseInt(et_height.getText().toString());
                quality = Integer.parseInt(et_quality.getText().toString());
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            // 获取选中图片的路径
            Cursor cursor = getContentResolver().query(intent.getData(), null, null, null, null);
            if (cursor.moveToFirst()) {
                String photoPath = cursor.getString(cursor
                        .getColumnIndex(MediaStore.Images.Media.DATA));
                if (!TextUtils.isEmpty(photoPath)) {
                    File file = new File(photoPath);
                    if (file.exists()) {
                        updateView(getFormatSize(file.length()), false);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    File file1 = CompressorUtils.compressImage(file, width, height, Bitmap.CompressFormat.WEBP, quality, resultPath + File.separator + file.getName());
                                    updateView("\r\n压缩后" + getFormatSize(file1.length()), true);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            BitmapFactory.Options options = new BitmapFactory.Options();
                                            options.inJustDecodeBounds = false;
                                            Bitmap bmp = BitmapFactory.decodeFile(file1.getAbsolutePath(), options);
                                            image.setImageBitmap(bmp);
                                        }
                                    });

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }
                } else {
                    updateView("error", false);
                }
            }
            cursor.close();

        }
    }


    /**
     * 格式化单位
     *
     * @param size
     */
    public static String getFormatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return size + "Byte";
        }
        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB";
        }
        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB";
        }
        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB";
    }
}
