package playing.gameOfCrapsOOP;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Craps {
    private int howManyGameOfCrapsToPlay;
    private int emptySpaces;
    private int numberOfGamesPlayedAlready;
    private SecureRandom rnd;
    private Player currentPlayer;

    public Craps(int howManyGameOfCrapsToPlay, int emptySpaces, Player currentPlayer) throws InterruptedException {
        this.howManyGameOfCrapsToPlay = setHowManyGameOfCrapsToPlay(howManyGameOfCrapsToPlay, true);
        this.emptySpaces = setEmptySpaces(emptySpaces, true);
        this.numberOfGamesPlayedAlready = 0;
        this.rnd = new SecureRandom();
        this.currentPlayer = currentPlayer;
    }

    public int setEmptySpaces(int emptySpaces, boolean forConstructor) throws InterruptedException {
        int valueToReturn = 2;
        String errorMessage = String.format("%n%s%s%n", " ".repeat(getEmptySpaces()), "ERROR please use a value equal or greater than zero!");

        if (emptySpaces < 0) {
            System.out.printf("%s", errorMessage);
            TimeUnit.SECONDS.sleep(1);
        } else {
            valueToReturn = emptySpaces;
        }

        if (!forConstructor) {
            this.emptySpaces = emptySpaces;
        }

        return valueToReturn;
    }

    public int setHowManyGameOfCrapsToPlay(int howManyGameOfCrapsToPlay, boolean forConstructor) throws InterruptedException {
        int valueToReturn = 1;
        String errorMessage = String.format("%n%s%s%n", " ".repeat(getEmptySpaces()), "ERROR the number of games to play should be greater than zero, if not it will default to 100!");

        if (howManyGameOfCrapsToPlay <= 0) {
            System.out.printf("%s", errorMessage);
            TimeUnit.SECONDS.sleep(1);
        } else {
            valueToReturn = howManyGameOfCrapsToPlay;
        }

        if (!forConstructor) {
            this.howManyGameOfCrapsToPlay = valueToReturn;
        }

        return valueToReturn;
    }

    public int getHowManyGameOfCrapsToPlay() {
        return howManyGameOfCrapsToPlay;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public int getEmptySpaces() {
        return emptySpaces;
    }

    public int getNumberOfGamesPlayedAlready() {
        return numberOfGamesPlayedAlready;
    }

    public void setNumberOfGamesPlayedAlready(int numberOfGamesPlayedAlready) {
        this.numberOfGamesPlayedAlready = numberOfGamesPlayedAlready;
    }

    public SecureRandom getRnd() {
        return rnd;
    }

    private List<Integer> rollTheDice() {
        int dice1 = rnd.nextInt(6) + 1;
        int dice2 = rnd.nextInt( 6) + 1;

        return new ArrayList<>(Arrays.asList(dice1, dice2));
    }

    public void playingTheGame() {
        boolean isGone;
        List<Integer> dices;
        double averageLength = 0.0;
        int numberOfRolls = 0, point = 0, sum, toCountPlays = 0;
        getCurrentPlayer().getStatisticsForPlayer().setNumbersOfGamesSetToBePlayed(howManyGameOfCrapsToPlay, false);

        while (toCountPlays < howManyGameOfCrapsToPlay) {
            isGone = true;
            dices = rollTheDice();
            sum = Math.addExact(dices.get(0), dices.get(1));

            if (numberOfRolls != 0) {
                if (sum == 7) {
                    currentPlayer.getStatisticsForPlayer().addLoss(numberOfRolls);
                } else if (sum == point) {
                    currentPlayer.getStatisticsForPlayer().addWin(numberOfRolls);
                } else {
                    isGone = false;
                }
            } else {
                if (sum == 7 || sum == 11) {
                    currentPlayer.getStatisticsForPlayer().addWin(numberOfRolls);
                } else if (sum >= 2 && sum < 4) {
                    currentPlayer.getStatisticsForPlayer().addLoss(numberOfRolls);
                } else {
                    point = sum;
                    isGone = false;
                }
            }

            if (isGone) {
                point = 0;
                averageLength += numberOfRolls;
                numberOfRolls = 0;
                currentPlayer.getStatisticsForPlayer().addAGamePlayed();
                numberOfGamesPlayedAlready += 1;
                toCountPlays += 1;
            } else {
                numberOfRolls += 1;
            }
        }

        currentPlayer.getStatisticsForPlayer().addAverageLengthPerInstance(averageLength / howManyGameOfCrapsToPlay);
        currentPlayer.getStatisticsForPlayer().addToHistoryNumberOfRollsAndGamesPlayed();
    }
}
