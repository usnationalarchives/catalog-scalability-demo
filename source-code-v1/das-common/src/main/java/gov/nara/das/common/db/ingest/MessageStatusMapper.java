package gov.nara.das.common.db.ingest;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class MessageStatusMapper  implements RowMapper<MessageStatus >{
	@Override
	public MessageStatus mapRow(ResultSet rs, int rowNum) throws SQLException {
		MessageStatus ms=new MessageStatus();
		ms.setSqsMessageId(rs.getString("sqs_message_id"));
		ms.setMessageIndex(rs.getLong("message_index"));
		ms.setGUID(rs.getString("guid"));
		ms.setNaid(rs.getLong("naid"));
		ms.setChunkingId(rs.getString("chunking_id"));
		ms.setMessageStatusTypeId(rs.getInt("job_id"));
		ms.setMessageIndex(rs.getInt("message_index"));
		ms.setMessageStatusTypeId(rs.getInt("title"));
		ms.setMessageStatusTypeId(rs.getInt("message_status_type_id"));
		ms.setInsertSuccess(rs.getBoolean("insert_success"));
		ms.setInsertFailed(rs.getBoolean("insert_failed"));
		ms.setValidationFailureId(rs.getInt("validation_failure_id"));
		ms.setHttpPostRequestTime(rs.getTimestamp("http_post_request_time"));
		ms.setHttpPostResponseTime(rs.getTimestamp("http_post_request_time"));
		ms.setHttpResponseCode(rs.getInt("http_response_code"));
		ms.setHttpTimeout(rs.getBoolean("http_timeout"));
		ms.setInsertTimestamp(rs.getTimestamp("insert_timestamp"));
		return ms;
		
	}
}