/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;

/**
 *
 * @author Admin
 */
public class MenuManager {
    private static final String MENU_DIRECTORY = Paths.get("src", "sources").toString();
    
    public static String loadMenuOfDay(String menuFileName) {
        String menuContent = "";
        String filePath = Paths.get(MENU_DIRECTORY, menuFileName).toString();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            StringBuilder menuBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                menuBuilder.append(line).append("\n");
            }
            menuContent = menuBuilder.toString();
        } catch (IOException e) {
            System.err.println("Error reading menu file: " + e.getMessage());
        }
        return menuContent;
    }
}
