/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package restaurant.simulator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Admin
 */
public class DataManager {
    private static final String CSV_DIRECTORY = "sources";
    private Map<String, String> trabajadores;
    private Map<String, String> licenciasValides;
    private Map<String, String> fechasExpiracion;
    
    public DataManager() {
        this.trabajadores = new HashMap<>();
        this.licenciasValides = new HashMap<>();
        this.fechasExpiracion = new HashMap<>();
        cargarTrabajadores("treballador.csv");
        cargarLicenciasValides("llicenciesValides.csv");
        cargarFechasExpiracion("llicencia1.csv");
        cargarFechasExpiracion("llicencia2.csv");
        cargarFechasExpiracion("llicencia3.csv");
    }
    
    private void cargarTrabajadores(String filename) {
        Path filePath = Paths.get(CSV_DIRECTORY, filename);
        try (BufferedReader br = new BufferedReader(new FileReader(filePath.toString()))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    trabajadores.put(parts[0], parts[2]); // Nombre -> Rol
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void cargarLicenciasValides(String filename) {
        Path filePath = Paths.get(CSV_DIRECTORY, filename);
        try (BufferedReader br = new BufferedReader(new FileReader(filePath.toString()))) {
            String line;
            while ((line = br.readLine()) != null) {
                licenciasValides.put(line, line); // Licencia válida
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void cargarFechasExpiracion(String filename) {
        Path filePath = Paths.get(CSV_DIRECTORY, filename);
        try (BufferedReader br = new BufferedReader(new FileReader(filePath.toString()))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    fechasExpiracion.put(parts[0], parts[1]); // Licencia -> Fecha de expiración
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
