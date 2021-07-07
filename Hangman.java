
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;


public class Hangman extends JFrame {

    private JFrame frame;
    private JLabel labelPic;
    private JTextField userInputTextField, wordDisplayTextField, resultDisplayTextField, guessDisplayTextField;
    private JButton enterButton;
    private JButton startButton;
    public char userInput;
    public int attempt;
    public String databaseWord;
    public String guessWord;
    public char [] displayWordArray;


    ImageIcon hangman0 = new ImageIcon("Hangman1.jpg");
    ImageIcon hangman1 = new ImageIcon("Hangman2.jpg");
    ImageIcon hangman2 = new ImageIcon("Hangman3.jpg");
    ImageIcon hangman3 = new ImageIcon("Hangman4.jpg");
    ImageIcon hangman4 = new ImageIcon("Hangman5.jpg");
    ImageIcon hangman5 = new ImageIcon("Hangman6.jpg");
    ImageIcon hangman6 = new ImageIcon("Hangman7.jpg");
    ImageIcon hangman7 = new ImageIcon("HangmanError.jpg");


    // Create constructor to for the JFrame
    public Hangman() {

        // Create new JFrame with the title of Hangman Game
        frame = new JFrame();
        frame.setTitle("Hangman Game");

        // Set the layout to FlowLayout
        frame.setLayout(new FlowLayout());
        // Set the exit method to exit when X button is pressed
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setLocationRelativeTo(null);

        // Create new JLabel to allow re-edits of hanging man picture
        labelPic = new JLabel();
        labelPic.setText("");
        frame.add(labelPic);


        // Add the pictures to the JFrame
        JPanel picturePanel = new JPanel();

        // Set the icon of the label to be the hanging post picture
        labelPic.setIcon(hangman0);


        // Add the pictures to the panel
        frame.add(picturePanel);

        // Create text field for the userInput at 10 columns long and uneditable (upon startup)
        userInputTextField = new JTextField(10);
        userInputTextField.setEditable(false);
        // Add text field to frame
        frame.add(userInputTextField);

        // Create text field to display the word spaces at 10 columns long and uneditable
        wordDisplayTextField = new JTextField(10);
        wordDisplayTextField.setEditable(false);
        // Add text field to frame
        frame.add(wordDisplayTextField);

        // Create button to initialize game with the label of "Start Game"
        startButton = new JButton("Start Game");
        startButton.addActionListener(new StartGameHandler());
        // Add button to the frame
        frame.add(startButton);

        // Create button to input the user-inputted letter
        enterButton = new JButton("Enter");
        enterButton.addActionListener(new EnterHandler());
        // Make in unusable at first
        enterButton.setEnabled(false);
        // Add button to the frame
        frame.add(enterButton);

        // Create text field to report to the user if their guess is right/wrong with 22 columns long
        resultDisplayTextField = new JTextField(22);
        resultDisplayTextField.setEditable(false);
        // Add text field to frame
        frame.add(resultDisplayTextField);

        // Create text field to report to the user all of their guesses
        guessDisplayTextField = new JTextField(22);
        guessDisplayTextField.setEditable(false);
        // Add text field to frame
        frame.add(guessDisplayTextField);

        // Make the frame visible to the user
        frame.setVisible(true);
        // Make the frame 600 width, and 300 height
        frame.setSize(600, 300);
    }


    // Create handler to create a new game when the user clicks the Start Game button

    public class StartGameHandler implements ActionListener {

        public void actionPerformed(ActionEvent e) {

            // Make the user input text field editable indicating the game has begun
            userInputTextField.setEditable(true);

            // Call the user_letter char variable and make it empty
            userInput = '\0';

            // Call the attempt integer variable and set it to zero
            attempt = 0;

            // Call getRandomWord method and place results into variable
            databaseWord = getRandomWord();

            // Call replaceCharacters method and place dashed characters array into array variable
            displayWordArray = replaceCharacters(databaseWord);

            // Call the displayWord method and put into GUI
            displayWord(displayWordArray);

            // Call guesses string and place label into variable
            guessWord = "Guesses:";

            // Display the label to the GUI
            guessDisplayTextField.setText(guessWord);

            // Reset the attempts back to 0
            attempt = 0;

            // Set the picture back to the original in case user plays more than once
            labelPic.setIcon(hangman0);

            // Re-enable the user to use the button indicating the game has begun
            enterButton.setEnabled(true);

        }
    }


    // Create handler for when the user clicks the Enter Word button

    public class EnterHandler implements ActionListener {

        public void actionPerformed(ActionEvent e) {


            // Initialize userInput to "A"
            userInput = 'A';

            // Call getUserInput method and place results of it to userInput variable
            userInput = getUserInput();

            // Display the guess to the GUI
            displayGuess(userInput, guessWord);

            // On the condition that the letter matches any letter in the word
            if (databaseWord.indexOf(userInput) != -1) {

                // Display that the guess was found
                resultDisplayTextField.setText(String.format("\'%c\' was found.", userInput));

                // Call the alterUserInput
                alterUserInput(databaseWord, displayWordArray, userInput);

                // Call the displayWord method
                displayWord(displayWordArray);

                // Under the condition that the user is a winner (using the called method)...
                if (isWinner(databaseWord, displayWordArray)) {

                    // Display to the user the word and that they won
                    resultDisplayTextField.setText(String.format("The word was %s: You Won!", databaseWord));

                    // Make the user input text box uneditable, indicating that the game is over
                    userInputTextField.setEditable(false);

                    // Shut down the enter button to indicate that the game has ended
                    enterButton.setEnabled(false);
                }
            }
            // On the condition that the letter DOES NOT match any letter in the word
            else {

                // Add +1 to the attempts
                attempt++;

                changePicture(attempt);


                // Display all the attempts made
                resultDisplayTextField.setText(String.format("\'%s\' was NOT found. %d attempts remaining.", userInput, (6 - attempt)));

                // Under the condition that the user has reached 6 attempts
                if (attempt == 6) {

                    // Display to the user that they have lost
                    resultDisplayTextField.setText(String.format("The word was %s: You Lost!", databaseWord));

                    // Set the user input text box to false indicating that the game has ended
                    userInputTextField.setEditable(false);

                    // Shut down the enter button to indicate that the game has ended
                    enterButton.setEnabled(false);
                }
            }


        }
    }




    // Create getRandomWord method to pick a random word from the database

    public static String getRandomWord() {

        // Create integer counter variable
        int i = 0;

        // Create string array made for 50 strings
        String[] wordList = new String[50];

        // Create new random object
        Random rand = new Random();

        // Generate random integers in range 0 to 49
        int rand_int = rand.nextInt(49);


        try {

            // Create file input stream object using database.txt file
            FileInputStream fis = new FileInputStream("database.txt");
            // Create file scanner object
            Scanner fscan = new Scanner(fis);

            // Create while loop to scan the next string
            while (fscan.hasNext()) {

                // Read the next string and place it into array at index i
                wordList[i] = fscan.next();

                // Add +1 to the counter
                i++;
            }

            // Output the answer word to the screen (for confirmation purposes only)
            System.out.println(wordList[rand_int]);

            // Close the file scanner
            fscan.close();

            // Create resultword variable and have string from array at a random index and place into variable
            String resultWord = wordList[rand_int];

            // Make the resultword all uppercase
            resultWord = resultWord.toUpperCase();

            // return the word
            return resultWord;
        }
        // When the file cannot be found
        catch (FileNotFoundException fnf_except) {

            // Tell the user by printing to the screen
            JOptionPane.showMessageDialog(null, "ERROR: The file could not be found.\n");
        }

        // Return "error" to the program
        return "ERROR";
    }



    // Create method that detects if a user is a winner

    public static boolean isWinner(String databaseWord, char[] display) {

        // Create new string from char array
        String compare_str = new String(display);

        // If the array characters and the original word, return the boolean
        return databaseWord.equals(compare_str);
    }



    // Create method to get the word entered into the user input

    public char getUserInput() {

        // Create boolean variable to detect if a character is valid input
        boolean is_valid = false;

        // Create a variable with a decoy character
        char letter = 'A';


        // Get the character from the userInputTextBox at index 0, place char into variable
        letter = userInputTextField.getText().charAt(0);

        // Under the condition that the letter is not alphabetic
        if (Character.isAlphabetic(letter) == false) {

            // Report to the user that they need to
            JOptionPane.showMessageDialog(null, "ERROR! Input must be a letter.\n");
        }

        // Return the validated user input value
        return Character.toUpperCase(letter);
    }


    // Create method to change the user input

    public static void alterUserInput(String databaseWord, char[] display, char letter) {

        for (int i = 0; i < databaseWord.length(); i++) {

            // Under the condition that the word's letter at position (index) i
            if (databaseWord.charAt(i) == letter) {

                // Set the character at index i of the array
                display[i] = letter;
            }
        }

    }


    // Create method to display the guess

    public void displayGuess(char currentGuess, String finalGuess) {

        // Add all the guesses so far, a space, and the current guess
        guessWord = String.format("%s %c", finalGuess, currentGuess);

        // Display all of the guesses so far
        guessDisplayTextField.setText(guessWord);

    }


    // Create method to display the word

    public void displayWord(char [] chars) {

        // Create empty string called output
        String output = "";

        // Create method to iterate through the buffer array
        for (int i = 0; i < chars.length; i++) {

            // Add char at array index i to output variable
            output = String.format("%s%c", output , chars[i] );
        }

        // Send output to the outputTextField
        wordDisplayTextField.setText(output);

    }


    // Create method to replace characters with dashes

    public static char[] replaceCharacters(String str) {

        // Declare a variable to store the array
        char [] copyOfStr = str.toCharArray();

        for (int i = 0; i < copyOfStr.length; i++) {

            // Under the condition that there is a letter in the array
            if (Character.isAlphabetic(copyOfStr[i]) == true) {

                // replace it with a dash
                copyOfStr[i] = '-';
            }

        }
        // Return the word with dashes in it
        return copyOfStr;
    }


    public void changePicture(int counter) {

        if (counter == 1) {
            labelPic.setIcon(hangman1);
        }
        else if (counter == 2) {
            labelPic.setIcon(hangman2);
        }
        else if (counter == 3) {
            labelPic.setIcon(hangman3);
        }
        else if (counter == 4) {
            labelPic.setIcon(hangman4);
        }
        else if (counter == 5) {
            labelPic.setIcon(hangman5);
        }
        else if (counter == 6) {
            labelPic.setIcon(hangman6);
        }
        else {
            labelPic.setIcon(hangman7);
        }

    }




}