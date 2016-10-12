package com.eli.calc.shape.reports;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.eli.calc.shape.service.ws.CalcType;
import com.eli.calc.shape.service.ws.CalculationResult;
import com.eli.calc.shape.service.ws.ShapeName;


public class ShapeCalculationsReports {


	public static synchronized String formattedResultsReportSummary(List<CalculationResult> results){

		StringBuilder string = new StringBuilder();
		
		if (null==results || results.isEmpty()) {
			string.append("\t NO RESULTS - NOTHING TO REPORT\n");
			return string.toString();
		}

	
		string.append("=================================================\n");
		string.append("SUMMARY REPORT BY SHAPE:\n");
		string.append("=================================================\n");

		boolean calcedArea = false;
		boolean calcedVolume = false;
		
		Set<ShapeName> shapeNames = new HashSet<ShapeName>();
		for (CalculationResult result : results) { shapeNames.add(result.getShapeName()); }
		
		for (ShapeName shapeName : shapeNames) {

			string.append("\n" + shapeName + "\n");

			double areaSum = 0;
			double volumeSum = 0;
			int areaEntries = 0;
			int volumeEntries = 0;
			
			for (CalculationResult result : results) {

				if (shapeName.equals(result.getShapeName())) {

					if (CalcType.CALC_AREA.equals(result.getCalcType())) {
						areaSum+=result.getResult();
						calcedArea = true;
						areaEntries++;
					}
					if (CalcType.CALC_VOLUME.equals(result.getCalcType())) {
						volumeSum+=result.getResult();
						calcedVolume = true;
						volumeEntries++;
					}
				}
			}

			if (calcedArea)string.append("\t"+CalcType.CALC_AREA + "\n\t\tENTRIES: " + areaEntries + "\tTOTAL: " + areaSum + "\n");
			//else string.append("\t"+CalculationType.CALC_AREA + "\n\t\tNOT REQUESTED\n");

			if (calcedVolume)string.append("\t"+CalcType.CALC_VOLUME + "\n\t\tENTRIES: " + volumeEntries + "\tTOTAL: " + volumeSum + "\n");
			//else string.append("\t"+CalculationType.CALC_VOLUME + "\n\t\tNOT REQUESTED\n");

			calcedArea = false;
			calcedVolume = false;
		}	

		string.append("\n");

	
	
	
	
	
		return string.toString();

	}

	public static synchronized String formattedResultsReportByShapeByDimension(List<CalculationResult> results){
		
		StringBuilder string = new StringBuilder();
		
		if (null==results || results.isEmpty()) {
			string.append("\t NO RESULTS - NOTHING TO REPORT\n");
			return string.toString();
		}

		string.append("=================================================\n");
		string.append("DETAILED REPORT BY SHAPE:\n");
		string.append("=================================================\n");

		StringBuilder byCalcArea = new StringBuilder();
		StringBuilder byCalcVolume = new StringBuilder();
		
		Set<ShapeName> resultShapeNames = new HashSet<ShapeName>();
		for (CalculationResult result : results) { resultShapeNames.add(result.getShapeName()); }
		
		// since this report helper class is now part of a SOAP client, it is possible
		// that the local definition of ShapeName may not contain all of the shape names
		// of the incoming results from the SOAP service..
		//  thus, 'shapeName' might be NULL..
		for (ShapeName shapeName : resultShapeNames) {

			//new code to handle possibility of NULL shape name
			if (null==shapeName) {
				throw new RuntimeException(
						"\n\nNULL ShapeName. It seems local ShapeName list might not match incoming ShapeName(s).\n"
						+"Perhaps this application needs an update (re-build requesting new WSDL.\n\n");
			}

			string.append("\n" + shapeName + "\n");

			for (CalculationResult result : results) {

				if (shapeName.equals(result.getShapeName())) {

					if (CalcType.CALC_AREA.equals(result.getCalcType())) {
						byCalcArea.append("\t\t" + result.getDimension() + "\t" + result.getResult() + "\n");
					}
					if (CalcType.CALC_VOLUME.equals(result.getCalcType())) {
						byCalcVolume.append("\t\t" + result.getDimension() + "\t" + +result.getResult() + "\n");
					}
				}
			}

			if (byCalcArea.length()>0)string.append("\t"+CalcType.CALC_AREA + "\n\t\t" + "INPUT\tRESULT" + "\n" +byCalcArea);
			//else string.append("\t"+CalculationType.CALC_AREA + "\n\t\tNOT REQUESTED\n");

			if (byCalcVolume.length()>0)string.append("\t"+CalcType.CALC_VOLUME + "\n\t\t" + "INPUT\tRESULT" + "\n" +byCalcVolume);
			//else string.append("\t"+CalculationType.CALC_VOLUME + "\n\t\tNOT REQUESTED\n");

			byCalcArea = new StringBuilder();
			byCalcVolume = new StringBuilder();
		}	

		string.append("\n");

		return string.toString();

	}

}
