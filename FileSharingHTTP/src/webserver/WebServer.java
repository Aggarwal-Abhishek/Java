
package webserver;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.Vector;


public class WebServer {

	
	
	public static String currentPath = new File("").getAbsolutePath();
	public static String uploadPath = currentPath + "/";
	
	String[] keys = new String[10];
	Task[] tasks = new Task[10];
	int key_pos = 0;
	
	public static abstract class Task {
		public abstract Response run(String uri, Properties header) ;
	}
	public void addTask(String uri, Task task) {
		keys[key_pos] = uri;
		tasks[key_pos] = task;
		++key_pos;
	}
	public void removeTask(String uri) {
		
		for(int i=0; i<key_pos; i++) {
			if(keys[i].equals(uri)) {
				
				for(int j=i+1; j<key_pos; j++) {
					keys[j-1] = keys[j];
					tasks[j-1] = tasks[j];
				}
				--key_pos;
				return;
			}
		}
		
		
		System.out.println("No Task found on uri : " + uri);
		System.out.println("All Tasks are : ");
		for(String x : keys) {
			System.out.println(x);
		}
	
	}
	
	
	public Response send404() {
		return new Response(HTTP_NOTFOUND, MIME_PLAINTEXT,
                "Error 404, file not found.");
	}
	public Response redirect(String uri) {
		Response r = new Response(HTTP_REDIRECT, MIME_HTML,
                "<html><body>Redirected: <a href=\"" + uri + "\">" +
                        uri + "</a></body></html>");
        r.addHeader("Location", uri);
        return r;
	}
	public Response serverFile(String uri, Properties header) {
		try {
			
			File f = new File(uri);
            
            String mime = null;
            int dot = f.getCanonicalPath().lastIndexOf('.');
            if (dot >= 0)
                mime = (String) theMimeTypes.get(f.getCanonicalPath().substring(dot + 1).toLowerCase());
            if (mime == null)
                mime = MIME_DEFAULT_BINARY;

            
            long startFrom = 0;
            String range = header.getProperty("range");
            if (range != null) {
                if (range.startsWith("bytes=")) {
                    range = range.substring("bytes=".length());
                    int minus = range.indexOf('-');
                    if (minus > 0)
                        range = range.substring(0, minus);
                    try {
                        startFrom = Long.parseLong(range);
                    } catch (NumberFormatException nfe) {
                    }
                }
            }

            FileInputStream fis = new FileInputStream(f);
            fis.skip(startFrom);
            Response r = new Response(HTTP_OK, mime, fis);
            r.addHeader("Content-length", "" + (f.length() - startFrom));
            r.addHeader("Content-range", "" + startFrom + "-" +
                    (f.length() - 1) + "/" + f.length());
            return r;
        } catch (IOException ioe) {
            return new Response(HTTP_FORBIDDEN, MIME_PLAINTEXT, "FORBIDDEN: Reading file failed.");
        }
	}
	public Response send(String txt, String mime) {
		return new Response(HTTP_OK, mime, txt) ;
	}
	
	
	
	
	public Response serve(String uri, String method, Properties header, Properties parms, Properties files) {
//        System.out.println(method + " '" + uri + "' ");

        
//        Enumeration<?> e = header.propertyNames();
//        while (e.hasMoreElements()) {
//            String value = (String) e.nextElement();
//            System.out.println("  HDR: '" + value + "' = '" +
//                    header.getProperty(value) + "'");
//        }
//        e = parms.propertyNames();
//        while (e.hasMoreElements()) {
//            String value = (String) e.nextElement();
//            System.out.println("  PRM: '" + value + "' = '" +
//                    parms.getProperty(value) + "'");
//        }
//        e = files.propertyNames();
//        while (e.hasMoreElements()) {
//            String value = (String) e.nextElement();
//            System.out.println("  UPLOADED: '" + value + "' = '" +
//                    files.getProperty(value) + "'");
//        }
         
        return serveFile(uri, header, new File("."), true);
    }

    
    
    
	
	
    
    
    
    
    

    public Response serveFile(String uri, Properties header, File homeDir,
                              boolean allowDirectoryListing) {


    	uri = uri.replace("\\", "/");
    	if(!uri.startsWith("/")) uri = "/" + uri;
    	if(!uri.endsWith("/")) uri = uri + "/";
    	
    	
    	
    	for(int i=0; i<key_pos; i++) {
    		if(uri.startsWith(keys[i])) {
    			return tasks[i].run(uri, header);
    		}
    	}
    	
    	System.out.println("No Task found on uri : " + uri);
		System.out.println("All Tasks are : ");
		for(String x : keys) {
			System.out.println(x);
		}
        return send404();
    }

    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    public static void MoveFileToUpload(String from, String name, String to) {
   	 try {
        	
   		 if(new File(to, name).exists()) {
   			 name = System.currentTimeMillis() + name ;
   		 }
        	
				Files.move(
						Paths.get(from),
						Paths.get(to + name)
				);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
   }
   
   
   
   
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    public class Response {

    	public Response() {
            this.status = HTTP_OK;
        }
        public Response(String status, String mimeType, InputStream data) {
            this.status = status;
            this.mimeType = mimeType;
            this.data = data;
        }
        public Response(String status, String mimeType, String txt) {
            this.status = status;
            this.mimeType = mimeType;
            try {
                this.data = new ByteArrayInputStream(txt.getBytes("UTF-8"));
            } catch (java.io.UnsupportedEncodingException uee) {
                uee.printStackTrace();
            }
        }

        
        public void addHeader(String name, String value) {
            header.put(name, value);
        }

        
        public String status;

        
        public String mimeType;

        
        public InputStream data;

        
        public Properties header = new Properties();
    }

    
    
    
    
    
    
    
    
    
    
    
    
    public static final String
            HTTP_OK = "200 OK",
            HTTP_REDIRECT = "301 Moved Permanently",
            HTTP_FORBIDDEN = "403 Forbidden",
            HTTP_NOTFOUND = "404 Not Found",
            HTTP_BADREQUEST = "400 Bad Request",
            HTTP_INTERNALERROR = "500 Internal Server Error",
            HTTP_NOTIMPLEMENTED = "501 Not Implemented";

   
    public static final String
            MIME_PLAINTEXT = "text/plain",
            MIME_HTML = "text/html",
            MIME_DEFAULT_BINARY = "application/octet-stream",
            MIME_XML = "text/xml";

    
    
    
    
    
   
    public WebServer(int port) throws IOException {
        myTcpPort = port;
        myServerSocket = new ServerSocket(myTcpPort);
        myThread = new Thread(new Runnable() {
            public void run() {
                try {
                    while (true)
                        new HTTPSession(myServerSocket.accept());
                } catch (IOException ioe) {
                }
            }
        });
        myThread.setDaemon(true);
        myThread.start();
    }

    
    public void stop() {
        try {
            myServerSocket.close();
            myThread.join();
        } catch (IOException ioe) {
        } catch (InterruptedException e) {
        }
    }


    
    
    
    
    
    
    
    
    public static void main(String[] args) {
        

        
        
        int port ;
        
        try {
        	port = Integer.parseInt(args[0]);
        }
        catch(Exception e) {port = 80;}
        

        try {
            new WebServer(port);
        } catch (IOException ioe) {
            System.err.println("Couldn't start server:\n" + ioe);
            System.exit(-1);
        }

        System.out.println("Now serving files in port " + port + " from \"" +
                new File("").getAbsolutePath() + "\"");
        while (true) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    

    
    private class HTTPSession implements Runnable {
        public HTTPSession(Socket s) {
            mySocket = s;
            Thread t = new Thread(this);
            t.setDaemon(true);
            t.start();
        }

        public void run() {
            try {
                InputStream is = mySocket.getInputStream();
                if (is == null) return;

                
                int bufsize = 8192;
                byte[] buf = new byte[bufsize];
                int rlen = is.read(buf, 0, bufsize);
                if (rlen <= 0) return;

                
                ByteArrayInputStream hbis = new ByteArrayInputStream(buf, 0, rlen);
                BufferedReader hin = new BufferedReader(new InputStreamReader(hbis));
                Properties pre = new Properties();
                Properties parms = new Properties();
                Properties header = new Properties();
                Properties files = new Properties();

                
                decodeHeader(hin, pre, parms, header);
                String method = pre.getProperty("method");
                String uri = pre.getProperty("uri");

                long size = 0x7FFFFFFFFFFFFFFFl;
                String contentLength = header.getProperty("content-length");
                if (contentLength != null) {
                    try {
                        size = Integer.parseInt(contentLength);
                    } catch (NumberFormatException ex) {
                    }
                }

                
                int splitbyte = 0;
                boolean sbfound = false;
                while (splitbyte < rlen) {
                    if (buf[splitbyte] == '\r' && buf[++splitbyte] == '\n' && buf[++splitbyte] == '\r' && buf[++splitbyte] == '\n') {
                        sbfound = true;
                        break;
                    }
                    splitbyte++;
                }
                splitbyte++;

                
                ByteArrayOutputStream f = new ByteArrayOutputStream();
                if (splitbyte < rlen) f.write(buf, splitbyte, rlen - splitbyte);

                
                if (splitbyte < rlen)
                    size -= rlen - splitbyte + 1;
                else if (!sbfound || size == 0x7FFFFFFFFFFFFFFFl)
                    size = 0;

                
                buf = new byte[512];
                while (rlen >= 0 && size > 0) {
                    rlen = is.read(buf, 0, 512);
                    size -= rlen;
                    if (rlen > 0)
                        f.write(buf, 0, rlen);
                }

                
                byte[] fbuf = f.toByteArray();

                
                ByteArrayInputStream bin = new ByteArrayInputStream(fbuf);
                BufferedReader in = new BufferedReader(new InputStreamReader(bin));

                
                if (method.equalsIgnoreCase("POST")) {
                    String contentType = "";
                    String contentTypeHeader = header.getProperty("content-type");
                    StringTokenizer st = new StringTokenizer(contentTypeHeader, "; ");
                    if (st.hasMoreTokens()) {
                        contentType = st.nextToken();
                    }

                    if (contentType.equalsIgnoreCase("multipart/form-data")) {
                        
                        if (!st.hasMoreTokens())
                            sendError(HTTP_BADREQUEST, "BAD REQUEST: Content type is multipart/form-data but boundary missing. Usage: GET /example/file.html");
                        String boundaryExp = st.nextToken();
                        st = new StringTokenizer(boundaryExp, "=");
                        if (st.countTokens() != 2)
                            sendError(HTTP_BADREQUEST, "BAD REQUEST: Content type is multipart/form-data but boundary syntax error. Usage: GET /example/file.html");
                        st.nextToken();
                        String boundary = st.nextToken();

                        decodeMultipartData(boundary, fbuf, in, parms, files);
                    } else {
                        
                        String postLine = "";
                        char pbuf[] = new char[512];
                        int read = in.read(pbuf);
                        while (read >= 0 && !postLine.endsWith("\r\n")) {
                            postLine += String.valueOf(pbuf, 0, read);
                            read = in.read(pbuf);
                        }
                        postLine = postLine.trim();
                        decodeParms(postLine, parms);
                    }
                }

                
                Response r = serve(uri, method, header, parms, files);
                if (r == null)
                    sendError(HTTP_INTERNALERROR, "SERVER INTERNAL ERROR: Serve() returned a null response.");
                else
                    sendResponse(r.status, r.mimeType, r.header, r.data);

                in.close();
                is.close();
                if (method.equalsIgnoreCase("GET")) {
                    if (parms.getProperty("exit") != null) {
                        System.exit(0);
                    }
                }
            } catch (IOException ioe) {
                try {
                    sendError(HTTP_INTERNALERROR, "SERVER INTERNAL ERROR: IOException: " + ioe.getMessage());
                } catch (Throwable t) {
                }
            } catch (InterruptedException ie) {
                
            }
        }

        
        
        
        private void decodeHeader(BufferedReader in, Properties pre, Properties parms, Properties header)
                throws InterruptedException {
            try {
                
                String inLine = in.readLine();
                if (inLine == null) return;
                StringTokenizer st = new StringTokenizer(inLine);
                if (!st.hasMoreTokens())
                    sendError(HTTP_BADREQUEST, "BAD REQUEST: Syntax error. Usage: GET /example/file.html");

                String method = st.nextToken();
                pre.put("method", method);

                if (!st.hasMoreTokens())
                    sendError(HTTP_BADREQUEST, "BAD REQUEST: Missing URI. Usage: GET /example/file.html");

                String uri = st.nextToken();

                
                int qmi = uri.indexOf('?');
                if (qmi >= 0) {
                    decodeParms(uri.substring(qmi + 1), parms);
                    uri = decodePercent(uri.substring(0, qmi));
                } else uri = decodePercent(uri);

                
                
                if (st.hasMoreTokens()) {
                    String line = in.readLine();
                    while (line != null && line.trim().length() > 0) {
                        int p = line.indexOf(':');
                        if (p >= 0)
                            header.put(line.substring(0, p).trim().toLowerCase(), line.substring(p + 1).trim());
                        line = in.readLine();
                    }
                }

                pre.put("uri", uri);
            } catch (IOException ioe) {
                sendError(HTTP_INTERNALERROR, "SERVER INTERNAL ERROR: IOException: " + ioe.getMessage());
            }
        }

        
        private void decodeMultipartData(String boundary, byte[] fbuf, BufferedReader in, Properties parms, Properties files)
                throws InterruptedException {
            try {
                int[] bpositions = getBoundaryPositions(fbuf, boundary.getBytes());
                int boundarycount = 1;
                String mpline = in.readLine();
                while (mpline != null) {
                    if (mpline.indexOf(boundary) == -1)
                        sendError(HTTP_BADREQUEST, "BAD REQUEST: Content type is multipart/form-data but next chunk does not start with boundary. Usage: GET /example/file.html");
                    boundarycount++;
                    Properties item = new Properties();
                    mpline = in.readLine();
                    while (mpline != null && mpline.trim().length() > 0) {
                        int p = mpline.indexOf(':');
                        if (p != -1)
                            item.put(mpline.substring(0, p).trim().toLowerCase(), mpline.substring(p + 1).trim());
                        mpline = in.readLine();
                    }
                    if (mpline != null) {
                        String contentDisposition = item.getProperty("content-disposition");
                        if (contentDisposition == null) {
                            sendError(HTTP_BADREQUEST, "BAD REQUEST: Content type is multipart/form-data but no content-disposition info found. Usage: GET /example/file.html");
                        }
                        StringTokenizer st = new StringTokenizer(contentDisposition, ";");
                        Properties disposition = new Properties();
                        while (st.hasMoreTokens()) {
                            String token = st.nextToken().trim();
                            int p = token.indexOf('=');
                            if (p != -1)
                                disposition.put(token.substring(0, p).trim().toLowerCase(), token.substring(p + 1).trim());
                        }
                        String pname = disposition.getProperty("name");
                        pname = pname.substring(1, pname.length() - 1);

                        String value = "";
                        if (item.getProperty("content-type") == null) {
                            while (mpline != null && mpline.indexOf(boundary) == -1) {
                                mpline = in.readLine();
                                if (mpline != null) {
                                    int d = mpline.indexOf(boundary);
                                    if (d == -1)
                                        value += mpline;
                                    else
                                        value += mpline.substring(0, d - 2);
                                }
                            }
                        } else {
                            if (boundarycount > bpositions.length)
                                sendError(HTTP_INTERNALERROR, "Error processing request");
                            int offset = stripMultipartHeaders(fbuf, bpositions[boundarycount - 2]);
                            String path = saveTmpFile(fbuf, offset, bpositions[boundarycount - 1] - offset - 4);
                            files.put(pname, path);
                            value = disposition.getProperty("filename");
                            value = value.substring(1, value.length() - 1);
                            do {
                                mpline = in.readLine();
                            } while (mpline != null && mpline.indexOf(boundary) == -1);
                        
                            
                            MoveFileToUpload(path, value, uploadPath);
                        }
                        parms.put(pname, value);
                    }
                }
            } catch (IOException ioe) {
                sendError(HTTP_INTERNALERROR, "SERVER INTERNAL ERROR: IOException: " + ioe.getMessage());
            }
        }

        
        @SuppressWarnings("deprecation")
		public int[] getBoundaryPositions(byte[] b, byte[] boundary) {
            int matchcount = 0;
            int matchbyte = -1;
            Vector<Integer> matchbytes = new Vector<Integer>();
            for (int i = 0; i < b.length; i++) {
                if (b[i] == boundary[matchcount]) {
                    if (matchcount == 0)
                        matchbyte = i;
                    matchcount++;
                    if (matchcount == boundary.length) {
                        matchbytes.addElement(new Integer(matchbyte));
                        matchcount = 0;
                        matchbyte = -1;
                    }
                } else {
                    i -= matchcount;
                    matchcount = 0;
                    matchbyte = -1;
                }
            }
            int[] ret = new int[matchbytes.size()];
            for (int i = 0; i < ret.length; i++) {
                ret[i] = ((Integer) matchbytes.elementAt(i)).intValue();
            }
            return ret;
        }

       
        private String saveTmpFile(byte[] b, int offset, int len) {
            String path = "";
            if (len > 0) {
                String tmpdir = System.getProperty("java.io.tmpdir");
                try {
                    File temp = File.createTempFile("NanoHTTPd", "", new File(tmpdir));
                    OutputStream fstream = new FileOutputStream(temp);
                    fstream.write(b, offset, len);
                    fstream.close();
                    path = temp.getAbsolutePath();
                } catch (Exception e) { // Catch exception if any
                    System.err.println("Error: " + e.getMessage());
                }
            }
            return path;
        }


        
        private int stripMultipartHeaders(byte[] b, int offset) {
            int i = 0;
            for (i = offset; i < b.length; i++) {
                if (b[i] == '\r' && b[++i] == '\n' && b[++i] == '\r' && b[++i] == '\n')
                    break;
            }
            return i + 1;
        }

        
        private String decodePercent(String str) throws InterruptedException {
            try {
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < str.length(); i++) {
                    char c = str.charAt(i);
                    switch (c) {
                        case '+':
                            sb.append(' ');
                            break;
                        case '%':
                            sb.append((char) Integer.parseInt(str.substring(i + 1, i + 3), 16));
                            i += 2;
                            break;
                        default:
                            sb.append(c);
                            break;
                    }
                }
                return sb.toString();
            } catch (Exception e) {
                sendError(HTTP_BADREQUEST, "BAD REQUEST: Bad percent-encoding.");
                return null;
            }
        }

        
        private void decodeParms(String parms, Properties p)
                throws InterruptedException {
            if (parms == null)
                return;

            StringTokenizer st = new StringTokenizer(parms, "&");
            while (st.hasMoreTokens()) {
                String e = st.nextToken();
                int sep = e.indexOf('=');
                p.put(
                        decodePercent((sep >= 0) ? e.substring(0, sep) : e).trim(),
                        (sep >= 0) ? decodePercent(e.substring(sep + 1)) : ""
                );
            }
        }

        
        private void sendError(String status, String msg) throws InterruptedException {
            sendResponse(status, MIME_PLAINTEXT, null, new ByteArrayInputStream(msg.getBytes()));
            throw new InterruptedException();
        }

        
        private void sendResponse(String status, String mime, Properties header, InputStream data) {
            try {
                if (status == null)
                    throw new Error("sendResponse(): Status can't be null.");

                OutputStream out = mySocket.getOutputStream();
                PrintWriter pw = new PrintWriter(out);
                pw.print("HTTP/1.0 " + status + " \r\n");

                if (mime != null)
                    pw.print("Content-Type: " + mime + "\r\n");

                if (header == null || header.getProperty("Date") == null)
                    pw.print("Date: " + gmtFrmt.format(new Date()) + "\r\n");

                if (header != null) {
                    Enumeration<?> e = header.keys();
                    while (e.hasMoreElements()) {
                        String key = (String) e.nextElement();
                        String value = header.getProperty(key);
                        pw.print(key + ": " + value + "\r\n");
                    }
                }

                pw.print("\r\n");
                pw.flush();

                if (data != null) {
                    byte[] buff = new byte[2048];
                    while (true) {
                        int read = data.read(buff, 0, 2048);
                        if (read <= 0)
                            break;
                        out.write(buff, 0, read);
                    }
                }
                out.flush();
                out.close();
                if (data != null)
                    data.close();
            } catch (IOException ioe) {
                // Couldn't write? No can do.
                try {
                    mySocket.close();
                } catch (Throwable t) {
                }
            }
        }

        private Socket mySocket;
    }

    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    @SuppressWarnings({ "deprecation", "unused" })
    
	private String encodeUri(String uri) {
        String newUri = "";
        StringTokenizer st = new StringTokenizer(uri, "/ ", true);
        while (st.hasMoreTokens()) {
            String tok = st.nextToken();
            if (tok.equals("/"))
                newUri += "/";
            else if (tok.equals(" "))
                newUri += "%20";
            else {
                newUri += URLEncoder.encode(tok);
                // For Java 1.4 you'll want to use this instead:
                // try { newUri += URLEncoder.encode( tok, "UTF-8" ); } catch ( java.io.UnsupportedEncodingException uee ) {}
            }
        }
        return newUri;
    }

    private int myTcpPort;
    private final ServerSocket myServerSocket;
    private Thread myThread;

    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    

    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
  
    private static Hashtable<String, String> theMimeTypes = new Hashtable<String, String>();

    static {
        StringTokenizer st = new StringTokenizer(
                		"css		text/css " +
                        "js			text/javascript " +
                        "htm		text/html " +
                        "html		text/html " +
                        "txt		text/plain " +
                        "asc		text/plain " +
                        "gif		image/gif " +
                        "jpg		image/jpeg " +
                        "jpeg		image/jpeg " +
                        "png		image/png " +
                        "mp3		audio/mpeg " +
                        "m3u		audio/mpeg-url " +
                        "pdf		application/pdf " +
                        "doc		application/msword " +
                        "ogg		application/x-ogg " +
                        "zip		application/octet-stream " +
                        "exe		application/octet-stream " +
                        "class		application/octet-stream " +
                        "mp4		video/mp4"
                        
        		
        		);
        
        while (st.hasMoreTokens())
            theMimeTypes.put(st.nextToken(), st.nextToken());
    }

  
    private static java.text.SimpleDateFormat gmtFrmt;

    static {
        gmtFrmt = new java.text.SimpleDateFormat("E, d MMM yyyy HH:mm:ss 'GMT'", Locale.US);
        gmtFrmt.setTimeZone(TimeZone.getTimeZone("GMT"));
    }


}
