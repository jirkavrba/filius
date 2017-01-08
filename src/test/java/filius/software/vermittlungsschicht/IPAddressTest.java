package filius.software.vermittlungsschicht;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import filius.exception.InvalidParameterException;

public class IPAddressTest {

    @Test
    public void testConstructor_IPv4() throws Exception {
        String ipAddressAsString = "192.168.1.1";
        IPAddress address = new IPAddress(ipAddressAsString);

        assertThat(address.toString(), is(ipAddressAsString));
    }

    @Test
    public void testConstructor_IPv4_WithNetmask() throws Exception {
        String ipAddressAsString = "192.168.1.1/24";
        IPAddress address = new IPAddress(ipAddressAsString);

        assertThat(address.toString(), is(ipAddressAsString));
    }

    @Test(expected = InvalidParameterException.class)
    public void testConstructor_IPv4_InvalidSyntax() throws Exception {
        new IPAddress("1.1.1");
    }

    @Test
    public void testConstructor_IPv6() throws Exception {
        String ipAddressAsString = "1:2:3:4:5:6:7:8";
        IPAddress address = new IPAddress(ipAddressAsString);

        assertThat(address.toString(), is(ipAddressAsString));
    }

    @Test
    public void testConstructor_IPv6_WithPENDINGZeroSequencePlaceholder() throws Exception {
        String ipAddressAsString = "1::";
        IPAddress address = new IPAddress(ipAddressAsString);

        assertThat(address.toString(), is("1:0:0:0:0:0:0:0"));
    }

    @Test
    public void testConstructor_IPv6_WithLEADINGZeroSequencePlaceholder() throws Exception {
        String ipAddressAsString = "::1";
        IPAddress address = new IPAddress(ipAddressAsString);

        assertThat(address.toString(), is("0:0:0:0:0:0:0:1"));
    }

    @Test
    public void testConstructor_IPv6_WithMIDDLEZeroSequencePlaceholder() throws Exception {
        String ipAddressAsString = "1::8";
        IPAddress address = new IPAddress(ipAddressAsString);

        assertThat(address.toString(), is("1:0:0:0:0:0:0:8"));
    }

    @Test
    public void testConstructor_IPv6_WithIPv4Notation() throws Exception {
        String ipAddressAsString = "1:2:3:4:5:6:192.168.1.1";
        IPAddress address = new IPAddress(ipAddressAsString);

        assertThat(address.toString(), is(ipAddressAsString));
    }

    @Test
    public void testConstructor_IPv6_WithHexadecimalDigits() throws Exception {
        String ipAddressAsString = "AAaa:BBbb:CCcc:DDdd:a111:b222:c333:d444";
        IPAddress address = new IPAddress(ipAddressAsString);

        assertThat(address.toString(), is(ipAddressAsString.toLowerCase()));
    }

    @Test
    public void testConstructor_IPv6_WithNetmask() throws Exception {
        String ipAddressAsString = "1:2:3:4:5:6:7:8/24";
        IPAddress address = new IPAddress(ipAddressAsString);

        assertThat(address.toString(), is(ipAddressAsString));
    }

    @Test(expected = InvalidParameterException.class)
    public void testConstructor_IPv6_InvalidSyntax() throws Exception {
        new IPAddress("1:2:3:4:5:6:7");
    }

    @Test
    public void testEquals_DifferentNotations() throws Exception {
        IPAddress address1 = new IPAddress("1:2:3:4:5:6:1.1.1.1");
        IPAddress address2 = new IPAddress("1:2:3:4:5:6:101:101");

        assertThat(address1, is(address2));
    }

    @Test
    public void testEquals_DifferentNetworkMask() throws Exception {
        IPAddress address1 = new IPAddress("1:2:3:4:5:6:1.1.1.1/16");
        IPAddress address2 = new IPAddress("1:2:3:4:5:6:1.1.1.1/10");

        assertThat(address1, not(is(address2)));
    }

    @Test
    public void testIsLoopbackAddress_IPv4_Yes() throws Exception {
        assertTrue(new IPAddress("127.0.0.1/16").isLoopbackAddress());
        assertTrue(new IPAddress("127.0.0.1/8").isLoopbackAddress());
        assertTrue(new IPAddress("127.0.0.1/0").isLoopbackAddress());
        assertTrue(new IPAddress("127.255.0.42").isLoopbackAddress());
    }

    @Test
    public void testIsLoopbackAddress_IPv4_No() throws Exception {
        assertFalse(new IPAddress("128.255.0.42").isLoopbackAddress());
        assertFalse(new IPAddress("128.0.0.1/16").isLoopbackAddress());
    }

    @Test
    public void testIsLoopbackAddress_IPv6_Yes() throws Exception {
        assertTrue(new IPAddress("0:0:0:0:0:0:0:1/16").isLoopbackAddress());
        assertTrue(new IPAddress("::1/16").isLoopbackAddress());
        assertTrue(new IPAddress("::1/8").isLoopbackAddress());
    }

    @Test
    public void testIsLoopbackAddress_IPv6_No() throws Exception {
        assertFalse(new IPAddress("0:0:0:0:0:0:0:2/16").isLoopbackAddress());
        assertFalse(new IPAddress("1:0:0:0:0:0:0:1/16").isLoopbackAddress());
    }
}
