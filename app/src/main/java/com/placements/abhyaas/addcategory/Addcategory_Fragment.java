package com.placements.abhyaas.addcategory;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.placements.abhyaas.databinding.FragmentAddcategoryBinding;
import com.placements.abhyaas.utils.CShowProgress;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class Addcategory_Fragment extends Fragment {


    Spinner spinner;
    FragmentAddcategoryBinding binding;
    ProgressDialog progressDialog;
    Uri pdfUri = null;
    CShowProgress progress;
    FirebaseStorage storage;
    StorageReference storageReference;
    String url[]=new String[2];




    public Addcategory_Fragment() {


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding= FragmentAddcategoryBinding.inflate(inflater, container, false);
        progress = CShowProgress.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        getCategories();




        binding.fabupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pdfUri !=null && !TextUtils.isEmpty(binding.addCategory.getText().toString()))
                    uploadfile(pdfUri);
                else
                    Toast.makeText(getActivity(),"Select Fields",Toast.LENGTH_SHORT).show();
            }
        });
        binding.photoimageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED)
                {
                    selectimage();
                }
                else
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},9);

            }
        });
        return binding.getRoot();
    }



    public void save_to_db() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        HashMap<String, Object> map = new HashMap<>();
        map.put("Category", url[1]+","+binding.addCategory.getText().toString());
        db.collection("users")
                .document("Placements")
                .set(map, SetOptions.merge())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                            Toast.makeText(getContext(), "Added", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(getContext(), "Error in adding", Toast.LENGTH_SHORT).show();
                    }
                });
        map.put("Category",binding.addCategory.getText().toString());
        map.put("photo_cat",url[0]);
        db.collection("users")
                .document("Placements")
                .collection("Category")
                .document()
                .set(map, SetOptions.merge());


    }
    public void getCategories(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users")
                .document("Placements")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            url[1] = document.getString("Category");
                        }
                    }
                });
    }






    public void selectimage()
    {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,86);

    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == 9) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                selectimage();
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
                                save_to_db();
                            }
                        });

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

            }
        });

    }
    private void updateUI() {

        binding.imageText.setText("Added file");


    }


}