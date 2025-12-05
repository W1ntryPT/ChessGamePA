package pt.isec.pa.chess.ui.components;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.util.Pair;
import pt.isec.pa.chess.model.ChessGameManager;
import pt.isec.pa.chess.ui.Constants;
import pt.isec.pa.chess.ui.UiData;
import pt.isec.pa.chess.ui.res.SoundManager;

import javax.net.ssl.SNIHostName;
import java.io.File;
import java.util.Optional;

public class Header extends MenuBar {
	private final ChessGameManager model;

	Menu game;
	MenuItem newGame, openGame, saveGame, importGame, exportGame, quitGame;
	Menu mode;
	RadioMenuItem normal, learning , showMoves;

	MenuItem undo, redo;
	ToggleGroup modeToogle;

	Menu settings;

	ToggleGroup sound,language;

	RadioMenuItem soundOn, soundOff, English, Portuguese;
	String whites, blacks;


	public Header(ChessGameManager model) {
		this.model = model;

		createViews();
		registerHandlers();
		update();
	}


	private void createViews() {

		this.setBackground(Background.fill(Constants.Primary_500));

		game = new Menu("Game");
			newGame = new MenuItem("New");
			openGame = new MenuItem("Open");
			saveGame = new MenuItem("Save");
			importGame = new MenuItem("Import");
			exportGame = new MenuItem("Export");
			quitGame = new MenuItem("Quit");
		game.getItems().addAll(newGame, openGame, saveGame, importGame, exportGame, quitGame);

		settings = new Menu("Settings");
			soundOn = new RadioMenuItem("On");
			soundOff = new RadioMenuItem("Off");
			English = new RadioMenuItem("English");
			Portuguese = new RadioMenuItem("Portuguese");
		settings.getItems().addAll(soundOn,soundOff,English,Portuguese);

		mode = new Menu("Mode");
			normal = new RadioMenuItem ("Normal");
			learning = new RadioMenuItem ("Learning");
			undo = new MenuItem("Undo");
			redo = new MenuItem("Redo");
			showMoves = new RadioMenuItem("Show Moves");
		mode.getItems().addAll(normal, learning, undo, redo, showMoves);

		sound = new ToggleGroup();
		sound.getToggles().addAll(soundOn,soundOff);
		language = new ToggleGroup();
		language.getToggles().addAll(English, Portuguese);
		modeToogle = new ToggleGroup();
		modeToogle.getToggles().addAll(normal,learning);

		getMenus().addAll(game, mode,settings);
	}

	private void registerHandlers() {
		newGame.setOnAction(actionEvent -> {
			getPlayerNames();
			this.model.newGame(whites,blacks);
		});

		openGame.setOnAction(actionEvent -> {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Open Game");
			fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Game Files (*.ser)", "*.ser"));

			File file = fileChooser.showOpenDialog(getScene().getWindow());
			if(file == null || file.isDirectory() || !file.canRead())
				return;

			model.open(file);
		});

		saveGame.setOnAction(actionEvent -> {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Save Game");
			fileChooser.setInitialFileName("ChessGame.ser");
			fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Game Files (*.ser)", "*.ser"));

			File file = fileChooser.showSaveDialog(getScene().getWindow());
			if(file == null || !file.getName().endsWith(".ser"))
				return;

			model.save(file);
		});

		importGame.setOnAction(actionEvent -> {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Import Game");
			fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files (*.txt)", "*.txt"));

			File file = fileChooser.showOpenDialog(getScene().getWindow());
			if(file == null || file.isDirectory() || !file.canRead())
				return;

			getPlayerNames();
			model.importGame(file, whites, blacks);
		});

		exportGame.setOnAction(actionEvent -> {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Export Game");
			fileChooser.setInitialFileName("ChessGame.txt");
			fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files (*.txt)", "*.txt"));

			File file = fileChooser.showSaveDialog(getScene().getWindow());
			if(file == null || !file.getName().endsWith(".txt"))
				return;

			model.exportGame(file);
		});
		quitGame.setOnAction(actionEvent -> Platform.exit());

		normal.setOnAction(actionEvent -> {
			UiData.getInstance().setLearningMode(false);
		});
		learning.setOnAction(actionEvent -> {
			UiData.getInstance().setLearningMode(true);
		});

		UiData.getInstance().addListener(UiData.SETTING_CHANGE, evt -> update());


		showMoves.setOnAction(actionEvent -> {
			UiData.getInstance().setShowMoves(!UiData.getInstance().isShowMoves());
		});

		UiData.getInstance().addListener(UiData.SETTING_CHANGE, evt -> update());

		soundOn.setOnAction(actionEvent -> {
			UiData.getInstance().setSound(true);
		});

		soundOff.setOnAction(actionEvent -> {
			UiData.getInstance().setSound(false);
		});

		English.setOnAction(actionEvent -> {
			UiData.getInstance().setLanguage(SoundManager.EN);
		});

		Portuguese.setOnAction(actionEvent -> {
			UiData.getInstance().setLanguage(SoundManager.PT);
		});

		undo.setOnAction(actionEvent -> {
			this.model.undo();
		});

		redo.setOnAction(actionEvent -> {
			this.model.redo();
		});
	}

	private void update() {
		learning.setSelected(UiData.getInstance().learningMode());
		normal.setSelected(!UiData.getInstance().learningMode());
		undo.setDisable(!UiData.getInstance().learningMode());
		redo.setDisable(!UiData.getInstance().learningMode());
		showMoves.setDisable(!UiData.getInstance().learningMode());
		soundOff.setSelected(!UiData.getInstance().isSoundOn());
		soundOn.setSelected(UiData.getInstance().isSoundOn());
		English.setSelected(UiData.getInstance().getLanguage().equals(SoundManager.EN));
		Portuguese.setSelected(UiData.getInstance().getLanguage().equals(SoundManager.PT));
	}


	private void getPlayerNames() {
		Dialog<Pair<String, String>> dialog = new Dialog<>();
		dialog.setTitle("New Game");
		dialog.setHeaderText("Enter the names of the players:");

		ButtonType startButtonType = new ButtonType("Start", ButtonBar.ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(startButtonType, ButtonType.CANCEL);

		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));

		TextField whiteField = new TextField();
		whiteField.setPromptText(this.model.getWhitesName());

		TextField blackField = new TextField();
		blackField.setPromptText(this.model.getBlacksName());

		grid.add(new Label("White:"), 0, 0);
		grid.add(whiteField, 1, 0);
		grid.add(new Label("Black:"), 0, 1);
		grid.add(blackField, 1, 1);

		dialog.getDialogPane().setContent(grid);

		dialog.setResultConverter(dialogButton -> {
			if (dialogButton == startButtonType) {
				return new Pair<>(whiteField.getText(), blackField.getText());
			}
			return null;
		});

		Optional<Pair<String, String>> result = dialog.showAndWait();

		result.ifPresent(names -> {
			whites = names.getKey();
			blacks = names.getValue();
		});
	}

}
