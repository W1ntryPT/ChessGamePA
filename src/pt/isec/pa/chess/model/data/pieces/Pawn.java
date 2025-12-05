package pt.isec.pa.chess.model.data.pieces;

import pt.isec.pa.chess.model.data.Board;
import pt.isec.pa.chess.model.data.ColorType;
import pt.isec.pa.chess.model.data.MoveType;
import pt.isec.pa.chess.model.data.Piece;

import java.util.Arrays;

public class Pawn extends Piece {

	private boolean firstMove;
	private boolean enPassant;

	public Pawn(int row, char collunn, ColorType color) {
		super("pawn", color == ColorType.WHITE ? 'P' : 'p', row, collunn, color);
		this.firstMove = color == ColorType.WHITE ? row == 2 : row == 7;
		this.enPassant = false;
	}


	@Override
	public MoveType move(int row, char column, Board board) {
		int oldRow = getRow();
		MoveType res = getMoves(board)[row-1][getColumnIndex(Character.toUpperCase(column))];

		if(res != MoveType.NONE) {
			if(res == MoveType.ENPASSAT)
				row += getColor() == ColorType.WHITE ? 1 : -1;

			firstMove = false;
			setRow(row);
			setColumn(column);
			enPassant = Math.abs(getRow() - oldRow) == 2;
		}

		// evolve
		if(getColor() == ColorType.WHITE ? board.getRowSize() == getRow() : getRow() == 1)
			return MoveType.EVOLVE;

		return res;
	}

	@Override
	public MoveType[][] getMoves(Board board) {
		int rowDirection = getColor() == ColorType.WHITE ? 1 : -1;
		MoveType[][] res = new MoveType[board.getRowSize()][board.getColumnSize()];
		for(MoveType[] r : res)
			Arrays.fill(r,MoveType.NONE);

		for(int i = 1; i <= (firstMove ? 2 : 1); i++) {
			int row = getRow()+rowDirection*i;
			Piece p = board.getPiece(row, getColumn());

			if(p != null) break;
			if(row < 1 || row > 8) break;

			res[row-1][getColumnIndex(getColumn())] = MoveType.MOVE;
		}

		Piece piece = board.getPiece(getRow()+rowDirection, (char) (getColumn()+1));
		if(piece != null && piece.getColor() != getColor())
			res[piece.getRow()-1][getColumnIndex((char) (getColumn()+1))] = MoveType.TAKE;

		piece = board.getPiece(getRow()+rowDirection, (char) (getColumn()-1));
		if(piece != null && piece.getColor() != getColor())
			res[piece.getRow()-1][getColumnIndex((char) (getColumn()-1))] = MoveType.TAKE;

		piece = board.getPiece(getRow(), (char) (getColumn()+1));
		if(piece instanceof Pawn pawn && pawn.enPassant && pawn.getColor() != getColor())
			res[pawn.getRow()-1][getColumnIndex(pawn.getColumn())] = MoveType.ENPASSAT;

		piece = board.getPiece(getRow(), (char) (getColumn()-1));
		if(piece instanceof Pawn pawn && pawn.enPassant && pawn.getColor() != getColor())
			res[pawn.getRow()-1][getColumnIndex(pawn.getColumn())] = MoveType.ENPASSAT;


		return res;
	}

}