/**
 * Created with IntelliJ IDEA.
 * User: Модификатор
 * Date: 11.05.13
 * Time: 15:43
 * To change this template use File | Settings | File Templates.
 */
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;


public class MainView {
    private JFrame frame;

    private MarkCollection points;

    private JPanel master;

    private JPanel west_panel;
    private Graphic east_panel;

    private XPanel xlist;
    private YPanel ylist;

    private JLabel data_label;

    /**
     * Create the application.
     */
    public MainView() {
        initialize();
    }

    public void setVisible() {
        frame.setVisible(true);
    }

    public void setPoints(MarkCollection points) {
        this.points = points;
    }

    public void setGraphic(Graphic graphic) {
        east_panel = graphic;

        master.add(east_panel, BorderLayout.EAST);
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 800, 580);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        master = new JPanel(new BorderLayout());
        frame.getContentPane().add(master);

        west_panel = new JPanel(new FlowLayout());
        master.add(west_panel, BorderLayout.WEST);

        data_label = new JLabel();
        master.add(data_label, BorderLayout.NORTH);
    }

    public void setLabel(String label) {
        data_label.setText(label);
    }

    public void setYValues(float[] values) {
        ylist = new YPanel(values, new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                newDots();
            }
        });
        ylist.setLabel("Y value:");
        west_panel.add(ylist);
    }

    public void setXValues(float[] values) {
        xlist = new XPanel(values, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                newDots();
            }
        });
        xlist.setLabel("X value:");
        west_panel.add(xlist);
    }

    public void setRadius(float radius) {
        final RadiusPanel slider = new RadiusPanel(1, 40);
        slider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                points.setRadius(slider.getValue());
            }
        });
        slider.setValue(radius);
        west_panel.add(slider);
    }

    public void newDots() {
        float y_value = ylist.getValue();
        float x_value = xlist.getValue();
        if(!Float.isNaN(y_value) && !Float.isNaN(x_value)) {
            Mark mark = new Mark(x_value, y_value);
            points.add(mark);
        }
    }

    public void initPointRadius() {
        int size = east_panel.point_radius;

        JPanel panel = new JPanel(new GridLayout(2, 1));
        JLabel label = new JLabel("Point size: ");

        final JSpinner size_control = new JSpinner();
        size_control.setValue(size);
        size_control.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                east_panel.point_radius = (Integer)size_control.getValue();
            }
        });

        panel.add(label);
        panel.add(size_control);

        west_panel.add(panel, BorderLayout.CENTER);
    }
}