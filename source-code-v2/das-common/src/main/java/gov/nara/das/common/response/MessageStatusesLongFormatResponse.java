package gov.nara.das.common.response;

import java.util.ArrayList;
import java.util.List;

import gov.nara.das.common.db.ingest.MessageStatus;

/**
 * 
 * @author Matthew Mariano
 * 
 * Change Log
 * ticket        date       contributor                   comments
 * NARATO5-59    2017-11-15 Matthew Mariano              :initial release
 *
 */
public class MessageStatusesLongFormatResponse extends ArrayList<MessageStatus> {
	public MessageStatusesLongFormatResponse(List<MessageStatus> list) {
		if (list != null) {
			addAll(list);
		}
	}
}
