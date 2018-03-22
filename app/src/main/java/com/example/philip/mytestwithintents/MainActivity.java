package com.example.philip.mytestwithintents;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.database.Cursor; //??okej?
import android.content.Context; //????????????
import android.net.Uri; //?????
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = AppCompatActivity.class.getName();
    public String lastCameraFileUri;

    static final int REQUEST_OPEN_IMAGE = 1;

    static final int REQUEST_OPEN_CAMERA = 2;
    static final int REQUEST_PERMISSION_CAM = 3;
    static final int REQUEST_TEST_PERMISSION_CAM = 4;
    static final int REQUEST_TEST_PERMISSION_WRITESTORAGE = 5;

    static boolean ALL_PERMISSIONS_OK = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "getApplicationContext().getPackageName()!!: " + getApplicationContext().getPackageName());
        Log.d(TAG, "SDK version is: " + Build.VERSION.SDK_INT);
        Log.d(TAG, "Applicationid  is: " + BuildConfig.APPLICATION_ID);

        forceRightPermissions();




        final Button button = findViewById(R.id.buttonX);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                Log.d(TAG, "hello123");
                Log.d(TAG, "This is intent.getType(): " + intent.getType());

                intent.setType("image/*");
                Log.d(TAG, "This is intent.getType(): " + intent.getType());
                Log.d(TAG, "this.getClass() : " + this.getClass());
                startActivityForResult(intent, REQUEST_OPEN_IMAGE);
            }
        });


        final Button callCameraBtn = findViewById(R.id.callCameraButton);
        callCameraBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                dispatchTakePictureIntent();

            }
        });

        final Button buttonTestCamPerm = findViewById(R.id.buttonTestCamPerm);
        buttonTestCamPerm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d(TAG, "Inside buttonTestCamPerm");

                testCameraPermission();


            }
        });


        final Button buttonDoCamPerm = findViewById(R.id.buttonDoCameraPerm);
        buttonDoCamPerm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d(TAG, "Inside buttonDoCameraPerm");

                doCameraPermission();
            }
        });


        final Button buttonTestStoragePerm = findViewById(R.id.testStoragePerm);
        buttonTestStoragePerm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d(TAG, "Inside buttonTestStoragePerm");

                testStorageWritePerrmission();
            }
        });

        final Button buttonDoStoragePerm = findViewById(R.id.doStoragePerm);
        buttonDoStoragePerm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d(TAG, "Inside buttonDoStoragePerm");
                doStorageWritePerrmission();
            }
        });

    }

    private void forceRightPermissions() {
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            //Om den inte har behörighet, exekvera behörighetsskylten.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_TEST_PERMISSION_CAM);
        }

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_TEST_PERMISSION_WRITESTORAGE);


    }

    private void doCameraPermission() {
        Log.d(TAG, "Before requestPermission");

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_TEST_PERMISSION_CAM);

        Log.d(TAG, "Before requestPermission");
    }

    private void testCameraPermission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED) {
            Log.d(TAG, "true. Camera. Permission is denied");
        } else
            Log.d(TAG, "false. You have access to camera");
        Log.d(TAG, "after permission IF");
    }



    private void doStorageWritePerrmission() {
        Log.d(TAG, "Before requestPermission");

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_TEST_PERMISSION_WRITESTORAGE);

        Log.d(TAG, "Before requestPermission");
    }

    private void testStorageWritePerrmission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_DENIED) {
            Log.d(TAG, "true. Ext Storage. Permission is denied");
        } else
            Log.d(TAG, "false. You have access to ext storage");
        Log.d(TAG, "after permission IF ext storage");
    }




    //Denna anropas alltid när en activity är typ klar. Den funkar osm ett slot i QT.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_OPEN_IMAGE) {
            Log.d(TAG, "HELLO, This is data: " + data.getDataString());

            //Lite avancerad
            String filePath = getRealPathFromURI(getApplicationContext(), data.getData());
            Log.d(TAG, "WHat could this filePath be? " + filePath);
        } else if (requestCode == REQUEST_OPEN_CAMERA) {
            Log.d(TAG, "We arrived here, but i do not do anything with it");
            //och vad gör männskann här. När hen har fått tillbaka en bild
        } else if (requestCode == REQUEST_PERMISSION_CAM) {
            Log.d(TAG, "Done. in request code REQUEST_PERMISSION_CAM");

        } else if (requestCode == REQUEST_TEST_PERMISSION_CAM) {
            Log.d(TAG, "Done. in request code REQUEST_TEST_PERMISSION_CAM");
        } else if (requestCode == REQUEST_TEST_PERMISSION_WRITESTORAGE) {
            Log.d(TAG, "Done. in request code REQUEST_TEST_PERMISSION_WRITESTORAGE");
        }

    }
//Anropas endast vid request image! inte vid kameraanrop

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        //Frivilligt:  super.onActivityResult(requestCode, resultCode, data);

    }


    //Call Camera funktionalities
    //Den här ska starta upp kameran. Jag tror den ber android att starta kameran
    // //så användaren kan ta en bild. MEn jag förstår inte varför man ska bland in en fil just då.
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Ensure that there’s a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go

            File photoFile = null;
            try {

                photoFile = createImageFile();

            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.d(TAG, "ERROR ex.getLocalizedMessage() : " + ex.getLocalizedMessage());
                Log.d(TAG, "ERROR ex.getMessage()          : " + ex.getMessage());

            }

            // Continue only if the File was successfully created
            if (photoFile != null) {


                //Denna getApplicationContext().getPackageName() returnerar com.example.philip.mytestwithintents
                Uri photoURI = FileProvider.getUriForFile(this,
                        getApplicationContext().getPackageName()+".fileprovider",
                        photoFile);
                lastCameraFileUri = photoFile.toString();
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_OPEN_CAMERA);


                //Gammalt sätt
                /*
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                lastCameraFileUri = photoFile.toString();
                startActivityForResult(takePictureIntent, REQUEST_OPEN_CAMERA);
                */
            }
        } else {
            Log.d(TAG, "ERROR. Problems with your camera?!");
        }
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());


        String imageFileName = "MYAPPTEMP_" + timeStamp + " ";


        //Okej, här får vi platsen där alla bilder sparas
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        Log.d(TAG, "This is storageDir.toString() : " + storageDir.toString());



        File image = File.createTempFile(imageFileName,".jpg",storageDir );
        Log.d(TAG, "This is image.toString()      : " + image.toString());


        return image;
    }




}
