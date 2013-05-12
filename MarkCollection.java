import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Модификатор
 * Date: 12.05.13
 * Time: 2:11
 * To change this template use File | Settings | File Templates.
 */
public class MarkCollection {
    private ArrayList<Mark> points;
    private float radius;

    ICollectionListener observer;

    public MarkCollection() {
        this.points = new ArrayList<Mark>();
    }

    public void add(Mark mark) {
        for(int i = 0; i < points.size(); i++) {
            if(mark.equals(points.get(i))) {
                return;
            }
        }

        points.add(mark);
        checkOutgoing();
    }

    public void setRadius(float radius) {
        this.radius = radius;
        checkOutgoing();
    }

    public float getRadius() {
        return radius;
    }

    public void checkOutgoing() {
        forEach(new MarkIterator() {
            @Override
            public boolean Iterate(Mark mark, boolean isInside) {
                if(!isInside) {
                    observer.Notify();
                    return false;
                }
                return true;
            }
        });
    }

    public void forEach(MarkIterator foo) {
        for(int i = 0; i < points.size(); i++) {
            if(!foo.Iterate(points.get(i), points.get(i).isInside(radius))) {
                break;
            };
        }
    }
}
