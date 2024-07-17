package org.alexandergonzalez.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import org.alexandergonzalez.main.Principal;

public class MenuPrincipalController implements Initializable{
    private Principal escenarioPrincipal; 
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    public void ventanaProgramador(){
        escenarioPrincipal.ventanaProgramador();
    }
    public void ventanaEmpresas(){
        escenarioPrincipal.ventanaEmpresas();
    }
    public void ventanaProductos(){
        escenarioPrincipal.ventanaProductos();
    }
    public void ventanaTipoEmpleado(){
        escenarioPrincipal.ventanaTipoEmpleado();
    }
    public void ventanaTipoPlato(){
        escenarioPrincipal.ventanaTipoPlato();
    }
        public void ventanaServiciosEmpleados(){
        escenarioPrincipal.ventanaServiciosEmpleado();
    }
    public void ventanaServiciosPlatos(){
        escenarioPrincipal.ventanaServiciosPlatos();
    }
    public void ventanaProductosPlatos(){
        escenarioPrincipal.ventanaProductosPlatos();   
    }
}
