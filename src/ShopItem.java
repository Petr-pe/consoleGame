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
        if(hero.money < price) return;
        hero.money -= price;
        switch (stat){
            case Health -> hero.health = Math.min(hero.health + statAmount, hero.maxHealth);
            case MaxHealth -> hero.maxHealth += statAmount;
            case CritChance -> hero.critChance += statAmount;
            case DodgeChance -> hero.dodgeChance += statAmount;
            case Damage -> hero.basicDamage += statAmount;
            case CritMultiply -> hero.critMultiply++;
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
