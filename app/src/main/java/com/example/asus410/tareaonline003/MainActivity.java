package com.example.asus410.tareaonline003;

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

    public void onClickCargar(View view) {
        try {

            //Objeto para leer el fichero
            FileInputStream fIn = openFileInput("textfile.txt");
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

}
