package playing.gameOfCrapsOOP;

import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;

public class Statistics {
    private Integer numbersOfGamesSetToBePlayed;
    private int[] gamesWonOnRoll;
    private int[] gamesLostOnRoll;
    private double[] averageLengthOfTheGameOfCraps;
    private Integer numberOfGamesPlayed;
    private Integer emptySpaces;
    private Integer nrOfInstances;
    private int[][] historyGamesWonOnRoll;
    private int[][] historyGamesLostOnRoll;
    private int[] historyOfNumberOfGames;
    private double[][] historyNumberOfWinsAndLossesPerInstanceAndChances;
    private List<int[]> toBeProcessedForCalcStats;

    public Statistics(int emptySpaces) {
        this(0, emptySpaces);
    }

    public Statistics(int numbersOfGamesSetToBePlayed, int emptySpaces) {
        this.numbersOfGamesSetToBePlayed = setNumbersOfGamesSetToBePlayed(numbersOfGamesSetToBePlayed, true);
        this.gamesWonOnRoll = new int[100];
        this.gamesLostOnRoll = new int[100];
        this.averageLengthOfTheGameOfCraps = new double[100];
        this.numberOfGamesPlayed = 0;
        this.emptySpaces = setEmptySpace(emptySpaces, true);
        this.nrOfInstances = 0;
        this.historyGamesWonOnRoll = new int[100][];
        this.historyGamesLostOnRoll = new int[100][];
        this.historyOfNumberOfGames = new int[100];
        this.historyNumberOfWinsAndLossesPerInstanceAndChances = new double[100][];
    }

    public Integer getNumbersOfGamesSetToBePlayed() {
        return numbersOfGamesSetToBePlayed;
    }

    public int[] getGamesWonOnRoll() {
        return gamesWonOnRoll;
    }

    public void resetCountersForANewInstance() {
        this.numbersOfGamesSetToBePlayed = 0;
        this.gamesWonOnRoll = new int[100];
        this.gamesLostOnRoll = new int[100];
    }

    public int[] getGamesLostOnRoll() {
        return gamesLostOnRoll;
    }

    public double[] getAverageLengthOfTheGameOfCraps() {
        return averageLengthOfTheGameOfCraps;
    }

    public void addWin(int numberOfRoll) {
        ++this.gamesWonOnRoll[numberOfRoll];
    }

    public void addLoss(int numberOfRoll) {
        ++this.gamesLostOnRoll[numberOfRoll];
    }

    public Integer getNumberOfGamesPlayed() {
        return numberOfGamesPlayed;
    }

    public void addAGamePlayed() {
        this.numberOfGamesPlayed += 1;
    }

    public void addNumberOfInstances() {
        this.nrOfInstances += 1;
    }

    public int setNumbersOfGamesSetToBePlayed(Integer numbersOfGamesSetToBePlayed, boolean forConstructor) {
        Integer valueToReturn = numbersOfGamesSetToBePlayed;

        if (numbersOfGamesSetToBePlayed <= 0) {
            valueToReturn = 1;
        }

        if (!forConstructor) {
            this.numbersOfGamesSetToBePlayed = valueToReturn;
        }

        return valueToReturn;
    }

    public int setEmptySpace(int givenValue, boolean forConstructor) {
        int valueToReturn = givenValue;

        if (givenValue < 0) {
            valueToReturn = 2;
        }

        if (!forConstructor) {
            this.emptySpaces = valueToReturn;
        }

        return valueToReturn;
    }

    private List<int[]> processTheArraysForCurrentWinsAndLosses(boolean forConstructor) {
        int[] newArr = {}; int indexWhereToCut;
        this.toBeProcessedForCalcStats = new ArrayList<>(Arrays.asList(gamesWonOnRoll, gamesLostOnRoll));

        for (int i = 0; i < toBeProcessedForCalcStats.size(); i++) {
            for (int j = toBeProcessedForCalcStats.get(i).length - 1; j >= 0; j--) {
                if (toBeProcessedForCalcStats.get(i)[j] != 0) {
                    indexWhereToCut = (j + 1);
                    newArr = Arrays.copyOfRange(toBeProcessedForCalcStats.get(i), 0, indexWhereToCut);
                    break;
                }
            }
            toBeProcessedForCalcStats.set(i, newArr);
        }

        if (!forConstructor) {
            this.gamesWonOnRoll = toBeProcessedForCalcStats.get(0);
            this.gamesLostOnRoll = toBeProcessedForCalcStats.get(1);
        }

        return toBeProcessedForCalcStats;
    }

    public void addToHistoryNumberOfRollsAndGamesPlayed() {
        processTheArraysForCurrentWinsAndLosses(false);

        this.historyGamesWonOnRoll[nrOfInstances] = getGamesWonOnRoll();
        this.historyGamesLostOnRoll[nrOfInstances] = getGamesLostOnRoll();
        this.historyOfNumberOfGames[nrOfInstances] = getNumbersOfGamesSetToBePlayed();
    }

    private double[] findWinsAndLossesPerInstance(boolean forConstructor) {
        int sum;
        double[] numberOfWinsAndLossesAlongWithChances = new double[4];

        for (int i = 0; i < toBeProcessedForCalcStats.size(); i++) {
            sum = Arrays.stream(toBeProcessedForCalcStats.get(i)).reduce(0, Integer::sum);
            numberOfWinsAndLossesAlongWithChances[i] = sum;
        }

        numberOfWinsAndLossesAlongWithChances[2] = ((numberOfWinsAndLossesAlongWithChances[0] * 100) / (double)numbersOfGamesSetToBePlayed);
        numberOfWinsAndLossesAlongWithChances[3] = (100 - numberOfWinsAndLossesAlongWithChances[2]);

        if (!forConstructor) {
            this.historyNumberOfWinsAndLossesPerInstanceAndChances[nrOfInstances] = numberOfWinsAndLossesAlongWithChances;
        }

        return numberOfWinsAndLossesAlongWithChances;
    }

    public void addAverageLengthPerInstance(double averageLength) {
        this.averageLengthOfTheGameOfCraps[nrOfInstances] = averageLength;
    }

    public void printNumberOfWinsAndLossesPerRoll(String typeOfPrint) {
        String printingType = "Wins";
        int[] arrayToWorkWith = gamesWonOnRoll;
        double chances;

        if (!"Wins".equals(typeOfPrint)) {
            printingType = "Losses";
            arrayToWorkWith = gamesLostOnRoll;
        }

        System.out.printf("%n%s%s%s%s%n%n", " ".repeat(getEmptySpaces()), "Number of ",  printingType , " per roll and chances for them: ");

        System.out.printf("%s%-10s%-10s%-10s%n", " ".repeat(getEmptySpaces()), "Rolls", printingType, "Chances");

        for (int i = 0; i < arrayToWorkWith.length; i++) {
            chances = ((arrayToWorkWith[i] * 100) / (double)getNumbersOfGamesSetToBePlayed());
            System.out.printf("%s%s%3s%4s%7s%4s%6.1f%1s%n", " ".repeat(getEmptySpaces()), "| ", (i + 1), " | ", arrayToWorkWith[i], " | ", chances, "% ");
        }
    }

    public Integer getEmptySpaces() {
        return emptySpaces;
    }

    public Integer getNrOfInstances() {
        return nrOfInstances;
    }

    public int[][] getHistoryGamesWonOnRoll() {
        return historyGamesWonOnRoll;
    }

    public int[][] getHistoryGamesLostOnRoll() {
        return historyGamesLostOnRoll;
    }

    public int[] getHistoryOfNumberOfGames() {
        return historyOfNumberOfGames;
    }

    public void calculateStatisticsForNumberOfGamesPlayedAndPrintThem(int lengthOfSeparator, String separator) throws InterruptedException {
        findWinsAndLossesPerInstance(false);

        printNumberOfWinsAndLossesPerRoll("Wins");
        Thread.sleep(1000);

        System.out.printf("%n%s%s%n", " ".repeat(getEmptySpaces()), separator.repeat(lengthOfSeparator));
        printNumberOfWinsAndLossesPerRoll("Losses");
        Thread.sleep(1000);

        System.out.printf("%n%s%s%n", " ".repeat(getEmptySpaces()), separator.repeat(lengthOfSeparator));
        printingChancesOfWinningAtCrapsTotalPerInstance();

        System.out.printf("%n%s%s%n", " ".repeat(getEmptySpaces()), separator.repeat(lengthOfSeparator));
        printingAverageLengthOfInstance();

        System.out.printf("%n%s%s%n", " ".repeat(getEmptySpaces()), separator.repeat(lengthOfSeparator));
    }

    private void printingChancesOfWinningAtCrapsTotalPerInstance() throws InterruptedException {
        System.out.printf("%n%s%s%12s%12s%s%s%n", " ".repeat(getEmptySpaces() + 4), "Games Played /", " Wins /", " Losses /" , " Chances For Winning /", " For Loosing");

        if (this.historyOfNumberOfGames.length != 0) {
            System.out.printf("%s%2s%14s%12s%12s%22.1f%14.1f%n", " ".repeat(getEmptySpaces()), "1.", historyOfNumberOfGames[nrOfInstances],
                    historyNumberOfWinsAndLossesPerInstanceAndChances[nrOfInstances][0], historyNumberOfWinsAndLossesPerInstanceAndChances[nrOfInstances][1],
                    historyNumberOfWinsAndLossesPerInstanceAndChances[nrOfInstances][2], historyNumberOfWinsAndLossesPerInstanceAndChances[nrOfInstances][3]);
        } else {
            System.out.printf("%n%s%s%n", " ".repeat(getEmptySpaces()), "Currently there are no records in history.");
        }

        Thread.sleep(1000);
    }

    private void printingAverageLengthOfInstance() throws InterruptedException {
        System.out.printf("%n%s%s%n", " ".repeat(getEmptySpaces() + 4), "Games Played / Average length");

        if (historyOfNumberOfGames.length != 0) {
            System.out.printf("%s%5s%11s%17.1f%n", " ".repeat(getEmptySpaces()), "1. ", historyOfNumberOfGames[nrOfInstances],
                    averageLengthOfTheGameOfCraps[nrOfInstances]);
        } else {
            System.out.printf("%n%s%s%n", " ".repeat(getEmptySpaces()), "Currently there are no records in history of number of games.");
        }

        Thread.sleep(1000);
    }

    public double[][] getHistoryNumberOfWinsAndLossesPerInstance() {
        return historyNumberOfWinsAndLossesPerInstanceAndChances;
    }

    public List<int[]> getToBeProcessedForCalcStats() {
        return toBeProcessedForCalcStats;
    }

    private void printingTotalChancesOfWinningAtCraps() throws InterruptedException {
        System.out.printf("%n%s%s%12s%12s%s%s%n", " ".repeat(getEmptySpaces() + 3), "Games Played /", " Wins /", " Losses /" , " Chances For Winning /", " For Loosing");

        for (int i = 0; i < nrOfInstances; i++) {
            System.out.printf("%s%2s%12s%12s%12s%22.1f%14.1f%n", " ".repeat(getEmptySpaces()), (i + 1) + ". ", historyOfNumberOfGames[i],
                    historyNumberOfWinsAndLossesPerInstanceAndChances[i][0], historyNumberOfWinsAndLossesPerInstanceAndChances[i][1],
                    historyNumberOfWinsAndLossesPerInstanceAndChances[i][2], historyNumberOfWinsAndLossesPerInstanceAndChances[i][3]);
        }

        Thread.sleep(1000);
    }

    private void printingAverageTotalLengthOfAnInstance() throws InterruptedException {
        System.out.printf("%n%s%s%n", " ".repeat(getEmptySpaces() + 3), "Games Played / Average length");

        for (int i = 0; i < nrOfInstances; i++) {
            System.out.printf("%s%1s%12s%17.1f%n", " ".repeat(getEmptySpaces()), (i + 1) + ". ", historyOfNumberOfGames[i],
                    averageLengthOfTheGameOfCraps[i]);
        }

        Thread.sleep(1000);
    }

    private void printingTotalNumberOfGamesAndNumberOfInstancesPlayed() {
        System.out.printf("%n%n%s%s%d%n%s%s%d%n", " ".repeat(getEmptySpaces()), "Total number of games played: ", getNumberOfGamesPlayed(),
                " ".repeat(getEmptySpaces()) ,"Number of instances: ", getNrOfInstances());
    }

    public void printTotalAllStatistics() throws InterruptedException {
        printingTotalNumberOfGamesAndNumberOfInstancesPlayed();
        printingTotalChancesOfWinningAtCraps();
        printingAverageTotalLengthOfAnInstance();
    }

    public void resetAllStatistics() {
        this.averageLengthOfTheGameOfCraps = new double[100];
        this.numberOfGamesPlayed = 0;
        this.nrOfInstances = 0;
        this.historyGamesWonOnRoll = new int[100][];
        this.historyGamesLostOnRoll = new int[100][];
        this.historyOfNumberOfGames = new int[100];
        this.historyNumberOfWinsAndLossesPerInstanceAndChances = new double[100][];
    }
}
