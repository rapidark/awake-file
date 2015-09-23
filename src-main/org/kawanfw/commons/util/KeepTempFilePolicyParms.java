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
package org.kawanfw.commons.util;
/**
 * 
 * Define if we delete the temp files created when uploading and downloading
 * Blobs/Clob.
 * 
 * @author Nicolas de Pomereu
 */

public class KeepTempFilePolicyParms {

    /** if true, the local blob files will be deleted after been uploaded */
    public static boolean KEEP_TEMP_FILE = FrameworkDebug.isSet(KeepTempFilePolicyParms.class);
    
    /**
     * No Constructor
     */
    protected KeepTempFilePolicyParms() {
    }

}
