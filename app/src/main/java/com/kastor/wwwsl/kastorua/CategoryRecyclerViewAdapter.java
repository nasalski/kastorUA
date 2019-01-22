package com.kastor.wwwsl.kastorua;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CategoryRecyclerViewAdapter extends RecyclerView.Adapter<CategoryRecyclerViewAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Category> itemsList;
    final private OnItemClickListener listener;
    final private String pathToPNG = "https://kastor.ua/userfiles/foto/goods/0/";

    ArrayList<Series> listSeries;
    Const aConst;
    String url;
    ApiInterface service;
    SeriesRecyclerViewAdapter adapterSeries;
    RecyclerView.LayoutManager layoutManager;

    public CategoryRecyclerViewAdapter(Context context, ArrayList<Category> itemsList, OnItemClickListener listener) {
        this.context = context;
        this.itemsList = itemsList;
        this.listener = listener;

    }
public interface OnItemClickListener{
    void OnClick( int position);
}
    //  подтянем в recyclerview файл с элементами
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category,parent,false);
        Log.v("","");
        return new ViewHolder(v, this.listener);
    }
    // в этом классе каждому элементу recyclerview присвоим значение нашего списка ItemList
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Category items = itemsList.get(position);
        try {
            listSeries = items.getSeries();
        } catch ( Exception e){
            Log.e("Error",e.getMessage());
        }
        holder.textView.setText(items.getNname());
        Typeface typeFace=FontLoader.getTypeFace(context,"BLISSPROLIGHT");
        if(typeFace!=null) holder.textView.setTypeface(typeFace);
        holder.textView.setTextSize(20);
        // пикассо нужен для загрузки картинки по ссылке url
        Picasso.with(context).load( pathToPNG + items.getFfoto())
                .error(R.mipmap.ic_launcher)
                .into(holder.imageView);
        if(items.getK_color()!="")
            holder.cardView1.getBackground().setColorFilter(Color.parseColor(items.getK_color()), PorterDuff.Mode.MULTIPLY);

        /*if(items.getK_color()!="")
            holder.content_frame.setBackgroundColor(Color.parseColor(items.getK_color()));*/
        //layoutManager = new LinearLayoutManager(context);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2);
        holder.recyclerViewSeries.setLayoutManager(gridLayoutManager);
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_slide_right);
        holder.recyclerViewSeries.setLayoutAnimation(animation);
        adapterSeries = new SeriesRecyclerViewAdapter(context, listSeries,items.getK_color(), new SeriesRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void OnClick(int pos) {
                Toast.makeText(context, "pressed " + pos, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context,SerieWeelsActivity.class);
                intent.putExtra("treeId",items.getSeries().get(pos).getTree_id());
                context.startActivity(intent);
            }
        });
        holder.recyclerViewSeries.setAdapter(adapterSeries);
    }

    @Override
    public int getItemCount() {
        try {
            return itemsList.size();
        } catch (NullPointerException e){
            return 0;
        }
    }
// в этом класе объявляем элементы экрана, а точнее каждого item'а в recyclerview
public static   class ViewHolder extends RecyclerView.ViewHolder{
    private ImageView imageView;
    private TextView textView;
    private RecyclerView recyclerViewSeries;
    private CardView cardView1;
    private LinearLayout content_frame;
    public ViewHolder(final View itemView, final OnItemClickListener listener ) {
        super(itemView);
        imageView = (ImageView) itemView.findViewById(R.id.img_category);
        textView = (TextView) itemView.findViewById(R.id.text_category);
        content_frame = (LinearLayout) itemView.findViewById(R.id.item_category);
        cardView1 = (CardView) itemView.findViewById(R.id.card_view);
        recyclerViewSeries = (RecyclerView) itemView.findViewById(R.id.recycler_serie);
        content_frame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.OnClick(getAdapterPosition());
            }
        });


    }
}
}

