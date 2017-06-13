package com.cst.scanner;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.cst.scanner.BaseUI.BaseFragment;
import com.cst.scanner.BaseUI.FLocationAndPurpose;
import com.cst.scanner.BaseUI.Helper.Singleton;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FAboutUs.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FAboutUs#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FAboutUs extends BaseFragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    RelativeLayout rlLocation,rlPurpose,rlLikeusFb,rlCst;
    LinearLayout llAbout,llHelps,llScan,llFiles;
    public FAboutUs() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FAboutUs.
     */
    // TODO: Rename and change types and number of parameters
    public static FAboutUs newInstance(String param1, String param2) {
        FAboutUs fragment = new FAboutUs();
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
        View view = inflater.inflate(R.layout.fragment_fabout_us,container,false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        llAbout = (LinearLayout) view.findViewById(R.id.llAbout);
        llAbout.setBackgroundColor(getResources().getColor(R.color.colorRed));
        rlLocation = (RelativeLayout) view.findViewById(R.id.rl1);
        rlLocation.setOnClickListener(this);
        rlPurpose = (RelativeLayout) view.findViewById(R.id.rl2);
        rlPurpose.setOnClickListener(this);
        rlLikeusFb = (RelativeLayout) view.findViewById(R.id.rl3);
        rlLikeusFb.setOnClickListener(this);
        rlCst = (RelativeLayout) view.findViewById(R.id.rl4);
        rlCst.setOnClickListener(this);
        llHelps = (LinearLayout) view.findViewById(R.id.llHelps);
        llHelps.setOnClickListener(this);
        llFiles = (LinearLayout) view.findViewById(R.id.llFiles);
        llFiles.setOnClickListener(this);
        llScan = (LinearLayout) view.findViewById(R.id.llScan);
        llScan.setOnClickListener(this);

        MainActivity.getInstance().getTitleBarTop().setText(R.string.btnInfo);
        MainActivity.getInstance().getButtonBack().setVisibility(View.VISIBLE);
        MainActivity.getInstance().getNavTop().setVisibility(View.VISIBLE);
        MainActivity.getInstance().getButtonBack().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.getInstance().getSupportFragmentManager().popBackStack();
                MainActivity.getInstance().getButtonBack().setVisibility(View.GONE);
                MainActivity.getInstance().getNavTop().setVisibility(View.GONE);
            }
        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity.getInstance().getTitleBarTop().setText(R.string.btnInfo);
        MainActivity.getInstance().getNavTop().setVisibility(View.VISIBLE);
        MainActivity.getInstance().getRelaNavTop().setBackgroundColor(getResources().getColor(R.color.colorNav1));
        MainActivity.getInstance().getButtonBac1k().setVisibility(View.GONE);
        MainActivity.getInstance().getButtonBack().setVisibility(View.VISIBLE);


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
            case R.id.rl1:
                MainActivity.getInstance().getButtonBack().setVisibility(View.GONE);
//                navToByReplace(MainActivity.getInstance().getSupportFragmentManager(),new FLocationAndPurpose(),
//                        "FLocationAndPurpose","FLocationAndPurpose",true,R.id.rlFabout,1);
                Intent i = new Intent(getActivity(),AddressActivity.class);
                startActivity(i);
                Singleton.getGetInstance().isLocation = true;
                break;
            case R.id.rl2:
                MainActivity.getInstance().getButtonBack().setVisibility(View.GONE);
                navToByReplace(MainActivity.getInstance().getSupportFragmentManager(),new FLocationAndPurpose(),
                        "FLocationAndPurpose","FLocationAndPurpose",true,R.id.rlFabout,1);
                Singleton.getGetInstance().isLocation = false;
                break;
            case R.id.rl3:
                MainActivity.getInstance().getButtonBack().setVisibility(View.GONE);
                goToUrl("https://www.facebook.com/CST-Solution-Vietnam-2337164829844347/");
                break;
            case R.id.rl4:
                goToUrl("http://cstsolution.com");
                break;
            case R.id.llScan:
                resetColor();
                llScan.setBackgroundColor(getResources().getColor(R.color.colorRed));
                Intent intent = new Intent(getActivity(),AutoScanActivity.class);
                startActivity(intent);
                break;
            case R.id.llFiles:
                resetColor();
                llFiles.setBackgroundColor(getResources().getColor(R.color.colorRed));

                MainActivity.getInstance().getButtonBack().setVisibility(View.GONE);
                navToByReplace(MainActivity.getInstance().getSupportFragmentManager(),new FStorage(),
                        "FStorage","FStorage",true,R.id.rlFabout,1);
                Singleton.getGetInstance().isLocation = false;
                break;
            case R.id.llHelps:
                resetColor();
                llHelps.setBackgroundColor(getResources().getColor(R.color.colorRed));

                MainActivity.getInstance().getButtonBack().setVisibility(View.GONE);
                navToByReplace(MainActivity.getInstance().getSupportFragmentManager(),new FragmentHelps(),
                        "FragmentHelps","FragmentHelps",true,R.id.rlFabout,1);
                break;
            default:break;
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    public void resetColor() {
        llAbout.setBackgroundColor(getResources().getColor(R.color.colorNav2));
        llFiles.setBackgroundColor(getResources().getColor(R.color.colorNav2));
        llHelps.setBackgroundColor(getResources().getColor(R.color.colorNav2));
        llScan.setBackgroundColor(getResources().getColor(R.color.colorNav2));

    }
}
