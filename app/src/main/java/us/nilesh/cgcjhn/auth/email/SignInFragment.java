package us.nilesh.cgcjhn.auth.email;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import us.nilesh.cgcjhn.R;
import us.nilesh.cgcjhn.auth.AuthVerify;
import us.nilesh.cgcjhn.auth.AuthenticationActivity;


public class SignInFragment extends Fragment {

    View view;

    private ExtendedFloatingActionButton registerPageBtn, signInBtn;
    private EditText emailEditTextS,passEditTextS;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private TextView forgotPassword;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_signin, container, false);

        registerPageBtn=(ExtendedFloatingActionButton)view.findViewById(R.id.registerPageButton);
        signInBtn=(ExtendedFloatingActionButton)view.findViewById(R.id.signInButton);
        emailEditTextS=(EditText)view.findViewById(R.id.editTextEmailS);
        passEditTextS=(EditText)view.findViewById(R.id.editTextPasswordS);
        forgotPassword=(TextView)view.findViewById(R.id.forgotPass);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle extras = requireActivity().getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("verification");
//            extras=null;
            if (value!=null && value.equals("pending")){
                new AlertDialog.Builder(getContext())
                        .setTitle("Verification Pending")
                        .setMessage("We've sent you an verification email.\nPlease check your Given E-Mail ID inbox.")
                        .setNegativeButton(R.string.ok, null)
                        .setIcon(R.drawable.alert_bell)
                        .show();
            }
        }
        if (user!=null){
            Intent intent = new Intent(getActivity(),AuthVerify.class);
            startActivity(intent);
            requireActivity().finish();
        }

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInProfile();
            }
        });

        registerPageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(SignInFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });
    }
    public void showDialog() {
        LayoutInflater li = LayoutInflater.from(SignInFragment.this.getActivity());
        View promptsView = li.inflate(R.layout.fragment_reset, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SignInFragment.this.getActivity());
        alertDialogBuilder.setView(promptsView);
        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.emailEntry);
        // set dialog message
        alertDialogBuilder.setCancelable(false).setNegativeButton("Send",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        String user_text = (userInput.getText()).toString();
                        if (user_text.contains(".com")) {
                            FirebaseAuth auth = FirebaseAuth.getInstance();
                            auth.sendPasswordResetEmail(user_text)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                // do something when mail was sent successfully.
                                                Snackbar snackbar = Snackbar
                                                        .make(view, "Check your E-mail inbox, we've sent you an email.", Snackbar.LENGTH_LONG);
                                                snackbar.show();
                                            }
                                        }
                                    });
                        } else{
                            String message = "Enter an valid E-mail address." + " \n \n" + "Please try again!";
                            AlertDialog.Builder builder = new AlertDialog.Builder(SignInFragment.this.getActivity());
                            builder.setTitle("Error");
                            builder.setMessage(message);
                            builder.setPositiveButton("Cancel", null);
                            builder.setNegativeButton("Retry", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    showDialog();
                                }
                            });
                            builder.create().show();
                        }
                    }
                }).setPositiveButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.dismiss();
                    }
                }
        );
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void updateUI(FirebaseUser getUser){
        if (getUser==null){
            Toast.makeText(this.getActivity(), "E-mail ID / Password not Correct Or E-mail ID not registered yet.",
                    Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(this.getActivity(), "Signing in...!", Toast.LENGTH_SHORT).show();
            if (!getUser.isEmailVerified()){
                Toast.makeText(this.getActivity(), "Verify your E-Mail ID", Toast.LENGTH_LONG).show();
            }
//            NavHostFragment.findNavController(SignInFragment.this)
//                    .navigate(R.id.action_FirstFragment_to_SecondFragment);
            Intent intent = new Intent(getActivity(), AuthVerify.class);
            startActivity(intent);
            requireActivity().finish();
        }
    }

    private void signInProfile(){
        String email, password;
        email = emailEditTextS.getText().toString();
        password = passEditTextS.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(requireContext().getApplicationContext(), "Please enter email!!",
                    Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(requireContext().getApplicationContext(), "Please enter password!!",
                    Toast.LENGTH_LONG).show();
            return;
        }

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this.requireActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = auth.getCurrentUser();
                            updateUI(user);
                        } else {
                            updateUI(null);
                        }
                    }
                });
    }
}