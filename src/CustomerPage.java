import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CustomerPage extends JFrame {

    private JPanel panel;
    private JButton accountSettingsButton;
    private JButton viewYourClassesButton;
    private JButton bookAClassButton;

    public CustomerPage() {
        this.setContentPane(panel);
        this.setTitle("Customer Page");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800,600);
        setLocationRelativeTo(null); // centers window
        this.setVisible(true);

        //Action Listeners
        actionListenerAccountSettingsButton();
    }

    public void actionListenerAccountSettingsButton() {
        accountSettingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goToCustomerAccountSettings();
            }
        });
    }

    public void goToCustomerAccountSettings() {
        this.dispose();
        new CustomerAccountSettings();
    }
}