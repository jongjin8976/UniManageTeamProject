package com.study.test.admin.service;

import java.util.List;

import com.study.test.admin.vo.AdminMenuVO;
import com.study.test.admin.vo.AdminSubMenuVO;
import com.study.test.admin.vo.EmpVO;
import com.study.test.member.vo.MemberVO;
import com.study.test.school.colleage.ColleageVO;
import com.study.test.school.dept.DeptManageVO;
import com.study.test.school.dept.DeptVO;
import com.study.test.school.double_major.DoubleMajorVO;
import com.study.test.stu.vo.StatusInfoVO;
import com.study.test.stu.vo.StuVO;

public interface AdminService {
	//어드민 매뉴
	List<AdminMenuVO> getAdminMenuList();
	//어드민 서브매뉴
	List<AdminSubMenuVO> getAdminSubMenuList(String menuCode);
	//coll_list 조회
	List<ColleageVO> getCollList();
	//dept_list조회
	List<DeptVO> getDeptList();
	//doubleMajorList조회
	List<DoubleMajorVO> getDoubleMajorList();
	//학생 등록
	void regStu(StuVO stuVO);
	//교직원 등록
	void regEmp(EmpVO empVO);
	//학적관리 리스트 조회
	List<DeptManageVO> getDeptManageList(DeptManageVO deptManageVO);
	//학적 변경 대상자 조회
	DeptManageVO getDeptManageData(String applyNo);
	//학적 변경 대상자 인적사항 조회
	MemberVO getMemInfo(String memNo);
	//학적 변경 실행
	void updateStuCollDept(StuVO stuVO, String applyNo);
	//변경 대상자 정보 리스트 조회
	List<DeptManageVO> getApplyNoByStuInfoList(DeptManageVO deptManageVO);
	//대상자들 업데이트
	int updateStuInfoByApplyData(DeptManageVO deptManageVO);
	//휴학 신청 대상자 조회
	List<StatusInfoVO> getLeaveManageList();
	
	//휴학 신청 대상자 조회
	StatusInfoVO getLeaveManageMember(String statusNo);
	
	//휴학신청 승인 statusInfo
	int updateStatusInfoByTakeOff(String statusNo, String stuNo);
	
	//휴학 신청 대상자 인적사항 조회
	MemberVO getMemInfoByState(String memNo);
	
	
	
	
	
	
	
	
}
