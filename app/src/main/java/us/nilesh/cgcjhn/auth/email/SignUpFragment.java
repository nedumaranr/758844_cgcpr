package us.nilesh.cgcjhn.auth.email;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

import us.nilesh.cgcjhn.R;


public class SignUpFragment extends Fragment {

    View view;

    private ExtendedFloatingActionButton registerBtn, sigInPgeBtn;
    private EditText emailEditText,userEditText,phoneEditText,rollEditTxt;
    private TextInputEditText passEditText,confirmPassEditText;
    private FirebaseAuth mAuth;
    DatabaseReference ref;
//    private FirebaseFirestore fStore;
    private String userId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_signup, container, false);

        mAuth = FirebaseAuth.getInstance();
//        fStore= FirebaseFirestore.getInstance();

        registerBtn=(ExtendedFloatingActionButton)view.findViewById(R.id.registerButton);
        sigInPgeBtn=(ExtendedFloatingActionButton)view.findViewById(R.id.signInPageButton);
        emailEditText=(EditText)view.findViewById(R.id.editTextEmail);
        passEditText=view.findViewById(R.id.editTextPassword);
        confirmPassEditText=view.findViewById(R.id.editTextConfirmPassword);
        userEditText=(EditText)view.findViewById(R.id.editTextPersonName);
//        phoneEditText=(EditText)view.findViewById(R.id.editTextNumber);
        rollEditTxt=(EditText)view.findViewById(R.id.rollEditTxt);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerProfile();
            }
        });

        sigInPgeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(SignUpFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);

            }
        });


    }

    void updateUI(FirebaseUser getUser){
        if (getUser==null){
            Snackbar snackbar = Snackbar
                    .make(view, "Failed to register! try using different E-mail.", Snackbar.LENGTH_LONG);
            snackbar.show();
        }
        else {
            Snackbar snackbar = Snackbar
                    .make(view, "User registered, Successfully!", Snackbar.LENGTH_LONG);
            snackbar.show();
            NavHostFragment.findNavController(SignUpFragment.this)
                    .navigate(R.id.action_SecondFragment_to_FirstFragment);
        }
    }

    void registerProfile(){
        String email, password, displayName, phoneNumber, confirmPass,rollNumber,gender;
        email = emailEditText.getText().toString();
        password = passEditText.getText().toString();
        displayName=userEditText.getText().toString();
//        phoneNumber=phoneEditText.getText().toString();
        confirmPass=confirmPassEditText.getText().toString();
        rollNumber=rollEditTxt.getText().toString();

        if (TextUtils.isEmpty(displayName)) {
            userEditText.setError("Enter your name!");
            return;
        }
        if (TextUtils.isEmpty(rollNumber)) {
            userEditText.setError("Enter your roll no.");
            return;
        }
//        if (TextUtils.isEmpty(phoneNumber)) {
//            phoneEditText.setError("Enter phone number!");
//            return;
//        }
        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("E-mail required!");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            passEditText.setError("Password required!");
            return;
        }
        if (password.length()<8){
            passEditText.setError("Password should 8 digit long.");
            return;
        }
        if (!password.equals(confirmPass)) {
//            Log.d(TAG, "registerProfile: "+password+"  "+confirmPass);
            confirmPassEditText.setError("Passwords are not same!");
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this.requireActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
//                            Log.d(TAG, "createUserWithEmail:success");
                            userId= Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
                            HashMap<String,String> student=new HashMap<>();
                            student.put("email",email);
                            student.put("name",displayName);
//                            student.put("pnum", phoneNumber);
                            student.put("roll", rollNumber);
                            student.put("pass", password);
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                            ref.child("USERS").child(userId).setValue(student);

                            FirebaseUser user = mAuth.getCurrentUser();
                            user.sendEmailVerification();
                            updateUI(user);
                        } else {
                            updateUI(null);
                        }
                    }
                });
    }
}