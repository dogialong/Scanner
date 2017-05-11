package com.cst.scanner.BaseUI;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cst.scanner.BaseUI.Helper.Singleton;
import com.cst.scanner.MainActivity;
import com.cst.scanner.R;

/**
 * Created by longdg on 28/04/2017.
 */

public class FLocationAndPurpose extends BaseFragment  {
    TextView tvPurpose,tvLocation;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.location_purpose,container,false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvPurpose = (TextView) view.findViewById(R.id.tvPurpose);
        tvLocation = (TextView) view.findViewById(R.id.tvLocation);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(Singleton.getGetInstance().isLocation) {
            MainActivity.getInstance().getRelaNavTop().setBackgroundColor(getResources().getColor(R.color.colorBgAbout));
            MainActivity.getInstance().getTitleBarTop().setText("Location");

            tvLocation.setVisibility(View.VISIBLE);
            tvPurpose.setVisibility(View.GONE);
        }else {
            MainActivity.getInstance().getRelaNavTop().setBackgroundColor(getResources().getColor(R.color.colorBgAbout));
            MainActivity.getInstance().getTitleBarTop().setText("Purpose");
            tvLocation.setVisibility(View.GONE);
            tvPurpose.setVisibility(View.VISIBLE);
        }
        MainActivity.getInstance().getButtonBac1k().setVisibility(View.VISIBLE);
        MainActivity.getInstance().getButtonBac1k().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.getInstance().getSupportFragmentManager().popBackStack();
                MainActivity.getInstance().getNavTop().setVisibility(View.VISIBLE);
                MainActivity.getInstance().getTitleBarTop().setText("About us");
                MainActivity.getInstance().getRelaNavTop().setBackgroundColor(getResources().getColor(R.color.colorNav1));
                MainActivity.getInstance().getButtonBac1k().setVisibility(View.GONE);
                MainActivity.getInstance().getButtonBack().setVisibility(View.VISIBLE);
            }
        });
    }
}
