package com.dat.bit.csmis.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.dat.bit.csmis.entity.Staff;
import com.dat.bit.csmis.pdf.service.PdfService;
import com.dat.bit.csmis.service.StaffService;
import com.itextpdf.text.DocumentException;

@Controller
@RequestMapping("/DAT")
public class MenuUploadController {
	
	
	@Autowired
	private PdfService pdfService;
	
	@Autowired
	private StaffService staffService;
	
	public String LunchMenuUploadPage(String staffId,Model model) {
		Staff staffData = staffService.getStaffByStaff_Id(staffId);
		String loginUsername = staffData.getName();
		
		try {
//			String encodedPdf = pdfService.getPdfAsByteString("thisweek.pdf");
//
//			model.addAttribute("thispdf", encodedPdf);
//			
//			String encodedPdf1 = pdfService.getPdfAsByteString("nextweek.pdf");
//
//			model.addAttribute("nextpdf", encodedPdf1);
		}catch(Exception e) {
			
		}
		
		model.addAttribute("loginUserName",loginUsername);
		
		return "Lunch/meal_setup";
	}
	
	
	@GetMapping("/lunch_menu_upload")
	public String LunchMenu(Authentication authentication,Model model) {
		String staffId = authentication.getName();
		
		model.addAttribute("showMealContainer_className","showMealContainer nodata");
		
		return LunchMenuUploadPage(staffId, model);
	}
	
	@PostMapping("/lunch_menu_import")
	public String importThismenu(@RequestParam("pdfFile") MultipartFile pdfFile,
								@RequestParam("saveMenuOption")String saveMenuOption,
								Authentication authentication,Model model)
			throws IOException, DocumentException {
		
		String staffId = authentication.getName();
		
		try {
			switch (saveMenuOption) {
				case "this":{
				//******** Local Development *************************
					pdfService.storeThisPdf_localDevelopment(pdfFile);
					
					String encodedPdf = pdfService.getPdfAsByteString_fromLocalDevelopment("thisweek.pdf");
				//******** Tomcat Server *****************************
//					pdfService.storeThisPdf_TomcatServer(pdfFile);
//					
//					String encodedPdf = pdfService.getPdfAsByteString_fromTomcatServer("thisweek.pdf");

					model.addAttribute("thispdf", encodedPdf);
				}break;
				case "next":{
				//******** Local Development *************************
					pdfService.storeNextPdf_localDevelopment(pdfFile);
					
					String encodedPdf = pdfService.getPdfAsByteString_fromLocalDevelopment("nextweek.pdf");
				//******** Tomcat Server *****************************
					//pdfService.storeNextPdf_TomcatServer(pdfFile);
					
					//String encodedPdf = pdfService.getPdfAsByteString_fromTomcatServer("nextweek.pdf");

					model.addAttribute("thispdf", encodedPdf);

				}break;
	
				default:
					break;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
		model.addAttribute("showMealContainer_className","showMealContainer withdata");

		return LunchMenuUploadPage(staffId, model);
	}

}
