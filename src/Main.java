import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Main {
    static Scanner scanner = new Scanner(System.in);
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
            int chose = Math.clamp(scanner.nextInt(), 1, choosingSize) - 1;
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
        int count = (int)(Math.random() * 4) + 1;//+1 тк может получиться 0
        for (int i = 0; i < count; i++) enemies.add(new Enemy("rnd"));

        //вывод данных
        UI.AssemblyFightScreen(enemies);
        UI.AddHeroStatistic(hero);
        UI.ShowScreen();

        //цикл боя
        while(!enemies.isEmpty()){
            //запрос на индекс врага
            int index = Math.clamp(scanner.nextInt(), 1, enemies.size()) - 1;
            Enemy damageableEnemy = enemies.get(index);

            //сохранение значения хп перед получением урона
            int lastEnemyHp = damageableEnemy.health;
            //ход героя
            hero.hit(damageableEnemy);
            //добавление партикла
            UI.AddStatsChangeParticle("enemy" + index, "HP",-(lastEnemyHp - damageableEnemy.health));

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
            Thread.sleep(1000);

            //ход врагов
            for (Enemy enemy : enemies) {
                //сохранение хп героя перед получением урона
                int lastHeroHp = hero.health;
                //ход врага
                enemy.hit(hero);
                //добавление партикла
                UI.AddStatsChangeParticle("hero", "HP", -(lastHeroHp - hero.health));
                //вывод данных
                UI.AssemblyFightScreen(enemies);
                UI.AddHeroStatistic(hero);
                UI.ShowScreen();
                Thread.sleep(1000);
            }
        }
    }
    static void startBossBattle(Hero hero) throws InterruptedException {
        Enemy boss = new Enemy("bRnd");
        // Вывод начальных данных
        UI.AssemblyFightScreen(boss);
        UI.AddHeroStatistic(hero);
        UI.ShowScreen();

        // Цикл боя
        while (boss.health > 0) {
            // Сохранение значения HP босса перед получением урона
            int lastBossHp = boss.health;
            // Ход героя
            hero.hit(boss);
            // Добавление партикла изменения HP босса
            UI.AddStatsChangeParticle(
                    "enemy0",
                    "HP",
                    -(lastBossHp - boss.health)
            );
            // Вывод данных
            UI.AssemblyFightScreen(boss);
            UI.AddHeroStatistic(hero);
            UI.ShowScreen();
            Thread.sleep(1000);

            // Проверка, побеждён ли босс
            if (boss.health <= 0) {
                hero.money += (int)((double)boss.reward * 1.5);
                UI.AddStatsChangeParticle("hero", "Money", (int)((double)boss.reward * 1.5));
                break;
            }

            // Сохранение значения HP героя перед получением урона
            int lastHeroHp = hero.health;
            // Ход босса
            boss.hit(hero);
            // Добавление партикла изменения HP героя
            UI.AddStatsChangeParticle(
                    "hero",
                    "HP",
                    -(lastHeroHp - hero.health)
            );

            // Вывод данных
            UI.AssemblyFightScreen(boss);
            UI.AddHeroStatistic(hero);
            UI.ShowScreen();
            Thread.sleep(1000);
        }
    }
    static void enterInShop(Hero hero) throws InterruptedException {
        Stats[] stats = Stats.values();
        Weapons[] weapons = Weapons.values();
        Random random = new Random();

        ArrayList<ShopItem> items = new ArrayList<>();
        //заполнение массива
        for (int i = 0; i < 4; i++) {
            int type = random.nextInt(2); //от 0 до 2
            switch (type){
                case 0 :{
                    Stats stat = stats[random.nextInt(2, stats.length) - 2];
                    int amount = random.nextInt(5, 10);
                    int price = random.nextInt(10, 15);
                    items.add(new StatItem(amount, price, stat));
                    break;
                }
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
            UI.ShowScreen();

            int index = scanner.nextInt();
            if(index == 0) break;
            index = Math.clamp(index, 1, items.size()) - 1;
            //покупка
            if (hero.money < items.get(index).getPrice()) {
                System.out.println("Недостаточно денег!");
                continue;
            }
            items.get(index).buy(hero);
            items.remove(index);
        }
    }
    static void enterInRest(Hero hero) throws InterruptedException {

    }



}
