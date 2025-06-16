package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import utils.Estado_H;
import utils.Estado_Reserva;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Reserva_Habitacion {

    private int id;
    private int id_usuario;
    private int id_habitacion;
    private LocalDate fecha_entrada;
    private LocalDate fecha_salida;
    private Estado_Reserva estado;
    private Date fecha_reserva;

    public Reserva_Habitacion(int id_usuario, int id_habitacion, LocalDate fecha_entrada, LocalDate fecha_salida, Estado_Reserva estado) {
        this.id_usuario = id_usuario;
        this.id_habitacion = id_habitacion;
        this.fecha_entrada = fecha_entrada;
        this.fecha_salida = fecha_salida;
        this.estado = estado;
    }

    public Reserva_Habitacion(int id, int id_usuario, int id_habitacion, LocalDate fecha_entrada, LocalDate fecha_salida, Estado_Reserva estado) {
        this.id = id;
        this.id_usuario = id_usuario;
        this.id_habitacion = id_habitacion;
        this.fecha_entrada = fecha_entrada;
        this.fecha_salida = fecha_salida;
        this.estado = estado;
    }

    public Reserva_Habitacion(int id, Estado_Reserva estadoR2) {
        this.id = id;

        this.estado = estadoR2;
    }
}
