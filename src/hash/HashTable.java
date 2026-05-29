///HashTable
package hash;

import java.util.ArrayList;
import java.util.List;

public class HashTable<K, V> {
    private static final int DEFAULT_CAPACITY = 101; // Número primo
    private List<HashNode<K, V>> buckets;
    private int size;
    private int colisiones;
    
    public HashTable() {
        this(DEFAULT_CAPACITY);
    }
    
    public HashTable(int capacity) {
        this.buckets = new ArrayList<>(capacity);
        for (int i = 0; i < capacity; i++) {
            buckets.add(null);
        }
        this.size = 0;
        this.colisiones = 0;
    }
    
    // Método de hash: División
    private int getBucketIndex(K key) {
        int hashCode = key.hashCode();
        return Math.abs(hashCode) % buckets.size();
    }
    
    // Insertar o actualizar
    public void put(K key, V value) {
        int index = getBucketIndex(key);
        HashNode<K, V> head = buckets.get(index);
        
        // Verificar si la llave ya existe
        HashNode<K, V> current = head;
        while (current != null) {
            if (current.getKey().equals(key)) {
                current.setValue(value);
                return;
            }
            current = current.getNext();
        }
        
        // Insertar al inicio de la lista (nodo cabeza)
        HashNode<K, V> newNode = new HashNode<>(key, value);
        newNode.setNext(head);
        buckets.set(index, newNode);
        
        // Verificar colisión
        if (head != null) {
            colisiones++;
        }
        
        size++;
    }
    
    // Buscar por llave
    public V get(K key) {
        long startTime = System.nanoTime();
        
        int index = getBucketIndex(key);
        HashNode<K, V> head = buckets.get(index);
        
        HashNode<K, V> current = head;
        int elementosRevisados = 0;
        
        while (current != null) {
            elementosRevisados++;
            if (current.getKey().equals(key)) {
                long endTime = System.nanoTime();
                long tiempoMs = (endTime - startTime) / 1000000;
                System.out.println(" Tiempo busqueda: " + tiempoMs + " ms");
                System.out.println(" Elementos revisados: " + elementosRevisados);
                return current.getValue();
            }
            current = current.getNext();
        }
        
        long endTime = System.nanoTime();
        System.out.println(" Tiempo busqueda (no encontrado): " + 
                          ((endTime - startTime) / 1000000) + " ms");
        return null;
    }
    
    // Eliminar
    public V remove(K key) {
        int index = getBucketIndex(key);
        HashNode<K, V> head = buckets.get(index);
        HashNode<K, V> prev = null;
        HashNode<K, V> current = head;
        
        while (current != null) {
            if (current.getKey().equals(key)) {
                if (prev == null) {
                    buckets.set(index, current.getNext());
                } else {
                    prev.setNext(current.getNext());
                }
                size--;
                return current.getValue();
            }
            prev = current;
            current = current.getNext();
        }
        return null;
    }
    
    public boolean containsKey(K key) {
        return get(key) != null;
    }
    
    public int getSize() { return size; }
    
    public int getColisiones() { return colisiones; }
    
    public boolean isEmpty() { return size == 0; }
    
    // Obtener factor de carga
    public double getLoadFactor() {
        return (double) size / buckets.size();
    }
    
    // Mostrar estadísticas
    public void mostrarEstadisticas() {
        System.out.println(" ESTADiSTICAS DE TABLA HASH");
        System.out.println("   Capacidad: " + buckets.size());
        System.out.println("   Elementos almacenados: " + size);
        System.out.println("   Colisiones: " + colisiones);
        System.out.println("   Factor de carga: " + String.format("%.4f", getLoadFactor()));
        
        // Distribución de buckets
        int maxChainLength = 0;
        int emptyBuckets = 0;
        for (HashNode<K, V> bucket : buckets) {
            int chainLength = 0;
            HashNode<K, V> current = bucket;
            while (current != null) {
                chainLength++;
                current = current.getNext();
            }
            if (chainLength == 0) emptyBuckets++;
            if (chainLength > maxChainLength) maxChainLength = chainLength;
        }
        System.out.println("   Buckets vacios: " + emptyBuckets);
        System.out.println("   Longitud maxima de cadena: " + maxChainLength);
    }
    
    // Obtener todos los valores
    public List<V> getAllValues() {
        List<V> values = new ArrayList<>();
        for (HashNode<K, V> bucket : buckets) {
            HashNode<K, V> current = bucket;
            while (current != null) {
                values.add(current.getValue());
                current = current.getNext();
            }
        }
        return values;
    }
}