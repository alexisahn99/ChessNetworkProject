package utility;
/*
 * This class defines the flags that will be used to communicate 
 * between View - [ Client - Server ] - Controller
*/

public enum FunctionFlag {
    DISABLE("disable the board"), 
    DESTINATION("moveable squares"), 
    SOURCE("current pieces"),
    REPAINT("end of turn"),
    CHECKMATE("game over");

    FunctionFlag(String s) {}
}


