package coop.tecso.daa.common;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.util.Log;

/**
 * UDAA Content Provider
 * 
 * @author leonardo.fagnano
 *
 */
public class DAAProvider extends ContentProvider {
	
	private static final String LOG_TAG = ContentProvider.class.getSimpleName();
	
	public static final String PROVIDER_NAME = "coop.tecso.udaa.common";
	public static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER_NAME);
	
	private static final int LIBRARY  = 1;
	private static final int DATABASE = 2;
	
	private static final HashMap<String, Integer> MIME_TYPES;
	static {
		MIME_TYPES = new HashMap<String, Integer>();
		MIME_TYPES.put("lib", LIBRARY);
		MIME_TYPES.put("db", DATABASE);
	}

	@Override
	public boolean onCreate() {
		// Validations...
		return true;
	}

	@Override
	public String getType(Uri uri) {
		Log.i(LOG_TAG, "getType: "+uri);
		
		int match = match(uri.toString());
		switch (match){
		case LIBRARY:
			return "application/vnd.android.package-archive";
		case DATABASE:
			return "application/octet-stream";
		default:
			return null;
		}
	}

	@Override
	public ParcelFileDescriptor openFile(Uri uri, String mode)
			throws FileNotFoundException {
		Log.i(LOG_TAG, "openFile: "+uri+ " - mode: "+mode);
		
		String intPath = null;
		int match = match(uri.toString());
		switch (match){
		case LIBRARY:
			intPath = "dex";
			break;
		case DATABASE:
			intPath = "db";
			break;
		default:
			Log.e(LOG_TAG, "openFile: non matched extension");
			return null;
		}

		File file = new File(getContext().getDir(intPath, Context.MODE_PRIVATE), uri.getPath());
		if (file.exists()) 
			return(ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY));

		throw new FileNotFoundException(uri.getPath());  
	}

	@Override
	public Cursor query(Uri url, String[] projection, String selection,
			String[] selectionArgs, String sort) {
		throw new RuntimeException("Operation not supported");
	}

	@Override
	public Uri insert(Uri uri, ContentValues initialValues) {
		throw new RuntimeException("Operation not supported");
	}

	@Override
	public int update(Uri uri, ContentValues values, String where, String[] whereArgs) {
		throw new RuntimeException("Operation not supported");
	}

	@Override
	public int delete(Uri uri, String where, String[] whereArgs) {
		throw new RuntimeException("Operation not supported");
	}
	
	private int match(String file){
		int dotposition= file.lastIndexOf(".");
		String ext = file.substring(dotposition + 1, file.length()); 
		
		return MIME_TYPES.get(ext);
	}
}