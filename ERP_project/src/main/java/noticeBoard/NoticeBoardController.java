package noticeBoard;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import service.EmplClass;
import service.Employee;
import service.ServiceClass;

@Controller
public class NoticeBoardController {
	@Autowired
	NoticeService ns;

	@GetMapping("/notice")
	public String notice(Model model, String page) {
		ServiceClass list;
		if (page != null) {
			list = ns.getService(page);
			model.addAttribute("list", list);
		} else {
			list = ns.getService("1");
			model.addAttribute("list", list);
		}
		List<String> writer= new ArrayList<String>();
		List<NoticeBoard> nList = (List <NoticeBoard>)list.getTablelist();
		for(int i=0;i<nList.size();i++) {
			writer.add("["+ns.getWriter(nList.get(i).getWriter()).getDepartment().getDepartmentName()+"] "+ns.getWriter(nList.get(i).getWriter()).getName());
		}
		model.addAttribute("writer", writer);
		return "notice/notice";
	}

		

	@GetMapping("/noticeSearch")
	public String noticeSearch(Model model, NoticeComment comment, int id, String deleteNo,String updateCommentId,String updateComment , HttpServletRequest req,
			HttpSession session) {
		ns.noticeSearchService(model,comment,id,deleteNo,updateCommentId,updateComment,req,session);
	
		
		return "community/notice/noticeSearch"; 
	}

	/* 삭제 페이지 */
	@GetMapping("/deleteNotice")
	public String getDeleteNotice(int id) {

		ns.selectOne(id);

		return "notice";
	}

	@PostMapping("/deleteNotice")
	public String postDeleteNotice(int id) {
		ns.deleteNotice(id);
		System.out.println(id);
		String deleteFolder = "D:/files/noticeBoard/"+id;
		File file = new File(deleteFolder);
		while(file.exists()) {
			File[] fileList=file.listFiles();
			for(int i = 0; i<fileList.length;i++) {
				fileList[i].delete();
			}
			if(fileList.length==0&& file.isDirectory()) {
				file.delete();
				
			}
		}
		return "community/notice/noticeDeleteResult";
	}

	/* 작성 페이지 */
	@GetMapping("/noticeWriter")
	public String getNoticeWriter(Model model) {

		
		
		return "community/notice/noticeWriter";

	}

	@PostMapping("/noticeWriter")
	public String postNoticeWriter(Model model, NoticeBoard noticeBoard, List<MultipartFile> filename) {
		ns.noticeWriteService(model,noticeBoard,filename);
		return "community/notice/noticeWriterResult";

	}

	/* 수정 페이지 */
	@PostMapping("/noticeModify")
	public String getNoticeModify(Model model, int id) {
		NoticeBoard result = ns.selectOne(id);
		if(!result.getFilename().equals("")) {
			
		String[] fileStr=result.getFilename().split(",");
		
		List<String> file= new ArrayList<String>();
		for(String str: fileStr) {
			file.add(str);
		}
		
		model.addAttribute("file", file);
		}
		model.addAttribute("notice", result);
		return "community/notice/noticeModify";
	}

	@PostMapping("/noitceModify1")
	public String getNoticeModify1(Model model, NoticeBoard board, int id,List<MultipartFile>filename1) {
		System.out.println(board);
		String fileName="";
		String uploadFolder="D:/files/noticeBoard/"+board.getId();
		if(!filename1.get(0).getOriginalFilename().equals("")) {
			File folder = new File(uploadFolder);
		if(!folder.exists()) {
			folder.mkdir();
		}
			for(MultipartFile m:filename1) {
				File fileList= new File(uploadFolder,m.getOriginalFilename());
				try {
					m.transferTo(fileList);
				} catch (IllegalStateException | IOException e) {
					e.printStackTrace();
				}
			}
		
			
			for( MultipartFile m:filename1) {
				fileName+=m.getOriginalFilename()+",";
			}
		}
		if(board.getFilename()!=null) {
			board.setFilename(board.getFilename()+","+fileName);
			
		}else {
			board.setFilename(fileName);
		}
	
	
		File folder = new File(uploadFolder);
		if(folder.exists()) {
			if(!board.getFilename().equals("")) {
		
				String[] str=board.getFilename().split(",");
				File[] fileList=folder.listFiles();
				List<String> list= new ArrayList<String>();
			
				for(int i =0; i<str.length;i++) {
					list.add(str[i]);
				}
				for(int i=0; i<fileList.length;i++) {
					if(!list.contains(fileList[i].getName())) {
						fileList[i].delete();
					}
				}

			}else {
				System.out.println("test1");
			
					File[] fileList=folder.listFiles();
					for(int i=0; i<fileList.length;i++) {
						System.out.println("test2");
						System.out.println(fileList[i]);
						fileList[i].delete();
					
					}
					System.out.println("test3");
					folder.delete();
				
					
						
				}
			}
			
		
		ns.updateNotice(board);
		return "community/notice/noticeUpdateResult";
	}

	@GetMapping("/filedownload")
	public String fileDownload(Model model, int id, String filename) {

		
		model.addAttribute("id", id);
		model.addAttribute("filename", filename);
		
		
		return "file/fileDownload";
	}

}
