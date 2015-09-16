package representation;

import java.io.Serializable;

/**
 * Marker interface used to unify the two defined types
 * 
 * @author Nandra Cosmin
 * @since 10.02.2015
 *
 * 
 */
public interface Type extends Serializable {

	@Override
	public boolean equals(Object obj);

	@Override
	public int hashCode();
}
