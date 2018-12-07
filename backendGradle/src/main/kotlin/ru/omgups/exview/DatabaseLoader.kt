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
fun Cathedra.addAuditorium(auditorium: Auditorium) {
    auditoriums.add(auditorium)
    auditorium.cathedra = this
}



fun Cathedra.addSubject(subj: Subject) {
    subjects.add(subj)
}

fun Lector.addSubject(subj: Subject) {
    subjects.add(subj)
    subj.lectors.add(this)
}

fun Auditorium.addSessionSubjects(sessionSubjects: SessionSubject) {
    this.sessionSubjects.add(sessionSubjects)
    sessionSubjects.auditorium = this
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
        val cath3 = Cathedra("Безопастность жизнедейтельности и экология")
        val cath4 = Cathedra("Вагоны и вагонное хозяйство")
        val cath5 = Cathedra("Высшая математика")
        val cath6 = Cathedra("Информатика, прикладная математика и механика")
        cathedraRepo.saveAll(arrayListOf(cath1, cath2, cath3, cath4, cath5, cath6))


        val lector1 = Lector("Сапковский Иван Иванович")
        val lector2 = Lector("Степановский Степан Степанович")
        val lector3 = Lector("Михалковский Михаил Михоилович")
        val lector4 = Lector("Годунов Борис Всеволодович")
        val lector5 = Lector("Алексанров Александр Александрович")
        val lector6 = Lector("Венский Сергей Дмитриевич")
        val lector7 = Lector("Чебоксаровский Леонид Вячеславович")
        lectorRepo.saveAll(arrayListOf(lector1, lector2, lector3, lector4, lector5, lector6, lector7))

        val subj1 = Subject("Математика")
        val subj2 = Subject("Геометрия")
        val subj3 = Subject("Физика")
        val subj4 = Subject("Философия")
        val subj5 = Subject("Информатика")
        val subj6 = Subject("Программирование")
        val subj7 = Subject("Физкультура")
        val subj8 = Subject("Компьютерные сети")
        val subj9 = Subject("Схемотехника")
        val subj10 = Subject("КТОП ЭВМ")
        subjectRepo.saveAll(arrayListOf(subj1, subj2, subj3, subj4, subj5, subj6, subj7, subj8, subj9, subj10))

        cath1.addSubject(subj5)
        cath1.addSubject(subj6)
        cath1.addSubject(subj8)
//        cath2.addSubject()

        subj1.addLector(lector1)
        subj1.addLector(lector2)
        subj2.addLector(lector1)
        subj2.addLector(lector2)
        subj3.addLector(lector1)
        subj3.addLector(lector2)
        subj4.addLector(lector3)
        subj5.addLector(lector4)
        subj6.addLector(lector4)
        subj7.addLector(lector5)
        subj8.addLector(lector6)
        subj9.addLector(lector7)
        subj10.addLector(lector7)

        lector3.setCathedra(cath1)
        lector5.setCathedra(cath2)
        lector6.setCathedra(cath3)
        lector4.setCathedra(cath4)
        lector1.setCathedra(cath5)
        lector2.setCathedra(cath5)
        lector7.setCathedra(cath6)

        val group1 = StudentsGroup("11a")
        val group2 = StudentsGroup("11b")
        val group3 = StudentsGroup("11c")
        val group4 = StudentsGroup("12a")
        val group5 = StudentsGroup("12b")
        val group6 = StudentsGroup("12c")
        val group7 = StudentsGroup("13a")
        val group8 = StudentsGroup("13b")
        val group9 = StudentsGroup("13c")
        val group10 = StudentsGroup("14a")
        val group11 = StudentsGroup("14b")
        val group12 = StudentsGroup("14c")
        val group13 = StudentsGroup("15a")
        val group14 = StudentsGroup("15b")
        val group15 = StudentsGroup("15c")
        studentsGroupRepo.saveAll(arrayListOf(group1, group2, group3, group4, group5, group6, group7, group8, group9, group10, group11, group12, group13, group14, group15))

        val sessionSubject1 = SessionSubject()
        val sessionSubject2 = SessionSubject()
        val sessionSubject3 = SessionSubject()
        val sessionSubject4 = SessionSubject()
        val sessionSubject5 = SessionSubject()
        val sessionSubject6 = SessionSubject()
        sessionSubjectGroupRepo.saveAll(arrayListOf(sessionSubject1, sessionSubject2, sessionSubject3, sessionSubject4, sessionSubject5, sessionSubject6))

        sessionSubject1.setLector(lector1)
        sessionSubject1.setStudentGroup(group1)
        sessionSubject1.setSubject(subj1)

        sessionSubject2.setLector(lector2)
        sessionSubject2.setStudentGroup(group2)
        sessionSubject2.setSubject(subj1)

        sessionSubject3.setLector(lector3)
        sessionSubject3.setStudentGroup(group3)
        sessionSubject3.setSubject(subj4)

        sessionSubject4.setLector(lector4)
        sessionSubject4.setStudentGroup(group4)
        sessionSubject4.setSubject(subj5)

        sessionSubject5.setLector(lector5)
        sessionSubject5.setStudentGroup(group5)
        sessionSubject5.setSubject(subj7)

        sessionSubject6.setLector(lector6)
        sessionSubject6.setStudentGroup(group6)
        sessionSubject6.setSubject(subj8)
        sessionSubjectGroupRepo.saveAll(arrayListOf(sessionSubject1, sessionSubject2, sessionSubject3, sessionSubject4, sessionSubject5, sessionSubject6))

        val auditorium1 = Auditorium("1-301")
        val auditorium2 = Auditorium("1-302")
        val auditorium3 = Auditorium("1-303")
        val auditorium4 = Auditorium("1-304")
        val auditorium5 = Auditorium("1-305")
        val auditorium6 = Auditorium("1-306")
        val auditorium7 = Auditorium("1-307")
        val auditorium8 = Auditorium("1-308")
        auditoriumRepo.saveAll(arrayListOf(auditorium1, auditorium2, auditorium3, auditorium4, auditorium5, auditorium6, auditorium7, auditorium8))

        /*cathedraRepo.saveAll(arrayListOf(cath1, cath2, cath3, cath4, cath5, cath6))
        cath1.addAuditorium(auditorium1)
        cath1.addAuditorium(auditorium2)
        cath1.addAuditorium(auditorium3)
        cath2.addAuditorium(auditorium1)
        cath2.addAuditorium(auditorium2)
        cath2.addAuditorium(auditorium3)
        auditoriumRepo.saveAll(arrayListOf(auditorium1, auditorium2, auditorium3))*/

        auditorium1.addSessionSubjects(sessionSubject1)
        auditorium2.addSessionSubjects(sessionSubject2)
        auditorium3.addSessionSubjects(sessionSubject3)
        auditorium4.addSessionSubjects(sessionSubject4)
        auditorium5.addSessionSubjects(sessionSubject5)
        auditorium6.addSessionSubjects(sessionSubject6)

        cathedraRepo.saveAll(arrayListOf(cath1, cath2, cath3, cath4, cath5, cath6))
        lectorRepo.saveAll(arrayListOf(lector1, lector2, lector3, lector4, lector5, lector6, lector7))
        subjectRepo.saveAll(arrayListOf(subj1, subj2, subj3, subj4, subj5, subj6, subj7, subj8, subj9, subj10))
        sessionSubjectGroupRepo.saveAll(arrayListOf(sessionSubject1, sessionSubject2, sessionSubject3, sessionSubject4, sessionSubject5, sessionSubject6))
        studentsGroupRepo.saveAll(arrayListOf(group1, group2, group3, group4, group5, group6, group7, group8, group9, group10, group11, group12, group13, group14, group15))
        auditoriumRepo.saveAll(arrayListOf(auditorium1, auditorium2, auditorium3, auditorium4, auditorium5, auditorium6, auditorium7, auditorium8))
//        cathedraRepo.saveAll(arrayListOf(cath1, cath2, cath3, cath4, cath5, cath6))
//        lectorRepo.saveAll(arrayListOf(lector1, lector2, lector3, lector4))
//        subjectRepo.saveAll(arrayListOf(subj1, subj2, subj3, subj4, subj5, subj6, subj7))
//        sessionSubjectGroupRepo.saveAll(arrayListOf(l1,l2,l3))
//        studentsGroupRepo.saveAll(arrayListOf(group1,group2,group3))

    }
}