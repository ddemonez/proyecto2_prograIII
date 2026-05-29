// productos
package model;

public class Producto {
    private int idProducto;
    private String nombre;
    private String descripcion;
    private double precio;
    private int stock;
    private int idMarca;
    private String marcaNombre;
    
    public Producto() {}
    
    public Producto(int idProducto, String nombre, String descripcion, 
                    double precio, int stock, int idMarca) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
        this.idMarca = idMarca;
    }
    
    // Getters y Setters
    public int getIdProducto() { return idProducto; }
    public void setIdProducto(int idProducto) { this.idProducto = idProducto; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
    public int getIdMarca() { return idMarca; }
    public void setIdMarca(int idMarca) { this.idMarca = idMarca; }
    public String getMarcaNombre() { return marcaNombre; }
    public void setMarcaNombre(String marcaNombre) { this.marcaNombre = marcaNombre; }
    
    @Override
    public String toString() {
        return "Producto [ID=" + idProducto + ", Nombre=" + nombre + 
               ", Precio=Q" + precio + ", Stock=" + stock + ", Marca=" + marcaNombre + "]";
    }
}