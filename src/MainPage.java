import javax.swing.*;

public class MainPage extends JFrame {
    private JPanel jpanel;
    private JButton signInButton;
    private JButton signUpButton;

    public MainPage() {
        setContentPane(jpanel);
        setTitle("Main Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null); // centers window
        setVisible(true);

        // Action Listeners (clean lambda style)
        signUpButton.addActionListener(e -> goToSignUpPage());
        signInButton.addActionListener(e -> goToSignInPage());
    }

    public static void main(String[] args) {
        try {
            Database.connect();
            Database.autoCloseDB();
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(MainPage::new);
    }

    // Navigation Methods
    private void goToSignUpPage() {
        this.dispose();
        new SignUp();
    }

    private void goToSignInPage() {
        this.dispose();
        new SignIn();
    }
}