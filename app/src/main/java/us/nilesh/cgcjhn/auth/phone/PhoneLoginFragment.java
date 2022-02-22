package us.nilesh.cgcjhn.auth.phone;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.hbb20.CountryCodePicker;

import us.nilesh.cgcjhn.R;


public class PhoneLoginFragment extends Fragment {

    View view;
    CountryCodePicker ccp;
    EditText phoneNumET;
    Button sendOtp;
    FirebaseAuth firebaseAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_phone_login, container, false);


        phoneNumET =view.findViewById(R.id.phoneNumET);
        ccp=view.findViewById(R.id.ccp);
        ccp.registerCarrierNumberEditText(phoneNumET);
        sendOtp =view.findViewById(R.id.sendOtp);

        sendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(PhoneLoginFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });

        return view;
    }
}