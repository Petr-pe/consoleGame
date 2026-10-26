public enum Stats {
    Health("Health"),
    Damage("Damage"),
    CritChance("Crit Chance"),
    CritMultiply("Crit Multiply"),
    DodgeChance("Dodge Chance"),
    MaxHealth("Max Health"),
    MinDamage("Min Damage"),
    MaxDamage("Max Damage");
    private final String string;
    Stats(String str){
        string = str;
    }
}

