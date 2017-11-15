package gov.nara.das.ingest.controllers;


import org.json.JSONObject;

public class IngestRequest {
    public String  status;
    public String url;

    public String bucketName;
    public String objectKey;
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String toJSON(){
        JSONObject j=new JSONObject();
        j.put("url",url);
        j.put("status",status);
        return j.toString(3);
    }
    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getObjectKey() {
        return objectKey;
    }

    public void setObjectKey(String objectKey) {
        this.objectKey = objectKey;
    }
}
