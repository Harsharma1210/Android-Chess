package com.example.chessgame.app;

import com.example.chessgame.pieces.*;

import java.util.Random;

public class Chess {

	public Board b;
	public Board prevB;
	public boolean drawOffered;
	public boolean isWinner;
    public boolean isWhiteTurn;
    public boolean check;
	public int[] wKPosf;
	public int[] bKPosf;
	int[] prevwK;
	int[] prevbK;
	public boolean canUndo;

	public Chess() {
	    drawOffered = false;
	    isWinner = false;
	    isWhiteTurn = true;
	    wKPosf = new int[] {4,7};
	    bKPosf = new int[] {4,0};
		b = new Board();
	}

	public Chess(boolean empty) {
		drawOffered = false;
	    isWinner = false;
	    isWhiteTurn = false;
	    wKPosf = new int[] {4,7};
	    bKPosf = new int[] {4,0};
		b = new Board();
	}

	public boolean executeMove(String move) {
		if (tryMove(move, isWhiteTurn)) {
			isWhiteTurn = !isWhiteTurn;
			if (isCheck(b, wKPosf, bKPosf)[0]) {
				boolean checkMate = false;
				if (isWhiteTurn) checkMate = isCheckMate('w');
				else checkMate = isCheckMate('b');
				if (checkMate) {
					isWhiteTurn = !isWhiteTurn;
					isWinner = true;
				} else {
					check = true;
				}
			} else check = false;
		} else {
			return false;
		}
		return true;
	}

	public boolean tryMove(String move, boolean isWhiteTurn) {
		prevB = getCopy(b);
		prevwK = new int[]{wKPosf[0], wKPosf[1]};
		prevbK = new int[]{bKPosf[0], bKPosf[1]};

		int[] positions = getPositions(move);
		String arg = "";
		int oldX = positions[0];
		int oldY = positions[1];
		int newX = positions[2];
		int newY = positions[3];

		if ((oldX<0 || oldX>7 ) || (oldY<0 || oldY>7)) {
			canUndo = false;
			return false;
		}
		if (b.isEmpty(oldX, oldY)) {
			canUndo = false;
			return false;
		}
		if (b.getPiece(oldX, oldY).isWhite() != isWhiteTurn) {
			canUndo = false;
			return false;
		}

		Piece p = b.board[oldY][oldX];

		if (move.length()>5) {
			arg = move.substring(6);
		}

		if (arg.length()==1) p.promotion = arg.charAt(0);

		// Check if move is valid
		if (!p.isLegalMove(newX, newY, b.isEmpty(newX ,newY), b)) {
			canUndo = false;
			return false;
		}

		// Check if undo
		if (arg.length()==4) undo();

		// Check for enpassant
		if (p instanceof Pawn) {
			if (p.enpassant == true) {
				b.board[oldY][newX] = null;
				p.enpassant = false;
			} 
		}
		
		// Check for castling
		
		if (p.castling=='l') { 
			if (isCheck(b, wKPosf, bKPosf)[0]) {
				p.castling = '0';
			}
			else if (p.isLegalMove(newX, newY, b.isEmpty(newX ,newY), b)){
				for (int i=1; i<4; i++) {
					if (isUnderAttack(i, oldY, isWhiteTurn)==true) {
						p.castling = '0';
						break;
					}
				}
			}
			if (p.castling=='l') {
				b.movePiece(0, oldY, 3, newY);
			}
		}
		
		if (p.castling=='r') {
			
			if (isCheck(b, wKPosf, bKPosf)[0]) {
				p.castling = '0';
			}
			else if (p.isLegalMove(newX, newY, b.isEmpty(newX ,newY), b)){
				for (int i=5; i<7; i++) {
					if (isUnderAttack(i, oldY, isWhiteTurn)==true) {
						p.castling = '0';
						break;
					}
				}
			}
			
			if (p.castling=='r') {
				b.movePiece(7, oldY, 5, newY);
			}
		}
		
		
		if (p instanceof King) {
			if (isUnderAttack(newX, newY, isWhiteTurn)==true) {
				canUndo = false;
				return false;
			}	
		}

		b.movePiece(oldX, oldY, newX, newY);
		
		if (p instanceof King) {
			if (p.isWhite()) {
				wKPosf[0] = newX;
				wKPosf[1] = newY;
			} else {
				bKPosf[0] = newX;
				bKPosf[1] = newY;
			}
		} 
		
		boolean[] isCheck = isCheck(b, wKPosf, bKPosf);
		
		if (isCheck[0] && isCheck[1] == isWhiteTurn) {
			b.movePiece(newX, newY, oldX, oldY);
			if (p instanceof King) {
				if (p.isWhite()) {
					wKPosf[0] = oldX;
					wKPosf[1] = oldY;
				} else {
					bKPosf[0] = oldX;
					bKPosf[1] = oldY;
				}
			}
			canUndo = false;
			return false;
		}
		
		canUndo = true;
		return true;
	}

	public boolean[] isCheck(Board board, int[] wKPos, int[] bKPos) {
		Board dummy = getCopy(board);

		for (int i=0; i<8; i++) {	//y
			for (int j=0; j<8; j++) {	//x
				if (dummy.isEmpty(j, i)) continue;
				else {
					Piece p1 = dummy.getPiece(j, i);
					boolean firstMove = p1.firstMove;
					char promotion = p1.promotion;
					boolean enpassant = p1.enpassant;
					char castling = p1.castling;

					if (p1.isLegalMove(wKPos[0], wKPos[1], dummy.isEmpty(wKPos[0], wKPos[1]), dummy)) {
						p1.firstMove = firstMove;
						p1.promotion = promotion;
						p1.enpassant = enpassant;
						p1.castling = castling;
						return new boolean[] {true, true};
					}
					else if (p1.isLegalMove(bKPos[0], bKPos[1], dummy.isEmpty(bKPos[0], bKPos[1]), dummy)) {
						p1.firstMove = firstMove;
						p1.promotion = promotion;
						p1.enpassant = enpassant;
						p1.castling = castling;
						return new boolean[] {true, false};
					}
				}
			}
		}
		
		return new boolean[] {false, false};
	}

	public boolean isCheckMate(char type) {
		Board dummy = getCopy(b);
		int[] wKPos = wKPosf;
		int[] bKPos = bKPosf;
		
		// Find every piece associated with checkmated(?) side
		boolean isWhite = (type=='w') ? true : false; 
		for (int i=0; i<8; i++) { // y
			for (int j=0; j<8; j++) { // x
				if (dummy.isEmpty(j, i)) continue;
				else if (dummy.getPiece(j, i).isWhite()==isWhite){ // If its white's move, then check if black is checkmated
					Piece p = dummy.getPiece(j, i);
					int currX = p.getX();
					int currY = p.getY();
					boolean firstMove = p.firstMove;
					char promotion = p.promotion;
					boolean enpassant = p.enpassant;
					char castling = p.castling;
					
					for (int r=0; r<8; r++) { // y
						for (int c=0; c<8; c++) { // x
							if (p.isLegalMove(c, r, dummy.isEmpty(c, r), dummy)) {
								
								if (p instanceof King) {
									dummy.movePiece(currX, currY, c, r);
									if (isWhite) {
										wKPos[0] = c;
										wKPos[1] = r;
									} else {
										bKPos[0] = c;
										bKPos[1] = r;
									}
								} else dummy.movePiece(currX, currY, c, r);
								
								if (!isCheck(dummy, wKPos, bKPos)[0]) {
									if (p instanceof King) {
										dummy.movePiece(c, r, currX, currY);
										if (isWhite) {
											wKPos[0] = currX;
											wKPos[1] = currY;
										} else {
											bKPos[0] = currX;
											bKPos[1] = currY;
										}
									} else dummy.movePiece(c, r, currX, currY);
									p.firstMove = firstMove;
									p.promotion = promotion;
									p.enpassant = enpassant;
									p.castling = castling;
									return false;
								}
								else {
									if (p instanceof King) {
										dummy.movePiece(c, r, currX, currY);
										if (isWhite) {
											wKPos[0] = currX;
											wKPos[1] = currY;
										} else {
											bKPos[0] = currX;
											bKPos[1] = currY;
										}
									} else dummy.movePiece(c, r, currX, currY);
									dummy.board[i][j].firstMove = firstMove;
									dummy.board[i][j].promotion = promotion;
									dummy.board[i][j].enpassant = enpassant;
									dummy.board[i][j].castling = castling;
								}
							}
						}
					}

				}
			}
		}
		
		return true;
	}

	public boolean isUnderAttack(int x, int y, boolean isWhiteTurn) {
		Board dummy = getCopy(b);
	// Check if adjacent moves are valid
		for (int i=0; i<8; i++) {	//y
			for (int j=0; j<8; j++) {	//x
				if (dummy.isEmpty(j, i)) continue;
				else if (dummy.getPiece(j, i).isWhite() != isWhiteTurn){
					Piece p = dummy.getPiece(j, i);
					boolean firstMove = p.firstMove;
					char promotion = p.promotion;
					boolean enpassant = p.enpassant;
					char castling = p.castling;
					
					if (p.isLegalMove(x, y, dummy.isEmpty(x, y), dummy)) {
						p.firstMove = firstMove;
						p.promotion = promotion;
						p.enpassant = enpassant;
						p.castling = castling;
						return true;
					}	
				}
			}
		}
		
		return false;
	}

	public int[] getPositions(String move) {
		if (move.length()<5) return new int[] {-1,-1,-1,-1};
		String oldPos = move.substring(0, 2);
		String newPos = move.substring(3, 5);
		
		int oldX, oldY, newX, newY;
		
		// oldX
		switch(oldPos.charAt(0)) {
	        case 'a': oldX = 0; break;
	        case 'b': oldX = 1; break;
	        case 'c': oldX = 2; break;
	        case 'd': oldX = 3; break;
	        case 'e': oldX = 4; break;
	        case 'f': oldX = 5; break;
	        case 'g': oldX = 6; break;
	        case 'h': oldX = 7; break;
	        default: oldX = -1;
		}
		
		// newX
		switch(newPos.charAt(0)) {
	        case 'a': newX = 0; break;
	        case 'b': newX = 1; break;
	        case 'c': newX = 2; break;
	        case 'd': newX = 3; break;
	        case 'e': newX = 4; break;
	        case 'f': newX = 5; break;
	        case 'g': newX = 6; break;
	        case 'h': newX = 7; break;
	        default: newX = -1;
		}
		
		// oldY
		oldY = 8 - Character.getNumericValue(oldPos.charAt(1));

		// newY
		newY = 8 - Character.getNumericValue(newPos.charAt(1));
		
		return new int[] {oldX,oldY,newX, newY};
	}

	public int[] getPosition(String pos) {
		if (pos.length()<2) return new int[] {-1,-1};

		int x, y;

		// oldX
		switch(pos.charAt(0)) {
			case 'a': x = 0; break;
			case 'b': x = 1; break;
			case 'c': x = 2; break;
			case 'd': x = 3; break;
			case 'e': x = 4; break;
			case 'f': x = 5; break;
			case 'g': x = 6; break;
			case 'h': x = 7; break;
			default: x = -1;
		}

		// oldY
		y = 8 - Character.getNumericValue(pos.charAt(1));

		return new int[] {x,y};
	}

	public Board getCopy(Board b) {
		Board copy = new Board(true);
		for (int i=0; i<8; i++) {
			for (int j=0; j<8; j++) {
				Piece p = b.board[i][j];
				Piece p2 = null;

				if (p!=null) {
					String name = p.getPiecename();
					char c = p.castling;
					boolean e = p.enpassant;
					boolean f = p.firstMove;

					switch (name) {
						case "wp":
							p2 = new Pawn(j,i, true);
							p2.castling = c;
							p2.enpassant = e;
							p2.firstMove = f;
							break;
						case "bp":
							p2 = new Pawn(j,i, false);
							p2.castling = c;
							p2.enpassant = e;
							p2.firstMove = f;
							break;
						case "wR":
							p2 = new Rook(j,i, true);
							p2.castling = c;
							p2.enpassant = e;
							p2.firstMove = f;
							break;
						case "bR":
							p2 = new Rook(j,i, false);
							p2.castling = c;
							p2.enpassant = e;
							p2.firstMove = f;
							break;
						case "wN":
							p2 = new Knight(j,i, true);
							p2.castling = c;
							p2.enpassant = e;
							p2.firstMove = f;
							break;
						case "bN":
							p2 = new Knight(j,i, false);
							p2.castling = c;
							p2.enpassant = e;
							p2.firstMove = f;
							break;
						case "wB":
							p2 = new Bishop(j,i, true);
							p2.castling = c;
							p2.enpassant = e;
							p2.firstMove = f;
							break;
						case "bB":
							p2 = new Bishop(j,i, false);
							p2.castling = c;
							p2.enpassant = e;
							p2.firstMove = f;
							break;
						case "wQ":
							p2 = new Queen(j,i, true);
							p2.castling = c;
							p2.enpassant = e;
							p2.firstMove = f;
							break;
						case "bQ":
							p2 = new Queen(j,i, false);
							p2.castling = c;
							p2.enpassant = e;
							p2.firstMove = f;
							break;
						case "wK":
							p2 = new King(j,i, true);
							p2.castling = c;
							p2.enpassant = e;
							p2.firstMove = f;
							break;
						case "bK":
							p2 = new King(j,i, false);
							p2.castling = c;
							p2.enpassant = e;
							p2.firstMove = f;
							break;
					}
				}

				copy.board[i][j] = p2;
			}
		}
		return copy;
	}

	public void undo() {
		b=getCopy(prevB);
		wKPosf=prevwK;
		bKPosf=prevbK;
		isWhiteTurn = !isWhiteTurn;
		canUndo = false;
	}

	public String randomMove(boolean isWhiteTurn) {

		int x1 = new Random().nextInt(8) + 1;
		int y1 = new Random().nextInt(8) + 1;
		int x2 = new Random().nextInt(8) + 1;
		int y2 = new Random().nextInt(8) + 1;


		while (!executeMove(getSquarePos(x1+96,y1)+" "+getSquarePos(x2+96,y2))) {
			x1 = new Random().nextInt(8) + 1;
			y1 = new Random().nextInt(8) + 1;
			x2 = new Random().nextInt(8) + 1;
			y2 = new Random().nextInt(8) + 1;
		}

		return getSquarePos(x1+96,y1)+" "+getSquarePos(x2+96,y2);
	}

	public String getSquarePos(int x,int y) {
		return String.valueOf((char)x)+y;
	}
}
