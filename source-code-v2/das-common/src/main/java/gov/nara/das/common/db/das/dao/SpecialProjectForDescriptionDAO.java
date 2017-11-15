package gov.nara.das.common.db.das.dao;
import java.util.List;

import gov.nara.das.common.db.das.SpecialProjectForDescription;
/**
 * 
 * @author Matthew Mariano
 * 
 * Change Log
 * ticket        date       contributor                   comments
 * NARATO5-59    2017-11-15 Matthew Mariano              :initial release
 *
 */
public interface SpecialProjectForDescriptionDAO {
	public Long create(SpecialProjectForDescription spd);
	public SpecialProjectForDescription getById(long id);
	public List<SpecialProjectForDescription> getByDescriptionNaid(long descNaid);
}
