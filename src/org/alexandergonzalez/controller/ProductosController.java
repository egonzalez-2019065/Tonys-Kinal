
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
import org.alexandergonzalez.bean.Producto;
import org.alexandergonzalez.db.Conexion;
import org.alexandergonzalez.main.Principal;

public class ProductosController implements Initializable{
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO}
    private operaciones tipoOperacion = operaciones.NINGUNO;
    private ObservableList<Producto> listaProducto;
    private Principal escenarioPrincipal;
    
    
    
    @FXML private TextField txtCodigoProducto;
    @FXML private TextField txtNombreProducto;
    @FXML private TextField txtCantidad;
    @FXML private TableView tblProductos;
    @FXML private TableColumn colCodProducto;
    @FXML private TableColumn colNombreProducto;
    @FXML private TableColumn colCantidad;
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
        tblProductos.setItems(getProducto());
        colCodProducto.setCellValueFactory(new PropertyValueFactory<Producto, Integer>("codigoProducto"));
        colNombreProducto.setCellValueFactory(new PropertyValueFactory<Producto, String>("nombreProducto"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<Producto, Integer>("cantidad"));
    }
    
    public ObservableList<Producto> getProducto(){
        ArrayList<Producto> lista = new ArrayList<Producto>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarProductos");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Producto(resultado.getInt("codigoProducto"),
                            resultado.getString("nombreProducto"),
                            resultado.getInt("cantidad")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaProducto = FXCollections.observableList(lista);
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
        if(txtNombreProducto.getText().isEmpty() || !txtCantidad.getText().matches("\\d+")){
            JOptionPane.showMessageDialog(null, "Ingrese correctamente los datos", "Guardar Productos", JOptionPane.WARNING_MESSAGE);
        }else{
            Producto producto = new Producto();
            producto.setNombreProducto(txtNombreProducto.getText());
            producto.setCantidad(Integer.parseInt(txtCantidad.getText()));
            try{
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarProducto(?,?)");
                procedimiento.setString(1, producto.getNombreProducto());
                procedimiento.setInt(2, producto.getCantidad());
                procedimiento.execute();
                listaProducto.add(producto);
            }catch(Exception e){
                e.printStackTrace();
            }
        }       
    }

    public void seleccionarElemento(){
        if(tblProductos.getSelectionModel().getSelectedItem() != null){
            txtCodigoProducto.setText(String.valueOf(((Producto)tblProductos.getSelectionModel().getSelectedItem()).getCodigoProducto()));
            txtNombreProducto.setText(((Producto)tblProductos.getSelectionModel().getSelectedItem()).getNombreProducto());
            txtCantidad.setText(String.valueOf(((Producto)tblProductos.getSelectionModel().getSelectedItem()).getCantidad()));
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
                if(tblProductos.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null,"¿Estás seguro de eliminar este campo", 
                            "Eliminar Producto", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarProducto(?)");
                            procedimiento.setInt(1, (((Producto)tblProductos.getSelectionModel().getSelectedItem()).getCodigoProducto()));
                            procedimiento.execute();
                            listaProducto.remove(tblProductos.getSelectionModel().getSelectedIndex());
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
                if(tblProductos.getSelectionModel().getSelectedItem() != null){
                    btnAgregar.setDisable(true);
                    btnEliminar.setDisable(true);
                    btnActualizar.setText("Finalizar");
                    btnReporte.setText("Cancelar");
                    imgActualizar.setImage(new Image("/org/alexandergonzalez/image/iconoActualizado.png"));
                    imgReporte.setImage(new Image("/org/alexandergonzalez/image/iconoCancelar.png"));
                    activarControles();
                    txtNombreProducto.clear();
                    txtCantidad.clear();
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
        if(txtNombreProducto.getText().isEmpty() || !txtCantidad.getText().matches("\\d+")){
            JOptionPane.showMessageDialog(null, "Ingrese correctamente los datos", "Actualizar Productos", JOptionPane.WARNING_MESSAGE);
        }else{
            Producto producto = (Producto)tblProductos.getSelectionModel().getSelectedItem();
            producto.setNombreProducto(txtNombreProducto.getText());
            producto.setCantidad(Integer.parseInt(txtCantidad.getText()));
            try{
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EditarProducto(?,?,?)");
                procedimiento.setInt(1, producto.getCodigoProducto());
                procedimiento.setString(2, producto.getNombreProducto());
                procedimiento.setInt(3, producto.getCantidad());
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
                btnActualizar.setText("Actualizar");
                btnReporte.setText("Reporte");
                btnAgregar.setDisable(false);
                btnEliminar.setDisable(false);
                imgActualizar.setImage(new Image("/org/alexandergonzalez/image/iconoActualizar.png"));
                imgReporte.setImage(new Image("/org/alexandergonzalez/image/iconoReporte.png"));
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
 
    public void desactivarControles(){
        txtCodigoProducto.setEditable(false);
        txtNombreProducto.setEditable(false);
        txtCantidad.setEditable(false);
    }
    
    public void activarControles(){
        txtCodigoProducto.setEditable(false);
        txtNombreProducto.setEditable(true);
        txtCantidad.setEditable(true);
    }
    public void limpiarControles(){
        txtCodigoProducto.clear();
        txtNombreProducto.clear();
        txtCantidad.clear();
        tblProductos.getSelectionModel().clearSelection();
    }
    
}
