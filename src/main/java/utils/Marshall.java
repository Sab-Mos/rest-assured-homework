package util;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.soap.MessageFactory;
import jakarta.xml.soap.SOAPMessage;
import jakarta.xml.soap.SOAPPart;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class Marshall {
    public static <T> String marshallSoapRequest(T object) {
        try {
            MessageFactory mf = MessageFactory.newInstance();
            SOAPMessage msg = mf.createMessage();
            SOAPPart part = msg.getSOAPPart();

            JAXBContext ctx = JAXBContext.newInstance(object.getClass());
            Marshaller m = ctx.createMarshaller();
            m.marshal(object, new DOMResult(part.getEnvelope().getBody()));

            Transformer tr = TransformerFactory.newInstance().newTransformer();
            Properties p = new Properties();
            p.setProperty("indent","yes");
            p.setProperty("omit-xml-declaration","yes");
            tr.setOutputProperties(p);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            tr.transform(msg.getSOAPPart().getContent(), new StreamResult(out));
            return out.toString(StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
