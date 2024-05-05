/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package restaurant;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author Admin
 */
public class WaiterGUI extends javax.swing.JFrame {
    private JPanel tablePanel;
    private JButton[] tableButtons;
    private boolean[] tablePaidStatus = new boolean[9];
    private Color[] originalColors = new Color[9];

    /**
     * Creates new form WaiterGUI
     */
    public WaiterGUI() {
        setTitle("Waiter GUI");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        
        for(int i=0;i<tablePaidStatus.length;i++){
            tablePaidStatus[i] = true;
        }
        
        tablePanel = new JPanel(new GridLayout(3, 3));
        tableButtons = new JButton[9];
        
        for (int i = 0; i < tableButtons.length; i++) {
            tableButtons[i] = new JButton("Table " + (i + 1));
            originalColors[i] = tableButtons[i].getBackground();
            updateButtonColor(tableButtons[i], i);
            tableButtons[i].addActionListener(new TableButtonListener(i, tableButtons[i]));
            tablePanel.add(tableButtons[i]);
        }
        
        Container container = getContentPane();
        container.add(tablePanel, BorderLayout.CENTER);
    }
    
    private void updateButtonColor(JButton button, int tableIndex){
        if(tablePaidStatus[tableIndex]){
            button.setBackground(Color.GREEN);
        }else {
            button.setBackground(Color.RED);
        }
    }
    
    private class TableButtonListener implements ActionListener {
        private int tableIndex;
        private JButton button;

        public TableButtonListener(int tableIndex, JButton button){
            this.tableIndex = tableIndex;
            this.button = button;
        }

        @Override
        public void actionPerformed(ActionEvent e){
            if (tablePaidStatus[tableIndex]) {
                // Mostrar TableGUI al hacer clic en la mesa
                TableGUI tableGUI = new TableGUI(WaiterGUI.this);
                tableGUI.setVisible(true);
            } else {
                String[] options = {"Si", "No"};
                int result = JOptionPane.showOptionDialog(WaiterGUI.this,
                        "¿Está ocupada la mesa " + (tableIndex + 1) + "?",
                        "Confirmar ocupación", JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

                if (result == JOptionPane.YES_OPTION) {
                    tablePaidStatus[tableIndex] = true;
                    updateButtonColor(button, tableIndex);
                }
            }
        }
    }
    
    private void showTableDialog(int tableIndex, JButton button) {
        String[] options = {"Si", "No"};
        int result = JOptionPane.showOptionDialog(this,
                "¿Sigue ocupada la mesa " + (tableIndex + 1) + "?",
                "Confirmar ocupación", JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        if (result == JOptionPane.NO_OPTION) {
            tablePaidStatus[tableIndex] = false;
            updateButtonColor(button, tableIndex);
        }
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
            java.util.logging.Logger.getLogger(WaiterGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(WaiterGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(WaiterGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(WaiterGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new WaiterGUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
