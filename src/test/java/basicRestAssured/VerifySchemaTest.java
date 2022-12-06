package basicRestAssured;

import com.github.fge.jsonschema.SchemaVersion;
import com.github.fge.jsonschema.cfg.ValidationConfiguration;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class VerifySchemaTest {

    /*
    * given() --> configuration: headers/ params/ auth/ body
    * when() --> method: url
    * then() --> response: headers/ body/ response code/ message code/ etc
    *           verification
    *           extract
    * log
    * */

    @Test
    public void verifyCreateProject() {
        JSONObject body = new JSONObject();
        body.put("Content", "Bootcamp");

        JsonSchemaFactory schemaFactory = JsonSchemaFactory.newBuilder()
                .setValidationConfiguration(ValidationConfiguration.newBuilder()
                        .setDefaultVersion(SchemaVersion.DRAFTV4).freeze()
                ).freeze();

        Response response = given()
                .auth()
                .preemptive()
                .basic("bootAPI@bootAPI.com","12345")
                .body(body.toString())
                .log().all()
        .when()
                .post("https://todo.ly/api/projects.json");

        response.then()
                .log().all()
                .statusCode(200)
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("createVerificationSchema.json").using(schemaFactory))
                .body("Content", equalTo("Bootcamp"));


    }
}
