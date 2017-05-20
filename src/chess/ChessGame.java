package chess;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

class ChessGame extends StateBasedGame {

	private static final String Chess = "Chess";
	public static final int MAIN_MENU = 0;
	public static final int PLAY = 1;
	public static final int WIDTH = 480, HEIGHT = 480;

	public static void main(final String[] args) {
		AppGameContainer appgc;
		try {
			appgc = new AppGameContainer(new ChessGame(Chess));
			appgc.setDisplayMode(WIDTH, HEIGHT, false);
			appgc.start();
		} catch (final SlickException e) {
			e.printStackTrace();
		}
	}

	ChessGame(final String name) {
		super(name);
		addState(new ChessMenu(MAIN_MENU));
		addState(new ChessPlay(PLAY));
	}

	@Override
	public void initStatesList(final GameContainer gc) throws SlickException {
		getState(MAIN_MENU).init(gc, this);
		getState(PLAY).init(gc, this);
		this.enterState(MAIN_MENU);
	}

}
