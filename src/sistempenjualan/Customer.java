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
    private Bootstrap app;
    /**
     * Creates new form Customer
     */
    public Customer(Bootstrap app) {
        this.app = app;
        initComponents();
        Koneksi.koneksi();
        tampilData();
    }
    
    private void tampilData() {
        try {
            String sql = "SELECT * FROM m_customer";
            rs = Koneksi.stm.executeQuery(sql);
 
            //DefaultTableModel model = (DefaultTableModel) jTable3.getModel();
            //model.setRowCount(0);
            String[] columns = {"ID", "Nomor Telepon", "Nama Customer", "Alamat", "Gender"};
            DefaultTableModel model = new DefaultTableModel(columns, 0);
            jTable3.getTableHeader().setFont(
                new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 22)
            );
 
            while (rs.next()) {
                Object[] row = {
                    rs.getInt("id"),
                    rs.getString("phone_number"),
                    rs.getString("customer_name"),
                    rs.getString("address"),
                    rs.getString("gender")
                };
                model.addRow(row);
            }
            
            jTable3.setModel(model);
            rs.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void createData() {
        String nomor = no_tlp.getText().trim();
        String nama   = cust_name.getText().trim();
        String alamat = address.getText().trim();
        String gender = "";
        if (laki.isSelected()) {
            gender = "Laki-laki";
        } else if (pr.isSelected()) {
            gender = "Perempuan";
        }
 
        if (nama.isEmpty() || nomor.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nama dan Nomor Telepon tidak boleh kosong!",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
 
        try {
            String sql = "INSERT INTO m_customer (phone_number, customer_name, address, gender) VALUES ("
                    + "'" + nomor + "', "
                    + "'" + nama + "', "
                    + "'" + alamat + "', "
                    + "'" + gender + "'"
                    + ")";
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
 
        String nomor = no_tlp.getText().trim();
        String nama   = cust_name.getText().trim();
        String alamat = address.getText().trim();
        String gender = "";
        if (laki.isSelected()) {
            gender = "Laki-laki";
        } else if (pr.isSelected()) {
            gender = "Perempuan";
        }
 
        if (nama.isEmpty() || alamat.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nama dan Alamat tidak boleh kosong!",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
 
        int id = (int) jTable3.getValueAt(baris, 0);
 
        try {
            String sql = "UPDATE m_customer SET "
                    + "phone_number='" + nomor
                    + "', customer_name='" + nama
                    + "', address='" + alamat
                    + "', gender='" + gender
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

        no_tlp.setText("");
        cust_name.setText("");
        address.setText("");
        buttonGroup1.clearSelection();
        jTable3.clearSelection();
    }
 
    private void tabelDiklik() {
        int baris = jTable3.getSelectedRow();

        if (baris >= 0) {

            no_tlp.setText(jTable3.getValueAt(baris, 1).toString());
            cust_name.setText(jTable3.getValueAt(baris, 2).toString());
            address.setText(jTable3.getValueAt(baris, 3).toString());

            String gender = jTable3.getValueAt(baris, 4).toString();

            if (gender.equalsIgnoreCase("Laki-laki")) {
                laki.setSelected(true);
            } else if (gender.equalsIgnoreCase("Perempuan")) {
                pr.setSelected(true);
            }
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
        buttonGroup1 = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        cust_name = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jButton4 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        laki = new javax.swing.JRadioButton();
        pr = new javax.swing.JRadioButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        address = new javax.swing.JTextArea();
        no_tlp = new javax.swing.JTextField();

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
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 220, -1, -1));

        jLabel2.setText("Nama Customer");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 150, -1, -1));

        cust_name.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cust_nameActionPerformed(evt);
            }
        });
        getContentPane().add(cust_name, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 150, 336, -1));

        jTable3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable3MouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(jTable3);

        getContentPane().add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 550, 660, 240));

        jButton4.setText("←");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, -1, -1));

        jPanel1.setLayout(new java.awt.GridLayout());

        jButton2.setText("Tambah");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2);

        jButton3.setText("Edit");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton3);

        jButton5.setText("Batal");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton5);

        jButton1.setText("Hapus");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1);

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 450, 520, 60));

        jLabel3.setText("Nomor Telepon");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 80, -1, -1));

        jLabel4.setText("Gender");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 340, -1, -1));

        jTextField1.setText("+62");
        getContentPane().add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 80, 60, -1));

        buttonGroup1.add(laki);
        laki.setText("Laki-laki");
        getContentPane().add(laki, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 340, -1, -1));

        buttonGroup1.add(pr);
        pr.setText("Perempuan");
        getContentPane().add(pr, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 340, -1, -1));

        address.setColumns(20);
        address.setRows(5);
        jScrollPane4.setViewportView(address);

        getContentPane().add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 220, 340, -1));
        getContentPane().add(no_tlp, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 80, 270, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
        tabelDiklik();
    }//GEN-LAST:event_jTable3MouseClicked

    private void cust_nameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cust_nameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cust_nameActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        app.showDashboard();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        clearForm();
    }//GEN-LAST:event_jButton5ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea address;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JTextField cust_name;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JRadioButton laki;
    private javax.swing.JTextField no_tlp;
    private javax.swing.JRadioButton pr;
    // End of variables declaration//GEN-END:variables
}
