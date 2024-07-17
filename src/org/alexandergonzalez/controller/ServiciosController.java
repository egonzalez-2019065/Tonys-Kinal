
package org.alexandergonzalez.controller;

import com.jfoenix.controls.JFXTimePicker;
import eu.schudt.javafx.controls.calendar.DatePicker;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
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
import org.alexandergonzalez.bean.Servicio;
import org.alexandergonzalez.db.Conexion;
import org.alexandergonzalez.main.Principal;


public class ServiciosController implements Initializable {

    private Principal escenarioPrincipal;
    private ObservableList<Servicio> listaServicios;
    private ObservableList<Empresa> listaEmpresas;
    private DatePicker fecha;
    private enum operaciones{GUARDAR, ELIMINAR, ACTUALIZAR,NINGUNO}
    private operaciones tipoOperacion = operaciones.NINGUNO;

    
    @FXML private TableView tblServicios;
    @FXML private TableColumn colCodigo;
    @FXML private TableColumn colFecha;
    @FXML private TableColumn colServicio;
    @FXML private TableColumn colHora;
    @FXML private TableColumn colUbicacion;
    @FXML private TableColumn colTelefono;
    @FXML private TableColumn colEmpresa;
    @FXML private ComboBox cmbEmpresa;
    @FXML private GridPane grpFecha;
    @FXML private TextField txtTipoServicio;
    @FXML private TextField txtLugarServicio;
    @FXML private TextField txtTelefono;
    @FXML private JFXTimePicker jfxHora;
    @FXML private TextField txtCodigo;
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
        grpFecha.add(fecha, 2, 0);
        fecha.getStylesheets().add("/org/alexandergonzalez/resource/TonysKinal.css");
        cargarDatos();
        fecha.setDisable(true);
        cmbEmpresa.setDisable(true);
    }

    public void cargarDatos(){
       cmbEmpresa.setItems(getEmpresa());
       tblServicios.setItems(getServicio());
       colCodigo.setCellValueFactory(new PropertyValueFactory <Servicio, Integer>("codigoServicio"));
       colFecha.setCellValueFactory(new PropertyValueFactory <Servicio, Date>("fechaServicio"));
       colServicio.setCellValueFactory(new PropertyValueFactory<Servicio,String>("tipoServicio"));
       colHora.setCellValueFactory(new PropertyValueFactory<Servicio, String>("horaServicio"));
       colUbicacion.setCellValueFactory(new PropertyValueFactory<Servicio,String>("lugarServicio"));
       colTelefono.setCellValueFactory(new PropertyValueFactory<Servicio, String>("telefonoContacto"));
       colEmpresa.setCellValueFactory(new PropertyValueFactory<Servicio, Integer>("codigoEmpresa"));
    }
    
    public void seleccionarElementos(){
        if(tblServicios.getSelectionModel().getSelectedItem() !=null){
            fecha.setDisable(false);
            cmbEmpresa.setDisable(false);
            fecha.selectedDateProperty().set(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getFechaServicio());
            txtCodigo.setText(String.valueOf(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getCodigoServicio()));
            txtTipoServicio.setText(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getTipoServicio());
            txtLugarServicio.setText(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getLugarServicio());
            txtTelefono.setText(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getTelefonoContacto());
            cmbEmpresa.getSelectionModel().select(buscarEmpresa(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getCodigoEmpresa()));
            jfxHora.setValue(LocalTime.parse(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getHoraServicio()));
            
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
        return listaEmpresas = FXCollections.observableList(lista);
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
                cargarDatos();
                tipoOperacion = operaciones.NINGUNO;
        }
    }
    
    public void guardar(){
        if(fecha.getSelectedDate() == null || txtTipoServicio.getText().isEmpty() || jfxHora.getValue() == null|| txtLugarServicio.getText().isEmpty() 
            || txtTelefono.getText().length() != 8 || cmbEmpresa.getSelectionModel().isEmpty()){
            JOptionPane.showMessageDialog(null, "Ingrese correctamente los datos", "Guardar Servicio",JOptionPane.WARNING_MESSAGE);
        }else{  
            try{
                Servicio miServicio = new Servicio();
                miServicio.setFechaServicio(fecha.getSelectedDate());
                miServicio.setTipoServicio(txtTipoServicio.getText());
               miServicio.setHoraServicio(String.valueOf(jfxHora.getValue()));
                miServicio.setLugarServicio(txtLugarServicio.getText());
                miServicio.setTelefonoContacto(txtTelefono.getText());
                miServicio.setCodigoEmpresa(((Empresa)cmbEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
                
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarServicio(?,?,?,?,?,?)");
                procedimiento.setDate(1, new java.sql.Date(miServicio.getFechaServicio().getTime()));
                procedimiento.setString(2, miServicio.getTipoServicio());
                procedimiento.setString(3, miServicio.getHoraServicio());
                procedimiento.setString(4, miServicio.getLugarServicio());
                procedimiento.setString(5, miServicio.getTelefonoContacto());
                procedimiento.setInt(6, miServicio.getCodigoEmpresa());
                procedimiento.execute();
                listaServicios.add(miServicio);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    
    public void eliminar(){
        switch(tipoOperacion){
            case GUARDAR:
                btnAgregar.setText("Agregar");
                btnEliminar.setText("Eliminar");
                btnActualizar.setDisable(false);
                btnReporte.setDisable(false);
                imgAgregar.setImage(new Image("org/alexandergonzalez/image/iconoAgregar.png"));
                imgEliminar.setImage(new Image("org/alexandergonzalez/image/iconoEliminar.png"));
                tipoOperacion = operaciones.NINGUNO;
            break;
            default:
                if(tblServicios.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Estás seguro de eliminar este campo?",
                            "Eliminar Servicio", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarServicio(?)");
                            procedimiento.setInt(1, ((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getCodigoServicio());
                            procedimiento.execute();
                            listaServicios.remove(tblServicios.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                        }catch(Exception e){
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
                if(tblServicios.getSelectionModel().getSelectedItem()!= null){
                    btnAgregar.setDisable(true);
                    btnEliminar.setDisable(true);
                    btnActualizar.setText("Finalizar");
                    btnReporte.setText("Cancelar");
                    imgActualizar.setImage(new Image("/org/alexandergonzalez/image/iconoActualizado.png"));
                    imgReporte.setImage(new Image("/org/alexandergonzalez/image/iconoCancelar.png"));
                    activarControles();
                    txtTipoServicio.clear();
                    jfxHora.setValue(null);
                    txtLugarServicio.clear();
                    txtTelefono.clear();
                    fecha.selectedDateProperty().set(null);
                    cmbEmpresa.getSelectionModel().clearSelection();
                    cmbEmpresa.setDisable(true);
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
        if(fecha.getSelectedDate() == null || txtTipoServicio.getText().isEmpty() || jfxHora.getValue() == null || txtLugarServicio.getText().isEmpty() 
                || txtTelefono.getText().length() != 8){
            JOptionPane.showMessageDialog(null, "Ingrese correctamente los datos", "Actualizar Servicio",JOptionPane.WARNING_MESSAGE);
        }else{
            try{
                Servicio miServicio = ((Servicio)tblServicios.getSelectionModel().getSelectedItem());
                miServicio.setFechaServicio(fecha.getSelectedDate());
                miServicio.setTipoServicio(txtTipoServicio.getText());
                miServicio.setHoraServicio(String.valueOf(jfxHora.getValue()));
                miServicio.setLugarServicio(txtLugarServicio.getText());
                miServicio.setTelefonoContacto(txtTelefono.getText());
                
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EditarServicio(?,?,?,?,?,?)");
                procedimiento.setInt(1, miServicio.getCodigoServicio());
                procedimiento.setDate(2, new java.sql.Date(miServicio.getFechaServicio().getTime()));
                procedimiento.setString(3, miServicio.getTipoServicio());
                procedimiento.setString(4, miServicio.getHoraServicio());
                procedimiento.setString(5, miServicio.getLugarServicio());
                procedimiento.setString(6, miServicio.getTelefonoContacto());
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
                btnAgregar.setDisable(false);
                btnEliminar.setDisable(false);
                btnActualizar.setText("Actualizar");
                btnReporte.setText("Reporte");
                imgActualizar.setImage(new Image("/org/alexandergonzalez/image/iconoActualizar.png"));
                imgReporte.setImage(new Image("/org/alexandergonzalez/image/iconoReporte.png"));
                tipoOperacion = operaciones.NINGUNO;
        }
    }
    public void limpiarControles(){
        txtCodigo.clear();
        fecha.selectedDateProperty().set(null);
        txtTipoServicio.clear();
        jfxHora.setValue(null);
        txtLugarServicio.clear();
        txtTelefono.clear();
        cmbEmpresa.setValue(null);
        tblServicios.getSelectionModel().clearSelection();
    }
    
    public void activarControles(){
        fecha.setDisable(false);
        txtTipoServicio.setEditable(true);
        jfxHora.setDisable(false);
        txtLugarServicio.setEditable(true);
        txtTelefono.setEditable(true);
        cmbEmpresa.setDisable(false); 
    }
    
    public void desactivarControles(){
        fecha.setDisable(true);
        txtTipoServicio.setEditable(false);
        jfxHora.setDisable(true);
        txtLugarServicio.setEditable(false);
        txtTelefono.setEditable(false);
        cmbEmpresa.setDisable(true);
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
    public void ventanaEmpresas(){
        escenarioPrincipal.ventanaEmpresas();
    }

}
