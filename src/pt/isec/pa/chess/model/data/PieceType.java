package pt.isec.pa.chess.model.data;

/**
 * Enum representing the types of chess pieces.
 */
public enum PieceType {
	KING, QUEEN, BISHOP, KNIGHT, ROOK, PAWN;

	/**
	 * Returns the {@link PieceType} corresponding to the provided character.
	 *
	 * @param type the character representing a piece type ('K', 'Q', 'B', 'N', 'R', 'P')
	 * @return the corresponding {@link PieceType}, or {@code null} if the character is invalid
	 */
	public static PieceType getType(char type) {
		return switch(Character.toUpperCase(type)) {
			case 'K' -> KING;
			case 'Q' -> QUEEN;
			case 'B' -> BISHOP;
			case 'N' -> KNIGHT;
			case 'R' -> ROOK;
			case 'P' -> PAWN;
			default -> null;
		};
	}
}
