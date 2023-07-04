package com.example.sautinews;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.Stack;

public class UndoManager {

    private final EditText editText;
    private final Stack<UndoOperation> undoStack;
    private final Stack<UndoOperation> redoStack;

    public UndoManager(EditText editText) {
        this.editText = editText;
        this.undoStack = new Stack<>();
        this.redoStack = new Stack<>();
    }

    public void onTextChanged(int start, int before, int count) {
        // Save the operation to the undo stack
        undoStack.push(new UndoOperation(start, before, editText.getText().toString()));
        // Clear the redo stack as new changes are made
        redoStack.clear();
    }

    public boolean canUndo() {
        return !undoStack.isEmpty();
    }

    public boolean canRedo() {
        return !redoStack.isEmpty();
    }

    public void undo() {
        if (canUndo()) {
            UndoOperation undoOperation = undoStack.pop();
            redoStack.push(new UndoOperation(undoOperation.start, undoOperation.text.length(), editText.getText().toString()));
            editText.setText(undoOperation.text);
            editText.setSelection(undoOperation.start);
        }
    }

    public void redo() {
        if (canRedo()) {
            UndoOperation redoOperation = redoStack.pop();
            undoStack.push(new UndoOperation(redoOperation.start, redoOperation.text.length(), editText.getText().toString()));
            editText.setText(redoOperation.text);
            editText.setSelection(redoOperation.start);
        }
    }

    private static class UndoOperation {
        private final int start;
        private final int length;
        private final String text;

        public UndoOperation(int start, int length, String text) {
            this.start = start;
            this.length = length;
            this.text = text;
        }
    }
}

