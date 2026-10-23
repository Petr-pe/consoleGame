import java.util.ArrayList;
import java.util.Map;

public class UI {
    static Screen screen = new Screen(100, 50);
    static void AssemblyChoseScreen(Rooms rooms[]){
        int ind = 1, x = 1, y = 3;
        for (Rooms room : rooms){
            String boxText = TextBox.DoorBox.getUpdatedInfo(Map.of("ind", ind, "type", room.typeName));
            ind++;
            screen.place(boxText, x, y);
            x += 22;
        }
    }
    static void AssemblyShopScreen(/*ShopItems*/){

    }
    static void AssemblyFightScreen(ArrayList<Enemy> enemies, Hero hero){
        int ind = 1;
        for (Enemy enemy : enemies){
            String boxText = TextBox.EnemyBox.getUpdatedInfo(Map.of(
                    "ind", ind,
                    "hp", enemy.health,
                    "dmg", enemy.damage)
            );
            ind++;

        }
    }
    static void AssemblyFightScreen(Enemy enemy, Hero hero){
        String box = TextBox.EnemyBox.getUpdatedInfo(Map.of(
                "ind", 1,
                "hp", enemy.health,
                "dmg", enemy.damage)
        );

    }
    static void AssemblyRestScreen(){

    }
    static String GetHeroStatistic(Hero hero){
        return TextBox.HeroBox.getUpdatedInfo(Map.of(
                "money", hero.money,
                "mhp", hero.maxHealth,
                "hp", hero.health,
                "dmg", hero.basicDamage,
                "weapon", (hero.weapon.getName() + ' ' + String.valueOf(hero.weapon.minDamage) + '-' + String.valueOf(hero.weapon.maxDamage)),
                "critch", hero.critChance,
                "dodgech", hero.dodgeChance)
        );
    }
    static void ShowScreen(){
        screen.show();
    }
}
enum TextBox{
    EnemyBox("""
            =====================
            | Index: [ind]
            | HP: [hp]
            | Damage: [dmg]
            ====================="""),
    HeroBox("""
            =====================
            | Money: [money]
            | Max HP: [mhp]
            | HP: [hp]
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
            """);

    private String template; //шаблон

    TextBox (String text){
        template = text;
    }
    String getUpdatedInfo(Map<String, ?> stringMap){
        String result = template;
        //stringMap.entrySet() условно возращает массив объектов с парами значений: ключ и значение
        for (var pair : stringMap.entrySet())
            result = result.replace('[' + pair.getKey() + ']', String.valueOf(pair.getValue()));

        return result;
    }

}
