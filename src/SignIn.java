import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SignIn extends JFrame {
    private JPanel jpanel;
    private JTextField textField1;
    private JPasswordField passwordField1;
    private JButton Submit;


    public SignIn() {
        this.setContentPane(jpanel);
        this.setTitle("Sign In");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBounds(0,0,500,400);
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
        Submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginUser();
            }
        });
    }
    public void loginUser() {

        String username = textField1.getText();
        String password = new String(passwordField1.getPassword());

        try {

            // Check Employees first
            String employeeSQL =
                    "SELECT employeetypeid FROM employees WHERE username = ? AND password = ?";

            PreparedStatement stmt1 =
                    Database.connection.prepareStatement(employeeSQL);

            stmt1.setString(1, username);
            stmt1.setString(2, password);

            ResultSet rs1 = stmt1.executeQuery();

            int type = -1; // intellij gets mad if you don't set a default val
            if (rs1.next()) {
                type = rs1.getInt("employeetypeid");
                JOptionPane.showMessageDialog(this, "Employee Login Success");
                this.dispose();
                new EmployeePage(type);
                return;
            }

            // Check Customers
            String customerSQL =
                    "SELECT * FROM customers WHERE username = ? AND password = ?";

            PreparedStatement stmt2 =
                    Database.connection.prepareStatement(customerSQL);

            stmt2.setString(1, username);
            stmt2.setString(2, password);

            ResultSet rs2 = stmt2.executeQuery();

            if (rs2.next()) {
                JOptionPane.showMessageDialog(this, "Customer Login Success");
                this.dispose();
                new CustomerPage();
                return;
            }

            JOptionPane.showMessageDialog(this, "Invalid Login");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
