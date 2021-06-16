package com.placements.abhyaas.Home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.placements.abhyaas.MainActivity;
import com.placements.abhyaas.R;
import com.placements.abhyaas.adapters.RecordAdapter;
import com.placements.abhyaas.databinding.FragmentHomeBinding;
import com.placements.abhyaas.models.RecordModel;
import com.placements.abhyaas.utils.CShowProgress;
import com.placements.abhyaas.utils.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    FragmentHomeBinding binding;
    CShowProgress progress;
    String name;
    ArrayList<RecordModel> recordModels =  new ArrayList<>();
    RecordAdapter recordAdapter;
    Toolbar toolbar;
    public  HomeFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        progress = CShowProgress.getInstance();
        getActivity().getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                startActivity(new Intent(getActivity(), MainActivity.class));
            }
        });
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String link="https://forms.gle/9g8eeZorH51bLNCW8";
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(link));
                startActivity(intent);
            }
        });

        return view;
    }

    private void setupRecordAdapter() {
        recordAdapter = new RecordAdapter(getActivity(), recordModels);
        binding.recyclerview.setHasFixedSize(true);
        binding.recyclerview.setEmptyView(binding.noitemsview);
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerview.setAdapter(recordAdapter);
    }

    private void signOut() {
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(getActivity(),"Signout sucessfully", Toast.LENGTH_SHORT).show();
        /*startActivity(new Intent(this, Signin.class));
        finish();*/
    }

    @Override
    public void onStart() {
        super.onStart();
        getAllRecords();
    }

    private void getAllRecords() {
        progress.showProgress(getActivity());
        recordModels.clear();
        final FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("Records")
                .orderBy("datetime")
                .get().addOnCompleteListener(task -> {
            if (task.isComplete()) {
                progress.hideProgress();

                if (!task.getResult().isEmpty()){
                    recordModels = (ArrayList<RecordModel>) task.getResult().toObjects(RecordModel.class);
                    setupRecordAdapter();
                }else{
                    Toast.makeText(getActivity(), "no data found!", Toast.LENGTH_SHORT).show();
                    setupRecordAdapter();
                }


            }else{
                progress.hideProgress();
                setupRecordAdapter();
                Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



    }
