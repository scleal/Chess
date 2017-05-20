package chess;

enum ChessColor {
	BLACK, WHITE;

	static ChessColor switchColor(final ChessColor color) {
		return color == WHITE ? BLACK : WHITE;
	}
}
