package pe.com.nextel.servlet;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import pe.com.nextel.bean.*;
import pe.com.nextel.service.NormalizarDireccionService;
import pe.com.nextel.util.Constante;
import pe.com.nextel.util.MiUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by Administrador on 01/10/2015.
 */
public class NormalizarDireccionServlet extends GenericServlet {
    private static final long serialVersionUID = 1L;

    private static final String CONTENT_TYPE = "text/html; charset=UTF-8";
    NormalizarDireccionService normalizarDireccionService= new NormalizarDireccionService();

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)  throws ServletException, IOException {
        response.setContentType(CONTENT_TYPE);
        doGet(request, response);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{
            String myaction=request.getParameter("myaction");
            if(myaction!=null) {
                if(myaction.equals("validarCargaNorm")){
                    validarCargaNorm(request, response);
                }else if (myaction.equals("cargarpopupEdit")) {
                    PrintWriter out = response.getWriter();
                    try{
                        out.println("<html>");
                        out.println("<head><title>OrderServlet1</title></head>");
                        out.println("<script >");
                        cargarPopupNormalizacionEdit(request, response, out);
                    }catch(Exception e){
                        e.printStackTrace();
                    }finally{
                        out.println("</script>");
                        out.close();
                    }
                } else if(myaction.equals("getDirNorm")){
                    getListDireccionNormalizada(request, response);
                } else if(myaction.equals("obtenerListTipos")){
                    obtenerListTipos(request, response);
                } else if(myaction.equals("obtenerDepartamentos")){
                    obtenerListDepProvDist(request, response, Constante.FLAG_UBIGEO_DEPARTAMENTO);
                } else if(myaction.equals("obtenerProvincias")){
                    obtenerListDepProvDist(request, response, Constante.FLAG_UBIGEO_PROVINCIA);
                } else if(myaction.equals("obtenerDistritos")){
                    obtenerListDepProvDist(request, response, Constante.FLAG_UBIGEO_DISTRITO);
                } else if(myaction.equals("obtenerIdDepProvDist")){
                    obtenerIdDepProvDist(request, response);
                } else if(myaction.equals("obtenerDirecciones")){
                    obtenerDirecciones(request, response);
                } else if(myaction.equals("guardarDireccionesNorm")){
                    guardarDireccionesNorm(request, response);
                } else if(myaction.equals("mensajeAlertServer")){
                    mensajeAlertServer(request, response);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
            response.setContentType("text/plain");
            response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
            response.getWriter().write(e.getMessage());
            response.flushBuffer();
        }
    }

    private void mensajeAlertServer(HttpServletRequest request, HttpServletResponse response)throws ServletException,IOException{
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        try {
            Type mapType = new TypeToken<Map<String, String>>(){}.getType();
            Map<String, String> dataJ = new Gson().fromJson(request.getParameter("myObjMain"), mapType);

            String table = dataJ.get("table");
            String value = dataJ.get("value");

            String mensaje = normalizarDireccionService.getValueDescXTableAndValue(table, value);

            Map<String, Object> respJson = new HashMap<String, Object>();
            respJson.put("mensajeAlert", mensaje);
            Gson gson = new Gson();
            String resp = gson.toJson(respJson);

            out.write(resp);
        }catch(Exception e){
            e.printStackTrace();
            response.setContentType("text/plain");
            response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
            response.getWriter().write(e.getMessage());
            response.flushBuffer();
        }finally{
            out.close();
        }
    }

    private void validarCargaNorm(HttpServletRequest request, HttpServletResponse response)throws ServletException,IOException{
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        Map<String, Object> respJson = new HashMap<String, Object>();
        try{
            if(normalizarDireccionService.normalizacionIsActive()){
                long orderId = Long.parseLong(request.getParameter("orderID"));
                String datosNorm = normalizarDireccionService.datosOrdenId(orderId);
                System.out.println("[NormalizarDireccionServlet][validarCargaNorm] - orderId: "+orderId+"; datosNorm: " + datosNorm);
                if(datosNorm != null) {
                    if (normalizarDireccionService.validarDatos(datosNorm)) {
                        respJson.put("valida", 1);
                        respJson.put("datosNorm", datosNorm);
                    } else {
                        respJson.put("valida", 0);
                    }
                }else{
                    respJson.put("valida", 0);
                }
            }

            Gson gson = new Gson();
            String resp = gson.toJson(respJson);

            out.write(resp);
        }catch(Exception e){
            respJson.put("valida", 0);
            Gson gson = new Gson();
            String resp = gson.toJson(respJson);

            out.write(resp);
        }finally{
            out.close();
        }
    }

    private void guardarDireccionesNorm(HttpServletRequest request, HttpServletResponse response)throws ServletException,IOException{
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        Map<String, Object> respJson = new HashMap<String, Object>();
        try{
            Type mapType = new TypeToken<Map<String, ArrayList<HashMap<String, Object>>>>(){}.getType();
            Map<String, ArrayList<HashMap<String, Object>>> dataJ = new Gson().fromJson(request.getParameter("idDirNorm"), mapType);

            List<HashMap<String, Object>> arrayDir = dataJ.get("arrayDirNorm");

            List<String> listDirEqual = new ArrayList<String>();
            for(int i=0; i<arrayDir.size(); i++){
                Map<String, Object> map = (HashMap<String, Object>)arrayDir.get(i);
                String tipo = MiUtil.emptyValObjTrim(map.get("tipo"));

                boolean isFoundEquals = false;
                String operacion = MiUtil.emptyValObjTrim(map.get("operacion"));
                if(listDirEqual.size() > 0){
                    for(int k=0; k<listDirEqual.size(); k++){
                        if(listDirEqual.get(k).equals(tipo)){
                            isFoundEquals = true;
                            break;
                        }
                    }

                    if(!isFoundEquals || operacion.equals("I")){
                        updateDireccionCliente(map);
                    }
                }else{
                    updateDireccionCliente(map);
                }

                if(!isFoundEquals){
                    String tiposDirEquals = MiUtil.emptyValObjTrim(map.get("tiposDirEquals"));
                    String[] arrayDirEqual = tiposDirEquals.split(",");
                    if(arrayDirEqual.length > 1 && operacion.equals("U")){
                        for(int j=0; j<arrayDirEqual.length; j++){
                            if(!arrayDirEqual[j].equals(tipo)){
                                listDirEqual.add(arrayDirEqual[j]);
                            }
                        }
                    }
                }
            }
            respJson.put("valida", 1);
            Gson gson = new Gson();
            String resp = gson.toJson(respJson);

            out.write(resp);
        }catch(Exception e){
            e.printStackTrace();
            response.setContentType("text/plain");
            response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
            response.getWriter().write("Error al actualizar las direcciones");
            response.flushBuffer();
        }finally{
            out.close();
        }
    }

    private String updateDireccionCliente(Map<String, Object> map)throws Exception{
        System.out.println("GUARDADO: "+map.get("tipo"));
        System.out.println("indice[] idAddress: "+map.get("idDir"));

        LinkedTreeMap subMap = (LinkedTreeMap)map.get("data");
        DireccionNormWSBean bean = new DireccionNormWSBean();
        ComplDireccionUpBean objCompl = new ComplDireccionUpBean();

        String estadoFlag = MiUtil.emptyValObjTrim(map.get("estado"));
        if(String.valueOf(Constante.ESTADO_DIR_NO_NORMALIZADO).equals(estadoFlag)){
            objCompl.setEstado(MiUtil.emptyValObjTrim(map.get("estado")));
            objCompl.setMotivo(MiUtil.emptyValObjTrim(map.get("motivo")));

            System.out.println("indice[] tipo: "+map.get("tipo"));
            System.out.println("indice[] estado: "+map.get("estado"));
            System.out.println("indice[] motivo: "+map.get("motivo"));
            System.out.println("indice[] operacion: "+map.get("operacion"));
            System.out.println("indice[] tiposEquals: "+map.get("tiposDirEquals"));
        }else if(String.valueOf(Constante.ESTADO_DIR_NORMALIZADO).equals(estadoFlag) ||
                String.valueOf(Constante.ESTADO_DIR_NORMALIZADO_EDITADO).equals(estadoFlag)){
            if(subMap!=null){
                bean.setTipoVia(MiUtil.emptyValObjTrim(subMap.get("tipoVia")));
                bean.setNombreVia(MiUtil.emptyValObjTrim(subMap.get("nombreVia")));
                bean.setNumeroPuerta(MiUtil.emptyValObjTrim(subMap.get("numeroPuerta")));
                bean.setManzana(MiUtil.emptyValObjTrim(subMap.get("manzana")));
                bean.setNombreUbanizacion(MiUtil.emptyValObjTrim(subMap.get("nombreUbanizacion")));
                bean.setNumeroInterior(MiUtil.emptyValObjTrim(subMap.get("numeroInterior")));
                bean.setLote(MiUtil.emptyValObjTrim(subMap.get("lote")));
                bean.setTipoZona(MiUtil.emptyValObjTrim(subMap.get("tipoZona")));
                bean.setNombreZona(MiUtil.emptyValObjTrim(subMap.get("nombreZona")));
                bean.setTipoInterior(MiUtil.emptyValObjTrim(subMap.get("tipoInterior")));
                bean.setReferencia(MiUtil.emptyValObjTrim(subMap.get("referencia")));
            }

            if(String.valueOf(Constante.ESTADO_DIR_NORMALIZADO).equals(estadoFlag)){
                bean.setGeolocalizacionX(new BigDecimal((Double)subMap.get("geolocalizacionX")));
                bean.setGeolocalizacionY(new BigDecimal((Double)subMap.get("geolocalizacionY")));
            }

            String direccionNorm = (MiUtil.emptyValConcat(bean.getTipoVia()) + MiUtil.emptyValConcat(bean.getNombreVia())).trim();

            String addressSales = bean.getTipoVia()  +";"+
                    bean.getNombreVia()  +";"+
                    (MiUtil.emptyValConcat(bean.getNumeroPuerta())+ bean.getManzana()).trim() +";" +
                    (MiUtil.emptyValConcat(bean.getTipoInterior()) + MiUtil.emptyValConcat(bean.getNumeroInterior())+ bean.getLote()).trim()+";" +
                    bean.getTipoZona() +";" +
                    bean.getNombreZona();

            objCompl.setDireccionNorm(direccionNorm);
            objCompl.setEstado(MiUtil.emptyValObjTrim(map.get("estado")));
            objCompl.setMotivo(MiUtil.emptyValObjTrim(map.get("motivo")));
            objCompl.setUbigeo(MiUtil.emptyValObjTrim(map.get("codUbigeo")));
            objCompl.setDescDep(MiUtil.emptyValObjTrim(map.get("textUbigeoDep")));
            objCompl.setDescProv(MiUtil.emptyValObjTrim(map.get("textUbigeoProv")));
            objCompl.setDescDist(MiUtil.emptyValObjTrim(map.get("textUbigeoDist")));
            objCompl.setIdDep(MiUtil.emptyValObjTrim(map.get("idPKDep")));
            objCompl.setIdProv(MiUtil.emptyValObjTrim(map.get("idPKProv")));
            objCompl.setIdDist(MiUtil.emptyValObjTrim(map.get("idPKDist")));
            objCompl.setAddressSales(addressSales);
            objCompl.setOperacion(MiUtil.emptyValObjTrim(map.get("operacion")));
            objCompl.setTipoDireccion(MiUtil.emptyValObjTrim(map.get("tipo"))+"0");

            System.out.println("indice[] tipo: "+map.get("tipo")+"0");
            System.out.println("indice[] estado: "+map.get("estado"));
            System.out.println("indice[] motivo: "+map.get("motivo"));
            System.out.println("indice[] operacion: "+map.get("operacion"));
            System.out.println("indice[] tiposEquals: "+map.get("tiposDirEquals"));

            System.out.println("indice[] tipoVia: "+subMap.get("tipoVia"));
            System.out.println("indice[] nombreVia: "+subMap.get("nombreVia"));
            System.out.println("indice[] tipoZona: "+subMap.get("tipoZona"));
            System.out.println("indice[] nombreZona: "+subMap.get("nombreZona"));
            System.out.println("indice[] ubigeo: "+map.get("codUbigeo"));
            System.out.println("----------------------------------------------------");
        }

        BigDecimal idAddress = new BigDecimal(MiUtil.emptyValObjTrim(map.get("idDir")));
        String resultUpdate = normalizarDireccionService.updateAddressNormalize(idAddress, objCompl, bean);

        return resultUpdate;
    }

    private void obtenerDirecciones(HttpServletRequest request, HttpServletResponse response)throws ServletException,IOException{
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        try{
            long longRegionId=11111;
            String strObjectType="CUSTOMER";
            String strGeneratortype = "INC";
            long codCliente = Long.parseLong(request.getParameter("codCliente"));

            List<DireccionClienteBean> listDir = normalizarDireccionService.obtenerDireccionCliente(codCliente, longRegionId, strObjectType, strGeneratortype);

            Map<String, Object> respJson = new HashMap<String, Object>();
            respJson.put("listDir", listDir);

            Gson gson = new Gson();
            String resp = gson.toJson(respJson);

            out.write(resp);
        }catch(Exception e){
            e.printStackTrace();
            response.setContentType("text/plain");
            response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
            response.getWriter().write(e.getMessage());
            response.flushBuffer();
        }finally{
            out.close();
        }
    }

    private void obtenerListTipos(HttpServletRequest request, HttpServletResponse response)throws ServletException,IOException{
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        try{
            ArrayList listTipo = normalizarDireccionService.obtenerListTipo(request.getParameter("tipo"));

            Map<String, Object> respJson = new HashMap<String, Object>();
            respJson.put("listTipo", listTipo);

            Gson gson = new Gson();
            String resp = gson.toJson(respJson);

            out.write(resp);
        }catch(Exception e){
            e.printStackTrace();
            response.setContentType("text/plain");
            response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
            response.getWriter().write(e.getMessage());
            response.flushBuffer();
        }finally{
            out.close();
        }
    }

    private void obtenerListDepProvDist(HttpServletRequest request, HttpServletResponse response, int tipo)throws ServletException,IOException{
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        try{
            String codDep = Constante.CADENA_VACIA;
            String codProv = Constante.CADENA_VACIA;
            if(Constante.FLAG_UBIGEO_PROVINCIA == tipo || Constante.FLAG_UBIGEO_DISTRITO == tipo){
                codDep = request.getParameter("codDep");
                if(Constante.FLAG_UBIGEO_DISTRITO == tipo){
                    codProv = request.getParameter("codProv");
                }
            }

            List<NewUbigeoBean> listDep = normalizarDireccionService.obtenerListDepProvDist(tipo, codDep, codProv);

            Map<String, Object> respJson = new HashMap<String, Object>();
            respJson.put("listDepProvDist", listDep);

            Gson gson = new Gson();
            String resp = gson.toJson(respJson);

            out.write(resp);
        }catch(Exception e){
            e.printStackTrace();
            response.setContentType("text/plain");
            response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
            response.getWriter().write(e.getMessage());
            response.flushBuffer();
        }finally{
            out.close();
        }
    }

    private void obtenerIdDepProvDist(HttpServletRequest request, HttpServletResponse response)throws ServletException,IOException{
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        try{
            Map<String, String> mapUbigeo = normalizarDireccionService.getDataReniecXUbigeoInei(request.getParameter("ubigeoIneiSelect"));

            Map<String, Object> respJson = new HashMap<String, Object>();
            if(mapUbigeo != null){
            	respJson.put("depId", mapUbigeo.get("iddep"));
    			respJson.put("provId", mapUbigeo.get("idprov"));
    			respJson.put("distId", mapUbigeo.get("iddist"));
            }else{
            	respJson.put("depId", request.getParameter("depId"));
    			respJson.put("provId", request.getParameter("provId"));
    			respJson.put("distId", request.getParameter("distId"));
            }

            Gson gson = new Gson();
            String resp = gson.toJson(respJson);

            out.write(resp);
        }catch(Exception e){
            e.printStackTrace();
            response.setContentType("text/plain");
            response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
            response.getWriter().write(e.getMessage());
            response.flushBuffer();
        }finally{
            out.close();
        }
    }

    private int contarPalabras(String s) {
        int contador = 1, pos;
        s = s.trim();
        if (s.isEmpty()) {
            contador = 0;
        } else {
            pos = s.indexOf(" ");
            while (pos != -1) {
                contador++;
                pos = s.indexOf(" ", pos + 1);
            }
        }
        return contador;
    }

    private boolean palabrasConError(String s){
        String[] palabras = s.split(" ");

        int countPalabrasError = 0;
        for(int i=0; i<palabras.length; i++){
            String palabra = palabras[i];
            if(palabra.length() == 1){
                countPalabrasError++;
            }
        }

        if(countPalabrasError == palabras.length){
            return true;
        }else{
            return false;
        }
    }

    private String normalizarEspacios(String cadena) {
        int flag = 1;
        StringBuffer buffer = new StringBuffer("");
        int i = 0;
        int tempi;

        if(cadena != null){
            while (i < cadena.length()) {
                if (flag == 1) {
                    if (cadena.charAt(i) != ' ') {
                        buffer.append(cadena.charAt(i));
                        flag = 2;
                    }
                } else {
                    if (cadena.charAt(i) != ' ') {
                        buffer.append(cadena.charAt(i));
                    } else {
                        tempi = i + 1;
                        if (tempi < cadena.length()) {
                            if (cadena.charAt(tempi) != ' ') {
                                buffer.append(cadena.charAt(i));
                            }
                        }
                    }
                }
                i++;
            }
        }

        return buffer.toString();
    }

    private void getListDireccionNormalizada(HttpServletRequest request, HttpServletResponse response)throws ServletException,IOException{
    	response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        
        System.out.println("[NormalizarDireccionServlet][listarDirecciones], inicio");
        PrintWriter out = response.getWriter();
        try {
        	Type mapType = new TypeToken<Map<String, String>>(){}.getType();  
            Map<String, String> dataJ = new Gson().fromJson(request.getParameter("dataNeg"), mapType);
            
            DireccionJsonBean direccionJ = new DireccionJsonBean();
            direccionJ.setIdDireccion(new BigDecimal(dataJ.get("idDireccion")));
            direccionJ.setTipoDireccion(dataJ.get("tipoDireccion"));
            direccionJ.setCodigoCliente(dataJ.get("codigoCliente"));
            direccionJ.setUsuarioApp(dataJ.get("usuarioApp"));
            direccionJ.setNombreAplicacion(dataJ.get("nombreAplicacion"));
            direccionJ.setCodigoUnico(String.valueOf(System.currentTimeMillis()));
            direccionJ.setUbigeoId(new BigDecimal(dataJ.get("distId")));
            direccionJ.setNumeroPedido(dataJ.get("idOrden"));
            
            String direccion = MiUtil.valText(dataJ.get("direccion"));
            direccion = normalizarEspacios(direccion);

            int flag = 0;
        	int numOcurrencia = 0;
            String mensajeAlert = Constante.CADENA_VACIA;
            
            Map<String, Object> respJson = new HashMap<String, Object>();
            
            if(direccion.isEmpty() || contarPalabras(direccion) == 1 || palabrasConError(direccion)){
                mensajeAlert = normalizarDireccionService.getValueDescXTableAndValue(Constante.MENSAJE_ALERTA_NORMALIZACION,
                        Constante.MENSAJE_ALERTA_ADDRESS_INCORRECT);
            }else{
            	String ubigeoInei = normalizarDireccionService.getUbigeoIneiXUbigeoId(direccionJ.getUbigeoId());
            	
                if(ubigeoInei!=null && !ubigeoInei.trim().isEmpty()){
                	ubigeoInei = ubigeoInei.trim();
                    if(dataJ.get("referencia")==null || dataJ.get("referencia").isEmpty()){
                    	direccionJ.setDireccion(direccion);
                    }else{
                    	direccionJ.setDireccion(direccion + " REF " + dataJ.get("referencia"));
                    }
                	
                	Map<String, Object> mapResult = normalizarDireccionService.getDireccionNormalizada(direccionJ, ubigeoInei);

                	flag = (Integer)mapResult.get("flagGeo");
                	numOcurrencia = (Integer)mapResult.get("numOcurrencia");
                	       	
                	if(flag == Constante.FLAG_DIRECCION_GEOCODIFICADA ||
                			(flag == Constante.FLAG_DIRECCION_AMBIG_NO_GEOCODIFICADA && numOcurrencia > 0)){
                		List<DireccionNormWSBean> listBean = (ArrayList<DireccionNormWSBean>)mapResult.get("listaBean");
                		
                		DireccionNormWSBean firstObj = listBean.get(0);
                		
                		if(!ubigeoInei.equals(firstObj.getUbigeoInei())){
                			System.out.println("DIFERENTE - codigo INEI recibido: "+firstObj.getUbigeoInei());
                			Map<String, String> mapUbigeo = normalizarDireccionService.getDataReniecXUbigeoInei(firstObj.getUbigeoInei());
                			if(mapUbigeo != null){
                				respJson.put("depId", mapUbigeo.get("iddep"));
                    			respJson.put("provId", mapUbigeo.get("idprov"));
                    			respJson.put("distId", mapUbigeo.get("iddist"));
                			}else{
                				respJson.put("depId", dataJ.get("depId"));
                    			respJson.put("provId", dataJ.get("provId"));
                    			respJson.put("distId", dataJ.get("distId"));
                			}
                		}else{
                			respJson.put("depId", dataJ.get("depId"));
                			respJson.put("provId", dataJ.get("provId"));
                			respJson.put("distId", dataJ.get("distId"));
                		}
                		
                		respJson.put("lista", listBean);
                		
                	}if(flag == Constante.CONSULTA_EXCEPTION_TIMEOUT ||
                			flag == Constante.CONSULTA_EXCEPTION_DISPONIBILIDAD) {
                		String messageError = (String) mapResult.get("messageError");

                        mensajeAlert = normalizarDireccionService.getValueDescXTableAndValue(Constante.MENSAJE_ALERTA_NORMALIZACION,
                                Constante.MENSAJE_ALERTA_ERROR_RESPONSE);

                        mensajeAlert = mensajeAlert.replace("%n%", messageError);
                	}else{
                		mensajeAlert = (String)mapResult.get("motivoNoGeo");
                	}
                }else{
                	mensajeAlert = normalizarDireccionService.getValueDescXTableAndValue(Constante.MENSAJE_ALERTA_NORMALIZACION,
                            Constante.MENSAJE_ALERTA_UBIGEOINEI_VACIO);
                }
            }
            
            respJson.put("flag", flag);
        	respJson.put("numOcurrencia", numOcurrencia);
            respJson.put("mensajeAlert", mensajeAlert);

    		Gson gson = new Gson();
    		String resp = gson.toJson(respJson);
            out.write(resp);
            
        }catch(Exception e) {
        	e.printStackTrace();
        	response.setContentType("text/plain");
    		response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
    		
    		String mensajeAlert = normalizarDireccionService.getValueDescXTableAndValue(Constante.MENSAJE_ALERTA_NORMALIZACION,
        			Constante.MENSAJE_ALERTA_ERROR_RESPONSE);

            String subMensaje = normalizarDireccionService.getValueDescXTableAndValue(Constante.MENSAJE_SUB_ALERTA_NORMALIZACION,
                    Constante.MENSAJE_SUB_ALERTA_POR_DISPONIBILIDAD);

            mensajeAlert = mensajeAlert.replace("%n%", subMensaje);
    		
    		response.getWriter().write(mensajeAlert);
    		response.flushBuffer();
        }finally{
        	out.close();
        }
    }

    private void cargarPopupNormalizacionEdit(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
            throws Exception,ServletException,IOException {
        try {
            String datosNorm = request.getParameter("datosNorm");
            String url = "PopUpNormalizarEdit.jsp?datosNorm="+datosNorm;
            String winUrl = "POPUPNORMALIZACION/PopUpFrameNormalizar.jsp?av_url=" + url;
            out.println("window.open(\""+winUrl+"\", \"Normalizacion\",\"toolbar = no, location = no, directories = no, status = no, menubar = no, scrollbars = yes, resizable = yes, screenX = 60, top = 40, left = 60, screenY = 40, width = 710,height=410\"); ");
        }catch (Exception e){
            out.println("alert(\"Ocurrió un error al cargar el modulo de Normalización de direcciones\")");
        }
    }

    @Override
    protected void executeDefault(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}