package gov.nara.das.common.response;

import java.util.ArrayList;
import java.util.List;

import gov.nara.das.common.db.ingest.MessageStatus;

public class MessageStatusesLongFormatResponse extends ArrayList<MessageStatus>{
	public MessageStatusesLongFormatResponse(List<MessageStatus> list){
		addAll(list);
	}
}
