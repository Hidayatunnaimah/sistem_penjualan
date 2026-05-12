/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistempenjualan;

import java.sql.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.JOptionPane;
/**
 *
 * @author Asus
 */
public class Customer extends javax.swing.JFrame {
    ResultSet rs;
    /**
     * Creates new form Customer
     */
    public Customer() {
        initComponents();
        Koneksi.koneksi();
        tampilData();
    }
    
    private void tampilData() {
        try {
            String sql = "SELECT * FROM m_customer";
            rs = Koneksi.stm.executeQuery(sql);
 
            DefaultTableModel model = (DefaultTableModel) jTable3.getModel();
            model.setRowCount(0);
 
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("cust_name"),
                    rs.getString("alamat")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void createData() {
        String nama   = jTextField2.getText().trim();
        String alamat = jTextField1.getText().trim();
 
        if (nama.isEmpty() || alamat.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nama dan Alamat tidak boleh kosong!",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
 
        try {
            String sql = "INSERT INTO m_customer (cust_name, alamat) VALUES ('"
                    + nama + "', '" + alamat + "')";
            Koneksi.stm.executeUpdate(sql);
            JOptionPane.showMessageDialog(this, "Data berhasil disimpan!");
            clearForm();
            tampilData();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal menyimpan data: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    
    private void editData() {
        int baris = jTable3.getSelectedRow();
        if (baris < 0) {
            JOptionPane.showMessageDialog(this, "Pilih data yang ingin diedit terlebih dahulu!",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
 
        String nama   = jTextField2.getText().trim();
        String alamat = jTextField1.getText().trim();
 
        if (nama.isEmpty() || alamat.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nama dan Alamat tidak boleh kosong!",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
 
        int id = (int) jTable3.getValueAt(baris, 0);
 
        try {
            String sql = "UPDATE m_customer SET cust_name='" + nama
                    + "', alamat='" + alamat
                    + "' WHERE id=" + id;
            Koneksi.stm.executeUpdate(sql);
            JOptionPane.showMessageDialog(this, "Data berhasil diupdate!");
            clearForm();
            tampilData();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal mengupdate data: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void hapusData() {
        int baris = jTable3.getSelectedRow();
        if (baris < 0) {
            JOptionPane.showMessageDialog(this, "Pilih data yang ingin dihapus terlebih dahulu!",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
 
        int    id   = (int)    jTable3.getValueAt(baris, 0);
        String nama = (String) jTable3.getValueAt(baris, 1);
 
        int konfirmasi = JOptionPane.showConfirmDialog(this,
                "Yakin ingin menghapus data \"" + nama + "\"?",
                "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
 
        if (konfirmasi == JOptionPane.YES_OPTION) {
            try {
                String sql = "DELETE FROM m_customer WHERE id=" + id;
                Koneksi.stm.executeUpdate(sql);
                JOptionPane.showMessageDialog(this, "Data berhasil dihapus!");
                clearForm();
                tampilData();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Gagal menghapus data: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void clearForm() {
        jTextField1.setText("");
        jTextField2.setText("");
        jTable3.clearSelection();
    }
 
    private void tabelDiklik() {
        int baris = jTable3.getSelectedRow();
        if (baris >= 0) {
            jTextField2.setText(jTable3.getValueAt(baris, 1).toString());
            jTextField1.setText(jTable3.getValueAt(baris, 2).toString());
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

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(jTable2);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setText("Alamat");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(32, 105, -1, -1));

        jLabel2.setText("Nama Customer");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(31, 31, -1, -1));

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });
        getContentPane().add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(234, 102, 336, -1));

        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });
        getContentPane().add(jTextField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(239, 40, 336, -1));

        jButton1.setText("Hapus");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 160, -1, -1));

        jButton2.setText("Create");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(42, 165, -1, -1));

        jButton3.setText("Edit");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(149, 163, -1, -1));

        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID", "Customer Name", "Alamat"
            }
        ));
        jTable3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable3MouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(jTable3);

        getContentPane().add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 230, -1, 150));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        hapusData();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        createData();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        editData();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jTable3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable3MouseClicked
        int baris = jTable3.getSelectedRow();
        if (baris >= 0) {
            jTextField2.setText(jTable3.getValueAt(baris, 1).toString()); // Nama Customer
            jTextField1.setText(jTable3.getValueAt(baris, 2).toString()); // Alamat
        }
    }//GEN-LAST:event_jTable3MouseClicked

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed

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
            java.util.logging.Logger.getLogger(Customer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Customer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Customer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Customer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Customer().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    // End of variables declaration//GEN-END:variables
}
