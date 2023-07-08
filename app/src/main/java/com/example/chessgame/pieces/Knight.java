package com.example.chessgame.pieces;

import com.example.chessgame.app.*;


public class Knight extends Piece{
	private String name;

    public Knight(int x, int y, boolean isWhite) {
    	super(x, y, isWhite);
    	if (isWhite) name = "wN";
    	else name = "bN";
    }

    @Override
    public boolean isLegalMove(int newX, int newY, boolean isNewSpotEmpty, Board board) {
    	enpassant = false;

        //Number of spaces moved in X and Y direction
        int deltaX = Math.abs(newX - super.getX());
        int deltaY = Math.abs(newY - super.getY());
        
    	// If out of bounds, return false
    	if (newX<0 || newX>7 || newY<0 || newY>7) return false;
    	
    	//If landing on own piece, return false
        if(!isNewSpotEmpty && board.getPiece(newX, newY).isWhite()==isWhite()) return false;
    	
    	// If legal move, return true
    	if ( (deltaX==2 && deltaY==1) || (deltaX==1 && deltaY==2) ) return true;
    	
    	
        return false;
    }
    
    @Override
	public String getPiecename() {
		return name;
	}
} 
