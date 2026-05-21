/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sistempenjualan;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.text.SimpleDateFormat;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;
import java.util.HashMap;
/**
 *
 * @author Hida
 */
public class ManajemenStok extends javax.swing.JFrame {
    ResultSet rs;
    private int[] vendorIds;
    private int[] produkIds;
    private int selectedMovementId = -1;
    private Bootstrap app;

    /** Creates new form ManajemenStok */
    public ManajemenStok(Bootstrap app) {
        this.app = app;
        initComponents();
        Koneksi.koneksi();
        loadVendor();
        loadProduk();
        loadTableData(null, null);
        batal.setVisible(false);
        start.setDateFormatString("yyyy-MM-dd");
        end.setDateFormatString("yyyy-MM-dd");
    }
    
    
    private void loadVendor() {
        select_vendor.removeAllItems();
        select_vendor.addItem("-- Semua Vendor --");
        try {
            rs = Koneksi.stm.executeQuery("SELECT id, vendor_name FROM m_vendor ORDER BY vendor_name");
            java.util.List<Integer> ids = new java.util.ArrayList<>();
            while (rs.next()) {
                ids.add(rs.getInt("id"));
                select_vendor.addItem(rs.getString("vendor_name"));
            }
            vendorIds = ids.stream().mapToInt(i -> i).toArray();
            rs.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Gagal load vendor: " + ex.getMessage());
        }
    }
    
    private void loadProduk() {
        select_product.removeAllItems();
        select_product.addItem("-- Pilih Produk --");
        try {
            rs = Koneksi.stm.executeQuery(
                "SELECT id, code, product_name FROM m_product ORDER BY code");
            java.util.List<Integer> ids = new java.util.ArrayList<>();
            while (rs.next()) {
                ids.add(rs.getInt("id"));
                select_product.addItem(rs.getString("code") + " | " + rs.getString("product_name"));
            }
            produkIds = ids.stream().mapToInt(i -> i).toArray();
            rs.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Gagal load produk: " + ex.getMessage());
        }
    }
    
    private void loadProdukByVendor(int vendorIds) {
        select_product.removeAllItems();
        select_product.addItem("-- Pilih Produk --");
        try {
            PreparedStatement ps = Koneksi.con.prepareStatement(
                "SELECT id, code, product_name FROM m_product WHERE vendor_id = ? ORDER BY code");
            ps.setInt(1, vendorIds);
            
            rs = ps.executeQuery();
            java.util.List<Integer> ids = new java.util.ArrayList<>();
            while (rs.next()) {
                ids.add(rs.getInt("id"));
                select_product.addItem(rs.getString("code") + " | " + rs.getString("product_name"));
            }
            
            produkIds = ids.stream().mapToInt(i -> i).toArray();
            rs.close();
            ps.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Gagal load produk: " + ex.getMessage());
        }
    }

    private void loadTableData(String start, String end) {
        String[] columns = {"id", "Tanggal", "Jam", "Kode Produk", "Nama Produk", "Nama Vendor", "Tipe", "Jumlah", "Note"};
        DefaultTableModel model = (DefaultTableModel) manajemenstok_tabel.getModel();
        model.setColumnIdentifiers(columns);
        model.setRowCount(0);

        try {
            StringBuilder sql = new StringBuilder(
                "SELECT sm.id, DATE(sm.created_at) AS date, TIME(sm.created_at) AS time, " +
                "mp.code, mp.product_name, mv.vendor_name, sm.movement_type, sm.qty, sm.notes " +
                "FROM stock_movement sm " +
                "LEFT JOIN stock s ON s.id = sm.stock_id " +
                "LEFT JOIN m_product mp ON s.product_id = mp.id " +
                "LEFT JOIN m_vendor mv ON mv.id = mp.vendor_id"
            );

            java.util.List<String> params = new java.util.ArrayList<>();
            if (start != null && !start.isEmpty()) {
                sql.append(params.isEmpty() ? " WHERE" : " AND");
                sql.append(" DATE(sm.created_at) >= ?");
                params.add(start);
            }
            if (end != null && !end.isEmpty()) {
                sql.append(params.isEmpty() ? " WHERE" : " AND");
                sql.append(" DATE(sm.created_at) <= ?");
                params.add(end);
            }
            sql.append(" ORDER BY sm.created_at DESC");

            PreparedStatement ps = Koneksi.con.prepareStatement(sql.toString());
            for (int i = 0; i < params.size(); i++) {
                ps.setString(i + 1, params.get(i));
            }

            rs = ps.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("date"),
                    rs.getString("time"),
                    rs.getString("code"),
                    rs.getString("product_name"),
                    rs.getString("vendor_name"),
                    rs.getString("movement_type"),
                    rs.getInt("qty"),
                    rs.getString("notes")
                });
            }
            rs.close();
            ps.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Gagal load data: " + ex.getMessage());
        }
        
        manajemenstok_tabel.getColumnModel().getColumn(0).setMinWidth(0);
        manajemenstok_tabel.getColumnModel().getColumn(0).setMaxWidth(0);
        manajemenstok_tabel.getColumnModel().getColumn(0).setWidth(0);
    }

    private void filterData() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        String start_date = sdf.format(start.getDate());
        String end_date   = sdf.format(end.getDate());
        loadTableData(
            start_date.isEmpty() ? null : start_date,
            end_date.isEmpty()   ? null : end_date
        );
    }

    private void loadRowToForm() {
        int row = manajemenstok_tabel.getSelectedRow();
        if (row < 0) return;

        DefaultTableModel model = (DefaultTableModel) manajemenstok_tabel.getModel();

        selectedMovementId = Integer.parseInt(model.getValueAt(row, 0).toString());

        try {
            PreparedStatement ps = Koneksi.con.prepareStatement(
                "SELECT sm.id, sm.qty, sm.notes, " +
                "       mp.id AS product_id, mp.code, mp.product_name, " +
                "       mv.id AS vendor_id, mv.vendor_name " +
                "FROM stock_movement sm " +
                "LEFT JOIN stock s   ON s.id = sm.stock_id " +
                "LEFT JOIN m_product mp ON mp.id = s.product_id " +
                "LEFT JOIN m_vendor  mv ON mv.id = mp.vendor_id " +
                "WHERE sm.id = ?"
            );
            ps.setInt(1, selectedMovementId);

            ResultSet rsEdit = ps.executeQuery();
            if (!rsEdit.next()) {
                JOptionPane.showMessageDialog(this, "Data tidak ditemukan!");
                rsEdit.close();
                ps.close();
                return;
            }

            int    vendorId   = rsEdit.getInt("vendor_id");
            int    productId  = rsEdit.getInt("product_id");
            int    qtyVal     = rsEdit.getInt("qty");
            String notesVal   = rsEdit.getString("notes");
            rsEdit.close();
            ps.close();

            select_vendor.setEnabled(false);
            for (int i = 0; i < vendorIds.length; i++) {
                if (vendorIds[i] == vendorId) {
                    select_vendor.setSelectedIndex(i + 1);
                    break;
                }
            }

            loadProdukByVendor(vendorId);
            select_product.setEnabled(false);
            for (int i = 0; i < produkIds.length; i++) {
                if (produkIds[i] == productId) {
                    select_product.setSelectedIndex(i + 1);
                    break;
                }
            }

            qty.setText(String.valueOf(qtyVal));
            notes.setText(notesVal != null ? notesVal : "");

            simpan.setText("Update");
            batal.setVisible(true);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Gagal load data edit: " + ex.getMessage());
        }
    }
    private void simpanData() {
        int produkIdx = select_product.getSelectedIndex();
        if (produkIdx <= 0) {
            JOptionPane.showMessageDialog(this, "Pilih produk terlebih dahulu!");
            return;
        }

        String jumlahStr = qty.getText().trim();
        if (jumlahStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Jumlah tidak boleh kosong!");
            return;
        }

        int jumlah;
        try {
            jumlah = Integer.parseInt(jumlahStr);
            if (jumlah <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Jumlah harus lebih dari 0!");
            return;
        }

        int productId = produkIds[produkIdx - 1];
        String notes_value = notes.getText().trim();

        try {
            PreparedStatement psStock = Koneksi.con.prepareStatement(
                "SELECT id FROM stock WHERE product_id = ? LIMIT 1");
            psStock.setInt(1, productId);
            ResultSet rsStock = psStock.executeQuery();

            int stockId;
            if (rsStock.next()) {
                stockId = rsStock.getInt("id");
            } else {
                JOptionPane.showMessageDialog(this, "Data stock untuk produk ini tidak ditemukan!");
                rsStock.close(); psStock.close();
                return;
            }
            rsStock.close(); psStock.close();

            PreparedStatement psInsert = Koneksi.con.prepareStatement(
                "INSERT INTO stock_movement (stock_id, movement_type, qty, notes, created_at, created_by) " +
                "VALUES (?, 'IN', ?, ?, NOW(), 1)");
            psInsert.setInt(1, stockId);
            psInsert.setInt(2, jumlah);
            psInsert.setString(3, notes_value.isEmpty() ? null : notes_value);
            psInsert.executeUpdate();
            psInsert.close();

            JOptionPane.showMessageDialog(this, "Data berhasil disimpan!");

            select_vendor.setSelectedIndex(0);
            loadProduk();
            qty.setText("");
            notes.setText("");

            loadTableData(null, null);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Gagal simpan: " + ex.getMessage());
        }
    }
    
    private void updateData() {
        if (selectedMovementId < 0) {
            JOptionPane.showMessageDialog(this, "Tidak ada data yang dipilih untuk diupdate!");
            return;
        }

        String jumlahStr = qty.getText().trim();
        if (jumlahStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Jumlah tidak boleh kosong!");
            return;
        }

        int jumlah;
        try {
            jumlah = Integer.parseInt(jumlahStr);
            if (jumlah <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Jumlah harus angka positif!");
            return;
        }

        String notesValue = notes.getText().trim();

        int confirm = JOptionPane.showConfirmDialog(this,
            "Yakin ingin mengupdate data stok ini?", "Konfirmasi Update",
            JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            PreparedStatement ps = Koneksi.con.prepareStatement(
                "UPDATE stock_movement SET qty = ?, notes = ? WHERE id = ?"
            );
            ps.setInt(1, jumlah);
            ps.setString(2, notesValue.isEmpty() ? null : notesValue);
            ps.setInt(3, selectedMovementId);
            ps.executeUpdate();
            ps.close();

            JOptionPane.showMessageDialog(this, "Data berhasil diupdate!");
            resetForm();
            loadTableData(null, null);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Gagal update: " + ex.getMessage());
        }
    }
    
    private void resetForm() {
        selectedMovementId = -1;
        select_vendor.setEnabled(true);
        select_vendor.setSelectedIndex(0);
        loadProduk();
        select_product.setEnabled(true);
        qty.setText("");
        notes.setText("");
        simpan.setText("Simpan");
        batal.setVisible(false);
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        filter = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        start = new com.toedter.calendar.JDateChooser();
        end = new com.toedter.calendar.JDateChooser();
        jScrollPane1 = new javax.swing.JScrollPane();
        manajemenstok_tabel = new javax.swing.JTable();
        jLabel9 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        export = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        select_vendor = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        select_product = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        qty = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        notes = new javax.swing.JTextArea();
        batal = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        simpan = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(java.awt.Color.white);
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("MANAJEMEN STOK");

        filter.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        filter.setText("Filter");
        filter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                filterActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel6.setText("Start");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel7.setText("End");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jLabel6))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(start, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(34, 34, 34)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addComponent(end, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 61, Short.MAX_VALUE)
                .addComponent(filter, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(88, 88, 88))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(filter, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7))
                        .addGap(1, 1, 1)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(start, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(end, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(91, Short.MAX_VALUE))
        );

        manajemenstok_tabel.setAutoCreateRowSorter(true);
        manajemenstok_tabel.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        manajemenstok_tabel.setModel(new javax.swing.table.DefaultTableModel(
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
        manajemenstok_tabel.setRowHeight(35);
        manajemenstok_tabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                manajemenstok_tabelMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(manajemenstok_tabel);

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel9.setText("Riwayat IN/OUT Produk");

        jButton1.setText("←");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        export.setText("Export");
        export.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(export)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(33, 33, 33)
                        .addComponent(jLabel1))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel9)
                        .addComponent(jScrollPane1)
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jButton1))
                .addGap(18, 18, 18)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(export)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(46, 46, 46))
        );

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 650, 870));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel8.setText("INPUT BARANG MASUK");

        jLabel3.setBackground(new java.awt.Color(255, 255, 255));
        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setText("Cari Vendor");

        select_vendor.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        select_vendor.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        select_vendor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                select_vendorActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setText("Pilih Produk");

        select_product.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        select_product.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setText("Jumlah");

        qty.setFont(new java.awt.Font("Segoe UI", 0, 25)); // NOI18N

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setText("Notes");

        notes.setColumns(20);
        notes.setFont(new java.awt.Font("Monospaced", 0, 25)); // NOI18N
        notes.setRows(5);
        jScrollPane2.setViewportView(notes);

        batal.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        batal.setText("Batal");
        batal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batalActionPerformed(evt);
            }
        });

        jSeparator1.setBackground(new java.awt.Color(51, 51, 51));

        simpan.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        simpan.setText("Simpan");
        simpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                simpanActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.LEADING)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel3))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(select_vendor, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel2))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(select_product, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel5))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(qty, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel4))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(batal, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(simpan, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(38, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(70, 70, 70)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(select_vendor, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(select_product, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(qty, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(batal)
                    .addComponent(simpan))
                .addGap(395, 395, 395))
        );

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 0, 380, 720));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1025, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 751, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void batalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_batalActionPerformed
        // TODO add your handling code here:
       resetForm();
    }//GEN-LAST:event_batalActionPerformed

    private void filterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_filterActionPerformed
        // TODO add your handling code here:
        filterData();
    }//GEN-LAST:event_filterActionPerformed

    private void select_vendorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_select_vendorActionPerformed
        // TODO add your handling code here:
        int index = select_vendor.getSelectedIndex();

        if(index > 0) {
            int vendorId = vendorIds[index - 1];
            loadProdukByVendor(vendorId);
        }
    }//GEN-LAST:event_select_vendorActionPerformed

    private void simpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_simpanActionPerformed
        // TODO add your handling code here:
       if (simpan.getText().equals("Update")) {
            updateData();
        } else {
            simpanData();
        }
    }//GEN-LAST:event_simpanActionPerformed

    private void manajemenstok_tabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_manajemenstok_tabelMouseClicked
        // TODO add your handling code here:
        loadRowToForm();
    }//GEN-LAST:event_manajemenstok_tabelMouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        app.showStok();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void exportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportActionPerformed
        // TODO add your handling code here:
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            String start_date = sdf.format(start.getDate());
            String end_date   = sdf.format(end.getDate());

            HashMap<String, Object> parameter = new HashMap<>();

            parameter.put("start", start_date);
            parameter.put("end", end_date);

            JasperPrint jp = JasperFillManager.fillReport(
                getClass().getResourceAsStream("/sistempenjualan/report_stock.jasper"),
                parameter,
                Koneksi.con
            );

            JasperViewer.viewReport(jp, false);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }//GEN-LAST:event_exportActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton batal;
    private com.toedter.calendar.JDateChooser end;
    private javax.swing.JButton export;
    private javax.swing.JButton filter;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable manajemenstok_tabel;
    private javax.swing.JTextArea notes;
    private javax.swing.JTextField qty;
    private javax.swing.JComboBox<String> select_product;
    private javax.swing.JComboBox<String> select_vendor;
    private javax.swing.JButton simpan;
    private com.toedter.calendar.JDateChooser start;
    // End of variables declaration//GEN-END:variables

}
