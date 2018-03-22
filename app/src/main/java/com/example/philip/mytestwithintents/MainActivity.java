package com.example.philip.mytestwithintents;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = AppCompatActivity.class.getName();
    public String lastCameraFileUri;

    static final int REQUEST_OPEN_IMAGE = 1;

    static final int REQUEST_OPEN_CAMERA = 2;
    static final int REQUEST_PERMISSIONS = 3;

    static boolean ALL_PERMISSIONS_OK = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "getApplicationContext().getPackageName()!!: " + getApplicationContext().getPackageName());
        Log.d(TAG, "SDK version is: " + Build.VERSION.SDK_INT);
        Log.d(TAG, "Applicationid  is: " + BuildConfig.APPLICATION_ID);

        //THIS IS ONLY ALLOWED IN PROTOTYPE SOFTWARE, in a real realease on Google Play you have to handle
        //permissions if the user presses DENY.
        String[] permissions = {Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.RECORD_AUDIO};


        ActivityCompat.requestPermissions(this, permissions, REQUEST_PERMISSIONS);



        final Button callGalleryBtn = findViewById(R.id.buttonX);
        callGalleryBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
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
            Log.d(TAG,"We arrived to read the file");
            Log.d(TAG,"This should be the lastCameraFileUri: "+ lastCameraFileUri);
            //och vad gör männskann här. När hen har fått tillbaka en bild. Jo
            setPic();
        } else if (requestCode == REQUEST_PERMISSIONS) {
            Log.d(TAG, "permission jox har körts");
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
                        getApplicationContext().getPackageName() + ".fileprovider",
                        photoFile);
                lastCameraFileUri = photoFile.toString();
                Log.d(TAG, "photoFile.toString(): " + photoFile.toString());
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


        String imageFileName = "XBalancer" + timeStamp;


        //Okej, här får vi platsen där alla bilder sparas
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        Log.d(TAG, "This is storageDir.toString() : " + storageDir.toString());


        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        Log.d(TAG, "This is image.toString()      : " + image.toString());


        return image;
    }


    private void setPic() {

        ImageView mImageView = findViewById(R.id.imageView1);
        // Get the dimensions of the View
        int targetW = mImageView.getWidth();
        int targetH = mImageView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(lastCameraFileUri, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(lastCameraFileUri, bmOptions);
        mImageView.setImageBitmap(bitmap);
    }

}