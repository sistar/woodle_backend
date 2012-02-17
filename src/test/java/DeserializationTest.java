import com.opitzconsulting.woodle.AppException;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.type.TypeReference;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class DeserializationTest {


    @Test
    public void testDeserialize() throws Exception {
        String JSON_ARRAY = "[{\"id\":\"json Hacking-2012-02-01T13:10:00.000+01:00\",\"title\":\"json Hacking\",\"location\":\"North Pole\",\"description\":\"icy JSON stuff\",\"startDate\":\"2012-02-01T13:10:00.000+01:00\",\"endDate\":\"2012-02-01T13:10:00.000+01:00\",\"attendance\":[\"santa@claus.no\"],\"maybeAttendance\":[\"rupert@north.pole\"],\"user\":\"santa@claus.no\",\"maxNumber\":1}]";
        List<Map<String, Object>> appointments = parseData(JSON_ARRAY);
        assertThat(appointments.size(), is(equalTo(1)));
        assertThat((String) appointments.get(0).get("id"), is(equalTo("json Hacking-2012-02-01T13:10:00.000+01:00")));
    }

    private List<Map<String, Object>> parseData(String jsonString) throws AppException {
        JsonFactory jsonFactory = new JsonFactory();
        ObjectMapper objectMapper = new ObjectMapper(jsonFactory);
        objectMapper.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);
        objectMapper.getSerializationConfig().set(
                SerializationConfig.Feature.INDENT_OUTPUT, true
        );
        TypeReference<List> typeReference = new TypeReference<List>() {
        };
        List<Map<String, Object>> appointments;
        try {
            appointments = objectMapper.readValue(jsonString, typeReference);
        } catch (Exception e) {
            throw new AppException("Fehler beim parsen", e);
        }
        return appointments;
    }
}
