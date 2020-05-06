package astar;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;



/**
 * The Main Class. The main class of the Astar program.
 *
 * @author Sertac Gorkem
 * @version 1.0.1
 * @since 2019-07-15
 */

public class Main {

	public static void deleteIfExists(File file)
	    	throws IOException{
	 
	    	if(file.isDirectory()){
	
	    		if(file.list().length==0){
	    			
	    		   file.delete();
	    			
	    		}else{
	    			
	        	   String files[] = file.list();
	     
	        	   for (String temp : files) {
	        	      File fileDelete = new File(file, temp);

	        	     deleteIfExists(fileDelete);
	        	   }
	        		
	        	   if(file.list().length==0){
	           	     file.delete();
	        	   }
	    		}
	    		
	    	}else{
	    		file.delete();
	    	}
	    }
	
	/**
	 * Starts application, generates path, or reads pre-existing files, or uses the example
	 * 
	 * @param args String[]
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		//
		Scanner sc = new Scanner(System.in);
		System.out.println("To generate new paths press 0, to read a path press 1 to use example press 2");
		String response = sc.nextLine();
		boolean proceedToAlgo = false;
		Map mapArr[] = new Map[50];
		String datePath = "";
		
		
		if(response.equals("0")) {
			
			generateMaps();
		}
		else if(response.equals("1")){
			System.out.println("Please enter path's directory");
			String pathDirectory = sc.nextLine();
			
			if(pathDirectory.contains("paths")) {
				String date = pathDirectory.substring(pathDirectory.indexOf("paths/"));
				datePath = date;
			}
			
			File _file = new File(pathDirectory);
			proceedToAlgo = true;
			if(_file.isDirectory() != true) {
				System.out.println("Not a directory exiting");
				proceedToAlgo = false;
			}
			else {
				//String[] files = _file.list();
				mapArr = readFiles(_file.list(), pathDirectory);
				//loops the files
//				for (String fileName : files)
//				{
//					if(!fileName.contains(".txt")) {
//						continue;
//					}
//					String fp = pathDirectory + "/" + fileName;
//					Map mp = new Map(false);
//					mp.fileName = fp;
//					
//					try {
//						File readfile = new File(fp); 
//						Scanner fsc = new Scanner(readfile); 
//						  
//						int row = 0;
//					    while (fsc.hasNextLine()) {
//					    	String line = fsc.nextLine();
//					    	mp.readString(line, row);
//					    	row++;
//					    }
//					    fsc.close();
//					    
//					    String subber = fileName.substring(0, fileName.indexOf('.'));
//					    int index = Integer.parseInt(subber);
//					    mapArr[index] = mp;
//					}catch(Exception e) {
//						System.out.println("Error occured while reading the file");
//					}
//				}
			}
		}else if(response.equals("2")) {
			Map mp = new Map(true);
			mapArr[0] = mp;
			proceedToAlgo = true;
		}

		if(proceedToAlgo == true) {
			doAstar(mapArr, datePath);
			//forward astar
			
//			if(response.equals("2")) {
//
//				
//				mapArr[0].beginAdaptive();
//				mapArr[0].print();
//			}
//			else {
//				
//				String str = datePath + "/forwardPath";
//				String str2 = datePath + "/AlternatePlans";
//				String str3 = datePath + "/backwardsPlans";
//				String str6 = datePath + "/backwardAlternatives";
//				String str4 = datePath + "/adaptivePath";
//				String str5 = datePath + "/AdaptiveAlternatives";
//
//				
//				File forward =  new File(str);
//				File alternate = new File(str2);
//				File backward = new File(str3);
//				File backwardAlternate = new File(str6);
//				File adaptive = new File(str4);
//				File adaptiveAlternate = new File(str5);
//
//				//check if results exist for paths
//				try {
//					deleteIfExists(forward);
//					deleteIfExists(alternate);
//					deleteIfExists(backward);
//					deleteIfExists(backwardAlternate);
//					deleteIfExists(adaptive);
//					deleteIfExists(adaptiveAlternate);
//
//					
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				//create dirs
//				forward.mkdirs();
//				alternate.mkdirs();
//				backward.mkdirs();
//				backwardAlternate.mkdirs();
//				adaptive.mkdirs();
//				adaptiveAlternate.mkdirs();
//				
//				ArrayList<String>resultList = new ArrayList<String>();
//				
//				long avgForward = 0;
//				long avgBack = 0;
//				long avgAdaptive = 0;
//				
//				long nc1 = 0;
//				long nc2 = 0;
//				long nc3 = 0;
//				
//				int count = 0;
//				
//				for(int i = 0; i< mapArr.length; i++) {
//					if(mapArr[i] != null) {
//						count++;
//						
//						String strresult =  "forward " + i + " ";
//						
//						//forward
//						long startTime = System.currentTimeMillis();
//						boolean result1 = mapArr[i].begin();
//						long endTime = System.currentTimeMillis();
//						String fp = str + "/" + i + ".txt";
//						mapArr[i].printSolution(fp);
//						
//						String printingPath = str2 + "/" + i + "/";
//						new File(printingPath).mkdirs();
//						mapArr[i].printPaths(printingPath);
//						long res = endTime - startTime;
//						avgForward += res;
//						strresult += result1 + " " + res + "ms " + mapArr[i].nodeCount + "nodes";
//						nc1 += mapArr[i].nodeCount;
//						
//						
//						//backward
//						String backResult = "backward " + i + " ";
//						mapArr[i].resetArr();
//						
//						long startTime2 = System.currentTimeMillis();
//						boolean result2 = mapArr[i].beginBackward();
//						long endTime2 = System.currentTimeMillis();
//						
//						String fp3 = str3 + "/" + i + ".txt";
//						mapArr[i].printSolution(fp3);
//						
//						
//						
//						String printingPath3 = str6 + "/" + i +"/";
//						new File(printingPath3).mkdirs();
//						mapArr[i].printPaths(printingPath3);
//						
//						res = endTime2 - startTime2;
//						backResult += result2 + " " + res + "ms " + mapArr[i].nodeCount + "nodes";
//						avgBack+= res;
//						nc2 += mapArr[i].nodeCount;
//						
//						//adaptive
//						String adaptiveResult = "adaptive " + i + " ";
//						mapArr[i].resetArr();
//						
//						long startTime3 = System.currentTimeMillis();
//						boolean result3 = mapArr[i].beginAdaptive();
//						long endTime3 = System.currentTimeMillis();
//						
//						String fp4 = str4 + "/" + i + ".txt";
//						mapArr[i].printSolution(fp4);
//						
//						String adaptiveAlternatePath = str5 + "/" + i + "/";
//						new File(adaptiveAlternatePath).mkdirs();
//						mapArr[i].printPaths(adaptiveAlternatePath);
//						
//						res = endTime3 - startTime3;
//						adaptiveResult += result3 + " " + res + "ms " + mapArr[i].nodeCount + "nodes";
//						
//						avgAdaptive += res;
//						nc3 += mapArr[i].nodeCount;
//						
//						resultList.add(strresult);
//						resultList.add(backResult);
//						resultList.add(adaptiveResult);
//						
//					}
//				}
//				try {
//				String averageForwardTime = "Average forward runtime: " + avgForward/count;
//				String averageBackawardTime = "Average backward runtime: " + avgBack/count;
//				String averageAdaptiveTime = "Average adaptive runtime: " + avgAdaptive/count;
//				
//				
//				String averageFrontNode = "Average forward node count: " + nc1/count;
//				String averageBackwardNode = "Average back node count: " + nc2/count;
//				String averageAdaptiveNode = "Average adaptive node count: " + nc3/count;
//				
//				resultList.add(averageForwardTime);
//				resultList.add(averageBackawardTime);
//				resultList.add(averageAdaptiveTime);
//				
//				resultList.add(averageFrontNode);
//				resultList.add(averageBackwardNode);
//				resultList.add(averageAdaptiveNode);
//				}
//				catch (Exception e){
//					
//				}
//				String resultPath = datePath + "/results.txt";
//				try {
//					FileWriter writer = new FileWriter(resultPath, true);
//					for(int i = 0; i< resultList.size(); i++) {
//						writer.write(resultList.get(i));
//						writer.write("\n");
//					}
//					writer.close();
//				}
//				catch(Exception e) {
//					
//				}
//				
//			}
		}
		sc.close();

	}

	
	private static void generateMaps() {
		Date date = Calendar.getInstance().getTime();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
		String strDate = dateFormat.format(date);
		strDate = "paths/" + strDate;
		new File(strDate).mkdirs();
		for(int i = 0; i< 50; i++) {
			Map pathMap = new Map(false);
			String fileString = strDate + "/" + i + ".txt";
			pathMap.generate(fileString);
			
		}
		System.out.println("Arrays generated");
	}
	
	private static Map[] readFiles(String []files, String pathDirectory) {
		Map mapArr[] = new Map[50];
		for (String fileName : files)
		{
			if(!fileName.contains(".txt")) {
				continue;
			}
			String fp = pathDirectory + "/" + fileName;
			Map mp = new Map(false);
			mp.fileName = fp;
			
			try {
				File readfile = new File(fp); 
				Scanner fsc = new Scanner(readfile); 
				  
				int row = 0;
			    while (fsc.hasNextLine()) {
			    	String line = fsc.nextLine();
			    	mp.readString(line, row);
			    	row++;
			    }
			    fsc.close();
			    
			    String subber = fileName.substring(0, fileName.indexOf('.'));
			    int index = Integer.parseInt(subber);
			    mapArr[index] = mp;
			}catch(Exception e) {
				System.out.println("Error occured while reading the file");
			}
		}
		return mapArr;
	}
	
	private static void doAstar(Map[] mapArr, String datePath) {
		if(mapArr[0] != null) {
			if(mapArr[0].isExample == true) {
			
			mapArr[0].beginAdaptive();
			mapArr[0].print();
			}
		}
		else {
			
			String str = datePath + "/forwardPath";
			String str2 = datePath + "/AlternatePlans";
			String str3 = datePath + "/backwardsPlans";
			String str6 = datePath + "/backwardAlternatives";
			String str4 = datePath + "/adaptivePath";
			String str5 = datePath + "/AdaptiveAlternatives";

			
			File forward =  new File(str);
			File alternate = new File(str2);
			File backward = new File(str3);
			File backwardAlternate = new File(str6);
			File adaptive = new File(str4);
			File adaptiveAlternate = new File(str5);

			//check if results exist for paths
			try {
				deleteIfExists(forward);
				deleteIfExists(alternate);
				deleteIfExists(backward);
				deleteIfExists(backwardAlternate);
				deleteIfExists(adaptive);
				deleteIfExists(adaptiveAlternate);

				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//create dirs
			forward.mkdirs();
			alternate.mkdirs();
			backward.mkdirs();
			backwardAlternate.mkdirs();
			adaptive.mkdirs();
			adaptiveAlternate.mkdirs();
			
			ArrayList<String>resultList = new ArrayList<String>();
			
			long avgForward = 0;
			long avgBack = 0;
			long avgAdaptive = 0;
			
			long nc1 = 0;
			long nc2 = 0;
			long nc3 = 0;
			
			int count = 0;
			
			for(int i = 0; i< mapArr.length; i++) {
				if(mapArr[i] != null) {
					count++;
					
					String strresult =  "forward " + i + " ";
					
					//forward
					long startTime = System.currentTimeMillis();
					boolean result1 = mapArr[i].beginForward();
					long endTime = System.currentTimeMillis();
					String fp = str + "/" + i + ".txt";
					mapArr[i].printSolution(fp);
					
					String printingPath = str2 + "/" + i + "/";
					new File(printingPath).mkdirs();
					mapArr[i].printPaths(printingPath);
					long res = endTime - startTime;
					avgForward += res;
					strresult += result1 + " " + res + "ms " + mapArr[i].nodeCount + "nodes";
					nc1 += mapArr[i].nodeCount;
					
					
					//backward
					String backResult = "backward " + i + " ";
					mapArr[i].resetArr();
					
					long startTime2 = System.currentTimeMillis();
					boolean result2 = mapArr[i].beginBackward();
					long endTime2 = System.currentTimeMillis();
					
					String fp3 = str3 + "/" + i + ".txt";
					mapArr[i].printSolution(fp3);
					
					
					
					String printingPath3 = str6 + "/" + i +"/";
					new File(printingPath3).mkdirs();
					mapArr[i].printPaths(printingPath3);
					
					res = endTime2 - startTime2;
					backResult += result2 + " " + res + "ms " + mapArr[i].nodeCount + "nodes";
					avgBack+= res;
					nc2 += mapArr[i].nodeCount;
					
					//adaptive
					String adaptiveResult = "adaptive " + i + " ";
					mapArr[i].resetArr();
					
					long startTime3 = System.currentTimeMillis();
					boolean result3 = mapArr[i].beginAdaptive();
					long endTime3 = System.currentTimeMillis();
					
					String fp4 = str4 + "/" + i + ".txt";
					mapArr[i].printSolution(fp4);
					
					String adaptiveAlternatePath = str5 + "/" + i + "/";
					new File(adaptiveAlternatePath).mkdirs();
					mapArr[i].printPaths(adaptiveAlternatePath);
					
					res = endTime3 - startTime3;
					adaptiveResult += result3 + " " + res + "ms " + mapArr[i].nodeCount + "nodes";
					
					avgAdaptive += res;
					nc3 += mapArr[i].nodeCount;
					
					resultList.add(strresult);
					resultList.add(backResult);
					resultList.add(adaptiveResult);
					
				}
			}
			try {
			String averageForwardTime = "Average forward runtime: " + avgForward/count;
			String averageBackawardTime = "Average backward runtime: " + avgBack/count;
			String averageAdaptiveTime = "Average adaptive runtime: " + avgAdaptive/count;
			
			
			String averageFrontNode = "Average forward node count: " + nc1/count;
			String averageBackwardNode = "Average back node count: " + nc2/count;
			String averageAdaptiveNode = "Average adaptive node count: " + nc3/count;
			
			resultList.add(averageForwardTime);
			resultList.add(averageBackawardTime);
			resultList.add(averageAdaptiveTime);
			
			resultList.add(averageFrontNode);
			resultList.add(averageBackwardNode);
			resultList.add(averageAdaptiveNode);
			}
			catch (Exception e){
				
			}
			String resultPath = datePath + "/results.txt";
			try {
				FileWriter writer = new FileWriter(resultPath, true);
				for(int i = 0; i< resultList.size(); i++) {
					writer.write(resultList.get(i));
					writer.write("\n");
				}
				writer.close();
			}
			catch(Exception e) {
				System.out.println(e.getMessage());
			}
			
		}
	}
}
