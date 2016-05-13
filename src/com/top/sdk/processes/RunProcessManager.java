package com.top.sdk.processes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import com.top.sdk.utils.LogUtil;

import android.util.Log;

public class RunProcessManager {
	/** first app user */
	public static final int AID_APP = 10000;

	/** offset for uid ranges for each user */
	public static final int AID_USER = 100000;

	public static String getForegroundApp() {
	  File[] files = new File("/proc").listFiles(); 
	  int lowestOomScore = Integer.MAX_VALUE;
	  String foregroundProcess = "";
     
	  for (File file : files) {
	    if (!file.isDirectory()) {
	      continue;
	    }
	    int pid;
	    try {
	    	
	      pid = Integer.parseInt(file.getName());
	      if(pid==23408 || pid == 23378)
	      Log.d("test", "pid"+pid);
	     
	    } catch (NumberFormatException e) {
	      continue;
	    }

	    try {
	      String cgroup = read(String.format("/proc/%d/cgroup", pid));
	       // 4:cpu:/  2:memory:/
	//      LogUtil.d("zz","cgroup--->"+cgroup);
	      
	      String[] lines = cgroup.split("\n");
	     // LogUtil.d("lines-->"+lines.length);
	       if(pid==23408 || pid == 23378)
	       // Log.d("test", "cgroup-->"+cgroup ); 
	      if (lines.length <2) {     
	        continue;
	      }
	       String cpuSubsystem = "";
	       String cpuaccctSubsystem = "";
          if(lines.length==3){
	       cpuSubsystem = lines[0];
	       cpuaccctSubsystem = lines[2];
          }
          if(lines.length==2){
        	 cpuSubsystem = lines[0];
   	       cpuaccctSubsystem = lines[1];
          }
	   // Log.d("test", "cpuaccctSubsystem-->"+cpuaccctSubsystem +"==="+pid);

	      if (!cpuaccctSubsystem.endsWith(Integer.toString(pid))) {
	        // not an application process
	        continue;
	      }
//	      if (cpuSubsystem.endsWith("bg_non_interactive")) {
//	        // background policy
//	        continue;
//	      }
	      String cmdline = read(String.format("/proc/%d/cmdline", pid)).trim();
	      if (cmdline.contains("com.android.systemui")) {
	        continue;
	      }
           
	      int uid = Integer.parseInt(cpuaccctSubsystem.split(":")[2].split("/")[1].replace("uid_", ""));
	      if (uid >= 1000 && uid <= 1038) {
	        // system process
	        continue;
	      }

	      int appId = uid - AID_APP;
	      int userId = 0;
	      // loop until we get the correct user id.
	      // 100000 is the offset for each user.
	      while (appId > AID_USER) {
	        appId -= AID_USER;
	        userId++;
	      }

	      if (appId < 0) {
	        continue;
	      }

	      // u{user_id}_a{app_id} is used on API 17+ for multiple user account support.
	      // String uidName = String.format("u%d_a%d", userId, appId);

	      File oomScoreAdj = new File(String.format("/proc/%d/oom_score_adj", pid));
	      if (oomScoreAdj.canRead()) {
	        int oomAdj = Integer.parseInt(read(oomScoreAdj.getAbsolutePath()));
//	        Log.d("test", "oomAdj"+oomAdj);
	        if (oomAdj != 0) {
	          continue;
	        }
	      }

	      int oomscore = Integer.parseInt(read(String.format("/proc/%d/oom_score", pid)));
//	           Log.d("test", "oomscore"+oomscore);
	      if (oomscore < lowestOomScore) {
	        lowestOomScore = oomscore;
	        foregroundProcess = cmdline;
	      }

	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	  }
	  return foregroundProcess;
	}

	private static String read(String path) throws IOException {
	  StringBuilder output = new StringBuilder();
	  File fileName = new File(path);
	  InputStreamReader isr=new InputStreamReader(new FileInputStream(fileName),"UTF-8");
	  BufferedReader reader = new BufferedReader(isr);
	  output.append(reader.readLine());
	  for (String line = reader.readLine(); line != null; line = reader.readLine()) {
	    output.append('\n').append(line);
	  }
	  reader.close();
	  isr.close();
	  return output.toString();
	}
	
}
