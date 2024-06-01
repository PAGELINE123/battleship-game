/*
Name:
Battleship
-----
Programmer:
Mansour Abdelsalam
-----
Date finished:

-----
Description: 

*/

import java.io.*;
import java.util.Scanner;
import java.util.InputMismatchException;

public class Battleship{
    //global constants for readability
    static final int SIZE = 10;
    static final int PLAYER_BOARD = 1;
    static final int CPU_BOARD = 2;
    static final int PLAYER_SHOTS = 3;
    static final int CPU_SHOTS = 4;

    static final char EMPTY_CELL = '-';

    static final char DESTROYER_CELL = 'D';
    static final int DESTROYER_SIZE = 2;

    static final char CRUISER_CELL = 'C';
    static final int CRUISER_SIZE = 3;

    static final char SUBMARINE_CELL = 'S';
    static final int SUBMARINE_SIZE = 3;

    static final char BATTLESHIP_CELL = 'B';
    static final int BATTLESHIP_SIZE = 4;

    static final char AIR_CARRIER_CELL = 'A';
    static final int AIR_CARRIER_SIZE = 5;

    static final char HIT_CELL = 'X';
    static final char MISS_CELL = 'O';

    public static void main(String[] args){
        Scanner scan = new Scanner(System.in);

        //variables
        boolean program_running = false;
        boolean in_menu = false;
        boolean in_quit_menu = false;
        boolean game_running = false;
        boolean invalid_choice = false;
        boolean player_turn_passed = false;
        char[][] player_board = new char[SIZE][SIZE];
        char[][] cpu_board = new char[SIZE][SIZE];
        char[][] player_shots = new char[SIZE][SIZE];
        char[][] cpu_shots = new char[SIZE][SIZE];
        int difficulty = 0; //0 default, 1 easy, 2 normal, 3 hard
        String save_path = "";
        String user_choice = "";
        
        program_running = true;
        
        //outer program loop
        while(program_running){
            //enter menu on program loop start
            in_menu = true;
            
            //display options for game
            System.out.println("BATTLESHIP GAME by Mansour");
            System.out.println("< 1 > Start a new game");
            System.out.println("< 2 > Load an old game");
            System.out.println("< 3 > Show instructions");
            System.out.println("< 4 > Quit program");

            //read user choice for game, loop if invalid input or showing instructions
            do{
                System.out.print(" > ");
                user_choice = scan.nextLine();
                System.out.println(); //blank line

                //new game
                //if statement instead of switch for string because of school java version
                if (user_choice.equals("1")) {
                    reset_boards(player_board, cpu_board, player_shots, cpu_shots);
                    game_running = true;
                    in_menu = false;
                } 
                //load game
                else if (user_choice.equals("2")) { 
                    save_path = new_save_path();
                    System.out.println(); //blank line

                    //load game information
                    player_board = load_file(save_path, PLAYER_BOARD);
                    cpu_board = load_file(save_path, CPU_BOARD);
                    player_shots = load_file(save_path, PLAYER_SHOTS);
                    cpu_shots = load_file(save_path, CPU_SHOTS);
                        
                    //start game if all boards have data
                    if(is_loaded(player_board) && is_loaded(cpu_board) && is_loaded(player_shots) && is_loaded(cpu_shots)){
                        System.out.println("Game successfully loaded.");
                        game_running = true;
                        in_menu = false;
                    }
                } 
                //show instructions
                else if (user_choice.equals("3")){
                    print_instructions();
                }
                //quit program
                else if (user_choice.equals("4")){
                    System.out.println("Exiting program...");
                    program_running = false;
                    in_menu = false;
                }
                //invalid input
                else{
                    System.out.println("Invalid input. Please enter a number between 1 and 4.");
                    System.out.println(); //blank line
                }
            } while (in_menu);
            System.out.println(); //blank line

            if(game_running){
                System.out.println("Enter the name of the file to save to.");
                save_path = new_save_path();
                System.out.println(); //blank line
            }
            
            //game loop
            while(game_running) {
                //print boards visible to the player
                print_board(player_board);
                print_board(player_shots);
                System.out.println(); //blank line

                player_turn_passed = false;
                invalid_choice = false;

                //player's turn
                System.out.println("Your turn:");
                System.out.println("< 1 > Shoot");
                System.out.println("< 2 > Save game");
                System.out.println("< 3 > Quit game and return to main menu");
                
                do{
                    System.out.print(" > ");
                    user_choice = scan.nextLine();
                    System.out.println(); //blank line
                    
                    //shoot
                    if(user_choice.equals("1")){
                        //player_shoot();
                        player_turn_passed = true; //proceed to next turn
                    }
                    //save game
                    else if(user_choice.equals("2")){
                        System.out.println("Saving to "+save_path+"...");
                        //save_game(save_path, player_board, cpu_board, player_shots, cpu_shots);
                        System.out.println("Game succesfully saved.");
                        System.out.println(); //blank line
                    }
                    //quit game and return to main menu
                    else if(user_choice.equals("3")){
                        in_quit_menu = true;
                        
                        System.out.println("Quit game and return to main menu:");
                        System.out.println("< 1 > Save and quit");
                        System.out.println("< 2 > Quit without saving");
                        System.out.println("< 3 > Cancel");
                        System.out.println(); //blank line
                        
                        do{
                            System.out.print(" > ");
                            user_choice = scan.nextLine();
                            System.out.println(); //blank line
                            
                            //save and quit
                            if(user_choice.equals("1")){
                               System.out.println("Saving to "+save_path+"...");
                                //save_game(save_path, player_board, cpu_board, player_shots, cpu_shots);
                                System.out.println("Game succesfully saved.");
                                System.out.println("Exiting to main menu...");
                                System.out.println(); //blank line
                                in_quit_menu = false;
                                game_running = false;
                            }
                            //quit without saving
                            else if(user_choice.equals("2")){
                                System.out.println("Are you sure you want to exit to the main menu?");
                                System.out.println("All unsaved progress will be lost.");
                                System.out.println(); //blank line
                                System.out.println("< Y > Exit ");
                                System.out.println("Press any other key to cancel");
                                System.out.println(); //blank line
                                
                                System.out.print(" > ");
                                user_choice = scan.nextLine();
                                
                                //exit if the user typed Y or y
                                if((user_choice.toLowerCase()).equals("Y")){
                                    System.out.println("Exiting to main menu...");
                                    System.out.println(); //blank line
                                    in_quit_menu = false;
                                    game_running = false;
                                }
                            }
                            //cancel
                            else if(user_choice.equals("3")){
                                System.out.println();
                                in_quit_menu = false;
                            }
                            //invalid input
                            else {
                                System.out.println("Invalid input. Please enter a number between 1 and 3.");
                            }
                        } while(in_quit_menu);
                    } else {
                        //invalid choice if user_choice does not match any option
                        System.out.println("Invalid input. Please enter a number between 1 and 4.");
                        invalid_choice = true;
                    }
                } while(invalid_choice);
                
                //computer's turn, plays if the player succesfully completes their turn
                if(player_turn_passed){
                    System.out.println("Computer's turn:");
                    System.out.println(" --- ");
                    System.out.println(); //blank line
                    //cpu_turn();
                }
            }
        }
    }
    
    /*
    Method:
    load_file
    -----
    Parameters:
    String save_path - the file path for the save
    int load_choice - the choice to determine what to load from the save
    -----
    Returns:
    char[][] array_data - array that contains the data read from the save
    -----
    Description:
    This method reads from the save file, and uses a switch statement to determine what data to read and return.
    If the choice for load_file is the CPU’s shots, load_file reads the save file until it encounters “CPU shots:”.
    From there, it reads the next lines a number of times equal to the board size, and uses String.toCharArray()
    to fill each row of the char array array_data.
    Finally, array_data is returned. 
    */
    public static char[][] load_file(String save_path, int load_choice){
        //variables
        boolean finished_loading = false;
        char[][] array_data;
        String file_line = "";

        //initialise array_data to be the same size as the board
        array_data = new char[SIZE][SIZE];
        
        try{
            //create BufferedReader reading from save.txt
            BufferedReader reader = new BufferedReader(new FileReader(save_path));

            //begin reading from file
            file_line = reader.readLine();
            //choose which data to return based on choice
            switch(load_choice) {
                case(PLAYER_BOARD):
                    while(!finished_loading){
                        if(file_line.equals("Player ships:")){
                            file_line = reader.readLine(); //go to next row
                            for(int i = 0; i < SIZE; i++){
                                array_data[i] = file_line.toCharArray();
                                file_line = reader.readLine();
                            }
                            //end loop after array_data is filled with save data
                            finished_loading = true;
                        } else {
                            file_line = reader.readLine();
                        }
                    }
                    break;
                case(CPU_BOARD):
                   while(!finished_loading){
                        if(file_line.equals("CPU ships:")){
                            file_line = reader.readLine(); //go to next row
                            for(int i = 0; i < SIZE; i++){
                                array_data[i] = file_line.toCharArray();
                                file_line = reader.readLine();
                            }
                            //end loop after array_data is filled with save data
                            finished_loading = true;
                        } else {
                            file_line = reader.readLine();
                        }
                    }
                    break;
                case(PLAYER_SHOTS):
                   while(!finished_loading){
                        if(file_line.equals("Player shots:")){
                            file_line = reader.readLine(); //go to next row
                            for(int i = 0; i < SIZE; i++){
                                array_data[i] = file_line.toCharArray();
                                file_line = reader.readLine();
                            }
                            //end loop after array_data is filled with save data
                            finished_loading = true;
                        } else {
                            file_line = reader.readLine();
                        }
                    }
                    break;
                case(CPU_SHOTS):
                   while(!finished_loading){
                        if(file_line.equals("CPU shots:")){
                            file_line = reader.readLine(); //go to next row
                            for(int i = 0; i < SIZE; i++){
                                array_data[i] = file_line.toCharArray();
                                file_line = reader.readLine();
                            }
                            //end loop after array_data is filled with save data
                            finished_loading = true;
                        } else {
                            file_line = reader.readLine();
                        }
                    }
                    break;
                default:
                    System.out.println("Load choice out of bounds.");
                    System.out.println(); //blank line
                    break;
            }
            
            //close BufferedReader
            reader.close();
        } catch (IOException e) {
            System.out.println("Error accessing save file:");
            System.out.println(e);
        }
        
        //return data from save
        return array_data;
    }

    /*
    Method:
    save_game
    -----
    Parameters:
    String save_path - the file path for the save
    char[][] p_board - 2d array containing the player's board
    char[][] c_board - 2d array containing the computer's board
    char[][] p_shots - 2d array containing the player's shots
    char[][] c_shots - 2d array contianing the computer's shots
    -----
    Returns:
    void
    -----
    Description:
    
    */
    /*
    public static void save_game(String save_path, char[][] p_board, char[][] c_board, char[][] p_shots, char[][] c_shots){
        //create BufferedWriter writing to save.txt
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter(save_path));
            writer.write("Test");
            
            //close BufferedWriter
            writer.close();
        } catch (IOException e) {
            System.out.println("Error writing to save file.");
            System.out.println(e);
        }
    }
    */

    /*
    Method:
    reset_boards
    -----
    Parameters:
    String save_path - the file path for the save
    char[][] p_board - 2d array containing the player's board
    char[][] c_board - 2d array containing the computer's board
    char[][] p_shots - 2d array containing the player's shots
    char[][] c_shots - 2d array contianing the computer's shots
    -----
    Returns:
    void
    -----
    Description:
    This method sets all elements in the player and computer's board/shots to '-', resetting the boards.
    Uses pass-by-reference to make changes.
    */
    public static void reset_boards(char[][] p_board, char[][] c_board, char[][] p_shots, char[][] c_shots){
        //set all elements to '-'
        for(int i = 0; i<SIZE; i++){
            for(int j = 0; j<SIZE; j++){
                p_board[i][j] = '-';
                c_board[i][j] = '-';
                p_shots[i][j] = '-';
                c_shots[i][j] = '-';
            }
        }
    }
    
    /*
    Method:
    is_loaded
    -----
    Parameters:
    char[][] array_data - the array to check has successfully loaded
    -----
    Returns:
    boolean is_valid - the true or false value for if the data loaded correctly or not
    -----
    Description:
    This method checks and returns whether or not the array passed to it has any null cells (which is 0 for char) and if it's not the size it should be.
    */
    public static boolean is_loaded(char[][] array_data){
        boolean is_valid = true;

        //check if any cell is a null
        for (int i = 0; i<SIZE; i++){
            for(int j = 0; j<SIZE; j++){
                if (array_data[i][j] == 0 || array_data[i].length != SIZE) {
                    is_valid = false;
                }
            }
        }

        return is_valid;
    }

    /*
    Method:
    new_save_path
    -----
    Parameters:
    none
    -----
    Returns:
    String save_path - the valid save path for the new save file
    -----
    Description:
    This method asks the user for a new save path and checks if it can be written to by creating a BufferedWriter for the save path in a try-catch block.
    If the save path causes an IOException, the user is asked to reenter the save path.
    */
    public static String new_save_path(){
        Scanner scan = new Scanner(System.in);
        
        boolean valid_save_path = false;
        String save_path = "";

        System.out.println("Enter the save file name (with .txt):");
        do{
            System.out.print(" > ");
            save_path = scan.nextLine();
            try{
                //test if creating a file will cause an IOException
                File file = new File(save_path);
                if (file.exists() && file.isFile() && file.canWrite()){
                    valid_save_path = true;
                }
            } catch(IOException e){
                //make user reenter save path if IOException occurs
                System.out.println("Error accessing save file:");
                System.out.println(e);
                System.out.println(); //blank line
                System.out.println("Reenter save file name:");
            }
        } while(!valid_save_path);

        return save_path;
    }

    /*
    Method:
    print_board
    -----
    Parameters:
    char[][] board - the board to be printed
    -----
    Returns:
    void
    -----
    Description:
    This method loops through the 2d array passed to it and prints all elements using a standard nested for loop.
    */
    public static void print_board(char[][] board){
        //loop through and print all items of the board
        for(int i = 0; i<SIZE; i++){
          for(int j = 0; j<SIZE; j++){
            System.out.print(board[i][j]+" ");
          }
          System.out.println(); //new line
        }
    }

    /*
    Method:
    print_instructions
    -----
    Parameters:
    none
    -----
    Returns:
    void
    -----
    Description:
    This method prints out the instructions for Battleship. In a method to avoid cluttering the main loop.
    */
    public static void print_instructions(){
        System.out.println("Battleship Game Instructions");
        System.out.println("Objective");
        System.out.println("The goal of Battleship is to sink all of your opponent's ships before they sink all of yours.");
        System.out.println();
        
        System.out.println("Game Setup");
        System.out.println("Game Board:");
        System.out.println("Each player has a 10x10 grid.");
        System.out.println("Your grid shows your battleships, and the computer's shots on your battleships.");
        System.out.println("Another grid tracks your shots on the computer's battleships.");
        System.out.println();
        
        System.out.println("Ships:");
        System.out.println("Each player has the following ships:");
        System.out.println("1 Carrier (5 spaces)");
        System.out.println("1 Battleship (4 spaces)");
        System.out.println("1 Cruiser (3 spaces)");
        System.out.println("1 Submarine (3 spaces)");
        System.out.println("1 Destroyer (2 spaces)");
        System.out.println();
        
        System.out.println("Placing Ships");
        System.out.println("Player's Ships:");
        System.out.println("Place your ships on your grid.");
        System.out.println("Ships can be placed horizontally or vertically.");
        System.out.println("Ships cannot overlap or be placed outside the grid.");
        System.out.println();
        
        System.out.println("Computer's Ships:");
        System.out.println("The computer will automatically place its ships on its grid.");
        System.out.println();
        
        System.out.println("Playing the Game");
        System.out.println("Taking Turns:");
        System.out.println("You and the computer take turns firing shots at each others' ships.");
        System.out.println("Enter the coordinates for your shot when it's your turn.");
        System.out.println("Example:");
        System.out.println("Column: 4");
        System.out.println("Row: 5");
        System.out.println();
        
        System.out.println("Checking Shots:");
        System.out.println("If the shot hits an opponent's ship, the hit is marked on your grid with an 'X'");
        System.out.println("If the shot misses, the miss is marked on your grid with an 'O'.");
        System.out.println("The computer will also take shots at your grid and you must mark the hits and misses accordingly.");
        System.out.println();
        
        System.out.println("Sunken Ships:");
        System.out.println("When all parts of a ship are hit, the ship is sunk.");
        System.out.println("You are told when you or the computer sunk a ship (e.g., \"The computer sunk your battleship!\")");
        System.out.println();
    }
}
