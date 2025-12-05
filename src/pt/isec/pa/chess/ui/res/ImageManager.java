package pt.isec.pa.chess.ui.res;

import javafx.scene.image.Image;

import java.io.InputStream;
import java.util.HashMap;

public class ImageManager {
	private ImageManager() { }

	private static final String ROOT = "images/";
	private static final String PIECES = "pieces/";

	private static final HashMap<String, Image> images = new HashMap<>();

	public static Image getImage(String filename) {
		Image image= images.get(filename);
		if(image== null) {
			try(InputStream is = ImageManager.class.getResourceAsStream(ROOT + filename)) {
				if(is == null) return null;

				image = new Image(is);
				images.put(filename, image);

			} catch(Exception e) {
				return null;
			}
		}

		return image;
	}

	public static Image getPieceIcon(char type) {
		char color = Character.isUpperCase(type) ? 'W' : 'B';

		return switch(Character.toUpperCase(type)) {
			case 'K' -> getImage(PIECES + "king" + color + ".png");
			case 'Q' -> getImage(PIECES + "queen" + color + ".png");
			case 'B' -> getImage(PIECES + "bishop" + color + ".png");
			case 'N' -> getImage(PIECES + "knight" + color + ".png");
			case 'R' -> getImage(PIECES + "rook" + color + ".png");
			case 'P' -> getImage(PIECES + "pawn" + color + ".png");
			default -> null;
		};
	}

	public static Image getExternalImage(String filename) {
		Image image= images.get(filename);
		if(image== null) {
			try {
				image = new Image(filename);
				images.put(filename, image);
			} catch(Exception e) {
				return null;
			}
		}
		return image;
	}

	public static void purgeImage(String filename) { images.remove(filename); }

}
