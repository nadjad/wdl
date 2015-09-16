package representation.types;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import representation.Type;

public class CompositeType implements Type {

	private static String[] TYPES = { "List", "Tuple" };
	private static List<String> TYPES_LIST = Arrays.asList(TYPES);

	private List<Type> associatedTypes;
	private String superType;

	public static CompositeType getInstance(String superType, Type associated) {
		if (associated != null && TYPES_LIST.contains(superType)) {
			List<Type> associates = new ArrayList<Type>();
			associates.add(associated);
			return new CompositeType(superType, associates);
		}
		return null;
	}

	public static CompositeType getInstance(String superType,
			List<Type> associated) {
		if (associated != null && !associated.isEmpty()
				&& TYPES_LIST.contains(superType)) {
			return new CompositeType(superType, associated);
		}
		return null;
	}

	private CompositeType(String superType, List<Type> associates) {
		this.superType = superType;
		this.associatedTypes = associates;
	}

	public String getSuperType() {
		return superType;
	}

	public List<Type> getAssociatedTypes() {
		return associatedTypes;
	}

	public void setAssociatedTypes(List<Type> associatedTypes) {
		this.associatedTypes = associatedTypes;
	}

	@Override
	/**
	 * The equality condition is satisfied if the objects have equal
	super-types
	 * (List or Tuple) and all the associated sub-types are equal. The
	sub-types must
	 * be identical in name and position within the two objects
	 * @param obj
	 * @return the equality of the objects
	 */
	public boolean equals(Object obj) {
		if (obj != null) {
			if (obj == this) {
				return true;
			} else {
				if (obj instanceof CompositeType) {
					CompositeType newType = (CompositeType) obj;
					List<Type> newAssociated = newType.getAssociatedTypes();
					int assSize = newAssociated.size();
					if (this.superType.equals(newType.getSuperType())
							&& this.associatedTypes.size() == assSize) {
						boolean equal = true;
						for (int i = 0; i < assSize; i++) {
							if (!this.associatedTypes.get(i).equals(
									newAssociated.get(i))) {
								equal = false;
								break;
							}
						}
						return equal;
					} else {
						return false;
					}
					// return (this.superType.equals(newType.getSuperType()) &&
					// this.associatedType
					// .equals(newType.getAssociatedType()));
				} else {
					return false;
				}
			}
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((associatedTypes == null) ? 0 : associatedTypes.hashCode());
		result = prime * result
				+ ((superType == null) ? 0 : superType.hashCode());
		return result;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(this.superType + " of: ");
		for (Type t : this.associatedTypes)
			sb.append(t.toString() + ",");

		return new String(sb);
	}

}
