package pt.isec.pa.chess.ui.pages;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import pt.isec.pa.chess.model.ChessGameManager;
import pt.isec.pa.chess.ui.Constants;
import pt.isec.pa.chess.ui.components.Board;
import pt.isec.pa.chess.ui.components.Header;
import pt.isec.pa.chess.ui.components.SideBar;

import javax.swing.text.html.HTMLDocument;


public class LandingPage extends BorderPane {
	private final ChessGameManager model;

	private MenuBar header;
	private Board board;
	VBox left,right;
	HBox bottom;
	Pane center;

	SideBar sideBar;
	StackPane boardWrapper;

	public LandingPage(ChessGameManager model) {
		this.model = model;
		this.setBackground(Background.fill(Constants.Background_200));

		createViews();
		registerHandlers();
		update();
	}

	// TODO: usar apenas a border pane (left e bottom labels, right sidebar, center board)
	private void createViews() {
		header = new Header(model);
		board = new Board(model);
		sideBar = new SideBar(model);
		center = new Pane();
		center.getChildren().add(board);

		left = new VBox();
		bottom = new HBox();
		right = new VBox();
		/*boardWrapper = new StackPane(board);

		left.setAlignment(Pos.CENTER);
		bottom.setAlignment(Pos.CENTER);

		GridPane grid = new GridPane();
		grid.add(left, 0, 0);
		grid.add(boardWrapper, 1, 0);

		ColumnConstraints col1 = new ColumnConstraints(30);
		ColumnConstraints col2 = new ColumnConstraints();
		col2.setHgrow(Priority.ALWAYS);
		ColumnConstraints col3 = new ColumnConstraints(30);
		grid.getColumnConstraints().addAll(col1, col2, col3);

		RowConstraints row = new RowConstraints();
		row.setVgrow(Priority.ALWAYS);
		grid.getRowConstraints().add(row);
		grid.setBackground(Background.fill(Color.SALMON));*/


		sideBar.setPadding(new Insets(20));
		sideBar.setSpacing(20);
		BackgroundFill background = new BackgroundFill(Constants.Background_500,new CornerRadii(15),Insets.EMPTY);
		sideBar.setBackground(new Background(background));
		right.getChildren().add(sideBar);
		right.setAlignment(Pos.CENTER);

		this.setTop(header);
		this.setCenter(center);
		this.setBottom(bottom);
		this.setRight(right);
	}


	private void registerHandlers() {
		center.widthProperty().addListener((obs, oldVal, newVal) -> {
			board.setWidth(center.getWidth());
			board.setHeight(center.getHeight());
			update();
		});

		center.heightProperty().addListener((obs, oldVal, newVal) -> {
			board.setWidth(center.getWidth());
			board.setHeight(center.getHeight());
			update();
		});

	}

	private void update() {

		/*left.getChildren().clear();
		bottom.getChildren().clear();

		int rows = model.getRowSize();
		int cols = model.getColumnSize();

		double availableWidth = boardWrapper.getWidth();
		double availableHeight = boardWrapper.getHeight();

		double squareSize = Math.min(availableWidth / cols, availableHeight / rows);
		double boardHeight = squareSize * rows;

		// Row labels (1-8)
		for (int r = 0; r < rows; r++) {
			Label label = new Label(String.valueOf(rows - r));
			label.setPrefHeight(squareSize);
			label.setMinHeight(squareSize);
			label.setMaxHeight(squareSize);
			label.setPrefWidth(30);
			label.setAlignment(Pos.CENTER);
			left.getChildren().add(label);
		}
		left.setTranslateX((availableWidth - boardHeight) / 2);

		// Column labels (A-H)
		for (int c = 0; c < cols; c++) {
			Label label = new Label(String.valueOf((char) ('A' + c)));
			label.setPrefWidth(squareSize);
			label.setMinWidth(squareSize);
			label.setMaxWidth(squareSize);
			label.setPrefHeight(35);
			label.setAlignment(Pos.CENTER);
			bottom.getChildren().add(label);
		}*/
	}

}
