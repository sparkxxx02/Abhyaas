package com.placements.abhyaas.viewcompany;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.placements.abhyaas.R;
import com.squareup.picasso.Picasso;

public class ViewHolder extends RecyclerView.ViewHolder {
    public TextView cat_name,doc_name,cpi,comp_cat,ctc,skill,app_comp,app_name,app_ctc,app_cat;
    public ImageView cat_image,doc_image,app_comp_photo;


    public ViewHolder(View itemView) {
        super(itemView);
        cat_image= itemView.findViewById(R.id.cat_Image);
        cat_name=itemView.findViewById(R.id.cat_Name);
        doc_name=itemView.findViewById(R.id.comp_name);
        ctc=itemView.findViewById(R.id.item_ctc);
        doc_image=itemView.findViewById(R.id.doc_Image);
        cpi=itemView.findViewById(R.id.cpi_txt);
        skill= itemView.findViewById(R.id.confirm_booked_time);
        //App_Date=itemView.findViewById(R.id.confirm_booked_date);
        app_comp= itemView.findViewById(R.id.confirm_booked_name);
        app_comp_photo=itemView.findViewById(R.id.confirm_booked_image);
        app_name=itemView.findViewById(R.id.confirm_booked_patient_name);
        app_ctc=itemView.findViewById(R.id.confirm_ctc);
        app_cat=itemView.findViewById(R.id.confirm_cat);

    }
    public TextView getCat_name() {
        return cat_name;
    }

    public void setCat_name(String string) {
       cat_name.setText(string);
    }


    public ImageView getCat_image() {
        return cat_image;
    }

    public void setCat_image(String url) {
        Picasso.get().load(url).error(R.drawable.ic_launcher_foreground).into(cat_image);
    }

    public TextView getDoc_name() {
        return doc_name;
    }

    public void setDoc_name(String string) {
       doc_name.setText(string);
    }

    public TextView getcpi() {
        return cpi;
    }

    public void setcpi(String string) {
        cpi.setText("CPI:"+string+" or Greater than");
    }

    public ImageView getDoc_image() {
        return doc_image;
    }

    public void setDoc_image(String url) {
        Picasso.get().load(url).error(R.drawable.ic_launcher_foreground).into(doc_image);
    }

    public void setApp_cat(String str) {
        app_cat.setText("Category: "+str);
    }

    public TextView getcomp_cat() {
        return comp_cat;
    }

    public void setcomp_cat(String string) {
        this.comp_cat = comp_cat;
    }

    public TextView getctc() {
        return ctc;
    }

    public void setctc(String string) {
        ctc.setText("CTC: "+string);
    }
    public TextView getskill() {
        return skill;
    }

    public void setskill(String str) {
        skill.setText("Role: "+str);
    }

    public TextView getapp_comp() {
        return app_comp;
    }

    public void setapp_comp(String str) {
        app_comp.setText(str);
    }

    public ImageView getapp_comp_photo() {
        return app_comp_photo;
    }

    public void setapp_comp_photo(String str) {
        Picasso.get().load(str).error(R.drawable.ic_launcher_foreground).into(app_comp_photo);
    }

    public void setapp_name(String str) {
        app_name.setText(str);
    }

    public TextView getapp_name() {
        return app_name;
    }

    public TextView getApp_ctc() {
        return app_ctc;
    }

    public void setApp_ctc(String str) {
        app_ctc.setText("CTC:"+str);
    }
}