package life;

import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class Universe {
    private final int size;
    private int alive;
    private int generationCounter = 1;

    public Universe(int size) {
        this.size = size;
    }

    public String[][] create() {
        alive = 0;
        Random rand = new Random();
        String[][] game = new String[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (rand.nextBoolean()) {
                    game[i][j] = "O";
                    alive++;
                } else {
                    game[i][j] = " ";
                }
            }
        }
        return game;
    }

    @NotNull
    public String[][] generateNextStep(String[][] game) {
        alive = 0;
        String[][] gameCopy = deepCloneGame(game);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int counterNeighbors = 0;
                counterNeighbors += case1(game, i, j);
                counterNeighbors += case2(game, i, j);
                counterNeighbors += case3(game, i, j);
                counterNeighbors += case4(game, i, j);
                counterNeighbors += case5(game, i, j);
                counterNeighbors += case6(game, i, j);
                counterNeighbors += case7(game, i, j);
                counterNeighbors += case8(game, i, j);
                if (game[i][j].equals(" ") && counterNeighbors == 3) {
                    gameCopy[i][j] = "O";
                } else if (game[i][j].equals("O") && (counterNeighbors > 3 || counterNeighbors < 2)) {
                    gameCopy[i][j] = " ";
                }
                if (gameCopy[i][j].equals("O")) {
                    alive++;
                }
            }
        }
        game = deepCloneGame(gameCopy);
        return game;
    }

    @NotNull
    private String[][] deepCloneGame(String[][] game) {
        String[][] gameCopy = game.clone();
        for (int i = 0; i < gameCopy.length; i++) {
            gameCopy[i] = gameCopy[i].clone();
        }
        return gameCopy;
    }

    private int case8(String[][] game, int i, int j) {
        int counterNeighbors = 0;
        if (i - 1 < 0 && j + 1 > size - 1) {
            counterNeighbors = countO(game, size - 1, 0);
        } else if (i - 1 < 0 && j + 1 <= size - 1) {
            counterNeighbors = countO(game, size - 1, j + 1);
        } else if (i - 1 >= 0 && j + 1 > size - 1) {
            counterNeighbors = countO(game, i - 1, 0);
        } else {
            counterNeighbors = countO(game,i - 1, j + 1);
        }
        return counterNeighbors;
    }

    private int case7(String[][] game, int i, int j) {
        int counterNeighbors = 0;
        if (i + 1 > size - 1 && j - 1 < 0) {
            counterNeighbors = countO(game,0, size - 1);
        } else if (i + 1 > size - 1 && j - 1 >= 0) {
            counterNeighbors = countO(game,0, j - 1);
        } else if (i + 1 <= size - 1 && j - 1 < 0){
            counterNeighbors = countO(game, i + 1, size - 1);
        } else {
            counterNeighbors = countO(game, i + 1, j - 1);
        }
        return counterNeighbors;
    }

    private int case6(String[][] game, int i, int j) {
        int counterNeighbors = 0;
        if (i - 1 < 0 && j - 1 < 0) {
            counterNeighbors = countO(game, size - 1, size - 1);
        } else if (i - 1 >= 0 && j - 1 < 0){
            counterNeighbors = countO(game, i - 1, size - 1);
        } else if (i - 1 < 0) {
            counterNeighbors = countO(game, size - 1, j - 1);
        } else {
            counterNeighbors = countO(game, i - 1, j - 1);
        }
        return counterNeighbors;
    }

    private int case5(String[][] game, int i, int j) {
        return countO(game, i, j - 1 >= 0 ? j - 1 : size - 1);
    }

    private int case1(String[][] game, int i, int j) {
        return countO(game, i + 1 > size - 1 ? 0 : i + 1, j);
    }

    private int case2(String[][] game, int i, int j) {
        return countO(game, i, j + 1 > size - 1 ? 0 : j + 1);
    }

    private int case3(String[][] game, int i, int j) {
        int counterNeighbors = 0;
        if (i + 1 > size - 1 && j + 1 > size - 1) {
            counterNeighbors = countO(game, 0, 0);
        } else if (i + 1 > size - 1 && j + 1 < size) {
            counterNeighbors = countO(game, 0, j + 1);
        } else if (i + 1 < size && j + 1 > size - 1){
            counterNeighbors = countO(game, i + 1, 0);
        } else {
            counterNeighbors = countO(game, i + 1, j + 1);
        }
        return counterNeighbors;
    }

    private int case4(String[][] game, int i, int j) {
        return countO(game, i - 1 < 0 ? size - 1 : i - 1, j);
    }

    private static int countO(String[][] game, int i, int j) {
        int count = 0;
        if (game[i][j].equals("O")) {
            count++;
        }
        return count;
    }

    public void increaseGenerationCounter() {
        generationCounter++;
    }

    public int getGenerationCounter() {
        return generationCounter;
    }

    public void setGenerationCounter(int generationCounter) {
        this.generationCounter = generationCounter;
    }

    public int getAlive() {
        return alive;
    }
}
