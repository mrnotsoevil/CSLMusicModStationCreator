package cslmusicmod.stationeditor;

import cslmusicmod.stationeditor.model.Station;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

/**
 * An editor component that allows the user to select collections
 */
public class CollectionListEditor extends JList {

    private Station station;

    private List<String> target;

    public CollectionListEditor(Station station, List<String> target) {
        this.station = station;
        this.target = target;

        initComponent();
        updateEntries();
    }

    private void initComponent() {
        setCellRenderer(new ItemRenderer(station, target));
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                JList list = (JList) event.getSource();
                int index = list.locationToIndex(event.getPoint());
                String item = list.getModel().getElementAt(index).toString();

                if(CollectionListEditor.this.target.contains(item)) {
                    CollectionListEditor.this.target.remove(item);
                }
                else {
                    CollectionListEditor.this.target.add(item);
                }

                list.repaint(list.getCellBounds(index, index));
            }
        });
    }

    private void updateEntries() {

        DefaultListModel model = new DefaultListModel();
        station.getCollections().stream().forEach(x -> model.addElement(x));
        this.setModel(model);
    }

    private static class ItemRenderer extends JCheckBox implements ListCellRenderer {

        private Station station;

        private List<String> target;

        public ItemRenderer(Station station, List<String> target) {
            this.station = station;
            this.target = target;
        }

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean hasFocus) {
            setEnabled(list.isEnabled());
            setSelected(target.contains(value));
            setFont(list.getFont());
            setBackground(list.getBackground());
            setForeground(list.getForeground());
            setText(value.toString());
            return this;
        }
    }
}
