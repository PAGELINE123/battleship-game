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
    //create Scanner reading from save.txt
    try{
      Scanner file_sc = new Scanner(save_path);
    } catch (IOException e) {
      System.out.println("Error accessing save file.");
      System.out.println(e);
    }
    
    //variables
    String file_line = "";
    
    switch(load_choice){}
    
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
