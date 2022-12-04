public class BoardPos {
    private String toStringVal = "0";
    public void SetToStringVal(String val) {
        toStringVal = val;
    }
    @Override
    public String toString() {
        return toStringVal;
    }
}
