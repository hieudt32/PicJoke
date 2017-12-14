package funia.maker.pip.picjoke.scopic.service;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.concurrent.TimeoutException;


public class Service {
    private static final String TAG = "Service: ";

    public Service() {
    }

    public String getDataFromService(JSONObject request, String url) {
        HttpURLConnection connection = null;
        String strRequest = request.toString();
        try {
            URL link = new URL(url);
            connection = (HttpURLConnection) link.openConnection();
            connection.setReadTimeout(30000);
            connection.setConnectTimeout(30000);
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestMethod("POST");
            connection.connect();
            //Send request
            OutputStream wr = new BufferedOutputStream(connection.getOutputStream());
            wr.write(strRequest.getBytes());
            wr.flush();
            wr.close();
            InputStream is = connection.getInputStream();
//            String userid = connection.getHeaderField("userid");
//            String tokenkey = connection.getHeaderField("tokenkey");
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String strResponse = ReadAll(rd);
            is.close();
            return strResponse;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return null;
    }

    public String readJsonFromUrl(String url) {
        InputStream is = null;
        try {
            is = new URL(url).openStream();
            BufferedReader rd = new BufferedReader
                    (new InputStreamReader(is, Charset.forName("UTF-8")));
            return ReadAll(rd);

        } catch (ConnectException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private String ReadAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }


}
