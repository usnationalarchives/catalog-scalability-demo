package gov.nara.das.ingest.controllers;


import org.json.JSONObject;

public class ValidatedRequest {

    public String bucketName="";
    public String objectKey="";
    public String data="";
    public String type="";



    public String toJSON(){
        JSONObject j=new JSONObject();
        j.put("bucketName",bucketName);
        j.put("objectKey",objectKey);
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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
