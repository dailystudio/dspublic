package com.dailystudio.app.utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.mozilla.universalchardet.UniversalDetector;

import android.content.Context;
import android.content.res.AssetManager;

import com.dailystudio.development.Logger;

public class FileUtils {

	private final static String NO_MEDIA_TAG_FILE = ".nomedia";
	
	private static final int DOWNLOAD_CONNECTION_TIMEOUT = (3 * 1000);
	private static final int DOWNLOAD_READ_TIMEOUT = (20 * 1000);
	
	public static boolean checkOrCreateNoMediaDirectory(String directory) {
		if (directory == null) {
			return false;
		}
		
		File dir = new File(directory);
	
		return checkOrCreateNoMediaDirectory(dir);
	}
	
	public static boolean checkOrCreateNoMediaDirectory(File directory) {
		if (directory == null) {
			return false;
		}
		
		if (directory.exists()) {
			if (directory.isDirectory()) {
				return true;
			} else {
				Logger.warnning("%s is NOT a directory", directory);
			}
		}
		
		final boolean success = directory.mkdirs();
		if (success == false) {
			return false;
		}
		
		return checkOrCreateNoMediaTagInDirectory(directory);
	}

	private static boolean checkOrCreateNoMediaTagInDirectory(File dir) {
		if (dir == null) {
			return false;
		}
		
		File tagFile = new File(dir, NO_MEDIA_TAG_FILE);
		if (tagFile.exists()) {
			return true;
		}
		
		boolean success = false;
		try {
			success = tagFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
			success = false;
		}
		
		return success;
	}
	
	public static boolean isFileExisted(String filename) {
		if (filename == null) {
			return false;
		}
		
		File dstFile = new File(filename);
		if (dstFile.exists()) {
			return true;
		}
		
		return false;
	}

	public static String getDirectory(String filename) {
		if (filename == null) {
			return null;
		}
		
		File dstFile = new File(filename);
		
		return dstFile.getParent();
	}
	
	public static boolean checkOrCreateFile(String filename) {
		if (filename == null) {
			return false;
		}
		
		File file = new File(filename);
		
		return checkOrCreateFile(file);
	}
	
	public static boolean checkOrCreateFile(File file) {
		if (file == null) {
			return false;
		}
		
		if (file.exists()) {
			return true;
		}
		
		boolean success = false;
		try {
			success = file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
			success = false;
		}
		
		return success;
	}
	
	public static boolean deleteFiles(String path) {
		if (path == null) {
			return false;
		}
		
	    File file = new File(path);

	    boolean success = false;
	    if (file.exists()) {
	        String deleteCmd = "rm -r " + path;
	        
	        Runtime runtime = Runtime.getRuntime();
	        try {
	            runtime.exec(deleteCmd);
	            
	            success = true;
	        } catch (IOException e) { 
	        	Logger.debug("failure: [%s]", e.toString());
	        	
	        	success = false;
	        }
	        
	    }
	    
	    return success;
	}
	
	public static long getFileLength(String file) {
		if (file == null) {
			return 0l;
		}
		
		File dstFile = new File(file);
		if (dstFile.exists()) {
			return 0l;
		}

		return dstFile.length();
	}
	
	public static String getFileContent(String file) throws IOException {
		if (file == null) {
			return null;
		}
		
		final String encoding = detectFileEncoding(file);
		Logger.debug("encoding = %s", encoding);

		return getFileContent(new FileInputStream(file), encoding);
	}
	
	public static String getFileContent(InputStream istream, String encoding) throws IOException {
		if (istream == null) {
			return null;
		}
		
		InputStreamReader ireader = null;
		if (encoding != null) {
			ireader = new InputStreamReader(istream, encoding);
		} else {
			ireader = new InputStreamReader(istream);
		}
		
		StringWriter writer = new StringWriter();
		
		char buffer[] = new char[2048];
		int n = 0;
		while((n = ireader.read(buffer)) != -1) {
			writer.write(buffer, 0, n);
		}
		
		writer.flush();
		istream.close();
		
		return writer.toString();
	}
	
	public static String getAssetFileContent(Context context, String file) throws IOException {
		if (context == null || file == null) {
			return null;
		}
		
		final AssetManager asstmgr = context.getAssets();
		if (asstmgr == null) {
			return null;
		}
		
		final String encoding = detectFileEncoding(asstmgr.open(file));
		Logger.debug("encoding = %s", encoding);

		InputStream istream = asstmgr.open(file);
		if (istream == null) {
			return null;
		}
		
		return getFileContent(istream, encoding);
	}
	
	public static String detectFileEncoding(String filename) {
		if (filename == null) {
			return null;
		}
		
		File file = new File(filename);
		
		return detectFileEncoding(file);
	}
	
	public static String detectFileEncoding(File file) {
		if (file == null) {
			return null;
		}
		
	    FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			Logger.warnning("get encodeing failure: %s", e.toString());
			fis = null;
		}
		
		return detectFileEncoding(fis);
	}
	
	public static String detectFileEncoding(InputStream istream) {
		if (istream == null) {
			return null;
		}
		
	    byte[] buf = new byte[4096];
	    
	    UniversalDetector detector = new UniversalDetector(null);

	    int nread;
	    try {
			while ((nread = istream.read(buf)) > 0 && !detector.isDone()) {
			  detector.handleData(buf, 0, nread);
			}
		} catch (IOException e) {
			Logger.warnning("get encodeing failure: %s", e.toString());
		}
		
	    detector.dataEnd();

	    String encoding = detector.getDetectedCharset();

	    detector.reset();
	    
	    try {
	    	istream.close();
	    }  catch (IOException e) {
			Logger.warnning("close stream failure: %s", e.toString());
		}
	    
	    return encoding;
	}
	
	public static String getFileExtension(String filename) {
		return getFileExtension(filename, "");
	}

    public static String getFileExtension(String filename, String defExt) {
    	if ((filename != null) && (filename.length() > 0)) {
    		int i = filename.lastIndexOf('.');

    		if ((i >-1) && (i < (filename.length() - 1))) {
    			return filename.substring(i + 1);
    		}
    	}
    	
    	return defExt;
    }
    
	public static void writeFileContent(String file, String fileContent) throws IOException {
		if (file == null || fileContent == null) {
			return;
		}
		
		StringReader reader = new StringReader(fileContent);
		FileWriter ostream = new FileWriter(file);
		
		char buffer[] = new char[2048];
		int n = 0;
		while((n = reader.read(buffer)) != -1) {
			ostream.write(buffer, 0, n);
		}
		
		ostream.flush();
		reader.close();
		
		return;
	}
	
	public static boolean downloadFile(String fileUrl, String dstFile) {
		if (fileUrl == null || dstFile == null) {
			return false;
		}
		
		if (FileUtils.checkOrCreateFile(dstFile) == false) {
			return false;
		}
		
		InputStream is = null;
		OutputStream os = null;

		boolean success = false;
		try {
			URL u = new URL(fileUrl);

			URLConnection connection = u.openConnection();
			
			/*
			 * XXX: we could not use u.openStream() here.
			 * 		the default connect/read timeout is infinite.
			 * 		we need to set a acceptable value
			 */
			connection.setConnectTimeout(DOWNLOAD_CONNECTION_TIMEOUT);
			connection.setReadTimeout(DOWNLOAD_READ_TIMEOUT);
			
			is = connection.getInputStream();
			
			os = new FileOutputStream(dstFile);
	         
			DataInputStream dis = new DataInputStream(is);
			DataOutputStream dos = new DataOutputStream(os);
			
			@SuppressWarnings("unused")
			int bytesReceived = 0;
			int bytesRead = 0;
			byte[] buffer = new byte[2048];

			while ((bytesRead = dis.read(buffer, 0, 2048)) > 0) {
				bytesReceived += bytesRead;
//				Logger.debug("bytes received = %d", bytesReceived);
				
				dos.write(buffer, 0, bytesRead);
				dos.flush();
			}
			
			success = true;
		} catch (MalformedURLException mue) {
			mue.printStackTrace();
			
			success = false;
		} catch (IOException ioe) {
			ioe.printStackTrace();
			
			success = false;
		}  catch (NullPointerException ne) {
			/*
			 * XXX: sometime, here will be thrown
			 * 		a NULL-pointer exception for address 
			 * 		resolving.
			 */
			ne.printStackTrace();
			
			success = false;
		} finally {
			try {
				if (is != null) {
					is.close();
				}
				
				if (os != null) {
					os.close();
				}
			} catch (IOException ioe) {
			}
		}

		return success;
	}

}
