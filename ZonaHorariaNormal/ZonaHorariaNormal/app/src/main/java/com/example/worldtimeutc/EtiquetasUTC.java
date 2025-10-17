package com.example.worldtimeutc;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Locale;

//Clase helper para construir etiquetas con UTC
public class EtiquetasUTC
{

    //Construye "País (Zona, UTC±hh:mm)"
    public static String label(String NombreCiudad, String ZonaCiudad)
    {

        //Toma el offset actual de esa zona
        ZonedDateTime Ahora = ZonedDateTime.now(ZoneId.of(ZonaCiudad));

        //Offset en segundos respecto a UTC;
        int totalSeconds = Ahora.getOffset().getTotalSeconds();

        //Horas de diferencia
        int hours = totalSeconds / 3600;

        //Minutos de diferencia (absoluto)
        int minutes = Math.abs((totalSeconds % 3600) / 60);

        //Determina el signo
        String sign = totalSeconds >= 0 ? "+" : "-";

        //Formatea horas con 2 dígitos
        String h = String.format(Locale.US, "%02d", Math.abs(hours));

        //Formatea minutos con 2 dígitos
        String m = String.format(Locale.US, "%02d", minutes);

        //Devuelve etiqueta final
        return NombreCiudad + " (" + ZonaCiudad + ", UTC" + sign + h + ":" + m + ")";
    }
}
