package org.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.stream.*;
import javax.xml.stream.events.XMLEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.StringWriter;

public class XmlStream {

    private static Logger logger = LoggerFactory.getLogger(XmlStream.class);

    public static void main(String[] args) throws Exception {
        File source = new File("");
        String markTrade = "{http://schemas.xmlsoap.org/soap/envelope/}trade";
        String removeLegs = "{http://schemas.xmlsoap.org/soap/envelope/}legs";
        String splitLegs = "{http://schemas.xmlsoap.org/soap/envelope/}leg";

        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
        InputStream in = new FileInputStream(source);
        XMLEventReader reader = inputFactory.createXMLEventReader(in);
        XMLEvent event;


        // Buffer to hold the trade without legs
        StringWriter tradeBuffer = new StringWriter();
        XMLEventWriter tradeWriter = outputFactory.createXMLEventWriter(tradeBuffer);
        // This is the tracker for the trade start and stop tags
        boolean deleteSection = false;
        boolean shouldStart = false;
        // Buffer to hold individual legs
        StringWriter legBuffer = new StringWriter();
        XMLEventWriter legWriter = outputFactory.createXMLEventWriter(legBuffer);
        // This is the tracker for the leg start and stop tags
        boolean trimSection = false;
        int totalLegsProcessed = 0;
        logger.info("Starting the parser");
        while (reader.hasNext()) {
            event = reader.nextEvent();

            if (event.getEventType() == XMLStreamConstants.START_ELEMENT && event.asStartElement().getName().toString().equalsIgnoreCase(markTrade)) {
                shouldStart = true;

            } else if (event.getEventType() == XMLStreamConstants.END_ELEMENT && (event.asEndElement().getName().toString().equalsIgnoreCase(markTrade))) {
                shouldStart = false;
                tradeWriter.add(event);
            }

            if (event.getEventType() == XMLStreamConstants.START_ELEMENT && event.asStartElement().getName().toString().equalsIgnoreCase(removeLegs)) {
                deleteSection = true;
                continue;
            } else if (event.getEventType() == XMLStreamConstants.END_ELEMENT && (event.asEndElement().getName().toString().equalsIgnoreCase(removeLegs))) {
                deleteSection = false;
                continue;
            } else if (deleteSection) {
                trimSection = false;
                if (event.getEventType() == XMLStreamConstants.START_ELEMENT && event.asStartElement().getName().toString().equalsIgnoreCase(splitLegs)) {
                    trimSection = true;
                    legWriter.add(event);
                } else if (event.getEventType() == XMLStreamConstants.END_ELEMENT && (event.asEndElement().getName().toString().equalsIgnoreCase(splitLegs))) {
                    trimSection = false;
                    legWriter.add(event);
                    legWriter.flush();
                    legWriter.close();
                    sendLeg(legBuffer.toString());
                    totalLegsProcessed++;
                    legBuffer.close();
                    legBuffer = new StringWriter();
                    legWriter = outputFactory.createXMLEventWriter(legBuffer);
                } else if (trimSection) {
                    continue;
                } else {
                    legWriter.add(event);
                }
                continue;
            } else {
                if(shouldStart)
                    tradeWriter.add(event);
            }
        }

        sendTrade(tradeBuffer.toString());

        logger.info("Total legs processed {}",totalLegsProcessed);
    }

    public static void sendLeg(String leg) {
       logger.info("Sending this leg {}",leg);
    }

    public static void sendTrade(String trade) {
        logger.info("Sending this trade {}",trade);
    }
}