/* This code is not written by the original author(s).
 *
 *
 */

package omrproj;

import net.sourceforge.jiu.codecs.*;
import net.sourceforge.jiu.data.*;
import net.sourceforge.jiu.color.reduction.*;
import net.sourceforge.jiu.filters.*;

import java.io.*;

public class BatchProcess
{
	public static void main(String[] args)
	{
		System.out.println("This program's arguments are as follows:");
		System.out.println("<template name> <form filename 0> [form 1] [form n]");
		System.out.println("Template name must NOT have an extension.");
		System.out.println("You will be expected to add your own schema line to the csv.");
		
		System.out.printf("\nProcessing %d forms this session.", args.length - 1);
		
		String templateName = args[0];
		String [] csvAppend = new String[args.length];
		ImageManipulation imageManip;
		Gray8Image grayImage;
		
		for(int i=1;i<args.length;i++)
		{
			System.out.printf("\nAttempting file number %d (%s).", i, args[i]);
			grayImage = ImageUtil.readImage(args[i]);
			imageManip = new ImageManipulation(grayImage);
			
			imageManip.locateConcentricCircles();
			imageManip.readConfig(templateName + ".config");
			imageManip.readFields(templateName + ".fields");
			imageManip.readAscTemplate(templateName + ".asc");
			imageManip.searchMarks();
			
			csvAppend[i] = imageManip.generateCSV();
		}
		
		System.out.println("\nAttempting to write file.");
		try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(templateName + ".csv", true))))
		{
			for(int i=1;i<args.length;i++)
			{
				out.println(csvAppend[i]);
			}
		} catch (IOException e) {
			//e.printStackTrace();
			System.out.println("Failed to open file.");
			System.exit(1);
		}
	}
}