package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import utils.Estado_H;
import utils.Estado_Reserva;

import java.util.ArrayList;
import java.util.Date;

@Data
@NoArgsConstructor

public class Reserva_Actividad {
    private int id;
    private int id_user;
    private int id_actividad;
    private Estado_Reserva estadoReserva;
    private Date fecha;

    public static int id_next= 1;

    public Reserva_Actividad(int id, int id_user, int id_actividad, Estado_Reserva estadoReserva) {
        this.id = id;
        this.id_user = id_user;
        this.id_actividad = id_actividad;
        this.estadoReserva = estadoReserva;
    }

    public Reserva_Actividad(int id_user, int id_actividad, Estado_Reserva estadoReserva) {
        this.id_user = id_user;
        this.id_actividad = id_actividad;
        this.estadoReserva = estadoReserva;
    }

    public Reserva_Actividad(int id, Estado_Reserva estadoReserva) {
        this.id = id;
        this.estadoReserva = estadoReserva;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public int getId_actividad() {
        return id_actividad;
    }

    public void setId_actividad(int id_actividad) {
        this.id_actividad = id_actividad;
    }

    public Estado_Reserva getEstadoReserva() {
        return estadoReserva;
    }

    public void setEstadoReserva(Estado_Reserva estadoReserva) {
        this.estadoReserva = estadoReserva;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public static int getId_next() {
        return id_next;
    }

    public static void setId_next(int id_next) {
        Reserva_Actividad.id_next = id_next;
    }
}