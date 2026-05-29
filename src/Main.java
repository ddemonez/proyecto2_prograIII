// Main.java
import database.OracleConnection;
import service.DataLoader;
import model.*;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println(" SISTEMA DE GESTION COMERCIAL");
        System.out.println("   Tablas Hash y Grafos");
        
        DataLoader dataLoader = new DataLoader();
        
        try {
            // Probar conexión
            OracleConnection.getConnection();
            
            // Cargar catálogos a tablas hash
            dataLoader.cargarCatalogos();
            
            // Menú interactivo
            Scanner scanner = new Scanner(System.in);
            int opcion;
            
            do {
                System.out.println("\n==================================================");
                System.out.println(" MENU PRINCIPAL");
                System.out.println("==================================================");
                System.out.println("1. Buscar producto por ID");
                System.out.println("2. Buscar marca por ID");
                System.out.println("3. Buscar tipo de cliente por ID");
                System.out.println("4. Mostrar todos los productos");
                System.out.println("5. Mostrar estadisticas de tablas hash");
                System.out.println("6. Salir");
                System.out.print("\nSeleccione una opcion: ");
                
                opcion = scanner.nextInt();
                
                switch (opcion) {
                    case 1:
                        System.out.print("Ingrese ID del producto: ");
                        int idProducto = scanner.nextInt();
                        Producto producto = dataLoader.buscarProducto(idProducto);
                        if (producto != null) {
                            System.out.println(" Producto encontrado: " + producto);
                        } else {
                            System.out.println(" Producto no encontrado");
                        }
                        break;
                        
                    case 2:
                        System.out.print("Ingrese ID de la marca: ");
                        int idMarca = scanner.nextInt();
                        Marca marca = dataLoader.buscarMarca(idMarca);
                        if (marca != null) {
                            System.out.println(" Marca encontrada: " + marca);
                        } else {
                            System.out.println(" Marca no encontrada");
                        }
                        break;
                        
                    case 3:
                        System.out.print("Ingrese ID del tipo cliente: ");
                        int idTipo = scanner.nextInt();
                        String tipo = dataLoader.buscarTipoCliente(idTipo);
                        if (tipo != null) {
                            System.out.println(" Tipo cliente encontrado: " + tipo);
                        } else {
                            System.out.println(" Tipo cliente no encontrado");
                        }
                        break;
                        
                    case 4:
                        System.out.println("\n LISTA DE PRODUCTOS:");
                        List<Producto> productos = dataLoader.getAllProductos();
                        for (Producto p : productos) {
                            System.out.println("   " + p);
                        }
                        break;
                        
                    case 5:
                        dataLoader.getTablaProductos().mostrarEstadisticas();
                        break;
                        
                    case 6:
                        System.out.println(" Saliendo del sistema...");
                        break;
                        
                    default:
                        System.out.println(" Opción invalida");
                }
                
            } while (opcion != 6);
            
            scanner.close();
            
        } catch (Exception e) {
            System.err.println(" Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            OracleConnection.closeConnection();
        }
    }
}