Implementation
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
