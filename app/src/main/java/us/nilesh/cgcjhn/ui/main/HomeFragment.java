package us.nilesh.cgcjhn.ui.main;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

import us.nilesh.cgcjhn.R;
import us.nilesh.cgcjhn.adapter.PubNoticeAdapter;
import us.nilesh.cgcjhn.adapter.DepNoticeAdapter;
import us.nilesh.cgcjhn.interfaces.NoticeInterface;
import us.nilesh.cgcjhn.ui.NoticeViewFragment;
import us.nilesh.cgcjhn.ui.WebFragment;


public class HomeFragment extends Fragment implements NoticeInterface {

    View view;
    PubNoticeAdapter mAdapter;
    DepNoticeAdapter mDepAdapter;
//    private static final String TAG="FirstActivityLog";
    private ExtendedFloatingActionButton ptuFabBtn;
//    private HomeViewModel homeViewModel;
    private String contentA,contentD;
    private int countA,countD;
    private RecyclerView allNotice, depNotice;
    private ProgressBar aLoadingBar,dLoadingBar;
    private SwipeRefreshLayout aSwipeLayout,dSwipeLayout;
    private TextView aLoadingTV,dLoadingTV;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        view = inflater.inflate(R.layout.fragment_homenotice, container, false);
//        Log.d(TAG, "onCreateView: ");

        allNotice=view.findViewById(R.id.all_notice_rv);
        depNotice=view.findViewById(R.id.dep_notice_rv);
        aLoadingBar =view.findViewById(R.id.aLoadingNoticeBar);
        aLoadingTV =view.findViewById(R.id.aLoadingNoticeTV);
        aSwipeLayout =view.findViewById(R.id.aRefreshNotice);
        dLoadingBar =view.findViewById(R.id.dLoadingNoticeBar);
        dLoadingTV =view.findViewById(R.id.dLoadingNoticeTV);
        dSwipeLayout =view.findViewById(R.id.dRefreshNotice);

        ArrayList<String> aTitle = new ArrayList<>();
        ArrayList<String> aTime = new ArrayList<>();
        ArrayList<String> aDate = new ArrayList<>();
        ArrayList<String> aContent = new ArrayList<>();
        ArrayList<String> dTitle = new ArrayList<>();
        ArrayList<String> dTime = new ArrayList<>();
        ArrayList<String> dDate = new ArrayList<>();
        ArrayList<String> dContent = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("UPLOADS");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                contentA = snapshot.child("publicAnnouncement").getValue().toString();
                contentD = snapshot.child("departmentAnnouncement").getValue().toString();
                String[] aArray=contentA.split(", ");
                String[] dArray=contentD.split(", ");
                countA =aArray.length;
                countD=dArray.length;
                for (int i = 0; i< countA; i++){
                    if (i==0 && aArray[i].contains("{")){
                        aArray[i]=aArray[i].replace("{","");
                    }else if (i== countA-1 && aArray[i].contains("}")){
                        aArray[i]=aArray[i].replace("}","");
                    }
                    String[] innerArray = aArray[i].split("=");
                    for (int j=0;j<1;j++){
                        String[] inArray=innerArray[0].split(" | ");
//                        Log.d(TAG, "onDataChange: "+ Arrays.toString(inArray));
                        aDate.add(String.valueOf(inArray[0]));
                        aTime.add(String.valueOf(inArray[2]));
                        aTitle.add(String.valueOf(inArray[4]));
                        aContent.add(String.valueOf(innerArray[1]));
                    }
                }
                for (int i = 0; i< countD; i++){
                    if (i==0 && dArray[i].contains("{")){
                        dArray[i]=dArray[i].replace("{","");
                    }else if (i== countD-1 && dArray[i].contains("}")){
                        dArray[i]=dArray[i].replace("}","");
                    }
                    String[] innerArray = dArray[i].split("=");
                    for (int j=0;j<1;j++){
                        String[] inArray=innerArray[0].split(" | ");
//                        Log.d(TAG, "onDataChange: "+ Arrays.toString(inArray));
                        dDate.add(String.valueOf(inArray[0]));
                        dTime.add(String.valueOf(inArray[2]));
                        dTitle.add(String.valueOf(inArray[4]));
                        dContent.add(String.valueOf(innerArray[1]));
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
//        --------All-Notice--------
        allNotice.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new PubNoticeAdapter(getContext(), aTitle,aContent,aDate,aTime,this);
//        --------Dep-Notice--------
        depNotice.setLayoutManager(new LinearLayoutManager(getContext()));
        mDepAdapter=new DepNoticeAdapter(getContext(),dTitle,dContent,dDate,dTime,this);

        CountDownTimer cdtA=new CountDownTimer(1000, 1000) {
            public void onTick(long millisUntilFinished) {
            }
            public void onFinish() {
                aLoadingBar.setVisibility(View.GONE);
                aLoadingTV.setVisibility(View.GONE);
                allNotice.setAdapter(mAdapter);
                if (aTitle.isEmpty()){
                    selfCall();
                }
            }
        }.start();
        CountDownTimer cdtD=new CountDownTimer(1000, 1000) {
            public void onTick(long millisUntilFinished) {
            }
            public void onFinish() {
                dLoadingBar.setVisibility(View.GONE);
                dLoadingTV.setVisibility(View.GONE);
                depNotice.setAdapter(mDepAdapter);
                if (dTitle.isEmpty()){
                    selfCall();
                }
            }
        }.start();

        aSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                selfCall();
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        aSwipeLayout.setRefreshing(false);
                    }
                }, 1200);
            }
        });
        aSwipeLayout.setColorSchemeColors(
                getResources().getColor(android.R.color.holo_blue_bright),
                getResources().getColor(android.R.color.holo_green_light),
                getResources().getColor(android.R.color.holo_orange_light),
                getResources().getColor(android.R.color.holo_red_light)
        );
        dSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                selfCall();
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        dSwipeLayout.setRefreshing(false);
                    }
                }, 1200);
            }
        });
        dSwipeLayout.setColorSchemeColors(
                getResources().getColor(android.R.color.holo_blue_bright),
                getResources().getColor(android.R.color.holo_green_light),
                getResources().getColor(android.R.color.holo_orange_light),
                getResources().getColor(android.R.color.holo_red_light)
        );

        return view;
    }

//    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onClickNotice(String DocName, String Title, String Description) {
        Fragment fragment = new NoticeViewFragment();
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.constraint_first_frag, fragment);
        fragmentTransaction.addToBackStack(null);
        Bundle args = new Bundle();
//        Log.d(TAG, "onClickNotice: "+DocName);
        if (DocName != null){
            args.putString("doc", DocName);
        }
        args.putString("title",Title);
        args.putString("des",Description);
        fragment.setArguments(args);
        fragmentTransaction.commit();
//        NavHostFragment.findNavController(HomeFragment.this).navigate(R.id.action_nav_home_to_noticeViewFragment);

//        WebView webView  = new WebView(this.getActivity());
//        webView.getSettings().setJavaScriptEnabled(true);
//        final Activity activity = this.getActivity();
//        webView.setWebViewClient(new WebViewClient() {
//            @Override
//            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
//                Toast.makeText(getActivity(), description, Toast.LENGTH_SHORT).show();
//            }
//            @TargetApi(android.os.Build.VERSION_CODES.M)
//            @Override
//            public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError rerr) {
//                // Redirect to deprecated method, so you can use it in all SDK versions
//                onReceivedError(view, rerr.getErrorCode(), rerr.getDescription().toString(), req.getUrl().toString());
//            }
//        });
//        webView.loadUrl(LINK);
//        requireActivity().setContentView(webView );
    }

    private void selfCall(){
        NavHostFragment.findNavController(HomeFragment.this)
                .navigate(R.id.action_nav_home_self);
    }
}