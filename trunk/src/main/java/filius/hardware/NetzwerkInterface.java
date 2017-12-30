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
package filius.hardware;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import filius.exception.InvalidParameterException;
import filius.rahmenprogramm.Information;
import filius.software.vermittlungsschicht.IPAddress;
import filius.software.vermittlungsschicht.IPVersion;

public class NetzwerkInterface implements Serializable {
    private static final Logger LOG = LoggerFactory.getLogger(NetzwerkInterface.class);

    private static final long serialVersionUID = 1L;
    private String mac;
    private IPAddress addressIPv4 = IPAddress.defaultAddress(IPVersion.IPv4);
    private IPAddress gatewayIPv4;
    private IPAddress dnsIPv4;
    private IPAddress addressIPv6 = IPAddress.defaultAddress(IPVersion.IPv6);
    private IPAddress gatewayIPv6;
    private IPAddress dnsIPv6;
    private Port anschluss;

    public NetzwerkInterface() {
        setMac(Information.getInformation().holeFreieMACAdresse());
        setGateway("");
        setDns("");
        anschluss = new Port(this);
    }

    public Port getPort() {
        return anschluss;
    }

    public void setPort(Port port) {
        this.anschluss = port;
    }

    /**
     * IPv4-Adresse des DNS-Servers, der zur Aufloesung von Domainnamen verwendet wird.
     */
    public String getDns() {
        return dnsIPv4 == null ? "" : dnsIPv4.address();
    }

    /**
     * IPv4-Adresse des DNS-Servers, der zur Aufloesung von Domainnamen verwendet wird.
     */
    public void setDns(String dns) {
        try {
            this.dnsIPv4 = new IPAddress(dns);
        } catch (InvalidParameterException e) {
            LOG.info("Could not set invalid IPv4 DNS server address " + dns, e);
        }
    }

    /**
     * IPv6-Adresse des DNS-Servers, der zur Aufloesung von Domainnamen verwendet wird.
     */
    public String getIPv6Dns() {
        return dnsIPv6 == null ? "" : dnsIPv6.address();
    }

    /**
     * IPv6-Adresse des DNS-Servers, der zur Aufloesung von Domainnamen verwendet wird.
     */
    public void setIPv6Dns(String dns) {
        try {
            this.dnsIPv6 = new IPAddress(dns);
        } catch (InvalidParameterException e) {
            LOG.info("Could not set invalid IPv6 DNS server address " + dns, e);
        }
    }

    /** IPv4-Adresse der Netzwerkschnittstelle */
    public String getIp() {
        return addressIPv4.toString();
    }

    public IPAddress addressIPv4() {
        return addressIPv4;
    }

    /** IPv4-Adresse der Netzwerkschnittstelle */
    public void setIp(String ip) {
        try {
            // In order to be compliant with older project files where IP address and netmask are stored separately
            this.addressIPv4 = new IPAddress(ip, addressIPv4.netmask());
        } catch (InvalidParameterException e1) {
            try {
                this.addressIPv4 = new IPAddress(ip);
            } catch (InvalidParameterException e) {
                LOG.info("Could not set invalid IPv4 address " + ip, e);
            }
        }
    }

    /** IPv6-Adresse der Netzwerkschnittstelle */
    public String getIPv6() {
        return addressIPv6.toString();
    }

    public IPAddress addressIPv6() {
        return addressIPv6;
    }

    /** IPv6-Adresse der Netzwerkschnittstelle */
    public void setIPv6(String ip) {
        try {
            this.addressIPv6 = new IPAddress(ip);
        } catch (InvalidParameterException e) {
            LOG.info("Could not set invalid IPv6 address " + ip, e);
        }
    }

    public String getMac() {
        return mac.toUpperCase();
    }

    public void setMac(String mac) {
        if (mac != null) {
            Information.getInformation().macHinzufuegen(mac);
            this.mac = mac;
        }
    }

    /**
     * This method is used to read old project files with separated attributes for IPv4 address and netmask!
     * 
     * @deprecated Use {@link setIp()} instead.
     */
    @Deprecated
    public void setSubnetzMaske(String subnetzMaske) {
        try {
            this.addressIPv4 = new IPAddress(addressIPv4.address(), subnetzMaske);
        } catch (InvalidParameterException e) {
            LOG.info("Could not set invalid IPv4 netmask" + subnetzMaske, e);
        }
    }

    /**
     * IPv4-Adresse des Standard-Gateways. Dorthin werden alle Pakete gesendet, fuer dessen Zieladresse kein anderer
     * Eintrag in der Weiterleitungstabelle vorhanden ist.
     */
    public String getGateway() {
        return gatewayIPv4 == null ? "" : gatewayIPv4.address();
    }

    /**
     * IPv4-Adresse des Standard-Gateways. Dorthin werden alle Pakete gesendet, fuer dessen Zieladresse kein anderer
     * Eintrag in der Weiterleitungstabelle vorhanden ist.
     */
    public void setGateway(String gateway) {
        try {
            this.gatewayIPv4 = new IPAddress(gateway);
        } catch (InvalidParameterException e) {
            LOG.info("Could not set invalid IPv4 default gateway" + gateway, e);
        }
    }

    /**
     * IPv6-Adresse des Standard-Gateways. Dorthin werden alle Pakete gesendet, fuer dessen Zieladresse kein anderer
     * Eintrag in der Weiterleitungstabelle vorhanden ist.
     */
    public String getIPv6Gateway() {
        return gatewayIPv6 == null ? "" : gatewayIPv6.address();
    }

    /**
     * IPv6-Adresse des Standard-Gateways. Dorthin werden alle Pakete gesendet, fuer dessen Zieladresse kein anderer
     * Eintrag in der Weiterleitungstabelle vorhanden ist.
     */
    public void setIPv6Gateway(String gateway) {
        try {
            this.gatewayIPv6 = new IPAddress(gateway);
        } catch (InvalidParameterException e) {
            LOG.info("Could not set invalid IPv6 default gateway" + gateway, e);
        }
    }
}
