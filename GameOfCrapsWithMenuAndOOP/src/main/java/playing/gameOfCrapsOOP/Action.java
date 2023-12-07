package playing.gameOfCrapsOOP;

import java.util.List;
import java.util.Arrays;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Action {
    private String valueFromUser;
    private Scanner input;
    private int emptySpaces;
    private final List<Player> registeredPlayers;
    private final List<SubMenu> modules;

    private SubMenu useHelp;
    private SubMenu registerPlayer;
    private SubMenu printingPlayers;
    private SubMenu changePlayer;
    private SubMenu askHowManyTimesToPlay;
    private SubMenu playGame;
    private SubMenu printingStatistics;
    private SubMenu afterPrintingStatistics;

    public Action(int emptySpaces) throws InterruptedException {
        this.input = new Scanner(System.in);
        this.emptySpaces = setEmptySpaces(emptySpaces, true);
        this.registeredPlayers = new ArrayList<>();
        this.modules = new ArrayList<>();

        addingModules();
    }

    public Action(Action currentOne) {
        this.input = currentOne.getInput();
        this.emptySpaces = currentOne.getEmptySpaces();
        this.registeredPlayers = currentOne.getRegisteredPlayers();
        this.modules = getModules();
    }

    private void addingModules() throws InterruptedException {
        creatingHelpModule();
        creatingRegisterPlayerModule();
        creatingPrintPlayersModule();
        creatingChangePlayerModule();
        creatingHowManyTimesToPlayModule();
        creatingPlayingTheGameModule();
        creatingPrintingStatisticsFromMenuModule();
        creatingAfterPrintingStatisticsModule();

        modules.addAll(Arrays.asList(changePlayer, useHelp, registerPlayer, printingPlayers, askHowManyTimesToPlay,
                 playGame, printingStatistics, afterPrintingStatistics));
    }

    private void creatingHelpModule() throws InterruptedException {
        this.useHelp = new SubMenu(3, "back to return to main menu or quit to terminate: ",
                2, false);
        useHelp.setHeaderForGame("playing the game of craps", " # ", "-",
                2, 2, 0, false);

        String contentToBeSet = String.format("%s","There are two options to play the game of craps. For the first one you can tell how many games will be played " +
                "then stats will be calculated. For the second one we will play only one hand and stats will be calculated." +
                "In order to play a game a player must be registered and set to play. Also there are two more options to print statistics and reset them." +
                "When you start a game you can set how much money to play for that game. Along with that you can see all your winnings or loosing in statistics.");
        useHelp.addContent(contentToBeSet, false);
    }

    void help() throws InterruptedException {
        while (true) {
            System.out.print("\033[H\033[2J");
            System.out.flush();

            useHelp.printHeaderSubMenu("how to guide", true, 2, 1, 11);

            useHelp.printContent();

            useHelp.printSeparatedLineBottom();
            useHelp.printMessageForSubMenu();

            AuxChecks.checkForQuitAndBack(this.readInputFromUser(false), 2, false);

            if (!"back".equals(getValueFromUser())) {
                System.out.printf("%n%s%s%n", " ".repeat(getEmptySpaces()), "ERROR please use back or quit as options!");
            } else {
                break;
            }
        }
    }

    private void creatingRegisterPlayerModule() throws InterruptedException {
        this.registerPlayer = new SubMenu(3, "provide the necessary information to register a player or (quit/back): ", 2, false);
        registerPlayer.setHeaderForGame("playing the game of craps", " # ", "-",
                2, 2, 0, false);

        registerPlayer.addOptionsForSubMenu("name, age, occupation");
    }

    private void validateNameAgeOccupationWhenAddPlayer(String errorMessage, List<String> tempOptions) throws InterruptedException {
        int i = 0, j = 0;
        boolean alreadyExists = false;
        List<String> toCheck = new ArrayList<>(Arrays.asList(tempOptions.get(0), tempOptions.get(2)));

        while (i < 2) {
            try {
                if (j < toCheck.size()) {
                    Integer.parseInt(toCheck.get(j).trim());
                    System.out.printf("%s", errorMessage);
                    TimeUnit.SECONDS.sleep(1);
                    return;
                }
            } catch (NumberFormatException e) {
                j += 1;
            }
            i++;
        }

        try {
            int age = Integer.parseInt(tempOptions.get(1));
            Player toBeAdded = new Player(tempOptions.get(0), age, tempOptions.get(2), getEmptySpaces());

            if (toBeAdded.getAge() >= 18) {
                for (Player registeredPlayer : registeredPlayers) {
                    if ((registeredPlayer.getName().equalsIgnoreCase(toBeAdded.getName())) &&
                            (registeredPlayer.getAge() == toBeAdded.getAge()) &&
                            (registeredPlayer.getOccupation().equalsIgnoreCase(toBeAdded.getOccupation()))) {
                        alreadyExists = true;
                        break;
                    }
                }

                if (!alreadyExists) {
                    registeredPlayers.add(toBeAdded);
                    String messageToBeDisplayed = String.format("%n%s%s%s%s", " ".repeat(getEmptySpaces()), "Registering ", toBeAdded.getName(), " as a player");
                    Loading.starting(7, '.', messageToBeDisplayed, 200, true, "DONE");
                } else {
                    System.out.printf("%n%s%s", " ".repeat(getEmptySpaces()), "Given player is already registered!");
                }

                TimeUnit.SECONDS.sleep(1);
            }
        } catch (NumberFormatException f) {
            System.out.printf("%s", errorMessage);
            TimeUnit.SECONDS.sleep(1);
        }
    }

    private void creatingPlayers() throws InterruptedException {
        ArrayList<String> tempListWithOptions = new ArrayList<>();

        for (String s : new ArrayList<>(Arrays.asList(getValueFromUser().toLowerCase().trim().split(",")))) {
            tempListWithOptions.add(s.trim());
        }

        String errorMessage = String.format("%n%s%s", " ".repeat(getEmptySpaces()), "ERROR please provide all fields requested!");

        if (tempListWithOptions.size() != 3) {
            System.out.printf("%s", errorMessage);
            TimeUnit.SECONDS.sleep(1);
        } else {
            validateNameAgeOccupationWhenAddPlayer(errorMessage, tempListWithOptions);
        }
    }

    void registerPlayers() throws InterruptedException {
        while (true) {
            System.out.print("\033[H\033[2J");
            System.out.flush();

            registerPlayer.printHeaderSubMenu("add player", true, 2, 1, 11);

            registerPlayer.printOptionsForSubMenu("In order to add a player please provide complete");
            registerPlayer.printSeparatedLineBottom();
            registerPlayer.printMessageForSubMenu();

            AuxChecks.checkForQuitAndBack(this.readInputFromUser(false), 2, false);

            if (!"back".equals(getValueFromUser())) {
                creatingPlayers();
            } else {
                break;
            }
        }
    }

    private void creatingPrintPlayersModule() throws InterruptedException {
        this.printingPlayers = new SubMenu(3, "back to return to main menu or quit to terminate: ",
                2, false);
        printingPlayers.setHeaderForGame("playing the game of craps", " * ", "-",
                2, 2, 2, false);
    }

    void printingPlayers() throws InterruptedException {
        if (registeredPlayers.isEmpty()) {
            System.out.printf("%n%s%s", " ".repeat(getEmptySpaces()), "ERROR there are no players registered!");
            TimeUnit.SECONDS.sleep(1);
        } else {
            while (true) {
                System.out.print("\033[H\033[2J");
                System.out.flush();

                printingPlayers.printHeaderSubMenu("printing players", true, 2, 1, 9);

                for (int i = 0; i < registeredPlayers.size(); i++) {
                    System.out.printf("%n%s%s%s%s%s%s%s%s", " ".repeat(getEmptySpaces()), (i + 1), ". ", registeredPlayers.get(i).getName(),
                            ", Age: ", registeredPlayers.get(i).getAge(), ", Occupation: ", registeredPlayers.get(i).getOccupation().toLowerCase());
                }

                printingPlayers.printSeparatedLineBottom();
                printingPlayers.printMessageForSubMenu();

                AuxChecks.checkForQuitAndBack(this.readInputFromUser(false), 2, false);

                if (!"back".equals(getValueFromUser())) {
                    System.out.printf("%n%s%s", " ".repeat(getEmptySpaces()), "ERROR please use back or quit as options!");
                    TimeUnit.SECONDS.sleep(1);
                } else {
                    break;
                }
            }
        }
    }

    private void creatingChangePlayerModule() throws InterruptedException {
        this.changePlayer = new SubMenu(3, "please choose a player from above or back/quit option: ", 2, false);
        changePlayer.setHeaderForGame("playing the game of craps", " # ", "-",
                2, 2, 0, false);
        changePlayer.addOptionsForSubMenu("back, quit");
    }

    private String actOnChangePlayer() throws InterruptedException {
        String valueFromUser = "none";

        try {
            int valueOfIndex = Integer.parseInt(getValueFromUser());
            int sumOfSizes = (registeredPlayers.size() + changePlayer.getOptionsOnOneLine().size());

           if ((valueOfIndex < 1) || valueOfIndex > sumOfSizes) {
               System.out.printf("%n%s%s", " ".repeat(getEmptySpaces()), "ERROR please choose an option from those printed!");
               TimeUnit.SECONDS.sleep(1);
           } else {
               if (valueOfIndex <= registeredPlayers.size()) {

                   Player playerToBeSet = registeredPlayers.get(valueOfIndex - 1);

                   for (SubMenu module : modules) {
                       module.setCurrentPlayer(playerToBeSet, false);
                   }

                   String messageSuccessAddedPlayer = String.format("%n%s%s%s", " ".repeat(getEmptySpaces()), "Current player set to ", registerPlayer.getCurrentPlayer().getName());
                   Loading.starting(7, '.', messageSuccessAddedPlayer, 200, true, "DONE");
                   Thread.sleep(200);

                   valueFromUser = "true";
               } else if (valueOfIndex == sumOfSizes) {
                   AuxChecks.checkForQuitAndBack("quit", 2, false);
               } else if (valueOfIndex == (sumOfSizes - 1)) {
                   valueFromUser = AuxChecks.checkForQuitAndBack("back", 2, false);
               }
           }
        } catch (NumberFormatException e) {
            System.out.printf("%n%s%s", " ".repeat(getEmptySpaces()), "ERROR please choose an option from the ones printed!");
            TimeUnit.SECONDS.sleep(1);
        }

        return valueFromUser;
    }

    void changePlayer() throws InterruptedException {
        String valueFromUserChangingPlayer = "";
        String extraOpt;

        if (getRegisteredPlayers().isEmpty()) {
            System.out.printf("%n%s%s", " ".repeat(getEmptySpaces()), "ERROR there are no players registered!");
            TimeUnit.SECONDS.sleep(1);
        } else {
            while (!"back".equals(valueFromUserChangingPlayer)) {
                System.out.print("\033[H\033[2J");
                System.out.flush();

                changePlayer.printHeaderSubMenu("change player", true, 2, 1, 10);

                for (int i = 0; i < registeredPlayers.size(); i++) {
                    System.out.printf("%n%s%s%s%s%s%s%s%s%s", " ".repeat(getEmptySpaces()), "[ ", (i + 1), " ] ", registeredPlayers.get(i).getName(),
                            ", Age: ", registeredPlayers.get(i).getAge(), ", Occupation: ", registeredPlayers.get(i).getOccupation().toLowerCase());
                }

                for (int j = 0; j < changePlayer.getOptionsOnOneLine().size(); j++) {
                    extraOpt = changePlayer.getOptionsOnOneLine().get(j).trim();
                    System.out.printf("%n%s%s%s%s%s", " ".repeat(getEmptySpaces()), "[ ", (registeredPlayers.size() + j + 1),
                            " ] ", extraOpt.toUpperCase().charAt(0) + extraOpt.substring(1));
                }

                changePlayer.printSeparatedLineBottom();
                changePlayer.printMessageForSubMenu();

                this.readInputFromUser(false);
                valueFromUserChangingPlayer = actOnChangePlayer();
            }
        }
    }

    private void creatingHowManyTimesToPlayModule() throws InterruptedException {
        this.askHowManyTimesToPlay = new SubMenu(3, "provide requested information or back/quit: ",
                2, false);
        askHowManyTimesToPlay.setHeaderForGame("playing the game of craps", " * ", "-",
                2, 2, 0, false);
        askHowManyTimesToPlay.addContent(String.format("%s", "Please you need to tell us how many games you want to be played. Statistics will be calculated."), false);
    }

    private int howManyGamesToBePlay() throws InterruptedException {
        String valueFromUserGameToBePlay = "";
        String errorMessage = String.format(String.format("%n%s%s", " ".repeat(getEmptySpaces()), "ERROR please provide the req input greater than zero!"));
        int numberOfGames = 0;

        while ((numberOfGames <= 0) && !"back".equals(valueFromUserGameToBePlay)) {
            System.out.print("\033[H\033[2J");
            System.out.flush();

            askHowManyTimesToPlay.printHeaderSubMenu("games to be played", true, 2, 1, 8);

            askHowManyTimesToPlay.printContent();

            askHowManyTimesToPlay.printSeparatedLineBottom();
            askHowManyTimesToPlay.printMessageForSubMenu();

            valueFromUserGameToBePlay = AuxChecks.checkForQuitAndBack(this.readInputFromUser(false), 2, false);

            if ("back".equalsIgnoreCase(valueFromUserGameToBePlay)) return -1;

            try {
                 numberOfGames = Integer.parseInt(valueFromUserGameToBePlay);

                 if (numberOfGames <= 0) {
                     System.out.printf("%s", errorMessage);
                 } else {
                     String gamesSelectedToBePlayed = String.format("%n%s%s%s", " ".repeat(getEmptySpaces()), numberOfGames, " games were selected to be played");
                     Loading.starting(7, '.', gamesSelectedToBePlayed, 200, true, "DONE");
                 }

                Thread.sleep(500);
            } catch (NumberFormatException e) {
                System.out.printf("%s",errorMessage);
                TimeUnit.SECONDS.sleep(1);
            }
        }

        return numberOfGames;
    }

    private void creatingPlayingTheGameModule() throws InterruptedException {
        this.playGame = new SubMenu(3, "please choose an option from above: ",
                2, true);
        playGame.setHeaderForGame("playing the game of craps", " * ", "-",
                2, 2, 0, false);
        playGame.addOptionsForMenu("play again, return to how many games to play, back to main menu, quit");
    }

    private String actOnAfterGamesWherePlayed(String inputFromUser, int numberOfGamesToPlay) throws InterruptedException {
        String valueToReturn = "";

        switch (inputFromUser) {
            case "1" -> {
                String messageToUse = String.format("%n%s%s%s%s", " ".repeat(emptySpaces), "Playing again ", numberOfGamesToPlay, " games");
                Loading.starting(5,'.', messageToUse, 300, true, "START");
                Thread.sleep(250);

                return "play_again";
            }
            case "2" -> {
                String messageToUse = String.format("%n%s%s", " ".repeat(emptySpaces), "Return to number of games to play");
                Loading.starting(5,'.', messageToUse, 300, true, "DONE");
                Thread.sleep(200);

                return "number_games";
            }
            case "3" -> valueToReturn = AuxChecks.checkForQuitAndBack("back", 2, false);
            case "4" -> AuxChecks.checkForQuitAndBack("quit", 2, false);
            default ->  {
                System.out.printf("%n%s%s", " ".repeat(getEmptySpaces()), "ERROR please use an option from those above.");
                Thread.sleep(1000);
            }
        }

        return valueToReturn;
    }

    private String printOptionsAfterPlayingGamesOfCraps(int numberOfGamesToPlay) throws InterruptedException {
        String valueToCheck = "";

        while (!"back".equals(valueToCheck)) {
            System.out.print("\033[H\033[2J");
            System.out.flush();

            playGame.printHeaderSubMenu("what to do ?", true, 2, 1, 11);

            for (int i = 0; i < playGame.getOptionsForMenu().size(); i++) {
                System.out.printf("%n%s%s%s%s%s", " ".repeat(getEmptySpaces()), "[ ", (i + 1), " ] ", playGame.getOptionsForMenu().get(i));
            }

            playGame.printSeparatedLineBottom();
            playGame.printMessageForSubMenu();

            valueToCheck = actOnAfterGamesWherePlayed(this.readInputFromUser(false), numberOfGamesToPlay);

            if ("number_games".equalsIgnoreCase(valueToCheck)) {
                return "none";
            } else if ("play_again".equalsIgnoreCase(valueToCheck)) {
                break;
            }
        }

        return valueToCheck;
    }

    private void creatingAfterPrintingStatisticsModule() throws InterruptedException {
        this.afterPrintingStatistics = new SubMenu(3, "return to printing menu or quit (back/quit): ", 2, false);
        afterPrintingStatistics.setHeaderForGame("playing the game of craps", " # ", "-",
                2, 2, 0, false);
    }

    private void resetStats(Player playerToResetStatsFor, boolean allPlayers) throws InterruptedException {
        String messageToUseSinglePlayer = String.format("%n%s%s%s", " ".repeat(getEmptySpaces()), "--> Resetting statistics for player: ", playerToResetStatsFor.getName());

        playerToResetStatsFor.getStatisticsForPlayer().resetAllStatistics();

        Loading.starting(5, '.', messageToUseSinglePlayer, 300, true, "DONE");
        Thread.sleep(200);
    }

    public String decideAfterResettingStatistics(Player playerToResetStatsFor, boolean allPlayers) throws InterruptedException {
        String valueToReturn = "";

        while (!"back".equalsIgnoreCase(valueToReturn)) {
            System.out.print("\033[H\033[2J");
            System.out.flush();

            afterPrintingStatistics.printHeaderSubMenu("resetting statistics", true, 2, 1, 7);

            if (!allPlayers) {
                resetStats(playerToResetStatsFor, allPlayers);
            } else {
                for (Player registeredPlayer : registeredPlayers) {
                    resetStats(registeredPlayer, allPlayers);
                }
            }

            afterPrintingStatistics.printSeparatedLineBottom();
            afterPrintingStatistics.printMessageForSubMenu();

            valueToReturn = AuxChecks.checkForQuitAndBack(this.readInputFromUser(false), 2, false);
        }

        return valueToReturn;
    }

    private void printTotalAllStats(Player playerToPrintStatsFor) throws InterruptedException {
        System.out.printf("%n%s%s%s", " ".repeat(getEmptySpaces()), "--> For player: ", playerToPrintStatsFor.getName());
        playerToPrintStatsFor.getStatisticsForPlayer().printTotalAllStatistics();
    }

    public String decideAfterPrintingStatistics(Player playerToPrintStatsFor, boolean allPlayers) throws InterruptedException {
        String checkingValue = "";

        while (!"back".equalsIgnoreCase(checkingValue)) {
            System.out.print("\033[H\033[2J");
            System.out.flush();

            afterPrintingStatistics.printHeaderSubMenu("printing statistics", true, 2, 1, 7);

            if (!allPlayers) {
                printTotalAllStats(playerToPrintStatsFor);
            } else {
                for (Player registeredPlayer : registeredPlayers) {
                    printTotalAllStats(registeredPlayer);
                }
            }

            afterPrintingStatistics.printSeparatedLineBottom();
            afterPrintingStatistics.printMessageForSubMenu();

            checkingValue = AuxChecks.checkForQuitAndBack(this.readInputFromUser(false), 2, false);
        }

        return checkingValue;
    }

    private String actOnPrintingStatsFromMenu(String inputFromUser, boolean forPrint) throws InterruptedException {
        String valueReTurnToMain = "default";
        int numberOfPlayersReg = registeredPlayers.size();

        try {
            int indexForPlayer = Integer.parseInt(inputFromUser);

            if ((indexForPlayer > 0) && (indexForPlayer <= numberOfPlayersReg)) {
                Player statsForPlayerToPrint = registeredPlayers.get(indexForPlayer - 1);
                if (forPrint) {
                    decideAfterPrintingStatistics(statsForPlayerToPrint, false);
                } else {
                    decideAfterResettingStatistics(statsForPlayerToPrint, false);
                }
            } else if ((numberOfPlayersReg > 1) && (indexForPlayer == (numberOfPlayersReg + 1))) {
                if (forPrint) {
                    decideAfterPrintingStatistics(null, true);
                } else {
                    decideAfterResettingStatistics(null, true);
                }
            } else {
                int temp = numberOfPlayersReg;
                if (numberOfPlayersReg <= 1) temp = 0;

                if (indexForPlayer == (temp + 2)) {
                    valueReTurnToMain = AuxChecks.checkForQuitAndBack("back", 2, false);
                } else if (indexForPlayer == (temp + 3)) {
                    AuxChecks.checkForQuitAndBack("quit", 2, false);
                } else {
                    System.out.printf("%n%s%s", " ".repeat(getEmptySpaces()), "ERROR please use an option from above!");
                    Thread.sleep(1300);
                }
            }
        } catch (NumberFormatException e) {
            System.out.printf("%n%s%s", " ".repeat(getEmptySpaces()), "ERROR please use an option from above!");
            Thread.sleep(1300);
        }

        return valueReTurnToMain;
    }

    private boolean checksBeforeActionOnStatistics() throws InterruptedException {
        boolean toReturn = true;

        if (registeredPlayers.isEmpty()) {
            System.out.printf("%n%s%s", " ".repeat(getEmptySpaces()), "ERROR there is no player registered!");
            Thread.sleep(1300);
            toReturn = false;
        } else if ("none".equalsIgnoreCase(printingStatistics.getCurrentPlayer().getName())) {
            System.out.printf("%n%s%s", " ".repeat(getEmptySpaces()), "ERROR no current player selected!");
            Thread.sleep(1300);
            toReturn = false;
        }

        return toReturn;
    }

    public void printStatisticsForPlayers(boolean forPrint) throws InterruptedException {
        String valueToCheck = "";

        if (!checksBeforeActionOnStatistics()) return;
        String whatToWrite = (forPrint) ? "games statistics" : "reset statistics";

        int howManyRegPlayers = registeredPlayers.size();
        while (!"back".equalsIgnoreCase(valueToCheck)) {
            System.out.print("\033[H\033[2J");
            System.out.flush();

            printingStatistics.printHeaderSubMenu(whatToWrite, true, 2, 1, 9);

            for (int i = 0; i < howManyRegPlayers; i++) {
                Player player = registeredPlayers.get(i);
                System.out.printf("%n%s%s%s%s%s%s%s%s%s%s", " ".repeat(getEmptySpaces()), "[ ", (i + 1), " ] ", player.getName(),
                        ", Age: ", player.getAge(), ", Occupation: ", player.getOccupation().toLowerCase(),
                        player.getName().equalsIgnoreCase(printingStatistics.getCurrentPlayer().getName()) ? " # current player selected" : "");
            }

            for (int j = (howManyRegPlayers > 1) ? 0 : 1, k = (howManyRegPlayers > 1) ? howManyRegPlayers + 1 : howManyRegPlayers;
                 j < printingStatistics.getOptionsForMenu().size(); j++) {
                System.out.printf("%n%s%s%s%s%s", " ".repeat(getEmptySpaces()), "[ ", (j + k), " ] ", printingStatistics.getOptionsForMenu().get(j));
            }

            printingStatistics.printSeparatedLineBottom();
            printingStatistics.printMessageForSubMenu();

            valueToCheck = actOnPrintingStatsFromMenu(this.readInputFromUser(false), forPrint);
        }
    }

    private void creatingPrintingStatisticsFromMenuModule() throws InterruptedException {
        this.printingStatistics = new SubMenu(getRegisteredPlayers().size() + 3, "please choose an option from above:",
                2, true);
        printingStatistics.setHeaderForGame("playing the game of craps", " + ", "-",
                2, 2, 0, false);
        printingStatistics.addOptionsForMenu("all players, back, quit");
    }

    private void printStatisticsSubMethod() throws InterruptedException {
        int lengthOfSeparator = playGame.getHeaderForGame().getMessageToBeUsed().length();
        String separatorChar = playGame.getHeaderForGame().getSeparatingWithCharacters();

        playGame.getCurrentPlayer().getStatisticsForPlayer().calculateStatisticsForNumberOfGamesPlayedAndPrintThem(lengthOfSeparator, separatorChar);
    }

    public void playTheGames(boolean toAskForNumberOfGames) throws InterruptedException {
        String valueToCheck = "none";
        Craps newGameToBePlayed = new Craps(1, getEmptySpaces(), playGame.getCurrentPlayer());
        int howManyGames = 1;

        if (getRegisteredPlayers().isEmpty() || "None".equals(askHowManyTimesToPlay.getCurrentPlayer().getName())) {
            System.out.printf("%n%s%s", " ".repeat(getEmptySpaces()), "ERROR there are no players registered and/or no current player was set!");
            TimeUnit.SECONDS.sleep(1);
        } else {
            while (!"back".equals(valueToCheck)) {
                System.out.print("\033[H\033[2J");
                System.out.flush();

                if ("none".equals(valueToCheck)) {
                    if (toAskForNumberOfGames) howManyGames = howManyGamesToBePlay();

                    if (howManyGames == -1) {
                        valueToCheck = "back";
                    } else {
                        newGameToBePlayed.setHowManyGameOfCrapsToPlay(howManyGames, false);
                        valueToCheck = String.valueOf(howManyGames);
                    }
                } else {
                    String message = String.format("%s%s%s", "playing ", howManyGames, " games of craps");
                    String messageGamesRunning = String.format("%n%s%s", " ".repeat(getEmptySpaces()), "The Games Are Running");

                    playGame.printHeaderSubMenu(message, true, 2, 1, 4);
                    newGameToBePlayed.playingTheGame();
                    Loading.starting(10, '.', messageGamesRunning, 500, true, "DONE");

                    printStatisticsSubMethod();

                    newGameToBePlayed.getCurrentPlayer().getStatisticsForPlayer().resetCountersForANewInstance();
                    newGameToBePlayed.getCurrentPlayer().getStatisticsForPlayer().addNumberOfInstances();

                    valueToCheck = printOptionsAfterPlayingGamesOfCraps(newGameToBePlayed.getHowManyGameOfCrapsToPlay());

                    if ("none".equalsIgnoreCase(valueToCheck)) toAskForNumberOfGames = true;
                }
            }
        }
    }

    public List<Player> getRegisteredPlayers() {
        return List.copyOf(registeredPlayers);
    }

    public String getValueFromUser() {
        return valueFromUser;
    }

    public void setValueFromUser(String valueFromUser) {
        this.valueFromUser = valueFromUser;
    }

    public Scanner getInput() {
        return input;
    }

    public void setInput(Scanner input) {
        this.input = input;
    }

    public String readInputFromUser(boolean forConstructor) {
        String valueToReturn = getInput().nextLine().trim();

        if (!forConstructor) {
            this.valueFromUser = valueToReturn;
        }

        return valueToReturn;
    }

    public int getEmptySpaces() {
        return emptySpaces;
    }

    public int setEmptySpaces(int emptySpaces, boolean forConstructor) {
        int valueToReturn = Math.max(emptySpaces, 0);

        if (!forConstructor) {
            this.emptySpaces = valueToReturn;
        }

        return valueToReturn;
    }

    public SubMenu getUseHelp() {
        return useHelp;
    }

    public void setUseHelp(SubMenu useHelp) {
        this.useHelp = useHelp;
    }

    public List<SubMenu> getModules() {
        return List.copyOf(modules);
    }

    public String actOnInputFromMenu() throws InterruptedException {
        String toReturn = "";

        switch (valueFromUser) {
            case "1" -> playTheGames(true);
            case "2" -> playTheGames(false);
            case "3" -> printStatisticsForPlayers(true);
            case "4" -> registerPlayers();
            case "5" -> changePlayer();
            case "6" -> printStatisticsForPlayers(false);
            case "7" -> printingPlayers();
            case "8" -> help();
            case "9" -> toReturn = AuxChecks.checkForQuitAndBack("quit", getEmptySpaces(), true);
            default -> {
                System.out.printf("%n%s%s%n", " ".repeat(getEmptySpaces()), "ERROR please use an option from those mentioned above!");
                TimeUnit.SECONDS.sleep(1);
            }
        }

        return toReturn;
    }

    public void addModule(List<SubMenu> unitToBeAdded) {
        this.modules.addAll(unitToBeAdded);
    }
}
