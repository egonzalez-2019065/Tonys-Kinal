
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
import org.alexandergonzalez.bean.Empleado;
import org.alexandergonzalez.bean.TipoEmpleado;
import org.alexandergonzalez.db.Conexion;
import org.alexandergonzalez.main.Principal;


public class EmpleadoController implements Initializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();  
    }   
    
    private Principal escenarioPrincipal;
    private ObservableList<Empleado> listaEmpleado;
    private ObservableList<TipoEmpleado> listaTipoEmpleado;
    private enum operaciones{GUARDAR, ELIMINAR, ACTUALIZAR, NINGUNO};
    private operaciones tipoOperacion = operaciones.NINGUNO;
    
    
    @FXML private TextField txtCodigoEmpleado;
    @FXML private TextField txtNumeroEmpleado;
    @FXML private TextField txtNombres;
    @FXML private TextField txtApellidos;
    @FXML private TextField txtDireccion;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtGradoCocinero;
    @FXML private ComboBox cmbTipoEmpleado;
    @FXML private TableView tblEmpleados;
    @FXML private TableColumn colCodigo;
    @FXML private TableColumn colNumero;
    @FXML private TableColumn colNombres;
    @FXML private TableColumn colApellidos;
    @FXML private TableColumn colDireccion;
    @FXML private TableColumn colTelefono;
    @FXML private TableColumn colNivel;
    @FXML private TableColumn colTipo;
    @FXML private Button btnAgregar;
    @FXML private Button btnEliminar;
    @FXML private Button btnActualizar;
    @FXML private Button btnReporte;
    @FXML private ImageView imgAgregar;
    @FXML private ImageView imgEliminar;
    @FXML private ImageView imgActualizar;
    @FXML private ImageView imgReporte;

    public void cargarDatos(){
        tblEmpleados.setItems(getEmpleado());
        cmbTipoEmpleado.setDisable(true);
        colCodigo.setCellValueFactory(new PropertyValueFactory<Empleado, Integer>("codigoEmpleado"));
        colNumero.setCellValueFactory(new PropertyValueFactory<Empleado, Integer>("numeroEmpleado"));
        colNombres.setCellValueFactory(new PropertyValueFactory<Empleado, String>("nombresEmpleado"));
        colApellidos.setCellValueFactory(new PropertyValueFactory<Empleado, String>("apellidosEmpleado"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<Empleado, String>("direccionEmpleado"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<Empleado, String>("telefonoContacto"));
        colNivel.setCellValueFactory(new PropertyValueFactory<Empleado, String>("gradoCocinero"));
        colTipo.setCellValueFactory(new PropertyValueFactory<Empleado, String>("codigoTipoEmpleado"));
        cmbTipoEmpleado.setItems(getTipoEmpleado());
        
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
    
    public TipoEmpleado buscarEmpleado(int codigoTipoEmpleado){
        TipoEmpleado resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_BuscarTipoEmpleado(?)");
            procedimiento.setInt(1, codigoTipoEmpleado);
            ResultSet tipoEmpleado = procedimiento.executeQuery();
            while(tipoEmpleado.next()){
                resultado = new TipoEmpleado(tipoEmpleado.getInt("codigoTipoEmpleado"),
                            tipoEmpleado.getString("descripcion"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }
    
    
    public void seleccionarElementos(){
        if(tblEmpleados.getSelectionModel().getSelectedItem() != null){
            cmbTipoEmpleado.setDisable(false);
            txtCodigoEmpleado.setText(String.valueOf(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getCodigoEmpleado()));
            txtNumeroEmpleado.setText(String.valueOf(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getNumeroEmpleado()));
            txtNombres.setText(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getNombresEmpleado());
            txtApellidos.setText(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getApellidosEmpleado());
            txtDireccion.setText(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getDireccionEmpleado());
            txtTelefono.setText(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getTelefonoContacto());
            txtGradoCocinero.setText(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getGradoCocinero());
            cmbTipoEmpleado.getSelectionModel().select(buscarEmpleado(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado()));
            
        }else{
            JOptionPane.showMessageDialog(null, "Seleccione un campo con datos","Error",JOptionPane.WARNING_MESSAGE);
        }
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
                agregar();
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

    public void agregar(){
        if(!txtNumeroEmpleado.getText().matches("\\d+") || txtNumeroEmpleado.getText().isEmpty() ||txtNombres.getText().isEmpty() || txtApellidos.getText().isEmpty() || txtDireccion.getText().isEmpty()
            || txtTelefono.getText().length() != 8 || !txtTelefono.getText().matches("\\d+") ||txtGradoCocinero.getText().isEmpty() || cmbTipoEmpleado.getSelectionModel().isEmpty()){
                JOptionPane.showMessageDialog(null, "Está ingresando incorrectamente los datos", "Agregar Empleado", JOptionPane.WARNING_MESSAGE);
        }else{
            try{
                Empleado miEmpleado = new Empleado(); 
                miEmpleado.setNumeroEmpleado(Integer.parseInt(txtNumeroEmpleado.getText()));
                miEmpleado.setNombresEmpleado(txtNombres.getText());
                miEmpleado.setApellidosEmpleado(txtApellidos.getText());
                miEmpleado.setDireccionEmpleado(txtDireccion.getText());
                miEmpleado.setTelefonoContacto(txtTelefono.getText());
                miEmpleado.setGradoCocinero(txtGradoCocinero.getText());
                miEmpleado.setCodigoTipoEmpleado(((TipoEmpleado)cmbTipoEmpleado.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado());
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarEmpleado(?,?,?,?,?,?,?)");
                procedimiento.setInt(1, miEmpleado.getNumeroEmpleado());
                procedimiento.setString(2, miEmpleado.getApellidosEmpleado());
                procedimiento.setString(3, miEmpleado.getNombresEmpleado());
                procedimiento.setString(4, miEmpleado.getDireccionEmpleado());
                procedimiento.setString(5, miEmpleado.getTelefonoContacto());
                procedimiento.setString(6, miEmpleado.getGradoCocinero());
                procedimiento.setInt(7, miEmpleado.getCodigoTipoEmpleado());
                procedimiento.execute();
                listaEmpleado.add(miEmpleado);
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
                btnActualizar.setDisable(false);
                btnReporte.setDisable(false);
                imgAgregar.setImage(new Image("org/alexandergonzalez/image/iconoAgregar.png"));
                imgEliminar.setImage(new Image("org/alexandergonzalez/image/iconoEliminar.png"));
                tipoOperacion = operaciones.NINGUNO;
            break;
            default:
                if(tblEmpleados.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Estás seguro de eliminar este campo?", 
                        "Eliminar Empleado", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarEmpleado(?)");
                            procedimiento.setInt(1, ((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getCodigoEmpleado());
                            procedimiento.execute();
                            listaEmpleado.remove(tblEmpleados.getSelectionModel().getSelectedIndex());
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
                if(tblEmpleados.getSelectionModel().getSelectedItem()!= null){
                    btnAgregar.setDisable(true);
                    btnEliminar.setDisable(true);
                    btnActualizar.setText("Finalizar");
                    btnReporte.setText("Cancelar");
                    imgActualizar.setImage(new Image("/org/alexandergonzalez/image/iconoActualizado.png"));
                    imgReporte.setImage(new Image("/org/alexandergonzalez/image/iconoCancelar.png"));
                    activarControles();
                    cmbTipoEmpleado.setDisable(true);
                    txtNumeroEmpleado.clear();
                    txtNombres.clear();
                    txtApellidos.clear();
                    txtDireccion.clear();
                    txtTelefono.clear();
                    txtGradoCocinero.clear();
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
        if(!txtNumeroEmpleado.getText().matches("\\d+") || txtNumeroEmpleado.getText().isEmpty()|| txtNombres.getText().isEmpty() || txtApellidos.getText().isEmpty() || txtDireccion.getText().isEmpty()
            || txtTelefono.getText().length() != 8 || !txtTelefono.getText().matches("\\d+") || txtGradoCocinero.getText().isEmpty()){
                JOptionPane.showMessageDialog(null, "Está ingresando incorrectamente los datos", "Actualizar Empleado", JOptionPane.WARNING_MESSAGE);
        }else{        
            try{
                Empleado miEmpleado = (Empleado)tblEmpleados.getSelectionModel().getSelectedItem();
                miEmpleado.setNumeroEmpleado(Integer.parseInt(txtNumeroEmpleado.getText()));
                miEmpleado.setNombresEmpleado(txtNombres.getText());
                miEmpleado.setApellidosEmpleado(txtApellidos.getText());
                miEmpleado.setDireccionEmpleado(txtDireccion.getText());
                miEmpleado.setTelefonoContacto(txtTelefono.getText());
                miEmpleado.setGradoCocinero(txtGradoCocinero.getText());
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EditarEmpleado(?,?,?,?,?,?,?)");
                procedimiento.setInt(1, miEmpleado.getCodigoEmpleado());
                procedimiento.setInt(2, miEmpleado.getNumeroEmpleado());
                procedimiento.setString(3, miEmpleado.getApellidosEmpleado());
                procedimiento.setString(4, miEmpleado.getNombresEmpleado());
                procedimiento.setString(5, miEmpleado.getDireccionEmpleado());
                procedimiento.setString(6, miEmpleado.getTelefonoContacto());
                procedimiento.setString(7, miEmpleado.getGradoCocinero());
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
        }
    }
    public void limpiarControles(){
        txtCodigoEmpleado.clear();
        txtNumeroEmpleado.clear();
        txtNombres.clear();
        txtApellidos.clear();
        txtDireccion.clear();
        txtTelefono.clear();
        txtGradoCocinero.clear();
        cmbTipoEmpleado.setValue(null);
        tblEmpleados.getSelectionModel().clearSelection(); 
    }
    
    public void activarControles(){
        txtNumeroEmpleado.setEditable(true);
        txtNombres.setEditable(true);
        txtApellidos.setEditable(true);
        txtDireccion.setEditable(true);
        txtTelefono.setEditable(true);
        txtGradoCocinero.setEditable(true);
        cmbTipoEmpleado.setDisable(false);
    }
    public void desactivarControles(){
        txtNumeroEmpleado.setEditable(false);
        txtNombres.setEditable(false);
        txtApellidos.setEditable(false);
        txtDireccion.setEditable(false);
        txtTelefono.setEditable(false);
        txtGradoCocinero.setEditable(false);
        cmbTipoEmpleado.setDisable(true);
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
    public void ventanaTipoEmpleado(){
        escenarioPrincipal.ventanaTipoEmpleado();
    }   
}



