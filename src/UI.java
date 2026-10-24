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
    static Point HorizontalPosNextTextUnit(Point point, String textUnit, int ident){
        return new Point(point.x + GetTextWidth(textUnit) + ident, point.y);
    }
    static int GetTextWidth(String s) {
        int mx = 0;
        for (String line : s.split("\n"))
            mx = Math.max(mx, line.length());
        return mx;
    }
    static Unit FindUnit(String name){
        for (var u : units)
            if(u.name.equals(name))
                return u;
        return null;
    }
    static void AddUnit(Unit unit){
        screen.place(unit);
        units.add(unit);
    }

    static void AssemblyFightScreen(ArrayList<Enemy> enemies) {
        NewFrame();

        int ind = 0;
        Point p = new Point(listPoint);
        for (Enemy enemy : enemies) {
            String boxText = TextTemplate.EnemyBox.getUpdatedInfo(Map.of(
                    "ind", ind + 1,
                    "hp", enemy.health,
                    "dmg", enemy.damage)
            );
            AddUnit(new Unit("enemy" + ind, boxText, p));
            p = HorizontalPosNextTextUnit(p, boxText, 1);
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
    static void AssemblyChoseScreen(Rooms rooms[]) {
        NewFrame();

        int ind = 0;
        Point p = new Point(listPoint);
        for (Rooms room : rooms) {
            String boxText = TextTemplate.DoorBox.getUpdatedInfo(Map.of(
                    "ind", ind + 1,
                    "type", room.typeName)
            );
            AddUnit(new Unit("door" + ind, boxText, p));
            p = HorizontalPosNextTextUnit(p, boxText, 1);
            ind++;
        }
    }
    static void AssemblyShopScreen(/*ShopItems*/){

    }
    static void AssemblyRestScreen(){

    }

    static void AddParticleToList(int val, Point pos){
        String text = TextTemplate.StatsParticle.getUpdatedInfo(Map.of(
                "sign", val > 0 ? '+' : '-',
                "amo", Math.abs(val)
        ));
        particles.add(new Particle(text, pos));
    }

    // рядом с HP врага (бокс врага: x = 3 + 22*i, y = 3, строка HP = y + 2)
    static void EnemyHpParticle(int enemyIndex, int delta) {

    }
    // рядом с HP героя (бокс: x = 1, y = height - 10, строка HP = y + 3)
    static void HeroHpParticle(int delta) {

    }

    static void AddHeroStatistic(Hero hero){
        String stats = TextTemplate.HeroBox.getUpdatedInfo(Map.of(
                "money", hero.money,
                "mhp", hero.maxHealth,
                "hp", hero.health,
                "dmg", hero.basicDamage,
                "weapon", (hero.weapon.getName() + ' ' + String.valueOf(hero.weapon.minDamage) + '-' + String.valueOf(hero.weapon.maxDamage)),
                "critch", hero.critChance,
                "dodgech", hero.dodgeChance)
        );
        AddUnit(new Unit("hero", stats, heroStats));
    }

    static void NewFrame(){
        units.clear();
        particles.clear();
        screen.clear();
    }
    static void ShowScreen(){
        //вывод партиклов
        for (var p : particles)
            screen.place(p.text, p.position.x, p.position.y);
        //очистка партиклов (не с экрана, а с массива)
        particles.clear();

        screen.show();
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
        String lines[] = content.split("\n"); //делит строку на множество строк где есть этот вот аргумент
        for (int i = 0; i < lines.length; i++)
            if(lines[i].contains(label))
                return new Point(position.x + lines[i].length(), position.y + i); // по x конец строки, по y номер строки
        return null;
    }
}

//проосто заготовки
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
    ShopItemBox("""
            =====================
            | Index: [ind]
            | Money: [money]
            | Item type: [type]
            =====================
            """),
    DoorBox("""
            =====================
            | Index: [ind]
            | Room type: [type]
            =====================
            """),
    StatsParticle("[sign][amo]");

    private String template; //шаблон

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

//строка 148 попробовать UI.GetTextWidth() строка 19
//сделать функцию, которая будет по label определять строку, находить её конец (или конец самой длинной строки), и вставлять туда соответсвующий партикл
//добавить комментарии к функциям
//