/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package sistempenjualan;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author nurhalizah
 */
public class Product extends javax.swing.JPanel {
    
    /**
     * Creates new form Product1
     */
    public Product(Bootstrap app) {
        this.app = app;
        initComponents();
        init();
    }

    ResultSet rs;
    private Bootstrap app;
    private int[] vendorIds;
    private java.util.List<Integer> listVendorIdPerBaris = new java.util.ArrayList<>();

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            init();
        }
    }

    private void init() {
        Koneksi.koneksi();
        tampilData();
        loadVendor();
        edit.setVisible(false);
        hapus.setVisible(false);
    }

    private void loadVendor() {
        vendor.removeAllItems();
        vendor.setMaximumRowCount(100);
        vendor.addItem("-- Pilih Vendor --");
        try {
            rs = Koneksi.stm.executeQuery("SELECT id, vendor_name FROM m_vendor ORDER BY vendor_name");
            java.util.List<Integer> ids = new java.util.ArrayList<>();
            ids.add(-1);
            while (rs.next()) {
                ids.add(rs.getInt("id"));
                vendor.addItem(rs.getString("vendor_name"));
            }
            vendorIds = ids.stream().mapToInt(i -> i).toArray();
            rs.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Gagal load vendor: " + ex.getMessage());
        }
    }

    private void tampilData() {
        try {
            String sql = "SELECT mp.id, mp.code, mp.product_name, mp.price, mp.vendor_id, mv.vendor_name "
                    + "FROM m_product mp LEFT JOIN m_vendor mv ON mv.id = mp.vendor_id";
            rs = Koneksi.stm.executeQuery(sql);

            String[] columns = {"ID", "Kode Produk", "Nama Produk", "Harga", "Nama Vendor"};
            DefaultTableModel model = new DefaultTableModel(columns, 0);

            listVendorIdPerBaris.clear();

            while (rs.next()) {
                listVendorIdPerBaris.add(rs.getInt("vendor_id"));
                Object[] row = {
                    rs.getInt("id"),
                    rs.getString("code"),
                    rs.getString("product_name"),
                    rs.getString("price"),
                    rs.getString("vendor_name") // tabel tetap tampil nama vendor
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

        String namaP = nama.getText().trim();
        String kodeP = kode.getText().trim();
        String hargaP = harga.getText().trim();
        int selectedIndex = vendor.getSelectedIndex();
        int id_vendor = vendorIds[selectedIndex];

        if (namaP.isEmpty() || hargaP.isEmpty() || selectedIndex == 0) {

            JOptionPane.showMessageDialog(this,
                    "Nama product, harga, dan vendor tidak boleh kosong!",
                    "Peringatan",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {

            String sql = "INSERT INTO m_product (code, product_name, price, vendor_id) VALUES (?, ?, ?, ?)";

            PreparedStatement ps = Koneksi.con.prepareStatement(sql);

            ps.setString(1, namaP);
            ps.setString(2, namaP);
            ps.setString(3, hargaP);
            ps.setInt(4, id_vendor);

            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Data berhasil disimpan!");

            clearForm();
            tampilData();

        } catch (Exception e) {

            JOptionPane.showMessageDialog(this,
                    "Gagal menyimpan data: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editData() {
        int baris = jTable1.getSelectedRow();
        if (baris < 0) {
            JOptionPane.showMessageDialog(this, "Pilih data yang ingin diedit terlebih dahulu!",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String namaP = nama.getText().trim();
        String kodeP = kode.getText().trim();
        String hargaP = harga.getText().trim();
        int selectedIndex = vendor.getSelectedIndex();
        int id_vendor = vendorIds[selectedIndex];

        if (namaP.isEmpty() || hargaP.isEmpty() || selectedIndex == 0) {
            JOptionPane.showMessageDialog(this,
                    "Nama product, harga, dan vendor tidak boleh kosong!",
                    "Peringatan",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) jTable1.getValueAt(baris, 0);

        try {
            String sql = "UPDATE m_product SET "
                    + "code='" + kodeP
                    + "', product_name='" + namaP
                    + "', price='" + hargaP
                    + "', vendor_id='" + id_vendor
                    + "' WHERE id=" + id;
            Koneksi.stm.executeUpdate(sql);
            JOptionPane.showMessageDialog(this, "Data berhasil diupdate!");
            clearForm();
            tampilData();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal mengupdate data: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }

        tambah.setVisible(true);
        edit.setVisible(false);
        hapus.setVisible(false);
    }

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
                String sql = "DELETE FROM m_product WHERE id=" + id;
                Koneksi.stm.executeUpdate(sql);
                JOptionPane.showMessageDialog(this, "Data berhasil dihapus!");
                clearForm();
                tampilData();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Gagal menghapus data: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        tambah.setVisible(true);
        edit.setVisible(false);
    }

    private void clearForm() {
        nama.setText("");
        kode.setText("");
        harga.setText("");
        vendor.setSelectedIndex(0);
        jTable1.clearSelection();
        tambah.setVisible(true);
        edit.setVisible(false);
        hapus.setVisible(false);

    }

    private void tabelDiklik() {
        tambah.setVisible(false);
        edit.setVisible(true);
        hapus.setVisible(true);

        int baris = jTable1.getSelectedRow();
        if (baris >= 0) {
            kode.setText(jTable1.getValueAt(baris, 1).toString());
            nama.setText(jTable1.getValueAt(baris, 2).toString());
            harga.setText(jTable1.getValueAt(baris, 3).toString());
            int vendorId = listVendorIdPerBaris.get(baris);

            for (int i = 0; i < vendorIds.length; i++) {
                if (vendorIds[i] == vendorId) {
                    vendor.setSelectedIndex(i);
                    break;
                }
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

        jLabel5 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jButton4 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        nama = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        kode = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        harga = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        vendor = new javax.swing.JComboBox<>();
        jPanel2 = new javax.swing.JPanel();
        tambah = new javax.swing.JButton();
        edit = new javax.swing.JButton();
        hapus = new javax.swing.JButton();
        batal = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel5.setText("DATA PRODUK");

        jButton4.setText("←");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jLabel1.setText("Nama Produk");

        nama.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                namaActionPerformed(evt);
            }
        });

        jLabel4.setText("Kode Produk");

        jLabel2.setText("Harga Produk");

        jLabel3.setText("Vendor");

        vendor.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jPanel2.setLayout(new java.awt.GridLayout());

        tambah.setText("Tambah");
        tambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tambahActionPerformed(evt);
            }
        });
        jPanel2.add(tambah);

        edit.setText("Edit");
        edit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editActionPerformed(evt);
            }
        });
        jPanel2.add(edit);

        hapus.setText("Hapus");
        hapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hapusActionPerformed(evt);
            }
        });
        jPanel2.add(hapus);

        batal.setText("Batal");
        batal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batalActionPerformed(evt);
            }
        });
        jPanel2.add(batal);

        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 123, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(kode)
                    .addComponent(vendor, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(nama)
                    .addComponent(harga)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(vendor, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(kode, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(nama)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(harga, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(40, 40, 40)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(175, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        this.app.showDashboard();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void namaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_namaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_namaActionPerformed

    private void tambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tambahActionPerformed
        createData();  // TODO add your handling code here:
    }//GEN-LAST:event_tambahActionPerformed

    private void editActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editActionPerformed
        editData();    // TODO add your handling code here:
    }//GEN-LAST:event_editActionPerformed

    private void hapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hapusActionPerformed
        hapusData();    // TODO add your handling code here:
    }//GEN-LAST:event_hapusActionPerformed

    private void batalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_batalActionPerformed
        clearForm();// TODO add your handling code here:
    }//GEN-LAST:event_batalActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        tabelDiklik();
    }//GEN-LAST:event_jTable1MouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton batal;
    private javax.swing.JButton edit;
    private javax.swing.JButton hapus;
    private javax.swing.JTextField harga;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField kode;
    private javax.swing.JTextField nama;
    private javax.swing.JButton tambah;
    private javax.swing.JComboBox<String> vendor;
    // End of variables declaration//GEN-END:variables
}
