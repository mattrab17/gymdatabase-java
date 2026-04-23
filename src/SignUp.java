import javax.swing.*;

public class SignUp extends JFrame{
    private JPanel jpanel;
    private JTextField textField1;
    private JPasswordField passwordField1;
    private JTextField textField2;
    private JTextField textField3;
    private JCheckBox monthly$4999CheckBox;
    private JCheckBox yearly$49999CheckBox;

    public SignUp(){
        this.setContentPane(this.jpanel);
        this.setTitle("MainMenu");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBounds(0,0,1000,600);
        this.setVisible(true);

        //Action Listeners
    }
}
