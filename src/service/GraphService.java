// service/GraphService.java
package service;

import database.OracleConnection;
import graph.Graph;
import graph.GraphNode;
import graph.GraphEdge;
import model.*;

import java.sql.*;
import java.util.*;

public class GraphService {
    private Graph<Object> grafo;
    private DataLoader dataLoader;
    
    public GraphService(DataLoader dataLoader) {
        this.grafo = new Graph<>();
        this.dataLoader = dataLoader;
    }
    
    // Construir el grafo completo: Cliente → Factura → Detalle → Producto → Marca
    public void construirGrafo() {
        System.out.println("\n==================================================");
        System.out.println("  CONSTRUYENDO GRAFO DIRIGIDO");
        System.out.println("==================================================");
        
        long startTime = System.currentTimeMillis();
        
        // Mapas para guardar referencias a nodos (usando GraphNode<Object>)
        Map<Integer, GraphNode<Object>> clientesNodes = new HashMap<>();
        Map<Integer, GraphNode<Object>> facturasNodes = new HashMap<>();
        Map<Integer, GraphNode<Object>> productosNodes = new HashMap<>();
        Map<Integer, GraphNode<Object>> marcasNodes = new HashMap<>();
        
        try (Connection conn = OracleConnection.getConnection()) {
            
            // 1. Cargar todos los clientes
            String sqlClientes = "SELECT c.ID_CLIENTE, c.NOMBRES, c.APELLIDOS, c.EMAIL, c.TELEFONO, c.DIRECCION, tc.NOMBRE AS TIPO_CLIENTE " +
                                 "FROM CLIENTE c LEFT JOIN TIPO_CLIENTE tc ON c.ID_TIPO_CLIENTE = tc.ID_TIPO_CLIENTE";
            
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sqlClientes)) {
                
                while (rs.next()) {
                    Cliente cliente = new Cliente();
                    cliente.setIdCliente(rs.getInt("ID_CLIENTE"));
                    cliente.setNombres(rs.getString("NOMBRES"));
                    cliente.setApellidos(rs.getString("APELLIDOS"));
                    cliente.setEmail(rs.getString("EMAIL"));
                    cliente.setTelefono(rs.getString("TELEFONO"));
                    cliente.setDireccion(rs.getString("DIRECCION"));
                    cliente.setTipoClienteNombre(rs.getString("TIPO_CLIENTE"));
                    
                    GraphNode<Object> node = grafo.addNode(rs.getInt("ID_CLIENTE"), cliente);
                    clientesNodes.put(cliente.getIdCliente(), node);
                }
            }
            System.out.println("     Clientes cargados: " + clientesNodes.size());
            
            // 2. Cargar facturas y conectar con clientes
            String sqlFacturas = "SELECT f.ID_FACTURA, f.FECHA_FACTURA, f.TOTAL, f.ANIO, f.ID_CLIENTE " +
                                 "FROM FACTURA f";
            
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sqlFacturas)) {
                
                while (rs.next()) {
                    Factura factura = new Factura();
                    factura.setIdFactura(rs.getInt("ID_FACTURA"));
                    factura.setFechaFactura(rs.getDate("FECHA_FACTURA"));
                    factura.setTotal(rs.getDouble("TOTAL"));
                    factura.setAnio(rs.getInt("ANIO"));
                    factura.setIdCliente(rs.getInt("ID_CLIENTE"));
                    
                    // Obtener nombre del cliente
                    GraphNode<Object> clienteNode = clientesNodes.get(factura.getIdCliente());
                    if (clienteNode != null) {
                        Cliente cliente = (Cliente) clienteNode.getData();
                        factura.setClienteNombre(cliente.getNombreCompleto());
                    }
                    
                    GraphNode<Object> facturaNode = grafo.addNode(factura.getIdFactura(), factura);
                    facturasNodes.put(factura.getIdFactura(), facturaNode);
                    
                    // Conectar Cliente → Factura
                    GraphNode<Object> clienteNode2 = clientesNodes.get(factura.getIdCliente());
                    if (clienteNode2 != null) {
                        grafo.addEdge(clienteNode2, facturaNode, "FACTURA");
                    }
                }
            }
            System.out.println("     Facturas cargadas: " + facturasNodes.size());
            
            // 3. Cargar detalles y productos
            String sqlDetalles = "SELECT df.ID_DETALLE, df.ID_FACTURA, df.ID_PRODUCTO, " +
                                 "df.CANTIDAD, df.PRECIO_UNITARIO, df.SUBTOTAL, " +
                                 "p.NOMBRE AS PRODUCTO_NOMBRE, p.DESCRIPCION, p.PRECIO, p.STOCK, " +
                                 "m.ID_MARCA, m.NOMBRE AS MARCA_NOMBRE, m.PAIS_ORIGEN " +
                                 "FROM DETALLE_FACTURA df " +
                                 "JOIN PRODUCTO p ON df.ID_PRODUCTO = p.ID_PRODUCTO " +
                                 "LEFT JOIN MARCA m ON p.ID_MARCA = m.ID_MARCA";
            
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sqlDetalles)) {
                
                while (rs.next()) {
                    int idProducto = rs.getInt("ID_PRODUCTO");
                    int idFactura = rs.getInt("ID_FACTURA");
                    
                    // Crear producto si no existe en el grafo
                    GraphNode<Object> productoNode = productosNodes.get(idProducto);
                    Producto producto;
                    
                    if (productoNode == null) {
                        producto = dataLoader.buscarProducto(idProducto);
                        if (producto == null) {
                            producto = new Producto();
                            producto.setIdProducto(idProducto);
                            producto.setNombre(rs.getString("PRODUCTO_NOMBRE"));
                            producto.setDescripcion(rs.getString("DESCRIPCION"));
                            producto.setPrecio(rs.getDouble("PRECIO"));
                            producto.setStock(rs.getInt("STOCK"));
                        }
                        producto.setMarcaNombre(rs.getString("MARCA_NOMBRE"));
                        
                        productoNode = grafo.addNode(idProducto, producto);
                        productosNodes.put(idProducto, productoNode);
                    } else {
                        producto = (Producto) productoNode.getData();
                    }
                    
                    // Crear marca si no existe
                    int idMarca = rs.getInt("ID_MARCA");
                    if (idMarca > 0 && !marcasNodes.containsKey(idMarca)) {
                        Marca marca = dataLoader.buscarMarca(idMarca);
                        if (marca == null) {
                            marca = new Marca();
                            marca.setIdMarca(idMarca);
                            marca.setNombre(rs.getString("MARCA_NOMBRE"));
                            marca.setPaisOrigen(rs.getString("PAIS_ORIGEN"));
                        }
                        GraphNode<Object> marcaNode = grafo.addNode(idMarca, marca);
                        marcasNodes.put(idMarca, marcaNode);
                        
                        // Conectar Producto → Marca
                        grafo.addEdge(productoNode, marcaNode, "MARCA");
                    }
                    
                    // Crear detalle
                    DetalleFactura detalle = new DetalleFactura();
                    detalle.setIdDetalle(rs.getInt("ID_DETALLE"));
                    detalle.setIdFactura(idFactura);
                    detalle.setIdProducto(idProducto);
                    detalle.setProductoNombre(producto.getNombre());
                    detalle.setCantidad(rs.getInt("CANTIDAD"));
                    detalle.setPrecioUnitario(rs.getDouble("PRECIO_UNITARIO"));
                    detalle.setSubtotal(rs.getDouble("SUBTOTAL"));
                    
                    // Agregar detalle a la factura
                    GraphNode<Object> facturaNode = facturasNodes.get(idFactura);
                    if (facturaNode != null) {
                        Factura factura = (Factura) facturaNode.getData();
                        factura.agregarDetalle(detalle);
                        // Conectar Factura → Producto
                        grafo.addEdge(facturaNode, productoNode, "DETALLE");
                    }
                }
            }
            System.out.println("     Productos cargados: " + productosNodes.size());
            System.out.println("     Marcas cargadas: " + marcasNodes.size());
            
            long endTime = System.currentTimeMillis();
            System.out.println("     Tiempo construcción: " + (endTime - startTime) + " ms");
            System.out.println("     Total nodos en grafo: " + grafo.getSize());
            
        } catch (SQLException e) {
            System.err.println("  Error construyendo grafo: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // Reporte 1: Productos comprados por un cliente en un rango de años
    public void productosPorClienteYRango(int idCliente, int añoInicio, int añoFin) {
        System.out.println("\n==================================================");
        System.out.println("  REPORTE: PRODUCTOS COMPRADOS POR CLIENTE");
        System.out.println("==================================================");
        
        long startTime = System.nanoTime();
        
        GraphNode<Object> clienteNode = grafo.getNode(idCliente);
        if (clienteNode == null || !(clienteNode.getData() instanceof Cliente)) {
            System.out.println("  Cliente no encontrado");
            return;
        }
        
        Cliente cliente = (Cliente) clienteNode.getData();
        System.out.println("   Cliente: " + cliente.getNombreCompleto());
        System.out.println("   Email: " + cliente.getEmail());
        System.out.println("   Rango de años: " + añoInicio + " - " + añoFin);
        System.out.println();
        
        Set<String> productosUnicos = new LinkedHashSet<>();
        Map<String, Integer> productosCantidad = new LinkedHashMap<>();
        
        // Recorrer facturas del cliente
        for (GraphEdge<Object> edge : clienteNode.getEdges()) {
            if ("FACTURA".equals(edge.getRelationType())) {
                GraphNode<Object> facturaNode = edge.getDestination();
                Factura factura = (Factura) facturaNode.getData();
                
                if (factura.getAnio() >= añoInicio && factura.getAnio() <= añoFin) {
                    System.out.println("     FACTURA #" + factura.getIdFactura() + " (Año " + factura.getAnio() + ") - Total: Q" + factura.getTotal());
                    
                    for (DetalleFactura detalle : factura.getDetalles()) {
                        productosUnicos.add(detalle.getProductoNombre());
                        int cantidadActual = productosCantidad.getOrDefault(detalle.getProductoNombre(), 0);
                        productosCantidad.put(detalle.getProductoNombre(), cantidadActual + detalle.getCantidad());
                        System.out.println("      - " + detalle.getCantidad() + "x " + detalle.getProductoNombre() + " (Q" + detalle.getSubtotal() + ")");
                    }
                    System.out.println();
                }
            }
        }
        
        System.out.println("     RESUMEN DE PRODUCTOS ADQUIRIDOS:");
        for (Map.Entry<String, Integer> entry : productosCantidad.entrySet()) {
            System.out.println("      - " + entry.getKey() + ": " + entry.getValue() + " unidades");
        }
        
        long endTime = System.nanoTime();
        System.out.println("\n     Tiempo de consulta: " + ((endTime - startTime) / 1000000) + " ms");
        System.out.println("==================================================");
    }
    
    // Reporte 2: Trazabilidad inversa - Clientes que compraron un producto
    public void clientesQueCompraronProducto(int idProducto) {
        System.out.println("\n==================================================");
        System.out.println("  REPORTE: CLIENTES QUE COMPRARON PRODUCTO");
        System.out.println("==================================================");
        
        long startTime = System.nanoTime();
        
        GraphNode<Object> productoNode = grafo.getNode(idProducto);
        if (productoNode == null || !(productoNode.getData() instanceof Producto)) {
            System.out.println("  Producto no encontrado");
            return;
        }
        
        Producto producto = (Producto) productoNode.getData();
        System.out.println("   Producto: " + producto.getNombre());
        System.out.println("   Precio: Q" + producto.getPrecio());
        System.out.println();
        
        Set<Cliente> clientesEncontrados = new LinkedHashSet<>();
        Map<Cliente, Integer> clienteCantidad = new LinkedHashMap<>();
        
        // Recorrer el grafo para encontrar clientes
        for (GraphNode<Object> node : grafo.getAllNodes()) {
            if (node.getData() instanceof Cliente) {
                Cliente cliente = (Cliente) node.getData();
                int cantidadTotal = 0;
                
                for (GraphEdge<Object> edge1 : node.getEdges()) {
                    if ("FACTURA".equals(edge1.getRelationType())) {
                        GraphNode<Object> facturaNode = edge1.getDestination();
                        Factura factura = (Factura) facturaNode.getData();
                        
                        for (DetalleFactura detalle : factura.getDetalles()) {
                            if (detalle.getIdProducto() == idProducto) {
                                clientesEncontrados.add(cliente);
                                cantidadTotal += detalle.getCantidad();
                            }
                        }
                    }
                }
                
                if (cantidadTotal > 0) {
                    clienteCantidad.put(cliente, cantidadTotal);
                }
            }
        }
        
        System.out.println("    CLIENTES QUE COMPRARON ESTE PRODUCTO:");
        if (clienteCantidad.isEmpty()) {
            System.out.println("        Ningun cliente ha comprado este producto");
        } else {
            for (Map.Entry<Cliente, Integer> entry : clienteCantidad.entrySet()) {
                Cliente c = entry.getKey();
                System.out.println("      - " + c.getNombreCompleto() + " (" + c.getEmail() + ") - " + entry.getValue() + " unidades");
            }
        }
        
        long endTime = System.nanoTime();
        System.out.println("\n     Tiempo de consulta: " + ((endTime - startTime) / 1000000) + " ms");
        System.out.println("==================================================");
    }
    
    // Reporte 3: Recorrido completo Cliente → Factura → Producto → Marca
    public void recorridoClienteFacturaProductoMarca(int idCliente) {
        System.out.println("\n==================================================");
        System.out.println("  REPORTE: RECORRIDO CLIENTE → FACTURA → PRODUCTO → MARCA");
        System.out.println("==================================================");
        
        long startTime = System.nanoTime();
        
        GraphNode<Object> clienteNode = grafo.getNode(idCliente);
        if (clienteNode == null || !(clienteNode.getData() instanceof Cliente)) {
            System.out.println("  Cliente no encontrado");
            return;
        }
        
        Cliente cliente = (Cliente) clienteNode.getData();
        System.out.println("\n     CLIENTE");
        System.out.println("      ID: " + cliente.getIdCliente());
        System.out.println("      Nombre: " + cliente.getNombreCompleto());
        System.out.println("      Email: " + cliente.getEmail());
        System.out.println("      Telefono: " + cliente.getTelefono());
        System.out.println("      Direccion: " + cliente.getDireccion());
        System.out.println("      Tipo: " + cliente.getTipoClienteNombre());
        System.out.println();
        
        int facturaNum = 1;
        boolean tieneFacturas = false;
        
        for (GraphEdge<Object> edge1 : clienteNode.getEdges()) {
            if ("FACTURA".equals(edge1.getRelationType())) {
                tieneFacturas = true;
                GraphNode<Object> facturaNode = edge1.getDestination();
                Factura factura = (Factura) facturaNode.getData();
                
                System.out.println("      FACTURA #" + facturaNum++);
                System.out.println("      ID: " + factura.getIdFactura());
                System.out.println("      Fecha: " + factura.getFechaFactura());
                System.out.println("      Año: " + factura.getAnio());
                System.out.println("      Total: Q" + factura.getTotal());
                System.out.println();
                
                if (factura.getDetalles().isEmpty()) {
                    System.out.println("      (Sin productos en esta factura)");
                    System.out.println();
                } else {
                    int detalleNum = 1;
                    for (DetalleFactura detalle : factura.getDetalles()) {
                        System.out.println("         PRODUCTO #" + detalleNum++);
                        System.out.println("         Nombre: " + detalle.getProductoNombre());
                        System.out.println("         Cantidad: " + detalle.getCantidad());
                        System.out.println("         Precio unitario: Q" + detalle.getPrecioUnitario());
                        System.out.println("         Subtotal: Q" + detalle.getSubtotal());
                        
                        // Buscar marca asociada al producto
                        GraphNode<Object> productoNode = grafo.getNode(detalle.getIdProducto());
                        if (productoNode != null) {
                            for (GraphEdge<Object> edge3 : productoNode.getEdges()) {
                                if ("MARCA".equals(edge3.getRelationType())) {
                                    GraphNode<Object> marcaNode = edge3.getDestination();
                                    Marca marca = (Marca) marcaNode.getData();
                                    System.out.println("         🏷️ MARCA: " + marca.getNombre() + " (" + marca.getPaisOrigen() + ")");
                                }
                            }
                        }
                        System.out.println();
                    }
                }
            }
        }
        
        if (!tieneFacturas) {
            System.out.println("     Este cliente no tiene facturas registradas");
        }
        
        long endTime = System.nanoTime();
        System.out.println("      Tiempo de recorrido: " + ((endTime - startTime) / 1000000) + " ms");
        System.out.println("==================================================");
    }
    
    // Mostrar estructura del grafo
    public void mostrarEstructuraGrafo() {
        grafo.display();
    }
    
    public Graph<Object> getGrafo() {
        return grafo;
    }
}