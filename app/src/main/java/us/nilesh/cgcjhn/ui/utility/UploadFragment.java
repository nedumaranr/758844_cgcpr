package us.nilesh.cgcjhn.ui.utility;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

import us.nilesh.cgcjhn.R;

public class UploadFragment extends Fragment {

    private static final int FILE_CODE = 6969;
    View view;
    private Spinner spinner;
    private EditText titleET, contentET;
    private LinearLayout titleLL,contentLL;
    private TableRow requiredTableR;
    private TextView content, requiredField, attachedDocName;
    private CardView addDoc,attachedDoc;
    private ExtendedFloatingActionButton uploadBtn;
    private FirebaseAuth auth;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private String userId, body, title, LnT, links, count, currentDate, currentTime;
    private String countType, noticeType, displayName = null;
    private final static String SPLIT_VAL="696969";
    private String[] mimeTypes = {"image/*","application/pdf","application/msword","application/vnd.ms-powerpoint","application/vnd.ms-excel","text/plain"};
    private Uri filePath;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_upload, container, false);

        attachedDoc=view.findViewById(R.id.attachedDocCV);
        attachedDocName =view.findViewById(R.id.attachedDocNameTV);
        titleET =view.findViewById(R.id.titleET);
        contentET =view.findViewById(R.id.announcementET);
        addDoc=view.findViewById(R.id.addCV);
        uploadBtn=view.findViewById(R.id.uploadFab);
        titleLL=view.findViewById(R.id.titlell);
        contentLL=view.findViewById(R.id.contentll);
        requiredTableR=view.findViewById(R.id.requiredRow);
        spinner=view.findViewById(R.id.optionsUpload);
        content=view.findViewById(R.id.contentTV);
        requiredField=view.findViewById(R.id.requiredFieldTV);

        auth=FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        userId= Objects.requireNonNull(auth.getCurrentUser()).getUid();
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadInitials();
            }
        });

        addDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType(mimeTypes.length == 1 ? mimeTypes[0] : "*/*");
                if (mimeTypes.length > 0) {
                    intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                }
                intent.setAction(Intent.ACTION_GET_CONTENT);startActivityForResult(Intent.createChooser(intent, "Select File from here..."), FILE_CODE);
            }
        });

        String[] items = new String[]{"Not Selected", "Public Announcement", "Department Announcement", "Event Registration"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, items);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (spinner.getSelectedItem().toString()){
                    case "Not Selected":
                        titleLL.setVisibility(View.GONE);
                        contentLL.setVisibility(View.GONE);
                        requiredTableR.setVisibility(View.GONE);
                        addDoc.setVisibility(View.GONE);
                        break;
                    case "Public Announcement":
                    case "Department Announcement":
                        titleLL.setVisibility(View.VISIBLE);
                        contentLL.setVisibility(View.VISIBLE);
                        addDoc.setVisibility(View.VISIBLE);
                        requiredTableR.setVisibility(View.VISIBLE);
                        content.setText(R.string.content);
                        contentET.setHint(R.string.writehere);
                        requiredField.setText(R.string.titlecontent);
                        break;
                    case "Event Registration":
                        titleLL.setVisibility(View.VISIBLE);
                        contentLL.setVisibility(View.VISIBLE);
                        requiredTableR.setVisibility(View.VISIBLE);
                        addDoc.setVisibility(View.GONE);
                        contentET.setHint(R.string.linkhere);
                        content.setText(R.string.link);
                        requiredField.setText(R.string.titlelink);
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        return view;
    }

    @SuppressLint("Range")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        attachedDoc.setVisibility(View.VISIBLE);
        if (requestCode == FILE_CODE && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            String uriString = filePath.toString();
            File myFile = new File(uriString);
            if (uriString.startsWith("content://")) {
                try (Cursor cursor = requireActivity().getContentResolver().query(filePath, null, null, null, null)) {
                    if (cursor != null && cursor.moveToFirst()) {
                        displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    }
                }
            } else if (uriString.startsWith("file://")) {
                displayName = myFile.getName();
            }
            attachedDocName.setText(displayName);
        }
    }

    private void uploadInitials(){
        title = titleET.getText().toString();
        body = contentET.getText().toString();
        LnT = content.getText().toString();

        if (TextUtils.isEmpty(title)) {
            titleET.setError("You've to provide a Title");
            return;
        }
        if (title.contains("{") || title.contains("}")) {
            titleET.setError("You cannot use mentioned symbols in Title\nSymbols: {}[]/");
            return;
        }
        if (TextUtils.isEmpty(body)) {
            contentET.setError("You've to provide ");
            return;
        }
        if (content.getText()=="Link:" && !body.contains("https://")){
            content.setError("Enter an valid link!");
            return;
        }
        new AlertDialog.Builder(requireContext())
                .setTitle("Upload")
                .setMessage("Are you sure you want to Upload This?\nYou will not be able to change it afterwards.")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                        currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
                        Log.d("TAG", "onClick: "+currentDate+currentTime);
                        upload(spinner.getSelectedItem().toString());
                    }
                })
                .setNegativeButton(R.string.no, null)
                .setIcon(R.drawable.ic_baseline_cloud_upload_24)
                .show();
    }

    private void upload(String selectedType){
        switch (selectedType){
            case "Public Announcement":
                countType ="pubNotice";
                noticeType="publicAnnouncement";
                break;
            case "Department Announcement":
                countType ="depNotice";
                noticeType="departmentAnnouncement";
                break;
            case "Event Registration":
                countType ="event";
                noticeType="eventRegistration";
                break;
        }

        if (filePath != null) {
            ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            String docName=noticeType+"/"+UUID.randomUUID().toString()+displayName;
            StorageReference ref = storageReference.child(docName);
            ref.putFile(filePath)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    body=body+SPLIT_VAL+docName;
                                    progressDialog.dismiss();
                                    Toast.makeText(requireActivity(), "Image Uploaded!!", Toast.LENGTH_SHORT).show();
                                    finalUpload();
                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(requireActivity(),"Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage("Uploaded " + (int)progress + "%");
                                }
                            });
        }
    }

    private void finalUpload(){
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        rootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                links = snapshot.child("UPLOADS").child(noticeType).getValue().toString();
                String[] array=links.split(", ");
                count=String.valueOf(array.length);
                rootRef.child("UPLOADS").child("count").child(countType).setValue(count);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        rootRef.child("UPLOADS").child("uploader").child(userId).child(noticeType).child(currentDate+" | "+currentTime+" | "+title).setValue(body);
        rootRef.child("UPLOADS").child(noticeType).child(currentDate+" | "+currentTime+" | "+title).setValue(body);
        NavHostFragment.findNavController(UploadFragment.this).navigate(R.id.action_nav_uploadFragment_to_nav_home);
        new AlertDialog.Builder(requireContext())
                .setTitle("Upload")
                .setMessage("Uploaded Successfully!")
                .setNegativeButton(R.string.ok, null)
                .setIcon(R.drawable.ic_baseline_cloud_upload_24)
                .show();
    }
}