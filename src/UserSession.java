public class UserSession {

    public static int userId;
    public static String userTable;

        public static void logout() {
            userId = -1;
            userTable = null;
        }

}
