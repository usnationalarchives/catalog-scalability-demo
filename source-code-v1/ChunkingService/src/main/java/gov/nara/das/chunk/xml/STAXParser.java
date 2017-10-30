package gov.nara.das.chunk.xml;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.UUID;

import javax.xml.namespace.NamespaceContext;
import javax.xml.stream.*;
import javax.xml.stream.events.*;
import javax.xml.stream.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.namespace.QName;

/**
 * @brief STAX based parser
 *
 * @author Matthew Mariano
 *
 *         ticket date contributor comment 2017-08-30 Matthew Mariano created
 *
 */
public class STAXParser implements IParser {
	private final File file;
	private final XMLEventReader reader;
	private XMLEventWriter writer;
	private ByteArrayOutputStream bout = new ByteArrayOutputStream();
	private XMLEvent currentEvent;
	private final MessageProcessor processor;
	private final long jobId;
	private  String chunkingId;
	private int total = 0;
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	private String itemName;
	private int messageIndex;
	public STAXParser(File afile, MessageProcessor aprocessor, long aJobId)
			throws FileNotFoundException, XMLStreamException {
		file = afile;
		XMLInputFactory factory = XMLInputFactory.newInstance();
		reader = factory.createXMLEventReader(new FileReader(file));
		processor = aprocessor;
		jobId = aJobId;

	}

	public void processXML() throws XMLStreamException, IOException {
		try{
		while (reader.hasNext()) {
			currentEvent = reader.nextEvent();
			int i = currentEvent.getEventType();
			if (i == XMLEvent.START_ELEMENT) {
				// String name=reader.getLocalName();
				String name = currentEvent.asStartElement().getName().getLocalPart();
				if (name.endsWith("Array")) {
					processItems(name);
					// count++;
					break;
				}
			}
		}
		}catch(XMLStreamException| IOException e){
			throw e;
		}finally{
			processor.cleanup(jobId);
		}
	}

	private void processItems(String arrayName) throws XMLStreamException, IOException {
	    itemName = arrayName.replace("Array", "");
		int tagCount = 0;
		//
		boolean foundStart = false;
		while (reader.hasNext()) {
			if (tagCount > 2) {
				// break;
			}
			currentEvent = reader.nextEvent();
			output();
			// int i=reader.getEventType();
			int i = currentEvent.getEventType();
			boolean matchItemName=false;
			boolean startElement=i == XMLEvent.START_ELEMENT;
			boolean endElement=i == XMLEvent.END_ELEMENT;
			if(startElement ){
				matchItemName=itemName.equals(currentEvent.asStartElement().getName().getLocalPart());
			}else if(endElement){
				matchItemName=itemName.equals(currentEvent.asEndElement().getName().getLocalPart());
			}
			if (startElement && foundStart==false && matchItemName) {
				log.debug("2. found start element for jobId=" + jobId);
				foundStart = true;
				bout = new ByteArrayOutputStream();
				XMLOutputFactory factory = XMLOutputFactory.newInstance();
				writer = factory.createXMLEventWriter(bout);
				//
				createAndWriteStartElement(writer, "http://ui.das.nara.gov/", "import");
				createAndWriteStartElement(writer, "", itemName + "Array");
				writer.add(currentEvent);
				createAndWriteElement(writer, "", "jobId", jobId);
				UUID u=UUID.randomUUID();

				// it would be better to have Chunker create this id
				chunkingId = System.currentTimeMillis()+"-"+total+"-"+u.toString();
				createAndWriteElement(writer, "", "chunkingId", chunkingId);
				// create a holding place for sqs message id
				createAndWriteElement(writer, "", "sqsMessageId", "");
				createAndWriteElement(writer, "", "messageIndex", "" + total);
				createAndWriteElement(writer, "", "dasItemType", "" + matchItemName);
				writer.flush();
				bout.flush();
				//log.debug("bout=" + bout.toString());
				tagCount++;
			} else if (endElement && foundStart && matchItemName) {
				foundStart = false;
				writer.add(currentEvent);
				createAndWriteEndElement(writer, itemName + "Array");
				createAndWriteEndElement(writer, "import");
				writer.close();
				processItem();
			} else {
				if (foundStart) {
					writer.add(currentEvent);
				}
			}
		}
	}

	private <T> void createAndWriteElement(XMLEventWriter w, String nameSpaceURI, String tagName, T value)
			throws XMLStreamException {
		XMLEventFactory f = XMLEventFactory.newInstance();
		if (nameSpaceURI == null) {
			nameSpaceURI = "";
		}
		XMLEvent e = f.createStartElement("", nameSpaceURI, tagName);
		w.add(e);
		e = f.createCharacters("" + value);
		writer.add(e);
		e = f.createEndElement("", "", tagName);
		writer.add(e);
	}

	private void createAndWriteStartElement(XMLEventWriter w, String nameSpaceURI, String tagName)
			throws XMLStreamException {
		XMLEventFactory f = XMLEventFactory.newInstance();
		if (nameSpaceURI == null) {
			nameSpaceURI = "";
		}
		XMLEvent e = f.createStartElement("", nameSpaceURI, tagName);
		w.add(e);
		if (!"".equals(nameSpaceURI)) {
			XMLEvent ns = f.createNamespace("http://ui.das.nara.gov/");
			w.add(ns);
		}
	}

	private void createAndWriteEndElement(XMLEventWriter w, String tagName) throws XMLStreamException {
		XMLEventFactory f = XMLEventFactory.newInstance();
		XMLEvent e = f.createEndElement("", "", tagName);
		w.add(e);
	}

	/**
	 *
	 */
	private void output() {

	}

	private void processItem() {
		if (processor != null) {
			total++;
			processor.processMessage(this,bout.toString(),jobId,messageIndex, itemName);
		}
	}

	public static void println(Object... a) {
		for (int i = 0; i < a.length; i++) {
			System.out.println(a[i]);
		}
	}

	public int getTotal() {
		return total;
	}

	@Override
	public long getJobId() {
		return jobId;
	}

	@Override
	public File getFile() {
		return null;
	}
	@Override
	public String getCurrentMessageId(){
		return chunkingId;
	}
}
