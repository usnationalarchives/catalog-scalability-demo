package gov.nara.das.common.db.das;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
/**
 * 
 * @author Matthew Mariano
 * 
 * Change Log
 * ticket        date       contributor                   comments
 * NARATO5-59    2017-11-15 Matthew Mariano              :initial release
 *
 */
public class LanguageMapper  implements RowMapper<Language >{
	@Override
	public Language mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		Language language =new Language();
		language.setLanguageId(rs.getLong("languageid"));
		language.setDescNaId(rs.getLong("desc_naid"));
		language.setLanguageNaId(rs.getLong("language_naid"));
		return language;	
	}
}