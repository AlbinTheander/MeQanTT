package com.albin.mqtt.message;

public enum QoS {
	AT_MOST_ONCE  (0),
	AT_LEAST_ONCE (1),
	EXACTLY_ONCE  (2);
	
	final public int val;
	
	QoS(int val) {
		this.val = val;
	}
	
	static QoS valueOf(int i) {
		for(QoS q: QoS.values()) {
			if (q.val == i)
				return q;
		}
		return null;
	}
}