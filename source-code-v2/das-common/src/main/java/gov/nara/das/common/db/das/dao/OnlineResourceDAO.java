package gov.nara.das.common.db.das.dao;

import gov.nara.das.common.db.das.OnlineResource;
/**
 * 
 * @author Matthew Mariano
 * 
 * Change Log
 * ticket        date       contributor                   comments
 * NARATO5-59    2017-11-15 Matthew Mariano              :initial release
 *
 */
public interface OnlineResourceDAO {
	OnlineResource onlineResource =new OnlineResource();
	public final String TABLE_NAME="online_resource";
	public final String PK_FIELD="online_resourceID";
	public final String SEQUENCE_NAME="online_resource_online_resourceID_seq";
	public final String FIELD_LIST_FOR_INSERT="online_resourceID,desc_naid,auth_list_naid";
	public OnlineResource getById(long id);
	public long create(OnlineResource o);
}
