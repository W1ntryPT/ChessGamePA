package pt.isec.pa.chess.model.data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Arrays;

public abstract class Piece implements Cloneable, Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * @param column Character representing the Column
	 * @return index of a Chess Piece Column
	 * @throws IllegalArgumentException case the column value is not between 'A' and 'H'
	 */
	public static int getColumnIndex(char column) {
		column = Character.toUpperCase(column);
		if(column < 'A' || column > 'H')
			throw new IllegalArgumentException("Invalid Column value '" + column + "'");

		return column - 'A';
	}

	/**
	 * @param index value of the column
	 * @return Character representing the Column of a Chess Piece
	 * @throws IllegalArgumentException case the index value is not between 1 and 8
	 */
	public static int getIndexColumn(int index) {
		if(index < 0 || index > 8)
			throw new IllegalArgumentException("Invalid Index of a Column '" + index + "'");

		return (char) (index) +  'A';
	}


	private final ColorType color;
	private final char representation;
	private int row;
	private char column;
	private final String name;


	protected Piece(String name, char representation, int row, char column, ColorType color) {
		this.color = color;
		this.name = name;
		this.representation = representation;
		this.row = row;
		this.column = Character.toUpperCase(column);
	}


	/**
	 * @return {@link ColorType} the color of the piece.
	 */
	public ColorType getColor() { return this.color; }

	/**
	 * @return {@code true} if the piece is White otherwise returns {@code false}.
	 */
	public char getRepresentation() { return this.representation; }

	public String getName() { return this.name; }

	/**
	 * @return the row location of the piece
	 */
	public int getRow() { return this.row; }

	/**
	 * Sets the Row of a Piece and checks if the value is legal
	 */
	public void setRow(int row) {
		if(row >= 1 && row <= 8)
			this.row = row;
	}

	/**
	 * @return the collumn location of the piece
	 */
	public char getColumn() { return this.column; }

	/**
	 * Sets the Column of a Piece and checks if the value is legal
	 */
	public void setColumn(char column) {
		column = Character.toUpperCase(column);
		if(column >= 'A' && column <= 'H')
			this.column = column;
	}

	/**
	 * Attempts to move the piece to a new position based on the given row and column index.
	 *
	 * @param row the row index (1-8) of the desired move.
	 * @param column the column character ('A' to 'H') of the desired move.
	 * @return {@code true} if the piece is moved and {@code false} if the move is illegal.
	 */
	public abstract MoveType move(int row, char column, Board board);

	/**
	 * Computes all possible moves for this piece based on its movement rules and the current board state.
	 *
	 * @param board the current game {@link Board}
	 * @return {@code boolean[][]} where the {@code true} cells represent possible moves.
	 */
	public abstract MoveType[][] getMoves(Board board);

	/**
	 * Calculates possible moves in specified directions for a given number of steps.
	 *
	 * @param rowDirections array of row direction deltas (e.g. -1, 0, 1)
	 * @param columnDirections array of column direction deltas matching rowDirections
	 * @param times maximum number of steps to move in each direction
	 * @param board the current game {@link Board}
	 * @return {@code boolean[][]} where {@code true} cells represent valid destination squares
	 */
	protected MoveType[][] getMoves(int[] rowDirections, int[] columnDirections, int times, Board board, MoveType default_move) {
		MoveType[][] res = new MoveType[board.getRowSize()][board.getColumnSize()];
		for(MoveType[] re : res)
			Arrays.fill(re, MoveType.NONE);

		if(rowDirections.length != columnDirections.length)
			return res;

		for(int d = 0; d < rowDirections.length; d++) {
			int row = getRow(); int column = getColumnIndex(getColumn());
			for(int t = 0; t < times; t++) {
				row += rowDirections[d];
				column += columnDirections[d];

				if((row <= 0 || row > board.getRowSize() || column < 0 || column >= board.getColumnSize()))
					break;

				Piece p = board.getPiece(row, (char) getIndexColumn(column));
				if(p != null) {
					if(p.color != color)
						res[row-1][column] = default_move;
					break;
				}

				res[row-1][column] = default_move;
			}
		}

		return res;
	}

	@Override
	public String toString() {
		return "" + getRepresentation() + getColumn() + getRow();
	}

	@Override
	public Piece clone() {
		try {
			return (Piece) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new AssertionError("ChessPiece Cloning failed: " + e);
		}
	}

}
