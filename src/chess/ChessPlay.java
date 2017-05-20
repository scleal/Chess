package chess;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

class ChessPlay extends BasicGameState {

	private static ChessPiece startPiece;
	private static int startX, startY;
	private static ChessColor currentPlayer;
	private static ChessBoard board;
	private static boolean promotion = false;
	private static final int CELL_SIZE = 60;

	private static void drawPossibleMoves(final Graphics graphics) {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				final ChessMove move = new ChessMove(startX, startY, i, j);
				if (move.isValidMove(board) && !move.putsInCheck(board, startPiece)) {
					graphics.setColor(Color.blue);
					graphics.drawRect(i * CELL_SIZE, ChessGame.HEIGHT - (j + 1) * CELL_SIZE, CELL_SIZE, CELL_SIZE);
				}
			}
		}
	}

	private static void initializeGraphics(final Graphics graphics) {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (i % 2 == j % 2) {
					graphics.setColor(Color.white);
					graphics.fillRect(i * CELL_SIZE, j * CELL_SIZE, CELL_SIZE, CELL_SIZE);
				} else {
					graphics.setColor(Color.gray);
					graphics.fillRect(i * CELL_SIZE, j * CELL_SIZE, CELL_SIZE, CELL_SIZE);
				}
			}
		}
	}

	private static boolean isValidSelection(final ChessPiece chessPiece) {
		return chessPiece != null && chessPiece.getColor() == currentPlayer;
	}

	private static void makeSelection(final int posX, final int posY) {
		startPiece = board.getPiece(posX, posY);
		startX = posX;
		startY = posY;
	}

	private static void switchTurns() {
		if (!promotion) {
			startPiece = null;
			currentPlayer = ChessColor.switchColor(currentPlayer);
		}
	}

	ChessPlay(final int state) {
	}

	@Override
	public int getID() {
		return ChessGame.PLAY;
	}

	@Override
	public void init(final GameContainer gameContainer, final StateBasedGame game) {
		board = new ChessBoard();
		board.initializeBoard();
		currentPlayer = ChessColor.WHITE;
	}

	@Override
	public void render(final GameContainer gameContainer, final StateBasedGame game, final Graphics graphics) {
		initializeGraphics(graphics);
		board.draw();
		if (!promotion && startPiece != null) {
			graphics.setColor(Color.red);
			graphics.drawRect(startX * CELL_SIZE, ChessGame.HEIGHT - (startY + 1) * CELL_SIZE, CELL_SIZE, CELL_SIZE);
			drawPossibleMoves(graphics);
		}
		final boolean noValidMoves = board.noValidMoves(currentPlayer);
		final boolean inCheck = board.inCheck(currentPlayer);
		graphics.setColor(Color.red);
		if (noValidMoves && inCheck) {
			graphics.drawString(currentPlayer + " IN CHECKMATE!", 200, 200);
			graphics.drawString(currentPlayer == ChessColor.WHITE ? "BLACK WINS!" : "WHITE WINS!", 200, 220);
		} else if (noValidMoves) {
			graphics.drawString("STALEMATE", 200, 200);
		} else if (inCheck) {
			graphics.drawString(currentPlayer + " IN CHECK!", 200, 200);
		} else if (promotion) {
			graphics.drawString("Queen  (Q)", 200, 200);
			graphics.drawString("Rook   (R)", 200, 220);
			graphics.drawString("Bishop (B)", 200, 240);
			graphics.drawString("Knight (K)", 200, 260);
		}
	}

	@Override
	public void update(final GameContainer gameContainer, final StateBasedGame game, final int delta) {
		final Input input = gameContainer.getInput();
		if (promotion) {
			if (input.isKeyPressed(Input.KEY_Q)) {
				startPiece.setRank(ChessRank.QUEEN);
				promotion = false;
			} else if (input.isKeyPressed(Input.KEY_R)) {
				startPiece.setRank(ChessRank.ROOK);
				promotion = false;
			} else if (input.isKeyPressed(Input.KEY_B)) {
				startPiece.setRank(ChessRank.BISHOP);
				promotion = false;
			} else if (input.isKeyPressed(Input.KEY_K)) {
				startPiece.setRank(ChessRank.KNIGHT);
				promotion = false;
			}
			switchTurns();
		} else if (!promotion && input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
			final int posX = Mouse.getX() / CELL_SIZE;
			final int posY = Mouse.getY() / CELL_SIZE;
			final ChessMove move = new ChessMove(startX, startY, posX, posY);
			if (isValidSelection(startPiece) && move.isValidMove(board) && !move.putsInCheck(board, startPiece)) {
				board.doMove(startPiece, move);
				if (startPiece.getRank() == ChessRank.PAWN && move.endY % 7 == 0) {
					promotion = true;
				}
				switchTurns();
			} else if (isValidSelection(board.getPiece(posX, posY))) {
				makeSelection(posX, posY);
			}
		}

	}

}