package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import utils.Estado;
import utils.Rol;

import java.util.Date;

@Data
@NoArgsConstructor

public class User {
    private int id;
    private String username;
    private String email;
    private String password;
    private Rol rol;
    private Estado estado;
    private Date fecha_registro;

    public static int id_next=1;

    public User(int id, String username, String email, String password, Rol rol, Date fecha_registro, Estado estado) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.rol = rol;
        this.estado = estado;
        this.fecha_registro = new Date();
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.rol = Rol.cliente;
    }


    public Estado getEstado() {return estado;}

    public void setEstado(Estado estado) {this.estado = estado;}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public Date getFecha_registro() {
        return fecha_registro;
    }

    public void setFecha_registro(Date fecha_registro) {
        this.fecha_registro = fecha_registro;
    }

    public static int getId_next() {
        return id_next;
    }

    public static void setId_next(int id_next) {
        User.id_next = id_next;
    }

}
