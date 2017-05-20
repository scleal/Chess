package chess;

class ChessBoard {

	private final ChessPiece[][] chessBoard;

	ChessBoard() {
		chessBoard = new ChessPiece[8][8];
	}

	// Deep Copy Constructor
	ChessBoard(final ChessBoard other) {
		chessBoard = new ChessPiece[8][8];
		for (int i = 0; i < 8; i++) {
			chessBoard[i] = other.chessBoard[i].clone();
		}
	}

	private void clearCell(final int x, final int y) {
		chessBoard[x][y] = null;
	}

	// hack to return two ints by using a ChessMove object
	private ChessMove getKingPosition(final ChessColor currentPlayer) {
		int kingX = -1, kingY = -1;
		done:
		if (kingX == -1) {
			for (int i = 0; i < 8; i++) {
				for (int j = 0; j < 8; j++) {
					if (getPiece(i, j) != null && getPiece(i, j).getRank() == ChessRank.KING
						&& getPiece(i, j).getColor() == currentPlayer) {
						kingX = i;
						kingY = j;
						break done;
					}
				}
			}
		}
		return new ChessMove(kingX, kingY, -1, -1);
	}

	private void movePiece(final ChessPiece selectedPiece, final int x, final int y) {
		chessBoard[x][y] = selectedPiece;
	}

	ChessBoard doMove(final ChessPiece startPiece, final ChessMove move) {
		final ChessRank rank = startPiece.getRank();
		if (rank == ChessRank.KING && move.isValidCastleMove(this)) {
			int x = 7;
			int deltaX = 1;
			if (move.endX == move.startX - 2) {
				x = 0;
				deltaX = -1;
			}
			movePiece(getPiece(x, move.startY), move.startX + deltaX, move.startY);
			clearCell(x, move.startY);
		} else if (rank == ChessRank.PAWN
			&& move.isValidEnPassantMove(this, startPiece.getDelta(), startPiece.getStartingRow())) {
			clearCell(move.endX, move.startY);
		}
		startPiece.incrementMoveCount();
		movePiece(startPiece, move.endX, move.endY);
		clearCell(move.startX, move.startY);
		return this;
	}

	void draw() {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (chessBoard[i][j] != null) {
					chessBoard[i][j].draw(i * 60, ChessGame.HEIGHT - (j + 1) * 60);
				}
			}
		}
	}

	ChessPiece getPiece(final int x, final int y) {
		if (x < 0 || x > 7 || y < 0 || y > 7) {
			return null;
		}
		return chessBoard[x][y];
	}

	boolean inCheck(final ChessColor currentPlayer) {
		final ChessMove kingPosition = getKingPosition(currentPlayer);
		final ChessColor opposingPlayer = ChessColor.switchColor(currentPlayer);
		return underAttack(opposingPlayer, kingPosition.startX, kingPosition.startY);
	}

	void initializeBoard() {
		for (int i = 0; i < 8; i++) {
			chessBoard[i][1] = new ChessPiece(ChessRank.PAWN, ChessColor.WHITE);
			chessBoard[i][6] = new ChessPiece(ChessRank.PAWN, ChessColor.BLACK);
		}
		chessBoard[0][0] = new ChessPiece(ChessRank.ROOK, ChessColor.WHITE);
		chessBoard[1][0] = new ChessPiece(ChessRank.KNIGHT, ChessColor.WHITE);
		chessBoard[2][0] = new ChessPiece(ChessRank.BISHOP, ChessColor.WHITE);
		chessBoard[3][0] = new ChessPiece(ChessRank.QUEEN, ChessColor.WHITE);
		chessBoard[4][0] = new ChessPiece(ChessRank.KING, ChessColor.WHITE);
		chessBoard[5][0] = new ChessPiece(ChessRank.BISHOP, ChessColor.WHITE);
		chessBoard[6][0] = new ChessPiece(ChessRank.KNIGHT, ChessColor.WHITE);
		chessBoard[7][0] = new ChessPiece(ChessRank.ROOK, ChessColor.WHITE);

		chessBoard[0][7] = new ChessPiece(ChessRank.ROOK, ChessColor.BLACK);
		chessBoard[1][7] = new ChessPiece(ChessRank.KNIGHT, ChessColor.BLACK);
		chessBoard[2][7] = new ChessPiece(ChessRank.BISHOP, ChessColor.BLACK);
		chessBoard[3][7] = new ChessPiece(ChessRank.QUEEN, ChessColor.BLACK);
		chessBoard[4][7] = new ChessPiece(ChessRank.KING, ChessColor.BLACK);
		chessBoard[5][7] = new ChessPiece(ChessRank.BISHOP, ChessColor.BLACK);
		chessBoard[6][7] = new ChessPiece(ChessRank.KNIGHT, ChessColor.BLACK);
		chessBoard[7][7] = new ChessPiece(ChessRank.ROOK, ChessColor.BLACK);
	}

	boolean noValidMoves(final ChessColor currentPlayer) {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (getPiece(i, j) != null && getPiece(i, j).getColor() == currentPlayer) {
					for (int k = 0; k < 8; k++) {
						for (int l = 0; l < 8; l++) {
							final ChessMove move = new ChessMove(i, j, k, l);
							if (move.isValidMove(this) && !move.putsInCheck(this, getPiece(i, j))) {
								return false;
							}
						}
					}
				}
			}
		}
		return true;
	}

	boolean underAttack(final ChessColor opposingPlayer, final int x, final int y) {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (getPiece(i, j) != null && getPiece(i, j).getColor() == opposingPlayer) {
					final ChessMove move = new ChessMove(i, j, x, y);
					if (move.isValidMove(this)) {
						return true;
					}
				}
			}
		}
		return false;
	}

}
