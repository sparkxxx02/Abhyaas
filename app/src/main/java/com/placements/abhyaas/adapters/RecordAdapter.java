package com.placements.abhyaas.adapters;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.placements.abhyaas.databinding.RecordRowModelBinding;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.placements.abhyaas.models.RecordModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.ViewHolder> {
    public ArrayList<RecordModel> modelObjectArrayList;
    private Activity context;
    RecordRowModelBinding binding;

    private static final String TAG = "MyRequestsAdapter";

    public RecordAdapter(Activity context, ArrayList<RecordModel> modelObjectArrayList) {
        this.modelObjectArrayList = modelObjectArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding =RecordRowModelBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final RecordModel model = modelObjectArrayList.get(position);
        //simpleExoPlayerView=

        final RecordRowModelBinding binding = holder.articlerowBinding;
        SimpleDateFormat formatter = new SimpleDateFormat("EEE, MMM dd yyyy");

        binding.username.setText(model.getName());
        binding.description.setText(model.getDescription());
        binding.viewsDate.setText(model.getDate());
        binding.time.setText(model.getTime());

        binding.txtvideo.setOnClickListener(view -> {
            if (!model.getVideoUrl().equals("NA")){
                String urlAddress = model.getVideoUrl();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlAddress));
                context.startActivity(intent);
            }else{
                Toast.makeText(context, "not available!", Toast.LENGTH_SHORT).show();
            }

        });/*binding.txtaudio.setOnClickListener(view -> {
            if (!model.getAudioUrl().equals("NA")){
                String urlAddress = model.getAudioUrl();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlAddress));
                context.startActivity(intent);
            }else{
                Toast.makeText(context, "not available!", Toast.LENGTH_SHORT).show();
            }

        })*/;

        //prepareexoplayer(context.getApplication(),model.getVideoUrl(), binding );

    }



    @Override
    public int getItemCount() {
        return modelObjectArrayList == null ? 0 :
                modelObjectArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private RecordRowModelBinding articlerowBinding;

        public ViewHolder(RecordRowModelBinding articlerowBinding) {
            super(articlerowBinding.getRoot());
            this.articlerowBinding = articlerowBinding;
        }
    }


}

