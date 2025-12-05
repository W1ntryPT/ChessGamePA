package pt.isec.pa.chess.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pt.isec.pa.chess.model.ChessGameManager;
import pt.isec.pa.chess.ui.sceens.Checkmate;
import pt.isec.pa.chess.ui.sceens.Evolve;
import pt.isec.pa.chess.ui.sceens.Logger;
import pt.isec.pa.chess.ui.pages.LandingPage;
import pt.isec.pa.chess.ui.res.ImageManager;

public class MainJFX extends Application {
	private ChessGameManager model;

	@Override
	public void init() throws Exception {
		super.init();
		this.model = new ChessGameManager();
	}

	@Override
	public void start(Stage stage) {
		stage.getIcons().add(ImageManager.getPieceIcon('Q'));
		stage.setTitle("Chess");
		stage.setMaximized(true);

		Scene scene = new Scene(new LandingPage(model), 500, 500, Constants.Background_200);
		stage.setScene(scene);

		stage.show();
		Logger logger = new Logger();
		Checkmate checkmate = new Checkmate(this.model);
		Evolve evolve = new Evolve(this.model);

		stage.setOnCloseRequest(evt -> {
			logger.close();
			checkmate.close();
			evolve.close();
		});

	}

	@Override
	public void stop() throws Exception {
		super.stop();
	}
}
