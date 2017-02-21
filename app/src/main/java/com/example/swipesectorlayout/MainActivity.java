package com.example.swipesectorlayout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

import tw.lychee.swipecurvelayout.ImagePair;
import tw.lychee.swipecurvelayout.SwipeSectorLayout;


public class MainActivity extends AppCompatActivity {

    TextView mNameTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mNameTv = (TextView) findViewById(R.id.pokemon_name);

        SwipeSectorLayout swipeSectorLayout = (SwipeSectorLayout) findViewById(R.id.container);
        swipeSectorLayout.addItems(getItems());
        swipeSectorLayout.notifyDataSetChanged();
        mNameTv.setText("Bulbasaur");

        swipeSectorLayout.setOnPageChangeListener(new SwipeSectorLayout.OnPageChangeListener() {
            @Override
            public void onPageSelected(int index) {
                switch (index % 4) {
                    case 0:
                        mNameTv.setText("Bulbasaur");
                        break;
                    case 1:
                        mNameTv.setText("Charmander");
                        break;
                    case 2:
                        mNameTv.setText("Squirtle");
                        break;
                    case 3:
                        mNameTv.setText("Pikachu");
                        break;
                }
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
