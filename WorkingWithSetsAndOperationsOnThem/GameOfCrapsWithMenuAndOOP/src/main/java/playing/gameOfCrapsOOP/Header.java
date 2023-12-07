package playing.gameOfCrapsOOP;

import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


public class Header {
    private String messageToBeUsed;
    private String additionCharactersInFrontAndBack;
    private String separatingWithCharacters;
    private int emptySpaces;
    private int emptySpacesAbove;
    private int emptySpacesBellow;

    public Header(String messageToBeUsed, String additionCharactersInFrontAndBack, String separatingWithCharacters,
                  int emptySpaces, int emptySpacesAbove, int emptySpacesBellow) throws InterruptedException {
        this.additionCharactersInFrontAndBack = setAdditionCharactersInFrontAndBack(additionCharactersInFrontAndBack, true);
        this.messageToBeUsed = setMessageToBeUsed(messageToBeUsed, true, false);
        this.separatingWithCharacters = setSeparatingWithCharacters(separatingWithCharacters, true);
        this.emptySpaces = setEmptySpaces(emptySpaces, true);
        this.emptySpacesAbove = setEmptySpaces(emptySpacesAbove, true);
        this.emptySpacesBellow = setEmptySpaces(emptySpacesAbove, true);
    }
    public String setMessageToBeUsed(String messageToBeUsed, boolean forConstructor, boolean ifSubMenu) {
        List<String> temp;
        StringBuilder valueToReturn;
        valueToReturn = (!ifSubMenu) ? new StringBuilder("Playing A Nice Game Of Craps And Calculating Statistics") : new StringBuilder();

        if (messageToBeUsed.isBlank()) {
            valueToReturn.append("unknown submenu, please check");

            if (!forConstructor) {
                this.messageToBeUsed = valueToReturn.toString();
            }
            return valueToReturn.toString();
        }

        temp = new ArrayList<>(Arrays.asList(messageToBeUsed.toLowerCase().trim().split(" +")));
        int sizeToUse = temp.size();

        if (sizeToUse < 2) {
            valueToReturn.append("unknown submenu, please check");
        } else {
            String word = "";
            valueToReturn = new StringBuilder();

            if (!ifSubMenu) {
                valueToReturn.append(getAdditionCharactersInFrontAndBack());
            }

            for (int i = 0; i < sizeToUse; i++) {
                word = temp.get(i).trim();
                valueToReturn.append(StringUtils.capitalize(word));

                if (i != (sizeToUse - 1)) {
                    valueToReturn.append(" ");
                }
            }

            if (!ifSubMenu) {
                valueToReturn.append(getAdditionCharactersInFrontAndBack());
            }
        }

        if (!forConstructor) {
            this.messageToBeUsed = valueToReturn.toString();
        }

        return valueToReturn.toString();
    }

    private String validateAuxiliaryChars(String givenValue) throws InterruptedException {
        String valueToReturn = " # ";
        String errorMessage = String.format("%n%s%s%n", " ".repeat(getEmptySpaces()), "ERROR please use a non-alphabetical char for this. It is default to #!");

        if (givenValue.isBlank()) {
            System.out.printf("%s", errorMessage);
            TimeUnit.SECONDS.sleep(1);
        } else {
            valueToReturn = givenValue;
        }

        return valueToReturn;
    }

    public String setAdditionCharactersInFrontAndBack(String additionCharactersInFrontAndBack, boolean forConstructor) throws InterruptedException {
        String valueToReturn = validateAuxiliaryChars(additionCharactersInFrontAndBack);

        if (!forConstructor) {
            this.additionCharactersInFrontAndBack = valueToReturn;
        }

        return valueToReturn;
    }

    public String setSeparatingWithCharacters(String separatingWithCharacters, boolean forConstructor) throws InterruptedException {
        String valueToReturn = validateAuxiliaryChars(separatingWithCharacters);

        if (!forConstructor) {
            this.additionCharactersInFrontAndBack = valueToReturn;
        }

        return valueToReturn;
    }

    public int setEmptySpaces(int emptySpaces, boolean forConstructor) throws InterruptedException {
        int valueToReturn = 2;
        String errorMessage = String.format("%n%s%s%%n", " ".repeat(2), "ERROR please use a value equal or greater to zero. In this case it will default to 2!");

        if (emptySpaces < 0) {
            System.out.printf("%s", errorMessage);
            TimeUnit.SECONDS.sleep(1);
        } else {
            valueToReturn = emptySpaces;
        }

        if (!forConstructor) {
            this.emptySpaces = valueToReturn;
        }

        return valueToReturn;
    }

    public int getEmptySpaces() {
        return emptySpaces;
    }

    public String getAdditionCharactersInFrontAndBack() {
        return additionCharactersInFrontAndBack;
    }

    public String getMessageToBeUsed() {
        return messageToBeUsed;
    }

    public String getSeparatingWithCharacters() {
        return separatingWithCharacters;
    }

    public int getEmptySpacesAbove() {
        return emptySpacesAbove;
    }

    public void setEmptySpacesAbove(int emptySpacesAbove) throws InterruptedException {
        this.emptySpacesAbove = setEmptySpaces(emptySpacesAbove, false);
    }

    public int getEmptySpacesBellow() {
        return emptySpacesBellow;
    }

    public void setEmptySpacesBellow(int emptySpacesBellow) throws InterruptedException {
        this.emptySpacesBellow = setEmptySpaces(emptySpacesBellow, false);
    }

    public void printHeaderWithAllItsTrimmings() throws InterruptedException {
        printHeaderWithAllItsTrimmings(null, false, getEmptySpacesAbove(), getEmptySpacesBellow(), 2);
    }

    public void printHeaderWithAllItsTrimmings(String subMenuMessageToBeUsed, boolean ifSubMenu,
                                               int emptySpacesAbove, int emptySpacesBellow, int emptySpaceSubHeaders) throws InterruptedException {
        int numberOfCharsForLine = getMessageToBeUsed().length();

        if (ifSubMenu) {
            String toAddForHeader = setMessageToBeUsed(subMenuMessageToBeUsed, true, true);

            System.out.printf("%s", String.format("%s%s%s%n%s%s%n%s%s%n%s%s%s", "\n".repeat(emptySpacesAbove), " ".repeat(getEmptySpaces()), getSeparatingWithCharacters().repeat(numberOfCharsForLine),
                    " ".repeat(getEmptySpaces()), getMessageToBeUsed(), " ".repeat(getEmptySpaces()), getSeparatingWithCharacters().repeat(numberOfCharsForLine), " ".repeat(emptySpaceSubHeaders), toAddForHeader,"\n".repeat(emptySpacesBellow)));
        } else {
            System.out.printf("%s", String.format("%s%s%s%n%s%s%n%s%s%s", "\n".repeat(getEmptySpacesAbove()), " ".repeat(getEmptySpaces()),getSeparatingWithCharacters().repeat(numberOfCharsForLine),
                    " ".repeat(getEmptySpaces()), getMessageToBeUsed(), " ".repeat(getEmptySpaces()), getSeparatingWithCharacters().repeat(numberOfCharsForLine),"\n".repeat(getEmptySpacesBellow())));
        }
    }
}
