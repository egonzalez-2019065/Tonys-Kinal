
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
import org.alexandergonzalez.bean.TipoPlato;
import org.alexandergonzalez.db.Conexion;
import org.alexandergonzalez.main.Principal;


public class TipoPlatoController implements Initializable {
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO}
    private operaciones tipoOperacion = operaciones.NINGUNO;
    private ObservableList<TipoPlato>  listaTipoPlato;
    
    
    private Principal escenarioPrincipal;
    
    
    @FXML private TextField txtCodTipoPlato;
    @FXML private TextField txtTipoPlato;
    @FXML private TableView tblTipoPlatos;
    @FXML private TableColumn colCodTipoPlato;
    @FXML private TableColumn colPlato;
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
        tblTipoPlatos.setItems(getTipoPlato());
        colCodTipoPlato.setCellValueFactory(new PropertyValueFactory<TipoPlato, Integer>("codigoTipoPlato"));
        colPlato.setCellValueFactory(new PropertyValueFactory<TipoPlato, String>("descripcion"));
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
            break;
        }
    } 
    
    public void guardar(){
        if(txtTipoPlato.getText().isEmpty() || txtTipoPlato.getText().matches("\\d+")){
           JOptionPane.showMessageDialog(null, "Ingrese correctamente los datos", "Guardar Tipo Plato", JOptionPane.WARNING_MESSAGE);
        }else{
            TipoPlato platillo = new TipoPlato();
            platillo.setDescripcion(txtTipoPlato.getText());
            try{
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarTipoPlato(?)");
                procedimiento.setString(1, platillo.getDescripcion());
                procedimiento.execute();
                listaTipoPlato.add(platillo);
            }catch(Exception e){
                e.printStackTrace();
            }  
        }
    }
    
    public void seleccionarElementos(){
        if(tblTipoPlatos.getSelectionModel().getSelectedItem() != null){
            txtCodTipoPlato.setText(String.valueOf(((TipoPlato)tblTipoPlatos.getSelectionModel().getSelectedItem()).getCodigoTipoPlato()));
            txtTipoPlato.setText(((TipoPlato)tblTipoPlatos.getSelectionModel().getSelectedItem()).getDescripcion());
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
                if(tblTipoPlatos.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Estás seguro de eliminar este campo",
                            "Eliminar Tipo de Plato", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarTipoPlato(?)");
                            procedimiento.setInt(1, (((TipoPlato)tblTipoPlatos.getSelectionModel().getSelectedItem()).getCodigoTipoPlato()));
                            procedimiento.execute();
                            listaTipoPlato.remove(tblTipoPlatos.getSelectionModel().getSelectedIndex());
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                        limpiarControles();
                    }else{
                        limpiarControles();
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "Seleccione un campo con datos", "Error", JOptionPane.WARNING_MESSAGE);
                }
            break;
        }
    }
    public void editar(){
        switch(tipoOperacion){
            case NINGUNO:
                if(tblTipoPlatos.getSelectionModel().getSelectedItem() != null){
                    activarControles();
                    btnActualizar.setText("Finalizar");
                    btnReporte.setText("Cancelar");
                    btnAgregar.setDisable(true);
                    btnEliminar.setDisable(true);
                    imgActualizar.setImage(new Image("org/alexandergonzalez/image/iconoActualizado.png"));
                    imgReporte.setImage(new Image("org/alexandergonzalez/image/iconoCancelar.png"));
                    txtTipoPlato.clear();
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
        }
    }
    public void actualizar(){
        if(txtTipoPlato.getText().isEmpty() || txtTipoPlato.getText().matches("\\d+")){
            JOptionPane.showMessageDialog(null, "Ingrese correctamente los datos", "Acttualizar Tipo Plato", JOptionPane.WARNING_MESSAGE);
        }else{
            TipoPlato tipoPlato = (TipoPlato) tblTipoPlatos.getSelectionModel().getSelectedItem();
            tipoPlato.setDescripcion(txtTipoPlato.getText());
            try{
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EditarTipoPlato(?,?)");
                procedimiento.setInt(1, tipoPlato.getCodigoTipoPlato());
                procedimiento.setString(2, tipoPlato.getDescripcion());
                procedimiento.execute();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        
    }
    public void reporte(){
        switch(tipoOperacion){
            case ACTUALIZAR:
                activarControles();
                limpiarControles();
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
    public void ventanaPlato(){
        escenarioPrincipal.ventanaPlato();
    }
    public void desactivarControles(){
        txtCodTipoPlato.setEditable(false);
        txtTipoPlato.setEditable(false);
    }
    public void activarControles(){
        txtCodTipoPlato.setEditable(false);
        txtTipoPlato.setEditable(true);
    }
    public void limpiarControles(){
        txtCodTipoPlato.clear();
        txtTipoPlato.clear();
        tblTipoPlatos.getSelectionModel().clearSelection();
    }
}
