package com.albin.mqtt.message;

import java.io.IOException;
import java.io.InputStream;

public class ConnAckMessage extends Message {
	
	public enum ConnectionStatus {
		ACCEPTED,
		UNACCEPTABLE_PROTOCOL_VERSION,
		IDENTIFIER_REJECTED,
		SERVER_UNAVAILABLE,
		BAD_USERNAME_OR_PASSWORD,
		NOT_AUTHORIZED
	}
	
	private ConnectionStatus status;
	
	public ConnAckMessage() {
		super(Type.CONNACK);
	}

	public ConnAckMessage(Header header, InputStream in) throws IOException {
		super(header, in);
	}

	@Override
	protected void readMessage(InputStream in, int msgLength)
			throws IOException {
		if (msgLength != 2) {
			throw new IllegalStateException("Message Length must be 2 for CONNACK. Current value: " + msgLength);
		}
		// Ignore first byte
		in.read();
		int result = in.read();
		switch (result) {
		case 0:
			status = ConnectionStatus.ACCEPTED;
			break;
		case 1:
			status = ConnectionStatus.UNACCEPTABLE_PROTOCOL_VERSION;
			break;
		case 2:
			status = ConnectionStatus.IDENTIFIER_REJECTED;
			break;
		case 3:
			status = ConnectionStatus.SERVER_UNAVAILABLE;
			break;
		case 4:
			status = ConnectionStatus.BAD_USERNAME_OR_PASSWORD;
			break;
		case 5:
			status = ConnectionStatus.NOT_AUTHORIZED;
			break;
		default:
			throw new UnsupportedOperationException("Unsupported CONNACK code: " + result);
		}
	}
	
	public ConnectionStatus getStatus() {
		return status;
	}

}
