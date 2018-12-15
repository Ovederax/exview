package ru.omgups.exview.excel.view


import org.apache.poi.hssf.util.HSSFColor
import org.apache.poi.ss.usermodel.FillPatternType
import org.apache.poi.xssf.usermodel.CustomIndexedColorMap
import org.apache.poi.xssf.usermodel.DefaultIndexedColorMap
import org.apache.poi.xssf.usermodel.XSSFColor
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.stereotype.Component
import ru.omgups.exview.model.Lector
import ru.omgups.exview.model.StudentsGroup
import ru.omgups.exview.repo.AuditoriumRepo
import ru.omgups.exview.repo.LectorRepo
import ru.omgups.exview.repo.SessionSubjectRepo
import java.awt.Color
import java.io.FileOutputStream
import java.io.File
import java.io.OutputStream
import org.apache.poi.xssf.usermodel.IndexedColorMap




@Component
class ExcelView {
    @Autowired
    lateinit var sessionSubjectGroupRepo: SessionSubjectRepo
    @Autowired
    lateinit var auditoriumRepo: AuditoriumRepo
    @Autowired
    lateinit var lectorRepo: LectorRepo


    @Throws(Exception::class)
    fun buildExcelDocumentByGroup(fileName: String, date: Int): Resource {
        val book = XSSFWorkbook()
        val sheet = book.createSheet("Examen")
        var row = sheet.createRow(0)
        var name = row.createCell(0)
        name.setCellValue("Группы")
        name = row.createCell(1)
        name.setCellValue("Предмет")
        name = row.createCell(2)
        name.setCellValue("Преподователь")

        var j=3
        for (it in date..31){
            name = row.createCell(j++)
            name.setCellValue("$it")
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
                    name = row.createCell(subj.date - date + 3)
                    name.setCellValue(subj.auditorium?.name)
                    ++i
                    row = sheet.createRow(i)
                }
            }
        }
        for (it in 0..33) {
            sheet.autoSizeColumn(it)
        }

        val file = File("/$fileName")
        val outFile = FileOutputStream(file)
        book.write(outFile as OutputStream?)
        book.close()
        val resource = UrlResource(file.toURI())
        return resource
    }

    @Throws(Exception::class)
    fun buildExcelDocumentByLector(fileName: String, date: Int): Resource {
        val book = XSSFWorkbook()
        val sheet = book.createSheet("Examen")
        var row = sheet.createRow(0)
        var name = row.createCell(0)
        name.setCellValue("Преподователь")
        name = row.createCell(1)
        name.setCellValue("Предмет")
        name = row.createCell(2)
        name.setCellValue("Группы")

        var j=3
        for (it in date..31){
            name = row.createCell(j++)
            name.setCellValue("$it")
        }

        val iterable = lectorRepo.findAll();
        val lectors = HashSet<Lector>()
        for (it in iterable) {
            val ss = it.sessionSubjects
            if (ss != null) {
                lectors.add(it)
            }
        }
        lectors.sortedBy { it.name }
        var i = 1
        for (lector in lectors) {
            row = sheet.createRow(i)
            name = row.createCell(0)
            name.setCellValue(lector.name)
            for (subj in lector.sessionSubjects) {
                if (subj.date != -1) {
                    name = row.createCell(1)
                    name.setCellValue(subj.subject?.name)
                    name = row.createCell(2)
                    name.setCellValue(subj.studentsGroup?.name)
                    name = row.createCell(subj.date - date + 3)
                    name.setCellValue(subj.auditorium?.name ?: "—")
                    ++i
                    row = sheet.createRow(i)
                }
            }
        }
        for (it in 0..33) {
            sheet.autoSizeColumn(it)
        }

        val file = File("/$fileName")
        val outFile = FileOutputStream(file)
        book.write(outFile as OutputStream?)
        book.close()
        val resource = UrlResource(file.toURI())
        return resource
    }

    @Throws(Exception::class)
    fun buildExcelDocumentByAuditorium(fileName: String, date: Int): Resource {
        val book = XSSFWorkbook()
        val sheet = book.createSheet("Examen")
        var row = sheet.createRow(0)
        var name = row.createCell(0)
        name.setCellValue("Аудитория")

        var j=1
        for (it in date..31){
            name = row.createCell(j++)
            name.setCellValue("$it")
        }

        val auditoriums = auditoriumRepo.findAll();
        auditoriums.sortedBy { it.name }
        var i = 1
        for (auditorium in auditoriums) {
            row = sheet.createRow(i)
            name = row.createCell(0)
            name.setCellValue(auditorium.name)
            for (subj in auditorium.sessionSubjects) {
                if (subj.date != -1) {
                    name = row.createCell(subj.date - date + 1)
                    name.setCellValue(subj.subject?.name +"(" +subj.lector?.name?.substringBefore(" ")+ ")")
                }
            }
                    ++i
                    row = sheet.createRow(i)
        }
        for (it in 0..33) {
            sheet.autoSizeColumn(it)

        }
        val file = File("/$fileName")
        val outFile = FileOutputStream(file)
        book.write(outFile as OutputStream?)
        book.close()
        val resource = UrlResource(file.toURI())
        return resource
    }

    @Throws(Exception::class)
    fun buildExcelDocumentTimetable(fileName: String, date: Int): Resource {
        val book = XSSFWorkbook()
        val sheet = book.createSheet("Examen")
        var row = sheet.createRow(0)
        var name = row.createCell(0)
        name.setCellValue("Группа")
        name = row.createCell(1)
        name.setCellValue("Дисциплина")
        name = row.createCell(2)
        name.setCellValue("Аудитория")
        name = row.createCell(3)
        name.setCellValue("Экзаминатор")

        var j=4
        for (it in date..31){
            name = row.createCell(j++)
            name.setCellValue("$it")
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
        for (studentsGroup in groups) {
            row = sheet.createRow(i)
            name = row.createCell(0)
            name.setCellValue(studentsGroup.name)
            for (subj in studentsGroup.sessionSubjects) {
                if (subj.date != -1) {
                    name = row.createCell(1)
                    name.setCellValue(subj.subject?.name)
                    name = row.createCell(2)
                    name.setCellValue(subj.auditorium?.name)
                    name = row.createCell(3)
                    name.setCellValue(subj.lector?.name)
                    name = row.createCell(subj.date - date + 3)

                    val  backgroundStyle = book.createCellStyle()
                    val colorMap = book.stylesSource.indexedColors // в рот ваше казино
                    backgroundStyle.setFillForegroundColor(XSSFColor(Color.RED, colorMap));
                    backgroundStyle.fillPattern = FillPatternType.SOLID_FOREGROUND;
                    name.cellStyle = backgroundStyle
//                    name.setCellValue("+")
                    ++i
                    row = sheet.createRow(i)
                }
            }
        }
        for (it in 0..40) {
            sheet.autoSizeColumn(it)
        }

        val file = File("/$fileName")
        val outFile = FileOutputStream(file)
        book.write(outFile as OutputStream?)
        book.close()
        val resource = UrlResource(file.toURI())
        return resource
    }
}