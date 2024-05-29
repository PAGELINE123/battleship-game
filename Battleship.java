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
        char[][] player_board = new char[SIZE][SIZE];
        char[][] cpu_board = new char[SIZE][SIZE];
        char[][] player_shots = new char[SIZE][SIZE];
        char[][] cpu_shots = new char[SIZE][SIZE];
        int difficulty = 0; //0 default, 1 easy, 2 normal, 3 hard
        String save_path = "";

        save_path = scan.nextLine();

        //load file information
        player_board = load_file(save_path, PLAYER_BOARD);
        cpu_board = load_file(save_path, CPU_BOARD);
        player_shots = load_file(save_path, PLAYER_SHOTS);
        cpu_shots = load_file(save_path, CPU_SHOTS);

        //save_file(save_path, player_board, cpu_board, player_shots, cpu_shots);
    }

    public static char[][] load_file(String save_path, int load_choice){
        //variables
        boolean finished_loading = false;
        char[][] array_data;
        String file_line = "";

        //initialise array_data to be the same size as the board
        array_data = new char[SIZE][SIZE];
        
        try{
            //create BufferedReader reading from save.txt
            BufferedReader reader = new BufferedReader(new FileWriter(save_path));

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
                            }
                            //end loop after array_data is filled with save data
                            finished_loading = true;
                        } else {
                            file_line = reader.readLine();
                        }
                    }
                    break;
                case(CPU_BOARD):
                    
                    break;
                case(PLAYER_SHOTS):
                    
                    break;
                case(CPU_SHOTS):
                    
                    break;
                default():
                    System.out.println("Load choice out of bounds.");
                    break;
            }
            return array_data;
        } catch (IOException e) {
            System.out.println("Error accessing save file.");
            System.out.println(e);
        }
    }

    public static void save_file(String save_path, char[][] p_board, char[][] c_board, char[][] p_shots, char[][] c_shots){
        //create BufferedWriter writing to save.txt
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter(SAVE_PATH));
            writer.write("Test");
        } catch (IOException e) {
            System.out.println("Error writing to save file.");
            System.out.println(e);
        }
    }
}
