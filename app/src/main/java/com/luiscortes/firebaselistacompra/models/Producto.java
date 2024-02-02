package com.luiscortes.firebaselistacompra.models;

public class Producto {
    private String nombre;
    private String imagen;
    private Categoria categoria;

    public Producto() {
    }

    public Producto(String nombre, String imagen, Categoria categoria) {
        this.nombre = nombre;
        this.imagen = imagen;
        this.categoria = categoria;
    }


    public String getNombre() {
        return nombre;
    }

    public String getImagen() {
        return imagen;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    @Override
    public String toString() {
        return "Producto{" +
                "nombre='" + nombre + '\'' +
                ", imagen='" + imagen + '\'' +
                ", categoria=" + categoria +
                '}';
    }
}
