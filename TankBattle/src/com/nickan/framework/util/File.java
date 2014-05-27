package com.nickan.framework.util;


import java.util.Scanner;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

/**
 * Handles reading and writing of files (supposedly), still work in progress
 * @author Nickan
 *
 */
public class File {
	
	private File() { }
	
	/**
	 * 
	 * @param filePath
	 * @param type
	 * @return The new instance of java.util.Scanner
	 */
	public static Scanner newScanner(String filePath, FileType type) {
		FileHandle file = getFile(filePath, type);
		return new Scanner(file.read());
	}
	
	/**
	 * 
	 * @param strFile
	 * @param fileType
	 * @return FileHandle instance
	 */
	private static FileHandle getFile(String strFile, FileType fileType) {
		FileHandle file = null;
		if (fileType == FileType.Internal) {
			try {
				file = Gdx.files.internal(strFile);
			} catch (Exception e) {
				Gdx.app.log("FileHandle", "Could not get internal file '" + strFile +  "'");
			}
		} else if (fileType == FileType.Local) {
			try {
				file = Gdx.files.local(strFile);
			} catch (Exception e) {
				Gdx.app.log("FileHandle", "Could not get local file '" + strFile +  "'");
			}
		} else if (fileType == FileType.External) {
			try {
				file = Gdx.files.external(strFile);
			} catch (Exception e) {
				Gdx.app.log("FileHandle", "Could not get external file '" + strFile +  "'");
			}
		}
		return file;
	}
	
}
