package com.kastor.wwwsl.kastorua;


import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.text.Html.FROM_HTML_MODE_COMPACT;


/**
 * A simple {@link Fragment} subclass.
 */
public class CharactFragment extends Fragment {
    private int treeId;
    private ApiInterface service;
    private ArrayList<Series> listSeries;
    private Const aConst;
    private String url;

    private TextView textViewTverd;
    private LinearLayout conteinerTverd;
    private TextView textViewMaterial;
    private TextView textViewTerm;
    private TextView textViewWeight;
    private TextView textViewDesc;


    public CharactFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_charact, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        treeId = getArguments().getInt("treeId");
        textViewMaterial = (TextView) getActivity().findViewById(R.id.textViewMaterial);
        textViewTverd = (TextView) getActivity().findViewById(R.id.textViewTverd);
        conteinerTverd = (LinearLayout) getActivity().findViewById(R.id.conteinerTverd);
        textViewTerm = (TextView) getActivity().findViewById(R.id.textViewTerm);
        textViewWeight = (TextView) getActivity().findViewById(R.id.textViewWeight);
        textViewDesc = (TextView) getActivity().findViewById(R.id.textViewDesc);

        listSeries = new ArrayList<>();
        aConst = new Const();
        url = aConst.BASE_URL;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(ApiInterface.class);
        Call<List<Series>> call = service.getSerie(treeId);
        call.enqueue(new Callback<List<Series>>() {
            @Override
            public void onResponse(Call<List<Series>> call, Response<List<Series>> response) {
                if(response.body()!= null)
                    listSeries.addAll(response.body());
 //               Toast.makeText(getContext(),treeId,Toast.LENGTH_SHORT).show();
                Log.v("Slava","response - " + String.valueOf(response.body().get(0).toString()));
                Log.v("Slava","tverd - " + String.valueOf(listSeries.get(0).getTverd()));
                Log.v("Slava", "treeId " + String.valueOf(treeId));
                if(listSeries.get(0).getTverd().equals(""))
                    conteinerTverd.setVisibility(View.GONE);
                else textViewTverd.setText(String.valueOf(listSeries.get(0).getTverd() + " Shore A." ));

                textViewTerm.setText(String.valueOf("от " + listSeries.get(0).getTemp_min() +
                                                        " до " + listSeries.get(0).getTemp_max() + " \u2103 "));


                /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    textViewDesc.setText(Html.fromHtml(Html.fromHtml(listSeries.get(0).getContent()).toString(), Html.FROM_HTML_MODE_LEGACY));
                } else {
                    textViewDesc.setText(Html.fromHtml(listSeries.get(0).getContent()).toString());
                }*/
                textViewDesc.setText(Html.fromHtml(listSeries.get(0).getContent()));
                Typeface typeFace=FontLoader.getTypeFace(getContext(),"BLISSPROLIGHT");
                if(typeFace!=null) {
                    textViewDesc.setTypeface(typeFace);
                    textViewTerm.setTypeface(typeFace);
                    textViewWeight.setTypeface(typeFace);
                    textViewTverd.setTypeface(typeFace);
                    textViewMaterial.setTypeface(typeFace);
                }
            }

            @Override
            public void onFailure(Call<List<Series>> call, Throwable t) {

            }
        });
    }
}
