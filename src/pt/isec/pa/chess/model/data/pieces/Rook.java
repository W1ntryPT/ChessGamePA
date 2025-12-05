package pt.isec.pa.chess.model.data.pieces;

import pt.isec.pa.chess.model.data.Board;
import pt.isec.pa.chess.model.data.ColorType;
import pt.isec.pa.chess.model.data.MoveType;
import pt.isec.pa.chess.model.data.Piece;

public class Rook extends Piece {

	private boolean hasMoved;

	public Rook(int row, char collunn, ColorType color) {
		super("rook", color == ColorType.WHITE ? 'R' : 'r', row, collunn, color);
		this.hasMoved = false;
	}

	public Rook(int row, char column, ColorType color, boolean hasMoved) {
		super("rook", color == ColorType.WHITE ? 'R' : 'r', row, column, color);
		this.hasMoved = hasMoved;
	}


	public boolean hasMoved() { return this.hasMoved; }

	@Override
	public MoveType move(int row, char column, Board board) {
		MoveType res = getMoves(board)[row-1][getColumnIndex(Character.toUpperCase(column))];

		if(res != MoveType.NONE) {
			setRow(row);
			setColumn(column);
			this.hasMoved = true;
		}

		return res;
	}

	@Override
	public MoveType[][] getMoves(Board board) {
		int[] rowDirections = {-1, 1, 0, 0};
		int[] columnDirections = {0, 0, 1, -1};

		return super.getMoves(rowDirections, columnDirections, 8, board, MoveType.TAKE);
	}

	@Override
	public String toString() {
		return super.toString() + (hasMoved ? "" : "*");
	}

}
