package com.placements.abhyaas.profile;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.placements.abhyaas.MainActivity;
import com.placements.abhyaas.R;
import com.placements.abhyaas.utils.CShowProgress;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class MyProfile extends Fragment {
    private static final String TAG ="MyProfile" ;
    FirebaseUser fUser;
    View V;
    TextView name,age,number,email,cpi,address,txt_user;
    ImageView email_edit;
    SharedPreferences sharedPreferences_login;
    String res= FirebaseAuth.getInstance().getCurrentUser().getUid();
    ProgressDialog progressDialog;
    Uri pdfUri = null;
    CShowProgress progress;
    FirebaseStorage storage;
    Button btn_resume;
    StorageReference storageReference;
    public static boolean UPLOADED=false;
    final String[] url=new String[2];

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fUser = FirebaseAuth.getInstance().getCurrentUser();
    }

            @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        V= inflater.inflate(R.layout.fragment_myprofile, container, false);
        
        init();
        getPreviousUser();

        Button save = V.findViewById(R.id.buttonSave);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btn_resume.getText().toString().equals("Added"))
                {
                    if(pdfUri !=null)
                        uploadfile(pdfUri);
                    else
                        Toast.makeText(getActivity(),"Select a file",Toast.LENGTH_SHORT).show();
                }
                else
                    updateProfile();
            }
        });
        btn_resume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED)
                {
                    selectpdf();
                }
                else
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},9);


            }
        });




        return V;
    }


    public void updateProfile() {
        HashMap<String, Object> map = new HashMap<>();
        init();
        map.put("Name", name.getText().toString());
        map.put("number", number.getText().toString());
        map.put("CPI", cpi.getText().toString());
        map.put("CV",url[0]);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .document(res)
                .set(map, SetOptions.merge())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                            Toast.makeText(getContext(), "Added Sucess", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(getContext(), "Error in adding", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void getPreviousUser() {
        //String res = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).toString();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .document(res)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            init();
                            name.setText(document.getString("Name"));
                           // Toast.makeText(getActivity(), name.getText().toString(), Toast.LENGTH_SHORT).show();
                            number.setText(document.getString("number"));
                            //Toast.makeText(getActivity(), number.getText().toString(), Toast.LENGTH_SHORT).show();
                            email.setText(document.getString("Email"));
                            address.setText(document.getString("ID"));
                            cpi.setText(document.getString("CPI"));
                            url[1]=document.getString("CV");
                            Log.d(TAG,"CV:-"+url[1]);
                            if(url[1]!=null)
                                UPLOADED=true;
                            else
                                UPLOADED=false;
                            checkUI();
                            
                            Toast.makeText(getContext(), "Sucessfully added", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }


    private void init() {
        name= V.findViewById(R.id.editName1);
        number= V.findViewById(R.id.editPhone);
        email= V.findViewById(R.id.editEmail);
        address= V.findViewById(R.id.edit_id);
        cpi= V.findViewById(R.id.editcpi);
        txt_user=V.findViewById(R.id.text_user);
        txt_user.setText(MainActivity.getName());
        progress = CShowProgress.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        btn_resume=V.findViewById(R.id.btn_resume);


        if (MainActivity.profile.equals("TPO"))
        {
            cpi.setVisibility(View.INVISIBLE);
            btn_resume.setVisibility(View.INVISIBLE);
            address.setVisibility(View.INVISIBLE);
        }

    }
    public void selectpdf()
    {
        Intent intent=new Intent();
        intent.setType("application/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,86);

    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == 9) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                selectpdf();
            else
                Toast.makeText(getActivity(), "please provide permission", Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 86 && resultCode== Activity.RESULT_OK && data!=null) {
            pdfUri=data.getData();
            updateUI();
        }
        else
        {
            Toast.makeText(getActivity(),"please select a file",Toast.LENGTH_SHORT).show();
        }
    }

    private void updateUI() {
        btn_resume.setText("Added");
        btn_resume.setBackgroundColor(Color.GREEN);

    }

    public void uploadfile(Uri pdfUri){
        progressDialog=new ProgressDialog(getActivity());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("Uploading file...");
        progressDialog.setProgress(0);
        progressDialog.show();


        String filename= System.currentTimeMillis()+"";
        StorageReference storageReference=storage.getReference();
        storageReference.child("Uploads").child(filename).putFile(pdfUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        StorageReference storageReference=storage.getReference();
                        storageReference.child("Uploads").child(filename).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                url[0]=uri.toString();
                                url[1]=url[0];
                                updateProfile();
                            }
                        });

                        //save_to_db();

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(getActivity(), "File failure not Uploaded",Toast.LENGTH_SHORT).show();


            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull @NotNull UploadTask.TaskSnapshot snapshot) {
                int currentprogress= (int) (100*snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                progressDialog.setProgress(currentprogress);
                if (currentprogress==100)
                {
                    Toast.makeText(getActivity(),"Uploaded",Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();

                }


            }
        });

    }
    public void checkUI()
    {
        Log.d(TAG,"uploaded value="+UPLOADED);
        if(UPLOADED)
        {
            TextView link= V.findViewById(R.id.link_resume);
            link.setVisibility(View.VISIBLE);
            link.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setData(Uri.parse(url[1]));
                    startActivity(intent);
                }
            });
        }
    }

}