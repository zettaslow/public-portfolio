// Brandon Smith
// CS 460
// 1/21/2015
import java.util.Scanner;


public class VigenereCipher
{
    private static char[] plain;
    private static char[] keyArray;
    
    public static void main(String[] args) throws Exception
    {
        String key;
        String plaintext;
        char[][] cipher;
        Scanner kb = new Scanner(System.in);
        
        //Take in user input of password and key
        System.out.println("Program written by Brandon Smith, Bronco ID: 009374833");
        System.out.println("Enter a password to encrypt:");
        plaintext = kb.nextLine();
        System.out.println("Enter a key to encrypt your password with:");
        key = kb.nextLine();
        
        plain = getCharacters(plaintext);
        keyArray = getKeyChars(key);
        
        //generate cipher table for error checking purposes. Not actually a secure thing to do.
        cipher = generateCipher(key, plaintext);
        printCipher(cipher);
        
        char[] encipheredText = encipher(plaintext, key, cipher);
        System.out.println("Chosen Password: " + plaintext);
        System.out.print("Encrypted password: ");
        System.out.println(encipheredText);
        System.out.print("Decrypted password: ");
        System.out.println(decipher(new String(encipheredText), key));
        
        kb.close();
    }
    
    //array of chars only the length of what is used in plaintext
    public static char[] getCharacters(String plaintext)
    {
        //size 27 to incorporate space character
        boolean[] isPresent = new boolean[27];
        int charCounter = 0;
        //iterate through string and generate boolean array of characters for cipher
        //ASCII: uppercase letters = 65-90, lowercase = 97-122
        for(int i = 0; i < plaintext.length(); i++)
        {
            if(plaintext.charAt(i) == ' ')
            {
                if(isPresent[26] == false)
                    charCounter++;
                isPresent[26] = true;
            }
            else
            {
                int asciiVal = (int)(plaintext.charAt(i));
                if(asciiVal >= 65 && asciiVal <= 90)
                {
                    if(isPresent[asciiVal - 65] == false)
                        charCounter++;
                    isPresent[asciiVal - 65] = true;
                }
                else if(asciiVal >= 97 && asciiVal <=122)
                {
                    if(isPresent[asciiVal - 97] == false)
                        charCounter++;
                    isPresent[asciiVal - 97] = true;
                }
            }
        }
        
        //generate characterArray from boolean presence array
        
        //sanity check
        if(charCounter > 27)
        {
            System.out.println("something went wrong");
            return null;
        }
        else
        {
            char[] result = new char[charCounter];
            int resultIndex = 0;
            for(int i = 0; i < isPresent.length; i++)
            {
                if(isPresent[i] == true)
                {
                    if(i == 26)
                    {
                        result[resultIndex] = ' ';
                        resultIndex++;
                        continue;
                    }
                    char[] intToChar = Character.toChars(i+65);
                    result[resultIndex] = intToChar[0];
                    resultIndex++;
                }
            }
            return result;
        }    
    }
    
    public static char[] getKeyChars(String key)
    {
        return key.toCharArray();     
    }
    
    //Builds cipher 2d array of chars. 
    public static char[][] generateCipher(String key, String plaintext)
    {
        char[] keyChars = getKeyChars(key);
        char[] plainChars = getCharacters(plaintext);
        
        //generate cipherChart
        char[][] cipherChart = new char[plainChars.length][keyChars.length];
        
        //fill cipherChart
        for(int row = 0; row < plainChars.length; row++)
        {
            for(int col = 0; col < keyChars.length; col++)
            {
                int original = (int)plainChars[row];
                int keyChar = (int)keyChars[col];
                int cipheredChar = (original + keyChar) - 65;
                if (cipheredChar > 90)
                    cipheredChar = (cipheredChar % 90) + 64;
                cipherChart[row][col] = (char)(cipheredChar);
            }
        }
        return cipherChart;
    }
    
    public static void printCipher(char[][] cipherTable)
    {
        System.out.println("Rows: " + cipherTable.length + "\nColumns: " + cipherTable[0].length);
        
        for(int row = 0; row < cipherTable.length; row++)
        {
            for(int col = 0; col < cipherTable[row].length ; col++)
            {
                System.out.print(cipherTable[row][col] + "   ");
            }
            System.out.println();
        }
    }
    
    //returns earliest possible location in charArray of target letter
    public static int getIndex(char target, char[] charArray)
    {
        for(int i = 0; i < charArray.length; i++)
        {
            if(charArray[i] == target)
                return i;
        }
        return 26;
    }
    
    //enciphers plaintext according to key and cipher. Adds key offset to plaintext and
    //matches according to ciphertable
    public static char[] encipher(String plaintext, String key, char[][] cipher)
    {
        char[] cipherText = new char[plaintext.length()];
        int keyIndex = 0;
        for(int i = 0; i < plaintext.length(); i++)
        {
            int rowIndex = getIndex(plaintext.charAt(i), plain);
            int colIndex = getIndex(key.charAt(keyIndex), keyArray);
            cipherText[i] = cipher[rowIndex][colIndex];
            keyIndex++;
            if(keyIndex >= key.length())
                keyIndex = 0;
        }
         
        return cipherText;
    }
    
    //deciphers by reversing the mathematical operations used to encrypt the plaintext
    public static char[] decipher(String cipherText, String key)
    {
        char[] decipheredText = new char[cipherText.length()];
        int keyIndex = 0;
        for(int i = 0; i < cipherText.length(); i++)
        {
            int cipherVal = (int)cipherText.charAt(i);
            int keyVal = (int)key.charAt(keyIndex);
            decipheredText[i] = (char)((cipherVal - keyVal + 26) % 26 + 65);
            if((int)decipheredText[i] < 65)
                decipheredText[i] = (char)32;
            keyIndex++;
            if(keyIndex >= key.length())
                keyIndex = 0;
        }
        
        return decipheredText;     
    }
}
