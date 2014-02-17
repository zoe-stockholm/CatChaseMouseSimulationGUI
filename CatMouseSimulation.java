import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
 
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
 
public class CatMouseSimulation {
 
    public enum Status {
        Init, Running, Eaten, Stopped
    }
 
    private static int times = 0;
    private static Cat myCat;
    private static Mouse myMouse;
    private static Status status = Status.Init;
 
    public static void main(String[] args) {
        System.out.println("Welcome to the CS9G cat and mouse simulation!");
        System.out.println("TIME" + "    " + "STATUS" + "    " + "MOUSE"
                + "        " + "CAT");
 
        myCat = new Cat(new Position(400.0, 0.0));
        myMouse = new Mouse(new Position(200.0, 0.0));
 
        CatMouseSimulation gui = new CatMouseSimulation();
        gui.go();
    }
 
    public void go() {
        // Frame
        JFrame frame = new JFrame("Cat and Mouse GUI");
 
        // Panel
        JPanel buttonPanel = new JPanel();
        MyDrawPanel drawPanel = new MyDrawPanel();
 
        // Buttons
        JButton reset = new JButton("reset");
        JButton step = new JButton("step");
        JButton run = new JButton("run");
        JButton quit = new JButton("quit");
 
        buttonPanel.add(reset);
        buttonPanel.add(step);
        buttonPanel.add(run);
        buttonPanel.add(quit);
 
        // Labels
        JLabel timeLabel = new JLabel("Time:");
        JLabel countLabel = new JLabel(" " + times);
 
        buttonPanel.add(timeLabel);
        buttonPanel.add(countLabel);
 
        // Listeners
        reset.addActionListener(new MyResetListener(drawPanel, countLabel));
        step.addActionListener(new MyStepListener(drawPanel, countLabel));
        run.addActionListener(new MyRunListener(drawPanel, countLabel));
        quit.addActionListener(new MyQuitListener(frame));
 
        // Draw UI
        frame.getContentPane().add(BorderLayout.SOUTH, buttonPanel);
        frame.getContentPane().add(BorderLayout.CENTER, drawPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);
        frame.setVisible(true);
    }
 
    // Run chase for number of minutes or terminated if mouse is eaten by cat.
    public static void runChase(int numMin, Cat cat, Mouse mouse,
            MyDrawPanel drawPanel, JLabel countLabel) {
        int counter = 1;
        String statusStr = "init";
        System.out.println(counter + "       " + statusStr + "      "
                + mouse.getPosition().toString() + "    "
                + cat.getPosition().toString());
 
        while (true) {
            boolean eaten = cat.move(mouse.getPosition());
            counter++;
 
            if (eaten == false) {
                mouse.move();
                statusStr = "running";
                System.out.println(counter + "       " + statusStr + "   "
                        + mouse.getPosition().toString() + "    "
                        + cat.getPosition().toString());
                times = counter;
                status = Status.Running;
                if (myCat.getPosition().getMyRadius() == 200 ) {
                    status = Status.Stopped;
                    System.out.println(counter + "       " + status + "   "
                            + mouse.getPosition().toString() + "    "
                            + cat.getPosition().toString());
                    updateUI (countLabel, drawPanel);
                    break;
                }
            } else if (eaten == true){
                statusStr = "eaten";
                System.out.println(counter + "       " + statusStr + "     "
                        + mouse.getPosition().toString() + "    "
                        + cat.getPosition().toString());
                times = counter;
                status = Status.Eaten;
                updateUI (countLabel, drawPanel);
                break;
            }
             
            // update UI for every loop
            updateUI (countLabel, drawPanel);
             
            // delay 20ms
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
 
            if (counter == numMin) {
                System.out
                        .println("After" + numMin + "min, the cat didn't chase up the mouse and the cat got tired and wander off.");
                times = counter;
                status = Status.Stopped;
                break;
            }
             
            updateUI (countLabel, drawPanel);
        }
    }
 
    private static void updateUI (JLabel countLabel, MyDrawPanel drawPanel) {
        drawPanel.onPaint();
        countLabel.setText("" + times + "Status:" + status);
        drawPanel.updateUI();
    }
 
    private class MyResetListener implements ActionListener {
        private final MyDrawPanel drawPanel;
        private final JLabel countLabel;
         
        public MyResetListener (final MyDrawPanel drawPanel, final JLabel countLabel) {
            this.drawPanel = drawPanel;
            this.countLabel = countLabel;
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            // Reset position
            myCat.setPosition(new Position (300.0, 0.0));
            myMouse.setPosition(new Position (200.0, 0.0));
            times = 0;
            status = Status.Init;
             
            System.out.println("Reset: " + "       "
                    + myMouse.getPosition().toString() + "    "
                    + myCat.getPosition().toString());
             
            updateUI(countLabel, drawPanel);
 
        }
 
    }
 
    private class MyStepListener implements ActionListener {
        private final MyDrawPanel drawPanel;
        private final JLabel countLabel;
         
        public MyStepListener (MyDrawPanel drawPanel, JLabel countLabel) {
            this.drawPanel = drawPanel;
            this.countLabel = countLabel;
        }
 
        @Override
        public void actionPerformed(ActionEvent e) {
            if (status == Status.Init|| status == Status.Running) {
                 
                //
                 
                myCat.move(myMouse.getPosition());
                myMouse.move();
                status = Status.Running;
                times ++;
                 
                // repaint UI
                updateUI(countLabel, drawPanel);
            }
        }
    }
 
    private class MyRunListener implements ActionListener {
        private final MyDrawPanel drawPanel;
        private final JLabel countLabel;
         
        public MyRunListener (MyDrawPanel drawPanel, JLabel countLabel) {
            this.drawPanel = drawPanel;
            this.countLabel = countLabel;
        }
 
        @Override
        public void actionPerformed(ActionEvent e) {
            SwingWorker<Object, Object> worker = new SwingWorker <Object, Object>() {
                 
                @Override
                protected Object doInBackground() throws Exception {
                    runChase(800, myCat, myMouse, drawPanel, countLabel);
                    return "Done";
                }      
            };
            worker.execute();
        }
    }
 
    private class MyQuitListener implements ActionListener {
        private final JFrame frame;
         
        public MyQuitListener (final JFrame frame) {
            this.frame = frame;
        }
     
        @Override
        public void actionPerformed(ActionEvent e) {
            frame.dispose();
        }
    }
 
    class MyDrawPanel extends JPanel {
        private static final long serialVersionUID = -1451876344365614067L;
        private Graphics g;
         
        public void onPaint() {
            paintComponent(g);
        }
         
        public void paintComponent(Graphics graphics) {
            g = graphics;
             
            g.setColor(Color.white);
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
 
            g.setColor(Color.gray);
            g.fillOval(200, 200, 400, 400);
 
            g.setColor(Color.yellow); // cat is yellow color
            g.fillOval((int) (myCat.getPosition().getMyRadius() * Math
                    .cos(myCat.getPosition().getMyAngle())) + 400,
                    400 + (int) (myCat.getPosition().getMyRadius() * Math
                            .sin(myCat.getPosition().getMyAngle())), 10, 10);
 
            g.setColor(Color.blue); // mouse is blue color
            g.fillOval(400 + (int) (200 * Math.cos(myMouse.getPosition()
                    .getMyAngle())), 400 + (int) (200 * Math.sin(myMouse
                    .getPosition().getMyAngle())), 10, 10);
 
            g.setColor(Color.black);
            g.drawLine((int) (myCat.getPosition().getMyRadius() * Math
                    .cos(myCat.getPosition().getMyAngle())) + 400,
                    400 + (int) (myCat.getPosition().getMyRadius() * Math
                            .sin(myCat.getPosition().getMyAngle())),
                    400 + (int) (200 * Math.cos(myMouse.getPosition()
                            .getMyAngle())), 400 + (int) (200 * Math
                            .sin(myMouse.getPosition().getMyAngle())));
        }
    }
}