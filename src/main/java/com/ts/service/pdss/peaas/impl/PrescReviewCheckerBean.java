package com.ts.service.pdss.peaas.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hitzd.DBUtils.CommonMapper;
import com.hitzd.DBUtils.JDBCQueryImpl;
import com.hitzd.DBUtils.TCommonRecord;
import com.hitzd.Factory.DBQueryFactory;
import com.hitzd.his.Beans.TPatOrderDiagnosis;
import com.hitzd.his.Beans.TPatOrderDrug;
import com.hitzd.his.Beans.TPatient;
import com.hitzd.his.Beans.TPatientOrder;
import com.hitzd.his.Utils.Config;
import com.hitzd.his.Utils.DictCache;
import com.hitzd.persistent.Persistent4DB;
import com.ts.entity.pdss.pdss.RSBeans.TAdministrationRslt;
import com.ts.entity.pdss.pdss.RSBeans.TCheckResult;
import com.ts.entity.pdss.pdss.RSBeans.TDrugDiagRslt;
import com.ts.entity.pdss.pdss.RSBeans.TDrugDosageRslt;
import com.ts.entity.pdss.pdss.RSBeans.TDrugHarmfulRslt;
import com.ts.entity.pdss.pdss.RSBeans.TDrugInteractionRslt;
import com.ts.entity.pdss.pdss.RSBeans.TDrugIvEffectRslt;
import com.ts.entity.pdss.pdss.RSBeans.TDrugSecurityRslt;
import com.ts.service.IReviewResultServ;
import com.ts.service.IWhiteAndBlackFilterServ;
import com.ts.service.pdss.pdss.manager.IDrugSecurityChecker;
import com.ts.service.pdss.pdss.manager.IPatientSaveCheckResult;
import com.ts.service.pdss.peaas.Utils.DrugUseDate;
import com.ts.service.pdss.peaas.manager.IPrescReviewChecker;

@Service
public class PrescReviewCheckerBean extends Persistent4DB implements IPrescReviewChecker {
    /* 审查功能*/
    @Resource(name = "drugSecurityChecker")
    private IDrugSecurityChecker drugSC;

    /* 保存审查结果 */
    @Resource(name = "patientSaveCheckResult")
    private IPatientSaveCheckResult patSaveCRLT;

    /**
     * 黑白名单校验
     */
    @Resource(name = "whiteAndWhiteFilterBean")
    private IWhiteAndBlackFilterServ whiteAndBlackFilterService;

    @Resource(name = "reviewResultService")
    private IReviewResultServ reviewResultService;

    /* 主键 */
    private String prescIDUUID = "";
    /* 问题sql总录入 */
    private List<String> sqls = new ArrayList<String>();

    /**
     * 点评方法
     *
     * @param t           处方信息
     * @param prescDetail 处方具体信息
     */
    @Override
    public TCommonRecord PrescReviewCheck(TCommonRecord t, List<TCommonRecord> prescDetail) {
        prescIDUUID = UUID.randomUUID().toString() + "_Presc";

        sqls = new ArrayList<String>();
        TCommonRecord tCom = new TCommonRecord();
        /* 不规范处方 */
        TCommonRecord NoNormCom = PrescNoNormCheck(t, prescDetail);
        for (String s : NoNormCom.getKeys()) {
            tCom.set(s, NoNormCom.get(s));
        }

        /* 将处方组成医嘱进行 */
        TPatientOrder po = getPatientOrder(t, prescDetail);

        /* 用药不适宜处方 */
        TCommonRecord SecurityCom = PrescSecurityCheck(po);
        for (String s : SecurityCom.getKeys()) {
            tCom.set(s, SecurityCom.get(s));
        }

        /* 超常处方 */
        TCommonRecord ExtraorCom = PrescExtraordinaryCheck(po);
        for (String s : ExtraorCom.getKeys()) {
            tCom.set(s, ExtraorCom.get(s));
        }
        return tCom;
    }

    /**
     *
     * @param t
     * @param prescDetail
     * @return
     */
    private TPatientOrder getPatientOrder(TCommonRecord t, List<TCommonRecord> prescDetail) {
        TPatientOrder po = new TPatientOrder();
        po.setPatientID(t.get("patient_Id") + "_presc");
        po.setDoctorDeptID(t.get("org_code"));
        po.setDoctorDeptName(t.get("org_name"));
        po.setDoctorID(t.get("doctor_code"));
        po.setDoctorName(t.get("doctor_name"));
        TPatient p = new TPatient();
        po.setPatient(p);
        /* 诊断信息   */
        String[] diagnosis = t.get("DIAGNOSIS_Codes").split(";");
        List<TPatOrderDiagnosis> podiags = new ArrayList<TPatOrderDiagnosis>();
        if (diagnosis != null) {
            for (String d : diagnosis) {
                TPatOrderDiagnosis podiag = new TPatOrderDiagnosis();
                podiag.setDiagnosisDictID(d);
                podiag.setDiagnosisName(d);
                podiags.add(podiag);
            }
        }
        po.setPatOrderDiagnosiss((TPatOrderDiagnosis[]) podiags.toArray(new TPatOrderDiagnosis[0]));
        /* 医嘱组织 */
        List<TPatOrderDrug> pods = new ArrayList<TPatOrderDrug>();
        int orderMainNo = 0;
        for (TCommonRecord td : prescDetail) {
            TPatOrderDrug pod = new TPatOrderDrug();
            // 药品ID
            pod.setDrugID(td.get("drug_code"));
            /* 药品名称 */
            pod.setDrugName(td.get("drug_name"));
            /* 护士名称 */
            pod.setDoctorName(t.get("doctor_name"));
            // 医嘱序号
            pod.setRecMainNo(td.get("order_no"));
            // 医嘱子序号
            pod.setRecSubNo(td.get("order_sub_no"));
            String orderno = td.get("order_no");
            if ("".equals(orderno)) {
                orderMainNo++;
                pod.setRecMainNo(String.valueOf(orderMainNo));
                pod.setRecSubNo("1");
            } else {
                if ("".equals(td.get("order_sub_no"))) {
                    pod.setRecSubNo("1");
                }
            }
            // 一次使用剂量
            pod.setDosage(td.get("dosage"));
            // 剂量单位
            pod.setDoseUnits(td.get("dosage_Units"));
            // 给药途径ID
            pod.setAdministrationID(td.get("administration"));
            /* 给药途径名称 */
            pod.setAdminName(td.get("administration"));
            // 执行频率ID
            pod.setPerformFreqDictID(td.get("Frequency"));
            // 执行执行频率文本(输入执行频率描述)
            pod.setPerformFreqDictText(td.get("Frequency"));
            // 开始日期时间
            pod.setStartDateTime(td.get(""));
            // 结束日期时间
            pod.setStopDateTime(td.get(""));
            // 开具医嘱科室
            pod.setDoctorDept(t.get("org_name"));
            // 开具医嘱医生
            pod.setDoctor(t.get("doctor_name"));
            // 药品厂商ID
            pod.setFirmID(td.get("FirmID"));
            pods.add(pod);
        }
        po.setPatOrderDrugs((TPatOrderDrug[]) pods.toArray((new TPatOrderDrug[0])));
        return po;
    }

    /**
     * 第十七条 有下列情况之一的，应当判定为不规范处方：
     * （一）    处方的前记、正文、后记内容缺项，书写不规范或者字迹难以辨认的；
     * （二）    医师签名、签章不规范或者与签名、签章的留样不一致的；
     * （三）     药师未对处方进行适宜性审核的（处方后记的审核、调配、核对、发药栏目无审核调配药师及核对发药药师签名，或者单人值班调剂未执行双签名规定）；
     * （四）     新生儿、婴幼儿处方未写明日、月龄的；
     * （五）     西药、中成药与中药饮片未分别开具处方的；
     * （六）     未使用药品规范名称开具处方的；
     * （七）     药品的剂量、规格、数量、单位等书写不规范或不清楚的；
     * （八）     用法、用量使用“遵医嘱”、“自用”等含糊不清字句的；
     * （九）     处方修改未签名并注明修改日期，或药品超剂量使用未注明原因和再次签名的；
     * （十）     开具处方未写临床诊断或临床诊断书写不全的；
     * （十一）单张门急诊处方超过五种药品的；
     * （十二）无特殊情况下，门诊处方超过7日用量，急诊处方超过3日用量，慢性病、老年病或特殊情况下需要适当延长处方用量未注明理由的；
     * （十三）开具麻醉药品、精神药品、医疗用毒性药品、放射性药品等特殊管理药品处方未执行国家有关规定的；
     * （十四）医师未按照抗菌药物临床应用管理规定开具抗菌药物处方的;
     * （十五）中药饮片处方药物未按照“君、臣、佐、使”的顺序排列，或未按要求标注药物调剂、煎煮等特殊要求的。
     */
    private TCommonRecord PrescNoNormCheck(TCommonRecord p, List<TCommonRecord> presc) {
        TCommonRecord tCom = new TCommonRecord();// 用于存储错误信息
        /* 记录处方药品数 */
        int prescDrugCount = 0;
        /* 药品的最大使用天数*/
        
        int maxUseDay = 0;
        /* 1-7 用法有误 */
        boolean chk1_7 = true;

        /* 1-12 无特殊情况下 用量信息 用量信息超标*/
        List<TCommonRecord> reviewErrorList = new ArrayList<TCommonRecord>();
        /*审核第1-5项*/
        String[] IC = new String[3];

        /* 1-10 校验 */
        prescCheck1_10(p, tCom);
        /* 2-7 重复用药 */
        prescCheck2_7(presc, tCom);

        for (TCommonRecord t : presc) {
            /* 1-8  */
            prescCheck1_8(t, tCom);
            
            prescCheck1_14(p, t, tCom);

            /* 统计出现 分类 1-5 */
            String itemclass = t.get("item_class");
            String[] whiteCheckString = new String[]{
                    t.get("drug_name")
            };
            if (whiteAndBlackFilterService.whiteAssert("1-5", whiteCheckString)) {
                continue;//校验白名单
            } else if ("A".equals(itemclass)) {
                IC[0] = "A";
            } else if ("B".equals(itemclass)) {
                IC[1] = "B";
            } else {
                IC[2] = "C";
            }

            /* 1-11  */
            if ("1".equals(t.get("ORDER_TYPE"))) {
                prescDrugCount++;
            }
            /**
             * 存储药品使用日期
             */
            DrugUseDate dud = new DrugUseDate();
            int days = dud.getPrescUseDate(t);
            
            maxUseDay = maxUseDay<days?days:maxUseDay;
            //保存结果至处方详细中
            saveDrugUseDays(t,days+"");
            
            /* 1-12 急诊科室标示   一盒 以上和 抗菌药物进行审核、真有意思的1-12*/ 
            if (t.getInt("amount") > 1) {
                //药品过滤
                if (whiteAndBlackFilterService.whiteAssert("1-12", new String[]{t.get("drug_code")})) continue;//1-12 药品过滤
                String[] diag = p.get("diagnosis_names").split(";");//诊断
                boolean isSlowDiag = false;
                for(int i=0; i < diag.length && !isSlowDiag; i++){
                    if (whiteAndBlackFilterService.whiteAssert("1-12", new String[]{diag[i]})) isSlowDiag=true;
                }

                if (whiteAndBlackFilterService.whiteAssert("1-12", new String[]{t.get(p.get(""))})) continue;//1-12 药品过滤
                if (whiteAndBlackFilterService.blackAssert("1-12", new String[]{t.get("drug_code")})) {
                	//
                }
                
                /**
                 * 与院方提供的days，进行比较， 选择较大者。
                 */
//                int drugDays = (int) getDrugDaysFromHosptal(t.get("presc_no"), t.get("drug_code"), t.get("amount"));//98医院
                int drugDays = 0;
                System.out.println("计算出的日期 ： "+ days + "   ############药品日期###########");
                if (drugDays > days) {
                    days = drugDays;
                }
                
                int litterDays = days/t.getInt("amount")*(t.getInt("amount")-1);
                int slowDiease = whiteAndBlackFilterService.get1_12CheckDay("1");
                int hurryDiease = whiteAndBlackFilterService.get1_12CheckDay("2");
                int normalDiease = whiteAndBlackFilterService.get1_12CheckDay("3");

                if(isSlowDiag){ //慢性病
                    if(litterDays > slowDiease){
                        t.set("exceedDay", (days - slowDiease) + "");
                        reviewErrorList.add(t);
                    }
                }else if ((Config.getParamValue("EmergencyPRESC") + ",").indexOf(p.get("org_code") + ",") != -1) {
                    if (litterDays > hurryDiease) { //急诊
                        t.set("exceedDay", (days - hurryDiease) + "");
                        reviewErrorList.add(t);
                    }
                } else {   //普通门诊
                    if (litterDays > normalDiease) {
                        t.set("exceedDay", (days - normalDiease) + "");
                        reviewErrorList.add(t);
                    }
                }
            }
            /* 1-7
            if(dosageUnit && "A".equals(itemclass))
            {
                dosageUnit = getDosageUnits(t.get("DOSAGE_UNITS"));
            }
            */
        }
        /*（五）西药、中成药与中药饮片未分别开具处方的; */
        prescCheck1_5(IC, tCom);
        /**
         * 检查1_12药物用量
         */
        prescCheck1_12(reviewErrorList, tCom);

        /* （七）     药品的剂量、规格、数量、单位等书写不规范或不清楚的;
        if(!dosageUnit)
        {
            tCom.set("1-7", "1-7");
        }
        */
        /*
         * （十一）单张门急诊处方超过五种药品的;
        if(prescDrugCount > 5 )
        {
            tCom.set("1-11", "1-11");
        }
        */
        	
        /**
         * 在tCom 在放入最大使用天数 
         */
        tCom.set("maxuseday", maxUseDay+"");
        
        return tCom;
    }

    /**
     * @param prescNo  处方号
     * @param drugCode 药品号
     * @param quantity 数量
     * @return 天数
     */
    private double getDrugDaysFromHosptal(String prescNo, String drugCode, String quantity) {
        JDBCQueryImpl query = DBQueryFactory.getQuery("HIS");
        try {
            String sql = "SELECT" +
                    "  drug_days " +
                    " FROM v_dcdt_drug_presc_detail " +
                    " WHERE presc_no = ? AND drug_code = ? AND quantity = ? ";
            List<TCommonRecord> drugDays = (List<TCommonRecord>) query.query(sql, new Object[]{prescNo, drugCode, quantity}, new CommonMapper());
            if (drugDays.size() > 0) {
                return drugDays.get(0).getDouble("drug_days");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            query = null;
        }
        return 0;
    }

    /**
     * 判断错误信息,并将错误信息放入放回信息中
     *
     * @param reviewErrorCode 错误编码
     * @param errorMessage    错误信息
     * @param returnMessage   返回封装
     */
    private void setReturnMessage(String reviewErrorCode, String errorMessage, TCommonRecord returnMessage) {
        if (!"".equals(errorMessage)) {
            returnMessage.set(reviewErrorCode, errorMessage);
        }
    }


    /**
     * 审核1-5项
     */
    private void prescCheck1_5(String[] resultItem, TCommonRecord returnMessage) {
        String reviewErrorCode = "1-5";
        String errorMessage = "";
        if ("A".equals(resultItem[0]) && "C".equals(resultItem[2]) || "C".equals(resultItem[2]) && "B".equals(resultItem[1])) {
            errorMessage += "该处方中,西药、中成药与中药饮片未分别开具处方的.<br>";
        }
        setReturnMessage(reviewErrorCode, errorMessage, returnMessage);
    }

    /**
     * 该部分审核 1-8
     * （八）用法、用量使用“遵医嘱”、“自用”等含糊不清字句的；
     *
     * @param t
     */
    public void prescCheck1_8(TCommonRecord t, TCommonRecord returnMessage)
    {
        String reviewErrorCode = "1-8";
        String errorMessage = "";
        String[] whiteCheckString = new String[] { t.get("administration"),
                t.get("dosage_units") };
        if (whiteAndBlackFilterService.whiteAssert(reviewErrorCode,
                whiteCheckString))
            return;// 校验白名单
        if (whiteAndBlackFilterService.blackAssert(reviewErrorCode,
                whiteCheckString))
        {// 黑名单校验
            errorMessage += (t.get("drug_name") + ": 黑名单记录<br>");
        }
        else if (t.get("dosage_units").indexOf("适量") != -1)
        {
            errorMessage += t.get("drug_name") + ": 使用\"适量\"模糊不清的字句<br>";
        }
        else if (t.get("dosage_units").indexOf("盒") != -1)
        {
            errorMessage += t.get("drug_name") + ": 使用\"盒\"模糊不清的字句<br>";
        }
        setReturnMessage(reviewErrorCode, errorMessage, returnMessage);
    }
    
    /**
     * 1-14
     * （十四）医师未按照抗菌药物临床应用管理规定开具抗菌药物处方的;
     * @param t
     */
    public void prescCheck1_14(TCommonRecord p,TCommonRecord t, TCommonRecord returnMessage)
    {
    	if(!"1".equalsIgnoreCase(t.get("antidrugflag")))return;//判断是否是抗菌药
        String reviewErrorCode = "1-14";
        String errorMessage = "";
        String doctorName = p.get("doctor_name");
        DictCache dictCache = DictCache.getNewInstance();
        TCommonRecord doctor = dictCache.getDoctorInfo(DBQueryFactory.getQuery("HIS"), doctorName);
        String title = doctor.get("title");
        List<String> masterDoctors = new ArrayList<String>();
        masterDoctors.add("主任医师");
        masterDoctors.add("副主任医师");
        masterDoctors.add("主任医师");
        masterDoctors.add("教授");
        masterDoctors.add("副教授");
        
        List<String> zhuzhiDoctors = new ArrayList<String>();
        zhuzhiDoctors.add("主治医师");
        zhuzhiDoctors.add("副主治医师");
        zhuzhiDoctors.add("教授");
        zhuzhiDoctors.add("副教授");
        
        if(masterDoctors.contains(title))
        {
        	//主任医师
        	//nothing
        }
        else if(zhuzhiDoctors.contains(title))
        {
        	//主治医师
        	String sql = "select anti_level from pdss.drug_map m where m.is_anti='1' and m.drug_no_local='"+t.get("drug_code")+"'";
        	List<TCommonRecord> antiLevels = DBQueryFactory.getQuery("PDSS").query(sql, new CommonMapper());
        	String antiLevel = "1";
        	if(antiLevels != null && antiLevels.size()>0){
        		antiLevel = antiLevels.get(0).get("anti_level");
        	}
        	if("3".equals(antiLevel))
        	{
        		errorMessage = "医生：" + doctorName + "  职称： " + title  + " 越级使用抗菌药： " + t.get("drug_name");
        	}
        }
        else
        {
        	//普通医师
        	//主治医师
        	String sql = "select anti_level from pdss.drug_map m where m.is_anti='1' and m.drug_no_local='"+t.get("drug_code")+"'";
        	List<TCommonRecord> antiLevels = DBQueryFactory.getQuery("PDSS").query(sql, new CommonMapper());
        	String antiLevel = "1";
        	if(antiLevels != null && antiLevels.size()>0)
        	{
        		antiLevel = antiLevels.get(0).get("anti_level");
        	}
        	if("2".equals(antiLevel) || "3".equals(antiLevel))
        	{
        		errorMessage = "医生：" + doctorName + "  职称： " + title  + " 越级使用抗菌药： " + t.get("drug_name");
        	}
        }
        setReturnMessage(reviewErrorCode, errorMessage, returnMessage);
    }

    /**
     * 该部分审核 1-8
     * （八）用法、用量使用“遵医嘱”、“自用”等含糊不清字句的；
     *
     * @param t
     */
    public boolean PrescCheck1_8(TCommonRecord t)
    {
        boolean rslt = false;
        if (t.get("dosage_units").indexOf("适量") != -1) return true;
        // if(t.get("dosage_units").indexOf("支") != -1) return true;
        if (t.get("dosage_units").indexOf("盒") != -1) return true;
        return rslt;
    }

    /**
     * 该部分审核  1-10
     * （十）     开具处方未写临床诊断或临床诊断书写不全的；
     *
     * @param p
     */

    public void prescCheck1_10(TCommonRecord p, TCommonRecord returnMessage)
    {
        String reviewErrorCode = "1-10";
        // 错误信息
        String errorMessage = "";
        DictCache dc = DictCache.getNewInstance();
        String diag = p.get("diagnosis_names");
        if (!"".equals(diag))
        {
            String[] diags = diag.split(";");
            for (String s : diags)
            {
                if (s == null || "".equals(s)) continue;
                if (whiteAndBlackFilterService.whiteAssert(reviewErrorCode,new String[] { s })) continue;// 校验白名单
                if (whiteAndBlackFilterService.blackAssert(reviewErrorCode,new String[] { s }))
                {
                    // 黑名单校验
                    errorMessage += (s + ": 黑名单记录<br>");
                    continue;
                }
                TCommonRecord tCom = dc.getDiagnosisByName(s.trim());
                if (!"".equals(tCom.get("diagnosis_code")))
                    continue; // 诊断信息码信息
                errorMessage += (s + ":非标准诊断信息.<br>");
            }
        }
        else
        {
            errorMessage += "该处方缺少诊断信息.<br>";

        }
        setReturnMessage(reviewErrorCode, errorMessage, returnMessage);
    }

    public boolean PrescCheck1_10(TCommonRecord p)
    {
        String reviewErrorCode = "1-10";
        boolean rslt = false;
        try
        {
            DictCache dc = DictCache.getNewInstance();
            String diag = p.get("diagnosis_names");
            if ("".equals(diag))
                return false;
            String PromDiag = "";
            if (!rslt)
            {
                String[] diags = diag.split(";");
                for (String s : diags)
                {

                    TCommonRecord tCom = dc.getDiagnosisByName(s.trim());
                    if (!"".equals(tCom.get("diagnosis_code")))
                        continue;
                    PromDiag = s + ";";
                    rslt = true;
                }
            }
            if (rslt)
            {

                String sql = "保存信息 " + PromDiag;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
        return rslt;
    }

    /**
     * 审核1-12:无特殊情况下，门诊处方超过7日用量，急诊处方超过3日用量，慢性病、老年病或特殊情况下需要适当延长处方用量未注明理由的；
     * 在审核中审计算了超出天数.TCommonRecord.exceedDay
     *
     * @param reviewErrorList 药物超量列表
     * @param returnMessage   返回的信息封装包
     */
    private void prescCheck1_12(List<TCommonRecord> reviewErrorList,
            TCommonRecord returnMessage)
    {
        String reviewErrorCode = "1-12";
        // 错误信息
        String errorMessage = "";
        for (TCommonRecord emergency : reviewErrorList)
        {
            errorMessage += emergency.get("drug_name") + ": 超出标准用量 "    + emergency.get("exceedDay") + "天<br>";
            System.out.println(emergency.get("drug_name") + ": 超出标准用量 " + emergency.get("exceedDay") + "天$$$$$$$$$");
        }
        setReturnMessage(reviewErrorCode, errorMessage, returnMessage);
    }

    private static List<String> units = new LinkedList<String>();

    static {
        units.add("g");
        units.add("mg");
        units.add("μg");
        units.add("n");
        units.add("l");
        units.add("ml");
        units.add("iu");
        units.add("u");
        units.add("片");
        units.add("丸");
        units.add("粒");
        units.add("袋");
        units.add("支");
        units.add("瓶");
        units.add("盒");
    }

    /**
     * （七）     药品的剂量、规格、数量、单位等书写不规范或不清楚的；
     * 　药品剂量与数量用阿拉伯数字书写。
     * 剂量应当使用法定剂量单位：重量以克（g）、毫克（mg）、微克（μg）、纳克（ng）为单位；容量以升（L）、毫升（ml）为单位；国际单位（IU）、   单位（U）；
     * 中药饮片以克（g）为单位。
     * 片剂、丸剂、胶囊剂、颗粒剂分别以片、丸、粒、袋为单位；
     * 溶液剂以支、瓶为单位；
     * 软膏及乳膏剂以支、盒为单位；
     * 注射剂以支、瓶为单位，应当注明含量；
     * 中药饮片以剂为单位。
     *
     * @param Unit
     * @return
     */
    private boolean getDosageUnits(String Unit)
    {
        boolean result = true;
        if (!units.contains(Unit.toLowerCase()))
            result = false;
        return result;
    }

    /**
     *
     * 第十八条 有下列情况之一的，应当判定为用药不适宜处方：
     * （一）适应证不适宜的；
     * （二）遴选的药品不适宜的；
     * （三）药品剂型或给药途径不适宜的；
     * （四）无正当理由不首选国家基本药物的；
     * （五）用法、用量不适宜的；
     * （六）联合用药不适宜的；
     * （七）重复给药的；
     * （八）有配伍禁忌或者不良相互作用的；
     * （九）其它用药不适宜情况的。
     */
    private TCommonRecord PrescSecurityCheck(TPatientOrder po) 
    {
        TCommonRecord tCom = new TCommonRecord();
        TDrugSecurityRslt dsr = drugSC.DrugSecurityCheck(po);
        TCheckResult[] tcrs = dsr.getCheckResults();
        /* 关联id */
        String uuid = prescIDUUID;
        tCom.set("uuid", uuid);
        patSaveCRLT.setNgroupnum(uuid);
        /* 互动信息*/
        patSaveCRLT.saveDrugInteractionCheckInfo(dsr);
        /* 配伍信息 */
        patSaveCRLT.saveDrugIvEffectCheckInfo(dsr);
        /* 禁忌症审查 */
        patSaveCRLT.saveDrugDiagCheckInfo(dsr);
        /* 重复成份审*/
//        patSaveCRLT.saveDrugIngredientCheckInfo( dsr);
        /* 用药途径审查  */
        patSaveCRLT.saveDrugAdministrationCheckInfo(dsr);
        /* 药物剂量审查 */
        patSaveCRLT.saveDrugDosageCheckInfo(dsr);
        /* 异常信号审查   */
        patSaveCRLT.saveDrugSideCheckInfo(dsr);
        for (int i = 0; i < tcrs.length; i++) 
        {
            TCheckResult crt = tcrs[i];
            /*  药品相互作用审查结果对象  */
            TDrugInteractionRslt[] drugInters = crt.getDrugInteractionRslt();
            if (drugInters != null && drugInters.length > 0)
            {
                boolean flag = false;
                for (int ii = 0; ii < drugInters.length; ii++)
                {
                    if ("R".equals(drugInters[ii].getAlertLevel()))
                    {
                        flag = true;
                        break;
                    }
                }
                if (flag)
                {
                    tCom.set("2-8", "2-8");//相互作用问题
                }
            }
            /* 药物重复成份审查结果对象 */
//            TDrugIngredientRslt[] drugingRslt = crt.getDrugIngredientRslt();
//            if(drugingRslt !=  null && drugingRslt.length > 0 )
//            {
//                tCom.set("2-7", "2-7");//药物重复成份问题
//            }
            /* 药品配伍审查结果对象  */
            TDrugIvEffectRslt[] drugIvefRslt = crt.getDrugIvEffectRslt();
            if (drugIvefRslt != null && drugIvefRslt.length > 0)
            {
                tCom.set("2-8", "2-8");//药品配伍问题
            }
            /* 药品禁忌审查结果对象 */
            TDrugDiagRslt[] drugdiagRslt = crt.getDrugDiagRslt();
            if (drugdiagRslt != null && drugdiagRslt.length > 0)
            {
                tCom.set("2-1", "2-1");//药品禁忌问题
            }
            /* 药物给药途径审查结果对象 */
            TAdministrationRslt[] asRslt = crt.getAdministrationRslt();
            if (asRslt != null && asRslt.length > 0) 
            {
                boolean flag = false;
                for (int ii = 0; ii < asRslt.length; ii++)
                {
                    if (!"G".equals(asRslt[ii].getAlertLevel()))
                    {
                        flag = true;
                        break;
                    }
                }
                if (flag) tCom.set("2-3", "2-3");     //药物给药途径问题
            }
            /* 药物剂量审查结果对象 */
            TDrugDosageRslt[] ddRslt = crt.getDrugDosageRslt();
            if (ddRslt != null && ddRslt.length > 0)
            {
                tCom.set("2-5", "2-5");//药物剂量问题
            }
            /* 不良反应审查结果对象  */
            TDrugHarmfulRslt[] dhfRslt = crt.getSideRslt();
            if (dhfRslt != null && dhfRslt.length > 0) 
            {
                tCom.set("2-2", "2-2");//不良反应问题
            }
        }
        return tCom;
    }

    /**
     * 该审核未 2-7
     * 重复用药
     *
     * @param prescDetail
     * @return
     */
    private void prescCheck2_7(List<TCommonRecord> prescDetail, TCommonRecord returnMessage)
    {
        // 重复用药白名单校验
        String reviewLevelCode = "2-7";
        String errorMessage = "";

        Map<String, Integer> counter = new HashMap<String, Integer>();// 计数器

        Map<String, String> drugNames = new HashMap<String, String>(); // 药品名称
        for (TCommonRecord t : prescDetail)
        {
            if (whiteAndBlackFilterService.whiteAssert(reviewLevelCode,
                    new String[] { t.get("drug_code") }))
                continue;
            if (whiteAndBlackFilterService.blackAssert(reviewLevelCode,
                    new String[] { t.get("drug_code") }))
            {
                errorMessage += t.get("drug_name") + ": 来源于黑名单.<br>";
                continue;
            }
            String key = t.get("drug_code") + t.get("drug_spec")
                    + t.get("adminstration");// 重复用药，条件为 药物编码 + 剂型 + 给药途径
            if (!counter.containsKey(key))
            {
                counter.put(key, 0);
                drugNames.put(key, t.get("drug_name"));
            }
            counter.put(key, counter.get(key) + 1);
        }

        for (String keyCode : counter.keySet())
        {
            Integer i = counter.get(keyCode);
            if (i > 1)
            {
                errorMessage += drugNames.get(keyCode) + ": 出现了重复用药.<br>";
            }
        }
        setReturnMessage(reviewLevelCode, errorMessage, returnMessage);
    }

    /**
     * 该审核未 2-7
     * 重复用药
     *
     * @param prescDetail
     * @return
     */
    @Deprecated
    private boolean PrescCheck2_7(List<TCommonRecord> prescDetail)
    {
        String f = "1801002LQ5,1801002LQ6,1703001LQ6,1703001LQ3,1802010IJ0,1802010LQ3,1802010LQ2,1802010IJ1,1802010LQ8"
                + ",1802010LQ9,1801001LQ2,1801001LQ0,1801001IJ0,1801002LQ6,1801002LQ5,1801001LQ1";
        boolean rslt = false;
        Map<String, Integer> Counter = new HashMap<String, Integer>();
        for (TCommonRecord t : prescDetail)
        {
            if (f.indexOf(t.get("drug_code")) != -1)
                continue;
            if (!Counter.containsKey(t.get("drug_code") + t.get("drug_spec")))
            {
                Counter.put(t.get("drug_code") + t.get("drug_spec"), 0);
            }
            Counter.put(t.get("drug_code") + t.get("drug_spec"),
                    Counter.get(t.get("drug_code") + t.get("drug_spec")) + 1);
        }
        for (String s : Counter.keySet())
        {
            Integer i = Counter.get(s);
            if (i > 1)
            {
                String sql = "" + s;
                rslt = true;
            }
        }
        return rslt;
    }

    /**
     * 第十九条 有下列情况之一的，应当判定为超常处方：
     * 1.无适应证用药；
     * 2.无正当理由开具高价药的；
     * 3.无正当理由超说明书用药的；
     * 4.无正当理由为同一患者同时开具2种以上药理作用相同药物的。
     */
    private TCommonRecord PrescExtraordinaryCheck(TPatientOrder po)
    {
        TCommonRecord tCom = new TCommonRecord();
        return tCom;
    }
    
    private void saveDrugUseDays(TCommonRecord prescDetail,String days) {
    	String sql = "update presc_detail set drug_use_days='" + days + "' where presc_id='" + prescDetail.get("presc_id") + "' and drug_code='" + prescDetail.get("drug_code") + "'";
    	
    	
    	
    	
    	this.setQueryCode("PEAAS");
    	this.query.execute(sql);
    }
}
