package com.enter4ward.webserver;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.zip.GZIPOutputStream;

import com.enter4ward.session.Session;


/**
 * The Class Response.
 */
public class Response {



	public static final String CODE_OK = "200 OK";

    /** The version. */
    private String version = "1.1";

    /** The status. */
    private String status = CODE_OK;

    /** The data. */
    private byte[] data = new byte[0];

    private Map<String, String> headers = new TreeMap<String,String>();
    
    /**
     * Sets the content type.
     *
     * @param c
     *            the new content type
     */
    public final void setContentType(final String c) {
    	addHeader(Headers.CONTENT_TYPE, c );
    }

    /**
     * Instantiates a new response.
     *
     * @param s
     *            the s
     * @param request
     *            the r
     */
    
    
    
    public void setContent(String text){
    	 setContentType(ContentTypes.TEXT_HTML);
         setContent(text.getBytes());
    }
    
    /**
     * Sets the data.
     *
     * @param d
     *            the new data
     */
    public final void setContent(final byte[] d) {
        data = d;
    }

    

    public void setContent(File file){
    	StringTokenizer fileToks = new StringTokenizer(file.getName(), ".");
        
        try {
            setContent(read(file));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            // e.printStackTrace();
        }
        if (fileToks.countTokens() > 1) {
            fileToks.nextElement();
            setContentType(fileToks.nextToken());
        } else {
            setContentType(ContentTypes.TEXT_HTML);
        }
    }
    
    public void setContent(File file, String filename){
    	setContent(file);
    	setContentDisposition("attachment; filename=\"" + filename
                + "\"");
    }
    
    
    public void addHeader(String key, String value){
    	headers.put(key,value);
    }


    
    /**
     * Read.
     *
     * @param file
     *            the file
     * @return the byte[]
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    private byte[] read(final File file) throws IOException {

        byte[] buffer = new byte[(int) file.length()];
        InputStream ios = null;
        try {
            ios = new FileInputStream(file);
            if (ios.read(buffer) == -1) {
                throw new IOException(
                        "EOF reached while trying to read the whole file");
            }
        } finally {
            if (ios != null) {
                ios.close();
            }
        }

        return buffer;
    }

    public void setChunked(){
    	headers.put(Headers.TRANSFER_ENCODING,"chunked");
    }
    
    
    protected Response(final String status, final Request request, Session session) {
        this.status = status;        
        if (request.acceptEncoding("gzip")) {
        	headers.put(Headers.CONTENT_ENCODING, "gzip");
        }
        headers.put(Headers.CONNECTION,(request.keepAlive()? "keep-alive":"close"));        
    
        

        if (session != null) {
        	headers.put(Headers.SET_COOKIE,"ID=" + session.getId() + "; Max-Age="
                    + session.getMaxAge() + "; Version=1;Path=/;");
        }
    }

    /**
     * Gets the server time.
     *
     * @return the server time
     */
    final String getServerTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormat.format(calendar.getTime());
    }

    /**
     * Builds the header.
     *
     * @param size
     *            the size
     * @param cookie
     *            the cookie
     * @return the string
     */
    final String buildHeader() {
        String s = "";
        s += "HTTP/" + version + " " + status + "\r\n";
        s += "Date: " + getServerTime() + "\r\n";
        s += "Server: com.enter4ward/" + HttpServer.VERSION + "\r\n";
       
        for(Entry<String, String> e : headers.entrySet()){
        	s+=e.getKey()+": "+e.getValue()+ "\r\n";
        }
        s += "\r\n";
        return s;
    }

    /**
     * Sets the content disposition.
     *
     * @param c the new content disposition
     */
    public final void setContentDisposition(final String c) {
       headers.put(Headers.CONTENT_DIPOSITION, c);
    }

    /**
     * Send data.
     *
     * @param socket
     *            the socket
     * @param session
     *            the session
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    public final byte [] build()
            throws IOException {
    	    	
    	if ("gzip".equals(headers.get(Headers.CONTENT_ENCODING))) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            GZIPOutputStream gzip = new GZIPOutputStream(out);
            gzip.write(data);
            gzip.close();
            data = out.toByteArray();
        }
    	if(data!=null){
        	addHeader(Headers.CONTENT_LENGTH, Integer.toString(data.length));    		
    	}
    	
    	
        byte[] header = buildHeader().getBytes();
        byte[] packg = new byte[header.length + data.length];
        System.arraycopy(header, 0, packg, 0, header.length);
        System.arraycopy(data, 0, packg, header.length, data.length);
        
        return packg;
    }

    
    
    

}
