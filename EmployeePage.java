import javax.swing.*;
import java.awt.*;

public class EmployeePage extends JFrame {

    private JPanel panel;

    public EmployeePage(int type) {

        setTitle("Employee Dashboard");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        panel = new JPanel();
        panel.setLayout(new GridLayout(0, 1, 10, 10));

        JLabel title = new JLabel("", SwingConstants.CENTER);
        panel.add(title);

        // Shows diff dashboards depending on the employeetypeid
        if (type == 2) {
            title.setText("Receptionist Dashboard");
            panel.add(new JButton("View Members"));
            panel.add(new JButton("Cancel Membership"));
            panel.add(new JButton("View Classes"));
            panel.add(new JButton("View Trainer Appointments"));

        }
        else if (type == 1) {
            title.setText("Fitness Coach Dashboard");
            panel.add(new JButton("Create Fitness Class"));
            panel.add(new JButton("Cancel Fitness Class"));
        }
        else if (type == 4) {
            title.setText("Trainer Dashboard");
            panel.add(new JButton("View Appointments"));
            panel.add(new JButton("Accept Appointment"));
            panel.add(new JButton("Decline Appointment"));
            panel.add(new JButton("Cancel Appointment"));
        }
        else if (type == 3) {
            title.setText("Admin Dashboard");
            panel.add(new JButton("View All Employees"));
            panel.add(new JButton("View All Customers"));
            panel.add(new JButton("System Settings"));
        }


        setContentPane(panel);
        setVisible(true);
    }
}