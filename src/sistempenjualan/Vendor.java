/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistempenjualan;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;
import java.sql.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.JOptionPane;
import java.util.HashMap;

/**
 *
 * @author ASUS
 */
public class Vendor extends javax.swing.JFrame {

    ResultSet rs;
    private Bootstrap app;

    /**
     * Creates new form Vendor
     * @param app
     */
    public Vendor(Bootstrap app) {
        this.app = app;
        initComponents();
        Koneksi.koneksi();
        tampilData();
    }

    private void tampilData() {
        try {
            String sql = "SELECT * FROM m_vendor";
            rs = Koneksi.stm.executeQuery(sql);

            String[] columns = {"ID", "Nama Vendor", "Nama PIC", "Nomor Telepon", "Email", "Alamat"};
            DefaultTableModel model = new DefaultTableModel(columns, 0);
            jTable1.getTableHeader().setFont(
                new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 22)
            );

            while (rs.next()) {
                Object[] row = {
                    rs.getInt("id"),
                    rs.getString("vendor_name"),
                    rs.getString("pic_name"),
                    rs.getString("no_hp"),
                    rs.getString("email"),
                    rs.getString("address")
                };
                model.addRow(row);
            }
            
            jTable1.setModel(model);
            rs.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void createData() {
        String nama = vendorname.getText().trim();
        String picInput = pic.getText().trim();
        String address = alamat.getText().trim();
        String no_hp = phone.getText().trim();
        String emailInput = email.getText().trim();

        if (nama.isEmpty() || picInput.isEmpty() || no_hp.isEmpty() || emailInput.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nama Vendor, Nama PIC, No. HP dan Email tidak boleh kosong!",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String sql = "INSERT INTO m_vendor (vendor_name, pic_name, no_hp, email, address) VALUES ("
                    + "'" + nama + "', "
                    + "'" + picInput + "', "
                    + "'" + no_hp + "', "
                    + "'" + emailInput + "', "
                    + "'" + address + "')";
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
        int baris = jTable1.getSelectedRow();
        if (baris < 0) {
            JOptionPane.showMessageDialog(this, "Pilih data yang ingin diedit terlebih dahulu!",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String nama = vendorname.getText().trim();
        String picInput = pic.getText().trim();
        String address = alamat.getText().trim();
        String no_hp = phone.getText().trim();
        String emailInput = email.getText().trim();

        if (nama.isEmpty() || picInput.isEmpty() || no_hp.isEmpty() || emailInput.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nama Vendor, Nama PIC, No. HP dan Email tidak boleh kosong!",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) jTable1.getValueAt(baris, 0);

        try {
            String sql = "UPDATE m_vendor SET "
                    + "vendor_name='" + nama
                    + "', pic_name ='" + picInput
                    + "', no_hp='" + no_hp
                    + "', email='" + emailInput
                    + "', address ='" + address
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

    // =============================================
    // DELETE
    // =============================================
    private void hapusData() {
        int baris = jTable1.getSelectedRow();
        if (baris < 0) {
            JOptionPane.showMessageDialog(this, "Pilih data yang ingin dihapus terlebih dahulu!",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) jTable1.getValueAt(baris, 0);
        String nama = (String) jTable1.getValueAt(baris, 1);

        int konfirmasi = JOptionPane.showConfirmDialog(this,
                "Yakin ingin menghapus data \"" + nama + "\"?",
                "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);

        if (konfirmasi == JOptionPane.YES_OPTION) {
            try {
                String sql = "DELETE FROM m_vendor WHERE id=" + id;
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
        vendorname.setText("");
        pic.setText("");
        alamat.setText("");
        phone.setText("");
        email.setText("");
        jTable1.clearSelection();
    }

    private void tabelDiklik() {
        int baris = jTable1.getSelectedRow();
        if (baris >= 0) {
            vendorname.setText(jTable1.getValueAt(baris, 1).toString());
            pic.setText(jTable1.getValueAt(baris, 2).toString());
            phone.setText(jTable1.getValueAt(baris, 3).toString());
            email.setText(jTable1.getValueAt(baris, 4).toString());
            alamat.setText(jTable1.getValueAt(baris, 5).toString());
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
        jButton4 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        vendorname = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        pic = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        phone = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        alamat = new javax.swing.JTextArea();
        jLabel4 = new javax.swing.JLabel();
        email = new javax.swing.JTextField();
        jButton6 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jButton4.setText("←");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jPanel1.setLayout(new java.awt.GridLayout(1, 0));

        jButton1.setText("Tambah");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1);

        jButton2.setText("Edit");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2);

        jButton5.setText("Batal");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton5);

        jButton3.setText("Hapus");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton3);

        jLabel1.setText("Nama Vendor");

        vendorname.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                vendornameActionPerformed(evt);
            }
        });

        jLabel5.setText("Nama PIC");

        pic.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                picActionPerformed(evt);
            }
        });

        jLabel3.setText("No HP");

        phone.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                phoneActionPerformed(evt);
            }
        });

        jLabel2.setText("Alamat");

        alamat.setColumns(20);
        alamat.setRows(5);
        jScrollPane2.setViewportView(alamat);

        jLabel4.setText("Email");

        email.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emailActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel5)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel2))
                .addGap(99, 99, 99)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pic)
                    .addComponent(vendorname)
                    .addComponent(phone)
                    .addComponent(email)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 361, Short.MAX_VALUE))
                .addContainerGap(77, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(vendorname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(pic, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(phone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(email, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(20, 20, 20)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addContainerGap(42, Short.MAX_VALUE))
        );

        jButton6.setText("Export");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jButton4))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(189, 189, 189)
                        .addComponent(jButton6)
                        .addGap(27, 27, 27)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 414, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(94, 94, 94)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 788, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(72, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton4)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(41, 41, 41)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(372, 372, 372)
                        .addComponent(jButton6)))
                .addContainerGap(484, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void vendornameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_vendornameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_vendornameActionPerformed

    private void emailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_emailActionPerformed

    private void phoneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_phoneActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_phoneActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        createData();      // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        hapusData();        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        editData();        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
       // TODO add your handling code here:
       tabelDiklik();
    }//GEN-LAST:event_jTable1MouseClicked

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        this.app.showDashboard();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void picActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_picActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_picActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        clearForm();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed

        try {

    JasperPrint jp = JasperFillManager.fillReport(
            getClass().getResourceAsStream("/sistempenjualan/report_vendor.jasper"),
            new HashMap<>(),
            Koneksi.con
    );

    JasperViewer.viewReport(jp, false);

} catch (Exception e) {

    JOptionPane.showMessageDialog(null, e);

}// TODO add your handling code here:
    }//GEN-LAST:event_jButton6ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea alamat;
    private javax.swing.JTextField email;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField phone;
    private javax.swing.JTextField pic;
    private javax.swing.JTextField vendorname;
    // End of variables declaration//GEN-END:variables
}
