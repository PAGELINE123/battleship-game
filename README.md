# battleship-game
Expectations Overview
In this course, we have learned about several programming concepts:
IPO
Variables & user input
Branching
Looping
Arrays & multidimensional arrays
Methods
Exception handling
File I/O
Software engineering

For your final task in this course, you will be expected to combine all of these concepts into a singular system.  If you have an idea for a project that you would like to do, you are more than welcome to do so as long as:
Your idea incorporates all of the concepts outlined above in meaningful ways.
It is of a sufficient size & complexity to warrant being a culminating activity.
You prepare a written proposal that will serve as your project requirements document.
You meet with your course teacher with your completed project requirements document, and your design must be approved BEFORE you begin any coding.
If you are interested in doing your own idea, you must present your initial project requirements document to me BY FRIDAY, May 31st.  Proposals made after this date will not be accepted, and you will be expected to do the default assigned task, which is outlined at the beginning of the next page.
Any custom assignments which have not been approved in advance WILL NOT BE ACCEPTED.
Due date:					Monday, June 17th

Default Assignment Description:  Battleship
Should you decide you do not wish to create your own idea for your culminating activity, you are tasked with coding a game of Battleship.  This document will serve as your Project Requirements Document, and a partial System Design Document.  It is not required for you to complete the System Design document, but it is strongly recommended that you at least consider aspects of your system before you begin coding them, along with all of the other steps you would normally consider as part of the Software Engineering Process.
Battleship is a game played by two players.  Each player has two boards that they play with:  The ship board, and the shots board.  Each board is a 10 x 10 grid.
At the beginning of the game, each player will place 5 ships of different sizes on the ship board.  The ships and their sizes are:
Destroyer (2 squares long)
Cruiser (3 squares long)
Submarine (3 squares long)
Battleship (4 squares long)
Carrier (5 squares long)
The ships may be placed vertically or horizontally, but NOT diagonally.
After the ships have been placed, the players then take turns firing shots at their opponent’s ships.  A shot is fired by choosing a location by giving the number of a ROW and COLUMN.  The player is then told whether it is a hit (if the square contains one of their opponents ships) or a miss (if the square does not contain a ship).  The results of the guess are then recorded on the shots board.
When the opponent fires at a player, the results of the opponent’s guess are recorded on the player’s ship board.  A ship is sunk if all squares on that ship have been hit.
The game is won when all of an opponent’s ships have been sunk.
The image to the left shows what a player’s ship board might look like during a game in progress.
The ships are represented in blue.  ‘X’s represent shots which the opponent has taken.
Your program should allow the player to play against an AI opponent for the second player, with the option for variable difficulty.
If you would like to see how the game is played, you may try an example here:	https://www.battleshiponline.org/  (There is sound.  Check your volume before you click!)

Requirements:
Your program must contain the following features:
A welcome menu which should allow the user to choose between starting a new game, loading an old game, showing the instructions, or quitting your program.
A game board which displays the status of the game by showing the player’s ship board and their shots board
The user must be able to place their ships on the board before the game begins. They should be able to place each ship oriented either horizontally or vertically.
You should ensure that any ship placement works!
An interface that allows the user to choose a row and column to target on their turn.
If the user tries to make an invalid selection (eg., a location that is not on the board, or one that they have already tried) your program should print an error message and let them try again.
On the player’s turn, they should also be able to choose to surrender, or Save the game.
The board must update after each turn and display ships, hits and misses by both the player and the computer.
When the user chooses to save the game, your program will store the current status of the game in a text file, which can be loaded later.  The format of your text file is outlined in the File System Format section of this document.
When saving or loading a game, the player should be allowed to choose the name of the file that is saved or loaded.  If they choose a filename that already exists, that file should be overwritten.
When a ship is sunk, the interface should display a message indicating which player has sunk a ship, and the name of the ship sunk.
For example, “Computer has sunk your Battleship!”
Your game should end when either all of the computer’s ships or all of the player’s ships have been sunk.  The program should indicate who has won when a game ends.
If the user loses the game, or they choose to surrender, your program will show the computer’s ships board, along with the shots you have taken.
After a game ends, the user should be returned to the main menu.
The status of the game must be stored in two-dimensional arrays.
Methods should be used to accomplish any tasks which will be repeated regularly.
(e.g., taking shots, computer turns, displaying the board, etc.)
The user should have the option to choose the game’s difficulty:
If the user chooses “easy”, the computer will always fire randomly.
If the user chooses “normal”, the computer will fire randomly until it gets a hit, and then will aim for spots next to the last hit until it gets another hit, a ship is sunk or all the squares around the last hit have been tried.



The Interface:
Your program should have a text-based user interface.  All input should be entered via the keyboard, and all displays are represented by characters.
For example, your screen could look something like this:
Ships:                              Shots:
   1  2  3  4  5  6  7  8  9 10        1  2  3  4  5  6  7  8  9 10
 1 -  -  -  -  A  X  X  A  A  -      1 -  -  -  -  -  -  -  -  -  -
 2 -  D  -  -  -  -  -  -  -  -      2 -  -  -  -  -  O  -  -  -  -
 3 -  D  -  -  -  -  -  O  -  -      3 -  X  -  -  -  -  -  -  -  -
 4 -  -  -  -  B  B  B  B  -  -      4 -  X  -  -  -  -  -  -  -  -
 5 -  -  O  -  -  -  O  -  -  -      5 -  X  -  -  -  -  -  -  -  -
 6 -  -  -  -  -  -  -  -  -  -      6 -  O  -  -  -  -  O  -  -  -
 7 -  C  C  C  -  -  -  -  S  -      7 -  -  -  -  -  -  -  -  -  -
 8 -  -  -  -  -  O  -  -  S  -      8 -  -  -  -  -  -  -  -  -  -
 9 -  -  -  -  -  -  -  -  S  -      9 -  -  -  -  -  -  -  -  -  -
10 -  -  -  -  -  -  -  -  -  -     10 -  -  -  -  -  -  -  -  -  -


In the above example, ‘X’s represent hits, ‘O’s represent misses, ‘-’ represent unused squares, and ‘D’, ‘C’, ‘S’, ‘B’ and ‘A’ represent the Destroyer, Cruiser, Submarine, Battleship and Aircraft Carrier, respectively.
The left board shows the player’s ships, and the guesses the AI has made, and the right board shows all the guesses the player has made.
As the game progresses, the board should be re-printed whenever a change occurs so that the user is always aware of the current state of the game.
Be sure to tell the user what the AI’s guess was, and whether it was a miss or a hit.
Also note that the board displays the column and row numbers from 1 through 10, but your 2D arrays are zero-indexed.  This means your array indexes will run from 0 through 9, and your program will have to compensate for this.


System Design:
Your program should make use of constants where appropriate.  If you would like to make your constants available to ALL of your methods, for this assignment you may declare your constants (and ONLY your constants) as static variables within your class, but before your methods.
For example:		final static int SIZE = 10;

All other variables should only exist within the appropriate methods (including main), and should only pass information back and forth as parameters or return values.

Your program should use methods for any “procedures” or “actions” that your program would have to perform.  Some good examples might be:
Printing a menu screen
Displaying the current game board
Setting up an “empty” board for a new game
Loading a game from a file and setting up the board
Letting the player place a ship on the game board
Having the AI place their ships on the game board
Letting the player take their turn
Checking if a ship has been sunk
Checking if the game has been won
Saving the game
File System Format:
Your game should use a text file that contains 44 lines of text:
The first line of text should be a line of text acting as the title for the player’s ships board.
The next ten lines of text should each contain ten characters, representing the player’s ships, and the shots the computer has taken against them.
The next line of text should be a line of text for the title for the computer’s ships board.
This is followed by another ten lines of text of ten characters, representing the computer’s ships, and the shots the player has taken against it.
The next 11 lines should be similar, but the board should be showing ONLY the shots the player has taken against the computer, with their ships hidden.
Similarly, the final 11 lines show ONLY the shots the CPU has taken against the computer, with the player’s shots hidden.

For the characters that represent the boards:
-	Represents and unused square	D, C, S, B, A	Represent different ship types
X	Represents a hit			O		Represents a miss

The file SaveExample.txt has been provided on brightspace.  Your game should be able to load this file correctly, and any games you save should follow a similar file format.
Remember that when loading a game, the player SHOULD NOT be able to see the Computer’s ships, they should be displayed as unused squares until they have been hit.
Hints:
Build your program one part at a time!  Pick a part to start with.  Do you want to get your file system working first?  Printing your board?  Player Turns?  Pick a place that makes sense for you to start.  Get that system working properly, test it to make sure it works (AND to make sure that you can’t cause it to fail!) and then move to another section.  It works best if you break the “actions” in your program down into methods, and then every time you finish coding a method, test it to ensure it works properly before you move on.
DON’T START CODING IMMEDIATELY!  Think about the overall logic of how the different parts of your program should work before you write any code!  You aren’t required to make a System Design document for this assignment, but you should still do the same process you would follow when creating one.  Building a program without a plan can make things very difficult in the long run!  This will be a complex assignment!
LEARN TO USE YOUR DEBUGGER!  As your program grows in size, it’s likely that most of the problems you have will be logical rather than syntax problems.  Because I will not be familiar with your program, it will be very difficult for me to find logical problems, and very often I will not have the time to search through your code to find them.  You will likely have to learn how to find these issues on your own.  Your best bet to find these is to learn how to effectively use your debugger!  If you would like me to show you how it works, let me know!
Once you have completed your system, try testing it.  Test it a lot.  Think about what are some of the inconvenient parts in its processes, and think about if there’s anything you could do to improve the user friendliness of your game.  Does everything work?  Have friends and family try it to see if they can use it, or to see if they experience problems anywhere.  These are likely places that you could enhance your program if you are interested in a better Thinking mark.  Any exceptions should be handled, and the user should not be able to crash your program.
If you aren’t sure how to structure your program, TALK TO ME ABOUT IT SOONER RATHER THAN LATER.  If you talk to me early, we can get you started on a good path first, which will be MUCH EASIER than trying to correct a poor path later on.
BACK.  UP.  YOUR.  WORK.  Whenever your program is in a “stable” state, you should save a back-up to your Google Drive.  Keep multiple versions (you can do this by changing the file name as you put the backups in your drive) -- you never know when you might have to revert to an older version of your code.
Assessment:

Mark break down:
200 marks
Welcome Screen with main menu giving choice of instructions, new game, load game, and exit
5 marks
If the player chooses to load a game, they should be able to choose the file
Your game is able to correctly load the SaveExample.txt file provided, as well as any save game your game creates
10 marks
If the player chooses a new game, they are able to set their ship locations at the beginning of the game
10 marks
If the player chooses a new game, the computer’s ships should be set in random locations on the board
10 marks
The status of the game should be stored in arrays
20 marks
An interface which displays the status of the player’s shots and ships boards
Any time something happens in the game, the board should be redisplayed
Interface shows row and column numbers from 1 through 10, NOT 0 to 9.
10 marks
The player should be able to choose their shot by giving a row and column
The system should ensure their selection is valid (proper input and on board)
A player cannot fire in a position they have previously tried
The player should receive informative feedback if they make a mistake
10 marks
The player should receive feedback after taking their turn whether their shot was a miss or a hit
5 marks
The player is able to save the game on their turn, and the game is correctly saved in a text file of the player’s choosing, according to the specified File System Format
If the chosen file already exists, that file should be overwritten
10 marks
The computer fires appropriately, and provides effective feedback regarding its actions (miss or a hit)
10 marks
Both difficulty levels have been implemented effectively:
Easy:  The computer fires randomly
Normal:  If the computer gets a hit, it will search for other hits nearby until a ship is sunk
15 marks
(Easy: 5)
(Normal: 10)
Proper feedback is given when a ship is sunk
(who sunk the ship, and the type of ship that was sunk)
5 marks
Game properly ends when either all of the player’s ships, or all of the computer’s ships have been sunk
Proper feedback is given when the game ends
5 marks
The user is able to surrender, and the computer’s ship board is displayed along with the player’s shots at the computer
10 marks
When a game ends, the user is returned to the main menu
5 marks
Proper use of methods for frequently performed functions
20 marks
Documentation, Style & Polish
Easy to read, properly formatted & commented code
Effective use of header blocks for all methods
System has clearly been tested and is free of bugs
20 marks
“Wow” factor – Going above & beyond to set your project apart from the rest
Good testing & error handling – bug free & program cannot be crashed
Well designed user interfaces & displays
Good control flow
Enhancements that go beyond requested features (MUST BE DOCUMENTED IN HEADER COMMENT BLOCK)
20 marks


