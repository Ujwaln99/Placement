import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginPage extends JFrame implements ActionListener {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public LoginPage() {
        // Set up the JFrame
        setTitle("Placement management system");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 200);
        setLayout(new GridLayout(3, 2));
        setLocationRelativeTo(null); // Center the frame on the screen

        // Create the username label and field
        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField();

        // Create the password label and field
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();

        // Create the login button
        loginButton = new JButton("Login");
        loginButton.addActionListener(this);

        // Add components to the frame
        add(usernameLabel);
        add(usernameField);
        add(passwordLabel);
        add(passwordField);
        add(new JLabel()); // Empty label for spacing
        add(loginButton);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            // Generate hash value for the password
            String hashedPassword = hashPassword(password);

            // Store the username and hashed password in a text file
            storeCredentials(username, hashedPassword);

            JOptionPane.showMessageDialog(this, "Hashed password stored in file!");

            // Close the login page
            this.dispose();

            // Open the home page
            SwingUtilities.invokeLater(() -> {
                Home homePage = new Home();
                homePage.setSize(1200, 700);
                homePage.setVisible(true);
            });
        }
    }

    private String hashPassword(String password) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = messageDigest.digest(password.getBytes());

            StringBuilder hashedPassword = new StringBuilder();
            for (byte b : hashBytes) {
                hashedPassword.append(String.format("%02x", b));
            }

            return hashedPassword.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void storeCredentials(String username, String hashedPassword) {
        try (FileWriter writer = new FileWriter("credentials.txt")) {
            writer.write("Username: " + username + "\n");
            writer.write("Hashed Password: " + hashedPassword + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginPage loginPage = new LoginPage();
            loginPage.setVisible(true);
        });
    }
}
