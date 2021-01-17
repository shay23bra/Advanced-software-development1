package test;


import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class TimeSeries {

	public String[] str;
	public int numOfCol = 0, numOfRow = 0;
	float[][] data;

	public TimeSeries(String csvFileName) {
		try {
			Scanner sc = new Scanner(new FileReader(csvFileName));

			String[] newStr = sc.nextLine().split(",");
			this.str = new String[newStr.length];
			for (int i = 0; i < newStr.length; i++) {
				str[i] = newStr[i];
				this.numOfCol++;
			}
			while (sc.hasNext()) {
				addData(sc.nextLine());
			}


		} catch (IOException exep) {
		}
	}


	public void addData(String nextLine) {
		String[] tempString = nextLine.split(",");
		if(this.data == null){
			data = new float[this.numOfCol][1];
			for(int i=0; i<this.numOfCol; i++){
				this.data[i][0] = Float.parseFloat(tempString[i]);
			}
		}else{
			float[][] tempData = new float[this.numOfCol][this.numOfRow+1];
			for(int i = 0; i < this.numOfCol; i++){
				for(int j = 0; j < this.numOfRow; j++) {
					tempData[i][j] = this.data[i][j];
				}
			}
			for(int i = 0; i < this.numOfCol; i++){
				tempData[i][this.numOfRow] = Float.parseFloat(tempString[i]);
				this.data = tempData;
			}
		}
		this.numOfRow++;

	}

	public String[] getStr() { return str; }

	public int getNumOfCol() {
		return numOfCol;
	}

	public int getNumOfRow() {
		return numOfRow;
	}

	public float[][] getData() {
		return data;
	}

	public float[] getCol(/*float[][] arr,*/ int x){
		float[] col = new float[this.data.length];
		for(int j = 0; j < this.data.length; j++){
			col[j] = this.data[x][j];
		}
		return col;
	}

	public float[] getCol(String feature){
		int i = 0;
		float[] col = new float[this.data.length];
		for(i=0; i<this.str.length; i++){
			if (this.str[i].equals(feature))
				break;
		}
		for(int j = 0; j < this.data.length; j++){
			col[j] = this.data[i][j];
		}
		return col;
	}

	public int getIndex(String feature){
		int index = -1;
		for(String i:str){
			index++;
			if(str[index].equals(feature))
				return index;
		}
		return -1;
	}


}