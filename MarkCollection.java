import java.util.ArrayList;
import java.util.Observable;

/**
 * Created with IntelliJ IDEA.
 * User: Модификатор
 * Date: 12.05.13
 * Time: 2:11
 * To change this template use File | Settings | File Templates.
 */
public class MarkCollection extends Observable {
    private ArrayList<Mark> points;
    private float radius;

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

        setChanged();
        notifyObservers("change");
    }

    public void setRadius(float radius) {
        check(radius);
        this.radius = radius;

        setChanged();
        notifyObservers("change");
    }

    public float getRadius() {
        return radius;
    }

    public void check(final float radius) {
        forEach(new IMarkIterator() {
            @Override
            public boolean Iterate(Mark mark, boolean isInside) {
                if(!mark.isInside(radius) && isInside) {
                    setChanged();
                    notifyObservers("animate");
                    return false;
                }
                return true;
            }
        });
    }

    public void forEach(IMarkIterator foo) {
        for(int i = 0; i < points.size(); i++) {
            if(!foo.Iterate(points.get(i), points.get(i).isInside(radius))) {
                break;
            };
        }
    }
}
