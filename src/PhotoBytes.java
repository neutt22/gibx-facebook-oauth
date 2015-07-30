import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;


public class PhotoBytes {
	
	public static byte[] fetchBytesFromImage(String image) {
	    File photo = new File(image);
		try {
			return getBytesFromInputStream(new FileInputStream(photo));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	    return null;
	  }

	private static byte[] getBytesFromInputStream(InputStream is) {
	    ByteArrayOutputStream os = new ByteArrayOutputStream();
	    try {
	      byte[] buffer = new byte[65535];
	      for (int len; (len = is.read(buffer)) != -1;) {
	        os.write(buffer, 0, len);
	      }
	      os.flush();
	      return os.toByteArray();
	    } catch (IOException e) {
	      return null;
	    }
	  }

}
