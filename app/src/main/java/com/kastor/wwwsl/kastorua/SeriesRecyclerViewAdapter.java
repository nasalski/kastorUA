package com.kastor.wwwsl.kastorua;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SeriesRecyclerViewAdapter extends RecyclerView.Adapter<SeriesRecyclerViewAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Series> itemsList;
    final private SeriesRecyclerViewAdapter.OnItemClickListener listener;
    final private String pathToPNG = "https://kastor.ua/userfiles/foto/goods/0/";
    private int lastPosition;
    private String color;

    public SeriesRecyclerViewAdapter(Context context, ArrayList<Series> itemsList, String color, SeriesRecyclerViewAdapter.OnItemClickListener listener) {
        this.context = context;
        this.itemsList = itemsList;
        this.color = color;
        this.listener = listener;

    }
    public interface OnItemClickListener{
        void OnClick( int position);
    }
    //  подтянем в recyclerview файл с элементами
    @Override
    public SeriesRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_serie,parent,false);
        Log.v("","");
        return new SeriesRecyclerViewAdapter.ViewHolder(v, this.listener);
    }
    // в этом классе каждому элементу recyclerview присвоим значение нашего списка ItemList
    @Override
    public void onBindViewHolder(SeriesRecyclerViewAdapter.ViewHolder holder, final int position) {

        final Series items = itemsList.get(position);

        holder.textView.setText(items.getNname());
        Typeface typeFace=FontLoader.getTypeFace(context,"BLISSPROLIGHT");
        if(typeFace!=null) {
            holder.textView.setTypeface(typeFace);
        }
        // пикассо нужен для загрузки картинки по ссылке url
        Picasso.with(context).load( pathToPNG + items.getFfoto())
                .error(R.mipmap.ic_launcher)
                .into(holder.imageView);
        /*holder.imageView.setBackgroundColor(Color.parseColor(color));*/
        if(!color.equals(""))
            holder.imageView.getBackground().setColorFilter(Color.parseColor(color), PorterDuff.Mode.MULTIPLY);
        /*if(items.getK_color()!="")
            holder.content_frame.setBackgroundColor(Color.parseColor(items.getK_color()));*/
        //setAnimation(holder.content_frame,position);
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
        private LinearLayout content_frame;
        public ViewHolder(final View itemView, final SeriesRecyclerViewAdapter.OnItemClickListener listener ) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.img_serie);
            textView = (TextView) itemView.findViewById(R.id.text_serie);
            content_frame = (LinearLayout) itemView.findViewById(R.id.item_serie);
            content_frame.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    listener.OnClick(getAdapterPosition());
                }
            });
            textView.setTextSize(20);
        }
    }
}
