package com.project.emailSchedular.controller;

import com.project.emailSchedular.service.ExcelReaderDBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class LoaddataController {

    @Autowired
    ExcelReaderDBService excelReaderService;
    @RequestMapping("/load")
    public String getAllEmployees(Model model)
    {
        excelReaderService.readDataFromExcelandsavetoDB();
        model.addAttribute("view","list-employees :: list-employees");
        return "index";
    }


}
