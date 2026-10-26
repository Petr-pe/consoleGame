import java.util.Random;

class Enemy {
    int health, damage, reward;

    Enemy(int health, int damage) {
        this.health = health;
        this.damage = damage;
        reward = (health + damage * 4) / 2;
    }
    static Enemy random(int minHealth, int maxHealth, int minDamage, int maxDamage){
        Random random = new Random();
        int hp = random.nextInt(minHealth, maxHealth);
        int dmg = random.nextInt(minDamage, maxDamage);
        return new Enemy(hp, dmg);
    }
    static Enemy random(int maxHealth, int maxDamage){
        return random(1,maxHealth,1,maxDamage);
    }
    static Enemy random(){
        return random(10,20,1,5);
    }
    static Enemy boss(){
        return random(50, 70, 3, 8);
    }

    void hit(Hero hero) throws InterruptedException {
        hero.takeDamage(damage);
    }
    void takeDamage(int damage) {
        this.health -= damage;
    }
}
