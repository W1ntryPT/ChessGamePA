package pt.isec.pa.chess.model.data;

import pt.isec.pa.chess.model.ChessGame;
import pt.isec.pa.chess.model.data.pieces.King;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Represents the chessboard in a Chess Game.
 * <p>
 * The {@code Board} class maintains a list of all active pieces and provides
 * core functionalities for manipulating them, such as adding, moving, or removing pieces.
 *
 * @see Piece
 * @see PieceFactory
 * @see MoveType
 */
public class Board implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	private static final int RowSize = 8;
	private static final int ColumnSize = 8;

	private final List<Piece> pieces;
	private Piece last_piece;


	/**
	 * {@code Board} constructor that initializes the board with the specified pieces.
	 *
	 * @param pieces an array of strings representing the initial pieces on the board, where each string defines a piece.
	 *
	 * @implNote The piece creation is delegated to the {@link PieceFactory}, which handles
	 *           the parsing and instantiation of the pieces based on their string representations.
	 */
	public Board(String[] pieces) {
		this.last_piece = null;
		this.pieces = new ArrayList<>();
		if(pieces == null)
			return;
		for(String p : pieces)
			this.pieces.add(PieceFactory.createPiece(p));
	}

	/**
	 * Creates a deep copy of the given {@code Board}, cloning all pieces to ensure
	 * the new board instance is independent of the original.
	 *
	 * @param board the {@code Board} to copy
	 */
	public Board(Board board) {
		this.last_piece = null;
		this.pieces = new ArrayList<>();
		for (Piece piece : board.pieces)
			this.pieces.add(piece.clone());
	}

	public int getRowSize() { return RowSize; }

	public int getColumnSize() { return ColumnSize; }


	/**
	 * Attempts to add a {@code Piece} to the board.
	 * <p>
	 * A piece can only be added if there is no other piece occupying its target position.
	 *
	 * @param piece the {@code Piece} to add to the board
	 * @return {@code true} if the piece was successfully added; {@code false} if the position is already occupied.
	 */
	public boolean addPiece(Piece piece) {
		if(getPiece(piece.getRow(), piece.getColumn()) != null)
			return false;

		this.pieces.add(piece);
		return true;
	}

	/**
	 * Removes and returns the {@code Piece} at the specified index from the board.
	 *
	 * @param index the index of the piece to remove from the internal list
	 * @return the removed {@code Piece}
	 * @throws IndexOutOfBoundsException if the index is out of range
	 */
	public Piece popPiece(int index) {
		return this.pieces.remove(index);
	}

	public Piece getLastMovedPiece() { return this.last_piece; }

	/**
	 * Returns a list of cloned {@link Piece} objects.
	 *
	 * @return a new {@link List} containing cloned copies of the {@link Piece} objects.
	 */
	public List<Piece> getPieces() {
		return this.pieces.stream().map(Piece::clone).collect(Collectors.toList());
	}

	/**
	 * Retrieves a copy of the chess piece at the specified position on the board.
	 *
	 * @param row the row position (1 - 8) on the board
	 * @param column the column position (A - H) on the board
	 * @return a copy of the requested {@code ChessPiece} if it exists, or {@code null} if no piece exists at the given position.
	 */
	public Piece getPiece(int row, char column) {
		return this.pieces.stream().filter(piece -> piece.getRow() == row && piece.getColumn() == Character.toUpperCase(column)).findFirst().orElse(null);
	}

	/**
	 * Retrieves the index of the chess piece at the specified position on the board.
	 *
	 * @param row the row position (1 - 8) on the board
	 * @param column the column position (A - H) on the board
	 * @return the index of the chess piece or {@code -1} if the piece is not found.
	 */
	public int getPieceIndex(int row, char column) {
		Piece p = this.pieces.stream().filter(piece -> piece.getRow() == row && piece.getColumn() == Character.toUpperCase(column)).findFirst().orElse(null);

		return p == null ? -1 : this.pieces.indexOf(p);
	}

	/**
	 * Retrieves the White King from the board.
	 *
	 * @return the White {@link King} piece, or {@code null} if no White King is found.
	 */
	public King getWhiteKing() {
		return this.pieces.stream() .filter(piece -> piece instanceof King && piece.getColor() == ColorType.WHITE) .findFirst() .map(piece -> (King) piece.clone()).orElse(null);
	}

	/**
	 * Retrieves the Black King from the board.
	 *
	 * @return the Black {@link King} piece, or {@code null} if no Black King is found.
	 */
	public King getBlackKing() {
		return this.pieces.stream() .filter(piece -> piece instanceof King && piece.getColor() == ColorType.BLACK) .findFirst() .map(piece -> (King) piece.clone()).orElse(null);
	}

	/**
	 * Calculates all possible moves for the opponent's pieces.
	 *
	 * @param currentPlayer the color of the current player.
	 * @return a {@code boolean[][]} where {@code true} cells indicate squares that are threatened by the opponent.
	 */
	public MoveType[][] getOpponentMoves(ColorType currentPlayer) {
		MoveType[][] moves = new MoveType[8][8];
		for(MoveType[] r : moves)
			Arrays.fill(r, MoveType.NONE);

		for (Piece piece : this.pieces.stream().filter(piece -> piece.getColor() != currentPlayer).toList()) {
			MoveType[][] pieceMoves = piece.getMoves(this);
			for (int i = 0; i < 8; i++)
				for (int j = 0; j < 8; j++)
					moves[i][j] = pieceMoves[i][j] == MoveType.TAKE ? MoveType.TAKE : moves[i][j];
		}

		return moves;
	}

	/**
	 * Moves a chess piece to a new position on the board. The piece is identified by its index in the list of pieces.
	 *
	 * @param row    the target row position to move the piece to.
	 * @param column the target column position to move the piece to (a character from 'a' to 'h').
	 * @return a {@link MoveType} defining the success of the movement.
	 */
	public MoveType movePiece(int pieceRow, char pieceColumn, int row, char column) {
		Piece p = this.pieces.stream().filter(piece -> piece.getRow() == pieceRow && piece.getColumn() == Character.toUpperCase(pieceColumn)).findFirst().orElse(null);
		if(p == null) return MoveType.NONE;

		MoveType res = p.move(row,column, this);
		this.last_piece = p;

		return res;
	}


	@Override
	public String toString() {
		StringBuilder res = new StringBuilder();

		for(Piece p : this.pieces)
			res.append(p.toString()).append(ChessGame.DIVIDER);

		return res.toString();
	}

}
