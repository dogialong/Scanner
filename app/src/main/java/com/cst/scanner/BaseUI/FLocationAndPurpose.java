package com.cst.scanner.BaseUI;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cst.scanner.BaseUI.Helper.Singleton;
import com.cst.scanner.MainActivity;
import com.cst.scanner.R;

/**
 * Created by longdg on 28/04/2017.
 */

public class FLocationAndPurpose extends BaseFragment  {
    TextView tvPurpose,tvLocation,tvEmail;
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
        tvEmail = (TextView) view.findViewById(R.id.tvEmail);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(Singleton.getGetInstance().isLocation) {
            MainActivity.getInstance().getRelaNavTop().setBackgroundColor(getResources().getColor(R.color.colorBgAbout));
            MainActivity.getInstance().getTitleBarTop().setText("Location");

            tvLocation.setVisibility(View.VISIBLE);
            tvPurpose.setVisibility(View.GONE);
            tvEmail.setVisibility(View.GONE);
        }else {
            MainActivity.getInstance().getRelaNavTop().setBackgroundColor(getResources().getColor(R.color.colorBgAbout));
            MainActivity.getInstance().getTitleBarTop().setText("Purpose");
            tvLocation.setVisibility(View.GONE);
            tvPurpose.setVisibility(View.VISIBLE);
            tvEmail.setVisibility(View.VISIBLE);
            tvEmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND_MULTIPLE);
                    emailIntent.setType("application/image");
                    emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] {"sales@cstsolution.com"});
//                    emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Gaergistic");
//                    emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, contentEmail);
//                    emailIntent.putExtra(Intent.EXTRA_STREAM, uris);
                    startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                }
            });
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
    protected void makeLinkClickable(SpannableStringBuilder strBuilder, final URLSpan span)
    {
        int start = strBuilder.getSpanStart(span);
        int end = strBuilder.getSpanEnd(span);
        int flags = strBuilder.getSpanFlags(span);
        ClickableSpan clickable = new ClickableSpan() {
            public void onClick(View view) {
                Toast.makeText(getContext(), "hihi", Toast.LENGTH_SHORT).show();
            }
        };
        strBuilder.setSpan(clickable, start, end, flags);
        strBuilder.removeSpan(span);
    }

    protected void setTextViewHTML(TextView text, String html)
    {
        CharSequence sequence = Html.fromHtml(html);
        SpannableStringBuilder strBuilder = new SpannableStringBuilder(sequence);
        URLSpan[] urls = strBuilder.getSpans(0, sequence.length(), URLSpan.class);
        for(URLSpan span : urls) {
            makeLinkClickable(strBuilder, span);
        }
        text.setText(strBuilder);
        text.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
