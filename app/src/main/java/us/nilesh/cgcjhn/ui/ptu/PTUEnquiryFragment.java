package us.nilesh.cgcjhn.ui.ptu;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.fragment.app.Fragment;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

import us.nilesh.cgcjhn.R;


public class PTUEnquiryFragment extends Fragment {

    View view;
    private static final String TAG="PtuNoticeLog";
    private WebView webview;
    private int x=0;
    private final String url="https://www.ptuexam.com/Webportal/StudentEnquiry";
    private final String ptu="https://www.ptuexam.com/login";
    private final String CLASS0="MuiPaper-root MuiAppBar-root MuiAppBar-positionAbsolute MuiAppBar-colorPrimary MuiPaper-elevation4";
    private final String CLASS1="MuiBottomNavigation-root jss85";

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_ptu_enquiry, container, false);

        webview = view.findViewById(R.id.admit_card_view);
//        WebSettings webs=webview.getSettings();
//        webs.setJavaScriptEnabled(true);
//        webs.
//        WebSettings webSettings = webView.getSettings();
//        webSettings.setJavaScriptEnabled(true);
//        webSettings.setSavePassword(true);
//        webSettings.setDomStorageEnabled(true);
//        webSettings.setSaveFormData(true);
//        webView.setWebViewClient(new WebViewClient() {
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//                webView.loadUrl(PTUUrl);
//                if (webView.getUrl().equals(PTUUrl)){
//                    Log.d(TAG, "shouldOverrideUrlLoading: GG");
//                }
//                webView.loadUrl("javascript:(function() {document.getElementByClassName('"+CLASS0+"').style.display='none';})()");
//                return super.shouldOverrideUrlLoading(view, request);
//            }
//
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                super.onPageFinished(view, url);
//            }
//        });
//        webView.loadUrl(LINK);

//        do {
//            webview.loadUrl(ptu);
//            x+=1;
//        } while(!webview.getUrl().equals(ptu));
//
//        if (x==1){
//            new MyAsynTask().execute();
//            Log.d(TAG, "onCreateView: "+x);
//        }
        new MyAsynTask().execute();
        return view;
    }

    @SuppressLint("StaticFieldLeak")
    private class MyAsynTask extends AsyncTask<Void, Void, Document> {
        @Override
        protected Document doInBackground(Void... voids) {

            Document document = null;
            try {
                document= Jsoup.connect(url).get();
                document.getElementsByClass(CLASS0).remove();
//                document.getElementsByClass("custom-header-image").remove();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return document;
        }

        @Override
        protected void onPostExecute(Document document) {
            super.onPostExecute(document);
            webview.loadDataWithBaseURL(url,document.toString(),"text/html","utf-8","");
            webview.getSettings().setCacheMode( WebSettings.LOAD_CACHE_ELSE_NETWORK );

            webview.setWebViewClient(new WebViewClient(){
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                    view.loadUrl(url);
                    return super.shouldOverrideUrlLoading(view, request);
                }
            });
        }
    }
    
}