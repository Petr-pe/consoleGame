import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    static Scanner scanner = new Scanner(System.in);
    static void main() throws InterruptedException {

        Hero hero = new Hero();
        while (true){
            //выбор
            //комната
            //действие
            int chosingSize = 3;
            //создание массива со случайными комнатами
            Rooms[] rooms = new Rooms[chosingSize];
            for (int i = 0; i < chosingSize; i++)
                rooms[i] = Rooms.getRand();

            //вывод данных
            UI.AssemblyChoseScreen(rooms);
            UI.ShowScreen();

            //выбор комнаты
            int chose = Math.clamp(scanner.nextInt(), 1, chosingSize + 1) - 1;
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

            //удаление врага если умер
            if(enemies.get(index).health <= 0) enemies.remove(index);

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
        Enemy boss = new Enemy("brnd");
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

    }
    static void enterInRest(Hero hero) throws InterruptedException {

    }



}
