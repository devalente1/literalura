package com.alura.literalura.repository;

import com.alura.literalura.model.Autor;
import com.alura.literalura.model.Idioma;
import com.alura.literalura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface IRepositorio extends JpaRepository<Autor, Long> {
    @Query("SELECT l FROM Libro l JOIN l.autor a WHERE l.titulo LIKE %:titulo%")
    Optional<Libro> buscarLibroBD(@Param("titulo") String titulo);

    @Query("SELECT a FROM Libro l JOIN l.autor a WHERE a.nombre LIKE %:nombre%")
    Optional<Autor> buscarAutorPorNombre(@Param("nombre") String nombre);

    @Query("SELECT l FROM Autor a JOIN a.libros l ORDER BY l.titulo")
    List<Libro> librosGuardadosBD();

    @Query("SELECT a FROM Autor a ORDER BY a.nombre")
    List<Autor> autoresGuardadosBD();

    @Query("""
    SELECT a
    FROM Autor a
    WHERE (a.fechaDeNacimiento IS NULL OR a.fechaDeNacimiento <= :fecha)
      AND (a.fechaDeMuerte IS NULL OR a.fechaDeMuerte > :fecha)
    ORDER BY a.nombre
""")
    List<Autor> AutoresVivosBD(@Param("fecha") Integer fecha);

    //List<Libro> findByIdiomaOrderByTituloAsc(Idioma idioma);
    @Query("SELECT l FROM Libro l WHERE l.idioma = :idioma ORDER BY l.titulo")
    List<Libro> librosPorIdiomaBD(@Param("idioma") Idioma idioma);


}
