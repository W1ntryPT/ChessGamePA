package pt.isec.pa.chess.model.data;

import pt.isec.pa.chess.model.data.pieces.*;

/**
 * Factory class for creating {@link Piece} objects.
 */
public class PieceFactory {

	private PieceFactory() {}

	/**
	 * Creates a new {@link Piece} object based on the provided parameters.
	 *
	 * @param type the type of the piece (e.g., KING, QUEEN, KNIGHT, etc.)
	 * @param row the row position of the piece on the board (1–8)
	 * @param column the column position of the piece on the board (A–H)
	 * @param color the color of the piece.
	 * @param hasMoved a boolean indicating if the piece has moved; {@code true} if the piece has moved, {@code false} otherwise
	 * @return a {@link Piece} object corresponding to the specified parameters
	 */
	public static Piece createPiece(PieceType type, int row, char column, ColorType color, boolean hasMoved) {
		return switch(type) {
			case KING -> new King(row, column, color, hasMoved);
			case QUEEN -> new Queen(row, column, color);
			case KNIGHT -> new Knight(row, column, color);
			case BISHOP -> new Bishop(row, column, color);
			case ROOK -> new Rook(row, column, color, hasMoved);
			case PAWN -> new Pawn(row, column, color);
		};
	}

	/**
	 * Creates a new {@link Piece} object based on a string representation of the piece.
	 * <br>
	 * This method parses the string format that represents a piece, extracting its type,
	 * position, color, and movement status. The string format is assumed to be as follows:
	 * - The first character indicates the piece type.
	 * - The second character is the column (A-H).
	 * - The third character is the row (1-8).
	 * - An optional fourth character, '*' (if present), indicates that the piece has not moved.
	 *
	 * @param text a string representation of the piece in the format: <pieceType><column><row>[optional '*']
	 * @return a {@link Piece} object created based on the string representation
	 * @throws IllegalArgumentException if the string format is invalid
	 * @implNote To determine the piece type, check the {@link PieceType} to understand which letters correspond to each type of piece.
	 */
	public static Piece createPiece(String text) {
		if(text == null || text.isEmpty() || text.isBlank())
			return null;

		PieceType type  = PieceType.getType(text.charAt(0));
		char collumn  = text.charAt(1);
		int row = Integer.parseInt(String.valueOf(text.charAt(2)));
		ColorType color = Character.isUpperCase(text.charAt(0)) ? ColorType.WHITE : ColorType.BLACK;
		boolean hasMoved = text.length() != 4 || text.charAt(3) != '*';

		return createPiece(type, row, collumn, color, hasMoved);
	}

}