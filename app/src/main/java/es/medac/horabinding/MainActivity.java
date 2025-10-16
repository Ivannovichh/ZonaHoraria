package es.medac.horabinding;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import es.medac.horabinding.databinding.ActivityMainBinding;

import pl.droidsonroids.gif.AnimationListener;
import pl.droidsonroids.gif.GifDrawable;

public class MainActivity extends AppCompatActivity {

    // 2. La declaración del objeto Binding como variable de la clase es correcta.
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // Conectamos el botón del XML
        Button btnHora = findViewById(R.id.btnHora);

        //Le ponemos un listener para abrir el TimePicker
        btnHora.setOnClickListener(v -> {

            // ESTE ES EL CÓDIGO DEL TIMEPICKER
            MaterialTimePicker timePicker = new MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_24H) // formato 24 horas
                    .setHour(12) // hora inicial
                    .setMinute(0) // minuto inicial
                    .setTitleText("Selecciona la hora")
                    .build();

            // Mostrar el picker
            timePicker.show(getSupportFragmentManager(), "TIME_PICKER");

            // Escuchar cuando el usuario confirma la hora
            timePicker.addOnPositiveButtonClickListener(view -> {
                int hour = timePicker.getHour();
                int minute = timePicker.getMinute();
                Log.d("TIME", "Hora seleccionada: " + hour + ":" + minute);
            });
        });
        // Dentro del método onCreate de MainActivity.java

// --- CÓDIGO PARA RELLENAR EL DESPLEGABLE CON GRUPOS ---

// 1. Crear una lista para almacenar todos los países de todas las zonas
        List<String> todosLosPaises = new ArrayList<>();

// 2. Obtener la referencia al array "índice" de zonas horarias
        TypedArray listaZonas = getResources().obtainTypedArray(R.array.lista_zonas_horarias);

// 3. Recorrer el array índice para leer cada grupo de países
        for (int i = 0; i < listaZonas.length(); i++) {
            // Obtenemos el ID del recurso de cada array de países (ej: R.array.paises_utc_menos_3)
            int resourceId = listaZonas.getResourceId(i, 0);

            if (resourceId > 0) {
                // Obtenemos el array de strings de países para esa zona
                String[] paisesDeLaZona = getResources().getStringArray(resourceId);
                // Añadimos todos los países de esa zona a nuestra lista principal
                Collections.addAll(todosLosPaises, paisesDeLaZona);
            }
        }
        // Liberar la memoria usada por TypedArray
            listaZonas.recycle();

        // 4. Crear el adaptador con la lista combinada de todos los países
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                todosLosPaises // Usamos la lista que hemos construido
            );

        // 5. Asignar el adaptador al componente
            binding.autoCompletePaises.setAdapter(adapter);

        // --- FIN DEL CÓDIGO ---

        // Listener para saber qué país se ha seleccionado
            binding.autoCompletePaises.setOnItemClickListener((parent, view, position, id) -> {
                String paisSeleccionado = (String) parent.getItemAtPosition(position);
                Toast.makeText(this, "País: " + paisSeleccionado, Toast.LENGTH_SHORT).show();
            });
    }

}
