package com.example.carlos.proyecto_series;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

//Se crea el adaptador personalizado para el ListView
public class Adaptador_epi extends BaseAdapter {
    Context context;
    ArrayList<Molde_episodios> listEpi;

    public Adaptador_epi(Context context,ArrayList<Molde_episodios> listEpi) {
        this.listEpi = listEpi;
        this.context=context;
    }

    @Override
    public int getCount() {
        return listEpi.size();
    }

    @Override
    public Object getItem(int position) {
        return listEpi.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Molde_episodios molde=(Molde_episodios) getItem(position);
        convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_episodios,null);
        TextView episode=convertView.findViewById(R.id.episodio);
        TextView content=convertView.findViewById(R.id.cont);

        episode.setText(molde.getEpisodio());
        content.setText(molde.getContenido());

        return convertView;
    }
}
