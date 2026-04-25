
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

//Imports used to convert a char[] into bytes manually
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;

//Imports used for adding salts to the password
import javax.crypto.spec.GCMParameterSpec;
import java.security.SecureRandom;


public class EncryptionManager {

    public static String encryptionKey = "9879e7dfe2"; // Encryption key can be changed

    private static SecretKeySpec secretKey;
    private static byte[] key;

    public static void setKey() {
        MessageDigest sha = null;
        try {
            key = encryptionKey.getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            secretKey = new SecretKeySpec(key, "AES");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

     /* How the new encrypt method works:

        1. A user enters a password(unencryptedPassword) at sign up
        2. The user's password is stored as a char[] for Memory Control purposes
            Why? -> Arrays like char[] can be manually overwritten with zeros
                    the exact millisecond after you are done using them
            What this means -> If a hacker does a "Memory Dump", or takes a snapshot of
                                a user's RAM, they will not see anything currently on the Heap
                                because the char[] that originally contained the user's password
                                is completely in zeros

            In contrast: We DO NOT EVER store passwords in String format

                      Why? -> Strings are immutable/permanent.
                      What this means -> Once a String is created, it stays in a special memory area
                                         called "The String Pool" until the computer system's "Garbage Collector"
                                         decides to delete it.
                                      -> If a hacker performs a "Memory Dump", they will see your password
        */

    public static String encrypt(char[] stringToEncrypt) {
        byte[] bytePayload = null;
        try {
            setKey(); //Prepares the digital "house key"
            //Cipher DEFINITION -> a mathematical algorithm that converts plain text into encrypted text
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding"); //Picks the lock type
            //Analogy: AES (Type of metal), GCM (the way the lock mechanism turns,
            //         NoPadding (does not add extra bytes to the text)
            // 1. Generates a 12 byte random salt (Nonce)
            byte[] nonce = new byte[12];
            new SecureRandom().nextBytes(nonce);

            // 2. Set up the cipher with the key AND the random salt
            GCMParameterSpec spec = new GCMParameterSpec(128, nonce);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, spec); //initializes the cipher in locking mode

            //3. Converts char[] to byte[]
            ByteBuffer bb = StandardCharsets.UTF_8.encode(CharBuffer.wrap(stringToEncrypt));
            //ByteBuffer -> takes holds the translated version of our password in encoded UTF-8 byte form
            bytePayload = new byte[bb.remaining()];
            //bytePayload -> a temporary container (byte array) that holds the size of the data
            bb.get(bytePayload);
            //This puts the bytes from the buffer into BytePayLoad


            //4. Encrypts the bytes
            byte[] cipherText = cipher.doFinal(bytePayload);
            //encryptedBytes -> An array with scrambled ciphertext(random binary data).
            //-> it was scrambled using the AES mathematical algorithm

            //5. Combine the salt + ciphertext so we can decrypt it later
            byte[] combined = new byte[nonce.length + cipherText.length];
            System.arraycopy(nonce, 0, combined, 0, nonce.length);
            System.arraycopy(cipherText, 0, combined, nonce.length, cipherText.length);

            return Base64.getEncoder().encodeToString(combined);
            //This line of code represents Binary data using 64 safe characters like A-Z, a-z, etc
            //It translates the scrambled random binary data from cipherText into a clean encrypted String like 8jkgfijEJF305

        } catch (Exception e) {
            System.out.println("Error while encrypting: " + e.toString());


        } finally { //Whether the encryption worked or crashed, this code will always execute

            if (bytePayload != null) { //if bytepayload contains our sensitive data in plain text
                Arrays.fill(bytePayload, (byte) 0); //ERASE THE DATA FROM RAM/MEMORY
            }
        }

        return null;

    }

    public static char[] decrypt(String stringToDecrypt) {
        byte[] combined = null;
        byte[] decryptedBytes = null;
        char[] passwordChars = null;

        try {
            setKey();

            // 1. Decode Base64
            combined = Base64.getDecoder().decode(stringToDecrypt);

            // 2. Separate Nonce/salt and CipherText
            byte[] nonce = new byte[12];
            byte[] cipherText = new byte[combined.length - 12];
            System.arraycopy(combined, 0, nonce, 0, 12);
            System.arraycopy(combined, 12, cipherText, 0, cipherText.length);

            // 3. Set up Cipher
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            GCMParameterSpec spec = new GCMParameterSpec(128, nonce);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, spec);

            // 4. Decrypt to byte array
            decryptedBytes = cipher.doFinal(cipherText);

            // 5. Direct translation: byte[] -> char[]
            CharBuffer cb = StandardCharsets.UTF_8.decode(ByteBuffer.wrap(decryptedBytes));
            passwordChars = new char[cb.remaining()];
            cb.get(passwordChars);

            return passwordChars;

        } catch (Exception e) {
            System.out.println("Error while decrypting: " + e.toString());
        } finally {

            if (combined != null) Arrays.fill(combined, (byte) 0);
            if (decryptedBytes != null) Arrays.fill(decryptedBytes, (byte) 0);
        }
        return null;
    }
}