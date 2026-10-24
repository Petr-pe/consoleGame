class Hero {
    int maxHealth = 100;
    int health = maxHealth;
    int basicDamage = 3;
    int money = 10;
    int dodgeChance = 5;
    int critChance = 5;
    int critMultiply = 2;
    Weapons weapon = Weapons.NONE;

    void hit(Enemy enemy) {
        int damage = basicDamage + weapon.getDamage();
        damage = (Math.random() < (double) this.critChance / 100) ? damage * critMultiply : damage;
        enemy.takeDamage(damage);
    }

    void takeDamage(int damage) throws InterruptedException {
        if (Math.random() < (double) this.dodgeChance / 100)
            dodge();
        else
            health -= damage;

        if (health <= 0)
            onDeath();
    }

    void dodge() {
        // что-то делает

    }

    void changeWeapon(Weapons weapon) {
        this.weapon = weapon;
    }

    void onDeath() throws InterruptedException {
        Thread.sleep(1000);

        //вызов окна...
        //но пока что это
        System.exit(0);
    }

}
