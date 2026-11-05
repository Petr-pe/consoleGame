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
            case Health -> hero.heal(statAmount);
            case MaxHealth -> hero.maxHealth += statAmount;
            case CritChance -> hero.critChance = Math.min(statAmount + hero.critChance, hero.maxStatChance);
            case DodgeChance -> hero.dodgeChance = Math.min(statAmount + hero.dodgeChance, hero.maxStatChance);
            case Damage -> hero.basicDamage += statAmount;
            case CritMultiply -> hero.critMultiply += statAmount;
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
        hero.changeWeapon(weapon);
    }
}
