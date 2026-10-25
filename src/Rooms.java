enum Rooms {
    Enemies("Enemies"),
    Boss("Boss"),
    Shop("Shop"),
    Rest("Rest");

    public String typeName;
    Rooms(String name) {typeName = name;}

    //даёт случайную комнату
    static Rooms getRand() {
        Rooms rooms[] = Rooms.values();
        return rooms[(int) (Math.random() * rooms.length)];
    }
}
