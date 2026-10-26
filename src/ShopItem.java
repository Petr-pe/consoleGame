import java.util.Random;

public abstract class ShopItem {
    protected int price;
    public ShopItem(int p){
        price = p;
    }
    public int getPrice(){
        return price;
    }
    public abstract void buy(Hero hero);
}

class StatItem extends ShopItem {
    int statAmount;
    Stats stat;
    public StatItem(int amount, int price, Stats stat){
        super(price);
        statAmount = amount;
        this.stat = stat;
    }

    @Override
    public void buy(Hero hero) {
        Random random = new Random();
        if(hero.money < price) return;
        hero.money -= price;
        switch (stat){
            case Health -> {
                statAmount = (int)((double)hero.maxHealth * 0.2);
                hero.heal(statAmount);
            }
            case MaxHealth -> {
                statAmount = random.nextInt(10,20);
                hero.maxHealth += statAmount;
            }
            case CritChance ->{
                statAmount = random.nextInt(3,6);
                hero.critChance += statAmount;
            }
            case DodgeChance ->{
                statAmount = random.nextInt(3,5);
                hero.dodgeChance += statAmount;
            }
            case Damage -> hero.basicDamage += statAmount;
            case CritMultiply -> {
                statAmount = 1;
                hero.critMultiply += statAmount;
            }
        }
    }
}
class WeaponItem extends ShopItem{
    Weapons weapon;
    public WeaponItem(Weapons weapon, int price){
        super(price);
        this.weapon = weapon;
    }
    @Override
    public void buy(Hero hero){
        if(hero.money < price) return;
        hero.money -= price;
        if(hero.weapon.minDamage + hero.weapon.maxDamage <= weapon.minDamage + weapon.maxDamage)
            hero.changeWeapon(weapon);

    }
}
