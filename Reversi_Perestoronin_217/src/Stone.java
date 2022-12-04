/**
 * Класс отвечающий за игровую фишку
 */
public class Stone extends BoardPos {
    /**
     * цвет фишки true - белый, false - черный
     */
    private boolean team;

    /**
     * get аксессор
     * @return поле team, т.е. цвет фишки
     */
    public boolean getTeam() {
        return team;
    }

    /**
     * меняет цвет фишки на противоположный
     */
    public void changeTeam() {
        team = !team;
    }

    /**
     * конструктор фишки
     * @param color цвет фишки
     */
    public Stone(Boolean color) {
        team = color;
    }

    /**
     * конструктор фишки от другой фишки
     * @param other другая фишка
     */
    public Stone(Stone other) {
        this(other.getTeam());
    }

    /**
     * переопределенный метод для перевода в строку
     * @return строка, содержащая цвет W - white (белый), B- black (черный)
     */
    @Override
    public String toString() {
        if (team) {
            return "W";
        }
        return "B";
    }

}
