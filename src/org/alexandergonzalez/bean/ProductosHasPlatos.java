package org.alexandergonzalez.bean;

public class ProductosHasPlatos {
    private int productosCodigoProducto;
    private int codigoPlato;
    private int codigoProducto;

    public ProductosHasPlatos() {
    }

    public ProductosHasPlatos(int productosCodigoProducto, int codigoPlato, int codigoProducto) {
        this.productosCodigoProducto = productosCodigoProducto;
        this.codigoPlato = codigoPlato;
        this.codigoProducto = codigoProducto;
    }
    
    public int getProductosCodigoProducto() {
        return productosCodigoProducto;
    }

    public void setProductosCodigoProducto(int productosCodigoProducto) {
        this.productosCodigoProducto = productosCodigoProducto;
    }

    public int getCodigoPlato() {
        return codigoPlato;
    }

    public void setCodigoPlato(int codigoPlato) {
        this.codigoPlato = codigoPlato;
    }

    public int getCodigoProducto() {
        return codigoProducto;
    }

    public void setCodigoProducto(int codigoProducto) {
        this.codigoProducto = codigoProducto;
    }
    
    
    
    
}
