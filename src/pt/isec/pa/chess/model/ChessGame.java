package pt.isec.pa.chess.model;

import pt.isec.pa.chess.model.data.*;
import pt.isec.pa.chess.model.data.pieces.King;
import pt.isec.pa.chess.model.data.pieces.Pawn;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Represents a Chess Game, managing the board state, player turns, captured pieces,
 * and game logic such as movement, checks, and checkmate conditions.
 */
public class ChessGame implements Serializable{
    @Serial
    private static final long serialVersionUID = 1L;

    public static final String DIVIDER = ",";
    private static final String INITIALGAME = "RA1*"+ DIVIDER +"NB1"+ DIVIDER +"BC1"+ DIVIDER +"QD1"+ DIVIDER +"KE1*"+ DIVIDER +"BF1"+ DIVIDER +"NG1"+ DIVIDER +"RH1*"+ DIVIDER +"PA2"+ DIVIDER +"PB2"+ DIVIDER +"PC2"+ DIVIDER +"PD2"+ DIVIDER +"PE2"+ DIVIDER +"PF2"+ DIVIDER +"PG2"+ DIVIDER +"PH2"+ DIVIDER +"pA7"+ DIVIDER +"pB7"+ DIVIDER +"pC7"+ DIVIDER +"pD7"+ DIVIDER +"pE7"+ DIVIDER +"pF7"+ DIVIDER +"pG7"+ DIVIDER +"pH7"+ DIVIDER +"rA8*"+ DIVIDER +"nB8"+ DIVIDER +"bC8"+ DIVIDER +"qD8"+ DIVIDER +"kE8*"+ DIVIDER +"bF8"+ DIVIDER +"nG8"+ DIVIDER +"rH8*";

    private final String whitesName, blacksName;
    private final List<Piece> whiteScore, blackScore;
    private ColorType currentPlayer;
    private final Board board;


    /**
     * Default {@code ChessGame} constructor.
     * <br><br>
     * Creates a default Chess Game with default player names "Player 1" and "Player 2"
     * and the first player set to White.
     */
    public ChessGame(){
        this.currentPlayer = ColorType.WHITE;
        this.board = new Board(new String[0]);
        this.whitesName = "Player 1";
        this.blacksName = "Player 2";
        this.whiteScore = new ArrayList<>();
        this.blackScore = new ArrayList<>();
    }

    /**
     * {@code ChessGame} constructor with player names.
     * <br><br>
     * Creates a Chess Game with specified player names for the white and black players,
     * and the first player set to White.
     *
     * @param whitesName the name of the white pieces player
     * @param blacksName the name of the black pieces player
     */
    public ChessGame(String whitesName, String blacksName) {
        this.currentPlayer = ColorType.WHITE;
        this.board = new Board(INITIALGAME.split(DIVIDER));
        this.whitesName = whitesName.isEmpty() || whitesName.isBlank() ? "Player 1" : whitesName;
        this.blacksName = blacksName.isEmpty() || blacksName.isBlank() ? "Player 2" : blacksName;
        this.whiteScore = new ArrayList<>();
        this.blackScore = new ArrayList<>();
    }

    /**
     * {@code ChessGame} Builder with custom starting player and board setup.
     *
     * @param game the file text content
     */
    public ChessGame(String game, String whitesName, String blacksName){
        this.whitesName = whitesName.isEmpty() || whitesName.isBlank() ? "Player 1" : whitesName;
        this.blacksName = blacksName.isEmpty() || blacksName.isBlank() ? "Player 2" : blacksName;
        this.whiteScore = new ArrayList<>();
        this.blackScore = new ArrayList<>();

        String[] pieces = game.split(DIVIDER);
        this.currentPlayer = ColorType.getColorType(pieces[0]);
        this.board = new Board(Arrays.copyOfRange(pieces, 1, pieces.length));
    }


    public int getRowSize() { return this.board.getRowSize(); }

    public int getColumnSize() { return this.board.getColumnSize(); }


    public String getWhitesName() { return this.whitesName; }

    /**
     * Returns a list of character representations of the Pieces captured by the White player.
     *
     * @return a {@code List<Character>} containing the representations of the captured black pieces
     */
    public List<Character> getWhiteScore() {
        List<Character> res = new ArrayList<>();
        for(Piece p : this.whiteScore)
            res.add(p.getRepresentation());

        return res;
    }

    public String getBlacksName() { return this.blacksName; }

    /**
     * Returns a list of character representations of the Pieces captured by the Black player.
     *
     * @return a {@code List<Character>} containing the representations of the captured white pieces
     */
    public List<Character> getBlackScore() {
        List<Character> res = new ArrayList<>();
        for(Piece p : this.blackScore)
            res.add((p.getRepresentation()));

        return res;
    }

    /**
     * Adds the captured Piece to the Player log
     * */
    private void addToScore(Piece piece) {
        if(piece.getColor() == ColorType.WHITE) {
            this.blackScore.add(piece);
            ModelLog.getInstance().log("Black took "+ piece.getRepresentation());
        }
        else {
            this.whiteScore.add(piece);
            ModelLog.getInstance().log("White took " + piece.getRepresentation());
        }
    }

    public ColorType getCurrentPlayer() { return this.currentPlayer; }

    /**
     * Switches the current player from {@code WHITE} to {@code BLACK} or vice versa.
     */
    private void changePlayer() {
        switch(this.currentPlayer) {
            case WHITE -> this.currentPlayer = ColorType.BLACK;
            case BLACK -> this.currentPlayer = ColorType.WHITE;
        }
    }

    public List<String> getBoardPieces() {
        return Arrays.stream(this.board.toString().split(DIVIDER)).toList();
    }


    /**
     * Retrives the representation of a Piece on a given postion
     *
     * @param row the row of the piece (1–8)
     * @param column the column of the piece (A–H)
     * @return the {@code char} representation of the piece at the given position, or a space character (' ') if the board is {@code null} or there is no piece at the position.
     */
    public char getPiece(int row, char column){
        if(this.board == null) return ' ';

        Piece p = this.board.getPiece(row,column);
        if(p == null) return ' ';

        return p.getRepresentation();
    }

    /**
     * Retrieves the name of the Piece on a given postion.
     *
     * @param row the row of the piece (1–8)
     * @param column the column of the piece (A–H)
     * @return the name of the piece as a {@code String}, or an empty string if the board is {@code null} or no piece is present.
     */
    public String getPieceName(int row, char column) {
        if(this.board == null) return "";

        Piece p = this.board.getPiece(row,column);
        if(p == null) return "";

        return p.getName();
    }

    /**
     * Retrieves the possible moves for the piece at the specified position.
     *
     * @param row the row of the piece (1–8)
     * @param column the column of the piece (A–H)
     * @return a {@code boolean[][]} where {@code true} cells represent valid moves.
     */
    public boolean[][] getPieceMoves(int row, char column) {
        Piece p = this.board.getPiece(row, column);
        boolean[][] res = new boolean[getRowSize()][getColumnSize()];

        if(p != null) {
            MoveType[][] moves = p.getMoves(this.board);
            for(int r = 0; r < getRowSize(); r++)
                for(int c = 0; c < getColumnSize(); c++)
                    res[r][c] = moves[r][c] != MoveType.NONE;
        }

        return res;
    }

    /**
     * Attempts to move a piece from its current position to the target position on the board.
     * <p>
     * The move is only allowed if the piece exists, belongs to the current player, and the player is not in check
     * unless moving the king. The piece's new position is updated after the move.
     *
     * @param pieceRow the current row of the piece (1–8)
     * @param pieceColumn the current column of the piece (A–H)
     * @param row the target row (1–8)
     * @param column the target column (A–H)
     * @return a {@link MoveType} defining the success of the movement.
     */
    public MoveType movePiece(int pieceRow, char pieceColumn, int row, char column) {
        MoveType res;
        Piece piece = this.board.getPiece(pieceRow, pieceColumn);
        if(piece == null || piece.getColor() != this.currentPlayer)
            return MoveType.NONE;

        // Simulate Move
        Board b = new Board(this.board);
        int victim = b.getPieceIndex(row,column);
        res = b.movePiece(pieceRow, pieceColumn,row,column);

        if(res != MoveType.NONE && victim != -1)
                b.popPiece(victim);
        if(res != MoveType.NONE && check(b))
            return MoveType.NONE;

        victim = this.board.getPieceIndex(row,column);
        res = this.board.movePiece(pieceRow, pieceColumn,row,column);
        if(res != MoveType.NONE) {
            changePlayer();
            if(victim != -1)
                addToScore(this.board.popPiece(victim));
        }

        return res;
    }

    /**
     * Determines whether the current player's is in check.
     *
     * @return {@code true} if the king is under threat by any opponent piece; {@code false} otherwise
     */
    public boolean check() {
        King king = this.currentPlayer == ColorType.WHITE ? this.board.getWhiteKing() : this.board.getBlackKing();

        return this.board.getOpponentMoves(this.currentPlayer)[king.getRow()-1][Piece.getColumnIndex(king.getColumn())] == MoveType.TAKE;
    }
    private boolean check(Board board) {
        if(board == null) return true;

        King king = this.currentPlayer == ColorType.WHITE ? board.getWhiteKing() : board.getBlackKing();

        return board.getOpponentMoves(this.currentPlayer)[king.getRow()-1][Piece.getColumnIndex(king.getColumn())] == MoveType.TAKE;
    }

    /**
     * Determines whether the current player's is in checkmate.
     *
     * @return {@code true} if the current player is in checkmate; {@code false} otherwise
     */
    public boolean checkmate() {
        if(!check())
            return false;

        King king = this.currentPlayer == ColorType.WHITE ? this.board.getWhiteKing() : this.board.getBlackKing();
        MoveType[][] opponentMoves = this.board.getOpponentMoves(this.currentPlayer);
        MoveType[][] kingMoves = king.getMoves(this.board);

        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++)
                if (kingMoves[i][j] != MoveType.NONE && opponentMoves[i][j] == MoveType.NONE) // the king can move
                    return false;

        // TODO : verificar se algum movimento remove o check

        return true;

    }

    /**
     * Evolves the last moved piece
     *
     * @param type the char representation of the piece to evolve to
     * */
    public void evolve(char type) {
        Piece p = this.board.getLastMovedPiece();
        if(p == null)
            return;

        // check if the piece belongs to the player
        // (is inverted because it's called after the move is done, aka: the current player is not the one "doing" evolving the piece)
        if(p.getColor() == this.currentPlayer)
            return;

        if(!(p instanceof Pawn pawn))
            return;

        // the Pawn is not on the right Row
        if((pawn.getColor() == ColorType.WHITE && pawn.getRow() != 8) || (pawn.getColor() == ColorType.BLACK && pawn.getRow() != 1))
            return;

        Piece e = this.board.popPiece(this.board.getPieceIndex(p.getRow(),p.getColumn()));
        this.board.addPiece(PieceFactory.createPiece(PieceType.getType(type), e.getRow(), e.getColumn(), e.getColor(), true));
    }

    @Override
    public String toString() {
        return ColorType.getColorTypeText(this.currentPlayer) + DIVIDER + board.toString();
    }

}
