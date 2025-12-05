package pt.isec.pa.chess.model;

import pt.isec.pa.chess.model.memento.CareTaker;
import pt.isec.pa.chess.model.memento.IMemento;
import pt.isec.pa.chess.model.memento.IOriginator;
import pt.isec.pa.chess.model.memento.Memento;
import pt.isec.pa.chess.model.data.ColorType;
import pt.isec.pa.chess.model.data.MoveType;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.util.List;


/**
 * Serves as a Facade that encapsulates the complexity of the underlying
 * Model, providing the UI with a simplified and consistent interface
 * for managing game state, moves, and other chess logic.
 */
public class ChessGameManager implements IOriginator {

	public static final String GAME_CHANGE = "GAME";
	public static final String PLAYER_CHANGE = "PLAYER";
	public static final String CHECKMATE = "CHECKMATE";
	public static final String EVOLVE = "EVOLVE";

	private ChessGame model;
	private final PropertyChangeSupport pcs;
	private final CareTaker careTaker;

	/**
	 * Constructs a new {@code ChessGameManager}, initializing a fresh chess game model,
	 * property change support, caretaker for undo/redo, and default settings.
	 * <p>
	 * Acts as a Facade between the UI and the Model, providing a simplified interface
	 * for managing game state and operations.
	 */
	public ChessGameManager() {
		this.model = new ChessGame();
		this.pcs = new PropertyChangeSupport(this);
		this.careTaker = new CareTaker(this);
	}


	/**
	 * Constructs a new {@code ChessGameManager}, initializing a fresh chess game model,
	 * property change support, caretaker for undo/redo, and default settings.
	 */
	public void addListener(String property, PropertyChangeListener listener) {
		pcs.addPropertyChangeListener(property,listener);
	}

	/**
	 * Creates a new Chess Game.
	 *
	 * @param whitesName the name of the player playing White
	 * @param blacksName the name of the player playing Black
	 */
	public void newGame(String whitesName, String blacksName) {
		this.model = new ChessGame(whitesName, blacksName);
		this.pcs.firePropertyChange(GAME_CHANGE, null, null);
		this.careTaker.reset();
		ModelLog.getInstance().reset();
		ModelLog.getInstance().log("New game created!");
	}

	/**
	 * Opens a saved chess game from the specified file.
	 *
	 * @param file the file containing the serialized chess game.
	 * @return {@code true} if the game is successfully opened and deserialized; {@code false} otherwise.
	 *
	 * @implNote The file should be a serialized file with a ".ser" extension.
	 */
	public boolean open(File file){
		ChessGame new_chessgame = ChessSerialization.deserialize(file);
		if(new_chessgame == null)
			return false;

		this.model = new_chessgame;
		this.pcs.firePropertyChange(GAME_CHANGE, null, null);
		this.careTaker.reset();
		ModelLog.getInstance().reset();
		ModelLog.getInstance().log("Opened game!");
		return true;
	}

	/**
	 * Saves the current chess game to the specified file.
	 *
	 * @param file the file to save the chess game to.
	 * @return {@code true} if the game is successfully saved; {@code false} otherwise.
	 *
	 * @implNote The file should be a serialized file with a ".ser" extension.
	 */
	public boolean save(File file){
		return ChessSerialization.serialize(file,this.model);
	}

	/**
	 * Imports a game from the specified file containing serialized game state.
	 *
	 * @param file the file containing the game state in text format.
	 * @return {@code true} if the game is successfully imported; {@code false} otherwise.
	 *
	 * @implNote The file should be a serialized file with a ".txt" extension.
	 */
	public boolean importGame(File file, String whitesName, String blacksName){
		String text = null;
		try {
			text = Files.readString(file.toPath());

			if(text == null || text.isEmpty() || text.isBlank())
				return false;

			this.model = new ChessGame(text, whitesName, blacksName);
			this.pcs.firePropertyChange(GAME_CHANGE, null, null);
			this.careTaker.reset();
			ModelLog.getInstance().reset();
			ModelLog.getInstance().log("Game imported!");

			return true;

		} catch (Exception ignore) {
			return false;
		}
	}

	/**
	 * Exports the current game to a text file.
	 *
	 * @param file the file to which the game will be exported.
	 * @return {@code true} if the game is successfully exported; {@code false} otherwise.
	 *
	 * @implNote The file should be a text file with a ".txt" extension.
	 */
	public boolean exportGame(File file){
		try (FileWriter fileWriter = new FileWriter(file)) {

			fileWriter.write(model.toString());
			fileWriter.close();

			return true;
		} catch (Exception ignore) {
			return false;
		}
	}

	public int getRowSize() { return this.model.getRowSize(); }

	public int getColumnSize() { return this.model.getColumnSize(); }

	public ColorType getCurrentPlayer() { return this.model.getCurrentPlayer(); }

	public String getWhitesName() { return this.model.getWhitesName(); }

	public String getBlacksName() { return this.model.getBlacksName(); }

	public List<Character> getWhiteScore() { return this.model.getWhiteScore(); }

	public List<Character> getBlackScore() { return this.model.getBlackScore(); }

	/**
	 * Checks if the current game is in Check.
	 *
	 * @return {@code true} if the current player is in check; {@code false} otherwise.
	 */
	public boolean check() {
		return this.model.check();
	}

	/**
	 * Checks if the current game is in checkmate.
	 *
	 * @return {@code true} if the current player is in checkmate; {@code false} otherwise.
	 */
	public boolean checkmate() {
		return this.model.checkmate();
	}

	/**
	 * Retrieves the character representation of the Piece at the specified position.
	 *
	 * @param row the row of the piece (1–8)
	 * @param column the column of the piece (A–H)
	 * @return the character representing the piece at the given position, or a space (' ') if no piece exists there.
	 */
	public char getPiece(int row, char column) { return this.model.getPiece(row, column); }

	/**
	 * Retrieves the name of the Piece at the specified position.
	 *
	 * @param row the row of the piece (1–8)
	 * @param column the column of the piece (A–H)
	 * @return the name of the piece as a String, or an empty string if no piece exists at the given position.
	 */
	public String getPieceName(int row, char column) { return this.model.getPieceName(row, column); }

	/**
	 * Retrieves the possible moves for the piece at the specified position.
	 *
	 * @param row the row of the piece (1–8)
	 * @param column the column of the piece (A–H)
	 * @return a {@code boolean[][]} where {@code true} cells represent valid moves.
	 */
	public boolean[][] getPieceMoves(int row, char column) {
		return this.model.getPieceMoves(row, column);
	}

	/**
	 * Attempts to move a piece from its current position to the target position on the board.
	 *
	 * @param pieceRow the current row of the piece to be moved (1–8)
	 * @param pieceColumn the current column of the piece to be moved (A–H)
	 * @param row the target row (1–8)
	 * @param column the target column (A–H)
	 * @return a {@link MoveType} defining the success of the movement.
	 */
	public MoveType movePiece(int pieceRow, char pieceColumn, int row, char column) {
		this.careTaker.save();

		MoveType res = this.model.movePiece(pieceRow, pieceColumn, row, column);

		this.pcs.firePropertyChange(GAME_CHANGE, null, null);

		if(res != MoveType.NONE) {
			this.pcs.firePropertyChange(PLAYER_CHANGE, null, null);
			ModelLog.getInstance().log("" + getPiece(row, column) + "" + pieceRow + "" + pieceColumn + " to " + row + "" + column);
		}

		if(res == MoveType.EVOLVE)
			this.pcs.firePropertyChange(EVOLVE, null, null);

		if(checkmate())
			pcs.firePropertyChange(CHECKMATE, false, true);

		return res;
	}

	/**
	 * Evolves the last moved piece
	 *
	 * @param type the char representation of the piece to evolve to
	 * */
	public void evolve(char type) {
		this.model.evolve(type);
		pcs.firePropertyChange(GAME_CHANGE, null, null);
	}

	/**
	 * Checks whether there is a previous game state available to undo.
	 *
	 * @return {@code true} if an undo operation is possible; {@code false} otherwise.
	 */
	public boolean canUndo() { return this.careTaker.hasUndo(); }

	/**
	 * Reverts the game to the previous state, if available.
	 */
	public void undo() {
		if(!canUndo()) return;

		this.careTaker.undo();
		pcs.firePropertyChange(GAME_CHANGE, null, null);
	}

	/**
	 * Checks whether there is a future game state available to redo.
	 *
	 * @return {@code true} if a redo operation is possible; {@code false} otherwise.
	 */
	public boolean canRedo() { return this.careTaker.hasRedo(); }

	/**
	 * Re-applies a previously undone game state, if available.
	 */
	public void redo() {
		if(!canRedo()) return;

		this.careTaker.redo();
		pcs.firePropertyChange(GAME_CHANGE, null, null);
	}

	@Override
	public IMemento save() {
		return new Memento(this.model);
	}

	@Override
	public void restore(IMemento memento) {
		Object obj = memento.getSnapshot();
		if(obj instanceof ChessGame m)
			this.model = m;
	}
}
