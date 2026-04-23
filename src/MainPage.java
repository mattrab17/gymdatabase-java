import javax.swing.*;

public class MainPage extends JFrame {
    private JPanel jpanel;

    public MainPage(){  //Class constructor
        this.setContentPane(this.jpanel);
        this.setTitle("MainMenu");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBounds(0,0,400,600);
        this.setVisible(true);

        //Action Listeners

    }

    public static void main(String[]args){ //Main Method

        Database.connect();
        Database.autoCloseDB();

        new MainPage();
    }
}
