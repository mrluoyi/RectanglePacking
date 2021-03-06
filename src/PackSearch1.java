import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class PackSearch1 {
	final int MAX_NUM_OF_RECTANGLE = 100;
	int maxArea = 0;
	int numOfRec = 0;
	int minArea = 0;
	ArrayList<Record> recordList;
	
	Rectangle recList[] = null;
	int checkList[] = null;
	int searchTime = 0;
	
	public Rectangle[] readRectangles(String fileName) {
		BufferedReader br = null;
		Rectangle rList[] = null;
		String line;
		String split[];

		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(
					fileName)));
			line = br.readLine();
			numOfRec = Integer.parseInt(line);
			rList = new Rectangle[MAX_NUM_OF_RECTANGLE];

			for (int i = 0; i < numOfRec; i++) {
				line = br.readLine();
				split = line.split(" ");
				rList[i] = new Rectangle(Integer.parseInt(split[0]),
						Integer.parseInt(split[1]));
			}
			br.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rList;
	}
	
	void init(String fileName){
		recList = readRectangles(fileName);

		int totalH = 0, totalW = 0;
		for (int i=0; i<numOfRec; i++){
			totalH += recList[i].getHeight();
			totalW += recList[i].getWidth();
		}
		maxArea = totalH * totalW;
		//System.out.println(maxArea);
		minArea = maxArea;
		
		checkList = new int[MAX_NUM_OF_RECTANGLE];
		for (int i = 0; i < numOfRec; i++) {
			checkList[i] = 1;
		}
	}

	PackSearch1(String fileName) {
		init(fileName);
	}
	
	PackSearch1() {
		String fileName = "data/test.txt";
		init(fileName);
	}

	public void searchForMinArea(int h, int w, int finalArea, ArrayList<Record> list) {
		int a1, h1, w1, a2, h2, w2;
		int tempResult[];
		int flag = 0;
		
		for (int i = 0; i < numOfRec; i++) {
			if (checkList[i] == 1) {
				checkList[i] = 0;
				flag = 1;
				
				int crtH = recList[i].getHeight();
				int crtW = recList[i].getWidth();

				list.add(new Record(i, 1));
				tempResult = getIncreasedArea(crtH, crtW, h, w, 1); // up = 1
				a1 = finalArea + tempResult[2];
				h1 = tempResult[0];
				w1 = tempResult[1];
				if (a1 < minArea){
					searchForMinArea(h1, w1, a1, list);
				}

				list.remove(list.size()-1);
				list.add(new Record(i, 0));
				tempResult = getIncreasedArea(crtH, crtW, h, w, 0); // right = 0
				a2 = finalArea + tempResult[2];
				h2 = tempResult[0];
				w2 = tempResult[1];
				if (a2 < minArea){
					searchForMinArea(h2, w2, a2, list);
				}
				
				checkList[i] = 1;
				list.remove(list.size()-1);
			}
		}

		if (flag == 0){ // all rectangles have been checked
			if (finalArea < minArea){
				minArea = finalArea;
				//recordList = list;		
				display(list);
				System.out.println("Area = " + finalArea + ", which is better.");
			}
			searchTime ++;
			//display(list);
			//System.out.println("Area = " + finalArea);
		}
	}	
	
	
	public int getArea(int h, int w, int lastArea, ArrayList<Record> list) {
		int minA = maxArea; // a very large number
		int area, a1, h1, w1, a2, h2, w2;
		int tempResult[];
		int flag = 0;
		
		for (int i = 0; i < numOfRec; i++) {
			if (checkList[i] == 1) {
				checkList[i] = 0;
				flag = 1;
				
				int crtH = recList[i].getHeight();
				int crtW = recList[i].getWidth();

				list.add(new Record(i, 1));
				tempResult = getIncreasedArea(crtH, crtW, h, w, 1); // up = 1
				a1 = lastArea + tempResult[2];
				h1 = tempResult[0];
				w1 = tempResult[1];
				area = getArea(h1, w1, a1, list);
				if (area < minA) {
					minA = area;
				}

				list.remove(list.size()-1);
				list.add(new Record(i, 0));
				tempResult = getIncreasedArea(crtH, crtW, h, w, 0); // right = 0
				a2 = lastArea + tempResult[2];
				h2 = tempResult[0];
				w2 = tempResult[1];
				area = getArea(h2, w2, a2, list);
				if (area < minA) {
					minA = area;
				}
				
				checkList[i] = 1;
				list.remove(list.size()-1);
			}
		}

		if (flag == 1){
			return minA;
		}else { // all rectangles have been checked
			display(list);
			System.out.println("Area = " + lastArea);
			return lastArea;
		}
	}
	
	void display(ArrayList<Record> list){
		for (Record record : list){
			System.out.print(record.getNumOfRec() + ":" + record.getHowToAdd());
			System.out.print("-->");
		}
	}

	public int[] getIncreasedArea(int crtH, int crtW, int h, int w,
			int upOrRight) {
		int result[] = new int[3]; // 0 = height, 1 = width, 2 = area

		if (upOrRight == 1) {
			if (crtW > w) {
				result[0] = h + crtH;
				result[1] = crtW;
				result[2] = crtH * crtW + (crtW - w) * h;
			} else {
				result[0] = h + crtH;
				result[1] = w;
				result[2] = crtH * w;
			}
		} else if (upOrRight == 0) {
			if (crtH > h) {
				result[0] = crtH;
				result[1] = w + crtW;
				result[2] = crtH * crtW + (crtH - h) * w;
			} else {
				result[0] = h;
				result[1] = w + crtW;
				result[2] = h * crtW;
			}
		} else {
			System.out
					.println("Error: please specify the place for adding the next rectangle.");
		}

		return result;
	}

	public static void main(String args[]) {
		PackSearch1 ps = new PackSearch1("data/test.txt");
		ArrayList<Record> logList = new ArrayList<Record>();
		//System.out.println(ps.getArea(0, 0, 0, logList));
		ps.searchForMinArea(0, 0, 0, logList);
		
		System.out.println("The total searching time is: " + ps.searchTime);
	}

}
