// DetalleFactura
package model;

public class DetalleFactura {
    private int idDetalle;
    private int idFactura;
    private int idProducto;
    private String productoNombre;
    private int cantidad;
    private double precioUnitario;
    private double subtotal;
    
    public DetalleFactura() {}
    
    public DetalleFactura(int idDetalle, int idFactura, int idProducto, 
                          int cantidad, double precioUnitario, double subtotal) {
        this.idDetalle = idDetalle;
        this.idFactura = idFactura;
        this.idProducto = idProducto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = subtotal;
    }
    
    // Getters y Setters
    public int getIdDetalle() { return idDetalle; }
    public void setIdDetalle(int idDetalle) { this.idDetalle = idDetalle; }
    public int getIdFactura() { return idFactura; }
    public void setIdFactura(int idFactura) { this.idFactura = idFactura; }
    public int getIdProducto() { return idProducto; }
    public void setIdProducto(int idProducto) { this.idProducto = idProducto; }
    public String getProductoNombre() { return productoNombre; }
    public void setProductoNombre(String productoNombre) { this.productoNombre = productoNombre; }
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    public double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(double precioUnitario) { this.precioUnitario = precioUnitario; }
    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }
    
    @Override
    public String toString() {
        return "  Detalle: " + cantidad + "x " + productoNombre + 
               " @ Q" + precioUnitario + " = Q" + subtotal;
    }
}