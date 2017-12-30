package filius.rahmenprogramm;

import java.time.LocalDateTime;

public class Scenario {
    private Boolean geaendert = Boolean.FALSE;
    private String ipVersion;
    private String created;
    private String lastChanged;
    private String author;

    public Scenario() {}

    public static Scenario createNew() {
        Scenario scenario = new Scenario();
        scenario.setAuthor(System.getProperty("user.name"));
        scenario.setIpVersion(Information.getDefaultIPVersion().toString());
        scenario.setCreated(LocalDateTime.now().toString());
        scenario.setLastChanged(scenario.getCreated());
        return scenario;
    }

    public Boolean isGeaendert() {
        return geaendert;
    }

    public void setGeaendert(Boolean geaendert) {
        this.geaendert = geaendert;
        lastChanged = LocalDateTime.now().toString();
        if (null == created) {
            created = LocalDateTime.now().toString();
            author = System.getProperty("user.name");
        }
    }

    public String getIpVersion() {
        return ipVersion;
    }

    public void setIpVersion(String ipVersion) {
        this.ipVersion = ipVersion;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getLastChanged() {
        return lastChanged;
    }

    public void setLastChanged(String lastChanged) {
        this.lastChanged = lastChanged;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
