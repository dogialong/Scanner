package com.cst.scanner;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.cst.scanner.Adapter.MainAdapter;
import com.cst.scanner.BaseUI.BaseFragment;
import com.cst.scanner.BaseUI.Helper.Singleton;
import com.cst.scanner.Database.DatabaseHandler;
import com.cst.scanner.Delegate.IListViewClick;
import com.cst.scanner.Model.FileObject;

import java.util.ArrayList;
import java.util.List;

import static com.cst.scanner.R.id.llFiles;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FStorage.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FStorage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FStorage extends BaseFragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    MainAdapter adapter;
    RecyclerView recyclerView;
    DatabaseHandler db;
    List<FileObject> list;
    LinearLayout llFile,llHelps,llAbout,llScan;
    public FStorage() {
        // Required empty public constructor
    }

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
        View view = inflater.inflate(R.layout.fragment_fstorage,container,false);
        return view;
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
        adapter  = new MainAdapter(getContext(), list, new IListViewClick() {
            @Override
            public void onClick(View v, final int position) {
                switch (v.getId()) {
                    case R.id.ivIcon:
                        Singleton.getGetInstance().isStorage = true;
                        Singleton.getGetInstance().positionSeleted = position;
                        Singleton.getGetInstance().where = "Document";
                        Intent intent = new Intent(getActivity(),ActivitySlider.class);
                        startActivity(intent);
                        break;
                    case R.id.share:
                        break;
                    case R.id.delete:
                        showDialog(R.layout.dialog_delete, new BaseFragment.IClick() {
                            @Override
                            public void click() {
                                db.deleteObject(list.get(position));
                                list = db.getListObject();
                                adapter.loadNewList(list);
                            }

                            @Override
                            public void click2() {

                            }
                        }, true);

                        break;
                    default:break;
                }
            }
        });
        setLayoutManager();
        recyclerView.setAdapter(adapter);
        MainActivity.getInstance().getNavTop().setVisibility(View.VISIBLE);
        MainActivity.getInstance().getButtonBack().setVisibility(View.VISIBLE);
        MainActivity.getInstance().getTitleBarTop().setText(R.string.btnFolder);

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
                Intent i = new Intent(getActivity(),MainActivity.class);
                startActivity(i);
                getActivity().finish();
            }
        });
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
                Intent intent = new Intent(getActivity(),AutoScanActivity.class);
                startActivity(intent);
                break;
            case llFiles:
                resetColor();
                llFile.setBackgroundColor(getResources().getColor(R.color.colorRed));

                MainActivity.getInstance().getButtonBack().setVisibility(View.GONE);
                navToByReplace(MainActivity.getInstance().getSupportFragmentManager(),new FStorage(),
                        "FStorage","FStorage",true,R.id.rlFiles,1);
                Singleton.getGetInstance().isLocation = false;
                break;
            case R.id.llHelps:
                resetColor();
                llHelps.setBackgroundColor(getResources().getColor(R.color.colorRed));

                MainActivity.getInstance().getButtonBack().setVisibility(View.GONE);
                navToByReplace(MainActivity.getInstance().getSupportFragmentManager(),new FragmentHelps(),
                        "FragmentHelps","FragmentHelps",true,R.id.rlFiles,1);
                break;
            case R.id.llAbout:
                resetColor();
                llAbout.setBackgroundColor(getResources().getColor(R.color.colorRed));
                MainActivity.getInstance().getButtonBack().setVisibility(View.GONE);
                navToByReplace(MainActivity.getInstance().getSupportFragmentManager(),new FAboutUs(),
                        "FAboutUs","FAboutUs",true,R.id.rlFiles,1);
                break;
            default:break;
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


}
