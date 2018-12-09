package ru.omgups.exview.excel.view

import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.stereotype.Component
import ru.omgups.exview.model.StudentsGroup
import ru.omgups.exview.repo.SessionSubjectRepo
import java.io.FileOutputStream
import java.io.File
import java.io.OutputStream


@Component
class ExcelView {
    @Autowired
    lateinit var sessionSubjectGroupRepo: SessionSubjectRepo

    @Throws(Exception::class)
    fun buildExcelDocument(fileName: String): Resource {
        val book = XSSFWorkbook()
        val sheet = book.createSheet("Examen")
        var row = sheet.createRow(0)
        var name = row.createCell(0)
        name.setCellValue("Группы")
        name = row.createCell(1)
        name.setCellValue("Предмет")
        name = row.createCell(2)
        name.setCellValue("Преподователь")
        var j = 3
        for (i in 8..31) {
            name = row.createCell(j)
            name.setCellValue(i.toDouble())
            ++j
        }
        val iterable = sessionSubjectGroupRepo.findAll();
        val groups = HashSet<StudentsGroup>()
        for (it in iterable) {
            val gr = it.studentsGroup
            if (gr != null) {
                groups.add(gr)
            }
        }
        groups.sortedBy { it.name }
        var i = 1
        for (gr in groups) {
            row = sheet.createRow(i)
            name = row.createCell(0)
            name.setCellValue(gr.name)
            for (subj in gr.sessionSubjects) {
                if (subj.date != -1) {
                    name = row.createCell(1)
                    name.setCellValue(subj.subject?.name)
                    name = row.createCell(2)
                    name.setCellValue(subj.lector?.name)
                    name = row.createCell(subj.date - 8 + 3)
                    name.setCellValue(subj.auditorium?.name)
                    ++i
                    row = sheet.createRow(i)
                }
            }
        }
        row = sheet.createRow(i)
        name = row.createCell(0)
        name.setCellValue("")
        for(it in 0..33) {
            sheet.autoSizeColumn(it)
        }

        val file = File("/$fileName")
        file.parentFile.mkdirs()
        val outFile = FileOutputStream(file)
        book.write(outFile as OutputStream?)
        book.close()
        val resource = UrlResource(file.toURI())
        return resource
    }
}