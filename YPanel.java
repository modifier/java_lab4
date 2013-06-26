import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Модификатор
 * Date: 11.05.13
 * Time: 21:18
 * To change this template use File | Settings | File Templates.
 */
public class YPanel extends JPanel {
    private JLabel label;
    private ArrayList<JRadioButton> rb;
    private float[] values;

    public YPanel(float[] values, ActionListener changed) {
        super(new GridLayout(0, 1));
        this.values = values;

        label = new JLabel();
        this.add(label);

        rb = new ArrayList<JRadioButton>();

        for(int i = 0; i < values.length; i++) {
           JRadioButton btn = new JRadioButton(Float.toString(values[i]));
            btn.addActionListener(changed);
            rb.add(btn);

            this.add(btn);
        }
    }

    public void setLabel(String label) {
        this.label.setText(label);
    }

    public ArrayList<Float> getValues() {
        ArrayList<Float> result = new ArrayList<Float>();
        for(int i = 0; i < rb.size(); i++) {
            if(rb.get(i).isSelected()) {
                result.add(values[i]);
            }
        }
        return result;
    }
}
