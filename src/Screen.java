public class Screen {


    private int width = 200, height = 100;
    private StringBuilder screenStrBldr;
    private final String emptyScreen;

    public int getHeight() {
        return height;
    }
    public int getWidth() {
        return width;
    }

    //конструктор
    Screen(int width, int height){
        this.width = width; this.height = height;
        StringBuilder screen = new StringBuilder(" ".repeat((width + 1) * height));
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                //вычисление точки для саписи символа
                int point = y * (width + 1) + x;
                if(y == 0 || y == height - 1) screen.setCharAt(point, '-');
                if(x == 0 || x == width - 1) screen.setCharAt(point, '|');
            }
        }

        for (int i = width; i < screen.length(); i += width + 1) screen.setCharAt(i, '\n');
        screenStrBldr = new StringBuilder(screen);
        emptyScreen = screen.toString();
    }
    //помещает текст в screen
    void place(String string, int x, int y){
        x = Math.clamp(x, 0, width); y = Math.clamp(y, 0, height);
        for (int i = 0, px = x, py = y; i < string.length(); i++) {
            //переход на след строку
            if(string.charAt(i) == '\n'){
                px = x; py++; continue;
            }

            //если значение по высоте для саписи уже выше самой высоты, то выход
            if(py >= height) break;

            if(px >= width){
                py++;
                px = x;
                while(i < string.length() && string.charAt(i) != '\n') i++;
                continue;
            }

            //вычисление точки для саписи символа
            int point = py * (width + 1) + px;
            this.screenStrBldr.setCharAt(point, string.charAt(i));
            px++;
        }
    }
    void place(String string, Point p){
        place(string, p.x, p.y);
    }
    void place(Unit unit){
        place(unit.content, unit.position);
    }
    //показывает screen
    void show(){
        // Очистить экран и переместить курсор в начало
        System.out.print("\u001B[H\u001B[2J\u001B[3J");
        System.out.println(screenStrBldr);
    }
    //очищает экран
    void clear(){
        screenStrBldr = new StringBuilder(emptyScreen);
    }

}
