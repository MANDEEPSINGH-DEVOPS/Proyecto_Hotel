package model;

import lombok.Data;
import lombok.NoArgsConstructor;
import utils.Estado_H;
import utils.Tipo_Habitacion;

@Data
@NoArgsConstructor

public class Habitacions {
    private int id;
    private Tipo_Habitacion tipo_habitacion;
    private byte[] imagen;
    private float price;
    private Estado_H estado;
    private String imagenBase64;

    public Habitacions(Tipo_Habitacion tipo_habitacion, float price, Estado_H estado) {
        this.tipo_habitacion = tipo_habitacion;
        this.price = price;
        this.estado = estado;
    }
    public Habitacions(int id, Tipo_Habitacion tipo_habitacion, float price, Estado_H estado) {
        this.id = id;
        this.tipo_habitacion = tipo_habitacion;
        this.price = price;
        this.estado = Estado_H.disponible;
    }

    public Habitacions(int id, Tipo_Habitacion tipoHabitacion, byte[] imagenBytes, float precio, Estado_H estado) {
        this.id = id;
        this.tipo_habitacion = tipoHabitacion;
        this.imagen = imagenBytes;
        this.price = precio;
        this.estado = estado;
    }

    public Habitacions(int id, Estado_H estado) {
        this.id = id;
        this.estado = estado;
    }

    public byte[] getImagen() {return imagen;}

    public void setImagen(byte[] imagen) {this.imagen = imagen;}

    public Estado_H getEstado() {
        return estado;
    }

    public void setEstado(Estado_H estado) {
        this.estado = estado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public Tipo_Habitacion getTipo_habitacion() {
        return tipo_habitacion;
    }

    public void setTipo_habitacion(Tipo_Habitacion tipo_habitacion) {
        this.tipo_habitacion = tipo_habitacion;
    }

    public String getImagenBase64() {
        return imagenBase64;
    }

    public void setImagenBase64(String imagenBase64) {
        this.imagenBase64 = imagenBase64;
    }
}