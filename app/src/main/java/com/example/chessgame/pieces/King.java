package com.example.chessgame.pieces;

import com.example.chessgame.app.*;


public class King extends Piece{
	private String name;

    public King(int x, int y, boolean isWhite) {
    	super(x, y, isWhite);
    	if (isWhite) name = "wK";
    	else name = "bK";
    }

    @Override
    public boolean isLegalMove(int newX, int newY, boolean isNewSpotEmpty, Board board) {
    	super.castling = '0';
    	super.enpassant = false;
        //Number of spaces moved in X and Y direction
        int deltaX = Math.abs(newX - super.getX());
        int deltaY = Math.abs(newY - super.getY());

        // If out of bounds, return false
    	if (newX<0 || newX>7 || newY<0 || newY>7) return false;
    	
    	// Check legal move
    	if ( (deltaX==0 && deltaY==0) || ((deltaX>1 || deltaY>1) && !(deltaX==2 && deltaY==0 && super.firstMove))) return false;
    	
    	//If landing on own piece, return false
        if(!isNewSpotEmpty && board.getPiece(newX, newY).isWhite()==isWhite()) return false;

        // Check for adjacent move
        if ( (deltaX==1 && deltaY==0) || (deltaX==0 && deltaY==1) || (deltaX==deltaY) ) {
        	super.firstMove = false;
        	return true;
        }
        
        // Check for castling
        
        if(deltaX == 2 && deltaY == 0) {
        	Piece rook = (newX<getX()) ? board.getPiece(newX-2, newY) : board.getPiece(newX+1, newY) ;
        	
    		if (rook instanceof Rook && rook.firstMove && 
    			  super.isHorizontalClear(rook.getX(), rook.getY(), board) && 
    			    rook.isWhite()==super.isWhite()) {
    			castling = (newX<getX()) ? 'l' : 'r';
    			return true;
    			
    		} else return false;
    		
        }
        
        return false;
     
    }
    
    @Override
	public String getPiecename() {
		return name;
	}
} 
