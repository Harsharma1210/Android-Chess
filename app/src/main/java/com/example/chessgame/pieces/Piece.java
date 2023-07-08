package com.example.chessgame.pieces;
import com.example.chessgame.app.*;


public abstract class Piece {
    private boolean white = true;
    public boolean firstMove = true;
    public int startX, startY;

    public Piece(int startX, int startY, boolean white) {
    	this.startX = startX;
    	this.startY = startY;
    	this.white = white;
    }
    

    public int getX() {  
    	return startX;
    }
    public int getY() {
    	return startY;
    }
    public boolean isWhite(){
        return this.white;
    }

    public boolean isHorizontalClear(int newX, int newY, Board board) {
    	int deltaX = Math.abs(newX - startX);
        int deltaY = Math.abs(newY - startY);
        
    	//Vertical Movement - Check for clear path
        if (deltaX==0) {
        	//Moving up
        	if (newY<startY) {
        		for (int i=newY+1; i<startY; i++) {
        			if (!board.isEmpty(startX, i)) return false;
        		}
        		return true;
        	}
        	
        	//Moving down
        	if (newY>startY) {
        		for (int i=startY+1; i<newY; i++) {
        			if (!board.isEmpty(startX, i)) return false;
        		}
        		return true;
        	}
        	
        }
        
        
        // Horizontal Movement - Check for clear path
        if (deltaY==0) {
        	//Moving left
        	if (newX<startX) {
        		for (int i=newX+1; i<startX; i++) {
        			if (!board.isEmpty(i, startY)) return false;
        		}
        		return true;
        	}
        	
        	//Moving right
        	if (newX>startX) {
        		for (int i=startX+1; i<newX; i++) {
        			if (!board.isEmpty(i, startY)) return false;
        		}
        		return true;
        	}
        	
        }
        
        return false;
    }

    public boolean isDiagonalClear(int newX, int newY, Board board) {

     // Upward Movement - Check for clear path
        if (newY<startY) {
        	//Moving left
        	if (newX<startX) {
        		for (int i=newX+1, j = newY+1; i<startX && j<startY; i++, j++) {
        			if (!board.isEmpty(i,j)) return false;
        		}
        		return true;
        	}
        	
        	//Moving right
        	if (newX>startX) {
        		for (int i=newX-1, j = newY+1; i>startX && j<startY; i--, j++) {
        			if (!board.isEmpty(i,j)) return false;
        		}
        		return true;
        	}
        }
        
        // Downward Movement - Check for clear path
        if (newY>startY) {
        	//Moving left
        	if (newX<startX) {
        		for (int i=newX+1, j = newY-1; i<startX && j>startY; i++, j--) {
        			if (!board.isEmpty(i,j)) return false;
        		}
        		return true;
        	}
        	
        	//Moving right
        	if (newX>startX) {
        		for (int i=newX-1, j = newY-1; i>startX && j>startY; i--, j--) {
        			if (!board.isEmpty(i,j)) return false;
        		}
        		return true;
        	}
        }
        
     
        return false;
    	
    }

    public abstract boolean isLegalMove(int newX, int newY, boolean isNewSpotEmpty, Board board);
    public char promotion;
    public boolean enpassant;
    public char castling = '0';
    public abstract String getPiecename();
}