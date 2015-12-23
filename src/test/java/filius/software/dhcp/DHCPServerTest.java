package filius.software.dhcp;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DHCPServerTest {

    @Test
    public void testInRange() throws Exception {
        DHCPServer server = new DHCPServer();
        server.setUntergrenze("1.1.1.1");
        server.setObergrenze("1.1.1.1");
        assertTrue(server.inRange("1.1.1.1"));
    }

    @Test
    public void testInRange_ComplexRange() throws Exception {
        DHCPServer server = new DHCPServer();
        server.setUntergrenze("1.10.100.200");
        server.setObergrenze("200.100.10.1");
        assertTrue(server.inRange("50.5.20.7"));
    }

    @Test
    public void testInRange_OverUpperLimit() throws Exception {
        DHCPServer server = new DHCPServer();
        server.setUntergrenze("1.1.1.1");
        server.setObergrenze("1.1.1.2");
        assertFalse(server.inRange("1.1.1.3"));
    }

    @Test
    public void testInRange_BelowLowerLimit() throws Exception {
        DHCPServer server = new DHCPServer();
        server.setUntergrenze("1.1.1.10");
        server.setObergrenze("1.1.1.12");
        assertFalse(server.inRange("1.1.1.9"));
    }

    @Test
    public void testInRange_BelowLowerLimitInFirstPart() throws Exception {
        DHCPServer server = new DHCPServer();
        server.setUntergrenze("2.1.1.10");
        server.setObergrenze("3.1.1.12");
        assertFalse(server.inRange("1.1.1.11"));
    }

    @Test
    public void testIpToIntArray() throws Exception {
        int[] ipAsIntArray = DHCPServer.ipToIntArray("1.2.3.4");

        assertThat(ipAsIntArray.length, is(4));
        assertThat(ipAsIntArray[0], is(1));
        assertThat(ipAsIntArray[1], is(2));
        assertThat(ipAsIntArray[2], is(3));
        assertThat(ipAsIntArray[3], is(4));
    }

    @Test(expected = NumberFormatException.class)
    public void testIpToIntArray_InvalidNumberInAddress() throws Exception {
        DHCPServer.ipToIntArray("1.2.3.x");
    }

    @Test(expected = NumberFormatException.class)
    public void testIpToIntArray_NotAnIpAddress() throws Exception {
        DHCPServer.ipToIntArray("hallo.welt");
    }

    @Test(expected = NumberFormatException.class)
    public void testIpToIntArray_TooManyParts() throws Exception {
        DHCPServer.ipToIntArray("1.2.3.4.5");
    }

    @Test
    public void testIpToLong() throws Exception {
        assertThat(DHCPServer.ipToLong("1.2.3.4"), is(0x1020304l));
    }
}
