package dev.umbra.core.impl.content;

public final class ValidationError {
    private final String path;
    private final String message;
    private final int lineNumber;

    public ValidationError(String path, String message, int lineNumber) {
        this.path = path;
        this.message = message;
        this.lineNumber = lineNumber;
    }

    public String getPath() {
        return path;
    }

    public String getMessage() {
        return message;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    @Override
    public String toString() {
        return (lineNumber > 0 ? "Line " + lineNumber + ": " : "") + "[" + path + "] " + message;
    }
}
