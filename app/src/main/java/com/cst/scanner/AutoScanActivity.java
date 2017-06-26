package com.cst.scanner;

import android.animation.ValueAnimator;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cst.scanner.Adapter.AImage2;
import com.cst.scanner.BaseUI.BaseActivity;
import com.cst.scanner.BaseUI.Helper.Singleton;
import com.cst.scanner.Custom.TakePictureView;
import com.cst.scanner.Database.DatabaseHandler;
import com.cst.scanner.Delegate.IListViewClick;
import com.cst.scanner.Model.FileObject;
import com.dinuscxj.progressbar.CircleProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.utils.Converters;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * the main activity - entry to the application
 */
public class AutoScanActivity extends BaseActivity implements CvCameraViewListener2 {
    /**
     * class name for debugging with logcat
     */
    private CircleProgressBar mCustomProgressBar4;
    private static final String TAG = MainActivity.class.getName();
    /**
     * the camera view
     */
    private TakePictureView mOpenCvCameraView;
    private String [] strings = {"3","2","Scan"};
    private static final Scalar RGB_RED = new Scalar(255, 0, 0);

    /**
     * the image thresholded for the upper HSV red range
     */
    private Mat upperRedRange;
    /**
     * the downscaled image (for removing noise)
     */
    private Mat downscaled;
    /**
     * the upscaled image (for removing noise)
     */
    private Mat upscaled;
    /**
     * the image changed by findContours
     */
    private Mat contourImage;
    private Mat image;
    /**
     * the activity manager needed for getting the memory info
     * which is necessary for getting the memory usage
     */
    private ActivityManager activityManager;
    /**
     * responsible for getting memory information
     */
    private ActivityManager.MemoryInfo mi;
    /**
     * the found contour as hierarchy vector
     */
    private Mat hierarchyOutputVector;
    /**
     * approximated polygonal curve with specified precision
     */
    private MatOfPoint2f approxCurve;
    private TextView tvScan;
    private RecyclerView lvImage;
    private LinearLayout llX,llFlash,llAuto,llV,llTakePicture;
    public boolean isDetect = false;
    public Mat mInter;
    public String filename;
    private AImage2 adapterImage;
    private ArrayList<FileObject> arrstamp;
    public Handler handler;
    public Handler handlerTime;
    public DatabaseHandler db;
    String fileOfImage;
    ImageView imgFlash,imgAuto;
    private boolean isFlashOn;
    private boolean hasFlash;
    int dem = 0;
    Camera.Parameters params;
    public static AutoScanActivity autoScanActivity;
    private boolean isAutoScan = true;
    private ArrayList<String> arrLinkPath ;
    private CountDownTimer countDownTimer;
    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    Log.i(TAG, "OpenCV loaded successfully");

                    upperRedRange = new Mat();
                    downscaled = new Mat();
                    upscaled = new Mat();
                    contourImage = new Mat();

                    hierarchyOutputVector = new Mat();
                    approxCurve = new MatOfPoint2f();

                    mOpenCvCameraView.enableView();

                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };

    public AutoScanActivity() {
        Log.i(TAG, "Instantiated new " + this.getClass());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "called onCreate");
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_my);
        // get the OverlayView responsible for displaying images on top of the camera
        autoScanActivity = this;
        db = new DatabaseHandler(getApplicationContext());
        arrLinkPath = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateandTime = sdf.format(new Date());
        fileOfImage = "Doc " + currentDateandTime;


        // check device has flash
        hasFlash = getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        if (!hasFlash) {
            // device doesn't support flash
            // Show alert message and close the application
            AlertDialog alert = new AlertDialog.Builder(AutoScanActivity.this)
                    .create();
            alert.setTitle("Error");
            alert.setMessage("Sorry, your device doesn't support flash light!");
            alert.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // closing the application
                    finish();
                }
            });
            alert.show();
            return;
        }
        mCustomProgressBar4 = (CircleProgressBar) findViewById(R.id.custom_progress4);
        imgFlash = (ImageView) findViewById(R.id.imgFlash);
        imgAuto = (ImageView) findViewById(R.id.imgAuto);
        tvScan = (TextView) findViewById(R.id.tvScan);
        mOpenCvCameraView = (TakePictureView) findViewById(R.id.java_camera_view);
        llX = (LinearLayout) findViewById(R.id.llX);
        llFlash = (LinearLayout) findViewById(R.id.llFlash);
        llAuto = (LinearLayout) findViewById(R.id.llAuto);
        llV = (LinearLayout) findViewById(R.id.llV);
        llTakePicture = (LinearLayout) findViewById(R.id.llTakepictue);
        llV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject json = new JSONObject();
                try {
                    json.put(Singleton.getGetInstance().key_json, new JSONArray(arrLinkPath));
                    String link_array = json.toString();
                    db.addImage(new FileObject(filename,fileOfImage,"","",link_array));
                    arrLinkPath.clear();
                    Singleton.getGetInstance().arrayList.add(new FileObject(filename,
                            fileOfImage,"das","dasd",link_array));
                    Singleton.getGetInstance().arrayList.remove(Singleton.getGetInstance().arrayList.size()-1);
                    if(handler != null) {
                        handler.removeCallbacksAndMessages(null);
                    }
                    Singleton.getGetInstance().where = "ScanActivity";
                    Singleton.getGetInstance().isStorage = false;
                    navToByReplace(getSupportFragmentManager(), new ActivitySlider(), "ActivitySlider", "ActivitySlider", true, R.id.rlScan);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        llFlash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOpenCvCameraView.isFlashLightON = !mOpenCvCameraView.isFlashLightON;
                mOpenCvCameraView.setupCameraFlashLight();
                mOpenCvCameraView.isFlashLightON = !mOpenCvCameraView.isFlashLightON;
                if(mOpenCvCameraView.isFlashLightON ) {
                    imgFlash.setImageResource(R.drawable.turn_off);
                } else {
                    imgFlash.setImageResource(R.drawable.flash_on);
                }
            }
        });
        llTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                mOpenCvCameraView.takePicture();
                filename = setFileName();
                Imgcodecs.imwrite(filename, image);
                arrLinkPath.add(filename);
//                db.addImage(new FileObject(filename,fileOfImage,"",""));
                Singleton.getGetInstance().arrayList.add(new FileObject(filename,
                        fileOfImage,"","",""));
                adapterImage.notifyDataSetChanged();
            }
        });
        llAuto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isAutoScan = !isAutoScan;
                if(isAutoScan) {
                    imgAuto.setImageResource(R.drawable.auto);

                } else {
                    if(handler != null) {
                        handler.removeCallbacksAndMessages(null);
                    }
                    imgAuto.setImageResource(R.drawable.auto_off);
                }
            }
        });
        llX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                MainActivity.getInstance().resetBackgroundCircle();
            }
        });

//        if (FIXED_FRAME_SIZE) {
//            mOpenCvCameraView.setMaxFrameSize(FRAME_SIZE_WIDTH, FRAME_SIZE_HEIGHT);
//        }
        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);

        mOpenCvCameraView.setCvCameraViewListener(this);

        mi = new ActivityManager.MemoryInfo();
        activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);

        lvImage = (RecyclerView) findViewById(R.id.lv);
        arrstamp = new ArrayList<>();

        adapterImage = new AImage2(getApplicationContext(), arrLinkPath, new IListViewClick() {
            @Override
            public void onClick(View v, int position) {
                if (handler != null) {
                    handler.removeCallbacksAndMessages(null);
                }
                Singleton.getGetInstance().isStorage = false;
                Singleton.getGetInstance().where ="adapterImage";
                navToByReplace(getSupportFragmentManager(), new ActivitySlider(), "ActivitySlider", "ActivitySlider", true, R.id.rlScan);

            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        lvImage.setLayoutManager(linearLayoutManager);
        lvImage.setAdapter(adapterImage);
    }


    @Override
    public void onPause() {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
        System.gc();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
        try {
            handler.removeCallbacksAndMessages(null);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void onCameraViewStarted(int width, int height) {
    }

    public void onCameraViewStopped() {
        System.gc();
    }
    List<MatOfPoint> squares = new ArrayList<MatOfPoint>();
    public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
         image = inputFrame.rgba();
        if(isAutoScan) {
            if (Math.random()>0.80) {

                findSquares(inputFrame.rgba().clone(),squares);

            }

//        Imgproc.drawContours(image, squares, -1, new Scalar(0,0,255));
            findLargesSquares(squares,image);
        }
        System.gc();
        return image;

    }

    public Mat warp(Mat inputMat, Mat startM) {
        int resultWidth = 1000;
        int resultHeight = 1000;

        Mat outputMat = new Mat(resultWidth, resultHeight, CvType.CV_8UC4);


        Point ocvPOut1 = new Point(0, 0);
        Point ocvPOut2 = new Point(0, resultHeight);
        Point ocvPOut3 = new Point(resultWidth, resultHeight);
        Point ocvPOut4 = new Point(resultWidth, 0);
        List<Point> dest = new ArrayList<Point>();
        dest.add(ocvPOut1);
        dest.add(ocvPOut2);
        dest.add(ocvPOut3);
        dest.add(ocvPOut4);
        Mat endM = Converters.vector_Point2f_to_Mat(dest);

        Mat perspectiveTransform = Imgproc.getPerspectiveTransform(startM, endM);

        Imgproc.warpPerspective(inputMat,
                outputMat,
                perspectiveTransform,
                new Size(resultWidth, resultHeight),
                Imgproc.INTER_CUBIC);

        return outputMat;
    }

    int thresh = 50, N = 5;
    // helper function:
    // finds a cosine of angle between vectors
    // from pt0->pt1 and from pt0->pt2
    double angle(Point pt1, Point pt2, Point pt0) {
        double dx1 = pt1.x - pt0.x;
        double dy1 = pt1.y - pt0.y;
        double dx2 = pt2.x - pt0.x;
        double dy2 = pt2.y - pt0.y;
        return (dx1 * dx2 + dy1 * dy2) / Math.sqrt((dx1 * dx1 + dy1 * dy1) * (dx2 * dx2 + dy2 * dy2) + 1e-10);
    }

    // returns sequence of squares detected on the image.
    // the sequence is stored in the specified memory storage
    void findSquares( Mat image, List<MatOfPoint> squares )
    {

        squares.clear();

        Mat smallerImg=new Mat(new Size(image.width()/2, image.height()/2),image.type());

        Mat gray=new Mat(image.size(),image.type());

        Mat gray0=new Mat(image.size(),CvType.CV_8U);

        // down-scale and upscale the image to filter out the noise
        Imgproc.pyrDown(image, smallerImg, smallerImg.size());
        Imgproc.pyrUp(smallerImg, image, image.size());

        // find squares in every color plane of the image
        for( int c = 0; c < 3; c++ )
        {

            extractChannel(image, gray, c);

            // try several threshold levels
            for( int l = 1; l < N; l++ )
            {
                //Cany removed... Didn't work so well


                Imgproc.threshold(gray, gray0, (l+1)*255/N, thresh, Imgproc.THRESH_BINARY);


                List<MatOfPoint> contours=new ArrayList<MatOfPoint>();

                // find contours and store them all as a list
                Imgproc.findContours(gray0, contours, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);

                MatOfPoint approx=new MatOfPoint();
                // test each contour
                for( int i = 0; i < contours.size(); i++ )
                {

                    // approximate contour with accuracy proportional
                    // to the contour perimeter
                    approx = approxPolyDP(contours.get(i),  Imgproc.arcLength(new MatOfPoint2f(contours.get(i).toArray()), true)*0.02, true);


                    // square contours should have 4 vertices after approximation
                    // relatively large area (to filter out noisy contours)
                    // and be convex.
                    // Note: absolute value of an area is used because
                    // area may be positive or negative - in accordance with the
                    // contour orientation

                    if( approx.toArray().length == 4 &&
                            Math.abs(Imgproc.contourArea(approx)) > 1000 &&
                            Imgproc.isContourConvex(approx) )
                    {
                        double maxCosine = 0;

                        for( int j = 2; j < 5; j++ )
                        {
                            // find the maximum cosine of the angle between joint edges
                            double cosine = Math.abs(angle(approx.toArray()[j%4], approx.toArray()[j-2], approx.toArray()[j-1]));
                            maxCosine = Math.max(maxCosine, cosine);
                        }

                        // if cosines of all angles are small
                        // (all angles are ~90 degree) then write quandrange
                        // vertices to resultant sequence
                        if( maxCosine < 0.3 ) {
                            squares.add(approx);
                        }
                    }
                }
            }
        }
        image.release();
        gray.release();
        gray0.release();
        smallerImg.release();
        System.gc();
    }


    double largest_area = 0;
    int largest_contour_index = -1;
    Rect bounding_rect;

    void findLargesSquares(final List<MatOfPoint> squares, Mat image) {
        Mat imageclone = image.clone();
        largest_area = 0;
        largest_contour_index = 0;
        largest_contour_index = -1;
        for (int i = 0; i < squares.size(); i++) {

            Rect rect = Imgproc.boundingRect(squares.get(i));

            double area = Imgproc.contourArea(squares.get(i));  //  Find the area of contour

            if (area > largest_area) {
                largest_area = area;
                largest_contour_index = i;               //Store the index of largest contour
                bounding_rect = Imgproc.boundingRect(squares.get(i)); // Find the bounding rectangle for biggest contour
            }
        }
        Imgproc.drawContours(image, squares, largest_contour_index, new Scalar(0, 0, 255),1);


//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    handlerTime = new Handler();
//                    handlerTime.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (dem < 3) {
//                                startTimer(strings[dem]);
//                                dem++;
//                            }
//                        }
//                    },1000);
//                }
//            });
        if (largest_contour_index != -1 ) {
           try {
               Rect rect = Imgproc.boundingRect(squares.get(largest_contour_index));
               mInter= imageclone.submat(rect);
               filename = setFileName();
               if (handler != null ) {
                   return;

               } else {
                   runOnUiThread(new Runnable() {
                       @Override
                       public void run() {


                           handler = new Handler();
                           handler.postDelayed(new Runnable() {
                               @Override
                               public void run() {
                                   Imgproc.cvtColor(mInter, mInter, Imgproc.COLOR_RGB2BGR);
                                   Imgcodecs.imwrite(filename, mInter);
                                   arrLinkPath.add(filename);
                                   adapterImage.notifyDataSetChanged();
                                   Singleton.getGetInstance().arrayList.add(new FileObject(filename,
                                           fileOfImage,"","",""));
                                   mInter.release();
                                   if(handler !=null) {
                                       handler.removeCallbacksAndMessages(null);
                                   }
                               }
                           },3000);

                       }
                   });
               }
           } catch (IndexOutOfBoundsException e) {e.printStackTrace();}
        } else {
            dem = 0;
           if(handler !=null) {
               runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                       mCustomProgressBar4.setVisibility(View.GONE);
                   }
               });
               handler.removeCallbacksAndMessages(null);
               handler = null;
           }
           if (handlerTime !=null) {
               handlerTime.removeCallbacksAndMessages(null);
               handlerTime = null;
               runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                       mCustomProgressBar4.setVisibility(View.GONE);
                   }
               });

           }
        }
    }

    void extractChannel(Mat source, Mat out, int channelNum) {
        List<Mat> sourceChannels = new ArrayList<Mat>();
        List<Mat> outChannel = new ArrayList<Mat>();

        Core.split(source, sourceChannels);

        outChannel.add(new Mat(sourceChannels.get(0).size(), sourceChannels.get(0).type()));

        Core.mixChannels(sourceChannels, outChannel, new MatOfInt(channelNum, 0));

        Core.merge(outChannel, out);
    }

    MatOfPoint approxPolyDP(MatOfPoint curve, double epsilon, boolean closed) {
        MatOfPoint2f tempMat = new MatOfPoint2f();

        Imgproc.approxPolyDP(new MatOfPoint2f(curve.toArray()), tempMat, epsilon, closed);

        return new MatOfPoint(tempMat.toArray());
    }
    public String setFileName() {
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
//        File path =Environment.getExternalStorageDirectory();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateandTime = sdf.format(new Date());
        String filename = "Doc " + currentDateandTime + ".jpg";
        final File file = new File(path, filename);
        filename = file.getAbsolutePath().toString();
        return filename;
    }

    private void startTimer(String s) {
        mCustomProgressBar4.setVisibility(View.VISIBLE);
        simulateProgress(s);

    }

    private void simulateProgress(final String text) {
        ValueAnimator animator = ValueAnimator.ofInt(0, 3000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int progress = (int) animation.getAnimatedValue();
                mCustomProgressBar4.setProgress(progress);
                mCustomProgressBar4.setProgressTextFormatPattern(text);
            }
        });
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setDuration(3000);
        animator.start();
    }
    public static AutoScanActivity getInstance() {
        return autoScanActivity;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(Singleton.getGetInstance().arrayList !=null) {
            Singleton.getGetInstance().arrayList.clear();
        }
        if(Singleton.getGetInstance().where.equals("ScanActivity")) {
            Singleton.getGetInstance().arrayList.clear();
            getSupportFragmentManager().popBackStack();
        } else if (Singleton.getGetInstance().where.equals("Document")) {
            Singleton.getGetInstance().arrayList.clear();
            getSupportFragmentManager().popBackStack();
        } else if (Singleton.getGetInstance().where.equals("adapterImage")) {
            getSupportFragmentManager().popBackStack();
        }
    }
}
