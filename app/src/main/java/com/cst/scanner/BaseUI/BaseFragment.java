package com.cst.scanner.BaseUI;


import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cst.scanner.BaseUI.Helper.ImageHelper;
import com.cst.scanner.R;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.flyco.dialog.widget.MaterialDialog;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static com.cst.scanner.BaseUI.Helper.Constant.CAMERA_REQUEST;
import static com.cst.scanner.BaseUI.Helper.Constant.GALLERY_PICTURE;

/**
 * Created by longdg on 10/01/2017.
 */

public class BaseFragment extends Fragment {
    public static final String TAG = "BaseFragment";
    public Context mContext = getContext();
    public SharedPreferences prefences;
    private Uri mImageCaptureUri;


    /**
     * CHeck for permission gallery
     */
    public void showGallery() {
        if (Build.VERSION.SDK_INT >= 23) {
            //this code will be executed on devices running ICS or later
            if (getContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    && getContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                getImageFromGallery();
            } else {
                if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Toast.makeText(getContext(), "Please accept permission for Photo feauture.", Toast.LENGTH_SHORT).show();
                }
                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Toast.makeText(getContext(), "Please accept permission for Photo feauture.", Toast.LENGTH_SHORT).show();
                }
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, GALLERY_PICTURE);
            }
        } else {
            getImageFromGallery();
        }

    }

    /**
     * Show Alert Dialog
     *
     * @param title
     * @param content
     * @param okClickListener
     */
    public void showAlertOke(String title, String content, DialogInterface.OnClickListener okClickListener) {
        new AlertDialog.Builder(getActivity()).setTitle(title)
                .setMessage(content)
                .setPositiveButton(android.R.string.yes, okClickListener)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public void showAlertOkeCancel(String title, String content, DialogInterface.OnClickListener okClickListener) {
        new AlertDialog.Builder(getActivity()).setTitle(title)
                .setMessage(content)
                .setPositiveButton(android.R.string.yes, okClickListener)
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }

    /**
     * Shows the soft keyboard
     */
    public void showSoftKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
        view.requestFocus();
        inputMethodManager.showSoftInput(view, 0);
    }

    /**
     * Hides the soft keyboard
     */
    public void hideSoftKeyboard() {
        if (getActivity().getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        }
    }
    public ArrayList convertJsonToArray1(SharedPreferences prefences, String key, ArrayList arrObj) {
        String json = prefences.getString(key, "");
        JSONArray jsonArr = null;
        try {
            jsonArr = new JSONArray(json);
            arrObj.clear();
            for (int i = 0; i < jsonArr.length(); i++) {

                arrObj.add(jsonArr.getString(i));

            }
            Log.d(TAG, "json: " + json);
            Log.d(TAG, "arrObj: " + arrObj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return arrObj;
    }
//    public ArrayList convertJsonToArray1(String key, ArrayList arrObj) {
//        prefences = getContext().getSharedPreferences(Constant.TAG_SHAREPREFERENCES, MODE_PRIVATE);
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

//    public void checkLocation() {
//        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
//                == PackageManager.PERMISSION_GRANTED &&
//                ActivityCompat.checkSelfPermission(getActivity(),
//                        Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//            getLocation();
//        } else {
//            if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
//                Toast.makeText(getContext(), "Please accept permission for get GPS.", Toast.LENGTH_SHORT).show();
//            }
//            if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)) {
//                Toast.makeText(getContext(), "Please accept permission for get GPS.", Toast.LENGTH_SHORT).show();
//            }
//            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, Constant.PLACE_PICKER_REQUEST);
//        }
//
//    }



    // region nav
    public static void navToByReplace(FragmentManager fragmentManager, Fragment f, String fragmentTag,
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

    public static String getPathStorageImageViewDrawCanvas(ImageView imageView, String nameFolder) {

        imageView.setDrawingCacheEnabled(true);

        imageView.buildDrawingCache(true);

        Bitmap saveBm = Bitmap.createBitmap(imageView.getDrawingCache());
        String path = ImageHelper.saveImage(saveBm, nameFolder);
        return path;
    }
//    public static Bitmap getBitmapFromImageViewDrawCanvas(CanvasView imageView) {
//        imageView.setDrawingCacheEnabled(true);
//
//        imageView.buildDrawingCache(true);
//
//        Bitmap saveBm = Bitmap.createBitmap(imageView.getDrawingCache());
//        return saveBm;
//    }

    public static Bitmap getBitmapFromImageViewDrawCanvas(ImageView imageView) {
        imageView.setDrawingCacheEnabled(true);

        imageView.buildDrawingCache(true);

        Bitmap saveBm = Bitmap.createBitmap(imageView.getDrawingCache());
        return saveBm;
    }

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

    public ArrayList<String> loadAllUrlOfImage(ArrayList<String> arrayList) {
        arrayList = new ArrayList<>();
        ArrayList<String> arr2 = new ArrayList<>();
        Uri uriImg = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projectionImg = {MediaStore.Images.Media.DATA};
        Cursor cursorImg = getContext().getContentResolver().query(uriImg, projectionImg, null, null, null);
        if (cursorImg != null) {
            while (cursorImg.moveToNext()) {

                int columnIndex = cursorImg.getColumnIndex(MediaStore.Images.Media.DATA);
                String path = cursorImg.getString(columnIndex);
                arrayList.add(path);
            }
            cursorImg.close();
        }
//        for (int i = 0; i < 9 ; i++) {
//            arr2.add(arrayList.get(i));
//        }
        return arrayList;
    }

//    public void loadImageWithLibrary(String path, int id1, int w, int h, View v) {
//        ImageLoader imageLoader = ImageLoader.getInstance();
//        final DrawImageView img1 = (DrawImageView) v.findViewById(id1);
//        imageLoader.init(ImageLoaderConfiguration.createDefault(getContext()));
//        ImageSize targetSize = new ImageSize(w, h); // result Bitmap will be fit to this size
//        final Bitmap[] bitmap = new Bitmap[1];
//        imageLoader.loadImage("file://" + path, targetSize, null, new SimpleImageLoadingListener() {
//            @Override
//            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//                // Do whatever you want with Bitmap
//                bitmap[0] = loadedImage;
//                img1.setImageBitmap(bitmap[0]);
//                Log.d("Edit", "onLoadingComplete:   bitmap[0] = loadedImage " + loadedImage);
//            }
//        });
//        Log.d("Edit", "onLoadingComplete:   bitmap[0] = bitmap " + bitmap[0]);
//
//    }

    public String parseStringtoDate(String start_dt) {
        DateFormat df = new SimpleDateFormat("dd MMMM yyyy, EEEE");
        Date date = null;
        String result = "";
        try {
            date = (Date)df.parse(start_dt);
            SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd");
             result = newFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "parseStringtoDate: " + result);
        return result;
    }

    //Convert Date to Calendar
    public Calendar dateToCalendar(Date date) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;

    }

    //Convert Calendar to Date
    private Date calendarToDate(Calendar calendar) {
        return calendar.getTime();
    }

    public Date parseStringToDate3 (String target) {
//        DateFormat df = new SimpleDateFormat("EEE MMM dd kk:mm:ss z yyyy", Locale.ENGLISH);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date result = null;
        try {
            result = df.parse(target);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }
    public String parseStringtoDate2(String start_dt) {
        SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date date = null;
        String result = "";
        try {
            date = (Date)newFormat.parse(start_dt);
            DateFormat df = new SimpleDateFormat("dd MMMM yyyy, EEEE");
//            DateFormat dateformat_UK = DateFormat.getDateInstance(DateFormat.FULL, Locale.UK);
            result = df.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "parseStringtoDate: " + result);
        return result;
    }
    public void showDateTimePicker(final TextView editText) {
        final Calendar date;
        final Calendar currentDate = Calendar.getInstance();
        date = Calendar.getInstance();
        final SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        new DatePickerDialog(getContext(), R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                date.set(year, monthOfYear, dayOfMonth);
                Date SelectedDate = date.getTime();
                DateFormat df = new SimpleDateFormat("dd MMMM yyyy, EEEE");
                DateFormat dateformat_UK = DateFormat.getDateInstance(DateFormat.FULL, Locale.UK);
                String StringDateformat_UK = df.format(SelectedDate);
                editText.setText(StringDateformat_UK);
//                editText.setText(date);
//                new TimePickerDialog(getContext(), R.style.DialogTheme, new TimePickerDialog.OnTimeSetListener() {
//                    @Override
//                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//                        date.set(Calendar.HOUR_OF_DAY, hourOfDay);
//                        date.set(Calendar.MINUTE, minute);
//
//                        try {
//                            Log.d(TAG, "The choosen one " + format1.parse(format1.format(date.getTime())));
//                            editText.setText(format1.parse(format1.format(date.getTime())).toString());
//                        } catch (ParseException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();
            }
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
    }
    public interface IClick {
    void click();
        void click2();
}
//    public void showDialog(int idView, final IClick iclick, boolean isHasCancel,String nameTitle) {
//        final Dialog dialog = new Dialog(getContext());
//        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(idView);
//        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
//        if (isHasCancel) {
//            dialog.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    dialog.dismiss();
//                }
//            });
//        }
//        TextView tvTitle = (TextView) dialog.findViewById(R.id.tvNameWSDialog);
//        tvTitle.setText(nameTitle);
//        dialog.findViewById(R.id.btnOk).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // delete food.
//                iclick.click();
//                dialog.dismiss();
//            }
//        });
//        dialog.show();
//    }

    public void showDialogNoTitle(String content) {
        final MaterialDialog dialog = new MaterialDialog(getContext());
        dialog.isTitleShow(false);
        dialog.content(content)//
                .btnNum(1)
                .btnText("Ok")//
                .show();
        dialog.setTitle("Notice");
        dialog.setOnBtnClickL(
                new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
//                        onButtonClick.buttonCLick();
                        dialog.cancel();
                    }
                });
    }
    public void showDialogNoTitleTwoChoice(String content, final OnBtnClickL onBtnClickL) {
        final MaterialDialog dialog = new MaterialDialog(getContext());
        dialog.isTitleShow(false);
        dialog.content(content)//
                .btnNum(2)
                .btnText("Cancel","Ok")
                .show();
        dialog.setTitle("Notice");
        dialog.setOnBtnClickL(
                new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
//                        onButtonClick.buttonCLick();
                        dialog.dismiss();
                    }
                },
                new OnBtnClickL() {//right btn click listener
                    @Override
                    public void onBtnClick() {
                        onBtnClickL.onBtnClick();
                        dialog.dismiss();
                    }
                });
    }
    public void showDialog(View v, final OnBtnClickL onBtnClickL) {
        final String[] stringItems = {"Cancel", "OK"};
        final ActionSheetDialog dialog = new ActionSheetDialog(getContext(), stringItems, v);
        dialog.isTitleShow(false).show();
        dialog.cancelText("Cancel");
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        dialog.cancel();
                        break;
                    case 1:

                        onBtnClickL.onBtnClick();
                        break;
                    default:
                        break;
                }

//
                dialog.dismiss();
            }
        });
    }
    public void showDialog(int idView, final BaseFragment.IClick iclick, boolean isHasCancel) {
        final Dialog dialog = new Dialog(getContext());
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

    public void showDialogAddTextInfo() {
        final String[] m_Text = {""};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Title");

// Set up the input
        final EditText input = new EditText(getContext());
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_Text[0] = input.getText().toString();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }


//    public void convertArrayToJson(String json, ArrayList arrObj) {
//        json = new Gson().toJson(arrObj);
//        SharedPreferences.Editor editor = getContext().getSharedPreferences(Constant.TAG_SHAREPREFERENCES, MODE_PRIVATE).edit();
//        editor.putString("json", json);
//        editor.commit();
//    }

//    public ArrayList convertJsonToArray(String json, ArrayList arrObj) {
//        SharedPreferences prefences = getActivity().getSharedPreferences(Constant.TAG_SHAREPREFERENCES, MODE_PRIVATE);
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
    private Bitmap addWaterMark(Bitmap src) {
        int w = src.getWidth();
        int h = src.getHeight();
        Bitmap result = Bitmap.createBitmap(w, h, src.getConfig());
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(src, 0, 0, null);

        Bitmap waterMark = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher);
        canvas.drawBitmap(waterMark, 0, 0, null);

        return result;
    }
    public static void createRandomShowText(EditText et) {
        char[] chars = "123456789abcefgh".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        String output = sb.toString();
        Log.d(TAG, "createRandomShowText: " + output);
        et.setText("Doc" + output);
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
                    GALLERY_PICTURE);
        } catch (ActivityNotFoundException e) {
        }
    }

//    public void loadImageWithLoader(String path, final ImageView imageView, int w, int h) {
//        ImageLoader imageLoader = ImageLoader.getInstance();
//        imageLoader.init(ImageLoaderConfiguration.createDefault(getContext()));
//        ImageSize targetSize = new ImageSize(w, h);
//        imageLoader.loadImage("file://" + path, targetSize, null, new SimpleImageLoadingListener() {
//            @Override
//            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//                // Do whatever you want with Bitmap
//                imageView.setImageBitmap(loadedImage);
//            }
//        });
//    }

    // bitmap
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

    /**
     * @param encodedString
     * @return bitmap (from given string)
     */
    public static Bitmap stringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    public String getPathImageFromGallery(Intent data) {

        String selectedImagePath = "";
        Bitmap bitmap = null;
        if (data != null) {
            Uri selectedImage = data.getData();
            String[] filePath1 = {MediaStore.Images.Media.DATA};
            Cursor c = getContext().getContentResolver().query(selectedImage, filePath1,
                    null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePath1[0]);
            selectedImagePath = c.getString(columnIndex);
        } else {
            Toast.makeText(getContext(), "Cancelled",
                    Toast.LENGTH_SHORT).show();
        }
        return selectedImagePath;
    }

    public static Bitmap getBitmapFromFilePath(String filePath) {
        Bitmap bitmap = BitmapFactory.decodeFile(filePath);
        return bitmap;
    }

    public static String[] convertArrayListToArray(ArrayList arrayList) {

        Object[] objectList = arrayList.toArray();
        String[] strs = Arrays.copyOf(objectList, objectList.length, String[].class);
        return strs;
    }

    public static String[] convertStringToArray(String str, String symbol) {
        String[] strs = str.split(symbol);
        return strs;
    }

    public static String convertArrayToString(String[] strs) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < strs.length; i++) {
            stringBuilder.append(strs[i]);
            stringBuilder.append(",");
        }
        Log.d(TAG, "convertArrayToString: " + stringBuilder.toString());
        return stringBuilder.toString();
    }
    public void showCamera() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (getContext().checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                    && getContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    && getContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                showCameraPreview();
            } else {
                if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                    Toast.makeText(getContext(), "Camera permission is needed to show the camera preview.", Toast.LENGTH_SHORT).show();
                }
                if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Toast.makeText(getContext(), "Please accept permission for Photo feauture.", Toast.LENGTH_SHORT).show();
                }
                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Toast.makeText(getContext(), "Please accept permission for Photo feauture.", Toast.LENGTH_SHORT).show();
                }
                requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, CAMERA_REQUEST);
            }
            Log.d(TAG, "showCamera: 1");
        } else {
            Log.d(TAG, "showCamera: 2");
            showCameraPreview();
        }

    }
    public void showCameraPreview() {
        if (Build.VERSION.SDK_INT >= 19) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, MyFileContentProvider.CONTENT_URI);
            startActivityForResult(intent, CAMERA_REQUEST);
        } else {
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


    public void ActionSheetDialogNoTitle(View v, String[] stringItems) {
//        final String[] stringItems = {"Snap a photo", "Take Photo", "Choose From Library"};
        final ActionSheetDialog dialog = new ActionSheetDialog(getContext(), stringItems, v);
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

    //web
    public void goToUrl (String url) {
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }
}
