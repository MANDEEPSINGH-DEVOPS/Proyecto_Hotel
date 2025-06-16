package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;


public class Activitats {
    private int id;
    private String nom;
    private String descripcion;
    private float precio;
    private byte[] imagen;
    private int cupo;
    private LocalDate fecha_actividad;
    //NECESITO ESTE ATRIBUTO PARA CONVERTIRA IMAGEN DE BYTE A STRING;
    private String imagenBase64;

    public static int id_next = 1;

    public Activitats(int id, String nom, String descripcion, float precio, int cupo, LocalDate fecha_actividad) {
        this.cupo = cupo;
        this.descripcion = descripcion;
        this.fecha_actividad = fecha_actividad;
        this.id = id_next;
        id_next++;
        this.nom = nom;
        this.precio = precio;
    }
    public Activitats(String nom, String descripcion, float precio, int cupo, LocalDate fecha_actividad,int id) {
        this.cupo = cupo;
        this.descripcion = descripcion;
        this.fecha_actividad = fecha_actividad;
        this.id = id_next;
        id_next++;
        this.nom = nom;
        this.precio = precio;
    }
    public Activitats(String nom, String descripcion,  float precio,int cupo,LocalDate fecha_actividad) {
        this.cupo = cupo;
        this.descripcion = descripcion;
        this.fecha_actividad = fecha_actividad;
        this.nom = nom;
        this.precio = precio;
    }
    public Activitats(int id,String nom, String descripcion,byte[] imagen,float precio,int cupo,LocalDate fecha_actividad) {
        this.id=id;
        this.cupo = cupo;
        this.descripcion = descripcion;
        this.fecha_actividad = fecha_actividad;
        this.imagen= imagen;
        this.nom = nom;
        this.precio = precio;
    }

    public int getCupo() {
        return cupo;
    }

    public void setCupo(int cupo) {
        this.cupo = cupo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDate getFecha_actividad() {
        return fecha_actividad;
    }

    public void setFecha_actividad(LocalDate fecha_actividad) {
        this.fecha_actividad = fecha_actividad;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static int getId_next() {
        return id_next;
    }

    public static void setId_next(int id_next) {
        Activitats.id_next = id_next;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }
    public String getImagenBase64() {
        return imagenBase64;
    }

    public void setImagenBase64(String imagenBase64) {
        this.imagenBase64 = imagenBase64;
    }

    @Override
    public String toString() {
        return "Activitats{" +
                "cupo=" + cupo +
                ", id=" + id +
                ", nom='" + nom + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", precio=" + precio +
                ", fecha_actividad=" + fecha_actividad +
                '}';
    }
}
