// Facturas
package model;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

public class Factura {
    private int idFactura;
    private Date fechaFactura;
    private double total;
    private int anio;
    private int idCliente;
    private String clienteNombre;
    private List<DetalleFactura> detalles;
    
    public Factura() {
        this.detalles = new ArrayList<>();
    }
    
    public Factura(int idFactura, Date fechaFactura, double total, int anio, int idCliente) {
        this.idFactura = idFactura;
        this.fechaFactura = fechaFactura;
        this.total = total;
        this.anio = anio;
        this.idCliente = idCliente;
        this.detalles = new ArrayList<>();
    }
    
    // Getters y Setters
    public int getIdFactura() { return idFactura; }
    public void setIdFactura(int idFactura) { this.idFactura = idFactura; }
    public Date getFechaFactura() { return fechaFactura; }
    public void setFechaFactura(Date fechaFactura) { this.fechaFactura = fechaFactura; }
    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }
    public int getAnio() { return anio; }
    public void setAnio(int anio) { this.anio = anio; }
    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }
    public String getClienteNombre() { return clienteNombre; }
    public void setClienteNombre(String clienteNombre) { this.clienteNombre = clienteNombre; }
    public List<DetalleFactura> getDetalles() { return detalles; }
    public void setDetalles(List<DetalleFactura> detalles) { this.detalles = detalles; }
    
    public void agregarDetalle(DetalleFactura detalle) {
        this.detalles.add(detalle);
    }
    
    @Override
    public String toString() {
        return "Factura [ID=" + idFactura + ", Fecha=" + fechaFactura + 
               ", Total=Q" + total + ", Año=" + anio + ", Cliente=" + clienteNombre + "]";
    }
}