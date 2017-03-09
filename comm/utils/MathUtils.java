package com.siweidg.comm.utils;

public class MathUtils {
	//阿拉伯数字转中文小写？ 
	 public static String transition(String si){
	  String []aa={"","十","百","千","万","十万","百万","千万","亿","十亿"};
	  String []bb={"一","二","三","四","五","六","七","八","九"};
	  char[] ch=si.toCharArray();
	  int maxindex=ch.length;
	//  字符的转换
	//两位数的特殊转换
	  if(maxindex==2){
	   for(int i=maxindex-1,j=0;i>=0;i--,j++){
	    if(ch[j]!=48){
	     if(j==0&&ch[j]==49){
	    return aa[i];
	     }else{
	    	 return bb[ch[j]-49]+aa[i];
	     }
	    }
	   }
	//其他位数的特殊转换，使用的是int类型最大的位数为十亿
	  }else{
	   for(int i=maxindex-1,j=0;i>=0;i--,j++){
	    if(ch[j]!=48){
	    	return bb[ch[j]-49]+aa[i];
	    }
	   }
	  }
	return si; 
	 } 
	 
	 public static void main(String[] args) {
		System.out.println(transition(111111111+""));
	}
}
