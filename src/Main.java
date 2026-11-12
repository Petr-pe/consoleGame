import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

//java -cp .\out\production\project\ Main

public class Main {
    static int durationTime = 600;
    static Scanner scanner = new Scanner(System.in);
    static Random random = new Random();
    static void main() throws InterruptedException {

        Hero hero = new Hero();
        while (true){
            //выбор
            //комната
            //действие
            int choosingSize = 3;
            //создание массива со случайными комнатами
            Rooms[] rooms = new Rooms[choosingSize];
            for (int i = 0; i < choosingSize; i++)
                rooms[i] = Rooms.getRand();

            //вывод данных
            UI.AssemblyChoseScreen(rooms);
            UI.AddHeroStatistic(hero);
            UI.ShowScreen();

            //выбор комнаты
            int chose = safeReadInt(1, choosingSize) - 1;
            switch (rooms[chose]){
                case Enemies -> startBattle(hero);
                case Boss -> startBossBattle(hero);
                case Shop -> enterInShop(hero);
                case Rest -> enterInRest(hero);
            }
        }
    }


    static void startBattle(Hero hero) throws InterruptedException {
        //создание списка до 4 врагов со случайными сататми
        ArrayList<Enemy> enemies = new ArrayList<>();
        int count = random.nextInt(1, 4);
        for (int i = 0; i < count; i++) enemies.add(Enemy.random());

        //вывод данных
        UI.AssemblyFightScreen(enemies);
        UI.AddHeroStatistic(hero);
        UI.ShowScreen();

        //цикл боя
        while(!enemies.isEmpty()){
            //запрос на индекс врага
            int index = safeReadInt(1, enemies.size()) - 1;
            Enemy damageableEnemy = enemies.get(index);

            //ход героя
            HitResult re = hero.hit(damageableEnemy);
            //добавление партикла
            UI.AddStatsChangeParticle("enemy" + index, Stats.Health.name, (re.crit() ? "crit:" : "") + re.damage());

            //удаление врага если умер и выдавание денег
            if(damageableEnemy.health <= 0){
                //добавление денег
                hero.money += damageableEnemy.reward;
                //добавление париткла
                UI.AddStatsChangeParticle("hero", "Money", damageableEnemy.reward);
                enemies.remove(index);
            }

            //вывод данных
            UI.AssemblyFightScreen(enemies);
            UI.AddHeroStatistic(hero);
            UI.ShowScreen();
            Thread.sleep(durationTime);

            //ход врагов
            for (Enemy enemy : enemies) {
                //ход врага
                HitResult rh = enemy.hit(hero);
                //добавление партикла
                UI.AddStatsChangeParticle("hero", Stats.Health.name, rh.dodged() ? "Dodged" : String.valueOf(re.damage()));
                //вывод данных
                UI.AssemblyFightScreen(enemies);
                UI.AddHeroStatistic(hero);
                UI.ShowScreen();
                Thread.sleep(durationTime);
            }
        }
    }
    static void startBossBattle(Hero hero) throws InterruptedException {
        Enemy boss = Enemy.boss();
        // Вывод начальных данных
        UI.AssemblyFightScreen(boss);
        UI.AddHeroStatistic(hero);
        UI.ShowScreen();

        // Цикл боя
        while (boss.health > 0) {
            // Ход героя
            HitResult rb = hero.hit(boss);
            // Добавление партикла изменения HP босса
            UI.AddStatsChangeParticle(
                    "enemy0",
                    Stats.Health.name,
                    (rb.crit() ? "crit:" : "") + rb.damage()
            );
            // Проверка, побеждён ли босс
            if (boss.health <= 0) {
                hero.money += (int)((double)boss.reward * 1.5);
                UI.AddStatsChangeParticle("hero", "Money", (int)((double)boss.reward * 1.5));
                break;
            }
            // Вывод данных
            UI.AssemblyFightScreen(boss);
            UI.AddHeroStatistic(hero);
            UI.ShowScreen();
            Thread.sleep(durationTime);


            // Ход босса
            HitResult rh = boss.hit(hero);
            // Добавление партикла изменения HP героя
            UI.AddStatsChangeParticle(
                    "hero",
                    Stats.Health.name,
                    rh.dodged() ? "Dodged" : String.valueOf(rh.damage())
            );

            // Вывод данных
            UI.AssemblyFightScreen(boss);
            UI.AddHeroStatistic(hero);
            UI.ShowScreen();
            Thread.sleep(durationTime);
        }
    }
    static void enterInShop(Hero hero) throws InterruptedException {
        Stats[] stats = Stats.values();
        Weapons[] weapons = Weapons.values();

        ArrayList<ShopItem> items = new ArrayList<>();
        //заполнение массива
        for (int i = 0; i < 4; i++) {
            int type = random.nextInt(2); //от 0 до 2
            switch (type){
                //если это стат
                case 0 :{
                    Stats stat = stats[random.nextInt(stats.length)];

                    int price, amount;
                    //распределение баланса магазина
                    switch (stat){
                        case Health -> {
                            amount = (int)((double)hero.maxHealth * 0.2);
                            price = amount;
                        }
                        case MaxHealth -> {
                            amount = random.nextInt(10,21);
                            price = (int)((double)amount * 0.5 + 10);
                        }
                        case Damage -> {
                            amount = random.nextInt(1, 6);
                            price = amount * 15;
                        }
                        case CritChance, DodgeChance, CritMultiply ->{
                            amount = 1;
                            price = 20;
                        }
                        default -> {
                            amount = 0;
                            price = 0;
                        }
                    };
                    //добавление стата
                    items.add(new StatItem(amount, price, stat));
                    break;
                }
                //если это оружие
                case 1:{
                    Weapons weapon = weapons[random.nextInt(1, weapons.length)];
                    int price = switch (weapon){
                        case RUSTY_SWORD -> {
                            yield 25;
                        }
                        case HUNTING_BOW -> {
                            yield 40;
                        }
                        case POISON_DAGGER -> {
                            yield 60;
                        }
                        case FLAMING_GREATSWORD -> {
                            yield 100;
                        }
                        case STAFF_OF_VOID -> {
                            yield 120;
                        }
                        default -> 0;
                    };
                    price += random.nextInt(10);
                    items.add(new WeaponItem(weapon, price));
                }
            }
        }
        //выбор и покупка пока есть товары
        while(!items.isEmpty()) {
            //показ
            UI.AssemblyShopScreen(items);
            UI.AddHeroStatistic(hero);
            UI.ShowScreen();
            //запрос на ввод
            int index = safeReadInt(0, items.size()) - 1;
            if(index == -1) break;
            ShopItem item = items.get(index);
            //покупка
            if (hero.money >= items.get(index).getPrice()) {
                //вывод партиклов типа о покупке
                if(item instanceof WeaponItem)
                    UI.AddStatsChangeParticle("hero", "Weapon", ((WeaponItem) item).weapon.getName());
                else if(item instanceof StatItem)
                    UI.AddStatsChangeParticle("hero", ((StatItem) item).stat.name, ((StatItem) item).statAmount);

                UI.AddStatsChangeParticle( "hero", "Money", items.get(index).getPrice() * -1);

                items.get(index).buy(hero);
                items.remove(index);
            }
            else{
                UI.screen.place("Мало Денег", UI.screen.getWidth()/2,UI.screen.getHeight()/2);
                Thread.sleep(durationTime);
            }

        }
    }
    static void enterInRest(Hero hero) throws InterruptedException {
        boolean askAgain = true;
        do {
            UI.screen.clear();
            UI.AddHeroStatistic(hero);
            UI.screen.place("сколько здоровья хочешь излечить? (цена = здоровье * 0.25)", 3, 3);
            UI.ShowScreen();

            String str = scanner.next();
            int amount = switch (str.toLowerCase()) {
                case "max", "full", "all" -> {
                    yield hero.maxHealth - hero.health;
                }
                default -> {
                    yield Math.min(Integer.parseInt(str), hero.maxHealth - hero.health);
                }
            };
            int price = (int) ((double) amount * 0.25);

            UI.screen.clear();
            UI.AddHeroStatistic(hero);
            UI.screen.place("Хочешь излечить " + amount + " хп за " + price + " денег? (y/n)", 3, 3);
            UI.ShowScreen();
            str = scanner.next();
            switch (str.toLowerCase()) {
                case "y", "ye", "yes" -> {
                    if(hero.money >= price){
                        hero.heal(amount);
                        hero.money -= price;
                        UI.AddStatsChangeParticle("hero", Stats.Health.name, "n");
                        UI.AddStatsChangeParticle("hero", "Money", "n");
                        askAgain = false;
                    }
                    else {
                        UI.screen.place("Мало денег", 3, 3);
                        Thread.sleep(durationTime);
                    }
                }
            }
        }while(askAgain);
    }

    static int safeReadInt(int min, int max){
        while(true){
            try{
                int value = scanner.nextInt();
                if (value >= min && value <= max)
                    return value;
                else
                    UI.screen.place("Введи число из диапазона",
                            UI.screen.getWidth() / 2, UI.screen.getHeight() / 2);
            }
            catch (InputMismatchException e){
                UI.screen.place("Введи хотя-бы целое число",
                        UI.screen.getWidth() / 2, UI.screen.getHeight() / 2);
            }
        }
    }

}
