package gui;

import java.awt.Dimension;

import javax.swing.JFileChooser;
import javax.swing.JPanel;

import main.Values;


public class DirectoryChooserUtil extends JPanel{
	
	
	
	
   /**
	 * 
	 */
	private static final long serialVersionUID = 1217832986269686605L;

   
   JFileChooser chooser;
   String choosertitle;
   
  public DirectoryChooserUtil() {
	  
      
	    JFileChooser chooser = new JFileChooser(); 
	    chooser.setCurrentDirectory(new java.io.File("."));
	    chooser.setDialogTitle(choosertitle);
	    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	    //
	    // disable the "All files" option.
	    //
	    chooser.setAcceptAllFileFilterUsed(false);
	    //    
	    if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) { 
	      System.out.println("getCurrentDirectory(): " 
	         +  chooser.getCurrentDirectory());
	      System.out.println("getSelectedFile() : " 
	         +  chooser.getSelectedFile());
	      
	      
	      Values.addFile(chooser.getSelectedFile().getAbsolutePath());
	      }
	    
	    
	    
	    else {
	      System.out.println("No Selection ");
	      }
}

  
    
   
  public Dimension getPreferredSize(){
    return new Dimension(200, 200);
    }
    
  public static void main(String s[]) {
    
    }
}