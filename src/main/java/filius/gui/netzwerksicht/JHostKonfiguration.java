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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;

import org.apache.commons.lang3.StringUtils;

import filius.Main;
import filius.gui.GUIContainer;
import filius.gui.JMainFrame;
import filius.gui.ValidateableTextField;
import filius.hardware.Hardware;
import filius.hardware.NetzwerkInterface;
import filius.hardware.knoten.Host;
import filius.rahmenprogramm.I18n;
import filius.software.system.Betriebssystem;
import filius.software.vermittlungsschicht.IPAddress;
import filius.software.vermittlungsschicht.IPVersion;

@SuppressWarnings("serial")
public class JHostKonfiguration extends JKonfiguration implements I18n {

    private static final int LABEL_WIDTH = 160;
    private ValidateableTextField name;
    private ValidateableTextField macAdresse;
    private ValidateableTextField addressIPv4;
    private ValidateableTextField netmaskIPv4;
    private ValidateableTextField gatewayIPv4;
    private ValidateableTextField dnsIPv4;
    private ValidateableTextField addressIPv6;
    private ValidateableTextField netmaskIPv6;
    private ValidateableTextField gatewayIPv6;
    private ValidateableTextField dnsIPv6;
    private JCheckBox dhcp;
    private JButton dhcpIPv4Button;
    private JButton dhcpIPv6Button;
    private JCheckBox useIpAsName;

    protected JHostKonfiguration(Hardware hardware) {
        super(hardware);
    }

    private void aendereAnzeigeName() {
        if (holeHardware() != null) {
            Host host = (Host) holeHardware();
            host.setUseIPAsName(useIpAsName.isSelected());
        }

        GUIContainer.getGUIContainer().updateViewport();
        updateAttribute();
    }

    /** Diese Methode wird vom JAendernButton aufgerufen */
    @Override
    public void aenderungenAnnehmen() {
        if (holeHardware() != null) {
            Host host = (Host) holeHardware();
            if (!useIpAsName.isSelected()) {
                host.setName(name.getText());
            }
            NetzwerkInterface nic = host.getNetzwerkInterfaces().get(0);
            nic.setIp(addressIPv4.getText());
            nic.setSubnetzMaske(netmaskIPv4.getText());
            nic.setGateway(gatewayIPv4.getText());
            nic.setDns(dnsIPv4.getText());
            nic.setIPv6(addressIPv6.getText());
            nic.setIPv6SubnetzMaske(netmaskIPv6.getText());
            nic.setIPv6Gateway(gatewayIPv6.getText());
            nic.setIPv6Dns(dnsIPv6.getText());

            Betriebssystem bs = (Betriebssystem) host.getSystemSoftware();
            bs.setDHCPKonfiguration(dhcp.isSelected());
            if (dhcp.isSelected()) {
                bs.getDHCPServer().setAktiv(false);
            }

        } else {
            Main.debug.println("GUIRechnerKonfiguration: Aenderungen konnten nicht uebernommen werden.");
        }
        GUIContainer.getGUIContainer().updateViewport();
        updateAttribute();
    }

    protected void initAttributEingabeBox(Box box, Box rightBox) {
        JLabel tempLabel;
        Box tempBox;

        name = addConfigParameter(box, messages.getString("jhostkonfiguration_msg1"),
                messages.getString("jhostkonfiguration_msg2"), true);
        macAdresse = addConfigParameter(box, messages.getString("jhostkonfiguration_msg9"), "", false);

        Box ipConfigBox = Box.createHorizontalBox();
        box.add(ipConfigBox);
        Box ipv4Box = Box.createVerticalBox();
        ipConfigBox.add(ipv4Box);

        addressIPv4 = addConfigParameter(ipv4Box, messages.getString("jhostkonfiguration_msg3"), IPAddress
                .defaultAddress(IPVersion.IPv4).normalizedAddress(), true);
        addressIPv4.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                checkIpAddress();
            }
        });
        netmaskIPv4 = addConfigParameter(ipv4Box, messages.getString("jhostkonfiguration_msg4"), IPAddress
                .defaultAddress(IPVersion.IPv4).netmask(), true);
        netmaskIPv4.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                checkNetmask();
            }
        });
        gatewayIPv4 = addConfigParameter(ipv4Box, messages.getString("jhostkonfiguration_msg5"), IPAddress
                .defaultAddress(IPVersion.IPv4).normalizedAddress(), true);
        gatewayIPv4.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                checkGatewayAddress();
            }
        });
        dnsIPv4 = addConfigParameter(ipv4Box, messages.getString("jhostkonfiguration_msg6"),
                IPAddress.defaultAddress(IPVersion.IPv4).normalizedAddress(), true);
        dnsIPv4.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                checkDnsAddress();
            }
        });

        Box ipv6Box = Box.createVerticalBox();
        ipConfigBox.add(ipv6Box);
        addressIPv6 = addConfigParameter(ipv6Box, messages.getString("jhostkonfiguration_msg3"), IPAddress
                .defaultAddress(IPVersion.IPv6).normalizedAddress(), true);
        addressIPv6.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                checkIpAddress();
            }
        });
        netmaskIPv6 = addConfigParameter(ipv6Box, messages.getString("jhostkonfiguration_msg4"), IPAddress
                .defaultAddress(IPVersion.IPv6).netmask(), true);
        netmaskIPv6.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                checkNetmask();
            }
        });
        gatewayIPv6 = addConfigParameter(ipv6Box, messages.getString("jhostkonfiguration_msg5"), IPAddress
                .defaultAddress(IPVersion.IPv6).normalizedAddress(), true);
        gatewayIPv6.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                checkGatewayAddress();
            }
        });
        dnsIPv6 = addConfigParameter(ipv6Box, messages.getString("jhostkonfiguration_msg6"),
                IPAddress.defaultAddress(IPVersion.IPv6).normalizedAddress(), true);
        dnsIPv6.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                checkDnsAddress();
            }
        });

        tempLabel = new JLabel(messages.getString("jhostkonfiguration_msg10"));
        tempLabel.setPreferredSize(new Dimension(LABEL_WIDTH, 10));
        tempLabel.setVisible(true);
        tempLabel.setOpaque(false);
        tempLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);

        useIpAsName = new JCheckBox();
        useIpAsName.setOpaque(false);
        useIpAsName.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                aendereAnzeigeName();
            }
        });

        tempBox = Box.createHorizontalBox();
        tempBox.setOpaque(false);
        tempBox.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        tempBox.setPreferredSize(new Dimension(400, 35));
        tempBox.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        tempBox.add(useIpAsName);
        tempBox.add(Box.createHorizontalStrut(5)); // Platz zw. tempLabel und
        tempBox.add(tempLabel);
        rightBox.add(tempBox, BorderLayout.NORTH);

        // =======================================================
        // Attribut Verwendung von DHCP
        tempLabel = new JLabel(messages.getString("jhostkonfiguration_msg7"));
        tempLabel.setPreferredSize(new Dimension(LABEL_WIDTH, 10));
        tempLabel.setVisible(true);
        tempLabel.setOpaque(false);
        tempLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);

        dhcp = new JCheckBox();
        dhcp.setSelected(false);
        dhcp.setOpaque(false);
        dhcp.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                aenderungenAnnehmen();
            }
        });

        tempBox = Box.createHorizontalBox();
        tempBox.setOpaque(false);
        tempBox.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        tempBox.setPreferredSize(new Dimension(400, 35));
        tempBox.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        tempBox.add(dhcp);
        tempBox.add(Box.createHorizontalStrut(5)); // Platz zw. tempLabel und
        tempBox.add(tempLabel);
        rightBox.add(tempBox, BorderLayout.NORTH);

        rightBox.add(Box.createVerticalStrut(10));

        // ===================================================
        // DHCP-Server einrichten
        tempBox = Box.createHorizontalBox();
        tempBox.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        tempBox.setOpaque(false);

        dhcpIPv4Button = new JButton(messages.getString("jhostkonfiguration_msg8"));
        dhcpIPv4Button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showDhcpConfiguration(IPVersion.IPv4);
            }
        });
        dhcpIPv6Button = new JButton(messages.getString("jhostkonfiguration_msg8"));
        dhcpIPv6Button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showDhcpConfiguration(IPVersion.IPv6);
            }
        });
        tempBox.add(dhcpIPv4Button);

        rightBox.add(tempBox);

        tempBox = Box.createVerticalBox();
        tempBox.setOpaque(false);
        tempBox.setPreferredSize(new Dimension(400, 120));
        rightBox.add(tempBox);

        updateAttribute();
    }

    private ValidateableTextField addConfigParameter(Box box, String label, String defaultValue, boolean editable) {
        JLabel tempLabel = new JLabel(label);
        tempLabel.setPreferredSize(new Dimension(LABEL_WIDTH, 10));
        tempLabel.setVisible(true);
        tempLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);

        ValidateableTextField textField = new ValidateableTextField(defaultValue);

        textField.setEditable(editable);
        if (editable) {
            textField.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    aenderungenAnnehmen();
                }
            });
            textField.addFocusListener(new FocusListener() {
                public void focusGained(FocusEvent arg0) {}

                public void focusLost(FocusEvent arg0) {
                    aenderungenAnnehmen();
                }
            });
        }

        Box tempBox = Box.createHorizontalBox();
        tempBox.setOpaque(false);
        tempBox.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        tempBox.setMaximumSize(new Dimension(400, 40));
        tempBox.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        tempBox.add(tempLabel);
        tempBox.add(Box.createHorizontalStrut(5)); // Platz zw. tempLabel und
        tempBox.add(textField);
        box.add(tempBox, BorderLayout.NORTH);
        return textField;
    }

    private void showDhcpConfiguration(IPVersion ipVersion) {
        JDHCPKonfiguration dhcpKonfig = new JDHCPKonfiguration(JMainFrame.getJMainFrame(),
                messages.getString("jhostkonfiguration_msg8"),
                (Betriebssystem) ((Host) holeHardware()).getSystemSoftware(), ipVersion);
        dhcpKonfig.setVisible(true);
    }

    private void checkIpAddress() {
        addressIPv4.setValid(IPAddress.verifyAddress(addressIPv4.getText(), IPVersion.IPv4));
        addressIPv6.setValid(IPAddress.verifyAddress(addressIPv6.getText(), IPVersion.IPv6));
    }

    private void checkDnsAddress() {
        dnsIPv4.setValid(StringUtils.isBlank(dnsIPv4.getText())
                || IPAddress.verifyAddress(dnsIPv4.getText(), IPVersion.IPv4));
        dnsIPv6.setValid(StringUtils.isBlank(dnsIPv6.getText())
                || IPAddress.verifyAddress(dnsIPv6.getText(), IPVersion.IPv6));
    }

    private void checkGatewayAddress() {
        gatewayIPv4.setValid(StringUtils.isBlank(gatewayIPv4.getText())
                || IPAddress.verifyAddress(gatewayIPv4.getText(), IPVersion.IPv4));
        gatewayIPv6.setValid(StringUtils.isBlank(gatewayIPv6.getText())
                || IPAddress.verifyAddress(gatewayIPv6.getText(), IPVersion.IPv6));
    }

    private void checkNetmask() {
        netmaskIPv4.setValid(IPAddress.verifyNetmaskDefinition(netmaskIPv4.getText(), IPVersion.IPv4));
        netmaskIPv6.setValid(IPAddress.verifyNetmaskDefinition(netmaskIPv6.getText(), IPVersion.IPv6));
    }

    public void updateAttribute() {
        if (holeHardware() != null) {
            Host host = (Host) holeHardware();
            name.setText(host.holeAnzeigeName());
            useIpAsName.setSelected(host.isUseIPAsName());
            name.setEnabled(!host.isUseIPAsName());

            Betriebssystem bs = (Betriebssystem) host.getSystemSoftware();
            NetzwerkInterface nic = host.getNetzwerkInterfaces().get(0);
            macAdresse.setText(nic.getMac());
            addressIPv4.setText(nic.getIp());
            netmaskIPv4.setText(nic.getSubnetzMaske());
            gatewayIPv4.setText(nic.getGateway());
            dnsIPv4.setText(nic.getDns());
            addressIPv6.setText(nic.getIPv6());
            netmaskIPv6.setText(nic.getIPv6SubnetzMaske());
            gatewayIPv6.setText(nic.getIPv6Gateway());
            dnsIPv6.setText(nic.getIPv6Dns());

            dhcp.setSelected(bs.isDHCPKonfiguration());
            dhcpIPv4Button.setEnabled(!dhcp.isSelected());
            dhcpIPv6Button.setEnabled(!dhcp.isSelected());

            addressIPv4.setEnabled(!bs.isDHCPKonfiguration());
            netmaskIPv4.setEnabled(!bs.isDHCPKonfiguration());
            gatewayIPv4.setEnabled(!bs.isDHCPKonfiguration());
            dnsIPv4.setEnabled(!bs.isDHCPKonfiguration());
            addressIPv6.setEnabled(!bs.isDHCPKonfiguration());
            netmaskIPv6.setEnabled(!bs.isDHCPKonfiguration());
            gatewayIPv6.setEnabled(!bs.isDHCPKonfiguration());
            dnsIPv6.setEnabled(!bs.isDHCPKonfiguration());

            checkIpAddress();
            checkDnsAddress();
            checkGatewayAddress();
            checkNetmask();
        } else {
            Main.debug.println("GUIRechnerKonfiguration: keine Hardware-Komponente vorhanden");
        }
    }
}
