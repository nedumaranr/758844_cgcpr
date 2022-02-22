package us.nilesh.cgcjhn.ui.ptu;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.fragment.app.Fragment;

import us.nilesh.cgcjhn.R;


public class ExamFormFragment extends Fragment {

    View view;
    private static final String TAG="PtuNoticeLog";
    private WebView webView;
    private String PTUUrl="https://brpaper.com/",
            CL="ads", CL2="bg-silver-deep",CL3="layer-overlay overlay-white-9 we-offer",
            CL4="parallax divider layer-overlay overlay-theme-colored-9",
            CL5="layer-overlay overlay-white-9 we-offer",CL6="divider",CL7="reservation",
            CL8="parallax divider layer-overlay overlay-theme-colored-9",CL9="bg-silver-deep",
            CL10="explore-sitemap",CL11="footer";

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_exam, container, false);

        webView = view.findViewById(R.id.ptu_exam_form_view);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                view.loadUrl("javascript:(function() { " +
                        "document.getElementsByClassName('"+CL+"')[0].style.display='none';" +
                        "document.getElementsByClassName('"+CL2+"')[0].style.display='none';" +
                        "document.getElementsByClassName('"+CL3+"')[0].style.display='none';" +
                        "document.getElementsByClassName('"+CL4+"')[0].style.display='none';" +
                        "document.getElementsByClassName('"+CL5+"')[0].style.display='none';" +
                        "document.getElementsByClassName('"+CL6+"')[0].style.display='none';" +
                        "document.getElementsByClassName('"+CL7+"')[0].style.display='none';" +
                        "document.getElementsByClassName('"+CL8+"')[0].style.display='none';" +
                        "document.getElementsByClassName('"+CL9+"')[0].style.display='none';" +
                        "document.getElementsByClassName('"+CL10+"')[0].style.display='none';" +
                        "document.getElementsByClassName('"+CL11+"')[0].style.display='none';" + "})()");
            }
        });
        webView.loadUrl(PTUUrl);

        return view;
    }
}