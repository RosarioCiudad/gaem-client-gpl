package coop.tecso.daa.domain.base;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 
 * @author tecso.coop
 *
 */
@DatabaseTable(tableName = "apm_tablaVersion")
public class TablaVersion extends AbstractEntity {

	@DatabaseField(canBeNull = false)
	private String tabla;

	@DatabaseField(canBeNull = false)
	private int lastVersion;

	public int getLastVersion() {
		return lastVersion;
	}

	public String getTabla() {
		return tabla;
	}

	public void setLastVersion(int lastVersion) {
		this.lastVersion = lastVersion;
	}

	public void setTabla(String tabla) {
		this.tabla = tabla;
	}
}