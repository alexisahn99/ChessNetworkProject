package server.controller;
/*
 * This class defines the flags that will be used to communicate 
 * between View - [ Client - Server ] - Controller
*/

public enum FunctionFlag {
    DESTINATION("moveable squares"), // this.view.drawPossibleMoves(movableSquares);
    SOURCE("current pieces"), // this.view.enableSquares(allCurrentPieces);
    REPAINT("end of turn"); // this.view.update() 
    // TODO: when REPAINT we need to send info of what piece is where

    FunctionFlag(String s)
    {
        //System.out.println("FunctionFlag constructor is called");
    }
}


