package contract.pos;

import au.com.dius.pact.provider.junit5.HttpTestTarget;
import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.State;
import au.com.dius.pact.provider.junitsupport.loader.PactFolder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;

@Provider("user-provider")
@PactFolder("target/pacts")
public class UserProviderPactTest {

    @BeforeEach
    void setUp(PactVerificationContext context) {

        if (context != null) {
            context.setTarget(
                    new HttpTestTarget(
                            "localhost",
                            8080,
                            "/"
                    )
            );
        }
    }

    @TestTemplate
    @ExtendWith(PactVerificationInvocationContextProvider.class)
    void verifyPact(PactVerificationContext context) {
        context.verifyInteraction();
    }

    @State("User 1 exists")
    void userExists() {
        System.out.println("Setting up User 1");
    }

    @State("User 2 exists")
    void user2Exists() {
        System.out.println("Provider state setup for User 2");
    }

    @State("User can be created")
    void createUser() {
        System.out.println("Setting up Create User");
    }

    @State("User 99 does not exist")
    void userNotExist(){
        System.out.println("Provider state setup for invalid user");
    }
}