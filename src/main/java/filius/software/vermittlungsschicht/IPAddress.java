package filius.software.vermittlungsschicht;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import filius.exception.InvalidParameterException;

public class IPAddress {
    private static final String GROUP_NETMASK = "netmask";
    private static final String[] GROUP_SEGMENT = { "segment0", "segment1", "segment2", "segment3", "segment4",
            "segment5", "segment6", "segment7" };
    private static final String BEFORE_ZERO_SEQ_PLACEHOLDER = "before";
    private static final String AFTER_ZERO_SEQ_PLACEHOLDER = "after";
    private static final String[] GROUP_BYTE = { "byte0", "byte1", "byte2", "byte3" };
    private static final String IP_V4_REGEX = "(?<" + GROUP_BYTE[0] + ">[0-9]{1,3})\\.(?<" + GROUP_BYTE[1]
            + ">[0-9]{1,3})\\.(?<" + GROUP_BYTE[2] + ">[0-9]{1,3})\\.(?<" + GROUP_BYTE[3] + ">[0-9]{1,3})";
    private static final String IP_V6_START_SEG_REGEX = "((?<" + BEFORE_ZERO_SEQ_PLACEHOLDER + GROUP_SEGMENT[0]
            + ">[0-9a-fA-F]{1,4})(:(?<" + BEFORE_ZERO_SEQ_PLACEHOLDER + GROUP_SEGMENT[1] + ">[0-9a-fA-F]{1,4})(:(?<"
            + BEFORE_ZERO_SEQ_PLACEHOLDER + GROUP_SEGMENT[2] + ">[0-9a-fA-F]{1,4})(:(?<" + BEFORE_ZERO_SEQ_PLACEHOLDER
            + GROUP_SEGMENT[3] + ">[0-9a-fA-F]{1,4})(:(?<" + BEFORE_ZERO_SEQ_PLACEHOLDER + GROUP_SEGMENT[4]
            + ">[0-9a-fA-F]{1,4})(:(?<" + BEFORE_ZERO_SEQ_PLACEHOLDER + GROUP_SEGMENT[5] + ">[0-9a-fA-F]{1,4})(:(?<"
            + BEFORE_ZERO_SEQ_PLACEHOLDER + GROUP_SEGMENT[6] + ">[0-9a-fA-F]{1,4})(:(?<" + BEFORE_ZERO_SEQ_PLACEHOLDER
            + GROUP_SEGMENT[7] + ">[0-9a-fA-F]{1,4}))?)?)?)?)?)?)?)?";
    private static final String IP_V6_START_SEG_WITH_DEC_NOTATION_REGEX = "((?<" + BEFORE_ZERO_SEQ_PLACEHOLDER
            + GROUP_SEGMENT[0] + ">[0-9a-fA-F]{1,4})(:(?<" + BEFORE_ZERO_SEQ_PLACEHOLDER + GROUP_SEGMENT[1]
            + ">[0-9a-fA-F]{1,4})(:(?<" + BEFORE_ZERO_SEQ_PLACEHOLDER + GROUP_SEGMENT[2] + ">[0-9a-fA-F]{1,4})(:(?<"
            + BEFORE_ZERO_SEQ_PLACEHOLDER + GROUP_SEGMENT[3] + ">[0-9a-fA-F]{1,4})(:(?<" + BEFORE_ZERO_SEQ_PLACEHOLDER
            + GROUP_SEGMENT[4] + ">[0-9a-fA-F]{1,4})(:(?<" + BEFORE_ZERO_SEQ_PLACEHOLDER + GROUP_SEGMENT[5]
            + ">[0-9a-fA-F]{1,4}))?)?)?)?)?)?";
    private static final String IP_V6_END_SEG_PART_REGEX = "(::(?<" + AFTER_ZERO_SEQ_PLACEHOLDER + GROUP_SEGMENT[0]
            + ">)?((((((((?<" + AFTER_ZERO_SEQ_PLACEHOLDER + GROUP_SEGMENT[1] + ">[0-9a-fA-F]{1,4}):)?(?<"
            + AFTER_ZERO_SEQ_PLACEHOLDER + GROUP_SEGMENT[2] + ">[0-9a-fA-F]{1,4}):)?(?<" + AFTER_ZERO_SEQ_PLACEHOLDER
            + GROUP_SEGMENT[3] + ">[0-9a-fA-F]{1,4}):)?(?<" + AFTER_ZERO_SEQ_PLACEHOLDER + GROUP_SEGMENT[4]
            + ">[0-9a-fA-F]{1,4}):)?(?<" + AFTER_ZERO_SEQ_PLACEHOLDER + GROUP_SEGMENT[5] + ">[0-9a-fA-F]{1,4}):)?(?<"
            + AFTER_ZERO_SEQ_PLACEHOLDER + GROUP_SEGMENT[6] + ">[0-9a-fA-F]{1,4}):)?(?<" + AFTER_ZERO_SEQ_PLACEHOLDER
            + GROUP_SEGMENT[7] + ">[0-9a-fA-F]{1,4}))?)?";
    private static final String IP_V6_END_SEG_PART_WITH_DEC_NOTATION_REGEX = "(::(?<" + AFTER_ZERO_SEQ_PLACEHOLDER
            + GROUP_SEGMENT[0] + ">)?((((((?<" + AFTER_ZERO_SEQ_PLACEHOLDER + GROUP_SEGMENT[1]
            + ">[0-9a-fA-F]{1,4}):)?(?<" + AFTER_ZERO_SEQ_PLACEHOLDER + GROUP_SEGMENT[2] + ">[0-9a-fA-F]{1,4}):)?(?<"
            + AFTER_ZERO_SEQ_PLACEHOLDER + GROUP_SEGMENT[3] + ">[0-9a-fA-F]{1,4}):)?(?<" + AFTER_ZERO_SEQ_PLACEHOLDER
            + GROUP_SEGMENT[4] + ">[0-9a-fA-F]{1,4}):)?(?<" + AFTER_ZERO_SEQ_PLACEHOLDER + GROUP_SEGMENT[5]
            + ">[0-9a-fA-F]{1,4}):)?|:)" + IP_V4_REGEX;
    private static final String NETMASK_REGEX = "(/(?<" + GROUP_NETMASK + ">[0-9]{1,2}))?";

    private static final Pattern IP_V4_VALIDATION_PATTERN = Pattern.compile(IP_V4_REGEX + NETMASK_REGEX);
    private static final Pattern IP_V6_SEGMENT_PATTERN = Pattern.compile(IP_V6_START_SEG_REGEX
            + IP_V6_END_SEG_PART_REGEX + NETMASK_REGEX);
    private static final Pattern IP_V6_WITH_DEC_NOTATION_SEGMENT_PATTERN = Pattern
            .compile(IP_V6_START_SEG_WITH_DEC_NOTATION_REGEX + IP_V6_END_SEG_PART_WITH_DEC_NOTATION_REGEX
                    + NETMASK_REGEX);
    private static final Pattern IP_V6_VALIDATION_PATTERN = Pattern.compile("(([0-9a-fA-F]{1,4}(:[0-9a-fA-F]{1,4}){5}"
            + "|:(:[0-9a-fA-F]{1,4}){0,5}" + "|[0-9a-fA-F]{1,4}:(:[0-9a-fA-F]{1,4}){0,4}"
            + "|([0-9a-fA-F]{1,4}:){2}(:[0-9a-fA-F]{1,4}){0,3}" + "|([0-9a-fA-F]{1,4}:){3}(:[0-9a-fA-F]{1,4}){0,2}"
            + "|([0-9a-fA-F]{1,4}:){4}(:[0-9a-fA-F]{1,4}){0,1}" + "|([0-9a-fA-F]{1,4}:){5})" + ":" + IP_V4_REGEX
            + "|:(:[0-9a-fA-F]{1,4}){0,7}" + "|[0-9a-fA-F]{1,4}(:[0-9a-fA-F]{1,4}){7}"
            + "|[0-9a-fA-F]{1,4}(:[0-9a-fA-F]{1,4}){0,6}:" + "|[0-9a-fA-F]{1,4}(:[0-9a-fA-F]{1,4}){0,5}::"
            + "|[0-9a-fA-F]{1,4}(:[0-9a-fA-F]{1,4}){0,4}:(:[0-9a-fA-F]{1,4}){0,2}"
            + "|[0-9a-fA-F]{1,4}(:[0-9a-fA-F]{1,4}){0,3}:(:[0-9a-fA-F]{1,4}){0,3}"
            + "|[0-9a-fA-F]{1,4}(:[0-9a-fA-F]{1,4}){0,2}:(:[0-9a-fA-F]{1,4}){0,4}"
            + "|[0-9a-fA-F]{1,4}(:[0-9a-fA-F]{1,4}){0,1}:(:[0-9a-fA-F]{1,4}){0,5}" + ")" + NETMASK_REGEX);

    private final IPVersion version;
    private final short[] addressBytes;
    private final int netmaskWidth;

    private boolean ipv6WithDecimalNotation;

    public IPAddress(String address) {
        Matcher ipv4matcher = IP_V4_VALIDATION_PATTERN.matcher(address);
        Matcher ipv6matcher = IP_V6_VALIDATION_PATTERN.matcher(address);

        String netmaskString = null;
        if (ipv4matcher.matches()) {
            version = IPVersion.IPv4;
            addressBytes = extractIPv4Segments(ipv4matcher);
            netmaskString = ipv4matcher.group(GROUP_NETMASK);
        } else if (ipv6matcher.matches()) {
            version = IPVersion.IPv6;
            ipv6WithDecimalNotation = ipv4matcher.find();
            addressBytes = extractIPv6Segments(address);
            netmaskString = ipv6matcher.group(GROUP_NETMASK);
        } else {
            throw new InvalidParameterException("invalid IP address: " + address);
        }
        if (StringUtils.isNoneBlank(netmaskString)) {
            netmaskWidth = Integer.parseInt(netmaskString);
        } else {
            netmaskWidth = 0;
        }
    }

    private short[] extractIPv6Segments(String address) {
        short[] ipv6AddressBytes = new short[16];

        Matcher ipv6segmentMatcher = IP_V6_SEGMENT_PATTERN.matcher(address);
        Matcher ipv6withDecNotationSegmentMatcher = IP_V6_WITH_DEC_NOTATION_SEGMENT_PATTERN.matcher(address);

        if (ipv6withDecNotationSegmentMatcher.matches()) {
            initIPv6SegmentBytes(ipv6withDecNotationSegmentMatcher, ipv6AddressBytes, 0, 6);
            initIPv6SegmentBytesFromDecimalNotation(ipv6withDecNotationSegmentMatcher, ipv6AddressBytes);
        } else if (ipv6segmentMatcher.matches()) {
            initIPv6SegmentBytes(ipv6segmentMatcher, ipv6AddressBytes, 0, 8);
        }
        return ipv6AddressBytes;
    }

    private void initIPv6SegmentBytesFromDecimalNotation(Matcher ipv6matcher, short[] ipv6AddressBytes) {
        short[] ipv4Bytes = extractIPv4Segments(ipv6matcher);

        for (int i = 0; i < ipv4Bytes.length; i++) {
            ipv6AddressBytes[12 + i] = ipv4Bytes[i];
        }
    }

    private void initIPv6SegmentBytes(Matcher ipv6matcher, short[] ipv6AddressBytes, int startIdx, int stopIdxExcluded) {
        short[] segmentBytes;
        for (int i = startIdx; i < stopIdxExcluded; i++) {
            segmentBytes = parseIPv6Segment(ipv6matcher, GROUP_SEGMENT[i]);
            ipv6AddressBytes[2 * i] = segmentBytes[0];
            ipv6AddressBytes[2 * i + 1] = segmentBytes[1];
        }
    }

    private short[] parseIPv6Segment(Matcher matcher, String groupName) {
        String segmentString = matcher.group(BEFORE_ZERO_SEQ_PLACEHOLDER + groupName);
        if (StringUtils.isBlank(segmentString)) {
            segmentString = matcher.group(AFTER_ZERO_SEQ_PLACEHOLDER + groupName);
        }
        int segment = StringUtils.isBlank(segmentString) ? 0 : Integer.parseInt(segmentString, 16);
        short[] byteValues = new short[2];
        byteValues[0] = (short) (segment / 256);
        byteValues[1] = (short) (segment % 256);
        return byteValues;
    }

    private short[] extractIPv4Segments(Matcher matcher) {
        String group0 = matcher.group(GROUP_BYTE[0]);
        String group1 = matcher.group(GROUP_BYTE[1]);
        String group2 = matcher.group(GROUP_BYTE[2]);
        String group3 = matcher.group(GROUP_BYTE[3]);
        short[] ipv4AddressBytes = null;
        if (StringUtils.isNoneBlank(group0) && StringUtils.isNoneBlank(group1) && StringUtils.isNoneBlank(group2)
                && StringUtils.isNoneBlank(group3)) {
            ipv4AddressBytes = new short[4];
            ipv4AddressBytes[0] = Short.parseShort(group0);
            ipv4AddressBytes[1] = Short.parseShort(group1);
            ipv4AddressBytes[2] = Short.parseShort(group2);
            ipv4AddressBytes[3] = Short.parseShort(group3);
        }
        return ipv4AddressBytes;
    }

    public IPVersion getVersion() {
        return version;
    }

    @Override
    public String toString() {
        StringBuffer address = new StringBuffer();
        if (IPVersion.IPv4 == version) {
            address.append(createIpv4AddressString(addressBytes));
        } else {
            for (int i = 0; i < 12; i += 2) {
                int segmentValue = 256 * addressBytes[i] + addressBytes[i + 1];
                address.append(String.format("%x:", segmentValue));
            }
            if (ipv6WithDecimalNotation) {
                address.append(createIpv4AddressString(ArrayUtils.subarray(addressBytes, 12, 16)));
            } else {
                int segmentValue6 = 256 * addressBytes[12] + addressBytes[13];
                int segmentValue7 = 256 * addressBytes[14] + addressBytes[15];
                address.append(String.format("%x:%x", segmentValue6, segmentValue7));
            }
        }
        if (netmaskWidth > 0) {
            address.append("/" + netmaskWidth);
        }
        return address.toString();
    }

    private String createIpv4AddressString(short[] segments) {
        return StringUtils.join(segments, '.');
    }

    public boolean isLoopbackAddress() {
        boolean loopbackAddress;
        if (IPVersion.IPv4 == version) {
            loopbackAddress = addressBytes[0] == 127;
        } else {
            loopbackAddress = true;
            for (int i = 0; i < 15; i++) {
                if (addressBytes[i] != 0) {
                    loopbackAddress = false;
                    break;
                }
            }
            if (loopbackAddress && 1 != addressBytes[15]) {
                loopbackAddress = false;
            }
        }
        return loopbackAddress;
    }

    public boolean isUnspecifiedAddress() {
        return false;
    }

    public boolean isNetworkAddress() {
        return false;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof IPAddress)) {
            return false;
        } else {
            short[] otherAddressBytes = ((IPAddress) other).addressBytes;
            if (addressBytes.length != otherAddressBytes.length) {
                return false;
            }
            for (int i = 0; i < addressBytes.length && i < otherAddressBytes.length; i++) {
                if (addressBytes[i] != otherAddressBytes[i]) {
                    return false;
                }
            }
            if (netmaskWidth != ((IPAddress) other).netmaskWidth) {
                return false;
            }
            return true;
        }
    }

    @Override
    public int hashCode() {
        return addressBytes.hashCode();
    }
}
