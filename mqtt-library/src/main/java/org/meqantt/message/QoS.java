/*******************************************************************************
 * Copyright 2011 Albin Theander
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.meqantt.message;

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
		throw new IllegalArgumentException("Not a valid QoS number: " + i);
	}
}
