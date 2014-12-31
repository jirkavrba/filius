/*
 ** This file is part of Filius, a network construction and simulation software.
 ** 
 ** Originally created at the University of Siegen, Institute "Didactics of
 ** Informatics and E-Learning" by a students' project group:
 **     members (2006-2007): 
 **         André Asschoff, Johannes Bade, Carsten Dittich, Thomas Gerding,
 **         Nadja Haßler, Ernst Johannes Klebert, Michell Weyer
 **     supervisors:
 **         Stefan Freischlad (maintainer until 2009), Peer Stechert
 ** Project is maintained since 2010 by Christian Eibl <filius@c.fameibl.de>
 **         and Stefan Freischlad
 ** Filius is free software: you can redistribute it and/or modify
 ** it under the terms of the GNU General Public License as published by
 ** the Free Software Foundation, either version 2 of the License, or
 ** (at your option) version 3.
 ** 
 ** Filius is distributed in the hope that it will be useful,
 ** but WITHOUT ANY WARRANTY; without even the implied
 ** warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 ** PURPOSE. See the GNU General Public License for more details.
 ** 
 ** You should have received a copy of the GNU General Public License
 ** along with Filius.  If not, see <http://www.gnu.org/licenses/>.
 */
package filius.gui.netzwerksicht;

import java.awt.Color;
import java.awt.Dimension;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import filius.gui.GUIContainer;

public class GUIPrintPanel extends JPanel {
    private static final int EMPTY_BORDER = 10;

    private static final long serialVersionUID = 1L;

    private GUINetworkPanel networkPanel = new GUINetworkPanel();
    private GUIDocumentationPanel docuPanel = new GUIDocumentationPanel();

    public GUIPrintPanel() {
        this.setSize(GUIContainer.FLAECHE_BREITE + 2 * EMPTY_BORDER, GUIContainer.FLAECHE_HOEHE + 2 * EMPTY_BORDER);
        setOpaque(false);

        add(networkPanel);
        networkPanel.setBounds(EMPTY_BORDER, EMPTY_BORDER, GUIContainer.FLAECHE_BREITE, GUIContainer.FLAECHE_HOEHE);

        add(docuPanel);
        docuPanel.setBounds(EMPTY_BORDER, EMPTY_BORDER, GUIContainer.FLAECHE_BREITE, GUIContainer.FLAECHE_HOEHE);
    }

    public void updateViewport(List<GUIKnotenItem> knoten, List<GUIKabelItem> kabel, List<GUIDocuItem> docuItems,
            boolean docuItemsEnabled) {
        networkPanel.updateViewport(knoten, kabel);
        docuPanel.updateViewport(docuItems, false);
    }

    public void updateViewport(List<GUIKnotenItem> knoten, List<GUIKabelItem> kabel, List<GUIDocuItem> docuItems,
            String footerText) {
        networkPanel.updateViewport(knoten, kabel);
        docuPanel.updateViewport(docuItems, false);

        JLabel footer = new JLabel(footerText);
        footer.setForeground(Color.lightGray);
        int footerWidth = footer.getFontMetrics(footer.getFont()).stringWidth(footerText) + 20;
        int footerHeight = footer.getFontMetrics(footer.getFont()).getHeight();
        int footerY = (int) Math.max(networkPanel.maxY, docuPanel.maxY) + 30;
        int footerX = (int) Math.min(networkPanel.minX, docuPanel.minX);
        footer.setBounds(footerX, footerY, footerWidth, footerHeight);
        docuPanel.add(footer);

        docuPanel.maxX = Math.max(footerX + footerWidth, docuPanel.maxX);
        docuPanel.maxY = footerY + footerHeight;
    }

    @Override
    public Dimension getSize() {
        return new Dimension(getWidth(), getHeight());
    }

    public int getClipY() {
        return (int) Math.min(networkPanel.minY, docuPanel.minY);
    }

    public int getClipHeight() {
        int totalMaxY = (int) Math.max(networkPanel.maxY, docuPanel.maxY);
        return totalMaxY - getClipY() + 2 * EMPTY_BORDER;
    }

    public int getClipWidth() {
        int totalMaxX = (int) Math.max(networkPanel.maxX, docuPanel.maxX);
        return totalMaxX - getClipX() + 2 * EMPTY_BORDER;
    }

    public int getClipX() {
        return (int) Math.min(networkPanel.minX, docuPanel.minX);
    }

    @Override
    public int getWidth() {
        return GUIContainer.FLAECHE_BREITE + 2 * EMPTY_BORDER;
    }

    @Override
    public int getHeight() {
        return GUIContainer.FLAECHE_HOEHE + 2 * EMPTY_BORDER;
    }
}
