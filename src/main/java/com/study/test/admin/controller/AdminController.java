package com.study.test.admin.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.study.test.admin.service.AdminService;
import com.study.test.admin.vo.AdminSubMenuVO;
import com.study.test.admin.vo.EmpVO;
import com.study.test.member.service.MemberService;
import com.study.test.member.vo.MemImgVO;

import com.study.test.member.vo.MemberVO;
import com.study.test.school.colleage.ColleageVO;
import com.study.test.school.dept.DeptManageVO;
import com.study.test.school.dept.DeptVO;
import com.study.test.school.double_major.DoubleMajorVO;
import com.study.test.school.service.SchoolService;
import com.study.test.stu.vo.StatusInfoVO;
import com.study.test.stu.vo.StuVO;
import com.study.test.util.ConstVariable;
import com.study.test.util.UploadUtil;

import jakarta.annotation.Resource;

@Controller
@RequestMapping("/admin")
public class AdminController {
	@Resource(name = "memberService")
	private MemberService memberService;
	@Resource(name = "adminService")
	private AdminService adminService;
	@Resource(name = "schoolService")
	private SchoolService schoolService;

	// 회원등록 페이지 이동
	@GetMapping("/joinMember")
	public String joinMember(AdminSubMenuVO adminSubMenuVO) {
		adminSubMenuVO.setMenuCode(ConstVariable.DEFAULT_MENU_CODE);
		adminSubMenuVO.setSubMenuCode(ConstVariable.DEFAULT_SUB_MENU_CODE);

		return "content/admin/join_member";
	}

	// 비밀번호 변겅 페이지 이동
	@GetMapping("/changePwd")
	public String changePwd(AdminSubMenuVO adminSubMenuVO) {
		adminSubMenuVO.setMenuCode(ConstVariable.DEFAULT_MENU_CODE);
		return "content/admin/change_pwd";
	}

	// 회원등록
	@PostMapping("/join")
	public String join(MemberVO memberVO, MultipartFile mainImg, Model model,AdminSubMenuVO adminSubMenuVO) {
		adminSubMenuVO.setMenuCode(ConstVariable.DEFAULT_MENU_CODE);
		String path = "";
		// 학생이면 추가정보 insert해야해서 조건문 넣음~
		if (memberVO.getMemRole().equals("STU")) {
			path = "redirect:/admin/insertStuInfo?memNo=" + memberVO.getMemNo();
		} else {
			path = "redirect:/admin/insertEmpInfo?memNo=" + memberVO.getMemNo();
		}

		// UploadUtill 객체 호출해서(util패키지에 만들어놓음)MemImgVO 객체에 받음
		MemImgVO attachedImgVO = UploadUtil.uploadFile(mainImg);
		// memberVO에서 받아온 memNo memImgVO에 넣음
		attachedImgVO.setMemNo(memberVO.getMemNo());
		// 다음에 들어갈 img코드 호출해서 세팅
		attachedImgVO.setImgCode(memberService.getNextImgCode());
		// memberVO.memImage에 imgCode 세팅
		memberVO.setMemImage(attachedImgVO.getImgCode());
		// memberVO안에있는 memImgVO에 UploadUtill로 불러온 데이터 넣음(트랜잭션처리때문에)
		memberVO.setMemImgVO(attachedImgVO);
		System.out.println("@@@@@@@@@@@@@@" + memberVO);
		memberService.regMember(memberVO);

		System.out.println("@@@@@@@@@"+path);
		System.out.println(adminSubMenuVO.getMenuCode());
		System.out.println(memberVO.getMemNo());
		return path;
	}

	// 학생정보등록페이지 이동
	@GetMapping("/insertStuInfo")
	public String insertStuInfo(@RequestParam("memNo")String memNo,AdminSubMenuVO adminSubMenuVO, Model model) {
		if(adminSubMenuVO.getMenuCode() == null) {
			adminSubMenuVO.setMenuCode(ConstVariable.DEFAULT_MENU_CODE);
		}
		
		
		String collNo = "COLL_001";
		System.out.println("@@@@@@@@@@@@"+memNo);
		Map<String, Object> schoolMap = new HashMap<>();
		List<ColleageVO> collList = schoolService.getCollList();
		List<DeptVO> deptList = schoolService.getDeptList(collNo);
		List<DoubleMajorVO> doubleMajorList = schoolService.getDoubleMajorList();
		schoolMap.put("collList", collList);
		schoolMap.put("deptList", deptList);
		schoolMap.put("doubleMajorList", doubleMajorList);
		schoolMap.put("memNo", memNo);
		
		model.addAttribute("schoolMap",schoolMap);
		
		return "content/admin/insert_stu_info";
	}
	
	// 교직원 정보 등록페이지 이동
	@GetMapping("/insertEmpInfo")
	public String insertEmpInfo(@RequestParam("memNo")String memNo, AdminSubMenuVO adminSubMenuVO, Model model) {
		if(adminSubMenuVO.getMenuCode() == null) {
			adminSubMenuVO.setMenuCode(ConstVariable.DEFAULT_MENU_CODE);
		}
		String collNo = "COLL_001";
		Map<String, Object> schoolMap = new HashMap<>();
		List<ColleageVO> collList = schoolService.getCollList();
		List<DeptVO> deptList = schoolService.getDeptList(collNo);
		List<DoubleMajorVO> doubleMajorList = schoolService.getDoubleMajorList();
		schoolMap.put("collList", collList);
		schoolMap.put("deptList", deptList);
		schoolMap.put("doubleMajorList", doubleMajorList);
		schoolMap.put("memNo", memNo);
		
		model.addAttribute("schoolMap",schoolMap);
		return "content/admin/insert_emp_info";
	}
	
	//대학교 코드 변경시 학과코드 변경
	@PostMapping("/deptListAjax")
	@ResponseBody
	public List<DeptVO> deptListAjax(String collNo) {
		List<DeptVO> deptList = schoolService.getDeptList(collNo);
		
		return deptList;
	}
	
	
	//학생정보등록
	@PostMapping("/insertStu")
	public String insertStu(StuVO stuVO) {
		System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"+stuVO);
		adminService.regStu(stuVO);	
		return "redirect:/admin/joinMember";
	}
	
	//교직원등록
	@PostMapping("/insertEmp")
	public String insertEmp(EmpVO empVO) {
		System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"+empVO);
		adminService.regEmp(empVO);
		return "redirect:/admin/joinMember";
	}
	
	// 학적변동승인(복학,휴학)
	@GetMapping("/updateStuInfo")
	public String updateStuInfo(AdminSubMenuVO adminSubMenuVO, Model model) {
		adminSubMenuVO.setMenuCode(ConstVariable.SECOND_MENU_CODE);
		StatusInfoVO statusInfoVO = new StatusInfoVO();
		List<StatusInfoVO> statusInfoList = adminService.getLeaveManageList(statusInfoVO);
		List<StatusInfoVO> rollBackManageList = adminService.getRollBackManageList(statusInfoVO);
		model.addAttribute("leaveManageList",statusInfoList);
		model.addAttribute("rollBackManageList",rollBackManageList);
		
		
		return "content/admin/update_stu_info";
	}
	
	//휴학 신청 모달창 오픈
	@PostMapping("/statusModalOpenAjax")
	@ResponseBody
	public Map<String, Object> statusModalOpenAjax(@RequestBody Map<String, String> dataMap) {
		Map<String, Object> statusMap = new HashMap<>();
		StatusInfoVO statusInfoVO = new StatusInfoVO();
		statusInfoVO.setStatusNo(dataMap.get("statusNo"));
		statusInfoVO.setAfterStatus(dataMap.get("afterStatus"));
		StatusInfoVO statusInfo = adminService.getLeaveManageMember(statusInfoVO);
		MemberVO memberInfo = adminService.getMemInfoByState(dataMap.get("stuNo"));
		
		statusMap.put("statusInfo", statusInfo);
		statusMap.put("memberInfo", memberInfo);
		
		return statusMap;
	}
	
	//휴학 신청 상태에 따른 검색
	@PostMapping("/selectByStatusAjax")
	@ResponseBody
	public List<StatusInfoVO> selectByStatusAjax(String statusData) {
		StatusInfoVO statusInfoVO = new StatusInfoVO();
		statusInfoVO.setIngStatus(statusData);
		System.out.println(statusInfoVO.getIngStatus());
		return adminService.getLeaveManageList(statusInfoVO);
	}
	
	//휴학 신청 날짜별 검색
	@PostMapping("/selectByDateStatusInfoAjax")
	@ResponseBody
	public List<StatusInfoVO> selectByDateStatusInfoAjax(@RequestBody Map<String, String> stateMap) {
		StatusInfoVO statusInfoVO = new StatusInfoVO();
		statusInfoVO.setIngStatus(stateMap.get("ingStatus"));
		statusInfoVO.setFromDate(stateMap.get("fromDate"));
		statusInfoVO.setToDate(stateMap.get("toDate"));
		List<StatusInfoVO> statusInfoList = adminService.getLeaveManageList(statusInfoVO);
		
		return statusInfoList;
	}
	
	//복학 신청 상태에 따른 검색
	@PostMapping("/selectByStatusRollAjax")
	@ResponseBody
	public List<StatusInfoVO> selectByStatusRollAjax(String statusData) {
		StatusInfoVO statusInfoVO = new StatusInfoVO();
		statusInfoVO.setIngStatus(statusData);
		return adminService.getRollBackManageList(statusInfoVO);
	}
	
	//복학 신청 날짜에 따른 검색
	@PostMapping("/selectByDateStatusRollInfoAjax")
	@ResponseBody
	public List<StatusInfoVO> selectByDateStatusRollInfoAjax(@RequestBody Map<String, String> stateMap) {
		StatusInfoVO statusInfoVO = new StatusInfoVO();
		statusInfoVO.setIngStatus(stateMap.get("ingStatus"));
		statusInfoVO.setFromDate(stateMap.get("fromDate"));
		statusInfoVO.setToDate(stateMap.get("toDate"));
		List<StatusInfoVO> statusInfoList = adminService.getRollBackManageList(statusInfoVO);
		
		return statusInfoList;
	}
	
	//휴학/복학 신청 승인
	@PostMapping("/changeStatusAjax")
	@ResponseBody
	public int changeStatusAjax(@RequestBody Map<String, String> statusMap) {
		StuVO stuVO = new StuVO();
		stuVO.setStuStatus(statusMap.get("stuStatus"));
		StatusInfoVO statusInfoVO = new StatusInfoVO();
		statusInfoVO.setStatusNo(statusMap.get("statusNo"));
		statusInfoVO.setStuNo(statusMap.get("stuNo"));
		statusInfoVO.setStuVO(stuVO);
		return adminService.updateStatusInfoByTakeOff(statusInfoVO);
		
	}
	
	//휴학 신청 일괄 승인
	@PostMapping("/checkedAcceptByStatusAjax")
	@ResponseBody
	public int checkedAcceptByStatusAjax(@RequestBody Map<String, List<String>> acceptDataMap) {
		//statusNOList 
		List<String> statusNoList = (List<String>)acceptDataMap.get("statusNoList");
		List<String> stuNoList = (List<String>)acceptDataMap.get("stuNoList");
		System.out.println("data@@@@@@@@@@@@@@@@@@@@@" + statusNoList);
		System.out.println("data2@@@@@@@@@@@@@@@@@@@@@@" + stuNoList);
		StatusInfoVO statusInfoVO = new StatusInfoVO();
		statusInfoVO.setStatusNoList(statusNoList);
		statusInfoVO.setStuNoList(stuNoList);
		return adminService.updateStatusNoListByTakeOff(statusInfoVO);
		
		
	}
	
	//복학 신청 일괄 승인
	@PostMapping("/checkedAcceptByStatusRollAjax")
	@ResponseBody
	public int checkedAcceptByStatusRollAjax(@RequestBody Map<String, List<String>> acceptDataMap) {
		List<String> statusNoList = (List<String>)acceptDataMap.get("statusNoList");
		List<String> stuNoList = (List<String>)acceptDataMap.get("stuNoList");
		StatusInfoVO statusInfoVO = new StatusInfoVO();
		statusInfoVO.setStatusNoList(statusNoList);
		statusInfoVO.setStuNoList(stuNoList);
		return adminService.updateStatusNoListByTakeOn(statusInfoVO);
		
		
	}
	
	// 전과/복수전공페이지 이동
	@GetMapping("/changeMajor")
	public String changeMajor(AdminSubMenuVO adminSubMenuVO, Model model) {
		adminSubMenuVO.setMenuCode(ConstVariable.SECOND_MENU_CODE);
		DeptManageVO deptManageVO = new DeptManageVO();
		model.addAttribute("deptManageList", adminService.getDeptManageList(deptManageVO));
		model.addAttribute("doubleRequestList", adminService.getDoubleMajorRequestList(deptManageVO));
		return "content/admin/change_major";
	}
	
	//전과 신청 모달 Ajax
	@ResponseBody
	@PostMapping("/acceptChangeMajorAjax")
	public Map<String, Object> acceptChangeMajorAjax(String applyNo, String memNo) {
		
		Map<String, Object> data = new HashMap<>();
		
		DeptManageVO acceptData = adminService.getDeptManageData(applyNo);
		MemberVO acceptInfoData = adminService.getMemInfo(memNo);
		data.put("acceptData", acceptData);
		data.put("acceptInfoData", acceptInfoData);
		
		return data;
	}
	
	//복수전공 모달 Ajax
	@ResponseBody
	@PostMapping("/acceptDoubleMajorAjax")
	public Map<String, Object> acceptDoubleMajorAjax(String applyNo, String memNo) {
		Map<String, Object> data = new HashMap<>();
		DeptManageVO acceptData = adminService.getDoubletManageData(applyNo);
		MemberVO acceptInfoData = adminService.getMemInfo(memNo);
		data.put("acceptData", acceptData);
		data.put("acceptInfoData", acceptInfoData);
		
		return data;
		
	}
	
	
	//전과신청 업데이트
	@ResponseBody
	@PostMapping("/updateStuInfoAjax")
	public void updateStuInfoAjax(@RequestBody Map<String, String> stuMap) {
		String memNo = stuMap.get("memNo");
		String applyNo = stuMap.get("applyNo");
		String toColl = stuMap.get("toColl");
		String toDept = stuMap.get("toDept");
		
		StuVO stuVO = new StuVO();
		stuVO.setStuNo(memNo);
		stuVO.setCollNo(toColl);
		stuVO.setDeptNo(toDept);
		
		adminService.updateStuCollDept(stuVO, applyNo);
		
	}
	//복수전공 신청 업데이트
	@ResponseBody
	@PostMapping("/updateStuInfoByDoubleAjax")
	public int updateStuInfoByDoubleAjax(@RequestBody Map<String, String> stuMap) {
		DeptManageVO deptManageVO = new DeptManageVO();
		StuVO stuVO = new StuVO();
		deptManageVO.setApplyNo(stuMap.get("applyNo"));
		String doubleMajorDeptNo = stuMap.get("doubleMajorDeptNo");
		//업데이트할 DOUBLE_NO 코드 가져오기
		String doubleNo = adminService.getDoubleNo(doubleMajorDeptNo);
		stuVO.setStuNo(stuMap.get("memNo"));
		stuVO.setDoubleNo(doubleNo);
		deptManageVO.setStuVO(stuVO);

		return adminService.updateStuDouble(deptManageVO);
	}
	
	
	
	//승인상태에 따른 검색
	@ResponseBody
	@PostMapping("/searchByStatusAjax")
	public List<DeptManageVO> searchByStatusAjax(String processStatus) {
		DeptManageVO deptManageVO = new DeptManageVO();
		deptManageVO.setProcessStatus(processStatus);
		System.out.println(processStatus);
		List<DeptManageVO> deptManageList = adminService.getDeptManageList(deptManageVO);
		
		return deptManageList;
	}
	
	//승인상태에 따른 복수전공 신청자 검색
	@ResponseBody
	@PostMapping("/searchByDoubleStatusAjax")
	public List<DeptManageVO> searchByDoubleStatusAjax(String processStatus) {
		System.out.println(processStatus);
		DeptManageVO deptManageVO = new DeptManageVO();
		deptManageVO.setProcessStatus(processStatus);
		List<DeptManageVO> doubleManageList = adminService.getDoubleMajorRequestList(deptManageVO);
	
		return doubleManageList;
	}
	
	//날짜별 검색
	@ResponseBody
	@PostMapping("/searchByDateAjax")
	public List<DeptManageVO> searchByDateAjax(String toDate, String fromDate) {
		DeptManageVO deptManageVO = new DeptManageVO();
		deptManageVO.setToDate(toDate);
		deptManageVO.setFromDate(fromDate);
		List<DeptManageVO> deptManageList = adminService.getDeptManageList(deptManageVO);
		
		return deptManageList;
	}
	//날짜별 복수전공자 검색
	@ResponseBody
	@PostMapping("/searchByDateDoubleAjax")
	public List<DeptManageVO> searchByDateDoubleAjax(String toDate, String fromDate) {
		DeptManageVO deptManageVO = new DeptManageVO();
		deptManageVO.setToDate(toDate);
		deptManageVO.setFromDate(fromDate);
		List<DeptManageVO> doubleManageList = adminService.getDoubleMajorRequestList(deptManageVO);
		return doubleManageList;
	}
	
	//일괄승인
	@ResponseBody
	@PostMapping("/checkedAcceptAjax")
	public int checkedAcceptAjax(@RequestBody Map<String, List<String>> applyMap) {
	    System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" + applyMap.get("applyCodeList"));
	    List<String> applyNoList = applyMap.get("applyCodeList");
	    DeptManageVO deptManageVO = new DeptManageVO();
	    deptManageVO.setApplyNoList(applyNoList);
	    
	    //승인 완료 안된 학생 데이터 조회
	    List<DeptManageVO> applyStuDataList = adminService.getApplyNoByStuInfoList(deptManageVO);

	    List<StuVO> updateStuList = new ArrayList<>();
	    for (DeptManageVO deptManage : applyStuDataList) {
	        StuVO stuVO = new StuVO();
	        stuVO.setCollNo(deptManage.getToColl());
	        stuVO.setDeptNo(deptManage.getToDept());
	        stuVO.setStuNo(deptManage.getStuNo());

	        System.out.println("데이터는 = " + stuVO);
	        updateStuList.add(stuVO);
	        System.out.println("업데이트된 리스트의 데이터는 = " + updateStuList);
	    }

	    deptManageVO.setStuVOList(updateStuList);

	    return adminService.updateStuInfoByApplyData(deptManageVO);
	}
	
	//복수 전공 일괄 승인
	@PostMapping("/checkedByDoubleMajorAjax")
	@ResponseBody
	public int checkedByDoubleMajorAjax(@RequestBody Map<String, List<String>> applyMap) {
		System.out.println("@@@@@@@@@@@@@@@@@@@@@@"+ applyMap);
		List<String> applyCodeList = applyMap.get("applyCodeList");
		List<String> stuNoList = applyMap.get("stuNoList");
		List<String> doubleMajorDeptNoList = applyMap.get("doubleMajorDeptNoList");
		//빈 객체
		List<StuVO> updateStuList = new ArrayList<>();
		//빈 객체
		DeptManageVO deptManageVO = new DeptManageVO();
		deptManageVO.setApplyNoList(applyCodeList);
		DoubleMajorVO doubleMajorVO = new DoubleMajorVO();
		doubleMajorVO.setDoubleMajorDeptNoList(doubleMajorDeptNoList);
		List<DoubleMajorVO> doubleNoList = adminService.getDoubleNoByDeptList(doubleMajorVO);
		for(int i = 0 ; i < doubleNoList.size(); i++) {
			StuVO stuVO = new StuVO();
			stuVO.setDoubleNo(doubleNoList.get(i).getDoubleNo());
			stuVO.setStuNo(stuNoList.get(i));
			updateStuList.add(stuVO);
		}
		deptManageVO.setStuVOList(updateStuList);
		System.out.println(deptManageVO);
		
		return adminService.updateDoubleMajorList(deptManageVO);
		
	}
	
	
	// 실적현황
	@GetMapping("/performanceData")
	public String performanceData(AdminSubMenuVO adminSubMenuVO) {
		adminSubMenuVO.setMenuCode(ConstVariable.SECOND_MENU_CODE);

		return "content/admin/performance_data";
	}

	// 학사경고,제적
	@GetMapping("/updateStuOut")
	public String updateStuOut(AdminSubMenuVO adminSubMenuVO, Model model) {
		adminSubMenuVO.setMenuCode(ConstVariable.THIRD_MENU_CODE);
		model.addAttribute("collList", schoolService.getCollList());
		model.addAttribute("deptList",schoolService.getDeptList(""));
		return "content/admin/update_stu_out";
	}
	
	//학사경고 페이지 학생검색기능
	@PostMapping("/getStuInfoListAjax")
	@ResponseBody
	public List<MemberVO> getStuInfoListAjax(@RequestBody Map<String, String>stuMap) {
		MemberVO memberVO = new MemberVO();
		StuVO stuVO = new StuVO();
		memberVO.setMemName(stuMap.get("memName"));
		stuVO.setCollNo(stuMap.get("collNo"));
		stuVO.setDeptNo(stuMap.get("deptNo"));
		stuVO.setStuStatus(stuMap.get("stuStatus"));
		memberVO.setStuVO(stuVO);
		
		return adminService.getProbStuList(memberVO);
		
		
		
		
	}

	// 제적처리 페이지
	@GetMapping("/dismissal")
	public String dismissal(AdminSubMenuVO adminSubMenuVO) {
		adminSubMenuVO.setMenuCode(ConstVariable.THIRD_MENU_CODE);

		return "content/admin/dismissal";
	}

}
