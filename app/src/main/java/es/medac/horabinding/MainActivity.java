package es.medac.horabinding; // <- CAMBIA esto por tu package real si es distinto

import android.os.Build;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import es.medac.horabinding.R;

/**
 * Actividad principal que muestra:
 * - Un TimePicker embebido (inline) para elegir hora local.
 * - Un Spinner (combobox) poblado desde strings.xml con países/zonas UTC.
 * - Calcula la diferencia horaria entre la zona seleccionada y la zona local
 *   y muestra la hora convertida.
 *
 * Comentarios en el código describen de forma genérica qué hace cada bloque.
 */
public class MainActivity extends AppCompatActivity {

    private TimePicker timePicker;
    private Spinner spinnerZones;
    private TextView tvResult;

    private String[] zoneLabels;
    private String[] zoneOffsets; // offsets en horas, como "-5", "1", "0", etc.

    @RequiresApi(api = Build.VERSION_CODES.M) // getHour/getMinute requieren API 23+
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Se establece el layout de la actividad
        setContentView(R.layout.activity_main);

        // Se obtienen referencias UI
        timePicker = findViewById(R.id.time_picker);
        spinnerZones = findViewById(R.id.spinner_zones);
        tvResult = findViewById(R.id.tv_result);

        // Mostrar TimePicker en formato 24 horas
        timePicker.setIs24HourView(true);

        // Cargar arrays definidos en strings.xml
        zoneLabels = getResources().getStringArray(R.array.utc_labels);
        zoneOffsets = getResources().getStringArray(R.array.utc_offsets);

        // Crear un ArrayAdapter simple para el Spinner y asignarlo
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, zoneLabels);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerZones.setAdapter(adapter);

        // Listener para cambios en el TimePicker: recalcula resultado
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                updateResult(); // actualiza la conversión al cambiar la hora
            }
        });

        // Listener para selección en el Spinner: recalcula resultado
        spinnerZones.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, android.view.View view, int position, long id) {
                updateResult(); // actualiza la conversión al cambiar la zona seleccionada
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No se hace nada si no hay selección
            }
        });

        // Inicializar el resultado con la selección por defecto
        updateResult();
    }

    /**
     * Método que calcula la diferencia horaria (en horas) entre la zona seleccionada
     * (según utc_offsets en strings.xml) y la zona local del dispositivo, y luego
     * calcula la hora convertida a la zona seleccionada usando la hora elegida en TimePicker.
     *
     * Muestra la diferencia y la hora convertida en tvResult.
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void updateResult() {
        // Tomar hora y minuto desde el TimePicker (API 23+)
        int hour = timePicker.getHour();
        int minute = timePicker.getMinute();

        // Obtener offset local en horas (considerando DST)
        TimeZone tz = TimeZone.getDefault();
        long now = System.currentTimeMillis();
        int localOffsetHours = tz.getOffset(now) / (1000 * 60 * 60);

        // Obtener offset seleccionado desde zoneOffsets
        int pos = spinnerZones.getSelectedItemPosition();
        int selectedOffsetHours = 0;
        try {
            selectedOffsetHours = Integer.parseInt(zoneOffsets[pos].trim());
        } catch (Exception e) {
            // Si hay problema parsing, se asume 0
            selectedOffsetHours = 0;
        }

        // Calcular diferencia: selected - local (por ejemplo si local = +1 y selected = -5 -> -6)
        int diffHours = selectedOffsetHours - localOffsetHours;

        // Crear un Calendar con la hora local seleccionada
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);

        // Clonar calendario y añadir la diferencia para obtener hora en zona seleccionada
        Calendar converted = (Calendar) cal.clone();
        converted.add(Calendar.HOUR_OF_DAY, diffHours);

        // Formatear horas para mostrar
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String localTimeStr = sdf.format(cal.getTime());
        String convertedTimeStr = sdf.format(converted.getTime());

        // Preparar texto de salida
        String diffSign = (diffHours >= 0) ? "+" + diffHours : String.valueOf(diffHours);
        String zoneLabel = (pos >= 0 && pos < zoneLabels.length) ? zoneLabels[pos] : "Zona seleccionada";

        // Mostrar resultado: zona, offset, diferencia y hora convertida
        String result = "Zona: " + zoneLabel + "\n"
                + "Offset seleccionado: UTC" + (selectedOffsetHours >= 0 ? "+" + selectedOffsetHours : selectedOffsetHours) + "\n"
                + "Offset local: UTC" + (localOffsetHours >= 0 ? "+" + localOffsetHours : localOffsetHours) + "\n"
                + "Diferencia (horas): " + diffSign + "\n\n"
                + "Hora local seleccionada: " + localTimeStr + "\n"
                + "Hora en zona seleccionada: " + convertedTimeStr;

        tvResult.setText(result);
    }
}
