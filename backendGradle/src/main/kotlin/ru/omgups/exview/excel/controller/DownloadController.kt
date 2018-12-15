package ru.omgups.exview.excel.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.core.io.Resource
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.GetMapping
import ru.omgups.exview.excel.view.ExcelView

@Controller
class DownloadController {

    @Autowired lateinit var excelView: ExcelView

    @GetMapping("/download/table_by_group")//{date}
    @ResponseBody
    fun serveFileByGroup(/*@PathVariable("date") date:Int*/): ResponseEntity<Resource> {
        val file: Resource = excelView.buildExcelDocumentByGroup("table_by_group.xlsx",8)
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.filename + "\"").body<Resource>(file)
    }

    @GetMapping("/download/table_by_lector")
    @ResponseBody
    fun serveFileByLector(/*@PathVariable("date") date:Int*/): ResponseEntity<Resource> {
        val file: Resource = excelView.buildExcelDocumentByLector("table_by_lector.xlsx",8)
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.filename + "\"").body<Resource>(file)
    }

    @GetMapping("/download/table_by_auditorium")
    @ResponseBody
    fun serveFileByAuditorium(/*@PathVariable("date") date:Int*/): ResponseEntity<Resource> {
        val file: Resource = excelView.buildExcelDocumentByAuditorium("table_by_auditorium.xlsx",8)
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.filename + "\"").body<Resource>(file)
    }

    @GetMapping("/download/timetable")
    @ResponseBody
    fun serveFile(/*@PathVariable("date") date:Int*/): ResponseEntity<Resource> {
        val file: Resource = excelView.buildExcelDocumentTimetable("timetable.xlsx",8)
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.filename + "\"").body<Resource>(file)
    }
}