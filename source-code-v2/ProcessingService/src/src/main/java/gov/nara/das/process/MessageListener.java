package gov.nara.das.process;

import java.io.IOException;

public interface MessageListener {
	public void  processMessage(String sqsMessageId) throws IOException;
}
