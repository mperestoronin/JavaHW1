import java.util.Objects;
import java.util.Scanner;

public class Main {
    static Boolean turn = false; // true - белые, false - черные
    static int highscore = 0;
    static Field field = new Field();
    public static void main(String[] args) {
        String playAgain = "1";
        while (playAgain.equals("1")) {
            System.out.println("Добро пожаловать в консольную игру Реверси!\nВыберите режим игры:\n" +
                    "1 - Игра против ИИ\n" +
                    "2 - Игра против другого игрока\n" +
                    "3 - Вывод лучшего результата за сессию\n" +
                    "4 - Данные о программе");
            Scanner in = new Scanner(System.in);
            String input = in.nextLine();
            if (Objects.equals(input, "1")) {
                System.out.println("Выбрана игра против ИИ.");
                PvE();
            } else if (Objects.equals(input, "2")) {
                System.out.println("Выбрана игра против другого игрока");
                PvP();
            } else if (Objects.equals(input, "3")){
                printHighscore();
            }
            else {
                System.out.println("Как работает откат хода?\n" +
                        "Для удобства использования я по-разному реализовал работу системы отмены хода.\n" +
                        "В режиме игрок против игрока, введенное число ходов на которые вы хотите откатиться будет соответствовать количеству отмененных ходов.\n" +
                        "Так, например вы сделали ход, решили пойти назад на 3 хода, ввели число 3. Программа сначала отменит ваш последний ход, затем ход вашего соперника, потом опять ваш ход.\n" +
                        "В режиме игрок против компьютера, число ходов на которые вы хотите откатиться, не будет учитывать ход компьютера т.к. его бессмысленно откатывать. \n" +
                        "Все тот же пример с отменой 3 ходов: Сначала отменяется ваш ход, потом ход компьютера, потом ваш ход, потом ход компьютера, потом ваш ход. Т. е. по факту, мы вернулись на 5 ходов (т.е. отменили 3 ваших хода).\n" +
                        "Мне такая система показалась гораздо удобней и интуитивней, чем та, что была использована в режиме игрок против игрока.\n" +
                        "\n" +
                        "В работе реализован весь требуемый функционал за исключением продвинутого режима.\n");
            }
            System.out.println("Вернуться в главное меню?\n" +
                    "1 - да\n" +
                    "любая другая строка - завершить работу программы");
            playAgain = in.nextLine();
        }
    }

    private static void printHighscore() {
        if (highscore == 0) {
            System.out.println("Вы еще не сыграли ни одной игры за эту сессию, ни одного результата нет!");
        } else {
            System.out.println("Лучший результат: " + highscore);
        }
    }


    public static boolean winCondition() {
        if (Field.getBlackCount() <= 0 || Field.getWhiteCount() <= 0 || (Field.possibleTurns(true).size() == 0 && Field.possibleTurns(false).size() == 0)) {
            return true;
        }
        return false;
    }

    public static void PvP() {
        while (!winCondition()) {
            Field.printPotentialTurns(turn);
            Field.saveField();
            var posTurns = Field.possibleTurns(turn);
            if (posTurns.size() == 0) {
                System.out.println("Нет доступных ходов, игрок пропускает ход.\n");
                turn = !turn;
                continue; //todo проверить
            } else {
                Field.printPossibleTurns(posTurns);
            }
            var coordinates = Field.inputCoordinates(posTurns);
            field.addStone(turn, coordinates[0], coordinates[1]);
            Field.printScore();
            //Field.printField(Field.getField());
            turn = !turn;
            accidentalTurn(true);
        }
        System.out.println("Игра закончилась! Доска на момент конца: ");
        Field.printField(Field.getField());
        if (Field.getBlackCount() > Field.getWhiteCount()) {
            System.out.println("Черные победили!");
            highscore = Field.getBlackCount();
        } else {
            System.out.println("Белые победили!");
            highscore = Field.getWhiteCount();
        }
    }

    public static void accidentalTurn(boolean pvp) {
        System.out.println("Сделали ход случайно? Введите '1', если да. Если нет, то введите любую другую строку.");
        Scanner in = new Scanner(System.in);
        String input = in.nextLine();
        if (Objects.equals(input, "1")) {
            System.out.println("На сколько ходов назад вы хотите вернуться?");
            int turnBack = 1;
            boolean flag = true;
            while (flag) {
                input = in.nextLine();
                while(!tryParseInt(input)) {
                    System.out.println("Вводить можно только числа!");
                    input = in.nextLine();
                }
                turnBack = Integer.parseInt(input);
                if (turnBack <= Field.getHistorySize()) {
                    flag = false;
                } else {
                    System.out.println("На данное количество ходов вернуться невозможно. Введите количество заного!");
                }
            }
            Field.GoBack(turnBack);
            if (pvp) {
                if (turnBack % 2 != 0)
                    turn = !turn;
            } else {
                turn = false;
            }
        }
    }

    public static boolean tryParseInt(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static void PvE() {
        while(!winCondition()) {
            Field.printScore();
            Field.printPotentialTurns(turn);
            Field.saveField();
            var posTurns = Field.possibleTurns(turn);
            if (posTurns.size() != 0) {
                Field.printPossibleTurns(posTurns);
                var coordinates = Field.inputCoordinates(posTurns);
                field.addStone(turn, coordinates[0], coordinates[1]);
                System.out.println("Вы сходили так:");
                Field.printField(Field.getField());
                Field.printScore();
            } else {
                System.out.println("Нет доступных ходов, вы пропускаете ход.\n");
            }
            turn = !turn;
            ComputerTurn();
            turn = !turn;
            if (posTurns.size() != 0) {
                accidentalTurn(false);
            }
        }
        System.out.println("Игра закончилась! Доска на момент конца: ");
        Field.printField(Field.getField());
        if (Field.getBlackCount() > Field.getWhiteCount()) {
            System.out.println("Черные победили!");
            highscore = Field.getBlackCount();
        } else {
            System.out.println("Белые победили!");
            highscore = Field.getWhiteCount();
        }
    }

    public static void ComputerTurn() {
        var posTurns = Field.possibleTurns(turn);
        if (posTurns.size() == 0) {
            System.out.println("У компьютера нет допустимых ходов, он пропускает ход!");
            return;
        }
        double bestTurnSum = 0;
        int x = posTurns.get(0)[0];
        int y = posTurns.get(0)[1];
        for (int i = 0; i < posTurns.size(); i++) {
            double sum = 0;
            sum += Field.evaluateTurn(posTurns.get(i)[0], posTurns.get(i)[1]);
            sum += Field.turnScore(posTurns.get(i)[0], posTurns.get(i)[1], turn);
            if (sum >= bestTurnSum) {
                x = posTurns.get(i)[0];
                y = posTurns.get(i)[1];
                bestTurnSum = sum;
            }
        }
        field.addStone(turn, x,y);
    }

}