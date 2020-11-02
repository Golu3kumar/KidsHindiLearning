package com.selfhelpindia.kidslearning;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;


import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.SliderViewHolder> {
    private List<SlideList> dList;
    private ViewPager2 viewPager2;
    private Context context;

    public SliderAdapter(List<SlideList> dList, ViewPager2 viewPager2, Context context) {
        this.dList = dList;
        this.viewPager2 = viewPager2;
        this.context = context;
    }

    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.slide_image_container, parent, false);
        return new SliderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {
        holder.setImage(dList.get(position), context);
    }

    @Override
    public int getItemCount() {
        return dList.size();
    }

    static class SliderViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView fullText, oneWord1, oneWord2;

        SliderViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageSlide);
            fullText = itemView.findViewById(R.id.fullText);
            oneWord1 = itemView.findViewById(R.id.oneWord1);
            oneWord2 = itemView.findViewById(R.id.oneWord2);
        }

        void setImage(SlideList slideList, Context context) {
            try {
                InputStream inputStream = context.getAssets().open(slideList.getUri());
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                Bitmap bmp = BitmapFactory.decodeStream(bufferedInputStream);
                imageView.setImageBitmap(bmp);
                oneWord1.setText(slideList.getOneWord1());
                oneWord2.setText(slideList.getOneWord2());
                Log.d("number", slideList.getOneWord2());
                String word = slideList.getFullText();
                if (word.contains("<SPAN1>")) {
                    word = word.replace("<SPAN1>", "");
                    String firstWord = word.substring(0, word.indexOf("<"));
                    String lastWord = word.substring(word.indexOf(">") + 1);
                    Spannable wordSpanning = new SpannableString(firstWord + lastWord);
                    wordSpanning.setSpan(new ForegroundColorSpan(Color.RED), 0, firstWord.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    fullText.setText(wordSpanning);
                    return;
                }
                fullText.setText(word);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
