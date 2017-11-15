package gov.nara.das.common.db.das.dao;

import java.util.List;

import gov.nara.das.common.db.das.VariantControlNumber;
/**
 * 
 * @author Matthew Mariano
 * 
 * Change Log
 * ticket        date       contributor                   comments
 * NARATO5-59    2017-11-15 Matthew Mariano              :initial release
 *
 */
public interface VariantControlNumberDAO {

	public final String TABLE_NAME="variant_control_number";
	public final String PK_FIELD="variant_control_numberid";
	public final String SEQUENCE_NAME="variant_control_number_variant_control_numberid_seq";
	public final String FIELD_LIST_FOR_INSERT="variant_control_numberid, desc_naid, auth_list_naid, variant_ctl_no, variant_ctl_no_note";

	public VariantControlNumber getById(long id);
	
	public long create(VariantControlNumber vcn);
	
	public List<?> getByDescriptionNaid(long descNaid);
	
}
