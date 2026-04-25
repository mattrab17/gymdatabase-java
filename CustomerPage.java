import javax.swing.*;

public class CustomerPage extends JFrame {

    public CustomerPage() {
        setTitle("Customer Dashboard");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(new JLabel("Customer Page Placeholder", SwingConstants.CENTER));
        setLocationRelativeTo(null);
        setVisible(true);
    }
}