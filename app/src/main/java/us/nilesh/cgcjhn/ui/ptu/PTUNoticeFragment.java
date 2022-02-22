package us.nilesh.cgcjhn.ui.ptu;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import us.nilesh.cgcjhn.R;


public class PTUNoticeFragment extends Fragment {

    View view;
//    PTUNoticeAdapter mptuNoticeAdapter;
    private static final String TAG="PtuNoticeLog";
//    private RecyclerView ptuNoticeRV;
    private WebView webView;
//    String uname="1928449",password="Footwork!123";
    private String PTUUrl="https://www.ptuexam.com/PublicAnnoucements",
                   CLASS="MuiPaper-root MuiAppBar-root MuiAppBar-positionFixed MuiAppBar-colorPrimary jss1 mui-fixed MuiPaper-elevation4";

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_ptunotice, container, false);

        webView = view.findViewById(R.id.ptu_notice_view);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return true;
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                webView.loadUrl("javascript:(function() { " +
                        "document.getElementsByClassName('"+CLASS+"')[0].style.display='none'; })()");
            }
        });
        webView.loadUrl(PTUUrl);

//        webView.setDownloadListener(new DownloadListener()
//        {
//            @Override
//            public void onDownloadStart(String url, String userAgent,
//                                        String contentDisposition, String mimeType,
//                                        long contentLength) {
//                if(verifyPermissions(getActivity())){
//                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url.trim( )));
//                    request.setMimeType(mimeType);
//                    String cookies = CookieManager.getInstance().getCookie(url);
//                    request.addRequestHeader("cookie", cookies);
//                    request.addRequestHeader("User-Agent", userAgent);
//                    request.setDescription("Downloading file...");
//                    request.setTitle(URLUtil.guessFileName(url, contentDisposition,
//                            mimeType));
//                    request.allowScanningByMediaScanner(); request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//                    request.setDestinationInExternalPublicDir(
//                            Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(
//                                    url, contentDisposition, mimeType));
//                    DownloadManager dm = (DownloadManager) requireActivity().getSystemService(DOWNLOAD_SERVICE);
//                    dm.enqueue(request);
//                    Toast.makeText(requireContext().getApplicationContext(), "Downloading File",
//                            Toast.LENGTH_LONG).show();
//                }else{
////                    prompt user for permission
//                    Log.d(TAG, "onDownloadStart: ");
//                }
//
//            }});
        return view;
    }

    private static final int REQUEST_CODE_PERMISSION = 2;
    private static final String[] PERMISSIONS_REQ = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    
    private static boolean verifyPermissions(Activity activity) {
        int WritePermision = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (WritePermision != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_REQ,
                    REQUEST_CODE_PERMISSION
            );
            return false;
        } else {
            return true;
        }
    }
}