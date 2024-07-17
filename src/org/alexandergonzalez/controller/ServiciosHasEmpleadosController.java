
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
import org.alexandergonzalez.bean.Empleado;
import org.alexandergonzalez.bean.Servicio;
import org.alexandergonzalez.bean.ServiciosHasEmpleados;
import org.alexandergonzalez.db.Conexion;
import org.alexandergonzalez.main.Principal;

public class ServiciosHasEmpleadosController implements Initializable {

    private Principal escenarioPrincipal;
    private enum operaciones{GUARDAR, ELIMINAR, ACTUALIZAR, NINGUNO}
    private operaciones tipoOperacion = operaciones.NINGUNO;
    private ObservableList<ServiciosHasEmpleados> listaServiciosEmpleados;
    private ObservableList<Empleado> listaEmpleado;
    private ObservableList<Servicio> listaServicio;
    private DatePicker fecha;
    
    
    @FXML private TableView tblServiciosEmpleados;
    @FXML private TableColumn colCodigo;
    @FXML private TableColumn colServicio;
    @FXML private TableColumn colEmpleado;
    @FXML private TableColumn colFecha;
    @FXML private TableColumn colHora;
    @FXML private TableColumn colUbicacion;
    @FXML private GridPane grpFecha;
    @FXML private TextField txtCodigo;
    @FXML private TextField txtServicio;
    @FXML private JFXTimePicker jfxHora;
    @FXML private TextField txtUbicacion;
    @FXML private ComboBox cmbEmpleado;
    @FXML private ComboBox cmbServicio;
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
        fecha = new DatePicker(Locale.ENGLISH);
        fecha.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        fecha.getCalendarView().todayButtonTextProperty().set("Today");
        fecha.getCalendarView().setShowWeeks(true);
        grpFecha.add(fecha,1,0);
        fecha.getStylesheets().add("/org/alexandergonzalez/resource/TonysKinal.css");
        fecha.setDisable(true);
    }

    public void cargarDatos(){
        tblServiciosEmpleados.setItems(getServiciosEmpleados());
        cmbEmpleado.setItems(getEmpleado());
        cmbServicio.setItems(getServicio());
        cmbEmpleado.setDisable(true);
        cmbServicio.setDisable(true);
        colCodigo.setCellValueFactory(new PropertyValueFactory<ServiciosHasEmpleados, Integer>("serviciosCodigoEmpleado"));
        colServicio.setCellValueFactory(new PropertyValueFactory<ServiciosHasEmpleados, Integer>("codigoServicio"));
        colEmpleado.setCellValueFactory(new PropertyValueFactory<ServiciosHasEmpleados, Integer>("codigoEmpleado"));
        colFecha.setCellValueFactory(new PropertyValueFactory<ServiciosHasEmpleados, Date>("fechaEvento"));
        colHora.setCellValueFactory(new PropertyValueFactory<ServiciosHasEmpleados, String>("horaEvento"));
        colUbicacion.setCellValueFactory(new PropertyValueFactory<ServiciosHasEmpleados, String>("lugarEvento"));
    }
    public ObservableList<ServiciosHasEmpleados> getServiciosEmpleados(){
        ArrayList<ServiciosHasEmpleados> lista = new ArrayList<ServiciosHasEmpleados>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarServicios_has_Empleados()");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new ServiciosHasEmpleados(resultado.getInt("Servicios_codigoEmpleados"),
                        resultado.getInt("codigoServicio"),
                        resultado.getInt("codigoEmpleado"),
                        resultado.getDate("fechaEvento"),
                        resultado.getString("horaEvento"),
                        resultado.getString("lugarEvento")
                ));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaServiciosEmpleados = FXCollections.observableList(lista);
    }
    
   public ObservableList<Empleado> getEmpleado(){
        ArrayList<Empleado> lista = new ArrayList<Empleado>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarEmpleados");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Empleado(resultado.getInt("codigoEmpleado"),
                        resultado.getInt("numeroEmpleado"),
                        resultado.getString("apellidosEmpleado"),
                        resultado.getString("nombresEmpleado"),
                        resultado.getString("direccionEmpleado"),
                        resultado.getString("telefonoContacto"),
                        resultado.getString("gradoCocinero"),
                        resultado.getInt("codigoTipoEmpleado")
                ));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaEmpleado = FXCollections.observableList(lista);
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
        return listaServicio = FXCollections.observableList(lista);
    }
    public Empleado buscarEmpleado(int codigoEmpleado){
        Empleado resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_BuscarEmpleado(?)");
            procedimiento.setInt(1, codigoEmpleado);
            ResultSet empleado = procedimiento.executeQuery();
            while(empleado.next()){
                resultado = (new Empleado(empleado.getInt("codigoEmpleado"),
                        empleado.getInt("numeroEmpleado"),
                        empleado.getString("apellidosEmpleado"),
                        empleado.getString("nombresEmpleado"),
                        empleado.getString("direccionEmpleado"),
                        empleado.getString("telefonoContacto"),
                        empleado.getString("gradoCocinero"),
                        empleado.getInt("codigoTipoEmpleado")
                ));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
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
    public void seleccionarElementos(){
        if(tblServiciosEmpleados.getSelectionModel().getSelectedItem() != null){
            txtCodigo.setText(String.valueOf(((ServiciosHasEmpleados)tblServiciosEmpleados.getSelectionModel().getSelectedItem()).getServiciosCodigoEmpleado()));
            cmbEmpleado.getSelectionModel().select(buscarEmpleado(((ServiciosHasEmpleados)tblServiciosEmpleados.getSelectionModel().getSelectedItem()).getCodigoEmpleado()));
            cmbServicio.getSelectionModel().select(buscarServicio(((ServiciosHasEmpleados)tblServiciosEmpleados.getSelectionModel().getSelectedItem()).getCodigoServicio()));
            fecha.selectedDateProperty().set(((ServiciosHasEmpleados)tblServiciosEmpleados.getSelectionModel().getSelectedItem()).getFechaEvento());
            jfxHora.setValue(LocalTime.parse(((ServiciosHasEmpleados)tblServiciosEmpleados.getSelectionModel().getSelectedItem()).getHoraEvento()));
            txtUbicacion.setText(((ServiciosHasEmpleados)tblServiciosEmpleados.getSelectionModel().getSelectedItem()).getLugarEvento());
        }else{
            JOptionPane.showMessageDialog(null, "Seleccione un campo con datos","Error",JOptionPane.WARNING_MESSAGE);
        }
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
                cargarDatos();
                tipoOperacion = operaciones.NINGUNO;
        }
    }
    public void guardar(){
        if(txtCodigo.getText().isEmpty() || !txtCodigo.getText().matches("\\d+") || cmbServicio.getSelectionModel().isEmpty() ||
            cmbEmpleado.getSelectionModel().isEmpty() || fecha.getSelectedDate() == null || jfxHora.getValue() == null || txtUbicacion.getText().isEmpty()){
            JOptionPane.showMessageDialog(null, "Está ingresando incorrectamente los datos", "Agregar Servicio y sus Empleados", JOptionPane.WARNING_MESSAGE);
        }else{
            ServiciosHasEmpleados miServicioEmpleado = new ServiciosHasEmpleados();
            miServicioEmpleado.setServiciosCodigoEmpleado(Integer.parseInt((txtCodigo.getText())));
            miServicioEmpleado.setCodigoServicio(((Servicio)cmbServicio.getSelectionModel().getSelectedItem()).getCodigoServicio());
            miServicioEmpleado.setCodigoEmpleado(((Empleado)cmbEmpleado.getSelectionModel().getSelectedItem()).getCodigoEmpleado());
            miServicioEmpleado.setFechaEvento(fecha.getSelectedDate());
            miServicioEmpleado.setHoraEvento(String.valueOf(jfxHora.getValue()));

            miServicioEmpleado.setLugarEvento(txtUbicacion.getText());
            try{
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarServicios_has_Empleados(?,?,?,?,?,?)");
                    procedimiento.setInt(1, miServicioEmpleado.getServiciosCodigoEmpleado());
                    procedimiento.setInt(2, miServicioEmpleado.getCodigoServicio());
                    procedimiento.setInt(3, miServicioEmpleado.getCodigoEmpleado());
                    procedimiento.setDate(4, new java.sql.Date(miServicioEmpleado.getFechaEvento().getTime()));
                    procedimiento.setString(5, miServicioEmpleado.getHoraEvento());
                    procedimiento.setString(6, miServicioEmpleado.getLugarEvento());
                    procedimiento.execute();
                    listaServiciosEmpleados.add(miServicioEmpleado);
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
                imgAgregar.setImage(new Image("org/alexandergonzalez/image/iconoAgregar.png"));
                tipoOperacion = operaciones.NINGUNO;
            break;
        }
    }
//            default:
//                if(tblServiciosEmpleados.getSelectionModel().getSelectedItem() != null){
//                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Estás seguro de eliminar este campo?",
//                            "Eliminar Servicios y sus Empleados", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
//                    if(respuesta == JOptionPane.YES_OPTION){
//                        try{
//                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarServicios_has_Empleados(?)");
//                            procedimiento.setInt(1, ((ServiciosHasEmpleados)tblServiciosEmpleados.getSelectionModel().getSelectedItem()).getServiciosCodigoEmpleado());
//                            procedimiento.execute();
//                            listaServiciosEmpleados.remove(tblServiciosEmpleados.getSelectionModel().getSelectedIndex());
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
//    
//    public void editar(){
//        switch(tipoOperacion){
//            case NINGUNO:
//                if(tblServiciosEmpleados.getSelectionModel().getSelectedItem()!= null){
//                    btnAgregar.setDisable(true);
//                    btnEliminar.setDisable(true);
//                    btnActualizar.setText("Finalizar");
//                    btnReporte.setText("Cancelar");
//                    imgActualizar.setImage(new Image("/org/alexandergonzalez/image/iconoActualizado.png"));
//                    imgReporte.setImage(new Image("/org/alexandergonzalez/image/iconoCancelar.png"));
//                    activarControles();
//                    cmbEmpleado.setDisable(true);
//                    cmbServicio.setDisable(true);
//                    txtCodigo.setEditable(false);
//                    fecha.selectedDateProperty().set(null);
//                    cmbEmpleado.setValue(null);
//                    cmbServicio.setValue(null);
//                    txtHora.clear();
//                    txtMinuto.clear();
//                    txtUbicacion.clear();
//                    tipoOperacion = operaciones.ACTUALIZAR;
//                }else{
//                    JOptionPane.showMessageDialog(null, "Seleccione un campo con datos", "Error", JOptionPane.WARNING_MESSAGE);
//                }
//            break;
//            case ACTUALIZAR:
//                actualizar();
//                limpiarControles();
//                desactivarControles();
//                btnAgregar.setDisable(false);
//                btnEliminar.setDisable(false);
//                btnActualizar.setText("Actualizar");
//                btnReporte.setText("Reporte");
//                imgActualizar.setImage(new Image("/org/alexandergonzalez/image/iconoActualizar.png"));
//                imgReporte.setImage(new Image("/org/alexandergonzalez/image/iconoReporte.png"));
//                cargarDatos();
//                tipoOperacion = operaciones.NINGUNO;
//        }
//    }
//    
//    public void actualizar(){
//        if(fecha.getSelectedDate() == null || txtHora.getText().isEmpty() || txtUbicacion.getText().isEmpty()){
//            JOptionPane.showMessageDialog(null, "Está ingresando incorrectamente los datos", "Actualizar Servicio y sus Empleados", JOptionPane.WARNING_MESSAGE);
//        }else{
//            ServiciosHasEmpleados miServicioEmpleado = (ServiciosHasEmpleados)tblServiciosEmpleados.getSelectionModel().getSelectedItem();
//            miServicioEmpleado.setFechaEvento(fecha.getSelectedDate());
//            if(!txtMinuto.getText().isEmpty())
//                miServicioEmpleado.setHoraEvento(Time.valueOf(txtHora.getText()+":"+txtMinuto.getText()+":"+"00"));
//            else
//                miServicioEmpleado.setHoraEvento(Time.valueOf(txtHora.getText()+":"+"00"+":"+"00"));
//
//            miServicioEmpleado.setLugarEvento(txtUbicacion.getText());
//            try{
//                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EditarServicios_has_Empleados(?,?,?,?)");
//                    procedimiento.setInt(1, miServicioEmpleado.getServiciosCodigoEmpleado());
//                    procedimiento.setDate(2, new java.sql.Date(miServicioEmpleado.getFechaEvento().getTime()));
//                    procedimiento.setTime(3, miServicioEmpleado.getHoraEvento());
//                    procedimiento.setString(4, miServicioEmpleado.getLugarEvento());
//                    procedimiento.execute();
//                    listaServiciosEmpleados.add(miServicioEmpleado);
//            }catch(Exception e){
//              e.printStackTrace();
//            }
//        }
//    }
//    
//    public void reporte(){
//        switch(tipoOperacion){
//            case ACTUALIZAR:
//                limpiarControles();
//                desactivarControles();
//                btnAgregar.setDisable(false);
//                btnEliminar.setDisable(false);
//                btnActualizar.setText("Actualizar");
//                btnReporte.setText("Reporte");
//                imgActualizar.setImage(new Image("/org/alexandergonzalez/image/iconoActualizar.png"));
//                imgReporte.setImage(new Image("/org/alexandergonzalez/image/iconoReporte.png"));
//                tipoOperacion = operaciones.NINGUNO;
//        }
//    }
    public void limpiarControles(){
        txtCodigo.clear();
        fecha.selectedDateProperty().set(null);
        cmbEmpleado.setValue(null);
        cmbServicio.setValue(null);
        jfxHora.setValue(null);
        txtUbicacion.clear();
        tblServiciosEmpleados.getSelectionModel().clearSelection();
    }
    public void activarControles(){
        txtCodigo.setEditable(true);
        fecha.setDisable(false);
        cmbEmpleado.setDisable(false);
        cmbServicio.setDisable(false);
        jfxHora.setEditable(true);
        txtUbicacion.setEditable(true);
    }
    public void desactivarControles(){
        txtCodigo.setEditable(false);
        fecha.setDisable(true);
        cmbEmpleado.setDisable(true);
        cmbServicio.setDisable(true);
        jfxHora.setEditable(false);
        txtUbicacion.setEditable(false);
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
