import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainPage extends JFrame {
    private JPanel jpanel;
    private JButton signInButton;
    private JButton signUpButton;

    public MainPage(){  //Class constructor
        this.setContentPane(this.jpanel);
        this.setTitle("MainMenu");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBounds(0,0,1000,600);
        this.setVisible(true);

        //Action Listeners
        setSignUpButtonAction();
        setSignInButtonAction();
        }

    public static void main(String[]args){ //Main Method

        Database.connect();
        Database.autoCloseDB();

        new MainPage();
    }

    //Action Listeners
    public void setSignUpButtonAction() {
        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            GoToSignUpPage();
            }
        });
    }

    //Navigation Methods

    public void GoToSignUpPage(){
        this.dispose();
        new SignUp();
    }
    public void setSignInButtonAction() {
        signInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {GoToSignInPage();
            }
        });
    }

    public void GoToSignInPage(){
        this.dispose();
        new SignIn();
    }
}
