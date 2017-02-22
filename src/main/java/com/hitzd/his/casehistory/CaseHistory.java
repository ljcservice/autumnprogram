package com.hitzd.his.casehistory;

import com.hitzd.his.casehistory.beans.*;

public class CaseHistory 
{
	private String PatientID;
	private String VisitID;
	private PatMasterIndex patIndex;
	
	public PatMasterIndex getPatIndex() {
		return patIndex;
	}
	public void setPatIndex(PatMasterIndex patIndex) {
		this.patIndex = patIndex;
	}
	public String getPatientID() {
		return PatientID;
	}
	public void setPatientID(String patientID) {
		PatientID = patientID;
	}
	public String getVisitID() {
		return VisitID;
	}
	public void setVisitID(String visitID) {
		VisitID = visitID;
	}
}
