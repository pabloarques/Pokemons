package com.example.pokemons;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.pokemons.databinding.FragmentFirstBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    private ArrayList<Pokemon> items;
    private PokemonsAdapter adapter;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);

        setHasOptionsMenu(true);

        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

         items = new ArrayList<>();
         adapter = new PokemonsAdapter(
                getContext(),
                R.id.txtPoke,
                items
        );

        binding.lvPokemon.setAdapter(adapter);
        refresh();
    }

    class PokemonsAdapter extends ArrayAdapter<Pokemon> {
        public PokemonsAdapter(Context context, int resource, List<Pokemon> objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){

            Pokemon pokemon = getItem(position);

            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.lv_pokemon_row, parent, false);
            }

            //union del codigo en las views del layout

            TextView txtPoke = convertView.findViewById(R.id.txtPoke);
            ImageView img_poke = convertView.findViewById(R.id.img_poke);

            txtPoke.setText(pokemon.getNombre());

            Glide.with(getContext()).load(
                    pokemon.getImage()
            ).into(img_poke);

            return convertView;
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.item_refresh) {
            refresh();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void refresh(){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());


        executor.execute(()-> {
            PokeApi api = new PokeApi();
            ArrayList <Pokemon> pokemons = api.getPokemon();
            handler.post(()->{
                adapter.clear();
                adapter.addAll(pokemons);
             });
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}