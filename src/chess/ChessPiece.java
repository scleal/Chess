package chess;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

class ChessPiece {

	private static String getImageDirectory(final ChessRank rank, final ChessColor color) {
		return "res/" + color.toString().toLowerCase() + "_" + rank.toString().toLowerCase() + ".png";
	}

	private static Image getImageFromDirectory(final ChessRank rank, final ChessColor color) {
		try {
			return new Image(getImageDirectory(rank, color));
		} catch (final SlickException e) {
			e.printStackTrace();
			System.exit(1);
			return null;
		}
	}

	private final ChessColor color;
	private ChessRank rank;

	private Image image;

	private int moveCount = 0;

	// copy constructor has no need for image since it's used only in simulating moves
	ChessPiece(final ChessPiece other) {
		rank = other.rank;
		color = other.color;
		image = null;
		moveCount = other.moveCount;
	}

	ChessPiece(final ChessRank rank, final ChessColor color) {
		this.rank = rank;
		this.color = color;
		image = getImageFromDirectory(rank, color);
		moveCount = 0;
	}

	void draw(final int x, final int y) {
		image.draw(x, y);
	}

	ChessColor getColor() {
		return color;
	}

	int getDelta() {
		return color == ChessColor.WHITE ? 1 : -1;
	}

	Image getImage() {
		return image;
	}

	int getMoveCount() {
		return moveCount;
	}

	ChessRank getRank() {
		return rank;
	}

	int getStartingRow() {
		return color == ChessColor.WHITE ? 1 : 6;
	}

	void incrementMoveCount() {
		moveCount++;
	}

	void setRank(final ChessRank rank) {
		this.rank = rank;
		image = getImageFromDirectory(this.rank, color);
	}

}
