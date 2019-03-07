package com.example.carlos.proyecto_series;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    //Declaracion de variables globales
    ArrayList<Molde_peli> listape;
    ArrayList<String> titulo;
    ArrayList<Bitmap> cartel;
    ArrayList<Integer> idSeries;
    RecyclerView recyclerPeliculas;
    Adaptador_peli adapter;
    DescargaSeries series;
    DescargaImagen down;

    // Metodo con el cual se va a llamar al metodo llenar Molde cada segundo
    final Handler handler =new Handler();
    Runnable run =new Runnable() {
        @Override
        public void run() {
            try {
                llenarMolde(titulo, cartel,idSeries);//le pasamos 3 paramtros (ArrayLists)
            }
            catch (Exception e){
                Log.i("Busca","Aqui checar");
            }

            handler.postDelayed(this,1000);
        }
    };


    //Clase creada que hereda de AsyncTask para poder descargar las imagenes
    public class DescargaImagen extends AsyncTask<String,Void,Bitmap>{
        @Override
        protected Bitmap doInBackground(String... urls) {
            URL url= null;
            try {
                url = new URL(urls[0]);
                HttpURLConnection urlConnection=(HttpURLConnection) url.openConnection();
                urlConnection.connect();
                InputStream input =urlConnection.getInputStream();
                Bitmap mybitmap =BitmapFactory.decodeStream(input);
                cartel.add(mybitmap);

                return mybitmap;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);

        }
    }

    //Clase creada para obtener los datos de las series (titulo,id,url)
    public class DescargaSeries extends AsyncTask<String,Void,String>{


        @Override
        protected String doInBackground(String... urls) {
            String result="";

            try {
                URL url=new URL(urls[0]);
                HttpURLConnection urlConnection=(HttpURLConnection) url.openConnection();
                urlConnection.connect();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader =new  InputStreamReader(in);
                int data=reader.read();
                while (data!=-1){
                    char current=(char) data;
                    result+=current;
                    data=reader.read();

                }
                JSONArray array=new JSONArray(result);
                for (int j=0;j<array.length();j++){
                    JSONObject object=array.getJSONObject(j);
                    if (!object.isNull("show")) {
                        String guarda = "";
                        String identifica = "";
                        String descarga = "";
                        String pase = "";
                        String otroc = "";
                        guarda = object.getString("show");
                        JSONObject otrito = new JSONObject(guarda);
                        if (!otrito.isNull("name")) {
                            descarga = otrito.getString("name");
                            pase = otrito.getString("image");
                            identifica=otrito.getString("id");
                            idSeries.add(Integer.valueOf(identifica));
                            JSONObject ultimo = new JSONObject(pase);
                            if (!ultimo.isNull("medium")) {
                                otroc = ultimo.getString("medium");
                                 down = new DescargaImagen();
                                down.execute(otroc);
                                titulo.add(descarga);
//                                Log.i("Conversion", otroc);
//                                Log.i("Serie", identifica);

                            }
                        }
                    }


                }
                Log.i("Resultado",result);
                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText busca=findViewById(R.id.editText);
        listape=new ArrayList<>();
        titulo=new ArrayList<>();
        cartel=new ArrayList<>();
        idSeries=new ArrayList<>();
        handler.post(run);

        //Configuramos el recyclerview para que este en forma de grid y con 3 columnas
        recyclerPeliculas=findViewById(R.id.recycler);
        recyclerPeliculas.setLayoutManager(new GridLayoutManager(this,3));

        //Creamos un nuevo objeto de la clase adaptador peli (personalizado)
        adapter=new Adaptador_peli(listape);
        adapter.setOnClicklistener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//En este metodo que se implemento verifica si se pulso un elemento del recycler y se obtiene el id del elemento y lo pasa a la otra actividad
               // Log.i("investigando", String.valueOf(listape.size()));
                down.cancel(true);
                series.cancel(true);
                Intent intento=new Intent(getApplicationContext(),DetallesActivity.class);

                Integer pasa=listape.get(recyclerPeliculas.getChildAdapterPosition(v)).getIdSerie();

               intento.putExtra("nombre",pasa);
                startActivity(intento);

            }
        });
        recyclerPeliculas.setAdapter(adapter);


        //Este metodo lo utilizamos para poder saber si se ha modificado el edittext
        busca.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {//utilizamos este metodo para monitorear en cada cambio
                listape.clear();
                series= new DescargaSeries();

                series.execute("http://api.tvmaze.com/search/shows?q="+s.toString());// Ejecutamos la clase descargar series con cada cambio que se realice en el edittext

            }

            @Override
            public void afterTextChanged(Editable s) {



            }
        });
    }

    //Este metodo sirve para añadir elementos al arraylist del tipo Molde peli para que cambie el adaptador
    private void llenarMolde(ArrayList<String> nombres, ArrayList<Bitmap> carteles,ArrayList<Integer> ids){
        int j=nombres.size()-6;
        while (j< nombres.size()){
            listape.add(new Molde_peli(carteles.get(j),nombres.get(j),ids.get(j)));
            j+=1;
        }
//        int p=0;
//        while (p<nombres.size()){
//            Log.i("Verdad: ",nombres.get(p)+" "+ids.get(p));
//            p++;
//        }

        adapter.notifyDataSetChanged();//notificamos al adaptador de los cambios
        titulo.clear();//borramos los datos de los arregklos dado que se obtienen demasiados resultados
        cartel.clear();
        idSeries.clear();

    }
}
