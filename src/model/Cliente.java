// clientes 
package model;

public class Cliente {
    private int idCliente;
    private String nombres;
    private String apellidos;
    private String telefono;
    private String email;
    private String direccion;
    private int idTipoCliente;
    private String tipoClienteNombre;
    
    public Cliente() {}
    
    public Cliente(int idCliente, String nombres, String apellidos, String telefono, 
                   String email, String direccion, int idTipoCliente) {
        this.idCliente = idCliente;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.telefono = telefono;
        this.email = email;
        this.direccion = direccion;
        this.idTipoCliente = idTipoCliente;
    }
    
    // Getters y Setters
    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }
    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }
    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public int getIdTipoCliente() { return idTipoCliente; }
    public void setIdTipoCliente(int idTipoCliente) { this.idTipoCliente = idTipoCliente; }
    public String getTipoClienteNombre() { return tipoClienteNombre; }
    public void setTipoClienteNombre(String tipoClienteNombre) { this.tipoClienteNombre = tipoClienteNombre; }
    
    public String getNombreCompleto() {
        return nombres + " " + apellidos;
    }
    
    @Override
    public String toString() {
        return "Cliente [ID=" + idCliente + ", Nombre=" + getNombreCompleto() + 
               ", Email=" + email + ", Tipo=" + tipoClienteNombre + "]";
    }
}