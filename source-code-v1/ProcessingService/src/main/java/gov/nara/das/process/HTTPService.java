package gov.nara.das.process;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import gov.nara.das.common.response.CreateDescriptionResponse;


@Service
public class HTTPService {
	@Value("${api_server_url}")
	private String url;
    public ResponseEntity<CreateDescriptionResponse> postNew(String messageData) {
    	HttpHeaders headers = new HttpHeaders();
    	headers.setContentType(MediaType.APPLICATION_XML);
    	HttpEntity<String> request = new HttpEntity<String>(messageData, headers);
    	RestTemplate restTemplate=new RestTemplate();
    	ResponseEntity<CreateDescriptionResponse> response = restTemplate.postForEntity(url+"/v1/ingest/description", request, CreateDescriptionResponse.class);
    	return response;
    }

}
