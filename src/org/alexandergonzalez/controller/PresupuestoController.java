
package org.alexandergonzalez.controller;

import eu.schudt.javafx.controls.calendar.DatePicker;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
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
import javafx.scene.layout.GridPane;
import javax.swing.JOptionPane;
import org.alexandergonzalez.bean.Empresa;
import org.alexandergonzalez.bean.Presupuesto;
import org.alexandergonzalez.db.Conexion;
import org.alexandergonzalez.main.Principal;
import org.alexandergonzalez.report.GenerarReporte;


public class PresupuestoController implements Initializable {
    
    private Principal escenarioPrincipal;
    private enum operaciones{GUARDAR, ELIMINAR, ACTUALIZAR, NINGUNO}
    private operaciones tipoOperacion = operaciones.NINGUNO;
    private ObservableList<Presupuesto>  listaPresupuesto;
    private ObservableList<Empresa> listaEmpresa; 
    private DatePicker fecha;
   
    
    @FXML private TextField txtCodigoPresupuesto;
    @FXML private TextField txtCantidad;
    @FXML private GridPane grpFecha;
    @FXML private ComboBox cmbCodigoEmpresa;
    @FXML private TableColumn colCodigoPresupuesto;
    @FXML private TableColumn colFechaSolicitud;
    @FXML private TableColumn colCantidad;
    @FXML private TableColumn colCodigoEmpresa;
    @FXML private TableView tblPresupuesto;
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
        fecha = new DatePicker(Locale.ENGLISH);
        fecha.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        fecha.getCalendarView().todayButtonTextProperty().set("Today");
        fecha.getCalendarView().setShowWeeks(true);
        grpFecha.add(fecha,3,0);
        fecha.getStylesheets().add("/org/alexandergonzalez/resource/TonysKinal.css");
        fecha.setDisable(true);
        cmbCodigoEmpresa.setDisable(true);
        cargarDatos();
    }
    public void cargarDatos(){
        cmbCodigoEmpresa.setItems(getEmpresa());
        tblPresupuesto.setItems(getPresupuesto());
        colCodigoPresupuesto.setCellValueFactory(new PropertyValueFactory<Presupuesto, Integer>("codigoPresupuesto"));
        colFechaSolicitud.setCellValueFactory(new PropertyValueFactory<Presupuesto, Date>("fechaSolicitud"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<Presupuesto, Double>("cantidad"));
        colCodigoEmpresa.setCellValueFactory(new PropertyValueFactory<Presupuesto, Integer>("codigoEmpresa"));
    }
    public void seleccionarElemento(){
        if(tblPresupuesto.getSelectionModel().getSelectedItem() != null){
            fecha.setDisable(false);
            cmbCodigoEmpresa.setDisable(false);
            txtCodigoPresupuesto.setText(String.valueOf(((Presupuesto)tblPresupuesto.getSelectionModel().getSelectedItem()).getCodigoPresupuesto()));
            fecha.selectedDateProperty().set(((Presupuesto)tblPresupuesto.getSelectionModel().getSelectedItem()).getFechaSolicitud());
            txtCantidad.setText(String.valueOf(((Presupuesto)tblPresupuesto.getSelectionModel().getSelectedItem()).getCantidad()));
            cmbCodigoEmpresa.getSelectionModel().select(buscarEmpresa(((Presupuesto)tblPresupuesto.getSelectionModel().getSelectedItem()).getCodigoEmpresa()));
        }else{
            JOptionPane.showMessageDialog(null, "Seleccione un campo con datos","Error",JOptionPane.WARNING_MESSAGE);
        }
    }
    public Empresa buscarEmpresa(int codigoEmpresa){
        Empresa resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_BuscarEmpresa(?)");
            procedimiento.setInt(1, codigoEmpresa);
            ResultSet empresa = procedimiento.executeQuery();
            while(empresa.next()){
                resultado = new Empresa(empresa.getInt("codigoEmpresa"),
                    empresa.getString("nombreEmpresa"),
                    empresa.getString("direccion"),
                    empresa.getString("telefono"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }

    public ObservableList<Presupuesto> getPresupuesto(){
        ArrayList<Presupuesto> lista = new ArrayList<Presupuesto>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarPresupuestos");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Presupuesto(resultado.getInt("codigoPresupuesto"),
                resultado.getDate("fechaSolicitud"),
                resultado.getDouble("cantidadPresupuesto"),
                resultado.getInt("codigoEmpresa")));
            }
        }catch(Exception e){
            e.printStackTrace();    
        }
        return listaPresupuesto = FXCollections.observableList(lista);
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
                limpiarControles();
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
        }
    }
    
    public void guardar(){
        if(txtCantidad.getText().isEmpty() || fecha.getSelectedDate() == null || cmbCodigoEmpresa.getSelectionModel().isEmpty()){
            JOptionPane.showMessageDialog(null, "Ingrese correctamente los datos", "Guardar Presupuesto",JOptionPane.WARNING_MESSAGE);
        }else{
            try{
                Presupuesto registro = new Presupuesto();
                registro.setFechaSolicitud(fecha.getSelectedDate());
                registro.setCantidad(Double.parseDouble(txtCantidad.getText()));
                registro.setCodigoEmpresa(((Empresa)cmbCodigoEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarPresupuesto(?,?,?)");
                procedimiento.setDate(1, new java.sql.Date(registro.getFechaSolicitud().getTime()));
                procedimiento.setDouble(2, registro.getCantidad());
                procedimiento.setInt(3, registro.getCodigoEmpresa());
                procedimiento.execute();
                listaPresupuesto.add(registro);
            }catch(Exception e){
                e.printStackTrace();
            }
            cargarDatos();
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
                if(tblPresupuesto.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Estás seguro de eliminar este campo?",
                            "Eliminar Presupuesto", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarPresupuesto(?)");
                            procedimiento.setInt(1, ((Presupuesto)tblPresupuesto.getSelectionModel().getSelectedItem()).getCodigoPresupuesto());
                            procedimiento.execute();
                            listaPresupuesto.remove(tblPresupuesto.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                        }catch(Exception e ){
                            e.printStackTrace();
                        }
                    }else{
                        limpiarControles();
                        desactivarControles();
                    }   
                }else{
                    JOptionPane.showMessageDialog(null, "Seleccione un campo con datos","Error",JOptionPane.WARNING_MESSAGE);
                }
        } 
    }
    
    public void editar(){
        switch(tipoOperacion){
            case NINGUNO:
                if(tblPresupuesto.getSelectionModel().getSelectedItem()!= null){
                    btnAgregar.setDisable(true);
                    btnEliminar.setDisable(true);
                    btnActualizar.setText("Finalizar");
                    btnReporte.setText("Cancelar");
                    imgActualizar.setImage(new Image("/org/alexandergonzalez/image/iconoActualizado.png"));
                    imgReporte.setImage(new Image("/org/alexandergonzalez/image/iconoCancelar.png"));
                    activarControles();
                    txtCantidad.clear();
                    fecha.selectedDateProperty().set(null);
                    cmbCodigoEmpresa.getSelectionModel().clearSelection();
                    cmbCodigoEmpresa.setDisable(true);
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
        }
    }
    
    public void actualizar(){
        if(txtCantidad.getText().isEmpty() || fecha.getSelectedDate() == null){
            JOptionPane.showMessageDialog(null, "Ingrese correctamente los datos", "Actualizar Presupuesto",JOptionPane.WARNING_MESSAGE);
        }else{
            Presupuesto presupuesto = (Presupuesto)tblPresupuesto.getSelectionModel().getSelectedItem();
            presupuesto.setFechaSolicitud(fecha.getSelectedDate());
            presupuesto.setCantidad(Double.parseDouble(txtCantidad.getText()));
            try{
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EditarPresupuesto(?,?,?)");
                procedimiento.setInt(1, presupuesto.getCodigoPresupuesto());
                procedimiento.setDate(2, new java.sql.Date(presupuesto.getFechaSolicitud().getTime()));
                procedimiento.setDouble(3, presupuesto.getCantidad());
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
                desactivarControles();
                btnAgregar.setDisable(false);
                btnEliminar.setDisable(false);
                btnActualizar.setText("Actualizar");
                btnReporte.setText("Reporte");
                imgActualizar.setImage(new Image("/org/alexandergonzalez/image/iconoActualizar.png"));
                imgReporte.setImage(new Image("/org/alexandergonzalez/image/iconoReporte.png"));
                cargarDatos();
                tipoOperacion = operaciones.NINGUNO;
            default:
                if(tblPresupuesto.getSelectionModel().getSelectedItem() != null)
                imprimirReporte();
                else
                  JOptionPane.showMessageDialog(null, "Seleccione un campo con datos","Error",JOptionPane.WARNING_MESSAGE);
                
                 
        }
    }
    
    public void imprimirReporte(){
        Map parametros = new HashMap();
        int codEmpresa = Integer.valueOf(((Empresa)cmbCodigoEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
        URL subReporteFinal = this.getClass().getResource("/org/alexandergonzalez/report/");
        URL imagenLogo = this.getClass().getResource("/org/alexandergonzalez/image/Logo.png");
        parametros.put("codEmpresa", codEmpresa);
        parametros.put("subReporteFinal", subReporteFinal);
        parametros.put("imagenLogo", imagenLogo);
        GenerarReporte.mostrarReporte("ReporteGeneral.jasper", "Reporte General", parametros);
    }
    
    public void activarControles(){
        txtCodigoPresupuesto.setEditable(false);
        txtCantidad.setEditable(true);
        cmbCodigoEmpresa.setDisable(false);
        fecha.setDisable(false);        
    }
    
    public void desactivarControles(){
        txtCodigoPresupuesto.setEditable(false);
        txtCantidad.setEditable(false);
        cmbCodigoEmpresa.setDisable(true);
        fecha.setDisable(true);
    }
    
    public void limpiarControles(){
        txtCodigoPresupuesto.clear();
        txtCantidad.clear();
        cmbCodigoEmpresa.setValue(null);
        fecha.selectedDateProperty().set(null);
        tblPresupuesto.getSelectionModel().clearSelection();
    }
    
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }
    
    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void ventanaEmpresas(){
        escenarioPrincipal.ventanaEmpresas();
    }
    
    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
}
