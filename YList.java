import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemListener;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Модификатор
 * Date: 11.05.13
 * Time: 21:18
 * To change this template use File | Settings | File Templates.
 */
public class YList extends JPanel {
    private JLabel label;
    private ArrayList<JRadioButton> checkBoxes;
    private float[] values;

    public YList(float[] values, ItemListener changed) {
        super(new GridLayout(0, 1));
        this.values = values;

        label = new JLabel();
        this.add(label);

        ButtonGroup group = new ButtonGroup();
        checkBoxes = new ArrayList<JRadioButton>();

        for(int i = 0; i < values.length; i++) {
            JRadioButton btn = new JRadioButton(Float.toString(values[i]));
            btn.addItemListener(changed);
            checkBoxes.add(btn);
            group.add(btn);

            this.add(btn);
        }
    }

    public void setLabel(String label) {
        this.label.setText(label);
    }

    public float getValue() {
        for(int i = 0; i < checkBoxes.size(); i++) {
            if(checkBoxes.get(i).isSelected()) {
                return values[i];
            }
        }
        return Float.NaN;
    }
}
