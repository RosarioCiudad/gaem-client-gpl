/*
 * Copyright (c) 2016 Municipalidad de Rosario, Coop. de Trabajo Tecso Ltda.
 *
 * This file is part of GAEM.
 *
 * GAEM is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License.
 *
 * GAEM is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with GAEM.  If not, see <http://www.gnu.org/licenses/>.
 */

package coop.tecso.aid.printer;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.starmicronics.stario.StarIOPort;
import com.starmicronics.stario.StarIOPortException;
import com.starmicronics.stario.StarPrinterStatus;

public class PrinterHelper {

	private static final String LOG_TAG = PrinterHelper.class.getSimpleName();

	public static final int STATUS_OK = 0;
	public static final int STATUS_OFFLINE = 1;
	public static final int STATUS_PAPER_EMPTY = 2;
	public static final int STATUS_COVER_OPEN = 3;

	private UnicodeTranslator ucodeTranslator;
	private Context context;
	private StarIOPort port;

	/**
	 * 
	 * @param context
	 */
	public PrinterHelper(Context context) {
		this.ucodeTranslator = new UnicodeTranslatorStar(); 
		this.context = context;

	}

	/**
	 *  Bluetooth Connection:
	 *  - BT: (No value) Uses first Star printer paired with the device 
	 *  - BT: Device_Name
	 *  - BT: MAC Address
	 * @param portName
	 * @throws StarIOPortException
	 */
	public void openConnection(String portName)	throws StarIOPortException {
		// Port Settings should be 'mini' for Star portable printers
		port = StarIOPort.getPort(portName, "mini", 10000, context);
		try{
			Thread.sleep(500);
		} catch(InterruptedException e) {
			Log.e(LOG_TAG, "**ERROR**", e);
		}
	}

	/**
	 * 
	 */
	public boolean isPaired(String portName) {
		try{
			port = StarIOPort.getPort(portName, "mini", 10000, context);
			return true;
		} catch(Exception e) {
			Log.e(LOG_TAG, "**ERROR**", e);
			return false;
		}
	}

	/**
	 * 
	 * @param data
	 */
	public void writeData(String data) throws Exception {
		// Formated Data
		String fData = formatData(data);
		// Unicode Formated Data 
		byte[] command = ucodeTranslator.convertString(fData);
		// Write data 
		port.writePort(command, 0, command.length);
		port.writePort(new byte[] {0x0a}, 0, 1);
	}

	/**
	 * 
	 * @param bitmap
	 * @param maxWidth
	 */
	public void writeData(Bitmap bitmap, int maxWidth) 
			throws StarIOPortException{
		//
		writeData(bitmap, maxWidth, false, true, false);
	}

	/**
	 * 
	 * @param bitmap
	 * @param maxWidth
	 * @param supportDithering
	 * @param compressionEnable
	 * @param pageModeEnable
	 * @throws StarIOPortException 
	 */
	public void writeData(Bitmap bitmap, int maxWidth, boolean supportDithering,
			boolean compressionEnable, boolean pageModeEnable) throws StarIOPortException {

		StarBitmap starbitmap = new StarBitmap(bitmap, false, maxWidth);
		byte[] command = starbitmap.getImageEscPosDataForPrinting(compressionEnable, pageModeEnable);
		port.writePort(command, 0, command.length);
	}
	

	/**
	 * 
	 */
	public int checkStatus(String portName) {
		StarIOPort port = null;
		try {
			port = StarIOPort.getPort(portName, "mini", 10000, context);
			//A sleep is used to get time for the socket to completely open
			try {
				Thread.sleep(500);
			} catch(InterruptedException e) {}

			StarPrinterStatus status = port.retreiveStatus();
			// Printer ONLINE		
			if(status.offline == false)
				return STATUS_OK;
			// Cover Open 
			if(status.coverOpen) 
				return STATUS_COVER_OPEN;
			// Empty Paper
			if(status.receiptPaperEmpty) 
				return STATUS_PAPER_EMPTY;
			// Printer OFFLINE	
			return STATUS_OFFLINE;
		} catch (StarIOPortException e){
			Log.e(LOG_TAG, "**ERROR**", e);
			// Printer OFFLINE	
			return STATUS_OFFLINE;
		} finally {
//			if(port != null){
//				try {
//					StarIOPort.releasePort(port);
//				} catch (StarIOPortException e) {}
//			}
		}
	}

	/**
	 * 
	 */
	public void closeConnection() {
		if(port != null){
			try {
				StarIOPort.releasePort(port);
			} catch (StarIOPortException e) {
				Log.e(LOG_TAG, "**ERROR**", e);
			}
		}
	}

	/**
	 * 
	 * @param data
	 * @return
	 */
	private String formatData(String data){
		// Bold-Text
		data = data.replace("<b>", "\u001b\u0045\u0001");  
		data = data.replace("</b>","\u001b\u0045\u0000"); 
		// Line-feed
		data = data.replace("<br>", "\n");
		// H1
		data = data.replace("<h1>", "\u001d\u0021\u0011");
		data = data.replace("</h1>","\u001d\u0021\u0000");
		// Center
		data = data.replace("<center>", "\u001b\u0061\u0031");
		data = data.replace("</center>","\u001b\u0061\u0030");
		// Right
		data = data.replace("<right>", "\u001b\u0061\u0032");
		data = data.replace("</right>","\u001b\u0061\u0030");
		// Other...

		return data;
	}
}