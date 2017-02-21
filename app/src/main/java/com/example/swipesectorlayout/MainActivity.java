package com.example.swipesectorlayout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

import tw.lychee.swipecurvelayout.ImagePair;
import tw.lychee.swipecurvelayout.SwipeSectorLayout;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SwipeSectorLayout swipeSectorLayout = (SwipeSectorLayout) findViewById(R.id.container);
        swipeSectorLayout.addItems(getItems());

        swipeSectorLayout.setOnPageChangeListener(new SwipeSectorLayout.OnPageChangeListener() {
            @Override
            public void onPageSelected(int index) {
                //Log.d("page", Integer.toString(index));
            }
        });

    }

    private ArrayList<ImagePair> getItems() {
        ArrayList<ImagePair> items = new ArrayList<>();
        items.add(new ImagePair(R.drawable.go_001, R.drawable.bg00));
        items.add(new ImagePair(R.drawable.go_004, R.drawable.bg02));
        items.add(new ImagePair(R.drawable.go_007, R.drawable.bg01));
        items.add(new ImagePair(R.drawable.go_025, R.drawable.bg03));
        items.add(new ImagePair(R.drawable.go_001, R.drawable.bg00));
        items.add(new ImagePair(R.drawable.go_004, R.drawable.bg02));
        items.add(new ImagePair(R.drawable.go_007, R.drawable.bg01));
        items.add(new ImagePair(R.drawable.go_025, R.drawable.bg03));
        return  items;
    }
}
