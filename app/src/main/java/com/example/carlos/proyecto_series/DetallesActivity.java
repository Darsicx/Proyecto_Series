package com.example.carlos.proyecto_series;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
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

public class DetallesActivity extends AppCompatActivity {
    //Declaramos las variables globales
    TextView titulo;
    TextView descrip;
    TextView cadenas;
    TextView sitios;
    TextView horario;
    TextView dias;
    ImageView imagen;
    String descarga = "";
    String summary="";
    String cadena = "";
    String sitio = "";
    String time = "";
    String days = "";
    String identificar="";
    ArrayList<String> episodios;
    ArrayList<String> resumen;
    ArrayList<Molde_episodios> listEpis;
    ListView lista;
    Adaptador_epi adapta;

    //Este metodo onclik sirve para pulsar el url de la serie si tiene y pasar a otra activity para visualizar la pagina
    public void cargaWeb(View view){

       if (sitios!= null || sitios.getText() != "null"){//Verificamos que exista url para poder pasar a la otra activity
           Intent nuevo=new Intent(getApplicationContext(),WebActivity.class);
           nuevo.putExtra("sitio",sitios.getText());
           startActivity(nuevo);
       }


    }

    //Clase creada para poder descargar el cartel de la serie
    public class DescargaLogo extends AsyncTask<String,Void, Bitmap>{
        @Override
        protected Bitmap doInBackground(String... urls) {

            try {
                URL url=new URL(urls[0]);
                HttpURLConnection urlConnection=(HttpURLConnection) url.openConnection();
                urlConnection.connect();
                InputStream in =urlConnection.getInputStream();
                Bitmap mybitmap= BitmapFactory.decodeStream(in);
                return mybitmap;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        //En este metodo que se ejecuta cuando termina doInBackground y lo usamos para colocar la imagen y emepezar la descarga de los episodios
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            imagen.setImageBitmap(bitmap);
            DescargaEpisodios episodios= new DescargaEpisodios();
            episodios.execute("http://api.tvmaze.com/shows/"+identificar+"/episodes");
        }
    }

    //Clase creada para descargar los episodios de las series
    public class DescargaEpisodios extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(String... urls) {
            String result = "";

            URL url = null;
            try {
                url = new URL(urls[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                while (data != -1) {
                    char current = (char) data;
                    result += current;
                    data = reader.read();

                }
                JSONArray array=new JSONArray(result);
                for (int y=0;y<array.length();y++){
                    JSONObject object= array.getJSONObject(y);
                    String primero=object.getString("id");
                    episodios.add(primero);
                    String segundo=object.getString("summary");
                    resumen.add(segundo);

                }
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

        //Cuando se terminen de descargar los episodios los colocamos en el listView con el metodo llenaEpisodio
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            llenaEpisodio();
        }
    }

    //Esta clase es creada para descargar los datos principales de la serie como su titulo,descripcion,cadena,url,horario,dias
    public class DescargaDatos extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                while (data != -1) {
                    char current = (char) data;
                    result += current;
                    data = reader.read();

                }
                    JSONObject object = new JSONObject(result);
                    String guarda = "";
                    String imagenes = "";

                    String pase = "";
                    String otroc = "";
                    String network = "";

                    String schedule = "";

                    identificar=object.getString("id");
                    descarga = object.getString("name");//Hasta aqui va bien Nombre titulo 5
                    pase = object.getString("image");
                    network = object.getString("network");
                    sitio = object.getString("officialSite");// Sitio Oficial  5
                    schedule = object.getString("schedule");
                    summary=object.getString("summary");//descripcion   5

                    JSONObject tim = new JSONObject(schedule);
                    time = tim.getString("time"); //Horario   5
                    days = tim.getString("days");// Dias   5
                    JSONObject net = new JSONObject(network);
                    cadena = net.getString("name");// Nombre Cadena  5
                    JSONObject ultimo = new JSONObject(pase);
                    otroc = ultimo.getString("medium");//Hasta aqui ya jala Imagen url   5
                    DescargaLogo logos=new DescargaLogo();
                    logos.execute(otroc);
                  //  Log.i("checa",descarga);

                    return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        //Cuando se terminen de descargar los datos los cargamos a las respectivas TextViews
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            titulo.setText(descarga);
            descrip.setText(summary);
            cadenas.setText(cadena);
            sitios.setText(sitio);
            horario.setText(time);
            dias.setText(days);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles);

         titulo=findViewById(R.id.titulo);
         descrip=findViewById(R.id.descripcion);
         cadenas=findViewById(R.id.cadena);
         sitios=findViewById(R.id.web);
         horario=findViewById(R.id.horario);
         dias=findViewById(R.id.dias);
         imagen=findViewById(R.id.imagen);
         listEpis=new ArrayList<>();
         episodios=new ArrayList<>();
         resumen=new ArrayList<>();
         lista=findViewById(R.id.listview);
        // listEpis.add(new Molde_episodios("Episodio 1","Prueba de contendio"));
        adapta=new Adaptador_epi(this,listEpis);
        lista.setAdapter(adapta);


        String hola ="";
        Intent intent = getIntent();
        hola =String.valueOf( intent.getIntExtra("nombre",0));//Obtenemos el id de la otra activity para poder descargar los datos
        Toast.makeText(this, hola, Toast.LENGTH_SHORT).show();
        try {
           DescargaDatos datos = new DescargaDatos();
           datos.execute("http://api.tvmaze.com/shows/"+hola);//Usamos el query usando el id de la serie
        }
        catch (Exception e){
            Toast.makeText(this, "Aqui hay error", Toast.LENGTH_SHORT).show();
        }


    }

    //Metodo creado prar poder añadir elementos a la lista que alimenta al adaptador
    public void llenaEpisodio(){

        int i=0;
        while (i<episodios.size()) {
            listEpis.add(new Molde_episodios(episodios.get(i),resumen.get(i))); //Se le pasan como parametros el #episodio y su resumen
            i++;
        }
        adapta.notifyDataSetChanged();//Se notiifican los cambios al adaptador
        episodios.clear();//Se borran las listas para no guardar tanta informacion
        resumen.clear();
    }
}