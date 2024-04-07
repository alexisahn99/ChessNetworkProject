package client;

import java.awt.*;
import javax.swing.*;

import utility.UnicodeMap;

public class ChessPieces 
{
    private JButton[][] boardSegment;

    public ChessPieces(JButton[][] boardSegment)
    {
        this.boardSegment = boardSegment;
        this.addPieces();
    }

    public void initializePiece(JButton button)
    {
        button.setFont(new Font("Dialog", Font.PLAIN, 45));
        button.setOpaque(true); 
        button.setBorder(null);
    }

    /**
     * Helper function to add pieces to board.
     */
    public void addPieces() {
        for (int i = 0; i < 8; i++) {
            boardSegment[6][i].setText(UnicodeMap.wPawn);
            initializePiece(boardSegment[6][i]);

            boardSegment[1][i].setText(UnicodeMap.bPawn);
            initializePiece(boardSegment[1][i]);

            if (i == 1 || i == 6) 
            {
                boardSegment[7][i].setText(UnicodeMap.wKnight);
                initializePiece(boardSegment[7][i]);

                boardSegment[0][i].setText(UnicodeMap.bKnight);
                initializePiece(boardSegment[0][i]);
            }
            else if (i == 2 || i == 5)
            {
                boardSegment[7][i].setText(UnicodeMap.wBishop);
                initializePiece(boardSegment[7][i]);

                boardSegment[0][i].setText(UnicodeMap.bBishop);
                initializePiece(boardSegment[0][i]);
            }
            else if (i == 0 || i == 7)
            {
                boardSegment[7][i].setText(UnicodeMap.wRook);
                initializePiece(boardSegment[7][i]);

                boardSegment[0][i].setText(UnicodeMap.bRook);
                initializePiece(boardSegment[0][i]);
            }
            else if (i == 3) {
                boardSegment[7][i].setText(UnicodeMap.wQueen);
                initializePiece(boardSegment[7][i]);

                boardSegment[0][i].setText(UnicodeMap.bQueen);
                initializePiece(boardSegment[0][i]);
            }
            else {
                boardSegment[7][i].setText(UnicodeMap.wKing);
                initializePiece(boardSegment[7][i]);

                boardSegment[0][i].setText(UnicodeMap.bKing);
                initializePiece(boardSegment[0][i]);
            }
        }
    }
}
