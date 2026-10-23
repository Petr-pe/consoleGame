class Enemy {
    int health = 100;
    int damage = 10;
    final int reward = (health + damage * 3) / 10;

    Enemy(String str) {
        if (str.equals("rnd")) {
            health = (int) (Math.random() * 100);
            health = Math.clamp(health, 30, 70);
            damage = (int) (Math.random() * 10);
        }
        else if (str.equals("brnd")) {
            health = (int) (Math.random() * 200);
            health = Math.clamp(health, 130, 200);
            damage = (int) (Math.random() * 20);
        }
    }

    void hit(Hero hero) throws InterruptedException {
        hero.takeDamage(damage);
    }

    void takeDamage(int damage) {
        this.health -= damage;
    }

}
