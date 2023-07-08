package com.example.chessgame.pieces;

import com.example.chessgame.app.*;


public class Queen extends Piece {
	private String name;

	public Queen(int x, int y, boolean isWhite) {
    	super(x, y, isWhite);
    	if (isWhite) name = "wQ";
    	else name = "bQ";
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
    	if ( (deltaX==0 && deltaY==0) || ( (deltaX!=deltaY) && (deltaX>0 && deltaY>0) ) ) return false;
    	
    	//If landing on own piece, return false
        if(!isNewSpotEmpty && board.getPiece(newX, newY).isWhite()==isWhite()) return false;

        // Check if path is clear
        if (deltaX==deltaY) return isDiagonalClear(newX, newY, board);
        else return isHorizontalClear(newX, newY, board);
        
    }
    
    @Override
	public String getPiecename() {
		return name;
	}
} 
