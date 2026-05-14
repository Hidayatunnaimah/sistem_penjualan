package sistempenjualan;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Koneksi {

    public static Connection con;
    public static Statement stm;

    public static void koneksi() {
        try {
            String url = "jdbc:mysql://localhost/sistem_penjualan";
            String user = "root";
            String pass = "";
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(url, user, pass);
            stm = con.createStatement();
            System.out.println("Koneksi berhasil!");
            migrate();
            seed();
        } catch (Exception e) {
            System.err.println("Koneksi gagal: " + e.getMessage());
        }
    }

    public static Connection getConnection() throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        return DriverManager.getConnection("jdbc:mysql://localhost/sistem_penjualan", "root", "");
    }

    private static void migrate() {
        try {
            stm.executeUpdate(
                "CREATE TABLE IF NOT EXISTS m_user ("
                + "    id         INT AUTO_INCREMENT PRIMARY KEY,"
                + "    username   VARCHAR(50)  NOT NULL UNIQUE,"
                + "    password   VARCHAR(255) NOT NULL,"
                + "    role       VARCHAR(255) NOT NULL"
                + ")"
            );
            stm.executeUpdate(
                "CREATE TABLE IF NOT EXISTS m_customer ("
                + "    id              INT AUTO_INCREMENT PRIMARY KEY,"
                + "    phone_number    VARCHAR(20),"
                + "    customer_name   VARCHAR(100) NOT NULL,"
                + "    address         TEXT,"
                + "    gender          ENUM('L','P')"
                + ")"
            );
            stm.executeUpdate(
                "CREATE TABLE IF NOT EXISTS m_vendor ("
                + "    id          INT AUTO_INCREMENT PRIMARY KEY,"
                + "    vendor_name VARCHAR(100) NOT NULL,"
                + "    pic_name    VARCHAR(100),"
                + "    no_hp       VARCHAR(20),"
                + "    email       VARCHAR(100),"
                + "    address     TEXT"
                + ")"
            );
            stm.executeUpdate(
                "CREATE TABLE IF NOT EXISTS m_product ("
                + "    id           INT AUTO_INCREMENT PRIMARY KEY,"
                + "    code         VARCHAR(20)   NOT NULL UNIQUE,"
                + "    vendor_id    INT           NOT NULL,"
                + "    product_name VARCHAR(100)  NOT NULL,"
                + "    price        DECIMAL(12,2) NOT NULL,"
                + "    FOREIGN KEY (vendor_id) REFERENCES m_vendor(id)"
                + ")"
            );
            stm.executeUpdate(
                "CREATE TABLE IF NOT EXISTS trx ("
                + "    id          INT AUTO_INCREMENT PRIMARY KEY,"
                + "    trx_number  VARCHAR(30)   NOT NULL UNIQUE,"
                + "    customer_id INT           NOT NULL,"
                + "    grand_total DECIMAL(14,2) NOT NULL,"
                + "    created_at  DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,"
                + "    FOREIGN KEY (customer_id) REFERENCES m_customer(id)"
                + ")"
            );
            stm.executeUpdate(
                "CREATE TABLE IF NOT EXISTS trx_detail ("
                + "    id         INT AUTO_INCREMENT PRIMARY KEY,"
                + "    trx_id     INT           NOT NULL,"
                + "    product_id INT           NOT NULL,"
                + "    qty        INT           NOT NULL,"
                + "    total      DECIMAL(14,2) NOT NULL,"
                + "    FOREIGN KEY (trx_id)     REFERENCES trx(id),"
                + "    FOREIGN KEY (product_id) REFERENCES m_product(id)"
                + ")"
            );
            stm.executeUpdate(
                "CREATE TABLE IF NOT EXISTS stock ("
                + "    id         INT AUTO_INCREMENT PRIMARY KEY,"
                + "    product_id INT      NOT NULL UNIQUE,"
                + "    stock      INT      NOT NULL DEFAULT 0,"
                + "    min_stock  INT      NOT NULL DEFAULT 0,"
                + "    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,"
                + "    FOREIGN KEY (product_id) REFERENCES m_product(id)"
                + ")"
            );
            stm.executeUpdate(
                "CREATE TABLE IF NOT EXISTS stock_movement ("
                + "    id            INT AUTO_INCREMENT PRIMARY KEY,"
                + "    stock_id      INT          NOT NULL,"
                + "    movement_type ENUM('IN','OUT','ADJUSTMENT') NOT NULL,"
                + "    qty           INT          NOT NULL,"
                + "    notes         VARCHAR(255),"
                + "    created_at    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,"
                + "    created_by    INT          NOT NULL,"
                + "    FOREIGN KEY (stock_id)   REFERENCES stock(id),"
                + "    FOREIGN KEY (created_by) REFERENCES m_user(id)"
                + ")"
            );
            System.out.println("Migration selesai.");
        } catch (Exception e) {
            System.err.println("Migration gagal: " + e.getMessage());
        }
    }

    private static void seed() {
        try {
            ResultSet rs = stm.executeQuery("SELECT COUNT(*) FROM m_user");
            rs.next();
            if (rs.getInt(1) > 0) {
                return;
            }

            // Users
            stm.executeUpdate(
                "INSERT INTO m_user (username, password, role) VALUES "
                + "('admin',   'admin123', 'admin'),"
                + "('budi_s',  'pass1234', 'sales'),"
                + "('rina_k',  'pass1234', 'sales'),"
                + "('doni_p',  'pass1234', 'sales'),"
                + "('sari_m',  'pass1234', 'sales')"
            );

            // Customers
            stm.executeUpdate(
                "INSERT INTO m_customer (phone_number, customer_name, address, gender) VALUES "
                + "('081234567890', 'Budi Santoso',  'Jl. Mawar No. 12, Jakarta',   'L'),"
                + "('082345678901', 'Rina Kusuma',   'Jl. Melati No. 5, Bandung',   'P'),"
                + "('083456789012', 'Doni Pratama',  'Jl. Kenanga No. 8, Surabaya', 'L'),"
                + "('084567890123', 'Sari Mulyani',  'Jl. Anggrek No. 3, Bekasi',   'P'),"
                + "('085678901234', 'Andi Wijaya',   'Jl. Dahlia No. 17, Depok',    'L')"
            );

            // Vendors
            stm.executeUpdate(
                "INSERT INTO m_vendor (vendor_name, pic_name, no_hp, email, address) VALUES "
                + "('PT Sumber Jaya',    'Hendra', '021-5551001', 'hendra@sumberjaya.com',  'Jl. Industri No. 1, Jakarta'),"
                + "('CV Maju Bersama',   'Dewi',   '021-5552002', 'dewi@majubersama.com',   'Jl. Raya Bogor No. 45, Bogor'),"
                + "('UD Sejahtera',      'Roni',   '021-5553003', 'roni@udsejahtera.com',   'Jl. Gatot Subroto No. 9, Jakarta'),"
                + "('PT Karya Utama',    'Linda',  '021-5554004', 'linda@karyautama.com',   'Jl. MT Haryono No. 22, Bekasi'),"
                + "('CV Berkah Mandiri', 'Agus',   '021-5555005', 'agus@berkahmandiri.com', 'Jl. Ahmad Yani No. 7, Tangerang')"
            );

            // Products
            stm.executeUpdate(
                "INSERT INTO m_product (code, vendor_id, product_name, price) VALUES "
                + "('PRD-001', 1, 'Laptop ASUS VivoBook',   8500000.00),"
                + "('PRD-002', 2, 'Mouse Logitech M100',      150000.00),"
                + "('PRD-003', 3, 'Keyboard Mechanical RGB',  450000.00),"
                + "('PRD-004', 4, 'Monitor LED 24 inch',     2300000.00),"
                + "('PRD-005', 5, 'Headset Gaming Rexus',     350000.00)"
            );

            // Stock
            stm.executeUpdate(
                "INSERT INTO stock (product_id, stock, min_stock) VALUES "
                + "(1, 20, 5),"
                + "(2, 80, 10),"
                + "(3, 50, 10),"
                + "(4, 15, 3),"
                + "(5, 35, 5)"
            );

            // Stock movements - IN (stok awal)
            stm.executeUpdate(
                "INSERT INTO stock_movement (stock_id, movement_type, qty, notes, created_by) VALUES "
                + "(1, 'IN', 20, 'Stok awal - pembelian dari vendor', 1),"
                + "(2, 'IN', 80, 'Stok awal - pembelian dari vendor', 1),"
                + "(3, 'IN', 50, 'Stok awal - pembelian dari vendor', 1),"
                + "(4, 'IN', 15, 'Stok awal - pembelian dari vendor', 1),"
                + "(5, 'IN', 35, 'Stok awal - pembelian dari vendor', 1)"
            );

            // Transactions
            stm.executeUpdate(
                "INSERT INTO trx (trx_number, customer_id, grand_total, created_at) VALUES "
                + "('TRX-20250501-001', 1, 8650000.00, '2025-05-01 09:15:00'),"
                + "('TRX-20250502-001', 2,  150000.00, '2025-05-02 10:30:00'),"
                + "('TRX-20250503-001', 3,  800000.00, '2025-05-03 13:00:00'),"
                + "('TRX-20250504-001', 4, 2300000.00, '2025-05-04 14:45:00'),"
                + "('TRX-20250505-001', 5,  700000.00, '2025-05-05 11:20:00')"
            );

            // Transaction details
            stm.executeUpdate(
                "INSERT INTO trx_detail (trx_id, product_id, qty, total) VALUES "
                + "(1, 1, 1, 8500000.00),"
                + "(2, 2, 1,  150000.00),"
                + "(3, 3, 1,  450000.00),"
                + "(4, 4, 1, 2300000.00),"
                + "(5, 5, 2,  700000.00)"
            );

            // Stock movements - OUT (penjualan)
            stm.executeUpdate(
                "INSERT INTO stock_movement (stock_id, movement_type, qty, notes, created_by) VALUES "
                + "(1, 'OUT', 1, 'Penjualan TRX-20250501-001', 2),"
                + "(2, 'OUT', 1, 'Penjualan TRX-20250502-001', 2),"
                + "(3, 'OUT', 1, 'Penjualan TRX-20250503-001', 3),"
                + "(4, 'OUT', 1, 'Penjualan TRX-20250504-001', 3),"
                + "(5, 'OUT', 2, 'Penjualan TRX-20250505-001', 4)"
            );

            System.out.println("Seed selesai.");
        } catch (Exception e) {
            System.err.println("Seed gagal: " + e.getMessage());
        }
    }
}