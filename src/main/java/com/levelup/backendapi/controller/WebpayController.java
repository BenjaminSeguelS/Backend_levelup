package com.levelup.backendapi.controller;

import cl.transbank.common.IntegrationApiKeys;
import cl.transbank.common.IntegrationCommerceCodes;
import cl.transbank.common.IntegrationType;
import cl.transbank.webpay.common.WebpayOptions;
import cl.transbank.webpay.webpayplus.WebpayPlus;
import cl.transbank.webpay.webpayplus.responses.WebpayPlusTransactionCreateResponse;
import cl.transbank.webpay.webpayplus.responses.WebpayPlusTransactionCommitResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/api/webpay")
@CrossOrigin(origins = "*")
public class WebpayController {

    // 1. INICIAR PAGO
    @PostMapping("/create")
    public WebpayPlusTransactionCreateResponse startPayment(@RequestBody Map<String, Object> payload) {
        int amount = Integer.parseInt(payload.get("amount").toString());
        String buyOrder = "orden-" + new Random().nextInt(100000);
        String sessionId = "session-" + new Random().nextInt(100000);
        
        String returnUrl = "http://localhost:8080/api/webpay/commit"; 

        try {
            WebpayPlus.Transaction tx = new WebpayPlus.Transaction(new WebpayOptions(
                IntegrationCommerceCodes.WEBPAY_PLUS, 
                IntegrationApiKeys.WEBPAY, 
                IntegrationType.TEST
            ));

            return tx.create(buyOrder, sessionId, amount, returnUrl);
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 2. CONFIRMAR PAGO (Maneja Éxito, Rechazo y Anulación)
    @RequestMapping(value = "/commit", method = {RequestMethod.GET, RequestMethod.POST})
    public RedirectView commitTransaction(
            @RequestParam(value = "token_ws", required = false) String tokenWs,
            @RequestParam(value = "TBK_TOKEN", required = false) String tbkToken,
            @RequestParam(value = "TBK_ORDEN_COMPRA", required = false) String tbkOrdenCompra,
            @RequestParam(value = "TBK_ID_SESION", required = false) String tbkIdSesion) {
        
        try {
            // CASO 1: PAGO ANULADO (Usuario presionó "Anular" en el formulario Webpay)
            if (tokenWs == null && tbkToken != null) {
                // Redirigir DIRECTAMENTE a productos como pediste
                return new RedirectView("http://localhost:3000/productos");
            }

            // CASO 2: ERROR GENÉRICO (Ni token_ws ni tbkToken)
            if (tokenWs == null) {
                return new RedirectView("http://localhost:3000/compra-exitosa?status=error");
            }

            // CASO 3: FLUJO NORMAL (Confirmar transacción)
            WebpayPlus.Transaction tx = new WebpayPlus.Transaction(new WebpayOptions(
                IntegrationCommerceCodes.WEBPAY_PLUS, 
                IntegrationApiKeys.WEBPAY, 
                IntegrationType.TEST
            ));

            WebpayPlusTransactionCommitResponse response = tx.commit(tokenWs);

            if (response.getStatus().equals("AUTHORIZED")) {
                return new RedirectView("http://localhost:3000/compra-exitosa?status=success");
            } else {
                return new RedirectView("http://localhost:3000/compra-exitosa?status=rejected");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new RedirectView("http://localhost:3000/compra-exitosa?status=error");
        }
    }
}