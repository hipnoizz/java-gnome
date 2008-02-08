/*
 * Scale.java
 *
 * Copyright (c) 2007-2008 Operational Dynamics Consulting Pty Ltd
 *
 * The code in this file, and the library it is a part of, are made available
 * to you by the authors under the terms of the "GNU General Public Licence,
 * version 2" plus the "Classpath Exception" (you may link to this code as a
 * library into other programs provided you don't make a derivation of it).
 * See the LICENCE file for the terms governing usage and redistribution.
 */
package org.gnome.gtk;

/**
 * A slider control which allows the user to manipulate a numeric value. As
 * with many other Widget hierarchies in GTK, there is a horizontal ({@link HScale})
 * and vertical ({@link VScale}) implementation for you to choose from.
 * 
 * <p>
 * Most of the useful methods (notably those relating to the value) are
 * inherited from the parent class, {@link Range}.
 * 
 * @author Andrew Cowie
 * @since 4.0.6
 */
/*
 * TODO needs a snapshot of HScale and VScale
 */
public abstract class Scale extends Range
{
    protected Scale(long pointer) {
        super(pointer);
    }

    /**
     * Specify the number of decimal places that will be shown in the value.
     * This also rounds the value so that when it is retrieved it will match
     * what is displayed.
     * 
     * @since 4.0.6
     */
    public void setDigits(int places) {
        GtkScale.setDigits(this, places);
    }
}
