=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=
CIS 120 Game Project README
PennKey: 60906986
=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=

===================
=: Core Concepts :=
===================

- List the four core concepts, the features they implement, and why each feature
  is an appropriate use of the concept. Incorporate the feedback you got after
  submitting your proposal.

  1. 2D Arrays
  I used a 2D Array to represent the 8 by 8 checkerboard. The implementation of the board through a 2D Array made sense
  because I was able to store the state of the board through a data structure that would only be modified if I chose to
  modify it. Through using a 2D array of integers, I was able to represent where checker pieces were able to move (0),
  where red checker pieces were on the board (1), where white checker pieces were on the board (2), where red kings were
  on the board (3), where white kings were on the board (4), and where checker pieces were not allowed to move (-1).
  Having this information saved in the model allowed me to update the GUI correctly corresponding to where certain
  integers were located within the 2D array as players began to move pieces.

  2. Collections
  I used a LinkedList to store red and white player's moves so that they could undo their last move if they chose to.
  Since the ordering of the moves a player made was important, it made sense to make a list because order matters
  within a list. Furthermore, since undoing a move only requires getting the last element within a list, it made sense
  to use a LinkedList because I would only be adding and removing from the head of the list.
  When a user undos their move, the last move they made will be popped off the list.

  3. File I/O
  I implemented File I/O to save current game progress so that users could load their most previous game if they chose
  to do so. It made sense to implement File I/O to save game state because I was able to just save my Checkers model
  into a file, and read from it to load it into the model if the user chose to reload the game. Since the model
  did not contain that much information, it was easy to parse through this information with a Buffered Reader and also
  write to it using a Buffered Writer. If it weren't for saving the game state through a file, I do not know how else
  I would save the game state.

  4. JUnit testable component
  I used JUnit tests to test how my game state was being updated. By using JUnit tests, I only needed to use the
  checkers model to see if my game state was being updated properly or not when a user plays the game. From separating
  the GUI from tests, I was actually able to see if the 2D array was modified correctly along with other attributes in
  the Checkers model. Even though it helps to use the GUI to test one's game, the GUI is not as reliable as actually
  testing how data structures respond to changes made by the player through JUnit. Something could seem like it is
  working perfectly while in reality the game state is not being updated correctly with each move.

=========================
=: Your Implementation :=
=========================

- Provide an overview of each of the classes in your code, and what their
  function is in the overall game.
  1. Checkers
  This class is a model for the checkers game. It stores and modifies the actual game state through data structures
  implemented.
  2. GameBoard
  This class is the view-controller for the checkers game. It responds to user input and updates the model according
  to user input.
  3. RunCheckers
  This class is the view of the checkers game. It presents the model's data to the user and merely displays the game.
  4. Move
  This class represents a move made by a player via the coordinates in the board. It also keeps track if the move is
  a jump and what the checker piece is via identity (white, red, king). This is mainly used in the Checkers model to
  keep track of game state and update it accordingly once an event is registered by the GameBoard.

- Were there any significant stumbling blocks while you were implementing your
  game (related to your design, or otherwise)?
While implementing my game, I had a lot of trouble at first figuring out how to separate the view, model, and view-
controller. I did not understand how each class corresponded to one another and should affect each other, however
after looking over the Tic Tac Toe design, I was able to follow along and proceed with my implementation.
After understanding more so how to separate each class into its respective functions for the game, I struggled with
implementing the Checkers class the most. Updating game state correctly took a lot of time for me to implement,
especially after adding jumps and king movements. There were a lot of moving parts, and I had to ensure that
every function was modifying the game state in the way that I desired.

- Evaluate your design. Is there a good separation of functionality? How well is
  private state encapsulated? What would you refactor, if given the chance?
I believe that my design is good in terms of separation functionality. By following the MVC design, I was able to
separate the view, model, and view-controller so that each class was able to act on its own. By keeping user-interaction
to the view-controller, output to view, and updating game state to model, I was efficiently able to create the game
without having such a big class with overloaded functionality. In terms of private state, I believe that my
classes are well encapsulated. All fields within classes are private and only can be accessed through getter and setter
functions. If given the chance to refactor, I don't think I would overall change my design at all. I would only
add features to my game, such as allowing players to double jump. Otherwise, I feel like every class does the job
it is supposed to and every data structure I use has a good reason to be used for the game's features.

========================
=: External Resources :=
========================

- Cite any external resources (images, tutorials, etc.) that you may have used 
  while implementing your game.
