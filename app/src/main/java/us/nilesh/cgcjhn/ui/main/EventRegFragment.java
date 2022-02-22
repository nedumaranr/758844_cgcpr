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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import us.nilesh.cgcjhn.interfaces.EnquiryInterface;
import us.nilesh.cgcjhn.R;
import us.nilesh.cgcjhn.adapter.EventAdapter;


public class EventRegFragment extends Fragment implements EnquiryInterface {

    View view;
//    private EventRegViewModel eventRegViewModel;
    private RecyclerView eventList;
    private EventAdapter eventAdapter;
    private ProgressBar loadingBar;
    private SwipeRefreshLayout swipeLayout;
    private TextView loadingTV;
    private String links, count;
//    private static final String TAG="eventfragment";

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        eventRegViewModel = new ViewModelProvider(this).get(EventRegViewModel.class);
        view = inflater.inflate(R.layout.fragment_event, container, false);

        eventList =view.findViewById(R.id.enquiryListRV);
        loadingBar=view.findViewById(R.id.loadingEventBar);
        loadingTV=view.findViewById(R.id.loadingEventTV);
        swipeLayout=view.findViewById(R.id.refreshEvent);

        ArrayList<String> Title = new ArrayList<>();
        ArrayList<String> Time = new ArrayList<>();
        ArrayList<String> Date = new ArrayList<>();
        ArrayList<String> Content = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("UPLOADS");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                links = snapshot.child("eventRegistration").getValue().toString();
                count=snapshot.child("count").child("event").getValue().toString();
                String[] array=links.split(", ");
                for (int i=0;i<Integer.parseInt(count);i++){
                    if (i==0 && array[i].contains("{")){
                        array[i]=array[i].replace("{","");
                    }else if (i== Integer.parseInt(count)-1 && array[i].contains("}")){
                        array[i]=array[i].replace("}","");
                    }
                    String[] innerArray = array[i].split("=");
                    for (int j=0;j<1;j++){
                        String[] inArray=innerArray[0].split(" | ");
//                        Log.d(TAG, "onDataChange: "+ Arrays.toString(inArray));
                        Date.add(String.valueOf(inArray[0]));
                        Time.add(String.valueOf(inArray[2]));
                        Title.add(String.valueOf(inArray[4]));
                        Content.add(String.valueOf(innerArray[1]));
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        eventList.setLayoutManager(new LinearLayoutManager(getContext()));
        eventAdapter =new EventAdapter(getContext(),Title,Content,Date,Time,this);
        CountDownTimer cdt=new CountDownTimer(1000, 1000) {
            public void onTick(long millisUntilFinished) {
            }
            public void onFinish() {
                loadingBar.setVisibility(View.GONE);
                loadingTV.setVisibility(View.GONE);
                eventList.setAdapter(eventAdapter);
                if (Title.isEmpty()){
                    selfCall();
                }
            }
        }.start();

        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                NavHostFragment.findNavController(EventRegFragment.this)
                        .navigate(R.id.action_nav_event_self);
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        swipeLayout.setRefreshing(false);
                    }
                }, 1200);
            }
        });
        swipeLayout.setColorSchemeColors(
                getResources().getColor(android.R.color.holo_blue_bright),
                getResources().getColor(android.R.color.holo_green_light),
                getResources().getColor(android.R.color.holo_orange_light),
                getResources().getColor(android.R.color.holo_red_light)
        );

        return view;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onClickEnquiry(String LINK) {
        WebView  webView  = new WebView(this.getActivity());
        webView.getSettings().setJavaScriptEnabled(true);
        final Activity activity = this.getActivity();
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(getActivity(), description, Toast.LENGTH_SHORT).show();
            }
            @TargetApi(android.os.Build.VERSION_CODES.M)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError rerr) {
                // Redirect to deprecated method, so you can use it in all SDK versions
                onReceivedError(view, rerr.getErrorCode(), rerr.getDescription().toString(), req.getUrl().toString());
            }
        });
        webView.loadUrl(LINK);
        requireActivity().setContentView(webView );
    }

    private void selfCall(){
        NavHostFragment.findNavController(EventRegFragment.this)
                .navigate(R.id.action_nav_event_self);
    }
}