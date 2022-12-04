import java.util.ArrayList;

public class Field {
    private static BoardPos[][] field = new BoardPos[8][8];

    public static BoardPos[][] getField() {
        return field;
    }

    private static ArrayList<BoardPos[][]> history = new ArrayList<>();
    private static int whiteCount = 2;
    private static int blackCount = 2;

    public static int getBlackCount() {
        return blackCount;
    }

    public static int getWhiteCount() {
        return whiteCount;
    }

    public static int getHistorySize() {
        return history.size();
    }

    public Field() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                field[i][j] = new BoardPos();
            }
        }
        field[3][3] = new Stone(true);
        field[3][4] = new Stone(false);
        field[4][4] = new Stone(true);
        field[4][3] = new Stone(false);
    }

    public static void printField(BoardPos[][] field) {
        System.out.print("x->");
        for (int i = 0; i < 8; i++) {
            System.out.print(i + " ");
        }
        System.out.print("\n");
        for (int i = 0; i < 8; i++) {
            System.out.print(i + "| ");
            for (int j = 0; j < 8; j++) {
                System.out.print(field[i][j] + " ");
            }
            System.out.print("\n");
        }
    }

    public static void printPotentialTurns(boolean turn) {
        var matrix = copyField();
        var potenTurns = possibleTurns(turn);
        for (int[] potenTurn : potenTurns) {
            matrix[potenTurn[1]][potenTurn[0]].SetToStringVal("?");
        }
        printField(matrix);
    }

    public static void printScore() {
        System.out.println("Черные: " + blackCount + " Белые: " + whiteCount);
    }

    private static int[] getScore() {
        int[] score = new int[2];
        int whiteAmount = 0;
        int blackAmount = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (field[i][j] instanceof Stone stone) {
                    if (stone.getTeam())
                        whiteAmount++;
                    else
                        blackAmount++;
                }
            }
        }
        score[0] = whiteAmount;
        score[1] = blackAmount;
        return score;
    }

    public static void saveField() {
        var arr = copyField();
        history.add(arr);
    }

    private static BoardPos[][] copyField() {
        BoardPos[][] arr = new BoardPos[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (field[i][j] instanceof Stone stone) {
                    arr[i][j] = new Stone(stone);
                } else {
                    arr[i][j] = new BoardPos();
                }
            }
        }
        return arr;
    }

    public static void GoBack(int key) {
        field = history.get(history.size() - key);
        var score = getScore();
        whiteCount = score[0];
        blackCount = score[1];
        while (key != 0) {
            history.remove(history.size() - 1);
            key--;
        }
    }

    public static int[] inputCoordinates(ArrayList<int[]> turns) {
        boolean flag = true;
        int x = 0, y = 0;
        while (flag) {
            System.out.println("Введите x координату камня, который желаете поставить. Нумерация с нуля.");
            x = Main.inputInt();
            System.out.println("Введите y координату камня, который желаете поставить. Нумерация с нуля.");
            y = Main.inputInt();
            if (x > 7 || y > 7 || x < 0 || y < 0 || field[y][x] instanceof Stone || !checkCoordinates(x, y, turns)) {
                System.out.println("На этой позиции уже стоит другой камень или вы указали некорретные координаты! Введите другие координаты.");
            } else {
                flag = false;
            }
        }
        return new int[]{x, y};
    }

    public static boolean checkCoordinates(int x, int y, ArrayList<int[]> turns) {
        for (int[] turn : turns) {
            if (turn[0] == x && turn[1] == y) {
                return true;
            }
        }
        return false;
    }

    public void addStone(Boolean team, int x, int y) {
        Stone stone = new Stone(team);
        field[y][x] = stone;
        verifyBoard(stone, y, x);
        System.out.println("Камень успешно добавлен.");
        if (team)
            whiteCount++;
        else
            blackCount++;
    }

    private static void keepScore(int a, int b) {
        ((Stone) field[a][b]).changeTeam();
        if (((Stone) field[a][b]).getTeam()) {
            whiteCount++;
            blackCount--;
        } else {
            blackCount++;
            whiteCount--;
        }
    }

    public static void verifyBoard(Stone stone, int y, int x) {
        for (int i = y + 1; i < 8; i++) {
            if (field[i][x] instanceof Stone stone1) {
                if (stone1.getTeam() == stone.getTeam()) {
                    for (int j = y + 1; j < i; j++) {
                        keepScore(j, x);
                    }
                    break;
                }
            } else {
                break;
            }
        }
        for (int i = y - 1; i > -1; i--) {
            if (field[i][x] instanceof Stone stone1) {
                if (stone1.getTeam() == stone.getTeam()) {
                    for (int j = y - 1; j > i; j--) {
                        keepScore(j, x);
                    }
                    break;
                }
            } else {
                break;
            }
        }
        for (int i = x + 1; i < 8; i++) {
            if (field[y][i] instanceof Stone stone1) {
                if (stone1.getTeam() == stone.getTeam()) {
                    for (int j = x + 1; j < i; j++) {
                        keepScore(y, j);
                    }
                    break;
                }
            } else {
                break;
            }
        }
        for (int i = x - 1; i > -1; i--) {
            if (field[y][i] instanceof Stone stone1) {
                if (stone1.getTeam() == stone.getTeam()) {
                    for (int j = x - 1; j > i; j--) {
                        keepScore(y, j);
                    }
                    break;
                }
            } else {
                break;
            }
        }

        int k = y - 1;
        for (int i = x + 1; i < 8 && k > -1; i++) {
            if (field[k][i] instanceof Stone stone1) {
                if (stone1.getTeam() == stone.getTeam()) {
                    int l = y - 1;
                    for (int j = x + 1; j < i && l > k; j++) {
                        keepScore(l, j);
                        l--;
                    }
                    break;
                }
            } else {
                break;
            }
            k--;
        }

        k = y + 1;
        for (int i = x - 1; k < 8 && i > -1; i--) {
            if (field[k][i] instanceof Stone stone1) {
                if (stone1.getTeam() == stone.getTeam()) {
                    int l = y + 1;
                    for (int j = x - 1; j > i && l < k; j--) {
                        keepScore(l, j);
                        l++;
                    }
                    break;
                }
            } else {
                break;
            }
            k++;
        }

        k = y - 1;
        for (int i = x - 1; i > -1 && k > -1; i--) {
            if (field[k][i] instanceof Stone stone1) {
                if (stone1.getTeam() == stone.getTeam()) {
                    int l = y - 1;
                    for (int j = x - 1; j > i && l > k; j--) {
                        keepScore(l, j);
                        l--;
                    }
                    break;
                }
            } else {
                break;
            }
            k--;
        }

        k = y + 1;
        for (int i = x + 1; i < 8 && k < 8; i++) {
            if (field[k][i] instanceof Stone stone1) {
                if (stone1.getTeam() == stone.getTeam()) {
                    int l = y + 1;
                    for (int j = x + 1; j < i && l < k; j++) {
                        keepScore(l, j);
                        l++;
                    }
                    break;
                }
            } else {
                break;
            }
            k++;
        }

    }

    private static void uniqueAdd(ArrayList<int[]> turns, int[] arr) {
        boolean flag = true;
        for (int[] turn : turns) {
            if (turn[0] == arr[0] && turn[1] == arr[1]) {
                flag = false;
                break;
            }
        }
        if (flag) {
            turns.add(arr);
        }
    }

    public static ArrayList<int[]> possibleTurns(boolean turn) {
        ArrayList<int[]> turns = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (field[i][j] instanceof Stone stone && stone.getTeam() != turn) {
                    int up = i - 1;
                    int down = i + 1;
                    int left = j - 1;
                    int right = j + 1;
                    if (up > -1 && !(field[up][j] instanceof Stone)) {
                        int[] arr = {j, up};
                        uniqueAdd(turns, arr);
                    }
                    if (down < 8 && !(field[down][j] instanceof Stone)) {
                        int[] arr = {j, down};
                        uniqueAdd(turns, arr);
                    }
                    if (left > -1) {
                        if (!(field[i][left] instanceof Stone)) {
                            int[] arr = {left, i};
                            uniqueAdd(turns, arr);
                        }
                        if (up > -1 && !(field[up][left] instanceof Stone)) {
                            int[] arr2 = {left, up};
                            uniqueAdd(turns, arr2);
                        }
                        if (down < 8 && !(field[down][left] instanceof Stone)) {
                            int[] arr2 = {left, down};
                            uniqueAdd(turns, arr2);
                        }
                    }
                    if (right < 8 ) {
                        if (!(field[i][right] instanceof Stone)) {
                            int[] arr = {right, i};
                            uniqueAdd(turns, arr);
                        }
                        if (up > -1 && !(field[up][right] instanceof Stone)) {
                            int[] arr2 = {right, up};
                            uniqueAdd(turns, arr2);
                        }
                        if (down < 8 && !(field[down][right] instanceof Stone)) {
                            int[] arr2 = {right, down};
                            uniqueAdd(turns, arr2);
                        }
                    }
                }
            }
        }
        turns = checkIfCloses(turns, turn);
        return turns;
    }

    private static ArrayList<int[]> checkIfCloses(ArrayList<int[]> turns, boolean turn) {
        ArrayList<int[]> correctTurns = new ArrayList<>();
        for (int[] ints : turns) {
            boolean flag = false;
            int x = ints[0];
            int y = ints[1];
            for (int i = y + 1; i < 8; i++) {
                if (field[i][x] instanceof Stone stone1) {
                    if (stone1.getTeam() == turn) {
                        if (i > y + 1) {
                            correctTurns.add(new int[]{x, y});
                            flag = true;
                        }
                        break;
                    }
                } else {
                    break;
                }
            }
            if (flag) {
                continue;
            }
            for (int i = y - 1; i > -1; i--) {
                if (field[i][x] instanceof Stone stone1) {
                    if (stone1.getTeam() == turn) {
                        if (i < y - 1) {
                            correctTurns.add(new int[]{x, y});
                            flag = true;
                        }
                        break;
                    }
                } else {
                    break;
                }
            }
            if (flag) {
                continue;
            }
            for (int i = x + 1; i < 8; i++) {
                if (field[y][i] instanceof Stone stone1) {
                    if (stone1.getTeam() == turn) {
                        if (i > x + 1) {
                            correctTurns.add(new int[]{x, y});
                            flag = true;
                        }
                        break;
                    }
                } else {
                    break;
                }
            }
            if (flag) {
                continue;
            }
            for (int i = x - 1; i > -1; i--) {
                if (field[y][i] instanceof Stone stone1) {
                    if (stone1.getTeam() == turn) {
                        if (i < x - 1) {
                            correctTurns.add(new int[]{x, y});
                            flag = true;
                        }
                        break;
                    }
                } else {
                    break;
                }
            }
            if (flag) {
                continue;
            }
            int h = y - 1;
            for (int i = x + 1; i < 8 && h > -1; i++) {
                if (field[h][i] instanceof Stone stone1) {
                    if (stone1.getTeam() == turn) {
                        if (i > x + 1) {
                            correctTurns.add(new int[]{x, y});
                            flag = true;
                        }
                        break;
                    }
                } else {
                    break;
                }
                h--;
            }
            if (flag) {
                continue;
            }

            h = y + 1;
            for (int i = x - 1; h < 8 && i > -1; i--) {
                if (field[h][i] instanceof Stone stone1) {
                    if (stone1.getTeam() == turn) {
                        if (i < x - 1) {
                            correctTurns.add(new int[]{x, y});
                            flag = true;
                        }
                        break;
                    }
                } else {
                    break;
                }
                h++;
            }
            if (flag) {
                continue;
            }

            h = y - 1;
            for (int i = x - 1; i > -1 && h > -1; i--) {
                if (field[h][i] instanceof Stone stone1) {
                    if (stone1.getTeam() == turn) {
                        if (i < x - 1) {
                            correctTurns.add(new int[]{x, y});
                            flag = true;
                        }
                        break;
                    }
                } else {
                    break;
                }
                h--;
            }
            if (flag) {
                continue;
            }

            h = y + 1;
            for (int i = x + 1; i < 8 && h < 8; i++) {
                if (field[h][i] instanceof Stone stone1) {
                    if (stone1.getTeam() == turn) {
                        if (i > x + 1) {
                            correctTurns.add(new int[]{x, y});
                        }
                        break;
                    }
                } else {
                    break;
                }
                h++;
            }
        }
        return correctTurns;
    }

    public static void printPossibleTurns(ArrayList<int[]> turns) {
        System.out.println("Всего доступно ходов: " + turns.size());
        System.out.println("Потенциальные ходы:");
        for (var turn : turns) {
            System.out.println(turn[0] + " - " + turn[1]);
        }
    }

    public static double evaluateTurn(int x, int y) {
        if ((x == 0 && y == 0) || (x == 7 && y == 0) || (x == 0 && y == 7) || (x == 7 && y == 7))
            return 0.8;
        if (x == 0 || y == 0 || x == 7 || y == 7)
            return 0.4;
        else
            return 0;
    }

    private static int evaluateStone(int y, int x) {
        if (x == 0 || y == 0 || x == 7 || y == 7)
            return 2;
        return 1;
    }

    public static int turnScore(int x, int y, Boolean turn) {
        int sum = 0;
        for (int i = y + 1; i < 8; i++) {
            if (field[i][x] instanceof Stone stone1) {
                if (stone1.getTeam() == turn) {
                    for (int j = y + 1; j < i; j++) {
                        sum += evaluateStone(j,x);
                    }
                    break;
                }
            } else {
                break;
            }
        }
        for (int i = y - 1; i > -1; i--) {
            if (field[i][x] instanceof Stone stone1) {
                if (stone1.getTeam() == turn) {
                    for (int j = y - 1; j > i; j--) {
                        sum += evaluateStone(j,x);
                    }
                    break;
                }
            } else {
                break;
            }
        }
        for (int i = x + 1; i < 8; i++) {
            if (field[y][i] instanceof Stone stone1) {
                if (stone1.getTeam() == turn) {
                    for (int j = x + 1; j < i; j++) {
                        sum += evaluateStone(y,j);
                    }
                    break;
                }
            } else {
                break;
            }
        }
        for (int i = x - 1; i > -1; i--) {
            if (field[y][i] instanceof Stone stone1) {
                if (stone1.getTeam() == turn) {
                    for (int j = x - 1; j > i; j--) {
                        sum += evaluateStone(y,j);
                    }
                    break;
                }
            } else {
                break;
            }
        }

        int k = y - 1;
        for (int i = x + 1; i < 8 && k > -1; i++) {
            if (field[k][i] instanceof Stone stone1) {
                if (stone1.getTeam() == turn) {
                    int l = y - 1;
                    for (int j = x + 1; j < i && l > k; j++) {
                        sum += evaluateStone(l,j);
                        l--;
                    }
                    break;
                }
            } else {
                break;
            }
            k--;
        }

        k = y + 1;
        for (int i = x - 1; k < 8 && i > -1; i--) {
            if (field[k][i] instanceof Stone stone1) {
                if (stone1.getTeam() == turn) {
                    int l = y + 1;
                    for (int j = x - 1; j > i && l < k; j--) {
                        sum += evaluateStone(l,j);
                        l++;
                    }
                    break;
                }
            } else {
                break;
            }
            k++;
        }

        k = y - 1;
        for (int i = x - 1; i > -1 && k > -1; i--) {
            if (field[k][i] instanceof Stone stone1) {
                if (stone1.getTeam() == turn) {
                    int l = y - 1;
                    for (int j = x - 1; j > i && l > k; j--) {
                        sum += evaluateStone(l,j);
                        l--;
                    }
                    break;
                }
            } else {
                break;
            }
            k--;
        }

        k = y + 1;
        for (int i = x + 1; i < 8 && k < 8; i++) {
            if (field[k][i] instanceof Stone stone1) {
                if (stone1.getTeam() == turn) {
                    int l = y + 1;
                    for (int j = x + 1; j < i && l < k; j++) {
                        sum += evaluateStone(l,j);
                        l++;
                    }
                    break;
                }
            } else {
                break;
            }
            k++;
        }
        return sum;
    }
}







