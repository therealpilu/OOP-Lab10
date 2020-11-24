package it.unibo.oop.lab.reactivegui02;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class ConcurrentGUI extends JFrame {

    private static final long serialVersionUID = 1L;
    private static final double WIDTH_PERC = 0.2;
    private static final double HEIGHT_PERC = 0.1;
    private final JLabel display = new JLabel();
    private final JButton up = new JButton("up");
    private final JButton down = new JButton("down");
    private final JButton stop = new JButton("stop");

    public ConcurrentGUI() {
        super();
        final Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize((int) (screensize.getWidth() * WIDTH_PERC), (int) (screensize.getHeight() * HEIGHT_PERC));
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        final JPanel panel = new JPanel();
        panel.add(display);
        panel.add(up);
        panel.add(down);
        panel.add(stop);
        this.getContentPane().add(panel);
        this.setVisible(true);

        final Agent agent = new Agent();
        new Thread(agent).start();

        up.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                // TODO Auto-generated method stub
                agent.changeCount(true);
            }
        });

        down.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                // TODO Auto-generated method stub
                agent.changeCount(false);
            }
        });

        stop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                // TODO Auto-generated method stub
                agent.stopCounting();
                up.setEnabled(false);
                down.setEnabled(false);
                stop.setEnabled(false);
            }
        });
    }

    private class Agent implements Runnable {

        private volatile boolean stop;
        private volatile int counter;
        private boolean flag = true;

        @Override
        public void run() {
            // TODO Auto-generated method stub
            while (!this.stop) {
                try {
                    SwingUtilities.invokeAndWait(new Runnable() {
                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            ConcurrentGUI.this.display.setText(Integer.toString(Agent.this.counter));
                        }
                    });
                    if (flag) {
                        this.counter++;
                    } else {
                        this.counter--;
                    }
                    Thread.sleep(100);
                } catch (InvocationTargetException | InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }

        public void changeCount(final boolean flag) {
            this.flag = flag;
        }

        public void stopCounting() {
            this.stop = true;
        }

    }

}
