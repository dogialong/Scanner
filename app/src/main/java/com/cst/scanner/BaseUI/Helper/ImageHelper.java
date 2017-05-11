package com.cst.scanner.BaseUI.Helper;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

/**
 * Created by longdg on 17/01/2017.
 */

public class ImageHelper {
    public static final String TAG = "ImageHelper";
    protected static final int CAMERA_REQUEST = 0;

    public static Bitmap decodeFilePathToBitmap(String file) {
        Bitmap bitmap = BitmapFactory.decodeFile(file);
        bitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, true);
        return bitmap;
    }
    public static  String saveImage(Bitmap bitmap) {
        boolean success = false;
        String filePath = "";
        String nameImageInSdcard = DateFormat.format("MM-dd-yy hh-mm-ss", new Date().getTime()).toString() + ".jpg";
        // save to sdcard
        File sdCardDirectory = Environment.getExternalStorageDirectory();
        File image = new File(sdCardDirectory.toString(), nameImageInSdcard);
        // Encode the file as a PNG image.
        FileOutputStream outStream;
        try {

            outStream = new FileOutputStream(image);
//            bitmap.createScaledBitmap(bitmap, 300, 300, false);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
        /* 100 to keep full quality of the image */

            outStream.flush();
            outStream.close();
            success = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (success) {
            filePath = image.getAbsolutePath();
        } else {
            Log.d(TAG, "saveImage: failed");
        }

        return filePath;
    }
    public static String saveImageWatermask(Bitmap bitmap, String name) {
        boolean success = false;
        String filePath = "";
        String nameImageInSdcard = DateFormat.format("MM-dd-yy HH:mm:ss", new Date().getTime()).toString() + name + ".jpg";
        // save to sdcard
        File root = new File(Environment.getExternalStorageDirectory()
                + File.separator + name + File.separator);
        if (!root.exists()) {
            root.mkdir();
        }
        File sdCardDirectory = Environment.getExternalStorageDirectory();
        File image = new File(root, nameImageInSdcard);
        // Encode the file as a PNG image.
        FileOutputStream outStream;
        try {

            outStream = new FileOutputStream(image);
            bitmap = Bitmap.createScaledBitmap(bitmap,100,70,true);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
        /* 100 to keep full quality of the image */

            outStream.flush();
            outStream.close();
            success = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (success) {
            filePath = image.getAbsolutePath();
        } else {
            Log.d(TAG, "saveImage failed ");
        }
        return filePath;
    }

    public static String saveImage(Bitmap bitmap, String name) {
        boolean success = false;
        String filePath = "";
        String nameImageInSdcard = "DOC_" +DateFormat.format("MM-dd-yy HH:mm:ss", new Date().getTime()).toString() + ".jpg";
        // save to sdcard
        File root = new File(Environment.getExternalStorageDirectory()
                + File.separator + name + File.separator);
        if (!root.exists()) {
            root.mkdir();
        }
        File sdCardDirectory = Environment.getExternalStorageDirectory();
        File image = new File(root, nameImageInSdcard);
        // Encode the file as a PNG image.
        FileOutputStream outStream;
        try {

            outStream = new FileOutputStream(image);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
        /* 100 to keep full quality of the image */

            outStream.flush();
            outStream.close();
            success = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (success) {
            filePath = image.getAbsolutePath();
        } else {
            Log.d(TAG, "saveImage failed ");
        }
        return filePath;
    }

    public static Bitmap bitmap_resize(Bitmap bitmap, int newWidth, int newHeight) {
        Bitmap scaledBitmap = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);

        float ratioX = newWidth / (float) bitmap.getWidth();
        float ratioY = newHeight / (float) bitmap.getHeight();
        float middleX = newWidth / 2.0f;
        float middleY = newHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bitmap, middleX - bitmap.getWidth() / 2, middleY - bitmap.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

        return scaledBitmap;

    }

    public static Bitmap decodeBitmapFromCamera(Intent data) {
        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
        bitmap = Bitmap.createScaledBitmap(bitmap, 400, 400, true);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), null, true);
        return bitmap;
    }
    public static Bitmap decodeBitmapFromPathGallery(String filePath) {
        Bitmap bitmap = BitmapFactory.decodeFile(filePath);
        bitmap = Bitmap.createScaledBitmap(bitmap, 400, 400, true);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), null, true);
        return bitmap;
    }
    public static Bitmap decodeBitmapFromGallery(Intent data, Context mContext) {
        Bitmap bitmap = null;
        if (data != null) {
            Uri selectedImage = data.getData();
            String[] filePath1 = {MediaStore.Images.Media.DATA};
            Cursor c = mContext.getContentResolver().query(selectedImage, filePath1,
                    null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePath1[0]);
            String selectedImagePath = c.getString(columnIndex);
            c.close();
            try {
                ExifInterface exif = new ExifInterface(selectedImagePath);
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                Log.d("EXIF", "Exif: " + orientation);
                Matrix matrix = new Matrix();
                if (orientation == 6) {
                    matrix.postRotate(90);
                } else if (orientation == 3) {
                    matrix.postRotate(180);
                } else if (orientation == 8) {
                    matrix.postRotate(270);
                }
                bitmap = BitmapFactory.decodeFile(selectedImagePath); // load
                // preview image
                bitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, false); // rotating bitmap
            } catch (Exception e) {

            }
        } else {
            Log.d(TAG, "decodeBitmapFromGallery: Failed");
        }
        return bitmap;
    }

    //    public static void takeCamera(Fragment context) {
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        context.startActivityForResult(intent,
//                CAMERA_REQUEST);
//    }
    public static Bitmap rotateBitmap(Bitmap bitmap, int orientation) {

        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                return bitmap;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
                return bitmap;
        }
        try {
            Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            return bmRotated;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void setImageViewFromPath(String path, ImageView iv) {
        try {
//            ExifInterface exif = null;
//            try {
//                exif = new ExifInterface(path);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
//                    ExifInterface.ORIENTATION_UNDEFINED);
            Bitmap bitmap = BitmapFactory.decodeFile(path); // load
            // preview image
            bitmap = bitmap_resize(bitmap, 400, 400); // rotating bitmap
            iv.setImageBitmap(bitmap);
//            Log.d(TAG, "setImageViewFromPath: success" );
        } catch (Exception e) {

        }
    }
}
