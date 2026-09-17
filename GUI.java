import javax.swing.*;
import java.awt.*;

public class GUI extends JFrame {

    private LRUCache cache;

    private JTextField inputField;

    private JTextArea cacheArea;
    private JTextArea logArea;
    private JTextArea historyArea;

    private JLabel lblHits;
    private JLabel lblMisses;
    private JLabel lblRatio;
    private JLabel lblLRU;
    private JLabel lblMRU;
    private JLabel lblCapacity;

    private JButton btnAdd;
    private JButton btnSearch;
    private JButton btnReset;
    private JButton btnSave;
    private JButton btnStats;

    public GUI() {

        // -------------------------
        // Ask Cache Size
        // -------------------------

        int capacity = 5;

        while (true) {

            try {

                String input = JOptionPane.showInputDialog(
                        null,
                        "Enter Cache Size",
                        "LRU Cache",
                        JOptionPane.QUESTION_MESSAGE);

                if (input == null)
                    System.exit(0);

                capacity = Integer.parseInt(input);

                if (capacity > 0)
                    break;

            } catch (Exception e) {

                JOptionPane.showMessageDialog(
                        null,
                        "Enter a valid positive number!");

            }

        }

        cache = new LRUCache(capacity);

        initializeGUI();
    }

    //--------------------------------

    private void initializeGUI() {

        setTitle("LRU Cache Simulator");

        setSize(1100,700);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setLayout(new BorderLayout());

        getContentPane().setBackground(new Color(45,45,45));

        //------------------------------------
        // TOP PANEL
        //------------------------------------

        JPanel top = new JPanel();

        top.setBackground(new Color(30,30,30));

        inputField = new JTextField(18);

        btnAdd = new JButton("Add");

        btnSearch = new JButton("Search");

        btnReset = new JButton("Reset");

        btnSave = new JButton("Save Logs");

        btnStats = new JButton("Statistics");

        top.add(new JLabel("Key :"));

        top.add(inputField);

        top.add(btnAdd);

        top.add(btnSearch);

        top.add(btnReset);

        top.add(btnSave);

        top.add(btnStats);

        add(top,BorderLayout.NORTH);

        //------------------------------------
        // CENTER
        //------------------------------------

        cacheArea = createTextArea();

        logArea = createTextArea();

        historyArea = createTextArea();

        JPanel cachePanel = createPanel("Current Cache",cacheArea);

        JPanel logPanel = createPanel("Operation Logs",logArea);

        JPanel historyPanel = createPanel("Search History",historyArea);

        JSplitPane right =
                new JSplitPane(
                        JSplitPane.VERTICAL_SPLIT,
                        logPanel,
                        historyPanel);

        right.setDividerLocation(300);

        JSplitPane main =
                new JSplitPane(
                        JSplitPane.HORIZONTAL_SPLIT,
                        cachePanel,
                        right);

        main.setDividerLocation(350);

        add(main,BorderLayout.CENTER);

        //------------------------------------
        // STATUS PANEL
        //------------------------------------

        JPanel status = new JPanel();

        status.setLayout(new GridLayout(2,3));

        lblHits = new JLabel();

        lblMisses = new JLabel();

        lblRatio = new JLabel();

        lblLRU = new JLabel();

        lblMRU = new JLabel();

        lblCapacity = new JLabel();

        status.add(lblHits);

        status.add(lblMisses);

        status.add(lblRatio);

        status.add(lblLRU);

        status.add(lblMRU);

        status.add(lblCapacity);

        add(status,BorderLayout.SOUTH);

        //------------------------------------
        // BUTTON EVENTS
        //------------------------------------

        btnAdd.addActionListener(e->{

            String key = inputField.getText().trim();

            if(key.isEmpty())
                return;

            String before = cache.getLastEvicted();

            cache.put(key);

            refresh();

            if(!before.equals(cache.getLastEvicted())){

                JOptionPane.showMessageDialog(
                        this,
                        "Cache Full!\n\nEvicted : "
                                + cache.getLastEvicted());

            }

            inputField.setText("");

        });

        //------------------------------------

        btnSearch.addActionListener(e->{

            String key = inputField.getText().trim();

            if(key.isEmpty())
                return;

            boolean found = cache.get(key);

            if(found){

                JOptionPane.showMessageDialog(
                        this,
                        "Cache Hit");

            }

            else{

                JOptionPane.showMessageDialog(
                        this,
                        "Cache Miss");

            }

            refresh();

            inputField.setText("");

        });

        //------------------------------------

        btnReset.addActionListener(e->{

            cache.clear();

            refresh();

            JOptionPane.showMessageDialog(
                    this,
                    "Cache Cleared");

        });

        //------------------------------------

        btnSave.addActionListener(e->{

            cache.saveLogs();

            JOptionPane.showMessageDialog(
                    this,
                    "Logs Saved Successfully");

        });

        //------------------------------------

        btnStats.addActionListener(e->{

            JOptionPane.showMessageDialog(

                    this,

                    "Hits : "
                            + cache.getHits()

                            + "\nMisses : "
                            + cache.getMisses()

                            + "\nHit Ratio : "
                            + String.format("%.2f",
                            cache.getHitRatio())
                            + "%"

                            + "\n\nLRU : "
                            + cache.getLRU()

                            + "\nMRU : "
                            + cache.getMRU()

                            + "\nCapacity : "
                            + cache.getCurrentSize()
                            + "/"
                            + cache.getCapacity()

            );

        });

        //------------------------------------

        refresh();

        setVisible(true);

    }

    //------------------------------------

    private JTextArea createTextArea(){

        JTextArea area = new JTextArea();

        area.setEditable(false);

        area.setFont(new Font(
                "Consolas",
                Font.PLAIN,
                15));

        area.setBackground(Color.BLACK);

        area.setForeground(Color.GREEN);

        return area;

    }

    //------------------------------------

    private JPanel createPanel(
            String title,
            JTextArea area){

        JPanel panel =
                new JPanel(new BorderLayout());

        panel.setBorder(
                BorderFactory.createTitledBorder(title));

        panel.add(
                new JScrollPane(area));

        return panel;

    }

    //------------------------------------

    private void refresh(){

        cacheArea.setText(cache.display());

        logArea.setText(cache.getLogs());

        historyArea.setText(cache.getHistory());

        lblHits.setText(
                "Hits : "
                        + cache.getHits());

        lblMisses.setText(
                "Misses : "
                        + cache.getMisses());

        lblRatio.setText(
                "Hit Ratio : "
                        + String.format("%.2f",
                        cache.getHitRatio())
                        + "%");

        lblLRU.setText(
                "LRU : "
                        + cache.getLRU());

        lblMRU.setText(
                "MRU : "
                        + cache.getMRU());

        lblCapacity.setText(
                "Capacity : "
                        + cache.getCurrentSize()
                        + "/"
                        + cache.getCapacity());

    }

}