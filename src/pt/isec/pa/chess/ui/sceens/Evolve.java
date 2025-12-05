package pt.isec.pa.chess.ui.sceens;

import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import pt.isec.pa.chess.model.ChessGameManager;
import pt.isec.pa.chess.model.data.ColorType;
import pt.isec.pa.chess.ui.res.ImageManager;

// TODO : deixar bonito e criar func no model para dar evolve à ultima peça movida

public class Evolve extends Stage {

	private final ChessGameManager model;

	private HBox options;
	private ImageView q,r,b,n;

	public Evolve(ChessGameManager model) {
		this.model = model;

		createViews();
		registerHandlers();
		update();
	}


	private void createViews() {
		this.setTitle("Select Piece Type");

		q = new ImageView(ImageManager.getPieceIcon(this.model.getCurrentPlayer() == ColorType.WHITE ? 'Q' : 'q'));
		r = new ImageView(ImageManager.getPieceIcon(this.model.getCurrentPlayer() == ColorType.WHITE ? 'R' : 'r'));
		b = new ImageView(ImageManager.getPieceIcon(this.model.getCurrentPlayer() == ColorType.WHITE ? 'B' : 'b'));
		n = new ImageView(ImageManager.getPieceIcon(this.model.getCurrentPlayer() == ColorType.WHITE ? 'N' : 'n'));

		q.setPreserveRatio(true); q.setFitHeight(50);
		r.setPreserveRatio(true); r.setFitHeight(50);
		b.setPreserveRatio(true); b.setFitHeight(50);
		n.setPreserveRatio(true); n.setFitHeight(50);

		this.options = new HBox();
		options.getChildren().addAll(q,r,b,n);

		this.setWidth(250);
		this.setHeight(100);

		this.setScene(new Scene(options));
	}

	private void registerHandlers() {
		this.model.addListener(ChessGameManager.EVOLVE, evt -> {
			update();
			this.show();
		});

		this.q.setOnMouseClicked(e -> {
			this.model.evolve('Q');
			this.hide();
		});
		this.r.setOnMouseClicked(e -> {
			this.model.evolve('R');
			hide();
		});
		this.b.setOnMouseClicked(e -> {
			this.model.evolve('B');
			this.hide();
		});
		this.n.setOnMouseClicked(e -> {
			this.model.evolve('N');
			this.hide();
		});

	}

	private void update() {

		q.setImage(ImageManager.getPieceIcon(this.model.getCurrentPlayer() != ColorType.WHITE ? 'Q' : 'q'));
		r.setImage(ImageManager.getPieceIcon(this.model.getCurrentPlayer() != ColorType.WHITE ? 'R' : 'r'));
		b.setImage(ImageManager.getPieceIcon(this.model.getCurrentPlayer() != ColorType.WHITE ? 'B' : 'b'));
		n.setImage(ImageManager.getPieceIcon(this.model.getCurrentPlayer() != ColorType.WHITE ? 'N' : 'n'));

	}

}
