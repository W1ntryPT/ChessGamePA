package pt.isec.pa.chess.model.data.pieces;

import pt.isec.pa.chess.model.data.Board;
import pt.isec.pa.chess.model.data.ColorType;
import pt.isec.pa.chess.model.data.MoveType;
import pt.isec.pa.chess.model.data.Piece;

public class Queen extends Piece {

	public Queen(int row, char collunn, ColorType color) {
		super("queen", color == ColorType.WHITE ? 'Q' : 'q', row, collunn, color);
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
		int[] rowDirections = {1, -1, 0, 0, 1, 1, -1, -1};
		int[] columnDirections = {0, 0, 1, -1, 1, -1, 1, -1};

		return super.getMoves(rowDirections, columnDirections, 8, board, MoveType.TAKE);
	}

}