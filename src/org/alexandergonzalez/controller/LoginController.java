
package org.alexandergonzalez.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javax.swing.JOptionPane;
import org.alexandergonzalez.bean.Login;
import org.alexandergonzalez.bean.Usuario;
import org.alexandergonzalez.db.Conexion;
import org.alexandergonzalez.main.Principal;

    public class LoginController implements Initializable {
        private Principal escenarioPrincipal;
        private ObservableList<Usuario> listaUsuario;
        
        @FXML private TextField txtUsuario;
        @FXML private TextField txtContrasena;
        
     
     
        @Override
        public void initialize(URL location, ResourceBundle resources) {

        }
        
        public ObservableList<Usuario> getUsuario(){
            ArrayList<Usuario> lista = new ArrayList<Usuario>();
            try{
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarUsuarios()");
                ResultSet resultado = procedimiento.executeQuery();
                while(resultado.next()){
                    lista.add( new Usuario(resultado.getInt("codigoUsuario"),
                        resultado.getString("nombreUsuario"),
                        resultado.getString("apellidoUsuario"),
                        resultado.getString("usuarioLogin"),
                        resultado.getString("contrasena")));
            }
            }catch(Exception e){
                e.printStackTrace();
            }
            return listaUsuario = FXCollections.observableArrayList(lista);
        }
        
        @FXML
        private void iniciarSesion(){
            Login login = new Login();
            int x = 0;
            boolean bandera = false;
            login.setUsuarioMaster(txtUsuario.getText());
            login.setPasswordLogin(txtContrasena.getText());
            while(x < getUsuario().size()){
                String user = getUsuario().get(x).getUsuarioLogin();
                String pass = getUsuario().get(x).getContrasena();
                if(user.equals(login.getUsuarioMaster()) && pass.equals(login.getPasswordLogin())){
                    JOptionPane.showMessageDialog(null, "Sesión Iniciada\n"+ getUsuario().get(x).getNombreUsuario()+ " " +
                            getUsuario().get(x).getApellidoUsuario()+ "\n"+ "Bienvenido");
                    escenarioPrincipal.menuPrincipal();
                    x = getUsuario().size();
                    bandera = true;
                }
                x++;
            }
            if(bandera == false){
                JOptionPane.showMessageDialog(null, "Usuario o contraseña incorrectos");
                txtUsuario.clear();
                txtContrasena.clear();
            }
        }
        
        public Principal getEscenarioPrincipal() {
            return escenarioPrincipal;
        }

        public void setEscenarioPrincipal(Principal escenarioPrincipal) {
            this.escenarioPrincipal = escenarioPrincipal;
        }

        public void ventanaUsuario(){
            escenarioPrincipal.ventanaUsuarios();
        }
    
    
    
}
