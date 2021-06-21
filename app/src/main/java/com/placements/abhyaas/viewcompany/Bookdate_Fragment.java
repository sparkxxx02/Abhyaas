package com.placements.abhyaas.viewcompany;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.placements.abhyaas.MainActivity;
import com.placements.abhyaas.Notifications.App;
import com.placements.abhyaas.R;
import com.placements.abhyaas.viewcompany.Dataclass.Dataclass_companies;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class Bookdate_Fragment extends Fragment {

    String name, cpi, job, photo, ctc, work,skills,category;
    View v;
    private TextView skills_txt,ctc_txt,cpi_txt,job_txt,work_txt, Name;
    private Button btn;
    RecyclerView dataList;
    static List<String> array_title = new ArrayList<>();
    ImageView Photo;
    AlertDialog alertDialog;
    AlertDialog.Builder dialogBuilder;
    private static final String TAG = "Bookdate_Fragment";

    private NotificationManagerCompat notificationManagerCompat;


    public Bookdate_Fragment() {

    }

    public Bookdate_Fragment(Dataclass_companies model) {
        this.name = model.getName();
        this.cpi = model.getcpi();
        this.photo = model.getPhotoURL();
        this.ctc = model.getCTC();
        this.job=model.getjob();
        this.skills=model.getSkills();
        this.work=model.getwork();
        this.category=model.getCategory();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_bookdate_, container, false);
        //setTime(v);
        btn=v.findViewById(R.id.btn_apply);
        Name = v.findViewById(R.id.company_name);
        Name.setText(name);
        Photo = v.findViewById(R.id.company_photo);
        job_txt=v.findViewById(R.id.about_txt);
        skills_txt=v.findViewById(R.id.skills_txt);
        ctc_txt=v.findViewById(R.id.ctc_txt);
        cpi_txt=v.findViewById(R.id.cpi_txt);
        work_txt= v.findViewById(R.id.work_txt);
        Picasso.get().load(photo).error(R.drawable.ic_launcher_foreground).into(Photo);
        //getData();
        job_txt.setText(job);
        work_txt.setText(work);
        cpi_txt.setText(cpi);
        ctc_txt.setText(ctc);
        skills_txt.setText(skills);
        checkapplication();


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                set_application();
                popupwindow();
                setUI(1);



            }
        });
        return v;
    }



    public void set_application() {
        Log.d(TAG, "set_application");

        HashMap<String, Object> map = new HashMap<>();
        map.put("Applicant_name", MainActivity.getName());
        map.put("Company_name", name);
        map.put("Company_photo", photo);
        map.put("ctc", ctc);
        map.put("role",skills);
        map.put("Category",category);


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("Applied")
                .document()
                .set(map, SetOptions.merge())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                            Toast.makeText(getContext(), "Applied", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(getContext(), "Error in application", Toast.LENGTH_SHORT).show();
                    }
                });

        FirebaseFirestore.getInstance().collection("users")
                .document("Appointments").collection("Dataset")
                .document()
                .set(map, SetOptions.merge());

    }



    private void popupwindow() {
        dialogBuilder = new AlertDialog.Builder(getActivity());
        final View contactPopupView = getLayoutInflater().inflate(R.layout.confirm_bookedapp, null);
        dialogBuilder.setView(contactPopupView);
        alertDialog = dialogBuilder.create();
        alertDialog.show();

        init(contactPopupView);

        /*contactPopupView.findViewById(R.id.popup_done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });*/
    }

    private void init(View v) {
        ImageView doc_photo = v.findViewById(R.id.confirm_booked_image);
        TextView doc_name = v.findViewById(R.id.confirm_booked_name);
        TextView patient = v.findViewById(R.id.confirm_booked_patient_name);
        TextView role = v.findViewById(R.id.confirm_booked_time);
        TextView ctc = v.findViewById(R.id.confirm_ctc);

        Picasso.get().load(photo).error(R.drawable.ic_launcher_foreground).into(doc_photo);
        doc_name.setText(name);
        patient.setText(MainActivity.getName());
        role.setText("Role: "+skills_txt.getText().toString());
        ctc.setText("CTC: "+ctc_txt.getText().toString());
    }
    private void checkapplication()
    {


        if(!MainActivity.profile.equals("Student"))
        {
            btn.setVisibility(View.INVISIBLE);
        }
        else {
            if(Integer.parseInt(cpi) >= Integer.parseInt(MainActivity.getCPI()))
            {
                btn.setVisibility(View.INVISIBLE);
                Toast.makeText(getActivity(),"Not Eligble",Toast.LENGTH_SHORT).show();
            }
        }

        FirebaseFirestore.getInstance().collection("users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("Applied")
                .whereEqualTo("Company_name", name)
                .whereEqualTo("ctc", ctc)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
                if(!snapshotList.isEmpty())
                {
                    setUI(1);
                }
            }
        });
    }
    public void setUI(int i)
    {
        btn.setClickable(false);
        btn.setBackgroundColor(Color.GRAY);

        if(i ==1)
            btn.setText("Already Applied");
        else
            btn.setText("Not Eligible");

    }



}