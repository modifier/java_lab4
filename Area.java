/**
 * Created with IntelliJ IDEA.
 * User: Модификатор
 * Date: 14.05.13
 * Time: 0:17
 * To change this template use File | Settings | File Templates.
 */
public class Area {
    private float radius;
    public  Area(float R) {
        this.radius = R;
    }

    public boolean contains(Mark m) {
        if(m.top() && m.right()) {
            return (Math.pow(m.x, 2) + Math.pow(m.y, 2)) < Math.pow(radius / 2, 2);
        } else if((m.bottom() || m.vmiddle()) && (m.right() || m.middle())) {
            return m.x - m.y < radius;
        } else if(m.bottom() && m.left()) {
            return m.x > -radius / 2 && m.y > -radius;
        }
        return false;
    }
}
