
package org.alexandergonzalez.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
import javafx.scene.layout.Pane;
import javax.swing.JOptionPane;
import org.alexandergonzalez.bean.Empresa;
import org.alexandergonzalez.db.Conexion;
import org.alexandergonzalez.main.Principal;
import org.alexandergonzalez.report.GenerarReporte;

public class EmpresasController implements Initializable{
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO}
    private operaciones tipoOperacion = operaciones.NINGUNO;
    private Principal escenarioPrincipal; 
    private ObservableList<Empresa> listaEmpresa; 
    
    
    @FXML private TextField txtCodigoEmpresa;
    @FXML private TextField txtUbicacion;
    @FXML private TextField txtNombreEmpresa;
    @FXML private TextField txtTelefono;
    @FXML private TableView tblEmpresas;
    @FXML private TableColumn colCodEmpresa;
    @FXML private TableColumn colNombreEmpresa;
    @FXML private TableColumn colDireccion;
    @FXML private TableColumn colTelefono;
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
        tblEmpresas.setItems(getEmpresa());
        colCodEmpresa.setCellValueFactory(new PropertyValueFactory<Empresa, Integer>("codigoEmpresa"));
        colNombreEmpresa.setCellValueFactory(new PropertyValueFactory<Empresa, String>("nombreEmpresa"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<Empresa, String>("direccion"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<Empresa, String>("telefono"));   
    }
    
    public ObservableList<Empresa> getEmpresa(){
        ArrayList<Empresa> lista = new ArrayList<Empresa>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarEmpresas");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add( new Empresa(resultado.getInt("codigoEmpresa"),
                        resultado.getString("nombreEmpresa"),
                        resultado.getString("direccion"),
                        resultado.getString("telefono")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaEmpresa = FXCollections.observableList(lista);
    }
    
    public void nuevo(){
        switch(tipoOperacion){
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
        if(txtNombreEmpresa.getText().isEmpty() || txtUbicacion.getText().isEmpty() || txtTelefono.getText().length() != 8 || !txtTelefono.getText().matches("\\d+")){
            JOptionPane.showMessageDialog(null, "Está ingresando incorrectamente los datos", "Guardar Empresa",JOptionPane.WARNING_MESSAGE);
        }else{
            Empresa empresa = new Empresa();
            empresa.setNombreEmpresa(txtNombreEmpresa.getText());
            empresa.setDireccionEmpresa(txtUbicacion.getText());
            empresa.setTelefono(txtTelefono.getText());
            try{
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarEmpresa (?, ?, ?)");
                procedimiento.setString(1, empresa.getNombreEmpresa());
                procedimiento.setString(2, empresa.getDireccion());
                procedimiento.setString(3, empresa.getTelefono());
                procedimiento.execute();
                listaEmpresa.add(empresa);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    
    public void seleccionarElemento(){
        if(tblEmpresas.getSelectionModel().getSelectedItem() != null){
        txtCodigoEmpresa.setText(String.valueOf(((Empresa)tblEmpresas.getSelectionModel().getSelectedItem()).getCodigoEmpresa()));
        txtNombreEmpresa.setText(((Empresa)tblEmpresas.getSelectionModel().getSelectedItem()).getNombreEmpresa());
        txtUbicacion.setText(((Empresa)tblEmpresas.getSelectionModel().getSelectedItem()).getDireccion());
        txtTelefono.setText(((Empresa)tblEmpresas.getSelectionModel().getSelectedItem()).getTelefono());
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
                if(tblEmpresas.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Estás seguro de eliminar este campo?", 
                            "Eliminar Empresa", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarEmpresa(?)");
                            procedimiento.setInt(1, (((Empresa)tblEmpresas.getSelectionModel().getSelectedItem()).getCodigoEmpresa()));
                            procedimiento.execute();
                            listaEmpresa.remove(tblEmpresas.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }else{
                        limpiarControles();
                    }
                }else{
                   JOptionPane.showMessageDialog(null, "Seleccione un campo con datos", "Error", JOptionPane.WARNING_MESSAGE);
                }
        }
    }
    
    public void editar(){
        switch(tipoOperacion){
            case NINGUNO: 
                if(tblEmpresas.getSelectionModel().getSelectedItem() != null){
                    btnAgregar.setDisable(true);
                    btnEliminar.setDisable(true);
                    btnActualizar.setText("Finalizar");
                    btnReporte.setText("Cancelar");
                    imgActualizar.setImage(new Image("/org/alexandergonzalez/image/iconoActualizado.png"));
                    imgReporte.setImage(new Image("/org/alexandergonzalez/image/iconoCancelar.png"));
                    activarControles();
                    txtUbicacion.clear();
                    txtNombreEmpresa.clear();
                    txtTelefono.clear();
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
                imgActualizar.setImage(new Image("/org/alexandergonzalez/image/iconoActualizar.png"));
                imgReporte.setImage(new Image("/org/alexandergonzalez/image/iconoReporte.png"));
                cargarDatos();
                tipoOperacion = operaciones.NINGUNO;
            break;
        }
    }
    
    public void actualizar(){
        if(txtNombreEmpresa.getText().isEmpty() || txtUbicacion.getText().isEmpty() || txtTelefono.getText().length() != 8 || !txtTelefono.getText().matches("\\d+")){
            JOptionPane.showMessageDialog(null, "Está ingresando incorrectamente los datos", "Actualizar Empresa", JOptionPane.WARNING_MESSAGE);
        }else{
            Empresa empresa = (Empresa)tblEmpresas.getSelectionModel().getSelectedItem();
            empresa.setNombreEmpresa(txtNombreEmpresa.getText());
            empresa.setDireccionEmpresa(txtUbicacion.getText());
            empresa.setTelefono(txtTelefono.getText());
            try{
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EditarEmpresa(?,?,?,?)");
                procedimiento.setInt(1, empresa.getCodigoEmpresa());
                procedimiento.setString(2, empresa.getNombreEmpresa());
                procedimiento.setString(3, empresa.getDireccion());
                procedimiento.setString(4, empresa.getTelefono());
                procedimiento.execute();
            }catch(Exception e){
                e.printStackTrace();
            }
            tipoOperacion = operaciones.NINGUNO;  
        }
    }
    
    public void reporte(){       
        switch(tipoOperacion){
            case ACTUALIZAR:
                limpiarControles();
                btnActualizar.setText("Actualizar");
                btnReporte.setText("Reporte");
                btnAgregar.setDisable(false);
                btnEliminar.setDisable(false);
                imgActualizar.setImage(new Image("/org/alexandergonzalez/image/iconoActualizar.png"));
                imgReporte.setImage(new Image("/org/alexandergonzalez/image/iconoReporte.png"));
                tipoOperacion = operaciones.NINGUNO;
           break;
            default:
                imprimirReporte();
       }
    }
    
    public void imprimirReporte(){
        Map parametros = new HashMap();
        URL imagenLogo = this.getClass().getResource("/org/alexandergonzalez/image/Logo.png");
        parametros.put("imagenLogo", imagenLogo);
        parametros.put("codigoEmpresa", null);
        GenerarReporte.mostrarReporte("ReporteEmpresas.jasper", "Reporte de Empresas", parametros);
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
    public void ventanaPresupuesto(){
        escenarioPrincipal.ventanaPresupuesto();
    }
    public void ventanaServicios(){
        escenarioPrincipal.ventanaServicios();
    }
    public void desactivarControles(){
        txtCodigoEmpresa.setEditable(false);
        txtUbicacion.setEditable(false);
        txtNombreEmpresa.setEditable(false);
        txtTelefono.setEditable(false);
    }
    public void activarControles(){
        txtCodigoEmpresa.setEditable(false);
        txtUbicacion.setEditable(true);
        txtNombreEmpresa.setEditable(true);
        txtTelefono.setEditable(true);
    }
    public void limpiarControles(){
        txtCodigoEmpresa.clear();
        txtUbicacion.clear();
        txtNombreEmpresa.clear();
        txtTelefono.clear();
        tblEmpresas.getSelectionModel().clearSelection();
    }  
}
