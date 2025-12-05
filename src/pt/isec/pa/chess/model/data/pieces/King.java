package pt.isec.pa.chess.model.data.pieces;

import pt.isec.pa.chess.model.data.Board;
import pt.isec.pa.chess.model.data.ColorType;
import pt.isec.pa.chess.model.data.MoveType;
import pt.isec.pa.chess.model.data.Piece;

public class King extends Piece {

	private boolean hasMoved;

	public King(int row, char column, ColorType color) {
		super("king", color == ColorType.WHITE ? 'K' : 'k', row, column, color);
		this.hasMoved = false;
	}

	public King(int row, char column, ColorType color, boolean hasMoved) {
		super("king", color == ColorType.WHITE ? 'K' : 'k', row, column, color);
		this.hasMoved = hasMoved;
	}


	public boolean hasMoved() { return this.hasMoved; }

	@Override
	public MoveType move(int row, char column, Board board) {
		MoveType res = getMoves(board)[row-1][getColumnIndex(Character.toUpperCase(column))];

		if(res != MoveType.NONE) {
			MoveType[][] opponentMoves = board.getOpponentMoves(this.getColor());
			if(opponentMoves[row-1][getColumnIndex(column)] == MoveType.TAKE)
				return MoveType.NONE;

			// Roque
			if(board.getPiece(row, column) instanceof Rook r && !r.hasMoved() && r.getColor() == getColor()) {
				int index = board.getPieces().indexOf(r);

				if(r.getColumn() == 'H') {
					board.movePiece(r.getRow(), r.getColumn(), row, 'F');
					setColumn('G');
					return MoveType.MOVE;
				} else if(r.getColumn() == 'A') {
					board.movePiece(r.getRow(), r.getColumn(), row, 'B');
					setColumn('C');
					return MoveType.MOVE;
				}
				return MoveType.NONE;
			}

			setRow(row);
			setColumn(column);
			this.hasMoved = true;
		}

		return res;
	}

	@Override
	public MoveType[][] getMoves(Board board) {
		int[] rowDirections = {1, 1, -1, -1, -1, 1, 0, 0};
		int[] columnDirections = {1, -1, 1, -1, 0, 0, 1, -1};

		MoveType[][] moves = super.getMoves(rowDirections, columnDirections, 1, board, MoveType.TAKE);

		/* ---- Roque ---- */
		if(hasMoved) return moves;

		// right
		if(board.getPiece(getRow(), 'H') instanceof Rook r && !r.hasMoved() && r.getColor() == getColor()) {
			int row = r.getRow() - 1, column = Piece.getColumnIndex(r.getColumn());

			if(board.getPiece(getRow(), 'F') != null || board.getPiece(getRow(), 'G') != null) {
				// verify if path is empty
			}
			else if(board.getOpponentMoves(getColor())[row][column-1] == MoveType.TAKE) {
				// verify if move puts the king in a threat
			}
			else
				moves[row][column] = MoveType.MOVE;
		}

		// left
		if(board.getPiece(getRow(), 'A') instanceof Rook r && !r.hasMoved() && r.getColor() == getColor()) {
			int row = r.getRow() - 1, column = Piece.getColumnIndex(r.getColumn());

			if(board.getPiece(getRow(), 'B') != null || board.getPiece(getRow(), 'C') != null || board.getPiece(getRow(), 'D') != null) {
				// verify if path is empty
			}
			else if(board.getOpponentMoves(getColor())[row][column+2] == MoveType.TAKE) {
				// verify if move puts the king in a threat
			}
			else
				moves[row][column] = MoveType.MOVE;
		}

		return moves;
	}

	@Override
	public String toString() {
		return super.toString() + (hasMoved ? "" : "*");
	}

}
