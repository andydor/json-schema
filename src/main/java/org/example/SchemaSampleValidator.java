package org.example;

import com.nimbusds.jose.util.JSONObjectUtils;
import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

public class SchemaSampleValidator {
    private static final String inputSchemaFilename = "src/main/resources/schema.json";
    private static final String inputSampleFilename = "src/main/resources/input.json";

    private static String readFile(String fileName) throws IOException {
        return Files.readString(Path.of(fileName));
    }

    private static Map<String, Object> loadSchema() throws ParseException {
        try {
            String schemaFileContent = Files.readString(Path.of(inputSchemaFilename));
            return JSONObjectUtils.parse(schemaFileContent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void validate() throws IOException, ParseException {
        String sampleFileContents = readFile(inputSampleFilename);
        JSONObject jsonInput = new JSONObject(sampleFileContents);

        Map<String, Object> schema = loadSchema();
        JSONObject rawSchema = new JSONObject(schema);

        SchemaLoader schemaLoader = SchemaLoader.builder()
                .schemaJson(rawSchema)
                .build();
        Schema jsonSchema = schemaLoader.load().build();
        jsonSchema.validate(jsonInput);
    }
}
