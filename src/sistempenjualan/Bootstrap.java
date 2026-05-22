/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistempenjualan;

import java.awt.*;
import javax.swing.*;

/**
 *
 * @author nurhalizah
 */
public class Bootstrap extends JFrame {

    private final String CARD_LOGIN = "card_login";
    private final String CARD_DASHBOARD_ADMIN = "card_dashboard";
    private final String CARD_VENDOR = "card_vendor";
    private final String CARD_CUSTOMER = "card_customer";
    private final String CARD_PRODUCT = "card_product";
    private final String CARD_USER = "card_user";
    private final String CARD_STOK = "card_stock";
    private final String CARD_MANAGEMENT_STOK = "management_stock";
    private final String CARD_REPORT_PENJUALAN = "card_report_penjualan";
    private final String CARD_FORM_PENJUALAN = "card_form_penjualan";
    private final String CARD_DASHBOARD_SALES = "card_dashboard_sales";
    
    private CardLayout cardLayout;
    private JPanel cards;
    private String sessionRole = null;
    private int sessionUserId = 0;
    private DashboardSales dashboard_sales;
    private DashboardAdmin dashboard;
    private MonitorStok stok;

    public Bootstrap() {
        setTitle("Aplikasi Penjualan & Inventory di PT Mitra Tiga Sepakat");
        setSize(1000, 1000);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);

        Login login = new Login(this);
        dashboard = new DashboardAdmin(this);
        Vendor vendor = new Vendor(this);
        Customer cust = new Customer(this);
        Product product = new Product(this);
        User user = new User(this);
        stok = new MonitorStok(this);
        ManajemenStok manageStock = new ManajemenStok(this);
        ReportPenjualan reportPenjualan = new ReportPenjualan(this);
        FormPenjualan formPenjualan = new FormPenjualan(this);
        dashboard_sales = new DashboardSales(this);

        cards.add(login.getContentPane(), CARD_LOGIN);
        cards.add(dashboard.getContentPane(), CARD_DASHBOARD_ADMIN);
        cards.add(vendor.getContentPane(), CARD_VENDOR);
        cards.add(cust.getContentPane(), CARD_CUSTOMER);
        cards.add(product.getContentPane(), CARD_PRODUCT);
        cards.add(user.getContentPane(), CARD_USER);
        cards.add(stok.getContentPane(), CARD_STOK);
        cards.add(manageStock.getContentPane(), CARD_MANAGEMENT_STOK);
        cards.add(reportPenjualan, CARD_REPORT_PENJUALAN);
        cards.add(formPenjualan,   CARD_FORM_PENJUALAN);
        cards.add(dashboard_sales.getContentPane(), CARD_DASHBOARD_SALES);
        
        cards.revalidate();
        cards.repaint();
        
        // Tampilkan pertama kali (login)
        cardLayout.show(cards, CARD_LOGIN);

        add(cards);
        setVisible(true);
    }

    // Set dan Get Session 
    public void setSessionRole(String role) {
        this.sessionRole = role;
    }

    public String getSessionRole() {
        return this.sessionRole;
    }
    
    public void setSessionUserId(int userId) {
        this.sessionUserId = userId;
    }

    public int getSessionUserId() {
        return this.sessionUserId;
    }
    // End Set dan Get Session 

    public void showCard(String name) {
        cardLayout.show(cards, name);
    }

    public void showLogin() {
        showCard(CARD_LOGIN);
    }

    public void showDashboard() {
        
        if ("admin".equalsIgnoreCase(sessionRole)) {
            dashboard.loadDashboard();
            showCard(CARD_DASHBOARD_ADMIN);
        } else {
            dashboard_sales.loadDashboard();
            showCard(CARD_DASHBOARD_SALES);
        }
    }

    public void showVendor() {
        showCard(CARD_VENDOR);
    }

    public void showCustomer() {
        showCard(CARD_CUSTOMER);
    }

    public void showProduct() {
        showCard(CARD_PRODUCT);
    }

    public void showUser() {
        showCard(CARD_USER);
    }

    public void showStok() {
        stok.refreshData();
        showCard(CARD_STOK);
    }

    public void showManagementStok() {
        showCard(CARD_MANAGEMENT_STOK);
    }

    public void showReportPenjualan() {
        showCard(CARD_REPORT_PENJUALAN);
    }
    
    public void showFormPenjualan() {
        showCard(CARD_FORM_PENJUALAN);
    }
    
    public void logout() {
        this.sessionRole = null;
        this.sessionUserId = 0;
        showLogin();
    }
}
