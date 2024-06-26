/*
Name:
Battleship
-----
Programmer:
Mansour Abdelsalam
-----
Date finished:
2024-06-15
-----
Description:
Program that plays a game of Battleship against a computer.

In the main menu, the player has the ability to start a new game, load a game from and old save, view instructions on how to play Battleship, and exit the program.
This program uses text files to save and load game states.

If the player starts a new game, they must place their own ships.
When the game starts from  either loading or starting a new game, the player can choose a difficulty for the computer.
The player must also enter the name of the file to save to.
The computer has two difficulties; easy mode and normal mode.
In easy mode, the computer shoots at a randomly chosen cell.
In normal mode, the computer shoots randomly until it gets a hit. From there it can track one ship and attempt to shoot at it until it sinks.

During the game, the player can choose to shoot, save the game, or quit the game.
When either side takes a shot, the shot's row and column number is printed.
If the shot sinks a ship, a message printing who sunk the ship and what ship was sunk is printed.

When the game ends on a player/computer loss, an appropriate ending message is printed. If the player quits, no special message is printed.
Both player and computer boards are printed when a game ends.
After the game ends, the player returns to the main menu.

Other features:
Autosave
Choice to randomly place your own ships
Cannot crash from attempting to load a corrupted/altered save file
Cannot crash or get stuck in general
*/

import java.io.*;
import java.util.Scanner;
import java.util.Random;
import java.util.InputMismatchException;

public class Battleship{
    //global constants for readability
    static final String SEPARATION_LINE = "-------------------------------------------------------------------";
    static final int SIZE = 10;
    static final int PLAYER_BOARD = 1;
    static final int CPU_BOARD = 2;
    static final int PLAYER_SHOTS = 3;
    static final int CPU_SHOTS = 4;
    static final int NUM_SHIPS = 5;
    static final String HORIZONTAL = "H";
    static final String VERTICAL = "V";

    //normal mode constants for indexing
    static final int CPU_LOGIC_ITEMS = 4;
    static final int CPU_PRIORITY = 0;
    static final int CPU_DIRECTION = 1;
    static final int CPU_ROOT = 2;
    static final int CPU_SHOOTING = 3;
    static final int DIMENSIONS = 2;

    //cell chars
    static final char EMPTY_CELL = '-';
    static final char HIT_CELL = 'X';
    static final char MISS_CELL = 'O';

    //ship chars and ship values
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

    //main method
    public static void main(String[] args){
        Scanner scan = new Scanner(System.in);

        //debug mode (enables printing of cpu logic items in normal mode)
        boolean debug_mode = false;

        //variables
        boolean program_running = false;
        boolean in_menu = false;
        boolean in_quit_menu = false;
        boolean placing_ships = false;
        boolean game_running = false;
        boolean invalid_choice = false;
        boolean player_turn_passed = false;
        int game_over = 0; //0 default/game not over, 1 for a loss, 2 for a win, 3 for a quit
        char[][] player_board = new char[SIZE][SIZE];
        char[][] cpu_board = new char[SIZE][SIZE];
        char[][] player_shots = new char[SIZE][SIZE];
        char[][] cpu_shots = new char[SIZE][SIZE];
        int[] player_hits = new int[NUM_SHIPS]; // 0 Destroyer, 1 Cruiser, 2 Submarine, 3 Battleship, 4 Aircraft Carrier
        int[] cpu_hits = new int[NUM_SHIPS]; // 0 Destroyer, 1 Cruiser, 2 Submarine, 3 Battleship, 4 Aircraft Carrier
        String[] cpu_shoot_logic = new String[CPU_LOGIC_ITEMS];
        int difficulty = 0; //0 default/easy, 1 normal
        String save_path = "";
        String auto_save_path = "";
        String user_choice = "";

        //start running program
        program_running = true;

        //outer program loop
        while(program_running){
            //enter menu on program loop start
            in_menu = true;
            //reset game over state on program loop start
            game_over = 0;

            //separation line
            System.out.println(SEPARATION_LINE);
            System.out.println(); //blank line

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
                    clear_boards(player_board, cpu_board, player_shots, cpu_shots);
                    placing_ships = true;
                    in_menu = false;
                } 
                //load game
                else if (user_choice.equals("2")) { 
                    save_path = new_save_path();
                    auto_save_path = "AUTOSAVE_"+save_path;
                    System.out.println(); //blank line

                    System.out.println("Loading game...");
                    //load game information
                    player_board = load_file(save_path, PLAYER_BOARD);
                    cpu_board = load_file(save_path, CPU_BOARD);
                    player_shots = load_file(save_path, PLAYER_SHOTS);
                    cpu_shots = load_file(save_path, CPU_SHOTS);

                    //start game if all boards have data
                    if(is_loaded(player_board) && is_loaded(cpu_board) && is_loaded(player_shots) && is_loaded(cpu_shots)){
                        System.out.println("Game successfully loaded.");
                        System.out.println(); //blank line
                        
                        //update player_hits and cpu_hits for loaded_file
                        update_hits(player_board, cpu_board, player_hits, cpu_hits);
                        
                        //do not enter ship placement phase if game is loaded, instead enter directly into game
                        game_running = true;
                        in_menu = false;
                    } else {
                        System.out.println("Error loading from \""+save_path+"\".");
                        System.out.println("Please check if the file is in the same directory, has complete data, and is not empty.");
                        System.out.println(); //blank line
                    }
                } 
                //show instructions
                else if (user_choice.equals("3")){
                    print_instructions();
                }
                //quit program
                else if (user_choice.equals("4")){
                    System.out.println("Exiting program...");
                    System.out.println(); //blank line
                    //separation line
                    System.out.println(SEPARATION_LINE);
                    System.out.println(); //blank line

                    program_running = false;
                    in_menu = false;
                }
                //invalid input
                else{
                    System.out.println("Invalid input. Please enter a number between 1 and 4.");
                }
            } while (in_menu);

            //player and cpu place boards
            if(placing_ships){
                //separation line
                System.out.println(SEPARATION_LINE);
                System.out.println(); //blank line

                player_place_ships(player_board);
                random_place_ships(cpu_board);
                
                //check if the player quit during placement
                if(player_board[0][0] != 'Q'){
                    //proceed to game if all ships are placed
                    game_running = true;
                }else{
                    //reset boards and go back to main menu if the player quit during placement
                    reset_boards(player_board, cpu_board, player_shots, cpu_shots);
                    //player quit out-of-game message
                    game_over = 4;
                    game_over_message(game_over, player_board, cpu_board);
                    System.out.println(); //blank line
                }
                placing_ships = false;
            }
            
            //things to be set before game start
            if(game_running){
                //separation line
                System.out.println(SEPARATION_LINE);
                System.out.println(); //blank line

                //get save path
                System.out.println("Enter the name of the file to save to.");
                save_path = new_save_path();
                auto_save_path = "AUTOSAVE_"+save_path;
                System.out.println(); //blank line
                
                //get difficulty
                System.out.println("Enter difficulty:");
                System.out.println("< 1 > Easy");
                System.out.println("< 2 > Normal");
                do{
                    //reset invalid_choice
                    invalid_choice = false;
                    
                    System.out.print(" > ");
                    user_choice = scan.nextLine();
                    System.out.println(); //blank line

                    if(user_choice.equals("1") || user_choice.equals("2")){
                        //no try-catch because user_choice must be parsable to reach this 
                        difficulty = Integer.parseInt(user_choice);

                        //decrement difficulty for usage as variable
                        difficulty--;

                        System.out.print("You have selected: ");
                        if(difficulty == 0){
                            System.out.println("EASY");
                        }else if(difficulty == 1){
                            System.out.println("NORMAL");
                            reset_logic(cpu_shoot_logic);
                        }
                    }else{
                        System.out.println("Invalid input. Please enter a number between 1 and 2.");
                        invalid_choice = true;
                    }
                }while(invalid_choice);
                System.out.println(); //blank line

                System.out.println("Starting game...");
                System.out.println(); //blank line
            }

            //game loop
            while(game_running) {
                //separation line
                System.out.println(SEPARATION_LINE);
                System.out.println(); //blank line

                //print out logic items
                if(debug_mode){
                    for(int i = 0; i<CPU_LOGIC_ITEMS; i++){
                        System.out.println(cpu_shoot_logic[i]);
                    }
                    System.out.println(); //blank line
                }
                
                //autosave on game loop start (mainly added for bug testing)
                save_game(auto_save_path, player_board, cpu_board, player_shots, cpu_shots);
                
                //print boards visible to the player
                print_boards(player_board, player_shots, 0);
                System.out.println(); //blank line

                //reset player_turn_passed and invalid_choice
                player_turn_passed = false;
                invalid_choice = false;

                //player's turn
                System.out.println("Your turn:");
                System.out.println("< 1 > Shoot");
                System.out.println("< 2 > Save game");
                System.out.println("< 3 > Quit game and return to main menu");

                do{
                    //reset invalid_choice on loop start
                    invalid_choice = false;
                    
                    
                    System.out.print(" > ");
                    user_choice = scan.nextLine();
                    System.out.println(); //blank line

                    //shoot
                    if(user_choice.equals("1")){
                        player_shoot(cpu_board, player_shots, cpu_hits);
                        System.out.println(); //blank line
                        player_turn_passed = true; //proceed to next turn
                    }
                    //save game
                    else if(user_choice.equals("2")){
                        System.out.println("Saving to "+save_path+"...");
                        save_game(save_path, player_board, cpu_board, player_shots, cpu_shots);
                        System.out.println("Game successfully saved.");
                        System.out.println(); //blank line
                    }
                    //quit game and return to main menu
                    else if(user_choice.equals("3")){
                        in_quit_menu = true;

                        System.out.println("Quit game and return to main menu:");
                        System.out.println("< 1 > Save and quit");
                        System.out.println("< 2 > Quit without saving");
                        System.out.println("< 3 > Cancel");

                        do{
                            System.out.print(" > ");
                            user_choice = scan.nextLine();
                            System.out.println(); //blank line

                            //save and quit
                            if(user_choice.equals("1")){
                               System.out.println("Saving to "+save_path+"...");
                                save_game(save_path, player_board, cpu_board, player_shots, cpu_shots);
                                System.out.println("Game successfully saved.");
                                System.out.println(); //blank line
                                game_over = 3; //change game_over to player exitted
                                in_quit_menu = false;
                            }
                            //quit without saving
                            else if(user_choice.equals("2")){
                                System.out.println("Are you sure you want to exit to the main menu?");
                                System.out.println("All unsaved progress will be lost.");
                                System.out.println(); //blank line
                                System.out.println("< Y > Exit ");
                                System.out.println("Enter any other key to cancel");

                                System.out.print(" > ");
                                user_choice = scan.nextLine();
                                System.out.println();

                                //change game_over to player exitted if player typed Y or y
                                if((user_choice.toLowerCase()).equals("y")){
                                    game_over = 3;
                                }
                                in_quit_menu = false;
                            }
                            //cancel
                            else if(user_choice.equals("3")){
                                in_quit_menu = false;
                            }
                            //invalid input
                            else {
                                System.out.println("Invalid input. Please enter a number between 1 and 3.");
                            }
                        } while(in_quit_menu);
                    } else {
                        //invalid choice if user_choice does not match any option
                        System.out.println("Invalid input. Please enter a number between 1 and 3.");
                        invalid_choice = true;
                    }
                } while(invalid_choice);

                //computer's turn, plays if the player successfully completes their turn
                if(player_turn_passed){
                    cpu_shoot(player_board, cpu_shots, difficulty, player_hits, cpu_shoot_logic);
                    System.out.println(); //blank line
                }

                //automatically detect for game_over only if game_over has not already been set to something else
                if(game_over == 0){
                    game_over = check_ships(player_board, cpu_board);
                }
                
                //if game over is not in default state, end game and print a message
                if(game_over != 0){
                    game_over_message(game_over, player_board, cpu_board);
                    System.out.println(); //blank line
                    
                    reset_logic(cpu_shoot_logic);
                    reset_boards(player_board, cpu_board, player_shots, cpu_shots);
                    game_running = false;
                }
            }
        }
    }

    /*
    Method:
    player_place_ships
    -----
    Parameters:
    char[][] p_board - the player's board
    -----
    Returns:
    void
    -----
    Description:
    This method asks the player to place all their ships. Each ship can be placed horizontally or vertically, and cannot be placed out of bounds or clipping out of bounds.
    Additionally, there is an option to randomly place the ships.
    Uses pass-by-reference to make changes.
    */
    public static void player_place_ships(char[][] p_board){
        Scanner scan = new Scanner(System.in);
        
        //variables
        boolean valid_placement = false;
        boolean valid_choice = false;
        boolean invalid_input = false;
        String user_choice = "";
        int row = 0;
        int col = 0;
        String orientation = "";
        char ship_cell = EMPTY_CELL; //default value
        int ship_size = 0;
        
        System.out.println("Ship placement phase:");
        System.out.println("< 1 > Place ships manually");
        System.out.println("< 2 > Place ships randomly");
        System.out.println("< 3 > Quit game");
        
        do{
            System.out.print(" > ");
            user_choice = scan.nextLine();
            System.out.println(); //blank line
          
            //if instead of switch because school java version does not support string switch statements
            //place ships manually
            if(user_choice.equals("1")){
                //print out placement guide
                System.out.println(" -- Reminder -- ");
                System.out.println("When placing on a row and column horizontally, your ship will place like this:");
                System.out.println("Row 1, Column 2");
                System.out.println("  1 2 3 4 5 6 ");
                System.out.println("1 - X X X X - ");
                System.out.println("2 - - - - - - ");
                System.out.println(); //blank line
                System.out.println("When placing on a row and column vertically, your ship will place like this:");
                System.out.println("Row 1, Column 2");
                System.out.println("  1 2 3");
                System.out.println("1 - X -");
                System.out.println("2 - X -");
                System.out.println("3 - X -");
                System.out.println("4 - X -");
                System.out.println("5 - - -");
                System.out.println(); //blank line
              
                //begin placing ships
                for(int i = 0; i<NUM_SHIPS; i++){
                    //reset whether the ship is placed in a valid spot
                    valid_placement = false;
            
                    //0 Destroyer, 1 Cruiser, 2 Submarine, 3 Battleship, 4 Aircraft Carrier
                    switch(i){
                        //destroyer
                        case(0):
                            System.out.println("PLACING: DESTROYER");
                            ship_cell = DESTROYER_CELL;
                            ship_size = DESTROYER_SIZE;
                            break;
                        //cruiser
                        case(1):
                            System.out.println("PLACING: CRUISER");
                            ship_cell = CRUISER_CELL;
                            ship_size = CRUISER_SIZE;
                            break;
                        //submarine
                        case(2):
                            System.out.println("PLACING: SUBMARINE");
                            ship_cell = SUBMARINE_CELL;
                            ship_size = SUBMARINE_SIZE;
                            break;
                        //battleship
                        case(3):
                            System.out.println("PLACING: BATTLESHIP");
                            ship_cell = BATTLESHIP_CELL;
                            ship_size = BATTLESHIP_SIZE;
                            break;
                        //aircraft carrier
                        case(4):
                            System.out.println("PLACING: AIRCRAFT CARRIER");
                            ship_cell = AIR_CARRIER_CELL;
                            ship_size = AIR_CARRIER_SIZE;
                            break;
                        default:
                            System.out.println("Ship to be placed does not exist.");
                            break;
                    }
            
                    //print board
                    print_board(p_board);
                    
                    System.out.println(); //blank line
                    
                    //print out the ship that is being placed
                    System.out.print("Ship: ");
                    for(int j = 0; j<ship_size; j++){
                        System.out.print(ship_cell+" ");
                    }
                    System.out.println(); //next line
                    
                    System.out.println(); //blank line
                    
                    do{
                        //reset at start of loop
                        invalid_input = false;
                        valid_placement = false;
                        try{
                            //ask for row and column number
                            System.out.print("Enter row number: ");
                            row = scan.nextInt();
                            System.out.print("Enter column number: ");
                            col = scan.nextInt();
                            System.out.println(); //blank line
                        }catch(InputMismatchException e){
                            invalid_input = true;
                            System.out.println("Invalid input. Row and column numbers must be whole numbers.");
                            System.out.println(); //blank line
                        }
                        scan.nextLine(); //remove new line character from input stream
                        
                        if(!invalid_input){
                            System.out.print("Enter orientation (\"H\" or \"V\"): ");
                            orientation = scan.nextLine();
                            orientation = orientation.toUpperCase();
                            
                            if((!orientation.equals(VERTICAL)) && (!orientation.equals(HORIZONTAL))){
                                invalid_input = true;
                                System.out.println("Invalid input. Orientation must be \"H\" or \"V\"");
                                System.out.println(); //blank line
                            }
                        }
                        
                        if(!invalid_input){
                            //lower row and column down by one to be compatible with indexing
                            row--;
                            col--;
                            //check if the placement of the ship is valid
                            if(is_valid_placement(p_board, row, col, orientation, ship_size, true)){
                                valid_placement = true;
                            }else{
                                System.out.println(); //blank line
                            }
                        }
                    } while(!valid_placement);
            
                    //place the ship
                    p_board = place_ship(p_board, row, col, orientation, ship_size, ship_cell);
                    System.out.println(); //blank line
                }
                System.out.println("Ships successfully placed.");
                System.out.println(); //blank line
                valid_choice = true;
            }
            //place ships randomly
            else if(user_choice.equals("2")){
                System.out.println("Randomly placing ships...");
                random_place_ships(p_board);
                System.out.println("Ships successfully placed.");
                System.out.println(); //blank line
                valid_choice = true;
            }
            else if(user_choice.equals("3")){
                p_board[0][0] = 'Q';
                valid_choice = true;
            }
            //invalid choice
            else{
                System.out.println("Invalid input. Please enter a number between 1 and 3.");
            }
        } while(!valid_choice);
    }
    
    /*
    Method:
    random_place_ships
    -----
    Parameters:
    char[][] board - the board to be given random ships
    -----
    Returns:
    void
    -----
    Description:
    This method fills the passed board with ships in random positions. Each ship can be placed horizontally or vertically, and cannot be placed out of bounds or clipping out of bounds.
    Cpu_board is always filled using this method.
    Uses pass-by-reference to make changes.
    */
    public static void random_place_ships(char[][] board){
        Random rand = new Random();
        
        //variables
        boolean valid_placement = false;
        int row = 0;
        int col = 0;
        String orientation = "";
        char ship_cell = EMPTY_CELL; //default value
        int ship_size = 0;
        
        //begin placing ships
        for(int i = 0; i<NUM_SHIPS; i++){
            //reset whether the ship is placed in a valid spot
            valid_placement = false;
            
            //0 Destroyer, 1 Cruiser, 2 Submarine, 3 Battleship, 4 Aircraft Carrier
            switch(i){
                //destroyer
                case(0):
                    ship_cell = DESTROYER_CELL;
                    ship_size = DESTROYER_SIZE;
                    break;
                //cruiser
                case(1):
                    ship_cell = CRUISER_CELL;
                    ship_size = CRUISER_SIZE;
                    break;
                //submarine
                case(2):
                    ship_cell = SUBMARINE_CELL;
                    ship_size = SUBMARINE_SIZE;
                    break;
                //battleship
                case(3):
                    ship_cell = BATTLESHIP_CELL;
                    ship_size = BATTLESHIP_SIZE;
                    break;
                //aircraft carrier
                case(4):
                    ship_cell = AIR_CARRIER_CELL;
                    ship_size = AIR_CARRIER_SIZE;
                    break;
                default:
                    System.out.println("Ship to be placed does not exist.");
                    break;
            }
            
            do{
                //generate row and column from 0-9
                row = rand.nextInt(SIZE);
                col = rand.nextInt(SIZE);
                //generate orientation from 0-1
                if(rand.nextInt(2) == 0){
                    orientation = HORIZONTAL;
                }else{
                    orientation = VERTICAL;
                }
                    
                //check if the placement of the ship is valid
                if(is_valid_placement(board, row, col, orientation, ship_size, false)){
                    valid_placement = true;
                }
            } while(!valid_placement);
            
            board = place_ship(board, row, col, orientation, ship_size, ship_cell);
        }
    }

    /*
    Method:
    is_valid_placement
    -----
    Parameters:
    char[][] board - the board to have the placement checked
    int row - the row number of the placement
    int col - the column number of the placement
    String orientation - the orientation of the placement, either vertical or horizontal
    int ship_size - the length of the ship that is being checked
    boolean show_messages - whether or not the method should print out error messages
    -----
    Returns:
    boolean is_valid - whether or not the ship placed at the given row number and column is in a valid position
    -----
    Description:
    This method takes row and column number, orientation, and the length of the ship, and checks if it is overlapping with another ship or if it is out of bounds.
    Returns true if the ship is in a valid position and can be placed without problems. False if it is not in a valid position.
    Also prints out a message detailing the cause of the placement being invalid based on the value of show_messages.
    */
    public static boolean is_valid_placement(char[][] board, int row, int col, String orientation, int ship_size, boolean show_messages){
        //variables
        //0 valid, 1 row or col out of bounds, 2 part of ship out of bounds horizontally, 3 ship part of ship out of bounds vertically, 
        //4 ship overlapping horizontally, 5 ship overlaping vertically
        int condition = 0;
        boolean is_valid = true;
        
        //check if row or column is out of bounds
        if((row >= SIZE || row < 0) || (col >= SIZE || col < 0)){
            condition = 1;
        }
        
        if(condition == 0){
            //check if part of the ship would be out of bounds
            if(orientation.equals(HORIZONTAL)){
                if((col+ship_size) > SIZE){
                    condition = 2;
                }
            } else if (orientation.equals(VERTICAL)){
                if((row+ship_size) > SIZE){
                    condition = 3;
                }
            }
        }
        
        if(condition == 0){
            //check if overlapping with another ship
            for(int i = 0; i<ship_size; i++){
                if(orientation.equals(HORIZONTAL)){
                    if(board[row][col+i] != EMPTY_CELL){
                        condition = 4;
                    }
                } else if (orientation.equals(VERTICAL)){
                    if(board[row+i][col] != EMPTY_CELL){
                        condition = 5;
                    }
                }
            }
        }
        
        //print error messages if desired
        if(show_messages){
            switch(condition){
                case(0):
                    //pass
                    break;
                case(1):
                    System.out.println("Invalid placement: Coordinates out of bounds.");
                    break;
                case(2):
                    System.out.println("Invalid placement: Ship is out of bounds horizontally.");
                    break;
                case(3):
                    System.out.println("Invalid placement: Ship is out of bounds vertically.");
                    break;
                case(4):
                    System.out.println("Invalid placement: Ship is overlapping with another ship horizontally.");
                    break;
                case(5):
                    System.out.println("Invalid placement: Ship is overlapping with another ship vertically.");
                    break;
                default:
                    System.out.println("Placement conditon out of bounds.");
                    break;
            }
        }
        
        //set is_valid to false if it is not in a valid condition
        if(condition != 0){
            is_valid = false;
        }
        
        //if is_valid passes all the tests, it remains true
        return is_valid;
    }
    
    /*
    Method:
    place_ship
    -----
    Parameters:
    char[][] board - the board to place the ship in
    int row - the row number of the placement
    int col - the column number of the placement
    String orientation - the orientation of the placement, either vertical or horizontal
    int ship_size - the length of the ship to be placed
    char ship_cell - the cell of the ship to be placed
    -----
    Returns:
    char[][] board - the board with the ships placed
    -----
    Description:
    Replaces the cells in the path of the ship placement with the ship's cell char. Validity of the placement is done in a separate method.
    Explicitly returns the new board.
    */
    public static char[][] place_ship(char[][] board, int row, int col, String orientation, int ship_size, char ship_cell){
        //variables
        if (orientation.equals(HORIZONTAL)){
            for(int i = 0; i<ship_size; i++){
                board[row][col+i] = ship_cell;
            }
        } else if (orientation.equals(VERTICAL)){
            for(int i = 0; i<ship_size; i++){
                board[row+i][col] = ship_cell;
            }
        }
        
        return board;
    }
    
    /*
    Method:
    player_shoot
    -----
    Parameters:
    char[][] c_board - the computer's board to be updated with a hit or miss cell
    char[][] p_shots - the player's shots used to keep track of what the player has shot
    int[] c_hits - the hits on the computer's hits
    -----
    Returns:
    void
    -----
    Description:
    This method allows the player to shoot at a cooridinate they enter.
    If they hit or miss, both the computer board and player shots board are marked with a hit or miss cell respectively.
    Updates c_hits if the player hits a ship.
    Uses pass-by-reference to make edits.
    */
    public static void player_shoot(char[][] c_board, char[][] p_shots, int[] c_hits){
        Scanner scan = new Scanner(System.in);
        
        //variables
        boolean invalid_shot = false;
        int row = 0;
        int col = 0;
      
        //ask player for shot
        System.out.println("Enter coordinates to shoot. ");
        do{
            //reset invalid_shot on loop start
            invalid_shot = false;
            try{
                System.out.print("ROW: ");
                row = scan.nextInt();
                System.out.print("COL: ");
                col = scan.nextInt();
                System.out.println(); //blank line
            
                //decrement row and col by one for indexing
                row--;
                col--;
            
                //check if row or col out of bounds or if the player has already shot at the cell
                if ((row >= SIZE || row < 0) || (col >= SIZE || col < 0)){
                    invalid_shot = true;
                    System.out.println("Coordinates out of bounds. ");
                } else if ((p_shots[row][col] == MISS_CELL) || (p_shots[row][col] == HIT_CELL)){
                    invalid_shot = true;
                    System.out.println("You have already shot at that cell. ");
                }
                if(invalid_shot){
                    System.out.println(); //blank line
                    System.out.println("Reenter coordinates: ");
                }
            } catch (InputMismatchException e){
                invalid_shot = true;
                System.out.println("Invalid input. Row and column numbers must be whole numbers.");
                System.out.println(); //blank line
                System.out.println("Reenter coordinates: ");
                scan.next(); //remove invalid input from input stream
            }
        }while(invalid_shot);
                
        System.out.println("You shot at:");
        System.out.println("Row "+(row+1)+", Column "+(col+1));
        System.out.println("----");
                
        //check if the shot coordinates hit a ship cell, make it a hit cell if so and a miss cell if not
        if(c_board[row][col] == DESTROYER_CELL || c_board[row][col] == CRUISER_CELL || c_board[row][col] == SUBMARINE_CELL || c_board[row][col] == BATTLESHIP_CELL || c_board[row][col] == AIR_CARRIER_CELL){
            System.out.println("HIT!");
            
            //update c_hits if one of the computer's ships were hit
            switch(c_board[row][col]){
                case(DESTROYER_CELL):
                    c_hits[0]++; //increment hits on destroyer by 1
                    if(c_hits[0] == DESTROYER_SIZE){
                        System.out.println("You sunk the computer's destroyer!");
                    }
                    break;
                case(CRUISER_CELL):
                    c_hits[1]++; //increment hits on cruiser by 1
                    if(c_hits[1] == CRUISER_SIZE){
                        System.out.println("You sunk the computer's cruiser!");
                    }
                    break;
                case(SUBMARINE_CELL):
                    c_hits[2]++; //increment hits on submarine by 1
                    if(c_hits[2] == SUBMARINE_SIZE){
                        System.out.println("You sunk the computer's submarine!");
                    }
                    break;
                case(BATTLESHIP_CELL):
                    c_hits[3]++; //increment hits on battleship by 1
                    if(c_hits[3] == BATTLESHIP_SIZE){
                        System.out.println("You sunk the computer's battleship!");
                    }
                    break;
                case(AIR_CARRIER_CELL):
                    c_hits[4]++; //increment hits on aircraft carrier by 1
                    if(c_hits[4] == AIR_CARRIER_SIZE){
                        System.out.println("You sunk the computer's aircraft carrier!");
                    }
                    break;
                default:
                    System.out.println("Invalid ship hit.");
                    break;
                }
                p_shots[row][col] = HIT_CELL;
                c_board[row][col] = HIT_CELL;
        }else{
            p_shots[row][col] = MISS_CELL;
            c_board[row][col] = MISS_CELL;
            System.out.println("MISS");
        }
        System.out.println("----");
    }
    
    /*
    Method:
    cpu_shoot
    -----
    Parameters:
    char[][] p_board - the player's board to be updated with a hit or miss cell
    char[][] c_shots - the computer's shots used to keep track of what the computer shot
    int difficulty - the difficulty of the computer, at higher difficulties implement a different method of shooting
    int[] p_hits - the hits on the player's ships
    String[] logic - the computer's "memory" to aid in shooting at higher difficulties; read activity log 2024-06-14 for more detail
    -----
    Returns:
    void
    -----
    Description:
    This method contains the computer's logic for shooting.
    At difficulty 0 (easy), generates a random row and column and attempts to shoot at those coordinates.
    If the computer already shot at those coordinates, it will generate another row and column until it is a valid spot to hit.
    At difficulty 1 (normal), the computer will shoot around a spot that it hit until it sinks a ship, and will randomly generate a shot when there are no leads to pursue.
    Updates p_hits if the computer hits a ship.
    Uses pass-by-reference to make edits.
    */
    public static void cpu_shoot(char[][] p_board, char[][] c_shots, int difficulty, int[] p_hits, String[] logic){
        Random rand = new Random();
        
        //variables
        boolean invalid_shot = false;
        boolean hit = false;
        boolean miss = false;
        int row = 0;
        int col = 0;
        //only used in normal mode
        boolean sunk_ship = false;
        String[] coordinates = new String[DIMENSIONS];
        int next_row = 0;
        int next_col = 0;
        int root_row = 0;
        int root_col = 0;
      
        //how the computer generates its row and column based on difficulty
        switch(difficulty){
            //easy
            //generate random row and column number
            case(0):
                //keep generating shots if the computer already hit that cell
                do{
                    //reset invalid_shot on new shot
                    invalid_shot = false;
                    //generate random coordinates to shoot from 0-9
                    row = rand.nextInt(SIZE);
                    col = rand.nextInt(SIZE);
                
                    if((c_shots[row][col] == MISS_CELL) || (c_shots[row][col] == HIT_CELL)){
                        invalid_shot = true;
                    }
                }while(invalid_shot);
                
                break;
            //normal
            //generate random row and column number, use priority shot if available
            case(1):
                //shoot at a random row and column number if not tracking a ship to shoot
                if(logic[CPU_SHOOTING].equals("idle")){
                    do{
                        //reset invalid_shot on new shot
                        invalid_shot = false;
                        //generate random coordinates to shoot from 0-9
                        row = rand.nextInt(SIZE);
                        col = rand.nextInt(SIZE);
                    
                        if((c_shots[row][col] == MISS_CELL) || (c_shots[row][col] == HIT_CELL)){
                            invalid_shot = true;
                        }
                    }while(invalid_shot);   
                }else{
                    //set up shot based on priority shot
                    coordinates = logic[CPU_PRIORITY].split(",");
                    row = Integer.parseInt(coordinates[0]);
                    col = Integer.parseInt(coordinates[1]);

                    //define root coordinates of ship
                    coordinates = logic[CPU_ROOT].split(",");
                    root_row = Integer.parseInt(coordinates[0]);
                    root_col = Integer.parseInt(coordinates[1]);
                }
                
                break;
        }

        //check if the shot coordinates hit a ship cell, make it a hit cell if so and a miss cell if not
        if(p_board[row][col] == DESTROYER_CELL || p_board[row][col] == CRUISER_CELL || p_board[row][col] == SUBMARINE_CELL || p_board[row][col] == BATTLESHIP_CELL || p_board[row][col] == AIR_CARRIER_CELL){
            hit = true;
        }else{
            miss = true;
        }

        //output shot message and check if a ship was sunk
        System.out.println("The computer shot at:");
        System.out.println("Row "+(row+1)+", Column "+(col+1));
        System.out.println("----");
        if(hit){
            System.out.println("HIT!");
                    
            //update p_hits if one of the player's ships were hit
            switch(p_board[row][col]){
                case(DESTROYER_CELL):
                    p_hits[0]++; //increment hits on destroyer by 1
                    if(p_hits[0] == DESTROYER_SIZE){
                        System.out.println("The computer sunk your destroyer!");
                        sunk_ship = true;
                    }
                    break;
                case(CRUISER_CELL):
                    p_hits[1]++; //increment hits on cruiser by 1
                    if(p_hits[1] == CRUISER_SIZE){
                        System.out.println("The computer sunk your cruiser!");
                        sunk_ship = true;
                    }
                    break;
                case(SUBMARINE_CELL):
                    p_hits[2]++; //increment hits on submarine by 1
                    if(p_hits[2] == SUBMARINE_SIZE){
                        System.out.println("The computer sunk your submarine!");
                        sunk_ship = true;
                    }
                    break;
                case(BATTLESHIP_CELL):
                    p_hits[3]++; //increment hits on battleship by 1
                    if(p_hits[3] == BATTLESHIP_SIZE){
                        System.out.println("The computer sunk your battleship!");
                        sunk_ship = true;
                    }
                    break;
                case(AIR_CARRIER_CELL):
                    p_hits[4]++; //increment hits on aircraft carrier by 1
                    if(p_hits[4] == AIR_CARRIER_SIZE){
                        System.out.println("The computer sunk your aircraft carrier!");
                        sunk_ship = true;
                    }
                    break;
                default:
                    System.out.println("Invalid ship hit.");
                    break;
            }
            //update c_shots and p_board
            c_shots[row][col] = HIT_CELL;
            p_board[row][col] = HIT_CELL;
        }else if(miss){
            c_shots[row][col] = MISS_CELL;
            p_board[row][col] = MISS_CELL;
            System.out.println("MISS");
        }
        System.out.println("----");

        //determine next priority shot/what to do based on hit or miss given current data
        if(difficulty == 1){
            if(hit){
                if(sunk_ship){
                    //reset computer logic, try and find another ship next shot
                    reset_logic(logic);
                }
                //if a ship is hit when the computer is idle, begin tracking the ship and change logic[CPU_SHOOTING] to "shooting"
                else if(logic[CPU_SHOOTING].equals("idle")){
                    //start shooting at a new ship
                    logic[CPU_SHOOTING] = "shooting";
                    logic[CPU_ROOT] = ""+row+","+col;
                    logic[CPU_DIRECTION] = "right";

                    //define root coordinates of ship
                    coordinates = logic[CPU_ROOT].split(",");
                    root_row = Integer.parseInt(coordinates[0]);
                    root_col = Integer.parseInt(coordinates[1]);
                }
            }

            //if out of bounds in the direction or is going to hit a cell already hit, check in other directions and shoot in a valid direction
            //shooting and checking direction order is: right -> left -> up -> down
            if(logic[CPU_DIRECTION].equals("up")){
                if(hit){ //keep going in the direction if something is hit
                    next_row = row-1;
                    next_col = col;
                }
                if(miss || next_row < 0 || (p_board[next_row][next_col] == HIT_CELL || p_board[next_row][next_col] == MISS_CELL)){ // change to next direction in order if miss/out of bounds/already hit
                    logic[CPU_DIRECTION] = "down";
                    next_row = root_row+1;
                    next_col = root_col;
                }
            }
            else if(logic[CPU_DIRECTION].equals("down")){
                if(hit){
                    next_row = row+1;
                    next_col = col;
                }
                if(miss || next_row == SIZE || (p_board[next_row][next_col] == HIT_CELL || p_board[next_row][next_col] == MISS_CELL)){
                    System.out.println("CPU must have sunk a ship at this point.");
                }
            }
            else if(logic[CPU_DIRECTION].equals("left")){
                if(hit){
                    next_row = row;
                    next_col = col-1;
                }
                if(miss || next_col < 0 || (p_board[next_row][next_col] == HIT_CELL || p_board[next_row][next_col] == MISS_CELL)){
                    logic[CPU_DIRECTION] = "up";
                    next_row = root_row-1;
                    next_col = root_col;
                    if(next_row < 0 || (p_board[next_row][next_col] == HIT_CELL || p_board[next_row][next_col] == MISS_CELL)){
                        logic[CPU_DIRECTION] = "down";
                        next_row = root_row+1;
                        next_col = root_col;
                    }
                }
            }
            else if(logic[CPU_DIRECTION].equals("right")){
                if(hit){
                    next_row = row;
                    next_col = col+1;
                }
                if(miss || next_col == SIZE || (p_board[next_row][next_col] == HIT_CELL || p_board[next_row][next_col] == MISS_CELL)){
                    logic[CPU_DIRECTION] = "left";
                    next_row = root_row;
                    next_col = root_col-1;
                    if(next_col < 0 || (p_board[next_row][next_col] == HIT_CELL || p_board[next_row][next_col] == MISS_CELL)){
                        logic[CPU_DIRECTION] = "up";
                        next_row = root_row-1;
                        next_col = root_col;
                        if(next_row < 0 || (p_board[next_row][next_col] == HIT_CELL || p_board[next_row][next_col] == MISS_CELL)){
                            logic[CPU_DIRECTION] = "down";
                            next_row = root_row+1;
                            next_col = root_col;
                        }
                    }
                }
            }

            //set the priority shot to the next row and next column if the computer is currently shooting
            if(logic[CPU_SHOOTING].equals("shooting")){
                logic[CPU_PRIORITY] = ""+next_row+","+next_col;
            }
        }
    }
    
    /*
    Method:
    reset_logic
    -----
    Parameters:
    String[] logic - the array that contains the computer's shooting "memory"
    -----
    Returns:
    void
    -----
    Description:
    Reset logic sets all values in cpu_shoot_logic to their default values.
    Uses pass-by-reference to make edits.
    */
    public static void reset_logic(String[] logic){
        //set all items in logic to blank
        for(int i = 0; i<CPU_LOGIC_ITEMS; i++){
            if(i == CPU_SHOOTING){
                logic[i] = "idle";
            }else{
                logic[i] = "";
            }
        }
    }

    /*
    Method:
    check_ships
    -----
    Parameters:
    char[][] p_board - the player's board to check for ship cells
    char[][] c_board - the computer's board to check for ship cells
    -----
    Returns:
    int game_status - the status of the game; 0 game not over, 1 for player loss, 2 for player win
    -----
    Description:
    This method loops through both p_board and c_board and checks if there are no more ships on a board.
    If the computer has no more ships, the player wins, and if the player has no ships, the player loses. If both still have ships, the game continues.
    Also checks if any ships have been sunk in the turn and prints out a message if they have.
    Returns game_status as an indicator of game state.
    */
    public static int check_ships(char[][] p_board, char[][] c_board){
        //variables
        boolean c_game_over = true;
        boolean p_game_over = true;
        int game_status = 0; //0 default/game not over, 1 for player loss, 2 for player win
        
        //check if either player board or computer board have any ship cells left
        for(int i = 0; i<SIZE; i++){
            for(int j = 0; j<SIZE; j++){
                if(p_board[i][j] == DESTROYER_CELL || p_board[i][j] == CRUISER_CELL || p_board[i][j] == SUBMARINE_CELL || p_board[i][j] == BATTLESHIP_CELL || p_board[i][j] == AIR_CARRIER_CELL){
                    p_game_over = false;
                }
                if(c_board[i][j] == DESTROYER_CELL || c_board[i][j] == CRUISER_CELL || c_board[i][j] == SUBMARINE_CELL || c_board[i][j] == BATTLESHIP_CELL || c_board[i][j] == AIR_CARRIER_CELL){
                    c_game_over = false;
                }
            }
        }
        
        //if player has no ships, player loses
        if(p_game_over){
            game_status = 1;
        }
        //if computer has no ships, player wins
        else if(c_game_over){
            game_status = 2;
        }
        //if both sides have ships, game continues
        else{
            game_status = 0;
        }
        
        return game_status;
    }
    
    /*
    Method:
    update_hits
    -----
    Parameters:
    char[][] p_board - the player's board to check for ship cells
    char[][] c_board - the computer's board to check for ship cells
    -----
    Returns:
    void
    -----
    Description:
    This method is called when a save file is loaded, and updates both player and cpu hit arrays to the proper amount of hits.
    Calculates hits by checking how many ship cells of each ship remain on the board and subtracting it by the ship’s size.
    This method is only called when a save file is loaded, as every other update to the hit arrays are done on player/computer shoot.
    This method uses pass-by-reference to make edits.
    */
    public static void update_hits(char[][] p_board, char[][] c_board, int[] p_hits, int[] c_hits){
        //variables
        int[] p_ships = new int[NUM_SHIPS];
        int[] c_ships = new int[NUM_SHIPS];
        int curr_ship_size = 0;
      
        //loop through boards
        for(int i = 0; i<SIZE; i++){
            for(int j = 0; j<SIZE; j++){
                //increment p_ships if the current cell is a ship cell
                switch(p_board[i][j]){
                    case(DESTROYER_CELL):
                        p_ships[0]++;
                        break;
                    case(CRUISER_CELL):
                        p_ships[1]++;
                        break;
                    case(SUBMARINE_CELL):
                        p_ships[2]++;
                        break;
                    case(BATTLESHIP_CELL):
                        p_ships[3]++;
                        break;
                    case(AIR_CARRIER_CELL):
                        p_ships[4]++;
                        break;
                    //no default, pass if not matching a ship cell
                }
                        
                //increment c_ships if the current cell is a ship cell
                switch(c_board[i][j]){
                    case(DESTROYER_CELL):
                        c_ships[0]++;
                        break;
                    case(CRUISER_CELL):
                        c_ships[1]++;
                        break;
                    case(SUBMARINE_CELL):
                        c_ships[2]++;
                        break;
                    case(BATTLESHIP_CELL):
                        c_ships[3]++;
                        break;
                    case(AIR_CARRIER_CELL):
                        c_ships[4]++;
                        break;
                    //no default, pass if not matching a ship cell
                }
            }
        }
        
        //update hits
        for(int i = 0; i<NUM_SHIPS; i++){
            //get size of ship to be checked
            switch(i){
                case(0):
                    curr_ship_size = DESTROYER_SIZE;
                    break;
                case(1):
                    curr_ship_size = CRUISER_SIZE;
                    break;
                case(2):
                    curr_ship_size = SUBMARINE_SIZE;
                    break;
                case(3):
                    curr_ship_size = BATTLESHIP_SIZE;
                    break;
                case(4):
                    curr_ship_size = AIR_CARRIER_SIZE;
                    break;
                default:
                    System.out.println("Ship to be checked out of bounds.");
                    break;
            }
            //update hits based on cells remaining of each ship
            p_hits[i] = curr_ship_size - p_ships[i];
            c_hits[i] = curr_ship_size - c_ships[i];
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
    From there, it fills each row with a nested for-loop.
    Finally, array_data is returned. 
    */
    public static char[][] load_file(String save_path, int load_choice){
        //variables
        boolean ready_to_load = false;
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
            while(!ready_to_load && file_line != null){
                switch(load_choice) {
                    case(PLAYER_BOARD):
                        if(file_line.equals("Player ships:")){
                            ready_to_load = true;
                        } else {
                            file_line = reader.readLine();
                        }
                        break;
                    case(CPU_BOARD):
                        if(file_line.equals("CPU ships:")){
                            ready_to_load = true;
                        } else {
                            file_line = reader.readLine();
                        }
                        break;
                    case(PLAYER_SHOTS):
                        if(file_line.equals("Player shots:")){
                            ready_to_load = true;
                        } else {
                            file_line = reader.readLine();
                        }
                        break;
                    case(CPU_SHOTS):
                        if(file_line.equals("CPU shots:")){
                            ready_to_load = true;
                        } else {
                            file_line = reader.readLine();
                        }
                        break;
                    default:
                        System.out.println("Load choice out of bounds.");
                        System.out.println(); //blank line
                        break;
                }
            }

            //once file_line has reached the choice's header, start filling array
            if(ready_to_load){
                //fill array with data
                file_line = reader.readLine(); //go to next row
                //check if file line is null
                if(file_line != null){
                    for(int i = 0; i < SIZE; i++){
                        //check if it is null in the next row
                        if (file_line != null) {
                            for(int j = 0; j<file_line.length(); j++){
                                array_data[i][j] = file_line.charAt(j);
                            }
                        }
                        file_line = reader.readLine();
                    }
                }
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
    Writes the game's data into a file with the path save_path.
    Writes the player's board, computer's board, the player's shots, and the computer's shots, with headers.
    */
    public static void save_game(String save_path, char[][] p_board, char[][] c_board, char[][] p_shots, char[][] c_shots){
        //create BufferedWriter writing to save.txt
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter(save_path));
            
            //write player board to file
            writer.write("Player ships:");
            writer.newLine(); //next line
            //loop through player_board
            for(int i = 0; i<SIZE; i++){
                for(int j = 0; j<SIZE; j++){
                    writer.write(p_board[i][j]);
                }
                writer.newLine(); //next line
            }
            
            //write computer board to file
            writer.write("CPU ships:");
            writer.newLine(); //next line
            //loop through player_board
            for(int i = 0; i<SIZE; i++){
                for(int j = 0; j<SIZE; j++){
                    writer.write(c_board[i][j]);
                }
                writer.newLine(); //next line
            }
            
            //write player shots to file
            writer.write("Player shots:");
            writer.newLine(); //next line
            //loop through player_board
            for(int i = 0; i<SIZE; i++){
                for(int j = 0; j<SIZE; j++){
                    writer.write(p_shots[i][j]);
                }
                writer.newLine(); //next line
            }
            
            //write computer shots to file
            writer.write("CPU shots:");
            writer.newLine(); //next line
            //loop through player_board
            for(int i = 0; i<SIZE; i++){
                for(int j = 0; j<SIZE; j++){
                    writer.write(c_shots[i][j]);
                }
                writer.newLine(); //next line
            }

            //close BufferedWriter
            writer.close();
        } catch (IOException e) {
            System.out.println("Error writing to save file.");
            System.out.println(e);
        }
    }

    /*
    Method:
    clear_boards
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
    This method sets all elements in the player and computer's board/shots to '-', clearing all non-empty cells on the boards.
    Uses pass-by-reference to make changes.
    */
    public static void clear_boards(char[][] p_board, char[][] c_board, char[][] p_shots, char[][] c_shots){
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
    This method sets all chars in the player and computer's board/shots to the integer 0, resetting the board to an empty state.
    Uses pass-by-reference to make changes.
    */
    public static void reset_boards(char[][] p_board, char[][] c_board, char[][] p_shots, char[][] c_shots){
        //set all elements to int 0
        for(int i = 0; i<SIZE; i++){
            for(int j = 0; j<SIZE; j++){
                p_board[i][j] = 0;
                c_board[i][j] = 0;
                p_shots[i][j] = 0;
                c_shots[i][j] = 0;
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
    This method checks and returns whether or not the array passed to it has any null cells (which is 0 for char), not one of the valid cells, and if it's not the size it should be.
    */
    public static boolean is_loaded(char[][] array_data){
        //variables
        boolean is_valid = true;

        //check if any cell is a null or not a valid cell
        for (int i = 0; i<SIZE; i++){
            for(int j = 0; j<SIZE; j++){

                if (array_data[i][j] == 0 ||
                !(array_data[i][j] == EMPTY_CELL || array_data[i][j] == HIT_CELL || array_data[i][j] == MISS_CELL || array_data[i][j] == DESTROYER_CELL || array_data[i][j] == CRUISER_CELL || array_data[i][j] == SUBMARINE_CELL || array_data[i][j] == BATTLESHIP_CELL || array_data[i][j] == AIR_CARRIER_CELL) ||
                array_data[i].length != SIZE) {

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
    This method asks the user for a new save path and checks if it can be written to by creating a File instance for the file at the save path.
    If it does not pass the checks, the user is asked to reenter the save path.
    */
    public static String new_save_path(){
        Scanner scan = new Scanner(System.in);

        //variables
        boolean valid_save_path = false;
        String save_path = "";

        System.out.println("Enter the save file name (with .txt):");
        do{
            System.out.print(" > ");
            save_path = scan.nextLine();

            //check if the file exists, is a file, is writable to, and has a .txt extension
            File file = new File(save_path);
            if (file.exists() && file.isFile() && file.canWrite() && save_path.endsWith(".txt")){
                valid_save_path = true;
            } else {
                //make user reenter save path if the path for the file entered is invalid 
                System.out.println("Error accessing save file \""+save_path+"\".");
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
    This method loops through the 2d array passed to it and prints all elements using a nested for loop.
    Also prints row and column numbers for coordinates on the left and top of the board.
    */
    public static void print_board(char[][] board){
        //print the column numbers
        System.out.print(" ");
        for(int i = 1; i<=SIZE; i++){
            System.out.printf("%3d", i);
        }
        System.out.println(); //blank line (new line)

        //loop through and print all items of the board 
        for(int i = 0; i<SIZE; i++){
            //print row numbers
            System.out.printf("%2d",i+1);
            for(int j = 0; j < SIZE; j++){
                if(j == 0){
                    System.out.printf("%2c", board[i][j]);
                }else{
                    System.out.printf("%3c", board[i][j]);
                }
            }
            System.out.println(); //new line
        }
    }
    
    /*
    Method:
    print_boards
    -----
    Parameters:
    char[][] ship_board - the first board to be printed (should contain the ships)
    char[][] shots_board - the second board to be printed (should contain the shots)
    int mode - 0 for ships/shots header, 1 for player/computer header
    -----
    Returns:
    void
    -----
    Description:
    Print_boards will print 2 arrays passed to it, where the first array passed is the board for ships and the second array passed is the board for shots. 
    It will print the column numbers at the top and row numbers to the left of each board as coordinates.
    */
    public static void print_boards(char[][] ship_board, char[][] shots_board, int mode){
        //print header based on mode
        switch(mode){
            case(0):
                System.out.printf("%-37sShots:\n","Ships:");
                break;
            case(1):
                System.out.printf("%-37sComputer Board:\n","Player Board:");
                break;
            default:
                System.out.println("INVALID HEADER MODE");
                break;
        }
        //print the numbers at the top for ships
        System.out.print(" ");
        for(int i = 1; i<=SIZE; i++){
            System.out.printf("%3d", i);
        }

        //print the gap between each board's numbers
        System.out.print("     ");

        //print the numbers at the top for shots
        System.out.print(" ");
        for(int i = 1; i<=SIZE; i++){
            System.out.printf("%3d", i);
        }

        System.out.println(); //blank line (new line)

        //loop through and print all items of both boards
        for(int i = 0; i<SIZE; i++){
            System.out.printf("%2d",i+1);
            for(int j = 0; j < SIZE; j++){
                if(j == 0){
                    System.out.printf("%2c", ship_board[i][j]);
                }else{
                    System.out.printf("%3c", ship_board[i][j]);
                }
            }

            //print gap between boards
            System.out.print("     ");

            //print row number for shots
            System.out.printf("%2d",i+1);

            //print the shots board
            for(int j = 0; j < SIZE; j++){
                if(j == 0){
                    System.out.printf("%2c", shots_board[i][j]);
                }else{
                    System.out.printf("%3c", shots_board[i][j]);
                }
            }
            System.out.println(); //new line
        }
    }
    
    /*
    Method:
    game_over_message
    -----
    Parameters:
    int game_over - the condition of the game end
    char[][] p_board - the player's board to be printed if needed
    char[][] c_board - the computer's board to be printed if needed
    -----
    Returns:
    void
    -----
    Description:
    Game_over will take the condition of the game's end and output a message based on it.
    */
    public static void game_over_message(int game_over, char[][] p_board, char[][] c_board){
        //output message based on game_over
        switch(game_over){
            //player loss
            case(1):
                System.out.println("********");
                System.out.println("YOU LOST...");
                System.out.println("********");
                System.out.println(); //blank line
                print_boards(p_board, c_board, 1);
                System.out.println(); //blank line
                break;
            //player win
            case(2):
                System.out.println("********");
                System.out.println("YOU WON!");
                System.out.println("********");
                System.out.println(); //blank line
                print_boards(p_board, c_board, 1);
                System.out.println(); //blank line
                break;
            //player quit in-game
            case(3):
                print_boards(p_board, c_board, 1);
                System.out.println(); //blank line
                break;
            //player quit out-of-game
            case(4):
                //no special message
                break;
            //invalid game state
            default:
                System.out.println("Invalid game state.");
                System.out.println(); //blank line
                break;
        }
        
        System.out.println("Returning to main menu...");
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
        System.out.println("A -  Carrier (5 spaces)");
        System.out.println("B -  Battleship (4 spaces)");
        System.out.println("C -  Cruiser (3 spaces)");
        System.out.println("S -  Submarine (3 spaces)");
        System.out.println("D -  Destroyer (2 spaces)");
        System.out.println();

        System.out.println("Placing Ships");
        System.out.println("Player's Ships:");
        System.out.println("Place your ships on your grid.");
        System.out.println("Ships can be placed horizontally or vertically.");
        System.out.println("Ships cannot overlap or be placed outside the grid.");
        System.out.println();
        System.out.println("When placing on a row and column horizontally, your ship will place like this:");
        System.out.println("Row 1, Column 2");
        System.out.println("  1 2 3 4 5 6 ");
        System.out.println("1 - X X X X - ");
        System.out.println("2 - - - - - - ");
        System.out.println();
        System.out.println("When placing on a row and column vertically, your ship will place like this:");
        System.out.println("Row 1, Column 2");
        System.out.println("  1 2 3");
        System.out.println("1 - X -");
        System.out.println("2 - X -");
        System.out.println("3 - X -");
        System.out.println("4 - X -");
        System.out.println("5 - - -");
        System.out.println();

        System.out.println("Computer's Ships:");
        System.out.println("The computer will automatically place its ships on its grid.");
        System.out.println();

        System.out.println("Playing the Game");
        System.out.println("Taking Turns:");
        System.out.println("You and the computer take turns firing shots at each others' ships.");
        System.out.println("Enter the coordinates for your shot when it's your turn.");
        System.out.println("Example:");
        System.out.println("ROW: 5");
        System.out.println("COL: 4");
        System.out.println();

        System.out.println("Checking Shots:");
        System.out.println("If the shot hits an opponent's ship, the hit is marked on your grid with an 'X'");
        System.out.println("If the shot misses, the miss is marked on your grid with an 'O'.");
        System.out.println("The computer will also take shots at your grid and the hits and misses will be marked accordingly.");
        System.out.println();

        System.out.println("Sunken Ships:");
        System.out.println("When all parts of a ship are hit, the ship is sunk.");
        System.out.println("You are told when you or the computer sunk a ship (e.g., \"The computer sunk your battleship!\")");
        System.out.println();
    }
}
