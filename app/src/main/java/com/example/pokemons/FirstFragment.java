package com.example.pokemons;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.pokemons.databinding.FragmentFirstBinding;

import java.sql.ClientInfoStatus;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    private ArrayList<Pokemon> items;
    private ArrayAdapter<Pokemon> adapter;
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

         items = new ArrayList<Pokemon>();
         adapter = new ArrayAdapter<>(
                getContext(),
                R.layout.lv_pokemon_row,
                R.id.txtPoke,
                items
        );

        binding.lvPokemon.setAdapter(adapter);
        refresh();





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