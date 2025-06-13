package net.fullstack.class101clone.type;

public enum FileType {
    IMAGE("image"),
    VIDEO("video");

    private final String value;

    FileType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static FileType getFileType(String value) {
        for (FileType type : FileType.values()) {
            if (type.getValue().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown file type: " + value);
    }
}
