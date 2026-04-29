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
            cancelMembershipBtn.addActionListener(e -> cancelMembership());
            viewAppointmentsBtn.addActionListener(e -> viewTrainerAppointments());
        }

        // fitness coach dashboard
        else if (type == 1) {
            title.setText("Fitness Coach Dashboard");

            JButton createClassBtn = new JButton("Create Fitness Class");
            JButton cancelClassBtn = new JButton("Cancel Fitness Class");

            panel.add(createClassBtn);
            panel.add(cancelClassBtn);

            createClassBtn.addActionListener(e -> openCreateClassPage());
            cancelClassBtn.addActionListener(e -> openCancelClassPage());
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
            acceptBtn.addActionListener(e -> updateAppointmentStatus("Accepted"));
            declineBtn.addActionListener(e -> updateAppointmentStatus("Declined"));
        }

        // admin dashboard
        else if (type == 3) {
            title.setText("Admin Dashboard");

            JButton createEmployeeBtn = new JButton("Create Employee");
            JButton modifyEmployeeBtn = new JButton("Modify Employee");
            JButton deleteEmployeeBtn = new JButton("Delete Employee");

            JButton modifyCustomerBtn = new JButton("Modify Customer");
            JButton deleteCustomerBtn = new JButton("Delete Customer");

            JButton resetPasswordBtn = new JButton("Reset Password");

            JButton deleteClassBtn = new JButton("Delete Class");
            JButton deleteAppointmentBtn = new JButton("Delete Appointment");
            JButton viewAppointmentsBtn = new JButton("View Appointments");

            JButton viewEmployeesBtn = new JButton("View Employees");
            JButton viewCustomersBtn = new JButton("View Customers");

            panel.add(createEmployeeBtn);
            panel.add(modifyEmployeeBtn);
            panel.add(deleteEmployeeBtn);

            panel.add(modifyCustomerBtn);
            panel.add(deleteCustomerBtn);

            panel.add(resetPasswordBtn);

            panel.add(deleteClassBtn);
            panel.add(deleteAppointmentBtn);
            panel.add(viewAppointmentsBtn);

            panel.add(viewEmployeesBtn);
            panel.add(viewCustomersBtn);

            createEmployeeBtn.addActionListener(e -> createEmployee());
            modifyEmployeeBtn.addActionListener(e -> modifyEmployee());
            deleteEmployeeBtn.addActionListener(e -> deleteEmployee());

            modifyCustomerBtn.addActionListener(e -> modifyCustomer());
            deleteCustomerBtn.addActionListener(e -> deleteCustomer());

            resetPasswordBtn.addActionListener(e -> resetPassword());

            deleteClassBtn.addActionListener(e -> openCancelClassPage());
            deleteAppointmentBtn.addActionListener(e -> deleteAppointment());
            viewAppointmentsBtn.addActionListener(e -> viewAppointments());

            viewEmployeesBtn.addActionListener(e -> viewEmployees());
            viewCustomersBtn.addActionListener(e -> viewMembers());
        }

        setContentPane(panel);
        setVisible(true);
    }

    // Cancel Membership
    private void cancelMembership() {

        JFrame frame = new JFrame("Cancel Membership");
        frame.setSize(300, 200);
        frame.setLocationRelativeTo(null);

        JPanel p = new JPanel(new GridLayout(2,2,10,10));

        JTextField customerId = new JTextField();
        JButton cancelBtn = new JButton("Cancel");

        p.add(new JLabel("Customer ID:"));
        p.add(customerId);
        p.add(new JLabel(""));
        p.add(cancelBtn);

        cancelBtn.addActionListener(e -> {
            try {

                String sql =
                        "UPDATE JavaGymDatabase.Customers " +
                                "SET membership_id = NULL " +
                                "WHERE customer_id = ?";

                PreparedStatement stmt = Database.connection.prepareStatement(sql);

                stmt.setInt(1, Integer.parseInt(customerId.getText()));

                int rows = stmt.executeUpdate();

                if(rows > 0){
                    JOptionPane.showMessageDialog(frame,
                            "Membership Cancelled");
                    frame.dispose();
                }
                else{
                    JOptionPane.showMessageDialog(frame,
                            "Customer Not Found");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame,
                        "Error cancelling membership");
            }
        });

        frame.setContentPane(p);
        frame.setVisible(true);
    }



    // View trainer appointments
    private void viewTrainerAppointments() {

        try {

            String sql =
                    "SELECT a.appointment_id, c.full_name, cl.class_name, a.status " +
                            "FROM JavaGymDatabase.Appointments a " +
                            "JOIN JavaGymDatabase.Customers c ON a.customer_id = c.customer_id " +
                            "JOIN JavaGymDatabase.Classes cl ON a.classes_id = cl.classes_id " +
                            "JOIN JavaGymDatabase.Employees e ON a.employee_id = e.employee_id " +
                            "WHERE e.employeetypeid = 4";

            PreparedStatement stmt = Database.connection.prepareStatement(sql);

            ResultSet rs = stmt.executeQuery();

            StringBuilder output =
                    new StringBuilder("TRAINER APPOINTMENTS:\n\n");

            while(rs.next()){

                output.append("Appointment ID: ")
                        .append(rs.getInt("appointment_id"))
                        .append(" | Customer: ")
                        .append(rs.getString("full_name"))
                        .append(" | Class: ")
                        .append(rs.getString("class_name"))
                        .append(" | Status: ")
                        .append(rs.getString("status"))
                        .append("\n");
            }

            JOptionPane.showMessageDialog(this, output.toString());

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error loading appointments");
        }
    }

    // Create class page

    private void openCreateClassPage() {

        JFrame frame = new JFrame("Create Class");
        frame.setSize(400, 400);
        frame.setLocationRelativeTo(null);

        JPanel p = new JPanel(new GridLayout(6,2,10,10));

        JTextField className = new JTextField();
        JTextField date = new JTextField();
        JTextField time = new JTextField();
        JTextField employeeId = new JTextField();
        JTextField groupClass = new JTextField();

        JButton submit = new JButton("Create");

        p.add(new JLabel("Class Name:"));
        p.add(className);

        p.add(new JLabel("Date (YYYY-MM-DD):"));
        p.add(date);

        p.add(new JLabel("Time (HH:MM:SS):"));
        p.add(time);

        p.add(new JLabel("Employee ID:"));
        p.add(employeeId);

        p.add(new JLabel("Is Group Class (0/1):"));
        p.add(groupClass);

        p.add(new JLabel(""));
        p.add(submit);

        submit.addActionListener(e -> {
            try {
                String sql = "INSERT INTO JavaGymDatabase.Classes " +
                        "(class_name, date, time, employee_id, isGroupClass) " +
                        "VALUES (?, ?, ?, ?, ?)";

                PreparedStatement stmt = Database.connection.prepareStatement(sql);

                stmt.setString(1, className.getText());
                stmt.setString(2, date.getText());
                stmt.setString(3, time.getText());
                stmt.setInt(4, Integer.parseInt(employeeId.getText()));
                stmt.setInt(5, Integer.parseInt(groupClass.getText()));

                stmt.executeUpdate();

                JOptionPane.showMessageDialog(frame,
                        "Class Created Successfully");

                frame.dispose();

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame,
                        "Failed to create class");
            }
        });

        frame.setContentPane(p);
        frame.setVisible(true);
    }


    // Cancel class page

    private void openCancelClassPage() {

        JFrame frame = new JFrame("Cancel Class");
        frame.setSize(300, 200);
        frame.setLocationRelativeTo(null);

        JPanel p = new JPanel(new GridLayout(2,2,10,10));

        JTextField classId = new JTextField();
        JButton deleteBtn = new JButton("Delete");

        p.add(new JLabel("Class ID:"));
        p.add(classId);
        p.add(new JLabel(""));
        p.add(deleteBtn);

        deleteBtn.addActionListener(e -> {
            try {
                String sql =
                        "DELETE FROM JavaGymDatabase.Classes WHERE classes_id = ?";

                PreparedStatement stmt =
                        Database.connection.prepareStatement(sql);

                stmt.setInt(1,
                        Integer.parseInt(classId.getText()));

                int rows = stmt.executeUpdate();

                if(rows > 0){
                    JOptionPane.showMessageDialog(frame,
                            "Class Deleted");
                    frame.dispose();
                } else {
                    JOptionPane.showMessageDialog(frame,
                            "Class Not Found");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame,
                        "Error deleting class");
            }
        });

        frame.setContentPane(p);
        frame.setVisible(true);
    }


    // View methods


    private void viewMembers() {
        try {
            String sql =
                    "SELECT customer_id, full_name, username FROM JavaGymDatabase.Customers";

            PreparedStatement stmt = Database.connection.prepareStatement(sql);

            ResultSet rs = stmt.executeQuery();

            StringBuilder output =
                    new StringBuilder("CUSTOMERS:\n\n");

            while (rs.next()) {
                output.append(rs.getInt("customer_id"))
                        .append(" | ")
                        .append(rs.getString("full_name"))
                        .append(" | ")
                        .append(rs.getString("username"))
                        .append("\n");
            }

            JOptionPane.showMessageDialog(this, output.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void viewEmployees() {
        try {
            String sql =
                    "SELECT employee_id, full_name, username FROM JavaGymDatabase.Employees";

            PreparedStatement stmt =
                    Database.connection.prepareStatement(sql);

            ResultSet rs = stmt.executeQuery();

            StringBuilder output =
                    new StringBuilder("EMPLOYEES:\n\n");

            while (rs.next()) {
                output.append(rs.getInt("employee_id"))
                        .append(" | ")
                        .append(rs.getString("full_name"))
                        .append(" | ")
                        .append(rs.getString("username"))
                        .append("\n");
            }

            JOptionPane.showMessageDialog(this, output.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void viewClasses() {
        try {
            String sql =
                    "SELECT class_name, date, time FROM JavaGymDatabase.Classes";

            PreparedStatement stmt = Database.connection.prepareStatement(sql);

            ResultSet rs = stmt.executeQuery();

            StringBuilder output =
                    new StringBuilder("CLASSES:\n\n");

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

    private void viewAppointments() {
        try {
            String sql =
                    "SELECT appointment_id, status FROM JavaGymDatabase.Appointments";

            PreparedStatement stmt = Database.connection.prepareStatement(sql);

            ResultSet rs = stmt.executeQuery();

            StringBuilder output =
                    new StringBuilder("APPOINTMENTS:\n\n");

            while (rs.next()) {
                output.append(rs.getInt("appointment_id"))
                        .append(" | ")
                        .append(rs.getString("status"))
                        .append("\n");
            }

            JOptionPane.showMessageDialog(this, output.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // For decline and accept for trainer
    private void updateAppointmentStatus(String status) {

        JFrame frame = new JFrame(status + " Appointment");
        frame.setSize(300, 200);
        frame.setLocationRelativeTo(null);

        JPanel p = new JPanel(new java.awt.GridLayout(2,2,10,10));

        JTextField appointmentId = new JTextField();
        JButton submitBtn = new JButton("Submit");

        p.add(new JLabel("Appointment ID:"));
        p.add(appointmentId);
        p.add(new JLabel(""));
        p.add(submitBtn);

        submitBtn.addActionListener(e -> {
            try {

                String sql =
                        "UPDATE JavaGymDatabase.Appointments " +
                                "SET status = ? " +
                                "WHERE appointment_id = ?";

                PreparedStatement stmt = Database.connection.prepareStatement(sql);

                stmt.setString(1, status);
                stmt.setInt(2,
                        Integer.parseInt(appointmentId.getText()));

                int rows = stmt.executeUpdate();

                if(rows > 0){
                    JOptionPane.showMessageDialog(frame,
                            "Appointment " + status);
                    frame.dispose();
                } else {
                    JOptionPane.showMessageDialog(frame,
                            "Appointment Not Found");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame,
                        "Error updating appointment");
            }
        });

        frame.setContentPane(p);
        frame.setVisible(true);
    }
    //Create employee for admin dashboard
    private void createEmployee() {
        try {
            String username = JOptionPane.showInputDialog("Username:");
            String password = JOptionPane.showInputDialog("Password:");
            String fullName = JOptionPane.showInputDialog("Full Name:");
            String type = JOptionPane.showInputDialog("Type:\n1 Coach\n2 Receptionist\n3 Admin\n4 Trainer");

            String sql = "INSERT INTO JavaGymDatabase.Employees " +
                    "(username,password,full_name,employeetypeid) VALUES (?,?,?,?)";

            PreparedStatement stmt = Database.connection.prepareStatement(sql);

            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, fullName);
            stmt.setInt(4, Integer.parseInt(type));

            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this,"Employee Created");

        } catch(Exception e){
            e.printStackTrace();
        }
    }
    //Delete employee for admin dashboard
    private void deleteEmployee() {
        try {
            String id = JOptionPane.showInputDialog("Employee ID:");

            String sql = "DELETE FROM JavaGymDatabase.Employees WHERE employee_id=?";

            PreparedStatement stmt = Database.connection.prepareStatement(sql);
            stmt.setInt(1,Integer.parseInt(id));

            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this,"Employee Deleted");

        } catch(Exception e){
            e.printStackTrace();
        }
    }
    // Modify employee for admin dashboard
    private void modifyEmployee() {
        try {
            String id = JOptionPane.showInputDialog("Employee ID:");
            String fullName = JOptionPane.showInputDialog("New Full Name:");

            String sql = "UPDATE JavaGymDatabase.Employees SET full_name=? WHERE employee_id=?";

            PreparedStatement stmt = Database.connection.prepareStatement(sql);
            stmt.setString(1,fullName);
            stmt.setInt(2,Integer.parseInt(id));

            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this,"Employee Updated");

        } catch(Exception e){
            e.printStackTrace();
        }
    }
    //reset pw for admin dashboard
    private void resetPassword() {
        try {
            String id = JOptionPane.showInputDialog("Employee ID:");
            String newPass = JOptionPane.showInputDialog("New Password:");

            String sql = "UPDATE JavaGymDatabase.Employees SET password=? WHERE employee_id=?";

            PreparedStatement stmt = Database.connection.prepareStatement(sql);
            stmt.setString(1,newPass);
            stmt.setInt(2,Integer.parseInt(id));

            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this,"Password Reset");

        } catch(Exception e){
            e.printStackTrace();
        }
    }
    //Delete appointment for admin dashboard
    private void deleteAppointment() {
        try {
            String id = JOptionPane.showInputDialog("Appointment ID:");

            String sql = "DELETE FROM JavaGymDatabase.Appointments WHERE appointment_id=?";

            PreparedStatement stmt = Database.connection.prepareStatement(sql);
            stmt.setInt(1,Integer.parseInt(id));

            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this,"Appointment Deleted");

        } catch(Exception e){
            e.printStackTrace();
        }
    }
    //Modify customer for admin dashboard
    private void modifyCustomer() {
        try {
            String id = JOptionPane.showInputDialog("Customer ID:");
            String fullName = JOptionPane.showInputDialog("New Full Name:");

            String sql =
                    "UPDATE JavaGymDatabase.Customers " +
                            "SET full_name=? WHERE customer_id=?";

            PreparedStatement stmt = Database.connection.prepareStatement(sql);

            stmt.setString(1, fullName);
            stmt.setInt(2, Integer.parseInt(id));

            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this,
                    "Customer Updated");

        } catch(Exception e){
            e.printStackTrace();
        }
    }
    //delete customer for admin dashboard
    private void deleteCustomer() {
        try {
            String id = JOptionPane.showInputDialog("Customer ID:");

            String sql =
                    "DELETE FROM JavaGymDatabase.Customers " +
                            "WHERE customer_id=?";

            PreparedStatement stmt = Database.connection.prepareStatement(sql);

            stmt.setInt(1, Integer.parseInt(id));

            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this,
                    "Customer Deleted");

        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
