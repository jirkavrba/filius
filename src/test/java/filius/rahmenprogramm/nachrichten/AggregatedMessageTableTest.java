package filius.rahmenprogramm.nachrichten;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import org.junit.Test;

import filius.gui.nachrichtensicht.AggregatedExchangeDialog;
import filius.gui.nachrichtensicht.AggregatedMessageTable;

public class AggregatedMessageTableTest {

    private static final String EMPTY_TABLE_EXPORT = "----------------------------------------------------------------------------------------------------------------------------------------------------------------------------\r\n"
            + "| Nr.        | Zeit                 | Quelle               | Ziel                 | Protokoll            | Schicht              | Bemerkungen                              | \r\n"
            + "----------------------------------------------------------------------------------------------------------------------------------------------------------------------------\r\n"
            + "----------------------------------------------------------------------------------------------------------------------------------------------------------------------------\r\n";

    @Test
    public void testWriteToStream() throws Exception {
        PipedOutputStream outputStream = new PipedOutputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(new PipedInputStream(outputStream), "UTF8"));

        AggregatedMessageTable messageTable = new AggregatedMessageTable(new AggregatedExchangeDialog(), null);
        messageTable.writeToStream(outputStream);
        outputStream.close();

        String output = readString(reader);
        reader.close();
        System.out.println(output);
        assertThat(output, is(EMPTY_TABLE_EXPORT));
    }

    private String readString(BufferedReader reader) throws IOException {
        int nextChar;
        StringBuilder buffer = new StringBuilder();
        while ((nextChar = reader.read()) != -1) {
            buffer.append((char) nextChar);
        }
        String output = buffer.toString();
        return output;
    }

}
