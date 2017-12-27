package com.example.asus410.tareaonline003;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {

    EditText Efichero;
    EditText Etexto;
    boolean mExternalStorageAvailable = false;
    boolean mExternalStorageWriteable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Efichero = (EditText)findViewById(R.id.Enombre);
        Etexto = (EditText)findViewById(R.id.Etextos);
        loadPref();
    }

    private void guardar(){
        String str = Etexto.getText().toString();
        String name = Efichero.getText().toString();
        OutputStreamWriter osw = null;

        try {
            // fichero para escribir datos. Modo de apertura PRIVADO
            FileOutputStream fOut = openFileOutput(name, MODE_PRIVATE);
            // Para convertir una secuencia de caracteres en un flujo de bytes
            osw = new OutputStreamWriter(fOut);
            // escribir la cadena en el archivo
            osw.write(str);
            // asegurar que todos los bytes se escriben en el archivo
            osw.flush();
            // cierra el archivo
            osw.close();

            // mostrar mensaje de archivo guardado
            Toast.makeText(getBaseContext(),
                    "Fichero guardado satisfactoriamente", Toast.LENGTH_SHORT)
                    .show();
            // borra Edittext
            Etexto.setText("");
        } catch (IOException ex) {
            ex.printStackTrace();
        }finally {
            if (null != osw){
                try {
                    osw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void guardarSd(){
        String str = Etexto.getText().toString();
        String name = Efichero.getText().toString();

        // comprobamos disponibilidad de memoria externa SD
        comprobarSD();

        if (mExternalStorageAvailable) {
            if (mExternalStorageWriteable) {
                try {
                    // *** almacenamiento tarjeta SD (120 Mb)*****

                    // Devuelve la ruta completa del almacenamiento externo
                    File sdCard = Environment.getExternalStorageDirectory();
                    File directory = new File(sdCard.getAbsolutePath()
                            + "/MisFicheros");
                    // crea el directorio MisFicheros en la tarjeta SD
                    directory.mkdirs();
                    File file = new File(directory, name);
                    // fichero para escribir los datos.
                    FileOutputStream fOut = new FileOutputStream(file);
                    // Para convertir una secuencia de caracteres en un flujo de
                    // bytes
                    OutputStreamWriter osw = new OutputStreamWriter(fOut);
                    // escribir la cadena en el archivo
                    osw.write(str);
                    // asegurar que todos los bytes se escriben en el archivo
                    osw.flush();
                    // cierra el archivo
                    osw.close();

                    // mostrar mensaje de archivo guardado
                    tostada ("Fichero guardado satisfactoriamente");


                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                    tostada("Error el fichero no existe en la memoria externa");

                } catch (IOException ex) {
                    ex.printStackTrace();
                    tostada("Error de E/S al escribir en el fichero de la memoria externa");
                }

            } else
                tostada("Memoria SD no accesible para escribir");
        } else
            tostada("Memoria SD no presente");

    } // fin onClickGuardar()

    public void onClickGuardar(View v) {
    guardar();
    }

    public void guardarSD(View v){
        guardarSd();
    }

    private void cargar(){

        String name = Efichero.getText().toString();
        try {

            //Objeto para leer el fichero
            FileInputStream fIn = openFileInput(name);
            //asociamos un bufferReader al fichero
            BufferedReader br = new BufferedReader(new InputStreamReader(fIn));
            //cadena para almacenar cada línea leída
            String str = null;
            //cadena final
            String s = "";
            //mientras haya datos
            while ((str = br.readLine()) != null) {
                //añadimos la línea leída a s
                s += str + "\n";
            }

            br.close();
            fIn.close();

            // establecer EditText en el texto que se ha leído
            Etexto.setText(s);

            Toast.makeText(getBaseContext(),
                    "Fichero cargado satisfactoriamente", Toast.LENGTH_SHORT)
                    .show();
        } catch (IOException ioe) {

            ioe.printStackTrace();
        }
    }
    public void onClickCargar(View view) {

        cargar();
    }

    public void cargarSD(){
        String name = Efichero.getText().toString();
        try {
            // para cargar o leer el contenido del archivo externo
            // ***Almacenamiento SD ***

            // obtiene la ruta de la memoria externa
            File sdCard = Environment.getExternalStorageDirectory();

            // Acceder al directorio /MisFicheros en la ruta de la memoria externa
            File directory = new File(sdCard.getAbsolutePath() + "/MisFicheros");
            // Acceder al fichero textfile.txt en la ruta /MisFicheros
            File file = new File(directory, name);

            // Asociar un flujo de entrada el fichero file
            FileInputStream fIn = new FileInputStream(file);
            //asociamos un bufferReader al fichero fIn
            BufferedReader br = new BufferedReader(new InputStreamReader(fIn));

            //cadena para almacenar cada línea leída
            String str = null;
            //cadena final
            String s = "";
            //mientras haya datos
            while ((str = br.readLine()) != null) {
                //añadimos la línea leída a s
                s += str + "\n";
            }

            br.close();
            fIn.close();

            // establecer EditText con el texto que se ha leído
            Etexto.setText(s);

            tostada ("Fichero cargado ");

        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            tostada("Error el fichero no existe en la memoria externa");

        } catch (IOException ex) {
            ex.printStackTrace();
            tostada("Error de E/S al leer en el fichero de la memoria externa");
        }

    } // fin onClickCargar()

    public void clickCargaSD(View v){
        cargarSD();
    }

    public void clickPreferencias(View v) {

        Intent intentAbrir = new Intent(this, setPrefer.class);
        startActivityForResult(intentAbrir, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        loadPref();
    }

    /**
     * Metodo para cargar las preferencias. Comprobamos el valor del string en preferecias cogemos
     * el texto y lo escribimos en el EditText de la pantalla principal. Pongo el metodo cargar para
     * que me muestre lo que tiene el fichero escrito.
     */
    private void loadPref(){
        SharedPreferences mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String my_edittext_preference = mySharedPreferences.getString("Epreferencias","");
        Efichero.setText(my_edittext_preference);

        cargar();

    }

    private void comprobarSD() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // lectura y escritura
            mExternalStorageAvailable = mExternalStorageWriteable = true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            // solo lectura
            mExternalStorageAvailable = true;
            mExternalStorageWriteable = false;
        } else {
            // memoria no disponible
            mExternalStorageAvailable = mExternalStorageWriteable = false;
        }

    }

    public void tostada (String mensaje) {
        Toast.makeText(getBaseContext(), mensaje, Toast.LENGTH_SHORT).show();
    }
}
