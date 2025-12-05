package pt.isec.pa.chess.model.data.pieces;

import pt.isec.pa.chess.model.data.Board;
import pt.isec.pa.chess.model.data.ColorType;
import pt.isec.pa.chess.model.data.MoveType;
import pt.isec.pa.chess.model.data.Piece;

public class Knight extends Piece {

	public Knight(int row, char collunn, ColorType color) {
		super("knight", color == ColorType.WHITE ? 'N' : 'n', row, collunn, color);
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
		int[] rowDirections = {2,1,-1,-2,-2,-1,1,2};
		int[] columnDirections = {1,2,2,1,-1,-2,-2,-1};

		return super.getMoves(rowDirections, columnDirections, 1, board, MoveType.TAKE);
	}

}
