package woodle.backend.test.auth;

import org.apache.http.*;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import woodle.backend.auth.AuthServlet;
import woodle.backend.controller.MemberRepository;
import woodle.backend.entity.Principle;
import woodle.backend.entity.Role;
import woodle.backend.model.Member;
import woodle.backend.test.rest.RestClientTest;
import woodle.backend.util.Resources;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(Arquillian.class)
@RunAsClient
public class ServletTest {
    @ArquillianResource
    URL deploymentUrl;

    @Deployment(testable = false)
    public static WebArchive getTestArchive() {
        WebArchive webArchive = ShrinkWrap.create(WebArchive.class, "servlet-test.war")
                .addClasses(AuthServlet.class, Principle.class, Role.class, Member.class, MemberRepository.class, Resources.class)
                .addAsWebInfResource(new File(RestClientTest.SRC_MAIN_WEBAPP_WEB_INF + "/web.xml"))
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsResource("import.sql")
                .addAsWebInfResource("META-INF/persistence.xml", "classes/META-INF/persistence.xml")
                .addAsWebInfResource(new File(RestClientTest.SRC_MAIN_WEBAPP_WEB_INF + "/faces-config.xml"))
                .addAsWebInfResource("jboss-deployment-structure-test.xml", "jboss-deployment-structure.xml")
                .addAsWebInfResource(new File(RestClientTest.SRC_MAIN_WEBAPP_WEB_INF + "/jboss-web.xml"));
        System.out.println(webArchive.toString(true));
        return webArchive;

    }

    @Test
    public void shouldBeAbleToInvokeServletInDeployedWebApp() throws Exception {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        String requestUrl = deploymentUrl + AuthServlet.URL_PATTERN.substring(1); //+ "?" + AuthServlet.USER_PARAM + "=hello";
        // We should now login with the user name and password
        HttpPost httpost = new HttpPost(requestUrl);

        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair(AuthServlet.USER_PARAM, "ralf.sigmund@gmail.com"));
        nvps.add(new BasicNameValuePair("password", "secret"));

        httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));

        HttpResponse response = httpclient.execute(httpost);
        HttpEntity entity = response.getEntity();
        if (entity != null)
            entity.consumeContent();

        StatusLine statusLine = response.getStatusLine();

        assertEquals("Verify that the servlet was deployed and returns expected result", 302, statusLine.getStatusCode());
    }

    protected void makeCall(String user, String pass, int expectedStatusCode, String url) throws Exception {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        try {
            HttpGet httpget = new HttpGet(url);

            HttpResponse response = httpclient.execute(httpget);

            HttpEntity entity = response.getEntity();
            if (entity != null)
                entity.consumeContent();

            // We should get the Login Page
            StatusLine statusLine = response.getStatusLine();
            System.out.println("Login form get: " + statusLine);
            assertEquals(200, statusLine.getStatusCode());

            System.out.println("Initial set of cookies:");
            List<org.apache.http.cookie.Cookie> cookies = httpclient.getCookieStore().getCookies();
            if (cookies.isEmpty()) {
                System.out.println("None");
            } else {
                for (int i = 0; i < cookies.size(); i++) {
                    System.out.println("- " + cookies.get(i).toString());
                }
            }

            // We should now login with the user name and password
            HttpPost httpost = new HttpPost(url + "/j_security_check");

            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(new BasicNameValuePair("j_username", user));
            nvps.add(new BasicNameValuePair("j_password", pass));

            httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));

            response = httpclient.execute(httpost);
            entity = response.getEntity();
            if (entity != null)
                entity.consumeContent();

            statusLine = response.getStatusLine();

            // Post authentication - we have a 302
            assertEquals(302, statusLine.getStatusCode());
            Header locationHeader = response.getFirstHeader("Location");
            String location = locationHeader.getValue();

            HttpGet httpGet = new HttpGet(location);
            response = httpclient.execute(httpGet);

            entity = response.getEntity();
            if (entity != null)
                entity.consumeContent();

            System.out.println("Post logon cookies:");
            cookies = httpclient.getCookieStore().getCookies();
            if (cookies.isEmpty()) {
                System.out.println("None");
            } else {
                for (int i = 0; i < cookies.size(); i++) {
                    System.out.println("- " + cookies.get(i).toString());
                }
            }

            // Either the authentication passed or failed based on the expected status code
            statusLine = response.getStatusLine();
            assertEquals(expectedStatusCode, statusLine.getStatusCode());
        } finally {
            // When HttpClient instance is no longer needed,
            // shut down the connection manager to ensure
            // immediate deallocation of all system resources
            httpclient.getConnectionManager().shutdown();
        }
    }
}
