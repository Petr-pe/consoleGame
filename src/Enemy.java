class Enemy {
    int health = 100;
    int damage = 10;
    final int reward = (health + damage * 3) / 10;

    Enemy(String str) {
        if (str.equals("rnd")) {
            health = (int) (Math.random() * 15);
            health = Math.clamp(health, 10, 15);
            damage = (int) (Math.random() * 4) + 1;
        }
        else if (str.equals("bRnd")) {
            health = (int) (Math.random() * 200);
            health = Math.clamp(health, 130, 200);
            damage = (int) (Math.random() * 14) + 1;
        }
    }

    void hit(Hero hero) throws InterruptedException {
        hero.takeDamage(damage);
    }

    void takeDamage(int damage) {
        this.health -= damage;
    }

}
