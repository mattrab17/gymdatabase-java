public class ManualEmployeeAccCreation {

    public static void main (String[]args) {
        //1. PUT THE PASSWORD YOU WANT INTO " "
        String password = "cat";



        //Encryption Code:
        char[] password1 = password.toCharArray();
        EncryptionManager.encrypt(password1);


        //2. THE ENCRYPTED PASSWORD WILL SHOW UP IN YOUR CONSOLE
        //PUT THAT PASSWORD INTO THE SQL DATABASE
        /*

        EX:
        INSERT INTO JavaGymDatabase.Employees (username, password, full_name, is_admin, employeetypeid)
        VALUES ('GymTrainer','1yqKMa/m8MujyFA8juG9IFkJKUTkuPVdxdcDeOqOl4J8','admin',1,3);
         */
        System.out.println("Encrypted Password: " + "\n" + password1);
    }
}
