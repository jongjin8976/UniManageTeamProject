package com.study.test.stu.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.test.stu.service.ScheduleService;
import com.study.test.stu.vo.ScheduleVO;

import jakarta.annotation.Resource;

@Controller
@RequestMapping("/schedule")
public class ScheduleController {

	@Resource(name = "scheduleService")
	private ScheduleService scheduleService;
	
	@ResponseBody
	@PostMapping("/selectMyScheduleAjax")
	public String selectMyScheduleAjax(String memNo) {
		System.out.println("멤노노노노" +memNo);
		
		scheduleService.selectMySchedule(memNo);
		
		return "";
	}
	
	
	@ResponseBody
	@PostMapping("/myScheduleAjax")
	public Map<String, Object> myScheduleAjax(@RequestBody String json, ScheduleVO scheduleVO) {

		
		Map<String, Object> myScheduleList = new HashMap<>();
			
		try {
            ObjectMapper mapper = new ObjectMapper();
            HashMap[] events = mapper.readValue(json, HashMap[].class);

            // events 배열에 변환된 데이터가 저장.
            for (HashMap<String, Object> event : events) {
                String title = (String) event.get("title");
                boolean allDay = (boolean) event.get("allday");
                String start = (String) event.get("start");
                String end = (String) event.get("end");
                String viewTitle = (String) event.get("viewTitle");
                String memNo = (String)event.get("memNo");
                
                //데이터 시간 날짜형으로 포맷
                LocalDateTime dateTime = LocalDateTime.parse(start, DateTimeFormatter.ISO_DATE_TIME);
                String startTime = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                
                LocalDateTime dateTime2 = LocalDateTime.parse(end, DateTimeFormatter.ISO_DATE_TIME);
                String endTime = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                

                System.out.println("Title: " + title);
                System.out.println("All Day: " + allDay);
                System.out.println("Start: " + startTime);
                System.out.println("End: " + endTime);
                System.out.println("View Title: " + viewTitle);
                System.out.println("학번: " + memNo);
                
                scheduleVO.setTitle(title);
                scheduleVO.setStartTime(startTime);
                scheduleVO.setEndTime(endTime);
                scheduleVO.setViewTitle(viewTitle);
                scheduleVO.setMemNo(memNo);
                
                
                scheduleService.regMySchedule(scheduleVO);
                
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return myScheduleList;
		
	}
}