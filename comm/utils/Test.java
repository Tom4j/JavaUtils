package com.siweidg.comm.utils;


import com.vividsolutions.jts.io.ParseException;

public class Test{
	public static void main(String[] args) throws ParseException {

		int dis=1000;  
		double degToMeter = Math.PI * 6378137 / 180.0;//6378137赤道半径，一度对应赤道上的一米  
		double buf =  (dis  / degToMeter);
		System.out.println(buf);
		
		
	}
}