/*
 * ValidateMemoryManagement.java
 *
 * Copyright (c) 2007 Vreixo Formoso Lopes
 * Copyright (c) 2007 Operational Dynamics Consulting Pty Ltd
 * 
 * The code in this file, and the library it is a part of, are made available
 * to you by the authors under the terms of the "GNU General Public Licence,
 * version 2" See the LICENCE file for the terms governing usage and
 * redistribution.
 */
package org.gnome.glib;

import java.util.HashSet;

import org.gnome.gtk.Button;
import org.gnome.gtk.TestCaseGtk;
import org.gnome.gtk.VBox;
import org.gnome.gtk.ValidatePacking;
import org.gnome.gtk.ValidateProperties;
import org.gnome.gtk.Widget;
import org.gnome.gtk.Window;

/**
 * Validates memory management by checking many possibly conflicting
 * situations. Note that some situations may exist that are not verified
 * because they cannot be automatically checked from the Java side.
 * 
 * FIXME we need a way to check what is being occurred on C side!!
 * 
 * @author Vreixo Formoso
 * @author Andrew Cowie
 */
public class ValidateMemoryManagement extends TestCaseGtk
{

    /**
     * Subclass Button to simplify automatic testing
     */
    private static class MyButton extends Button implements Button.CLICKED
    {
        /*
         * we need a static field to check object finalization Of course, this
         * not works if many instances are created!!
         */
        static HashSet finalized = new HashSet();

        private int code;

        public MyButton(String text, int code) {
            super(text);
            this.code = code;
        }

        public int getCode() {
            return code;
        }

        protected void finalize() {
            finalized.add(new Integer(code));

            /*
             * I prefer having this commented, because it helps ensure that we
             * have a robust design against bad user bad practices. Perhaps
             * another test case can validate that once do a more robust
             * design in that regard.
             */
            super.finalize();
        }

        // ignore
        public void onClicked(Button source) {}

    }

    /**
     * Ensures that Java object is removed in the scenario when it is created
     * but never used and then goes out of scope.
     */
    public final void testObjectNeverUsedIsRemoved() {

        MyButton b;

        b = new MyButton("Live free, die young!! Like me", 1);

        cycleMainLoop();

        /* eliminate reference so object can go out of scope */
        b = null;

        cycleMainLoop();
        cycleGarbageCollector();

        /* check if button has been deleted */
        assertTrue("An object created but never used is not deleted",
                MyButton.finalized.contains(new Integer(1)));
    }

    /**
     * Ensure that a Java Proxy is <b>not</b> removed when the GObject it is
     * proxying is still being used on C side
     */
    public final void testObjectNotRemovedWhenStillGtkAlive() {
        MyButton b;
        final VBox x;

        x = new VBox(false, 3);
        b = new MyButton("Old rockers never die!!", 2);

        /* add button to VBox */
        x.add(b);

        cycleMainLoop();

        /* object gets out of scope */
        b = null;

        cycleMainLoop();
        cycleGarbageCollector();

        /* check that button hasn't been deleted */
        assertFalse("Object deleted in Java but still alives in GTK",
                MyButton.finalized.contains(new Integer(2)));
    }

    /**
     * Ensure that a Java Proxy is removed when it goes out of scope on the
     * Java side and when GTK has dropped all of its Refs to the GObject on
     * the C side.
     */
    public final void testRemovingAfterNoLongerReachableInBothSides() {
        MyButton b;
        final VBox x;

        x = new VBox(false, 3);
        b = new MyButton("Ready to rock", 3);

        /* add button to VBox */
        x.add(b);

        cycleMainLoop();

        /* out of scope in Gtk+ */
        x.remove(b);

        cycleMainLoop();

        /* out of scope in Java */
        b = null;

        cycleMainLoop();
        cycleGarbageCollector();

        /* check that button has been deleted */
        assertTrue("Object no more needed is not deleted", MyButton.finalized.contains(new Integer(3)));
    }

    /**
     * We have a strong Java reference from the BindingsJavaClosure use by
     * g_signal_connect() to the Java Signal sublclass passed as the handler
     * to Plumbing.connectSignal(). If the handler is the Proxy (self
     * delegation), then we have a cyclic reference. This test creates that
     * situation and tries to see if finalization occurs when the GObject ref
     * count drops to 1 (our ToggleRef)
     */
    public final void testAvoidCyclicReferenceFromSignalHookup() {
        MyButton b;
        final VBox x;

        x = new VBox(false, 3);
        b = new MyButton("I will reference myself", 4);

        /* connect button to itself. cyclic ref! */
        b.connect(b);

        cycleMainLoop();

        /* add button to VBox */
        x.add(b);

        cycleMainLoop();

        /* out of scope in Gtk+ */
        x.remove(b);

        cycleMainLoop();

        /* out of scope in Java */
        b = null;

        cycleMainLoop();
        cycleGarbageCollector();

        /* check that button has been deleted */
        assertTrue("Refence from the signal handler to an Object where that Object is a" + "\n"
                + "self-delegated signal handler was not cleared! This is, in effect, a cyclic" + "\n"
                + "reference that is not being detected by our memory management mechanism.",
                MyButton.finalized.contains(new Integer(4)));
    }

    /**
     * Ensures no denaturation occurs, i.e., the Object retrieved from a GTK
     * call is actually the same class (the same fully derived object) as the
     * original object. In fact, it had better be <i>that</i> object.
     */
    /*
     * This test is redundant, because we need not only prevent denaturation,
     * but indeed get the _same_ object, which is tested below. Just keep this
     * here to known better what is the problem if any
     */
    public final void testNoDenaturation() {
        MyButton b;
        Window w;
        Widget c;

        w = new Window();
        b = new MyButton("To be or not to be...", 5);

        /* add button to Window */
        w.add(b);

        cycleMainLoop();

        /* get button from window */
        c = w.getChild();

        cycleMainLoop();

        assertNotNull("Retrieved null from Window.getChild()", c);
        assertTrue("Proxy retrieved for our MyButton subclass is not an instance of Button!",
                c instanceof Button);
        assertTrue("Proxy retrieved is a Button, not a MyButton instance. Denaturation occured!",
                c instanceof MyButton);

        /* and finally we check object retrieved is the _same_ */
        assertSame("A new Proxy was created when it should have been the already existing one", b, c);
        /* now we delete our refs */
        b = null;
        c = null;

        cycleMainLoop();
        cycleGarbageCollector();

        /* get button again and check */
        c = w.getChild();
        assertTrue("Proxy retrieved after a reference collection cycle is not our" + "\n"
                + "MyButton instance. Denaturation occured?", c instanceof MyButton);

    }

    /**
     * A new Java object should not be created when developer retrieves a
     * previously referenced Widget from GTK. This is actually tested all over
     * the framework; see {@link ValidatePacking#testGetChildAndParent()} and
     * {@link ValidateProperties#testButtonReliefStyle()}; this test ensures
     * main loop cycling and garbage collection does not interfere.
     */
    public final void testNoMultipleCreation() {
        MyButton b;
        final Window w;
        Widget c;

        w = new Window();
        b = new MyButton("I don't wanna be Dolly", 6);

        /* add button to Window */
        w.add(b);

        cycleMainLoop();

        /* get button from window */
        c = w.getChild();

        assertNotNull("Retrieved a null object", c);
        assertTrue("Not a MyButton instance", c instanceof MyButton);

        /* and finally we check object retrieved is the _same_ */
        assertSame("A different object was created", b, c);

        /* now we delete our refs */
        b = null;
        c = null;

        cycleMainLoop();
        cycleGarbageCollector();

        /* get button again and check */
        c = w.getChild();

        assertNotNull("Retrieved a null object", c);
        assertTrue("Not a MyButton instance", c instanceof MyButton);

        assertEquals("A different object was created", 6, ((MyButton) c).getCode());
    }
}
