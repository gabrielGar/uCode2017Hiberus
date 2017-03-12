package com.ubeacon.rts.ubeacon;

import android.util.Log;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by Samuel on 11/03/2017.
 */

public class BeaconDataHotel {
    public String uuid;
    public String lang;
    public String htmlContent;
    public String notificationContent;
    public String description;
    public String beaconName;
    public boolean notificado = false;

    /**
     * Read the information asociated with a beacon UUID from the local resources.
     * @param is
     */
    public BeaconDataHotel(InputStream is){
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = null;
        try {
            dBuilder = dbFactory.newDocumentBuilder();

            Document doc = dBuilder.parse(is);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("BeaconDataHotel");
            Node nNode = nList.item(0);
            Element eElement = (Element) nNode;
            this.beaconName = eElement.getElementsByTagName("beaconName").item(0).getTextContent();
            this.description = eElement.getElementsByTagName("description").item(0).getTextContent();
            this.htmlContent = eElement.getElementsByTagName("htmlcontent").item(0).getTextContent();
            this.lang = eElement.getElementsByTagName("lang").item(0).getTextContent();
            this.notificationContent = eElement.getElementsByTagName("notificationContent").item(0).getTextContent();


        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public BeaconDataHotel(){}

    /**
     * Use an api rest for obtaining the information asociated with the beacon uuid.
     * @param uuid
     */
    public BeaconDataHotel(String uuid){
        IdentificadorBeacon ib = new IdentificadorBeacon(uuid);
        if(ib.security_type == IdentificadorBeacon.HOTEL_GUEST){
            //Si es un invitado puede hacer peticiones a la api rest tranquilamente
        }else{
            //Hace falta que el usuario este registrado en la app
        }
    }
}
