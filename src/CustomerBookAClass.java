import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class CustomerBookAClass extends JFrame {
    private JPanel panel;
    private JButton goHomeButton;
    private JComboBox classesCB;
    private JButton confirmAppointmentButton;

    ArrayList<Integer> classIdStorage = new ArrayList<>();
    ArrayList<Integer> employeeIdStorage = new ArrayList<>();





    public CustomerBookAClass() {
        this.setContentPane(this.panel);
        this.setTitle("MainMenu");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800,600);
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        //Methods

        fillComboBoxWithClasses();

        //Action Listeners
        actionListenerConfirmAppointmentButton();
        goHomeButton.addActionListener(new ActionListener() {
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

    public void fillComboBoxWithClasses() {

        try {
            String query = """
                SELECT JavaGymDatabase.Classes.classes_id, JavaGymDatabase.Classes.class_name,
                JavaGymDatabase.Employees.employee_id, JavaGymDatabase.Employees.full_name, JavaGymDatabase.Classes.time
                FROM JavaGymDatabase.Classes
                JOIN JavaGymDatabase.Employees ON JavaGymDatabase.Classes.employee_id = JavaGymDatabase.Employees.employee_id
                """;

            PreparedStatement stm = Database.connection.prepareStatement(query);
            ResultSet rs = stm.executeQuery();

            classesCB.removeAllItems();
            classesCB.addItem("No Class Selected");

            classIdStorage.clear();
            employeeIdStorage.clear();

            while(rs.next()) {
                String className = rs.getString("class_name");
                String host = rs.getString("full_name");
                int classId = rs.getInt("classes_id");
                int employeeId = rs.getInt("employee_id");
                String time = rs.getString("time");

                classIdStorage.add(classId);
                employeeIdStorage.add(employeeId);

                classesCB.addItem("Class: " + className + " | " + "Hosted By: " + host + " | " + "Time: " + time);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Action Listeners

    public void actionListenerConfirmAppointmentButton() {
        confirmAppointmentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                confirmAppointmentButton();
            }
        });
    }

    public void confirmAppointmentButton() {

        if(classesCB.getSelectedItem().equals("Class Not Selected")) {
            JOptionPane.showMessageDialog(null, "You Must Select An Appointment To Book");
            return;
        } else {
            successfullyConfirmAppointment();

        }
    }

    public void successfullyConfirmAppointment() {

        int userId = UserSession.userId;
        int index = classesCB.getSelectedIndex() - 1;

        int classId = classIdStorage.get(index);
        int employeeId = employeeIdStorage.get(index);

        try {
            String query = """
            INSERT INTO JavaGymDatabase.Appointments
            (customer_id, classes_id, employee_id, status)
            VALUES (?, ?, ?, ?)
        """;

            PreparedStatement stm = Database.connection.prepareStatement(query);
            stm.setInt(1, userId);
            stm.setInt(2, classId);
            stm.setInt(3, employeeId);
            stm.setString(4, "Scheduled");

            stm.executeUpdate();

            JOptionPane.showMessageDialog(null, "Appointment Booked!");
        } catch(Exception e) {
            if (e.getMessage().toLowerCase().contains("duplicate")
                    || e.getMessage().toLowerCase().contains("unique_booking")) {

                JOptionPane.showMessageDialog(null, "You already booked this class.");

            } else {
                e.printStackTrace();
            }
        }

    }





}

