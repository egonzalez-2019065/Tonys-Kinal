/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alexandergonzalez.bean;

/**
 *
 * @author Alex
 */
public class TipoEmpleado {
    private int codigoTipoEmpleado;
    private String descripcion;

    public TipoEmpleado(){
        
    }
    public TipoEmpleado(int codigoTipoEmpleado, String descripcion) {
        this.codigoTipoEmpleado = codigoTipoEmpleado;
        this.descripcion = descripcion;
    }

    public int getCodigoTipoEmpleado() {
        return codigoTipoEmpleado;
    }

    public void setCodigoTipoEmpleado(int codigoTipoEmpleado) {
        this.codigoTipoEmpleado = codigoTipoEmpleado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return codigoTipoEmpleado +", "+descripcion;
    }
    
    
    
}
