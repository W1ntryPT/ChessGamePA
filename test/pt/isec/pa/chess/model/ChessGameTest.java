package pt.isec.pa.chess.model;

import org.junit.Test;
import pt.isec.pa.chess.model.data.MoveType;

import static org.junit.Assert.*;

public class ChessGameTest {

	private static final String DIVIDER = ChessGame.DIVIDER;

	@Test
	public void roqueGrande() {
		String initialGame = "WHITE" + DIVIDER + "RA1*" + DIVIDER + "KE1*";

		ChessGame game = new ChessGame(initialGame, "Whites", "Blacks");

		MoveType res = game.movePiece(1, 'E', 1, 'A');

		assertEquals(res, MoveType.MOVE);
	}
	@Test
	public void roquePequeno() {
		String initialGame = "BLACK" + DIVIDER + "rH8*" + DIVIDER + "kE8*";

		ChessGame game = new ChessGame(initialGame, "Whites", "Blacks");

		MoveType res = game.movePiece(8, 'E', 8, 'H');

		assertEquals(res, MoveType.MOVE);
	}

	@Test
	public void enPassat() {
		String initialGame = "BLACK" + DIVIDER + "PA5" + DIVIDER + "pb7" + DIVIDER + "kE8" + DIVIDER + "KE1";
		ChessGame game = new ChessGame(initialGame, "Whites", "Blacks");

		game.movePiece(7, 'B', 5, 'B');
		MoveType res = game.movePiece(5, 'A', 5, 'B');

		assertEquals(res, MoveType.ENPASSAT);
	}

	@Test
	public void checkmate() {
		String initialGame = "WHITE" + DIVIDER + "RA1*" + DIVIDER +"NB1"+ DIVIDER +"BC1"+ DIVIDER +"QD1"+ DIVIDER +"KE1*"+ DIVIDER +"BF1"+ DIVIDER +"NG1"+ DIVIDER +"RH1*"+ DIVIDER +"PA2"+ DIVIDER +"PB2"+ DIVIDER +"PC2"+ DIVIDER +"PD2"+ DIVIDER +"PF2"+ DIVIDER +"PG2"+ DIVIDER +"PH2"+ DIVIDER +"pA7"+ DIVIDER +"pB7"+ DIVIDER +"pC7"+ DIVIDER +"pE7"+ DIVIDER +"pF7"+ DIVIDER +"pG7"+ DIVIDER +"pH7"+ DIVIDER +"rA8*"+ DIVIDER +"nB8"+ DIVIDER +"bC8"+ DIVIDER +"qD8"+ DIVIDER +"kE8*"+ DIVIDER +"bF8"+ DIVIDER +"nG8"+ DIVIDER +"rH8*";

		ChessGame game = new ChessGame(initialGame, "Whites", "Blacks");

		MoveType m = game.movePiece(7, 'F', 5, 'B');
		boolean res = game.checkmate();

		assertTrue(res);
	}

	@Test
	public void evolve() {
		String initialGame = "WHITE" + DIVIDER + "PA7" + DIVIDER + "KA1" + DIVIDER + "kH1";
		ChessGame game = new ChessGame(initialGame, "Whites", "Blacks");

		MoveType m = game.movePiece(7, 'A', 8, 'A');

		assertEquals(m, MoveType.EVOLVE);
	}

}