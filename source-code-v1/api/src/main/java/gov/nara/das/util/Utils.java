package gov.nara.das.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Utils {
	public static void println(Object... a) {
		for (int i = 0; i < a.length; i++) {
			System.out.println(a[i]);
		}
	}
	public static final String readFully(InputStream is) throws IOException{
		byte b=0;
		ByteArrayOutputStream bout =new ByteArrayOutputStream();
		while(true){
			int i=is.read();
			if(i<0){
				break;
			}
			b=(byte) i;
			bout.write(b);
		}
		return bout.toString();
	}
}
