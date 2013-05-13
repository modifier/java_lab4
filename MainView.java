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
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;


public class MainView {
    private JFrame frame;

    private MarkCollection points;

    private JPanel west_panel;
    private JPanel east_panel;

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

    public void setGraphic(JPanel graphic) {
        east_panel = graphic;
        frame.getContentPane().add(east_panel, BorderLayout.CENTER);
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 800, 580);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        west_panel = new JPanel(new FlowLayout());
        frame.getContentPane().add(west_panel, BorderLayout.EAST);
    }

    public void setYValues(float[] values) {
        ylist = new YPanel(values, new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                newDots();
            }
        });
        ylist.setLabel("Y value:");
        west_panel.add(ylist, BorderLayout.NORTH);
    }

    public void setXValues(float[] values) {
        xlist = new XPanel(values, new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                newDots();
            }
        });
        xlist.setLabel("X value:");
        west_panel.add(xlist, BorderLayout.NORTH);
    }

    public void setRadius(float radius) {
        final RadiusPanel slider = new RadiusPanel(1, 30);
        slider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                points.setRadius(slider.getValue());
            }
        });
        slider.setValue(radius);
        west_panel.add(slider, BorderLayout.CENTER);
    }

    public void newDots() {
        float y_value = ylist.getValue();
        float x_value = xlist.getValue();
        if(!Float.isNaN(y_value) && !Float.isNaN(x_value)) {
            Mark mark = new Mark(x_value, y_value);
            points.add(mark);
        }
    }
}