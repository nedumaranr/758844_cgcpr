package us.nilesh.cgcjhn.auth.phone;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;

import java.util.concurrent.TimeUnit;

import us.nilesh.cgcjhn.R;
import us.nilesh.cgcjhn.auth.AuthVerify;


public class OTPAuthFragment extends Fragment {

    View view;
    EditText otpET;
    Button verifyBtn;
    String phoneNum;
    String otpId;
    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference databaseReference;
    final String TAG="otpauthlog";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_o_t_p_auth, container, false);

        phoneNum = requireActivity().getIntent().getStringExtra("mobile").toString();
        otpET =view.findViewById(R.id.otpET);
        verifyBtn =view.findViewById(R.id.verifyOtp);
        auth =FirebaseAuth.getInstance();
        user=auth.getCurrentUser();

        initiateOtp();

        verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(otpET.getText().toString().isEmpty())
                    Toast.makeText(requireContext().getApplicationContext(),"Blank Field can not be processed",Toast.LENGTH_LONG).show();
                else if(otpET.getText().toString().length()!=6)
                    Toast.makeText(requireContext().getApplicationContext(),"INvalid OTP",Toast.LENGTH_LONG).show();
                else {
                    PhoneAuthCredential credential= PhoneAuthProvider.getCredential(otpId, otpET.getText().toString());
                    signInWithPhoneAuthCredential(credential);
                }

            }
        });

        return view;
    }

    private void initiateOtp() {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNum,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this.requireActivity(),               // Activity (for callback binding)
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        otpId =s;
                    }

                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        signInWithPhoneAuthCredential(phoneAuthCredential);
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        Toast.makeText(requireContext().getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
        // OnVerificationStateChangedCallbacks
    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this.requireActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent=new Intent(getActivity(), AuthVerify.class);
                            startActivity(intent);
                            requireActivity().finish();
                        } else {
                            Toast.makeText(requireContext().getApplicationContext(),"Signin Code Error",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

}