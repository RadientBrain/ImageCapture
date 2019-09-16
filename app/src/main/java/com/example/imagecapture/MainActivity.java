
package com.example.imagecapture;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    ImageView captured;
    TextView show_count;
    Button capture_it, reset, save;
    int count = 0, save_click = 1;
    private boolean isOpPressed = false;
    int firsttime=0;


    private static final int PERMISSION_CODE = 1000;
    private static final int IMAGE_CAPTURE_CODE = 1001;

    BitmapDrawable drawable;
    Bitmap bitmap;


    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("**Exiting ImageCapture**");
        builder.setIcon(R.drawable.ic_exit);
        builder.setMessage("Are you Sure you want to exit?").setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        MainActivity.super.onBackPressed();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    //handling permission result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //this method is called, when user presses Allow or Deny from Permission Request Popup
        switch (requestCode) {
            case PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED) {
                    //permission from popup was granted
                    openCamera();
                } else {
                    //permission from popup was denied
                    Toast.makeText(this, "Allow Permissions...", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void openCamera() {
        //system os < marshmallow or permission already granted
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera");

        //camera intent
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, 0);
        startActivityForResult(intent, IMAGE_CAPTURE_CODE);
        count++;
        show_count.setText(Integer.toString(count));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bitmap mybitmap = (Bitmap) data.getExtras().get("data");
        captured.setImageBitmap(mybitmap);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        save_click = 1;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        captured = (ImageView) findViewById(R.id.img_captured);
        capture_it = (Button) findViewById(R.id.btn_capture);
        reset = (Button) findViewById(R.id.btn_reset);
        show_count = (TextView) findViewById(R.id.view_count);
        save = (Button) findViewById(R.id.btn_save);

        capture_it.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save_click = 1;
                //if system os is >= marshmallow, request runtime permission
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.CAMERA) ==
                            PackageManager.PERMISSION_DENIED ||
                            checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                                    PackageManager.PERMISSION_DENIED) {
                        //permission not enabled, request it
                        String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        //show popup to request permissions
                        requestPermissions(permission, PERMISSION_CODE);
                    } else {
                        //permission already granted
                        isOpPressed = true;
                        openCamera();

                    }
                } else {
                    //system os < marshmallow
                    isOpPressed = true;
                    openCamera();
                }
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isOpPressed && save_click==1) {
                    drawable = (BitmapDrawable) captured.getDrawable();
                    bitmap = drawable.getBitmap();

                    FileOutputStream outputStream = null;

                    File sdcard = Environment.getExternalStorageDirectory();
                    File directory = new File(sdcard.getAbsolutePath() + "/ImageCapture");

                    directory.mkdir();
                    String fileName = String.format("%d.jpg", System.currentTimeMillis());

                    File outFile = new File(directory, fileName);
                    save_click++;
                    Toast.makeText(MainActivity.this, "Image saved in Gallery", Toast.LENGTH_SHORT).show();

                    try {
                        outputStream = new FileOutputStream(outFile);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                        outputStream.flush();
                        outputStream.close();

                        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                        intent.setData(Uri.fromFile(outFile));
                        sendBroadcast(intent);


                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                else if (firsttime==0) {
                    firsttime++;
                    drawable = (BitmapDrawable) captured.getDrawable();
                    bitmap = drawable.getBitmap();

                    FileOutputStream outputStream = null;

                    File sdcard = Environment.getExternalStorageDirectory();
                    File directory = new File(sdcard.getAbsolutePath() + "/ImageCapture");

                    directory.mkdir();
                    String fileName = String.format("%d.jpg", System.currentTimeMillis());

                    File outFile = new File(directory, fileName);
                    save_click++;
                    Toast.makeText(MainActivity.this, "Image saved in Gallery", Toast.LENGTH_SHORT).show();

                    try {
                        outputStream = new FileOutputStream(outFile);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                        outputStream.flush();
                        outputStream.close();

                        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                        intent.setData(Uri.fromFile(outFile));
                        sendBroadcast(intent);


                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                else {
                    Toast.makeText(MainActivity.this, "Image already saved in Gallery", Toast.LENGTH_SHORT).show();
                }
            }

        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count = 0;
                show_count.setText(Integer.toString(count));
            }
        });
    }

}
