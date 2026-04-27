import javax.swing.*;
import java.awt.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class EmployeePage extends JFrame {

    private JPanel panel;
    private JLabel title;

    public EmployeePage(int type) {

        setTitle("Employee Dashboard");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        panel = new JPanel();
        panel.setLayout(new GridLayout(0, 1, 10, 10));

        title = new JLabel("", SwingConstants.CENTER);
        panel.add(title);


        // receptionist dashboard
        if (type == 2) {
            title.setText("Receptionist Dashboard");

            JButton viewMembersBtn = new JButton("View Members");
            JButton cancelMembershipBtn = new JButton("Cancel Membership");
            JButton viewClassesBtn = new JButton("View Classes");
            JButton viewAppointmentsBtn = new JButton("View Trainer Appointments");

            panel.add(viewMembersBtn);
            panel.add(cancelMembershipBtn);
            panel.add(viewClassesBtn);
            panel.add(viewAppointmentsBtn);

            viewMembersBtn.addActionListener(e -> viewMembers());
            viewClassesBtn.addActionListener(e -> viewClasses());
        }

        // fitness coach dashboard
        else if (type == 1) {
            title.setText("Fitness Coach Dashboard");

            JButton createClassBtn = new JButton("Create Fitness Class");
            JButton cancelClassBtn = new JButton("Cancel Fitness Class");

            panel.add(createClassBtn);
            panel.add(cancelClassBtn);

            createClassBtn.addActionListener(e -> createClass());
        }


        // trainer dashboard
        else if (type == 4) {
            title.setText("Trainer Dashboard");

            JButton viewAppointmentsBtn = new JButton("View Appointments");
            JButton acceptBtn = new JButton("Accept Appointment");
            JButton declineBtn = new JButton("Decline Appointment");

            panel.add(viewAppointmentsBtn);
            panel.add(acceptBtn);
            panel.add(declineBtn);

            viewAppointmentsBtn.addActionListener(e -> viewAppointments());
        }


        // admin dashboard
        else if (type == 3) {
            title.setText("Admin Dashboard");

            JButton viewEmployeesBtn = new JButton("View All Employees");
            JButton viewCustomersBtn = new JButton("View All Customers");
            JButton systemSettingsBtn = new JButton("System Settings");

            panel.add(viewEmployeesBtn);
            panel.add(viewCustomersBtn);
            panel.add(systemSettingsBtn);

            viewEmployeesBtn.addActionListener(e -> viewEmployees());
            viewCustomersBtn.addActionListener(e -> viewMembers());
        }

        setContentPane(panel);
        setVisible(true);
    }




    // methods for interaction w/ database
    // view customers
    private void viewMembers() {
        try {
            String sql = "SELECT customer_id, full_name, username FROM JavaGymDatabase.Customers";

            PreparedStatement stmt = Database.connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            StringBuilder output = new StringBuilder("CUSTOMERS:\n\n");

            while (rs.next()) {
                output.append("ID: ")
                        .append(rs.getInt("customer_id"))
                        .append(" | ")
                        .append(rs.getString("full_name"))
                        .append(" | ")
                        .append(rs.getString("username"))
                        .append("\n");
            }

            JOptionPane.showMessageDialog(this, output.toString());

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading customers");
        }
    }

    // view employees (Admin)
    private void viewEmployees() {
        try {
            String sql = "SELECT employee_id, full_name, username FROM JavaGymDatabase.Employees";

            PreparedStatement stmt = Database.connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            StringBuilder output = new StringBuilder("EMPLOYEES:\n\n");

            while (rs.next()) {
                output.append("ID: ")
                        .append(rs.getInt("employee_id"))
                        .append(" | ")
                        .append(rs.getString("full_name"))
                        .append(" | ")
                        .append(rs.getString("username"))
                        .append("\n");
            }

            JOptionPane.showMessageDialog(this, output.toString());

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading employees");
        }
    }

    // view classes
    private void viewClasses() {
        try {
            String sql = "SELECT class_name, date, time FROM JavaGymDatabase.Classes";

            PreparedStatement stmt = Database.connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            StringBuilder output = new StringBuilder("CLASSES:\n\n");

            while (rs.next()) {
                output.append(rs.getString("class_name"))
                        .append(" | ")
                        .append(rs.getDate("date"))
                        .append(" | ")
                        .append(rs.getTime("time"))
                        .append("\n");
            }

            JOptionPane.showMessageDialog(this, output.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // view appointments
    private void viewAppointments() {
        try {
            String sql = "SELECT appointment_id, status FROM JavaGymDatabase.Appointments";

            PreparedStatement stmt = Database.connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            StringBuilder output = new StringBuilder("APPOINTMENTS:\n\n");

            while (rs.next()) {
                output.append("ID: ")
                        .append(rs.getInt("appointment_id"))
                        .append(" | Status: ")
                        .append(rs.getString("status"))
                        .append("\n");
            }

            JOptionPane.showMessageDialog(this, output.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Example of create class
    private void createClass() {
        try {
            String sql = "INSERT INTO JavaGymDatabase.Classes (class_name, date, time, employee_id, isGroupClass) VALUES (?, ?, ?, ?, ?)";

            PreparedStatement stmt = Database.connection.prepareStatement(sql);

            stmt.setString(1, "Swimming");
            stmt.setString(2, "2026-05-01");
            stmt.setString(3, "06:00:00");
            stmt.setInt(4, UserSession.userId);
            stmt.setInt(5, 1);

            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Successfully created class.");

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error creating class. Please try again");
        }
    }
}
