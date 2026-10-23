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
        int count = (int)(Math.random() * 4);
        for (int i = 0; i < count; i++) enemies.add(new Enemy("rnd"));

        //цикл боя
        while(!enemies.isEmpty()){
            //вывод данных
            UI.AssemblyFightScreen(enemies, hero);
            UI.ShowScreen();

            //запрос на индекс врага
            int index = Math.clamp(scanner.nextInt(), 1, enemies.size() + 1);
            Enemy damageableEnemy = enemies.get(index - 1);

            //ход героя
            hero.hit(damageableEnemy);
            Thread.sleep(1000);

            //вывод данных
            UI.AssemblyFightScreen(enemies, hero);
            UI.ShowScreen();

            //удаление если умер
            if(enemies.get(index - 1).health <= 0) enemies.remove(index - 1);

            //вывод данных
            UI.AssemblyFightScreen(enemies, hero);
            UI.ShowScreen();

            //ход врагов
            for (Enemy enemy : enemies) {
                enemy.hit(hero);
                Thread.sleep(1000);

                //вывод данных
                UI.AssemblyFightScreen(enemies, hero);
                UI.ShowScreen();
            }
        }
    }
    static void startBossBattle(Hero hero) throws InterruptedException {
        Enemy boss = new Enemy("brnd");
        while (boss.health >= 0){
            hero.hit(boss);
            Thread.sleep(1000);

            //вывод данных
            UI.AssemblyFightScreen(boss, hero);
            UI.ShowScreen();

            boss.hit(hero);
            Thread.sleep(1000);

            //вывод данных
            UI.AssemblyFightScreen(boss, hero);
            UI.ShowScreen();
        }
    }
    static void enterInShop(Hero hero) throws InterruptedException {

    }
    static void enterInRest(Hero hero) throws InterruptedException {

    }



}
