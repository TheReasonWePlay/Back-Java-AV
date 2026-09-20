package com.javaAV.coivoiturage.soap;

import java.io.ByteArrayOutputStream;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.WebServiceMessage;

public class SoapLoggingInterceptor implements ClientInterceptor {

    @Override
    public boolean handleRequest(MessageContext messageContext) {

        System.out.println("\n========== XML SOAP ENVOYE ==========");

        afficherXml(messageContext.getRequest());

        System.out.println("=====================================\n");

        return true;
    }

    @Override
    public boolean handleResponse(MessageContext messageContext) {

        System.out.println("\n========== XML SOAP RECU ==========");

        afficherXml(messageContext.getResponse());

        System.out.println("===================================\n");

        return true;
    }

    @Override
    public boolean handleFault(MessageContext messageContext) {

        System.out.println("\n========== ERREUR SOAP ==========");

        afficherXml(messageContext.getResponse());

        System.out.println("=================================\n");

        return true;
    }

    @Override
    public void afterCompletion(
            MessageContext messageContext,
            Exception exception) {
    }

    private void afficherXml(WebServiceMessage message) {

        try {

            ByteArrayOutputStream outputStream =
                    new ByteArrayOutputStream();

            message.writeTo(outputStream);

            String xml = outputStream.toString("UTF-8");

            TransformerFactory transformerFactory =
                    TransformerFactory.newInstance();

            Transformer transformer =
                    transformerFactory.newTransformer();

            transformer.setOutputProperty(
                    OutputKeys.INDENT, "yes");

            transformer.setOutputProperty(
                    "{http://xml.apache.org/xslt}indent-amount",
                    "4");

            StreamSource source =
                    new StreamSource(
                            new java.io.StringReader(xml));

            StreamResult result =
                    new StreamResult(System.out);

            transformer.transform(source, result);

            System.out.println();

        } catch (Exception e) {

            System.out.println(
                    "Erreur formatage XML : "
                    + e.getMessage());

        }
    }
}