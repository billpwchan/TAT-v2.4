/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author T0155040
 */
public class WontCloseBufferedInputStream extends BufferedInputStream {

    /**
     *
     * @param in
     */
    public WontCloseBufferedInputStream(InputStream in) {
        super(in);
    }


    
    public void close() {
        // Do nothing.
    }

    /**
     *
     * @throws IOException
     */
    public void reallyClose() throws IOException {
        super.close();
    }
}
