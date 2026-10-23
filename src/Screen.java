public class Screen {
    private int width, height;
    StringBuilder screenStrBldr;

    Screen(int width, int height){
        this.width = width; this.height = height;
        StringBuilder screen = new StringBuilder(" ".repeat((width + 1) * height));
        for (int i = width; i < screen.length(); i += width + 1) screen.setCharAt(i, '\n');
        this.screenStrBldr = screen;
    }
    void place(String string, int x, int y){
        x = Math.clamp(x, 0, width); y = Math.clamp(y, 0, height);
        for (int i = 0, px = x, py = y; i < string.length(); i++) {
            //переход на след строку
            if(string.charAt(i) == '\n'){
                px = x; py++; continue;
            }

            //если значение по высоте для саписи уже выше самой высоты, то выход
            if(py > height) break;

            if(px > width){
                py++;
                px = x;
                while(string.charAt(i) != '\n' && i < string.length()) i++;
                continue;
            }

            //вычисление точки для саписи символа
            int point = py * (width + 1) + px;
            this.screenStrBldr.setCharAt(point, string.charAt(i));
            px++;
        }
    }
    void show(){
        // Очистить экран и переместить курсор в начало
        System.out.print("\033[2J\033[H");
        System.out.println(screenStrBldr);
    }


    int getWidth(){
        int counter = 0;
        for (char i : screenStrBldr.toString().toCharArray()){
            if(i == '\n') break;
            counter++;
        }
        return counter;
    }
    int getHeight(){
        return screenStrBldr.length() / (getWidth() + 1);
    }

}
