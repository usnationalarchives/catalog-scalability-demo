package gov.nara.das.common.db.ingest.dao.impl;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import gov.nara.das.common.db.ingest.Job;
import gov.nara.das.common.db.ingest.JobMapper;
import gov.nara.das.common.db.ingest.MessageStatus;
import gov.nara.das.common.db.ingest.MessageStatusMapper;
import gov.nara.das.common.db.ingest.dao.MessageStatusDAO;

@Repository
@ComponentScan("gov.nara.das.common.db")
public class MessageStatusTemplate implements MessageStatusDAO {
	
	@Value("${db.job.schema}")
	private String jobSchema;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public void upsert(MessageStatus ms) {
		String sql = "insert  into "+jobSchema+".message_status(sqs_message_id,message_index, guid, naid, chunking_id, job_id, title , message_status_type_id , insert_success, insert_failed, validation_failure_id, http_post_request_time, http_post_response_time , http_timeout, http_response_code, insert_timestamp"
				+ ") values (?, ?, ?, ?, ?, ?, ?, ?, ?, ? ,? , ? , ? , ? , ? , current_timestamp) "
				+ " on conflict (sqs_message_id,message_index)"
				+ " do update set naid=?, chunking_id=?, job_id=? , title= ? , message_status_type_id=?, insert_success=?, insert_failed=?, validation_failure_id=?, http_post_request_time=?, http_post_response_time=? ,http_response_code=?, http_timeout=?,  insert_timestamp=current_timestamp";
		boolean timeout = false;
		jdbcTemplate.update(sql, ms.getSqsMessageId(), ms.getMessageIndex(), ms.getGUID(),ms.getNaid(),ms.getChunkingId(), ms.getJobId(), ms.getTitle(),ms.getMessageStatusTypeId(), ms.getInsertSuccess(), ms.getInsertFailed(), ms.getValidationFailureId(),ms.getHttpPostRequestTime(), ms.getHttpPostResponseTime(), timeout, ms.getHttpResponseCode(),ms.getNaid(), ms.getChunkingId(), ms.getJobId(), ms.getTitle(), ms.getMessageStatusTypeId(), ms.getInsertSuccess(), ms.getInsertFailed(),ms.getValidationFailureId(), ms.getHttpPostRequestTime(), ms.getHttpPostResponseTime(),ms.getHttpResponseCode(), timeout);
//		String sql = "insert  "+jobSchema+".into message_status(sqs_message_id,message_index, guid, naid, chunking_id, job_id, title , insert_timestamp"
//				+ ") values (?, ?, ?, ?, ?,? , current_timestamp) "
//				+ " on conflict (sqs_message_id,message_index)"
//				+ " do update set naid=?, chunking_id=?, job_id=? , title= ? ";
//		boolean timeout = false;
//		jdbcTemplate.update(sql, ms.getSqsMessageId(), ms.getMessageIndex(), ms.getGUID(),ms.getNaid(),ms.getChunkingId(), ms.getJobId(), ms.getTitle(),ms.getNaid(),ms.getChunkingId(), ms.getJobId(), ms.getTitle());

		
		return;
	}

	@Override
	public List<MessageStatus> getByJobId(long jobId) {
		String sql = "select * from  "+jobSchema+".message_status where job_id=?";
		List<MessageStatus> results = jdbcTemplate.query(sql, new MessageStatusMapper(), new Object[] { jobId });
		return results;

	}
}
