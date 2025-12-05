package pt.isec.pa.chess.model.data.pieces;

import pt.isec.pa.chess.model.data.Board;
import pt.isec.pa.chess.model.data.ColorType;
import pt.isec.pa.chess.model.data.MoveType;
import pt.isec.pa.chess.model.data.Piece;

public class Bishop extends Piece {

	public Bishop(int row, char collunn, ColorType color) {
		super("bishop", color == ColorType.WHITE ? 'B' : 'b', row, collunn, color);
	}


	@Override
	public MoveType move(int row, char column, Board board) {
		MoveType res = getMoves(board)[row-1][getColumnIndex(Character.toUpperCase(column))];

		if(res != MoveType.NONE) {
			setRow(row);
			setColumn(column);
		}

		return res;
	}

	@Override
	public MoveType[][] getMoves(Board board) {
		int[] rowDirections = {1, 1, -1, -1};
		int[] columnDirections = {1, -1, 1, -1};

		return super.getMoves(rowDirections, columnDirections, 8, board, MoveType.TAKE);
	}

}
