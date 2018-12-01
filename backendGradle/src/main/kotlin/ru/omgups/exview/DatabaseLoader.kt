package ru.omgups.exview

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import ru.omgups.exview.model.*
import ru.omgups.exview.repo.*

fun Lector.addSubject(subj: Subject) {
    subjects.add(subj)
    subj.lectors.add(this)
}

fun Subject.addLector(lector: Lector) {
    lectors.add(lector)
    lector.subjects.add(this)
}

fun Lector.setCathedra(cathedra: Cathedra) {
    this.cathedra = cathedra
    cathedra.lectors.add(this)
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
    lateinit var facultetRepo: FacultetRepo
	
	@Autowired
    lateinit var studentsGroupRepo: StudentsGroupRepo
	
	@Autowired
    lateinit var auditoriumRepo: AuditoriumRepo

    @Autowired
    lateinit var sessionSubjectGroupRepo: SessionSubjectRepo

	
    override fun run(vararg args: String?) {
        val cath1 = Cathedra("АиСУ")
        val cath2 = Cathedra("Автоматика и телемеханика")
        val cath3 = Cathedra("Безопастность жихнедейтельности и экология")
        val cath4 = Cathedra("Вагоны и вагонное хозяйство")
        val cath5 = Cathedra("Высшая математика")
        val cath6 = Cathedra("Информатика, прикладная математика и механика")
        cathedraRepo.saveAll(arrayListOf(cath1,cath2,cath3,cath4,cath5,cath6))

        var subj1 = Subject("Математика")
        var subj2 = Subject("Геометрия")
        var subj3 = Subject("Физика")
        var subj4 = Subject("Философия")
        var subj5 = Subject("Информатика")
        var subj6 = Subject("Программирование")
        var subj7 = Subject("Физкультура")


        var lector1 = Lector("Иванов Иван Иванович")  //13
        var lector2 = Lector("Степаов Степан Степанович")  //14

        var lector3 = Lector("Михалков Михаил Михоилович")  //15
        var lector4 = Lector("Алексанров Александр Александрович")  //16
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
        //lector4.setCathedra(cath1)


        subjectRepo.saveAll(arrayListOf(subj1, subj2, subj3, subj4, subj5, subj6, subj7))
        lectorRepo.saveAll(arrayListOf(lector1, lector2, lector3, lector4))


        val group1 = StudentsGroup("11a")
		studentsGroupRepo.save(group1)
		studentsGroupRepo.save(StudentsGroup("11b"))
		studentsGroupRepo.save(StudentsGroup("11c"))
		studentsGroupRepo.save(StudentsGroup("11d"))
		studentsGroupRepo.save(StudentsGroup("11e"))
		studentsGroupRepo.save(StudentsGroup("11f"))
		studentsGroupRepo.save(StudentsGroup("11g"))

        var l1 =  SessionSubject()
        l1.lector = lector1
        l1.subject = subj1
        l1.studentGroup = group1

        sessionSubjectGroupRepo.save(l1)
	}
}