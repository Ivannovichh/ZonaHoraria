package com.example.worldtimeutc;

import android.os.Build;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
{

    private TimePicker timePicker;     // Referencia al selector de hora
    private Spinner spinnerOrigen;     //Spinner de país/zona origen
    private Spinner spinnerDestino;    //Spinner de país/zona destino
    private TextView txtResultado;     //TextView para mostrar el resultado
    private Button btnConvertir;       //Boton para convertir

    //Lista de opciones (país + zona + etiqueta UTC)
    private List<ZonaCiudad> items;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Obtiene el ImageView del GIF
        ImageView gif = findViewById(R.id.Gif);

        //Inicia Glide con el contexto
        Glide.with(this)

                //Indica que se va a cargar un GIF
                .asGif()

                //Recurso GIF en res/drawable
                .load(R.drawable.bola_del_mundo)

                //Lo introduce en el ImageView
                .into(gif);

        //Obtiene el TimePicker por su id
        timePicker = findViewById(R.id.timePicker);

        //Asignamos el formato 24h
        timePicker.setIs24HourView(true);

        //Buscamos los demás objetos por su id
        spinnerOrigen = findViewById(R.id.spinnerOrigen);
        spinnerDestino   = findViewById(R.id.spinnerDestino);
        txtResultado   = findViewById(R.id.txtResultado);
        btnConvertir  = findViewById(R.id.btnConvertir);

        //Crea la lista para los paises
        items = buildCountryList();

        ArrayAdapter<ZonaCiudad> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                items
        );

        //Layout para el spinner de los paises
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOrigen.setAdapter(adapter);

        //Asigna el pais origen seleccinado
        spinnerDestino.setAdapter(adapter);

        //Asigna el pais destino seleccinado

        //Evento para hacer la conversión al pulsar el botón
        btnConvertir.setOnClickListener(v -> convert());
    }

    //Construimos la lista de países
    private List<ZonaCiudad> buildCountryList()
    {
        String[][] data = new String[][]{
                {"España", "Europe/Madrid"},
                {"Portugal", "Europe/Lisbon"},
                {"Francia", "Europe/Paris"},
                {"Reino Unido", "Europe/London"},
                {"Estados Unidos (Este)", "America/New_York"},
                {"México", "America/Mexico_City"},
                {"Argentina", "America/Argentina/Buenos_Aires"},
                {"Japón", "Asia/Tokyo"},
                {"Australia (Sydney)", "Australia/Sydney"},
                {"Sudáfrica", "Africa/Johannesburg"}
        };

        //Crea lista vacía
        List<ZonaCiudad> list = new ArrayList<>();

        //Recorre cada país
        for (String[] d : data)
        {

            //Construye etiqueta con UTC actual
            String label = EtiquetasUTC.label(d[0], d[1]);

            //Crea objeto y lo añade
            list.add(new ZonaCiudad(label, d[1]));
        }
        return list;
    }

    //Realizamos la conversión de hora
    private void convert()
    {

        //Declaramos variables para la hora
        int Hora, Minutos;

        //Sentencia para comprobar la versión de API
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {

            //Leemos la hora y los minutos del TimePicker
            Hora = timePicker.getHour();
            Minutos = timePicker.getMinute();
        }
        //Creamos compatibilidad con APIs antiguas
        else
        {
            Hora = timePicker.getCurrentHour();
            Minutos = timePicker.getCurrentMinute();
        }

        //Tomamos la selección de los spinners
        ZonaCiudad Origen = (ZonaCiudad) spinnerOrigen.getSelectedItem();
        ZonaCiudad Destino   = (ZonaCiudad) spinnerDestino.getSelectedItem();

        //Usa la fecha de hoy
        LocalDate Fecha = LocalDate.now();

        //Crea la hora elegida
        LocalTime Tiempo = LocalTime.of(Hora, Minutos);

        //Combina fecha + hora (sin zona)
        LocalDateTime ldt = LocalDateTime.of(Fecha, Tiempo);

        //Asocia la zona de origen
        ZonedDateTime zOrigen = ldt.atZone(ZoneId.of(Origen.ID));

        //Convierte al instante en destino
        ZonedDateTime zDestino   = zOrigen.withZoneSameInstant(ZoneId.of(Destino.ID));

        //Diferencia de offset en segundos
        int difSegundos =
                zDestino.getOffset().getTotalSeconds()
                        - zOrigen.getOffset().getTotalSeconds();

        //Horas de diferencia
        int difHoras = difSegundos / 3600;

        //Minutos de = diffSeconds / 3600;

        //Minutos (valor absoluto)
        int difMinutos = Math.abs((difSegundos % 3600) / 60);

        //Signo +/-
        String sign = difSegundos >= 0 ? "+" : "-";

        //Formatea como ±H:MM
        String diffStr = String.format(Locale.US,
                "%s%d:%02d", sign, Math.abs(difHoras), difMinutos);

        //Formato de salida (solo hora + zona)
        DateTimeFormatter fmt =
                DateTimeFormatter.ofPattern("HH:mm (z)", Locale.getDefault());

        //Construye el texto con el resultado
        String result =
                getString(R.string.horaLocal) + " " + Origen.ID + ": " + zOrigen.format(fmt)
                        + "\n" + getString(R.string.horaDestino) + " " + Destino.ID + ": " + zDestino.format(fmt)
                        + "\n" + getString(R.string.diferenciaHoraria) + ": " + diffStr;

        //Muestra el resultado
        txtResultado.setText(result);
    }
}