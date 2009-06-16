/*
 * App.java
 *
 * Copyright (c) 2009 Operational Dynamics Consulting Pty Ltd
 * 
 * The code in this file, and the library it is a part of, are made available
 * to you by the authors under the terms of the "GNU General Public Licence,
 * version 2" plus the "Classpath Exception" (you may link to this code as a
 * library into other programs provided you don't make a derivation of it).
 * See the LICENCE file for the terms governing usage and redistribution.
 */
package org.gnome.unique;

import org.gnome.glib.Object;

/**
 * 
 * @author Andrew Cowie
 * @since 4.0.12
 */
public class Application extends Object
{
    protected Application(long pointer) {
        super(pointer);
    }

    /**
     * 
     * By convention, the name chosen to identify a unique application should
     * follow the application naming conventions used by DBus (these are
     * similar to the domain name -esque conventions used in Java package
     * space). Some examples are:
     * 
     * <ul>
     * <li><code>"org.gnome.Nautilus"</code><br>
     * (used by the Nautilus file manager)
     * <li><code>"org.gnome.Vino.Preferences"</code><br>
     * (used by the Vino VNC server's preferences &amp; configuration utility)
     * <li><code>"com.operationaldynamics.Slashtime"</code><br>
     * (used by the Slashtime timezone viewer program).
     * </ul>
     * 
     * @since 4.0.12
     */
    public Application(String name, String id) {
        super(UniqueApp.createApplication(basicValidation(name), id));
    }

    private static String basicValidation(final String name) {
        if (name.equals("")) {
            throw new IllegalArgumentException("name cannot be emtpy.");
        }
        if (name.indexOf('.') == -1) {
            throw new IllegalArgumentException("name needs follow DBus application naming conventions.");
        }
        if (name.indexOf('$') != -1) {
            throw new IllegalArgumentException("name cannot contain '$'.");
        }
        return name;
    }

    /**
     * Is some <i>other</i> instance of this program (ie the application with
     * this name) already running?
     * 
     * @since 4.0.12
     */
    public boolean isRunning() {
        return UniqueApp.isRunning(this);
    }

    /**
     * Get the application name that was used when this Application was
     * constructed.
     * 
     * @since 4.0.12
     */
    public String getName() {
        return getPropertyString("name");
    }
}
