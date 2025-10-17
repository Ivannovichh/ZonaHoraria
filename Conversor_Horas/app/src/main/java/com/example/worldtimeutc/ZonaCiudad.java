package com.example.worldtimeutc;

//Clase simple para representar una opción del spinner
public class ZonaCiudad
{
    public final String Nombre;   //Texto visible en el spinner (con país, zona y UTC)
    public final String ID;        //Identificador IANA de la zona (Europe/Madrid)

    public ZonaCiudad(String displayName, String zoneId)
    {

        //Asignamos una etiqueta visible
        this.Nombre = displayName;

        //Asignamos un identificador de la zona IANA
        this.ID = zoneId;
    }

    @Override
    public String toString()
    {
        return Nombre;
    }
}