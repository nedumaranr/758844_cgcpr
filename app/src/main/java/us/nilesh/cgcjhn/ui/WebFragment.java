package us.nilesh.cgcjhn.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import java.util.Objects;

import us.nilesh.cgcjhn.R;
import us.nilesh.cgcjhn.adapter.EventAdapter;

public class WebFragment extends Fragment {

    View view;
    WebView webView;

    @SuppressLint("SetJavaScriptEnabled")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_web, container, false);

        webView = view.findViewById(R.id.webFragmentView);

        assert getArguments() != null;
        String value = getArguments().getString("LINK");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.loadUrl(value);
        Log.d("", "onCreateView: Web"+value);

//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setDataAndType(Uri.parse(  value), "text/html");
//        requireActivity().startActivity(intent);

        return view;
    }
}