import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CustomerAccountSettings extends JFrame {
    private JPanel panel;
    private JTextField fullnameTF;
    private JTextField addressTF;
    private JTextField phoneNumTF;
    private JButton saveAllChangesButton;
    private JButton deleteAccountButton;
    private JCheckBox iConfirmCheckBox;
    private JButton backToHomeButton;


    public CustomerAccountSettings() {
        setContentPane(panel);
        setTitle("Main Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setVisible(true);

        //Methods:
        populateUserInfo();


        //Action listeners:
        actionListenerSetSaveAllChangesButton();
        actionListenerBackToHome();
        actionListenerDeleteAccountButton();

    }


    public void populateUserInfo() {
        int currentSignedInUserId = UserSession.userId;

        try {

            String query = "SELECT full_name, address, phone_number FROM JavaGymDatabase.Customers WHERE customer_id = ?";

            PreparedStatement stmt = Database.connection.prepareStatement(query);
            stmt.setInt(1,currentSignedInUserId);

            ResultSet rs = stmt.executeQuery();


            //POPULATE THE TABLE

            if(rs.next()) { //move the pointer to the first result found
                fullnameTF.setText(rs.getString("full_name"));
                addressTF.setText(rs.getString("address"));
                phoneNumTF.setText(rs.getString("phone_number"));

            }
        } catch(Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: Could not load user information.");
        }
    }

    public void saveAllChangesButton() {
        int currentSignedInUserId = UserSession.userId;

        String updatedName = fullnameTF.getText();
        String updatedAddress = addressTF.getText();
        String updatedPhone = phoneNumTF.getText();

        try {
            String updateInfoSQL = "UPDATE JavaGymDatabase.Customers SET full_name = ?, address = ?, phone_number = ? WHERE customer_id = ?";
            PreparedStatement stmt = Database.connection.prepareStatement(updateInfoSQL);

            stmt.setString(1, updatedName);
            stmt.setString(2, updatedAddress);
            stmt.setString(3, updatedPhone);
            stmt.setInt(4, currentSignedInUserId);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(null, "Changes have been saved");



        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    public void actionListenerSetSaveAllChangesButton() {
        saveAllChangesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveAllChangesButton();
            }
        });
    }

    public void actionListenerDeleteAccountButton() {
        deleteAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteAccount();
            }
        });
    }

    public void deleteAccount() {
        boolean checkMarkIsSelected = iConfirmCheckBox.isSelected();

        if(checkMarkIsSelected) {
            deleteAccountFromDatabase();
        } else {
            JOptionPane.showMessageDialog(this, "You Must Click \"I Confirm\" To Delete Your Account");

        }
    }

    public void deleteAccountFromDatabase() {

        int currentlySignedInUserId = UserSession.userId;
        String deleteSQL = "DELETE FROM JavaGymDatabase.Customers WHERE customer_id = ?";

        try {
            PreparedStatement stmt = Database.connection.prepareStatement(deleteSQL);
            stmt.setInt(1, currentlySignedInUserId);

            int rows = stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Account Has Been Successfully Deleted.");

            //Clear User session and log out
            UserSession.logout();
            this.dispose();
            new MainPage();




        } catch(Exception e) {
            System.out.println(e);
        }
    }
    public void actionListenerBackToHome() {
        backToHomeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goHome();
            }
        });
    }

    public void goHome() {
        this.dispose();
        new CustomerPage();
    }
}
