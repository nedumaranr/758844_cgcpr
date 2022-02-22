package us.nilesh.cgcjhn.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ShareCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

import us.nilesh.cgcjhn.R;
import us.nilesh.cgcjhn.interfaces.NoticeInterface;

public class NoticeViewFragment extends Fragment {

    View view;
    StorageReference storageRef;
    FirebaseStorage storage;
    String LINK=null;
    CardView viewDocBtn;
    LinearLayout attachmentLL;
    ImageView documentImage;
    TextView title,description;
    private static final String TAG="noticeview";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_notice_view, container, false);

        viewDocBtn=view.findViewById(R.id.viewDocCV);
        attachmentLL=view.findViewById(R.id.attachmentLL);
        documentImage=view.findViewById(R.id.imageViewNotice);
        title=view.findViewById(R.id.titleViewNotice);
        description=view.findViewById(R.id.desViewNotice);

        storage=FirebaseStorage.getInstance();
        storageRef=storage.getReference();
        assert getArguments() != null;
        String PATH = getArguments().getString("doc");
        String TITLE=getArguments().getString("title");
        String DESC = getArguments().getString("des");
        if (PATH!=null){
            StorageReference ref=storageRef.child(PATH);
            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    LINK= String.valueOf(uri)+"/view";
                    Log.d(TAG, "onSuccess: "+LINK);
                    ref.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
                        @Override
                        public void onSuccess(StorageMetadata storageMetadata) {
                            long sizeMB = storageMetadata.getSizeBytes() / (1024 * 1024);
                            String fileType = storageMetadata.getContentType();
                            assert fileType != null;
                            if (fileType.contains("image")){
                                attachmentLL.setVisibility(View.VISIBLE);
                                Glide.with(requireContext()).load(LINK).into(documentImage);
                            }else {
                                attachmentLL.setVisibility(View.GONE);
                                viewDocBtn.setVisibility(View.VISIBLE);
                                String[] typeOfFile=fileType.split("/");
                                Log.d(TAG, "onSuccess: type"+ Arrays.toString(typeOfFile));
                                try {
                                    File localFile=File.createTempFile("triptapjg","."+typeOfFile[1]);
                                    Log.d(TAG, "onSuccess: filename:"+localFile.toString());
                                    ref.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                            Uri pdfUri = Uri.parse(localFile.getPath());
                                            Log.d(TAG, "onSuccess: filepath:"+localFile.getPath());
                                            Intent intent = new Intent(Intent.ACTION_VIEW);
                                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                            intent.setDataAndType(pdfUri, fileType);
                                            intent.setPackage("com.adobe.reader");
                                            startActivity(intent);
                                            Log.d(TAG, "onSuccess: pdfUri:"+pdfUri.toString());
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d(TAG, "onFailure: file"+e);
                                        }
                                    });
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                        }
                    });
                }
            });
//            documentImage.setVisibility(View.VISIBLE);

//            Glide.with(this).load(storageRef.child(PATH).getDownloadUrl().toString()).into(documentImage);


//            long ONE_MEGABYTE=1024*1024;
//            storageRef.child(PATH).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
//                @Override
//                public void onSuccess(byte[] bytes) {
//                    if (PATH.contains(".pdf")){
//                        viewDocBtn.setVisibility(View.VISIBLE);
//                        Log.d("", "onSuccess: pdf");
//                    }else {
//                        documentImage.setVisibility(View.VISIBLE);
//                        documentImage.setImageBitmap(BitmapFactory.decodeByteArray(bytes,0,bytes.length));
//                    }
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    Log.d("", "onFailure: "+e);
//                }
//            });
        }
        title.setText(TITLE);
        description.setText(DESC);

//        viewDocBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                LINK="<iframe src='https://view.officeapps.live.com/op/embed.aspx?src="+LINK+"' width='px' height='px' frameborder='0'>\n</iframe>";
////                LINK= "<iframe src='http://docs.google.com/viewer?url="+LINK+"' width='100%' height='90%' style='border: none;'></iframe>";
////                LINK = "https://docs.google.com/gview?embedded=true&url="+LINK;
//                Log.d(TAG, "onClick: "+LINK);
//                Fragment fragment = new WebFragment();
//                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.cardViewNotice, fragment);
//                fragmentTransaction.addToBackStack(null);
//                Bundle args = new Bundle();
//                args.putString("LINK", LINK);
//                fragment.setArguments(args);
//                fragmentTransaction.commit();
//            }
//        });

        return view;
    }
}