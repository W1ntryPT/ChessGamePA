package pt.isec.pa.chess.model;

import java.io.*;


/**
 * Utility class for serializing and deserializing {@link ChessGame} objects.
 */
public class ChessSerialization {

	private ChessSerialization() {}

	/**
	 * Serializes a {@link ChessGame} object to the specified file.
	 *
	 * @param file the destination file where the {@code ChessGame} will be saved
	 * @param chessGame the {@code ChessGame} object to serialize
	 * @return {@code true} if serialization succeeds; {@code false} otherwise
	 * @implNote The file should have a ".ser" extension to indicate it stores a serialized object.
	 */
	public static boolean serialize(File file, ChessGame chessGame) {

		try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
			out.writeObject(chessGame);
			return true;
		} catch(Exception ignore) {
			return false;
		}

	}

	/**
	 * Deserializes a {@link ChessGame} object from the specified file.
	 *
	 * @param file the source file containing the serialized {@code ChessGame}
	 * @return the deserialized {@code ChessGame} object; or {@code null} if deserialization fails
	 * @implNote The file must be a valid-serialized file created by {@link #serialize(File, ChessGame)}.
	 */
	public static ChessGame deserialize(File file) {

		try(ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
			return (ChessGame) in.readObject();
		} catch(Exception ignore) {
			return null;
		}

	}

}
