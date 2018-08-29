package com.caucaragp.worldskills.emparejapp.models;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.caucaragp.worldskills.emparejapp.R;

public class AdapterJ extends RecyclerView.Adapter<AdapterJ.Holder>{
    //Declaración de variables
    int [] imagenesJuego;
    int item;
    Context context;
    public static boolean bandera = true;
    private OnItemClickListener onItemClickListener;

    public AdapterJ(int[] imagenesJuego, int item, Context context) {
        this.imagenesJuego = imagenesJuego;
        this.item = item;
        this.context = context;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    //Interface para el click de un item
    public interface OnItemClickListener{
        void itemClick (int position, ImageView imageView, View itemView);
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(item,parent,false);
        Holder holder = new Holder(view, onItemClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.connectData(imagenesJuego[position]);
    }

    @Override
    public int getItemCount() {
        return imagenesJuego.length;
    }

    public class Holder extends RecyclerView.ViewHolder {
        ImageView imgItem = itemView.findViewById(R.id.imgItem);
        public Holder(final View itemView, final OnItemClickListener onItemClickListener) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (bandera){
                        if (onItemClickListener!=null){
                            int position = getLayoutPosition();
                            if (position!=RecyclerView.NO_POSITION){
                                onItemClickListener.itemClick(position,imgItem,itemView);
                            }
                        }
                    }
                }
            });
        }

        //Método para conectar los datos dados por el constructor del AdapterJ a el imgItem
        public void connectData(int imagen){
            BitmapFactory.Options op = new BitmapFactory.Options();
            op.inSampleSize=1;
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), imagen,op);
            imgItem.setImageBitmap(bitmap);

        }

    }
}
