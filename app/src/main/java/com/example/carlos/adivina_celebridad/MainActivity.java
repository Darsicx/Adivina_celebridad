package com.example.carlos.adivina_celebridad;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> celebDirecciones=new ArrayList<>();
    ArrayList<String> celebNames=new ArrayList<>();
    ArrayList<Integer> listi=new ArrayList<>();
    TextDownloader texto;
    int posicionImagen=0;
    int locationAnswer;
    ImageView celebImagen;
    Button b1;
    Button b2;
    Button b3;
    Button b4;


    public void adivina(View view){
        try {
            switch (view.getId()) {
                case R.id.button:
                    if (listi.get(0) == posicionImagen) {
                        Toast.makeText(this, "Correcto", Toast.LENGTH_SHORT).show();
                        generaImagen();
                        generaTextos();
                        break;

                    } else {
                        Toast.makeText(this, "Incorrecto era: " + celebNames.get(posicionImagen), Toast.LENGTH_SHORT).show();
                        generaImagen();
                        generaTextos();
                        break;
                    }

                case R.id.button2:
                    if (listi.get(1) == posicionImagen) {
                        Toast.makeText(this, "Correcto", Toast.LENGTH_SHORT).show();
                        generaImagen();
                        generaTextos();
                        break;

                    } else {
                        Toast.makeText(this, "Incorrecto era: " + celebNames.get(posicionImagen), Toast.LENGTH_SHORT).show();
                        generaImagen();
                        generaTextos();
                        break;
                    }

                case R.id.button3:
                    if (listi.get(2) == posicionImagen) {
                        Toast.makeText(this, "Correcto", Toast.LENGTH_SHORT).show();
                        generaImagen();
                        generaTextos();
                        break;

                    } else {
                        Toast.makeText(this, "Incorrecto era: " + celebNames.get(posicionImagen), Toast.LENGTH_SHORT).show();
                        generaImagen();
                        generaTextos();
                        break;
                    }

                case R.id.button4:
                    if (listi.get(3) == posicionImagen) {
                        Toast.makeText(this, "Correcto", Toast.LENGTH_SHORT).show();
                        generaImagen();
                        generaTextos();
                        break;
                    } else {
                        Toast.makeText(this, "Incorrecto era: " + celebNames.get(posicionImagen), Toast.LENGTH_SHORT).show();
                        generaImagen();
                        generaTextos();
                        break;

                    }
            }
        }
        catch   (Exception e){
            Toast.makeText(this, "Erro en los botones", Toast.LENGTH_SHORT).show();
        }



    }

    public class ImageDownloader extends AsyncTask<String,Void,Bitmap>{


        @Override
        protected Bitmap doInBackground(String... urls) {
            try{

                URL url=new URL(urls[0]);
                HttpURLConnection urlConnection= (HttpURLConnection) url.openConnection();
                urlConnection.connect();
                InputStream input =urlConnection.getInputStream();
                Bitmap mybitmap =BitmapFactory.decodeStream(input);
                return mybitmap;

            }
            catch (Exception e){
                Toast.makeText(MainActivity.this, "Hubo un error al descargar las imagenes", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            return null;
        }
    }

    public class TextDownloader extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(String... url) {
            try {
                String result="";
                URL url1 = new URL(url[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url1.openConnection();
                urlConnection.connect();
                InputStream input = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(input);
                int data = reader.read();

                while (data !=-1){
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }
                return result;


            }
            catch (Exception e){
                Toast.makeText(MainActivity.this, "Hubo un error al extraer el HTML", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }


            return null;
        }
    }


    public void generaImagen(){
        try {
        Bitmap imagenPoner=null;
        Random randi =new Random();
        ImageDownloader    imagenes =new ImageDownloader();// Se necesita un crear un objeto nuevo cada que se descargue una nueva imagen
        posicionImagen=randi.nextInt(celebDirecciones.size());
        imagenPoner =imagenes.execute(celebDirecciones.get(posicionImagen)).get();
        celebImagen.setImageBitmap(imagenPoner);
        } catch (ExecutionException e) {
            e.printStackTrace();
            Toast.makeText(this, "Aqui esta", Toast.LENGTH_SHORT).show();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Toast.makeText(this, "Aqui esta", Toast.LENGTH_SHORT).show();
        }

    }

    public void generaTextos(){
        try {
            listi = new ArrayList<>();
            Random rand = new Random();
            locationAnswer = rand.nextInt(4);
            int incorrectanswer;

            for (int i = 0; i < 4; i++) {
                if (i == locationAnswer) {
                    listi.add(posicionImagen);
                } else {
                    incorrectanswer = rand.nextInt(celebDirecciones.size());
                    while (incorrectanswer == posicionImagen) {
                        incorrectanswer = rand.nextInt(celebDirecciones.size());
                    }
                    listi.add(incorrectanswer);

                }
            }

            b1.setText(celebNames.get(listi.get(0)));
            b2.setText(celebNames.get(listi.get(1)));
            b3.setText(celebNames.get(listi.get(2)));
            b4.setText(celebNames.get(listi.get(3)));
        }
        catch (Exception e){
            Toast.makeText(this, "Error en los textos", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b1=(Button)findViewById(R.id.button);
        b2=(Button)findViewById(R.id.button2);
        b3=(Button)findViewById(R.id.button3);
        b4=(Button)findViewById(R.id.button4);


        celebImagen=findViewById(R.id.imageView);

        texto=new TextDownloader();
        String resultadoHtml =null;
        try {
            resultadoHtml=texto.execute("http://www.posh24.se/kandisar").get();
            String[] splitResultado=resultadoHtml.split("<div class=\"sidebarContainer\">");

            Pattern p = Pattern.compile("img src=\"(.*?)\"");
            Matcher m = p.matcher(splitResultado[0]);

            while (m.find()){
                celebDirecciones.add(m.group(1));
            }

            Pattern q = Pattern.compile("alt=\"(.*?)\"");
            Matcher n = q.matcher(splitResultado[0]);

            while (n.find()){
                celebNames.add(n.group(1));
            }

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        generaImagen();
        generaTextos();
    }
}
