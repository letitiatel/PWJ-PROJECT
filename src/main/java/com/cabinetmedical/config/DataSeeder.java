package com.cabinetmedical.config;

import com.cabinetmedical.model.Doctor;
import com.cabinetmedical.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final DoctorRepository doctorRepository;

    @Override
    public void run(String... args) {
        List<Doctor> doctors = List.of(
                // ---- Cardiologie ----
                doctor("Popescu", "Ana", "Cardiologie", "0721000001", "ana.popescu@medicare.ro", 15, 9.4),
                doctor("Ionescu", "Mihai", "Cardiologie", "0721000002", "mihai.ionescu@medicare.ro", 22, 9.7),
                doctor("Stanescu", "Elena", "Cardiologie", "0721000003", "elena.stanescu@medicare.ro", 8, 8.9),
                doctor("Dumitrescu", "Radu", "Cardiologie", "0721000004", "radu.dumitrescu@medicare.ro", 12, 9.1),

                // ---- Dermatologie ----
                doctor("Marin", "Cristina", "Dermatologie", "0721000005", "cristina.marin@medicare.ro", 10, 9.2),
                doctor("Georgescu", "Andrei", "Dermatologie", "0721000006", "andrei.georgescu@medicare.ro", 6, 8.5),
                doctor("Vasilescu", "Maria", "Dermatologie", "0721000007", "maria.vasilescu@medicare.ro", 18, 9.6),
                doctor("Tudor", "Alexandru", "Dermatologie", "0721000008", "alexandru.tudor@medicare.ro", 4, 8.2),

                // ---- Pediatrie ----
                doctor("Stoica", "Ioana", "Pediatrie", "0721000009", "ioana.stoica@medicare.ro", 14, 9.5),
                doctor("Pavel", "Cătălin", "Pediatrie", "0721000010", "catalin.pavel@medicare.ro", 9, 9.0),
                doctor("Munteanu", "Diana", "Pediatrie", "0721000011", "diana.munteanu@medicare.ro", 20, 9.8),
                doctor("Dragomir", "Bogdan", "Pediatrie", "0721000012", "bogdan.dragomir@medicare.ro", 7, 8.7),

                // ---- Neurologie ----
                doctor("Constantinescu", "Ștefan", "Neurologie", "0721000013", "stefan.constantinescu@medicare.ro", 25, 9.9),
                doctor("Radu", "Laura", "Neurologie", "0721000014", "laura.radu@medicare.ro", 11, 9.3),
                doctor("Florescu", "Tudor", "Neurologie", "0721000015", "tudor.florescu@medicare.ro", 16, 9.4),
                doctor("Manole", "Simona", "Neurologie", "0721000016", "simona.manole@medicare.ro", 5, 8.4),

                // ---- Ortopedie ----
                doctor("Pop", "Vlad", "Ortopedie", "0721000017", "vlad.pop@medicare.ro", 13, 9.1),
                doctor("Lupu", "Adriana", "Ortopedie", "0721000018", "adriana.lupu@medicare.ro", 19, 9.5),
                doctor("Barbu", "Cosmin", "Ortopedie", "0721000019", "cosmin.barbu@medicare.ro", 8, 8.8),
                doctor("Ardelean", "Roxana", "Ortopedie", "0721000020", "roxana.ardelean@medicare.ro", 17, 9.6)
        );

        List<Doctor> toInsert = doctors.stream()
                .filter(d -> !doctorRepository.existsByEmail(d.getEmail()))
                .toList();

        if (toInsert.isEmpty()) {
            log.info("DataSeeder: toate cele {} medici seed există deja — nimic de inserat.",
                    doctors.size());
            return;
        }

        doctorRepository.saveAll(toInsert);
        log.info("DataSeeder: am inserat {} medici noi (din {} candidați).",
                toInsert.size(), doctors.size());
    }

    private static Doctor doctor(String nume, String prenume, String specializare,
                                 String telefon, String email,
                                 Integer aniExperienta, Double rating) {
        Doctor d = new Doctor();
        d.setNume(nume);
        d.setPrenume(prenume);
        d.setSpecializare(specializare);
        d.setTelefon(telefon);
        d.setEmail(email);
        d.setAniExperienta(aniExperienta);
        d.setRating(rating);
        return d;
    }
}
