package edu.stanford.hiv.analysis;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

public class StanfordXMLInterpreter {

	private BufferedWriter bufferedWriter;
	private static HashMap<String, List<String>> drugClassToDrugs = new HashMap<String, List<String>>();
	private static List<String> drugs = new ArrayList<String>();
	static {
		List<String> PRDrugs = new ArrayList<String>();
		PRDrugs.add("IDV/r");
		PRDrugs.add("SQV/r");
		PRDrugs.add("NFV");
		PRDrugs.add("FPV/r");
		PRDrugs.add("LPV/r");
		PRDrugs.add("ATV/r");
		PRDrugs.add("TPV/r");
		PRDrugs.add("DRV/r");
		List<String> RTDrugs = new ArrayList<String>();
		// NRTI
		RTDrugs.add("AZT");
		RTDrugs.add("DDI");
		RTDrugs.add("D4T");
		RTDrugs.add("3TC");
		RTDrugs.add("ABC");
		RTDrugs.add("FTC");
		RTDrugs.add("TDF");
		// NNRTI
		RTDrugs.add("NVP");
		RTDrugs.add("EFV");
		RTDrugs.add("ETR");
		RTDrugs.add("RPV");
		List<String> INIDrugs = new ArrayList<String>();
		INIDrugs.add("RAL");
		INIDrugs.add("EVG");
		INIDrugs.add("DTG");
		List<String> EIDrugs = new ArrayList<String>();
		EIDrugs.add("T20");
		drugClassToDrugs.put("PR", PRDrugs);
		drugClassToDrugs.put("RT", RTDrugs);
		drugClassToDrugs.put("IN", INIDrugs);
		drugClassToDrugs.put("EI", EIDrugs);
		
		drugs.add("IDV/r");
		drugs.add("SQV/r");
		drugs.add("NFV");
		drugs.add("FPV/r");
		drugs.add("LPV/r");
		drugs.add("ATV/r");
		drugs.add("TPV/r");
		drugs.add("DRV/r");
		drugs.add("AZT");
		drugs.add("DDI");
		drugs.add("D4T");
		drugs.add("3TC");
		drugs.add("ABC");
		drugs.add("FTC");
		drugs.add("TDF");
		drugs.add("NVP");
		drugs.add("EFV");
		drugs.add("ETR");
		drugs.add("RPV");
		drugs.add("RAL");
		drugs.add("EVG");
		drugs.add("DTG");
		drugs.add("T20");
		Collections.sort(drugs);
	}
	
	
	public StanfordXMLInterpreter(Reader xmlInput, File csvOutput) {
		SAXBuilder builder = new SAXBuilder();
		try {
			this.bufferedWriter = new BufferedWriter(new FileWriter(csvOutput));
			Document document = builder.build(xmlInput);
			Element root = document.getRootElement();
			if(root.getChild("success") != null) {
				initiateCsvFile();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void initiateCsvFile() throws IOException {
		System.out.print("id,");
		bufferedWriter.write("id,");
		for (String drug : drugs) {
			System.out.print(drug + ",");
			bufferedWriter.write(drug + ",");
			System.out.print(drug + "_level,");
			bufferedWriter.write(drug + "_level,");
			System.out.print(drug + "_score,");
			bufferedWriter.write(drug + "_score,");
		}
		System.out.print("\n");
		bufferedWriter.write("\n");
		bufferedWriter.flush();
	}

//	public void parseDrugs(Reader xml, String id) throws JDOMException, IOException {
//		SAXBuilder builder = new SAXBuilder();
//		Document document = builder.build(xml);
//		Element root = document.getRootElement();
//		bufferedWriter.write(id + ",");
//		if(root.getChild("failure") == null) {
//			List<Element> drugsClasses = root.getChild("success").getChild("summary").getChildren();
//			List<String> availableDrugs = new ArrayList<String>();
//			for(Element drugClass : drugsClasses) {
////				System.out.println("Present? " + drugClass.getChild("present").getValue());
//				if(Boolean.getBoolean(drugClass.getChild("present").getValue()))
//					availableDrugs.addAll(drugClassToDrugs.get(drugClass.getName()));
//			}
//			List<Element> drugs = root.getChild("success").getChild("drugScores").getChildren();
//			for (Element drug : drugs) {
//				if(availableDrugs.contains(drug.getAttribute("code").getValue())) {
//					System.out.println(drug.getAttribute("code").getValue());
//				}
////				System.out.print(drug.getAttribute("levelSIR").getValue() + ",");
////				bufferedWriter.write(drug.getAttribute("levelSIR").getValue() + ",");
////				System.out.print(drug.getAttribute("levelStanford").getValue() + ",");
////				bufferedWriter.write(drug.getAttribute("levelStanford").getValue() + ",");
////				System.out.print(drug.getAttribute("score").getValue() + ",");
////				bufferedWriter.write(drug.getAttribute("score").getValue() + ",");
//			}
//		} else {
//			bufferedWriter.write(root.getChild("failure").getChild("errorMessage").getValue());
//			System.out.print(root.getChild("failure").getChild("errorMessage").getValue());
//		}
//		System.out.print("\n");
//		bufferedWriter.write("\n");
//		bufferedWriter.flush();
//	}
	
	public void parseDrugs(Reader xml) throws JDOMException, IOException {
		SAXBuilder builder = new SAXBuilder();
		Document document = builder.build(xml);
		Element root = document.getRootElement();
		List<Element> sequences = root.getChildren("success");
		List<Element> sequencesFailures = root.getChildren("failure");
		for (Element sequence : sequences) {
			System.out.print(sequence.getChild("sequence").getAttribute("name").getValue().replaceAll("\\s+", "") + ",");
			bufferedWriter.write(sequence.getChild("sequence").getAttribute("name").getValue().replaceAll("\\s+", "") + ",");
			List<Element> drugsClasses = root.getChild("success").getChild("summary").getChildren();
			List<String> availableDrugs = new ArrayList<String>();
			for(Element drugClass : drugsClasses) {
				if(Boolean.parseBoolean(drugClass.getChild("present").getValue()))
					availableDrugs.addAll(drugClassToDrugs.get(drugClass.getName()));
			}
			List<Element> drugElements = sequence.getChild("drugScores").getChildren();
			for (String drug : drugs) {
				if(availableDrugs.contains(drug)) {
					boolean found = false;
					for(Element elementDrug:drugElements) {
						if(elementDrug.getAttribute("code").getValue().equals(drug)) {
//							System.out.println(drugElements.drug.getAttribute("code").getValue());
//							System.out.print(elementDrug.getAttribute("levelSIR").getValue() + ",");
							bufferedWriter.write(elementDrug.getAttribute("levelSIR").getValue() + ",");
//							System.out.print(elementDrug.getAttribute("levelStanford").getValue() + ",");
							bufferedWriter.write(elementDrug.getAttribute("levelStanford").getValue() + ",");
//							System.out.print(elementDrug.getAttribute("score").getValue() + ",");
							bufferedWriter.write(elementDrug.getAttribute("score").getValue() + ",");
							found = true;
						}
						
					}
					if(!found)
						System.out.print("ERROR: DRUG: " + drug);
				} else {
//					System.out.print("-,-,-,");
					bufferedWriter.write("-,-,-,");
				}
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
