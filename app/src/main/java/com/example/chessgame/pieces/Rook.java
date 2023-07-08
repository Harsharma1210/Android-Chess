package com.example.chessgame.pieces;

import com.example.chessgame.app.*;


public class Rook extends Piece{
	private String name;

    public Rook(int x, int y, boolean isWhite) {
    	super(x, y, isWhite);
    	if (isWhite) name = "wR";
    	else name = "bR";
    }

    @Override
    public boolean isLegalMove(int newX, int newY, boolean isNewSpotEmpty, Board board) {
    	enpassant = false;

    	//Number of spaces moved in X and Y direction
        int deltaX = Math.abs(newX - super.getX());
        int deltaY = Math.abs(newY - super.getY());
        
    	// If out of bounds, return false
    	if (newX<0 || newX>7 || newY<0 || newY>7) return false;
    	
    	// Check legal move
    	if ( (deltaX==0 && deltaY==0) || (deltaX>0 && deltaY>0) ) return false;
    	
    	// Check collision
        if(!isNewSpotEmpty && board.getPiece(newX, newY).isWhite()==isWhite()) return false;
        
        // Check for clear path
        return isHorizontalClear(newX, newY, board);
    }
    
    @Override
	public String getPiecename() {
		return name;
	}
} 
