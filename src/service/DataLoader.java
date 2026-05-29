/// DataLoader
package service;

import database.OracleConnection;
import model.*;
import hash.HashTable;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataLoader {
    
    // Tablas hash para catálogos
    private HashTable<Integer, Producto> tablaProductos;
    private HashTable<Integer, Marca> tablaMarcas;
    private HashTable<Integer, String> tablaTiposCliente;
    
    public DataLoader() {
        this.tablaProductos = new HashTable<>();
        this.tablaMarcas = new HashTable<>();
        this.tablaTiposCliente = new HashTable<>();
    }
    
    // Cargar todos los catálogos a las tablas hash
    public void cargarCatalogos() {
        System.out.println("\n==================================================");
        System.out.println(" CARGANDO CATALOGOS A TABLAS HASH");
        System.out.println("==================================================");
        
        long startTime = System.currentTimeMillis();
        
        cargarProductos();
        cargarMarcas();
        cargarTiposCliente();
        
        long endTime = System.currentTimeMillis();
        
        System.out.println("\n RESUMEN DE CARGA:");
        System.out.println("   Productos: " + tablaProductos.getSize());
        System.out.println("   Marcas: " + tablaMarcas.getSize());
        System.out.println("   Tipos Cliente: " + tablaTiposCliente.getSize());
        System.out.println("   Tiempo total de carga: " + (endTime - startTime) + " ms");
        
        tablaProductos.mostrarEstadisticas();
    }
    
    private void cargarProductos() {
        String sql = "SELECT p.ID_PRODUCTO, p.NOMBRE, p.DESCRIPCION, p.PRECIO, p.STOCK, p.ID_MARCA, m.NOMBRE AS MARCA_NOMBRE " +
                     "FROM PRODUCTO p LEFT JOIN MARCA m ON p.ID_MARCA = m.ID_MARCA";
        
        try (Connection conn = OracleConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Producto producto = new Producto();
                producto.setIdProducto(rs.getInt("ID_PRODUCTO"));
                producto.setNombre(rs.getString("NOMBRE"));
                producto.setDescripcion(rs.getString("DESCRIPCION"));
                producto.setPrecio(rs.getDouble("PRECIO"));
                producto.setStock(rs.getInt("STOCK"));
                producto.setIdMarca(rs.getInt("ID_MARCA"));
                producto.setMarcaNombre(rs.getString("MARCA_NOMBRE"));
                
                tablaProductos.put(producto.getIdProducto(), producto);
            }
            System.out.println(" Productos cargados: " + tablaProductos.getSize());
            
        } catch (SQLException e) {
            System.err.println(" Error cargando productos: " + e.getMessage());
        }
    }
    
    private void cargarMarcas() {
        String sql = "SELECT ID_MARCA, NOMBRE, PAIS_ORIGEN FROM MARCA";
        
        try (Connection conn = OracleConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Marca marca = new Marca();
                marca.setIdMarca(rs.getInt("ID_MARCA"));
                marca.setNombre(rs.getString("NOMBRE"));
                marca.setPaisOrigen(rs.getString("PAIS_ORIGEN"));
                
                tablaMarcas.put(marca.getIdMarca(), marca);
            }
            System.out.println(" Marcas cargadas: " + tablaMarcas.getSize());
            
        } catch (SQLException e) {
            System.err.println(" Error cargando marcas: " + e.getMessage());
        }
    }
    
    private void cargarTiposCliente() {
        String sql = "SELECT ID_TIPO_CLIENTE, NOMBRE FROM TIPO_CLIENTE";
        
        try (Connection conn = OracleConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                String tipoNombre = rs.getString("NOMBRE");
                tablaTiposCliente.put(rs.getInt("ID_TIPO_CLIENTE"), tipoNombre);
            }
            System.out.println(" Tipos Cliente cargados: " + tablaTiposCliente.getSize());
            
        } catch (SQLException e) {
            System.err.println(" Error cargando tipos cliente: " + e.getMessage());
        }
    }
    
    // Buscar producto por ID usando tabla hash
    public Producto buscarProducto(int idProducto) {
        System.out.println("\n Buscando producto ID: " + idProducto);
        return tablaProductos.get(idProducto);
    }
    
    // Buscar marca por ID usando tabla hash
    public Marca buscarMarca(int idMarca) {
        System.out.println("\n Buscando marca ID: " + idMarca);
        return tablaMarcas.get(idMarca);
    }
    
    // Buscar tipo cliente por ID usando tabla hash
    public String buscarTipoCliente(int idTipoCliente) {
        System.out.println("\n Buscando tipo cliente ID: " + idTipoCliente);
        return tablaTiposCliente.get(idTipoCliente);
    }
    
    // Obtener todos los productos de la tabla hash
    public List<Producto> getAllProductos() {
        return tablaProductos.getAllValues();
    }
    
    // Obtener todas las marcas
    public List<Marca> getAllMarcas() {
        return tablaMarcas.getAllValues();
    }
    
    // Getters
    public HashTable<Integer, Producto> getTablaProductos() { return tablaProductos; }
    public HashTable<Integer, Marca> getTablaMarcas() { return tablaMarcas; }
    public HashTable<Integer, String> getTablaTiposCliente() { return tablaTiposCliente; }
}