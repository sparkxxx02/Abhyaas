package com.placements.abhyaas.viewcompany;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.placements.abhyaas.R;
import com.placements.abhyaas.viewcompany.Dataclass.Dataclass_category;

public class AppointmentFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private FirebaseFirestore firebaseFirestore;
    String category;
    private FirestoreRecyclerAdapter recyclerAdapter;
    FirestoreRecyclerOptions<Dataclass_category> options;
    View v;
    public AppointmentFragment() {

        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_appointment, container, false);
        RecyclerView recyclerView = v.findViewById(R.id.recycler_categories);
        Query query = FirebaseFirestore.getInstance()
                .collection("users")
                .document("Placements").collection("Category");
        options= new FirestoreRecyclerOptions.Builder<Dataclass_category>()
                .setQuery(query, Dataclass_category.class)
                .build();
        recyclerAdapter = new FirestoreRecyclerAdapter<Dataclass_category, ViewHolder>(options) {
            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_categories,parent,false);
                return new ViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Dataclass_category model) {
                holder.setCat_name(model.getCategory());
                //holder.set(model.getQuantity());
                holder.setCat_image(model.getPhoto_cat());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment,
                                new DoctorsFragment(model.getCategory())).addToBackStack("Selected Fragment").commit();

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

