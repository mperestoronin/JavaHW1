/**
 * Класс описывающий любую позицию на игровом поле.
 */
public class BoardPos {
    /**
     * Значение, которое будет возвращено при вызове toString
     */
    private String toStringVal = "0";

    /**
     * set аксессор, задает значение строки, которая будет ваозвращена при вызове toString
     * @param val значение строки, которая будет ваозвращена при вызове toString
     */
    public void SetToStringVal(String val) {
        toStringVal = val;
    }

    /**
     * Переопределенный метод toString, определяющий, что будет возвращено при приведении BoardPas к строке
     * @return что будет возвращено при приведении BoardPas к строке (либо 0 либо ? - при отображении возможного хода)
     */
    @Override
    public String toString() {
        return toStringVal;
    }
}
