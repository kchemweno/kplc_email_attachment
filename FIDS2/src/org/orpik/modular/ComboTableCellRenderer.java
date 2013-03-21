/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.orpik.modular;

import java.awt.Color;
import java.awt.Component;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import org.orpik.logging.LogManager;

/**
 *
 * @author Chemweno
 */
public     class ComboTableCellRenderer implements ListCellRenderer, TableCellRenderer {
        DefaultListCellRenderer listRenderer = new DefaultListCellRenderer();
        DefaultTableCellRenderer tableRenderer = new DefaultTableCellRenderer();
        private static LogManager logManager = new LogManager();

        private void configureRenderer(JLabel renderer, Object value) {
            logManager.logInfo("Entering 'configureRenderer(JLabel renderer, Object value)' method");
            if ((value != null) && (value instanceof Color)) {
                renderer.setText(value.toString());
                //renderer.setBackground((Color)value);
            } else {
                renderer.setText((String) value);
            }
            logManager.logInfo("Exiting 'configureRenderer(JLabel renderer, Object value)' method");
        }

    @Override
        public Component getListCellRendererComponent(JList list, Object value, int index,
                boolean isSelected, boolean cellHasFocus) {
        logManager.logInfo("Entering 'getListCellRendererComponent(JList list, Object value, int index,"
                + " boolean isSelected, boolean cellHasFocus)' method");
            listRenderer = (DefaultListCellRenderer) listRenderer.getListCellRendererComponent(list, value,
                    index, isSelected, cellHasFocus);
            configureRenderer(listRenderer, value);
            logManager.logInfo("Exiting 'getListCellRendererComponent(JList list, Object value, int index,"
                + " boolean isSelected, boolean cellHasFocus)' method");
            return listRenderer;
        }

    @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
        logManager.logInfo("Entering 'getTableCellRendererComponent(JTable table, Object value, boolean isSelected,"
                + " boolean hasFocus, int row, int column)' method");
            tableRenderer = (DefaultTableCellRenderer) tableRenderer.getTableCellRendererComponent(table,
                    value, isSelected, hasFocus, row, column);
            configureRenderer(tableRenderer, value);
            logManager.logInfo("Exiting 'getTableCellRendererComponent(JTable table, Object value, boolean isSelected,"
                + " boolean hasFocus, int row, int column)' method");
            return tableRenderer;
        }
    }
