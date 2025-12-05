package pt.isec.pa.chess.ui.components;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import pt.isec.pa.chess.model.ChessGameManager;
import pt.isec.pa.chess.model.data.ColorType;
import pt.isec.pa.chess.ui.Constants;
import pt.isec.pa.chess.ui.res.ImageManager;

// TODO : ver se tornamos um footer bonito

public class SideBar extends VBox {

    private final ChessGameManager model;

    private Text whitesName, blacksName;
    private Label current;

    private VBox black, white;
    private HBox blackScore, whiteScore;

    public SideBar (ChessGameManager model){
        super();
        this.model = model;
        this.setAlignment(Pos.CENTER);
        createViews();
        registerHandlers();
        update();
    }

    private void createViews(){
        black = new VBox();
        white = new VBox();

        blackScore = new HBox();
        whiteScore = new HBox();

        current = new Label("");
        blacksName = new Text();
        whitesName = new Text();

        blacksName.setFont(Font.font("Arial",20));

        whitesName.setFont(Font.font("Arial",20));
        current.setFont(Font.font("Arial", FontWeight.BOLD,20));

        black.getChildren().addAll(blacksName, blackScore);
        white.getChildren().addAll(whitesName, whiteScore);
        this.getChildren().addAll(current, white,black);
    }

    private void registerHandlers(){
        this.model.addListener("PLAYER", evt -> update());
    }

    private void update(){
        whitesName.setFill(model.getCurrentPlayer() == ColorType.WHITE ? Constants.Primary_800 : Color.BLACK);
        blacksName.setFill(model.getCurrentPlayer() != ColorType.WHITE ? Constants.Primary_800 : Color.BLACK);
        current.setText(model.getCurrentPlayer() == ColorType.WHITE ? "Current Player: White" : "Current Player: Black");

        blacksName.setText("Blacks: "+model.getBlacksName());
        whitesName.setText("Whites: "+model.getWhitesName());

        blackScore.getChildren().clear();
        for(char p : this.model.getBlackScore()) {
            ImageView img = new ImageView(ImageManager.getPieceIcon(p));
            img.setFitHeight(25);
            img.setPreserveRatio(true);
            blackScore.getChildren().add(img);
        }

        whiteScore.getChildren().clear();
        for(char p : this.model.getWhiteScore()) {
            ImageView img = new ImageView(ImageManager.getPieceIcon(p));
            img.setFitHeight(25);
            img.setPreserveRatio(true);
            whiteScore.getChildren().add(img);
        }
    }
}
