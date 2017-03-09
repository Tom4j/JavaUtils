package com.siweidg.comm.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class TestJson {
	public static void main(String[] args) {

		 Gson gson = new GsonBuilder().setPrettyPrinting()
				.setDateFormat("yyyy-MM-dd HH:mm:ss").create();
	String str = "{  \"yqsj\": [    \"sdf\"  ],  \"zb\": [    \"sdf\"  ],  \"jiaofu\": [    \"1\"  ],  \"yingxiang\": [    \"1\"  ],  \"chutu\": [    \"1\"  ],  \"tyzb\": [    \"1\"  ],  \"zhengshe\": [    \"1\"  ],  \"jfyq\": [ \"sf\"  ],  \"fenfu\": [    \"1\"  ],  \"cgfw\": [    \"sdf\"  ],  \"zsteshu\": [    \"dsfsd\"  ],  \"cgnr\": [    \"sd\"  ],  \"qtyq\": [    \"sfd\"  ],  \"cttx\": [    \"sdf\"  ],  \"bd\": [    \"2\"  ],  \"tfyq\": [    \"sdf\"  ],  \"zjyq\": [    \"sdf\"  ],  \"tsteshu\": [    \"dsfs\"  ],  \"yxfbl\": [    \"2\"  ],  \"ws\": [    \"2\"  ],  \"ty\": [    \"dsf\"  ],  \"tiaose\": [    \"1\"  ],  \"xiangqian\": [    \"1\"  ],  \"cggs\": [    \"fsdf\"  ],  \"xqteshu\": [    \"sdfds\"  ],  \"cgmm\": [    \"sf\"  ],  \"zscheck\": [    \"D\"  ]}";
		Map<String,Object> ms = gson.fromJson(str, new TypeToken<Map<String, Object>>() {  }.getType());
		for(String s :ms.keySet()) {
			List objs =  (List) ms.get(s);
			System.out.println(objs.get(0));
		}
	}
}
