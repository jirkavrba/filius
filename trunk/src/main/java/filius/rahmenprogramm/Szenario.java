package filius.rahmenprogramm;

import filius.software.vermittlungsschicht.IPVersion;

public class Szenario {
    private boolean geaendert;
    private String pfad;
    private final IPVersion ipVersion;

    Szenario(IPVersion ipVersion) {
        this.ipVersion = ipVersion;
    }

    public void reset() {
        geaendert = false;
        pfad = null;
    }

    public boolean isGeaendert() {
        return geaendert;
    }

    public void setGeaendert(boolean geaendert) {
        this.geaendert = geaendert;
    }

    public String getPfad() {
        return pfad;
    }

    public void setPfad(String pfad) {
        this.pfad = pfad;
    }

    public IPVersion getIpVersion() {
        return ipVersion;
    }
}
