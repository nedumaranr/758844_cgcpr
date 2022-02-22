package us.nilesh.cgcjhn.auth;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import us.nilesh.cgcjhn.MainActivity;
import us.nilesh.cgcjhn.R;


public class AuthVerify extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference ref;
    ImageView imageView;
    CountDownTimer cdt;
    final String TAG="authscreen";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_verify);

        imageView=findViewById(R.id.timeImg);
        Glide.with(this).load(R.drawable.time).into(imageView);
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();

        cdt=new CountDownTimer(1000, 200) {
            public void onTick(long millisUntilFinished) {
            }
            public void onFinish() {
                if (user!=null){
                    if (user.isEmailVerified()){
                        Intent intent = new Intent(AuthVerify.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }else {
                        Intent intent = new Intent(AuthVerify.this,AuthenticationActivity.class);
                        intent.putExtra("verification","pending");
                        startActivity(intent);
                        finish();
                    }
                }
            }
        }.start();
    }
}