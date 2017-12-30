package filius.hardware;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class NetzwerkInterfaceTest {

    @Test
    public void testSetIp() throws Exception {
        NetzwerkInterface nic = new NetzwerkInterface();

        nic.setIp("1.2.3.4");

        assertThat(nic.getIp(), startsWith("1.2.3.4"));
    }

    @Test
    public void testSetIp_setNetworkMaskAfterwards() throws Exception {
        NetzwerkInterface nic = new NetzwerkInterface();

        nic.setIp("1.2.3.4");
        nic.setSubnetzMaske("255.128.0.0");

        assertThat(nic.getIp(), is("1.2.3.4/9"));
    }

    @Test
    public void testSetIp_setNetworkMaskBeforehand() throws Exception {
        NetzwerkInterface nic = new NetzwerkInterface();

        nic.setSubnetzMaske("255.128.0.0");
        nic.setIp("1.2.3.4");

        assertThat(nic.getIp(), is("1.2.3.4/9"));
    }

}
