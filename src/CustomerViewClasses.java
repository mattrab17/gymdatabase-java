import net.proteanit.sql.DbUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CustomerViewClasses extends JFrame {

    private JPanel panel;
    private JButton goHomeButton;
    private JTable scheduledClassesTable;
    private JTable classHistoryTable;


    public CustomerViewClasses() {
        this.setContentPane(panel);
        this.setTitle("Customer Page");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1100,600);
        setLocationRelativeTo(null); // centers window
        this.setVisible(true);

        //Methods
        populateScheduledClassesTable();
        populateClassHistoryTable();

        //Action Listeners
        actionListenerGoHomeButton();
    }

    public void populateScheduledClassesTable() {

        int currentUserId = UserSession.userId;
        try {

            String query = """
                SELECT JavaGymDatabase.Classes.class_name, JavaGymDatabase.Classes.date, JavaGymDatabase.Classes.time,
                       JavaGymDatabase.Employees.full_name, JavaGymDatabase.Classes.isGroupClass, JavaGymDatabase.Appointments.status
                FROM JavaGymDatabase.Appointments
                JOIN JavaGymDatabase.Classes ON JavaGymDatabase.Appointments.classes_id = JavaGymDatabase.Classes.classes_id
                JOIN JavaGymDatabase.Employees ON JavaGymDatabase.Appointments.employee_id = JavaGymDatabase.Employees.employee_id
                WHERE JavaGymDatabase.Appointments.customer_id = ?
                """;

            PreparedStatement stm = Database.connection.prepareStatement(query);
            stm.setInt(1, currentUserId);
            ResultSet result = stm.executeQuery();

            //JTable Column Names
            String[] columns = {"Class Name", "Date", "Time", "Instructor", "Group Class?"};
            javax.swing.table.DefaultTableModel model =
                    new javax.swing.table.DefaultTableModel(columns, 0);

            //Fill the table with attributes from database
            while(result.next()) {
                String status = result.getString("status");
                if(status.equals("Scheduled")) {
                    int groupClass = result.getInt("isGroupClass");
                    String groupClassFinal = "";
                    if(groupClass == 0) {
                        groupClassFinal = "No";
                    } else{
                        groupClassFinal = "Yes";
                    }
                    String className = result.getString("class_name");
                    String date = result.getString("date");
                    String time = result.getString("time");
                    String instructor = result.getString("full_name");

                    Object[] row = {className, date, time, instructor, groupClassFinal};
                    model.addRow(row);
                }
            }

            //Set the table model
            scheduledClassesTable.setModel(model);



        } catch(Exception e) {
            System.out.println(e);
        }
    }

    public void populateClassHistoryTable() {
        int currentUserId = UserSession.userId;
        try {

            String query = """
                SELECT JavaGymDatabase.Classes.class_name, JavaGymDatabase.Classes.date, JavaGymDatabase.Classes.time,
                       JavaGymDatabase.Employees.full_name, JavaGymDatabase.Classes.isGroupClass, JavaGymDatabase.Appointments.status
                FROM JavaGymDatabase.Appointments
                JOIN JavaGymDatabase.Classes ON JavaGymDatabase.Appointments.classes_id = JavaGymDatabase.Classes.classes_id
                JOIN JavaGymDatabase.Employees ON JavaGymDatabase.Appointments.employee_id = JavaGymDatabase.Employees.employee_id
                WHERE JavaGymDatabase.Appointments.customer_id = ?
                """;

            PreparedStatement stm = Database.connection.prepareStatement(query);
            stm.setInt(1, currentUserId);
            ResultSet result = stm.executeQuery();

            //JTable Column Names
            String[] columns = {"Class Name", "Date", "Time", "Instructor", "Group Class?"};
            javax.swing.table.DefaultTableModel model =
                    new javax.swing.table.DefaultTableModel(columns, 0);

            //Fill the table with attributes from database
            while(result.next()) {
                String status = result.getString("status");
                if(status.equals("Completed")) {
                    int groupClass = result.getInt("isGroupClass");
                    String groupClassFinal = "";
                    if(groupClass == 0) {
                        groupClassFinal = "No";
                    } else{
                        groupClassFinal = "Yes";
                    }
                    String className = result.getString("class_name");
                    String date = result.getString("date");
                    String time = result.getString("time");
                    String instructor = result.getString("full_name");

                    Object[] row = {className, date, time, instructor, groupClassFinal};
                    model.addRow(row);
                }
            }

            //Set the table model
            classHistoryTable.setModel(model);



        } catch(Exception e) {
            System.out.println(e);
        }
    }


    //Action Listeners

    public void actionListenerGoHomeButton() {
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

}


