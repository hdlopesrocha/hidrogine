package com.enter4ward.myserver;

import com.enter4ward.webserver.HttpServer;

/**
 * The Class Global.
 */
public final class Global {

    /**
     * Instantiates a new global.
     */
    private Global() {
    }

    /**
     * The main method.
     *
     * @param args
     *            the arguments
     */
    public static void main(final String[] args) {
    	new HttpServer("config.json").run();
    }
}
