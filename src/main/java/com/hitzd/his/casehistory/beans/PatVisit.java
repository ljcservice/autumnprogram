package com.hitzd.his.casehistory.beans;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.hitzd.his.casehistory.beans.exam.ExamMaster;
import com.hitzd.his.casehistory.beans.germtest.GermTest;
import com.hitzd.his.casehistory.beans.germtest.GermTestResult;
import com.hitzd.his.casehistory.beans.labtest.LabTestMaster;
import com.hitzd.his.casehistory.beans.operation.Operation;
import com.hitzd.his.casehistory.beans.operation.OperationMaster;
import com.hitzd.his.casehistory.beans.operation.OperationName;

/**
 * PatVisit generated by MyEclipse Persistence Tools
 */

public class PatVisit implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -4890409247745862978L;

	private String patientId;

	private Long visitId;

	private String deptAdmissionTo;

	private Date admissionDateTime;

	private String deptDischargeFrom;

	private Date dischargeDateTime;

	private String occupation;

	private String maritalStatus;

	private String identity;

	private String armedServices;

	private String duty;

	private String unitInContract;

	private String chargeType;

	private Long workingStatus;

	private String insuranceType;

	private String insuranceNo;

	private String serviceAgency;

	private String mailingAddress;

	private String zipCode;

	private String nextOfKin;

	private String relationship;

	private String nextOfKinAddr;

	private String nextOfKinZipcode;

	private String nextOfKinPhone;

	private String patientClass;

	private String admissionCause;

	private Date consultingDate;

	private String patAdmCondition;

	private String consultingDoctor;

	private String admittedBy;

	private Long emerTreatTimes;

	private Long escEmerTimes;

	private Long seriousCondDays;

	private Long criticalCondDays;

	private Long icuDays;

	private Long ccuDays;

	private Long specLevelNursDays;

	private Long firstLevelNursDays;

	private Long secondLevelNursDays;

	private Long autopsyIndicator;

	private String bloodType;

	private String bloodTypeRh;

	private Long bloodTranTimes;

	private Long bloodTranVol;

	private Long bloodTranReactTimes;

	private Long decubitalUlcerTimes;

	private String alergyDrugs;

	private String adverseReactionDrugs;

	private String mrValue;

	private String mrQuality;

	private Long followIndicator;

	private Long followInterval;

	private String followIntervalUnits;

	private String director;

	private String attendingDoctor;

	private String doctorInCharge;

	private String dischargeDisposition;

	private Double totalCosts;

	private Double totalPayments;

	private Date catalogDate;

	private String cataloger;

	private String catalogWriter;

	private Long infusionReactTimes;

	private String topUnit;

	private Long serviceSystemIndicator;

	private String healthLevel;

	private String mrInfectReport;

	private Long infectIndicator;

	private Double bodyWeight;

	private Double bodyHeight;

	private String internalNo;

	private String identityClass;

	private Long hbsagIndicator;

	private Long hcvAbIndicator;

	private Long hivAbIndicator;

	private String chiefDoctor;

	private String advancedStudiesDoctor;

	private String practiceDoctorOfGraduate;

	private String practiceDoctor;

	private String doctorOfControlQuality;

	private String nurseOfControlQuality;

	private Date dateOfControlQuality;

	private Long firstCaseIndicator;

	private Long thirdLevelNursDays;

	private String XExamNo;

	private String medicalPayWay;

	private Long firstAidIndicator;

	private Long trainingInjuryIndicator;

	private String inDeptName;

	private Long inClinicAttr;

	private Long inOutpOrInp;

	private Long inInternalOrSergery;

	private String outDeptName;

	private Long outClinicAttr;

	private Long outOutpOrInp;

	private Long outInternalOrSergery;

	private Long settleIndi;

	private Date linkDate;
	
	// 诊断列表
	private List<Diagnosis> diagnosis = new ArrayList<Diagnosis>();
	public int getDiagnosisCount()
	{
		return diagnosis.size();
	}
	public Diagnosis getDiagnosisByIndex(int index)
	{
		return diagnosis.get(index);
	}
	public void addDiagnosis(Diagnosis value)
	{
		diagnosis.add(value);
	}
	public void removeDiagnosisByIndex(int index)
	{
		diagnosis.remove(index);
	}
	
	// 摆药记录
	private List<DrugDispenseRec> drugDispenseRecs = new ArrayList<DrugDispenseRec>();
	public int getDrugDispenseRecCount()
	{
		return drugDispenseRecs.size();
	}
	public DrugDispenseRec getDrugDispenseRecByIndex(int index)
	{
		return drugDispenseRecs.get(index);
	}
	public void addDrugDispenseRec(DrugDispenseRec value)
	{
		drugDispenseRecs.add(value);
	}
	public void removeDrugDispenseRecByIndex(int index)
	{
		drugDispenseRecs.remove(index);
	}
	// 住院费用明细
	private List<InpBillDetail> inpBillDetails = new ArrayList<InpBillDetail>();
	public int getInpBillDetailCount()
	{
		return inpBillDetails.size();
	}
	public InpBillDetail getInpBillDetailByIndex(int index)
	{
		return inpBillDetails.get(index);
	}
	public void addInpBillDetail(InpBillDetail value)
	{
		inpBillDetails.add(value);
	}
	public void removeInpBillDetailByIndex(int index)
	{
		inpBillDetails.remove(index);
	}
	// 手术记录
	private List<Operation> operations = new ArrayList<Operation>();
	public int getOperationCount()
	{
		return operations.size();
	}
	public Operation getOperationByIndex(int index)
	{
		return operations.get(index);
	}
	public void addOperation(Operation value)
	{
		operations.add(value);
	}
	public void removeOperationByIndex(int index)
	{
		operations.remove(index);
	}
	// 手术主记录
	private List<OperationMaster> operationMasters = new ArrayList<OperationMaster>();
	public int getOperationMasterCount()
	{
		return operationMasters.size();
	}
	public OperationMaster getOperationMasterByIndex(int index)
	{
		return operationMasters.get(index);
	}
	public void addDiagnosis(OperationMaster value)
	{
		operationMasters.add(value);
	}
	public void removeOperationMasterByIndex(int index)
	{
		operationMasters.remove(index);
	}
	// 手术名称
	private List<OperationName> operationNames = new ArrayList<OperationName>();
	public int getOperationNameCount()
	{
		return operationNames.size();
	}
	public OperationName getOperationNameByIndex(int index)
	{
		return operationNames.get(index);
	}
	public void addOperationName(OperationName value)
	{
		operationNames.add(value);
	}
	public void removeOperationNameByIndex(int index)
	{
		operationNames.remove(index);
	}
	// 医嘱记录
	private List<Orders> orders = new ArrayList<Orders>();
	public int getOrdersCount()
	{
		return orders.size();
	}
	public Orders getOrdersByIndex(int index)
	{
		return orders.get(index);
	}
	public void addOrders(Orders value)
	{
		orders.add(value);
	}
	public void removeOrdersByIndex(int index)
	{
		orders.remove(index);
	}
	// 体征记录
	private List<VitalSignsRec> vitalSignsRecs = new ArrayList<VitalSignsRec>();
	public int getVitalSignsRecCount()
	{
		return vitalSignsRecs.size();
	}
	public VitalSignsRec getVitalSignsRecByIndex(int index)
	{
		return vitalSignsRecs.get(index);
	}
	public void addVitalSignsRec(VitalSignsRec value)
	{
		vitalSignsRecs.add(value);
	}
	public void removeVitalSignsRecByIndex(int index)
	{
		vitalSignsRecs.remove(index);
	}
	// 检查记录
	private List<ExamMaster> examMasters = new ArrayList<ExamMaster>();
	public int getExamMasterCount()
	{
		return examMasters.size();
	}
	public ExamMaster getExamMasterByIndex(int index)
	{
		return examMasters.get(index);
	}
	public void addExamMaster(ExamMaster value)
	{
		examMasters.add(value);
	}
	public void removeExamMasterByIndex(int index)
	{
		examMasters.remove(index);
	}
	// 微生物检验记录
	private List<GermTest> germTests = new ArrayList<GermTest>();
	public int getGermTestCount()
	{
		return germTests.size();
	}
	public GermTest getGermTestByIndex(int index)
	{
		return germTests.get(index);
	}
	public void addGermTest(GermTest value)
	{
		germTests.add(value);
	}
	public void removeGermTestByIndex(int index)
	{
		germTests.remove(index);
	}
	// 检测记录
	private List<LabTestMaster> labTestMaster = new ArrayList<LabTestMaster>();
	public int getLabTestMasterCount()
	{
		return labTestMaster.size();
	}
	public LabTestMaster getLabTestMasterByIndex(int index)
	{
		return labTestMaster.get(index);
	}
	public void addLabTestMaster(LabTestMaster value)
	{
		labTestMaster.add(value);
	}
	public void removeLabTestMasterByIndex(int index)
	{
		labTestMaster.remove(index);
	}
	// 
	private List<GermTestResult> germTestResults = new ArrayList<GermTestResult>();
	public int getGermTestResultCount()
	{
		return germTestResults.size();
	}
	public GermTestResult getGermTestResultByIndex(int index)
	{
		return germTestResults.get(index);
	}
	public void addGermTestResult(GermTestResult value)
	{
		germTestResults.add(value);
	}
	public void removeGermTestResultByIndex(int index)
	{
		germTestResults.remove(index);
	}
	

	public String getAdmission_Cause() {
		return admissionCause;
	}

	public void setAdmission_Cause(String admissionCause) {
		this.admissionCause = admissionCause;
	}

	public Date getAdmission_Date_Time() {
		return admissionDateTime;
	}

	public void setAdmission_Date_Time(Date admissionDateTime) {
		this.admissionDateTime = admissionDateTime;
	}

	public String getAdmitted_By() {
		return admittedBy;
	}

	public void setAdmitted_By(String admittedBy) {
		this.admittedBy = admittedBy;
	}

	public String getAdvanced_Studies_Doctor() {
		return advancedStudiesDoctor;
	}

	public void setAdvanced_Studies_Doctor(String advancedStudiesDoctor) {
		this.advancedStudiesDoctor = advancedStudiesDoctor;
	}

	public String getAdverse_Reaction_Drugs() {
		return adverseReactionDrugs;
	}

	public void setAdverse_Reaction_Drugs(String adverseReactionDrugs) {
		this.adverseReactionDrugs = adverseReactionDrugs;
	}

	public String getAlergy_Drugs() {
		return alergyDrugs;
	}

	public void setAlergy_Drugs(String alergyDrugs) {
		this.alergyDrugs = alergyDrugs;
	}

	public String getArmed_Services() {
		return armedServices;
	}

	public void setArmed_Services(String armedServices) {
		this.armedServices = armedServices;
	}

	public String getAttending_Doctor() {
		return attendingDoctor;
	}

	public void setAttending_Doctor(String attendingDoctor) {
		this.attendingDoctor = attendingDoctor;
	}

	public Long getAutopsy_Indicator() {
		return autopsyIndicator;
	}

	public void setAutopsy_Indicator(Long autopsyIndicator) {
		this.autopsyIndicator = autopsyIndicator;
	}

	public Long getBlood_TranReact_Times() {
		return bloodTranReactTimes;
	}

	public void setBlood_TranReact_Times(Long bloodTranReactTimes) {
		this.bloodTranReactTimes = bloodTranReactTimes;
	}

	public Long getBlood_Tran_Times() {
		return bloodTranTimes;
	}

	public void setBlood_Tran_Times(Long bloodTranTimes) {
		this.bloodTranTimes = bloodTranTimes;
	}

	public Long getBlood_Tran_Vol() {
		return bloodTranVol;
	}

	public void setBlood_Tran_Vol(Long bloodTranVol) {
		this.bloodTranVol = bloodTranVol;
	}

	public String getBlood_Type() {
		return bloodType;
	}

	public void setBlood_Type(String bloodType) {
		this.bloodType = bloodType;
	}

	public String getBlood_Type_Rh() {
		return bloodTypeRh;
	}

	public void setBlood_Type_Rh(String bloodTypeRh) {
		this.bloodTypeRh = bloodTypeRh;
	}

	public Double getBody_Height() {
		return bodyHeight;
	}

	public void setBody_Height(Double bodyHeight) {
		this.bodyHeight = bodyHeight;
	}

	public Double getBody_Weight() {
		return bodyWeight;
	}

	public void setBody_Weight(Double bodyWeight) {
		this.bodyWeight = bodyWeight;
	}

	public Date getCatalog_Date() {
		return catalogDate;
	}

	public void setCatalog_Date(Date catalogDate) {
		this.catalogDate = catalogDate;
	}

	public String getCataloger() {
		return cataloger;
	}

	public void setCataloger(String cataloger) {
		this.cataloger = cataloger;
	}

	public String getCatalog_Writer() {
		return catalogWriter;
	}

	public void setCatalog_Writer(String catalogWriter) {
		this.catalogWriter = catalogWriter;
	}

	public Long getCcu_Days() {
		return ccuDays;
	}

	public void setCcu_Days(Long ccuDays) {
		this.ccuDays = ccuDays;
	}

	public String getCharge_Type() {
		return chargeType;
	}

	public void setCharge_Type(String chargeType) {
		this.chargeType = chargeType;
	}

	public String getChief_Doctor() {
		return chiefDoctor;
	}

	public void setChief_Doctor(String chiefDoctor) {
		this.chiefDoctor = chiefDoctor;
	}

	public Date getConsulting_Date() {
		return consultingDate;
	}

	public void setConsulting_Date(Date consultingDate) {
		this.consultingDate = consultingDate;
	}

	public String getConsulting_Doctor() {
		return consultingDoctor;
	}

	public void setConsulting_Doctor(String consultingDoctor) {
		this.consultingDoctor = consultingDoctor;
	}

	public Long getCritical_Cond_Days() {
		return criticalCondDays;
	}

	public void setCritical_Cond_Days(Long criticalCondDays) {
		this.criticalCondDays = criticalCondDays;
	}

	public Date getDate_Of_Control_Quality() {
		return dateOfControlQuality;
	}

	public void setDate_Of_Control_Quality(Date dateOfControlQuality) {
		this.dateOfControlQuality = dateOfControlQuality;
	}

	public Long getDecubital_Ulcer_Times() {
		return decubitalUlcerTimes;
	}

	public void setDecubital_Ulcer_Times(Long decubitalUlcerTimes) {
		this.decubitalUlcerTimes = decubitalUlcerTimes;
	}

	public String getDept_Admission_To() {
		return deptAdmissionTo;
	}

	public void setDept_Admission_To(String deptAdmissionTo) {
		this.deptAdmissionTo = deptAdmissionTo;
	}

	public String getDept_Discharge_From() {
		return deptDischargeFrom;
	}

	public void setDept_Discharge_From(String deptDischargeFrom) {
		this.deptDischargeFrom = deptDischargeFrom;
	}

	public String getDirector() {
		return director;
	}

	public void setDirector(String director) {
		this.director = director;
	}

	public Date getDischarge_Date_Time() {
		return dischargeDateTime;
	}

	public void setDischarge_Date_Time(Date dischargeDateTime) {
		this.dischargeDateTime = dischargeDateTime;
	}

	public String getDischarge_Disposition() {
		return dischargeDisposition;
	}

	public void setDischarge_Disposition(String dischargeDisposition) {
		this.dischargeDisposition = dischargeDisposition;
	}

	public String getDoctor_In_Charge() {
		return doctorInCharge;
	}

	public void setDoctor_In_Charge(String doctorInCharge) {
		this.doctorInCharge = doctorInCharge;
	}

	public String getDoctor_Of_Control_Quality() {
		return doctorOfControlQuality;
	}

	public void setDoctor_Of_Control_Quality(String doctorOfControlQuality) {
		this.doctorOfControlQuality = doctorOfControlQuality;
	}

	public String getDuty() {
		return duty;
	}

	public void setDuty(String duty) {
		this.duty = duty;
	}

	public Long getEmer_Treat_Times() {
		return emerTreatTimes;
	}

	public void setEmer_Treat_Times(Long emerTreatTimes) {
		this.emerTreatTimes = emerTreatTimes;
	}

	public Long getEsc_Emer_Times() {
		return escEmerTimes;
	}

	public void setEsc_Emer_Times(Long escEmerTimes) {
		this.escEmerTimes = escEmerTimes;
	}

	public Long getFirst_Aid_Indicator() {
		return firstAidIndicator;
	}

	public void setFirst_Aid_Indicator(Long firstAidIndicator) {
		this.firstAidIndicator = firstAidIndicator;
	}

	public Long getFirst_Case_Indicator() {
		return firstCaseIndicator;
	}

	public void setFirst_Case_Indicator(Long firstCaseIndicator) {
		this.firstCaseIndicator = firstCaseIndicator;
	}

	public Long getFirst_Level_Nurs_Days() {
		return firstLevelNursDays;
	}

	public void setFirst_Level_Nurs_Days(Long firstLevelNursDays) {
		this.firstLevelNursDays = firstLevelNursDays;
	}

	public Long getFollow_Indicator() {
		return followIndicator;
	}

	public void setFollow_Indicator(Long followIndicator) {
		this.followIndicator = followIndicator;
	}

	public Long getFollow_Interval() {
		return followInterval;
	}

	public void setFollow_Interval(Long followInterval) {
		this.followInterval = followInterval;
	}

	public String getFollow_Interval_Units() {
		return followIntervalUnits;
	}

	public void setFollow_Interval_Units(String followIntervalUnits) {
		this.followIntervalUnits = followIntervalUnits;
	}

	public Long getHbsag_Indicator() {
		return hbsagIndicator;
	}

	public void setHbsag_Indicator(Long hbsagIndicator) {
		this.hbsagIndicator = hbsagIndicator;
	}

	public Long getHcv_Ab_Indicator() {
		return hcvAbIndicator;
	}

	public void setHcv_Ab_Indicator(Long hcvAbIndicator) {
		this.hcvAbIndicator = hcvAbIndicator;
	}

	public String getHealth_Level() {
		return healthLevel;
	}

	public void setHealth_Level(String healthLevel) {
		this.healthLevel = healthLevel;
	}

	public Long getHiv_Ab_Indicator() {
		return hivAbIndicator;
	}

	public void setHiv_Ab_Indicator(Long hivAbIndicator) {
		this.hivAbIndicator = hivAbIndicator;
	}

	public Long getIcu_Days() {
		return icuDays;
	}

	public void setIcu_Days(Long icuDays) {
		this.icuDays = icuDays;
	}

	public String getIdentity() {
		return identity;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}

	public String getIdentity_Class() {
		return identityClass;
	}

	public void setIdentity_Class(String identityClass) {
		this.identityClass = identityClass;
	}

	public Long getIn_Clinic_Attr() {
		return inClinicAttr;
	}

	public void setIn_Clinic_Attr(Long inClinicAttr) {
		this.inClinicAttr = inClinicAttr;
	}

	public String getIn_Dept_Name() {
		return inDeptName;
	}

	public void setIn_Dept_Name(String inDeptName) {
		this.inDeptName = inDeptName;
	}

	public Long getInfect_Indicator() {
		return infectIndicator;
	}

	public void setInfect_Indicator(Long infectIndicator) {
		this.infectIndicator = infectIndicator;
	}

	public Long getInfusion_React_Times() {
		return infusionReactTimes;
	}

	public void setInfusion_React_Times(Long infusionReactTimes) {
		this.infusionReactTimes = infusionReactTimes;
	}

	public Long getIn_Internal_Or_Sergery() {
		return inInternalOrSergery;
	}

	public void setIn_Internal_Or_Sergery(Long inInternalOrSergery) {
		this.inInternalOrSergery = inInternalOrSergery;
	}

	public Long getIn_Outp_Or_Inp() {
		return inOutpOrInp;
	}

	public void setIn_Outp_Or_Inp(Long inOutpOrInp) {
		this.inOutpOrInp = inOutpOrInp;
	}

	public String getInsurance_No() {
		return insuranceNo;
	}

	public void setInsurance_No(String insuranceNo) {
		this.insuranceNo = insuranceNo;
	}

	public String getInsurance_Type() {
		return insuranceType;
	}

	public void setInsurance_Type(String insuranceType) {
		this.insuranceType = insuranceType;
	}

	public String getInternal_No() {
		return internalNo;
	}

	public void setInternal_No(String internalNo) {
		this.internalNo = internalNo;
	}

	public Date getLink_Date() {
		return linkDate;
	}

	public void setLink_Date(Date linkDate) {
		this.linkDate = linkDate;
	}

	public String getMailing_Address() {
		return mailingAddress;
	}

	public void setMailing_Address(String mailingAddress) {
		this.mailingAddress = mailingAddress;
	}

	public String getMarital_Status() {
		return maritalStatus;
	}

	public void setMarital_Status(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	public String getMedical_Pay_Way() {
		return medicalPayWay;
	}

	public void setMedical_Pay_Way(String medicalPayWay) {
		this.medicalPayWay = medicalPayWay;
	}

	public String getMr_Infect_Report() {
		return mrInfectReport;
	}

	public void setMr_Infect_Report(String mrInfectReport) {
		this.mrInfectReport = mrInfectReport;
	}

	public String getMr_Quality() {
		return mrQuality;
	}

	public void setMr_Quality(String mrQuality) {
		this.mrQuality = mrQuality;
	}

	public String getMr_Value() {
		return mrValue;
	}

	public void setMr_Value(String mrValue) {
		this.mrValue = mrValue;
	}

	public String getNext_Of_Kin() {
		return nextOfKin;
	}

	public void setNext_Of_Kin(String nextOfKin) {
		this.nextOfKin = nextOfKin;
	}

	public String getNext_Of_Kin_Addr() {
		return nextOfKinAddr;
	}

	public void setNext_Of_Kin_Addr(String nextOfKinAddr) {
		this.nextOfKinAddr = nextOfKinAddr;
	}

	public String getNext_Of_Kin_Phone() {
		return nextOfKinPhone;
	}

	public void setNext_Of_Kin_Phone(String nextOfKinPhone) {
		this.nextOfKinPhone = nextOfKinPhone;
	}

	public String getNext_Of_Kin_Zipcode() {
		return nextOfKinZipcode;
	}

	public void setNext_Of_Kin_Zipcode(String nextOfKinZipcode) {
		this.nextOfKinZipcode = nextOfKinZipcode;
	}

	public String getNurse_Of_Control_Quality() {
		return nurseOfControlQuality;
	}

	public void setNurse_Of_Control_Quality(String nurseOfControlQuality) {
		this.nurseOfControlQuality = nurseOfControlQuality;
	}

	public String getOccupation() {
		return occupation;
	}

	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}

	public Long getOut_Clinic_Attr() {
		return outClinicAttr;
	}

	public void setOut_Clinic_Attr(Long outClinicAttr) {
		this.outClinicAttr = outClinicAttr;
	}

	public String getOut_Dept_Name() {
		return outDeptName;
	}

	public void setOut_Dept_Name(String outDeptName) {
		this.outDeptName = outDeptName;
	}

	public Long getOut_Internal_Or_Sergery() {
		return outInternalOrSergery;
	}

	public void setOut_Internal_Or_Sergery(Long outInternalOrSergery) {
		this.outInternalOrSergery = outInternalOrSergery;
	}

	public Long getOut_Outp_Or_Inp() {
		return outOutpOrInp;
	}

	public void setOut_Outp_Or_Inp(Long outOutpOrInp) {
		this.outOutpOrInp = outOutpOrInp;
	}

	public String getPat_Adm_Condition() {
		return patAdmCondition;
	}

	public void setPat_Adm_Condition(String patAdmCondition) {
		this.patAdmCondition = patAdmCondition;
	}

	public String getPatient_Class() {
		return patientClass;
	}

	public void setPatient_Class(String patientClass) {
		this.patientClass = patientClass;
	}

	public String getPatient_Id() {
		return patientId;
	}

	public void setPatient_Id(String patientId) {
		this.patientId = patientId;
	}

	public String getPractice_Doctor() {
		return practiceDoctor;
	}

	public void setPractice_Doctor(String practiceDoctor) {
		this.practiceDoctor = practiceDoctor;
	}

	public String getPractice_Doctor_Of_Graduate() {
		return practiceDoctorOfGraduate;
	}

	public void setPractice_Doctor_Of_Graduate(String practiceDoctorOfGraduate) {
		this.practiceDoctorOfGraduate = practiceDoctorOfGraduate;
	}

	public String getRelationship() {
		return relationship;
	}

	public void setRelationship(String relationship) {
		this.relationship = relationship;
	}

	public Long getSecond_Level_Nurs_Days() {
		return secondLevelNursDays;
	}

	public void setSecond_Level_Nurs_Days(Long secondLevelNursDays) {
		this.secondLevelNursDays = secondLevelNursDays;
	}

	public Long getSerious_Cond_Days() {
		return seriousCondDays;
	}

	public void setSerious_Cond_Days(Long seriousCondDays) {
		this.seriousCondDays = seriousCondDays;
	}

	public String getService_Agency() {
		return serviceAgency;
	}

	public void setService_Agency(String serviceAgency) {
		this.serviceAgency = serviceAgency;
	}

	public Long getService_System_Indicator() {
		return serviceSystemIndicator;
	}

	public void setService_System_Indicator(Long serviceSystemIndicator) {
		this.serviceSystemIndicator = serviceSystemIndicator;
	}

	public Long getSettle_Indi() {
		return settleIndi;
	}

	public void setSettle_Indi(Long settleIndi) {
		this.settleIndi = settleIndi;
	}

	public Long getSpec_Level_Nurs_Days() {
		return specLevelNursDays;
	}

	public void setSpec_Level_Nurs_Days(Long specLevelNursDays) {
		this.specLevelNursDays = specLevelNursDays;
	}

	public Long getThird_Level_Nurs_Days() {
		return thirdLevelNursDays;
	}

	public void setThird_Level_Nurs_Days(Long thirdLevelNursDays) {
		this.thirdLevelNursDays = thirdLevelNursDays;
	}

	public String getTop_Unit() {
		return topUnit;
	}

	public void setTop_Unit(String topUnit) {
		this.topUnit = topUnit;
	}

	public Double getTotal_Costs() {
		return totalCosts;
	}

	public void setTotal_Costs(Double totalCosts) {
		this.totalCosts = totalCosts;
	}

	public Double getTotal_Payments() {
		return totalPayments;
	}

	public void setTotal_Payments(Double totalPayments) {
		this.totalPayments = totalPayments;
	}

	public Long getTraining_Injury_Indicator() {
		return trainingInjuryIndicator;
	}

	public void setTraining_Injury_Indicator(Long trainingInjuryIndicator) {
		this.trainingInjuryIndicator = trainingInjuryIndicator;
	}

	public String getUnit_In_Contract() {
		return unitInContract;
	}

	public void setUnit_In_Contract(String unitInContract) {
		this.unitInContract = unitInContract;
	}

	public Long getVisit_Id() {
		return visitId;
	}

	public void setVisit_Id(Long visitId) {
		this.visitId = visitId;
	}

	public Long getWorking_Status() {
		return workingStatus;
	}

	public void setWorking_Status(Long workingStatus) {
		this.workingStatus = workingStatus;
	}

	public String getXExam_No() {
		return XExamNo;
	}

	public void setXExam_No(String examNo) {
		XExamNo = examNo;
	}

	public String getZip_Code() {
		return zipCode;
	}

	public void setZip_Code(String zipCode) {
		this.zipCode = zipCode;
	}
	
	
}