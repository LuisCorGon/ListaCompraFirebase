package com.luiscortes.firebaselistacompra.models;

import com.luiscortes.firebaselistacompra.models.Producto;

import java.util.ArrayList;
import java.util.List;

public class Lista {

    private String nombre;

    private String fechaHora;
    private List<Producto> productos;
    private ArrayList<String> correosUsuarios;

    public Lista() {
    }

    public Lista(String nombre, String fechaHora, List<Producto> productos, ArrayList<String> correosUsuarios) {
        this.nombre = nombre;
        this.fechaHora = fechaHora;
        this.productos = productos;
        this.correosUsuarios = correosUsuarios;
    }

    public String getNombre() {
        return nombre;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public String getFechaHora() {
        return fechaHora;
    }

    public ArrayList<String> getCorreosUsuarios() {
        return correosUsuarios;
    }

    @Override
    public String toString() {
        return "Lista{" +
                "nombre='" + nombre + '\'' +
                ", fechaHora='" + fechaHora + '\'' +
                ", productos=" + productos +
                ", correosUsuarios=" + correosUsuarios +
                '}';
    }
}
