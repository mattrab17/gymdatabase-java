import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SignIn extends JFrame {
    private JPanel jpanel;
    private JTextField textField1;
    private JPasswordField passwordField1;
    private JButton SignIn;
    private JButton goBackButton;


    public SignIn() {
        this.setContentPane(jpanel);
        this.setTitle("Sign In");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(600,400);
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        textField1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        passwordField1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        //SIGN IN BUTTON ACTION LISTENER
        SignIn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginUser(); //log in method
            }
        });
        goBackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goBack();
            }
        });
    }

    public void goBack() {
        this.dispose();
        new MainPage();
    }
    //Log In
    public void loginUser() {
        String username = textField1.getText();
        char[] inputtedPassword = passwordField1.getPassword();
        char[] decryptedDatabasePassword = null;

        try {
            // CHECK THE EMPLOYEES TABLE
            String employeeSQL = "SELECT employee_id, password, employeetypeid, is_admin FROM JavaGymDatabase.Employees WHERE username = ?";
            PreparedStatement stmt1 = Database.connection.prepareStatement(employeeSQL);
            stmt1.setString(1, username);
            ResultSet rs1 = stmt1.executeQuery();

            if (rs1.next()) {
                String encryptedPassword = rs1.getString("password");
                int type = rs1.getInt("employeetypeid");
                int isAdmin = rs1.getInt("is_admin");

                // Decrypt the password
                decryptedDatabasePassword = EncryptionManager.decrypt(encryptedPassword);

                // Compare passwords
                if (decryptedDatabasePassword != null && java.util.Arrays.equals(inputtedPassword, decryptedDatabasePassword)) {
                    JOptionPane.showMessageDialog(this, "Employee Login Success");
                    this.dispose();

                    //User Session Code:
                    UserSession.userId = rs1.getInt("employee_id");
                    UserSession.userTable = "Employees";

                    // Navigate to the correct page
                    if (isAdmin == 1) { //if employee is an admin
                        //new AdminPage(); // go to admin page
                    } else { //if employee is not an admin
                        new EmployeePage(type); //go to the employee's specific role page
                    }
                    return; //exit method
                } else { //Password is incorrect
                    JOptionPane.showMessageDialog(this, "Invalid Password");
                    return;
                }
            }

            // CHECK THE CUSTOMERS TABLE
            String customerSQL = "SELECT customer_id, password FROM JavaGymDatabase.Customers WHERE username = ?";
            PreparedStatement stmt2 = Database.connection.prepareStatement(customerSQL);
            stmt2.setString(1, username);
            ResultSet rs2 = stmt2.executeQuery();

            if (rs2.next()) {
                String encryptedPassword = rs2.getString("password");

                // Decrypt the password
                decryptedDatabasePassword = EncryptionManager.decrypt(encryptedPassword);

                if (decryptedDatabasePassword != null && java.util.Arrays.equals(inputtedPassword, decryptedDatabasePassword)) {
                    JOptionPane.showMessageDialog(this, "Customer Login Success");
                    this.dispose();

                    //User Session Code:
                    UserSession.userId = rs2.getInt("customer_id");
                    UserSession.userTable = "Customers";
                    // Navigate to customer page
                    new CustomerPage();
                    return;
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid Password");
                    return;
                }
            }

            //  Neither a user or employee was found
            JOptionPane.showMessageDialog(this, "Invalid Login: User not found.");

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
           //wipe the plain text password from memory
            if (inputtedPassword != null) {
                java.util.Arrays.fill(inputtedPassword, '0');
            }
            //wipe the decrypted database password from memory
            if (decryptedDatabasePassword != null) {
                java.util.Arrays.fill(decryptedDatabasePassword, '0');
            }
        }
    }
}
