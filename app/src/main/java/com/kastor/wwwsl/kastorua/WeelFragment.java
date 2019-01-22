package com.kastor.wwwsl.kastorua;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class WeelFragment extends Fragment {
    private RecyclerView recyclerViewWeel;
    private WeelsRecyclerViewAdapter adapterWeel;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Weel> listWeels;
    private Const aConst;
    private String url;
    private ApiInterface service;

    private int treeId;


    public WeelFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerViewWeel = getActivity().findViewById(R.id.recycler_weel);
        listWeels = new ArrayList<>();
        treeId = getArguments().getInt("treeId");
        aConst = new Const();
        url = aConst.BASE_URL;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(ApiInterface.class);
        Call<List<Weel>> call = service.getSerieWeels(treeId);
        call.enqueue(new Callback<List<Weel>>() {
            @Override
            public void onResponse(Call<List<Weel>> call, Response<List<Weel>> response) {
                if(response.body()!=null) listWeels.addAll(response.body());
                layoutManager = new LinearLayoutManager(getContext(),1, false);
                recyclerViewWeel.setLayoutManager(layoutManager);
                adapterWeel = new WeelsRecyclerViewAdapter(getContext(), listWeels, new WeelsRecyclerViewAdapter.OnItemClickListener() {
                    @Override
                    public void OnClick(int position) {
                        Toast.makeText(getContext(),"pos - " + position, Toast.LENGTH_SHORT).show();
                    }
                });
                recyclerViewWeel.setAdapter(adapterWeel);
            }

            @Override
            public void onFailure(Call<List<Weel>> call, Throwable t) {

            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_weel, container, false);
    }

}
