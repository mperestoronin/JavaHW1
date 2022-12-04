public class Stone extends BoardPos {
    private boolean team;
    public boolean getTeam() {
        return team;
    }
    public void changeTeam() {
        team = !team;
    }
    public Stone(Boolean color) {
        team = color;
    }
    public Stone(Stone other) {
        this(other.getTeam());
    }

    @Override
    public String toString() {
        if (team) {
            return "W";
        }
        return "B";
    }

}
