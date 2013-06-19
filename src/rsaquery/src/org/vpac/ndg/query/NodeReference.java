package org.vpac.ndg.query;

class NodeReference {
	String nodeId;
	String socketName;

	@Override
	public String toString() {
		return String.format("#%s/%s", nodeId, socketName);
	}

	public NodeReference copy() {
		NodeReference nr = new NodeReference();
		nr.nodeId = nodeId;
		nr.socketName = socketName;
		return nr;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nodeId == null) ? 0 : nodeId.hashCode());
		result = prime * result
				+ ((socketName == null) ? 0 : socketName.hashCode());
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
		NodeReference other = (NodeReference) obj;
		if (nodeId == null) {
			if (other.nodeId != null)
				return false;
		} else if (!nodeId.equals(other.nodeId))
			return false;
		if (socketName == null) {
			if (other.socketName != null)
				return false;
		} else if (!socketName.equals(other.socketName))
			return false;
		return true;
	}
}