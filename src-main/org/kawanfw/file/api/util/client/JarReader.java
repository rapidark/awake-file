/*
 * This file is part of Awake FILE. 
 * Awake file: Easy file upload & download over HTTP with Java.                                    
 * Copyright (C) 2015,  KawanSoft SAS
 * (http://www.kawansoft.com). All rights reserved.                                
 *                                                                               
 * Awake FILE is free software; you can redistribute it and/or                 
 * modify it under the terms of the GNU Lesser General Public                    
 * License as published by the Free Software Foundation; either                  
 * version 2.1 of the License, or (at your option) any later version.            
 *                                                                               
 * Awake FILE is distributed in the hope that it will be useful,               
 * but WITHOUT ANY WARRANTY; without even the implied warranty of                
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU             
 * Lesser General Public License for more details.                               
 *                                                                               
 * You should have received a copy of the GNU Lesser General Public              
 * License along with this library; if not, write to the Free Software           
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  
 * 02110-1301  USA
 *
 * Any modifications to this file must keep this entire header
 * intact.
 */
package org.kawanfw.file.api.util.client;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.kawanfw.commons.util.ClientLogger;
import org.kawanfw.commons.util.FrameworkDebug;

/**
 * 
 * Utilities to read content of .jar files and extracing bytecode from a .class in a jar.
 * 
 * @author Nicolas de Pomereu
 *
 */
public class JarReader {

    /** For debug info */
    private static boolean DEBUG = FrameworkDebug.isSet(JarReader.class);
    
    /**
     * Protected constructor
     */
    protected JarReader() {

    }

    /**
     * Extract all classnames from a jar.
     * 
     * @param in
     *            the input stream of the jar file
     * @return the class names
     * @throws IOException
     *             ir any I/O error
     */
    public static List<String> getClassnames(InputStream in) throws IOException {

	try {
	    List<String> classNames = new ArrayList<String>();
	    ZipInputStream zip = new ZipInputStream(in);
	    for (ZipEntry entry = zip.getNextEntry(); entry != null; entry = zip
		    .getNextEntry())
		if (entry.getName().endsWith(".class") && !entry.isDirectory()) {
		    // This ZipEntry represents a class. Now, what class does it
		    // represent?
		    StringBuilder className = new StringBuilder();
		    for (String part : entry.getName().split("/")) {
			if (className.length() != 0)
			    className.append(".");
			className.append(part);
			if (part.endsWith(".class"))
			    className.setLength(className.length()
				    - ".class".length());
		    }
		    classNames.add(className.toString());
		}
	    return classNames;
	} finally {
	    IOUtils.closeQuietly(in);
	}
    }

    /**
     * Extract bytecode of .class as byte array
     * 
     * @param in
     *            the ZipInputStream
     * @param classname
     *            the class name to search for
     * @return byte array contaning the .class bytecode
     * @throws IOException
     *             if I/O errrors
     * @throws FileNotFoundException
     *             if the .class file corresponding to the classname does not
     *             exists
     */
    public static byte[] extractClassFileBytecode(InputStream in,
	    String classname) throws IOException, FileNotFoundException {

	if (in == null) {
	    throw new IllegalArgumentException("in is null!");
	}

	if (classname == null) {
	    throw new IllegalArgumentException("classname is null!");
	}

	String entryToFind = convertClassNameToEntry(classname);

	ZipInputStream zis = new ZipInputStream(in);

	byte[] byteArray = null;
	
	try {
	    ZipEntry entry;
	    while ((entry = zis.getNextEntry()) != null) {
		debug("Unzipping: " + entry.getName());

		int size;
		byteArray = new byte[2048];

		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		while ((size = zis.read(byteArray, 0, byteArray.length)) != -1) {
		    bos.write(byteArray, 0, size);
		}
		bos.flush();
		bos.close();

		if (entry.getName().equals(entryToFind)) {
		    return bos.toByteArray();
		}

	    }
	    
	    return null;
	    
	} finally {
	    zis.close();
	}
	
    }

    /**
     * Convert a class name to corresponding zip entry name
     * @param classname	the clas name to convert
     * @return the c entry name
     */
    public static String convertClassNameToEntry(String classname) {

	if (classname == null) {
	    throw new IllegalArgumentException("classname is null!");
	}

	String entryToFind = classname;
	if (entryToFind.contains(".")) {
	    entryToFind = entryToFind.replace(".", "/");
	    entryToFind = entryToFind + ".class";
	}
	return entryToFind;
    }

    /**
     * debug tool
     */
    private static void debug(String s) {
	if (DEBUG) {
	    ClientLogger.getLogger().log(Level.WARNING, s);
	}
    }
    
    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {

	InputStream in = new FileInputStream(
		"I:\\_dev_awake\\awake-file-3.0\\dist\\awake-file-3.0.jar");

	byte[] b = extractClassFileBytecode(in,
		"org.kawanfw.file.api.util.client.PathUtil");
	System.out.println("b.length: " + b.length);
	FileUtils.writeByteArrayToFile(new File("c:\\temp\\the.class"), b);

    }

}
