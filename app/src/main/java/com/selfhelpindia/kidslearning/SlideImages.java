package com.selfhelpindia.kidslearning;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

public class SlideImages extends AppCompatActivity {
    private ViewPager2 viewPager2;
    List<SlideList> dList;
    FetchJsonInBackground fetchJsonInBackground;
    MediaPlayer mp;
    ImageView leftSlide, rightSlide, autoPlay, musicController;
    SharedPreferences sf;
    SharedPreferences.Editor editor;
    int currentPage = 0;
    private static final String SHARED_PREF = "MusicController";
    Timer timer;
    final long DELAY_MS = 200;//delay in milliseconds before task is to be executed
    long PERIOD_MS = 2500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_images);
        viewPager2 = findViewById(R.id.viewPagerImageSlider);
        sf = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        mp = new MediaPlayer();
        timer = new Timer(); // This will create a new Thread
        leftSlide = findViewById(R.id.leftSlide);
        rightSlide = findViewById(R.id.rightSlide);
        autoPlay = findViewById(R.id.autoPlayButton);
        musicController = findViewById(R.id.musicController);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dList = new ArrayList<>();
        fetchJsonInBackground = new FetchJsonInBackground();
        fetchJsonInBackground.execute();
        viewPager2.setOffscreenPageLimit(1);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                playMusic(position);
            }
        });
        leftSlide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                timer.cancel();
                autoPlay.setImageResource(R.drawable.playbutton);
                int position = viewPager2.getCurrentItem();
                if (position == 0) {
                    viewPager2.setCurrentItem(position, true);
                    return;
                }
                viewPager2.setCurrentItem(position - 1, true);
            }
        });
        rightSlide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                timer.cancel();
                autoPlay.setImageResource(R.drawable.playbutton);
                int position = viewPager2.getCurrentItem();
                if (position == viewPager2.getAdapter().getItemCount() - 1) {
                    viewPager2.setCurrentItem(position, true);
                    return;
                }
                viewPager2.setCurrentItem(position + 1, true);
            }
        });
        autoPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                if (autoPlay.getTag().toString().trim().equals("on")) {
                    autoPlay.setImageResource(R.drawable.pausebutton);
                    autoPlay.setTag("off");
                    currentPage = viewPager2.getCurrentItem();
                    final Handler handler = new Handler();
                    final Runnable Update = new Runnable() {
                        public void run() {
                            if (currentPage == viewPager2.getAdapter().getItemCount()) {
                                currentPage = 0;
                            }
                            viewPager2.setCurrentItem(currentPage++, true);
                        }
                    };
                    timer = new Timer(); // This will create a new Thread
                    timer.schedule(new TimerTask() { // task to be scheduled
                        @Override
                        public void run() {
                            handler.post(Update);
                        }
                    }, DELAY_MS, PERIOD_MS);
                    //And your necessary code
                } else if (autoPlay.getTag().toString().trim().equals("off")) {
                    autoPlay.setTag("on");
                    autoPlay.setImageResource(R.drawable.playbutton);
                    timer.cancel();
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                    viewPager2.setCurrentItem(viewPager2.getCurrentItem(), true);
                    //And your necessary code
                }
            }
        });

    }

    private void playMusic(int position) {
        SlideList slideList = dList.get(position);
        if (!sf.getString("music", "on").equals("on")) {
            mp.setVolume(0.0f, 0.0f);
            return;
        }
        stopPlaying();
        mp = MediaPlayer.create(getApplicationContext(), getResources().getIdentifier(slideList.getMusic(),
                "raw", getPackageName()));
        mp.start();
    }

    private void stopPlaying() {
        if (mp != null) {
            mp.stop();
        }
    }


    public void backToMainActivity(View view) {
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        timer.cancel();
        stopPlaying();
    }

    @Override
    public void onDestroy() {
        // Don't forget to shutdown tts!
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        timer.cancel();
        stopPlaying();
        if (fetchJsonInBackground != null) {
            if (!fetchJsonInBackground.isCancelled()) {
                fetchJsonInBackground.cancel(true);
            }
        }
        super.onDestroy();
    }

    public void manageSound(View view) {
        editor = sf.edit();
        if (musicController.getTag().toString().trim().equals("on")) {
            musicController.setImageResource(R.drawable.ic_baseline_volume_off_24);
            musicController.setTag("off");
            editor.putString("music", "off");
            mp.setVolume(0.0f, 0.0f);

            //And your necessary code
        } else if (musicController.getTag().toString().trim().equals("off")) {
            musicController.setTag("on");
            musicController.setImageResource(R.drawable.ic_baseline_volume_up_24);
            mp.setVolume(1, 1);
            editor.putString("music", "on");
            editor.apply();
            playMusic(viewPager2.getCurrentItem());
            //And your necessary code
        }
        editor.apply();
    }

    public class FetchJsonInBackground extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (!sf.getString("music", "on").equals("on")) {
                musicController.setImageResource(R.drawable.ic_baseline_volume_off_24);
            }
            dList.clear();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            JSONObject reader = null;
            try {
                String dash_name = "";
                switch (getIntent().getStringExtra("dash_name")) {
                    case "स्वर":
                        dash_name = "vowels";
                        break;
                    case "व्यंजन":
                        dash_name = "consonants";
                        break;
                    case "संख्या":
                        dash_name = "numbers";
                        break;
                    case "सब्जी":
                        dash_name = "vegetables";
                        break;
                    case "जंगली जानवर":
                        dash_name = "wildAnimals";
                        break;
                    case "फल":
                        dash_name = "fruits";
                        break;
                    case "पक्षी":
                        dash_name = "birds";
                        break;
                    case "पालतू जानवर":
                        dash_name = "domesticAnimals";
                        break;
                    case "वाहन":
                        dash_name = "vehicles";
                        break;
                    case "शारीर के अंग":
                        dash_name = "bodyparts";
                        break;
                    case "ऋतू":
                        dash_name = "seasons";
                        break;
                    case "स्थानों के नाम":
                        dash_name = "places";
                        break;
                    case "दिन":
                        dash_name = "days";
                        break;
                    case "महीना":
                        dash_name = "months";
                        break;
                    case "सौर मंडल":
                        dash_name = "solarsystem";
                        break;
                    case "रंग":
                        dash_name = "colors";
                        break;
                }
                reader = new JSONObject(loadJSONFromAssets());
                JSONObject reader1 = reader.getJSONObject(dash_name);
                JSONArray jsonArray = reader1.getJSONArray("assets");
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        String music = obj.getString("sound");
                        String image = obj.getString("img") + ".webp";
                        String fullText = obj.getString("word");
                        String oneWord1 = "";
                        String oneWord2 = "";
                        if (obj.has("u") || obj.has("l")){
                            oneWord1 = obj.getString("u");
                            oneWord2 = obj.getString("l");
                        }

                        dList.add(new SlideList(image, music, fullText,oneWord1,oneWord2));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            viewPager2.setAdapter(new SliderAdapter(dList, viewPager2, getApplicationContext()));

        }

        private String loadJSONFromAssets() {
            String json = null;
            try {
                InputStream inputStream = getApplicationContext().getAssets().open("alphabet.json");
                int size = inputStream.available();
                byte[] buffer = new byte[size];
                inputStream.read(buffer);
                inputStream.close();
                json = new String(buffer, StandardCharsets.UTF_8);
            } catch (IOException ex) {
                ex.printStackTrace();
                return null;
            }
            return json;
        }
    }
}