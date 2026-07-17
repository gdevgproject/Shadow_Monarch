package dev.umbra.core.impl.content;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class JsonLocationTracker {
    private static final Pattern LINE_PATTERN = Pattern.compile("line (\\d+)");

    public static Map<String, Integer> trackLines(String jsonContent) throws IOException {
        Map<String, Integer> pathLines = new HashMap<>();
        try (JsonReader reader = new JsonReader(new StringReader(jsonContent))) {
            reader.setLenient(true);
            track(reader, "$", pathLines);
        }
        return pathLines;
    }

    private static void track(JsonReader reader, String path, Map<String, Integer> pathLines) throws IOException {
        JsonToken token = reader.peek();
        int line = getLineNumber(reader);
        pathLines.put(path, line);

        switch (token) {
            case BEGIN_OBJECT:
                reader.beginObject();
                while (reader.hasNext()) {
                    String name = reader.nextName();
                    track(reader, path + "." + name, pathLines);
                }
                reader.endObject();
                break;
            case BEGIN_ARRAY:
                reader.beginArray();
                int index = 0;
                while (reader.hasNext()) {
                    track(reader, path + "[" + index + "]", pathLines);
                    index++;
                }
                reader.endArray();
                break;
            default:
                reader.skipValue();
                break;
        }
    }

    private static int getLineNumber(JsonReader reader) {
        String str = reader.toString();
        Matcher matcher = LINE_PATTERN.matcher(str);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }
        return -1;
    }
}
