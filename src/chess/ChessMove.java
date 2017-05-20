package chess;

class ChessMove {

	private static boolean getCondition(final int i, final int start, final int end) {
		return end < start ? i > end : i < end;
	}

	final int startX, startY, endX, endY;

	ChessMove(final int startX, final int startY, final int endX, final int endY) {
		this.startX = startX;
		this.startY = startY;
		this.endX = endX;
		this.endY = endY;
	}

	private boolean getConditionBishop(final int i, final int j) {
		if (endX > startX && endY > startY) {
			return i < endX && j < endY;
		}
		if (endX < startX && endY > startY) {
			return i > endX && j < endY;
		}
		if (endX < startX && endY < startY) {
			return i > endX && j > endY;
		}
		return i < endX && j > endY;
	}

	private boolean isValidMoveBishop(final ChessBoard board) {
		int i = -1, j = -1;
		int deltaX = 1, deltaY = 1;
		if (endX < startX && endY > startY) {
			deltaX = -1;
		} else if (endX > startX && endY < startY) {
			deltaY = -1;
		} else if (endX < startX && endY < startY) {
			deltaX = -1;
			deltaY = -1;
		}
		for (i = startX + deltaX, j = startY + deltaY; getConditionBishop(i, j); i += deltaX, j += deltaY) {
			if (board.getPiece(i, j) != null) {
				return false;
			}
		}
		return i == endX && j == endY;
	}

	private boolean isValidMoveKing() {
		return Math.abs(startX - endX) <= 1 && Math.abs(startY - endY) <= 1;
	}

	private boolean isValidMoveKnight() {
		return Math.abs(endX - startX) == 1 && Math.abs(endY - startY) == 2
			|| Math.abs(endY - startY) == 1 && Math.abs(endX - startX) == 2;
	}

	private boolean isValidMovePawn(final ChessBoard board) {
		final int delta = board.getPiece(startX, startY).getDelta();
		final int startingRow = board.getPiece(startX, startY).getStartingRow();
		final ChessPiece occupyingPiece = board.getPiece(endX, endY);
		if (occupyingPiece != null) {
			return Math.abs(startX - endX) == 1 && startY + delta == endY;
		} else if (endX == startX) {
			if (startingRow == startY) {
				return endY == startingRow + delta
					|| board.getPiece(endX, startingRow + delta) == null && endY == startingRow + delta * 2;
			} else {
				return endY == startY + delta;
			}
		}
		return false;
	}

	private boolean isValidMoveRook(final ChessBoard board) {
		if (endX == startX) {
			int deltaY = 1;
			if (startY > endY) {
				deltaY = -1;
			}
			for (int i = startY + deltaY; getCondition(i, startY, endY); i += deltaY) {
				if (board.getPiece(startX, i) != null) {
					return false;
				}
			}
			return true;
		} else if (endY == startY) {
			int deltaX = 1;
			if (startX > endX) {
				deltaX = -1;
			}
			for (int i = startX + deltaX; getCondition(i, startX, endX); i += deltaX) {
				if (board.getPiece(i, startY) != null) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	boolean isValidCastleMove(final ChessBoard board) {
		final ChessPiece king = board.getPiece(startX, startY);
		if (king.getMoveCount() != 0) {
			return false;
		}
		if (Math.abs(startX - endX) != 2 || startY != endY) {
			return false;
		}
		int x = 7;
		int delta = 1;
		if (startX > endX) {
			x = 0;
			delta = -1;
		}
		final ChessPiece rook = board.getPiece(x, startY);
		if (rook != null && rook.getMoveCount() != 0) {
			return false;
		}
		final ChessColor opposingColor = ChessColor.switchColor(king.getColor());
		for (int i = startX; getCondition(i, startX, x); i += delta) {
			if (board.getPiece(i, startY) != null && i != startX || board.underAttack(opposingColor, i, startY)) {
				return false;
			}
		}
		return true;
	}

	boolean isValidEnPassantMove(final ChessBoard board, final int delta, final int startingRow) {
		if (startY != startingRow + delta * 3 || Math.abs(startX - endX) != 1 || startY + delta != endY) {
			return false;
		}
		if (board.getPiece(endX, endY) != null) {
			return false;
		}
		final ChessPiece potentialPawn = board.getPiece(endX, startY);
		return potentialPawn != null && potentialPawn.getRank() == ChessRank.PAWN && potentialPawn.getMoveCount() == 1;
	}

	boolean isValidMove(final ChessBoard board) {
		final ChessPiece startPiece = board.getPiece(startX, startY);
		final ChessPiece occupyingPiece = board.getPiece(endX, endY);
		if (occupyingPiece != null && occupyingPiece.getColor() == startPiece.getColor()) {
			return false;
		}
		switch (startPiece.getRank()) {
		case PAWN:
			return isValidMovePawn(board)
				|| isValidEnPassantMove(board, startPiece.getDelta(), startPiece.getStartingRow());
		case ROOK:
			return isValidMoveRook(board);
		case KNIGHT:
			return isValidMoveKnight();
		case BISHOP:
			return isValidMoveBishop(board);
		case QUEEN:
			return isValidMoveBishop(board) || isValidMoveRook(board);
		case KING:
			return isValidCastleMove(board) || isValidMoveKing();
		}
		return false;
	}

	boolean putsInCheck(final ChessBoard board, final ChessPiece piece) {
		final ChessBoard boardCopy = new ChessBoard(board);
		final ChessPiece pieceCopy = new ChessPiece(piece);
		return boardCopy.doMove(pieceCopy, this).inCheck(pieceCopy.getColor());
	}

}
