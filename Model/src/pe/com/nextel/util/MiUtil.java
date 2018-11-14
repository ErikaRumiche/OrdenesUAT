package pe.com.nextel.util;

import java.io.*;

import java.math.BigDecimal;

import java.math.BigInteger;
import java.sql.Timestamp;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;

import java.util.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;

import org.apache.commons.lang.StringUtils;

import org.apache.xmlbeans.XmlObject;
import pe.com.nextel.bean.DominioBean;
import pe.com.nextel.bean.TableBean;
import pe.com.portability.bean.PortabilityParticipantBean;


public class MiUtil{


    public static String emptyValConcat(String val){
        return (val!=null && val.length()>0)?val + " ":"";
    }

    public static String emptyValObjTrim(Object ob){
        if(ob != null){
            return String.valueOf(ob).trim();
        }else{
            return "";
        }
    }

    public static boolean cadenaVacia(String val){
        if(val == null || val.isEmpty()){
            return true;
        }else{
            return false;
        }
    }

    public static String valText(String val){
        return (val!=null && val.length()>0)?val.trim():"";
    }

    @SuppressWarnings( "rawtypes" )
    private static HashMap<Class, JAXBContext>	objMapaContexto	= new HashMap<Class, JAXBContext>();

    @SuppressWarnings( "rawtypes" )
    private static JAXBContext obtenerContextoJaxBFromClass(Class objClase ){
        JAXBContext objContexto = null;
        objContexto = objMapaContexto.get( objClase );

        if( objContexto == null ){
            try{
                objContexto = JAXBContext.newInstance(objClase);
                objMapaContexto.put( objClase, objContexto );
            }
            catch( Exception e ){
                System.out.println("ERROR creando 'JAXBContext': " + e.getMessage());
            }
        }

        return objContexto;
    }

    @SuppressWarnings( "unchecked" )
    public static String transfromarAnyObjectToXmlText(Object objJaxB ){

        String commandoRequestEnXml = null;

        try{
            JAXBContext objContexto = obtenerContextoJaxBFromClass(objJaxB.getClass() );

            Marshaller objMarshaller = objContexto.createMarshaller();
            StringWriter objStringWritter = new StringWriter();

            objMarshaller.marshal( new JAXBElement( new QName( Constante.CADENA_VACIA, objJaxB.getClass().getName() ), objJaxB.getClass(), objJaxB ), objStringWritter );
            XmlObject objXML = XmlObject.Factory.parse( objStringWritter.toString() );

            commandoRequestEnXml = objXML.toString();
        }
        catch( Exception e ){
            System.out.println("ERROR parseando object to 'XML': " + e.getMessage());
        }

        return commandoRequestEnXml;
    }

    @SuppressWarnings( "rawtypes" )
    public static Object xmlTextToJaxB( String xmlText, Class clas ){
        JAXBContext context;
        Object object = null;

        try{
            context = obtenerContextoJaxBFromClass( clas );
            Unmarshaller um = context.createUnmarshaller();

            InputStream is = new ByteArrayInputStream( xmlText.getBytes( "UTF-8" ) );

            object = um.unmarshal( is );

        }
        catch( Exception e ){
            e.printStackTrace();
            System.out.println("Error unmarshalling xmlObject:" + xmlText);

        }
        return object;
    }

    /***********************************************************************
     ***********************************************************************
     ***********************************************************************
     *  INTEGRACION DE ORDENES Y RETAIL - INICIO
     *  REALIZADO POR: Carmen Gremios Cornelio
     *  FECHA: 28/08/2007
     ***********************************************************************
     ***********************************************************************
     ***********************************************************************/
    public static int parseInt(String val){
        try
        {
            if(val != null && !val.equals(""))
            {
                int i = Integer.parseInt(val);
                return i;
            } else
                return 0;
        }
        catch(Exception e)
        {
            System.out.println("Error convirtiendo:" + val + " a int ERROR=" + e);
        }
        return 0;
    }

    public static double parseDouble(String val)
    {
        try
        {
            if(val != null && !val.equals(""))
            {
                double i = Double.parseDouble(val);
                return i;
            } else
            {
                double d = 0;
                return d;
            }
        }
        catch(Exception e)
        {
            System.out.println("ERROR convirtiendo:" + val + " a double ERROR=" + e);
        }
        int k = 0;
        return (double)k;
    }

    public static long parseLong(String val)
    {
        try
        {
            if(val != null && !val.equals(""))
            {
                val=extractComasPuntos(val);
                long i = Long.parseLong(val);
                return i;
            } else
            {
                long d = 0;
                return d;
            }
        }
        catch(Exception e)
        {
            System.out.println("ERROR convirtiendo:" + val + " a long ERROR=" + e);
        }
        int k = 0;
        return (long)k;
    }

    public static String extractComasPuntos(String cad){

        String result ="";
        if (cad==null)
            return null;
        for (int j=0;j<cad.length();j++){
            if (cad.charAt(j)=='.')
                return result;
            if(cad.charAt(j)!=',')
            {
                result=result+cad.charAt(j);
            }
        }

        return result;
    }

    public static boolean isNotNull(String val)
    {
        return val != null && !val.equals("");
    }

    public static String buildCombo(ArrayList objList, String strValor, String strTexto)
    {
        StringBuffer strbfCadena = new StringBuffer();
        HashMap h=null;
        String strDatoV=null;
        String strDatoT=null;
        if (objList!=null){
            strbfCadena.append("<option value=\"\"></option>");
            for(int i = 0; i < objList.size(); i++)
            {   h = (HashMap)objList.get(i);
                strDatoV= (String)h.get(strValor);
                strDatoT= (String)h.get(strTexto);
                if (strDatoV!=null && !"".equals(strDatoV)){
                    strbfCadena.append("<option value=\"" + strDatoV + "\">");
                    strbfCadena.append(strDatoT);
                    strbfCadena.append("</option>\n");
                }
            }
        }
        return strbfCadena.toString();
    }
    public static String buildInboxCombo(ArrayList objList, String strValor, String strTexto, String strStatus)
    {
        StringBuffer strbfCadena = new StringBuffer();
        HashMap h=null;
        String strDatoV=null;
        String strDatoT=null;
        if (objList!=null){
            strbfCadena.append("<option value=\"\"></option>");
            for(int i = 0; i < objList.size(); i++)
            {   h = (HashMap)objList.get(i);
                strDatoV= (String)h.get(strValor);
                strDatoT= (String)h.get(strTexto);
                if (strDatoV!=null && !"".equals(strDatoV)){
                    if (!(strDatoV.equalsIgnoreCase(strStatus))) {
                        strbfCadena.append("<option value=\"" + strDatoV + "\">");
                        strbfCadena.append(strDatoT);
                        strbfCadena.append("</option>\n");
                    }
                }
            }
        }
        return strbfCadena.toString();
    }
    public static String buildComboExcluded(ArrayList objList, String strValor, String strTexto,String strValorExcluded)
    {
        StringBuffer strbfCadena = new StringBuffer();
        HashMap h=null;
        String strDatoV=null;
        String strDatoT=null;
        if (objList!=null){
            strbfCadena.append("<option value=\"\"></option>");
            for(int i = 0; i < objList.size(); i++)
            {   h = (HashMap)objList.get(i);
                strDatoV= (String)h.get(strValor);
                strDatoT= (String)h.get(strTexto);
                if (strDatoV!=null && !"".equals(strDatoV) && !MiUtil.getString(strValorExcluded).equals(strDatoV)){
                    strbfCadena.append("<option value=\"" + strDatoV + "\">");
                    strbfCadena.append(strDatoT);
                    strbfCadena.append("</option>\n");
                }
            }
        }
        return strbfCadena.toString();
    }

    public static String buildList(ArrayList objList, String strValor, String strTexto)
    {
        StringBuffer strbfCadena = new StringBuffer();
        HashMap h=null;
        String strDatoV=null;
        String strDatoT=null;
        if (objList!=null){
            for(int i = 0; i < objList.size(); i++)
            {   h = (HashMap)objList.get(i);
                strDatoV= (String)h.get(strValor);
                strDatoT= (String)h.get(strTexto);
                if (strDatoV!=null && !"".equals(strDatoV)){
                    strbfCadena.append("<option value=\"" + strDatoV + "\">");
                    strbfCadena.append(strDatoT);
                    strbfCadena.append("</option>\n");
                }
            }
        }
        return strbfCadena.toString();
    }

    public static String buildComboSelected(ArrayList objList, String strValor, String strTexto,String strValueSelected)
    {
        StringBuffer strbfCadena = new StringBuffer();
        HashMap h=null;
        String strDatoV=null;
        String strDatoT=null;
        String strSelected="";
        if (objList!=null){
            strbfCadena.append("<option value=\"\"></option>");
            for(int i = 0; i < objList.size(); i++)
            {  h = (HashMap)objList.get(i);
                strDatoV= (String)h.get(strValor);
                strDatoT= (String)h.get(strTexto);
                strSelected="";
                if (strDatoV!= null && !"".equals(strDatoV)){
                    if (strValueSelected.equals(strDatoV)){
                        strSelected="selected";
                    }
                    strbfCadena.append("<option value=\"" + strDatoV + "\"" +strSelected+">");
                    strbfCadena.append(strDatoT);
                    strbfCadena.append("</option>\n");
                }
            }
        }

        return strbfCadena.toString();
    }


    public static String buildComboWithOneOption(String strValor,String strTexto)
    {
        StringBuffer strbfCadena = new StringBuffer();
        String strSelected="selected";
        strbfCadena.append("<option value=\"0\"></option>");
        strbfCadena.append("<option value=\"" + strValor + "\"" +strSelected+">"+strTexto+"</option>");
        return strbfCadena.toString();
    }

    public static String buildCombo(ArrayList objList, String strValor, String strTexto,String strTexto2)
    {
        StringBuffer strbfCadena = new StringBuffer();
        String strDatoV=null;
        String strDatoT=null;
        String strDatoT2=null;
        HashMap h=null;
        if (objList!=null){
            for(int i = 0; i < objList.size(); i++)
            {   h = (HashMap)objList.get(i);
                strDatoV= (String)h.get(strValor);
                strDatoT= (String)h.get(strTexto);
                strDatoT2= (String)h.get(strTexto2);
                if (strDatoV!=null && !"".equals(strDatoV)){
                    strbfCadena.append("<option value=\"" + strDatoV + "\">");
                    strbfCadena.append(strDatoT + " - "+strDatoT2);
                    strbfCadena.append("</option>\n");
                }
            }
        }
        return strbfCadena.toString();
    }


    public static String toFecha(java.sql.Date dateFecha)
    {
        try
        {  if (dateFecha==null)
            return "";
            DateFormat formatter = null;
            formatter = new SimpleDateFormat("dd/MM/yyyy");
            String strFecha = formatter.format(dateFecha);
            return strFecha;
        }
        catch(Exception e)
        {
            System.out.println("Error getdate:" + e);
        }
        String s = null;
        return s;
    }

    public static String toFecha(java.sql.Timestamp dateFecha)
    {
        try
        {  if (dateFecha==null)
            return "";
            DateFormat formatter = null;
            formatter = new SimpleDateFormat("dd/MM/yyyy");
            String strFecha = formatter.format(dateFecha);
            return strFecha;
        }
        catch(Exception e)
        {
            System.out.println("Error getdate:" + e);
        }
        String s = null;
        return s;
    }


    public static java.sql.Date toFecha(String strFecha,String inFormat)
    {  java.sql.Date dtfecha=null;
        try
        {  if(strFecha==null || "".equals(strFecha))
            return null;
            //"yyyy-MM-dd HH:mm"
            SimpleDateFormat input = new SimpleDateFormat(inFormat);
            dtfecha = new  java.sql.Date(input.parse(strFecha).getTime());
            return dtfecha;
        }
        catch(Exception e)
        {
            System.out.println("Error toFecha:" + e);
            return null;
        }
    }

    public static String toFechaHora(java.sql.Timestamp tsFechaHora)
    {
        try
        {  if (tsFechaHora==null)
            return "";
            DateFormat formatter = null;
            formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            String strFecha = formatter.format(tsFechaHora);
            return strFecha;
        }
        catch(Exception e)
        {
            System.out.println("Error toStringFechaHora:" + e);
        }
        return null;
    }

    public static Timestamp toFechaHora(String strFechaHora,String strFormato)
    {
        try
        {  if (strFechaHora==null){
            return null;
        }
            strFechaHora= strFechaHora.trim();
            SimpleDateFormat sdf = new SimpleDateFormat(strFormato);
            Timestamp t = new Timestamp(sdf.parse(strFechaHora).getTime());
            return t;
        }
        catch(Exception e)
        {
            System.out.println("Error toFechaHora:" + e);
        }
        return null;
    }

    public static String getString(String val)
    {
        if(val != null && !val.equals(""))
            return val.trim();
        else
            return "";
    }

    public static String initCap(String val)
    {  String strInit=null;
        String strEnd=null;
        String strResult=null;
        if(val != null && !val.equals("")){
            val=val.trim();
            strInit=val.substring(0,1);
            strEnd =val.substring(1,val.length());
            strInit=strInit.toUpperCase();
            strEnd=strEnd.toLowerCase();
            strResult=strInit.concat(strEnd);
            return strResult;
        }else
            return "";
    }

    public static String getStringNull(String val)
    {
        if (val.equals(""))
            return null;
        else if(val != null && !val.equals(""))
            return val.trim();
        else
            return val;
    }

    public static String getIfNotEmpty(String val)
    {
        if(val != null && !val.equals(""))
            return val.trim();
        else
            return "0";
    }

    public static String getString(long val)
    {
        if(val == 0)
            return "";
        else
            return String.valueOf(val);
    }

    public static String getDate(String formato)
    {
        try
        {
            DateFormat formatter = null;
            formatter = new SimpleDateFormat(formato);
            String dato = formatter.format(new java.util.Date());
            return dato;
        }
        catch(Exception e)
        {
            System.out.println("Error getDate:" + e);
        }
        return "";
    }


    //Correcion del método estaba devolviendo como fecha 1969-12-31
    public static java.sql.Date getDatePlSql()
    {
        try
        {
            //java.sql.Date dtfecha=new java.sql.Date(0);
            java.util.Date utilDate = new java.util.Date();
            java.sql.Date dtfecha = new java.sql.Date(utilDate.getTime());
            System.out.println("fecha"+dtfecha.toString());
            return dtfecha;
        }
        catch(Exception e)
        {
            System.out.println("Error getDate:" + e);
        }
        return null;
    }
    

/*    public static String dateFormat(String fecha,String inFormat,String outFormat){
       try{
           //"yyyy-MM-dd HH:mm:ss z" 
           SimpleDateFormat input = new SimpleDateFormat(inFormat);
           SimpleDateFormat output = new SimpleDateFormat(outFormat);
           java.sql.Date df = new  java.sql.Date(input.parse(fecha).getTime());  
           String dato = output.format(df);
           return dato;
       }
       catch(Exception e){
           System.out.println("Error dateFormat:" + e);
           return null;
       }        
       
    }

    public static String format(java.sql.Date dateFecha){
       try{
           DateFormat formatter = null;
           formatter = new SimpleDateFormat("dd/MM/yyyy");
           String strFecha = formatter.format(dateFecha);           
           return strFecha;
       }
       catch(Exception e){
           System.out.println("Error getdate:" + e);
       }
       String s = null;
       return s;
    }

    public static String addDate(int num,String formato){ //yyyy:año, MM:mes, dd:dia
       try{
           DateFormat formatter = null;
           formatter=new SimpleDateFormat(formato);            
           Calendar c1 = Calendar.getInstance();
           c1.setTime(new java.util.Date());
           c1.add(Calendar.DATE, num); 
           String dato=formatter.format(c1.getTime());
            return dato;
       }
       catch(Exception e){
           System.out.println("Error getdate:" + e);
           return null;
       }

    }*/


    public static java.sql.Date getDateBD(String formato){ //dd/MM/yyyy
        java.sql.Date result = null;
        try{
      /*SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
      java.sql.Date effect_from = new java.sql.Date(formatter.parse(txt1).getTime());
      java.sql.Date end_on = new java.sql.Date(formatter.parse(txt2).getTime());
      
      */
            SimpleDateFormat formatter = new SimpleDateFormat(formato);
            java.util.Date parsedDate = formatter.parse(getDate(formato));
            result = new java.sql.Date(parsedDate.getTime());
            return result;

        }catch(Exception e){
            System.out.println("Error getDateBD:" + e);
        }

        return result;
    }

    public static java.sql.Timestamp getTimeStampBD(String formato){ //dd/MM/yyyy
        java.sql.Timestamp result = null;
        try{
      /*SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
      java.sql.Date effect_from = new java.sql.Date(formatter.parse(txt1).getTime());
      java.sql.Date end_on = new java.sql.Date(formatter.parse(txt2).getTime());
      
      */
            SimpleDateFormat formatter = new SimpleDateFormat(formato);
            java.util.Date parsedDate = formatter.parse(getDate(formato));
            result = new java.sql.Timestamp(parsedDate.getTime());

            return result;

        }catch(Exception e){
            System.out.println("Error getDateBD:" + e);
        }

        return result;
    }

    public static String formatDecimal(double x){
        return(decimalFormat(x,"###,###,###.00",',','.'));
    }

    public static String decimalFormat (double x, String fmt, char sepMiles, char sepDecimales){
        Locale locale = new Locale ("en", "US");
        DecimalFormatSymbols simbolos = new DecimalFormatSymbols (locale);
        simbolos.setGroupingSeparator (sepMiles);
        simbolos.setDecimalSeparator (sepDecimales);
        return new DecimalFormat(fmt, simbolos).format (x);
    }

    public static String getMessageClean(String msg){
        String strMsgError = "";
   /*//strMsgError=strMsgError.replace(ascii[13],"");
   //strMsgError=strMsgError.replaceAll(CHR(13),"\n");
   if (strMsgError==null)
     return "Error provocado por un NullPointer";   
   strMsgError=strMsgError.replace('"',' ');
   strMsgError=strMsgError.replaceAll("\\u000a"," ");//Línea nueva
   strMsgError=strMsgError.replaceAll("\\u000a"," ");//Línea nueva
   strMsgError=strMsgError.replaceAll("\\u0027","");//Comilla simple
   strMsgError=strMsgError.replaceAll("''","");
   
   return strMsgError;*/

        StringBuffer sbMsg = new StringBuffer("");
        int posIni=0;
        int posFinal=0;

        if (msg!=null) {
            if (msg.length()>0){
                posFinal = msg.indexOf('\n');             // CAMBIO DE LINEA
                while (posFinal!=-1){
                    sbMsg.append(msg.substring(posIni,posFinal)+"\\n");
                    posIni = posFinal+1;
                    posFinal = msg.indexOf('\n',posIni);   // CAMBIO DE LINEA
                }
                if (posIni==0){
                    sbMsg.append(msg);
                }else{
                    sbMsg.append(msg.substring(posIni));
                }
            }
        }

        strMsgError = sbMsg.toString().replaceAll("''","");
        strMsgError = strMsgError.replace('"','\'');
        strMsgError = strMsgError.replaceAll("\\u000a"," ");//Línea nueva
        strMsgError = strMsgError.replaceAll("\\u000d","");
        strMsgError=strMsgError.replaceAll("\\u0027","");

        return strMsgError;
    }


    public static String getStrClean(String strParam){

        strParam=strParam.replaceAll("'","");
        return strParam;
    }


    public static String decode(String strParam1, String strParam2,String strTrueValue,String strFalseValue){
        strParam1 =(strParam1==null?"1":strParam1);
        strParam2 =(strParam2==null?"1":strParam2);
        if (strParam1.equals(strParam2))
            return strTrueValue;
        else
            return strFalseValue;
    }

    public static String getDescripcion(ArrayList objList, String strValor, String strTexto,String strValue)
    {
        HashMap h=null;
        String strDatoV=null;
        String strDatoT=null;
        if (objList!=null){
            for(int i = 0; i < objList.size(); i++)
            {  h = (HashMap)objList.get(i);
                strDatoV= (String)h.get(strValor);
                strDatoT= (String)h.get(strTexto);
                if (strValue!=null && !"".equals(strDatoV)){
                    if (strValue.equals(strDatoV)){
                        return strDatoT;
                    }
                }
            }
        }
        //System.out.println("strbfCadena-->"+strbfCadena);
        return "";
    }

    public static String getValue(ArrayList objList, int iIndice, String strFieldName)
    {
        HashMap objTabla=null;
        String strReturn=null;
        strFieldName=getString(strFieldName);

        try{
            objTabla=(HashMap)objList.get(iIndice);
            strReturn=(String)objTabla.get(strFieldName);
            strReturn=getString(strReturn);
            return strReturn;
        }catch(Exception e){
            return "";
        }
    }

    public static String getValueId(ArrayList objList, String strValor, String strTexto,String strDescripcion)
    {
        HashMap h=null;
        String strDatoV=null;
        String strDatoT=null;
        if (objList!=null){
            for(int i = 0; i < objList.size(); i++)
            {  h = (HashMap)objList.get(i);
                strDatoV= (String)h.get(strValor);
                strDatoT= MiUtil.getString((String)h.get(strTexto));
                if (strDescripcion!=null && !"".equals(strDatoT)){
                    if (strDescripcion.equals(strDatoT)){
                        return strDatoV;
                    }
                }
            }
        }
        return "";
    }


    public static String toAnswer(String strDato)
    {
        if (strDato==null) return "";
        if (Constante.ANSWER_YES.equals(strDato))
            return "Si";
        else
            return "No";
    }

    public static String getDateIfNull(java.sql.Timestamp tsFechaHora){
        String strFecha=null;
        if (tsFechaHora==null)
            strFecha= getDate("dd/MM/yyyy HH:mm");
        else
            strFecha=toFechaHora(tsFechaHora);

        return strFecha;
    }

    public static boolean contentInArrayList(ArrayList objList, String strNameFieldToCompare,String strValueToCompare)
    {
        HashMap h=null;
        String strDato=null;
        if (strValueToCompare==null)
            return false;

        strValueToCompare=strValueToCompare.toUpperCase();
        if (objList!=null){
            for(int i = 0; i < objList.size(); i++)
            {  h = (HashMap)objList.get(i);
                strDato= MiUtil.getString((String)h.get(strNameFieldToCompare)).toUpperCase();
                if (strValueToCompare.equals(strDato)){
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean contentStringInArrayList(ArrayList objList, String strValueToCompare){

        String strItem=null;
        String strDato=null;
        if (strValueToCompare==null)
            return false;

        if (objList!=null){
            for(int i = 0; i < objList.size(); i++){
                strItem = (String)objList.get(i);
                if (strValueToCompare.equals(strItem)){
                    return true;
                }
            }
        }
        return false;
    }


    public static boolean contentInArray(int value, int[] arreglo){

        for(int i=0;i<arreglo.length;i++){
            if ( value==arreglo[i]){
                return true;
            }
        }
        return false;
    }


    public static String escape(String strCadena){
        String strReturn="";
        if (strCadena!=null)
            strReturn=strCadena.replaceAll("\\u0027","\'");

        return strReturn;
    }

    public static String escape2(String strCadena){

        String strReturn="";
        if (strCadena!=null){
            char cComilla ='\u0022';
            strCadena=strCadena.replaceAll("'","u01X270z");
            strCadena=strCadena.replaceAll("&","u01X260z");
            strReturn=strCadena.replaceAll(cComilla+"","u01X220z");
        }
        return strReturn;
    }

    public static String unescape2(String strCadena){
        String strReturn="";
        if (strCadena!=null){
            char cComilla ='\u0022';
            strCadena=strCadena.replaceAll("u01X270z","'"); //comilla simple
            strCadena=strCadena.replaceAll("u01X260z","&"); //ampersand
            strReturn=strCadena.replaceAll("u01X220z",cComilla+"");
        }
        return strReturn;
    }

/***********************************************************************
 ***********************************************************************
 ***********************************************************************
 *  INTEGRACION DE ORDENES Y RETAIL - FIN
 *  REALIZADO POR: Carmen Gremios Cornelio
 *  FECHA: 28/08/2007
 ***********************************************************************
 ***********************************************************************
 ***********************************************************************/

    /***********************************************************************
     ***********************************************************************
     ***********************************************************************
     *  INTEGRACION DE ORDENES Y RETAIL - INICIO
     *  REALIZADO POR: Lee Rosales Crispin
     *  FECHA: 06/11/2007
     ***********************************************************************
     ***********************************************************************
     ***********************************************************************/

    public static void printBottomReplaceMessage(String appContext,String strMessage,PrintWriter out){
        //out.println("<script>");
        out.println("alert('" + strMessage + "')");
        //out.println("</script>");
        out.println("location.replace('"+appContext+"/websales/Bottom.html');");
        out.close();
    }
    public static String getStringObject(Object[] val,int indice){
        if(val != null)
            return (String)val[indice];
        else
            return "";
    }

    /***********************************************************************
     ***********************************************************************
     ***********************************************************************
     *  INTEGRACION DE ORDENES Y RETAIL - INICIO
     *  REALIZADO POR: Lee Rosales Crispin
     *  FECHA: 06/11/2007
     ***********************************************************************
     ***********************************************************************
     ***********************************************************************/

    /**
     * Motivo: Obtiene una fecha según determinado formato (Ejm: dd/MM/yyyy)
     * <br>Realizado por: <a href="mailto:richard.delosreyes@nextel.com.pe">Richard De los Reyes</a>
     * <br>Fecha: 24/10/2007
     *
     * @return Fecha como Cadena.
     * @param formato
     * @param fecha
     */
    public static String getDate(java.util.Date fecha, String formato) {
        try {
            return new SimpleDateFormat(formato).format(fecha);
        }
        catch(Exception e) {
            //GenericObject.logger.error(GenericObject.formatException(e));
        }
        return "";
    }
   /* Inicio Descomentar para Desarrollo*/

    public static Context getInitialContext() throws NamingException {
        Hashtable env = new Hashtable();

        StaticProperties singleton = StaticProperties.instance();
        Properties properties = singleton.props;

        try {
            env.put(Context.INITIAL_CONTEXT_FACTORY, properties.getProperty("JNDI.INITIAL_CONTEXT_FACTORY"));
            env.put(Context.PROVIDER_URL,properties.getProperty("JNDI.PROVIDER_URL"));
            env.put(Context.SECURITY_PRINCIPAL, properties.getProperty("JNDI.SECURITY_PRINCIPAL"));
            env.put(Context.SECURITY_CREDENTIALS, properties.getProperty("JNDI.SECURITY_CREDENTIALS"));
        }catch(Exception e){
            e.printStackTrace();
        }
        return new InitialContext(env);
    }
   
    /*Fin Descomentar para Desarrollo */


    //  Inicio Descomentar para Local
  /*
   public static Context getInitialContext() throws NamingException {
      System.out.println("[Inicio][getInitialContext]");
      Hashtable env = new Hashtable();
      env.put(Context.INITIAL_CONTEXT_FACTORY, "com.evermind.server.rmi.RMIInitialContextFactory");
      env.put(Context.SECURITY_PRINCIPAL, "oc4jadmin");
      env.put(Context.SECURITY_CREDENTIALS, "welcome");
      env.put(Context.PROVIDER_URL, "ormi://localhost:23892/current-workspace-app");      
      System.out.println("[Fin][getInitialContext]");
      return new InitialContext(env);      
   }
   
   public static Context getInitialContextServ1() throws NamingException {
      Hashtable env = new Hashtable();
      env.put(Context.INITIAL_CONTEXT_FACTORY, "com.evermind.server.rmi.RMIInitialContextFactory");
      env.put(Context.SECURITY_PRINCIPAL, "admin");
      env.put(Context.SECURITY_CREDENTIALS, "welcome");
      env.put(Context.PROVIDER_URL, "ormi://localhost:23892/current-workspace-app");      
      return new InitialContext(env);      
   }
 
   public static Context getInitialContextServ2() throws NamingException {
      System.out.println("[Inicio][getInitialContextServ2]");
      Hashtable env = new Hashtable();
      env.put(Context.INITIAL_CONTEXT_FACTORY, "com.evermind.server.rmi.RMIInitialContextFactory");
      env.put(Context.SECURITY_PRINCIPAL, "admin");
      env.put(Context.SECURITY_CREDENTIALS, "welcome");
      env.put(Context.PROVIDER_URL, "ormi://172.25.103.56:23892/current-workspace-app");      
      //env.put("dedicated.rmicontext", "true");   
      System.out.println("[Fin][getInitialContextServ2]");
      return new InitialContext(env);      
   }
  */
    //Fin Descomentar para Local

    /***********************************************************************
     ***********************************************************************
     ***********************************************************************
     * INTEGRACION DE ORDENES Y RETAIL - INICIO
     * REALIZADO POR: Richard De los Reyes
     * FECHA: 19/11/2007
     ***********************************************************************
     ***********************************************************************
     **********************************************************************/

    public static BigDecimal defaultBigDecimal(Object objBigDecimal, BigDecimal bdDefault) {
        return objBigDecimal==null?bdDefault:(BigDecimal) objBigDecimal;
    }

    public static String defaultString(Object objString, String strDefault) {
        return objString==null?strDefault:objString.toString();
    }

    public static String buildComboSelected(ArrayList arrList, String strValueSelected) {
        StringBuffer strbfCadena = new StringBuffer();
        DominioBean dominio = new DominioBean();
        String strDatoV = null;
        String strDatoT = null;
        String strSelected = "";
        if (arrList != null) {
            strbfCadena.append("<option value=\"\"></option>");
            for(int i = 0; i < arrList.size(); i++) {
                dominio = (DominioBean) arrList.get(i);
                strDatoV = dominio.getValor();
                strDatoT = dominio.getDescripcion();
                strSelected = "";
                if (StringUtils.isNotEmpty(strDatoV)) {
                    if (strDatoV!=null && strValueSelected.equals(strDatoV)){
                        strSelected="selected=\"selected\"";
                    }
                    strbfCadena.append("<option value=\"" + strDatoV + "\"" +strSelected+">");
                    strbfCadena.append(strDatoT);
                    strbfCadena.append("</option>\n");
                }
            }
        }
        return strbfCadena.toString();
    }

    public static String buildComboSelected1(ArrayList arrList, String strValueSelected) {
        StringBuffer strbfCadena = new StringBuffer();
        DominioBean dominio = new DominioBean();
        String strDatoV = null;
        String strDatoT = null;
        String strSelected = "";
        if (arrList != null) {
            strbfCadena.append("<option value=\"\"></option>");
            for(int i = 0; i < arrList.size(); i++) {
                dominio = (DominioBean) arrList.get(i);
                strDatoV = dominio.getValor();
                strDatoT = dominio.getDescripcion();
                strSelected = "";
                if (StringUtils.isNotEmpty(strDatoV)) {
                    if (strDatoV!=null && strValueSelected.equals(strDatoV)){
                        strSelected="selected=\"selected\"";
                    }
                    strbfCadena.append("<option value=\"" + strDatoT + "\"" +strSelected+">");
                    strbfCadena.append(strDatoT);
                    strbfCadena.append("</option>\n");
                }
            }
        }
        return strbfCadena.toString();
    }

    public static String buildComboSelected(ArrayList arrList, String strValueSelected, boolean bShowEmpty) {
        StringBuffer strbfCadena = new StringBuffer();
        DominioBean dominio = new DominioBean();
        String strDatoV = null;
        String strDatoT = null;
        String strSelected = "";
        if (arrList != null) {
            if (bShowEmpty) {
                strbfCadena.append("<option value=\"\"></option>");
            }
            for(int i = 0; i < arrList.size(); i++) {
                dominio = (DominioBean) arrList.get(i);
                strDatoV = dominio.getValor();
                strDatoT = dominio.getDescripcion();
                strSelected = "";
                if (StringUtils.isNotEmpty(strDatoV)) {
                    if (strDatoV!=null && strValueSelected.equals(strDatoV)){
                        strSelected="selected=\"selected\"";
                    }
                    strbfCadena.append("<option value=\"" + strDatoV + "\"" +strSelected+">");
                    strbfCadena.append(strDatoT);
                    strbfCadena.append("</option>\n");
                }
            }
        }
        return strbfCadena.toString();
    }

    /***********************************************************************
     ***********************************************************************
     ***********************************************************************
     * INTEGRACION DE ORDENES Y RETAIL - FIN
     * REALIZADO POR: Richard De los Reyes
     * FECHA: 19/11/2007
     ***********************************************************************
     ***********************************************************************
     **********************************************************************/

    /***********************************************************************
     *  INICIO
     *  REALIZADO POR: Cristian Espinoza
     *  FECHA: 15/01/2008
     ***********************************************************************/

    public static String deleteTagHTML(String strCadena, int iTag) {

        String strCadenaResultante="";
        for (int x=0; x < strCadena.length(); x++) {
            if ((int)strCadena.charAt(x) != iTag)
                strCadenaResultante += strCadena.charAt(x);
        }
        return getStringNull(strCadenaResultante);
    }

    public static String getParameterCadenaURL(String parametros,String strParamEncontrar){

        String strValorParam="";
        int indInicio=parametros.indexOf(strParamEncontrar);
        if (indInicio!=-1)	{ //el parametro existe en la URL enviada
            int indFinValorParametro=parametros.indexOf("&",indInicio);
            if (indFinValorParametro==-1)//es el ultimo elemento
                indFinValorParametro=parametros.length();
            int indInicioValorParametro=parametros.indexOf("=",indInicio);
            strValorParam=parametros.substring(indInicioValorParametro+1,indFinValorParametro);
        }
        return strValorParam;
    }
    /***********************************************************************
     *  FIN
     *  REALIZADO POR: Cristian Espinoza
     *  FECHA: 15/01/2008
     ***********************************************************************/
    /**
     * @autor Odubock
     * 22/05/2008
     * @return retorna el mensaje extenso, formateado para poder mostrar sin errores la alerta o mensaje
     * @param message
     */
    public static String showCleanAlerts(String message){
        StringBuffer sbMessage = new StringBuffer("");
        int posIni=0;
        int posFinal=0;

        if (message!=null) {
            if (message.length()>0){
                posFinal = message.indexOf('\n');
                while (posFinal!=-1){
                    sbMessage.append(message.substring(posIni,posFinal)+"\\n");
                    posIni = posFinal+1;
                    posFinal = message.indexOf('\n',posIni);
                }
                if (posIni==0){
                    sbMessage.append(message);
                }else{
                    sbMessage.append(message.substring(posIni));
                }
            }
        }
        return sbMessage.toString().replace('"','\'');
    }
    /**
     * Imprime el mensaje de error lanzado por una excepción.
     * @autor Estefanía Gamonal
     * 13/06/2008
     * @param message
     */
    public static void printErrorMessage(String strMessage, PrintWriter out){
        out.println("<script>");
        out.println("alert('"+getMessageClean(strMessage)+"')");
        out.println("</script>");
        out.close();
    }


    /**
     * Imprime el tipo de teléfono para ST, Sí es 'S' devuelve 'Orginal', de lo contrario 'Refurbish' .
     * @autor Tomás Mogrovejo
     * 21/05/2009
     * @param message
     */

    public static String toTypePhone(String strDato)
    {
        if (strDato==null) return "";
        if (Constante.ANSWER_YES.equals(strDato))
            return "Original";
        else
            return "Refurbish";
    }

    /**
     * Verifica si existe un string en un array de nptable bean.
     * @autor María Isabel Limaylla
     * 31/07/2009
     * @param objList message
     */
    public static boolean contentStringInArrayListOfNpTableBean(ArrayList objList, String strValueToCompare)
    {
        TableBean objTableBean = null;
        String strDato=null;
        if (strValueToCompare==null)
            return false;

        strValueToCompare=strValueToCompare.toUpperCase();
        if (objList!=null){

            for(int i = 0; i < objList.size(); i++)
            {
                objTableBean = new TableBean();
                objTableBean = (TableBean)objList.get(i);

                strDato= MiUtil.getString((String)objTableBean.getNpValue()).toUpperCase();
                if (strValueToCompare.equals(strDato)){
                    return true;
                }
            }
        }
        return false;


    }


    /**
     * Construye el combo de Cedentes de una Orden de portabilidad.
     * @autor David Lazo de la Vega
     * 24/08/2009
     * @param message
     */
    public static String buildComboSelectedAssig(ArrayList arrList, String strValueSelected) {
        StringBuffer strbfCadena = new StringBuffer();
        PortabilityParticipantBean dominio = new PortabilityParticipantBean();
        String strDatoV = null;
        String strDatoT = null;
        String strSelected = "";
        if (arrList != null) {
            strbfCadena.append("<option value=\"\"></option>");
            for(int i = 0; i < arrList.size(); i++) {
                dominio = (PortabilityParticipantBean) arrList.get(i);
                strDatoV = dominio.getNpParticipantId();
                strDatoT = dominio.getNpDescripcion();
                strSelected = "";
                if (StringUtils.isNotEmpty(strDatoV)) {
                    if (strDatoV!=null && strValueSelected.equals(strDatoV)){
                        strSelected="selected=\"selected\"";
                    }
                    strbfCadena.append("<option value=\"" + strDatoV + "\"" +strSelected+">");
                    strbfCadena.append(strDatoT);
                    strbfCadena.append("</option>\n");
                }
            }
        }
        return strbfCadena.toString();
    }

    public static String trimNotNull(String cadena){
        if(cadena == null){
            cadena = "";
        }else{
            cadena = cadena.trim();
        }
        return cadena;
    }

    public static String getWSProcessPortedNumberEnabled() throws NamingException {
        StaticProperties singleton = StaticProperties.instance();
        Properties properties = singleton.props;
        String strEnabled = null;
        try {
            strEnabled =  properties.getProperty("WS.PROCESSPORTEDNUMBER_ENABLED");
        }catch(Exception e){
            e.printStackTrace();
        }
        return strEnabled;
    }

    public static String getString(Object value) {
        return value != null ? value.toString() : null;
    }

    public static int getInt(Object value) {
        try {
            return Integer.parseInt(getString(value));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static long getLong(Object value) {
        try {
            return Long.parseLong(getString(value));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static BigInteger getBigInteger(Object value) {
        return BigInteger.valueOf(getLong(value));
    }

    public static Integer getInteger(Object value) {
        try {
            return Integer.parseInt(getString(value));
        } catch (NumberFormatException e) {
            return null;
        }
    }
    
    //JQUISPE PRY-0762 Obtiene BigDecimal
    public static BigDecimal getBigDecimal(String val){
    	
    	BigDecimal valor = null;
    	
    	try{
    		valor = new BigDecimal(val);
    	}catch(Exception e){
    		System.out.println("ERROR convirtiendo:" + val + " a BigDecimal ERROR=" + e);
    	}
    	
    	return valor;
    }    
        
    //MVERA PRY-0762 Valida especificacion Renta Adelantada
    public static HashMap<String,String> getNpTable(List<HashMap<String,String>> list, String npvalue){        
    	for(HashMap<String,String> map : list){
    		if(map.get("wv_npValue").equalsIgnoreCase(npvalue)){
    			return map;
    		}
    	}
    	return null;
    }    
    
    //DESP PRY-0762 Construye combo tabla
    public static String buildComboTabla(ArrayList arrList, String strValueSelected) {
        StringBuffer strbfCadena = new StringBuffer();
        TableBean tableBean = new TableBean();
        String strDatoV = null;
        String strDatoT = null;
        String strSelected = "";
        if (arrList != null) {
            strbfCadena.append("<option value=\"\"></option>");
            for(int i = 0; i < arrList.size(); i++) {
            	tableBean = (TableBean) arrList.get(i);
                strDatoV = tableBean.getNpTable();
                strDatoT = tableBean.getNpValue();
                strSelected = "";
                if (StringUtils.isNotEmpty(strDatoV)) {
                    if (strDatoV!=null && strValueSelected.equals(strDatoV)){
                        strSelected="selected=\"selected\"";
                    }
                    strbfCadena.append("<option value=\"" + strDatoV + "\"" +strSelected+">");
                    strbfCadena.append(strDatoT);
                    strbfCadena.append("</option>\n");
                }
            }
        }
        return strbfCadena.toString();
    }

    /**
     * @autor JCURI on 14/07/2017
     * @param trackingID
     */
    public static String generateTrackingID() {
		String trackingID = StringUtils.EMPTY;
		try {
			trackingID = TrackingIdGenerator.getInstance().generateLongId();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return trackingID;
	}

    /**
     * @autor JCURI on 16/07/2017
     * @param igv,amount,quantity
     */
    public static int calculateRoundPA(double igv, double amount, int quantity) {
		try {
			double priceIgv = (amount * igv)/100;
			int roundPrice =(int) Math.ceil(amount + priceIgv);
			roundPrice = roundPrice * quantity;
			return roundPrice;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

}