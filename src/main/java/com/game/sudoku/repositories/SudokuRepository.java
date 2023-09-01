package com.game.sudoku.repositories;

import org.springframework.data.repository.CrudRepository;

import com.game.sudoku.entities.Sudoku;

public interface SudokuRepository extends CrudRepository<Sudoku, Integer> {
    
}
