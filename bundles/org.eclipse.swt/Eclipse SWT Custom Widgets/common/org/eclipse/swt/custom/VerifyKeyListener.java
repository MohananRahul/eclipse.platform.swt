package org.eclipse.swt.custom;
/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.events.*;
import org.eclipse.swt.internal.EventListenerCompatibility;

public interface VerifyKeyListener extends EventListenerCompatibility {
/**
 * @param event.character the character that was typed (input)	
 * @param event.keyCode the key code that was typed (input)
 * @param event.stateMask the state of the keyboard (input)
 * @param event.doit processed or not (output)
 */
public void verifyKey (VerifyEvent event);
}
