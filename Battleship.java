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
        boolean game_running = false;
        char[][] player_board = new char[SIZE][SIZE];
        char[][] cpu_board = new char[SIZE][SIZE];
        char[][] player_shots = new char[SIZE][SIZE];
        char[][] cpu_shots = new char[SIZE][SIZE];
        int difficulty = 0; //0 default, 1 easy, 2 normal, 3 hard
        String save_path = "";

        //title
        System.out.println("BATTLESHIP GAME by Mansour");
        System.out.println();
        
        game_running = true;
        
        //game loop
        while(game_running){
            //display options for game
            System.out.println("< 1 > Start a new game");
            System.out.println("< 2 > Load an old game");
            System.out.println("< 3 > Show instructions");
            System.out.println("< 4 > Quit program");
            
            System.out.println("Enter the save file name (with .txt):");
            save_path = scan.nextLine();
            System.out.println(); //blank line

            //start game if all boards have data
            if(player_board && cpu_board && player_shots && cpu_shots){
                game_running = true;
            }
            
            //load game information
            player_board = load_file(save_path, PLAYER_BOARD);
            cpu_board = load_file(save_path, CPU_BOARD);
            //player_shots = load_file(save_path, PLAYER_SHOTS);
            //cpu_shots = load_file(save_path, CPU_SHOTS);

            print_board(player_board);
            print_board(cpu_board);
        
            //save_file(save_path, player_board, cpu_board, player_shots, cpu_shots);
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
            System.out.println("Error accessing save file.");
            Systme.out.println(); //blank line
            System.out.println(e);
        }
        
        //return data from save
        return array_data;
    }

    /*
    Method:
    save_file
    -----
    Parameters:
    String save_path - the file path for the save
    char[][]
    char[][]
    char[][]
    char[][]
    -----
    Returns:
    void
    -----
    Description:
    
    */
    /*
    public static void save_file(String save_path, char[][] p_board, char[][] c_board, char[][] p_shots, char[][] c_shots){
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
}
