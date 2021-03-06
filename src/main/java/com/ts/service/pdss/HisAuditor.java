package com.ts.service.pdss;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import org.apache.cxf.message.Message;
import org.apache.cxf.phase.PhaseInterceptorChain;
import org.apache.cxf.transport.http.AbstractHTTPDestination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.hitzd.DBUtils.TCommonRecord;
import com.hitzd.his.Beans.TLabTest;
import com.hitzd.his.Beans.TPatOperation;
import com.hitzd.his.Beans.TPatOrderDiagnosis;
import com.hitzd.his.Beans.TPatOrderDrug;
import com.hitzd.his.Beans.TPatOrderDrugSensitive;
import com.hitzd.his.Beans.TPatOrderVisitInfo;
import com.hitzd.his.Beans.TPatVitalSigns;
import com.hitzd.his.Beans.TPatientOrder;
import com.hitzd.his.Beans.TPreveUseDrug;
import com.hitzd.his.Beans.TTreatUseDrug;
import com.hitzd.his.Utils.Config;
import com.hitzd.his.Utils.DateUtils;
import com.hitzd.his.Utils.DrugUtils;
import com.hitzd.springBeanManager.SpringBeanUtil;
import com.ts.entity.pdss.Message.CheckBeanMessage;
import com.ts.entity.pdss.SaveER.QueueBeanTCR;
import com.ts.entity.pdss.Saver.QueueBean;
import com.ts.entity.pdss.Saver.SaveBeanRS;
import com.ts.entity.pdss.ias.RSBeans.TAntiDrugSecurityCheckResult;
import com.ts.entity.pdss.ias.RSBeans.TAntiDrugSecurityResult;
import com.ts.entity.pdss.pdss.Beans.TDrug;
import com.ts.entity.pdss.pdss.RSBeans.TCheckResultCollection;
import com.ts.entity.pdss.pdss.RSBeans.TDrugSecurityRslt;
import com.ts.entity.pdss.peaas.Beans.TOperationDrug;
import com.ts.entity.pdss.peaas.Beans.TOperationType;
import com.ts.entity.pdss.peaas.Beans.TPrescPatMasterBean;
import com.ts.entity.pdss.peaas.RSBeans.TPrescCheckRslt;
import com.ts.entity.pdss.peaas.RSBeans.TPrescSecurityRslt;
import com.ts.service.pdss.ias.manager.IAntiDrugAuditor;
import com.ts.service.pdss.ias.manager.IAntiDrugSecurityChecker;
import com.ts.service.pdss.pdss.Utils.CommonUtils;
import com.ts.service.pdss.pdss.impl.PatientSaveCheckResult;
import com.ts.service.pdss.pdss.manager.IDrugSecurityChecker;
import com.ts.service.pdss.pdss.manager.IPatientSaveCheckResult;
import com.ts.service.pdss.peaas.manager.IPrescSecurityChecker;
import com.ts.util.Logger;

/**
 * 
 * 总体审查接口 
 */
@WebService(
        endpointInterface = "com.ts.service.pdss.IHisAuditor",
        portName = "PdssServicePort",
        serviceName = "PdssService",
        targetNamespace = "http://www.tmp.com/services/pdssService"
    )
@Service("hisAuditor")
public class HisAuditor implements IHisAuditor 
{
	private final static Logger log = Logger.getLogger("HisAuditor");
	public HisAuditor(){}

	/* 存放最后一次的审查结果  */
	public static Map<String, TCheckResultCollection>  DoctorIPCheckRS = new HashMap<String, TCheckResultCollection>();
	
	/* 抗菌药物审查  */
	@Resource(name="antiDrugSecurityCheckerBean")
	private IAntiDrugSecurityChecker antiDrugscr;
	
	/* 药物安全审查  */
	@Resource(name="drugSecurityChecker")
	private IDrugSecurityChecker drugsecuity;
	
	/* 大处方安全审查 */
	@Resource(name="prescSecurityCheckBean")
	private IPrescSecurityChecker presc;
	
//	/* 客户自定定义审查 */
//	@Resource(name="drugCustomChkServiceBean")
//	private IDrugCustomChkService CustomChk;
//	
//	
//	/**
//	 * 门诊处方客户自定义审核
//	 * @return
//	 */
//	public TDrugCustomSecurityRslt  OutpCustomCheck(String[] doctorInfo, String[] patientInfo, String[][] drugInfo, String[][] diagnosisInfo, String[][] sensitiveInfo, String[][] patSigns, String[] patOper)
//	{
//	    TPatientOrder po = CommonUtils.getPrescPatientOrder(doctorInfo, patientInfo, drugInfo, diagnosisInfo, sensitiveInfo, patSigns, patOper);
//	    return CustomChk.CheckAll(po, "1");
//	}
//	
//	/**
//	 * 住院医嘱自定审核 
//	 * @return
//	 */
//	public TDrugCustomSecurityRslt InpCustomCheck(String[] doctorInfo,String[] patientInfo, String[][] drugInfo,String[][] diagnosisInfo, String[][] sensitiveInfo,String[][] patSigns,String[] patOper,String[][] antiDrug)
//	{
//	    TPatientOrder po = CommonUtils.getPatientOrder(doctorInfo, patientInfo, drugInfo, diagnosisInfo, sensitiveInfo, patSigns,patOper,antiDrug);
//	    return CustomChk.CheckAll(po, "0");
//	}
	
	/**
	 * 返回体检信息 
	 */
    public TPatVitalSigns[] getpatVsVisitSigns(String patID , String Visitid)
	{
	    return antiDrugscr.getpatVsVisitSigns(patID, Visitid);
	}
	
	/**
	 * 返回检验信息 
	 * @param patid
	 * @param visitid
	 * @return
	 */
	@SuppressWarnings ("unchecked")
    public TLabTest[] getPatLabTest(String patid , String visitid)
	{
	   return antiDrugscr.getPatLabTest(patid, visitid);
	}
	
    public TLabTest[] getpatLabTestNoDetail(String patid , String visitid)
    {
        return antiDrugscr.getpatLabTestNoDetail(patid, visitid);
    }
    
    public TLabTest getpatLabTestDetail(String TestNO)
    {
        return antiDrugscr.getpatLabTestDetail(TestNO);
    }
	
	/**
	 * 返回手术信息 
	 * @param patid
	 * @param visitid
	 * @return
	 */
    public TPatOperation[] getPatVsVisitOperation(String patid , String visitid)
	{
	    return antiDrugscr.getPatVsVisitOperation(patid, visitid);
	}
	
    /**
     * 返回 手术类型 
     * @param name
     * @return
     */
    public TOperationType[] getOperationTypes(String name)
    {
        return this.presc.getOperationTypes(name);
    }
    
    /**
     * 返回手术类型下的药品 
     * @param operationID
     * @return
     */
    public TOperationDrug[] getOperationDrugs(String operationID)
    {
        return this.presc.getOperationDrugs(operationID);
    }
	
	public String[] DrugAuthorizationCheck(String[] PrescBeanInfo)
	{
	    return presc.DrugAuthorizationCheck(PrescBeanInfo);
	}
	
	/**
	 * 门诊药品审核(医生)
	 */
	public TPrescCheckRslt PrescCheckRs(String[] doctorInfo, String[] patientInfo, String[][] drugInfo, String[][] diagnosisInfo, String[][] sensitiveInfo, String[][] patSigns, String[] patOper)
	{
	    try
	    {
	        return presc.PrescCheckRs(doctorInfo, patientInfo, drugInfo, diagnosisInfo, sensitiveInfo, patSigns, patOper);
	    }
	    catch(Exception e )
	    {
	        e.printStackTrace();
	    }
	    return new TPrescCheckRslt();
	}
	
	/**
	 * 病人开具所有处方 
	 */
	public TPrescPatMasterBean getUsePrescDetail(String patient_id,Integer SelDay, String[] back)
	{
	   return  presc.getUsePrescDetail( patient_id,SelDay,  back);  
	}
	
	/**
	 * 预防抗菌药物记录保存
	 */
	@Override
//	public void SavePreveUseDrug(String[][] PreveUseInfo)
	public void SavePreveUseDrug(TPreveUseDrug[] preveUseInfo)
	{
	    PatientSaveCheckResult pscr = new PatientSaveCheckResult();
        TPatientOrder po = new TPatientOrder();
        pscr.setNgroupnum(UUID.randomUUID().toString());
	    try
	    {
//	        po.setPreveUseDrug(CommonUtils.getPreveUseDrug(PreveUseInfo));
	        po.setDoctorDeptName(preveUseInfo[0].getDEPT_NAME());
	        po.setDoctorName(preveUseInfo[0].getDOCTOR_NAME());
	        TPatOrderVisitInfo  patVisit =  new TPatOrderVisitInfo();
	        patVisit.setPatientID(preveUseInfo[0].getPATIENT_ID());
	        patVisit.setVisitID(preveUseInfo[0].getVISIT_ID());
	        po.setPatVisitInfo(patVisit);
	    	po.setPreveUseDrug(preveUseInfo);
	        SaveBeanRS sbRS = new SaveBeanRS();
	        sbRS.setPo(po);
	        sbRS.setCheckRC(new TCheckResultCollection());
	        QueueBean.setSaveBeanRS(sbRS);
//	        pscr.savePreveUseDrug(po);
	    }
	    catch(Exception e)
	    {
	        e.printStackTrace();
	    }
	    finally
	    {
	        pscr = null;
	        po   = null;
	    }
	}
	
	/**
	 * 治疗抗菌药物记录保存
	 */
	@Override
//	public void SaveTreatUseDrug(String[][] TreatUseInfo)
	public void SaveTreatUseDrug(TTreatUseDrug[] TreatUseInfo)
	{

	    PatientSaveCheckResult pscr = new PatientSaveCheckResult();
	    TPatientOrder po = new TPatientOrder();
	    pscr.setNgroupnum(UUID.randomUUID().toString());
	    try
	    {
	        po.setDoctorDeptName(TreatUseInfo[0].getDEPT_NAME());
            po.setDoctorName(TreatUseInfo[0].getDOCTOR_NAME());
            TPatOrderVisitInfo  patVisit =  new TPatOrderVisitInfo();
            patVisit.setPatientID(TreatUseInfo[0].getPATIENT_ID());
            patVisit.setVisitID(TreatUseInfo[0].getVISIT_ID());
            po.setPatVisitInfo(patVisit);
	    	po.setTreatUseDrug(TreatUseInfo);
            SaveBeanRS sbRS = new SaveBeanRS();
            sbRS.setPo(po);
            sbRS.setCheckRC(new TCheckResultCollection());
            QueueBean.setSaveBeanRS(sbRS);
//	        pscr.saveTreatUseDrug(po);
	    }
	    catch(Exception e )
	    {
	        e.printStackTrace();
	    }
	    finally
	    {
	        pscr = null;
	        po   = null;
	    }
	}
	
	/**
	 * 使用 病人id 和住院id 审查医嘱信息 
	 * @param patientID
	 * @param visitID
	 * @return
	 */
	public String[] DrugCheckRSByPatientIdAndVisitId(String patientID , String visitID ,String parm)
	{
		List<String> listStr = new ArrayList<String>();
		try
		{
			/* 判断住院号是否为空 　*/
			String visit_ID = visitID;
			IHisGetPatientOrders hisPO = (IHisGetPatientOrders)SpringBeanUtil.getBean("hisGetPatientOrdersBean");
			TPatientOrder           po = hisPO.getPatientOrder(patientID, visit_ID);
			if(po == null) return  new String[]{"错误"};
			TCheckResultCollection tcc = DrugSecurityCheckAllPO(po);
			  /* 用ip为key 储存审查结果  */
//		    DoctorIPCheckRS.put(XFireServletController.getRequest().getRemoteAddr(), tcc);
		    DoctorIPCheckRS.put(this.getRemoteAddrIp(), tcc);
		    /*  获得药物安全审查   */
		    TDrugSecurityRslt drugSR = tcc.getDsr();
		    if(drugSR == null) drugSR = new TDrugSecurityRslt();
		    /*  返回在药品drug_no_local */
		    listStr.add(drugSR.FetchCheckResult("1"));
		    listStr.add(drugSR.getResultInfo());
		    
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	    return (String[])listStr.toArray(new String[0]);
	}
	
	/**
	 * 只返回 药品审查结果 灯 信息  
	 * @param doctorInfo
	 * @param patientInfo
	 * @param drugInfo
	 * @param diagnosisInfo
	 * @param sensitiveInfo
	 * @param patSigns
	 * @param patOper
	 * @return
	 */
	public String[] DrugCheckRSInfo(String[] doctorInfo,String[] patientInfo, String[][] drugInfo,String[][] diagnosisInfo, String[][] sensitiveInfo,String[][] patSigns,String[] patOper,String[][] antiDrug)
	{
	    List<String> listStr = new ArrayList<String>();
	    try
	    {
	    	 TCheckResultCollection tcc = DrugSecurityCheckAll(doctorInfo,patientInfo,drugInfo, diagnosisInfo, sensitiveInfo, patSigns, patOper , antiDrug);
	 	    /* 用ip为key 储存审查结果  */
//	 	    DoctorIPCheckRS.put(XFireServletController.getRequest().getRemoteAddr(), tcc);
	 	    DoctorIPCheckRS.put(this.getRemoteAddrIp(), tcc);
	 	    /* 获得药物安全审查 */
	 	    TDrugSecurityRslt drugSR = tcc.getDsr();
	 	    /* 抗菌药物审查  */
            TAntiDrugSecurityResult[] adsr = tcc.getAdsr();
            StringBuffer sbfr = new StringBuffer();
            if(adsr != null )
            {
                for(TAntiDrugSecurityResult a : adsr)
                {
                    sbfr.append("[").append(a.getDrug_ID()).append("].[" + a.getDrug_name() + "].").append(a.getOrder_No()).append(".").append(a.getOrder_Sub_No())
                        .append(".").append(a.getTAntiDrugSecurity().length).append(";");
                }
            }
            if(sbfr.length() > 0 )
                sbfr.deleteCharAt(sbfr.length() - 1);
            if(drugSR != null)
            {
//                listStr.add(drugSR.FetchCheckResult(this.WebCilentDCF));
                listStr.add(drugSR.getResultInfo());
            }
            else
            {
                listStr.add("");
                listStr.add("");
            }    
	 	    listStr.add(sbfr.toString());
	    }
	    catch(Exception e )
	    {
	    	e.printStackTrace();
	    }
	    return (String[])listStr.toArray(new String[0]);
	}
 
	/**
	 * 
	 * 从缓存中取出 结果
	 * 根据单药品信息到查询审查结果中寻找数据
	 * 
	 */
	public TCheckResultCollection getDrugSingleCheckRS(String drugNoLoacl , String recMainNo, String recSubNo)
	{
	    TCheckResultCollection crc = new TCheckResultCollection();
	    /* 组织本地药品信息  */
	    TDrug drug = new TDrug();
        drug.setDRUG_NO_LOCAL(drugNoLoacl);
        drug.setRecMainNo(recMainNo);
        drug.setRecSubNo(recSubNo);
	    
	    /* 从map中找到医生 最后一次的审查 */
//	    TCheckResultCollection tcc = DoctorIPCheckRS.get(XFireServletController.getRequest().getRemoteAddr());
	    TCheckResultCollection tcc = DoctorIPCheckRS.get(this.getRemoteAddrIp());
	    if(tcc == null)
	        return crc;
	    /* 判断是否开启药物安全审查  */
        if(Config.getIntParamValue("PDSSSwitcher") == 1)
        {
            /* 药物安全审查*/
            TDrugSecurityRslt dsr = new TDrugSecurityRslt();
            /* 药物安全审查单药品 结果 */
            dsr.setSingleCheckResult(drug, tcc.getDsr().getCheckResult(drug));
            crc.setDsr(dsr);    
        }
        
        /* 判断是否开启  抗菌物安全审查  */
        if(Config.getIntParamValue("ADSSSwitcher") == 1) 
        {
            /* 抗菌药物 */
            if(DrugUtils.isKJDrug(drugNoLoacl))
            {
                TAntiDrugSecurityResult[] adsr = tcc.getAdsr();
                for(TAntiDrugSecurityResult a : adsr)
                {
                    if(drugNoLoacl.equals(a.getDrug_ID()) && recMainNo.equals(a.getOrder_No()) && recSubNo.equals(a.getOrder_Sub_No()))
                    {
                        crc.setAdsr(new TAntiDrugSecurityResult[]{a});
                        break;
                    }
                }
            }
        }
        /* 判断是否开启 大处方处方  */
        if(Config.getIntParamValue("PESSSwitcher") == 1)
        {
            /*  大处方 药物安全审查*/
            TDrugSecurityRslt dsr = new TDrugSecurityRslt();
            TPrescSecurityRslt psr = new TPrescSecurityRslt();
            /*  大处方 药物安全审查单药品 结果 */
            dsr.setSingleCheckResult(drug, tcc.getPsr().getDsr().getCheckResult(drug));
            psr.setDsr(dsr);
            crc.setPsr(psr);
        }
        /* 判断是否开启 医保审查   */
        if(Config.getIntParamValue("MASSSwitcher") == 1)
        {
            
        }
	    return crc;
	}
	
	/**
	 * 事后审查
	 */
//	@Override
//	public void DrugSecutityCheckAllPO(TPatientOrder po)
//	{
//        TCheckResultCollection crc = new TCheckResultCollection();
//        try
//        {
//        	 long x1 = System.currentTimeMillis();
//             /* 判断是否开启药物安全审查  */
//             if(Config.getIntParamValue("PDSSSwitcher") == 1)
//             {
//                 /* 药物安全审查结果 */
//                 TDrugSecurityRslt drugsr = DrugSecurityCheck(po);
//                 crc.setDsr(drugsr);
//                 System.out.println("药物安全审查结束:" + (System.currentTimeMillis() - x1));
//             }
//             /* 判断是否开启  抗菌物安全审查  */
//             if(Config.getIntParamValue("ADSSSwitcher") == 1) 
//             {
//                 x1 = System.currentTimeMillis();
//                 /* 抗菌药物审查数据组织 */
//                 TPatOrderDiagnosis[] podi = po.getPatOrderDiagnosiss();
//                 String[] diagnosis = new String[podi.length];
//                 for(int i = 0 ; i < podi.length ; i++)
//                 {
//                     diagnosis[i] = podi[i].getDiagnosisDictID();
//                 }
//                 List<TAntiDrugSecurityResult> list = new ArrayList<TAntiDrugSecurityResult>();
//                 String[] doctorInfo = new String[6];
//                 doctorInfo[0] = po.getDoctorDeptID();
//                 doctorInfo[1] = po.getDoctorDeptName();
//                 doctorInfo[2] = po.getDoctorID();
//                 doctorInfo[3] = po.getDoctorName();
//                 doctorInfo[4] = po.getDoctorTitleID();
//                 doctorInfo[5] = po.getDoctorTitleName();
//                 /* 手术记录信息 */
//                 String[] patOper = new String[5];
//                 TPatOperation[] operator = po.getPatOperation();
//                 if(operator != null && operator.length  > 0)
//                 {
//                     patOper[0] = operator[0].getOperCode();
//                     patOper[1] = operator[0].getOperName(); 
//                     patOper[2] = operator[0].getOperLevel(); 
//                     patOper[3] = operator[0].getOperStartTime(); 
//                     patOper[4] = operator[0].getOperEndTime();
//                 }
//                 for(TPatOrderDrug pod : po.getPatOrderDrugs())
//                 {
//                     String[]  orderDrug = new String[7];
//                     /* 判断是否 为抗菌药物  */
//                     if(!DrugUtils.isKJDrug(pod.getDrugID()))
//                         continue;
//                     orderDrug[0] = pod.getDrugID();
//                     orderDrug[1] = pod.getDrugName();
//                     orderDrug[2] = pod.getRecMainNo();
//                     orderDrug[3] = pod.getRecSubNo();
//                     orderDrug[4] = pod.getStartDateTime();
//                     orderDrug[5] = pod.getStopDateTime();
//                     orderDrug[6] = pod.getUseType();
//                     TAntiDrugSecurityResult adsr = AntiDrugSecurityCheckerA(doctorInfo, diagnosis, patOper, orderDrug, new String[]{po.getPatVisitInfo().getPatientID(),po.getPatVisitInfo().getVisitID()});
//                     if(crc.getDsr() == null)
//                     {
//                         TDrugSecurityRslt drugsr1 = null;
//                         TPatientOrder po1 = CommonUtils.getPatientOrder(doctorInfo, new String[]{}, new String[][]{}, new String[][]{},new String[][]{}, new String[][]{},patOper);
//                         po1.setPatOrderDrugs(new TPatOrderDrug[]{pod});
//                         drugsr1  = this.DrugSpecPeopleCheck(po1);
//                         if(drugsr1.getCheckResults() != null)adsr.setDspList(drugsr1.getCheckResults()[0].getDrugSpecPeopleRslt());
//                         drugsr1  =  this.DrugAllergenCheck(po1);
//                         if(drugsr1.getCheckResults() != null)adsr.setAllergenRslt(drugsr1.getCheckResults()[0].getDrugAllergenRslt());
//                         drugsr1  = this.DrugDosageCheck(po1);
//                         if(drugsr1.getCheckResults() != null)adsr.setDosageRslt(drugsr1.getCheckResults()[0].getDrugDosageRslt());
//                     }
//                     else
//                     {
//                         TDrugSecurityRslt drugsr1 = crc.getDsr();
//                         TDrug drug = new TDrug();
//                         drug.setDRUG_NO_LOCAL(pod.getDrugID());
//                         drug.setRecMainNo(pod.getRecMainNo());
//                         drug.setRecSubNo(pod.getRecSubNo());
//                         adsr.setDspList(drugsr1.getCheckResult(drug).getDrugSpecPeopleRslt());
//                         adsr.setAllergenRslt(drugsr1.getCheckResult(drug).getDrugAllergenRslt());
//                         adsr.setDosageRslt(drugsr1.getCheckResult(drug).getDrugDosageRslt());
//                     }
//                     list.add(adsr);
//                 }
//                 crc.setAdsr((TAntiDrugSecurityResult[])list.toArray(new TAntiDrugSecurityResult[0]));
//             }
//             /* 判断是否开启 大处方处方  */
//             if(Config.getIntParamValue("PESSSwitcher") == 1)
//             {
//                 x1 = System.currentTimeMillis();
//                 crc.setPsr(presc.PrescSecurityCheck(po));
//                 System.out.println("大处方安全审查结束:" + (System.currentTimeMillis() - x1));
//             }
//             /* 判断是否开启 医保审查   */
//             if(Config.getIntParamValue("MASSSwitcher") == 1)
//             {
//                 x1 = System.currentTimeMillis();
//                 System.out.println("医保审查结束:" + (System.currentTimeMillis() - x1));
//             }
//             /* 保存信息到队列中  */
//             SaveBeanRS sbRS = new SaveBeanRS();
//             sbRS.setPo(po);
//             sbRS.setCheckRC(crc);
//             QueueBean.setSaveBeanRS(sbRS);
//        }
//        catch(Exception e)
//        {
//        	e.printStackTrace();
//        }
//	}
	
	/**
	 * 所有审查  并且 保存其结果  
	 * @param po (patient_id , visit_id ) 参数 
	 * @return
	 */
	public TCheckResultCollection DrugSecurityCheckAllPO(TPatientOrder po)
	{
		TCommonRecord tcr = new TCommonRecord();                                                  //审查时间监控
		tcr.set("CHECK_START_TIME",DateUtils.getDateTime());      								  //审查开始时间
		String ip = this.getRemoteAddrIp();
		log.debug("--------------------------------------------------------------------");
		log.debug("输入参数" + po.toString());
		log.debug("用户地址:" + ip);
		tcr.set("IP", ip);                                                     //用户地址
		long xx =  System.currentTimeMillis();
		TCheckResultCollection crc = new TCheckResultCollection();
		log.debug("创建医嘱对象:" + (System.currentTimeMillis() - xx));
		tcr.set("ORDERS_OBJ", String.valueOf(System.currentTimeMillis() - xx));                   //创建医嘱对象
		long x1 = System.currentTimeMillis();
		/* 判断是否开启药物安全审查  */
		if(Config.getIntParamValue("PDSSSwitcher") == 1)
		{
		    /* 药物安全审查结果 */
		    TDrugSecurityRslt drugsr = DrugSecurityCheck(po);
	        crc.setDsr(drugsr);
	        log.debug("药物安全审查结束:" + (System.currentTimeMillis() - x1));
	        tcr.set("DRUG_OVER", String.valueOf(System.currentTimeMillis() - x1));                //药物安全审查结束
		}
		/* 保存信息到队列中  */
		SaveBeanRS sbRS = new SaveBeanRS();
		sbRS.setPo(po);
		sbRS.setCheckRC(crc);
		QueueBean.setSaveBeanRS(sbRS);
		
		log.debug("审查总消耗时间:" + (System.currentTimeMillis() - xx));
		log.debug("药物医嘱个数 :"  + po.getPatOrderDrugs().length);
		setCheckBeanMessag(po,ip,String.valueOf(System.currentTimeMillis() - xx));
		
		tcr.set("ID", sbRS.getID());
		tcr.set("EXA_DATE", String.valueOf(System.currentTimeMillis() - xx));                     //审查总消耗时间
		tcr.set("DRUG_ORDERS_NUM", String.valueOf(po.getPatOrderDrugs().length));                 //药物医嘱个数
		tcr.set("PATIENT_ID", po.getPatVisitInfo().getPatientID());                               //病人ID
		tcr.set("VISIT_ID", po.getPatVisitInfo().getVisitID());                                   //住院次数
		tcr.set("DOCTOR_NAME", po.getDoctorName());                                               //医生名称
		tcr.set("DEPT_NAME", po.getDoctorDeptName());                                             //部门名称
		tcr.set("OPERATION_DATE",DateUtils.getDateTime());                                        //操作时间
		tcr.set("AllSingleCheckTime","0");                     //单项审查时间总汇
//		tcr.set("AllSingleCheckTime", HisSubCheckTime.getSubCheckTimeInfo());                     //单项审查时间总汇
		/* 审查信息列队中 */
		QueueBeanTCR.setSaveBeanRS(tcr);
		return crc;
	}
	
	private void setCheckBeanMessag(TPatientOrder po ,String ip,String checkTime)
	{
	    //添加审核简体信息
	    CheckBeanMessage.setCheckBeanMessage(ip, po.getDoctorName(),String.valueOf(po.getPatOrderDrugs().length), checkTime, po.getPatient().getName(),po.getDoctorDeptName());
	}
	
	/**
	 *   所有审查  并且 保存其结果  
	 *   集成审查所用  参数   
	 *   主用审核方法 
	 */
	@Override
	public TCheckResultCollection DrugSecurityCheckAll(String[] doctorInfo,String[] patientInfo, String[][] drugInfo,String[][] diagnosisInfo, String[][] sensitiveInfo,String[][] patSigns,String[] patOper,String[][] antiDrug) 
	{
		TCommonRecord tcr = new TCommonRecord();                                                  //审查时间监控
		tcr.set("CHECK_START_TIME",DateUtils.getDateTime());      								  //审查开始时间
		System.out.println("--------------------------------------------------------------------");
//		System.out.println("." + XFireServletController.getRequest());
		System.out.println("用户地址:" + this.getRemoteAddrIp());
//		tcr.set("IP", XFireServletController.getRequest().getRemoteAddr());                       //用户地址
		tcr.set("IP", this.getRemoteAddrIp());                       //用户地址
		long xx =  System.currentTimeMillis();
		TCheckResultCollection crc = new TCheckResultCollection();
		TPatientOrder po = CommonUtils.getPatientOrder(doctorInfo, patientInfo, drugInfo, diagnosisInfo, sensitiveInfo, patSigns,patOper,antiDrug);
		System.out.println("创建医嘱对象:" + (System.currentTimeMillis() - xx));
		tcr.set("ORDERS_OBJ", String.valueOf(System.currentTimeMillis() - xx));                   //创建医嘱对象
		long x1 = System.currentTimeMillis();
		/* 判断是否开启药物安全审查  */
		if(Config.getIntParamValue("PDSSSwitcher") == 1)
		{
		    /* 药物安全审查结果 */
		    TDrugSecurityRslt drugsr = DrugSecurityCheck(po);
	        crc.setDsr(drugsr);
	        System.out.println("药物安全审查结束:" + (System.currentTimeMillis() - x1));
	        tcr.set("DRUG_OVER", String.valueOf(System.currentTimeMillis() - x1));                //药物安全审查结束
		}
		/* 判断是否开启  抗菌物安全审查  */
//		if(Config.getIntParamValue("ADSSSwitcher") == 1) 
//        {
//		    x1 = System.currentTimeMillis();
//    		/* 抗菌药物审查数据组织 */
//    		TPatOrderDiagnosis[] podi = po.getPatOrderDiagnosiss();
//    		String[] diagnosis = new String[podi.length];
//    		for(int i = 0 ; i < podi.length ; i++)
//    		{
//    			diagnosis[i] = podi[i].getDiagnosisDictID();
//    		}
//    		List<TAntiDrugSecurityResult> list = new ArrayList<TAntiDrugSecurityResult>();
//    		int count = 0 ;
//    		if(po != null || po.getPatOrderDrugs() != null)
//    		{	
//    		    for(TPatOrderDrug pod : po.getPatOrderDrugs())
//        		{
//        			String[]  orderDrug = new String[7];
//        			/* 判断是否 为抗菌药物  */
//        			if(!DrugUtils.isKJDrug(pod.getDrugID()))
//        				continue;
//        			count++;
//        			orderDrug[0] = pod.getDrugID();
//        			orderDrug[1] = pod.getDrugName();
//        			orderDrug[2] = pod.getRecMainNo();
//        			orderDrug[3] = pod.getRecSubNo();
//        			orderDrug[4] = pod.getStartDateTime();
//        			orderDrug[5] = pod.getStopDateTime();
//        			orderDrug[6] = pod.getUseType();
//        			TAntiDrugSecurityResult adsr = AntiDrugSecurityCheckerA(doctorInfo, diagnosis, patOper, orderDrug, new String[]{po.getPatVisitInfo().getPatientID(),po.getPatVisitInfo().getVisitID()});
//        			if(crc.getDsr() == null)
//        			{
//        				TDrugSecurityRslt drugsr1 = null;
//        				TPatientOrder po1 = CommonUtils.getPatientOrder(doctorInfo, patientInfo, drugInfo, diagnosisInfo, sensitiveInfo, patSigns,patOper);
//        				po1.setPatOrderDrugs(new TPatOrderDrug[]{pod});
//        				drugsr1  = this.DrugSpecPeopleCheck(po1);
//        				if(drugsr1.getCheckResults() != null)adsr.setDspList(drugsr1.getCheckResults()[0].getDrugSpecPeopleRslt());
//        				drugsr1  =  this.DrugAllergenCheck(po1);
//        				if(drugsr1.getCheckResults() != null)adsr.setAllergenRslt(drugsr1.getCheckResults()[0].getDrugAllergenRslt());
//        				drugsr1  = this.DrugDosageCheck(po1);
//        				if(drugsr1.getCheckResults() != null)adsr.setDosageRslt(drugsr1.getCheckResults()[0].getDrugDosageRslt());
//        			}
//        			else
//        			{
//        				TDrugSecurityRslt drugsr1 = crc.getDsr();
//        				if(drugsr1 != null)
//        				{
//        				    TDrug drug = new TDrug();
//            				drug.setDRUG_NO_LOCAL(pod.getDrugID());
//            				drug.setRecMainNo(pod.getRecMainNo());
//            				drug.setRecSubNo(pod.getRecSubNo());
//            				adsr.setDspList(drugsr1.getCheckResult(drug).getDrugSpecPeopleRslt());
//            				adsr.setAllergenRslt(drugsr1.getCheckResult(drug).getDrugAllergenRslt());
//            				adsr.setDosageRslt(drugsr1.getCheckResult(drug).getDrugDosageRslt());
//        				}
//        			}
//        			list.add(adsr);
//        		}
//    		}
//    		crc.setAdsr((TAntiDrugSecurityResult[])list.toArray(new TAntiDrugSecurityResult[0]));
//    		System.out.println("抗菌药物审查结束:" + (System.currentTimeMillis() - x1) + "抗菌药物数量: " + count);
//    		tcr.set("ANTI_OVER", String.valueOf(System.currentTimeMillis() - x1));                //抗菌药物审查结束
//    		tcr.set("ANTI_NUM", String.valueOf(count));                                           //抗菌药物数量
//        }
		/* 判断是否开启 大处方处方  */
	    if(Config.getIntParamValue("PESSSwitcher") == 1)
        {
	        x1 = System.currentTimeMillis();
	        crc.setPsr(presc.PrescSecurityCheck(po));
	        System.out.println("大处方安全审查结束:" + (System.currentTimeMillis() - x1));
	        tcr.set("BIG_DRUG_OVER", String.valueOf(System.currentTimeMillis() - x1));            //大处方安全审查结束
        }
	    /* 判断是否开启 医保审查   */
        if(Config.getIntParamValue("MASSSwitcher") == 1)
        {
            x1 = System.currentTimeMillis();
            System.out.println("医保审查结束:" + (System.currentTimeMillis() - x1));
            tcr.set("MEDICAL_OVER", String.valueOf(System.currentTimeMillis() - x1));             //医保审查结束
        }
        
//        /* 判断是否开启 用户自定义审核 */
//        if(Config.getIntParamValue("") == 1)
//        {
//            crc.setDcsr(this.CustomChk.CheckAll(po, "0"));
//        }
        
		/* 保存信息到队列中  */
		SaveBeanRS sbRS = new SaveBeanRS();
		sbRS.setPo(po);
		sbRS.setCheckRC(crc);
		QueueBean.setSaveBeanRS(sbRS);
		
		System.out.println("审查总消耗时间:" + (System.currentTimeMillis() - xx));
		System.out.println("药物医嘱个数 :"  + po.getPatOrderDrugs().length);
		
		tcr.set("ID", sbRS.getID());
		tcr.set("EXA_DATE", String.valueOf(System.currentTimeMillis() - xx));                     //审查总消耗时间
		tcr.set("DRUG_ORDERS_NUM", String.valueOf(po.getPatOrderDrugs().length));                 //药物医嘱个数
		tcr.set("PATIENT_ID", po.getPatVisitInfo().getPatientID());                               //病人ID
		tcr.set("VISIT_ID", po.getPatVisitInfo().getVisitID());                                   //住院次数
		tcr.set("DOCTOR_NAME", po.getDoctorName());                                               //医生名称
		tcr.set("DEPT_NAME", po.getDoctorDeptName());                                             //部门名称
		tcr.set("OPERATION_DATE",DateUtils.getDateTime());                                        //操作时间
		
		/* 审查信息列队中 */
		QueueBeanTCR.setSaveBeanRS(tcr);
		return crc;
	}
	
//	public TAntiDrugSecurityCheckResult antiDrugOverQuotaA(String[] doctorInfo,String[] diagnosis,String[] operInfo,String[] drugInfo,String[] patientInfo)
//	{
//		return antiDrugscr.antiDrugOverQuotaA(doctorInfo, diagnosis, operInfo, drugInfo, patientInfo);
//	}
//
//	public TAntiDrugSecurityCheckResult antiDrugOverTimeA(String[] doctorInfo,String[] diagnosis,String[] operInfo,String[] drugInfo,String[] patientInfo)
//	{
//		return antiDrugscr.antiDrugOverTimeA(doctorInfo, diagnosis, operInfo, drugInfo, patientInfo);
//	}
//
//	public TAntiDrugSecurityCheckResult antiDrugExcessAuthorizationA( String[] doctorInfo,String[] diagnosis,String[] operInfo,String[] drugInfo,String[] patientInfo) 
//	{
//		return antiDrugscr.antiDrugExcessAuthorizationA(doctorInfo, diagnosis, operInfo, drugInfo, patientInfo);
//	}
//
//	public TAntiDrugSecurityCheckResult antiDrugOverMomentA(String[] doctorInfo, String[] diagnosis, String[] operInfo,	String[] drugInfo,String[] patientInfo) 
//	{
//		return antiDrugscr.antiDrugOverMomentA(doctorInfo, diagnosis, operInfo, drugInfo, patientInfo);
//	}
//	
//	public TAntiDrugSecurityCheckResult antiDrugOverRateA(String[] doctorInfo, String[] diagnosis, String[] operInfo,	String[] drugInfo,String[] patientInfo) 
//	{
//		return antiDrugscr.antiDrugOverRateA(doctorInfo, diagnosis, operInfo, drugInfo, patientInfo);
//	}
//	
//    public TAntiDrugSecurityCheckResult antiDrugOperationA(String[] doctorInfo, String[] diagnosis, String[] operInfo,   String[] drugInfo,String[] patientInfo) 
//    {
//        return antiDrugscr.antiDrugOperationA(doctorInfo, diagnosis, operInfo, drugInfo, patientInfo);
//    }
//    
//    public TAntiDrugSecurityCheckResult antiDrugSpecA(String[] doctorInfo, String[] diagnosis, String[] operInfo,   String[] drugInfo,String[] patientInfo) 
//    {
//        return antiDrugscr.antiDrugSpecA(doctorInfo, diagnosis, operInfo, drugInfo, patientInfo);
//    }
//    
//    public TAntiDrugSecurityResult AntiDrugSecurityCheckerA(String[] doctorInfo,String[] diagnosis,String[] operInfo,String[] drugInfo,String[] patientInfo)
//	{
//		return antiDrugscr.AntiDrugSecurityCheckerA(doctorInfo, diagnosis, operInfo, drugInfo, patientInfo);
//	}
    
//    /**
//     * 相互作用检查
//     * 改造完成
//     */
//    public TDrugSecurityRslt DrugInteractionCheck(TPatientOrder po)
//    {
//        return drugsecuity.DrugInteractionCheck(po);
//    }
//    
//    /**
//     * 
//     * 改造完成
//     * @param Drugs
//     * @return
//     */
//    public TDrugSecurityRslt DrugInteractionCheckS(String[] Drugs)
//    {
//        try {
//			return drugsecuity.DrugInteractionCheckS(Drugs);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//        return null;
//    }

//    /**
//     *   配伍审查
//     */
//    public TDrugSecurityRslt DrugIvEffectCheck(TPatientOrder po)
//    {
//        TPatOrderDrug[] pods = po.getPatOrderDrugs();
//        String[] DrugIds = new String[pods.length];
//        String[] RecMainIds = new String[pods.length];
//        String[] AdminIds = new String[pods.length];
//        for (int i = 0; i < pods.length; i++)
//        {
//        	TPatOrderDrug drug = pods[i];
//        	DrugIds[i] = drug.getDrugID();
//        	RecMainIds[i] = drug.getRecMainNo();
//        	AdminIds[i] = drug.getAdministrationID();
//        }
//        return DrugIvEffectCheckS(DrugIds, RecMainIds, AdminIds);
//    }
//
//    /**
//     * 无具体实现
//     */
//    public TDrugSecurityRslt DrugIvEffectCheckS(String[] DrugIds, String[] RecMainIds, String[] AdministrationIds)
//    {
//    	return drugsecuity.DrugIvEffectCheckS(DrugIds, RecMainIds, AdministrationIds);
//    }
    
//    /**
//     * 禁忌症审查 
//     * 改造完成
//     */
//    public TDrugSecurityRslt DrugDiagCheck(TPatientOrder po)
//    {
//        try {
//        	return drugsecuity.DrugDiagCheck(po);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//        return null;
//    }
//    /**
//     *  改造完成
//     */
//    public TDrugSecurityRslt DrugDiagCheckS(String[] drugs, String[] diagnosis)
//    {
//        try {
//			return drugsecuity.DrugDiagCheckS(drugs, diagnosis);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//        return null;
//    }
    
//    /* 特殊人群审查 */
//    public TDrugSecurityRslt DrugSpecPeopleCheck(TPatientOrder po)
//    {
//        TPatOrderDrug[] pods = po.getPatOrderDrugs();
//        String[] Drugs = new String[pods.length];
//        for (int i = 0; i < pods.length; i++)
//            Drugs[i] = pods[i].getDrugID();
//        String patType = "0"; // 普通
//        if (po.getPatInfoExt().TheIsLact()) patType = "1";// 孕妇
//        else if (po.getPatInfoExt().TheIsPregnant()) patType = "2";
//        return DrugSpecPeopleCheckS(Drugs, po.getPatient().getDateOfBirth(), patType, po.getPatInfoExt().getIsLiverWhole(), po.getPatInfoExt().getIsKidneyWhole());
//    }
//    
//    /**
//     * 多个本地药品码，出生日期，病人类别（孕妇、哺乳、普通），肝功能不全标志，肾功能不全标志
//     * 
//     * 无具体实现
//     **/
//    public TDrugSecurityRslt DrugSpecPeopleCheckS(String[] DrugIds, String BirthDay, String patType, String isLiverWhole, String isKidneyWhole)
//    {
//        return drugsecuity.DrugSpecPeopleCheckS(DrugIds, BirthDay, patType, isLiverWhole, isKidneyWhole);
//    }
//
//    /* 重复成分审查 */
//    public TDrugSecurityRslt DrugIngredientCheck(TPatientOrder po)
//    {
//        TPatOrderDrug[] pods = po.getPatOrderDrugs();
//        String[] Drugs = new String[pods.length];
//        for (int i = 0; i < pods.length; i++)
//            Drugs[i] = pods[i].getDrugID();
//        return DrugIngredientCheckS(Drugs);
//    }

//    /**
//     * 无具体实现
//     */
//    public TDrugSecurityRslt DrugIngredientCheckS(String[] DrugIds)
//    {
//        return drugsecuity.DrugIngredientCheckS(DrugIds);
//    }
//    
//    /* 用药途径审查 */
//    public TDrugSecurityRslt DrugAdministrationCheck(TPatientOrder po)
//    {
//        TPatOrderDrug[] pods = po.getPatOrderDrugs();
//        String[] DrugIds = new String[pods.length];
//        String[] AdminIds = new String[pods.length];
//        for (int i = 0; i < pods.length; i++)
//        {
//        	TPatOrderDrug drug = pods[i];
//        	DrugIds[i] = drug.getDrugID();
//        	AdminIds[i] = drug.getAdministrationID();
//        }
//        return DrugAdministrationCheckS(DrugIds, AdminIds);
//    }
//    
//    /**
//     * 无具体实现
//     */
//    public TDrugSecurityRslt DrugAdministrationCheckS(String[] DrugIds, String[] AdminIds)
//    {
//        return drugsecuity.DrugAdministrationCheckS(DrugIds, AdminIds);
//    }
//
//    /* 过敏药物审查 */
//    public TDrugSecurityRslt DrugAllergenCheck(TPatientOrder po)
//    {
//        TPatOrderDrug[] pods = po.getPatOrderDrugs();
//        String[] DrugIds = new String[pods.length];
//        for (int i = 0; i < pods.length; i++)
//        {
//        	TPatOrderDrug drug = pods[i];
//        	DrugIds[i] = drug.getDrugID();
//        }
//        TPatOrderDrugSensitive[] podss = po.getPatOrderDrugSensitives();
//        String[] SensitIds = new String[podss.length];
//        for (int i = 0; i < podss.length; i++)
//        {
//        	SensitIds[i] = podss[i].getPatOrderDrugSensitiveID();
//        }
//        return DrugAllergenCheckS(DrugIds, SensitIds);
//    }
//
//    /**
//     * 无具体实现
//     */
//    public TDrugSecurityRslt DrugAllergenCheckS(String[] DrugIds, String[] SensitIds)
//    {
//        return drugsecuity.DrugAllergenCheckS(DrugIds, SensitIds);
//    }
    
//    /* 药物剂量审查 */
//    public TDrugSecurityRslt DrugDosageCheck(TPatientOrder po)
//    {
//        TPatOrderDrug[] pods = po.getPatOrderDrugs();
//        String[] DrugIds = new String[pods.length];
//    	String[] Dosages = new String[pods.length];
//    	String[] PerformFreqDictIds = new String[pods.length];
//		String[] StartDates = new String[pods.length];
//		String[] StopDates = new String[pods.length];
//        for (int i = 0; i < pods.length; i++)
//        {
//        	TPatOrderDrug drug = pods[i];
//        	DrugIds[i] = drug.getDrugID();
//        	Dosages[i] = drug.getDosage();
//        	PerformFreqDictIds[i] = drug.getPerformFreqDictID();
//        	StartDates[i] = drug.getStartDateTime();
//        	StopDates[i] = drug.getStopDateTime();
//        }
//        return DrugDosageCheckS(DrugIds, Dosages, PerformFreqDictIds, 
//        		StartDates, StopDates, po.getPatInfoExt().getWeight(), po.getPatInfoExt().getHeight(),
//        		po.getPatient().getDateOfBirth());
//    }
    
//    /**
//     * 无具体实现
//     * 多个本地药品码，剂量，x次/天，用药起始时间，用药中止时间（若为空，则按当天计算）,体重，身高，出生日期
//     */
//    public TDrugSecurityRslt DrugDosageCheckS(String[] DrugIds, String[] Dosages, String[] PerformFreqDictIds, 
//    		String[] StartDates, String[] StopDates, 
//    		String Weight, String Height, String BirthDay)
//    {
//        return drugsecuity.DrugDosageCheckS(DrugIds, Dosages, PerformFreqDictIds, StartDates, StopDates, Weight, Height, BirthDay);
//    }
//    
//    /* 异常信号审查 */
//    public TDrugSecurityRslt DrugSideCheck(TPatientOrder po)
//    {
//        TPatOrderDrug[] pods = po.getPatOrderDrugs();
//        String[] DrugIds = new String[pods.length];
//        String[] AdminIds = new String[pods.length];
//        for (int i = 0; i < pods.length; i++)
//        {
//        	TPatOrderDrug drug = pods[i];
//        	DrugIds[i] = drug.getDrugID();
//        	AdminIds[i] = drug.getAdministrationID();
//        }
//        TPatOrderDrugSensitive[] podss = po.getPatOrderDrugSensitives();
//        String[] SensitIds = new String[podss.length];
//        for (int i = 0; i < podss.length; i++)
//        {
//        	SensitIds[i] = podss[i].getPatOrderDrugSensitiveID();
//        }
//        return DrugSideCheckS(DrugIds, AdminIds, SensitIds);
//    }
//    
//    /**
//     * 无具体实现
//     */
//    public TDrugSecurityRslt DrugSideCheckS(String[] DrugIds, String[] AdminIds, String[] SensitIds)
//    {
//    	return drugsecuity.DrugSideCheckS(DrugIds, AdminIds, SensitIds);
//    }
    
//    /* 相互作用检查 */
//	@Override
//	public TDrugSecurityRslt MedicareChecker(TPatientOrder po) 
//	{
//		return null;
//	}
//	
//	@Override
//	public TDrugSecurityRslt MedicareCheckerS(String DrugID, String DiagnoseCode) 
//	{
//		return null;
//	}
	
    /**
     * 参数字符串及字符串数组
     * @param doctorInfo    -- doctorDeptID,doctorDeptName,doctorID,doctorName,doctorTitleID,doctorTitleName
     * @param patientInfo   -- patientID,name,sex,dateOfBirth,birthPlace,nation,isLact,isPregnant,insureanceType,__
     *                         insuranceNo,isLiverWhole,isKidneyWhole,isWorking,height,weight,visitID,inDept,inMode,__
     *                         inDate,outDept,outDate,outMode,patAdmCondition
     * @param drugInfo      -- {drugID,drugStandID,DrugName,recMainNo,recSubNo,dosage,doseUnits,administrationID,__
     *                          administrationStandID,performFreqDictID,performFreqDictStandID,performFreqDictText,startDateTime,__
     *                          stopDateTime,doctorDept,doctor,isGroup,firmID,isNew,dvaliddate,UseType,UseCause}
     * @param diagnosisInfo -- {diagnosisDictID}
     * @param sensitiveInfo -- {patOrderDrugSensitiveID,drugAllergenID}
     * @param PatSigns   -- {TWOK,TWValue,XXDate,XXOK,XXValue,CValue}
     * @return
     */
    public TDrugSecurityRslt DrugSecurityCheckS(String[] doctorInfo, String[] patientInfo, String[][] drugInfo, String[][] diagnosisInfo, String[][] sensitiveInfo,String[][] patSigns,String[] patOperation)
    {
    	//TODO 在此加上调试开关  
    	return DrugSecurityCheck(CommonUtils.getPatientOrder(doctorInfo, patientInfo, drugInfo, diagnosisInfo, sensitiveInfo, patSigns,patOperation));
    }
    
    @Resource(name="patientSaveCheckResult")
    private IPatientSaveCheckResult patientSavaBean;
    
    /**
     * 药物安全审查 全局检查函数，可以一次性检查所有的检查项目 PatientOrder (医嘱对象)
     * 改造完成
     * @return
     */
    public TDrugSecurityRslt DrugSecurityCheck(final TPatientOrder po)
    {
         return drugsecuity.DrugSecurityCheck(po);
    }
    
//    /**
//     * 审核方面无具体实现
//     */
//    @Override
//    public TDrugSecurityRslt DrugIvEffectCheckA(String[] doctorInfo,
//            String[] patientInfo, String[][] drugInfo,
//            String[][] diagnosisInfo, String[][] sensitiveInfo,String[][] patSigns,String[] patOperation)
//    {
//    	TPatientOrder      po = CommonUtils.getPatientOrder(doctorInfo, patientInfo, drugInfo, diagnosisInfo, sensitiveInfo, patSigns,patOperation);
//    	TDrugSecurityRslt dsr = drugsecuity.DrugIvEffectCheck(po);
//    	this.patientSavaBean.savePatientCheckInfo(po, dsr);
//    	this.patientSavaBean.saveDrugIvEffectCheckInfo(dsr);
//        return dsr;
//    }
//    /**
//     * 审核方面无具体实现
//     */
//    @Override
//    public TDrugSecurityRslt DrugSpecPeopleCheckA(String[] doctorInfo,
//            String[] patientInfo, String[][] drugInfo,
//            String[][] diagnosisInfo, String[][] sensitiveInfo,String[][] patSigns,String[] patOperation)
//    {
//    	TPatientOrder      po = CommonUtils.getPatientOrder(doctorInfo, patientInfo, drugInfo, diagnosisInfo, sensitiveInfo,patSigns,patOperation);
//    	TDrugSecurityRslt dsr = drugsecuity.DrugSpecPeopleCheck(po);
//    	this.patientSavaBean.savePatientCheckInfo(po, dsr);
//    	this.patientSavaBean.saveDrugSpecPeopleCheckInfo(dsr);
//    	return dsr; 
//    }
//    
//    /**
//     * 审核方面无具体实现
//     */
//    @Override
//    public TDrugSecurityRslt DrugIngredientCheckA(String[] doctorInfo,
//            String[] patientInfo, String[][] drugInfo,
//            String[][] diagnosisInfo, String[][] sensitiveInfo,String[][] patSigns,String[] patOperation)
//    {
//    	TPatientOrder      po = CommonUtils.getPatientOrder(doctorInfo, patientInfo, drugInfo, diagnosisInfo, sensitiveInfo, patSigns,patOperation);
//    	TDrugSecurityRslt dsr = drugsecuity.DrugIngredientCheck(po);
//    	this.patientSavaBean.savePatientCheckInfo(po, dsr);
//    	this.patientSavaBean.saveDrugIngredientCheckInfo(dsr);
//    	return dsr; 
//    }
//
//    /**
//     * 审核方面无具体实现
//     */
//    @Override
//    public TDrugSecurityRslt DrugAdministrationCheckA(String[] doctorInfo,
//            String[] patientInfo, String[][] drugInfo,
//            String[][] diagnosisInfo, String[][] sensitiveInfo,String[][] patSigns,String[] patOperation) 
//    {
//    	TPatientOrder      po = CommonUtils.getPatientOrder(doctorInfo, patientInfo, drugInfo, diagnosisInfo, sensitiveInfo, patSigns,patOperation);
//    	TDrugSecurityRslt dsr = drugsecuity.DrugAdministrationCheck(po);
//    	this.patientSavaBean.savePatientCheckInfo(po, dsr);
//    	this.patientSavaBean.saveDrugAdministrationCheckInfo(dsr);
//    	return dsr;
//    }
//
//    /**
//     * 审核方面无具体实现
//     */
//    @Override
//    public TDrugSecurityRslt DrugAllergenCheckA(String[] doctorInfo,
//            String[] patientInfo, String[][] drugInfo,
//            String[][] diagnosisInfo, String[][] sensitiveInfo,String[][] patSigns,String[] patOperation)
//    {
//    	TPatientOrder      po = CommonUtils.getPatientOrder(doctorInfo, patientInfo, drugInfo, diagnosisInfo, sensitiveInfo, patSigns,patOperation);
//    	TDrugSecurityRslt dsr = drugsecuity.DrugAllergenCheck(po);
//    	this.patientSavaBean.savePatientCheckInfo(po, dsr);
//    	this.patientSavaBean.saveDrugAllergenCheckInfo(dsr);
//    	return dsr;
//    }
//
//    /**
//     * 审核方面无具体实现
//     */
//    @Override
//    public TDrugSecurityRslt DrugDosageCheckA(String[] doctorInfo,
//            String[] patientInfo, String[][] drugInfo,
//            String[][] diagnosisInfo, String[][] sensitiveInfo,String[][] patSigns,String[] patOperation)
//    {
//    	TPatientOrder      po = CommonUtils.getPatientOrder(doctorInfo, patientInfo, drugInfo, diagnosisInfo, sensitiveInfo, patSigns,patOperation);
//    	TDrugSecurityRslt dsr = drugsecuity.DrugDosageCheck(po);
//    	this.patientSavaBean.savePatientCheckInfo(po, dsr);
//    	this.patientSavaBean.saveDrugDosageCheckInfo(dsr);
//    	return dsr;
//    }
//
//    /**
//     * 审核方面无具体实现
//     */
//    @Override
//    public TDrugSecurityRslt DrugSideCheckA(String[] doctorInfo,
//            String[] patientInfo, String[][] drugInfo,
//            String[][] diagnosisInfo, String[][] sensitiveInfo,String[][] patSigns,String[] patOperation)
//    {
//    	TPatientOrder      po = CommonUtils.getPatientOrder(doctorInfo, patientInfo, drugInfo, diagnosisInfo, sensitiveInfo, patSigns,patOperation);
//    	TDrugSecurityRslt dsr = drugsecuity.DrugSideCheck(po);
//    	this.patientSavaBean.savePatientCheckInfo(po, dsr);
//    	this.patientSavaBean.saveDrugSideCheckInfo(dsr);
//    	return dsr;
//    }
//    
//    /**
//     * 审核方面改造完成
//     */
//    @Override
//    public TDrugSecurityRslt DrugInteractionCheckA(String[] doctorInfo,
//            String[] patientInfo, String[][] drugInfo,
//            String[][] diagnosisInfo, String[][] sensitiveInfo,String[][] patSigns,String[] patOperation)
//    {
//    	TPatientOrder  po = CommonUtils.getPatientOrder(doctorInfo, patientInfo, drugInfo, diagnosisInfo, sensitiveInfo, patSigns,patOperation);
//    	TDrugSecurityRslt dsr = drugsecuity.DrugInteractionCheck(po);
//    	this.patientSavaBean.savePatientCheckInfo(po, dsr);
//    	this.patientSavaBean.saveDrugInteractionCheckInfo(dsr);
//    	return dsr;
//    }
//    
//    /**
//     * 审核方面改造完成
//     */
//    @Override
//    public TDrugSecurityRslt DrugDiagCheckA(String[] doctorInfo,
//            String[] patientInfo, String[][] drugInfo,
//            String[][] diagnosisInfo, String[][] sensitiveInfo,String[][] patSigns,String[] patOperation)
//    {
//    	TPatientOrder po = CommonUtils.getPatientOrder(doctorInfo, patientInfo, drugInfo, diagnosisInfo, sensitiveInfo, patSigns,patOperation);
//    	TDrugSecurityRslt dsr;
//		try {
//			dsr = drugsecuity.DrugDiagCheck(po);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return null;
//		}
//    	this.patientSavaBean.savePatientCheckInfo(po, dsr);
//    	this.patientSavaBean.saveDrugDiagCheckInfo(dsr);
//    	return dsr;
//    }
//    
//    /**
//     * 无具体实现
//     */
//	@Override
//	public TDrugSecurityRslt MedicareCheckerA(String[] doctorInfo,
//			String[] patientInfo, String[][] drugInfo,
//			String[][] diagnosisInfo, String[][] sensitiveInfo,String[][] patSigns,String[] patOperation)
//	{
//		TPatientOrder po = CommonUtils.getPatientOrder(doctorInfo, patientInfo, drugInfo, diagnosisInfo, sensitiveInfo, patSigns,patOperation);
//    	TDrugSecurityRslt dsr = drugsecuity.MedicareChecker(po);
//    	return dsr;
//	}
	
	/**
	 * 返回要保存的ip 
	 * @return
	 */
	private String getRemoteAddrIp()
	{
	    
	    Message message = PhaseInterceptorChain.getCurrentMessage();
	    HttpServletRequest httpReq = (HttpServletRequest)message.get(AbstractHTTPDestination.HTTP_REQUEST);
	    String ip = httpReq.getRemoteAddr() ;
//	    String ip = this.WebClientIP;
//	    if(XFireServletController.getRequest() != null) ip = XFireServletController.getRequest().getRemoteAddr();
	    return ip;
	}

	@Resource(name="antiDrugAuditorBean")
	private IAntiDrugAuditor antiDrugAuditorBean;
	
	/**
	 * 获取审核和登记开关
	 * @param DrugDoctorInfo 药品代码、名称、剂型、规格、厂家、数量、给药途径、医生姓名、职称、科室代码
	 * @return 审核、登记开关
	 */
	@Override
	public String[] getAntiDrugCheckRule(String[] DrugDoctorInfo)
	{
		return antiDrugAuditorBean.getAntiDrugCheckRule(DrugDoctorInfo);
	}
	

	public static void main(String[] args) {
//		Integer[] s =new Integer[]{1,-33,2,4,3,3,5,666,77};
//		JSONArray j=JSONArray.fromObject(s);
//		System.out.println(j.toString());
		
		TPatVitalSigns p1 = new TPatVitalSigns();
		p1.setPatid("1111");
		p1.setBloodLow("1");
		TPatVitalSigns p2 = new TPatVitalSigns();
		p2.setPatid("2222");
		p2.setBloodLow("222222");
		TPatVitalSigns[] dd =new TPatVitalSigns[]{p1,p2};
		JSONArray w=JSONArray.fromObject(dd);
		System.out.println(w.toString());
		
		try {
			//new HisAuditorController().getObject(w.toString(), TPatVitalSigns.class, 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	
}
}