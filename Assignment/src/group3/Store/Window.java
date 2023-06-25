package group3.Store;

import group3.Admin.AdminPage;
import group3.DeliveryStaff.DeliveryStaffPage;
import group3.Customer.Cart;
import group3.Store.Login.LoginPage;
import group3.Store.Pages.HomePage;
import group3.Customer.Profile;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Window implements ActionListener {
    private final JFrame f;
    private BufferedImage img;
    private JButton showHomePageButton, accountButton, cartButton;
    private final JPanel contentPane;
    private UserType userType;
    private String username;
    private final HomePage homePage;
    private final Cart cart;
    private final Orders orders;

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getSource() == showHomePageButton) {
                updatePage(homePage.getMainContainer());
            } else if (e.getSource() == accountButton) {
                if (!isLoggedIn()) {
                    LoginPage loginPage = new LoginPage();
                    loginPage.show();
                } else {
                    switch (userType) {
                        case ADMIN:
                            AdminPage adminPage = new AdminPage();
                            adminPage.show();
                            break;
                        case DELIVERY_STAFF:
                            DeliveryStaffPage deliveryStaffPage = new DeliveryStaffPage();
                            deliveryStaffPage.show();
                            break;
                        case CUSTOMER:
                            Profile profilePage = new Profile();
                            updatePage(profilePage.getMainContainer());
                            break;
                    }
                }
            } else if (e.getSource() == cartButton) {
                if (userType != UserType.CUSTOMER) JOptionPane.showMessageDialog(contentPane, "Please log in.", "View Cart Failed", JOptionPane.INFORMATION_MESSAGE);
                else {
                    updatePage(cart.getMainContainer());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public Window() {
        f = new JFrame("Amogus Pet Shop");
        f.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        f.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int confirmation = JOptionPane.showConfirmDialog(f, "Are you sure you want to exit?", "Exit Confirmation", JOptionPane.YES_NO_OPTION);
                if (confirmation == JOptionPane.YES_OPTION) f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                else if (confirmation == JOptionPane.NO_OPTION) f.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            }
        });
        try {
            img = ImageIO.read(new File("res/logo.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        f.setIconImage(img);

        userType = null;
        username = null;
        contentPane = new JPanel();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.PAGE_AXIS));
        addNavButtons();
        homePage = new HomePage();
        cart = new Cart();
        new FileManager().checkFiles();
        orders = new Orders();
        updatePage(homePage.getMainContainer());
    }

    private void addNavButtons() {
        JPanel navButtons = new JPanel(new GridBagLayout());
        Dimension buttonDim = new Dimension(140, 30);
        GridBagConstraints c = new GridBagConstraints();

        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.NONE;
        c.weightx = 0;
        showHomePageButton = new JButton("Home");
        showHomePageButton.setPreferredSize(buttonDim);
        showHomePageButton.setFocusable(false);
        showHomePageButton.addActionListener(this);
        navButtons.add(showHomePageButton, c);

        c.gridx = 1;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        JButton filler = new JButton("");
        filler.setModel(new DefaultButtonModel() {
            @Override
            public boolean isArmed() {
                return false;
            }

            @Override
            public boolean isPressed() {
                return false;
            }

            @Override
            public boolean isRollover() {
                return false;
            }
        });
        navButtons.add(filler, c);

        c.gridx = 2;
        c.fill = GridBagConstraints.NONE;
        c.weightx = 0;
        accountButton = new JButton(isLoggedIn() ? "View Account" : "Login");
        accountButton.setPreferredSize(buttonDim);
        accountButton.setFocusable(false);
        accountButton.addActionListener(this);
        navButtons.add(accountButton, c);

        c.gridx = 3;
        cartButton = new JButton("Cart (0)");
        cartButton.setPreferredSize(buttonDim);
        cartButton.setFocusable(false);
        cartButton.addActionListener(this);
        navButtons.add(cartButton, c);

        contentPane.add(navButtons);
    }

    public void showWindow() {
        f.setVisible(true);
    }

    public void updatePage(Container page) {
        if (contentPane.getComponentCount() == 2) contentPane.remove(1);
        contentPane.add(page);
        f.setContentPane(contentPane);
        f.pack();
        f.setLocationRelativeTo(null);
    }

    public void refreshWindow() {
        Container page = (Container) contentPane.getComponent(1);
        contentPane.removeAll();
        addNavButtons();
        cart.updateCartButton();
        updatePage(page);
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public UserType getUserType() {
        return userType;
    }

    public void logout() {
        userType = null;
        this.username = null;
        cart.clearCartMap();
        refreshWindow();
    }

    public Orders getOrders() {
        return orders;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public BufferedImage getIcon() {
        return img;
    }

    public JFrame getFrame() {
        return f;
    }

    public boolean isLoggedIn() {
        return userType != null;
    }

    public HomePage getHomePage() {
        return homePage;
    }

    public Cart getCart() {
        return cart;
    }

    public JButton getCartButton() {
        return cartButton;
    }
}
