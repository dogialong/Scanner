package com.cst.scanner;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cst.scanner.Adapter.AMain;
import com.cst.scanner.BaseUI.BaseFragment;
import com.cst.scanner.BaseUI.Helper.Singleton;
import com.cst.scanner.Database.DatabaseHandler;
import com.cst.scanner.Delegate.IListViewClick;
import com.cst.scanner.Model.FileObject;
import com.itextpdf.text.DocumentException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.cst.scanner.R.id.llFiles;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FStorage.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FStorage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FStorage extends BaseFragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    SharedPreferences preferences;
    AMain adapter;
    RecyclerView recyclerView;
    DatabaseHandler db;
    List<FileObject> list;
    LinearLayout llFile, llHelps, llAbout, llScan;
    ArrayList<String> arrsLink;
    int positionDelete = -1;
    public FStorage() {
        // Required empty public constructor
    }

    Boolean rightPassword = false;

    public static FStorage newInstance(String param1, String param2) {
        FStorage fragment = new FStorage();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.f_storage, container, false);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        preferences = getActivity().getSharedPreferences(Singleton.getGetInstance().KEY_SHAREPREFERENCE, Context.MODE_PRIVATE);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        llFile = (LinearLayout) view.findViewById(llFiles);
        llFile.setBackgroundColor(getResources().getColor(R.color.colorRed));
        llAbout = (LinearLayout) view.findViewById(R.id.llAbout);
        llAbout.setOnClickListener(this);
        llHelps = (LinearLayout) view.findViewById(R.id.llHelps);
        llHelps.setOnClickListener(this);
        llScan = (LinearLayout) view.findViewById(R.id.llScan);
        llScan.setOnClickListener(this);
        db = new DatabaseHandler(getContext());
        list = new ArrayList<>();
        list = db.getListObject();
        arrsLink = new ArrayList<>();

        adapter = new AMain(getContext(), list, new IListViewClick() {
            @Override
            public void onClick(View v, final int position) {
                switch (v.getId()) {
                    case R.id.ivIcon:
                        Singleton.getGetInstance().isStorage = true;
                        Singleton.getGetInstance().positionSeleted = position;
                        Singleton.getGetInstance().where = "Document";
                        if (list.get(position).getStatus().equals("yes")) {
                            checkPass(R.layout.dialog_key);

                        } else {
                            navToByReplace(getActivity().getSupportFragmentManager(), new ActivitySlider(), "ActivitySlider", "ActivitySlider", true, R.id.rlFiles);
                            MainActivity.getInstance().getRelaNavTop().setVisibility(View.GONE);
                        }

                        break;
                    case R.id.share:
                        arrsLink.clear();
                        try {
                            JSONObject jsonObject = new JSONObject(list.get(position).getImage());
                            JSONArray items = jsonObject.getJSONArray(Singleton.getGetInstance().key_json);
                            for (int i = 0; i < items.length(); i++) {
                                arrsLink.add(items.getString(i));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        showDialogshare(R.layout.dialog_share, new dialogClick() {
                            @Override
                            public void click(View v) {

                                convertPdf(arrsLink, nameFile);
                                if (!pass.equals("")) {
                                    try {
                                        encryptPdf(nameFile, pass);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    } catch (DocumentException e) {
                                        e.printStackTrace();
                                    }
                                }

                                Intent intentShareFile = new Intent(Intent.ACTION_SEND);
                                File fileWithinMyDir = new File(Singleton.getGetInstance().linkPdf);

                                if (fileWithinMyDir.exists()) {
                                    intentShareFile.setType("application/pdf");
                                    intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + Singleton.getGetInstance().linkPdf));

                                    intentShareFile.putExtra(Intent.EXTRA_SUBJECT,
                                            "Sharing File...");
                                    intentShareFile.putExtra(Intent.EXTRA_TEXT, "Sharing File...");

                                    startActivity(Intent.createChooser(intentShareFile, "Share File"));
                                    pass = "";
                                }
                            }
                        }, true);

                        break;
                    case R.id.delete:
                        showDialog(R.layout.dialog_delete, new BaseFragment.IClick() {
                            @Override
                            public void click() {
                                positionDelete = position;
                                if (list.get(position).getStatus().equals("yes")) {
                                    checkPassDelete(R.layout.dialog_key);
                                } else {
                                    db.deleteObject(list.get(position));
                                    list = db.getListObject();
                                    adapter.loadNewList(list);
                                }


                            }

                            @Override
                            public void click2() {

                            }
                        }, true);

                        break;
                    default:
                        break;
                }
            }
        });

        setLayoutManager();
        recyclerView.setAdapter(adapter);
        MainActivity.getInstance().

                getNavTop().

                setVisibility(View.VISIBLE);
        MainActivity.getInstance().

                getButtonBack().

                setVisibility(View.VISIBLE);
        MainActivity.getInstance().

                getTitleBarTop().

                setText(R.string.btnFolder);

    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity.getInstance().getButtonBack().setVisibility(View.VISIBLE);
        MainActivity.getInstance().getNavTop().setVisibility(View.VISIBLE);
        MainActivity.getInstance().getTitleBarTop().setText(R.string.btnFolder);
        MainActivity.getInstance().getButtonBack().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), MainActivity.class);
                startActivity(i);
                getActivity().finish();
            }
        });
        Log.d(TAG, "onResume: ");
    }

    private void setLayoutManager() {


        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2);

        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {

        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llScan:
                resetColor();
                llScan.setBackgroundColor(getResources().getColor(R.color.colorRed));
                Intent intent = new Intent(getActivity(), AutoScanActivity.class);
                startActivity(intent);
                break;
            case llFiles:
                resetColor();
                llFile.setBackgroundColor(getResources().getColor(R.color.colorRed));

                MainActivity.getInstance().getButtonBack().setVisibility(View.GONE);
                navToByReplace(MainActivity.getInstance().getSupportFragmentManager(), new FStorage(),
                        "FStorage", "FStorage", true, R.id.rlFiles, 1);
                Singleton.getGetInstance().isLocation = false;
                break;
            case R.id.llHelps:
                resetColor();
                llHelps.setBackgroundColor(getResources().getColor(R.color.colorRed));

                navToByReplace(MainActivity.getInstance().getSupportFragmentManager(), new FragmentHelp(),
                        "FragmentHelps", "FragmentHelps", true, R.id.rlFiles, 1);
                MainActivity.getInstance().getButtonBack().setVisibility(View.GONE);
                MainActivity.getInstance().getRelaNavTop().setVisibility(View.GONE);
                break;
            case R.id.llAbout:
                resetColor();
                llAbout.setBackgroundColor(getResources().getColor(R.color.colorRed));
                MainActivity.getInstance().getButtonBack().setVisibility(View.GONE);
                navToByReplace(MainActivity.getInstance().getSupportFragmentManager(), new FAboutUs(),
                        "FAboutUs", "FAboutUs", true, R.id.rlFiles, 1);
                break;
            default:
                break;
        }

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void resetColor() {
        llAbout.setBackgroundColor(getResources().getColor(R.color.colorNav2));
        llFile.setBackgroundColor(getResources().getColor(R.color.colorNav2));
        llHelps.setBackgroundColor(getResources().getColor(R.color.colorNav2));
        llScan.setBackgroundColor(getResources().getColor(R.color.colorNav2));

    }

    private String pass, nameFile;

    private interface dialogClick {
        void click(View v);
    }

    public void showDialogshare(int idView, final dialogClick click, boolean isHasCancel) {
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
        final TextView tvCountNumberPage = (TextView) dialog.findViewById(R.id.tvNumberPage);
        if (  Locale.getDefault().getDisplayLanguage().equals("Tiếng Việt")) {
            tvCountNumberPage.setText("Van ban co " + arrsLink.size() + " trang");
        } else {
            tvCountNumberPage.setText("Document has " + arrsLink.size() + " page");
        }

        final EditText edNameFile = (EditText) dialog.findViewById(R.id.edNameDoc);
        final EditText edPass = (EditText) dialog.findViewById(R.id.key);
        ImageView imgKey = (ImageView) dialog.findViewById(R.id.imgkey);
        imgKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edPass.setFocusableInTouchMode(true);
            }
        });
        edNameFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edNameFile.setSelection(0);
                edNameFile.setCursorVisible(true);
                edNameFile.setFocusable(true);
                edNameFile.setText("");
                edNameFile.setFocusableInTouchMode(true);
            }

        });
        dialog.findViewById(R.id.btnOk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pass = ((EditText) dialog.findViewById(R.id.key)).getText().toString();
                if (edNameFile.getText().toString().equals("")) {
                    nameFile = getString(R.string.name_default);
                } else {
                    nameFile = edNameFile.getText().toString();
                }

                // delete food.
                click.click(v);

                dialog.dismiss();
            }
        });
        dialog.show();
    }

    String password = "";
    private void checkPass (int view) {
        showDialogKey(view, new IClick() {
            @Override
            public void click() {
                if (password.equals(preferences.getString(Singleton.getGetInstance().password, "").toString())) {
                    MainActivity.getInstance().getRelaNavTop().setVisibility(View.GONE);
                    navToByReplace(getActivity().getSupportFragmentManager(), new ActivitySlider(), "ActivitySlider", "ActivitySlider", true, R.id.rlFiles);
                    rightPassword = true;
                } else {
                    checkPass(R.layout.dialog_success_other);
                }
            }

            @Override
            public void click2() {

            }
        }, true);
    }
    private void checkPassDelete (int view) {
        showDialogKey(view, new IClick() {
            @Override
            public void click() {
                if (password.equals(preferences.getString(Singleton.getGetInstance().password, "").toString())) {
                    db.deleteObject(list.get(positionDelete));list = db.getListObject();
                    list = db.getListObject();
                    adapter.loadNewList(list);
                } else {
                    checkPass(R.layout.dialog_success_other);
                }
            }

            @Override
            public void click2() {

            }
        }, true);
    }
    public void showDialogKey(int idView, final BaseFragment.IClick iclick, boolean isHasCancel) {
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
                password = ((EditText) dialog.findViewById(R.id.edOther)).getText().toString();
                iclick.click();
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
