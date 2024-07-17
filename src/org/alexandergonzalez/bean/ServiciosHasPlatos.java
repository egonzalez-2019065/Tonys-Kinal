
package org.alexandergonzalez.bean;

public class ServiciosHasPlatos {
    private int serviciosCodigoServicio;
    private int codigoPlato;
    private int codigoServicio;

    public ServiciosHasPlatos() {
    }

    public ServiciosHasPlatos(int serviciosCodigoServicio, int codigoPlato, int codigoServicio) {
        this.serviciosCodigoServicio = serviciosCodigoServicio;
        this.codigoPlato = codigoPlato;
        this.codigoServicio = codigoServicio;
    }

    public int getServiciosCodigoServicio() {
        return serviciosCodigoServicio;
    }

    public void setServiciosCodigoServicio(int serviciosCodigoServicio) {
        this.serviciosCodigoServicio = serviciosCodigoServicio;
    }

    public int getCodigoPlato() {
        return codigoPlato;
    }

    public void setCodigoPlato(int codigoPlato) {
        this.codigoPlato = codigoPlato;
    }

    public int getCodigoServicio() {
        return codigoServicio;
    }

    public void setCodigoServicio(int codigoServicio) {
        this.codigoServicio = codigoServicio;
    }

    
}
