package es.medac.horabinding;

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

/*
 * Actividad principal del Conversor de Zona Horaria.
 * Permite seleccionar hora, país de origen y destino,
 * y convierte la hora automáticamente según el desfase UTC.
 * El encabezado muestra un GIF animado usando GifImageView.
 */
public class MainActivity extends AppCompatActivity {

    private TimePicker timePicker;
    private Spinner spinnerFrom;
    private Spinner spinnerTo;
    private TextView tvResult;

    private String[] zoneLabels;
    private String[] zoneOffsets; // Offsets horarios (por ejemplo: "-5", "1", "0")

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Referencias visuales
        timePicker = findViewById(R.id.time_picker);
        spinnerFrom = findViewById(R.id.spinner_zones_from);
        spinnerTo   = findViewById(R.id.spinner_zones_to);
        tvResult    = findViewById(R.id.tv_result);

        // Configura el TimePicker en formato 24h
        timePicker.setIs24HourView(true);

        // Carga las zonas horarias desde strings.xml
        zoneLabels  = getResources().getStringArray(R.array.utc_labels);
        zoneOffsets = getResources().getStringArray(R.array.utc_offsets);

        // Crea el adaptador para ambos spinners
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                zoneLabels
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFrom.setAdapter(adapter);
        spinnerTo.setAdapter(adapter);

        // Listener para el cambio de hora
        timePicker.setOnTimeChangedListener((view, hourOfDay, minute) -> updateResult());

        // Listener para el cambio de selección de zona
        AdapterView.OnItemSelectedListener spinListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, android.view.View view, int position, long id) {
                updateResult();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        };

        spinnerFrom.setOnItemSelectedListener(spinListener);
        spinnerTo.setOnItemSelectedListener(spinListener);

        // Calcula y muestra la conversión inicial
        updateResult();
    }

    // Calcula la conversión de hora entre zonas horarias
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void updateResult() {
        int hour = timePicker.getHour();
        int minute = timePicker.getMinute();

        int posFrom = spinnerFrom.getSelectedItemPosition();
        int posTo   = spinnerTo.getSelectedItemPosition();

        int fromOffsetHours = parseOffset(zoneOffsets, posFrom);
        int toOffsetHours   = parseOffset(zoneOffsets, posTo);

        int diffHours = toOffsetHours - fromOffsetHours;

        Calendar calFrom = Calendar.getInstance();
        calFrom.set(Calendar.SECOND, 0);
        calFrom.set(Calendar.MILLISECOND, 0);
        calFrom.set(Calendar.HOUR_OF_DAY, hour);
        calFrom.set(Calendar.MINUTE, minute);

        Calendar calTo = (Calendar) calFrom.clone();
        calTo.add(Calendar.HOUR_OF_DAY, diffHours);

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String fromTimeStr = sdf.format(calFrom.getTime());
        String toTimeStr   = sdf.format(calTo.getTime());

        String fromLabel = (posFrom >= 0 && posFrom < zoneLabels.length) ? zoneLabels[posFrom] : "Origen";
        String toLabel   = (posTo   >= 0 && posTo   < zoneLabels.length) ? zoneLabels[posTo]   : "Destino";

        String result = "Origen: " + fromLabel + " (UTC" + signed(fromOffsetHours) + ")\n"
                + "Destino: " + toLabel + " (UTC" + signed(toOffsetHours) + ")\n"
                + "Diferencia (horas): " + signed(diffHours) + "\n\n"
                + "Hora en origen: " + fromTimeStr + "\n"
                + "Hora en destino: " + toTimeStr;

        tvResult.setText(result);
    }

    // Convierte texto de offset en número entero
    private int parseOffset(String[] offsets, int pos) {
        try {
            if (pos >= 0 && pos < offsets.length) {
                return Integer.parseInt(offsets[pos].trim());
            }
        } catch (Exception ignored) { }
        return 0;
    }

    // Devuelve un número con signo (+ o -)
    private String signed(int h) {
        return (h >= 0) ? ("+" + h) : String.valueOf(h);
    }
}
