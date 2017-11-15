package gov.nara.das.process;

import java.net.URLEncoder;

import org.apache.http.client.utils.URLEncodedUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	private final Logger log = LoggerFactory.getLogger(this.getClass());
    public ResponseEntity<String> postNew(String messageData) {
    	HttpHeaders headers = new HttpHeaders();
    	headers.setContentType(MediaType.APPLICATION_XML);
    	HttpEntity<String> request = new HttpEntity<String>(messageData, headers);
    	RestTemplate restTemplate=new RestTemplate();
    	log.debug("posting to ES"+url+"/v1/api/entity/description");
    	ResponseEntity<String> response = restTemplate.postForEntity(url+"/v1/api/entity/description", request, String.class);
    	return response;
    }
    public ResponseEntity<CreateDescriptionResponse> postNewBatch(String messageData) {
    	HttpHeaders headers = new HttpHeaders();
    	headers.setContentType(MediaType.TEXT_PLAIN);
    	HttpEntity<String> request = new HttpEntity<String>(messageData, headers);
    	RestTemplate restTemplate=new RestTemplate();
    	log.debug("posting to ES"+url+"/v1/api/entity/descriptionBatch");
    	ResponseEntity<CreateDescriptionResponse> response = restTemplate.postForEntity(url+"/v1/api/entity/descriptionBatch", request, CreateDescriptionResponse.class);
    	return response;
    }
}
