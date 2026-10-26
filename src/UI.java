import java.util.ArrayList;
import java.util.Map;

public class UI {
    //поля
    static Screen screen = new Screen(100, 25);

    static Point heroStats = new Point(0, screen.getHeight() - 10);
    //точка откуда начинается отображение списков врагов, предметов магазина, выбора дверей
    static Point listPoint = new Point(3, 3);
    static ArrayList<Particle> particles = new ArrayList<>();
    static ArrayList<Unit> units = new ArrayList<>();


    //методы
    //Вычисляет координаты конца объекта
    static Point EndOfBox(Point point, String textBox){
        return new Point(point.x + GetTextWidth(textBox), point.y);
    }
    //даёт количество символов в самой большой подстроке
    static int GetTextWidth(String s) {
        int mx = 0;
        for (String line : s.split("\n"))
            mx = Math.max(mx, line.length());
        return mx;
    }
    //находит unit в списке по имени
    static private Unit FindUnit(String name){
        for (var u : units)
            if(u.name.equals(name))
                return u;
        return null;
    }
    // добавляет unit на экран и в список
    static private void AddUnit(Unit unit){
        screen.place(unit);
        units.add(unit);
    }

    //собирают экран
    static void AssemblyFightScreen(ArrayList<Enemy> enemies) {
        NewFrame();
        screen.place("Выбери индекс врага, чтобы попытаться побить его", 1,1);

        int ind = 0;
        Point p = new Point(listPoint);
        for (Enemy enemy : enemies) {
            String boxText = TextTemplate.EnemyBox.getUpdatedInfo(Map.of(
                    "ind", ind + 1,
                    "hp", enemy.health,
                    "dmg", enemy.damage)
            );
            AddUnit(new Unit("enemy" + ind, boxText, p));
            p = EndOfBox(p, boxText); p.x++;
            ind++;
        }
    }
    static void AssemblyFightScreen(Enemy enemy) {
        NewFrame();

        String box = TextTemplate.EnemyBox.getUpdatedInfo(Map.of(
                "ind", 1,
                "hp", enemy.health,
                "dmg", enemy.damage)
        );
        AddUnit(new Unit("enemy0", box, listPoint));
    }
    static void AssemblyChoseScreen(Rooms[] rooms) {
        NewFrame();
        screen.place("Выбери индекс двери, в которую хочешь войти (работает Boss, Enemies, Shop)", 1,1);

        int ind = 0;
        Point p = new Point(listPoint);
        for (Rooms room : rooms) {
            String boxText = TextTemplate.DoorBox.getUpdatedInfo(Map.of(
                    "ind", ind + 1,
                    "type", room.typeName)
            );
            AddUnit(new Unit("door" + ind, boxText, p));
            p = EndOfBox(p, boxText); p.x++;
            ind++;
        }
    }
    static void AssemblyShopScreen(ArrayList<ShopItem> items){
        NewFrame();
        screen.place("Выберите индекс товара, который хочешь купить.\n" +
                "Если не хочешь или не можешь потратиться нажми 0 и выйди с магазина", 1,1);

        int ind = 1;
        Point p = new Point(listPoint);
        for (ShopItem item : items) {
            String boxText = " ";
            if(item instanceof StatItem){
                boxText = TextTemplate.ShopStatItemBox.getUpdatedInfo(Map.of(
                        "ind", ind,
                        "price", item.price,
                        "amount", ((StatItem)item).statAmount,
                        "stat", ((StatItem) item).stat
                ));
            }
            else if(item instanceof WeaponItem){
                boxText = TextTemplate.ShopWeaponItemBox.getUpdatedInfo(Map.of(
                        "ind", ind,
                        "price", item.price,
                        "weapon", ((WeaponItem) item).weapon.getName(),
                        "minDmg", ((WeaponItem) item).weapon.minDamage,
                        "maxDmg", ((WeaponItem) item).weapon.maxDamage
                ));
            }
            AddUnit(new Unit("item" + ind, boxText, p));
            p = EndOfBox(p, boxText); p.x++;
            ind++;
        }

    }
    static void AssemblyRestScreen(){

    }

    //формирует и добавляет партиклы в список
    static private void AddStatParticleToList(int val, Point pos){
        String text = TextTemplate.StatsParticle.getUpdatedInfo(Map.of(
                "sign", val > 0 ? '+' : '-',
                "amo", Math.abs(val)
        ));
        particles.add(new Particle(text, pos));
    }
    //высчитывает куда ставить партикл
    static void AddStatsChangeParticle(String unitName, String stat, int change, Point offset) {
        Unit unit = FindUnit(unitName); if(unit == null) return;
        Point particlePoint = unit.lineEnd(stat).plus(offset);
        AddStatParticleToList(change, particlePoint);
    }
    static void AddStatsChangeParticle(String unitName, String stat, int change) {
        AddStatsChangeParticle(unitName, stat, change, new Point(2,0));
    }

        //выводит статистику героя на экран
    static void AddHeroStatistic(Hero hero){
        String stats = TextTemplate.HeroBox.getUpdatedInfo(Map.of(
                "money", hero.money,
                "mhp", hero.maxHealth,
                "hp", hero.health,
                "dmg", hero.basicDamage,
                "weapon", (hero.weapon.getName() + ' ' + hero.weapon.minDamage + '-' + hero.weapon.maxDamage),
                "critch", hero.critChance,
                "dodgech", hero.dodgeChance)
        );
        AddUnit(new Unit("hero", stats, heroStats));
    }

    //очищает все списки
    static void NewFrame(){
        units.clear();
        screen.clear();
    }
    //показывает экран и выводит партиклы
    static void ShowScreen(){
        //вывод партиклов
        for (var p : particles)
            screen.place(p.text, p.position);
        //очистка партиклов (не с экрана, а с массива)
        particles.clear();

        screen.show();
        for(var unit : units)
            System.out.println("Name: " + unit.name + " | X Y: " + unit.position.x + ' ' + unit.position.y + " | Width: " + GetTextWidth(unit.content));

    }
}

class Particle{
    String text;
    Point position;
    Particle(String txt, Point point){
        text = txt; position = new Point(point);
    }
}
class Unit{
    String name;
    String content;
    Point position;
    Unit(String n, String c, Point pos){
        name = n; content = c; position = new Point(pos);
    }
    Point lineEnd(String label){
        String[] lines = content.split("\n"); //делит строку на множество строк, где есть этот вот аргумент
        for (int i = 0; i < lines.length; i++)
            if(lines[i].contains(label))
                return new Point(position.x + lines[i].length(), position.y + i); // по x конец строки, по y номер строки
        return null;
    }
}

//просто заготовки
enum TextTemplate {
    EnemyBox("""
            =====================
            | Index: [ind]
            | HP: [hp]
            | Damage: [dmg]
            ====================="""),
    HeroBox("""
            =====================
            | Money: [money]
            | HP: [hp] - [mhp]
            | Damage: [dmg]
            | Weapon: [weapon]
            | Crit Chance: [critch]
            | Dodge Chance: [dodgech]
            ====================="""),
    DoorBox("""
            =====================
            | Index: [ind]
            | Room type: [type]
            =====================
            """),
    StatsParticle("[sign][amo]"),
    ShopStatItemBox("""
            =====================
            | Index: [ind]
            | Price: [price]
            | Stat: [stat]
            | Amount: [amount]
            ====================="""),
    ShopWeaponItemBox("""
            =====================
            | Index: [ind]
            | Price: [price]
            | Weapon: [weapon]
            |       [minDmg] - [maxDmg]
            =====================""");

    private final String template; //шаблон

    String getTemplate(){
        return template;
    }

    //конструктор
    TextTemplate(String text){
        template = text;
    }
    //заполняет заготовки данными и даёт их
    String getUpdatedInfo(Map<String, ?> stringMap){
        String result = template;
        //stringMap.entrySet() условно возращает массив объектов с парами значений: ключ и значение
        for (var pair : stringMap.entrySet())
            result = result.replace('[' + pair.getKey() + ']', String.valueOf(pair.getValue()));

        return result;
    }
}
