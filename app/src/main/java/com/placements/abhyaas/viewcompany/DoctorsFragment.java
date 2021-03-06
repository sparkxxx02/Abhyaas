package com.placements.abhyaas.viewcompany;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.placements.abhyaas.viewcompany.Dataclass.Dataclass_companies;
import com.placements.abhyaas.R;

import java.util.Arrays;
import java.util.List;

public class DoctorsFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private static final String TAG = "Dr";
    private FirebaseFirestore firebaseFirestore;
    String category, clinic_name;
    Spinner spinner;
    private FirestoreRecyclerAdapter recyclerAdapter;
    FirestoreRecyclerOptions<Dataclass_companies> options;
    View v;
    TextView temp1;


    public DoctorsFragment(String category) {
        this.category=category;
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_doctors, container, false);

        RecyclerView recyclerView = v.findViewById(R.id.recycler_dr);
        Query query = FirebaseFirestore.getInstance().collection("users")
                .document("Placements").collection("Names")
                .whereEqualTo("Category", category);

        options= new FirestoreRecyclerOptions.Builder<Dataclass_companies>()
                .setQuery(query, Dataclass_companies.class)
                .build();
        recyclerAdapter = new FirestoreRecyclerAdapter<Dataclass_companies, ViewHolder>(options) {
            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_doctors,parent,false);
                return new ViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Dataclass_companies model) {
                holder.setDoc_image(model.getPhotoURL());
                holder.setDoc_name(model.getName());
                holder.setcpi(model.getcpi());
                holder.setctc(model.getCTC());



                holder.itemView.findViewById(R.id.button_consult).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment,
                                new Bookdate_Fragment(model)).addToBackStack("Selected Fragment").commit();


                    }
                });

            }

        };


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(recyclerAdapter);

        //((MainScreen)getActivity()).getSupportActionBar().setTitle(category);
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }



}

