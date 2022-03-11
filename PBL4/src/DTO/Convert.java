package DTO;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Convert {

	public static Object deserialize(byte[] data) throws Exception {
	    ByteArrayInputStream in = new ByteArrayInputStream(data);
	    ObjectInputStream is = new ObjectInputStream(in);
	    return is.readObject();
	}
	  public static byte[] serialize(Object obj) throws Exception{
	        ByteArrayOutputStream out = new ByteArrayOutputStream();
	        ObjectOutputStream os = new ObjectOutputStream(out);
	        os.writeObject(obj);
	        return out.toByteArray();
	    }

}
