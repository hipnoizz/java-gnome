/*
 * CheckMenuItem.java
 *
 * Copyright (c) 2007 Operational Dynamics Consulting Pty Ltd
 *
 * The code in this file, and the library it is a part of, are made available
 * to you by the authors under the terms of the "GNU General Public Licence,
 * version 2" plus the "Classpath Exception" (you may link to this code as a
 * library into other programs provided you don't make a derivation of it).
 * See the LICENCE file for the terms governing usage and redistribution.
 */
package org.gnome.gtk;

/**
 * A MenuItem that maintains a <code>boolean</code> state.
 * 
 * <p>
 * A CheckMenuItem is just like a MenuItem, but additionally it displays a
 * Check box together with the MenuItem label, indicating the state of the
 * boolean value it holds. When that value is set to <code>true</code>, the
 * item is <i>active</i> and the Check box is checked.
 * 
 * <p>
 * The active state is switched automatically when the user activates the
 * item. You can check the current state with the {@link #getActive()} method.
 * 
 * <p>
 * You can use a CheckMenuItem as a way to allow users of your application
 * enable or disable an application option. This is usually used with GUI
 * options, for example to let the users hide an optional Widget of your user
 * interface.
 * 
 * <p>
 * While you can still connect to the <code>ACTIVATE</code> signal, in this
 * case there is a better option, the {@link TOGGLED} signal, that is emitted
 * when the active state changes.
 * 
 * @see MenuItem
 * 
 * @author Vreixo Formoso
 * @since 4.0.4
 */
public class CheckMenuItem extends MenuItem
{
    protected CheckMenuItem(long pointer) {
        super(pointer);
    }

    /**
     * Construct a CheckMenuItem
     */
    public CheckMenuItem() {
        super(GtkCheckMenuItem.createCheckMenuItem());
    }

    /**
     * Construct a CheckMenuItem with a given text label. The label may
     * contain underscores (<code>_<code>) which, if present, will indicate the
     * mnemonic which will activate that CheckMenuItem directly if that key is
     * pressed.
     */
    public CheckMenuItem(String mnemonicLabel) {
        super(GtkCheckMenuItem.createCheckMenuItemWithMnemonic(mnemonicLabel));
    }

    /**
     * Construct a CheckMenuItem with a given text label, and additionally
     * connect a handler to its TOGGLED signal.
     */
    public CheckMenuItem(String mnemonicLabel, TOGGLED handler) {
        super(GtkCheckMenuItem.createCheckMenuItemWithMnemonic(mnemonicLabel));
        connect(handler);
    }

    /**
     * Set the active state of the Item. This is switched automatically when
     * the user activates (clicks) the menu item, but in some situations you
     * will want to change it manually.
     */
    public void setActive(boolean isActive) {
        GtkCheckMenuItem.setActive(this, isActive);
    }

    /**
     * Retrieve the active state of the item.
     */
    public boolean getActive() {
        return GtkCheckMenuItem.getActive(this);
    }

    /**
     * Set the inconsistent state. This refers to an additional third state
     * meaning that currently it cannot be decided what is the active state of
     * the item.
     * 
     * <p>
     * Think, for example, in a text editor application, in which a
     * CheckMenuItem is used to choose between a bold or a normal font. If the
     * user selects a range of text where both normal and bold fonts are being
     * used, the state is inconsistent, and we want to mark it in a different
     * way.
     * 
     * <p>
     * However, note that, while such property can be really useful in a
     * {@link ToggleToolButton}, its utility in a CheckMenuItem is really
     * unclear.
     * 
     * <p>
     * Notice also that this property only affects visual appearance, it
     * doesn't affect the semantics of the Widget.
     */
    public void setInconsistent(boolean setting) {
        GtkCheckMenuItem.setInconsistent(this, setting);
    }

    /**
     * Get the inconsistent state.
     * 
     * @see #setInconsistent(boolean)
     */
    public boolean getInconsistent() {
        return GtkCheckMenuItem.getInconsistent(this);
    }

    /**
     * The handler interface for a change in the active state. This is
     * triggered when the active state changes, either when the user activates
     * the MenuItem, or when it is changed with
     * {@link CheckMenuItem#setActive(boolean) setActive()}.
     * 
     * @see MenuItem.ACTIVATE
     */
    public interface TOGGLED extends GtkCheckMenuItem.TOGGLED
    {
        void onToggled(CheckMenuItem sourceObject);
    }

    /**
     * Connect an {@link TOGGLED} handler to the widget.
     */
    public void connect(TOGGLED handler) {
        GtkCheckMenuItem.connect(this, handler);
    }

}
