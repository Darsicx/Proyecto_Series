package com.example.carlos.proyecto_series;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

//Se crea el adaptador personalizado para el recycler view
public class Adaptador_peli extends RecyclerView.Adapter<Adaptador_peli.ViewHolderpeli> implements View.OnClickListener {

    ArrayList<Molde_peli> listaPeli;
    private View.OnClickListener listener;

    public Adaptador_peli(ArrayList<Molde_peli> listaPeli) {
        this.listaPeli = listaPeli;
    }

    @NonNull
    @Override
    public ViewHolderpeli onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list,viewGroup,false);
        view.setOnClickListener(this);
        return new ViewHolderpeli(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderpeli viewHolderpeli, int i) {
        viewHolderpeli.nombre.setText(listaPeli.get(i).getNombre());
        viewHolderpeli.foto.setImageBitmap(listaPeli.get(i).getCartel());
        viewHolderpeli.identifica.setText(listaPeli.get(i).getIdSerie().toString());


    }

    @Override
    public int getItemCount() {
        return listaPeli.size();
    }

    public void setOnClicklistener(View.OnClickListener listener){
        this.listener=listener;
    }

    @Override
    public void onClick(View v) {
        if (listener!=null){
            listener.onClick(v);
        }

    }

    public class ViewHolderpeli extends RecyclerView.ViewHolder {
        TextView nombre;
        ImageView foto;
        TextView identifica;
        public ViewHolderpeli(@NonNull View itemView) {
            super(itemView);
            nombre= itemView.findViewById(R.id.texto);
            foto=itemView.findViewById(R.id.imagen);
            identifica=itemView.findViewById(R.id.identificador);
        }
    }
}
