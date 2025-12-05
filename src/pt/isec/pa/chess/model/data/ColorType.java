package pt.isec.pa.chess.model.data;

/**
 * Enum representing the two player types in a chess game.
 */
public enum ColorType {
	WHITE, BLACK;

	/**
	 * Converts a string to its corresponding {@link ColorType}.
	 *
	 * @param type the string representation of the player type (e.g., "WHITE", "BLACK")
	 * @return the corresponding {@link ColorType}, or {@code null} if the input is invalid
	 * @implNote The string is case-insensitive. For example, "white", "WHITE", or "White" all return {@link #WHITE}.
	 */
	public static ColorType getColorType(String type) {
		return switch(type.toUpperCase()) {
			case "WHITE" -> WHITE;
			case "BLACK" -> BLACK;
			default -> null;
		};
	}

	/**
	 * Returns the string representation of the specified {@link ColorType}.
	 *
	 * @param type the {@link ColorType} to convert to a string
	 * @return "WHITE" if the type is {@link #WHITE}, "BLACK" if {@link #BLACK}
	 */
	public static String getColorTypeText(ColorType type) {
		return switch(type) {
			case WHITE -> "WHITE";
			case BLACK -> "BLACK";
		};
	}

}
