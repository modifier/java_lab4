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
            return m.x < radius && m.y < radius;
        } else if(m.bottom() && (m.right() || m.vmiddle())) {
            return m.x*m.x + m.y*m.y < radius*radius;
        } else if((m.bottom() || m.middle()) && m.left()) {
            return -2 * m.x - radius < m.y;
        }
        return false;
    }
}
