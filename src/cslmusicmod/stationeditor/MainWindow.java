package cslmusicmod.stationeditor;

import com.google.gson.Gson;
import cslmusicmod.stationeditor.model.Station;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class MainWindow extends JFrame {
    private JPanel mainPanel;
    private JTabbedPane tabs;
    private JTextField stationName;
    private JPanel testpanel;
    private JMenuBar menuBar;
    private JMenu fileMenu;

    private Action openAction;
    private Action exitAction;

    private Station station;

    public  MainWindow() throws IOException {

        Gson gson = Station.getGson();
        String json = new String(Files.readAllBytes(Paths.get("TestStation.json")));
        station =  gson.fromJson(json, Station.class);

        initComponents();
    }

    private void initActions() {
        openAction = new AbstractAction("Open") {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

            }
        };
        exitAction = new AbstractAction("Exit") {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                MainWindow.this.dispatchEvent(new WindowEvent(MainWindow.this, WindowEvent.WINDOW_CLOSING));
            }
        };
    }

    private void initMenuBar() {
        menuBar = new JMenuBar();
        fileMenu = new JMenu("File");
        fileMenu.add(openAction);
        fileMenu.add(exitAction);

        setJMenuBar(menuBar);
        menuBar.add(fileMenu);
    }

    private void initComponents() {
        setTitle("CSL Music Mod Station Editor");
        setContentPane(mainPanel);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();

        testpanel.add(new JScrollPane(new CollectionListEditor(station, new ArrayList<>())));

        initActions();
        initMenuBar();
    }

    public static void main(String[] args) throws IOException {
        MainWindow wnd = new MainWindow();
        wnd.setVisible(true);
    }

}
