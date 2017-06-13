package com.cst.scanner.Custom;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.icu.text.SimpleDateFormat;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;

import org.opencv.android.JavaCameraView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.List;

public class TakePictureView extends JavaCameraView implements Camera.PictureCallback {

    private static final String TAG = "Sample::Tutorial2View";
    public String mPictureFileName = "";
    private Context myreference;
    public static boolean isFlashLightON = true;
    public TakePictureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.myreference = context;
    }

    public List<String> getEffectList() {
        return mCamera.getParameters().getSupportedColorEffects();
    }

    public boolean isEffectSupported() {
        return (mCamera.getParameters().getColorEffect() != null);
    }

    public String getEffect() {
        return mCamera.getParameters().getColorEffect();
    }

    public void setEffect(String effect) {
        Camera.Parameters params = mCamera.getParameters();
        params.setColorEffect(effect);
        mCamera.setParameters(params);
    }

    public List<Size> getResolutionList() {
        return mCamera.getParameters().getSupportedPreviewSizes();
    }

    public void setResolution(Size resolution) {
        disconnectCamera();
        mMaxHeight = resolution.height;
        mMaxWidth = resolution.width;
        connectCamera(getWidth(), getHeight());
    }

    public Size getResolution() {
        return mCamera.getParameters().getPreviewSize();
    }

    // Setup the camera
    public void setupCameraFlashLight() {
        Camera  camera = mCamera;
        if (camera != null) {
            Camera.Parameters params = camera.getParameters();

            if (params != null) {
                if (isFlashLightON) {
                    isFlashLightON = false;
                    params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                    camera.setParameters(params);
                    camera.startPreview();
                } else {
                    isFlashLightON = true;
                    params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                    camera.setParameters(params);
                    camera.startPreview();

                }
            }
        }

    }

    public void takePicture() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String currentDateandTime = sdf.format(new Date());
        mPictureFileName = setFileName();
        // Postview and jpeg are sent in the same buffers if the queue is not empty when performing a capture.
        // Clear up buffers to avoid mCamera.takePicture to be stuck because of a memory issue
        mCamera.setPreviewCallback(null);
        // PictureCallback is implemented by the current class
        mCamera.takePicture(null, null, this);
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        Log.i(TAG, "Saving a bitmap to file");
        // The camera preview was automatically stopped. Start it again.
        mCamera.startPreview();
        mCamera.setPreviewCallback(this);

        // Write the image in a file (in jpeg format)
        try {
            FileOutputStream fos = new FileOutputStream(mPictureFileName);

            fos.write(data);
            fos.close();

        } catch (java.io.IOException e) {
            Log.e("PictureDemo", "Exception in photoCallback", e);
        }

    }
    public String setFileName() {
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String currentDateandTime = sdf.format(new Date());
        String filename = "Doc_" + currentDateandTime + ".jpg";
        final File file = new File(path, filename);
        filename = file.getAbsolutePath().toString();
        return filename;
    }
}
