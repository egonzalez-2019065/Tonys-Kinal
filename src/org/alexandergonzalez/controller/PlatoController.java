
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
import org.alexandergonzalez.bean.TipoPlato;
import org.alexandergonzalez.db.Conexion;
import org.alexandergonzalez.main.Principal;

public class PlatoController implements Initializable {

    private ObservableList<Plato> listaPlatos;
    private ObservableList<TipoPlato> listaTipoPlato;
    private enum operaciones{GUARDAR, ELIMINAR, ACTUALIZAR,NINGUNO}
    private operaciones tipoOperacion = operaciones.NINGUNO;

    
    @FXML private TableView tblPlatos;
    @FXML private TableColumn colCodigo;
    @FXML private TableColumn colCantidad;
    @FXML private TableColumn colNombre;
    @FXML private TableColumn colDescripcion;
    @FXML private TableColumn colPrecio;
    @FXML private TableColumn colTipoPlato;
    @FXML private ComboBox cmbPlato;
    @FXML private TextField txtCodigo;
    @FXML private TextField txtCantidad;
    @FXML private TextField txtNombre;
    @FXML private TextField txtPrecio;
    @FXML private TextField txtDescripcion;
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
        cmbPlato.setItems(getTipoPlato());
        tblPlatos.setItems(getPlato());
        colCodigo.setCellValueFactory(new PropertyValueFactory<Plato, Integer>("codigoPlato"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<Plato, Integer>("cantidadPlato"));
        colNombre.setCellValueFactory(new PropertyValueFactory<Plato, String>("nombrePlato"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<Plato, String>("descripcionPlato"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<Plato, Double>("precioPlato"));
        colTipoPlato.setCellValueFactory(new PropertyValueFactory<Plato, Integer>("codigoTipoPlato"));
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
        return listaPlatos = FXCollections.observableList(lista);
    }
    public TipoPlato buscarTipoPlato(int codigoTipoPlato){
        TipoPlato resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_BuscarTipoPlato(?)");
            procedimiento.setInt(1, codigoTipoPlato);
            ResultSet tipoPlato = procedimiento.executeQuery();
            while(tipoPlato.next()){
                resultado = new TipoPlato(tipoPlato.getInt("codigoTipoPlato"),
                            tipoPlato.getString("descripcion"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }
    public void seleccionarElementos(){
        if(tblPlatos.getSelectionModel().getSelectedItem() != null){
           txtCodigo.setText(String.valueOf(((Plato)tblPlatos.getSelectionModel().getSelectedItem()).getCodigoPlato()));
           txtCantidad.setText(String.valueOf(((Plato)tblPlatos.getSelectionModel().getSelectedItem()).getCantidadPlato()));
           txtNombre.setText(((Plato)tblPlatos.getSelectionModel().getSelectedItem()).getNombrePlato());
           txtDescripcion.setText(((Plato)tblPlatos.getSelectionModel().getSelectedItem()).getDescripcionPlato());
           txtPrecio.setText(String.valueOf(((Plato)tblPlatos.getSelectionModel().getSelectedItem()).getCodigoPlato()));
           cmbPlato.getSelectionModel().select(buscarTipoPlato(((Plato)tblPlatos.getSelectionModel().getSelectedItem()).getCodigoTipoPlato()));
        }else{
            JOptionPane.showMessageDialog(null, "Seleccione un campo con datos","Error",JOptionPane.WARNING_MESSAGE);
        }
    }
    public ObservableList<TipoPlato> getTipoPlato(){
        ArrayList<TipoPlato> lista = new ArrayList<TipoPlato>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarTipoPlatos");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new TipoPlato(resultado.getInt("codigoTipoPlato"),
                            resultado.getString("descripcion")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaTipoPlato = FXCollections.observableList(lista);
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
        if(!txtCantidad.getText().matches("\\d+") || txtCantidad.getText().isEmpty() || txtNombre.getText().isEmpty() || txtDescripcion.getText().isEmpty() ||
            txtPrecio.getText().isEmpty() || cmbPlato.getSelectionModel().isEmpty()){
            JOptionPane.showMessageDialog(null, "Está ingresando incorrectamente los datos", "Agregar Plato", JOptionPane.WARNING_MESSAGE);
        }else{
            Plato miPlato = new Plato();
            miPlato.setCantidadPlato(Integer.parseInt(txtCantidad.getText()));
            miPlato.setNombrePlato(txtNombre.getText());
            miPlato.setDescripcionPlato(txtDescripcion.getText());
            miPlato.setPrecioPlato(Double.parseDouble(txtPrecio.getText()));
            miPlato.setCodigoTipoPlato(((TipoPlato)cmbPlato.getSelectionModel().getSelectedItem()).getCodigoTipoPlato());
            try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarPlato(?,?,?,?,?)");
            procedimiento.setInt(1, miPlato.getCantidadPlato());
            procedimiento.setString(2, miPlato.getNombrePlato());
            procedimiento.setString(3, miPlato.getDescripcionPlato());
            procedimiento.setDouble(4, miPlato.getPrecioPlato());
            procedimiento.setDouble(5, miPlato.getCodigoTipoPlato());
            procedimiento.execute();
            listaPlatos.add(miPlato);
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
                if(tblPlatos.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Estás seguro de eliminar este campo?", 
                        "Eliminar Plato", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarPlato(?)");
                            procedimiento.setInt(1, ((Plato)tblPlatos.getSelectionModel().getSelectedItem()).getCodigoPlato());
                            procedimiento.execute();
                            listaPlatos.remove(tblPlatos.getSelectionModel().getSelectedIndex());
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
                if(tblPlatos.getSelectionModel().getSelectedItem()!= null){
                    btnAgregar.setDisable(true);
                    btnEliminar.setDisable(true);
                    btnActualizar.setText("Finalizar");
                    btnReporte.setText("Cancelar");
                    imgActualizar.setImage(new Image("/org/alexandergonzalez/image/iconoActualizado.png"));
                    imgReporte.setImage(new Image("/org/alexandergonzalez/image/iconoCancelar.png"));
                    activarControles();
                    cmbPlato.setDisable(true);
                    txtCantidad.clear();
                    txtNombre.clear();
                    txtDescripcion.clear();
                    txtPrecio.clear();
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
        if(!txtCantidad.getText().matches("\\d+") || txtCantidad.getText().isEmpty() || txtNombre.getText().isEmpty() || txtDescripcion.getText().isEmpty() ||
            txtPrecio.getText().isEmpty() || cmbPlato.getSelectionModel().isEmpty()){
            JOptionPane.showMessageDialog(null, "Está ingresando incorrectamente los datos", "Actualizar Plato", JOptionPane.WARNING_MESSAGE);
        }else{
            Plato miPlato = (Plato)tblPlatos.getSelectionModel().getSelectedItem();
            miPlato.setCantidadPlato(Integer.parseInt(txtCantidad.getText()));
            miPlato.setNombrePlato(txtNombre.getText());
            miPlato.setDescripcionPlato(txtDescripcion.getText());
            miPlato.setPrecioPlato(Double.parseDouble(txtPrecio.getText()));
            miPlato.setCodigoTipoPlato(((TipoPlato)cmbPlato.getSelectionModel().getSelectedItem()).getCodigoTipoPlato());
            try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EditarPlato(?,?,?,?,?)");
            procedimiento.setInt(1, miPlato.getCodigoPlato());
            procedimiento.setInt(2, miPlato.getCantidadPlato());
            procedimiento.setString(3, miPlato.getNombrePlato());
            procedimiento.setString(4, miPlato.getDescripcionPlato());
            procedimiento.setDouble(5, miPlato.getPrecioPlato());
            procedimiento.execute();
            listaPlatos.add(miPlato);
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
    public void activarControles(){
        txtCantidad.setEditable(true);
        txtNombre.setEditable(true);
        txtDescripcion.setEditable(true);
        txtPrecio.setEditable(true);
        cmbPlato.setDisable(false);
    }
    public void desactivarControles(){
        txtCantidad.setEditable(false);
        txtNombre.setEditable(false);
        txtDescripcion.setEditable(false);
        txtPrecio.setEditable(false);
        cmbPlato.setDisable(true);
    }
    public void limpiarControles(){
        txtCodigo.clear();
        txtCantidad.clear();
        txtNombre.clear();
        txtDescripcion.clear();
        txtPrecio.clear();
        cmbPlato.setValue(null);
        tblPlatos.getSelectionModel().clearSelection();
    }
    private Principal escenarioPrincipal; 

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
    public void ventanaTipoPlato(){
        escenarioPrincipal.ventanaTipoPlato();
    }
}
