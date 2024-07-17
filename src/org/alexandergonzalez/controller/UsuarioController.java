
package org.alexandergonzalez.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.alexandergonzalez.bean.Usuario;
import org.alexandergonzalez.db.Conexion;
import org.alexandergonzalez.main.Principal;


public class UsuarioController implements Initializable {

    private Principal escenarioPrincipal;
    private enum operaciones{GUARDAR,ELIMINAR, NINGUNO}
    private operaciones tipoOperacion = operaciones.NINGUNO;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }

    
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    @FXML private TextField txtCodigo;
    @FXML private TextField txtApellidos;
    @FXML private TextField txtNombres;
    @FXML private TextField txtUsuario;
    @FXML private TextField txtContrasena;
    @FXML private Button btnAgregar;
    @FXML private Button btnEliminar;
    @FXML private ImageView imgAgregar;
    @FXML private ImageView imgEliminar;
    
    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void nuevo(){
        switch(tipoOperacion){
            case NINGUNO:
                activarControles();
                limpiarControles();
                btnAgregar.setText("Guardar");
                btnEliminar.setText("Cancelar");
                imgAgregar.setImage(new Image("org/alexandergonzalez/image/iconoGuardar.png"));
                imgEliminar.setImage(new Image("org/alexandergonzalez/image/iconoCancelar.png"));
                tipoOperacion = operaciones.GUARDAR;
            break;
            case GUARDAR:
                guardar();
                ventanaLogin();
                limpiarControles();
                desactivarControles();
                btnAgregar.setText("Nuevo");
                btnEliminar.setText("Cancelar");
                imgAgregar.setImage(new Image("org/alexandergonzalez/image/iconoAgregar.png"));
                imgEliminar.setImage(new Image("org/alexandergonzalez/image/iconoEliminar.png"));
                tipoOperacion = operaciones.NINGUNO;
        }
    }
    
    public void guardar(){
        Usuario registro = new Usuario();
        registro.setNombreUsuario(txtNombres.getText());
        registro.setApellidoUsuario(txtApellidos.getText());
        registro.setUsuarioLogin(txtUsuario.getText());
        registro.setContrasena(txtContrasena.getText());
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarUsuario(?,?,?,?)");
            procedimiento.setString(1, registro.getNombreUsuario());
            procedimiento.setString(2, registro.getApellidoUsuario());
            procedimiento.setString(3, registro.getUsuarioLogin());
            procedimiento.setString(4, registro.getContrasena());
            procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaLogin(){
        escenarioPrincipal.ventanaLogin();
    }
    
    
    public void activarControles(){
        txtCodigo.setEditable(true);
        txtApellidos.setEditable(true);
        txtNombres.setEditable(true);
        txtUsuario.setEditable(true);
        txtContrasena.setEditable(true);
    }
    
    public void desactivarControles(){
        txtCodigo.setEditable(false);
        txtApellidos.setEditable(false);
        txtNombres.setEditable(false);
        txtUsuario.setEditable(false);
        txtContrasena.setEditable(false);
    }
    
    public void limpiarControles(){
        txtCodigo.clear();
        txtApellidos.clear();
        txtNombres.clear();
        txtUsuario.clear();
        txtContrasena.clear();
    }
    
    
    
}
