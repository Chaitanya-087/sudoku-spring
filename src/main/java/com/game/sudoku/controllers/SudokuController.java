package com.game.sudoku.controllers;

import java.net.http.HttpRequest;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.game.sudoku.entities.Sudoku;
import com.game.sudoku.repositories.SudokuRepository;
import com.game.sudoku.utilities.Validator;

import jakarta.annotation.PostConstruct;

@Controller
@RequestMapping("/sudoku")
public class SudokuController {

    @Value("${server.port}")
    private int port;

    @Autowired
    private Validator validator;

    @Autowired
    private SudokuRepository sudokuRepository;

    @Autowired
    private Sudoku sudoku;

    @PostConstruct
    public void init() {
        String boardText = " ".repeat(81);
        sudoku.setBoardText(boardText);
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("isPlay", false);
        model.addAttribute("board", sudoku.getBoardText().toCharArray());
        model.addAttribute("type", "create");
        return "sudoku";
    }

    @PostMapping("/check")
    public String createSubmit(@RequestParam Map<String, String> paramMap, Model model, RedirectAttributes attr) {
        // String boardText = paramMap.values().stream().collect(Collectors.joining());
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 81; i++) {
            String val = paramMap.get(String.valueOf(i));
            sb.append(val);
        }
        String boardText = sb.toString();

        if (validator.isValidConfig(boardText)) {
            sudoku.setBoardText(boardText);
            sudokuRepository.save(sudoku);
            attr.addFlashAttribute("link", String.format("http://localhost:%d/sudoku/%d", port, sudoku.getId()));
        } else {
            attr.addFlashAttribute("error", "not a valid board");
        }
        return "redirect:/sudoku/create";
    }

    @GetMapping("/{id}")
    public String play(@PathVariable("id") int id, Model model) {
        Sudoku sudoku = sudokuRepository.findById(id).get();
        model.addAttribute("id", sudoku.getId());
        model.addAttribute("isPlay", true);
        model.addAttribute("board", sudoku.getBoardText().toCharArray());
        model.addAttribute("type", "play");
        return "sudoku";
    }

    @PostMapping("/valid/{id}")
    public String valid(@RequestParam Map<String, String> paramMap, @PathVariable int id, RedirectAttributes attr) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 81; i++) {
            String val = paramMap.get(String.valueOf(i));
            sb.append(val);
        }
        String boardText = sb.toString();
        System.out.println(boardText);
        if (!validator.isValidConfig(boardText)) {
            attr.addFlashAttribute("error", "not a valid board");
        } else {
            
            sudoku.setBoardText(boardText);
            sudokuRepository.save(sudoku);
        }
        return String.format("redirect:/sudoku/%d", id);
    }
}
