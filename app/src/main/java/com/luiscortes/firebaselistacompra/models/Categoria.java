package com.luiscortes.firebaselistacompra.models;

import java.util.HashMap;
import java.util.Map;

public class Categoria {

    private String nombre;
    private String imagen;

    private static Map<String, String> mapaRecursos = new HashMap<>();

    static {
        mapaRecursos.put("Fruta", "fruit");
        mapaRecursos.put("Pescado", "sea");
        mapaRecursos.put("Verdura", "vegetable");
        mapaRecursos.put("Carne", "meat");
        mapaRecursos.put("Dulce", "crepe");
    }

    public Categoria() {
    }

    public Categoria(String nombre) {
        this.nombre = nombre;
        this.imagen = mapaRecursos.get(nombre);
    }

    public Categoria(String nombre, String imagen) {
        this.nombre = nombre;
        this.imagen = imagen;
    }


    public String getNombre() {
        return nombre;
    }

    public String getImagen() {
        return imagen;
    }
}
