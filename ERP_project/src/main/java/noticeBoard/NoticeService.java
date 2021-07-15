package noticeBoard;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.SelectKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import anonymousBoard.AnonymousBoard;
import department.Department;
import service.EmplClass;
import service.Employee;
import service.ServiceClass;

@Service
public class NoticeService {
	@Autowired
	NoticeBoardMapper nm;

	public ServiceClass getService(String page) {
		ServiceClass sc = new ServiceClass(Integer.parseInt(page), 15, nm.count());
		sc.setTablelist(nm.selectList(sc.getFirstRow(),15));
		return sc;
	}

	public NoticeBoard showContent(int id) {
		NoticeBoard nb = nm.select(id);
		nm.countPlus(nb.getCount() + 1, id);
		return nb;
	}

	public List<NoticeBoard> mainList() {
		return nm.mainList();
	}

	public NoticeBoard selectOne(int id) {
		NoticeBoard result = nm.select(id);
		return result;
	}
	
	public int noticeWriter(NoticeBoard noticeBoard) {

		noticeBoard.setWriteDate(LocalDate.now());
		nm.insert(noticeBoard);
		return noticeBoard.getId();
	}

	public boolean deleteNotice(int id) {
		return nm.delete(id);
	}

	public int updateNotice(NoticeBoard board) {
		return nm.update(board);
	}

	public EmplClass getWriter(String writer) {
        Employee empl=nm.getWriter(writer);
        EmplClass ec=nm.getECWriter(writer);
        Department dp =new Department(empl.getDepartmentId(),nm.getDepartment(empl.getDepartmentId()));
        ec.setDepartment(dp);

        return ec;
    }

	public int getLastIndex() {
		return nm.getLastIndex()+1;
		
	}
	public int getNextIndex(int id) {
		return nm.getNextIndex(id);
	}
	public int getBeforeIndex(int id) {
		return nm.getBeforeIndex(id);
	}
	public Map<String, Integer> getIndexInfo(int id) {
		Map<String, Integer> index = new HashMap<String, Integer>();
		
		if(nm.getFirstIndex()==id) {
			index.put("beforeIndex",id);
		}else {
			index.put("beforeIndex",nm.getBeforeIndex(id));
		}
		if(nm.getLastIndex()==id) {
			index.put("nextIndex",id);
		}else {
			index.put("nextIndex", nm.getNextIndex(id));
		}
		return index;
	}

	public void noticeWriteService(Model model, NoticeBoard noticeBoard, List<MultipartFile> filename) {
		String imgName= "";
		if(!filename.get(0).getOriginalFilename().equals("")) {
			
			for(MultipartFile fileList : filename) {
				
				imgName+=fileList.getOriginalFilename()+",";
			}
		noticeBoard.setFilename(imgName);
		noticeBoard.setWriteDate(LocalDate.now());
		nm.insert(noticeBoard);
		String uploadFolder = "D:/files/noticeBoard/"+noticeBoard.getId();
		File folder = new File(uploadFolder);
		
		if(!folder.exists()) {
			try {
				folder.mkdir();
			}catch(Exception e) {
				System.out.println("폴더 못만들어 이바보야");
			}
		}
		
		for(MultipartFile fileList : filename) {
			
			File uploadFile = new File(uploadFolder, fileList.getOriginalFilename());
	
			
		try {
			fileList.transferTo(uploadFile);
		}catch (Exception e){
			e.printStackTrace();
		}
		
		}
		}else {
			noticeBoard.setWriteDate(LocalDate.now());
			nm.insert(noticeBoard);
		}
		
	}
	public void NoticeComment(HttpSession session, NoticeComment noticeComment) {
		
	}
	
	public List<NoticeComment> getCommentList(int id){
		return nm.getCommentList(id);
	}
	
	public int getCommentCount(int id) {
		return nm.getCommentCount(id);
	}

	public void deleteComment(String deleteNo) {
	
	}

	public void updateNoticeComment(String updateComment, String updateCommentId) {
		if(updateCommentId!=null) {
			nm.updateComment(updateComment,updateCommentId);
		}
	}

	public void NoticeView(int boardId, HttpServletRequest req) {
		NoticeView nv = new NoticeView(boardId,req.getRemoteAddr());
		if(nm.getOverlapCount(nv).equals("0")) {
			nm.setNoticeViewer(nv);
			nm.setNoticeBoardCount(nm.getViewCount(boardId), boardId);
		}
	}

	public void noticeSearchService(Model model, noticeBoard.NoticeComment comment, int id, String deleteNo,
			String updateCommentId, String updateComment, HttpServletRequest req, HttpSession session) {
		NoticeView nv = new NoticeView(id,req.getRemoteAddr());
		if(nm.getOverlapCount(nv).equals("0")) {
			nm.setNoticeViewer(nv);
			nm.setNoticeBoardCount(nm.getViewCount(id), id);
		}
		NoticeBoard result = nm.select(id);
		if(result.getFilename()!=null) {
			
		String[] fileStr=result.getFilename().split(",");
		
		List<String> file= new ArrayList<String>();
		for(String str: fileStr) {
			file.add(str);
		}
		
		model.addAttribute("file", file);
		}
		Map<String, Integer> index = new HashMap<String, Integer>();
		
		if(nm.getFirstIndex()==id) {
			index.put("beforeIndex",id);
		}else {
			index.put("beforeIndex",nm.getBeforeIndex(id));
		}
		if(nm.getLastIndex()==id) {
			index.put("nextIndex",id);
		}else {
			index.put("nextIndex", nm.getNextIndex(id));
		}
		Employee empl=nm.getWriter(result.getWriter());
		EmplClass ec=nm.getECWriter(result.getWriter());
		Department dp =new Department(empl.getDepartmentId(),nm.getDepartment(empl.getDepartmentId()));
		ec.setDepartment(dp);
		model.addAttribute("beforeIndex", index.get("beforeIndex"));
		model.addAttribute("nextIndex", index.get("nextIndex"));
		model.addAttribute("result", result);
		model.addAttribute("writer", ec);
		if(comment.getComment()!=null) {
			Employee employee = (Employee) session.getAttribute("empl");
			
			NoticeComment nc = new NoticeComment(0,comment.getBoardId(), comment.getComment(), employee.getUserId() , employee.getName(),nm.getDepartment(employee.getDepartmentId()),LocalDateTime.parse(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))));
			nm.setComment(nc);
			}
		if(deleteNo!=null) {
			nm.deleteComment(deleteNo);
			}
		if(updateCommentId!=null) {
			nm.updateComment(updateComment,updateCommentId);
		}
		
		model.addAttribute("commentCount", nm.getCommentCount(id));
		model.addAttribute("noticeComment", nm.getCommentList(id));
		
	}

}
