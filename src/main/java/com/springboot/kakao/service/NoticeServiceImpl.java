package com.springboot.kakao.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.UUID;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import com.springboot.kakao.model.beans.FileBean;
import com.springboot.kakao.model.beans.NoticeBean;
import com.springboot.kakao.mapper.NoticeDao;
import com.springboot.kakao.model.dto.NoticeDto;
import com.springboot.kakao.model.dto.NoticeInsertDto;
import com.springboot.kakao.model.dto.NoticeUpdateDto;

@Service
public class NoticeServiceImpl implements NoticeService {

	@Autowired
	private NoticeDao noticeDao;
	
	@Autowired
	private ServletContext context;
	
	private NoticeBean noticeBean;
	private List<NoticeDto> noticeListAll;
	
	public void setNoticeBean(int pageNumber) {
		noticeBean = new NoticeBean();
		noticeBean.setNoticeTotalCount(noticeListAll.size());
		noticeBean.setPageNumber(pageNumber);
		noticeBean.setStartIndex();
		noticeBean.setEndIndex();
		noticeBean.setTotalPage();
		noticeBean.setStartPage();
		noticeBean.setEndPage();
	}
	
	@Override
	public NoticeBean getNoticeBean() {
		return noticeBean;
	}
	
	@Override
	public int parseIntPageNumber(String pageNumber) {
		return Integer.parseInt(pageNumber);
	}
	
	@Override
	public List<NoticeDto> getNoticeListAll() {
		return noticeDao.getNoticeListAll();
	}
	
	@Override
	public List<NoticeDto> getNoticeList(int pageNumber) {
		noticeListAll = getNoticeListAll();
		List<NoticeDto> noticeList = new ArrayList<NoticeDto>();
		
		setNoticeBean(pageNumber);
		
		for(int i = noticeBean.getStartIndex(); i < noticeBean.getEndIndex() && i < noticeBean.getNoticeTotalCount(); i++) {
			noticeList.add(noticeListAll.get(i));
		}
		return noticeList;
	}

	@Override
	public NoticeDto fileUpload(NoticeInsertDto noticeInsertDto) {
		MultipartFile[] multipartFiles = noticeInsertDto.getNotice_file();
		String filePath = context.getRealPath("/static/fileupload");
		NoticeDto noticeDto = new NoticeDto();
		
		StringBuilder originName = new StringBuilder();
		StringBuilder tempName = new StringBuilder();
		
		for(MultipartFile multipartFile : multipartFiles) {
			String originFile = multipartFile.getOriginalFilename();
			if(originFile.equals("")) {
				return noticeDto;
			}
			String originFileExtension = originFile.substring(originFile.lastIndexOf("."));
			String tempFile = UUID.randomUUID().toString().replaceAll("-", "") + originFileExtension;
		
			originName.append(originFile);
			originName.append(",");
			tempName.append(tempFile);
			tempName.append(",");
			
			File file = new File(filePath, tempFile);
			if(!file.exists()) {
				file.mkdirs();
			}
		
			try {
				multipartFile.transferTo(file);
			} catch (IllegalStateException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		originName.delete(originName.length()-1, originName.length());
		tempName.delete(tempName.length()-1, tempName.length());
		
		noticeDto.setOriginFileNames(originName.toString());
		noticeDto.setTempFileNames(tempName.toString());
		return noticeDto;
		
	}

	@Override
	public int noticeInsert(NoticeInsertDto noticeInsertDto) {
		NoticeDto noticeDto = fileUpload(noticeInsertDto);
		noticeDto.setNotice_code(getNoticeMaxCode() +1);
		noticeDto.setNotice_title(noticeInsertDto.getNotice_title());
		noticeDto.setNotice_writer(noticeInsertDto.getNotice_writer());
		noticeDto.setNotice_content(noticeInsertDto.getNotice_content());
		
		int mstInsertFlag = noticeDao.noticeMstInsert(noticeDto);
		int dtlInsertFlag = 0;
		
		if(mstInsertFlag ==1 ) {
			dtlInsertFlag = noticeDao.noticeDtlInsert(noticeDto);
			return noticeDto.getNotice_code();
		}
		
		return dtlInsertFlag;
	}

	@Override
	public int getNoticeMaxCode() {
		return noticeDao.getNoticeMaxCode();
	}

	@Override
	public NoticeDto getNotice(String notice_code) {
		plusNoticeCount(notice_code);
		return noticeDao.getNotice(Integer.parseInt(notice_code));
	}

	@Override
	public List<FileBean> getFileList(NoticeDto noticeDto) {
		
		if(noticeDto.getOriginFileNames() == null || noticeDto.getTempFileNames() == null) {
			return null;
		}
		List<FileBean> fileList = new ArrayList<FileBean>();
		
		StringTokenizer ofn = new StringTokenizer(noticeDto.getOriginFileNames(), ",");
		StringTokenizer tfn = new StringTokenizer(noticeDto.getTempFileNames(), ",");
		
		while(ofn.hasMoreTokens()) {
			FileBean fileBean = new FileBean();
			fileBean.setOriginFileName(ofn.nextToken());
			fileBean.setTempFileName(tfn.nextToken());
			fileList.add(fileBean);
		}
		
		return fileList;
	}

	@Override
	public byte[] fileDownload(FileBean fileBean) {
		String filePath = context.getRealPath("/static/fileupload");
		File file = new File(filePath, fileBean.getTempFileName());
		byte[] fileData = null;
		
		try {
			fileData = FileCopyUtils.copyToByteArray(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return fileData;
	}

	@Override
	public void plusNoticeCount(String notice_code) {
		noticeDao.plusNoticeCount(Integer.parseInt(notice_code));	
	}

	@Override
	public int noticeDelete(String notice_code) {
		int i_notice_code = Integer.parseInt(notice_code);
		
		NoticeDto noticeDto = noticeDao.getNotice(i_notice_code);
		String filePath = context.getRealPath("/static/fileupload");
		List<FileBean> fileList = getFileList(noticeDto);
		
		int result = 0;
		result += noticeDao.noticeDtlDelete(i_notice_code);
		System.out.println(result);
		if(result == 1) {
			result += noticeDao.noticeMstDelete(i_notice_code);
			if(result == 2 && fileList != null) {
				for(FileBean fileBean : fileList) {
					File file = new File(filePath, fileBean.getTempFileName());
					if(file.exists()) {
						file.delete();
					}
				}
			}
		}
		
		return result;
	}

	public StringBuilder deleteFileName(String[] fileNames, String[] deleteFileNames) {
		System.out.println("되고 있니?");
		StringBuilder buildName = new StringBuilder();
		if(fileNames != null) {
			for(String fileName : fileNames) {
				int count = 0;
				if(deleteFileNames != null) {
					for(String deleteFileName : deleteFileNames) {
						if(fileName.equals(deleteFileName)) {
							count++;
							break;
						}
					}
				}
				if ( count == 0 ) {
					buildName.append(fileName);
					buildName.append(",");
				}
			}
			
		}
		
		
		return buildName;
	}
	public NoticeDto fileUpdate(NoticeUpdateDto noticeUpdateDto) {
		System.out.println("되고 있지");
		NoticeDto noticeDto = new NoticeDto();
		
		StringBuilder originNames = deleteFileName(noticeUpdateDto.getOriginFileNames(), noticeUpdateDto.getDeleteOriginFileNames());
		StringBuilder tempNames = deleteFileName(noticeUpdateDto.getTempFileNames(), noticeUpdateDto.getDeleteTempFileNames());
		
		MultipartFile[] multipartFiles = noticeUpdateDto.getNotice_file();
		String filePath = context.getRealPath("/static/fileupload");
		
		if(multipartFiles != null) {
			for(MultipartFile multipartFile : multipartFiles) {
				String originFile = multipartFile.getOriginalFilename();
				if(originFile.equals("")  || multipartFile == null) {
					break;
				}
				
				String originFileExtension = originFile.substring(originFile.lastIndexOf("."));
				String tempFile = UUID.randomUUID().toString().replaceAll("-", "") + originFileExtension;
				
				originNames.append(originFile);
				originNames.append(",");
				tempNames.append(tempFile);
				tempNames.append(",");
				
				File file = new File(filePath, tempFile);  // , tempFile 필수
				if(!file.exists()) {
					file.mkdirs();  // 하위 경로 모두 포함
				}
				
				try {
					multipartFile.transferTo(file);     // multipartFile을 file에 카피할 것
				} catch (IllegalStateException | IOException e) {
					e.printStackTrace();
				}  
			}
		}
		
		
		if(originNames.length() != 0) {
			originNames.delete(originNames.length() -1, originNames.length());
			tempNames.delete(tempNames.length() -1, tempNames.length());
			
			noticeDto.setOriginFileNames(originNames.toString()); // toString 필수
			noticeDto.setTempFileNames(tempNames.toString());
		}
		return noticeDto;
	}
	@Override
	public int noticeUpdate(NoticeUpdateDto noticeUpdateDto) {
		System.out.println("되고있음");
		NoticeDto noticeDto = fileUpdate(noticeUpdateDto);
		noticeDto.setNotice_code(noticeUpdateDto.getNotice_code());
		noticeDto.setNotice_content(noticeUpdateDto.getNotice_content());
		int result = noticeDao.noticeUpdate(noticeDto);
		if(result == 1 && noticeUpdateDto.getDeleteOriginFileNames() != null) {
			String filePath = context.getRealPath("/static/fileupload");
			for(String fileName : noticeUpdateDto.getDeleteTempFileNames()) {
				File file = new File(filePath, fileName);
				if(file.exists()) {
					file.delete();
				}
			}
		}
		return result;
	}
	
	


	
}