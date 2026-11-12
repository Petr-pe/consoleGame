class Hero {
    int maxHealth = 100;
    int health = maxHealth;
    int basicDamage = 3;
    int money = 10;
    int dodgeChance = 5;
    int critChance = 5;
    int critMultiply = 2;
    Weapons weapon = Weapons.NONE;

    final int maxStatChance = 50;

    HitResult hit(Enemy enemy) {
        int damage = basicDamage + weapon.getDamage();
        boolean crit = Math.random() < (double) this.critChance / 100;
        damage = crit ? damage * critMultiply : damage;
        HitResult res = enemy.takeDamage(damage);
        return new HitResult(res.damage(), res.dodged(), crit);
    }

    HitResult takeDamage(int damage) {
        if (Math.random() < (double) this.dodgeChance / 100)
            return new HitResult(0, true, false);
        health -= damage;
        return new HitResult(damage, false, false);
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
    void heal(int amount){
        health = Math.min(health + amount, maxHealth);
    }

}
