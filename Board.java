package life;

import javax.swing.*;
import java.awt.*;

public class Board extends JPanel {
    private final int width;
    private String[][] game;
    private final int size;
    private final Universe universe;

    public Board(int width, String[][] game, int size, Universe universe) {
        this.setPreferredSize(new Dimension(width, width));
        this.width = width;
        this.game = game;
        this.size = size;
        this.universe = universe;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int tileSize = width / size;
        if (universe.getGenerationCounter() > 1) {
            game = universe.getGame();
        }
        fillBoard(g, size, tileSize);
    }

    private void fillBoard(Graphics g, int boardSize, int tileSize) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());

        g.setColor(Color.BLACK);
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (game[i][j].equals("O")) {
                    g.fillRect(i * tileSize, j * tileSize, tileSize, tileSize);
                }
            }
        }
    }

}
