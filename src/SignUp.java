import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class SignUp extends JFrame{
    private JPanel jpanel;
    private JTextField usernameField;
    private JPasswordField passwordField1;
    private JTextField fullNameField;
    private JTextField addressField;
    private JCheckBox monthly$4999CheckBox;
    private JCheckBox yearly$49999CheckBox;
    private JButton signUpButton;
    private JTextField phonenumberfield;


    public SignUp(){
        this.setContentPane(this.jpanel);
        this.setTitle("MainMenu");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBounds(0,0,1000,600);
        this.setVisible(true);

        //Action Listeners
        actionListenerSignUpButton();



    }



    //Encryption Code
    public static String getEncryptedPassword(char[] unencryptedPassword) {
        try {
            String encryptedPassword = EncryptionManager.encrypt(unencryptedPassword);
            //System.out.println(encryptedPassword);
            return encryptedPassword;


        } catch (Throwable e) {
            System.out.println(e);
            return null;
        }
    }

    //Database SQL code
    public static void insertUserIntoDatabase(String username, String encryptedPassword, String fullName, String address, String phoneNumber, int membershipType) {
        try {
            String query = "INSERT INTO JavaGymDatabase.customers (username, password, full_name, address, phone_number, membership_id) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stm = Database.connection.prepareStatement(query);
            stm.setString(1, username);
            stm.setString(2, encryptedPassword);
            stm.setString(3, fullName);
            stm.setString(4, address);
            stm.setString(5,phoneNumber);
            stm.setInt(6,membershipType);
            stm.executeUpdate();


        } catch (Exception e) {
            e.printStackTrace();

        }

    }



//Methods---------------------------------------------------------------------

    public void addUserInfo() {
        String username = usernameField.getText();
        char[] userUnencryptedPassword = passwordField1.getPassword(); //User's plain text password
        String encryptedPassword = getEncryptedPassword(userUnencryptedPassword);
        String fullName = fullNameField.getText();
        int membershipType;
        if(monthly$4999CheckBox.isSelected()==true) {
            membershipType = 1;
        } else{
            membershipType = 2;
        }
        String address = addressField.getText();
        String phoneNumber = phonenumberfield.getText();

        insertUserIntoDatabase(username, encryptedPassword, fullName, address, phoneNumber, membershipType);
        JOptionPane.showMessageDialog(null, "User has successfully signed up!");
        backToHome();

    }

    public void backToHome() {
        this.dispose();
        new MainPage();
    }
    public boolean confirmOneMembershipWasPicked() {


        /*
        * T T -> F
        * F F -> F
        * T F -> T
        * F T -> T
        * XOR */

        boolean monthly = monthly$4999CheckBox.isSelected();
        boolean yearly = yearly$49999CheckBox.isSelected();

        if(!(monthly ^ yearly)){

            if(monthly && yearly){ //Both are checked
                JOptionPane.showMessageDialog(this, "Please select only ONE membership.", "Error", JOptionPane.ERROR_MESSAGE);
            } else{ //neither are checked
                JOptionPane.showMessageDialog(this, "You must select at least ONE membership to sign up.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            return false;
        }

        return true;

    }

    public boolean confirmNonEmptyValues() {
        if(usernameField.getText().isEmpty() ||
                passwordField1.getPassword().length == 0 ||
                fullNameField.getText().isEmpty() ||
                phonenumberfield.getText().isEmpty() ||
                addressField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields must be filled in before signing up", "Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    public boolean confirmIfDoubleUsername() {
        try{
            String username = usernameField.getText();
            String query = "SELECT COUNT(*) FROM JavaGymDatabase.customers WHERE username = ?";
            PreparedStatement stm = Database.connection.prepareStatement(query);

            ResultSet rs = stm.executeQuery();

            if (rs.next()) {
                int count = rs.getInt(1);
                if (count > 0) {
                    return false; //username is taken
                }
            }

        } catch(Exception e){
            System.out.println(e);
        }
        return true; //username is available
    }

    public boolean confirmIfDoublePhoneNumber() {
        try{
            String phone = phonenumberfield.getText();
            String query = "SELECT COUNT(*) FROM JavaGymDatabase.customers WHERE phone_number = ?";
            PreparedStatement stm = Database.connection.prepareStatement(query);

            ResultSet rs = stm.executeQuery();

            if (rs.next()) {
                int count = rs.getInt(1);
                if (count > 0) {
                    return false; //phone is taken
                }
            }

        } catch(Exception e){
            System.out.println(e);
        }
        return true; //phone is available
    }

//    Action Listeners-----------------------------------------------

    public void actionListenerSignUpButton() {
        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(confirmOneMembershipWasPicked()==false || confirmNonEmptyValues()==false) {
                    return;
                } else if(confirmIfDoubleUsername()==false) {
                    JOptionPane.showMessageDialog(null, "That username is already taken. Please choose another.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                } else if(confirmIfDoublePhoneNumber()==false){
                    JOptionPane.showMessageDialog(null, "Phone Number entered is already in use", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }else{
                    addUserInfo();
                }

            }
        });
    }
}
