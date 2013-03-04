package org.talend.designer.neo4j.data;

import java.util.ArrayList;
import java.util.List;

public class Relationship {
	
	private String type;
	
	private Direction direction;
	
	private String indexName;
	
	private String key;
	
	private String value;

	public Relationship() {
		direction = Direction.OUTGOING;
	}

	private Relationship(String type, Direction direction, String indexName,
			String key, String value) {
		this.type = type;
		this.direction = direction;
		this.indexName = indexName;
		this.key = key;
		this.value = value;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getIndexName() {
		return indexName;
	}

	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((direction == null) ? 0 : direction.hashCode());
		result = prime * result
				+ ((indexName == null) ? 0 : indexName.hashCode());
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
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
		Relationship other = (Relationship) obj;
		if (direction == null) {
			if (other.direction != null)
				return false;
		} else if (!direction.equals(other.direction))
			return false;
		if (indexName == null) {
			if (other.indexName != null)
				return false;
		} else if (!indexName.equals(other.indexName))
			return false;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
	@Override
	public Relationship clone() {
		return new Relationship(type, direction, indexName, key, value);
	}
	public static enum Direction {
		OUTGOING("Outgoing"), INCOMMING("Incomming");
		
		private Direction(String name) {
			this.name = name;
		}

		private String name;

		public String getName() {
			return name;
		}
		
		public static Direction getFromName(String value) {
			if (value == null || value.isEmpty()) {
				return null;
			}
			for (Direction direction : Direction.values()) {
				if (value.equals(direction.name)) {
					return direction;
				}
			}
			return null;
		}
		
		public static String[] getNames() {
			List<String> names = new ArrayList<>(values().length);
			for (Direction direction : values()) {
				names.add(direction.name);
			}
			return names.toArray(new String[names.size()]);
		}
	}
}
