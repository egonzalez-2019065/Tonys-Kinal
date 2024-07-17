/*
    Programador: Edwin Alexander González García 
    Carné: 2019065
    Código Técnico: IN5AM 
    Fecha Creación: 28/03/2023
    Fechas de Modificación: 11/04/2023 - 17/04/2023 - 18/04/2023 - 19/04/2023 - 20/04/2023
    23/04/2023 - 25/04/2023 - 26/04/2023 - 2/05/2023 - 9/5/2023 - 10/05/2023 - 22/05/2023 - 23/05/2023 - 24/05/2023
    27/05/2023 - 28/05/2023 - 06/06/2023
*/
package org.alexandergonzalez.main;
import java.io.IOException;
import java.io.InputStream;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.alexandergonzalez.controller.EmpleadoController;
import org.alexandergonzalez.controller.EmpresasController;
import org.alexandergonzalez.controller.MenuPrincipalController;
import org.alexandergonzalez.controller.PlatoController;
import org.alexandergonzalez.controller.PresupuestoController;
import org.alexandergonzalez.controller.ProductosController;
import org.alexandergonzalez.controller.ProductosHasPlatosController;
import org.alexandergonzalez.controller.ProgramadorController;
import org.alexandergonzalez.controller.ServiciosController;
import org.alexandergonzalez.controller.ServiciosHasEmpleadosController;
import org.alexandergonzalez.controller.ServiciosHasPlatosController;
import org.alexandergonzalez.controller.TipoEmpleadoController;
import org.alexandergonzalez.controller.TipoPlatoController;
import org.alexandergonzalez.controller.UsuarioController;
import org.alexandergonzalez.controller.LoginController;

public class Principal extends Application {
    private final String PAQUETE_VISTA = "/org/alexandergonzalez/view/";
    private Stage escenarioPrincipal; 
    private Scene escena; 
    
    @Override
    public void start(Stage escenarioPrincipal) throws IOException {
        this.escenarioPrincipal = escenarioPrincipal;
        this.escenarioPrincipal.setTitle("Tony's Kinal 2019065");
        escenarioPrincipal.getIcons().add(new Image("/org/alexandergonzalez/image/Logo.png"));
       // Parent root = FXMLLoader.load(getClass().getResource("/org/alexandergonzalez/view/MenuPrincipalView.fxml"));
       // Scene escena = new Scene(root);
       // escenarioPrincipal.setScene(escena);
       ventanaLogin();
       escenarioPrincipal.show();
        
    }
    
    public void menuPrincipal(){
        try{
            MenuPrincipalController menu = (MenuPrincipalController)cambiarEscena("MenuPrincipalView.fxml",700,529);
            menu.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void ventanaProgramador(){
        try{
            ProgramadorController programador = (ProgramadorController)cambiarEscena("ProgramadorView.fxml",700,529);
            programador.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void ventanaEmpresas(){
        try{
            EmpresasController empresa = (EmpresasController)cambiarEscena("EmpresasView.fxml",746,529);
            empresa.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void ventanaProductos(){
        try{
            ProductosController producto = (ProductosController)cambiarEscena("ProductosView.fxml",752,529);
            producto.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void ventanaTipoEmpleado(){
        try{
            TipoEmpleadoController tipoEmpleado = (TipoEmpleadoController)cambiarEscena("TipoEmpleadoView.fxml",717,529);
            tipoEmpleado.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void ventanaTipoPlato(){
        try{
            TipoPlatoController tipoPlato = (TipoPlatoController)cambiarEscena("TipoPlatoView.fxml",717,529);
            tipoPlato.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void ventanaPresupuesto(){
        try{
            PresupuestoController presupuesto = (PresupuestoController)cambiarEscena("PresupuestosView.fxml",759,529);
            presupuesto.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void ventanaEmpleado(){
        try{
            EmpleadoController empleado  = (EmpleadoController)cambiarEscena("EmpleadoView.fxml",1032,529);
            empleado.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void ventanaServicios(){
        try{
           ServiciosController servicios = (ServiciosController)cambiarEscena("ServiciosView.fxml",1041,529);
           servicios.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void ventanaPlato(){
        try{
            PlatoController plato = (PlatoController)cambiarEscena("PlatosView.fxml",717,529);
            plato.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void ventanaServiciosEmpleado(){
        try{
            ServiciosHasEmpleadosController miServicioEmpleado = (ServiciosHasEmpleadosController)cambiarEscena("ServiciosHasEmpladosView.fxml",831,529);
            miServicioEmpleado.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void ventanaServiciosPlatos(){
        try{
            ServiciosHasPlatosController miServicioPlato = (ServiciosHasPlatosController)cambiarEscena("ServiciosHasPlatosView.fxml",673,529);
            miServicioPlato.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void ventanaProductosPlatos(){
        try{
            ProductosHasPlatosController miProductoPlato = (ProductosHasPlatosController)cambiarEscena("ProductosHasPlatosView.fxml",673,529);
            miProductoPlato.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void ventanaUsuarios(){
        try{
            UsuarioController miUsuario = (UsuarioController)cambiarEscena("UsuarioView.fxml",717,529);
            miUsuario.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void ventanaLogin(){
        try{
            LoginController miLogin = (LoginController)cambiarEscena("LoginView.fxml",717,529);
            miLogin.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        launch(args);
    }
    
    public Initializable cambiarEscena(String fxml, int ancho, int alto) throws Exception{
        Initializable resultado = null;
        FXMLLoader cargadorFXML = new FXMLLoader();
        InputStream archivo = Principal.class.getResourceAsStream(PAQUETE_VISTA+fxml); // para enseñarle a java la ruta 
        cargadorFXML.setBuilderFactory(new JavaFXBuilderFactory());
        cargadorFXML.setLocation(Principal.class.getResource(PAQUETE_VISTA+fxml));
        escena = new Scene((AnchorPane)cargadorFXML.load(archivo),ancho,alto);
        escenarioPrincipal.setScene(escena);
        escenarioPrincipal.sizeToScene();
        resultado = (Initializable)cargadorFXML.getController();
        return resultado; 
    }
}
