/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.alexandergonzalez.bean.TipoEmpleado;
import org.alexandergonzalez.db.Conexion;
import org.alexandergonzalez.main.Principal;

/**
 *
 * @author Alex
 */
public class TipoEmpleadoController implements Initializable{
    
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO}
    private operaciones tipoOperacion = operaciones.NINGUNO;
    private ObservableList<TipoEmpleado> listaTipoEmpleado;
     
    private Principal escenarioPrincipal;
    
    
    @FXML private TextField txtCodTipoEmpleado;
    @FXML private TextField txtTipoEmpleado;
    @FXML private TableView tblTipoEmpleados;
    @FXML private TableColumn colCodTipoEmpleado;
    @FXML private TableColumn colTipoEmpleado;
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
        tblTipoEmpleados.setItems(getTipoEmpleado());
        colCodTipoEmpleado.setCellValueFactory(new PropertyValueFactory<TipoEmpleado, Integer>("codigoTipoEmpleado"));
        colTipoEmpleado.setCellValueFactory(new PropertyValueFactory<TipoEmpleado, String>("descripcion"));
    }

    public ObservableList<TipoEmpleado> getTipoEmpleado(){
        ArrayList<TipoEmpleado> lista = new ArrayList<TipoEmpleado>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_listarTipoEmpleados");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new TipoEmpleado(resultado.getInt("codigoTipoEmpleado"),
                            resultado.getString("descripcion")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaTipoEmpleado = FXCollections.observableList(lista);
    }
    
    public void nuevo(){
        switch(tipoOperacion) {
            case NINGUNO:
                activarControles();
                btnAgregar.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnActualizar.setDisable(true);
                btnReporte.setDisable(true);
                imgAgregar.setImage(new Image("org/alexandergonzalez/image/iconoGuardar.png"));
                imgEliminar.setImage(new Image("org/alexandergonzalez/image/iconoCancelar.png"));
                tipoOperacion = operaciones.GUARDAR;
            break;
            case GUARDAR:
                guardar();
                limpiarControles();
                desactivarControles();
                btnAgregar.setText("Agregar");
                btnEliminar.setText("Eliminar");
                btnActualizar.setDisable(false);
                btnReporte.setDisable(false);
                imgAgregar.setImage(new Image("org/alexandergonzalez/image/iconoAgregar.png"));
                imgEliminar.setImage(new Image("org/alexandergonzalez/image/iconoEliminar.png"));
                tipoOperacion = operaciones.NINGUNO;
                cargarDatos();
        }
    }
    public void guardar(){
        if(txtTipoEmpleado.getText().isEmpty() || txtTipoEmpleado.getText().matches("\\d+")){
          JOptionPane.showMessageDialog(null, "Ingrese correctamente los datos", "Guardar Tipo Empleado", JOptionPane.WARNING_MESSAGE);
        }else{
          TipoEmpleado empleado = new TipoEmpleado();
            empleado.setDescripcion(txtTipoEmpleado.getText());
            try{
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarTipoEmpleado(?)");
                procedimiento.setString(1, empleado.getDescripcion());
                procedimiento.execute();
                listaTipoEmpleado.add(empleado);
            }catch(Exception e){
                e.printStackTrace();
            }  
        }
        
    }
    
    public void seleccionarElementos(){
        if(tblTipoEmpleados.getSelectionModel().getSelectedItem()!= null){
        txtCodTipoEmpleado.setText(String.valueOf(((TipoEmpleado)tblTipoEmpleados.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado()));
        txtTipoEmpleado.setText(((TipoEmpleado)tblTipoEmpleados.getSelectionModel().getSelectedItem()).getDescripcion());
        }else{
            JOptionPane.showMessageDialog(null, "Seleccione un campo con datos", "Error", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    public void eliminar(){
        switch(tipoOperacion){
            case GUARDAR: 
                limpiarControles();
                desactivarControles();
                btnAgregar.setText("Agregar");
                btnEliminar.setText("Eliminar");
                btnActualizar.setDisable(false);
                btnReporte.setDisable(false);
                imgAgregar.setImage(new Image("org/alexandergonzalez/image/iconoAgregar.png"));
                imgEliminar.setImage(new Image("org/alexandergonzalez/image/iconoEliminar.png"));
                tipoOperacion = operaciones.NINGUNO;
            break;
            default:
                if(tblTipoEmpleados.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Estás seguro de eliminar este campo",
                            "Eliminar Tipo de Empleado", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try{
                           PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarTipoEmpleado(?)");
                           procedimiento.setInt(1, ((TipoEmpleado)tblTipoEmpleados.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado());
                           procedimiento.execute();
                           listaTipoEmpleado.remove(tblTipoEmpleados.getSelectionModel().getSelectedIndex());
                           limpiarControles();
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }else{
                        limpiarControles();
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "Seleccione un campo con datos", "Error", JOptionPane.WARNING_MESSAGE);
                    limpiarControles();
                }
            break;
        }
    }
    
    public void editar(){
        switch(tipoOperacion){
            case NINGUNO:
                if(tblTipoEmpleados.getSelectionModel().getSelectedItem() != null){
                    btnAgregar.setDisable(true);
                    btnEliminar.setDisable(true);
                    btnActualizar.setText("Finalizar");
                    btnReporte.setText("Cancelar");
                    imgActualizar.setImage(new Image("org/alexandergonzalez/image/iconoActualizado.png"));
                    imgReporte.setImage(new Image("org/alexandergonzalez/image/iconoCancelar.png"));
                    activarControles();
                    txtTipoEmpleado.clear();
                    tipoOperacion = operaciones.ACTUALIZAR;
                }else{
                    JOptionPane.showMessageDialog(null, "Seleccione un campo con datos", "Error", JOptionPane.WARNING_MESSAGE);
                }
            break;
            case ACTUALIZAR: 
                actualizar();
                limpiarControles();
                desactivarControles();
                btnAgregar.setDisable(false);
                btnEliminar.setDisable(false);
                btnActualizar.setText("Actualizar");
                btnReporte.setText("Reporte");
                imgActualizar.setImage(new Image("org/alexandergonzalez/image/iconoActualizar.png"));
                imgReporte.setImage(new Image("org/alexandergonzalez/image/iconoReporte.png"));
                cargarDatos();
                tipoOperacion = operaciones.NINGUNO;
            break;
        }
    }
    public void actualizar(){
        if(txtTipoEmpleado.getText().isEmpty() || txtTipoEmpleado.getText().matches("\\d+")){
            JOptionPane.showMessageDialog(null, "Ingrese correctamtente los datos", "Actualizar Tipo Empleado", JOptionPane.WARNING_MESSAGE);
        }else{
            TipoEmpleado tipoEmpleado = (TipoEmpleado)tblTipoEmpleados.getSelectionModel().getSelectedItem();
            tipoEmpleado.setDescripcion(txtTipoEmpleado.getText());
            try{
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EditarTipoEmpleado(?,?)");
                procedimiento.setInt(1, tipoEmpleado.getCodigoTipoEmpleado());
                procedimiento.setString(2, tipoEmpleado.getDescripcion());
                procedimiento.execute();
            }catch(Exception e){
                e.printStackTrace();
            }
        } 
    }
    
    public void reporte(){
        switch(tipoOperacion){
            case ACTUALIZAR:
                limpiarControles();
                desactivarControles();
                btnActualizar.setText("Actualizar");
                btnReporte.setText("Reporte");
                btnAgregar.setDisable(false);
                btnEliminar.setDisable(false);
                imgActualizar.setImage(new Image("org/alexandergonzalez/image/iconoActualizar.png"));
                imgReporte.setImage(new Image("org/alexandergonzalez/image/iconoReporte.png"));
                tipoOperacion = operaciones.NINGUNO;
            break;
        }
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
    public void ventanaEmpleado(){
        escenarioPrincipal.ventanaEmpleado();
    }
    public void desactivarControles(){
        txtCodTipoEmpleado.setEditable(false);
        txtTipoEmpleado.setEditable(false);
    }
    public void activarControles(){
        txtCodTipoEmpleado.setEditable(false);
        txtTipoEmpleado.setEditable(true);
    }
    public void limpiarControles(){
        txtCodTipoEmpleado.clear();
        txtTipoEmpleado.clear();
        tblTipoEmpleados.getSelectionModel().clearSelection();
    }
}
