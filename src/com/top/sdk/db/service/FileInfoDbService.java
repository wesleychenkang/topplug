package com.top.sdk.db.service;

import java.util.List;

import com.top.sdk.entity.FileInfo;

public interface FileInfoDbService {
   
	public List<FileInfo> getListFileInfo();
	
	public FileInfo getFileInfoByUrl(String url);
	
	public List<FileInfo> getAllSuccessFile();
	
	public boolean addFileInfo(FileInfo info);
	
	public boolean addListFileInfo(List<FileInfo> list);
	
	public boolean updateFileInfo(FileInfo info);

}
