package com.example.swipesectorlayout;

import android.support.annotation.DrawableRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import tw.lychee.swipecurvelayout.ImageAdapter;
import tw.lychee.swipecurvelayout.SwipeSectorLayout;


public class MainActivity extends AppCompatActivity {
    private ArrayList<Pokemon> mPokemons;
    private TextView mNameTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPokemons = getPokemons();
        mNameTv = (TextView) findViewById(R.id.pokemon_name);
        mNameTv.setText(mPokemons.get(0).getName());

        SwipeSectorLayout swipeSectorLayout = (SwipeSectorLayout) findViewById(R.id.container);
        swipeSectorLayout.setAdapter(new MyAdapter(mPokemons));

        swipeSectorLayout.setOnPageChangeListener(new SwipeSectorLayout.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mNameTv.setText(mPokemons.get(position).getName());
            }
        });

    }

    private ArrayList<Pokemon> getPokemons() {
        ArrayList<Pokemon> pokemons = new ArrayList<>();
        pokemons.add(new Pokemon("Bulbasaur", Pokemon.TYPE_GRASS, R.drawable.go_001));
        pokemons.add(new Pokemon("Charmander", Pokemon.TYPE_FIRE, R.drawable.go_004));
        pokemons.add(new Pokemon("Squirtle", Pokemon.TYPE_WATER, R.drawable.go_007));
        pokemons.add(new Pokemon("Pikachu", Pokemon.TYPE_ELECTRIC, R.drawable.go_025));
        return pokemons;
    }

    class MyAdapter extends ImageAdapter {
        private ArrayList<Pokemon> mItems;
        MyAdapter(ArrayList<Pokemon> items) {
            mItems = items;
        }

        @Override
        public void setItemView(int position, ImageView view) {
            view.setImageResource(mItems.get(position).getResId());
        }

        @Override
        public void setBackgroundView(int position, ImageView view) {
            switch (mItems.get(position).getType()) {
                case Pokemon.TYPE_GRASS:
                    view.setImageResource(R.drawable.bg00);
                    break;
                case Pokemon.TYPE_FIRE:
                    view.setImageResource(R.drawable.bg01);
                    break;
                case Pokemon.TYPE_WATER:
                    view.setImageResource(R.drawable.bg02);
                    break;
                case Pokemon.TYPE_ELECTRIC:
                    view.setImageResource(R.drawable.bg03);
                    break;
            }
        }

        @Override
        public int getCount() {
            return mItems.size();
        }
    }

    class Pokemon {
        final static int TYPE_GRASS = 0;
        final static int TYPE_FIRE = 1;
        final static int TYPE_WATER = 2;
        final static int TYPE_ELECTRIC = 3;

        private String mName;
        private int mType;
        private int mResId;
        Pokemon(String name, int type, @DrawableRes int resId) {
            mName = name;
            mType = type;
            mResId = resId;
        }

        public String getName() {
            return mName;
        }

        public int getType() {
            return mType;
        }

        public int getResId() {
            return mResId;
        }
    }
}
