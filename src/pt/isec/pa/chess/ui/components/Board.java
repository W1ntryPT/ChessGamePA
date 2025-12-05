package pt.isec.pa.chess.ui.components;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import pt.isec.pa.chess.model.ChessGameManager;
import pt.isec.pa.chess.model.data.ColorType;
import pt.isec.pa.chess.model.data.MoveType;
import pt.isec.pa.chess.ui.Constants;
import pt.isec.pa.chess.ui.UiData;
import pt.isec.pa.chess.ui.res.ImageManager;
import pt.isec.pa.chess.ui.res.SoundManager;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;

import static java.lang.Thread.sleep;

public class Board extends Canvas {

	private final ChessGameManager model;
	private double squareSize = 0, offsetX = 0, offsetY = 0;
	private boolean firstClick;

	private boolean[][] moves;
	private int firstColumn, firstRow;
	private int secondColumn, secondRow;


    public Board(ChessGameManager model) {
		super();
		this.model = model;

		this.firstClick = true;
		this.firstRow = this.firstColumn = this.secondColumn = this.secondRow = -1;
		moves = new boolean[model.getRowSize()][model.getColumnSize()];

		createViews();
		registerHandlers();
		update();
	}


	private void createViews() {
	}

	private void registerHandlers() {

		this.model.addListener(ChessGameManager.GAME_CHANGE,evt -> update());

		this.widthProperty().addListener((obs, oldVal, newVal) -> update());

		this.heightProperty().addListener((obs, oldVal, newVal) -> update());

		this.setOnMouseClicked(event -> {
			if(firstClick){
				secondColumn = secondRow = -1;

				firstColumn = (int) ((event.getX() - offsetX)/squareSize); ;
				firstRow = (int) (model.getRowSize() - (event.getY() - offsetY)/squareSize + 1);

				String name = this.model.getPieceName(firstRow, (char) ('A'+firstColumn - 1));
				char p = model.getPiece(firstRow, (char) ('A'+firstColumn - 1));

				// if the click wast on a Piece or a Piece that doesn't belong to the Current Player
				firstClick = p == ' ' ||
						(Character.isUpperCase(p) && model.getCurrentPlayer() != ColorType.WHITE) ||
						(Character.isLowerCase(p) && model.getCurrentPlayer() != ColorType.BLACK);


				if(firstColumn <= 0 || firstColumn > model.getColumnSize() || firstRow <= 0 || firstRow > model.getRowSize())
					firstColumn = firstRow = -1;

				if(firstClick) {
					if(UiData.getInstance().isSoundOn()  && firstColumn != -1 && firstRow != -1){
						SoundManager.play( "empty",UiData.getInstance().getLanguage());
					}
					firstRow = firstColumn = -1;

				}
				else if(UiData.getInstance().isSoundOn() && firstColumn != -1 && firstRow != -1)
					SoundManager.playSequence(UiData.getInstance().getLanguage(), Character.isUpperCase(p) ? "white" : "black",name,String.valueOf((char) ('A' + firstColumn - 1)),String.valueOf(firstRow));

				if(UiData.getInstance().isShowMoves()){
					moves = model.getPieceMoves(firstRow,(char) ('A'+firstColumn - 1));
				}

				update();
			}else{
				moves = new boolean[model.getRowSize()][model.getColumnSize()];
				secondColumn = (int) ((event.getX() - offsetX)/squareSize);
				secondRow = (int) (model.getRowSize() - (event.getY() - offsetY)/squareSize + 1);
				firstClick = true;
				MoveType res = this.model.movePiece(firstRow, (char) ('A'+firstColumn-1), secondRow, (char) ('A'+secondColumn - 1));
				if(res == MoveType.NONE) {
					firstRow = firstColumn = secondColumn = secondRow = -1;
					if (UiData.getInstance().isSoundOn())
						SoundManager.play( "empty",UiData.getInstance().getLanguage());
					update();
				} else if(UiData.getInstance().isSoundOn()) {
					SoundManager.playSequence(UiData.getInstance().getLanguage(),String.valueOf((char) ('A' + secondColumn - 1)), String.valueOf(secondRow));
				}
			}
		});
	}

	private void update() {

		GraphicsContext gc = this.getGraphicsContext2D();
		gc.clearRect(0, 0, getWidth(), getHeight());

		int rows = model.getRowSize() + 1;
		int cols = model.getColumnSize() + 1;

		squareSize = Math.min(getWidth() / cols, getHeight() / rows);
		offsetX = (getWidth() - (squareSize * cols)) / 2;
		offsetY = (getHeight() - (squareSize * rows)) / 2;

		for (int r = rows-1; r >= 0; r--) {
			for (int c = 0; c < cols; c++) {
				if((c == 0) && (r == 0))
					continue;;

				if (c == 0) {
					String text = String.valueOf(r);
					double x = offsetX + squareSize * 0.9;
					double y = offsetY + (rows - r) * squareSize - squareSize * 0.42;
					gc.setFill(Color.BLACK);
					gc.fillText(text, x, y);
					gc.setTextAlign(TextAlignment.CENTER);
				}

				else if (r == 0){

					String text = String.valueOf((char) ('A' + c - 1));
					double x = offsetX + c * squareSize + squareSize * 0.5;
					double y = offsetY + rows * squareSize - squareSize * 0.8;
					gc.setFill(Color.BLACK);
					gc.setTextAlign(TextAlignment.CENTER);
					gc.fillText(text, x, y);
				}
				else {
					Image piece = ImageManager.getPieceIcon(model.getPiece(r, (char) ('A'+c - 1)));
					boolean isWhite = (r + c) % 2 != 0;

					if(r == firstRow && c == firstColumn) {
						gc.setFill(Constants.Selected_Highlight);
					}else if(r==secondRow && c==secondColumn){
						gc.setFill(Constants.Selected_Highlight);
					}else if(moves[r -1][c - 1]){
						gc.setFill(piece != null ? Constants.Take_Highlight : Constants.Selected_Highlight);
					}else {
						gc.setFill(isWhite ? Constants.Cell_White : Constants.Cell_Black);
					}

					gc.fillRect(offsetX + c * squareSize, offsetY + (rows - r -1) * squareSize, squareSize, squareSize);
					if(piece != null){
						gc.drawImage(piece,offsetX + c * squareSize, offsetY + (rows - r -1) * squareSize, squareSize, squareSize);
					}
				}
			}
		}
	}
}
