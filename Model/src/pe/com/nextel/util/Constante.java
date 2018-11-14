
package pe.com.nextel.util;
import pe.com.nextel.bean.PersonInfoBean;

public class Constante {

    public static int iScrnOptToImgCateg=2626; //
    public static int iScrnOptToExcel=2143;

    /**Constante <b>SCRN_OPTTO_REP_CALLCENTER</b><br>
     * Objetivo  Permite editar el Responsable de CallCenter solo si<br>
     *           son supervisores de CallCenter<br>
     * Developer LROSALES<br>
     * Fecha     10/10/2008
     * */
    public static int SCRN_OPTTO_REP_CALLCENTER=3752; //
    public static int SCRN_OPTTO_IMGCATEG=2626; //
    public static int SCRN_OPTTO_PAYFORM=1871;
    public static int SCRN_OPTTO_CARRIER=2467;
    public static int SCRN_OPTTO_EXCEL=2143;
    public static int SCRN_OPTTO_NEW_SITE=2451;
    public static int SCRN_OPTTO_PAYMONT=3683;
    public static int SCRN_OPTTO_ADDENDUM_TERM=3711;
    public static int SCRN_OPTTO_NEW_BA=3733;
    public static int SCRN_OPTTO_NEW_PROSPECT=3759;
    public static int SCRN_OPTTO_FRAUDVALIDATION=4288;
    public static int SCRN_OPTTO_EDITRECONEXDATE=4368;
    public static int SCRN_OPTTO_MESSAGE_PORTAB=4574;
    public static int SCRN_OPTTO_ADDDOCUMENT=4644;
    public static int SCRN_OPTTO_ORDEREDIT=1838;
    public static String NEW_ON_DISPLAY =   "NEW_ON_DISPLAY";
    public static String NEW_ON_SAVE    =   "NEW_ON_SAVE";
    public static String EDIT_ON_DISPLAY="EDIT_ON_DISPLAY";
    public static String EDIT_ON_SAVE   ="EDIT_ON_SAVE";
    public static String EDIT_ON_DELETE ="EDIT_ON_DELETE";
    public static String ON_LOAD        ="ON_LOAD";
    public static String MODIFY         ="MODIFY";
    public static String SPECIFICATION  ="SPECIFICATION";
    public static String DIR_ENTREGA    ="ENTREGA";
    public static String CONTROL_TEXT   ="TEXT";
    public static String CONTROL_SELECT ="SELECT";
    public static String CONTROL_OTRO   ="OTRO";
    public static String NPERROR        ="NPERROR";
    public static String DETAIL_ON_DISPLAY = "DETAIL_ON_DISPLAY";
    public static String PATH_APPORDER_LOCAL  ="websales";
    public static String PATH_APPORDER_SERVER  ="/websales";
    //public static String PATH_APPORDER_SERVER  ="/apporderweb/websales";  //para realizar pruebas locales descomentar

    public static String ANSWER_NOT  ="N";
    public static String ANSWER_YES  ="S";
    public static String TYPE_ORDEN_EXTERNA="Externa";
    public static String TYPE_ORDEN_INTERNA="Interna";
    public static String EDIT ="EDIT";

    public static String CODE_SERVICE="02";
    public static String SHOW_BUTON_PARTE_INGRESO="S";
    public static String DIVISOR  =",";
    //public static String STATUS_UNKNOWN ="Unknown";
    public static int NUMBER_ALLOWED_NEW_SITE =1;
    public static String SUCCESS_ORA_RESULT ="ORA-0000";
    public static String TYPE_CONTACT_FACTURACION ="30";
    public static int TYPE_CUSTOMER =1;
    public static int TYPE_SITE =2;
    public static int TYPE_BILLINGACC =3;

    //Tipo Direccion
    public static String TYPE_ADDRESS_ENTREGA ="ENTREGA";
    public static String TYPE_ADDRESS_FACTURACION ="20";

    //Cliente
    public static String CUSTOMERTYPE_CUSTOMER ="CUSTOMER";
    public static String CUSTOMERTYPE_SITE ="SITE";
    public static String TYPE_CRM_CUSTOMER ="Prospect";

    //Status
    public static String SITE_STATUS_ACTIVO ="Activo";
    public static String SITE_STATUS_UNKNOWN ="Unknown";
    public static String SITE_STATUS_NUEVO ="Solicitado";
    public static String STATUS_DESACTIVADO ="Desactivado"; //CEM COR430
    public static String STATUS_ACTIVO      ="Activo"; 	   //CEM COR459
    public static String STATUS_SUSPENDIDO ="Suspendido";


    public static String DISPATCH_PLACE_ID_FULLFILLMENT="41";
    public static String GENERATOR_TYPE_INC="INC";
    public static String GENERATOR_TYPE_OPP="OPP";
    public static String GENERATOR_TYPE_OPP_PORTAB="OPP_PORTAB";
    //Inbox
    public static String INBOX_EDICION ="EDICION";
    public static String INBOX_BAGLOCK ="BAGLOCK";
    public static String INBOX_ADM_VENTAS ="ADM_VENTAS";
    public static String INBOX_CREDITO ="CREDITO";
    public static String INBOX_ACTIVACION_PROGRAMACION ="ACTIVACION/PROGRAMAC";
    public static String INBOX_BACKOFFICE ="BACKOFFICE";
    public static String INBOX_CALLCENTER ="CALLCENTER FF";
    public static String INBOX_ALMACEN_DELIVERY ="ALMACEN_DELIVERY"; ////
    public static String INBOX_TIENDA01        ="TIENDA01";
    public static String INBOX_INSTALACION_ING ="INSTALACION_ING";
    public static String INBOX_ST_PLATAFORMA 	="ST_PLATAFORMA";
    public static String INBOX_PROCESOS_AUTOMATICOS 	="PROCESOS_AUTOMATICOS";
    public static String INBOX_VENTAS = "VENTAS";
    public static String INBOX_ALMACEN_TIENDA = "ALMACEN_TIENDA";//GGRANADOS
    public static String INBOX_PRESUPUESTO = "PRESUPUESTO";
    public static String INBOX_RECURSOS_HUMANOS = "RECURSOS_HUMANOS";
    public static String INBOX_ALMACEN_INVENTARIO = "ALMACEN_INVENTARIO"; //[PRY-0710] EFLORES

    //Accion
    public static String ACTION_INBOX_AVANZAR ="AVANZAR";
    public static String ACTION_INBOX_SALTAR ="SALTAR";
    public static String ACTION_INBOX_IR_A ="IR A";
    public static String ACTION_INBOX_BACK ="RETROCEDER";
    public static String ACTION_INBOX_BAGLOCK ="BAGLOCK";
    public static String ACTION_INBOX_RECHAZAR ="RECHAZAR";
    public static String ACTION_INBOX_ANULAR ="ANULAR"; //CEM - COR0379
    public static String ACTION_INBOX_PRIMER_INBOX ="PRIMER_INBOX"; //CGC - Identifica al primer inbox sea cual sea el inbox real.
    public static String ACTION_INBOX_PRIMER_INBOX_REC ="PRIMER_INBOX_REC"; //KSH - Identifica al primer inbox cuando el inbox incial es ADM_VENTAS.

    public static String TABLE_TYPE_ADDRESS ="TIPODIRECCION";
    public static String TABLE_TYPE_CONTACT ="TIPOCONTACTO";
    public static String OBJECT_TYPE_CONTACT ="PERSON";

    public static String NAME_ORIGEN_FFPEDIDOS ="FFPEDIDOS";
    public static String NAME_CAMP_DET ="CAMDET";
    //Listado de Pago
    public static String PAYMENT_SOURCE ="10";

    //Forma de Pago
    public static String PAYFORM_CARGO_EN_RECIBO = "Cargo en el Recibo";
    public static String PAYFORM_CONTADO = "Contado";

    //Pagina
    public static String PAGE_ORDER_DETAIL ="ORDER_DETAIL";
    public static String PAGE_ORDER_EDIT ="ORDER_EDIT";

    //Tab
    public static String TAB_NOTAS_ORDER="Tab5";


    //Divisiones de Negocio
    public static byte KN_TELEFONIA_IDEN =1;  // Telefonía IDEN
    public static byte KN_BANDA_ANCHA    =2;  // Banda Ancha

    //Soluciones de Negocio
    public static byte KN_ACCESO_INTERNET =4;
    public static byte KN_ENLACE_DATOS    =5;
    public static byte KN_TELEFONIA_FIJA    =6;

    public static String NAME_ORIGEN_MASSIVE ="MASSIVE";

    //Spec ID
    public static int SPEC_TRASLADO =2050;
    public static int SPEC_SERVCIO_TECNICO_ACCESORIOS =2028;
    public static int SPEC_SERVCIO_TECNICO_REPARACION =2029;
    public static int SPEC_SERVCIO_TECNICO_REEMPLAZO =2030;
    public static int SPEC_SERVCIO_TECNICO_CASORAPIDO =2031;
    public static int SPEC_CAMBIAR_DATOS_CLIENTE =2015;
    public static int SPEC_CAMBIAR_PLAN_TARIFARIO =2013;
    public static int SPEC_ACTIVAR_DESACTIVAR_SERVICIOS =2024;
    public static int SPEC_REPAIRS[]={2028,2029,2030,2031};
    public static int SPEC_REACTIVATION = 2007; /*CEM*/
    public static int SPEC_SERVICIO_LOCUCION = 2025; // CEM COR0459
    public static int SPEC_EMPLEADO_FAMILIAR = 2019; // CEM COR0430
    public static int SPEC_EMPLEADO_AMIGO 	= 2020; // CEM COR0430
    public static int SPEC_CAMBIAR_ESTRUCT_CUENTA 	= 2016; // CEM COR0354
    public static int SPEC_REPOSICION 			= 2010; 	// CEM COR0303
    public static int SPEC_CAMBIO_MODELO 		= 2009; 	// CEM COR0242
    public static int SPEC_CAMBIO_MODELO_ENTRE_TECNOLOGIAS=2065;
    public static int SPEC_INTERCAMBIO_NUMERO 	= 2012;
    public static int SPEC_CAMBIO_NUMERO 	    = 2011;
    public static int SPEC_TRANSFERENCIA 	    = 2017;
    public static int SPEC_ACCESO_INTERNET 	    = 2004;
    public static int SPEC_PREPAGO_NUEVA 	    = 2002; //PREPAGO NUEVA & ADICION
    public static int SPEC_PREPAGO_TDE          = 3100; //PREPAGO TDE
    public static int SPEC_REPOSICION_PREPAGO_TDE = 3102; //REPOSICION PREPAGO TDE - TDECONV034
    public static int SPEC_PREPAGO_PORTA 	    = 2069; //PREPAGO PORTABILIDAD
    public static int SPEC_POSTPAGO_PORTA      = 2068;
    public static String MODALITY_PROPIO = "Propio";
    public static int SPEC_POSTPAGO_VENTA 	    = 2001; //PREPAGO NUEVA & ADICION
    public static int SPEC_CHECK_RESP_PAGO[] 	= 	{2001, 2002, 3100, 3102, 2017}; //JPEREZ //ADD 3102 - TDECONV034
    public static int SPEC_DEVOLUCION[]			=	{2039,2041,2042,2043};
    public static int SPEC_ARTICULOS_VARIOS 	    = 2033;
    public static int SPEC_CDI_VIAJERO = 2037;
    public static int SPEC_PORTABILIDAD_REC_NO_FUND = 2072;
    public static int SPEC_PORTABILIDAD_RET_NUM = 2071;

    public static int SPEC_BOLSA_CREACION=2021;// BOLSA CREACION
    public static int SPEC_BOLSA_CAMBIO = 2022; //BOLSA CAMBIO

   // ADT-RCT-092
   public static int SPEC_ACTIVAR_PAQUETES_ROAMING = 2086;

    public static int SPEC_PORTABILIDAD[] = {2068,2069}; //TMOGROVEJO
    //public static int SPEC_PORT_DIR_ENTREGA[] = {2070, 2071};  //JLINARES

    //Categoría ID
    public static int KN_VTA_INTERNET_ACC_INTERNET = 2004;
    public static int KN_VTA_INTERNET_ENLACE_DATOS = 2005;
    public static int KN_VTA_TELEFONIA_FIJA_BA     = 2006;
    public static int KN_ACT_DES_SERVICIOS_BA = 2048;
    public static int KN_ACT_CAMB_PLAN_BA = 2049;

    //Configuraciones
    public static String KV_ST_NOTIFICATION_PREFERENCES = "ST_NOTIFICATION_PREFERENCES";


    //Repair
    public static String REPAIR_SITUACION ="REPAIR_SITUACION";
    public static String REPAIR_RESOLUTION_CODE ="REPAIR_RESOLUTION_CODE";

    /** Inicio - RDELOSREYES*/
    public final static String KN_ORDERS = "ORDERS";
    public final static String MESSAGE_OUTPUT = "strMessage";
    public final static String DATA_STRUCT = "hshDataMap";
    public final static int CUSTOMER_STATUS_VALIDO = 1;
    public final static int CUSTOMER_STATUS_INVALIDO = -1;
    public final static String CUSTOMER_CONDICION_PROSPECT = "Prospect";
    public final static String CUSTOMER_CONDICION_CUSTOMER = "Customer";
    public final static String SERVLET_METHOD = "hdnMethod";
    public final static String JERARQUIA_PAIS = "0";
    public final static String JERARQUIA_DEPARTAMENTO = "1";
    public final static String JERARQUIA_PROVINCIA = "2";
    public final static String JERARQUIA_DISTRITO = "3";
    public final static int PRODUCT_LINE_EQUIPMENT = 2;
    public final static int PRODUCT_LINE_SERVICE = 11;
    public final static int PRODUCT_LINE_KIT = 12;
    public final static String NEW_ORDER_FLUJO_NORMAL = "0";
    public final static String NEW_ORDER_SOLICITUD_EXISTE = "1";
    public final static int FLAG_BUSQUEDA_ORDEN = 1;
    public final static int FLAG_BUSQUEDA_REPARACION = 2;
    public final static String ORDER_STATUS_EN_PROCESO = "EN PROCESO";
    public final static String ORDER_STATUS_ANULADO = "ANULADO";
    public final static String ORDER_STATUS_CERRADO = "CERRADO";
    public final static String APPLICATION_SALES = "SALES_LOGIN";
    public final static String APPLICATION_SAC = "SAC_LOGIN";
    public final static String APPLICATION_CC = "CC_LOGIN";
    public final static String CONSULTOR_DIRECTO = "D";
    public final static String CONSULTOR_INDIRECTO = "I";
    public final static String BUILDING_TIPO_FISICA = "Fisica";
    public final static String LEVEL_DEFAULT = "1";
    public final static String CODE_DEFAULT = "A";
    public final static String BUSUNITID_DEFAULT = "13";
    public final static String PARAM_ORDER_ID = "an_nporderid";
    public final static String COMMENT_ACTION = "Comentario";
    public final static String MODALIDAD_VENTAS = "20";
    public final static String TRANSACCION_NUEVA = "NEW";
    public final static String TRANSACCION_NUEVA_EMPLEADO = "NEW_EMP";
    public final static String TRANSACCION_EXISTENTE = "ALTER";
    public final static String TRANSACCION_CONVERSION = "CONVERT";
    public final static String CUSTOMER_LEVEL_FORTY = "40";
    public final static String CUSTOMER_LEVEL_TEN = "10";
    public final static String TIPO_LARGE = "LARGE";
    public final static String TIPO_FLAT = "FLAT";
    public final static String TABLE_NAME_SITE = "SW_SITE";
    public final static String TABLE_NAME_CUSTOMER = "SW_CUSTOMER";
    public final static String SINCRONIZACION_SITE = "S";
    public final static String SINCRONIZACION_CUSTOMER = "C";
    public final static String APPLICATION_ID_BPEL = "201";
    public final static String TIPO_DOC_DNI = "DNI";
    public final static int NUM_REGISTROS_X_PAGINAS = 15;
    public static String PAGE_ORDER_NEW ="ORDER_NEW";
    /** Fin - RDELOSREYES*/

    /** Inicio - LROSALES*/
    public final static String IND_STATUS_ACTIVE = "a";
    public final static String IND_STATUS_INACTIVE = "i";
    public final static String IND_STATUS_DEACTIVATE = "d";
    public final static String IND_STATUS_UNKNOW = "u";
    public final static String IND_STATUS_SOLICITADO = "s";

    public final static String VALIDATE_INT_POSITIVE   = "INT_POSITIVE";
    public final static String VALIDATE_INTEGER   = "INTEGER";
    public final static String VALIDATE_FLOAT     = "FLOAT";
    public final static String VALIDATE_DATE      = "DATE";
    public final static String VALIDATE_DATE_PLUS = "DATE PLUS";
    public final static String VALIDATE_DATE_TIME = "DATE TIME";
    public final static String VALIDATE_DATE_TIME_PLUS = "DATE TIME PLUS";
    public final static String NPT_CONTR_ASOCIATE = "CONTR_ASOCIATE";
    public final static String NPT_EQUIPMENT_RETURN = "EQUIPMENT_RETURN";
    public final static String NPT_EQUIPMENT_NOTYET_GIVEBACK = "EQUIPMENT_NOTYET_GIVEBACK";

    /**Constante <b>NPT_SECTION_BA_FROM_SITE</b><br>
     * Objetivo  Indica si la sección de Cuentas de Facturación <br>
     *           debe ser visualizada en la sección de Responsables de Pago<br>
     * Developer LROSALES<br>
     * Fecha     21/10/2008
     * */
    public final static String NPT_SECTION_BA_FROM_SITE = "SECTION_BA_FROM_SITE";
    /** Fin - LROSALES*/
    /**Inicio - CBARZOLA*/
    public final static String NPT_VALIDATE_SOLUTION = "VALIDATE_SOLUTION";
    public static String ACTIVE_STATUS      ="1";
    public static String INBOX_NOT_BACK[]={"VENTAS"};
    /**Fin - CBARZOLA*/
    /** Inicio - CESPINOZA*/
    public final static String TIPO_DOC_RUC = "RUC";
    public final static String TIPO_DOC_LE = "LE";
    public final static String STATUS_CONTRACT = "Desactivado";
    public final static int TAGHTML_NBSP = 160;  //Espacio sin separación -- &nbsp --
    public final static String FIELD_ONLY_READ = "0";  //Controles del formulario en modo lectura
    public final static String COUNTRY = "COUNTRY";  //Usado para FXI_WORLD_NUMBER
    public final static String GUION = "-";			//CEM COR0354
    public final static int SOLUCION_PREPAGO = 2;
    public final static String FROM_ONCHANGE     = "ONCHANGE";
    public final static String FROM_LOADDETAIL   = "LOADDETAIL";
    public final static String SET_VALUE_IN_CONTROL = "SET_VALUE_IN_CONTROL";
    public final static String UTILIZAR_DEFAULT = "1";
    /** Fin - CESPINOZA*/

    /** - Odubock */
    public final static String TIPO_CLIENTE              = "Cliente";
    public final static String TIPO_ALQUILER             = "Alquiler";
    public static int SPEC_LOAD_ADEND_DEFAULT[]          ={2007,2009,2010,2013,2017,2024,2012};
    //public static String SPEC_LOAD_CMB_PROCESS           ="[\"ALQUILER_REP\",\"ALQUILER_REX\",\"REP\",\"REX\",\"STP\",\"STP_REPO\"]";
    public static String SPEC_LOAD_CMB_PROCESS           ="[\"ALQUILER_REP\",\"ALQUILER_REX\",\"REX\",\"STP\",\"STP_REPO\"]";
    public static int SPEC_BAG_DATE[]                    ={2022,2023};
    public static String TIPO_PLANTILLA_NUEVA            = "Nueva";
    public static String TIPO_PLANTILLA_RENOVACION       = "Renovación";
    public static String INBOX_ALMACEN_DESPACHO          = "ALMACEN_DESPACHO";
    public static int SPEC_SERV_TEC_SHOW_ALERT_STOCK[]   ={2028,2029,2030,2031,2033};
    /** Fin - Odubock*/
    public static String ST_PROCESO_REX            = "REX";
    public static String ST_STATUS_CERRADO         = "CERRADO";
    public static String CONVERT_PORCENT           = "99999999";
    public static int OPP_TYPE_BA = 3; // Tipo de Oportunidad banda ancha

    public static String ST_ORDENES_GROSS = "ORDENES_GROSS_RP_BA";
    public static String ST_ORDENES_SERV_ADIC = "ORDENES_RP_BA";

    public static String REPORT = "REPORT";
    public static String URL_SERVER_REPORT = "NPURLSERVERREPORT";

    public static String FORM = "FORM";
    public static String URL_SERVER_SERVLET = "NPURLFORMSERVLET";

    public static int TYPE_DOC_INVOICE_GR = 100;
    public static int TYPE_DOC_INVOICE_PI = 110;
    public static int TYPE_DOC_INVOICE_FACT = 30;

    public static int TYPE_DOC_INVOICE_BOLE = 40;
    public static int TYPE_DOC_INVOICE_ND = 90;
    public static int TYPE_DOC_INVOICE_NC = 80;

    //Constante para determinar en que inbox siempre son editable los items de la orden
    public static String EXCEP_INBOX_ITEM_EDITABLE = "EXCEP_INBOX_ITEM_EDITABLE";

    //rpolo roL DE CONSULTOR DIRECTO
    public static long ROL_CONSULTORD =2050;
    //rpolo roL DE CONSULTOR INDIRECTO
    public static long ROL_CONSULTORI =2051;

    //Constante tipo de tipos de cuenta
    public static String EXCLUSIVITY_TYPE_MAYORES 	="MAYORES";
    public static String EXCLUSIVITY_TYPE_ESTRATEGICA 	="ESTRATEGICA";
    public static String EXCLUSIVITY_TYPE_CORPORATIVO 	="CORPORATIVO";

    //Constante Grupo Solución
    public static String SOLUTION_GROUP_MOVILES  = "Móviles";
    public static int SOLUTION_GROUP_MOVILES_ID  = 1;

    //Constante Tipo de Soluciones
    public static String SOLUTION_TYPE_POST = "Postpago";
    public static String SOLUTION_TYPE_PRE  = "Prepago";

    // Constante de Niveles para los combos de diagnostico Jherrera 10/06/2009
    public static int DIAGNOSIS_LEVEL_1  = 1;
    public static int DIAGNOSIS_LEVEL_2  = 2;

    // Constante de Nivel de orden para el combo de Nivel PHIDALGO 15/03/2010
    public static String ORDERS_LEVEL  = "ORDERS_NP_LEVEL";
    public static String LEVEL_ONE_CODE  = "1";
    public static String LEVEL_TWO_CODE  = "2";

    //Constante para obtener los Estados de Item para las Suspensiones Definitivas rmartinez 28/06/2009
    public static String ITEM_STATUS = "ITEM_STATUS";
    public static int SPEC_SUSPENSIONES[] = {2062, 2063, 2064}; //Agregado rmartinez para el manejo de suspensiones

    //Constante para obtener los Tipos de IP en una venta móvil
    public static String IP = "IP";
    public static String TIPO_IP_GENERICO = "1";

    /*Estado de OPeracion para Ventas Moviles y Cambio de Modelo -> Categoria 2001,2002,2009*/
    public static String OPERATION_STATUS_OK = "OK";
    public static String OPERATION_STATUS_ERROR = "ERROR";
    public static String OPERATION_STATUS_INCOMPLETO = "INCOMPLETO";
    /*[CMT][vcedenos][Se agregan estados para cambio de modelo]*/
    public static String OPERATION_STATUS_EN_PROCESO = "EN PROCESO";
    public static String OPERATION_STATUS_MANUAL = "MANUAL";
  /*Estado de OPeracion -> Fin*/

    /* Líneas de producto  */
    public static int PRODUCT_LINE_SERV_INST_TF         = 216; //Servicio Instalación TF
    public static int PRODUCT_LINE_EQ_TF                = 215; //Línea de productos Equipo de Banda Ancha (antes Equipo de Telefonía Fija)
    public static int PRODUCT_LINE_EQ_INTERNET          = 126; //Línea de productos Equipo de Internet
    public static int PRODUCT_LINE_KIT_TF               = 272; //Línea de productos Equipo de TF
    public static int PRODUCT_LINE_SERV_INST_INTERNET   = 166; //Línea de productos Servicio de Instalación de Internet
    public static int PRODUCT_KIT_INTERNET              = 15; //Línea de productos Kit Internet

    /*Regla para obtener vendedores Data*/
    public static int SALES_RULE_ID_DATA    = 98;
    public static int SALES_DATA_WINNER_TYPE  = 2;

    public static String SERVICIOS_ADICIONALES = "SERVICIOS ADICIONALES";
    public static int SPEC_SSAA_SUSCRIPCIONES = 2079;
    public static int SPEC_SSAA_PROMOTIONS = 2080;
    public static String SERVICE_PROCESSING = "1"; //Procesamiento del servicio
    public static String SERVICE_GROUP_SUBSC = "2"; //Grupo de suscripciones
    public static String SERVICE_GROUP_PROM = "3"; //Grupo de promociones


    public static int MASSIVE_MAX_RECORD_DEFAULT  = 300;

    public static String RUTA_JNDI_PROPERTIES = "D:\\javaConfig\\Ordenes\\jndi.properties";
    public static String RUTA_JNDI_PROPERTIES_2 = "D:\\javaConfig\\Ordenes\\jndi_config.properties";

    public static int SPEC_SERVICIOS_ADICIONALES[] = {2024}; /*mlopezl - modif hpptt # 1 - 30/06/2010*/

    /* FPICOY - Modificaciones HPPTT - Garantia Extendida */
    public static String PRODUCT_LINE_KIT_GAR_EXT  = "21";
    public static String CMB_ITEM_PRODUCT_MODEL_ID  = "124";

    /*LSILVA - Tipo de Orden Interna*/
    public static String TIPO_ORDEN_INTERNA_ACTIVATE = "ACTIVATE";
    public static String TIPO_ORDEN_INTERNA_DEACTIVATE = "DEACTIVATE";

    /*MLOPEZL - Tipo de Orden Interna*/
    public static String TIPO_ORDEN_INITIAL = "INITIAL";
    public static String TIPO_ORDEN_FINAL = "FINAL";


    /* EZUBIAURR - Modificaciones HPPTT - Compatibilidad de Servicios */
    public static int SPEC_EMPLEADO_ASIGNACION 	= 2018;
    public static int SPEC_VENTA_MOVILES_RETAIL_POSTPAGO 	= 2053;
    public static int SPEC_PORTABILIDAD_POSTPAGO 	= 2068;
    public static int SPEC_VENTA_INTERNET_ENLACE_DATOS 	= 2005;
    public static int SPEC_VENTA_TELFIJA_NUEVA_ADICION 	= 2006;
    public static int SPEC_PRESTAMO_CLIENTE_POR_DEMO 	= 2034;
    public static int SPEC_PRESTAMO_TEST 	= 2035;
    public static int SPEC_TRANSFERENCIA_SUB_REG 	= 2074;
    public static int SPEC_ACT_DES_SERVICIOS_BA = 2048;
    public static int SPEC_ACT_CAMB_PLAN_BA = 2049;
    public static String PRODUCT_LINE_EQUIPO_MOVIL 	= "2";
    public static String PRODUCT_LINE_EQUIPO_MOVIL_3G 	= "202";
    public static String PRODUCT_LINE_EQUIPO_MOVIL_3G_HPPTT 	= "490";
    public static String EQUIPO_MOVIL_3G_HPPTT_NPDESC = "Equipo Móvil Nextel 3G";//EZUBIAURR 17/01
    public static String EQUIPO_MOVIL_3G_HPPTT_NPTABLE = "PRODUCT_LINE_EQUIP_MOVIL";//EZUBIAURR 17/01
    public static String PRODUCT_LINE_TRANSFERENCIA_EQUIPO 	= "9";
    public static String PRODUCT_LINE_USB_MODEM 	= "193";
    public static int SOLUTION_3G_HPPTT_POST 	= 17; //EZM 13/12 SolutionId 3G HPPTT POST
    public static int SOLUTION_3G_POST 	= 15; //EZM 13/12 SolutionId 3G POST
    public static int SOLUTION_DATA = 12;
    public static int SOLUTION_INTERNET_MOVIL_POST = 13;


    /* FPICOY - Modificaciones HPPTT - Reserva de Numeros Golden */
    public static String UFMI_NO_RESERVADO  = "0";
    public static String UFMI_RESERVADO  = "1";
    public static String PRODUCT_LINE_KIT_GOLDEN  = "23";
    public static String ITEM_ID_RESERVA_NUMEROS  = "113";
    public static String MODALITY_VENTA  = "Venta";

    /* RHUACANI-ASISTP - Modificaciones HPPTT - Reserva de Numeros Golden */
    public static String GOLDEN_NUMBERS  = "5";
    public static String ELQUELLAMAPAGA_NUMBERS = "3"; //PBASUALDO 20101206
    public static String PRODUCT_NUMBER_CHANGE_RIPLEY = "26600"; //PBASUALDO 20101206
    public static String SEARCH_PTN  = "1";
    public static String SEARCH_UFMI = "89";
    public static String OTHER_NUMBERS  = "1";
    public static String TT_TMCODE  = "1222";
    public static String ITEM_HEADER_ID_RESERVA_NUMEROS  = "123";
    public static String QUANTITY_SEARCH_RESERVA_NUMEROS  = "10";
    public static String ITEM_ID_NUEVO_NUMERO = "58";
    public static int SOLUTION_3G_HPPTT_PRE 	= 18;
    public final static int SOLUCION_2G_POST = 3;
    public final static int SOLUCION_2G_PRE = 2;

    public static String MSG_BLACKLIST_OPERADOR = "El equipo se encuentra en el Blacklist del Operador";

    public static String SALES_STRUCT_PORTAL_DEFAULT  = "GERENCIA SAC";
    public static String NOT_BACK_INBOX = "NOT_BACK_INBOX";
    public static String INBOX_PORTABILIDAD_NUMERICA = "PORTABILIDAD_NUMERICA";

    public static String MSG_SIM = "SIM";
    public static String MSG_IMEI = "IMEI";
    //Generator type de la orden para presupuesto
    public static String GENERATOR_TYPE_ORDER = "ORDER";
    //EXTERNO.MVALLE
    public static String MSG_GENERATED_PI = "Se generó correctamente Parte de Ingreso por cambio de dirección";
    public static String MSG_GENERATED_GR = "Guía generada correctamente. ";
    //JSALAZAR 01022011 INICIO
    public static String ESTADO_EJECUCION_ITEM_NPDESC = "Estado de Ejecución del Item";
    public static String ESTADO_EJECUCION_ITEM_NPTABLE = "STATUS_ITEM_EXECUTION";
    //JSALAZAR 01022011 FIN
    //SSAA POR DEFECTO - FPICOY 23/11/2011
    public final static String SPECIFICATION_TO_SSAA_DEFAULT_YES  = "1";
    public final static String SPECIFICATION_TO_SSAA_DEFAULT_NOT  = "0";

    //FPICOY 01122011 - RENOVACION DE BUCKETS
    public final static int SPEC_SSAA_RENOV_BCKT_SCP = 2026;
    public final static int SPEC_SSAA_RENOV_BCKT_CCP = 2081;
    public final static String PRODUCT_LINE_BUCKET_NPDESC = "Linea de Producto Bucket";
    public final static String PRODUCT_LINE_BUCKET_NPTABLE = "PRODUCT_LINE_BUCKET";

    /**
     * Identifica la ruta donde se va ha encontrar el archivo properties
     */
    public static final String FILE_PROPERTIES = "/oracle/javaconfig/AppOrder/apporder.properties";

    //RMARTINEZ 101212 - REGLA DE PARA JERARQUIA DE VENTA A PALZOS
    public final static int NP_SALES_RULE_VEP = 157;

    //configuracion_type para respuesta de cotizacion

    public final static String RESPUESTA_COTIZACION_TYPE_OBJECT = "1";
    public final static String RESPUESTA_COTIZACION_VALID_STATUS = "1";
    public final static String RESPUESTA_COTIZACION_ORDER_BY = "v1";
    public final static String RESPUESTA_COTIZACION_CAMPO1 = "v1";
    public final static String RESPUESTA_COTIZACION_CAMPO2 = "v2";

    //portega
    public static String ESTADO_COTIZACION_PENDIENTE = "PENDIENTE";
    public static String ESTADO_COTIZACION_ESPERA = "ESPERA";
    public static String ESTADO_COTIZACION_APROBADO = "ACEPTA COTIZACIÓN";
    public static String ESTADO_COTIZACION_RECHAZADO = "NO ACEPTA COTIZACIÓN";
    public static String ESTADO_COTIZACION_DEVOLUCION = "DEVOLUCIÓN";

    public final static String PORTABILITY_MESSAGE_TYPE_01R01 = "01R01";
    //JLIMAYMANTA
    public final static String TYPE_SERVICE_TE  = "TE";

    //LTE
    public static String NAME_ORIGEN_USSD ="USSD";

    //Portabilidad Fase 3 - LVALENCIA
    public static String CLIENTE_ESPECIAL_TIPOE = "Especial";
    public static String CLIENTE_ESPECIAL_TIPON = "Normal";
    public static String CLIENTE_ESPECIAL_DIASXDEFECTO = "3";

    /* INICIO ADT-BCL-083 -- LHUAPAYA
    * Constantes propias de bolsa de celulares
    * */
    public static int SPEC_BOLSA_CREAR = 2082;
    public static int SPEC_BOLSA_DOWNGRADE = 2083;
    public static int SPEC_BOLSA_UPGRADE = 2084;
    public static int SPEC_BOLSA_DESACTIVAR = 2085;
    public static String SPEC_TYPE_BOLSA = "BOLCEL";
    public static String ORDERS_BC_LEVEL = "ORDERS_NP_BC_LEVEL";
    public static String CONTROL_CHECK = "CHECK";
    public static int IN_BLACKLIST = 1;
    public static int OFF_BLACKLIST = 0;
    /* FIN ADT-BCL-083 -- LHUAPAYA*/

    // ADT-RCT-092
    public static final String TIPO_BOLSA_RECURRENTE = "REC";

	//Verificacion Biometrica
    public static String  Tipo_Biometrica="B";
    public static String  Tipo_NOBiometrica="NB";

    public static String  Source_CRM="CRM";
    public static String  Source_CRMVIA="CRMVIA"; //IOZ15092016
    public static Integer  Flagdni=2;

    public static String  action_A="A";  //A:anular
    public static String  action_C="C";  //C:continuar
    public static String  action_NB="NB";//NB:No biometrica
    public static String  action_R="R";  //R:reintento
    public static String  action_D="D";  //D:desconocido
    public static String  action_CN="CN";  //CN:CANCELAR
    public static String  action_UNR="UNR";  //UNR:usuario no registrado

    public static String  action_Domain_NB="30"; //7:Dominio no biometrico
    public static String  action_Domain_A="8"; //8:Dominio Anular Orden

    public static String type_transaction="ORDEN";
    public static int    status_cancelar=4;
    public static String aceptaCancel="10208";

    public static String MSG_VALIDATION_ERROR = "Ocurrió un error en la validación";

    public static String CADENA_VACIA = "";
    public static String MESSAGE_RESPONSE_NULL = "El response es nulo";
    public static String CADENA_VALOR_UNO = "1";
    public static String CADENA_VALOR_CERO = "0";
    public static String NORMALIZACION_DIR_AUTH = "NORMALIZACION_DIR_AUTH";
    public static String NORMALIZACION_DIR_AUTH_USUARIO = "AUTH_USUARIO";
    public static String NORMALIZACION_DIR_AUTH_PASSWORD = "AUTH_PASSWORD";
    public static String NORMALIZACION_DIR_AUTH_PAQUETE = "AUTH_PAQUETE";
    public static String NORMALIZACION_REFERENCIA_SWITCH = "NORMALIZACION_REFERENCIA_SWITCH_ON_OFF";
    public static String REFERENCIA_ACTIVA_DESACTIVA = "REFERENCIA_ACTIVA_DESACTIVA";

    public static String NORMALIZACION_SWITCH_ON_OFF = "NORMALIZACION_SWITCH_ON_OFF";
    public static String NORMALIZACION_ACTIVA_DESACTIVA = "NORMALIZACION_ACTIVA_DESACTIVA";

    public static int FLAG_DIRECCION_GEOCODIFICADA=1;
    public static int FLAG_DIRECCION_AMBIG_NO_GEOCODIFICADA=0;
    public static int FLAG_ERROR_ACCESO=-1;
    public static int RESPONSE_IS_NULL=-2;
    public static int CONSULTA_EXCEPTION_TIMEOUT=-3;
    public static int CONSULTA_EXCEPTION_DISPONIBILIDAD=-5;
    public static int ERROR_PAGE=-4;

    public static String TIPOVIA  = "STREET_TYPE";
    public static String TIPOZONA  = "ZONE_TYPE";
    public static String TIPOINTERIOR  = "INTERIOR_TYPE";

    //public static String NO_ERRORS = "NO ERRORS"; --Se Comenta porque se repite dos veces

    public static int FLAG_UBIGEO_DEPARTAMENTO=1;
    public static int FLAG_UBIGEO_PROVINCIA=2;
    public static int FLAG_UBIGEO_DISTRITO=3;

    public static String DESC_LEGAL = "LEGAL";
    public static String DESC_FACTURACION = "FACTURACION";
    public static String DESC_ENTREGA = "ENTREGA";
    public static String DESC_COMUNICACION = "COMUNICACION";

    public static int VALUE_FLAG_NEGATIVE = 9;

    public static String MENSAJE_MOTIVO_NORMALIZACION = "MENSAJE_MOTIVO_NORMALIZACION";
    public static int MENSAJE_MOTIVO_NORMALIZADO  =  1;
    public static int MENSAJE_MOTIVO_NORMALIZADO_EDITADO  =  2;
    public static int MENSAJE_MOTIVO_NO_NORMALIZADO  =  3;
    public static int MENSAJE_MOTIVO_ERROR404  =  4;
    public static int MENSAJE_MOTIVO_TIMEOUT  =  5;
    public static int MENSAJE_MOTIVO_GENERICERROR  =  6;

    public static int ESTADO_DIR_NO_NORMALIZADO  =  1;
    public static int ESTADO_DIR_NORMALIZADO  =  2;
    public static int ESTADO_DIR_NORMALIZADO_EDITADO  =  3;


    public static String MENSAJE_ALERTA_NORMALIZACION = "MENSAJE_ALERTA_NORMALIZACION";
    public static String MENSAJE_ALERTA_FALTA_NORM_NUMERO  =  "1";
    public static String MENSAJE_ALERTA_NO_RESULTS  =  "2";
    public static String MENSAJE_ALERTA_ERROR_RESPONSE  =  "3";
    public static String MENSAJE_ALERTA_ADDRESS_INCORRECT  =  "4";
    public static String MENSAJE_ALERTA_UBIGEOINEI_VACIO  =  "5";

    public static String NORMALIZACION_VENTAS = "NORMALIZACION_VENTAS";
    public static String DIVISION = "DIVISION";
    public static String CATEGORIA = "CATEGORIA";
    public static String SUBCATEGORIA = "SUBCATEGORIA";

    public static String CLIENTE_PROSPECT = "PROSPECT";

    /*PM0010548*/
    public static String INBOX_ACTIVACION_DIFERIDA = "ACTIVACION/DIFERIDA";

    //PM0010526
    public static String NO_ERRORS = "NO ERRORS";
    // MMONTOYA [Bolsa Celular]
    public static final String EJB_SERVER = "ejb.server";
    public static final String EJB_USER = "ejb.user";
    public static final String EJB_PASSWORD = "ejb.password";

    public static final String ERROR = "Error: ";
    public static String ERROR_EXCEPTION_TIMEOUT = "TIMEOUT";

    public static String MENSAJE_SUB_ALERTA_NORMALIZACION = "MENSAJE_SUB_ALERTA_NORMALIZACION";
    public static String MENSAJE_SUB_ALERTA_POR_DISPONIBILIDAD = "1";
    public static String MENSAJE_SUB_ALERTA_POR_TIMEOUT = "2";

    /*DERAZO REQ-0428*/
    public static String CONFIG_MOTIVE_LIST = "NEGOTIATION_MOTIVE_PENALTY";
    public static String CONFIG_PAYMENT_TERM_PENALTY_LIST = "PAYMENT_TERM_PENALTY";
    public static String OPTION_SHOW_PAY_PENALTY = "SHOW_PAY_PENALTY";
    public static String OPTION_EDIT_ORDER = "EDIT_ORDER";
    public static String OPTION_ADVANCE_ORDER = "ADVANCE_ORDER";
    public static String NPTABLE_TIPO_CARGO = "TIPOCARGO";
    public static String PENALTY_CHK_FLAT = "PENALTY_CHK_FLAT";
    public static String PENALTY_CHK_LARGE = "PENALTY_CHK_LARGE";
    public static String SPECIFICATION_INBOX_PENALTY = "SPECIFICATION_INBOX_PENALTY"; // EIORTIZ 17-06-2016
    public static String ESTADO_DE_PAGO_ORDEN_PENDIENTE = "Pendiente";
    public static String SWITCH_PENALTY_ADDENDUM = "PENALTY_ADDENDUM_FLAG";

    // Casos de uso
    public static String UC_NEW_ORDER = "NEW_ORDER";
    public static String UC_ISO_PACKSIM_ACTIV = "ISO_PACKSIM_ACTIV";

    //[PM0011173]LROQUE
    public static final String SOURCER_RETAIL = "RETAIL";
    public static final String CODIGO_RESP_OSB_OK = "0";
    public static final String DESC_COMPANIA = "COMPANIA";
    public static final long ID_APLICACION_CMR = 127;

    //VIA
    public static String SOURCE_VIA = "CRMVIA";

    public static String REQUEST_GET_VIA_CUSTOMER = "requestGetViaCustomer";
    //EFLORES CDM+CDP PRY-0817
    public static final String NPOBJHTMLNAME_KEEP_SIM = "chkMantenerSIM";
    public static final String NPOBJHTMLNAME_KEEP_SIM_VAL = "hdnItemValuechkMantenerSIM";

    //DIGITALIZATION

    public static String Source_DIGIT = "DIGITALIZATION";
    public static int TRANSACCION_POSTVENTA = 1;
    public static int VIA_PERSONA = 0;
    public static int VIA_EMPRESA = 1;
    public static int VIA_PERSONA_EMPRESA = 2;

    public static String TAG_PERSONA = "P";
    public static String TAG_EMPRESA = "E";

    public static String TAG_ORDEN_ORIGIN = "3";
    public static int DIGIT_FLAG_ACTIVE = 1;

    //VIDD
    public static final String REQUEST_GET_EMAIL="requestGetEmail";
    public static final String REQUEST_GET_COMBO="requestGetCombo";
    public static final String SIGNATURE_TYPE ="SIGNATURE_TYPE";
    public static final String REASON ="SIGNATURE_REASON";
    public static final String TIPO_DOC ="DOC_TYPE";
    public static final String DOC_TYPE_ASSIGNEE ="DOC_TYPE_ASSIGNEE";
    public static final String ATTACH_DOC_LIMIT_COUNT = "ATTACH_DOC_LIMIT_COUNT";
    public static final String ATTACH_DOC_TYPE ="ATTACH_DOC_TYPE";
    public static final String ATTACH_DOC_TYPE_MIME_TYPE ="ATTACH_DOC_TYPE_MIME_TYPE";
    public static final String ATTACH_DOC_TYPE_KB_SIZE ="ATTACH_DOC_TYPE_KB_SIZE";
    public static final String ATTACH_DOC_TYPE_WS = "ATTACH_DOC_TYPE_WS";
    public static final int SIGNATURE_TYPE_DIGITAL =1;
    public static final int SIGNATURE_TYPE_MANUAL =2;
    public static final int TRX_TYPE_ALL=1;
    public static final int SOURCE_ORDERS_ID=3;
    public static final int SOURCE_INCIDENT_ID=2;
    public static final int SOURCE_ALL_ID=1;
    public static final int TRX_TYPE_PORTABILIDAD_ID=3;
    public static final int TRX_TYPE_VENTA_ID=2;
    public static final int TRX_TYPE_POSTVENTA_ID=4;
    public static final int TYPE_TRX_ALL_ID=1;
    public static final int CHANNEL_PERSON_ID=2;
    public static final int CHANNEL_COMPANY_ID=3;
    public static final int CHANNEL_ALL_ID=1;
    public static final int SECTION_IDENT_VERIF_ID=1;
    public static final int SECTION_ASSIGNEE_ID=2;
    public static final int SECTION_DIGIT_DOCUMENT_ID=3;
    public static final int SECTION_ALL_ID=0;
    public static final String SECTION_DIGIT_DOCUMENT="Digitalizaci&oacute;n de Documentos";
    public static final String SECTION_POPULATE_CENTER="Centro Poblado de Uso Frecuente";
    public static final String SECTION_ASSIGNEE="Apoderado";
    public static final int TYPE_ORDER_ID=3;
    public static final String OPERATION_GET_RULES="getRules";
    public static final String OPERATION_GENERATE_DOCUMENTS="createDocumentByRule";
    public static final String OPERATION_ENVIAR_CORREO="sendEmail";
    public static final String OPERATION_CREATE_DOCUMENT="createDocument";
    public static final String OPERATION_GET_NUMBER_LINES="getNumberLines";
    public static final String WS_DOCUMENT_MANAGE="DocumentManage";
    public static final String WS_CONTRACT_MANAGE="ContractManage";
    public static final String WS_DOCUMENT="Document";
    public static final String RESPONSE_CODE_EXCEPTION="-1000";
    public static final int RESPONSE_CODE_WS_EXCEPTION=-1000;
    public static final int RESPONSE_CODE_WS_SUCCESS=1;
    public static final int RESPONSE_CODE_WS_FAIL=-1;
    public static final String APPLICATION_ORDERS="APP009";
    public static final String APPLICATION_INCIDENT="APP012";
    public static final String CHANNEL_ORDERS = "ORDERS";
    public static final String CHANNEL_INCIDENT = "INCIDENT";
    public static final int LENGHT_DNI=8;
    public static final int LENGHT_CE=12;
    public static final int LENGHT_PAS=12;
    public static final String VERIF_TYPE_BIOMETRICA = "BIOMETRIC";
    public static final String VERIF_TYPE_NO_BIOMETRIC = "NOT BIOMETRIC";
    public static final String VERIF_TYPE_EXONERATE = "EXONERATE";
    public static final int ASSIGNEE_SECTION_ACTIVE = 1;
    public static final String STR_RESULT= "strResult";
    public static final int VALID_UPLOAD_FILE_FLAG = 1;
    public static final String EXECUTION_TYPE_SYNC = "SYNC";
    public static final String EXECUTION_TYPE_ASYNC = "ASYNC";
    public static final String TRANS_TYPE_ALL_DOCUMENTS_BY_RULE = "ALL_DOCUMENTS_BY_RULE";
    public static final int GENERATION_REASON_ERROR = 80;
    public static final int MESSAGE_GEN_DIGITAL_OK=1;
    public static final int MESSAGE_GEN_MANUAL_OK=1;
    public static final String MESSAGE_GENERATION_DOCUMENTS="MESSAGE_GENERATION_DOCUMENTS";
    public static final String TEMPLATE_CODE_PERSON_VENTA="personas_venta";
    public static final String TEMPLATE_CODE_COMPANY_VENTA_NEW="empresa_venta_porta_nuevo";
    public static final String TEMPLATE_CODE_COMPANY_VENTA_OLD="empresa_venta_porta_antiguo";
    public static final String TEMPLATE_CODE_COMPANY_PVT_ALL="empresa_posventa_todas";
    public static final int SIGNATURE_REASON_FOREIGN = 40;
    public static final int SIGNATURE_REASON_DISABLED_VIA = 70;
    public static final int GENERATION_STATUS_INITIAL = 0;
    public static final int GENERATION_STATUS_IN_PROCESS = 2;





    public static final String DNI_ASSIGNEE = "1";
    public static final String SHOW_TYPE_DETAIL="DETAIL";
    public static final String SHOW_TYPE_EDIT="EDIT";
    public static final String SHOW_TYPE_EDIT_DETAIL="EDIT_DETAIL";
    public static final String RESULT = "result";


    //SECTIONS
    public static final int TAG_VI_SECTION = 1;
    public static final int ASSIGNEE_SECTION_ID = 2;
    public static final int ID_SECTION_DIGIT = 3;
    public static final int TAG_VIA_SECTION = 4;


    //JQUISPE PRY-0762
    public static final String CLIENTE_PRE_EVAL_CONDICIONADO = "CONDICIONADO";
    public static final String CLIENTE_PRE_EVAL_TIPO_VENTA_POST = "01";
    public static final String CLIENTE_PRE_EVAL_TIPO_PORTA_POST = "03";


    //PRY-0710 Constantes EFLORES
    public static final String ROL_USER_MOD_PROD_NPTABLE = "ROL_USER_MODIFICAR_PRODUCTO";
    public static final String ROL_USER_MOD_PROD_NPVALUEDESC_SCREEN_OPTION="SCREEN_OPTION";
    public static final String ROL_USER_MOD_PROD_NPVALUEDESC_SPECIFICATION="SPECIFICATION";
    public static final String ROL_USER_MOD_PROD_NPVALUEDESC_INBOX="INBOX";

    //DERAZO PRY-0721
    public static final String DIVISION_TELF_FIJA = "8";
    public static final String CONTROL_ITEM_REGION_ID = "150";

    public static final String DIGITAL_DOCUMENT_GENERATED = "GENERADO";
    public static final String ATTACHE_DIGITAL_DOCUMENTS  =  "CARGADO";
    public static final String STATUS_DIGITAL_DOCUMENTS  =  "PENDIENTE";
    public static final int    LIST_DIGITAL_DOCUMENT_GENERATED  =  0;
    public static final int    LIST_ATTACHE_DIGITAL_DOCUMENTS  =  1;
    public static final int    LIST_ALL_DOCUMENT_GENERATED  =  2;
    public static final String EJEC_TYPE_SYNC  =  "SYNC";
    public static final String EJEC_TYPE_SIGNED_SYNC  =  "SIGNED_SYNC";
    public static final String EJEC_TYPE_ASYNC  =  "ASYNC";
    public static final String TRX_TYPE_ALL_DOCUMENTS_BY_RULE  =  "ALL_DOCUMENTS_BY_RULE";
    public static final String TRX_TYPE_WITH_LENDING  =  "WITH_LENDING";
    public static final String TRX_TYPE_CHANGE_PLAN  =  "CHANGE_PLAN";
    public static final String TRX_TYPE_REJECTED_PORTABILITY  =  "REJECTED_PORTABILITY";
    public static final int    FLAG_SECTION_ACTIVE  =  1;
    public static final int    FLAG_SECTION_ST_ACTIVE  =  2;
    public static final int    FLAG_UPD_DOC_GENERATION_NO  =  0;
    public static final int    FLAG_UPD_DOC_GENERATION_YES  =  1;
    public final static String PARAM_INCIDENT_ID = "incidentId";
    public static final String OPERATION_DOCUMENT_LIST="getDocumentList";
    public static final String COD_ERROR_DOC_LIST = "-1";
    public static final String COD_OK_DOC_LIST = "1";
    public static final String COD_CLEAR_DOC_LIST = "2";
    public static final String COD_WAIT_DOC_LIST = "3";
    public static final String DIGITAL_DOCUMENT_ALL = "TODOS";
    public static final String DOCUMENT_MIME_TYPE_PDF = "application/pdf";
    public static final String DOCUMENT_MIME_TYPE_JPEG = "image/jpeg";
    public static final String DOCUMENT_MIME_TYPE_PJPEG = "image/pjpeg";
    public static final String DOCUMENT_EXT_PDF = "pdf";
    public static final String DOCUMENT_EXT_JPG = "jpg";
    public static final String SIGNATURE_REASON_ERROR_GEN = "80";
    public static final String SOURCE_ORIGIN_NAME_INC = "Incidentes";
    public static final String SOURCE_ORIGIN_NAME_ORD = "Ordenes";
    public static final String OPERATION_GET_DOCUMENT = "getDocument";
    public static final int SIGNATURE_REASON_ERROR_GENERATION = 80;
    public static final String MESSAGE_DIGIT_OK = "La creaci\u00F3n de los formatos digitales fue exitosa, en un momento llegar\u00E1n al correo indicado por el cliente";
    public static final String MESSAGE_MANUAL_OK = "Ya est\u00E1n disponibles los formatos digitales para su firma manual, impr\u00EDmelos desde la pesta\u00F1a Documentos Digitales";
    public static final String MESSAGE_DIGIT_ERROR = "Hubo un error en la generaci\u00F3n de documentos digitales. Seguir el proceso manual con formatos preimpresos.";
    public static final String MESSAGE_REASON_40 = "Esta persona se encuentra exonerada de Verificaci\u00F3n de Identidad. Imprime los documentos digitales desde la pesta\u00F1a Documentos Digitales para su firma manual";
    public static final String MESSAGE_REASON_50 ="Se realiz\u00F3 la verificaci\u00F3n No Biom\u00E9trica con \u00E9xito. Imprime los documentos digitales desde la pesta\u00F1a Documentos Digitales para su firma manual";
    public static final String MESSAGE_REASON_60 = "Hubo un error en la verificaci\u00F3n biom\u00E9trica. Imprime los documentos digitales desde la pesta\u00F1a Documentos Digitales para su firma manual.";
    public static final String MESSAGE_ST_VALIDATE="Debe generar los formatos digitales para poder generar la orden externa";
    public static final String MESSAGE_NO_BIOMETRIC_PORTAB = "Se realiz\u00F3 la verificaci\u00F3n No Biom\u00E9trica con \u00E9xito. Los documentos Digitales se generar\u00E1n sin Firma Digital";
    public static final String MESSAGE_EXONERATE_PORTAB = "Esta persona se encuentra exonerada de Verificaci\u00F3n de Identidad. Los documentos Digitales se generar\u00E1n sin Firma Digital";
    public static final String MESSAGE_REASON_70 = "El cliente no tiene verificaci\u00F3n de identidad aislada habilitada. Imprime los documentos digitales desde la pesta\u00F1a Documentos Digitales para su firma manual.";
    public static final String MESSAGE_ERROR_VIA_ASSIGNEE = "La verificaci\u00F3n de identidad Biom\u00E9trica realizada no coincide con los datos del Apoderado";
    public static final String MESSAGE_ERROR_NO_BIO_VIA_ASSIGNEE = "La verificaci\u00F3n de identidad No Biometrica realizada no coincide con los datos del Apoderado";
    public static final String MESSAGE_ERROR_VIA_CLIENT = "Los datos no coinciden con la Verificaci\u00F3n de Identidad Aislada, ingrese los datos del apoderado";
    public static final String MESSAGE_CONFIRM_VIA_BIOMETRIC = "La verificaci\u00F3n de identidad realizada es No Biom\u00E9trica. Los documentos Digitales se generar\u00E1n sin Firma Digital. ¿Desea continuar?";
    public static final String MESSAGE_ALERT_VIA_ASSIGNEE_EXO = "Ya est\u00E1n disponibles los formatos digitales para su firma manual, impr\u00EDmelos desde la pesta\u00F1a \"Documentos Digitales\"";
    public static final String MESSAGE_ERROR_VIA_ASSIGNEE_EXO = "La verificaci\u00F3n de identidad Exonerada no coincide con los datos del Apoderado";
    public static final String MESSAGE_ALERT_VIA_ASSIGNEE_CLIE = "Ya est\u00E1n disponibles los formatos digitales para su firma manual, impr\u00EDmelos desde la pesta\u00F1a \"Documentos Digitales\"";
    public static final String MESSAGE_ERROR_NO_VIA = "Para este proceso se debe de realizar una verificaci\u00F3n de Identidad Aislada";
    public static final String MESSAGE_ALERT_DISABLED_VIA = "El cliente no tiene verificaci\u00F3n de identidad aislada habilitada. Imprime los documentos digitales desde la pesta\u00F1a \"Documentos Digitales\" para su firma manual";
    public static final String MESSAGE_IN_PROCESS_GENERATION = "Por favor espere la generaci\u00F3n de los documentos digitales";
    public static final String MESSAGE_VALIDATE_IMEILOAN = "Antes de generar el formato por favor seleccionar Activar Equipo";
	public static final String MESSAGE_FILES_REPEATED = "Por favor adjuntar los archivos con diferente nombre";
    public static final int POSTVENTA_TYPE_VARIOS = 1;
    public static final int POSTVENTA_TYPE_REP = 2;
    public static final int POSTVENTA_TYPE_ST = 3;
    public static final int POSTVENTA_TYPE_CP = 4;
    public static final int MAX_CPUF = 3;

    public static final String CODIGO_RESP_DIGIT_OK = "0";
    public static final String CODIGO_RESP_SEND_EMAIL_OK = "3";
    public static final int FLAG_ATT_DOC_LIST_OK = 1;

    public static final String MESSAGE_ALERT_ATT_DOC_OK = "Los archivos se est\u00E1n cargando, se mostrar\u00E1n en cuanto se encuentren listos";

    public static final String newLine = System.getProperty("line.separator");

    //EFLORES PM0011359
    public static final String VEP_OPERATIONS = "VEP_OPERATIONS";
    public static enum VEP_OPERATIONS_ENUM{
        DELETE_AND_SAVE_VEP,DELETE_VEP
    }

    //EFLORES BAFI2
    public static final String CONTROL_ITEM_PROVINCE_ID = "152";
    public static final String CONTROL_ITEM_DISTRICT_ID = "153";

    //CFERNANDEZ WATSHAPP ILIMITADO
    public static final String ERROR_WS_PORTAL_CAUTIVO = "El servicio no se encuentra disponible.";

    //INICIO DERAZO REQ-0940
    public static String DELIVERY_NOTIFICATION_CHECKED = "1";
    public static String DELIVERY_NOTIFICATION_UNCHECKED = "0";

    public static String CONF_SPEC_TRACEABILITY = "SPEC_TRACEABILITY";
    public static String CONF_DISP_PLACE_TRACEABILITY = "DISP_PLACE_TRACEABILITY";
    public static String CONF_TYPE_DOCUMENT_TRACEABILITY = "TYPE_DOCUMENT_TRACEABILITY";
    public static String CONF_TYPE_CUSTOMER_RUC_TRACEABILITY = "TYPE_CUSTOMER_RUC_TRACEABILITY";
    public static String CONF_FLAG_TRACEABILITY = "FLAG_TRACEABILITY";
    //FIN DERAZO

    //EFLORES [TDECONV003-1]
    public static String PREFIJO_TELEFONO = "51";
    //FIN EFLORES [TDECONV003-1]

    //DERAZO TDECONV003-2
    public static final String CONTROL_ITEM_IMEI_FS_ID = "165";
    public static final String LENGTH_IMEI_FS = "15";

    //KOTINIANO [TDECONV003]
    public static String TDE_SWITCH = "TDECONV003_TDE_SWITCH";
    public static String TDE_SWITCH_NPVALUEDESC = "TDECONV003 - Migracion Plan";
    public static String TDE_SWITCH_ON ="1";
    //JCASTILLO TDECONV003
    public static final String SIGNATURE_REASON_MIGRATION_PLAN_VALUE = "120";
    public static final String SIGNATURE_REASON_MIGRATION_PLAN_DESC = "Por migraci&oacute;n de plan";
    public static final int GENERATION_STATUS_ERROR = -1;

    //[TDECONV003-8] INI PZACARIAS
    public static String TDECONV003_8 = "TDECONV003-8";
    public static String MSJ_VAL_NUMTEL_MSISDN = "MSJ_VAL_NUMTEL_MSISDN";
    public static String MSJ_VAL_NUMTEL_SIM = "MSJ_VAL_NUMTEL_SIM";
    public static String MSJ_VAL_NUMTEL_TIP_DOC = "MSJ_VAL_NUMTEL_TIP_DOC";
    public static String CONTRACT_STATUS_FS = "contractStatusFS";
    public static String PLMN_CODE_FS = "plmnCodeFS";

    public static String RESPONSE_FS_STATUS_OK = "OK";
    public static String RESPONSE_FS_STATUS_WARNING = "WARNING";
    public static String RESPONSE_FS_STATUS_ERROR = "ERROR";
    //[TDECONV003-8] FIN

    //INICIO: PRY-1200 | AMENDEZ
    public static String VEP_ORIGIN_APP="APPORDER";
    public static String DEFAULT_QUOTA_VEP="DEFAULT_QUOTA_VEP";
    //FIN: PRY-1200 | AMENDEZ

    //VCEDENO [REGULATORIO PORTABILIDAD]
    public static String MOTIVO_RECHAZO_DEUDA = "REC01PRT09";
    public static String REMITENTO_PORTABILIDAD = "20";
    public static String TIPO_MENSAJE_PORTABILIDAD = "ENVIO";
    public static String ID_MENSAJE_PORTABILIDAD = "APD";
    public static String ORIGEN_PORTABILIDAD = "ORDEN";
    public static String TIPO_OBJETO_PORTABILIDAD = "ORD";
    public static String CONSTANTE_ACREDITACION_PAGO = "-P";

}