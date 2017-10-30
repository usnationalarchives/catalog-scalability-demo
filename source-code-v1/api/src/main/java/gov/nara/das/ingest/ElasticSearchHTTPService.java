package gov.nara.das.ingest;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import gov.nara.das.common.db.das.Description;
import gov.nara.das.common.response.CreateDescriptionResponse;
import static gov.nara.das.common.elastic.ElasticSearchUtils.*;
@Service
public class ElasticSearchHTTPService {
	@Value("${es_server_url}")
	private String esURL;
    public ResponseEntity<CreateDescriptionResponse> postNew(Description description) {
    	String url=esURL+"/descriptions_approved/description/"+description.getDescNaId();
    	HttpHeaders headers = new HttpHeaders();
    	headers.setContentType(MediaType.APPLICATION_JSON);
    	HttpEntity<String> request = new HttpEntity<String>(convertToElasticSearchJSON(description), headers);
    	RestTemplate restTemplate=new RestTemplate();
    	ResponseEntity<CreateDescriptionResponse> response = restTemplate.postForEntity(url, request, CreateDescriptionResponse.class);
    	return response;
    }

}
