package gov.nara.das.chunk.xml;

import java.io.File;

public interface IParser {
	public int getTotal();
	public long getJobId();
	public File getFile();
	public String getCurrentMessageId();
}
