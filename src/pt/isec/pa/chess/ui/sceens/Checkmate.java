package pt.isec.pa.chess.ui.sceens;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import pt.isec.pa.chess.model.ChessGameManager;
import pt.isec.pa.chess.model.data.ColorType;

// TODO : tornar bonito

public class Checkmate extends Stage {
	private final ChessGameManager model;
	private Label winner;

	public Checkmate(ChessGameManager model) {
		this.model = model;

		createViews();
		registerHandlers();
		update();
	}


	private void createViews() {
		this.setWidth(300);
		this.setHeight(500);
		this.setTitle("Checkmate!");

		winner = new Label("");

		this.setScene(new Scene(new Pane(winner)));

	}

	private void registerHandlers() {
		this.model.addListener(ChessGameManager.CHECKMATE,evt -> this.show());
	}

	private void update() {
		this.winner.setText("'" + (this.model.getCurrentPlayer() == ColorType.WHITE ? this.model.getWhitesName() : this.model.getBlacksName()) + "' Won!");
	}

}
