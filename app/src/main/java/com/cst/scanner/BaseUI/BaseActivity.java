package com.cst.scanner.BaseUI;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.cst.scanner.BaseUI.Helper.Constant;
import com.cst.scanner.BaseUI.Helper.ImageHelper;
import com.cst.scanner.R;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.util.Patterns.EMAIL_ADDRESS;
import static com.cst.scanner.BaseUI.Helper.Constant.CAMERA_REQUEST;
import static com.cst.scanner.BaseUI.Helper.Constant.REQUEST_TAKE_GALLERY_VIDEO;
import static com.cst.scanner.BaseUI.Helper.Constant.STORAGE_REQUEST;


/**
 * Created by longdg on 10/01/2017.
 */

public class BaseActivity extends AppCompatActivity {
    private Uri mImageCaptureUri;
    private static final String TAG = "BaseActivity";
    protected android.support.v4.app.FragmentManager fragmentManager;
    private static final int REQUEST_PERMISSIONS = 20;
    private static SharedPreferences prefences;
    /*
 * First check if device is supporting flashlight or not
 */
//    hasFlash = getApplicationContext().getPackageManager()
//        .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        fragmentManager = getSupportFragmentManager();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (requestCode == CAMERA_REQUEST) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    showCameraPreview();
                    Log.d(TAG, "onRequestPermissionsResult: chay voa day");
                } else {
                    Toast.makeText(this, "Camera permission is needed to show the camera preview. Please accept permission", Toast.LENGTH_SHORT).show();
                }

            } else {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
            if (requestCode == Constant.GALLERY_PICTURE) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getImageFromGallery();
                } else {
                    Toast.makeText(this, "Please accept permission to take photo from gallery.", Toast.LENGTH_SHORT).show();
                }
            } else {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
            if (requestCode == STORAGE_REQUEST) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(this, "Cant save file.", Toast.LENGTH_SHORT).show();
                }
            } else {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        } else {
            Log.d(TAG, "onRequestPermissionsResult: khong voa day");
        }

//        if(requestCode == Constant.PLACE_PICKER_REQUEST) {
//            getLocation();
//        }
    }

    public void permissionsRequired() {
        if (Build.VERSION.SDK_INT >= 23) {
            if ( checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

            } else {
                if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Toast.makeText(this, "Media permission is needed to show the all file of media.", Toast.LENGTH_SHORT).show();
                }
                if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                    Toast.makeText(this, "Please accept permission for files feauture.", Toast.LENGTH_SHORT).show();
                }
                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Toast.makeText(this, "Please accept permission for Photo feauture.", Toast.LENGTH_SHORT).show();
                }
                requestPermissions(new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_REQUEST);
            }
        } else {

        }
    }

    ConnectivityManager connManager;
    NetworkInfo mWifi;
    public boolean checkWifi () {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }
    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }
//    public String getLocation() {
//        StringBuilder GPS = new StringBuilder();
////             Get the location manager
//
//        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
//        try {
//            startActivityForResult(builder.build(MainActivity.getInstance().getApplicationContext()), PLACE_PICKER_REQUEST);
////                saveInfo();
//        } catch (GooglePlayServicesRepairableException e) {
//            e.printStackTrace();
//        } catch (GooglePlayServicesNotAvailableException e) {
//            e.printStackTrace();
//        }
//        return GPS.toString();
//    }
    //    @Override
//    public void onRequestPermissionsResult(int requestCode,
//                                           String permissions[], int[] grantResults) {
//        Log.d(TAG, "Permission callback called-------");
//        switch (requestCode) {
//            case REQUEST_PERMISSIONS: {
//
//                Map<String, Integer> perms = new HashMap<>();
//                // Initialize the map with both permissions
//                perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
//                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
//                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
//                // Fill with actual results from user
//                if (grantResults.length > 0) {
//                    for (int i = 0; i < permissions.length; i++)
//                        perms.put(permissions[i], grantResults[i]);
//                    // Check for both permissions
//                    if (perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
//                            && perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
//                            && perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
//                        Log.d(TAG, "CAMERA & READ_EXTERNAL_STORAGE  WRITE_EXTERNAL_STORAGE services permission granted");
//                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                        startActivityForResult(intent, CAMERA_REQUEST);
//                    } else {
//                        Log.d(TAG, "Some permissions are not granted ask again ");
//                        //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
////                        // shouldShowRequestPermissionRationale will return true
//                        //show the dialog or snackbar saying its necessary and try again otherwise proceed with setup.
//                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
//                            showDialogOK("SMS and Location Services Permission required for this app",
//                                    new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {
//                                            switch (which) {
//                                                case DialogInterface.BUTTON_POSITIVE:
//                                                    checkAndRequestPermissions();
//                                                    break;
//                                                case DialogInterface.BUTTON_NEGATIVE:
//                                                    // proceed with logic by disabling the related features or quit the app.
//                                                    break;
//                                            }
//                                        }
//                                    });
//                        }
//                        //permission is denied (and never ask again is  checked)
//                        //shouldShowRequestPermissionRationale will return false
//                        else {
//                            Toast.makeText(this, "Go to settings and enable permissions", Toast.LENGTH_LONG)
//                                    .show();
//                            //                            //proceed with logic by disabling the related features or quit the app.
//                        }
//                    }
//                }
//            }
//        }
//
//    }

    public void showCamera() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                showCameraPreview();
            } else {
                if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                    Toast.makeText(this, "Camera permission is needed to show the camera preview.", Toast.LENGTH_SHORT).show();
                }
                if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Toast.makeText(this, "Please accept permission for Photo feauture.", Toast.LENGTH_SHORT).show();
                }
                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Toast.makeText(this, "Please accept permission for Photo feauture.", Toast.LENGTH_SHORT).show();
                }
                requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, CAMERA_REQUEST);
            }
            Log.d(TAG, "showCamera: 1");
        } else {
            Log.d(TAG, "showCamera: 2");
            showCameraPreview();
        }

    }

    public void showGallery() {
        if (Build.VERSION.SDK_INT >= 23) {
            //this code will be executed on devices running ICS or later
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                getImageFromGallery();
            } else {
                if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Toast.makeText(this, "Please accept permission for Photo feauture.", Toast.LENGTH_SHORT).show();
                }
                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Toast.makeText(this, "Please accept permission for Photo feauture.", Toast.LENGTH_SHORT).show();
                }
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, Constant.GALLERY_PICTURE);
            }
        } else {
            getImageFromGallery();
        }

    }
    public void showGalleryVideo() {
        if (Build.VERSION.SDK_INT >= 23) {
            //this code will be executed on devices running ICS or later
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                getVideoFromGallery();
            } else {
                if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Toast.makeText(this, "Please accept permission for Photo feauture.", Toast.LENGTH_SHORT).show();
                }
                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Toast.makeText(this, "Please accept permission for Photo feauture.", Toast.LENGTH_SHORT).show();
                }
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_TAKE_GALLERY_VIDEO);
            }
        } else {
            getVideoFromGallery();
        }

    }
    public void requirePermissionStorage () {
        if (Build.VERSION.SDK_INT >= 23) {
            //this code will be executed on devices running ICS or later
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

            } else {
                if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Toast.makeText(this, "Please accept permission for Photo feauture.", Toast.LENGTH_SHORT).show();
                }
                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Toast.makeText(this, "Please accept permission for Photo feauture.", Toast.LENGTH_SHORT).show();
                }
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_REQUEST);
            }
        } else {

        }
    }
    public void showCameraPreview() {
        if (Build.VERSION.SDK_INT >= 19) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, MyFileContentProvider.CONTENT_URI);
            startActivityForResult(intent, CAMERA_REQUEST);
        } else {
            Toast.makeText(this, "aaaaaaaa", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//
            mImageCaptureUri = Uri.fromFile(new File(Environment
                    .getExternalStorageDirectory(), "tmp_avatar_"
                    + String.valueOf(System.currentTimeMillis())
                    + ".jpg"));

            intent.putExtra(MediaStore.EXTRA_OUTPUT,
                    mImageCaptureUri);

            try {
                intent.putExtra("return-data", true);

                startActivityForResult(intent, CAMERA_REQUEST);
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
            }
        }
//
    }
    public void showDialogToTakePicture(int idView, final BaseFragment.IClick iclick, String nameTitle) {
        final Dialog dialog = new Dialog(this);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(idView);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);

        dialog.findViewById(R.id.btnTakePicture).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                iclick.click();
            }
        });

//        TextView tvTitle = (TextView) dialog.findViewById(R.id.tvNameWSDialog);
//        tvTitle.setText(nameTitle);
        dialog.findViewById(R.id.btnGallery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // delete food.
                iclick.click2();
                dialog.dismiss();
//                showGallery();
            }
        });
        dialog.show();
    }
    public void showDialog(int idView, final BaseFragment.IClick iclick, boolean isHasCancel) {
        final Dialog dialog = new Dialog(this);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(idView);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        if (isHasCancel) {
            dialog.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }
        dialog.findViewById(R.id.btnOk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // delete food.
                iclick.click();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }

    protected void navToByAdd(Fragment f, String fragmentTag, String backStackTag,
                              boolean isAddStack, int contentViewId) {
        FragmentTransaction fTraf = fragmentManager.beginTransaction();
        fTraf.add(contentViewId, f, fragmentTag);
        if (isAddStack) {
            fTraf.addToBackStack(backStackTag);
        }
        fTraf.commit();
    }
    public static void navToByReplace(android.support.v4.app.FragmentManager fragmentManager, Fragment f, String fragmentTag,
                                      String backStackTag, boolean isAddToStack, int containerViewId, int i) {
        FragmentTransaction fTrans = fragmentManager.beginTransaction();
        if (i==1){
            fTrans.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_right, R.anim.exit_to_left);
        }
        else {
            fTrans.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_left, R.anim.exit_to_right);
        }
        fTrans.replace(containerViewId, f, fragmentTag);
        if (isAddToStack) {
            fTrans.addToBackStack(backStackTag);
        }
        fTrans.commit();
    }

    protected void navToByReplace(android.support.v4.app.FragmentManager fragmentManager1, Fragment f, String fragmentTag,
                                  String backStackTag, boolean isAddToStack, int containerViewId) {
        FragmentTransaction fTrans = fragmentManager1.beginTransaction();
        fTrans.replace(containerViewId, f, fragmentTag);
        if (isAddToStack) {
            fTrans.addToBackStack(backStackTag);
        }
        fTrans.commit();
    }

    // check permissions
    public boolean checkAndRequestPermissions() {
        int permissionWrite = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int cameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int permissionRead = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (permissionWrite != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (permissionRead != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(
                    new String[listPermissionsNeeded.size()]), REQUEST_PERMISSIONS);
            Toast.makeText(getApplicationContext(), "Add required", Toast.LENGTH_LONG).show();
//            checkAndRequestPermissions();
            return false;
        }
        return true;
    }

    public boolean checkForEmail(String str) {
        boolean isRight = false;
        if (EMAIL_ADDRESS.matcher(str).matches()) {
            Toast.makeText(this, "Successful", Toast.LENGTH_SHORT).show();
            isRight = true;
        } else {
            isRight = false;
            Toast.makeText(this, "Email invalid.Please try again", Toast.LENGTH_SHORT).show();
        }
        return isRight;
    }

    private void goToUrl (String url) {
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }
//    public void convertArrayToJson(String json, ArrayList arrObj) {
//        json = new Gson().toJson(arrObj);
//        SharedPreferences.Editor editor = getSharedPreferences(Constant.TAG_SHAREPREFERENCES, MODE_PRIVATE).edit();
//        editor.putString("json", json);
//        editor.commit();
//        Log.d(TAG, "convertArrayToJson: " + json);
//    }

    //    public void convertArrayToJson1(String key, ArrayList arrObj) {
//
//        SharedPreferences.Editor editor = getSharedPreferences(Constant.TAG_SHAREPREFERENCES, MODE_PRIVATE).edit();
//        editor.putString(key,  new Gson().toJson(arrObj));
//        editor.commit();
//        Log.d(TAG, "convertArrayToJson: " +  new Gson().toJson(arrObj));
//    }
//    public ArrayList convertJsonToArray(String json, ArrayList arrObj) {
//        prefences = getSharedPreferences(Constant.TAG_SHAREPREFERENCES, MODE_PRIVATE);
//        json = prefences.getString("json", "");
//        JSONArray jsonArr = null;
//        try {
//            jsonArr = new JSONArray(json);
//            arrObj.clear();
//            for (int i = 0; i < jsonArr.length(); i++) {
//
//                arrObj.add(jsonArr.getString(i));
//
//            }
//            Log.d(TAG, "json: " + json);
//            Log.d(TAG, "arrObj: " + arrObj);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return arrObj;
//    }
    public Bitmap decodeBitmap (int icon_resource) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inSampleSize = 2;
        Bitmap icon = BitmapFactory.decodeResource(getResources(),
                icon_resource,opts);
        return icon;
    }
    public static String bitMapToString(Bitmap bitmap) {
        if(bitmap != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] b = baos.toByteArray();
            String temp = Base64.encodeToString(b, Base64.DEFAULT);
            return temp;
        } else {
            return "";
        }

    }
    //    public Bitmap decodeBitmap (String file) {
//        BitmapFactory.Options opts = new BitmapFactory.Options();
//        opts.inSampleSize = 16;
//        Bitmap icon = BitmapFactory.decodeFile(file,opts);
//        return icon;
//    }
    public Bitmap decodeBitmapFromPath (String file) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        Bitmap icon = BitmapFactory.decodeFile(file);
        icon = Bitmap.createScaledBitmap(icon,100,70, false);
        return icon;
    }
    public Bitmap decodeBitmapFromPathCamera (String file) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        Bitmap icon = BitmapFactory.decodeFile(file);
        icon = Bitmap.createScaledBitmap(icon,100,70, false);
        return icon;
    }
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }
    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
//    public ArrayList convertJsonToArray1(String key, ArrayList arrObj) {
//        prefences = getSharedPreferences(Constant.TAG_SHAREPREFERENCES, MODE_PRIVATE);
//        String json = prefences.getString(key, "");
//        JSONArray jsonArr = null;
//        try {
//            jsonArr = new JSONArray(json);
//            arrObj.clear();
//            for (int i = 0; i < jsonArr.length(); i++) {
//
//                arrObj.add(jsonArr.getString(i));
//
//            }
//            Log.d(TAG, "json: " + json);
//            Log.d(TAG, "arrObj: " + arrObj);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return arrObj;
//    }

    public static void setListViewHeighBaseOnChildren(ListView lv) {
        ListAdapter listAdapter = lv.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = lv.getPaddingTop() + lv.getPaddingBottom();
        lv.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(lv.getWidth(), View.MeasureSpec.UNSPECIFIED);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, lv);
            listItem.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = lv.getLayoutParams();
        params.height = totalHeight + (lv.getDividerHeight() * listAdapter.getCount() - 1);

        lv.setLayoutParams(params);
        lv.requestLayout();
    }

    public void getImageFromGallery() {
        Intent pictureActionIntent = null;

        pictureActionIntent = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        try {
            pictureActionIntent.putExtra("return-data", true);
            startActivityForResult(
                    pictureActionIntent,
                    Constant.GALLERY_PICTURE);
        } catch (ActivityNotFoundException e) {
        }
    }
    public void getVideoFromGallery () {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent,"Select Video"),REQUEST_TAKE_GALLERY_VIDEO);
    }

    public void ActionSheetDialogNoTitle(View v, final Activity activity, String[] stringItems) {
//        final String[] stringItems = {"Snap a photo", "Take Photo", "Choose From Library"};
        final ActionSheetDialog dialog = new ActionSheetDialog(this, stringItems, v);
        dialog.isTitleShow(false).show();
        dialog.cancelText("Cancel");
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        showCamera();
                        break;
                    case 1:

                        showGallery();
                        break;
                    default:
                        break;
                }

//
                dialog.dismiss();
            }
        });
    }

    public void setImageSelectedFromGallery(Intent data, ImageView imageView) {
        String selectedImagePath = "";
        Bitmap bitmap = null;
        if (data != null) {
            Uri selectedImage = data.getData();
            String[] filePath1 = {MediaStore.Images.Media.DATA};
            Cursor c = getContentResolver().query(selectedImage, filePath1,
                    null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePath1[0]);
            selectedImagePath = c.getString(columnIndex);
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
//                bitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, false); // rotating bitmap
                ImageHelper.saveImage(bitmap, "waterMask");
            } catch (Exception e) {

            }
            imageView.setImageBitmap(bitmap);
        } else {
            Toast.makeText(getApplicationContext(), "Cancelled",
                    Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * Shows the soft keyboard
     */
    public void showSoftKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        view.requestFocus();
        inputMethodManager.showSoftInput(view, 0);
    }

    /**
     * Hides the soft keyboard
     */
    public void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
    public String getPathImageFromGallery(Intent data) {

        String selectedImagePath = "";
        Bitmap bitmap = null;
        if (data != null) {
            Uri selectedImage = data.getData();
            String[] filePath1 = {MediaStore.Images.Media.DATA};
            Cursor c = getContentResolver().query(selectedImage, filePath1,
                    null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePath1[0]);
            selectedImagePath = c.getString(columnIndex);
        } else {
            Toast.makeText(getApplicationContext(), "Cancelled",
                    Toast.LENGTH_SHORT).show();
        }
        return selectedImagePath;
    }

    public Bitmap getBitmapFromImageChooseGallery(Intent data) {
        // Get selected gallery image
        Uri selectedPicture = data.getData();
        // Get and resize profile image
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(selectedPicture, filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();

        Bitmap loadedBitmap = BitmapFactory.decodeFile(picturePath);

        ExifInterface exif = null;
        try {
            File pictureFile = new File(picturePath);
            exif = new ExifInterface(pictureFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        int orientation = ExifInterface.ORIENTATION_NORMAL;

        if (exif != null)
            orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                loadedBitmap = rotateBitmap(loadedBitmap, 90);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                loadedBitmap = rotateBitmap(loadedBitmap, 180);
                break;

            case ExifInterface.ORIENTATION_ROTATE_270:
                loadedBitmap = rotateBitmap(loadedBitmap, 270);
                break;
        }
        return loadedBitmap;
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    //    protected Bitmap addWaterMark(Bitmap src) {
//        int w = src.getWidth();
//        int h = src.getHeight();
//        Bitmap result = Bitmap.createBitmap(w, h, src.getConfig());
//        Canvas canvas = new Canvas(result);
//        canvas.drawBitmap(src, 0, 0, null);
//        Bitmap waterMark = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
//        canvas.drawBitmap(waterMark, canvas.getWidth() - w, canvas.getHeight()-h, null);
//
//        return result;
//    }
    public static Bitmap addLogo(Bitmap mainImage, Bitmap logoImage) {
        Bitmap finalImage = null;
        int width, height = 0;
        width = mainImage.getWidth();
        height = mainImage.getHeight();
        finalImage = Bitmap.createBitmap(width, height, mainImage.getConfig());
        Canvas canvas = new Canvas(finalImage);
        canvas.drawBitmap(mainImage, 0,0,null);
        canvas.drawBitmap(logoImage, 0 ,canvas.getHeight()-logoImage.getHeight() ,null);

        return finalImage;
    }
    public static Matrix rotateImage (Uri uri) {
        Matrix matrix = new Matrix();
        try {
            ExifInterface exif = new ExifInterface(uri.getPath());
            int rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            int rotationInDegrees = exifToDegrees(rotation);

            if (rotation != 0f) {matrix.preRotate(rotationInDegrees);}
        } catch (IOException e) {
            e.printStackTrace();
        }
        return matrix;
    }
    private static int exifToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) { return 90; }
        else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {  return 180; }
        else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {  return 270; }
        return 0;
    }
}
