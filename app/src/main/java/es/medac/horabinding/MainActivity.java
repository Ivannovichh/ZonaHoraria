package es.medac.horabinding;

import android.os.Build;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

// IMPORTANTE: esta clase la genera Data Binding a partir de res/layout/activity_main.xml
import es.medac.horabinding.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    // Binding del layout: da acceso a todas las vistas del XML
    private ActivityMainBinding binding;

    // Datos para poblar los spinners
    private String[] zoneLabels;
    private String[] zoneOffsets;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 1) Inflar el layout mediante Data Binding y establecer la vista raíz
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 2) Configurar vistas usando binding (sin findViewById)
        binding.timePicker.setIs24HourView(true);

        // 3) Cargar arrays desde resources
        zoneLabels  = getResources().getStringArray(R.array.utc_labels);
        zoneOffsets = getResources().getStringArray(R.array.utc_offsets);

        // 4) Adaptador para ambos spinners
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                zoneLabels
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerZonesFrom.setAdapter(adapter);
        binding.spinnerZonesTo.setAdapter(adapter);

        // 5) Listeners de cambios: hora y selección de zona
        binding.timePicker.setOnTimeChangedListener((view, hourOfDay, minute) -> updateResult());

        AdapterView.OnItemSelectedListener spinListener = new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, android.view.View view, int position, long id) {
                updateResult();
            }
            @Override public void onNothingSelected(AdapterView<?> parent) { }
        };
        binding.spinnerZonesFrom.setOnItemSelectedListener(spinListener);
        binding.spinnerZonesTo.setOnItemSelectedListener(spinListener);

        // 6) Cálculo inicial
        updateResult();
    }

    // Calcula y muestra la conversión de hora entre zonas seleccionadas
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void updateResult() {
        int hour   = binding.timePicker.getHour();
        int minute = binding.timePicker.getMinute();

        int posFrom = binding.spinnerZonesFrom.getSelectedItemPosition();
        int posTo   = binding.spinnerZonesTo.getSelectedItemPosition();

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

        binding.tvResult.setText(result);
    }

    // Convierte texto de offset en entero seguro
    private int parseOffset(String[] offsets, int pos) {
        try {
            if (pos >= 0 && pos < offsets.length) {
                return Integer.parseInt(offsets[pos].trim());
            }
        } catch (Exception ignored) { }
        return 0;
    }

    // Devuelve el número con signo explícito
    private String signed(int h) {
        return (h >= 0) ? ("+" + h) : String.valueOf(h);
    }
}
