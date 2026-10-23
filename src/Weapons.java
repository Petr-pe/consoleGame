enum Weapons {
    NONE("Рука", 0, 0),
    RUSTY_SWORD("Ржавый меч", 8, 12),
    HUNTING_BOW("Охотничий лук", 10, 16),
    POISON_DAGGER("Кинжал отравителя", 6, 10),
    FLAMING_GREATSWORD("Пылающий двуручник", 18, 25),
    STAFF_OF_VOID("Посох Бездны", 15, 30);

    private final String name;
    public final int minDamage;
    public final int maxDamage;

    Weapons(String name, int minDamage, int maxDamage) {
        this.name = name;
        this.minDamage = minDamage;
        this.maxDamage = maxDamage;
    }

    public String getName() {
        return this.name;
    }

    public int getDamage() {
        return minDamage + (int) (Math.random() * (maxDamage - minDamage + 1));
    }
}
