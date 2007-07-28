/*
 * Notebook.java
 *
 * Copyright (c) 2007 Operational Dynamics Consulting Pty Ltd, and Others
 *
 * The code in this file, and the library it is a part of, are made available
 * to you by the authors under the terms of the "GNU General Public Licence,
 * version 2" plus the "Classpath Exception" (you may link to this code as a
 * library into other programs provided you don't make a derivation of it).
 * See the LICENCE file for the terms governing usage and redistribution.
 */
package org.gnome.gtk;

/**
 * A tabbed notebook container. These are common sights in web browsers and
 * text editors and are the recommended way to create user interfaces that
 * must manage multiple documents if creating a completely independent window
 * per document (the prefered GNOME approach) is inappropriate.
 * 
 * <p>
 * Note that Notebooks are a poor way to organize pages of application
 * preferences; if your program has that many options there is probably
 * something very wrong with your design in the first place. Do you <i>really</i>
 * need to present that many different configuration settings to the user?
 * 
 * @author Sebastian Mancke
 * @author Andrew Cowie
 * @since 4.0.3
 */
/*
 * The various add page functions return -1 on failure. This should be
 * intercepted and a RuntimeException thrown.
 */
public class Notebook extends Container
{
    protected Notebook(long pointer) {
        super(pointer);
    }

    /**
     * Constructs a new Notebook
     */
    public Notebook() {
        super(GtkNotebook.createNotebook());
    }

    /**
     * Append a new tab to the Notebook after the current tab.
     * 
     * @param child
     *            The Widget to be shown on the new Notebook page.
     * @param tabLabel
     *            The Label Widget you want to use for the tab itself.
     */
    public int appendPage(Widget child, Widget tabLabel) {
        return GtkNotebook.appendPage(this, child, tabLabel);
    }

    /**
     * Insert a tab to the Notebook before the current tab.
     * 
     * @param child
     *            The Widget shown on the new Notebook page.
     * @param tabLabel
     *            The Label Widget for the tab
     * @return the position of the prepended tab.
     */
    public int prependPage(Widget child, Widget tabLabel) {
        return GtkNotebook.prependPage(this, child, tabLabel);
    }

    /**
     * Insert a tab at the supplied position in the Notebook
     * 
     * @param child
     *            The Widget shown on the new Notebook page.
     * @param tabLabel
     *            The Label Widget to use on the tab itself.
     * @param position
     *            The possition at which to insert the page.
     * @return the position in the Notebook of the inserted tab.
     */
    public int insertPage(Widget child, Widget tabLabel, int position) {
        return GtkNotebook.insertPage(this, child, tabLabel, position);
    }

    /**
     * Remove a tab
     * 
     * @param pageNum
     *            The position number (from 0) of the page to remove.
     */
    public void removePage(int pageNum) {
        GtkNotebook.removePage(this, pageNum);
    }

    /**
     * Activate/show the page at the supplied position
     * 
     * @param pageNum
     *            Position of the page to activate
     */
    public void setCurrentPage(int pageNum) {
        GtkNotebook.setCurrentPage(this, pageNum);
    }

    /**
     * The handler interface for notification of changes in the current page.
     */
    public interface CHANGE_CURRENT_PAGE extends GtkNotebook.CHANGE_CURRENT_PAGE
    {
        /**
         * @param offset the tab which is now the current page.
         */
        public void onChangeCurrentPage(Notebook sourceObject, int offset);
    }

    /**
     * Connects a {@see CHANGE_CURRENT_PAGE} handler to the Notebook.
     */
    public void connect(CHANGE_CURRENT_PAGE handler) {
        GtkNotebook.connect(this, handler);
    }
}