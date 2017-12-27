package com.example.asus410.tareaonline003;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {

    EditText Efichero;
    EditText Etexto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Efichero = (EditText)findViewById(R.id.Enombre);
        Etexto = (EditText)findViewById(R.id.Etextos);
        loadPref();
    }

    public void onClickGuardar(View view) {
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

    public void clickPreferencias(View v) {

        Intent intentAbrir = new Intent(this, setPrefer.class);
        startActivityForResult(intentAbrir, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        loadPref();
    }

    private void loadPref(){
        SharedPreferences mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);


        String my_edittext_preference = mySharedPreferences.getString("Epreferencias","");
        Efichero.setText(my_edittext_preference);

        cargar();

    }
}
