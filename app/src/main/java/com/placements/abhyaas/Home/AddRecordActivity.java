package com.placements.abhyaas.Home;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.UploadTask;
import com.placements.abhyaas.MainActivity;
import com.placements.abhyaas.databinding.ActivityAddRecordBinding;
import com.placements.abhyaas.utils.CShowProgress;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;

public class AddRecordActivity extends AppCompatActivity {

    ActivityAddRecordBinding binding;
    String currentAdd = "";
    ProgressDialog progressDialog;
    Uri pdfUri = null;
    private int REQUEST_VIDEO = 101;
    CShowProgress progress;
    FirebaseStorage storage;
    StorageReference storageReference;
    private static String TAG="AddRecord";
    public boolean pdf=false;
    public boolean image=false;
    public int pdf_count=0;
    public int image_count=0;
    public final String[] url=new String[2];
    public static long epoch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddRecordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //Toast.makeText(AddRecordActivity.this,"Name-"+MainActivity.NAME,Toast.LENGTH_SHORT).show();
        progress = CShowProgress.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        epoch= Instant.now().toEpochMilli();




        binding.fabupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pdfUri !=null)
                    uploadfile(pdfUri);
                else
                    Toast.makeText(AddRecordActivity.this,"Select a file",Toast.LENGTH_SHORT).show();
            }
        });
        binding.selectvideolayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(AddRecordActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED)
                {
                    selectpdf();
                }
                else
                    ActivityCompat.requestPermissions(AddRecordActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},9);

            }
        });
        binding.selectimagelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(AddRecordActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED)
                {
                    selectimage();
                }
                else
                    ActivityCompat.requestPermissions(AddRecordActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},9);

            }
        });

    }


    
    public void selectpdf()
    {
        Intent intent=new Intent();
        intent.setType("application/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,86);
        pdf=true;
        image=false;
    }
    public void selectimage()
    {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,86);
        pdf=false;
        image=true;
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case 9:{
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED)
                    selectpdf();
                else
                    Toast.makeText(this,"please provide permission",Toast.LENGTH_SHORT).show();
            }

        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 86 && resultCode==RESULT_OK && data!=null) {
            pdfUri=data.getData();
            if(pdf)
                updateUI("pdf");
            else
                updateUI("image");
        }
        else
        {
            Toast.makeText(this,"please select a file",Toast.LENGTH_SHORT).show();
        }
    }
    public void uploadfile(Uri pdfUri){
        progressDialog=new ProgressDialog(this);
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

                        //save_to_db();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull @NotNull Exception e) {
                                Toast.makeText(AddRecordActivity.this, "File failure not Uploaded", Toast.LENGTH_SHORT).show();


                            }
                        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(@NonNull @NotNull UploadTask.TaskSnapshot snapshot) {
                                int currentprogress = (int) (100 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                                progressDialog.setProgress(currentprogress);
                /*if(currentprogress==100)
                {
                    progressDialog.dismiss();
                    updateUI();
                }*/
                            }
                        });

                    }



    private void save_to_db() {
        HashMap<String, Object> map = new HashMap<>();
        LocalDateTime ldt = Instant.ofEpochMilli(epoch)
                .atZone(ZoneId.systemDefault()).toLocalDateTime();

        String TandD=String.valueOf(ldt);
        int index=TandD.indexOf("T");
        String time=TandD.substring(index+1,index+6);
        String date=TandD.substring(0,index);
        map.put("Name",MainActivity.getName());
        map.put("dandt",TandD);
        map.put("videoUrl",url[0]);
        map.put("Time",time);
        map.put("Date",date);
        map.put("datetime", FieldValue.serverTimestamp());
        map.put("Description",binding.editDescribe.getText().toString());




        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Records")
                .document(String.valueOf(epoch))
                .set(map, SetOptions.merge())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(AddRecordActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(AddRecordActivity.this,MainActivity.class));
                        }
                        else
                            Toast.makeText(AddRecordActivity.this, "Error in saving", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateUI(String str) {
        if(str.equals("pdf")) {
            binding.selectimagelayout.setVisibility(View.INVISIBLE);
        }
        else {
            binding.selectvideolayout.setVisibility(View.INVISIBLE);
        }

    }
}