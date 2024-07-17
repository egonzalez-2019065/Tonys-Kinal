
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
import org.alexandergonzalez.bean.Producto;
import org.alexandergonzalez.bean.ProductosHasPlatos;
import org.alexandergonzalez.db.Conexion;
import org.alexandergonzalez.main.Principal;

public class ProductosHasPlatosController implements Initializable{
    
    private ObservableList<ProductosHasPlatos> listaProductosPlatos;
    private ObservableList<Producto> listaProductos;
    private ObservableList<Plato> listaPlatos;
    private enum operaciones{GUARDAR, ELIMINAR, NINGUNO}
    private operaciones tipoOperacion = operaciones.NINGUNO;
     
    private Principal escenarioPrincipal;
    
    @FXML private TableView tblProductosPlatos;
    @FXML private TableColumn colCodigo;
    @FXML private TableColumn colProductos;
    @FXML private TableColumn colPlatos;
    @FXML private TextField txtCodigo;
    @FXML private ComboBox cmbProductos;
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
       cmbProductos.setDisable(true);
       cmbPlatos.setDisable(true);
       cmbProductos.setItems(getProducto());
       cmbPlatos.setItems(getPlato());
       tblProductosPlatos.setItems(getProductosPlatos()); 
       colCodigo.setCellValueFactory(new PropertyValueFactory<ProductosHasPlatos, Integer>("productosCodigoProducto"));
       colProductos.setCellValueFactory(new PropertyValueFactory<ProductosHasPlatos, Integer>("codigoProducto"));
       colPlatos.setCellValueFactory(new PropertyValueFactory<ProductosHasPlatos, Integer>("codigoPlato"));
       
       
    }
    
    public ObservableList<ProductosHasPlatos> getProductosPlatos(){
        ArrayList<ProductosHasPlatos> lista =  new ArrayList<ProductosHasPlatos>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarProductos_has_Platos()");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new ProductosHasPlatos(resultado.getInt("Productos_CodigoProducto"),
                        resultado.getInt("codigoProducto"),
                        resultado.getInt("codigoPlato")
                ));
            }   
        }catch(Exception e){
           e.printStackTrace();
        }
        return listaProductosPlatos = FXCollections.observableList(lista);
    }
    
    public Producto buscarProducto(int codigoServicio){
        Producto resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_BuscarProducto(?)");
            procedimiento.setInt(1, codigoServicio);
            ResultSet producto = procedimiento.executeQuery();
            while(producto.next()){
                resultado = (new Producto(producto.getInt("codigoProducto"),
                            producto.getString("nombreProducto"),
                            producto.getInt("cantidad")
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
    
    public void seleccionarElemento(){
        if(tblProductosPlatos.getSelectionModel().getSelectedItem() != null){
            txtCodigo.setText(String.valueOf(((ProductosHasPlatos)tblProductosPlatos.getSelectionModel().getSelectedItem()).getProductosCodigoProducto()));
            cmbProductos.getSelectionModel().select(buscarProducto(((ProductosHasPlatos)tblProductosPlatos.getSelectionModel().getSelectedItem()).getCodigoProducto()));
            cmbPlatos.getSelectionModel().select(buscarPlato(((ProductosHasPlatos)tblProductosPlatos.getSelectionModel().getSelectedItem()).getCodigoPlato()));
        }else{
            JOptionPane.showMessageDialog(null, "Seleccione un campo con datos","Error",JOptionPane.WARNING_MESSAGE);
        }
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
        return listaProductos = FXCollections.observableList(lista);
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
        if(txtCodigo.getText().isEmpty() || !txtCodigo.getText().matches("\\d+") || cmbProductos.getSelectionModel().isEmpty() ||
              cmbPlatos.getSelectionModel().isEmpty()){
            JOptionPane.showMessageDialog(null, "Está ingresando incorrectamente los datos", "Guardar Productos y sus Platos",JOptionPane.WARNING_MESSAGE);
        }else{
            try{
                ProductosHasPlatos miProductoPlato = new ProductosHasPlatos();
                miProductoPlato.setProductosCodigoProducto(Integer.parseInt(txtCodigo.getText()));
                miProductoPlato.setCodigoProducto(((Producto)cmbProductos.getSelectionModel().getSelectedItem()).getCodigoProducto());
                miProductoPlato.setCodigoPlato(((Plato)cmbPlatos.getSelectionModel().getSelectedItem()).getCodigoPlato());  
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarProductos_has_Platos(?,?,?)");
                procedimiento.setInt(1, miProductoPlato.getProductosCodigoProducto());
                procedimiento.setInt(2, miProductoPlato.getCodigoPlato());
                procedimiento.setInt(3, miProductoPlato.getCodigoProducto());
                procedimiento.execute();
                listaProductosPlatos.add(miProductoPlato);
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
//                if(tblProductosPlatos.getSelectionModel().getSelectedItem() != null){
//                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Estás seguro de eliminar este campo?",
//                            "Eliminar Productos y sus Platos", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
//                    if(respuesta == JOptionPane.YES_OPTION){
//                        try{
//                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarProductos_has_Platos(?)");
//                            procedimiento.setInt(1, ((ProductosHasPlatos)tblProductosPlatos.getSelectionModel().getSelectedItem()).getProductosCodigoProducto());
//                            procedimiento.execute();
//                            listaProductosPlatos.remove(tblProductosPlatos.getSelectionModel().getSelectedIndex());
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
        cmbProductos.setDisable(false);
        cmbPlatos.setDisable(false);
    }
    public void desactivarControles(){
        txtCodigo.setEditable(false);
        cmbProductos.setDisable(true);
        cmbPlatos.setDisable(true);
    }
    public void limpiarControles(){
        txtCodigo.clear();
        cmbProductos.setValue(null);
        cmbPlatos.setValue(null);
        tblProductosPlatos.getSelectionModel().clearSelection();
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
    public void ventanaProductos(){
        escenarioPrincipal.ventanaProductos();
    }
}
