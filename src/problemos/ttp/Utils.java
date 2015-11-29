package problemos.ttp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Utils {
	
	// function returns a linkern tour for a given TTP instance
	public static int[] linkernTour(TTPInstance instance) {
        int[] result = new int[instance.numberOfNodes+1];
        
        boolean debugPrint = false;

        String temp = instance.file.getName();	// look only at the file name, ignore path details
        int index = temp.indexOf("_");
        String tspfilename = instance.file.getPath(); 
        if (index==-1) index = tspfilename.indexOf(".");
        
        String tspresultfilename = //System.getProperty("user.dir") + "/"
        		"resources/ttp/tspTours/" 
        		+ temp.substring(0,index)+".linkern.tour";
        if (debugPrint) System.out.println("LINKERN: "+tspfilename);
    
        File tspresultfile = new File(tspresultfilename);
        
        if (debugPrint) System.out.println(tspresultfile.getAbsolutePath()+" exists="+tspresultfile.exists());
        
        try {
            if (!tspresultfile.exists()) {
                List<String> command = new ArrayList<String>();
                command.add("./resources/ttp/tspTours/linkern");
                command.add("-o");
                command.add(tspresultfilename);
                command.add(tspfilename);
//                Utils.printListOfStrings(command);
                
                ProcessBuilder builder = new ProcessBuilder(command);
                builder.redirectErrorStream(true);
                final Process process = builder.start();
                InputStream is = process.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                String line;
                while ((line = br.readLine()) != null) {
                    if (debugPrint) System.out.println("<LINKERN> "+line);
                }
                
                if (debugPrint) System.out.println("Program terminated?");    
                int rc = process.waitFor();
                if (debugPrint) System.out.println("Program terminated!");
            } else {
            	if (debugPrint) System.out.println("a linkern tour already exists");
            }
            
            BufferedReader br = new BufferedReader( new FileReader(tspresultfilename));
            // discard the first line
            br.readLine();
            String line = null; 
            for (int i=0; i<result.length-1; i++) {
                line = br.readLine();
                if (debugPrint) System.out.println("<TOUR> "+line);
                index = line.indexOf(" ");
                int number = Integer.parseInt(line.split("\\s+")[0]);
                result[i] = number; 
            }
            
            if (debugPrint) System.out.println(Arrays.toString(result));
          
            
            } catch (Exception ex) {
            	ex.printStackTrace();
            }
        return result;
    }
	
	public static void printListOfStrings(List<String> list) {
        String result = "";
        for (String s:list)
            result+=s+" ";
        System.out.println(result);
    }
}
