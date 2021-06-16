package com.placements.abhyaas.Login;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.placements.abhyaas.R;

public class ViewAdapter extends PagerAdapter {
    private Context context;
    private Integer[] images={R.drawable.car_1, R.drawable.car_2,R.drawable.car_3};
    private int[] tagtext={R.string.one, R.string.two,R.string.three };
    private int[] tagtit={R.string.one_topic, R.string.two_topic,R.string.three_topic };



    public ViewAdapter(Context context)
    {
        this.context=context;
    }
    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view== object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = null;

            view = layoutInflater.inflate(R.layout.item, null);
            ImageView imageView = view.findViewById(R.id.tagphoto);
            imageView.setImageResource(images[position]);
            TextView text = view.findViewById(R.id.tagline);
            text.setText(tagtext[position]);
            TextView text2 = view.findViewById(R.id.tag_desc);
            text2.setText(tagtit[position]);

            ViewPager viewPager = (ViewPager) container;
            viewPager.addView(view, 0);
            return view;

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ViewPager viewPager=(ViewPager) container;
        View view= (View) object;
        viewPager.removeView(view);
    }

}

