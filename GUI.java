import java.util.Timer;
import java.util.TimerTask;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.BoxLayout;
import javax.swing.Box;
import javax.swing.SwingUtilities;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.IOException;


public class GUI
{
    JFrame f;
    JPanel numbers;
    JTextField jtf;
    
    JLabel Lmatch;
    JLabel Ltotal;
    JLabel Lprob;
    JLabel Le;
    
    Timer t;
    TimerTask action;
    
    Mechanics mech;
    
    private static String filename = "./data.txt";
    
    boolean running = false;
    long total = 0;
    long match = 0;
    double prob = 0;
    double e = 0;
    int deckSize = 52;
    
    public static void main(String[] args){GUI g = new GUI();}
    private GUI(){
        makeFrame();
        load();
    }
    
    private void makeFrame(){
        f = new JFrame("Getting e by mixing cards");
        f.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        f.addWindowListener(new WindowAdapter(){public void windowClosing(WindowEvent e){save();System.exit(1);}});
        f.setVisible(false);
        f.setResizable(false);
        
        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        
        
        JPanel input = new JPanel(new GridLayout(1,2,5,5));
        input.add(new JLabel("Number of cards in deck: "));
        jtf = new JTextField("52",5);
        jtf.setActionCommand("Enter");
        jtf.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){setDeckSize();}});
        input.add(jtf);
        
        
        JPanel info = new JPanel(new GridLayout(1,2,20,20));
        JPanel labels = new JPanel(new GridLayout(4,1,5,5));
        numbers = new JPanel(new GridLayout(4,1,5,5));
        
        labels.add(new JLabel("Iterations: ", SwingUtilities.RIGHT));
        Ltotal = new JLabel(String.valueOf(total), SwingUtilities.RIGHT);
        numbers.add(Ltotal);
        
        labels.add(new JLabel("Matches: ", SwingUtilities.RIGHT));
        Lmatch = new JLabel(String.valueOf(match), SwingUtilities.RIGHT);
        numbers.add(Lmatch);
        
        labels.add(new JLabel("Probability: ", SwingUtilities.RIGHT));
        Lprob = new JLabel(String.valueOf(prob), SwingUtilities.LEFT);
        numbers.add(Lprob);
        
        labels.add(new JLabel("Approximated e: ", SwingUtilities.RIGHT));
        Le = new JLabel(String.valueOf(e), SwingUtilities.LEFT);
        numbers.add(Le);
        
        info.add(labels);
        info.add(numbers);
        
        
        JButton b;
        JPanel buttons = new JPanel(new GridLayout(2,3,5,5));
        
        b = new JButton("Start");
        b.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){start();}});
        buttons.add(b);
        
        b = new JButton("Stop");
        b.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){stop();}});
        buttons.add(b);
        
        b = new JButton("Reset");
        b.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){reset();}});
        buttons.add(b);
        
        b = new JButton("Continue");
        b.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){cont();}});
        buttons.add(b);
        
        b = new JButton("Save");
        b.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){save();}});
        buttons.add(b);
        
        b = new JButton("Load");
        b.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){load();}});
        buttons.add(b);
        
        
        
        main.add(input);
        main.add(Box.createRigidArea(new Dimension(10,10)));
        main.add(info);
        main.add(Box.createRigidArea(new Dimension(10,10)));
        main.add(buttons);
        
        
        f.setLayout(new BorderLayout(10,10));
        f.add(new JLabel(""), "North");
        f.add(new JLabel(""), "East");
        f.add(new JLabel(""), "South");
        f.add(new JLabel(""), "West");
        f.add(main, "Center");
        
        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
    
    private void update(){
        total = mech.total;
        match = mech.match;
        prob = (double)((double)(total-match)/(double)(total));
        e = 1/prob;
        
        Ltotal.setText(String.valueOf(total));
        Lmatch.setText(String.valueOf(match));
        Lprob.setText(String.valueOf(prob));
        Le.setText(String.valueOf(e));
    }
    
    private void load(){
        reset();
        try(BufferedReader br = new BufferedReader(new FileReader(filename))){
            total = Integer.valueOf(br.readLine().split(":")[1]);
            match = Integer.valueOf(br.readLine().split(":")[1]);
        }catch(IOException | NumberFormatException e){
            total = 1;
            match = 1;
        }
        cont();
    }
    
    private void save(){
        String text = String.format("total:%d\nmatch:%d",total,match);
        try(PrintWriter out = new PrintWriter(filename)){
            out.print(text);
        }catch(IOException e){
            
        }
    }
    
    private void cont(){
        if(!running){
            mech = new Mechanics(deckSize);
            mech.setTotal(total);
            mech.setMatch(match);
            mech.start();
            
            setTimer();
        }
        running = true;
    }
    
    private void reset(){
        endTimer();
        
        stop();
        
        total = 0;
        match = 0;
        prob = 0;
        e = 0;
        
        Ltotal.setText(String.valueOf(total));
        Lmatch.setText(String.valueOf(match));
        Lprob.setText(String.valueOf(prob));
        Le.setText(String.valueOf(e));
        
        running = false;
    }
    
    private void stop(){
        if(running){
            try{
                mech.interrupted = true;
                total = mech.total;
                match = mech.match;
                mech.end = true;
            }catch(NullPointerException e){}
            
            mech = null;
            
            endTimer();
        }
        running = false;
    }
    
    private void start(){
        if(!running){
            setDeckSize();
            reset();
            
            mech = new Mechanics(deckSize);
            mech.start();
            
            setTimer();
        }
        running = true;
    }
    
    private void setTimer(){
        t = new Timer();
        action = new TimerTask(){public void run(){update();}};
        t.scheduleAtFixedRate(action,70L,70L);
    }
    
    private void endTimer(){
        if(t != null){
            t.cancel();
        }
        t = null;
    }
    
    private void setDeckSize(){
        try{
            String in = jtf.getText().replace(" ","");
            in = in.replace(",","");
            in = in.replace(".","");
            deckSize = Integer.valueOf(in);
            jtf.setText(in);
        }catch(NumberFormatException ex){
            jtf.setText("52");
        }
    }
}
