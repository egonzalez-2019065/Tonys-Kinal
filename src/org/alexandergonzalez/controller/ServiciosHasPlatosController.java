
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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.alexandergonzalez.bean.Plato;
import org.alexandergonzalez.bean.Servicio;
import org.alexandergonzalez.bean.ServiciosHasPlatos;
import org.alexandergonzalez.db.Conexion;
import org.alexandergonzalez.main.Principal;

public class ServiciosHasPlatosController implements Initializable{

    private ObservableList<ServiciosHasPlatos> listaServiciosPlatos;
    private ObservableList<Servicio> listaServicios;
    private ObservableList<Plato> listaPlato;
    private Principal escenarioPrincipal;
    private enum operaciones{GUARDAR, ELIMINAR, NINGUNO}
    private operaciones tipoOperacion = operaciones.NINGUNO;
    
    @FXML private TableView tblServiciosPlatos;
    @FXML private TableColumn colCodigo;
    @FXML private TableColumn colServicios;
    @FXML private TableColumn colPlatos;
    @FXML private TextField txtCodigo;
    @FXML private ComboBox cmbServicios;
    @FXML private ComboBox cmbPlatos;
    @FXML private Button btnAgregar;
    @FXML private Button btnEliminar;
    @FXML private Button btnActualizar;
    @FXML private Button btnReporte;
    @FXML private ImageView imgAgregar;
    @FXML private ImageView imgEliminar;
    @FXML private ImageView imgActualizar;
    @FXML private ImageView imgReporte;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
    }

    public void cargarDatos(){
       cmbServicios.setDisable(true);
       cmbPlatos.setDisable(true);
       cmbServicios.setItems(getServicio());
       cmbPlatos.setItems(getPlato());
       tblServiciosPlatos.setItems(getServiciosPlatos()); 
       colCodigo.setCellValueFactory(new PropertyValueFactory<ServiciosHasPlatos, Integer>("serviciosCodigoServicio"));
       colServicios.setCellValueFactory(new PropertyValueFactory<ServiciosHasPlatos, Integer>("codigoServicio"));
       colPlatos.setCellValueFactory(new PropertyValueFactory<ServiciosHasPlatos, Integer>("codigoPlato"));
       
    }
    public ObservableList<ServiciosHasPlatos> getServiciosPlatos(){
        ArrayList<ServiciosHasPlatos> lista =  new ArrayList<ServiciosHasPlatos>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarServicios_has_Platos()");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new ServiciosHasPlatos(resultado.getInt("Servicios_CodigoServicio"),
                        resultado.getInt("codigoServicio"),
                        resultado.getInt("codigoPlato")
                ));
            }   
        }catch(Exception e){
           e.printStackTrace();
        }
        return listaServiciosPlatos = FXCollections.observableList(lista);
    }
    public Servicio buscarServicio(int codigoServicio){
        Servicio resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_BuscarServicio(?)");
            procedimiento.setInt(1, codigoServicio);
            ResultSet servicio = procedimiento.executeQuery();
            while(servicio.next()){
                resultado = (new Servicio(servicio.getInt("codigoServicio"),
                        servicio.getDate("fechaServicio"),
                        servicio.getString("tipoServicio"),
                        servicio.getString("horaServicio"),
                        servicio.getString("lugarServicio"),
                        servicio.getString("telefonoContacto"),
                        servicio.getInt("codigoEmpresa")
                ));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }
    public Plato buscarPlato(int codigoPlato){
        Plato resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_buscarPlato(?)");
            procedimiento.setInt(1, codigoPlato);
            ResultSet plato = procedimiento.executeQuery();
            while(plato.next()){
            resultado = (new Plato(plato.getInt("codigoPlato"),
                        plato.getInt("cantidadPlato"),
                        plato.getString("nombrePlato"),
                        plato.getString("descripcionPlato"),
                        plato.getDouble("precioPlato"),
                        plato.getInt("codigoTipoPlato")
                ));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;      
    }

    public void seleccionarElemento(){
        if(tblServiciosPlatos.getSelectionModel().getSelectedItem() != null){
            txtCodigo.setText(String.valueOf(((ServiciosHasPlatos)tblServiciosPlatos.getSelectionModel().getSelectedItem()).getServiciosCodigoServicio()));
            cmbServicios.getSelectionModel().select(buscarServicio(((ServiciosHasPlatos)tblServiciosPlatos.getSelectionModel().getSelectedItem()).getCodigoServicio()));
            cmbPlatos.getSelectionModel().select(buscarPlato(((ServiciosHasPlatos)tblServiciosPlatos.getSelectionModel().getSelectedItem()).getCodigoPlato()));
        }else{
            JOptionPane.showMessageDialog(null, "Seleccione un campo con datos","Error",JOptionPane.WARNING_MESSAGE);
        }
    }
    
    public ObservableList<Servicio> getServicio(){
        ArrayList<Servicio> lista = new ArrayList<Servicio>();  
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarServicios");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Servicio(resultado.getInt("codigoServicio"),
                        resultado.getDate("fechaServicio"),
                        resultado.getString("tipoServicio"),
                        resultado.getString("horaServicio"),
                        resultado.getString("lugarServicio"),
                        resultado.getString("telefonoContacto"),
                        resultado.getInt("codigoEmpresa")
                ));
            }   
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaServicios = FXCollections.observableList(lista);
    }
    
    public ObservableList<Plato> getPlato(){
        ArrayList<Plato> lista = new ArrayList<Plato>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarPlatos");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Plato(resultado.getInt("codigoPlato"),
                        resultado.getInt("cantidadPlato"),
                        resultado.getString("nombrePlato"),
                        resultado.getString("descripcionPlato"),
                        resultado.getDouble("precioPlato"),
                        resultado.getInt("codigoTipoPlato")
                ));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaPlato = FXCollections.observableList(lista);
    }
    
    public void nuevo(){
        switch(tipoOperacion){
            case NINGUNO:
                activarControles();
                limpiarControles();
                btnAgregar.setText("Guardar");
                imgAgregar.setImage(new Image("org/alexandergonzalez/image/iconoGuardar.png"));
                tipoOperacion = operaciones.GUARDAR;
            break;
            case GUARDAR:
                guardar();
                limpiarControles();
                desactivarControles();
                btnAgregar.setText("Agregar");
                imgAgregar.setImage(new Image("org/alexandergonzalez/image/iconoAgregar.png"));
                tipoOperacion = operaciones.NINGUNO;
        }
    }
    public void guardar(){
        if(txtCodigo.getText().isEmpty() || !txtCodigo.getText().matches("\\d+") || cmbServicios.getSelectionModel().isEmpty() ||
              cmbPlatos.getSelectionModel().isEmpty()){
            JOptionPane.showMessageDialog(null, "Está ingresando incorrectamente los datos", "Guardar Servicios y sus Platos",JOptionPane.WARNING_MESSAGE);
        }else{
            try{
                ServiciosHasPlatos miServicioPlato = new ServiciosHasPlatos();
                miServicioPlato.setServiciosCodigoServicio(Integer.parseInt(txtCodigo.getText()));
                miServicioPlato.setCodigoServicio(((Servicio)cmbServicios.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
                miServicioPlato.setCodigoPlato(((Plato)cmbPlatos.getSelectionModel().getSelectedItem()).getCodigoPlato());  
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarServicios_has_Platos(?,?,?)");
                procedimiento.setInt(1, miServicioPlato.getServiciosCodigoServicio());
                procedimiento.setInt(2, miServicioPlato.getCodigoPlato());
                procedimiento.setInt(3, miServicioPlato.getCodigoServicio());
                procedimiento.execute();
                listaServiciosPlatos.add(miServicioPlato);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    
    public void eliminar(){
        switch(tipoOperacion){
            case GUARDAR:
                limpiarControles();
                desactivarControles();
                btnAgregar.setText("Agregar");
                btnEliminar.setText("Eliminar");
                imgAgregar.setImage(new Image("org/alexandergonzalez/image/iconoAgregar.png"));
                tipoOperacion = operaciones.NINGUNO;
            break;
        }
    }    
//            default:
//                
//                if(tblServiciosPlatos.getSelectionModel().getSelectedItem() != null){
//                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Estás seguro de eliminar este campo?",
//                            "Eliminar Servicio y sus Platos", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
//                    if(respuesta == JOptionPane.YES_OPTION){
//                        try{
//                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarServicios_has_Platos(?)");
//                            procedimiento.setInt(1, ((ServiciosHasPlatos)tblServiciosPlatos.getSelectionModel().getSelectedItem()).getServiciosCodigoServicio());
//                            procedimiento.execute();
//                            listaServiciosPlatos.remove(tblServiciosPlatos.getSelectionModel().getSelectedIndex());
//                            limpiarControles();
//                        }catch(Exception e ){
//                            e.printStackTrace();
//                        }
//                    }else{
//                        limpiarControles();
//                        desactivarControles();
//                    }   
//                }else{
//                    JOptionPane.showMessageDialog(null, "Seleccione un campo con datos","Error",JOptionPane.WARNING_MESSAGE);
//                }
//        } 
//    }
    
    public void activarControles(){
        txtCodigo.setEditable(true);
        cmbServicios.setDisable(false);
        cmbPlatos.setDisable(false);
    }
    public void desactivarControles(){
        txtCodigo.setEditable(false);
        cmbServicios.setDisable(true);
        cmbPlatos.setDisable(true);
    }
    public void limpiarControles(){
        txtCodigo.clear();
        cmbServicios.setValue(null);
        cmbPlatos.setValue(null);
        tblServiciosPlatos.getSelectionModel().clearSelection();
    }
    
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
    public void ventanaServicios(){
        escenarioPrincipal.ventanaServicios();
    }
}
