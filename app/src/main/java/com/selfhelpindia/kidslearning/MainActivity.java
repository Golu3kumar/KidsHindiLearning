package com.selfhelpindia.kidslearning;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    List<DashboardList> myLists;
    RecyclerView rv;
    DashBoardAdapter adapter;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        rv = (RecyclerView) findViewById(R.id.rec);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new GridLayoutManager(this, 2));
        myLists = new ArrayList<>();
        getData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        exit();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.share:
                shareApp();
                return true;
            case R.id.rateUs:
                ratingApp();
                return true;
            case R.id.exit:
                exit();
                return true;

        }
        return true;
    }

    private void exit() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.custom_exit_dialog, null);
        Button yes = view.findViewById(R.id.yes);
        Button no = view.findViewById(R.id.no);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.super.onBackPressed();
                alertDialog.cancel();
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });

        alertDialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();
        alertDialog.show();

    }

    private void ratingApp() {
        final String PLAY_STORE_LINK = "https://play.google.com/store/apps/details?id=" + getPackageName();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(PLAY_STORE_LINK));
        startActivity(intent);
    }

    private void shareApp() {
        final String PLAY_STORE_LINK = "https://play.google.com/store/apps/details?id=" + getPackageName();
        String yourShareText = "बच्चो को चित्र और आवाज की मदद से पढ़ाने के लिए,अभी नीचे दिए गए लिंक पर क्लिक कर ऐप को डाउनलोड करे. धन्यवाद!" + "\n " + PLAY_STORE_LINK;
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, yourShareText);
        intent.setType("text/plain");
        startActivity(intent);
    }

    private void getData() {
        myLists.add(new DashboardList(R.drawable.swar, "स्वर"));
        myLists.add(new DashboardList(R.drawable.varnmala, "व्यंजन"));
        myLists.add(new DashboardList(R.drawable.numbers, "संख्या"));
        myLists.add(new DashboardList(R.drawable.vegetable, "सब्जी"));
        myLists.add(new DashboardList(R.drawable.wildanimal, "जंगली जानवर"));
        myLists.add(new DashboardList(R.drawable.fruits, "फल"));
        myLists.add(new DashboardList(R.drawable.birds, "पक्षी"));
        myLists.add(new DashboardList(R.drawable.domesticanimal, "पालतू जानवर"));
        myLists.add(new DashboardList(R.drawable.transportaion, "वाहन"));
        //new
        myLists.add(new DashboardList(R.drawable.bodyparts, "शारीर के अंग"));
        myLists.add(new DashboardList(R.drawable.weather, "ऋतू"));
        myLists.add(new DashboardList(R.drawable.places, "स्थानों के नाम"));
        myLists.add(new DashboardList(R.drawable.days, "दिन"));
        myLists.add(new DashboardList(R.drawable.months, "महीना"));
        myLists.add(new DashboardList(R.drawable.galaxy, "सौर मंडल"));
        myLists.add(new DashboardList(R.drawable.colors, "रंग"));
        adapter = new DashBoardAdapter(myLists, this);
        rv.setAdapter(adapter);
    }
}