package gov.nara.das.ingest;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.spi.LoggerFactoryBinder;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import gov.nara.das.common.db.das.Description;
import gov.nara.das.common.response.CreateDescriptionResponse;
import gov.nara.das.util.ESResponse;

import static gov.nara.das.common.elastic.ElasticSearchUtils.*;
@Service
public class ElasticSearchHTTPService {
	
	@Value("${es_server_url}")
	private String esURL;
	Logger log= LoggerFactory.getLogger(this.getClass());

    public ESResponse postNew(Description description) {
    	String url=esURL+"/descriptions_approved/description/"+description.getDescNaId();
    	HttpHeaders headers = new HttpHeaders();
    	headers.setContentType(MediaType.APPLICATION_JSON);
    	String json=convertToElasticSearchJSON(description);
    	//
    	log.error("sending ES json="+json);
    	//
    	HttpEntity<String> request = new HttpEntity<String>(json, headers);
    	RestTemplate restTemplate=new RestTemplate();
    	//ResponseEntity<CreateDescriptionResponse> response = restTemplate.postForEntity(url, request, CreateDescriptionResponse.class);


    	//
        HttpEntity<String> entity = new HttpEntity<String>(json, headers); 
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, entity, String.class, description.getDescNaId());
        // check the response, e.g. Location header,  Status, and body
        response.getHeaders().getLocation();
        response.getStatusCode();
        String responseBody = response.getBody();
    	log.error("receveived ES response="+response.getBody());
    	log.error(" received ES response status code="+response.getStatusCodeValue());
    	//
    	return new ESResponse(description,request,response);

    }
    public ESResponse postSearch(String jsonQuery ) {
    	String url=esURL+"/descriptions_approved/_search";
    	HttpHeaders headers = new HttpHeaders();
    	headers.setContentType(MediaType.APPLICATION_JSON);
    	//
    	log.error("sending ES json="+jsonQuery);
    	//
    	HttpEntity<String> request = new HttpEntity<String>(jsonQuery, headers);
    	RestTemplate restTemplate=new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
        // check the response, e.g. Location header,  Status, and body
        response.getHeaders().getLocation();
        response.getStatusCode();
        String responseBody = response.getBody();
    	log.error("receveived ES response="+response.getBody());
    	log.error(" received ES response status code="+response.getStatusCodeValue());
    	//
    	ESResponse esr=new ESResponse(request,response);
    	return esr ;

    }
}
