package com.placements.abhyaas.viewcompany;

import android.content.Intent;
import android.graphics.Color;
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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.placements.abhyaas.viewcompany.Dataclass.*;
import com.placements.abhyaas.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecentAppointment_Fragment extends Fragment {
    private static final String TAG = "RecentAppointment_Fragm";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    TextView Time, Date, Patient, Dr, Clinic;
    ImageView Photo;
    View v;
    private FirestoreRecyclerAdapter recyclerAdapter;
    FirestoreRecyclerOptions<Dataclass_recentapp> options;

    private ImageView mapl;
    private TextView cli;
    private List<String> locations;

    private String dr, patient, clinic, date, photo, time,category;
    public static String init="0";

    public RecentAppointment_Fragment() {
        // Required empty public constructor
    }

    public RecentAppointment_Fragment(String dr, String patient, String clinic, String time, String date, String photo,String category) {
        this.dr = dr;
        this.patient = patient;
        this.clinic = clinic;
        this.time = time;
        this.date = date;
        this.photo = photo;
        this.category=category;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_recent_appointment_, container, false);
        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.recycler_recentapp);
        Query query;
        if(init.equals("1"))
        {
            query = FirebaseFirestore.getInstance().collection("users")
                    .document("Appointments").collection("Dataset");

        }
        else
        {
            query = FirebaseFirestore.getInstance().collection("users")
                    .document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("Applied");
        }

        options = new FirestoreRecyclerOptions.Builder<Dataclass_recentapp>()
                .setQuery(query, Dataclass_recentapp.class)
                .build();
        recyclerAdapter = new FirestoreRecyclerAdapter<Dataclass_recentapp, ViewHolder>(options) {
            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.confirm_bookedapp, parent, false);
                view.findViewById(R.id.btn_details).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /*onclick_details(view);*/
                    }
                });


                return new ViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Dataclass_recentapp model) {
                holder.setapp_comp(model.getCompany_name());
                holder.setApp_ctc(model.getctc());
                holder.setapp_name(model.getApplicant_name());
                holder.setskill(model.getRole());
                holder.setApp_cat(model.getCategory());
                holder.setapp_comp_photo(model.getCompany_photo());
            }
        };

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(recyclerAdapter);
        return v;
    }


    @Override
    public void onStop() {
        super.onStop();
        recyclerAdapter.stopListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        recyclerAdapter.startListening();
    }


    public void onclick_details(View view)
    {
        TextView name=view.findViewById(R.id.confirm_booked_name);
        TextView ctc=view.findViewById(R.id.confirm_ctc);
        FirebaseFirestore.getInstance().collection("users")
                .document("Placements").collection("Names")
                .whereEqualTo("Name",name.getText().toString() )
                .whereEqualTo("Category",ctc.getText().toString().substring(11))
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
                for (DocumentSnapshot snapshot : snapshotList) {
/*
                    String Name, String Category, String photoURL, String job, String cpi, String CTC,String skills,String work
*/
                    Dataclass_companies dt=new Dataclass_companies(snapshot.getString("Name"),
                                                                    snapshot.getString("Category"),
                                                                    snapshot.getString("photoURL"),
                                                                    snapshot.getString("job"),snapshot.getString("cpi"),
                            snapshot.getString("CTC"),snapshot.getString("skills"),snapshot.getString("work"));
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new Bookdate_Fragment(dt)).addToBackStack("Profile Setup").commit();



                }
            }
        });
    }
}