package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.util.ArrayList;

import com.samskivert.mustache.Mustache;







@SuppressWarnings("unused")
public class Utility {

	public static String current = new File("").getAbsolutePath();
	
	public static String ServeDirectory(String path) {
		
		
		
		String password = Values.password ;
		
		class ListItem{


            
			String onclick , icon , name , date , size ;

            public ListItem(String onclick, String icon, String name, long date, long size) {

                this.onclick = onclick;
                this.icon = icon;
                this.name = name;
                this.date = Utility.getDate(date);
                this.size = Utility.getSize(size);
            }
            public ListItem(String onclick, String icon, String name, long date, String size) {

                this.onclick = onclick;
                this.icon = icon;
                this.name = name;
                this.date = Utility.getDate(date);
                this.size = size ;
            }
        };

        

        String
                FileIconPath =  "/assets/icons/file.png" ,
                FolderIconPath = "/assets/icons/folder.png" ,
                DirectoryPageText ;

        try {

            InputStream is = new FileInputStream("assets/html/directorypage.html");
            byte[] data = new byte[is.available()] ;
            is.read(data) ;
            is.close();

            DirectoryPageText = new String(data) ;
            //            Toast.makeText(context,DirectoryPageText,Toast.LENGTH_LONG).show();

            final File file = new File(path) ;

            String text = new String(DirectoryPageText) , parent_dir="#";

            try {
                parent_dir = file.getParent();
                if(parent_dir == null)parent_dir = "#" ;
            }catch(Exception e) {parent_dir="#";}



            ArrayList<ListItem> itemList = new ArrayList<ListItem>();


            String[] childs = file.list() ;
            if(childs == null)childs = new String[]{} ;

            long size = 0;

            for(String child : childs) {

                File f = new File(file,child) ;


                if(f.isDirectory()) {

                    String[] temp = f.list() ;
                    String tstr ;
                    if(temp == null )tstr = " ";
                    else if(temp.length <= 1)tstr = temp.length + " file" ;
                    else tstr = temp.length + " files" ;


//                    System.out.println(URLEncoder.encode("/"+password + "/" + f.getAbsolutePath(),"utf-8").replace("%2F","/"));

                    itemList.add(
                            new ListItem(
                            		URLEncoder.encode("/" + password + "/files/" + f.getAbsolutePath(),"utf-8").replace("%2F","/").replace("%5C", "\\"), 
                            		"/" + password + FolderIconPath, child, f.lastModified(), tstr)
                    );

                }else {

                    String ext = child.substring(child.lastIndexOf(".")+1);
                    ext = ext.toLowerCase();

                    try{
                        
                    	File tf = new File(current + "/assets/icons/" + ext+".png");
                    	if(!tf.exists())throw new Exception();
                    	
                        itemList.add(
                                new ListItem(URLEncoder.encode("/" + password + "/files/" +f.getAbsolutePath(),"utf-8").replace("%2F","/").replace("%5C", "\\"), "/" + password + "/assets/icons/"+ext+".png", child, f.lastModified(), f.length())
                        );

                    }catch (Exception e){
                    	
                    	System.out.println("Not Found : " + ext +".png");
                    	
                        itemList.add(
                                new ListItem(URLEncoder.encode("/"+password + "/files/" +f.getAbsolutePath(),"utf-8").replace("%2F","/").replace("%5C", "\\"), "/" + password + FileIconPath, child, f.lastModified(), f.length())
                        );
                    }


                    size += f.length() ;
                }

            }


            

            final String pString = URLEncoder.encode("/" + password + "/files/" + parent_dir,"utf-8").replace("%2F","/").replace("%5C", "\\");
            final String zString = URLEncoder.encode("/"+password+"/zip/"+file.getAbsolutePath(),"utf-8").replace("%2F","/").replace("%5C", "\\");
            final ArrayList<ListItem> temp_item_list = new ArrayList<ListItem>(itemList) ;


            final long size2 = size ;


            
            

            text = Mustache.compiler().compile(text).execute(new Object() {
                String parent = pString ;
                String zip =  zString;
                String filesize = getSize(size2);
                String location = file.getAbsolutePath() ;
                ArrayList<ListItem>list_item = temp_item_list ;

            });

            //			return"";
            return text ;
        }catch(Exception e) {e.printStackTrace();}

		
		
		return "";
	}
	
	
	public static String ServeDirectory(ArrayList<String> path) {
		
		String password = Values.password ;
		
		class ListItem{


            String onclick , icon , name , date , size ;

            public ListItem(String onclick, String icon, String name, long date, long size) {

                this.onclick = onclick;
                this.icon = icon;
                this.name = name;
                this.date = Utility.getDate(date);
                this.size = Utility.getSize(size);
            }
            public ListItem(String onclick, String icon, String name, long date, String size) {

                this.onclick = onclick;
                this.icon = icon;
                this.name = name;
                this.date = Utility.getDate(date);
                this.size = size ;
            }
        };

        

        String
                FileIconPath =  "/assets/icons/file.png" ,
                FolderIconPath = "/assets/icons/folder.png" ,
                DirectoryPageText ;

        try {

            InputStream is = new FileInputStream("assets/html/directorypage.html");
            byte[] data = new byte[is.available()] ;
            is.read(data) ;
            is.close();

            DirectoryPageText = new String(data) ;
            //            Toast.makeText(context,DirectoryPageText,Toast.LENGTH_LONG).show();

            

            String text = new String(DirectoryPageText) , parent_dir="#";



            ArrayList<ListItem> itemList = new ArrayList<ListItem>();


            
            

            long size = 0;

            for(String child : path) {
            	

                File f = new File(child) ;


                if(f.isDirectory()) {

                    String[] temp = f.list() ;
                    String tstr ;
                    if(temp == null )tstr = " ";
                    else if(temp.length <= 1)tstr = temp.length + " file" ;
                    else tstr = temp.length + " files" ;


//                    System.out.println(URLEncoder.encode("/"+password + "/" + f.getAbsolutePath(),"utf-8").replace("%2F","/"));

                    itemList.add(
                            new ListItem(
                            		URLEncoder.encode("/" + password + "/files/" + f.getAbsolutePath(),"utf-8").replace("%2F","/").replace("%5C", "\\"), 
                            		"/" + password + FolderIconPath, child, f.lastModified(), tstr)
                    );

                }else {

                    String ext = child.substring(child.lastIndexOf(".")+1);
                    ext = ext.toLowerCase();

                    try{
                        
                    	File tf = new File(current + "/assets/icons/" + ext+".png");
                    	if(!tf.exists())throw new Exception();
                    	
                        itemList.add(
                                new ListItem(URLEncoder.encode("/" + password + "/files/" + f.getAbsolutePath(),"utf-8").replace("%2F","/").replace("%5C", "\\"), "/" + password + "/assets/icons/"+ext+".png", child, f.lastModified(), f.length())
                        );

                    }catch (Exception e){
                    	
                    	System.out.println("Not Found : " + ext +".png");
                    	
                        itemList.add(
                                new ListItem(URLEncoder.encode("/" + password + "/files/" +f.getAbsolutePath(),"utf-8").replace("%2F","/").replace("%5C", "\\"), "/" + password + FileIconPath, child, f.lastModified(), f.length())
                        );
                    }


                    size += f.length() ;
                }

            }


            

            final String pString = URLEncoder.encode("/" + password + "/files/" + parent_dir,"utf-8").replace("%2F","/").replace("%5C", "\\");
            final String zString = "#";
            final ArrayList<ListItem> temp_item_list = new ArrayList<ListItem>(itemList) ;


            final long size2 = size ;


            
            

            text = Mustache.compiler().compile(text).execute(new Object() {
                String parent = pString ;
                String zip =  zString;
                String filesize = getSize(size2);
                String location = "Shared Files & Folders will appear here...";
                ArrayList<ListItem>list_item = temp_item_list ;

            });

            //			return"";
            return text ;
        }catch(Exception e) {e.printStackTrace();}

		
		
		return "";
	}
	
		
	
	
	
	
	
	

    public static String getSize(long size) {
        String subs = " " ;

        if(size<1024)subs += size + " Bytes" ;
        else if(size<1048576)subs += (size/1024) + "." + ((size%1024)*10)/1024 + " KB" ;
        else if(size<1073741824)subs += (size/1048576) + "." + ((size%1048576)*10)/1048576 + " MB" ;
        else subs += (size/1073741824) + "." + ((size%1073741824)*10)/1073741824 + " GB" ;

        return subs ;
    }
    public static String getDate(long lastModified) {
        return DateFormat.getDateTimeInstance(
                DateFormat.DEFAULT , DateFormat.DEFAULT).format(lastModified) ;
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
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    

    
    
    
}
