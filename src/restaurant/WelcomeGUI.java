/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package restaurant;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import model.LicenseManager;
import model.MenuManager;

/**
 *
 * @author Admin
 */
public class WelcomeGUI extends javax.swing.JFrame {
    private JMenuBar menuBar;
    private JMenu fileMenu, waiterMenu, menuMenu, configMenu, languageMenu;
    private JMenuItem loadLicenseItem, loadWaiterDataItem, loadMenuOfDayItem, generalConfigItem, catalanMenuItem, spanishMenuItem;
    private JTextArea informationArea;
    private JButton startButton;
    private JTable menuTable;
    private DefaultTableModel menuTableModel;
    private static final String SOURCES_DIRECTORY = Paths.get("src", "sources").toString();
    private static final String CAMBRERS_FILE_PATH = Paths.get(SOURCES_DIRECTORY, "cambrers.csv").toString();
    private boolean isCatalan = true;
    /**
     * Creates new form WelcomeGUI
     */
    public WelcomeGUI() {
        initComponents();
        initializeMenu();
        initializeTable();
        initializeInformationArea();
    }
    
    private void initializeMenu() {
        setTitle("Welcome to Restaurant Simulator");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        menuBar = new JMenuBar();
        
        fileMenu = new JMenu("File");
        waiterMenu = new JMenu("Waiter");
        menuMenu = new JMenu("Menu");
        configMenu = new JMenu("Config");
        languageMenu = new JMenu("Language");
        
        loadLicenseItem = new JMenuItem("Load License File");
        loadWaiterDataItem = new JMenuItem("Load/Add Waiter Data");
        loadMenuOfDayItem = new JMenuItem("Load Menu of the Day");
        generalConfigItem = new JMenuItem("General Configuration");
        catalanMenuItem = new JMenuItem("Català");
        spanishMenuItem = new JMenuItem("Castellano");
        
        fileMenu.add(loadLicenseItem);
        waiterMenu.add(loadWaiterDataItem);
        menuMenu.add(loadMenuOfDayItem);
        configMenu.add(generalConfigItem);
        languageMenu.add(catalanMenuItem);
        languageMenu.add(spanishMenuItem);
        
        menuBar.add(fileMenu);
        menuBar.add(waiterMenu);
        menuBar.add(menuMenu);
        menuBar.add(configMenu);
        menuBar.add(languageMenu);
        
        setJMenuBar(menuBar);
        
        informationArea = new JTextArea();
        informationArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(informationArea);
        
        startButton = new JButton("Start");
        startButton.setEnabled(false);
        
        Container container = getContentPane();
        container.setLayout(new BorderLayout());
        container.add(scrollPane, BorderLayout.CENTER);
        container.add(startButton, BorderLayout.SOUTH);
        
        loadLicenseItem.addActionListener(e -> {showLicenseDialog();});
        loadWaiterDataItem.addActionListener(e -> {showWaiterDialog();});
        loadMenuOfDayItem.addActionListener(e -> {showMenu();});
        generalConfigItem.addActionListener(e -> showGeneralConfigDialog());
        catalanMenuItem.addActionListener(e -> setLanguage(true));
        spanishMenuItem.addActionListener(e -> setLanguage(false));
        
        
    }
    
    private void initializeTable() {
        String[] columnNames = {"Plato", "Precio"};
        menuTableModel = new DefaultTableModel(columnNames, 0);
        menuTable = new JTable(menuTableModel);
        JScrollPane tableScrollPane = new JScrollPane(menuTable);
        Container container = getContentPane();
        container.add(tableScrollPane, BorderLayout.EAST);
    }
    
    private void initializeInformationArea() {
        updateInformationArea(isCatalan ?
                "Benvingut a Restaurant Simulator.\n" +
                "Si us plau, carregui una llicència i les dades del dia per començar.\n" +
                "Seleccioni el seu idioma preferit a 'Idioma'."
                :
                "Bienvenido a Restaurant Simulator.\n" +
                "Por favor, carga una licencia y los datos del día para empezar.\n" +
                "Seleccione su idioma preferido en 'Idioma'.");
    }
    
    private void updateInformationArea(String message) {
        informationArea.setText(message);
    }
    
    private void showWaiterDialog() {
        JDialog waiterDialog = new JDialog(this, "Load/Add Waiter Data", true);
        waiterDialog.setLayout(new FlowLayout());

        JTextField userField = new JTextField(20);
        JPasswordField passwordField = new JPasswordField(20);

        JButton loadButton = new JButton("Load");
        JButton addButton = new JButton("Add");

        waiterDialog.add(new JLabel("User:"));
        waiterDialog.add(userField);
        waiterDialog.add(new JLabel("Password:"));
        waiterDialog.add(passwordField);
        waiterDialog.add(loadButton);
        waiterDialog.add(addButton);

        loadButton.addActionListener(e -> validateWaiter(userField.getText(), new String(passwordField.getPassword()), waiterDialog));
        addButton.addActionListener(e -> {
            try {
                addWaiter(userField.getText(), new String(passwordField.getPassword()), waiterDialog);
            } catch (IOException ex) {
                Logger.getLogger(WelcomeGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        waiterDialog.setSize(300, 200);
        waiterDialog.setVisible(true);
        updateButtonState();
    }
    
    private boolean validateInput(String user, String password) {
        Pattern userPattern = Pattern.compile("^[A-Z].*");
        Pattern passwordPattern = Pattern.compile("^(?=.*[0-9].*[0-9])(?=.*[a-zA-Z].*[a-zA-Z])[0-9a-zA-Z]{8,}$");
        return userPattern.matcher(user).matches() && passwordPattern.matcher(password).matches();
    }
    
    private void validateWaiter(String user, String password, JDialog dialog) {
        String filePath = Paths.get(SOURCES_DIRECTORY, "cambrers.csv").toString();
        
        if (!validateInput(user, password)) {
            JOptionPane.showMessageDialog(dialog, "Invalid user or password format", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isValid = false;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(user) && parts[1].equals(password)) {
                    isValid = true;
                    break;
                }
            }

            if (isValid) {
                JOptionPane.showMessageDialog(dialog, "User validated successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(dialog, "User or password does not match", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void addWaiter(String user, String password, JDialog dialog) throws IOException {
        if (!validateInput(user, password)) {
            JOptionPane.showMessageDialog(dialog, "Invalid user or password format", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try{
            Files.createDirectories(Paths.get(SOURCES_DIRECTORY));
            File file = new File(CAMBRERS_FILE_PATH);
            if(!file.exists()) {
                file.createNewFile();
            }
            try(FileWriter fw = new FileWriter(file, true)){
                String newData = user + "," + password + ",cambrer\n";
                fw.write(newData);
                JOptionPane.showMessageDialog(dialog, isCatalan ? "Cambrer afegit correctament" : "Camarero añadido correctamente", "Exit", JOptionPane.INFORMATION_MESSAGE);
            }
        }catch(IOException e){
            JOptionPane.showMessageDialog(dialog, isCatalan ? "Error en escriure al fitxer: " : "Error al escribir en el archivo: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void setLanguage(boolean isCatalan) {
        this.isCatalan = isCatalan;
    }
    
    private void showLicenseDialog() {
        String[] licenses = {"llicencia1.csv", "llicencia2.csv", "llicencia3.csv"};
        String selectedLicense = (String) JOptionPane.showInputDialog(this, "Select a license", "License Selection", JOptionPane.QUESTION_MESSAGE, null, licenses, licenses[0]);
        if (selectedLicense != null) {
            String resultMessage = LicenseManager.loadAndValidateLicense(selectedLicense);
            JOptionPane.showMessageDialog(this, resultMessage, "License Validation Result", JOptionPane.INFORMATION_MESSAGE);
        }
        updateButtonState();
        updateInformationArea(isCatalan ?
            "Llicència carregada correctament. Ara pots carregar les dades del dia."
            :
            "Licencia cargada correctamente. Ahora puedes cargar los datos del día.");
    }
    
    private void showMenu() {
        String[] menus = {"menu20240310.csv", "menu20240311.csv"};
        String selectedMenu = (String) JOptionPane.showInputDialog(this, "Select a menu", "Menu Selection", JOptionPane.QUESTION_MESSAGE, null, menus, menus[0]);
        if (selectedMenu != null) {
            String menuContent = MenuManager.loadMenuOfDay(selectedMenu);
            informationArea.setText(menuContent);
            updateMenuTable(selectedMenu);
        }
        updateButtonState();
        updateInformationArea(isCatalan ?
            "Menú del dia carregat. Tot llest per començar el dia."
            :
            "Menú del día cargado. Todo listo para empezar el día.");
    }
    
    private void updateMenuTable(String selectedMenu) {
        menuTableModel.setRowCount(0);
        String filePath = Paths.get(SOURCES_DIRECTORY, selectedMenu).toString();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                menuTableModel.addRow(parts);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void showGeneralConfigDialog() {
        JDialog configDialog = new JDialog(this, "Configuración General", true);
        configDialog.setLayout(new GridLayout(0, 2)); // Usamos GridLayout para simplificar

        // Nombre del restaurante
        configDialog.add(new JLabel("Nombre del Restaurante:"));
        JTextField restaurantNameField = new JTextField(20);
        configDialog.add(restaurantNameField);

        // Seleccionar idioma
        configDialog.add(new JLabel("Idioma Preferido:"));
        JComboBox<String> languageComboBox = new JComboBox<>(new String[]{"Català", "Castellano"});
        configDialog.add(languageComboBox);

        // Hora de cierre de la cocina
        configDialog.add(new JLabel("Hora de Cierre (HH:mm):"));
        JTextField closingTimeField = new JTextField("23:00");
        configDialog.add(closingTimeField);

        // Botón para guardar la configuración
        JButton saveButton = new JButton("Guardar");
        saveButton.addActionListener(e -> {
            // Aquí puedes guardar la configuración seleccionada
            // Por ejemplo, ajustar el idioma y guardar el nombre del restaurante y la hora de cierre
            isCatalan = languageComboBox.getSelectedIndex() == 0;
            // Otros ajustes según sea necesario
            configDialog.dispose();
        });
        configDialog.add(saveButton);

        // Ajuste de tamaño y visualización
        configDialog.pack();
        configDialog.setVisible(true);
    }
    
    private boolean areRequirementsMet() {
        boolean licenseIsLoaded = isLicenseLoaded(); // Suponiendo que tienes una función en LicenseManager para verificar si la licencia está cargada
        boolean waiterDataIsLoaded = isWaiterDataLoaded(); // Función para verificar si los datos del camarero están cargados o añadidos
        boolean menuOfDayIsLoaded = isMenuOfDayLoaded(); // Función para verificar si el menú del día está cargado

        return licenseIsLoaded && waiterDataIsLoaded && menuOfDayIsLoaded;
    }
    
    private boolean isLicenseLoaded() {
        // Verifica si existe un archivo de licencia cargado
        String[] licenses = {"llicencia1.csv", "llicencia2.csv", "llicencia3.csv"};
        for (String license : licenses) {
            String licenseFilePath = Paths.get(SOURCES_DIRECTORY, license).toString();
            if (new File(licenseFilePath).exists()) {
                return true;
            }
        }
        return false;
    }
    
    private boolean isWaiterDataLoaded() {
        // Verifica si existe el archivo de camareros cargado
        String filePath = Paths.get(SOURCES_DIRECTORY, "cambrers.csv").toString();
        return new File(filePath).exists();
    }
    
    private boolean isMenuOfDayLoaded() {
        // Verifica si hay al menos una fila en la tabla de menú
        return menuTableModel.getRowCount() > 0;
    }
    
    private void updateButtonState() {
        startButton.setEnabled(areRequirementsMet());
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(WelcomeGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(WelcomeGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(WelcomeGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(WelcomeGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new WelcomeGUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
