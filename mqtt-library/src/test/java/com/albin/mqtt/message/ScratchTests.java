package com.albin.mqtt.message;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.junit.Test;

import com.albin.mqtt.util.FormatUtil;

public class ScratchTests {
	
	@Test
	public void testStringCoding() throws IOException {
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(byteOut);
		dos.writeUTF("Albin");
		dos.flush();
		byte[] data = byteOut.toByteArray();
		System.out.println(FormatUtil.dumpByteArray(data));
	}

}
