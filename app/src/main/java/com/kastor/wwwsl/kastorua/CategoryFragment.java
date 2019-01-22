package com.kastor.wwwsl.kastorua;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CategoryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CategoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CategoryFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ApiInterface service;
    private RecyclerView recyclerViewCategory;
    private RecyclerView recyclerViewSeries;
    //private SwipeRefreshLayout swipeRefreshLayout;
    private CategoryRecyclerViewAdapter adapterCategory;
    private SeriesRecyclerViewAdapter adapterSeries;
    private ArrayList<Category> listCategory;
    private ArrayList<Series> listSeries;
    private RecyclerView.LayoutManager layoutManager;

    private String url;
    private Const aConst;

    private OnFragmentInteractionListener mListener;

    public CategoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CategoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CategoryFragment newInstance(String param1, String param2) {
        CategoryFragment fragment = new CategoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerViewCategory = (RecyclerView) getActivity().findViewById(R.id.recycler_category);
        //swipeRefreshLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.swipe_category);
        listCategory = new ArrayList<>();
        aConst = new Const();
        url = aConst.BASE_URL;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(ApiInterface.class);
        Call<List<Category>> call = service.getCategory();
        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if(response.body()!= null)
                    listCategory.addAll(response.body());

                layoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), 1, false);
                //GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 2);
                recyclerViewCategory.setLayoutManager(layoutManager);
                adapterCategory = new CategoryRecyclerViewAdapter(getActivity().getApplicationContext(), listCategory, new CategoryRecyclerViewAdapter.OnItemClickListener() {
                    @Override
                    public void OnClick(final int position) {
                        Toast.makeText(getActivity().getApplicationContext(), "pressed " + position, Toast.LENGTH_SHORT).show();
                       /* Intent intent = new Intent(getActivity().getApplicationContext(), RecipeActivity.class);
                        intent.putExtra("categoryId",list.get(position).getId());
                        startActivity(intent);*/
                           if(listCategory.get(position).isClicked()){
                               listCategory.get(position).setSeries(null);
                               listCategory.get(position).setClicked(false);
                               adapterCategory.notifyItemChanged(position);
                           } else{
                               listCategory.get(position).setClicked(true);
                               listSeries = new ArrayList<>();
                               Call<List<Series>> call2 = service.getSeries(listCategory.get(position).getTree_id());
                               call2.enqueue(new Callback<List<Series>>() {
                                   @Override
                                   public void onResponse(Call<List<Series>> call, Response<List<Series>> response) {
                                       if(response.body()!= null)
                                           listSeries.addAll(response.body());
                                       listCategory.get(position).setSeries(listSeries);
                                       //recyclerViewCategory.scrollToPosition(position);
                                       adapterCategory.notifyItemChanged(position);

                                   }

                                   @Override
                                   public void onFailure(Call<List<Series>> call, Throwable t) {

                                   }
                               });
                           }


                    }
                });
                recyclerViewCategory.setAdapter(adapterCategory);

            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                // можем что то выдать в случае ошибки
                Log.v("", "");
            }
        });

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_category, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
