
package org.alexandergonzalez.bean;

import java.util.Date;

public class Presupuesto {
    public int codigoPresupuesto; 
    public Date fechaSolicitud;
    public double cantidad;
    private int codigoEmpresa;
    
    public Presupuesto(){
        
    }

    public Presupuesto(int codigoPresupuesto, Date fechaSolicitud, double cantidad, int codigoEmpresa) {
        this.codigoPresupuesto = codigoPresupuesto;
        this.fechaSolicitud = fechaSolicitud;
        this.cantidad = cantidad;
        this.codigoEmpresa = codigoEmpresa;
    }

    public int getCodigoPresupuesto() {
        return codigoPresupuesto;
    }

    public void setCodigoPresupuesto(int codigoPresupuesto) {
        this.codigoPresupuesto = codigoPresupuesto;
    }

    public Date getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(Date fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public int getCodigoEmpresa() {
        return codigoEmpresa;
    }

    public void setCodigoEmpresa(int codigoEmpresa) {
        this.codigoEmpresa = codigoEmpresa;
    }
    
}

