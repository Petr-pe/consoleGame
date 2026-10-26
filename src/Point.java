public class Point {
    public int x;
    public int y;

    Point(int x, int y){
        this.x = x; this.y = y;
    }
    Point(Point other){
        x = other.x; y = other.y;
    }
    void clamp(int mnx, int mxx, int mny, int mxy){
        x = Math.clamp(x, mnx, mxx);
        y = Math.clamp(y, mny, mxy);
    }
    Point plus(Point other){
        return new Point(x + other.x, y + other.y);
    }
}
