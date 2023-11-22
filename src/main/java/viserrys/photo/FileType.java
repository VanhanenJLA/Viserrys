package viserrys.photo;

import java.util.EnumSet;

public enum FileType {
    IMAGE_JPEG("image/jpeg"),
    IMAGE_PNG("image/png");

    private final String mimeType;

    FileType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getMimeType() {
        return mimeType;
    }

    private static final EnumSet<FileType> supportedFileTypes = EnumSet.allOf(FileType.class);

    public static void ensureSupportedFileType(String type) throws Exception {
        if (supportedFileTypes.stream().noneMatch(ft -> ft.getMimeType().equals(type))) {
            throw new Exception("Unsupported file type: " + type);
        }
    }
    
}