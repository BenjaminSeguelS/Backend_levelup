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

    // CAMBIO IMPORTANTE: Tu IP pública
private static final String BASE_URL = "http://52.2.45.72";
    // 1. INICIAR PAGO
    @PostMapping("/create")
    public WebpayPlusTransactionCreateResponse startPayment(@RequestBody Map<String, Object> payload) {
        int amount = Integer.parseInt(payload.get("amount").toString());
        String buyOrder = "orden-" + new Random().nextInt(100000);
        String sessionId = "session-" + new Random().nextInt(100000);
        
        // CORREGIDO: URL de retorno apunta a tu IP pública (Nginx maneja el /api)
        String returnUrl = BASE_URL + "/api/webpay/commit"; 

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

    // 2. CONFIRMAR PAGO
    @RequestMapping(value = "/commit", method = {RequestMethod.GET, RequestMethod.POST})
    public RedirectView commitTransaction(@RequestParam("token_ws") String tokenWs) {
        try {
            WebpayPlus.Transaction tx = new WebpayPlus.Transaction(new WebpayOptions(
                IntegrationCommerceCodes.WEBPAY_PLUS, 
                IntegrationApiKeys.WEBPAY, 
                IntegrationType.TEST
            ));

            WebpayPlusTransactionCommitResponse response = tx.commit(tokenWs);

            // CORREGIDO: Redirigir al Frontend en producción (puerto 80, no 3000)
            if (response.getStatus().equals("AUTHORIZED")) {
                return new RedirectView(BASE_URL + "/compra-exitosa?status=success");
            } else {
                return new RedirectView(BASE_URL + "/compra-exitosa?status=rejected");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new RedirectView(BASE_URL + "/compra-exitosa?status=error");
        }
    }
}