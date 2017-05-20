package chess;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class ChessMenu extends BasicGameState {

	private static Image playNow;
	private static Image exitGame;

	private static int buttonLeft() {
		return ChessGame.WIDTH / 2 - playNow.getWidth() / 2;
	}

	private static int buttonRight() {
		return ChessGame.WIDTH / 2 + playNow.getWidth() / 2;
	}

	private static int exitButtonBottom() {
		return exitButtonTop() - exitGame.getHeight();
	}

	private static int exitButtonTop() {
		return ChessGame.HEIGHT / 2;
	}

	private static int playButtonBottom() {
		return playButtonTop() - playNow.getHeight();
	}

	private static int playButtonTop() {
		return ChessGame.HEIGHT - ChessGame.HEIGHT / 3;
	}

	public ChessMenu(final int state) {
	}

	@Override
	public int getID() {
		return ChessGame.MAIN_MENU;
	}

	@Override
	public void init(final GameContainer gameContainer, final StateBasedGame game) {
		try {
			playNow = new Image("res/play_now.png");
			exitGame = new Image("res/exit_game.png");
		} catch (final SlickException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void render(final GameContainer gameContainer, final StateBasedGame game, final Graphics graphics) {
		graphics.drawString("Welcome to Chess!", ChessGame.WIDTH / 2 - 75, ChessGame.HEIGHT / 4);
		playNow.draw(buttonLeft(), ChessGame.HEIGHT / 3);
		exitGame.draw(buttonLeft(), ChessGame.HEIGHT / 2);
	}

	@Override
	public void update(final GameContainer gameContainer, final StateBasedGame game, final int delta) {
		final Input input = gameContainer.getInput();
		if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
			final int posX = Mouse.getX();
			final int posY = Mouse.getY();
			if (posX > buttonLeft() && posX < buttonRight()) {
				if (posY > playButtonBottom() && posY < playButtonTop()) {
					game.enterState(ChessGame.PLAY);
				} else if (posY > exitButtonBottom() && posY < exitButtonTop()) {
					System.exit(0);
				}
			}
		}

	}

}
