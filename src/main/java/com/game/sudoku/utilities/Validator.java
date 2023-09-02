package com.game.sudoku.utilities;

import java.util.HashSet;

import org.springframework.stereotype.Component;

@Component
public class Validator {
    public boolean notInRow(char[][] board, int row) {
        HashSet<Character> temp = new HashSet<>();

        for (int i = 0; i < 9; i++) {
            if (temp.contains(board[row][i])) return false;

            if (board[row][i] != '.') {
                temp.add(board[row][i]);
            }
        }

        return true;
    }

    public boolean notInCol(char[][] board, int col) {
        HashSet<Character> temp =  new HashSet<>();

        for (int i = 0; i < 9; i++) {
            if (temp.contains(board[i][col])) return false;

            if (board[i][col] != '.') {
                temp.add(board[i][col]);
            }
        }

        return true;
    }

    public boolean notInBox(char[][] board, int row, int col) {
        HashSet<Integer> temp = new HashSet<>();

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int curr = board[i + row][j + col];
                
                if (temp.contains(curr)) return false;

                if (curr != '.') temp.add(curr);
            }
        }
        return true;
    }

    public boolean isValid(char[][] board, int row, int col) {
        return notInCol(board, col) && notInRow(board, row) && notInBox(board, row - row % 3, col - col % 3);
    }

    public boolean isValidConfig(String board) {
        char[][] temp = new char[9][9];
        for (int i = 0; i < 81; i++) {
            temp[i / 9][i % 9] = board.charAt(i);
        }
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (!isValid(temp, i, j)) return false;
            }
        }
        return true;
    }
}


