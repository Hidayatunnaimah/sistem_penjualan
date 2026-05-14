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

    private CardLayout cardLayout;
    private JPanel cards;

    private String sessionRole = null;

    public Bootstrap() {
        setTitle("Aplikasi Penjualan & Inventory di PT Mitra Tiga Sepakat");
        setSize(1000, 1000);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);

        Login login = new Login(this);
        DashboardAdmin dashboard = new DashboardAdmin(this);
        Vendor vendor = new Vendor(this);
        Customer cust = new Customer(this);
        Product product = new Product(this);
        User user = new User(this);
        MonitorStok stok = new MonitorStok(this);
        ManajemenStok manageStock = new ManajemenStok(this);
        
        cards.add(login.getContentPane(), CARD_LOGIN);
        cards.add(dashboard.getContentPane(), CARD_DASHBOARD_ADMIN);
        cards.add(vendor.getContentPane(), CARD_VENDOR);
        cards.add(cust.getContentPane(), CARD_CUSTOMER);
        cards.add(product.getContentPane(), CARD_PRODUCT);
        cards.add(user.getContentPane(), CARD_USER);
        cards.add(stok.getContentPane(), CARD_STOK);
        cards.add(manageStock.getContentPane(), CARD_MANAGEMENT_STOK);

        // Tampilkan pertama kali (login)
        cardLayout.show(cards, CARD_LOGIN);

        add(cards);
        setVisible(true);
    }

    // Set dan Get Session Role
    public void setSessionRole(String role) {
        this.sessionRole = role;
    }

    public String getSessionRole() {
        return this.sessionRole;
    }
    // End Set dan Get Session Role

    public void showCard(String name) {
        cardLayout.show(cards, name);
    }

    public void showLogin() {
        showCard(CARD_LOGIN);
    }

    public void showDashboard() {
        if ("admin".equalsIgnoreCase(sessionRole)){
            showCard(CARD_DASHBOARD_ADMIN);
        } else {
            showCard(CARD_VENDOR);
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
        showCard(CARD_STOK);
    }

    public void showManagementStok(){
        showCard(CARD_MANAGEMENT_STOK);
    }
}
