/*******************************************************************************
 * Copyright (c) 2003, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.browser;

import org.eclipse.swt.internal.C;
import org.eclipse.swt.internal.mozilla.*;

class SingletonEnumerator {
	XPCOMObject supports;
	XPCOMObject simpleEnumerator;
	int refCount = 0;
	nsISupports value;
	boolean consumed;

public SingletonEnumerator (nsISupports value) {
	this.value = value;
	this.value.AddRef ();  
	createCOMInterfaces ();
}

int AddRef () {
	refCount++;
	return refCount;
}

void createCOMInterfaces () {
	/* Create each of the interfaces that this object implements */
	supports = new XPCOMObject (new int[] {2, 0, 0}) {
		public int /*long*/ method0 (int /*long*/[] args) {return queryInterface (args[0], args[1]);}
		public int /*long*/ method1 (int /*long*/[] args) {return AddRef ();}
		public int /*long*/ method2 (int /*long*/[] args) {return Release ();}
	};

	simpleEnumerator = new XPCOMObject (new int[] {2, 0, 0, 1, 1}) {
		public int /*long*/ method0 (int /*long*/[] args) {return queryInterface (args[0], args[1]);}
		public int /*long*/ method1 (int /*long*/[] args) {return AddRef ();}
		public int /*long*/ method2 (int /*long*/[] args) {return Release ();}
		public int /*long*/ method3 (int /*long*/[] args) {return HasMoreElements (args[0]);}
		public int /*long*/ method4 (int /*long*/[] args) {return GetNext (args[0]);}
	};
}

void disposeCOMInterfaces () {
	if (supports != null) {
		supports.dispose ();
		supports = null;
	}	
	if (simpleEnumerator != null) {
		simpleEnumerator.dispose ();
		simpleEnumerator = null;	
	}
	if (value != null) {
		value.Release ();
		value = null;	
	}
}

int /*long*/ getAddress () {
	return simpleEnumerator.getAddress ();
}

int /*long*/ queryInterface (int /*long*/ riid, int /*long*/ ppvObject) {
	if (riid == 0 || ppvObject == 0) return XPCOM.NS_ERROR_NO_INTERFACE;
	nsID guid = new nsID ();
	XPCOM.memmove (guid, riid, nsID.sizeof);

	if (guid.Equals (nsISupports.NS_ISUPPORTS_IID)) {
		XPCOM.memmove (ppvObject, new int /*long*/[] {supports.getAddress ()}, C.PTR_SIZEOF);
		AddRef ();
		return XPCOM.NS_OK;
	}
	if (guid.Equals (nsISimpleEnumerator.NS_ISIMPLEENUMERATOR_IID)) {
		XPCOM.memmove (ppvObject, new int /*long*/[] {simpleEnumerator.getAddress ()}, C.PTR_SIZEOF);
		AddRef ();
		return XPCOM.NS_OK;
	}

	XPCOM.memmove (ppvObject, new int /*long*/[] {0}, C.PTR_SIZEOF);
	return XPCOM.NS_ERROR_NO_INTERFACE;
}

int Release () {
	refCount--;
	if (refCount == 0) disposeCOMInterfaces ();
	return refCount;
}

int HasMoreElements (int /*long*/ _retval) {
	XPCOM.memmove (_retval, new int[] {consumed ? 0 : 1}, 4); /*PRBool */
	return XPCOM.NS_OK;
}	
	
int GetNext (int /*long*/ _retval) {
	if (consumed) return XPCOM.NS_ERROR_UNEXPECTED;
	consumed = true;
    value.AddRef (); 
    XPCOM.memmove (_retval, new int /*long*/[] {value.getAddress ()}, C.PTR_SIZEOF);
    return XPCOM.NS_OK;
}		
}

