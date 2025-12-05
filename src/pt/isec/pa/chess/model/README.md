## Model classes

### ChessGameManager
...

### ChessGame
...

### Board
Responsible for the representation and management of the game board.
Keeps a list of all the Chess Pieces involved in the game and enbales the user to interact with them.

### Piece
Represnts a Chest Piece and all the methods and variables a Piece needs to have to function in the game setting.
Being an **Abstract Class** each type of piece inherits from it, wiht the base pieces being:
- King
- Queen
- Knight
- Bishop
- Rook
- Pawn

> Each piece defines how it moves on the board by overiding the movement method.

---

## Helper Classes and Enums

### PieceFactory
...

### ChessSerialization
...

---

### ColorType
...

### MoveType
...

### PieceType
...