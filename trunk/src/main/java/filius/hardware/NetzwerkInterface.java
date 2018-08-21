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

import filius.exception.InvalidParameterException;
import filius.rahmenprogramm.Information;
import filius.software.vermittlungsschicht.IPAddress;
import filius.software.vermittlungsschicht.IPVersion;

@SuppressWarnings("serial")
public class NetzwerkInterface implements Serializable {
    private String mac;
    private String addressIPv4 = IPAddress.defaultAddress(IPVersion.IPv4).address();
    private String netmaskIPv4 = IPAddress.defaultAddress(IPVersion.IPv4).netmask();
    private String gatewayIPv4;
    private String dnsIPv4;
    private String addressIPv6 = IPAddress.defaultAddress(IPVersion.IPv6).address();
    private String netmaskIPv6 = IPAddress.defaultAddress(IPVersion.IPv6).netmask();
    private String gatewayIPv6;
    private String dnsIPv6;
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
        return dnsIPv4 == null ? "" : dnsIPv4;
    }

    /**
     * IPv4-Adresse des DNS-Servers, der zur Aufloesung von Domainnamen verwendet wird.
     */
    public void setDns(String dns) {
        this.dnsIPv4 = dns;
    }

    /**
     * IPv6-Adresse des DNS-Servers, der zur Aufloesung von Domainnamen verwendet wird.
     */
    public String getIPv6Dns() {
        return dnsIPv6 == null ? "" : dnsIPv6;
    }

    /**
     * IPv6-Adresse des DNS-Servers, der zur Aufloesung von Domainnamen verwendet wird.
     */
    public void setIPv6Dns(String dns) {
        this.dnsIPv6 = dns;
    }

    /** IPv4-Adresse der Netzwerkschnittstelle */
    public String getIp() {
        return addressIPv4;
    }

    public IPAddress addressIPv4() throws InvalidParameterException {
        return new IPAddress(addressIPv4, netmaskIPv4);
    }

    /** IPv4-Adresse der Netzwerkschnittstelle */
    public void setIp(String ip) {
        this.addressIPv4 = ip;
    }

    /** IPv6-Adresse der Netzwerkschnittstelle */
    public String getIPv6() {
        return addressIPv6;
    }

    public IPAddress addressIPv6() throws InvalidParameterException {
        return new IPAddress(addressIPv6, netmaskIPv6);
    }

    /** IPv6-Adresse der Netzwerkschnittstelle */
    public void setIPv6(String ip) {
        this.addressIPv6 = ip;
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

    public void setSubnetzMaske(String subnetzMaske) {
        this.netmaskIPv4 = subnetzMaske;
    }

    public String getSubnetzMaske() {
        return netmaskIPv4;
    }

    public void setIPv6SubnetzMaske(String subnetzMaske) {
        this.netmaskIPv6 = subnetzMaske;
    }

    public String getIPv6SubnetzMaske() {
        return netmaskIPv6;
    }

    /**
     * IPv4-Adresse des Standard-Gateways. Dorthin werden alle Pakete gesendet, fuer dessen Zieladresse kein anderer
     * Eintrag in der Weiterleitungstabelle vorhanden ist.
     */
    public String getGateway() {
        return gatewayIPv4 == null ? "" : gatewayIPv4;
    }

    /**
     * IPv4-Adresse des Standard-Gateways. Dorthin werden alle Pakete gesendet, fuer dessen Zieladresse kein anderer
     * Eintrag in der Weiterleitungstabelle vorhanden ist.
     */
    public void setGateway(String gateway) {
        this.gatewayIPv4 = gateway;
    }

    /**
     * IPv6-Adresse des Standard-Gateways. Dorthin werden alle Pakete gesendet, fuer dessen Zieladresse kein anderer
     * Eintrag in der Weiterleitungstabelle vorhanden ist.
     */
    public String getIPv6Gateway() {
        return gatewayIPv6 == null ? "" : gatewayIPv6;
    }

    /**
     * IPv6-Adresse des Standard-Gateways. Dorthin werden alle Pakete gesendet, fuer dessen Zieladresse kein anderer
     * Eintrag in der Weiterleitungstabelle vorhanden ist.
     */
    public void setIPv6Gateway(String gateway) {
        this.gatewayIPv6 = gateway;
    }
}
