package Email;

import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Content;
import config.ConfigReader;

import java.time.LocalTime;
import java.io.IOException;

/**
 * Utility class for sending email notifications after test execution using the SendGrid API.
 * Email credentials and configuration are read from a properties file via {@link ConfigReader}.
 */
public class SendEmail {

    /**
     * Captures the current time when the email is triggered, for logging purposes.
     */
    static LocalTime CurrentTime = LocalTime.now();

    /**
     * Sends an email with a brief message after test execution if enabled.
     *
     * <p>Required properties in the configuration:
     * <ul>
     *   <li><b>api_key</b>: Your SendGrid API key (must be valid)</li>
     *   <li><b>from_email</b>: Sender's email address (must be verified in SendGrid)</li>
     *   <li><b>to_email</b>: Receiver's email address</li>
     *   <li><b>subject_of_email</b>: Subject line prefix</li>
     *   <li><b>send_email</b>: Flag to control sending (true/false)</li>
     * </ul>
     *
     * @param sendorNot If true, the email will be sent; if false, email sending is skipped.
     */
    public static void sendEmailWithReport(boolean sendorNot) {
        String apiKey = ConfigReader.getKey("api_key");
        SendGrid sg = new SendGrid(apiKey);
        Request request = new Request();

        try {
            if (sendorNot) {
                Email from = new Email(ConfigReader.getKey("from_email")); // Must be verified in SendGrid
                String subject = ConfigReader.getKey("subject_of_email") + " " + CurrentTime;
                Email to = new Email(ConfigReader.getKey("to_email"));
                Content content = new Content("text/html", "Test execution completed. Please check the attached report.");

                Mail mail = new Mail(from, subject, to, content);

                request.setMethod(Method.POST);
                request.setEndpoint("mail/send");
                request.setBody(mail.build());

                Response response = sg.api(request);
                System.out.println(response.getStatusCode() + " Email sent successfully at " + CurrentTime);
            } else {
                System.out.println("No email will be sent");
            }
        } catch (IOException e) {
            System.out.println("Error while sending email: " + e.getMessage());
        }
    }
}
