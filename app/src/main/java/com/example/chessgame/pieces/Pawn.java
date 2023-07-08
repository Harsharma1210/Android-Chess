package com.example.chessgame.pieces;
import com.example.chessgame.app.*;


public class Pawn extends Piece {
	private String name;
	
	public Pawn(int x, int y, boolean isWhite) {
		super(x, y, isWhite);
		if (isWhite) name = "wp";
    	else name = "bp";
	} 
	
	@Override
    public boolean isLegalMove(int newX, int newY, boolean isNewSpotEmpty, Board board) {
    	int xDifference = newX - getX();
    	int yDifference = newY - getY();
    	enpassant = false;

    	// If out of bounds, return false 
    	if (newX<0 || newX>7 || newY<0 || newY>7) return false;
    	
    	// If white and moves down, return false
    	if (isWhite() && yDifference>=0) return false; //e

    	// If black and moves up, return false
    	if (!isWhite() && yDifference<=0) return false; // e
    	
    	// If pawn moves strictly horizontally, return false
    	if (Math.abs(xDifference)>0 && yDifference==0) return false;   	//a
    	
    	// If moves more than 1 square, horizontally, return false
    	if (Math.abs(xDifference)>1) return false;							//a
    	
    	// If moves more than 1 square, vertically, on its non-first move, return false;
    	if (!firstMove && Math.abs(yDifference)>1) return false;
    	
    	// If moves more than 2 squares, vertically, on its first move, return false
    	if (firstMove && Math.abs(yDifference)>2) return false;
    	
 
    	//Check  diagonal movement
    	if (Math.abs(xDifference)>0 && Math.abs(yDifference)>0) {
    		// En passant
    		if (isNewSpotEmpty && !board.isEmpty(newX, getY())) {
    			if (board.getPiece(newX, getY()).isWhite() != isWhite()) {
	    			enpassant = true;
	    			// Check for promotion
	    			if(isWhite() && newY == 0 || !isWhite() && newY == 7)
	    	             board.board[getY()][getX()] = promote();
	    			return true;
    			} 
    			else return false;
    		}
    		// Diagonal Enemy
    		else if (!isNewSpotEmpty && (board.getPiece(newX, newY).isWhite() != isWhite())) {
    			// Check promotion
    			if(isWhite() && newY == 0 || !isWhite() && newY == 7)
   	             board.board[getY()][getX()] = promote();
    			return true;
    		}
    		else return false;
    	}
    	
    	// Checks vertical movement
    	if (!isNewSpotEmpty) return false;
    	else if (Math.abs(yDifference)==2) { // verify square before target is clear
    		if (isWhite() && !board.isEmpty(getX(), newY+1)) return false;
    		else if (!isWhite() && !board.isEmpty(getX(), newY-1)) return false;
    	}
    	
    	if(isWhite() && newY == 0 || !isWhite() && newY == 7)
            board.board[getY()][getX()] = promote();
    	firstMove = false;
    	
    	return true;
    }

	@Override
	public String getPiecename() {
		return name;
	}

	public Piece promote() {
		switch(promotion) {
        case 'R': return (Piece) new Rook(getX(), getY(), isWhite());
        case 'N': return (Piece) new Knight(getX(), getY(), isWhite());
        case 'B': return (Piece) new Bishop(getX(), getY(), isWhite());
        default: return (Piece) new Queen(getX(), getY(), isWhite());
    }
	}

}
