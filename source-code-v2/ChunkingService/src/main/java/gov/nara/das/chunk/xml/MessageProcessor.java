package gov.nara.das.chunk.xml;

import java.io.File;

public interface MessageProcessor {
    public final int CONTINUE=1;
    public final int QUIT=2;
    public int processMessage(IParser parser,String messageData,long jobId, int messageIndex, String type);
    public void cleanup(long jobId);
}
