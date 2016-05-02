package edu.stanford.hiv.analysis;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

public class StanfordXMLInterpreter {

	private BufferedWriter bufferedWriter;

	public StanfordXMLInterpreter(Reader xmlInput, File csvOutput) {
		SAXBuilder builder = new SAXBuilder();
		try {
			this.bufferedWriter = new BufferedWriter(new FileWriter(csvOutput));
			Document document = builder.build(xmlInput);
			Element root = document.getRootElement();
			if(root.getChild("success") != null) {
				List<Element> drugs = root.getChild("success").getChild("drugScores").getChildren();
				initiateCsvFile(drugs);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void initiateCsvFile(List<Element> drugs) throws IOException {
		System.out.print("id,");
		bufferedWriter.write("id,");
		for (Element drug : drugs) {
			System.out.print(drug.getAttribute("code").getValue() + ",");
			bufferedWriter.write(drug.getAttribute("code").getValue() + ",");
			System.out.print(drug.getAttribute("code").getValue() + "_level,");
			bufferedWriter.write(drug.getAttribute("code").getValue() + "_level,");
			System.out.print(drug.getAttribute("code").getValue() + "_score,");
			bufferedWriter.write(drug.getAttribute("code").getValue() + "_score,");
		}
		System.out.print("\n");
		bufferedWriter.write("\n");
		bufferedWriter.flush();
	}

	public void parseDrugs(Reader xml, String id) throws JDOMException, IOException {
		SAXBuilder builder = new SAXBuilder();
		Document document = builder.build(xml);
		Element root = document.getRootElement();
		bufferedWriter.write(id + ",");
		if(root.getChild("failure") == null) {
			List<Element> drugs = root.getChild("success").getChild("drugScores").getChildren();
			for (Element drug : drugs) {
				System.out.print(drug.getAttribute("levelSIR").getValue() + ",");
				bufferedWriter.write(drug.getAttribute("levelSIR").getValue() + ",");
				System.out.print(drug.getAttribute("levelStanford").getValue() + ",");
				bufferedWriter.write(drug.getAttribute("levelStanford").getValue() + ",");
				System.out.print(drug.getAttribute("score").getValue() + ",");
				bufferedWriter.write(drug.getAttribute("score").getValue() + ",");
			}
		} else {
			bufferedWriter.write(root.getChild("failure").getChild("errorMessage").getValue());
			System.out.print(root.getChild("failure").getChild("errorMessage").getValue());
		}
		System.out.print("\n");
		bufferedWriter.write("\n");
		bufferedWriter.flush();
	}
	
	public void parseDrugs(Reader xml) throws JDOMException, IOException {
		SAXBuilder builder = new SAXBuilder();
		Document document = builder.build(xml);
		Element root = document.getRootElement();
		List<Element> sequences = root.getChildren("success");
		List<Element> sequencesFailures = root.getChildren("failure");
		for (Element sequence : sequences) {
			bufferedWriter.write(sequence.getChild("sequence").getAttribute("name").getValue().replaceAll("\\s+", "") + ",");
			List<Element> drugs = sequence.getChild("drugScores").getChildren();
			for (Element drug : drugs) {
				System.out.print(drug.getAttribute("levelSIR").getValue() + ",");
				bufferedWriter.write(drug.getAttribute("levelSIR").getValue() + ",");
				System.out.print(drug.getAttribute("levelStanford").getValue() + ",");
				bufferedWriter.write(drug.getAttribute("levelStanford").getValue() + ",");
				System.out.print(drug.getAttribute("score").getValue() + ",");
				bufferedWriter.write(drug.getAttribute("score").getValue() + ",");
			}
			System.out.print("\n");
			bufferedWriter.write("\n");
			bufferedWriter.flush();
		}
		for (Element failure : sequencesFailures) {
			bufferedWriter.write(failure.getChild("sequence").getAttribute("name").getValue().replaceAll("\\s+", "") + ",");
			bufferedWriter.write(failure.getChild("errorMessage").getValue());
			System.out.print(failure.getChild("errorMessage").getValue());
			System.out.print("\n");
			bufferedWriter.write("\n");
			bufferedWriter.flush();
		}
	}
	
	public void end() throws IOException {
		bufferedWriter.flush();
		bufferedWriter.close();
	}

//	public static void main(String[] args) {
//		File file = new File("/Users/ewout/Documents/Rega_Algorithm/output.xml");
//		StanfordXMLInterpreter stanfordXMLInterpreter = new StanfordXMLInterpreter();
//		stanfordXMLInterpreter.parseXML(file);
//	}
}
