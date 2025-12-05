package pt.isec.pa.chess.ui.sceens;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import pt.isec.pa.chess.model.ModelLog;

import java.util.List;

public class Logger extends Stage {

	private final ModelLog modelLog;
	private ListView<Label> list;
	private Button reset;

	public Logger() {
		super();

		modelLog = ModelLog.getInstance();

		createViews();
		registerHandlers();
		update();
	}

	public void createViews() {
		this.setWidth(300);
		this.setHeight(500);
		this.setTitle("Chess Logger");

		BorderPane main = new BorderPane();
		main.setPrefWidth(this.getWidth());
		main.setPrefHeight(this.getHeight());

		list = new ListView<>();
		reset = new Button("Reset");

		main.setBottom(reset);
		main.setCenter(list);

		this.setScene(new Scene(main));
		this.show();
	}

	public void registerHandlers() {
		this.modelLog.addListener(ModelLog.LOG,evt -> update());

		this.reset.setOnAction(actionEvent -> this.modelLog.reset());
	}

	public void update() {
		List<String> logs = modelLog.getLog();

		for(String l : logs) {
			Label label = new Label(l);
			list.getItems().add(label);
		}

	}

}
