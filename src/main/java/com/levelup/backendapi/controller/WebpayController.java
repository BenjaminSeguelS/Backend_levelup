package com.levelup.backendapi.controller;

// --- 1. AQUÍ ESTABA EL ERROR: Faltaba importar IntegrationApiKeys ---
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
        
        // URL donde Transbank devolverá al usuario (Backend)
        String returnUrl = "http://localhost:8080/api/webpay/commit"; 

        try {
            // Ahora "IntegrationApiKeys" funcionará porque ya está importado arriba
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
    // Aceptamos POST (lo que usa Transbank) y GET (por si acaso)
    @RequestMapping(value = "/commit", method = {RequestMethod.GET, RequestMethod.POST})
    public RedirectView commitTransaction(@RequestParam("token_ws") String tokenWs) {
        try {
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