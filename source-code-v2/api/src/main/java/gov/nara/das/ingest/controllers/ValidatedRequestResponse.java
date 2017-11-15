package gov.nara.das.ingest.controllers;


import org.json.JSONObject;

public class ValidatedRequestResponse {
    public String  status;

    public String bucketName;
    public String objectKey;
    public String comment;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
