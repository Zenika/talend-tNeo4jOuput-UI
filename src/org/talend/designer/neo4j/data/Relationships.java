package org.talend.designer.neo4j.data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Relationships implements Iterable<Relationship>, Cloneable{

	private List<Relationship> relationships = new ArrayList<>();
	private boolean fromCache;
	private Relationship relationship = new Relationship();
	
	@Override
	public Iterator<Relationship> iterator() {
		return relationships.iterator();
	}

	public List<Relationship> getRelationships() {
		return relationships;
	}

	public void setRelationships(List<Relationship> relationships) {
		this.relationships = relationships;
	}

	public boolean isFromCache() {
		return fromCache;
	}

	public void setFromCache(boolean fromCache) {
		this.fromCache = fromCache;
	}

	public Relationship getRelationship() {
		return relationship;
	}

	public void setRelationship(Relationship relationship) {
		this.relationship = relationship;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (fromCache ? 1231 : 1237);
		result = prime * result
				+ ((relationship == null) ? 0 : relationship.hashCode());
		result = prime * result
				+ ((relationships == null) ? 0 : relationships.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Relationships other = (Relationships) obj;
		if (fromCache != other.fromCache)
			return false;
		if (relationship == null) {
			if (other.relationship != null)
				return false;
		} else if (!relationship.equals(other.relationship))
			return false;
		if (relationships == null) {
			if (other.relationships != null)
				return false;
		} else if (!relationships.equals(other.relationships))
			return false;
		return true;
	}

	public Relationships clone() {
		Relationships newRelationships = new Relationships();
		if (this.relationships != null) {
			List<Relationship> relationships = new ArrayList<>(this.relationships.size());
			for (Relationship relationship : this.relationships) {
				relationships.add(relationship.clone());
			}
			newRelationships.setRelationships(relationships);
		}
		newRelationships.setFromCache(fromCache);
		newRelationships.setRelationship(relationship.clone());
		return newRelationships;
	}
	
}
