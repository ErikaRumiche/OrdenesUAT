package pe.com.portability.bean;

import java.io.Serializable;

import java.util.Date;

import pe.com.nextel.util.GenericObject;


public class PortabilityOrderBean extends GenericObject implements Serializable
{
    //Variables Alta
    private static final long serialVersionUID = 1L;
    private long npPortabOrderId;
    private long npPortabItemId;
    private long npOrderid;
    private long npItemid;
    private long npItemDeviceId;
    private String npApplicationId;
    private String npMessageStatusId;
    private String npStatusPortability;
    private String npOrderParentId;
    private String npPhoneNumber;
    private String npModalityContract;
    private String npShippingDateMessage;
    private String npLastStateProcess;
    private String npLastStateDesc;
    private String npErrorIntegrity;
    private String npErrorIntegrityDesc;
    private String npReasonRejectionDesc;
    private String npReasonRejection;
    private String npScheduleDeadline;
    private String npExecutionDeadline;
    private String npExecutionDate;
    private String npCorrected;
    private String npState;
    private String npvalue;
    private String npvaluedesc;
    private String nptag1;
    private String npItemPosition;
    private String npModalityCont;
    private String npAssignor;
    private long npCustomerId;
    private String npCreateBy;
    private String npPortabType;
    private String npContract;
    private String npStateDesc;
    private String npAssignorDesc;
    private String npModalitySell;
    private long npProductLineId;
    private String npScheduleDBscs;
    private long npParentOrderId;
    private long npBalance;
    private long npPaymentParent;
    private long npPaymentChild;
    private long npPaymentAmount;
    private String npPhoneNumberWN;
    private String npCustomerType;
    private String npScheduleDays;

    //Variables Baja
    private long num_contract;
    private String num_tel;
    private String mot_susp;
    private long dias_susp;
    private String ult_est_proc_port;
    private String valor;
    private String descripcion;
    private String npcmbEvalSolBaja;
    private String npcmbMotivos;
    private String npcmbDocAtatchment;
    private String npav_ruta;
    private String npconfigFile;
    //private long nporderid;

    private String nporderid;
    private String npmodificate;
    private String nportabitemid;
    private String npParticipantDescription;

    private String npTitleOrdeParentChild;
    private long npOrderidParentChild;
    private String npTypeDocument;
    private String npDocument;

    //Reserva de Numeros de Radio UFMI - FPICOY - 28/10/2010
    private String npUfmi;
    private String npRqstdUfmi;
    private long npSolutionid;

    //P1D - Lee Rosales
  /*private String npLastStatusCode;
  private String npLastStatusDesc;
  private String npIntegrityErrorCode;
  private String npIntegrityErrorDesc;
  private String npRejectionReason;*/
    private double npAmountDue;
    private String npCurrencyType;
    private String npCurrencyDesc;
    private Date   npExpirationDateReceipt;
    private Date   npReleaseDate;


    public PortabilityOrderBean()
    {
    }

    public void setNpPortabOrderId(long npPortabOrderId)
    {
        this.npPortabOrderId = npPortabOrderId;
    }


    public long getNpPortabOrderId()
    {
        return npPortabOrderId;
    }


    public void setNpPhoneNumber(String npPhoneNumber)
    {
        this.npPhoneNumber = npPhoneNumber;
    }


    public String getNpPhoneNumber()
    {
        return npPhoneNumber;
    }


    public void setNpModalityContract(String npModalityContract)
    {
        this.npModalityContract = npModalityContract;
    }


    public String getNpModalityContract()
    {
        return npModalityContract;
    }


    public void setNpShippingDateMessage(String npShippingDateMessage)
    {
        this.npShippingDateMessage = npShippingDateMessage;
    }


    public String getNpShippingDateMessage()
    {
        return npShippingDateMessage;
    }


    public void setNpLastStateProcess(String npLastStateProcess)
    {
        this.npLastStateProcess = npLastStateProcess;
    }


    public String getNpLastStateProcess()
    {
        return npLastStateProcess;
    }


    public void setNpLastStateDesc(String npLastStateDesc)
    {
        this.npLastStateDesc = npLastStateDesc;
    }


    public String getNpLastStateDesc()
    {
        return npLastStateDesc;
    }


    public void setNpErrorIntegrity(String npErrorIntegrity)
    {
        this.npErrorIntegrity = npErrorIntegrity;
    }


    public String getNpErrorIntegrity()
    {
        return npErrorIntegrity;
    }


    public void setNpErrorIntegrityDesc(String npErrorIntegrityDesc)
    {
        this.npErrorIntegrityDesc = npErrorIntegrityDesc;
    }


    public String getNpErrorIntegrityDesc()
    {
        return npErrorIntegrityDesc;
    }


    public void setNpReasonRejectionDesc(String npReasonRejectionDesc)
    {
        this.npReasonRejectionDesc = npReasonRejectionDesc;
    }


    public String getNpReasonRejectionDesc()
    {
        return npReasonRejectionDesc;
    }


    public void setNpReasonRejection(String npReasonRejection)
    {
        this.npReasonRejection = npReasonRejection;
    }


    public String getNpReasonRejection()
    {
        return npReasonRejection;
    }


    public void setNpScheduleDeadline(String npScheduleDeadline)
    {
        this.npScheduleDeadline = npScheduleDeadline;
    }


    public String getNpScheduleDeadline()
    {
        return npScheduleDeadline;
    }


    public void setNpExecutionDeadline(String npExecutionDeadline)
    {
        this.npExecutionDeadline = npExecutionDeadline;
    }


    public String getNpExecutionDeadline()
    {
        return npExecutionDeadline;
    }


    public void setNpExecutionDate(String npExecutionDate)
    {
        this.npExecutionDate = npExecutionDate;
    }


    public String getNpExecutionDate()
    {
        return npExecutionDate;
    }


    public void setNpCorrected(String npCorrected)
    {
        this.npCorrected = npCorrected;
    }


    public String getNpCorrected()
    {
        return npCorrected;
    }


    public void setNpState(String npState)
    {
        this.npState = npState;
    }


    public String getNpState()
    {
        return npState;
    }


    public void setNpvalue(String npvalue)
    {
        this.npvalue = npvalue;
    }


    public String getNpvalue()
    {
        return npvalue;
    }


    public void setNpvaluedesc(String npvaluedesc)
    {
        this.npvaluedesc = npvaluedesc;
    }


    public String getNpvaluedesc()
    {
        return npvaluedesc;
    }


    public void setNptag1(String nptag1)
    {
        this.nptag1 = nptag1;
    }


    public String getNptag1()
    {
        return nptag1;
    }


    public void setNpOrderParentId(String npOrderParentId)
    {
        this.npOrderParentId = npOrderParentId;
    }


    public String getNpOrderParentId()
    {
        return npOrderParentId;
    }


    public void setNpItemid(long npItemid)
    {
        this.npItemid = npItemid;
    }


    public long getNpItemid()
    {
        return npItemid;
    }


    public void setNpItemDeviceId(long npItemDeviceId)
    {
        this.npItemDeviceId = npItemDeviceId;
    }


    public long getNpItemDeviceId()
    {
        return npItemDeviceId;
    }


    public void setNpMessageStatusId(String npMessageStatusId)
    {
        this.npMessageStatusId = npMessageStatusId;
    }


    public String getNpMessageStatusId()
    {
        return npMessageStatusId;
    }


    public void setNpStatusPortability(String npStatusPortability)
    {
        this.npStatusPortability = npStatusPortability;
    }


    public String getNpStatusPortability()
    {
        return npStatusPortability;
    }




    public void setNum_contract(long num_contract)
    {
        this.num_contract = num_contract;
    }


    public long getNum_contract()
    {
        return num_contract;
    }


    public void setNum_tel(String num_tel)
    {
        this.num_tel = num_tel;
    }


    public String getNum_tel()
    {
        return num_tel;
    }


    public void setMot_susp(String mot_susp)
    {
        this.mot_susp = mot_susp;
    }


    public String getMot_susp()
    {
        return mot_susp;
    }


    public void setDias_susp(long dias_susp)
    {
        this.dias_susp = dias_susp;
    }


    public long getDias_susp()
    {
        return dias_susp;
    }


    public void setUlt_est_proc_port(String ult_est_proc_port)
    {
        this.ult_est_proc_port = ult_est_proc_port;
    }


    public String getUlt_est_proc_port()
    {
        return ult_est_proc_port;
    }


    public void setValor(String valor)
    {
        this.valor = valor;
    }


    public String getValor()
    {
        return valor;
    }


    public void setDescripcion(String descripcion)
    {
        this.descripcion = descripcion;
    }


    public String getDescripcion()
    {
        return descripcion;
    }


    public void setNpcmbEvalSolBaja(String npcmbEvalSolBaja)
    {
        this.npcmbEvalSolBaja = npcmbEvalSolBaja;
    }


    public String getNpcmbEvalSolBaja()
    {
        return npcmbEvalSolBaja;
    }


    public void setNpcmbMotivos(String npcmbMotivos)
    {
        this.npcmbMotivos = npcmbMotivos;
    }


    public String getNpcmbMotivos()
    {
        return npcmbMotivos;
    }


    public void setNpcmbDocAtatchment(String npcmbDocAtatchment)
    {
        this.npcmbDocAtatchment = npcmbDocAtatchment;
    }


    public String getNpcmbDocAtatchment()
    {
        return npcmbDocAtatchment;
    }


    public void setNpav_ruta(String npav_ruta)
    {
        this.npav_ruta = npav_ruta;
    }


    public String getNpav_ruta()
    {
        return npav_ruta;
    }


    public void setNpmodificate(String npmodificate)
    {
        this.npmodificate = npmodificate;
    }


    public String getNpmodificate()
    {
        return npmodificate;
    }


    public void setNpItemPosition(String npItemPosition)
    {
        this.npItemPosition = npItemPosition;
    }


    public String getNpItemPosition()
    {
        return npItemPosition;
    }


    public void setNpModalityCont(String npModalityCont)
    {
        this.npModalityCont = npModalityCont;
    }


    public String getNpModalityCont()
    {
        return npModalityCont;
    }


    public void setNpAssignor(String npAssignor)
    {
        this.npAssignor = npAssignor;
    }


    public String getNpAssignor()
    {
        return npAssignor;
    }


    public void setNpCreateBy(String npCreateBy)
    {
        this.npCreateBy = npCreateBy;
    }


    public String getNpCreateBy()
    {
        return npCreateBy;
    }


    public void setNpCustomerId(long npCustomerId)
    {
        this.npCustomerId = npCustomerId;
    }


    public long getNpCustomerId()
    {
        return npCustomerId;
    }

    public void setNportabitemid(String nportabitemid)
    {
        this.nportabitemid = nportabitemid;
    }


    public String getNportabitemid()
    {
        return nportabitemid;
    }


    public void setNporderid(String nporderid)
    {
        this.nporderid = nporderid;
    }


    public String getNporderid()
    {
        return nporderid;
    }


    public void setNpOrderid(long npOrderid)
    {
        this.npOrderid = npOrderid;
    }


    public long getNpOrderid()
    {
        return npOrderid;
    }


    public void setNpPortabType(String npPortabType)
    {
        this.npPortabType = npPortabType;
    }


    public String getNpPortabType()
    {
        return npPortabType;
    }


    public void setNpPortabItemId(long npPortabItemId)
    {
        this.npPortabItemId = npPortabItemId;
    }


    public long getNpPortabItemId()
    {
        return npPortabItemId;
    }


    public void setNpContract(String npContract)
    {
        this.npContract = npContract;
    }


    public String getNpContract()
    {
        return npContract;
    }


    public void setNpStateDesc(String npStateDesc)
    {
        this.npStateDesc = npStateDesc;
    }


    public String getNpStateDesc()
    {
        return npStateDesc;
    }


    public void setNpApplicationId(String npApplicationId)
    {
        this.npApplicationId = npApplicationId;
    }


    public String getNpApplicationId()
    {
        return npApplicationId;
    }


    public void setNpAssignorDesc(String npAssignorDesc)
    {
        this.npAssignorDesc = npAssignorDesc;
    }


    public String getNpAssignorDesc()
    {
        return npAssignorDesc;
    }


    public void setNpModalitySell(String npModalitySell)
    {
        this.npModalitySell = npModalitySell;
    }


    public String getNpModalitySell()
    {
        return npModalitySell;
    }

    public void setNpParticipantDescription(String npParticipantDescription)
    {
        this.npParticipantDescription = npParticipantDescription;
    }

    public String getNpParticipantDescription()
    {
        return npParticipantDescription;
    }


    public void setNpProductLineId(long npProductLineId)
    {
        this.npProductLineId = npProductLineId;
    }


    public long getNpProductLineId()
    {
        return npProductLineId;
    }


    public void setNpScheduleDBscs(String npScheduleDBscs)
    {
        this.npScheduleDBscs = npScheduleDBscs;
    }


    public String getNpScheduleDBscs()
    {
        return npScheduleDBscs;
    }


    public void setNpconfigFile(String npconfigFile)
    {
        this.npconfigFile = npconfigFile;
    }


    public String getNpconfigFile()
    {
        return npconfigFile;
    }


    public void setNpParentOrderId(long npParentOrderId)
    {
        this.npParentOrderId = npParentOrderId;
    }


    public long getNpParentOrderId()
    {
        return npParentOrderId;
    }


    public void setNpBalance(long npBalance)
    {
        this.npBalance = npBalance;
    }


    public long getNpBalance()
    {
        return npBalance;
    }


    public void setNpPaymentParent(long npPaymentParent)
    {
        this.npPaymentParent = npPaymentParent;
    }


    public long getNpPaymentParent()
    {
        return npPaymentParent;
    }


    public void setNpPaymentChild(long npPaymentChild)
    {
        this.npPaymentChild = npPaymentChild;
    }


    public long getNpPaymentChild()
    {
        return npPaymentChild;
    }


    public void setNpPaymentAmount(long npPaymentAmount)
    {
        this.npPaymentAmount = npPaymentAmount;
    }


    public long getNpPaymentAmount()
    {
        return npPaymentAmount;
    }

    public void setNpTitleOrdeParentChild(String npTitleOrdeParentChild)
    {
        this.npTitleOrdeParentChild = npTitleOrdeParentChild;
    }


    public String getNpTitleOrdeParentChild()
    {
        return npTitleOrdeParentChild;
    }


    public void setNpOrderidParentChild(long npOrderidParentChild)
    {
        this.npOrderidParentChild = npOrderidParentChild;
    }


    public long getNpOrderidParentChild()
    {
        return npOrderidParentChild;
    }


    public void setNpPhoneNumberWN(String npPhoneNumberWN)
    {
        this.npPhoneNumberWN = npPhoneNumberWN;
    }


    public String getNpPhoneNumberWN()
    {
        return npPhoneNumberWN;
    }



    public void setNpTypeDocument(String npTypeDocument)
    {
        this.npTypeDocument = npTypeDocument;
    }


    public String getNpTypeDocument()
    {
        return npTypeDocument;
    }


    public void setNpDocument(String npDocument)
    {
        this.npDocument = npDocument;
    }


    public String getNpDocument()
    {
        return npDocument;
    }


    public void setNpUfmi(String npUfmi)
    {
        this.npUfmi = npUfmi;
    }


    public String getNpUfmi()
    {
        return npUfmi;
    }


    public void setNpRqstdUfmi(String npRqstdUfmi)
    {
        this.npRqstdUfmi = npRqstdUfmi;
    }


    public String getNpRqstdUfmi()
    {
        return npRqstdUfmi;
    }


    public void setNpSolutionid(long npSolutionid)
    {
        this.npSolutionid = npSolutionid;
    }


    public long getNpSolutionid()
    {
        return npSolutionid;
    }

    public void setNpAmountDue(double npAmountDue) {
        this.npAmountDue = npAmountDue;
    }

    public double getNpAmountDue() {
        return npAmountDue;
    }

    public void setNpCurrencyType(String npCurrencyType) {
        this.npCurrencyType = npCurrencyType;
    }

    public String getNpCurrencyType() {
        return npCurrencyType;
    }

    public void setNpCurrencyDesc(String npCurrencyDesc) {
        this.npCurrencyDesc = npCurrencyDesc;
    }

    public String getNpCurrencyDesc() {
        return npCurrencyDesc;
    }

    public void setNpExpirationDateReceipt(Date npExpirationDateReceipt) {
        this.npExpirationDateReceipt = npExpirationDateReceipt;
    }

    public Date getNpExpirationDateReceipt() {
        return npExpirationDateReceipt;
    }

    public void setNpReleaseDate(Date npReleaseDate) {
        this.npReleaseDate = npReleaseDate;
    }

    public Date getNpReleaseDate() {
        return npReleaseDate;
    }

    public String getNpCustomerType() { return npCustomerType; }

    public void setNpCustomerType(String npCustomerType) { this.npCustomerType = npCustomerType; }

    public String getNpScheduleDays() { return npScheduleDays; }

    public void setNpScheduleDays(String npScheduleDays) { this.npScheduleDays = npScheduleDays; }
}