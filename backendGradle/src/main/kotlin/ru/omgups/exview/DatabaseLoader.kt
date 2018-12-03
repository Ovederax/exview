package ru.omgups.exview

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import ru.omgups.exview.model.*
import ru.omgups.exview.repo.*
import ru.omgups.exview.servises.*

fun Subject.addLector(lector: Lector) {
    lector.addSubject(this)
}

fun Cathedra.addLector(lector: Lector) {
    lector.setCathedra(this)
}



@Component
class DatabaseLoader() : CommandLineRunner {
    @Autowired
    lateinit var subjectRepo: SubjectRepo

    @Autowired
    lateinit var lectorRepo: LectorRepo

    @Autowired
    lateinit var cathedraRepo: CathedraRepo

    @Autowired
    lateinit var studentsGroupRepo: StudentsGroupRepo

    @Autowired
    lateinit var auditoriumRepo: AuditoriumRepo

    @Autowired
    lateinit var sessionSubjectGroupRepo: SessionSubjectRepo


    override fun run(vararg args: String?) {
//        val cath1 = Cathedra("АиСУ")
//        val lector1 = Lector("Иванов Иван Иванович")
//        lectorRepo.saveAll(arrayListOf(lector1))
//        cathedraRepo.saveAll(arrayListOf(cath1))
//        lectorRepo.saveAll(arrayListOf(lector1))
//        cathedraRepo.saveAll(arrayListOf(cath1))


        val cath1 = Cathedra("АиСУ")
        val cath2 = Cathedra("Автоматика и телемеханика")
        val cath3 = Cathedra("Безопастность жихнедейтельности и экология")
        val cath4 = Cathedra("Вагоны и вагонное хозяйство")
        val cath5 = Cathedra("Высшая математика")
        val cath6 = Cathedra("Информатика, прикладная математика и механика")


        val subj1 = Subject("Математика")
        val subj2 = Subject("Геометрия")
        val subj3 = Subject("Физика")
        val subj4 = Subject("Философия")
        val subj5 = Subject("Информатика")
        val subj6 = Subject("Программирование")
        val subj7 = Subject("Физкультура")
        subjectRepo.saveAll(arrayListOf(subj1, subj2, subj3, subj4, subj5, subj6, subj7))

        val lector1 = Lector("Иванов Иван Иванович")  //13
        val lector2 = Lector("Степаов Степан Степанович")  //14
        val lector3 = Lector("Михалков Михаил Михоилович")  //15
        val lector4 = Lector("Алексанров Александр Александрович")  //16
        lectorRepo.saveAll(arrayListOf(lector1, lector2, lector3, lector4))

        subj1.addLector(lector1)
        subj1.addLector(lector2)
        subj2.addLector(lector1)
        subj2.addLector(lector2)
        subj3.addLector(lector1)
        subj3.addLector(lector2)
        subj4.addLector(lector3)
        subj5.addLector(lector4)
        subj6.addLector(lector4)

        lector1.setCathedra(cath5)
        lector2.setCathedra(cath5)
        lector3.setCathedra(cath1)

        val group1 = StudentsGroup("11a")
        studentsGroupRepo.save(group1)
        studentsGroupRepo.save(StudentsGroup("11b"))
        studentsGroupRepo.save(StudentsGroup("11c"))
        studentsGroupRepo.save(StudentsGroup("11d"))
        studentsGroupRepo.save(StudentsGroup("11e"))
        studentsGroupRepo.save(StudentsGroup("11f"))
        studentsGroupRepo.save(StudentsGroup("11g"))

        val l1 = SessionSubject()
        sessionSubjectGroupRepo.save(l1)

        l1.setLector(lector1)
        l1.setStudentGroup(group1)
        l1.setSubject(subj1)

        cathedraRepo.saveAll(arrayListOf(cath1, cath2, cath3, cath4, cath5, cath6))
        lectorRepo.saveAll(arrayListOf(lector1, lector2, lector3, lector4))
        subjectRepo.saveAll(arrayListOf(subj1, subj2, subj3, subj4, subj5, subj6, subj7))
        sessionSubjectGroupRepo.save(l1)
    }
}